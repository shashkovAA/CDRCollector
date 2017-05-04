package application;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import controller.RootController;
import objects.Paths;

/** Контроллер размера файла CDR */

public class CdrFileSizeController implements Runnable {
    private RootController rootController;
    private String cdrFileSize = "";
    private String cdrLogFileName = "";
    private SimpleDateFormat dateFormat = null;

    // ************************************************************************************************************
    // ===
    public void init(RootController mRootController, String mCdrFileSize) {

	this.rootController = mRootController;
	this.cdrFileSize = mCdrFileSize;
    }

    @Override
    public void run() {
	final int TIMEOUT_CHECK_CDR_FILE_SIZE = 14400000;
	dateFormat = new SimpleDateFormat("dd_MM_yyyy-HH_mm");

	while (true) {
	    try {
		Thread.currentThread().sleep(TIMEOUT_CHECK_CDR_FILE_SIZE); //

		Debug.log.info("Thread CheckCdrLogFileSize is wake up after "
			+ (int) Math.floor(TIMEOUT_CHECK_CDR_FILE_SIZE / 60000) + " minutes of sleep.");

		if (!cdrLogFileName.isEmpty())
		    splitCdrLogFile(cdrLogFileName);

	    } catch (InterruptedException except) {
		Debug.log.error(except.getMessage());
	    }
	}
    }

    private void splitCdrLogFile(String mCurrentCdrLogFileName) {

	File cdrDir = new File(Paths.cdrDirPath);

	Debug.log.info("Current log file name of the CDR :" + cdrLogFileName);
	Debug.log.info("File size log records CDR to monitor : " + cdrFileSize + " (" + parsingCdrFileSize(cdrFileSize)
		+ " bytes). ");

	for (File item : cdrDir.listFiles()) {
	    if (item.isFile())
		if ((item.getName().equals(cdrLogFileName)) && (item.length() > parsingCdrFileSize(cdrFileSize)))
		    renameOldFileAndCreateNewFile(item);
	}
    }

    private void renameOldFileAndCreateNewFile(File mFile) {

	String oldName = mFile.getName().replaceAll(Paths.cdrFileExtension, "");
	String newName = oldName + "-" + dateFormat.format(new Date());

	Debug.log.info("Cdr records strings " + oldName + Paths.cdrFileExtension + " will move to new file  " + newName
		+ Paths.cdrFileExtension + ", because it exceeds the maximum file size.  Current file size : "
		+ mFile.length() + " bytes." + ".");

	File newCdrFile = new File(Paths.cdrDirPath + newName + Paths.cdrFileExtension);
	mFile.renameTo(newCdrFile);

	File oldCdrFile = new File(Paths.cdrDirPath + oldName + Paths.cdrFileExtension);
	createNewLogFile(oldCdrFile);
    }

    private void createNewLogFile(File file) {
	try {
	    file.createNewFile();
	} catch (IOException except) {
	    Debug.log.error(except.getMessage());
	}
    }

    private long parsingCdrFileSize(String mCdrFileSize) {
	long size;

	if ((mCdrFileSize.contains("kb")) || (mCdrFileSize.contains("kB")) || (mCdrFileSize.contains("Kb"))
		|| (mCdrFileSize.contains("KB"))) {
	    size = Long.valueOf(mCdrFileSize.replaceAll("\\D+", "")) * 1024;

	} else if ((mCdrFileSize.contains("mb")) || (mCdrFileSize.contains("mB")) || (mCdrFileSize.contains("Mb"))
		|| (mCdrFileSize.contains("MB"))) {
	    size = Long.valueOf(mCdrFileSize.replaceAll("\\D+", "")) * 1024 * 1024;
	} else {
	    size = Long.valueOf(mCdrFileSize.replaceAll("\\D+", ""));
	    System.setProperty("cdrLogFileSize", mCdrFileSize.replaceAll("\\D+", "") + " B");
	}

	return size;

    }

    public void setCdrFileName(String mCdrLogFileName) {
	this.cdrLogFileName = mCdrLogFileName;

    }

}
