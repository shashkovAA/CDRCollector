package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.controlsfx.control.textfield.CustomTextField;

import application.Debug;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDateTimeTextField;
import objects.CdrRecord;
import objects.GlobalVars;
import objects.JavaFXControls;
import objects.MyCalendar;
import sql.Mssql;

public class SelectBarController {

    @FXML
    private AnchorPane sBar;
    @FXML
    private Label lblCountRecord;
    @FXML
    private LocalDateTimeTextField txtDateTimeBegin;
    @FXML
    private LocalDateTimeTextField txtDateTimeEnd;
    @FXML
    private Button btnShowDateTime;
    @FXML
    private Button btnExport;
    @FXML
    private Button btnFind;
    @FXML
    private Pane panePBX;
    @FXML
    private Pane paneDirection;
    @FXML
    private Pane paneDuration;
    @FXML
    private Pane paneRegion;
    @FXML
    private Pane paneNumber;
    @FXML
    private Pane paneTime;
    @FXML
    private Pane paneCallType;

    @FXML
    private CustomTextField txtDuration;
    @FXML
    private CustomTextField txtCallingNumber;
    @FXML
    private CustomTextField txtDnis;

    @FXML
    private CheckBox chkbINcludeCallingNumber;
    @FXML
    private CheckBox chkbIncludeDnis;

    @FXML
    private ComboBox<?> cmbDirection;
    @FXML
    private ComboBox<String> cmbPbx;
    @FXML
    private ComboBox<?> cmbCallType;
    @FXML
    private ComboBox<?> cmbRegion;
    @FXML
    private ComboBox<String> cmbCondition;

    @FXML
    private Hyperlink lnkToday;
    @FXML
    private Hyperlink lnkLastWeek;
    @FXML
    private Hyperlink lnkOneDay;
    @FXML
    private Hyperlink lnkLastMonth;
    @FXML
    private Hyperlink lnkClearDateTime;

    @FXML
    private ImageView imgClearLink;

    public Mssql mssql;

    private Image imgClear;

    private RootController rootController;

    private TableDiagController tableDiagController;

    private int numRecords = 0;

    private DateTimeFormatter formatter;

    private Date currentDate;

    private String todayDate;

    private String todayDateTimeBegin;

    private String todayDateTimeEnd;

    public void init(RootController rootController) {
	this.rootController = rootController;

    }

    // ************************************************************************************************************
    // ===
    @FXML
    public void initialize() {

	mssql = new Mssql();
	mssql.init(this);

	JavaFXControls.setupClearButtonField(txtDnis);
	JavaFXControls.setupClearButtonField(txtCallingNumber);

	formatter = DateTimeFormatter.ofPattern(GlobalVars.dateTimeFormat);
	txtDateTimeBegin.setDateTimeFormatter(formatter);
	txtDateTimeEnd.setDateTimeFormatter(formatter);

	// String myDate = new java.text.SimpleDateFormat("dd/MM/yy").format(new
	// java.util.Date()); //==============��������� ������� ����===========

	// txtDateTimeBegin.setText("12.03.2017 23:59");

	imgClear = new Image(getClass().getResourceAsStream("../sources/clear.png"));
	imgClearLink.setImage(imgClear);
	cmbPbx.getItems().addAll("Definity");
	cmbPbx.getSelectionModel().select(0);
	cmbCondition.getItems().addAll("", ">", "=", "<");
	cmbCondition.getSelectionModel().select(0);
	txtDuration.setDisable(true);
    }

    @FXML
    private void changeValueCmbDuration() {
	Debug.log.info("Combo box Value is change !");
	if (!cmbCondition.getSelectionModel().getSelectedItem().isEmpty())
	    txtDuration.setDisable(false);
	else
	    txtDuration.setDisable(true);
    }

    @FXML
    private void clckFind(ActionEvent event) {
	System.out.println("Кнопка Find");
	try {
	    
	    FXMLLoader loader = new FXMLLoader();

	    loader.setLocation(getClass().getResource("../view/TableDiag.fxml"));

	    AnchorPane page = (AnchorPane) loader.load();

	    
	    Stage dialogStage = new Stage();

	    dialogStage.setTitle("Выборка данных");

	    dialogStage.initModality(Modality.WINDOW_MODAL);

	    dialogStage.initOwner(rootController.getPrimaryStage());

	    Scene scene = new Scene(page);

	    dialogStage.setScene(scene);

	    // ������� �������� � ����������.

	    tableDiagController = loader.getController();

	    tableDiagController.setDialogStage(dialogStage);

	    getDataAndCount();

	    // ���������� ���������� ���� � ���, ���� ������������ ��� ��
	    // �������

	    dialogStage.showAndWait();

	} catch (IOException except) {
	    Debug.log.error(except.getMessage());

	}

    }

