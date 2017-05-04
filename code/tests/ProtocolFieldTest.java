package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import objects.ProtocolField;

public class ProtocolFieldTest {

    @Test
    public void TestFieldAddGet_1() {
	ProtocolField field = new ProtocolField("date", "4");
	assertEquals("date", field.getName());
	assertEquals("4", field.getLength());
    }

    @Test
    public void TestFieldAddGet_2() {
	String[] array = new String[] { "date", "4" };
	ProtocolField field = new ProtocolField();
	field.setNameValue(array);
	assertEquals("date", field.getName());
	assertEquals("4", field.getLength());
    }

}
