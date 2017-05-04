package objects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Класс представляет собой тип HashMap<String, Protocol> для хранения пар
 * значений : имя протокола(String) - протокол(Protocol).
 */

public class MapProtocols implements Serializable {

    private HashMap<String, Protocol> mapProtocols;
    private HashMap<String, Protocol> tempMap;
    private ArrayList<String> listInitFiles;
    private Protocol protocolFromFile;

    public MapProtocols() {
	mapProtocols = new HashMap<String, Protocol>();
	tempMap = new HashMap<String, Protocol>();
	ProtocolFilesList protocolFilesList = new ProtocolFilesList(GlobalVars.protocolFilesTrail);
	listInitFiles = new ArrayList<String>(protocolFilesList.get());

	createMapProtocols();
    }

    public void createMapProtocols() {
	if (listInitFiles.isEmpty()) {
	    Logging.warn("List of protocol files *.ini is empty. ProtocolMap was recreated from backup file.");
	    mapProtocols = restoreMapFromBackupFile();

	    if (mapProtocols.isEmpty()) {
		Logging.warn(
			"Backup file is empty! Run method initialization files unformatted.ini and CustomizedFields.ini");
		createProtocolFileFromHardcode(Paths.protocolUnformattedFileName, UnformattedProtocol.get());
		createProtocolFileFromHardcode(Paths.protocolCustomizedFileName, CustomizedProtocol.get());

	    } else
		restoreProtocolFilesFromCurrentMapProtocols(mapProtocols);
	}

	fillMapProtocols();

	tempMap = restoreMapFromBackupFile();
	if (mapProtocols.size() < tempMap.size()) {
	    compareProtocolMapsAndRecreateNotFindedProtocol(mapProtocols, tempMap);
	    mapProtocols = tempMap;
	}
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Protocol> restoreMapFromBackupFile() {
	HashMap<String, Protocol> mMapProtocols = new HashMap<String, Protocol>();

	try (ObjectInputStream oIn = new ObjectInputStream(new FileInputStream(Paths.serFilePathName));) {

	    mMapProtocols = (HashMap<String, Protocol>) oIn.readObject();

	} catch (ClassNotFoundException cnf) {
	    Logging.error(cnf.getMessage());
	} catch (IOException ioe) {
	    Logging.error(ioe.getMessage());
	}
	return mMapProtocols;
    }

    public void createProtocolFileFromHardcode(String fileName, Protocol protocol) {
	File file = new File(fileName);

	createFile(file);
	Logging.info("Create file " + file.getName());

	saveProtocolToFile(fileName, protocol);
    }

    public void createFile(File file) {
	try {
	    if (file.exists())
		file.delete();
	    file.createNewFile();
	} catch (IOException e) {
	    Logging.error(e.getMessage());
	}
    }

    public void saveProtocolToFile(String fileNameWithPath, Protocol protocol) {
	try (BufferedWriter bufferOut = new BufferedWriter(new FileWriter(fileNameWithPath));) {
	    for (ProtocolField field : protocol.get()) {
		bufferOut.write(field.getName() + GlobalVars.protocolFilesFieldNameValueSeparatorIn + field.getLength()
			+ Paths.lineSeparator);
		Logging.debug(field.getName() + GlobalVars.protocolFilesFieldNameValueSeparatorIn + field.getLength());
	    }

	} catch (FileNotFoundException fnfe) {
	    Logging.error(fnfe.getMessage());
	} catch (IOException ioe) {
	    Logging.error(ioe.getMessage());
	}
    }

    public void fillMapProtocols() {
	for (String fileName : listInitFiles) {
	    protocolFromFile = getProtocolFromFile(fileName, GlobalVars.protocolFilesFieldNameValueSeparatorIn);
	    mapProtocols.put(getShortNameFile(fileName), protocolFromFile);
	    Logging.info("Protocol " + getShortNameFile(fileName) + " add in protocol list.");
	}
    }

    public void compareProtocolMapsAndRecreateNotFindedProtocol(HashMap<String, Protocol> mMapProtocols,
	    HashMap<String, Protocol> mTempMapProtocols) {

	for (HashMap.Entry<String, Protocol> it : mTempMapProtocols.entrySet()) {
	    if (mMapProtocols.get(it.getKey()) == null) {
		Logging.warn("No protocol file found " + it.getKey() + ".ini");
		createProtocolFile(it.getKey(), it.getValue());
	    }
	}
    }

    public String getShortNameFile(String fileNameWithTrail) {
	String fileShortName = fileNameWithTrail.substring(0, fileNameWithTrail.indexOf('.'));
	return fileShortName;
    }

    public Protocol getProtocolFromFile(String fileName, String mDelimiter) {

	String readline = "";
	ProtocolField pField = new ProtocolField();
	Protocol cdrProtocol = new Protocol();

	Logging.debug("Protocol fields from file " + fileName + ":");

	try (BufferedReader buffer = new BufferedReader(new FileReader(Paths.protocolDirPath + fileName));) {
	    while ((readline = buffer.readLine()) != null) {
		Logging.debug(readline);
		pField = parseReadLineInProtocolFile(readline, mDelimiter);
		if (isProtocolFieldHasNameAndValue(pField))
		    if (!isDuplicateFieldNameInProtocol(cdrProtocol, pField))
			cdrProtocol.add(pField);
	    }
	    return cdrProtocol;

	} catch (FileNotFoundException e) {
	    Logging.error(e.getMessage());
	    return new Protocol();
	} catch (IOException e) {
	    Logging.error(e.getMessage());
	    return new Protocol();
	}

    }

    public boolean isProtocolFieldHasNameAndValue(ProtocolField pField) {
	return !pField.getName().isEmpty() && !pField.getLength().isEmpty();
    }

    public boolean isDuplicateFieldNameInProtocol(Protocol cdrProtocol, ProtocolField pField) {
	boolean duplicateFieldFlag = false;

	for (int i = 0; i < cdrProtocol.size(); i++) {
	    if (cdrProtocol.getProtocolField(i).getName().equals(pField.getName()))
		duplicateFieldFlag = true;
	}
	return duplicateFieldFlag;
    }

    public ProtocolField parseReadLineInProtocolFile(String line, String delimiter) {
	ProtocolField mField = new ProtocolField();
	line = checkLineOnContainInvalidDelimiter(line, delimiter);
	line = checkLineOnContainOnlyNameOfProtocolField(line, delimiter);
	if (!line.isEmpty()) {
	    mField.setNameValue(line.split(delimiter));
	    checkFieldsOnContainSpaces(mField);
	    checkFieldsOnContainInvalidValues(mField);

	} else {
	    // Logging.warn("A blank line in protocol file is being ignored!");
	    mField.setName("");
	    mField.setLength("");
	}
	return mField;
    }

    private String checkLineOnContainInvalidDelimiter(String line, String delimiter) {
	if (!line.endsWith(delimiter))
	    return line;
	else
	    return "";
    }

    private String checkLineOnContainOnlyNameOfProtocolField(String line, String delimiter) {
	if (line.contains(delimiter))
	    return line;
	else
	    return "";
    }

    private void checkFieldsOnContainSpaces(ProtocolField pField) {
	pField.setName(pField.getName().trim());
	pField.setLength(pField.getLength().trim());
    }

    private void checkFieldsOnContainInvalidValues(ProtocolField pField) {

	pField.setLength(pField.getLength().replaceAll("\\D+", ""));

	if (pField.getLength().isEmpty())
	    pField.setName("");
    }

    public void restoreProtocolFilesFromCurrentMapProtocols(HashMap<String, Protocol> mapProtocols) {

	for (HashMap.Entry<String, Protocol> it : mapProtocols.entrySet()) {

	    Logging.warn("Recreate protocol file " + it.getKey() + ".ini.");
	    createProtocolFile(it.getKey(), it.getValue());
	}
    }

    public void createProtocolFile(String protocolName, Protocol protocol) {
	String protocolFileNameWithPath = Paths.protocolDirPath + protocolName + ".ini";
	File protocolFile = new File(protocolFileNameWithPath);

	createFile(protocolFile);
	Logging.warn("Recreate file " + Paths.viewOnlyprotocolFileName + protocolName + ".ini");

	saveProtocolToFile(protocolFileNameWithPath, protocol);
    }

    public Set<Entry<String, Protocol>> entrySet() {
	return mapProtocols.entrySet();
    }

    public Protocol getProtocol(String key) {
	return mapProtocols.get(key);
    }

    public void put(String key, Protocol value) {
	mapProtocols.put(key, value);
    }

    public HashMap<String, Protocol> get() {
	if (mapProtocols != null)
	    return mapProtocols;
	else
	    return new HashMap<String, Protocol>();
    }

}
