import java.io.*;
import java.util.*;

public class Gen {
	/**
	 * Main Method
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
			sb.append("P");
			sb.append(" ");
			int max = 1000000;
			sb.append(max/2);
			sb.append(" ");
			for (int j = 0; j < max; j++) {
				sb.append(random.nextDouble()*100);
				sb.append(" ");
			}
			sb.append(System.lineSeparator());
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("file.dat"));
			writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		sb.setLength(0);
	}
}