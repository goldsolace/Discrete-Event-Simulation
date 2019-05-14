import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ProductionLine {

    private final double MAX_TIME = 10000000;// - 9900000;

    private double M;
    private double N;
    private int QMAX;
    private Random r;

    private EventScheduler eventScheduler;

    private Set<ProductionStage> stages;
    private Set<InterStageStorage> storages;
    private List<Item> stock;

    public ProductionLine(double mean, double range, int qMax) {
        M = mean;
        N = range;
        QMAX = qMax;
        r = new Random(5);
        eventScheduler = null;
        stages = new LinkedHashSet<>();
        storages = new LinkedHashSet<>();
        stock = new ArrayList<>();
    }

    private double generateDuration(int multiplier) {
        //if (true) return 1000;
        return M * multiplier + (N * multiplier * (r.nextDouble() - 0.5));
    }
    static int block = 0;

    public void startProduction() {
        System.out.println("START PRODUCTION");
        System.out.println(stages.size());
        eventScheduler = new EventScheduler(MAX_TIME, stages.size());
        Clock clock = eventScheduler.getClock();
        while (clock.getTime() < MAX_TIME) {

            if (eventScheduler.canSchedule()) {
                printStatus("SCHEDULE");
                for (ProductionStage stage : stages) {
                    Item item = stage.consume(clock.getTime());
                    if (item != null) {
                        //System.out.println(stage + " - PROCESSING");
                        try {
                            eventScheduler.schedule(new TimeEvent(stage, item, clock.getTime(), generateDuration(stage.getParallelism())));
                        }catch (Exception e) {
                            printStatus("THROW");
                            throw e;
                        }
                        //System.out.println("Item["+item.getId()+"] - SCHEDULED");
                    }
                }
            }

            if (eventScheduler.canExecute()) {
                TimeEvent timeEvent = eventScheduler.execute();
                printStatus("EXECUTE");
                if (timeEvent.getRemaining() > 0) {
                    eventScheduler.schedule(timeEvent);
                    continue;
                }

                timeEvent.setExitTime(clock.getTime());
                ProductionStage stage = timeEvent.getStage();

                //System.out.println("Item["+timeEvent.getItem().getId()+"] - FINISHED "+timeEvent.getStage());
                if (stage.getOutput() == null) {
                    //System.out.println("Item["+timeEvent.getItem().getId()+"] ---- COMPLETED");
                    stage.updateState(ProductionStage.State.IDLE, clock.getTime());
                    timeEvent.getItem().setExitTime(clock.getTime());
                    stock.add(timeEvent.getItem());
                } else if (stage.getOutput().isFull()) {
                    block++;
                    stage.updateState(ProductionStage.State.BLOCKED, clock.getTime());
                    System.out.println("BLOCKED ---- " + timeEvent);
                    eventScheduler.block(timeEvent);
                    if (block > 10) {
                        printStatus("THROW");
                        return;
                    }
                    printStatus("BLOCKED");
                } else {
                    block = 0;
                    stage.produce(timeEvent.getItem(), clock.getTime());
                    //System.out.println(clock.getTime() + " Finish");
                }
                printStatus("LOOP");
            }
        }

        for (ProductionStage stage : stages) {
            stage.updateState(ProductionStage.State.IDLE, clock.getTime());
        }
        printStatus("COMPLETE PRODUCTION");
        System.out.println("Stock: "+stock.size());
    }

    public void printStatus(String status) {
        if (!status.equals("THROW")) return;
        System.out.println("======== "+status+" STATUS ========");
        for (ProductionStage s : stages) {
            System.out.println(s);
        }
        for (InterStageStorage s : storages) {
            System.out.println(s);
        }
        System.out.println("Scheduled:");
        eventScheduler.printStatus();
    }

    /**
     * Parse an xml file describing the topology of a production line and build it.
     *
     * @param path of xml file
     * @return ProductionLine
     */
    public ProductionLine buildFromFile(String path) throws ParserConfigurationException, IOException, SAXException {
        Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new File(path));
        doc.getDocumentElement().normalize();

        HashMap<String, ProductionUnit> productionUnits = new HashMap<>();

        // Get list of stages
        NodeList stageNodes = doc.getElementsByTagName("ProductionStage");

        // Loop through stages
        for (int i = 0; i < stageNodes.getLength(); i++) {

            Node node = stageNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element stage = (Element) node;

                // Create station
                String name = stage.getElementsByTagName("Name").item(0).getTextContent();

                ProductionStage productionStage = getStage(productionUnits, name);

                if (productionStage == null) continue;

                NodeList predecessors = stage.getElementsByTagName("Predecessors");
                if (predecessors.getLength() > 0) {
                    Node sNode = predecessors.item(0);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element ePredecessors = (Element) sNode;
                        NodeList stages = ePredecessors.getElementsByTagName("Stage");
                        for (int j = 0; j < stages.getLength(); j++) {
                            productionStage.addPredecessor(getStage(productionUnits, stages.item(j).getTextContent()));
                        }
                    }
                }

                NodeList successors = stage.getElementsByTagName("Successors");
                if (successors.getLength() > 0) {
                    Node sNode = successors.item(0);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eSuccessors = (Element) sNode;
                        NodeList stages = eSuccessors.getElementsByTagName("Stage");
                        for (int j = 0; j < stages.getLength(); j++) {
                            productionStage.addSuccessor(getStage(productionUnits, stages.item(j).getTextContent()));
                        }
                    }
                }

                NodeList input = stage.getElementsByTagName("Input");
                if (input.getLength() > 0) {
                    InterStageStorage storage = getStorage(productionUnits, input.item(0).getTextContent());
                    if (storage == null) continue;
                    storages.add(storage);
                    productionStage.setInput(storage);
                } else {
                    // All stages except beginning
                    productionStage.updateState(ProductionStage.State.IDLE);
                }

                NodeList output = stage.getElementsByTagName("Output");
                if (output.getLength() > 0) {
                    InterStageStorage storage = getStorage(productionUnits, output.item(0).getTextContent());
                    if (storage == null) continue;
                    storages.add(storage);
                    productionStage.setOutput(storage);
                }

                stages.add(productionStage);
            }
        }
        return this;
    }

    private ProductionStage getStage(Map<String, ProductionUnit> map, String name) {
        ProductionUnit unit = map.computeIfAbsent(name, p -> new ProductionStage(name));
        if (unit instanceof ProductionStage) return (ProductionStage) unit;
        return null;
    }

    private InterStageStorage getStorage(Map<String, ProductionUnit> map, String name) {
        ProductionUnit unit = map.computeIfAbsent(name, p -> new InterStageStorage(name, QMAX));
        if (unit instanceof InterStageStorage) return (InterStageStorage) unit;
        return null;
    }

    public void printProductionLine() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stages.size(); i++) {
            for (ProductionStage s : stages) {
                if (s.getName().contains(""+i)) {
                    sb.append(s+" ");
                }
            }
            sb.append("\n");
        }
