package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;

import application.Debug;
import application.Main;
import application.UserProperties;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import objects.CdrRecord;
import objects.Dialogs;
import objects.GlobalVars;
import objects.ImageForCallType;
import objects.JavaFXControls;
import objects.Paths;
import sql.SqlSet;

public class SqlBarController {

    @FXML
    private RootController rootController;
    @FXML
    private GridPane grdPane;
    @FXML
    private AnchorPane sqlBar;
    @FXML
    private ToggleSwitch tglOnOffSqlServerConnect;
    @FXML
    private ComboBox<String> cmbSqlServerType;
    @FXML
    private Button btnCheckSqlServerConnect;
    @FXML
    private CustomTextField txtSqlServerIp;
    @FXML
    private CustomTextField txtSqlServerLogin;
    @FXML
    private CustomTextField txtSqlServerPort;
    @FXML
    private CustomPasswordField txtSqlServerPassword;
    @FXML
    private Label lblSqlRecordsCount;

    // private String dbName = GlobalVars.sqlServerDataBaseName;

    private Connection activeConnection = null;

    private Statement activeStatement = null;

    private String sqlQuery = "";

    private ImageForCallType tempImageType;

    // private Object setProperty;

    public static Properties sqlUserProperties = new Properties();

    // ************************************************************************************************************
    // ===

    @FXML
    public void init(RootController rootCtrl) {
	this.rootController = rootCtrl;

    }

    // ************************************************************************************************************
    // ===Метод срабатывает один раз после загрузки контента FXML

    @FXML
    public void initialize() {
	boolean enableSaveCdrToDbFlag = false;

	JavaFXControls.setupClearButtonField(txtSqlServerIp);
	JavaFXControls.setupClearButtonField(txtSqlServerLogin);
	JavaFXControls.setupClearButtonField(txtSqlServerPort);

	tglOnOffSqlServerConnect.setDisable(true);
	btnCheckSqlServerConnect.setDisable(true);

	cmbSqlServerType.getItems().addAll("MS SQL Server", "Postgres SQL Sever", "MySql Server");
	cmbSqlServerType.getSelectionModel().select(0);
	checkComboBox();

	txtSqlServerIp.setText("");
	txtSqlServerPort.setText("");
	txtSqlServerLogin.setText("");
	txtSqlServerPassword.setText("");

	// --------Загружаем пользовательские настройки из properties, которые
	// подгружаются в методе Main.init

	Iterator it = Main.sqlUserProperties.keySet().iterator();

	while (it.hasNext()) {

	    String key = it.next().toString();

	    Debug.log.debug(key);

	    if (key.equals(GlobalVars.sqlServerType)) {
		cmbSqlServerType.getSelectionModel().select(Main.sqlUserProperties.getProperty(key));
		checkComboBox();
		Debug.log.debug("Found SqlServerType from sqlUserSettings.xml file . For  " + key + " value is "
			+ Main.sqlUserProperties.getProperty(key));
	    }

	    if (key.equals(GlobalVars.sqlServerIp)) {

		txtSqlServerIp.setText(Main.sqlUserProperties.getProperty(key));
		checkIPField();
		Debug.log.debug("Found  sqlServerIp from sqlUserSettings.xml file . For  " + key + " value is "
			+ Main.sqlUserProperties.getProperty(key));
	    }

	    if (key.equals(GlobalVars.sqlServerPort)) {

		txtSqlServerPort.setText(Main.sqlUserProperties.getProperty(key));
		checkPort();
		Debug.log.debug("Found  sqlServerPort from sqlUserSettings.xml file . For  " + key + " value is "
			+ Main.sqlUserProperties.getProperty(key));
	    }
	    if (key.equals(GlobalVars.sqlServerLogin)) {

		txtSqlServerLogin.setText(Main.sqlUserProperties.getProperty(key));
		checkLoginField();
		Debug.log.debug("Found  sqlServerLogin from sqlUserSettings.xml file . For  " + key + " value is "
			+ Main.sqlUserProperties.getProperty(key));
	    }

	    if (key.equals(GlobalVars.sqlServerPassword)) {

		txtSqlServerPassword.setText(Main.sqlUserProperties.getProperty(key));
		checkPasswordField();
		Debug.log.debug("Found  sqlServerPassword from sqlUserSettings.xml file . For  " + key + " value is "
			+ Main.sqlUserProperties.getProperty(key));
	    }

	    if (key.equals(GlobalVars.enableSaveCdrToDBFlag)) {
		enableSaveCdrToDbFlag = Boolean.valueOf(Main.sqlUserProperties.getProperty(key));
		Debug.log.debug("Found  enableSaveCdrToDbFlag from sqlUserSettings.xml file . For  " + key
			+ " value is " + Main.sqlUserProperties.getProperty(key));
	    }

	}

	checkStatement();

	if (checkStatement() && enableSaveCdrToDbFlag && checkSqlServerConnect()) {
	    Debug.log.info("Save CDR entries to DataBase is enabled");
	    tglOnOffSqlServerConnect.setDisable(false);
	    tglOnOffSqlServerConnect.setSelected(true);
	    startSaveCdrToDB();
	    setUISqlServerSettingsDisable();
	    lblSqlRecordsCount.setText(getRecordsCount());
	}

    }
    // ************************************************************************************************************
    // ===Event Listener on
    // ToggleSwitch[#tglOnOffSqlServerConnect].onMouseClicked

