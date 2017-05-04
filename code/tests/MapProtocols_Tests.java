package tests;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import objects.MapProtocols;
import objects.MapProtocols2;
import objects.Paths;
import objects.Protocol;
import objects.ProtocolField;

public class MapProtocols_Tests {

    public static MapProtocols mapProtocols;
    public static MapProtocols2 mapProtocols2;
    private static File fileIn;
    private static File fileOut;
    Protocol testProtocol;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	mapProtocols = new MapProtocols();
	mapProtocols2 = new MapProtocols2();
	fileIn = new File(Paths.protocolDirPath + "testProtocolIn.ini");
	fileOut = new File(Paths.protocolDirPath + "testProtocolOut.ini");
	fileIn.createNewFile();
	fileOut.createNewFile();
	insertToFile(fileIn, "date=6");
	insertToFile(fileIn, "TiMe=4");
	insertToFile(fileIn, "");
	insertToFile(fileIn, "duration = 5");
	insertToFile(fileIn, "calling_num=15t");
	insertToFile(fileIn, "dialed_num=tr");
	insertToFile(fileIn, "calltype:1");
	insertToFile(fileIn, "direction=");
	insertToFile(fileIn, "frl=5");
	insertToFile(fileIn, "date=6");
	insertToFile(fileIn, "bcc=2");
	

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
	fileIn.delete();
	fileOut.delete();
    }
    
    

    @Test
    public void testGetProtocolFromFile_ProtocolField1() {
	testProtocol = new Protocol();
	testProtocol = mapProtocols.getProtocolFromFile("testProtocolIn.ini", "=");
	assertEquals("date", testProtocol.getProtocolField(0).getName());
	assertEquals("6", testProtocol.getProtocolField(0).getLength());
	assertEquals("time", testProtocol.getProtocolField(1).getName());
	assertEquals("4", testProtocol.getProtocolField(1).getLength());
	assertEquals("duration", testProtocol.getProtocolField(2).getName());
	assertEquals("5", testProtocol.getProtocolField(2).getLength());
	assertEquals("calling_num", testProtocol.getProtocolField(3).getName());
	assertEquals("15", testProtocol.getProtocolField(3).getLength());
	assertEquals("frl", testProtocol.getProtocolField(4).getName());
	assertEquals("5", testProtocol.getProtocolField(4).getLength());
	assertEquals("bcc", testProtocol.getProtocolField(5).getName());
	assertEquals("2", testProtocol.getProtocolField(5).getLength());

    }

    @Test
    public void testIsDuplicateFieldNameInProtocol() {
	testProtocol = new Protocol();
	ProtocolField pField = new ProtocolField("frl", "4");
	testProtocol = mapProtocols.getProtocolFromFile("testProtocolIn.ini", "=");
	assertEquals(true, mapProtocols.isDuplicateFieldNameInProtocol(testProtocol, pField));
    }

    @Test
    public void testGetShortNameFile() {
	assertEquals("testfile", mapProtocols.getShortNameFile("testfile.in"));
	assertEquals("testfile", mapProtocols.getShortNameFile("testfile.ini"));
	assertEquals("testfile", mapProtocols.getShortNameFile("testfile.init"));
	assertEquals("testfile", mapProtocols.getShortNameFile("testfile.txt.ini"));
    }

    @Test
    public void testParseReadLineInProtocolFile_1() {
	String line = "";
	assertEquals("", mapProtocols.parseReadLineInProtocolFile(line, "=").getName());
	assertEquals("", mapProtocols.parseReadLineInProtocolFile(line, "=").getLength());
    }

    @Test
    public void testParseReadLineInProtocolFile_2() {
	String line = "date=6";
	assertEquals("date", mapProtocols.parseReadLineInProtocolFile(line, "=").getName());
	assertEquals("6", mapProtocols.parseReadLineInProtocolFile(line, "=").getLength());
    }

    @Test
    public void testParseReadLineInProtocolFile_3() {
	String line = "date =   6";
	assertEquals("date", mapProtocols.parseReadLineInProtocolFile(line, "=").getName());
	assertEquals("6", mapProtocols.parseReadLineInProtocolFile(line, "=").getLength());
    }

    @Test
    public void testParseReadLineInProtocolFile_4() {
	String line = "date =  4r";
	assertEquals("date", mapProtocols.parseReadLineInProtocolFile(line, "=").getName());
	assertEquals("4", mapProtocols.parseReadLineInProtocolFile(line, "=").getLength());
    }

    @Test
    public void testParseReadLineInProtocolFile_5() {
	String line = "date=r";
	assertEquals("", mapProtocols.parseReadLineInProtocolFile(line, "=").getName());
	assertEquals("", mapProtocols.parseReadLineInProtocolFile(line, "=").getLength());
    }

    public static void insertToFile(File file, String text) {
	try (BufferedWriter bufferOut = new BufferedWriter(new FileWriter(file, true));) {
	    bufferOut.write(text);
	    bufferOut.write(Paths.lineSeparator);
	    bufferOut.flush();
	    bufferOut.close();
	} catch (IOException except) {
	}
    }

}
