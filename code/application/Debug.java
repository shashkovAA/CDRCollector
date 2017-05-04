package application;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import objects.GlobalVars;

public class Debug {
    public static Logger log;

    // ************************************************************************************************************
    // ===
    public static void initDebugLog(String logFilePath) {

	// ---«адаем начальные значени€, которые определ€тс€ если будут найдены
	// в конфиге

	System.setProperty("logFileName", logFilePath);
	System.setProperty("logFileLevel", "info");
	System.setProperty("logFileSize", "1 Mb");
	System.setProperty("logFileCount", "5");
	System.setProperty("logLevelConsole", "error");
	System.setProperty("logLevelAppConsole", "fatal");

	Iterator it = Main.loggingUserProperties.keySet().iterator();

	while (it.hasNext()) {
	    String key = it.next().toString();

	    if (key.equals(GlobalVars.logFileLevel))
		System.setProperty("logFileLevel", Main.loggingUserProperties.getProperty(key));

	    if (key.equals(GlobalVars.logFileSize))
		System.setProperty("logFileSize", Main.loggingUserProperties.getProperty(key));

	    if (key.equals(GlobalVars.logFileCount))
		System.setProperty("logFileCount", Main.loggingUserProperties.getProperty(key));

	    if (key.equals(GlobalVars.logLevelConsole))
		System.setProperty("logLevelConsole", Main.loggingUserProperties.getProperty(key));

	    if (key.equals(GlobalVars.logLevelAppConsole)) {
		System.setProperty("logLevelAppConsole", Main.loggingUserProperties.getProperty(key));
	    }
	}

	reconfigure();

	log = LogManager.getLogger(Debug.class.getName());
    }

    public static void reconfigure() {
	((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
    }

    public static String getLogFileLevel() {
	return System.getProperty("logFileLevel");
    }

    public static String getLogFileSize() {
	return System.getProperty("logFileSize");
    }

    public static String getLogFileCount() {
	return System.getProperty("logFileCount");
    }

    public static String getLogLevelConsole() {
	return System.getProperty("logLevelConsole");
    }

    public static String getLogLevelAppConsole() {
	return System.getProperty("logLevelAppConsole");
    }

    public static void setLogLevelAppConsole(String appLogLevel) {
	switch (appLogLevel.toLowerCase()) {
	case "fatal":
	    System.setProperty("logLevelAppConsole", "fatal");
	    reconfigure();
	    Debug.log.info("Set logging level for Application Console to 'Fatal'");
	    break;

	case "error":
	    System.setProperty("logLevelAppConsole", "error");
	    reconfigure();
	    Debug.log.info("Set logging level for Application Console to 'Error'");
	    break;

	case "warning":
	    System.setProperty("logLevelAppConsole", "warn");
	    reconfigure();
	    Debug.log.info("Set logging level for Application Console to 'Warning'");
	    break;

	case "info":
	    System.setProperty("logLevelAppConsole", "info");
	    reconfigure();
	    Debug.log.info("Set logging level for Application Console to 'Info'");
	    break;

	case "debug":
	    System.setProperty("logLevelAppConsole", "debug");
	    reconfigure();
	    Debug.log.info("Set logging level for Application Console to 'Debug'");
	    break;
	}
    }

    public static void setLogFileLevel(String fileLogLevel) {

	System.setProperty("logFileLevel", fileLogLevel.toLowerCase());
	reconfigure();
	Debug.log.info("Set  file logging level  to " + fileLogLevel.toUpperCase());
    }

}
