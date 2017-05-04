package objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Класс представляет собой тип на основе ArrayList-а полей протокола
 * ProtocolField.
 */

public class Protocol implements Serializable {

    private ArrayList<ProtocolField> fieldList;

    public Protocol() {
	fieldList = new ArrayList<ProtocolField>();
    }

    public void addAll(ArrayList<ProtocolField> fieldList) {
	this.fieldList = fieldList;
    }

    public void add(ProtocolField protocolField) {
	fieldList.add(protocolField);
    }

    public ProtocolField getProtocolField(int index) {
	return fieldList.get(index);
    }

    public ArrayList<ProtocolField> get() {
	return fieldList;
    }

   /* public ProtocolField get(int index) {
	return fieldList.get(index);
    }*/

    public int size() {
	return fieldList.size();
    }

    public void clear() {
	fieldList.clear();
    }

}
