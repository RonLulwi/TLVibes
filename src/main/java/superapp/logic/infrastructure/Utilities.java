package tlvibes.logic.infrastructure;

import java.nio.charset.Charset;
import java.util.Random;

public class Utilities {

	public static String GeneratingRandomString() {return GeneratingRandomString(8);}

	public static String GeneratingRandomString(int lenght) {
	    byte[] array = new byte[7]; // length is bounded by 7
	    new Random().nextBytes(array);
	    String generatedString = new String(array, Charset.forName("UTF-8"));
	    return generatedString;
	}
}
