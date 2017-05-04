package cdrRecords;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class CdrRecord2 {

    private HashMap<String, CdrField> record = new HashMap<>();

    public CdrRecord2() {
    }

    public void setElement(String elementName, CdrField cdrField) {
	record.put(elementName, cdrField);
    }

    public CdrField get(String elementName) {
	return record.get(elementName);
    }

    public Set<Entry<String, CdrField>> entrySet() {
	return record.entrySet();
    }

}
