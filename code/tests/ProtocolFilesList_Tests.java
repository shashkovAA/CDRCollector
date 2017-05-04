package tests;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import objects.GlobalVars;
import objects.Paths;
import objects.ProtocolFilesList;

public class ProtocolFilesList_Tests {

    private static File fileIni;
    private static File fileTxt;
    private static ProtocolFilesList list;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	list = new ProtocolFilesList(GlobalVars.protocolFilesXMLTrail);
	fileIni = new File(Paths.protocolDirPath + "testFile.xml");
	fileTxt = new File(Paths.protocolDirPath + "testFile.txt");
	fileIni.createNewFile();
	fileTxt.createNewFile();
	insertToFile(fileTxt, "Test Message");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
	fileIni.delete();
	fileTxt.delete();
    }

    @Test
    public void testFile1IsDirectory() {
	assertEquals(false, list.isDirectory(fileIni));

    }

    @Test
    public void testFile1IsHavaIniTrail() {
	assertEquals(true, list.isHaveFileNameSpecifiedTrail(fileIni));
    }

    @Test
    public void testFile1IsEmpty() {
	assertEquals(true, list.isEmptyFile(fileIni));
    }

    @Test
    public void testFile2IsNotEmpty() {
	assertEquals(false, list.isEmptyFile(fileTxt));
    }

    @Test
    public void testFile2IsHavaIniTrail() {
	assertEquals(false, list.isHaveFileNameSpecifiedTrail(fileTxt));
    }

    @Test
    public void testProtocolsList() {
	assertEquals(false, list.get().isEmpty());
    }

    public static void insertToFile(File file, String text) {
	try (BufferedWriter bufferOut = new BufferedWriter(new FileWriter(file, true));) {
	    bufferOut.write(text);
	    bufferOut.flush();
	    bufferOut.close();
	} catch (IOException except) {
	}
    }

}
