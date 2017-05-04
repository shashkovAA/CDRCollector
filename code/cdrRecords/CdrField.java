package cdrRecords;

import javafx.beans.property.SimpleObjectProperty;

public class CdrField {

    private SimpleObjectProperty option;

    public CdrField(Object type) {
	option = new SimpleObjectProperty(type);
    }

    public Object get() {
	return option.get();
    }

    public void set(Object value) {
	this.option.set(value);
    }

    public SimpleObjectProperty getProperty() {
	return option;
    }
}
