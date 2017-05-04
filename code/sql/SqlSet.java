package sql;

import java.sql.*;


public class SqlSet {
		
	
	public static String eventException = "";	

	 public static boolean TestConnection(String mDbType , String mIp, String mPort, String mLogin, String mPassword)
	{
		
		String testConnectionUrl = "jdbc:" + mDbType + "://" + mIp + ":" +mPort; 
		
		Connection testConnect = null;
		
		try 
		{
			testConnect = DriverManager.getConnection(testConnectionUrl,mLogin,mPassword); 
			
			try 
			{		
				testConnect.close();
			} 
			catch (SQLException e) 
			{
				eventException = e.getMessage ();
			}
			
			return true;
			
		}
		
		catch (SQLException sqlEvt)
		{
			eventException =  "SQLError: " + sqlEvt.getMessage ();
			return false;
		}
		
		catch (Exception e)
		{
			eventException = "Exception: " + e.getMessage ();
			return false;
		}
		
		finally
		{
			try 
			{
				if (testConnect!=null) testConnect.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
		}
		
	}

}
