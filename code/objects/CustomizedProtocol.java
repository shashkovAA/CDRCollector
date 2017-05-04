package objects;

public class CustomizedProtocol {

    public static Protocol get() {
	Protocol customizedProtocolFull = new Protocol();

	customizedProtocolFull.add(new ProtocolField("acct_code", "15"));
	customizedProtocolFull.add(new ProtocolField("attd_console", "2"));
	customizedProtocolFull.add(new ProtocolField("auth_code", "7"));
	customizedProtocolFull.add(new ProtocolField("bandwidth", "2"));
	customizedProtocolFull.add(new ProtocolField("bcc", "1"));
	customizedProtocolFull.add(new ProtocolField("calling_num", "15"));
	customizedProtocolFull.add(new ProtocolField("calltype", "1"));
	customizedProtocolFull.add(new ProtocolField("clg_num_in_tac", "1"));
	customizedProtocolFull.add(new ProtocolField("clg_pty_cat", "2"));
	customizedProtocolFull.add(new ProtocolField("code_dial", "4"));
	customizedProtocolFull.add(new ProtocolField("code_used", "4"));
	customizedProtocolFull.add(new ProtocolField("cond_code", "1"));
	customizedProtocolFull.add(new ProtocolField("contract_uri", "20"));
	customizedProtocolFull.add(new ProtocolField("date", "6"));
	customizedProtocolFull.add(new ProtocolField("dialed_num", "23"));
	customizedProtocolFull.add(new ProtocolField("duration", "4"));
	customizedProtocolFull.add(new ProtocolField("end_date_4d", "4"));
	customizedProtocolFull.add(new ProtocolField("end_time", "6"));
	customizedProtocolFull.add(new ProtocolField("feat_flag", "1"));
	customizedProtocolFull.add(new ProtocolField("frl", "1"));
	customizedProtocolFull.add(new ProtocolField("from_uri", "20"));
	customizedProtocolFull.add(new ProtocolField("in_crt_id", "3"));
	customizedProtocolFull.add(new ProtocolField("in_trk_code", "4"));
	customizedProtocolFull.add(new ProtocolField("ins", "3"));
	customizedProtocolFull.add(new ProtocolField("isdn_cc", "11"));
	customizedProtocolFull.add(new ProtocolField("ixc_code", "4"));
	customizedProtocolFull.add(new ProtocolField("line_feed", "1"));
	customizedProtocolFull.add(new ProtocolField("ma_uui", "1"));
	customizedProtocolFull.add(new ProtocolField("node_num", "2"));
	customizedProtocolFull.add(new ProtocolField("null", "1"));
	customizedProtocolFull.add(new ProtocolField("out_crt_id", "3"));
	customizedProtocolFull.add(new ProtocolField("ppm", "5"));
	customizedProtocolFull.add(new ProtocolField("request_uri", "20"));
	customizedProtocolFull.add(new ProtocolField("res_flag", "1"));
	customizedProtocolFull.add(new ProtocolField("return", "1"));
	customizedProtocolFull.add(new ProtocolField("secduration", "5"));
	customizedProtocolFull.add(new ProtocolField("space", "1"));
	customizedProtocolFull.add(new ProtocolField("start_date", "6"));
	customizedProtocolFull.add(new ProtocolField("start_date_4d", "8"));
	customizedProtocolFull.add(new ProtocolField("start_time", "6"));
	customizedProtocolFull.add(new ProtocolField("time", "4"));
	customizedProtocolFull.add(new ProtocolField("to_uri", "20"));
	customizedProtocolFull.add(new ProtocolField("tsc_ct", "4"));
	customizedProtocolFull.add(new ProtocolField("tsc_flag", "1"));
	customizedProtocolFull.add(new ProtocolField("vdn", "13"));

	return customizedProtocolFull;
    }

}
