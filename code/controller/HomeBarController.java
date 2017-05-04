package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.textfield.CustomTextField;

import application.Debug;
import application.Main;
import application.PbxCollector;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import objects.CdrRecord;
import objects.Dialogs;
import objects.GlobalVars;
import objects.ImageForCallType;
import objects.JavaFXControls;
import objects.MapProtocols;
import objects.Paths;
import objects.Protocol;
import objects.ProtocolField;
import objects.ProtocolFieldsArrays;

public class HomeBarController implements Runnable {

    @FXML
    private AnchorPane hBar;
    @FXML
    private ToggleSwitch tglGo;
    @FXML
    private TextField txtPort;
    @FXML
    private ComboBox<String> cmbProtocol;
    @FXML
    private CustomTextField txtNamePBX;
    @FXML
    private CustomTextField txtIPForControl;
    @FXML
    private Label lblCountRecord;
    @FXML
    private Button btnClear;
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

    public volatile int numSockets = 1;

    private RootController rootController;

    private ServerSocket myServer = null;

    private Socket myConnect = null;

    /** Файл, в который записываются CDR строки в исходном виде */
    private File cdrFile;

    /**
     * Файл, в который записываются CDR строки в форматированном виде, если они
     * не хранятся в базе данных.
     */
    private File cdrFormattedFile;

    /** Строка CDR записи, которая парсится */
    private String cdrRecord; //

    private MapProtocols mapProtocols;
    private Protocol currentCdrProtocol;
    private ProtocolFieldsArrays protocolFieldsArrays;

    public static HashMap<String, String> mapUserPreferences = new HashMap<>();
    // TODO Будет удален
    private ArrayList<ProtocolField> protocolFieldsList;

    /** Массив индексов начала определенного поля в cdr записи */
    private int[] indexArray; //

    /** Массив имен полей протокола */
    private String[] nameFieldArray;

    /** Массив длин полей протокола */
    private int[] lenFieldArray;

    /**
     * Определяет ожидаемую длину строки для набора полей, в соответствии с
     * выбранным протоколом. Если длина CDR записи меньше, то она дополняется
     * пробелами.
     */
    private int cdrRecordLengthWaiting = 0;

    /**
     * Номер записи CDR полученной за текущий час. Прибавляется к ID вызова.
     * Преобразуется к виду ХХХХ.
     */
    private int numHourRecod = 0;

    /**
     * Используемый протокол при разборе CDR записи. Протокол выбирается из
     * низпадающего списка на экране Home.
     */
    private String selectedProtocolString = "";

    /**
     * Наблюдаемый список (ArrayList) записей CDR. Используется для добавления в
     * TableView.
     */
    private ObservableList<CdrRecord> cdrDataList = FXCollections.observableArrayList();

    /**
     * Значение даты в формате dd.MM.yyyy, которое заносится в объект CdrRecord
     */
    private volatile String dateString = "";

    public ArrayList<TableCell> cells = new ArrayList<>();

    /** ID для CDR записи */
    private String stringForId = "";

    /** Часть ID CDR записи, которая содержит дату в формате: <b>yyyyMMdd */
    private String dateStringForId = "";

    private final static int MIN_PORT_NUMBER = 5000;

    private final static int MAX_PORT_NUMBER = 64500;

    private volatile boolean portAvalable = false;

    boolean enableRunCdrCollectDbFlag = false;

    public void init(RootController rootController) {
	this.rootController = rootController;
    }

    // ************************************************************************************************************
    // ===Метод срабатывает один раз после загрузки контента FXML

    @FXML
    public void initialize() {

	mapProtocols = new MapProtocols();
	currentCdrProtocol = new Protocol();

	setDefaultUISettingsOnTab();

	addListenerToObservableCdrList();

	// Наполняем ComboBox списком протоколов
	for (HashMap.Entry<String, Protocol> it : mapProtocols.entrySet()) {
	    cmbProtocol.getItems().addAll(it.getKey());
	}

	updateCountRecord();

	getUserPropertiesForUIFieldsFromFile();

	// -----Инициализация полей таблицы вызовов
	/*lastNameCol.setCellValueFactory(new Callback<CellDataFeatures<Person, String>, ObservableValue<String>>() {
	     public ObservableValue<String> call(CellDataFeatures<Person, String> p) {
	         // p.getValue() returns the Person instance for a particular TableView row
	         return p.getValue().lastNameProperty();
	     }
	
	    @Override
	    public ObservableValue<String> call(CellDataFeatures<Person, String> param) {
		// TODO Auto-generated method stub
		return null;
	    }
	  });
	 }*/

	numColumn.setCellValueFactory(cellData -> cellData.getValue().getNumberProperty());
	idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
	dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
	timeColumn.setCellValueFactory(cellData -> cellData.getValue().getTimeProperty());
	durationColumn.setCellValueFactory(cellData -> cellData.getValue().getDurationProperty());
	calling_numColumn.setCellValueFactory(cellData -> cellData.getValue().getCalling_numProperty());
	dialed_numColumn.setCellValueFactory(cellData -> cellData.getValue().getDialed_numProperty());
	directColumn.setCellValueFactory(cellData -> cellData.getValue().getDirectionProperty());
	outTrunkColumn.setCellValueFactory(cellData -> cellData.getValue().getCode_usedProperty());
	outChannelColumn.setCellValueFactory(cellData -> cellData.getValue().getOut_crt_idProperty());
	inTrunkColumn.setCellValueFactory(cellData -> cellData.getValue().getIn_trk_codeProperty());
	inChannelColumn.setCellValueFactory(cellData -> cellData.getValue().getIn_crt_idProperty());

	/*
	imageColumn.setCellValueFactory(cellData -> cellData.getValue().getImageForCallTypeProperty());
	
	imageColumn.setCellFactory(
		new Callback<TableColumn<CdrRecord, ImageForCallType>, TableCell<CdrRecord, ImageForCallType>>() {
	
		    @Override
	
		    public TableCell<CdrRecord, ImageForCallType> call(TableColumn<CdrRecord, ImageForCallType> param) {
			TableCell<CdrRecord, ImageForCallType> cell = new TableCell<CdrRecord, ImageForCallType>() {
			    ImageView imageview = new ImageView();
	
			    @Override
	
			    public void updateItem(ImageForCallType item, boolean empty) {
				if (item != null) {
	
				    VBox vbox = new VBox();
	
				    imageview.setFitHeight(16);
	
				    imageview.setFitWidth(16);
	
				    imageview.setImage(new Image(getClass()
					    .getResourceAsStream("../sources/" + item.getFileImageTypeName())));
	
				    vbox.getChildren().addAll(imageview);
	
				    setGraphic(vbox);
				}
			    }
			};
	
			cells.add(cell);
			return cell;
	
		    }
	
		});
		*/

	cdrTable.setItems(cdrDataList);

	// --------Проверка подгруженного порта
	checkPort();
	checkTextNamePbx();
	checkComboBox();
	checkIPField();

	if (enableRunCdrCollectDbFlag && checkStatement())
	    tglGo.setSelected(true);

    }

