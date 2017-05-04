package sql;


//Class EmployeeList displays Employees from the table EMP 
//using JDBC drivers of type 4

import java.sql.*;
import java.util.Properties;

import application.Debug;

class sql_test {

	//==============================Метод проверки создана ли база %mDbName
		
	static public void checkExistDB(String mDbType, String mIp, String mPort, String mDbName, Properties mProperties)
	{
		Connection connect = null;
		Statement statement = null;
		ResultSet result = null;
		boolean flagBaseExist = false;
		String sqlQuery;
		String connectionUrl = "jdbc:" + mDbType + "://" + mIp + ":" + mPort;

		try
		{

			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  // Написал для порядка. Для драйвера типа 4 - это не обязательно!

			connect = DriverManager.getConnection(connectionUrl, mProperties);

			sqlQuery = "select	name from sys.databases where name not in ('master','tempdb','model','msdb') and is_distributor = 0 and isnull(source_database_id,0) = 0";

			statement = connect.createStatement();

			result = statement.executeQuery(sqlQuery);

			while (result.next())
			{
				String BaseNames = result.getString("name");

				if (BaseNames.equals(mDbName))

				     flagBaseExist = true;
			}

			if (flagBaseExist == true)

			     Debug.log.warn("База данных с именем " + mDbName + " уже существует.");

			else
			{
				Debug.log.warn("Базы данных с именем " + mDbName + " на сервере нет. База создается.....");

				connect.createStatement().executeQuery("CREATE DATABASE [" + mDbName + "]");

			}
		}
		catch (SQLException se)
		{
			if (se.getErrorCode() != 0)

			     Debug.log.error("SQLError: " + se.getMessage() + " code: " + se.getErrorCode());

		}
		catch (Exception e)
		{
			Debug.log.error(e.getMessage());
		}
		finally
		{
			// clean up the system resources
			try
			{
				if (result != null) result.close();
				if (statement != null) statement.close();
				if (connect != null) connect.close();
			}
			catch (Exception e)
			{
				Debug.log.error(e.getMessage());
			}
		}
	}
	
//=============================Метод для проверки создана ли таблица %tableName в БД 


static private boolean checkExistCDRTable (Connection myconnect, String tableName)
{
	boolean existCDRTableFlag = false;
	ResultSet result = null;
			
	try 
	{
		
		result = myconnect.createStatement().executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES ");
		
		while (result.next()) 
		{
		Debug.log.info(result.getString("TABLE_NAME"));
		
		if (result.getString("TABLE_NAME").equals(tableName))
			
			existCDRTableFlag = true;
		}
		
	}
	catch (SQLException e) 
	{
		return false;
	}
	finally
	{
		try 
		{
			result.close();
		} 
		catch (SQLException e) 
		{
			Debug.log.error(e.getMessage());
		}
	}
	
	return existCDRTableFlag;
}	
//===========================================================================================================================
	
	public static void main(String argv[]) {
		
		
	Properties propers = new Properties();
	propers.setProperty("user", "od"); 
	propers.setProperty("password", "odconnect");
	
	String dbType = "sqlserver";
	String serverIp = "192.168.245.52";
	String serverPort = "1433";
	String dbName = "WTariffBase";
	String tabName = "Calls";
	
	
	String CDRCollectorConnectUrl = "jdbc:" + dbType + "://" + serverIp + ":" + serverPort + ";dataBaseName=" + dbName;
	
	//checkExistDB(dbType, serverIp, serverPort, dbName, propers);
	
	
	Connection dbConnection = null;
	  
		try  
		{ 
    	dbConnection = DriverManager.getConnection(CDRCollectorConnectUrl,propers);
    	
    	Debug.log.info(Boolean.toString(checkExistCDRTable(dbConnection, tabName)));
    	
		}
		
		catch (SQLException sqlEvt) 
		{
			Debug.log.error(sqlEvt.getMessage ());
		}
		
		finally
		{
			  try
			  {
				  if (dbConnection!=null) dbConnection.close();  
			  } 
			  catch(Exception e)
			  {
				  Debug.log.error(e.getMessage());
			  }
		}
	
	
	
	 
}
}