    private void getDataAndCount() {
	Connection connect = null;
	Statement statement = null;
	ResultSet result = null;
	numRecords = 0;
	String sqlQuery;

	try {

	    connect = DriverManager.getConnection(
		    rootController.getConnectionUrlToSqlServer() + ";dataBaseName=" + rootController.getDbName(),
		    rootController.getSqlServerLogin(), rootController.getSqlServerPassword());

	    sqlQuery = mssql.getSqlQueryForSearch();

	    Debug.log.info(sqlQuery);

	    statement = connect.createStatement();
	    result = statement.executeQuery(sqlQuery);

	    /*
	     * ������ ���������� �������� ��������� rowCount
	     * 
	     * sqlQuery="{call [dbo].[rowCount](?)}";
	     * 
	     * CallableStatement st = connect.prepareCall(sqlQuery);
	     * 
	     * st.setString(1, "9261389514");
	     * 
	     * result = st.executeQuery();
	     */

	    // result.next();

	    // num = result.getInt(1);

	    while (result.next()) {
		numRecords += 1;

		CdrRecord record = new CdrRecord();

		record.setNumber(String.valueOf(numRecords));
		record.setId(result.getString(1));
		record.setDate(result.getString(2));
		record.setTime(result.getString(3));
		record.setDuration(result.getString(4));
		record.setCalling_num(result.getString(5));
		record.setDialed_num(result.getString(6));
		record.setDirection(result.getString(7));

		tableDiagController.setRecord(record);

	    }

	    result.close();

	    statement.close();

	    // st.close();

	    connect.close();

	    // return String.valueOf(numRecords);

	} catch (SQLException except) {
	    Debug.log.error(except.getMessage());
	    // return "---";
	}

    }

    @FXML
    private void clckGetDateTime(ActionEvent event) {
	System.out.println("Click Button!");
	System.out.println(txtDateTimeBegin.getText());
	System.out.println(todayDateTimeBegin);
    }

    @FXML
    private void clckLinkToday() {
	todayDateTimeBegin = MyCalendar.getCurrentDayDateTimeBegin();
	todayDateTimeEnd = MyCalendar.getCurrentDayDateTimeNow();
	setDateTimeFields();
    }

    @FXML
    private void clckLinkCurrentWeek(ActionEvent event) {
	todayDateTimeBegin = MyCalendar.getMondayDateTimeOfCurrentWeek();
	todayDateTimeEnd = MyCalendar.getCurrentDayDateTimeNow();
	setDateTimeFields();
    }

    @FXML
    private void clckLinkCurrentMonth(ActionEvent event) {
	todayDateTimeBegin = MyCalendar.getFirstDayDateTimeOfCurrentMonth();
	todayDateTimeEnd = MyCalendar.getCurrentDayDateTimeNow();
	setDateTimeFields();
    }

    private void setDateTimeFields() {
	txtDateTimeBegin.setText(todayDateTimeBegin);
	txtDateTimeEnd.setText(todayDateTimeEnd);
    }

    @FXML
    private void clckLinkCurrentYear(ActionEvent event) {
	todayDateTimeBegin = MyCalendar.getFirstDayDateTimeOfCurrentYear();
	todayDateTimeEnd = MyCalendar.getCurrentDayDateTimeNow();
	txtDateTimeBegin.setText(todayDateTimeBegin);
	txtDateTimeEnd.setText(todayDateTimeEnd);
    }

    @FXML
    private void clckLinkClearDateTime(ActionEvent event) {
	txtDateTimeBegin.setText("");
	txtDateTimeEnd.setText("");
    }

    public String getDateTimeBegin() {
	return txtDateTimeBegin.getText();
    }

    public String getDateTimeEnd() {
	return txtDateTimeEnd.getText();
    }

    public String getCallingNumber() {
	return txtCallingNumber.getText().trim();
    }

    public boolean isSelectedChkbINcludeCallingNumber() {
	if (chkbINcludeCallingNumber.isSelected())
	    return true;
	else
	    return false;
    }

    public String getDialedNumber() {
	return txtDnis.getText().trim();
    }

    public boolean isSelectedchkbIncludeDnis() {
	if (chkbIncludeDnis.isSelected())
	    return true;
	else
	    return false;
    }

    public String getConditionDuration() {
	return cmbCondition.getSelectionModel().getSelectedItem();
    }

    public String getDuration() {
	return txtDuration.getText();
    }

}