    @FXML
    public void onOffSqlServerConnect(MouseEvent event) {
	if (tglOnOffSqlServerConnect.isSelected()) {
	    startSaveCdrToDB();
	    setUISqlServerSettingsDisable();
	    lblSqlRecordsCount.setText(getRecordsCount());
	} else {
	    stopSaveCdrToDB();
	    setUISqlServerSettingsEnable();
	}

    }

    // -----------------------------------------------------
    private void setUISqlServerSettingsEnable() {
	txtSqlServerIp.setDisable(false);
	txtSqlServerPort.setDisable(false);
	txtSqlServerLogin.setDisable(false);
	txtSqlServerPassword.setDisable(false);
	cmbSqlServerType.setDisable(false);
	btnCheckSqlServerConnect.setDisable(false);

    }

    // ------------------------------------------------------
    private void setUISqlServerSettingsDisable() {
	txtSqlServerIp.setDisable(true);
	txtSqlServerPort.setDisable(true);
	txtSqlServerLogin.setDisable(true);
	txtSqlServerPassword.setDisable(true);
	cmbSqlServerType.setDisable(true);
	btnCheckSqlServerConnect.setDisable(true);

    }

    // ************************************************************************************************************
    // ===Метод проверки доступа SQL сервера по нажатию на кнопку "Проверить
    // соединение"

    @FXML
    public void btnClickCheckSqlServerConnect(ActionEvent event) {

	if (checkSqlServerConnect()) {
	    Dialogs.showInfoDialog("Доступность " + cmbSqlServerType.getSelectionModel().getSelectedItem(), null,
		    "Сервер доступен!");
	    tglOnOffSqlServerConnect.setDisable(false);

	} else {
	    Dialogs.showErrorDialog("Доступность " + cmbSqlServerType.getSelectionModel().getSelectedItem(), null,
		    SqlSet.eventException);
	    tglOnOffSqlServerConnect.setDisable(true);

	}

    }

    // ************************************************************************************************************
    // ===
    private boolean checkSqlServerConnect() {
	return SqlSet.TestConnection(getSqlServerType(), getSqlServerIP(), getSqlServerPort(), getSqlServerLogin(),
		getSqlServerPassword());
    }

    // ************************************************************************************************************
    // ===Геттер для типа Sql сервера

    public String getSqlServerType() {
	String sqlServerType = "";

	switch (cmbSqlServerType.getSelectionModel().getSelectedItem()) {
	case "MS SQL Server":

	    sqlServerType = "sqlserver";
	    break;

	case "Postgres SQL Sever":

	    sqlServerType = "postgresql";
	    break;

	case "MySql Server":

	    sqlServerType = "mysql";
	    break;
	}

	return sqlServerType;
    }

    // ************************************************************************************************************
    // ===
    public String getSqlServerIP() {
	return txtSqlServerIp.getText().trim();
    }

    // ************************************************************************************************************
    // ===
    public String getSqlServerPort() {
	return txtSqlServerPort.getText().trim();
    }

