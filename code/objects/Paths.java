package objects;

import java.io.File;

public class Paths {

    /** Используется для переноса строк в текстовом файле */
    public static String lineSeparator = System.getProperty("line.separator");

    /** Родительский путь, где будут хранится файлы программы */
    public static String rootPath = new File("").getAbsolutePath();

    public static String rootFolderName = "CDRinits";

    public static String cdrFolderName = "cdr";

    public static String cdrFormattedFolderName = "formatted";

    public static String cdrFileExtension = ".log";

    public static String cdrFormattedFileExtension = ".csv";

    public static String protocolFoldeName = "protocols";

    public static String debugFolderName = "debug";

    public static String configFolderName = "config";

    public static String systemDirPath = rootPath + "//" + rootFolderName;

    public static String cdrDirPath = systemDirPath + "//" + cdrFolderName + "\\";

    public static String cdrDirFormattedPath = systemDirPath + "//" + cdrFolderName + "//" + cdrFormattedFolderName
	    + "\\";

    public static String protocolDirPath = systemDirPath + "//" + protocolFoldeName + "\\";

    public static String logDirPath = systemDirPath + "//" + debugFolderName + "\\";

    public static String configDirPath = systemDirPath + "//" + configFolderName + "\\";

    /** Расширение отдельно настраивается в log4j2.xml. */
    public static String logFilePathName = logDirPath + "debug";

    /** Используется для открытия файла через меню программы. */
    public static String logFilePathNameFull = logFilePathName + ".txt";

    public static String serFilePathName = systemDirPath + "\\currentprotocols.ser";

    public static String serCustFilePathName = systemDirPath + "\\userpreferrences.ser";

    public static String XMLFilePathName = systemDirPath + "\\userpreferrences.xml";

    public static String pbxUserSettingsXMLFilePathName = configDirPath + "\\pbxSettings.xml";

    public static String logUserSettingsXMLFilePathName = configDirPath + "\\loggingSettings.xml";

    public static String sqlUserSettingsXMLFilePathName = configDirPath + "\\sqlSettings.xml";

    public static String protocolUnformattedFileName = protocolDirPath + "unformatted.ini";

    public static String protocolCustomizedFileName = systemDirPath + "\\CustomizedFields.ini";

    // *******************************************************************************************
    // =========Переменные пути для наглядного отображения в логах журнала

    public static String viewOnlysystemDirPath = rootPath + "\\" + rootFolderName;

    public static String viewOnlycdrDirPath = viewOnlysystemDirPath + "\\" + cdrFolderName + "\\";

    public static String viewOnlycdrDirFormattedPath = viewOnlysystemDirPath + "\\" + cdrFolderName + "\\"
	    + cdrFormattedFolderName + "\\";

    public static String viewOnlyprotocolDirPath = viewOnlysystemDirPath + "\\" + protocolFoldeName;

    public static String viewOnlylogDirPath = viewOnlysystemDirPath + "\\" + debugFolderName;

    public static String viewOnlyConfigDirPath = viewOnlysystemDirPath + "\\" + configFolderName;

    public static String viewOnlyserFilePathName = viewOnlysystemDirPath + "\\currentprotocols.ser";

    public static String viewOnlyprotocolFileName = viewOnlyprotocolDirPath + "\\";

    public static String viewOnlyprotocolUnformattedFileName = viewOnlyprotocolDirPath + "\\unformatted.ini";

    public static String viewOnlyprotocolCustomizedFileName = viewOnlysystemDirPath + "\\CustomizedFields.ini";

    public static String viewOnlyserCustFilePathName = viewOnlysystemDirPath + "\\userpreferrences.ser";

    public static String viewOnlyXMLFilePathName = viewOnlysystemDirPath + "\\userpreferrences.xml";

    public static String viewOnlyPbxUserSettingsXMLFilePathName = viewOnlysystemDirPath + "\\" + configFolderName
	    + "\\pbxSettings.xml";

    public static String viewOnlyLogUserSettingsXMLFilePathName = viewOnlysystemDirPath + "\\" + configFolderName
	    + "\\loggingSettings.xml";

    public static String viewOnlySqlUserSettingsXMLFilePathName = viewOnlysystemDirPath + "\\" + configFolderName
	    + "\\sqlSettings.xml";

}
