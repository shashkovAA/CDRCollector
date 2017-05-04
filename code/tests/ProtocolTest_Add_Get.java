package tests;

import static org.junit.Assert.assertEquals;
//import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import objects.Protocol;
import objects.ProtocolField;

public class ProtocolTest_Add_Get {

    @Test
    public void test() {

	Protocol protocol = new Protocol();
	ProtocolField field = new ProtocolField();
	field.setName("date");
	field.setLength("6");
	protocol.add(field);
	// assertThat(field).isEqualTo(protocol.getProtocolField(0));
	assertEquals(field, protocol.getProtocolField(0));

    };
}
