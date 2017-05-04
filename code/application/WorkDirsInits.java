package application;

import java.io.File;

import objects.Paths;

public class WorkDirsInits {
    /**
     * Проверка необходимых каталогов программы. Если программа стартует
     * впервые, то создаем их.
     */
    public static void checkExistDirectories() {

	createSystemDirIfNotExist();
	createLogDirIsNotExist();
	createCdrDirIfNotExist();
	createCdrFormattedDirIfNotExist();
	createProtocolsDirIfNotExist();
	createConfigDirIfNotExist();
    }

    private static void createSystemDirIfNotExist() {
	File systemDir = new File(Paths.systemDirPath);
	if (!systemDir.isDirectory())
	    systemDir.mkdirs();
    }

    private static void createLogDirIsNotExist() {
	File logDir = new File(Paths.logDirPath);
	if (!logDir.isDirectory()) {
	    logDir.mkdirs();
	    Debug.log.info("INitialization. First start programm.");
	    Debug.log.info("Create directory " + Paths.viewOnlysystemDirPath);
	    Debug.log.info("Create directory " + Paths.viewOnlylogDirPath);
	}

	Debug.initDebugLog(Paths.logFilePathName);
    }

    private static void createCdrDirIfNotExist() {
	File dirCDR = new File(Paths.cdrDirPath);
	if (!dirCDR.isDirectory()) {
	    dirCDR.mkdir();
	    Debug.log.info("Create directory " + Paths.viewOnlycdrDirPath);
	}
    }

    private static void createProtocolsDirIfNotExist() {
	File protocolsDir = new File(Paths.protocolDirPath);
	if (!protocolsDir.isDirectory()) {
	    protocolsDir.mkdir();
	    Debug.log.info("Create directory " + Paths.viewOnlyprotocolDirPath);
	    ProtocolFieldsInits.defaultInits();
	}
    }

    private static void createConfigDirIfNotExist() {
	File configDir = new File(Paths.configDirPath);
	if (!configDir.isDirectory()) {
	    configDir.mkdirs();
	    Debug.log.info("Create directory " + Paths.viewOnlyConfigDirPath);
	}
    }

    private static void createCdrFormattedDirIfNotExist() {
	File dirCdrFormatted = new File(Paths.cdrDirFormattedPath);
	if (!dirCdrFormatted.isDirectory()) {
	    dirCdrFormatted.mkdir();
	    Debug.log.info("Create directory " + Paths.viewOnlycdrDirFormattedPath);
	}

    }

}