    // ************************************************************************************************************
    // ===
    public String getSqlServerLogin() {
	return txtSqlServerLogin.getText().trim();
    }

    // ************************************************************************************************************
    // ===
    public String getSqlServerPassword() {
	return txtSqlServerPassword.getText().trim();
    }

    // ************************************************************************************************************
    // ===
    public String getDbName() {
	return GlobalVars.sqlServerDataBaseName;
    }

    // ************************************************************************************************************
    // ===
    @FXML
    private void cmbChangeValue(ActionEvent event) {
	if (cmbSqlServerType.getSelectionModel().getSelectedItem().isEmpty())
	    cmbSqlServerType.setStyle(Main.colorNotAvalable);

	else
	    cmbSqlServerType.setStyle(Main.colorAvalable);

	checkStatement();
    }
    // -------------------------------------------------------------------------------------------------------------

    private void checkComboBox() {
	if (cmbSqlServerType.getSelectionModel().getSelectedItem().isEmpty())
	    cmbSqlServerType.setStyle(Main.colorNotAvalable);

	else
	    cmbSqlServerType.setStyle(Main.colorAvalable);

	checkStatement();

    }

    // ************************************************************************************************************
    // ===
    private String getRecordsCount() {
	Connection connect = null;
	Statement statement = null;
	ResultSet result = null;
	int num;
	String sqlQuery;
	String connectionUrl = "jdbc:" + getSqlServerType() + "://" + getSqlServerIP() + ":" + getSqlServerPort()
		+ ";dataBaseName=" + GlobalVars.sqlServerDataBaseName;

	try {

	    // Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); //
	    // Написал для порядка. Для драйвера типа 4 - это не обязательно!

	    connect = DriverManager.getConnection(connectionUrl, getSqlServerLogin(), getSqlServerPassword());

	    sqlQuery = "select	COUNT(*) from [dbo].[Calls]";

	    statement = connect.createStatement();

	    result = statement.executeQuery(sqlQuery);

	    result.next();

	    num = result.getInt(1);

	    result.close();

	    statement.close();

	    connect.close();

	    return String.valueOf(num);

	} catch (SQLException except) {
	    Debug.log.error(except.getMessage());
	    return "---";
	}

    }

    // ************************************************************************************************************
    // ===

    // -------------------------------------------------------------------------------------------------------------
    @FXML
    private void txtIpkeyRelesed(KeyEvent event) {
	checkIPField();
    }

    // ------------------------------------------------------------------------------------------------------------
    @FXML
    private void txtIpclickMouse(MouseEvent event) {
	checkIPField();
    }

    // ************************************************************************************************************
    // ===Проверяет регулярным выражением содержимое поля на соответствие IPv4

    private void checkIPField() {
	String textIp = txtSqlServerIp.getText();

	if (textIp.matches("((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)")) {
	    txtSqlServerIp.setStyle(Main.colorAvalable);
	    checkStatement();
	} else
	    txtSqlServerIp.setStyle(Main.colorNotAvalable);

	checkStatement();
    }

    // ************************************************************************************************************
    // ===Ограничение на ввод в поле Порт только цифр, не начинающихся с нуля и
    // длиной не более 5 символов.

    @FXML
    private void txtIpkeyTyped(KeyEvent event) {

	Debug.log.debug("Event source :" + event.getSource() + " Event type :" + event.getEventType()
		+ " Event character :" + event.getCharacter());

	if ((txtSqlServerIp.getText().length() < 15) && (event.getCharacter().equals("1")
		|| event.getCharacter().equals("2") || event.getCharacter().equals("3")
		|| event.getCharacter().equals("4") || event.getCharacter().equals("5")
		|| event.getCharacter().equals("6") || event.getCharacter().equals("7")
		|| event.getCharacter().equals("8") || event.getCharacter().equals("9")
		|| event.getCharacter().equals("0") || event.getCharacter().equals("."))) {
	    // =============Проверка на введение только одного первого нуля
	    if (txtSqlServerIp.getText().equals("") && (event.getCharacter().equals("0")))
		event.consume();
	    // =============Проверка на введение первым символом "точки"
	    if (txtSqlServerIp.getText().equals("") && (event.getCharacter().equals(".")))
		event.consume();
	    // =============Проверка на введение подряд более одного символа
	    // "точки"
	    if (event.getCharacter().equals("."))
		if (txtSqlServerIp.getText().endsWith("."))
		    event.consume();
	    Debug.log.debug("event.getCharacter() " + event.getCharacter());

	} else
	    event.consume();

    }

