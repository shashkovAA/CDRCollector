package objects;

import application.Debug;

public class ParsedCdrRecord {

    private String cdrString;
    private ProtocolFieldsArrays protocolFieldsArrays;
    private int indexOfLastFieldInList;

    public ParsedCdrRecord(String cdrString, ProtocolFieldsArrays protocolFieldsArrays) {
	this.cdrString = cdrString;
	this.protocolFieldsArrays = protocolFieldsArrays;
	indexOfLastFieldInList = getRealNumberFieldsInCdrRecord(cdrString);
    }

    private int getRealNumberFieldsInCdrRecord(String cdrString) {

	int length = protocolFieldsArrays.getSizeOfProtocolFieldsList();
	int indexLastRecord = 0;
	for (int i = 0; i < length; i++) {
	    if ((protocolFieldsArrays.getBeginIndexProtocolField(i)
		    + protocolFieldsArrays.getProtocolFieldLength(i)) > cdrString.length()) {
		indexLastRecord = i;
		break;
	    }
	}
	Debug.log.info("ќжидаема€ длина строки CDR записи ="
		+ String.valueOf(protocolFieldsArrays.getWaitingCdrStringLength()));
	Debug.log
		.info("ѕолученна€ длина строки CDR записи без пробелов = " + String.valueOf(cdrString.trim().length()));
	Debug.log.info("");
	return indexLastRecord;
    }

    public String getFieldValueFromCdrString(String fieldName) {
	String fieldValue = "";
	for (int i = 0; i < indexOfLastFieldInList; i++) {
	    if (protocolFieldsArrays.getProtocolFieldName(i).equals(fieldName)) {
		fieldValue = cdrString.substring(protocolFieldsArrays.getBeginIndexProtocolField(i),
			protocolFieldsArrays.getBeginIndexProtocolField(i)
				+ protocolFieldsArrays.getProtocolFieldLength(i));
		break;
	    }
	}
	return fieldValue;
    }

}
