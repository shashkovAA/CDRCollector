package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import objects.GlobalVars;
import objects.Paths;
import objects.ProtocolField;

public class ProtocolFieldsInits {

    /** �������� ������-����������� � ������ ������������� ���������� */
    static String fieldNameValueSeparator = GlobalVars.protocolFilesFieldNameValueSeparatorIn;

    /**
     * ����� ��������� ��������� HashMap ��� ���������� �� ������ � �����
     * Protocols.���� � ����� ������ ���������� ���, �� ���������
     * ����������������� �� ����� ������������. ��� ���������� ����� ���
     * ��������� �� hard-����.
     */

    public static HashMap<String, ArrayList<ProtocolField>> getHMapProtocols() {

	ArrayList<String> listProtocolFiles = new ArrayList<>();
	ArrayList<ProtocolField> fileCollection = new ArrayList<>(50);

	HashMap<String, ArrayList<ProtocolField>> mapProtocols = new HashMap<>();

	HashMap<String, ArrayList<ProtocolField>> tempMapProtocols = new HashMap<>();

	listProtocolFiles = getListInitFilesNamesInProtocolsFolder(); // ----------��������
	// ������ ������ (����)
	// ����������

	if (listProtocolFiles.isEmpty()) {
	    Debug.log.warn("List protocol files *.ini is empty. ProtocolMap will recreate from backup file.");

	    mapProtocols = restoreMapFromFile(); // -------���������������
						 // ��������� �� ����� .ser

	    if (mapProtocols.isEmpty()) // ----���� ���� ��� �������������� ���
					// �� ������, �� �������� ������
					// ��������� (������ ����������). ����
					// ��� ��� �������� �������������.
					// ������� ��������� ����� � ��� �� ���
					// ����� ����� ������������� ������
					// ����������
	    {
		Debug.log.warn(
			"Backup file is empty! Run method initialization files unformatted.ini and CustomizedFields.ini");

		defaultInits(); // ------����� ��� ������������� ������
				// ��������� Unformatted � ����� �������� �����
				// Customized �� hard-����

	    } else {
		restoreFilesFromMap(mapProtocols); // ��������������� �����
						   // ���������� �� ������������
						   // ��������� (������
						   // ����������)
	    }
	}

	for (String fileName : listProtocolFiles) // --------��������� ���������
						  // ���������� �� ������
	{
	    String fileShortName = fileName.substring(0, fileName.toString().length() - 4);

	    fileCollection = getArrayListFromFile(
		    /* fileShortName.toLowerCase() */fileName, fieldNameValueSeparator);

	    mapProtocols.put(fileShortName, fileCollection);

	    Debug.log.info("Protocol " + fileShortName + " add in protocol list.");

	}

	// ���� ������ ����������, ������� ������� �� ���� ������ � �����
	// protocols, ������ ��� � ������������ �� ����� backup-� ���������, ��
	// ������� ����������� ��� ����� � ������� ��� � �����.

	tempMapProtocols = restoreTempMapFromFile(); // ====���������������
						     // �������� ��������� ��
						     // ���������� ����� ���
						     // ��������� � ����������,
						     // ���������� �����������
						     // ������ ���������� ��
						     // �����.

	if (mapProtocols.size() < tempMapProtocols.size()) // ===���� �
							   // ���������
							   // ����������
							   // ����������� ������
							   // ���������
							   // ��������� ������,
							   // ��� �
							   // ���������������,
							   // ������ ����-��
							   // ������ �����
							   // ����������. �
							   // �����������-�� ��.
	{
	    compareProtocolMaps(mapProtocols, tempMapProtocols); // ����� ���
								 // ������
								 // ������������
								 // �����
								 // ��������� �
								 // �����������
								 // ���.

	    return tempMapProtocols;
	} else {
	    return mapProtocols;
	}

    }

    // ************************************************************************************************
    // ==================����� ��� ��������� ��������� ���� ������ �� �����
    // Protocols