    // ************************************************************************************************************
    // ===
    @FXML
    private void txtPortKeyTyped(KeyEvent event) {

	if ((txtSqlServerPort.getText().length() < 5)
		&& (event.getCharacter().equals("1") || event.getCharacter().equals("2")
			|| event.getCharacter().equals("3") || event.getCharacter().equals("4")
			|| event.getCharacter().equals("5") || event.getCharacter().equals("6")
			|| event.getCharacter().equals("7") || event.getCharacter().equals("8")
			|| event.getCharacter().equals("9") || event.getCharacter().equals("0"))) {
	    if (txtSqlServerPort.getText().equals("") && (event.getCharacter().equals("0")))
		event.consume(); // =============Проверка на введение только
				 // одного первого нуля

	} else
	    event.consume();
    }

    // ------------------------------------------------
    @FXML
    private void txtPortKeyReleased() {
	checkPort();
    }

    // ------------------------------------------------
    @FXML
    private void txtPortclickMouse(MouseEvent event) {
	checkPort();
    }

    // -------------------------------------------------
    private void checkPort() {
	if (!txtSqlServerPort.getText().isEmpty())
	    txtSqlServerPort.setStyle(Main.colorAvalable);

	else
	    txtSqlServerPort.setStyle(Main.colorNotAvalable);

	checkStatement();
    }

    // ************************************************************************************************************
    // ==
    @FXML
    private void txtLoginKeyReleased(KeyEvent event) {
	checkLoginField();
    }

    // --------------------------------------------------
    @FXML
    private void txtLoginMouseClick(MouseEvent event) {
	checkLoginField();
    }

    // ************************************************************************************************************
    // ===
    private void checkLoginField() {
	if (!txtSqlServerLogin.getText().isEmpty()) {
	    txtSqlServerLogin.setStyle(Main.colorAvalable);

	} else
	    txtSqlServerLogin.setStyle(Main.colorNotAvalable);

	checkStatement();
    }

    // ************************************************************************************************************
    // ===
    @FXML
    private void txtPasswordKeyReleased(KeyEvent event) {
	if (!txtSqlServerPassword.getText().isEmpty())
	    txtSqlServerPassword.setStyle(Main.colorAvalable);

	else
	    txtSqlServerPassword.setStyle(Main.colorNotAvalable);

	checkStatement();
    }

    // ************************************************************************************************************
    // ===
    private void checkPasswordField() {
	if (!txtSqlServerPassword.getText().isEmpty())
	    txtSqlServerPassword.setStyle(Main.colorAvalable);

	else
	    txtSqlServerPassword.setStyle(Main.colorNotAvalable);

    }

    private boolean checkStatement() {

	if ((txtSqlServerIp.getText()
		.matches("((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)"))
		&& !txtSqlServerPort.getText().isEmpty()
		&& !cmbSqlServerType.getSelectionModel().getSelectedItem().isEmpty()
		&& !txtSqlServerLogin.getText().isEmpty() && !txtSqlServerPassword.getText().isEmpty()) {
	    btnCheckSqlServerConnect.setDisable(false);
	    return true;
	} else {
	    btnCheckSqlServerConnect.setDisable(true);
	    return false;
	}
    }

    private void startSaveCdrToDB() {

	if (getSqlServerType().equals("sqlserver"))

	    if (isExistDB(GlobalVars.sqlServerDataBaseName)) {
		isExistCDRTable();
	    }
    }

    private void stopSaveCdrToDB() {
	Debug.log.info("stopSave CDR is work!");
	closeDbConnection();
    }

    private void sqlExceptionHandle() {
	if (tglOnOffSqlServerConnect.isSelected()) {
	    tglOnOffSqlServerConnect.setSelected(false);
	}

    }

    public String getConnectionUrlToSqlServer() {
	return "jdbc:" + getSqlServerType() + "://" + getSqlServerIP() + ":" + getSqlServerPort();
    }

    /** */

