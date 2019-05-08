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

    private final double MAX_TIME = 10000000;

    private final double M;
    private final double N;
    private final int QMAX;

    private Set<ProductionStage> stages = new LinkedHashSet<>();
    private Set<InterStageStorage> storages = new LinkedHashSet<>();

    private double time;

    public ProductionLine(double m, double n, int qMax) {
        M = m;
        N = n;
        QMAX = qMax;
        time = 0;
    }

    public void startProduction() {
        while (time < MAX_TIME) {

        }
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
        System.out.println(sb.toString());
    }


}
