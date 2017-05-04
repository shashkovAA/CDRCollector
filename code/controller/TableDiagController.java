package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import objects.CdrRecord;
import objects.ImageForCallType;

public class TableDiagController {

	@FXML
	private TableView<CdrRecord> cdrTable;

	@FXML
	private TableColumn<CdrRecord, String> numColumn;

	@FXML
	private TableColumn<CdrRecord, ImageForCallType> imageColumn;

	@FXML
	private TableColumn<CdrRecord, String> dateColumn;

	@FXML
	private TableColumn<CdrRecord, String> timeColumn;

	@FXML
	private TableColumn<CdrRecord, String> durationColumn;

	@FXML
	private TableColumn<CdrRecord, String> calling_numColumn;

	@FXML
	private TableColumn<CdrRecord, String> dialed_numColumn;

	@FXML
	private TableColumn<CdrRecord, String> directColumn;

	@FXML
	private TableColumn<CdrRecord, String> outTrunkColumn;

	@FXML
	private TableColumn<CdrRecord, String> outChannelColumn;

	@FXML
	private TableColumn<CdrRecord, String> inTrunkColumn;

	@FXML
	private TableColumn<CdrRecord, String> inChannelColumn;

	@FXML
	private TableColumn<CdrRecord, String> idColumn;
	
	
	
	@FXML
	private Button btnOk;

	@FXML
	private AnchorPane tableView;

	private Stage dialogStage;
	
	private SelectBarController selectBarController;
	
//	private CdrRecord cdrRecord;
	
	private ObservableList<CdrRecord> cdrDataSelectedList = FXCollections.observableArrayList();
	
	
	@FXML
	private void initialize()
	{
	
		numColumn.setCellValueFactory(cellData -> cellData.getValue().getNumberProperty());
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());	
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
		timeColumn.setCellValueFactory(cellData -> cellData.getValue().getTimeProperty());
		durationColumn.setCellValueFactory(cellData -> cellData.getValue().getDurationProperty());
		calling_numColumn.setCellValueFactory(cellData -> cellData.getValue().getCalling_numProperty());
		dialed_numColumn.setCellValueFactory(cellData -> cellData.getValue().getDialed_numProperty());
		directColumn.setCellValueFactory(cellData -> cellData.getValue().getDirectionProperty());
		/*outTrunkColumn.setCellValueFactory(cellData -> cellData.getValue().getCode_usedProperty());
		outChannelColumn.setCellValueFactory(cellData -> cellData.getValue().getOut_crt_idProperty());
		inTrunkColumn.setCellValueFactory(cellData -> cellData.getValue().getIn_trk_codeProperty());
		inChannelColumn.setCellValueFactory(cellData -> cellData.getValue().getIn_crt_idProperty());*/
		
		
		cdrTable.setItems(cdrDataSelectedList);
	}
//************************************************************************************************************
//===
	
	public void setDialogStage(Stage dialogStage)
	{
		this.dialogStage = dialogStage;
	}

//************************************************************************************************************
//===
	
	public void init(SelectBarController selectBarController)
	{
		this.selectBarController = selectBarController;
		
	}
//************************************************************************************************************
//===	
	
	@FXML
	void clckOk(ActionEvent event)
	{
		 dialogStage.close();
	}
//************************************************************************************************************
//===	

	public void setRecord(CdrRecord record)
	{
		cdrDataSelectedList.add(record);
		
	}

//************************************************************************************************************
//===
	

	
	
	
}
