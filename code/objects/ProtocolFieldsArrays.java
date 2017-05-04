package objects;

import application.Debug;

/**
 * Класс, содержащий разложение ArrayList <ProtocolFields> на 3 одномерных
 * массива: nameFielsdArray - содержит названия полей, lengthFielsdArray -
 * модержит длину полей, и beginIndexFieldsArray - содержит индекс начала кадого
 * поля в строке.
 */
public class ProtocolFieldsArrays {

    public final int PROTOCOLFIELDS_COUNT;
    private Protocol cdrProtocol;
    private String[] nameFielsdArray;
    private int[] lengthFielsdArray;
    private int[] beginIndexFieldsArray;
    private int waitingCdrRecordLength;

    public ProtocolFieldsArrays(Protocol cdrProtocol) {
	this.cdrProtocol = cdrProtocol;
	PROTOCOLFIELDS_COUNT = cdrProtocol.size();
	nameFielsdArray = new String[PROTOCOLFIELDS_COUNT];
	lengthFielsdArray = new int[PROTOCOLFIELDS_COUNT];
	beginIndexFieldsArray = new int[PROTOCOLFIELDS_COUNT];
	getNameFielsdArray();
	getLengthFieldsArray();
	getBeginIndexFieldsArray();
	setWaitingCdrRecordLength();
    }

    private void getNameFielsdArray() {
	for (int i = 0; i < PROTOCOLFIELDS_COUNT; i++)
	    nameFielsdArray[i] = cdrProtocol.getProtocolField(i).getName().toString();
    }

    private void getLengthFieldsArray() {
	for (int i = 0; i < PROTOCOLFIELDS_COUNT; i++)
	    lengthFielsdArray[i] = Integer.valueOf(cdrProtocol.getProtocolField(i).getLength().toString());
    }

    private void getBeginIndexFieldsArray() {
	beginIndexFieldsArray[0] = 0;
	for (int i = 1; i < PROTOCOLFIELDS_COUNT; i++) {
	    beginIndexFieldsArray[i] = beginIndexFieldsArray[i - 1]
		    + Integer.valueOf(cdrProtocol.getProtocolField(i - 1).getLength().toString());
	    Debug.log.debug(" nameFielsdArray[" + i + "] = " + nameFielsdArray[i] + "\t beginIndexFieldsArray[" + i
		    + "] =" + beginIndexFieldsArray[i] + "\t lengthFielsdArray[ " + i + "] = " + lengthFielsdArray[i]);
	}
    }

    private void setWaitingCdrRecordLength() {
	waitingCdrRecordLength = beginIndexFieldsArray[PROTOCOLFIELDS_COUNT - 1]
		+ Integer.valueOf(cdrProtocol.getProtocolField(PROTOCOLFIELDS_COUNT - 1).getLength().toString());
    }

    public String getProtocolFieldName(int number) {
	return nameFielsdArray[number];
    }

    public int getProtocolFieldLength(int number) {
	return lengthFielsdArray[number];
    }

    public int getBeginIndexProtocolField(int number) {
	return beginIndexFieldsArray[number];
    }

    public int getSizeOfProtocolFieldsList() {
	return PROTOCOLFIELDS_COUNT;
    }

    public int getWaitingCdrStringLength() {
	return waitingCdrRecordLength;
    }

}
