package paralax.game.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Logs class. Writes logs to a file.
 *
 * @author Adrian Setniewski
 *
 */

public class Logs {

	private static boolean isDateSet = false;
	private static String fileName = null;

	public static void printLog(String line) {

		if (!isDateSet) {
			Date date = new Date();
			fileName = "logs.txt";
			isDateSet = true; 
			line = "Logging started: " + date.toString() + System.lineSeparator() + line; // /n seems not to work with windows notepad :(
		}

		try (FileWriter file = new FileWriter(fileName, true);
				BufferedWriter buffer = new BufferedWriter(file);
				PrintWriter print = new PrintWriter(buffer)) {
			print.println(line);
		} catch (IOException e) {
			
		}

	}
}
