package objects;

import java.io.Serializable;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CdrRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    private StringProperty number; // 1
    private StringProperty id; // 2
    private StringProperty date; // 3
    private StringProperty time; // 4
    private StringProperty duration; // 5
    private StringProperty calling_num; // 6
    private StringProperty dialed_num; // 7
    private StringProperty direction; // 8
    private StringProperty acct_code; // 9
    private StringProperty auth_code; // 10
    private StringProperty attd_console; // 11
    private StringProperty bandwidth; // 12
    private StringProperty bcc; // 13
    private StringProperty calltype; // 14
    private StringProperty clg_num_in_tac; // 15
    private StringProperty clg_pty_cat; // 16
    private StringProperty code_dial; // 17
    private StringProperty code_used; // 18
    private StringProperty cond_code; // 19
    private StringProperty contact_uri; // 20
    private StringProperty end_date_4d; // 21
    private StringProperty end_time; // 22
    private StringProperty feat_flag; // 23
    private StringProperty frl; // 24
    private StringProperty from_uri; // 25
    private StringProperty in_crt_id; // 26
    private StringProperty in_trk_code; // 27
    private StringProperty ins; // 28
    private StringProperty isdn_cc; // 29
    private StringProperty ixc_code; // 30
    private StringProperty ma_uui; // 31
    private StringProperty node_num; // 32
    private StringProperty out_crt_id; // 33
    private StringProperty ppm; // 34
    private StringProperty request_uri; // 35
    private StringProperty res_flag; // 36
    private StringProperty sec_dur; // 37
    private StringProperty start_date; // 38
    private StringProperty start_date_4d; // 39
    private StringProperty start_time; // 40
    private StringProperty to_uri; // 41
    private StringProperty tsc_ct; // 42
    private StringProperty tsc_flag; // 43
    private StringProperty vdn; // 44
    private StringProperty pbxName; // 45
    private StringProperty timestampString; // 46
    private IntegerProperty durationSeconds; // 47

    private ObjectProperty<ImageForCallType> imageForCallType; // 48

    /**
     * Тип данных для CDR записи. При создании без указания аргументов
     * инициализируется пустыми строковыми значениями для полей типа String,
     * либо нулевыми значения для полей типа int.
     */
    public CdrRecord() {
	this("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
		"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 0, null);
    }

    public CdrRecord(String Number, String Id, String Date, String Time, String Duration, String Calling_num,
	    String Dialed_num, String Direction, String Acct_code, String Auth_code, String Attd_console,
	    String Bandwidth, String Bcc, String Calltype, String Clg_num_in_tac, String Clg_pty_cat, String Code_dial,
	    String Code_used, String Cond_code, String Contact_uri, String End_date_4d, String End_time,
	    String Feat_flag, String Frl, String From_uri, String In_crt_id, String In_trk_code, String Ins,
	    String Isdn_cc, String Ixc_code, String Ma_uui, String Node_num, String Out_crt_id, String Ppm,
	    String Request_uri, String Res_flag, String Sec_dur, String Start_date, String Start_date_4d,
	    String Start_time, String To_uri, String Tsc_ct, String Tsc_flag, String Vdn, String PbxName,
	    String TimestampString, int DurationSeconds, ImageForCallType img) {

	number = new SimpleStringProperty(Number);
	id = new SimpleStringProperty(Id);
	date = new SimpleStringProperty(Date);
	time = new SimpleStringProperty(Time);
	duration = new SimpleStringProperty(Duration);
	calling_num = new SimpleStringProperty(Calling_num);
	dialed_num = new SimpleStringProperty(Dialed_num);
	direction = new SimpleStringProperty(Direction);
	acct_code = new SimpleStringProperty(Acct_code);
	auth_code = new SimpleStringProperty(Auth_code);
	attd_console = new SimpleStringProperty(Attd_console);
	bandwidth = new SimpleStringProperty(Bandwidth);
	bcc = new SimpleStringProperty(Bcc);
	calltype = new SimpleStringProperty(Calltype);
	clg_num_in_tac = new SimpleStringProperty(Clg_num_in_tac);
	clg_pty_cat = new SimpleStringProperty(Clg_pty_cat);
	code_dial = new SimpleStringProperty(Code_dial);
	code_used = new SimpleStringProperty(Code_used);
	cond_code = new SimpleStringProperty(Cond_code);
	contact_uri = new SimpleStringProperty(Contact_uri);
	end_date_4d = new SimpleStringProperty(End_date_4d);
	end_time = new SimpleStringProperty(End_time);
	feat_flag = new SimpleStringProperty(Feat_flag);
	frl = new SimpleStringProperty(Frl);
	from_uri = new SimpleStringProperty(From_uri);
	in_crt_id = new SimpleStringProperty(In_crt_id);
	in_trk_code = new SimpleStringProperty(In_trk_code);
	ins = new SimpleStringProperty(Ins);
	isdn_cc = new SimpleStringProperty(Isdn_cc);
	ixc_code = new SimpleStringProperty(Ixc_code);
	ma_uui = new SimpleStringProperty(Ma_uui);
	node_num = new SimpleStringProperty(Node_num);
	out_crt_id = new SimpleStringProperty(Out_crt_id);
	ppm = new SimpleStringProperty(Ppm);
	request_uri = new SimpleStringProperty(Request_uri);
	res_flag = new SimpleStringProperty(Res_flag);
	sec_dur = new SimpleStringProperty(Sec_dur);
	start_date = new SimpleStringProperty(Start_date);
	start_date_4d = new SimpleStringProperty(Start_date_4d);
	start_time = new SimpleStringProperty(Start_time);
	to_uri = new SimpleStringProperty(To_uri);
	tsc_ct = new SimpleStringProperty(Tsc_ct);
	tsc_flag = new SimpleStringProperty(Tsc_flag);
	vdn = new SimpleStringProperty(Vdn);
	pbxName = new SimpleStringProperty(PbxName);
	timestampString = new SimpleStringProperty(TimestampString);
	durationSeconds = new SimpleIntegerProperty(DurationSeconds);
	imageForCallType = new SimpleObjectProperty(img);

    }

    // ----1-----------------------------------------

    public String getNumber() {
	return number.get();
    }

    public void setNumber(String Number) {
	this.number.set(Number);
    }

    public StringProperty getNumberProperty() {
	return number;
    }

    // -------2--------------------------------------

    public String getId() {
	return id.get();
    }

    public void setId(String Id) {
	this.id.set(Id);
    }

    public StringProperty getIdProperty() {
	return id;
    }

    // -------3----------------------------------------

    public String getDate() {
	return date.get();
    }

    public void setDate(String Date) {
	this.date.set(Date);
    }

    public StringProperty getDateProperty() {
	return date;
    }

    // -------4--------------------------------------

    public String getTime() {
	return time.get();
    }

    public void setTime(String Time) {
	this.time.set(Time);
    }

    public StringProperty getTimeProperty() {
	return time;
    }

    // ------5-------------------------------------

    public String getDuration() {
	return duration.get();
    }

    public void setDuration(String Duration) {
	this.duration.set(Duration);
    }

    public StringProperty getDurationProperty() {
	return duration;
    }

    // -----6----------------------------------------

    public String getCalling_num() {
	return calling_num.get();
    }

    public void setCalling_num(String Calling_num) {
	this.calling_num.set(Calling_num);
    }

    public StringProperty getCalling_numProperty() {
	return calling_num;
    }

    // -----7----------------------------------------

    public String getDialed_num() {
	return dialed_num.get();
    }

    public void setDialed_num(String Dialed_num) {
	this.dialed_num.set(Dialed_num);
    }

    public StringProperty getDialed_numProperty() {
	return dialed_num;
    }

    // -----8----------------------------------------

    public String getDirection() {
	return direction.get();
    }

    public void setDirection(String Direction) {
	this.direction.set(Direction);
    }

    public StringProperty getDirectionProperty() {
	return direction;
    }

    // -----9----------------------------------------

    public String getAcct_code() {
	return acct_code.get();
    }

    public void setAcct_code(String Acct_code) {
	this.acct_code.set(Acct_code);
    }

    public StringProperty getAcct_codeProperty() {
	return acct_code;
    }

    // -----10----------------------------------------

    public String getAuth_code() {
	return auth_code.get();
    }

    public void setAuth_code(String Auth_code) {
	this.auth_code.set(Auth_code);
    }

    public StringProperty getAuth_codeProperty() {
	return auth_code;
    }

    // ----11-----------------------------------------

    public String getAttd_console() {
	return attd_console.get();
    }

    public void setAttd_console(String Attd_console) {
	this.attd_console.set(Attd_console);
    }

    public StringProperty getAttd_consoleProperty() {
	return attd_console;
    }

    // --12-------------------------------------------

    public String getBandwidth() {
	return bandwidth.get();
    }

    public void setBandwidth(String Bandwidth) {
	this.bandwidth.set(Bandwidth);
    }

    public StringProperty getBandwidthProperty() {
	return bandwidth;
    }

    // ---13---bcc---------------------------------------------

    public String getBcc() {
	return bcc.get();
    }

    public void setBcc(String Bcc) {
	this.bcc.set(Bcc);
    }

    public StringProperty getBccProperty() {
	return bcc;
    }

    // ---14---calltype----------------------------------------

    public String getCalltype() {
	return calltype.get();
    }

    public void setCalltype(String Calltype) {
	this.calltype.set(Calltype);
    }

    public StringProperty getCalltypeProperty() {
	return calltype;
    }

    // ---15----clg_num_in_tac---------------------------------

    public String getClg_num_in_tac() {
	return clg_num_in_tac.get();
    }

    public void setClg_num_in_tac(String Clg_num_in_tac) {
	this.clg_num_in_tac.set(Clg_num_in_tac);
    }

    public StringProperty getClg_num_in_tacProperty() {
	return clg_num_in_tac;
    }

    // ---16----clg_pty_cat------------------------------------

    public String getClg_pty_cat() {
	return clg_pty_cat.get();
    }

    public void setClg_pty_cat(String Clg_pty_cat) {
	this.clg_pty_cat.set(Clg_pty_cat);
    }

    public StringProperty getClg_pty_catProperty() {
	return clg_pty_cat;
    }

    // ---17----code_dial-------------------------------------

    public String getCode_dial() {
	return code_dial.get();
    }

    public void setCode_dial(String Code_dial) {
	this.code_dial.set(Code_dial);
    }

    public StringProperty getCode_dialProperty() {
	return code_dial;
    }

    // ---18---code_used--------------------------------------

    public String getCode_used() {
	return code_used.get();
    }

    public void setCode_used(String Code_used) {
	this.code_used.set(Code_used);
    }

    public StringProperty getCode_usedProperty() {
	return code_used;
    }

    // ---19---cond_code--------------------------------------

    public String getCond_code() {
	return cond_code.get();
    }

    public void setCond_code(String Cond_code) {
	this.cond_code.set(Cond_code);
    }

    public StringProperty getCond_codeProperty() {
	return cond_code;
    }

    // ---20---contact_uri-------------------------------------

    public String getContact_uri() {
	return contact_uri.get();
    }

    public void setContact_uri(String Contact_uri) {
	this.contact_uri.set(Contact_uri);
    }

    public StringProperty getContact_uriProperty() {
	return contact_uri;
    }

    // ---21----end_date_4d------------------------------------

    public String getEnd_date_4d() {
	return end_date_4d.get();
    }

    public void setEnd_date_4d(String End_date_4d) {
	this.end_date_4d.set(End_date_4d);
    }

    public StringProperty getEnd_date_4dProperty() {
	return end_date_4d;
    }

    // ---22----end_time---------------------------------------

    public String getEnd_time() {
	return end_time.get();
    }

    public void setEnd_time(String End_time) {
	this.end_time.set(End_time);
    }

    public StringProperty getEnd_timeProperty() {
	return end_time;
    }

    // ---23----feat_flag--------------------------------------

    public String getFeat_flag() {
	return feat_flag.get();
    }

    public void setFeat_flag(String Feat_flag) {
	this.feat_flag.set(Feat_flag);
    }

    public StringProperty getFeat_flagProperty() {
	return feat_flag;
    }

    // ---24----frl--------------------------------------------

    public String getFrl() {
	return frl.get();
    }

    public void setFrl(String Frl) {
	this.frl.set(Frl);
    }

    public StringProperty getFrlProperty() {
	return frl;
    }

    // ---25----from_uri---------------------------------------

    public String getFrom_uri() {
	return from_uri.get();
    }

    public void setFrom_uri(String From_uri) {
	this.from_uri.set(From_uri);
    }

    public StringProperty getFrom_uriProperty() {
	return from_uri;
    }

    // ---26---in_crt_id---------------------------------------

    public String getIn_crt_id() {
	return in_crt_id.get();
    }

    public void setIn_crt_id(String In_crt_id) {
	this.in_crt_id.set(In_crt_id);
    }

    public StringProperty getIn_crt_idProperty() {
	return in_crt_id;
    }

    // ---27---in_trk_code-------------------------------------

    public String getIn_trk_code() {
	return in_trk_code.get();
    }

    public void setIn_trk_code(String In_trk_code) {
	this.in_trk_code.set(In_trk_code);
    }

    public StringProperty getIn_trk_codeProperty() {
	return in_trk_code;
    }

    // ---28---ins---------------------------------------------

    public String getIns() {
	return ins.get();
    }

    public void setIns(String Ins) {
	this.ins.set(Ins);
    }

    public StringProperty getInsProperty() {
	return ins;
    }

    // ---29---isdn_cc-----------------------------------------

    public String getIsdn_cc() {
	return isdn_cc.get();
    }

    public void setIsdn_cc(String Isdn_cc) {
	this.isdn_cc.set(Isdn_cc);
    }

    public StringProperty getIsdn_ccProperty() {
	return isdn_cc;
    }

    // ---30----ixc_code---------------------------------------

    public String getIxc_code() {
	return ixc_code.get();
    }

    public void setIxc_code(String Ixc_code) {
	this.ixc_code.set(Ixc_code);
    }

    public StringProperty getIxc_codeProperty() {
	return ixc_code;
    }

    // ---31----ma_uui-----------------------------------------

    public String getMa_uui() {
	return ma_uui.get();
    }

    public void setMa_uui(String Ma_uui) {
	this.ma_uui.set(Ma_uui);
    }

    public StringProperty getMa_uuiProperty() {
	return ma_uui;
    }

    // ---32----node_num---------------------------------------

    public String getNode_num() {
	return node_num.get();
    }

    public void setNode_num(String Node_num) {
	this.node_num.set(Node_num);
    }

    public StringProperty getNode_numProperty() {
	return node_num;
    }

    // ---33----out_crt_id-------------------------------------

    public String getOut_crt_id() {
	return out_crt_id.get();
    }

    public void setOut_crt_id(String Out_crt_id) {
	this.out_crt_id.set(Out_crt_id);
    }

    public StringProperty getOut_crt_idProperty() {
	return out_crt_id;
    }

    // ---34----ppm--------------------------------------------

    public String getPpm() {
	return ppm.get();
    }

    public void setPpm(String Ppm) {
	this.ppm.set(Ppm);
    }

    public StringProperty getPpmProperty() {
	return ppm;
    }

    // ---35----request_uri------------------------------------

    public String getRequest_uri() {
	return request_uri.get();
    }

    public void setRequest_uri(String Request_uri) {
	this.request_uri.set(Request_uri);
    }

    public StringProperty getRequest_uriProperty() {
	return request_uri;
    }

    // ---36----res_flag---------------------------------------

    public String getRes_flag() {
	return res_flag.get();
    }

    public void setRes_flag(String Res_flag) {
	this.res_flag.set(Res_flag);
    }

    public StringProperty getRes_flagProperty() {
	return res_flag;
    }

    // ---37---sec_dur-----------------------------------------

    public String getSec_dur() {
	return sec_dur.get();
    }

    public void setSec_dur(String Sec_dur) {
	this.sec_dur.set(Sec_dur);
    }

    public StringProperty getSec_durProperty() {
	return sec_dur;
    }

    // ---38---start_date--------------------------------------

    public String getStart_date() {
	return start_date.get();
    }

    public void setStart_date(String Start_date) {
	this.start_date.set(Start_date);
    }

    public StringProperty getStart_dateProperty() {
	return start_date;
    }

    // ---39----start_date_4d----------------------------------

    public String getStart_date_4d() {
	return start_date_4d.get();
    }

    public void setStart_date_4d(String Start_date_4d) {
	this.start_date_4d.set(Start_date_4d);
    }

    public StringProperty getStart_date_4dProperty() {
	return start_date_4d;
    }

    // ---40----start_time-------------------------------------

    public String getStart_time() {
	return start_time.get();
    }

    public void setStart_time(String Start_time) {
	this.start_time.set(Start_time);
    }

    public StringProperty getStart_timeProperty() {
	return start_time;
    }

    // ---41----to_uri-----------------------------------------

    public String getTo_uri() {
	return to_uri.get();
    }

    public void setTo_uri(String To_uri) {
	this.to_uri.set(To_uri);
    }

    public StringProperty getTo_uriProperty() {
	return to_uri;
    }

    // ---42----tsc_ct-----------------------------------------

    public String getTsc_ct() {
	return tsc_ct.get();
    }

    public void setTsc_ct(String Tsc_ct) {
	this.tsc_ct.set(Tsc_ct);
    }

    public StringProperty getTsc_ctProperty() {
	return tsc_ct;
    }

    // ---43---tsc_flag----------------------------------------

    public String getTsc_flag() {
	return tsc_flag.get();
    }

    public void setTsc_flag(String Tsc_flag) {
	this.tsc_flag.set(Tsc_flag);
    }

    public StringProperty getTsc_flagProperty() {
	return tsc_flag;
    }

    // ---44----vdn--------------------------------------------

    public String getVdn() {
	return vdn.get();
    }

    public void setVdn(String Vdn) {
	this.vdn.set(Vdn);
    }

    public StringProperty getVdnProperty() {
	return vdn;
    }

    // -------45----pbxName----------------------------------

    public String getPbxName() {
	return pbxName.get();
    }

    public void setPbxName(String PbxName) {
	this.pbxName.set(PbxName);
    }

    public StringProperty getPbxNameProperty() {
	return pbxName;
    }

    // ------46---Timestamp-----------------------------------------
    public String getTimestampString() {
	return timestampString.get();
    }

    public void setTimestampString(String TimestampString) {
	this.timestampString.set(TimestampString);
    }

    public StringProperty getTimestampStringProperty() {
	return timestampString;
    }

    // ------47---Timestamp-----------------------------------------
    public int getDurationSeconds() {
	return durationSeconds.get();
    }

    public void setDurationSeconds(int DurationSeconds) {
	this.durationSeconds.set(DurationSeconds);
    }

    public IntegerProperty getDurationSecondsProperty() {
	return durationSeconds;
    }

    // ---48---imageType---------------------------------------

    public Object getImageType() {
	return imageForCallType.get();
    }

    public void setImageForCallType(ImageForCallType img) {
	this.imageForCallType.set(img);
    }

    public ObjectProperty getImageForCallTypeProperty() {
	return imageForCallType;
    }

}
