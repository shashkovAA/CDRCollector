package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import application.Debug;
import objects.Paths;
import objects.Protocol;
import objects.ProtocolField;
import objects.ProtocolWraper;

public class Protocol_Test {

    private Protocol testProtocol;
    private Protocol newProtocol;
    private File xmlFile;

    @Before
    public void setUp() throws Exception {

	testProtocol = new Protocol();
	newProtocol = new Protocol();
	ProtocolField field1 = new ProtocolField("date", "6");
	field1.setFormat("MM/dd");
	field1.setIncludedInCdr(false);
	testProtocol.add(field1);
	
	testProtocol.add(new ProtocolField("time", "4", "hh:mm", true));
	testProtocol.add(new ProtocolField("duration", "4", "hmmd", true));
	testProtocol.add(new ProtocolField("calling_num", "15"));

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
	creatXMLFile(Paths.protocolDirPath + "test.xml");
	System.out.println("Копирование в файл");
	saveProtocolToFile(xmlFile);
	System.out.println("Восстановление");
	loadProtocolFromFile(xmlFile);

	assertEquals("date", testProtocol.getProtocolField(0).getName());
	assertEquals("6", newProtocol.getProtocolField(0).getLength());
	assertEquals(newProtocol.size(), testProtocol.size());
	assertEquals(true, xmlFile.exists());

    }

    /**
     * Сохраняет текущую информацию об адресатах в указанном файле.
     * 
     * @param file
     */
    public void saveProtocolToFile(File file) {
	try {
	    JAXBContext context = JAXBContext.newInstance(ProtocolWraper.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	    // Обёртываем наши данные об адресатах.
	    ProtocolWraper wrapper = new ProtocolWraper();
	    wrapper.setProtocol(testProtocol.get());

	    // Маршаллируем и сохраняем XML в файл.
	    m.marshal(wrapper, file);

	} catch (Exception except) {
	}
    }

    /**
     * Загружает информацию об адресатах из указанного файла. Текущая информация
     * об адресатах будет заменена.
     * 
     * @param file
     */
    public void loadProtocolFromFile(File file) {
	try {
	    JAXBContext context = JAXBContext.newInstance(ProtocolWraper.class);
	    Unmarshaller um = context.createUnmarshaller();

	    // Чтение XML из файла и демаршализация.
	    ProtocolWraper wrapper = (ProtocolWraper) um.unmarshal(file);

	    newProtocol.clear();
	    newProtocol.addAll(wrapper.getProtocol());

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void creatXMLFile(String fileName) {
	xmlFile = new File(fileName);
	if (!xmlFile.exists()) {
	    createTextFile(xmlFile);
	    System.out.println("Create XML file " + Paths.protocolFoldeName + "test.xml");
	}
    }

    private void createTextFile(File file) {
	try {
	    file.createNewFile();
	} catch (IOException e) {
	    Debug.log.error(e.getMessage());
	}
    }

}
