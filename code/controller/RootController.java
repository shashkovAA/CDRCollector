package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.Properties;

import application.CdrFileSizeController;
import application.Debug;
import application.Main;
import application.UserProperties;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import objects.CdrRecord;
import objects.GlobalVars;
import objects.Paths;

public class RootController implements Runnable {

    @FXML
    private HomeBarController hBarController;
    @FXML
    private SelectBarController sBarController;
    @FXML
    private ConsoleBarController cBarController;
    @FXML
    private SqlBarController sqlBarController;

    @FXML
    private Label lblClientStatus;
    @FXML
    private ImageView imgStatus;
    @FXML
    private MenuItem mntExit;
    @FXML
    private MenuItem mntBackground;
    @FXML
    private Menu mnLogLevel;

    @FXML
    private RadioMenuItem rdmnFatal;
    @FXML
    private RadioMenuItem rdmnError;
    @FXML
    private RadioMenuItem rdmnWarning;
    @FXML
    private RadioMenuItem rdmnInfo;
    @FXML
    private RadioMenuItem rdmnDebug;

    private Properties userProperties = new Properties();
    private Image imgStop;
    private Image imgStart;
    private String autoSaveConfigTimeOut;
    private Main main;

    private CdrFileSizeController cdrFileSizeController;
    private final int MILLISECONDS_IN_ONE_MINUTE = 60000;

    @FXML
    public void initialize() {

	hBarController.init(this);
	sBarController.init(this);
	cBarController.init(this);
	sqlBarController.init(this);

	imgStart = new Image(getClass().getResourceAsStream("../sources/start.png"));
	imgStop = new Image(getClass().getResourceAsStream("../sources/stop.png"));
	imgStatus.setImage(imgStop);

	switch (Debug.getLogFileLevel()) {
	case "fatal":
	    rdmnFatal.setSelected(true);
	    break;
	case "error":
	    rdmnError.setSelected(true);
	    break;
	case "warn":
	    rdmnWarning.setSelected(true);
	    break;
	case "info":
	    rdmnInfo.setSelected(true);
	    break;
	case "debug":
	    rdmnDebug.setSelected(true);
	    break;
	}

	String cdrLogFileSize = getCdrLogFileSizeFromUserSettingsFile();
	System.setProperty("cdrLogFileSize", cdrLogFileSize);

	cdrFileSizeController = new CdrFileSizeController();
	cdrFileSizeController.init(this, cdrLogFileSize);

	createThread(cdrFileSizeController, GlobalVars.nameThreadForControlCdrFileSize);
	createThread(this, GlobalVars.nameThreadForAutoSaveConfig);

	if (hBarController.getEnableRunCdrCollectDbFlag() && hBarController.getValidValuePbxFieldsFlag()) {
	    hBarController.startCollector();
	}

    }

    public void createThread(Runnable runwork, String threadName) {
	Thread mThread = new Thread(runwork);
	mThread.setName(threadName);
	mThread.start();
	Debug.log.info("Create new Thread with name : " + threadName);
    }

    private String getCdrLogFileSizeFromUserSettingsFile() {
	Iterator it = Main.pbxProperties.keySet().iterator();
	String cdrLogFileSize = GlobalVars.defaultCdrLogFileSize;
	while (it.hasNext()) {
	    String key = it.next().toString();
	    if (key.equals(GlobalVars.cdrFileSize)) {
		cdrLogFileSize = Main.pbxProperties.getProperty(key);
		Debug.log.debug("Found cdrLogFileSize from userpreferrences.xml file . For  " + key + " value is "
			+ Main.pbxProperties.getProperty(GlobalVars.cdrFileSize));
	    }
	}
	return cdrLogFileSize;
    }

    @FXML
    public void handleExitDialog() {

	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Выход из программы W-Тарификатор");
	alert.setHeaderText("");
	alert.setContentText("Правда хотите выйти?");

	Optional<ButtonType> result = alert.showAndWait();

	if (result.get() == ButtonType.OK)
	    handleExit();
    }

    public void handleExit() {
	saveCurrentProtocolsMap();
	saveUserPreferrencesMap();
	cBarController.saveLoggingSettings();
	sqlBarController.saveSqlUserSettings();
	sqlBarController.closeDbConnection();
	Debug.log.info("Stop application.");
	System.exit(0);
    }

    @FXML
    private void handleBackground() {
	// main.hideProgrammToTray();
    }

    // ************************************************************************************************************
    // ===

    public void changeImageToStart() {
	Debug.log.debug("Change image to start.png");
	imgStatus.setImage(imgStart);
    }

    public void changeImageToStop() {
	Debug.log.debug("Change image to stop.png");
	imgStatus.setImage(imgStop);
    }

    public void saveCurrentProtocolsMap() {
	hBarController.saveCurrentProtocolsMap();
    }