    private void setDefaultUISettingsOnTab() {
	JavaFXControls.setupClearButtonField(txtNamePBX);
	JavaFXControls.setupClearButtonField(txtIPForControl);

	txtNamePBX.setText("");
	cmbProtocol.getSelectionModel().select(0);
	txtPort.setText(GlobalVars.defaultPortPbxCollector);
	tglGo.setDisable(true);
	txtIPForControl.setStyle(Main.colorAvalable);
    }

    private void updateCountRecord() {

	lblCountRecord.setText(String.valueOf(cdrDataList.size()));
    }

    private void addListenerToObservableCdrList() {
	cdrDataList.addListener(new ListChangeListener<CdrRecord>() {
	    @Override
	    public void onChanged(ListChangeListener.Change change) {
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
			updateCountRecord();
		    }
		});
	    }
	});
    }

    private void getUserPropertiesForUIFieldsFromFile() {
	Iterator it = Main.pbxProperties.keySet().iterator();

	while (it.hasNext()) {
	    String key = it.next().toString();

	    if (key.equals(GlobalVars.namePbx1)) {
		setUITxtNamePbx(key);
	    }
	    if (key.equals(GlobalVars.nameProtocolPbx1)) {
		setUICmbProtocol(key);
	    }
	    if (key.equals(GlobalVars.namePortPbx1)) {
		setUITxtPort(key);
	    }
	    if (key.equals(GlobalVars.enableRunCdrCollectorFlag)) {
		setUIStateToggleRunCdrCollector(key);
	    }
	    if (key.equals(GlobalVars.ipAddressPbxForControl)) {
		setUITxtIPForControl(key);
	    }
	}
    }

    private void setUITxtNamePbx(String key) {
	txtNamePBX.setText(Main.pbxProperties.getProperty(key));
	Debug.log.debug("Found Pbx1 name from userpreferrences.xml file . For  " + key + " value is "
		+ Main.pbxProperties.getProperty(GlobalVars.namePbx1));
    }

    private void setUICmbProtocol(String key) {
	cmbProtocol.getSelectionModel().select(Main.pbxProperties.getProperty(GlobalVars.nameProtocolPbx1));
	Debug.log.debug("Found Protocol name for Pbx1 from userpreferrences.xml file . For  " + key + " value is "
		+ Main.pbxProperties.getProperty(GlobalVars.nameProtocolPbx1));
    }

    private void setUITxtPort(String key) {
	String givenPortFromXML = Main.pbxProperties.getProperty(key);
	if (givenPortFromXML.matches(GlobalVars.portPbxCollectorRegexValue)) {
	    txtPort.setText(givenPortFromXML);
	    Debug.log.debug("Found Port number from userpreferrences.xml file . For  " + key + " value is "
		    + Main.pbxProperties.getProperty(GlobalVars.namePortPbx1));
	} else {
	    txtPort.setText(GlobalVars.defaultPortPbxCollector);
	    String warning = "Found Port number from userpreferrences.xml file . For " + key + " value is "
		    + Main.pbxProperties.getProperty(GlobalVars.namePortPbx1)
		    + ". But this value is not allowed. Port value will be set on default.";
	    Debug.log.warn(warning);
	    Dialogs.showWarnDialog("Изменение порта", null, warning);
	}
    }

    private void setUIStateToggleRunCdrCollector(String key) {
	enableRunCdrCollectDbFlag = checkConvertToBolleanSettings(key);
	Debug.log.debug("Found enableRunCdrCollectDbFlag  from userpreferrences.xml file . For  " + key + " value is "
		+ Main.pbxProperties.getProperty(GlobalVars.enableRunCdrCollectorFlag));
    }

    private boolean checkConvertToBolleanSettings(String key) {
	try {
	    return Boolean.valueOf(Main.pbxProperties.getProperty(key));
	} catch (NumberFormatException except) {
	    Debug.log.error(except.getMessage());
	    return false;
	}
    }

    private void setUITxtIPForControl(String key) {
	txtIPForControl.setText(Main.pbxProperties.getProperty(key));
	Debug.log.debug("Found enableRunCdrCollectDbFlag  from userpreferrences.xml file . For  " + key + " value is "
		+ Main.pbxProperties.getProperty(GlobalVars.enableRunCdrCollectorFlag));
    }

    // ************************************************************************************************************
    // ===Ограничение на ввод в поле Порт только цифр, не начинающихся с нуля и
    // длиной не более 5 символов.

    @FXML
    private void txtPortKeyTyped(KeyEvent event) {

	Debug.log.debug("Event source :" + event.getSource() + " Event type :" + event.getEventType()
		+ " Event character :" + event.getCharacter());

	if ((txtPort.getText().length() < 5) && (event.getCharacter().equals("1") || event.getCharacter().equals("2")
		|| event.getCharacter().equals("3") || event.getCharacter().equals("4")
		|| event.getCharacter().equals("5") || event.getCharacter().equals("6")
		|| event.getCharacter().equals("7") || event.getCharacter().equals("8")
		|| event.getCharacter().equals("9") || event.getCharacter().equals("0"))) {
	    if (txtPort.getText().equals("") && (event.getCharacter().equals("0")))
		event.consume(); // =============Проверка на введение только
				 // одного первого нуля
	    Debug.log.debug("event.getCharacter() " + event.getCharacter());

	} else
	    event.consume();

    }

    // ************************************************************************************************************
    // ===
    @FXML
    private void txtPortKeyReleased(KeyEvent event) {
	checkPort();

    }
    // ************************************************************************************************************
    // ===

    @FXML
    private void txtPortKeyPress(KeyEvent event) {
	Debug.log.debug("Event source :" + event.getSource() + " Event type :" + event.getEventType() + " Event text :"
		+ event.getText() + " Event code :" + event.getCode());
    }

    // ************************************************************************************************************
    // ===
    @FXML
    private void txtNamePBXKeyReleased(KeyEvent event) {
	checkTextNamePbx();

    }

    // ************************************************************************************************************
    // ===
    @FXML
    private void testEvent(ActionEvent event) {
	Debug.log.debug("ComboBox for protocols change value to " + cmbProtocol.getSelectionModel().getSelectedItem());
    }

    // ************************************************************************************************************
    // ===
    @FXML
    private void changeToggle(MouseEvent event) {
	if (tglGo.isSelected())
	    startCollector();
	else
	    stopCollector();
    }

    // ************************************************************************************************************
    // ===
    @FXML
    private void txtNamePBXMouseClick(MouseEvent event) {
	checkTextNamePbx();
    }

    @FXML
    private void clearCdrList(ActionEvent event) {

	for (int i = 0; i < cdrDataList.size(); i++)
	    cdrDataList.get(i).setImageForCallType(null);

	// imageColumn.setCellValueFactory(cellData ->
	// cellData.getValue().getImageTypeProperty());

	/*
	 * ArrayList<Object> removedList = new ArrayList<>();
	 * 
	 * 
	 * Iterator it = cdrDataList.iterator(); while (it.hasNext()) {
	 * removedList.add(it.next());
	 * 
	 * }
	 * 
	 * 
	 * cdrDataList.removeAll(removedList);
	 */
	cdrDataList.clear();
	// cells.clear();

	/*
	 * ArrayList<ImageType> removedList2 = new ArrayList<>();
	 * 
	 * Iterator<ImageType> it2 = arrayImg.iterator();
	 * 
	 * while (it2.hasNext()) { removedList2.add(it2.next()); }
	 * arrayImg.removeAll(removedList2);
	 */

	// imageColumn.getColumns().get(1).setVisible(false);
	// imageColumn.getColumns().get(1).setVisible(true);

	// cdrTable.getItems().clear();

    }

    // @FXML

    private String getCdrLogFileNameWithExtension() {
	return txtNamePBX.getText() + "_" + cmbProtocol.getSelectionModel().getSelectedItem().toString()
		+ Paths.cdrFileExtension;
    }

    private String getCdrFormattedLogFileNameWithExtension() {
	return txtNamePBX.getText() + "_" + cmbProtocol.getSelectionModel().getSelectedItem().toString()
		+ Paths.cdrFormattedFileExtension;
    }

    public void startCollector() {

	String cdrLogFileName = Paths.cdrDirPath + getCdrLogFileNameWithExtension();
	String cdrFormattedLogFileName = Paths.cdrDirFormattedPath + getCdrFormattedLogFileNameWithExtension();

	rootController.setCdrFileNameForFileSizeController(getCdrLogFileNameWithExtension());

	// --------Получаем список полей для выбранного в ComboBox протокола из
	// HashMap mapProtocols

	selectedProtocolString = cmbProtocol.getSelectionModel().getSelectedItem().toString();
	
	protocolFieldsList = mapProtocols.getProtocol(selectedProtocolString).get();
	currentCdrProtocol = mapProtocols.getProtocol(selectedProtocolString);

	getProtocolArrays(); // TODO Будет удален!!
	
	protocolFieldsArrays = new ProtocolFieldsArrays(currentCdrProtocol);

	int serverPort = Integer.valueOf(txtPort.getText());

	Debug.log.info("Start CDR collector.");

	createCdrFile(cdrLogFileName);

	createCdrFormattedFile(cdrFormattedLogFileName);

	openSocketConnectionOnPort(serverPort);

	Debug.log.info("Socket open on port :" + serverPort + ". Waiting for client connect...");

	rootController.createThread(this, GlobalVars.nameThreadForSocketConnection);

	rootController.changeImageToStart();
	rootController.changeTxtFieldStatusClientWait();

	txtPort.setDisable(true);
	txtNamePBX.setDisable(true);
	cmbProtocol.setDisable(true);
	txtIPForControl.setDisable(true);

    }

    private void openSocketConnectionOnPort(int mSocketPort) {
	try {
	    myServer = new ServerSocket(mSocketPort);
	} catch (IOException except) {
	    Debug.log.error(except.getMessage());
	}
    }

    private void createCdrFile(String fileName) {
	cdrFile = new File(fileName);
	if (!cdrFile.exists()) {
	    createTextFile(cdrFile);
	    Debug.log.info("Create CDR log file " + Paths.viewOnlycdrDirPath + getCdrLogFileNameWithExtension());
	}
    }

    private void createCdrFormattedFile(String fileName) {
	cdrFormattedFile = new File(fileName);

	if (!cdrFormattedFile.exists()) {
	    createTextFile(cdrFormattedFile);
	    Debug.log.info("Create CDR formatted log file " + Paths.viewOnlycdrDirFormattedPath
		    + getCdrFormattedLogFileNameWithExtension());
	}
    }

    private void createTextFile(File file) {
	try {
	    file.createNewFile();
	} catch (IOException e) {
	    Debug.log.error(e.getMessage());
	}
    }

    private void getProtocolArrays() {

	nameFieldArray = new String[protocolFieldsList.size()];
	lenFieldArray = new int[protocolFieldsList.size()];

	for (int i = 0; i < protocolFieldsList.size(); i++) {
	    nameFieldArray[i] = protocolFieldsList.get(i).getName().toString();
	    lenFieldArray[i] = Integer.valueOf(protocolFieldsList.get(i).getLength().toString());
	}

	// ---------Получаем массив индексов для полей текущего протокола

	indexArray = new int[protocolFieldsList.size()];

	indexArray[0] = 0;

	for (int i = 1; i < protocolFieldsList.size(); i++) {
	    indexArray[i] = indexArray[i - 1] + Integer.valueOf(protocolFieldsList.get(i - 1).getLength().toString());
	    Debug.log.debug(" nameFieldArray[" + i + "] = " + nameFieldArray[i] + "\t indexArray[" + i + "] ="
		    + indexArray[i] + "\t lenFieldArray[ " + i + "] = " + lenFieldArray[i]);
	}

	cdrRecordLengthWaiting = indexArray[protocolFieldsList.size() - 1]
		+ Integer.valueOf(protocolFieldsList.get(protocolFieldsList.size() - 1).getLength().toString());

    }

    // ************************************************************************************************************************
    // ===Метод для закрытия сокета и коннекта от клиента

    public void stopCollector() {
	Debug.log.info("Stop CDR collector. Closing socket...");

	try {
	    if (myServer != null)
		myServer.close();
	    if (myConnect != null)
		myConnect.close();

	} catch (IOException e) {
	    Debug.log.error(e.getMessage());
	}

	rootController.changeImageToStop();
	rootController.changeTxtFieldStatusClientDisonnected();
	txtPort.setDisable(false);
	txtNamePBX.setDisable(false);
	cmbProtocol.setDisable(false);
	txtIPForControl.setDisable(false);
    }

    public void saveCurrentProtocolsMap() {
	try (ObjectOutputStream oOut = new ObjectOutputStream(new FileOutputStream(Paths.serFilePathName));) {
	    oOut.writeObject(mapProtocols.get());
	    oOut.close();
	    Debug.log.info("Save list protocols to backup file " + Paths.viewOnlyserFilePathName);
	} catch (IOException ioe) {
	    Debug.log.error(ioe.getMessage());
	}
    }

    // ************************************************************************************************************
    // ===
    /*
     * @FXML public void saveUserPreferrencesMap() {
     * 
     * userProperties.setProperty(Paths.namePortPbx1, txtPort.getText());
     * userProperties.setProperty(Paths.nameProtocolPbx1,
     * cmbProtocol.getSelectionModel().getSelectedItem());
     * userProperties.setProperty(Paths.namePbx1, txtNamePBX.getText());
     * UserProperties.savePropertiesXML(Paths.XMLFilePathName, userProperties,
     * "Custom user properties"); Debug.log.info("Save user settings to file  "
     * + Paths.viewOnlyXMLFilePathName);
     * 
     * }
     */
    /** Геттер для значения в текстовом поле Порт формы Home */
    public String getSocketPortValue() {
	return txtPort.getText();
    }

    // ************************************************************************************************************
    // ===Геттер для значения поля "Название АТС"

    public String getNamePBX() {
	return txtNamePBX.getText();
    }

    // ************************************************************************************************************
    // ===Геттер для значения, выбранного в combobox "Протокол"

    public String getNameProtocol() {
	return cmbProtocol.getSelectionModel().getSelectedItem();
    }

    // ************************************************************************************************************
    // ===
    public String getIpAddressPbxForControl() {
	return txtIPForControl.getText();
    }

    // ************************************************************************************************************
    // ===Метод работы для нового потока по открытию сокета и прослушиванию
    // порта

    @Override
    public void run() {

	PbxCollector runWork = null;
	Thread mythread = null;

	while (true) {

	    try {
		myConnect = myServer.accept();
		Debug.log.info("myConnect = " + myConnect);

		numSockets = numSockets - 1;
		String str = myConnect.getInetAddress().toString().substring(1);

		if (numSockets < 0) {
		    Debug.log.warn("Atempt connection from IP : " + str);
		    Debug.log.warn("Number of connections exceeds the specified threshold.");
		    myConnect.close();
		    numSockets = numSockets + 1;
		}

		else

		{

		    if (!(!txtIPForControl.getText().equals("") && txtIPForControl.getText().equals(str)
			    || txtIPForControl.getText().equals(""))) {
			Debug.log.info(
				"!txtIPForControl.getText().equals('') = " + !txtIPForControl.getText().equals(""));
			Debug.log.info(
				"txtIPForControl.getText().equals(str) = " + txtIPForControl.getText().equals(str));
			Debug.log.info("Total"
				+ (!(!txtIPForControl.getText().equals("") && txtIPForControl.getText().equals(str)
					|| txtIPForControl.getText().equals(""))));

			Debug.log.warn("Atempt connection from IP that isn't allowed. IP = " + str);
			myConnect.close();
			numSockets = numSockets + 1;
		    } else {

			Debug.log.info("Client connected from IP :" + str);

			Platform.runLater(new Runnable() {

			    @Override
			    public void run() {
				rootController.changeTxtFieldStatusClientConnected(str);
			    }

			});

			runWork = new PbxCollector(myConnect, cdrFile);
			runWork.init(this); // Метод инициализации связи потока
					    // с основным текущим контроллером

			mythread = new Thread(runWork);
			mythread.setName("ThreadCollectorPBX");
			Debug.log.info("Create thread with name :" + mythread.getName());
			mythread.start();

		    }
		}

	    } catch (IOException except) {
		Debug.log.info(
			"Server closed socket for new connections. (System message : " + except.getMessage() + ")");
		break;
	    }
	}

	try {
	    runWork.stopMe();

	} catch (NullPointerException event) {
	    Debug.log.info("Stoping collector thread until open client connection.");
	}
    }

    /** Входной метод разбора CDR записи */

    public void getRecord(String mCdrRecord) {

	String partCdr = "";
	String trailCdr = "";

	int[] crIndex = new int[20];

	int numEnters = 0;
	int partIndex = 0;
	int i;

	cdrRecord = mCdrRecord;

	if (cdrRecord.length() != 0) {

	    Debug.log.info("Исходная CDR запись :" + cdrRecord);

	    Debug.log.info("Исходная длина CDR записи :" + cdrRecord.length());

	    for (i = 0; i < cdrRecord.length(); i++)
		if ((int) cdrRecord.charAt(i) == 13) // (code CR = 13)
		{
		    crIndex[numEnters] = i;
		    numEnters += 1;
		}

	    for (i = 0; i < numEnters; i++) {
		Debug.log.debug("crIndex[" + i + "] = " + crIndex[i]);

		// ---Если CR встречаем, то вырезаем до этого символа часть cdr
		// записи и передаем в парсер

		partCdr = cdrRecord.substring(partIndex, crIndex[i]).trim();
		parsingCdrStringUnformatted(partCdr);

		partIndex = crIndex[i] + 1;

		Debug.log.warn("CDRPart Record :" + partCdr);
	    }

	    Debug.log.info("Number of CR in CDR String = " + numEnters);

	    if (partIndex == 0)

		parsingCdrStringUnformatted(cdrRecord.trim());

	    else {
		trailCdr = cdrRecord.substring(partIndex, cdrRecord.length()).trim(); // ---Вырезаем
										      // остаток
										      // строки

		if (trailCdr.length() >= 41) {
		    parsingCdrStringUnformatted(trailCdr);
		    Debug.log.warn("TrailCDRPart Record :" + trailCdr);
		}

	    }

	    Debug.log.info("Length of cdrRecord = " + cdrDataList.size());

	}
    }

    // ************************************************************************************************************
    // ===
    private void parsingCdrStringUnformatted(String cdrString) {

	CdrRecord record = new CdrRecord();

	// ParsedCdrRecord parsedCdrRecord = new ParsedCdrRecord(cdrString,
	// protocolFieldsArrays);

	// String calling =
	// parsedCdrRecord.getFieldValueFromCdrString("calling_num");

	// Debug.log.warn("Calling number from CDR :" + calling);

	String tempCond_code = "";

	String tempCode_used = "";

	String tempIn_trk_code = "";

	String tempDuration = "";

	if (!cdrString.isEmpty()) {
	    int index = cdrString.indexOf('/');

	    // -------------Обработка короткой строки, которая содержит дату и
	    // время, выдаваемой АТС. Нас интересует дата.

	    if ((cdrString.length() < 20) && (selectedProtocolString.equals("unformatted")) && (index != -1)) {

		String day = cdrString.substring(index + 1, index + 3);

		String month = cdrString.substring(index - 2, index);
		// Значение года берем текущий
		String year = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date());

		dateString = day + "." + month + "." + year;

		dateStringForId = year + month + day;

	    } else if (cdrString.length() > 41) {
		Debug.log.info("Ожидаемая длина строки CDR записи =" + String.valueOf(cdrRecordLengthWaiting));
		Debug.log.info(
			"Полученная длина строки CDR записи без пробелов = " + String.valueOf(cdrString.length()));
		/* 1 */
		record.setNumber(String.valueOf(cdrDataList.size() + 1));

		/*
		 * 3 Если протокол unformatted, то поля date в записи CDR не
		 * будет
		 */
		if (selectedProtocolString.equals("unformatted"))
		    record.setDate(dateString);

		int i = 0;

		// Так как для unformatted длина CDR строки разная, в
		// зависимости от типа вызова, то вместо цикла for для
		// парсинга полей используем
		// цикл while с дополнительным условием по контролю длины
		// записи, чтобы избежать получения exception-а

		while (i < protocolFieldsList.size() && (indexArray[i] + lenFieldArray[i] - 1) <= cdrString.length())

		{
		    switch (nameFieldArray[i]) {
		    case "date":
			record.setDate(
				getFormattedDate(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i])));
			break;

		    case "time":
			record.setTime(
				getFormattedTime(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i])));
			break;

		    case "duration":
			tempDuration = cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]);
			record.setDuration(getFormattedDurationFromHMMSS(tempDuration));

			break;

		    case "calling_num":
			record.setCalling_num(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "dialed_num":
			record.setDialed_num(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "acct_code":
			record.setAcct_code(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "auth_code":
			record.setAuth_code(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "attd_console":
			record.setAttd_console(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "bandwidth":
			record.setBandwidth(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "bcc":
			record.setBcc(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "calltype":
			record.setCalltype(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "clg_num_in_tac":
			record.setClg_num_in_tac(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "clg_pty_cat":
			record.setClg_pty_cat(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "code_dial":
			record.setCode_dial(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "code_used":
			tempCode_used = cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]);
			record.setCode_used(tempCode_used);
			break;

		    case "cond_code":
			tempCond_code = cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]);
			record.setCond_code(tempCond_code);
			break;

		    case "contact_uri":
			record.setContact_uri(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "end_date_4d":
			record.setEnd_date_4d(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "end_time":
			record.setEnd_time(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "feat_flag":
			record.setFeat_flag(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "frl":
			record.setFrl(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "from_uri":
			record.setFrom_uri(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "in_crt_id":
			record.setIn_crt_id(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "in_trk_code":
			tempIn_trk_code = cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]);
			record.setIn_trk_code(tempIn_trk_code);
			break;

		    case "ins":
			record.setIns(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "isdn_cc":
			record.setIsdn_cc(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "ixc_code":
			record.setIxc_code(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "ma_uui":
			record.setMa_uui(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "node_num":
			record.setNode_num(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "out_crt_id":
			record.setOut_crt_id(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "ppm":
			record.setPpm(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "request_uri":
			record.setRequest_uri(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "res_flag":
			record.setRes_flag(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "sec_dur":
			record.setSec_dur(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "start_date":
			record.setStart_date(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "start_date_4d":
			record.setStart_date_4d(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "start_time":
			record.setStart_time(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "to_uri":
			record.setTo_uri(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "tsc_ct":
			record.setTsc_ct(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "tsc_flag":
			record.setTsc_flag(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    case "vdn":
			record.setVdn(cdrString.substring(indexArray[i], indexArray[i] + lenFieldArray[i]));
			break;

		    }

		    Debug.log.debug("nameFieldArray[" + i + "] = " + nameFieldArray[i] + "\t\t indexArray[" + i + "] = "
			    + indexArray[i]);

		    i++;

		}
		/* 8 */
		record.setDirection(getDirection(tempCond_code, tempCode_used, tempIn_trk_code));

		/* 45 */
		record.setPbxName(txtNamePBX.getText());

		/* 46 */
		record.setTimestampString(record.getDate() + " " + record.getTime());

		/* 47 */
		record.setDurationSeconds(getDurationSeconds(tempDuration));

		/* 48 Добавляем иконку для типа вызова */
		record.setImageForCallType(getImg(tempCond_code, tempCode_used, tempIn_trk_code));

		/* 2 Добавляем ID-ку вызова */
		record.setId(stringForId);

		cdrDataList.add(record);

		rootController.storeCdrRecordToDB(record);

	    }
	}

    }

    // ************************************************************************************************************
    // ===Метод для форматирования даты звонка

    private String getFormattedDate(String date) {
	String formattedDate = date;
	dateStringForId = date;
	return formattedDate;
    }

    // ************************************************************************************************************
    // ===Метод для форматирования времени звонка

    private String getFormattedTime(String time) {
	String formattedTime = time.substring(0, 2) + ":" + time.substring(2, 4);

	String prevTimeValueInStringForId = "";

	if (cdrDataList.size() != 0) // ---Получаем значение часа в предыдущей
				     // записи cdr
	{
	    prevTimeValueInStringForId = cdrDataList.get(cdrDataList.size() - 1).getTime().substring(0, 2);
	} else
	    prevTimeValueInStringForId = time.substring(0, 2);

	// ---Добавляем в ID значение времени звонка

	stringForId = dateStringForId + time;

	// ----Формируем 4-х значный хвост ID-шки вида на основе порядкового
	// номера в ArrayList

	numHourRecod += 1;

	// Debug.log.info("prevTimeValueInStringForId = " +
	// prevTimeValueInStringForId);

	// String currentHour = new java.text.SimpleDateFormat("HH").format(new
	// java.util.Date()); //---Получаем текущий час
	// Debug.log.info("time.substring(0,2) =" + time.substring(0,2));

	if (!prevTimeValueInStringForId.equals(time.substring(0, 2))) // Если
								      // час в
								      // предыдущей
								      // cdr
								      // записи
								      // другой,
								      // то
								      // сбрасываем
								      // номер
								      // записи
								      // в ID на
								      // 1-ку
	    numHourRecod = 1;

	String stringNum = String.valueOf(numHourRecod);
	if (stringNum.length() == 1)
	    stringNum = "000" + stringNum;
	else if (stringNum.length() == 2)
	    stringNum = "00" + stringNum;
	else if (stringNum.length() == 3)
	    stringNum = "0" + stringNum;

	// ---Добавляем номер записи в ID (формат хххх)

	stringForId += stringNum;

	return formattedTime;
    }

    /**
     * Метод для форматирования длительности звонка и приведения его к виду
     * h:mm:ss.
     * 
     * @param duration
     *            - Неформатированная строка вида hmmss
     */
    private String getFormattedDurationFromHMMSS(String duration) {
	String formattedDuration = duration;
	if (formattedDuration.matches("[0-9]{4}")) {
	    // ---Переводим длительность из формата hmmx в h:mm:ss
	    String durationSec = String.valueOf((Integer.valueOf(duration.substring(3, 4).toString()) * 6));
	    // ---Добавляем 0 для 2-х значного отображения длительности
	    if (durationSec.length() == 1)
		durationSec = "0" + durationSec;
	    formattedDuration = duration.substring(0, 1) + ":" + duration.substring(1, 3) + ":" + durationSec;

	} else {
	    formattedDuration = "0:00:00";
	    Debug.log.error("Cdr field Duration consists not only digits : " + duration);
	}
	return formattedDuration;
    }

    /**
     * Метод для получения из строки длительности целого значения длительности
     * разговора в секундах
     * 
     * @param duration
     *            (формат hmmss)
     * @return N секунд.
     */
    private int getDurationSeconds(String duration) {

	return Integer.valueOf(duration.substring(3, 4).toString()) * 6
		+ Integer.valueOf(duration.substring(1, 3).toString()) * 60
		+ Integer.valueOf(duration.substring(0, 1).toString()) * 360;

    }

    // ************************************************************************************************************
    // ===Метод для определения типа вызова (направления)

    private String getDirection(String cond_code, String code_used, String in_trk_code) {
	// CoditionCodeLocal=0 -флаг локального звонка
	// CoditionCodeIncoming=9,B -флаг входящего звонка
	// CoditionCodeOutgoing=1,7,A,4,O -флаг исходящего звонка
	// CoditionCodeError=6,E,F,G,H,I -флаг неудавшегося звонка
	// CoditionCodeConference=C -флаг конференции

	switch (cond_code) {
	case "0":
	    return "внутренний";

	case "9":
	    if (!code_used.trim().isEmpty())
		return "транзитный";
	    else
		return "входящий";

	case "B":
	    if (!code_used.trim().isEmpty())
		return "транзитный";
	    else
		return "входящий";

	case "1":
	    return "исходящий";

	case "7":
	    return "исходящий";

	case "A":
	    return "исходящий";

	case "4":
	    return "исходящий";

	case "O":
	    return "конференция";

	case "6":
	    if (!code_used.trim().isEmpty() && !in_trk_code.trim().isEmpty())
		return "транзитный";
	    else
		return "несостоявшийся";

	case "E":
	    if (!code_used.trim().isEmpty() && !in_trk_code.trim().isEmpty())
		return "транзитный";
	    else
		return "несостоявшийся";

	case "F":
	    if (!code_used.trim().isEmpty() && !in_trk_code.trim().isEmpty())
		return "транзитный";
	    else
		return "несостоявшийся";

	case "G":
	    if (!code_used.trim().isEmpty() && !in_trk_code.trim().isEmpty())
		return "транзитный";
	    else
		return "несостоявшийся";

	case "H":
	    if (!code_used.trim().isEmpty() && !in_trk_code.trim().isEmpty())
		return "транзитный";
	    else
		return "несостоявшийся";

	case "I":
	    if (!code_used.trim().isEmpty() && !in_trk_code.trim().isEmpty())
		return "транзитный";
	    else
		return "несостоявшийся";

	case "C":
	    return "конференция";

	}
	return cond_code;
    }

    // ************************************************************************************************************
    // ===
    private ImageForCallType getImg(String cond_code, String code_used, String in_trk_code) {
	ImageForCallType img = new ImageForCallType();

	switch (cond_code) {
	case "0":
	    img.setFileImageTypeName("internal.png");
	    break;

	case "9":
	    if (!code_used.trim().isEmpty())
		img.setFileImageTypeName("transitOk_16.png");
	    else
		img.setFileImageTypeName("incoming2_16.png");
	    break;

	case "B":
	    if (!code_used.trim().isEmpty())
		img.setFileImageTypeName("transitOk_16.png");
	    else
		img.setFileImageTypeName("incoming2_16.png");
	    break;

	case "1":
	    img.setFileImageTypeName("outgoing2_16.png");
	    break;

	case "7":
	    img.setFileImageTypeName("outgoing2_16.png");
	    break;

	case "A":
	    img.setFileImageTypeName("outgoing2_16.png");
	    break;

	case "4":
	    img.setFileImageTypeName("outgoing2_16.png");
	    break;

	case "O":
	    img.setFileImageTypeName("conference16.png");
	    break;

	case "6":
	    if (!code_used.trim().isEmpty() && !in_trk_code.trim().isEmpty())
		img.setFileImageTypeName("transitFail_16.png");
	    else
		img.setFileImageTypeName("noanswer_16.png");
	    break;

	case "E":
	    if (!code_used.trim().isEmpty() && !in_trk_code.trim().isEmpty())
		img.setFileImageTypeName("transitFail_16.png");
	    else
		img.setFileImageTypeName("noanswer_16.png");
	    break;

	case "F":
	    if (!code_used.trim().isEmpty() && !in_trk_code.trim().isEmpty())
		img.setFileImageTypeName("transitFail_16.png");
	    else
		img.setFileImageTypeName("noanswer_16.png");
	    break;

	case "G":
	    if (!code_used.trim().isEmpty() && !in_trk_code.trim().isEmpty())
		img.setFileImageTypeName("transitFail_16.png");
	    else
		img.setFileImageTypeName("noanswer_16.png");
	    break;

	case "H":
	    if (!code_used.trim().isEmpty() && !in_trk_code.trim().isEmpty())
		img.setFileImageTypeName("transitFail_16.png");
	    else
		img.setFileImageTypeName("noanswer_16.png");
	    break;

	case "I":
	    if (!code_used.trim().isEmpty() && !in_trk_code.trim().isEmpty())
		img.setFileImageTypeName("transitFail_16.png");
	    else
		img.setFileImageTypeName("noanswer_16.png");
	    break;

	case "C":
	    img.setFileImageTypeName("conference16.png");
	    break;
	}

	return img;
    }

    // ************************************************************************************************************
    // ===Метод проверяет, что поле Имя АТС не пустое.

    private boolean checkStatement() {

	if (!txtNamePBX.getText().isEmpty() && portAvalable
		&& !cmbProtocol.getSelectionModel().getSelectedItem().isEmpty()
		&& ((txtIPForControl.getText().equals("")) || (txtIPForControl.getText()
			.matches("((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)")))) {
	    tglGo.setDisable(false);
	    return true;
	} else {
	    tglGo.setDisable(true);
	    return false;
	}
    }

    // ************************************************************************************************************
    // ===Метод проверки порта на соответствие диапазону и на незанятость в
    // системе другими приложениями

    private void checkPort() {

	String stringPort = txtPort.getText();

	if (stringPort.isEmpty())

	    stringPort = "0";

	int port = Integer.parseInt(stringPort);

	if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {

	    portAvalable = false;
	    txtPort.setStyle(Main.colorNotAvalable);
	    Debug.log.debug("Port " + String.valueOf(port) + " is out of range.");

	} else {
	    ServerSocket testSocket = null;

	    try {
		testSocket = new ServerSocket(port);
		testSocket.setReuseAddress(true);
		Debug.log.debug("Port " + String.valueOf(port) + " is avaliable ");
		portAvalable = true;
		txtPort.setStyle(Main.colorAvalable);

	    } catch (IOException e) {
		Debug.log.debug("Port  " + String.valueOf(port) + " is using now.");
		portAvalable = false;
		txtPort.setStyle(Main.colorNotAvalable);

	    }

	    finally {
		if (testSocket != null) {
		    try {
			testSocket.close();
		    } catch (IOException e) {
		    }
		}
	    }

	}

	checkStatement();
    }
    // ************************************************************************************************************
    // ===Метод проверяет наличие текста в поле Название АТС, и если оно пустое,
    // то меняет цвет и инициирует и меняет состояние Вкл/Выкл

    private void checkTextNamePbx() {
	String strTextNamePbx = txtNamePBX.getText();

	Debug.log.debug("txtNamePBX =" + strTextNamePbx);

	if (strTextNamePbx.length() < 3)
	    txtNamePBX.setStyle(Main.colorNotAvalable);

	else
	    txtNamePBX.setStyle(Main.colorAvalable);

	checkStatement();
    }

    // ************************************************************************************************************
    // === Проверка и подсветка checkbox-а выбора протокола. Если значение в нем
    // нет, то красный. Если есть - то зеленый.

    private void checkComboBox() {
	if (cmbProtocol.getSelectionModel().getSelectedItem().isEmpty())
	    cmbProtocol.setStyle(Main.colorNotAvalable);

	else
	    cmbProtocol.setStyle(Main.colorAvalable);

	checkStatement();

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
	String textIp = txtIPForControl.getText();

	if ((textIp.equals(""))
		|| (textIp.matches("((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)"))) {
	    txtIPForControl.setStyle(Main.colorAvalable);
	    checkStatement();
	} else
	    txtIPForControl.setStyle(Main.colorNotAvalable);

	checkStatement();
    }

    // ************************************************************************************************************
    // ===Ограничение на ввод в поле Порт только цифр, не начинающихся с нуля и
    // длиной не более 5 символов.

    @FXML
    private void txtIpkeyTyped(KeyEvent event) {

	Debug.log.debug("Event source :" + event.getSource() + " Event type :" + event.getEventType()
		+ " Event character :" + event.getCharacter());

	if ((txtIPForControl.getText().length() < 15) && (event.getCharacter().equals("1")
		|| event.getCharacter().equals("2") || event.getCharacter().equals("3")
		|| event.getCharacter().equals("4") || event.getCharacter().equals("5")
		|| event.getCharacter().equals("6") || event.getCharacter().equals("7")
		|| event.getCharacter().equals("8") || event.getCharacter().equals("9")
		|| event.getCharacter().equals("0") || event.getCharacter().equals("."))) {
	    if (txtIPForControl.getText().equals("") && (event.getCharacter().equals("0")))
		event.consume(); // =============Проверка на введение только
				 // одного первого нуля
	    if (txtIPForControl.getText().equals("") && (event.getCharacter().equals(".")))
		event.consume(); // =============Проверка на введение первым
				 // символом "точки"
	    if (event.getCharacter().equals("."))
		if (txtIPForControl.getText().endsWith("."))
		    event.consume(); // =============Проверка на введение подряд
				     // более одного символа "точки"

	    Debug.log.debug("event.getCharacter() " + event.getCharacter());

	} else
	    event.consume();

    }

    // ************************************************************************************************************
    // ===
    private void playSound(String sound) {
	try {
	    Media media = new Media(getClass().getResource("../sources/" + sound).toString());
	    MediaPlayer mediaPlayer = new MediaPlayer(media);
	    mediaPlayer.play();
	} catch (NullPointerException event) {
	    Debug.log.error("No file sound found :" + sound);
	}
    }

    // ************************************************************************************************************
    // ===Геттер используется для записи состояния переключателя в файл
    // пользовательских настроек

    public boolean getRunCdrCollectorFlag() {
	if (tglGo.isSelected())
	    return true;
	else
	    return false;
    }

    public File getCdrFile() {
	return cdrFile;
    }

    public File getCdrFormattedFile() {
	return cdrFormattedFile;
    }
    // ************************************************************************************************************
    // ===Геттер для флага enableRunCdrCollectDbFlag используется в
    // rootController

    public boolean getEnableRunCdrCollectDbFlag() {
	return enableRunCdrCollectDbFlag;
    }

    // ************************************************************************************************************
    // ===Геттер для флага checkStatement() используется в rootController

    public boolean getValidValuePbxFieldsFlag() {
	return checkStatement();
    }

}