    private boolean isExistDB(String mDbName) {
	Connection connect = null;
	Statement statement = null;
	ResultSet result = null;
	boolean flagBaseExist = false;
	String sqlQuery;

	try {
	    // Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); //
	    // Написал для порядка. Для драйвера типа 4 - это не обязательно!

	    connect = DriverManager.getConnection(getConnectionUrlToSqlServer(), getSqlServerLogin(),
		    getSqlServerPassword());
	    // Универсальынй платформо-независимый способ проверки каталогов
	    DatabaseMetaData meta = connect.getMetaData();
	    result = meta.getCatalogs();

	    while (result.next()) {
		String BaseNames = result.getString("TABLE_CAT");
		if (BaseNames.equals(mDbName))
		    flagBaseExist = true;
	    }

	    if (flagBaseExist == true)
		Debug.log.warn("DataBase " + mDbName + " is already exist.");
	    else {
		Debug.log.warn("Data base " + mDbName + " is not exist. Creating DataBase...");
		connect.createStatement().executeQuery("CREATE DATABASE [" + mDbName + "]");
	    }

	} catch (SQLException se) {
	    if (se.getErrorCode() != 0) {
		Debug.log.error("SQLError: " + se.getMessage() + " code: " + se.getErrorCode());
		Dialogs.showErrorDialog("Создание БД WTariffBase", null,
			"SQLError: " + se.getMessage() + " code: " + se.getErrorCode());
		sqlExceptionHandle();
		return false;
	    }

	} catch (Exception e) {
	    Debug.log.error(e.getMessage());
	    Dialogs.showErrorDialog("Создание БД WTariffBase", null, e.getMessage());
	    sqlExceptionHandle();
	    return false;

	} finally {
	    // clean up the system resources
	    try {
		if (result != null)
		    result.close();
		if (statement != null)
		    statement.close();
		if (connect != null)
		    connect.close();
	    } catch (Exception e) {
		Debug.log.error(e.getMessage());

	    }
	}
	return true;
    }

    /**
     * Метод инициализации коннекта к базе данных
     * %GlobalVars.sqlServerDataBaseName%
     * 
     * @return Connection
     */
    private Connection createConnectionToDB() {
	try {
	    Connection connect = DriverManager.getConnection(
		    getConnectionUrlToSqlServer() + ";dataBaseName=" + GlobalVars.sqlServerDataBaseName,
		    getSqlServerLogin(), getSqlServerPassword());
	    return connect;
	} catch (SQLException except) {
	    Debug.log.error(except.getMessage());
	    return null;
	}
    }

    /**
     * Метод для проверки существования таблицы
     * %GlobalVars.sqlServerCdrTableName% в БД
     * 
     */
    private boolean isExistCDRTable() {
	boolean existCDRTableFlag = false;
	try {
	    activeConnection = createConnectionToDB();
	    activeStatement = activeConnection.createStatement();
	    ResultSet result = activeStatement.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES ");

	    while (result.next()) {
		if (result.getString("TABLE_NAME").equals(GlobalVars.sqlServerCdrTableName)) {
		    existCDRTableFlag = true;
		    Debug.log.info("Table  " + GlobalVars.sqlServerCdrTableName + " is exist.");
		}
	    }

	    if (!existCDRTableFlag) {
		Debug.log.warn("Table  " + GlobalVars.sqlServerCdrTableName + " is not exist.");
		Debug.log.warn("Create Table " + GlobalVars.sqlServerCdrTableName + " in DataBase "
			+ GlobalVars.sqlServerDataBaseName + "!");
		activeStatement.executeUpdate(createSqlQueryAddTable());
	    }
	} catch (SQLException sqlEvt) {
	    Debug.log.error(sqlEvt.getMessage());
	    Dialogs.showErrorDialog("Создание таблицы " + GlobalVars.sqlServerCdrTableName + "в WTariffBase", null,
		    sqlEvt.getMessage());
	    sqlExceptionHandle();
	    return false;
	}

	return true;
    }

