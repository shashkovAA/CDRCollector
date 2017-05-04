package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UserProperties {

    private static Properties properties;
    private static File file;

    public static void savePropertiesXML(File mFile, Properties propers, String myComments) {

	file = mFile;
	properties = propers;

	if (!file.exists())
	    createFile();

	storePropertiesToFile(myComments);
    }

    public static void savePropertiesXML(String fileName, Properties propers, String myComments) {

	file = new File(fileName);
	properties = propers;

	if (!file.exists())
	    createFile();

	storePropertiesToFile(myComments);
    }

    private static void createFile() {
	try {
	    file.createNewFile();
	} catch (IOException except) {
	    Debug.log.error(except.getMessage());
	}
    }

    private static void storePropertiesToFile(String comments) {
	try (FileOutputStream outputToFile = new FileOutputStream(file);) {
	    properties.storeToXML(outputToFile, comments);
	} catch (IOException e) {
	    Debug.log.error(e.getMessage());
	}
    }

    public static Properties getPropertiesFromXML(File file) {

	if (!file.exists())
	    return new Properties();
	else
	    return loadPropertiesFromXml(file);
    }

    public static Properties getPropertiesFromXML(String fileName) {

	File file = new File(fileName);

	if (!file.exists())
	    return new Properties();
	else
	    return loadPropertiesFromXml(file);
    }

    private static Properties loadPropertiesFromXml(File file) {
	Properties propers = new Properties();

	try (FileInputStream in = new FileInputStream(file);) {
	    propers.loadFromXML(in);
	} catch (FileNotFoundException e1) {
	    Debug.log.error(e1.getMessage());
	} catch (IOException e1) {
	    Debug.log.error(e1.getMessage());
	}
	return propers;

    }

}
