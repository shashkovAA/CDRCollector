package objects;

public class GlobalVars {

    // *******************************************************************************************
    // =========Глобальные переменные для программы

    public static String sqlServerDataBaseName = "WTariffBase";

    public static String sqlServerCdrTableName = "Calls";

    public static String defaultCdrLogFileSize = "10 MB";

    public static int defaultAutoSaveConfigTimeOut = 60;

    public static String protocolFilesFieldNameValueSeparatorIn = "=";

    public static String defaultPortPbxCollector = "5000";

    public static String portPbxCollectorRegexValue = "[0-9]{4,5}";

    public static String protocolFilesTrail = ".ini";
    
    public static String protocolFilesXMLTrail = ".xml";

    // *******************************************************************************************
    // =========Переменные типа String для пользовательских настроек

    public static String namePbx1 = "namePbx1";

    public static String nameProtocolPbx1 = "nameProtocolPbx1";

    public static String namePortPbx1 = "namePortPbx1";

    public static String ipAddressPbxForControl = "ipAddressPbx";

    public static String enableRunCdrCollectorFlag = "enableRunCdrCollectorFlag";

    public static String cdrFileSize = "cdrFileSize";

    public static String logFileLevel = "logFileLevel";

    public static String logFileCount = "logFileCount";

    public static String logFileSize = "logFileSize";

    public static String logLevelConsole = "logLevelConsole";

    public static String logLevelAppConsole = "logLevelAppConsole";

    public static String autoSaveConfigTimeout = "autoSaveConfigTimeout";

    public static String sqlServerType = "sqlServerType";

    public static String sqlServerIp = "sqlServerIp";

    public static String sqlServerPort = "sqlServerPort";

    public static String sqlServerLogin = "sqlServerLogin";

    public static String sqlServerPassword = "sqlServerPassword";

    public static String enableSaveCdrToDBFlag = "enableSaveCdrToDB";

    public static String dateTimeFormat = "dd.MM.yyyy HH:mm";

    public static String nameThreadForSocketConnection = "MainCollectorThread";

    public static String nameThreadForPbxCollector = "";

    public static String nameThreadForAutoSaveConfig = "AutoSaveThread";

    public static String nameThreadForControlCdrFileSize = "CheckCdrLogFileSize";

}