    private String createSqlQueryAddTable() {
	return " CREATE TABLE Calls " + "(callId varchar(20) NULL, " + "date varchar(20) NULL,"
		+ "time varchar(20) NULL, " + "duration varchar(20) NULL," + "calling_num varchar(30) NULL,"
		+ "dialed_num varchar(30) NULL," + "direction varchar(20) NULL," + "acct_code  varchar(20) NULL,"
		+ "attd_console varchar(20) NULL," + "auth_code varchar(20) NULL," + "bandwidth varchar(20) NULL,"
		+ "bcc varchar(20) NULL," + "calltype varchar(20) NULL," + "clg_num_in_tac varchar(20) NULL,"
		+ "clg_pty_cat varchar(20) NULL," + "code_dial varchar(20) NULL," + "code_used varchar(20) NULL,"
		+ "cond_code varchar(20) NULL," + "contract_uri varchar(20) NULL," + "end_date_4d varchar(20) NULL,"
		+ "end_time varchar(20) NULL," + "feat_flag varchar(20) NULL," + "frl varchar(20) NULL,"
		+ "from_uri varchar(20) NULL," + "in_crt_id varchar(20) NULL," + "in_trk_code varchar(20) NULL,"
		+ "ins varchar(20) NULL," + "isdn_cc varchar(20) NULL," + "ixc_code varchar(20) NULL,"
		+ "ma_uui varchar(20) NULL," + "node_num varchar(20) NULL," + "out_crt_id varchar(20) NULL,"
		+ "ppm varchar(20) NULL," + "request_uri varchar(20) NULL," + "res_flag varchar(20) NULL,"
		+ "secduration varchar(20) NULL," + "start_date varchar(20) NULL," + "start_date_4d varchar(20) NULL,"
		+ "start_time varchar(20) NULL," + "to_uri varchar(20) NULL," + "tsc_ct varchar(20) NULL,"
		+ "tsc_flag varchar(20) NULL," + "vdn varchar(20) NULL," + "pbxName varchar(20) NULL,"
		+ "timestamp datetime NULL," + "durationSeconds int NULL," + "image varchar(20) NULL)";
    }

    @FXML
    public void insertCdrRecordToDB(CdrRecord mCdrRecord) {
	tempImageType = (ImageForCallType) mCdrRecord.getImageType();

	if (tglOnOffSqlServerConnect.isDisabled()) {
	    Debug.log.info("Storing CDR data in DataBase is not enable.");
	    Debug.log.info("Record with Id = " + mCdrRecord.getId() + " inserted to formatted file.");
	    insertCdrRecordToFormattedTempFile(mCdrRecord);

	} else {
	    try {
		if (activeConnection.isClosed())
		    activeConnection = createConnectionToDB();

		PreparedStatement prepStatement = activeConnection
			.prepareStatement(createSqlQueryInsertRow(mCdrRecord));
		int insertedRowCount = prepStatement.executeUpdate();

		if (insertedRowCount > 0) {
		    Debug.log.debug("rowCount = " + insertedRowCount);
		    Debug.log.info("Cdr record with Id = " + mCdrRecord.getId() + " insert in to DataBase.");
		    setLabelIncrementCdrRecords();
		} else {
		    Debug.log.warn("Cdr record with Id = " + mCdrRecord.getId() + " inserted to formatted file.");
		    insertCdrRecordToFormattedTempFile(mCdrRecord);
		}

	    } catch (SQLException except) {
		Debug.log.error(except.getMessage());
		Debug.log.warn("Cdr record with Id = " + mCdrRecord.getId() + " inserted to formatted file.");
		insertCdrRecordToFormattedTempFile(mCdrRecord);
	    }
	}
    }

