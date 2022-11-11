/**
 * 
 */
package util.com.vilcart.util;

import java.io.FileInputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author win10
 *
 */
public class ReadPropertiesFile {

	public static Properties readPropertiesFile() {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream("FirstTestNG.properties");
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return prop;
	}
}