    private static ArrayList<String> getListInitFilesNamesInProtocolsFolder() {
	ArrayList<String> listProtocolFiles = new ArrayList<>();// --------���������
								// ����
								// ���������� �
								// �����
								// /Protocols ��
								// ������
								// ������.

	File protocolDir = new File(Paths.protocolDirPath);

	Debug.log.info("Get list of files in directory " + Paths.viewOnlyprotocolDirPath);

	for (File item : protocolDir.listFiles()) {
	    if (item.isFile()) {
		if (item.getName().endsWith(".ini")) // --------���������� ����,
						     // ���� ��� ���������� ��
						     // .ini
		{
		    if (item.length() != 0) // -----------���������� ����, ����
					    // �� ������
		    {
			listProtocolFiles.add(item.getName());

			Debug.log.info("File :" + item.getName() + "   Seize :" + item.length());

		    } else
			Debug.log.warn("File :" + item.getName() + "   Seize :" + item.length()
				+ ". This file will be ignored. It is empty.");
		} else
		    Debug.log.warn("File :" + item.getName() + "   Seize :" + item.length()
			    + ". This file will be ignored. It is not *.ini file.");
	    }
	}

	return listProtocolFiles;
    }

    // ***************************************************************************************************************************
    // ==================����� �������������� ���������(������ ����������)
    // ���������� �� �����======================

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static HashMap<String, ArrayList<ProtocolField>> restoreMapFromFile() {
	HashMap<String, ArrayList<ProtocolField>> mMapProtocols = new HashMap<>();

	try (ObjectInputStream oIn = new ObjectInputStream(new FileInputStream(Paths.serFilePathName));)// ==============���������������
													// ���������
													// ��
													// �����
													// �����������
	{
	    mMapProtocols = (HashMap) oIn.readObject();

	    Debug.log.warn("Protocol list was rescreated from backup file " + Paths.viewOnlyserFilePathName);

	} catch (ClassNotFoundException cnf) {
	    Debug.log.error(cnf.getMessage());
	} catch (IOException ioe) {
	    Debug.log.error(ioe.getMessage());
	}

	return mMapProtocols;

    }

    // **********************************************************************************************************
    // ======================����� ��� ������������� ������ ���������
    // Unformatted � ����� �������� ����� Customized �� hard-����

    public static void defaultInits()

    {
	initUnformattedFile();
	initCustomizedFile();
    }

    // ***********************************************************************************************************

    public static void initUnformattedFile() {
	ArrayList<ProtocolField> DefaultUnformattedList = new ArrayList<>(30);

	String mUnformattedFile = Paths.protocolUnformattedFileName;

	File UnformattedFile = new File(mUnformattedFile);

	try {
	    if (UnformattedFile.exists())
		UnformattedFile.delete();

	    UnformattedFile.createNewFile();
	    Debug.log.info("Create file " + Paths.viewOnlyprotocolUnformattedFileName);

	} catch (IOException e) {
	    Debug.log.error(e.getMessage());
	}

	DefaultUnformattedList = createUnformattedList(); // =====�������
							  // ��������� ���
							  // Unformatted �
							  // ������� ����
							  // unformatted.ini

	try (BufferedWriter bufferOut = new BufferedWriter(new FileWriter(mUnformattedFile));) // =====����������
											       // ������
											       // ��
											       // ���������,
											       // �������
											       // ���������
											       // �
											       // ��,
											       // �
											       // ����
											       // unformatted.ini.
	{
	    for (ProtocolField it : DefaultUnformattedList) {
		bufferOut.write(it.getName() + fieldNameValueSeparator + it.getLength() + Paths.lineSeparator);

		Debug.log.debug(it.getName() + fieldNameValueSeparator + it.getLength());
	    }

	} catch (FileNotFoundException fnfe) {
	    Debug.log.error(fnfe.getMessage());
	} catch (IOException ioe) {
	    Debug.log.error(ioe.getMessage());
	}

    }

    // ***********************************************************************************************************