    private String createSqlQueryInsertRow(CdrRecord mCdrRecord) {
	return "INSERT INTO Calls values(" + mCdrRecord.getId() + ",'" + mCdrRecord.getDate() + "','"
		+ mCdrRecord.getTime() + "','" + mCdrRecord.getDuration() + "','" + mCdrRecord.getCalling_num().trim()
		+ "','" + mCdrRecord.getDialed_num().trim() + "','" + mCdrRecord.getDirection().trim() + "','"
		+ mCdrRecord.getAcct_code().trim() + "','" + mCdrRecord.getAttd_console().trim() + "','"
		+ mCdrRecord.getAuth_code().trim() + "','" + mCdrRecord.getBandwidth().trim() + "','"
		+ mCdrRecord.getBcc().trim() + "','" + mCdrRecord.getCalltype().trim() + "','"
		+ mCdrRecord.getClg_num_in_tac().trim() + "','" + mCdrRecord.getClg_pty_cat().trim() + "','"
		+ mCdrRecord.getCode_dial().trim() + "','" + mCdrRecord.getCode_used().trim() + "','"
		+ mCdrRecord.getCond_code().trim() + "','" + mCdrRecord.getContact_uri().trim() + "','"
		+ mCdrRecord.getEnd_date_4d().trim() + "','" + mCdrRecord.getEnd_time().trim() + "','"
		+ mCdrRecord.getFeat_flag().trim() + "','" + mCdrRecord.getFrl().trim() + "','"
		+ mCdrRecord.getFrom_uri().trim() + "','" + mCdrRecord.getIn_crt_id().trim() + "','"
		+ mCdrRecord.getIn_trk_code().trim() + "','" + mCdrRecord.getIns().trim() + "','"
		+ mCdrRecord.getIsdn_cc().trim() + "','" + mCdrRecord.getIxc_code().trim() + "','"
		+ mCdrRecord.getMa_uui().trim() + "','" + mCdrRecord.getNode_num().trim() + "','"
		+ mCdrRecord.getOut_crt_id().trim() + "','" + mCdrRecord.getPpm().trim() + "','"
		+ mCdrRecord.getRequest_uri().trim() + "','" + mCdrRecord.getRes_flag().trim() + "','"
		+ mCdrRecord.getSec_dur().trim() + "','" + mCdrRecord.getStart_date().trim() + "','"
		+ mCdrRecord.getStart_date_4d().trim() + "','" + mCdrRecord.getStart_time().trim() + "','"
		+ mCdrRecord.getTo_uri().trim() + "','" + mCdrRecord.getTsc_ct().trim() + "','"
		+ mCdrRecord.getTsc_flag().trim() + "','" + mCdrRecord.getVdn().trim() + "','"
		+ mCdrRecord.getPbxName().trim() + "', CONVERT(DATETIME,'"
		+ getFormattedTimestampString(mCdrRecord.getTimestampString()) + "', 120), '"
		+ mCdrRecord.getDurationSeconds() + "','" + tempImageType.getFileImageTypeName() + "')";
    }

    private void insertCdrRecordToFormattedTempFile(CdrRecord mCdrRecord) {
	try (BufferedWriter bufferOut = new BufferedWriter(
		new FileWriter(rootController.getCdrFormattedFile(), true));) {
	    bufferOut.write(createInsertedRowToFormattedCdrFile(mCdrRecord) + Paths.lineSeparator);
	    bufferOut.flush();
	    bufferOut.close();

	} catch (IOException except) {
	    Debug.log.error(except.getMessage());
	}
    }

    private String createInsertedRowToFormattedCdrFile(CdrRecord mCdrRecord) {
	return mCdrRecord.getId() + "," + mCdrRecord.getDate() + "," + mCdrRecord.getTime() + ","
		+ mCdrRecord.getDuration() + "," + mCdrRecord.getCalling_num().trim() + ","
		+ mCdrRecord.getDialed_num().trim() + "," + mCdrRecord.getDirection().trim() + ","
		+ mCdrRecord.getAcct_code().trim() + "," + mCdrRecord.getAttd_console().trim() + ","
		+ mCdrRecord.getAuth_code().trim() + "," + mCdrRecord.getBandwidth().trim() + ","
		+ mCdrRecord.getBcc().trim() + "," + mCdrRecord.getCalltype().trim() + ","
		+ mCdrRecord.getClg_num_in_tac().trim() + "," + mCdrRecord.getClg_pty_cat().trim() + ","
		+ mCdrRecord.getCode_dial().trim() + "," + mCdrRecord.getCode_used().trim() + ","
		+ mCdrRecord.getCond_code().trim() + "," + mCdrRecord.getContact_uri().trim() + ","
		+ mCdrRecord.getEnd_date_4d().trim() + "," + mCdrRecord.getEnd_time().trim() + ","
		+ mCdrRecord.getFeat_flag().trim() + "," + mCdrRecord.getFrl().trim() + ","
		+ mCdrRecord.getFrom_uri().trim() + "," + mCdrRecord.getIn_crt_id().trim() + ","
		+ mCdrRecord.getIn_trk_code().trim() + "," + mCdrRecord.getIns().trim() + ","
		+ mCdrRecord.getIsdn_cc().trim() + "," + mCdrRecord.getIxc_code().trim() + ","
		+ mCdrRecord.getMa_uui().trim() + "," + mCdrRecord.getNode_num().trim() + ","
		+ mCdrRecord.getOut_crt_id().trim() + "," + mCdrRecord.getPpm().trim() + ","
		+ mCdrRecord.getRequest_uri().trim() + "," + mCdrRecord.getRes_flag().trim() + ","
		+ mCdrRecord.getSec_dur().trim() + "," + mCdrRecord.getStart_date().trim() + ","
		+ mCdrRecord.getStart_date_4d().trim() + "," + mCdrRecord.getStart_time().trim() + ","
		+ mCdrRecord.getTo_uri().trim() + "," + mCdrRecord.getTsc_ct().trim() + ","
		+ mCdrRecord.getTsc_flag().trim() + "," + mCdrRecord.getVdn().trim() + ","
		+ mCdrRecord.getPbxName().trim() + "," + getFormattedTimestampString(mCdrRecord.getTimestampString())
		+ "," + String.valueOf(mCdrRecord.getDurationSeconds()) + "," + tempImageType.getFileImageTypeName()
		+ ";";
    }

