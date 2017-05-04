package cdrRecords;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import objects.ImageForCallType;

public class CdrRecord_Test {

    CdrRecord2 cdrRecord = new CdrRecord2();
    ImageForCallType image = new ImageForCallType();
    ImageForCallType image2 = new ImageForCallType();

    @Before
    public void setUpBefore() throws Exception {

	CdrField idField = new CdrField(new String("2017040515370001"));
	CdrField numberField = new CdrField(new Integer(12345678));
	CdrField imageField = new CdrField(new ImageForCallType("transitOk_16.png"));

	// image.setFileImageTypeName("transitOk_16.png");

	// idField.set("2017040515370001");
	// numberField.set(123456789);
	// imageField.set(image);

	cdrRecord.setElement("Id", idField);
	cdrRecord.setElement("Number", numberField);
	cdrRecord.setElement("Image", imageField);

    }

    @After
    public void tearDownAfter() throws Exception {
    }

    @Test
    public void test() {

	for (HashMap.Entry<String, CdrField> it : cdrRecord.entrySet()) {
	    System.out.println(it.getKey() + "  " + it.getValue().get());
	}
	image2 = (ImageForCallType) cdrRecord.get("Image").get();

	assertEquals("2017040515370001", cdrRecord.get("Id").get());
	assertEquals(12345678, cdrRecord.get("Number").get());
	assertEquals("transitOk_16.png", ((ImageForCallType) cdrRecord.get("Image").get()).getFileImageTypeName());

    }
}