    public static void initCustomizedFile() {
	ArrayList<ProtocolField> CustomizedFieldsList = new ArrayList<>(50);

	String mCusromFile = Paths.protocolCustomizedFileName;

	File CustomizedFile = new File(mCusromFile);

	try {

	    if (CustomizedFile.exists())
		CustomizedFile.delete();

	    CustomizedFile.createNewFile();
	    Debug.log.info("Create file " + Paths.viewOnlyprotocolCustomizedFileName);

	} catch (IOException e) {
	    Debug.log.error(e.getMessage());
	}

	CustomizedFieldsList = createCustomizedList(); // =====������� ���������
						       // Customized.

	try (BufferedWriter bufferOut = new BufferedWriter(new FileWriter(mCusromFile));) // =====����������
											  // ������
											  // ��
											  // ���������
											  // �
											  // ����
											  // CustomizedFields.ini.
											  // ����
											  // ����
											  // �����
											  // ��������������
											  // ���
											  // ������
											  // ���
											  // ��������
											  // ������
											  // ���������.
	{
	    for (ProtocolField it : CustomizedFieldsList) {
		bufferOut.write(it.getName() + fieldNameValueSeparator + it.getLength() + Paths.lineSeparator);
		Debug.log.debug(it.getName() + fieldNameValueSeparator + it.getLength());
	    }

	} catch (FileNotFoundException evt) {
	    Debug.log.error(evt.getMessage());
	} catch (IOException ev) {
	    Debug.log.error(ev.getMessage());
	}

    }

    // *************************************************************************************************
    // ======================����� ��� �������� ��������� ����� ��� �������
    // Unformatted

    private static ArrayList<ProtocolField> createUnformattedList() {
	ArrayList<ProtocolField> DefaultUnformattedList = new ArrayList<>(30);

	DefaultUnformattedList.add(new ProtocolField("time", "4"));
	DefaultUnformattedList.add(new ProtocolField("duration", "4"));
	DefaultUnformattedList.add(new ProtocolField("cond_code", "1"));
	DefaultUnformattedList.add(new ProtocolField("code_dial", "4"));
	DefaultUnformattedList.add(new ProtocolField("code_used", "4"));
	DefaultUnformattedList.add(new ProtocolField("dialed_num", "15"));
	DefaultUnformattedList.add(new ProtocolField("calling_num", "10"));
	DefaultUnformattedList.add(new ProtocolField("acct_code", "15"));
	DefaultUnformattedList.add(new ProtocolField("auth_code", "7"));
	DefaultUnformattedList.add(new ProtocolField("space", "2"));
	DefaultUnformattedList.add(new ProtocolField("frl", "1"));
	DefaultUnformattedList.add(new ProtocolField("in_crt_id", "3"));
	DefaultUnformattedList.add(new ProtocolField("out_crt_id", "3"));
	DefaultUnformattedList.add(new ProtocolField("feat_flag", "1"));
	DefaultUnformattedList.add(new ProtocolField("attd_console", "2"));
	DefaultUnformattedList.add(new ProtocolField("in_trk_code", "4"));
	DefaultUnformattedList.add(new ProtocolField("node_num", "2"));
	DefaultUnformattedList.add(new ProtocolField("ins", "3"));
	DefaultUnformattedList.add(new ProtocolField("ixc_code", "3"));
	DefaultUnformattedList.add(new ProtocolField("bcc", "1"));
	DefaultUnformattedList.add(new ProtocolField("ma_uui", "1"));
	DefaultUnformattedList.add(new ProtocolField("res_flag", "1"));
	DefaultUnformattedList.add(new ProtocolField("tsc_ct", "4"));

	return DefaultUnformattedList;
    }

    // *************************************************************************************************
    // ======================����� ��� �������� ��������� ��� Customized �����

