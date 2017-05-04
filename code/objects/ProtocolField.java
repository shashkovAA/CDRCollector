package objects;

import java.io.Serializable;

/**
 * ����� ��� �������� ������ ���� � ���������, ������� ����������� ��� �������
 * ������ ������ CDR. �������� ��������� ���(����) � ��������(����� ����).
 * �������� ������ ���� date:6 ���������� ���� � ������ date � ������ ���� � CDR
 * ������ ������ 6 ������.
 */

public class ProtocolField implements Serializable {

    private String name;
    private String length;
    private String format;
    private boolean includedInCdr;

    public ProtocolField() {
	this("", "");
    };

    public ProtocolField(String name, String length) {
	this.name = name;
	this.length = length;
	this.format = "";
	this.includedInCdr = true;
    }
    
    public ProtocolField(String name, String length, String format) {
  	this.name = name;
  	this.length = length;
  	this.format = format;
  	this.includedInCdr = true;
      }
    
    public ProtocolField(String name, String length, String format, boolean includedInCdr) {
	this.name = name;
	this.length = length;
	this.format = format;
	this.includedInCdr = includedInCdr;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getLength() {
	return length;
    }

    public void setLength(String value) {
	this.length = value;
    }

    public String getFormat() {
	return format;
    }

    public void setFormat(String format) {
	this.format = format;
    }
    
    public boolean getIncludedInCdr() {
 	return includedInCdr;
     }

     public void setIncludedInCdr(boolean includedInCdr) {
 	this.includedInCdr = includedInCdr;
     }

    public void setNameValue(String[] splitArray) {
	name = splitArray[0].toLowerCase();
	length = splitArray[1].toLowerCase();
	format = "";
	includedInCdr = true;
    }

}