    /**
     * Метод для получения форматированного timestamp-a
     * 
     * @param mTimeStampString
     *            в формате CRDrecord.getDate() + " " + CDRrecord.getTime()
     * @return TimeStampString в формате yyyy-MM-dd HH:mm:ss.S
     */
    private String getFormattedTimestampString(String mTimeStampString) {
	Date date = new Date();
	SimpleDateFormat format = new SimpleDateFormat(GlobalVars.dateTimeFormat);

	try {
	    date = format.parse(mTimeStampString);
	} catch (ParseException except) {
	    Debug.log.error(except.getMessage());

	}

	Timestamp timestamp = new Timestamp(date.getTime());
	Debug.log.info("Временной timestamp для cdr записи :" + timestamp.toString());

	return timestamp.toString();
    }

    private void setLabelIncrementCdrRecords() {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		if (!lblSqlRecordsCount.getText().equals("-") && (!lblSqlRecordsCount.getText().equals("---"))) {
		    int oldValue = Integer.valueOf(lblSqlRecordsCount.getText());
		    lblSqlRecordsCount.setText(String.valueOf(oldValue + 1));
		}
	    }
	});
    }

    public void closeDbConnection() {

	Debug.log.debug("activeConnection is " + activeConnection);

	try {
	    if (activeConnection != null)
		activeConnection.close();
	    else
		Debug.log.info("activeConnection is closed");

	    if (activeStatement != null)
		activeStatement.close();
	} catch (Exception e) {
	    Debug.log.error(e.getMessage());
	}

    }

    /** Метод для записи пользовательских настроек для SQL Tab в xml файл */

    public void saveSqlUserSettings() {
	sqlUserProperties.setProperty(GlobalVars.sqlServerType, cmbSqlServerType.getSelectionModel().getSelectedItem());
	sqlUserProperties.setProperty(GlobalVars.sqlServerIp, txtSqlServerIp.getText());
	sqlUserProperties.setProperty(GlobalVars.sqlServerPort, txtSqlServerPort.getText());
	sqlUserProperties.setProperty(GlobalVars.sqlServerLogin, txtSqlServerLogin.getText());
	sqlUserProperties.setProperty(GlobalVars.sqlServerPassword, txtSqlServerPassword.getText());

	if (tglOnOffSqlServerConnect.isSelected()) {
	    sqlUserProperties.setProperty(GlobalVars.enableSaveCdrToDBFlag, "true");
	} else {
	    sqlUserProperties.setProperty(GlobalVars.enableSaveCdrToDBFlag, "false");
	}

	UserProperties.savePropertiesXML(Paths.sqlUserSettingsXMLFilePathName, sqlUserProperties,
		"Sql server connection properties");
	Debug.log.info("Save SQL server settings to file  " + Paths.viewOnlySqlUserSettingsXMLFilePathName);

    }

}
