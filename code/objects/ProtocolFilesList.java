package objects;

import java.io.File;
import java.util.ArrayList;

/** Тип объекта, содержащий список файлов протоколов CDR. */

public class ProtocolFilesList {

    private String fileNameTrail;
    private ArrayList<String> fileList;

    public ProtocolFilesList() {
    }

    public ProtocolFilesList(String fileNameTrail) {
	this.fileNameTrail = fileNameTrail;
    }

    public void setFileNameTrail(String fileNameTrail) {
	this.fileNameTrail = fileNameTrail;
    }

    public ArrayList<String> get() {
	fileList = new ArrayList<String>();
	getListInitFilesNamesInProtocolsFolder();
	return fileList;
    }

    private void getListInitFilesNamesInProtocolsFolder() {

	File protocolDir = new File(Paths.protocolDirPath);

	Logging.info("Get list of files in directory " + Paths.viewOnlyprotocolDirPath);

	for (File file : protocolDir.listFiles()) {
	    if (!isDirectory(file)) {
		if (isHaveFileNameSpecifiedTrail(file)) {
		    if (!isEmptyFile(file)) {
			fileList.add(file.getName());
			Logging.info("File :" + file.getName() + "   Seize :" + file.length());

		    } else
			Logging.warn("File :" + file.getName() + "   Seize :" + file.length()
				+ ". This file will be ignored. It is empty.");
		} else
		    Logging.warn("File :" + file.getName() + "   Seize :" + file.length()
			    + ". This file will be ignored. It is not *"+ GlobalVars.protocolFilesXMLTrail +" file.");
	    }
	}
    }

    public boolean isDirectory(File file) {
	return !file.isFile();
    }

    public boolean isEmptyFile(File file) {
	return file.length() == 0;
    }

    public boolean isHaveFileNameSpecifiedTrail(File file) {
	return file.getName().endsWith(fileNameTrail);
    }

}
