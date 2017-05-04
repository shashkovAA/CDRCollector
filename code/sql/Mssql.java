package sql;

import application.Debug;
import controller.SelectBarController;

public class Mssql {

    private static String sqlQueryForSearch;

    private SelectBarController selectBarController;

    public void init(SelectBarController selectBarController) {
	this.selectBarController = selectBarController;
    }

    public String getSqlQueryForSearch() {
	sqlQueryForSearch = "SELECT TOP (1000) [callId], [date],[time],[duration],[calling_num],[dialed_num],[direction],[acct_code],"
		+ "[attd_console],[auth_code],[bandwidth],[bcc],[calltype],[clg_num_in_tac],[clg_pty_cat],[code_dial],[code_used],"
		+ "[cond_code],[contract_uri],[end_date_4d],[end_time],[feat_flag],[frl],[from_uri],[in_crt_id],[in_trk_code],[ins],"
		+ "[isdn_cc],[ixc_code],[ma_uui],[node_num],[out_crt_id],[ppm],[request_uri],[res_flag],[secduration],[start_date],"
		+ "[start_date_4d],[start_time],[to_uri],[tsc_ct],[tsc_flag],[vdn],[pbxName],[image]" +

		"FROM [WTariffBase].[dbo].[Calls]" + checkFields() + addConditionOntDateTimeBegin()
		+ addConditionOnDateTimeEnd() + addConditionOnCallingNumber() + addConditiontOnDialedNumber()
		+ addConditionOnDuration();

	return sqlQueryForSearch;
    }

    private String checkFields() {
	if (!selectBarController.getCallingNumber().isEmpty() || !selectBarController.getDateTimeBegin().isEmpty()
		|| !selectBarController.getDateTimeEnd().isEmpty())
	    return " WHERE ";
	else
	    return "";
    }

    private String addConditionOntDateTimeBegin() {
	Debug.log.info("selectBarController.getDateTimeBegin() = " + selectBarController.getDateTimeBegin());
	if (selectBarController.getDateTimeBegin().isEmpty())
	    return "";
	else
	    return "[timestamp] > '" + selectBarController.getDateTimeBegin() + "'";
    }

    private String addConditionOnDateTimeEnd() {
	Debug.log.info("selectBarController.getDateTimeEnd() = " + selectBarController.getDateTimeEnd());
	if (selectBarController.getDateTimeEnd().isEmpty())
	    return "";
	else
	    return " AND [timestamp] < '" + selectBarController.getDateTimeEnd() + "'";
    }

    private String addConditionOnCallingNumber() {
	Debug.log.info("selectBarController.getCallingNumber() =  " + selectBarController.getCallingNumber());
	if (selectBarController.getCallingNumber().isEmpty())
	    return "";
	else if (selectBarController.isSelectedChkbINcludeCallingNumber())
	    return " AND [calling_num] like '%" + selectBarController.getCallingNumber() + "%'";
	else
	    return " AND [calling_num] = '" + selectBarController.getCallingNumber() + "'";
    }

    private String addConditiontOnDialedNumber() {
	Debug.log.info("selectBarController.getDialedgNumber() =  " + selectBarController.getDialedNumber());
	if (selectBarController.getDialedNumber().isEmpty())
	    return "";
	else if (selectBarController.isSelectedchkbIncludeDnis())
	    return " AND [dialed_num] like '%" + selectBarController.getDialedNumber() + "%'";
	else
	    return " AND [dialed_num] = '" + selectBarController.getDialedNumber() + "'";
    }

    private String addConditionOnDuration() {
	Debug.log.info("ConditionOnDuration =  " + selectBarController.getConditionDuration()
		+ selectBarController.getDuration());
	if (!selectBarController.getConditionDuration().isEmpty() && !selectBarController.getDuration().isEmpty()) {
	    return "AND [durationSeconds] " + selectBarController.getConditionDuration() + "'"
		    + selectBarController.getDuration() + "'";
	} else
	    return "";
    }

}
