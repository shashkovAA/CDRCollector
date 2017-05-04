package objects;

public class UnformattedProtocol {

    public static Protocol get() {
	Protocol unformattedProtocol = new Protocol();
	unformattedProtocol.add(new ProtocolField("date", "4", "dd/MM", false));
	unformattedProtocol.add(new ProtocolField("time", "4", "hhmm"));
	unformattedProtocol.add(new ProtocolField("duration", "4", "hmmd"));
	unformattedProtocol.add(new ProtocolField("cond_code", "1"));
	unformattedProtocol.add(new ProtocolField("code_dial", "4"));
	unformattedProtocol.add(new ProtocolField("code_used", "4"));
	unformattedProtocol.add(new ProtocolField("dialed_num", "15"));
	unformattedProtocol.add(new ProtocolField("calling_num", "10"));
	unformattedProtocol.add(new ProtocolField("acct_code", "15"));
	unformattedProtocol.add(new ProtocolField("auth_code", "7"));
	unformattedProtocol.add(new ProtocolField("space", "2"));
	unformattedProtocol.add(new ProtocolField("frl", "1"));
	unformattedProtocol.add(new ProtocolField("in_crt_id", "3"));
	unformattedProtocol.add(new ProtocolField("out_crt_id", "3"));
	unformattedProtocol.add(new ProtocolField("feat_flag", "1"));
	unformattedProtocol.add(new ProtocolField("attd_console", "2"));
	unformattedProtocol.add(new ProtocolField("in_trk_code", "4"));
	unformattedProtocol.add(new ProtocolField("node_num", "2"));
	unformattedProtocol.add(new ProtocolField("ins", "3"));
	unformattedProtocol.add(new ProtocolField("ixc_code", "3"));
	unformattedProtocol.add(new ProtocolField("bcc", "1"));
	unformattedProtocol.add(new ProtocolField("ma_uui", "1"));
	unformattedProtocol.add(new ProtocolField("res_flag", "1"));
	unformattedProtocol.add(new ProtocolField("tsc_ct", "4"));

	return unformattedProtocol;
    }
}