    private static ArrayList<ProtocolField> createCustomizedList() {
	ArrayList<ProtocolField> CustomizedFieldsList = new ArrayList<>(50);

	CustomizedFieldsList.add(new ProtocolField("acct_code", "15"));
	CustomizedFieldsList.add(new ProtocolField("attd_console", "2"));
	CustomizedFieldsList.add(new ProtocolField("auth_code", "7"));
	CustomizedFieldsList.add(new ProtocolField("bandwidth", "2"));
	CustomizedFieldsList.add(new ProtocolField("bcc", "1"));
	CustomizedFieldsList.add(new ProtocolField("calling_num", "15"));
	CustomizedFieldsList.add(new ProtocolField("calltype", "1"));
	CustomizedFieldsList.add(new ProtocolField("clg_num_in_tac", "1"));
	CustomizedFieldsList.add(new ProtocolField("clg_pty_cat", "2"));
	CustomizedFieldsList.add(new ProtocolField("code_dial", "4"));
	CustomizedFieldsList.add(new ProtocolField("code_used", "4"));
	CustomizedFieldsList.add(new ProtocolField("cond_code", "1"));
	CustomizedFieldsList.add(new ProtocolField("contract_uri", "20"));
	CustomizedFieldsList.add(new ProtocolField("date", "6"));
	CustomizedFieldsList.add(new ProtocolField("dialed_num", "23"));
	CustomizedFieldsList.add(new ProtocolField("duration", "4"));
	CustomizedFieldsList.add(new ProtocolField("end_date_4d", "4"));
	CustomizedFieldsList.add(new ProtocolField("end_time", "6"));
	CustomizedFieldsList.add(new ProtocolField("feat_flag", "1"));
	CustomizedFieldsList.add(new ProtocolField("frl", "1"));
	CustomizedFieldsList.add(new ProtocolField("from_uri", "20"));
	CustomizedFieldsList.add(new ProtocolField("in_crt_id", "3"));
	CustomizedFieldsList.add(new ProtocolField("in_trk_code", "4"));
	CustomizedFieldsList.add(new ProtocolField("ins", "3"));
	CustomizedFieldsList.add(new ProtocolField("isdn_cc", "11"));
	CustomizedFieldsList.add(new ProtocolField("ixc_code", "4"));
	CustomizedFieldsList.add(new ProtocolField("line_feed", "1"));
	CustomizedFieldsList.add(new ProtocolField("ma_uui", "1"));
	CustomizedFieldsList.add(new ProtocolField("node_num", "2"));
	CustomizedFieldsList.add(new ProtocolField("null", "1"));
	CustomizedFieldsList.add(new ProtocolField("out_crt_id", "3"));
	CustomizedFieldsList.add(new ProtocolField("ppm", "5"));
	CustomizedFieldsList.add(new ProtocolField("request_uri", "20"));
	CustomizedFieldsList.add(new ProtocolField("res_flag", "1"));
	CustomizedFieldsList.add(new ProtocolField("return", "1"));
	CustomizedFieldsList.add(new ProtocolField("secduration", "5"));
	CustomizedFieldsList.add(new ProtocolField("space", "1"));
	CustomizedFieldsList.add(new ProtocolField("start_date", "6"));
	CustomizedFieldsList.add(new ProtocolField("start_date_4d", "8"));
	CustomizedFieldsList.add(new ProtocolField("start_time", "6"));
	CustomizedFieldsList.add(new ProtocolField("time", "4"));
	CustomizedFieldsList.add(new ProtocolField("to_uri", "20"));
	CustomizedFieldsList.add(new ProtocolField("tsc_ct", "4"));
	CustomizedFieldsList.add(new ProtocolField("tsc_flag", "1"));
	CustomizedFieldsList.add(new ProtocolField("vdn", "13"));
	// CustomizedFieldsList.add(new ProtocolField("spaces1", "1"));

	return CustomizedFieldsList;
    }

    // *********************************************************************************************************
    // ==============================����� �� ����������� ������ ���������� ��
    // HashMap

    private static void restoreFilesFromMap(HashMap<String, ArrayList<ProtocolField>> mMapProtocols) {

	for (HashMap.Entry<String, ArrayList<ProtocolField>> it : mMapProtocols.entrySet()) {

	    Debug.log.warn("Recreate protocol file " + it.getKey() + ".ini.");
	    createProtocolFiles(it.getKey(), it.getValue());

	}

    }

    // **********************************************************************************************************

    private static void createProtocolFiles(String mProtocolName, ArrayList<ProtocolField> mProtocolFieldsList)