//        for (ProductionStage s : stages) {
//            if (s.getInput() != null && !s.getInput().getName().equals(last)) {
//                sb.append("\n");
//                last = s.getInput().getName();
//            }
//            sb.append(s+" ");
//        }

//        for (ProductionStage s : stages) {
//            if (s.getInput() != null)
//                sb.append(s.getInput()+"\n");
//            sb.append(s+"\n");
//            if (s.getOutput() != null)
//                sb.append(s.getOutput()+"\n");
//            sb.append("\n");
//        }
        //System.out.println(sb.toString());
    }


    public void printStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("PRODUCTION LINE STATISTICS\n");
        for (ProductionStage stage : stages) {
            sb.append(stage).append("\n");
            double total = 0D;
            for (ProductionStage.State key : ProductionStage.State.values()) {
                if (key == ProductionStage.State.IDLE) continue;
                double time = stage.getStateStats().getOrDefault(key, 0D);
                total += time;
                sb.append(key).append(" = ").append(String.format("%.2f", (time / MAX_TIME) * 100D)).append("\n");
            }
            //System.out.println(total);
        }
        for (InterStageStorage storage : storages) {
            sb.append(storage.getName()).append("\n");
            sb.append("Average Time: ").append(String.format("%.2f", storage.getCumulativeTime() / MAX_TIME)).append("\n");
            sb.append("Average Items: ").append(String.format("%.2f", storage.getAverageItems())).append("\n");
        }

        HashMap<String, Integer> itemsPerPath = new HashMap<>();
        for (Item item : stock) {
            String path = item.getPath();
            itemsPerPath.put(path, itemsPerPath.getOrDefault(path, 0) + 1);
        }
        itemsPerPath.forEach((k,v) -> System.out.println(k + " " + v));
        System.out.println(sb.toString());
    }
}