    public void saveUserPreferrencesMap() {
	userProperties.setProperty(GlobalVars.namePortPbx1, hBarController.getSocketPortValue());
	userProperties.setProperty(GlobalVars.nameProtocolPbx1, hBarController.getNameProtocol());
	userProperties.setProperty(GlobalVars.namePbx1, hBarController.getNamePBX());
	userProperties.setProperty(GlobalVars.enableRunCdrCollectorFlag,
		String.valueOf(hBarController.getRunCdrCollectorFlag()));
	userProperties.setProperty(GlobalVars.autoSaveConfigTimeout, this.autoSaveConfigTimeOut);
	userProperties.setProperty(GlobalVars.ipAddressPbxForControl, hBarController.getIpAddressPbxForControl());

	if (System.getProperty("cdrLogFileSize").isEmpty())
	    userProperties.setProperty(GlobalVars.cdrFileSize, GlobalVars.defaultCdrLogFileSize);
	else
	    userProperties.setProperty(GlobalVars.cdrFileSize, System.getProperty("cdrLogFileSize"));

	UserProperties.savePropertiesXML(Paths.pbxUserSettingsXMLFilePathName, userProperties,
		"Custom user properties");
	Debug.log.info("Save pbx settings to file  " + Paths.viewOnlyPbxUserSettingsXMLFilePathName);
    }

    public void stopCollectorOnClose() {
	hBarController.stopCollector();
    }

    public void changeTxtFieldStatusClientConnected(String txtIp) {
	String date = new java.text.SimpleDateFormat("dd.MM").format(new java.util.Date());
	String time = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());

	lblClientStatus.setText("Подключен(" + txtIp + ") в " + time + " " + date);
    }

    public void changeTxtFieldStatusClientDisonnected() {
	lblClientStatus.setText("Нет подключения");
    }

    public void changeTxtFieldStatusClientWait() {
	lblClientStatus.setText("Ожидание подключения");
    }

    @Override
    public void run() {
	autoSaveConfigTimeOut = "60";
	Iterator it = Main.pbxProperties.keySet().iterator();

	while (it.hasNext()) {
	    String key = it.next().toString();
	    if (key.equals(GlobalVars.autoSaveConfigTimeout))
		autoSaveConfigTimeOut = Main.pbxProperties.getProperty(key);
	}
	while (true) {
	    sleepCurrentTread(autoSaveConfigTimeOut);
	    saveUserPreferrencesMap();
	}
    }

    private void sleepCurrentTread(String timeoutString) {
	try {
	    Thread.currentThread().sleep(convertStringToLong(autoSaveConfigTimeOut));
	} catch (InterruptedException except) {
	    Debug.log.error(except.getMessage());
	}
    }

    private long convertStringToLong(String timeoutString) {
	try {
	    return Long.valueOf(timeoutString) * MILLISECONDS_IN_ONE_MINUTE;
	} catch (NumberFormatException except) {
	    Debug.log.error(except.getMessage());
	    Debug.log.error("Set timeout for thread " + GlobalVars.nameThreadForControlCdrFileSize + " ms.");
	    return GlobalVars.defaultAutoSaveConfigTimeOut * MILLISECONDS_IN_ONE_MINUTE;
	}
    }

    @FXML
    private void setLogFatal() {
	Debug.setLogFileLevel("fatal");
    }

    @FXML
    private void setLogError() {
	Debug.setLogFileLevel("error");
    }

    @FXML
    private void setLogWarning() {
	Debug.setLogFileLevel("warn");
    }

    @FXML
    private void setLogInfo() {
	Debug.setLogFileLevel("info");
    }

    @FXML
    private void setLogDebug() {
	Debug.setLogFileLevel("debug");
    }

    @FXML
    private void openLogFolder() {
	try {
	    Desktop.getDesktop().open(new File(Paths.logDirPath));
	} catch (IOException except) {
	    Debug.log.error(except.getMessage());
	}
    }

    @FXML
    private void openCdrFolder() {
	try {
	    Desktop.getDesktop().open(new File(Paths.cdrDirPath));
	} catch (IOException except) {
	    Debug.log.error(except.getMessage());
	}
    }

    @FXML
    private void openLogFile() {
	File file = new File(Paths.logFilePathNameFull);

	try {
	    Desktop.getDesktop().edit(file);
	} catch (IOException except) {
	    Debug.log.error(except.getMessage());
	}
    }

    public void storeCdrRecordToDB(CdrRecord mCdrRecord) {
	sqlBarController.insertCdrRecordToDB(mCdrRecord);
    }

    public void init(Main main) {
	this.main = main;
    }

    public String getConnectionUrlToSqlServer() {
	return sqlBarController.getConnectionUrlToSqlServer();
    }

    public String getSqlServerLogin() {
	return sqlBarController.getSqlServerLogin();
    }

    public String getSqlServerPassword() {
	return sqlBarController.getSqlServerPassword();
    }

    public String getDbName() {
	return sqlBarController.getDbName();
    }

    public void setCdrFileNameForFileSizeController(String cdrLogFileName) {
	cdrFileSizeController.setCdrFileName(cdrLogFileName);
    }

    public Stage getPrimaryStage() {
	return main.getPrimaryStage();
    }

    public File getCdrFile() {
	return hBarController.getCdrFile();
    }

    public File getCdrFormattedFile() {
	return hBarController.getCdrFormattedFile();
    }

}
