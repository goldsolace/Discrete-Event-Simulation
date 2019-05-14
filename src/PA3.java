import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Programming Assignment 3 Main Class
 *
 * Reads in a file of polygons, stores them in a list, then stores them in a sorted list
 * and outputs both lists.
 *
 * @author Brice Purton
 * @studentID 3180044
 * @lastModified: 03-05-2019
 */

public class PA3 {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Please input 3 numbers for M N Qmax");
            return;
        }

        try {
            double M = Double.parseDouble(args[0]);
            double N = Double.parseDouble(args[1]);
            int Qmax = Integer.parseInt(args[2]);

            ProductionLine productionLine = new ProductionLine(M, N, Qmax).buildFromFile("src/ProductionTopology.xml");
            productionLine.startProduction();
            productionLine.printStatistics();
        } catch (NumberFormatException | ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
