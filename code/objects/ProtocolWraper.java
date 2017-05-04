package objects;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "protocol")
public class ProtocolWraper {

    private ArrayList<ProtocolField> protocol;

    @XmlElement(name = "field")
    public ArrayList<ProtocolField> getProtocol() {
	return protocol;
    }

    public void setProtocol(ArrayList<ProtocolField> protocol) {
	this.protocol = protocol;
    }

}