    {
	String mProtocolFile = Paths.protocolDirPath + mProtocolName + ".ini";

	File ProtocolFile = new File(mProtocolFile);

	try {
	    if (ProtocolFile.exists())
		ProtocolFile.delete();

	    ProtocolFile.createNewFile();
	    Debug.log.warn("Recreate file " + Paths.viewOnlyprotocolFileName + mProtocolName + ".ini");

	} catch (IOException e) {
	    Debug.log.error(e.getMessage());
	}

	try (BufferedWriter bufferOut = new BufferedWriter(new FileWriter(mProtocolFile));) // =====����������
											    // ������
											    // ��
											    // ���������,
											    // �������
											    // ���������
											    // �
											    // ��,
											    // �
											    // ����
											    // unformatted.ini.
	{
	    for (ProtocolField it : mProtocolFieldsList) {
		bufferOut.write(it.getName() + fieldNameValueSeparator + it.getLength() + Paths.lineSeparator);
		Debug.log.debug(it.getName() + fieldNameValueSeparator + it.getLength());
	    }
	} catch (FileNotFoundException fnfe) {
	    Debug.log.error(fnfe.getMessage());
	} catch (IOException ioe) {
	    Debug.log.error(ioe.getMessage());
	}
    }

    // ************************************************************************************************
    // ==================================����� �������� �������� �����
    // ����������� � ��������� �� �����, ���� �� ����������=======//

    private static ArrayList<ProtocolField> getArrayListFromFile(String mFileName, String mDelimiter) {

	String readline = "";
	String[] myFields = new String[2]; // ----------������������ ���
					   // ���������� �� ���� (����� �
					   // ��������) ��� ����������� ��
					   // ��������� �����

	ArrayList<ProtocolField> mCollection = new ArrayList<>(50); // ----------������������
								    // ������������
								    // ���������
	ArrayList<ProtocolField> mEmptyCollection = new ArrayList<>(50); // ----------������
									 // ���������.
									 // ������������
									 // ����
									 // �����������
									 // �����-����
									 // ����������

	Debug.log.debug("Prtocol fields from file " + mFileName + ":");

	try (BufferedReader buffer = new BufferedReader(new FileReader(Paths.protocolDirPath + mFileName));) {
	    while ((readline = buffer.readLine()) != null) {
		if (!readline.isEmpty()) {
		    myFields = readline.split(mDelimiter);
		    mCollection.add(new ProtocolField(myFields[0], myFields[1]));
		    Debug.log.debug(readline);
		}
	    }

	    return mCollection;
	}

	catch (FileNotFoundException e) {
	    Debug.log.error(e.getMessage());
	    return mEmptyCollection;

	}

	catch (IOException e) {
	    Debug.log.error(e.getMessage());
	    return mEmptyCollection; // ------���� ����������, �� ����������
				     // ������ ���������.

	}

    }

    // ***************************************************************************************************************************
    // ==================����� �������������� ���������(������ ����������)
    // ���������� �� �����======================

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static HashMap<String, ArrayList<ProtocolField>> restoreTempMapFromFile() {
	HashMap<String, ArrayList<ProtocolField>> mMapProtocols = new HashMap<>();

	try (ObjectInputStream oIn = new ObjectInputStream(new FileInputStream(Paths.serFilePathName));)// ==============���������������
													// ���������
													// ��
													// �����
													// �����������
	{
	    mMapProtocols = (HashMap) oIn.readObject();

	} catch (ClassNotFoundException cnf) {
	    Debug.log.error(cnf.getMessage());
	} catch (IOException ioe) {
	    Debug.log.error(ioe.getMessage());
	}

	return mMapProtocols;

    }

    // *******************************************************************************************************************************************
    // ======����� ��� ������ �������������� ����� ��������� � ����������� ���

    private static void compareProtocolMaps(HashMap<String, ArrayList<ProtocolField>> mMapProtocols,
	    HashMap<String, ArrayList<ProtocolField>> mTempMapProtocols) {

	for (HashMap.Entry<String, ArrayList<ProtocolField>> it : mTempMapProtocols.entrySet()) {
	    if (mMapProtocols.get(it.getKey()) == null) {
		Debug.log.warn("No protocol file found " + it.getKey() + ".ini");

		createProtocolFiles(it.getKey(), it.getValue()); // ======�����
								 // ��
								 // �����������
								 // ������������
								 // �����
	    }

	}

    }

}
