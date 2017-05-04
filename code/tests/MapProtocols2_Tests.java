package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

public class MapProtocols2_Tests {

    public static MapProtocols mapProtocols;
    public static MapProtocols2 mapProtocols2;
    private static File fileIn;
    private static File fileOut;
    Protocol testProtocol;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
//	mapProtocols = new MapProtocols();
	mapProtocols2 = new MapProtocols2();
	/*fileIn = new File(Paths.protocolDirPath + "testProtocolIn.ini");
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
	insertToFile(fileIn, "bcc=2");*/
	

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
	//fileIn.delete();
	//fileOut.delete();
    }
    
    

    @Test
    public void testGetProtocolFromFile_ProtocolField1() {
	fail("Not Implement");

    }

   

}
