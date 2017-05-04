package sql;



public class first {

	public static void main(String[] args) 
	{
		
		//if (TestConnectMSSQL.TestConnection("sqlserver" , "192.168.245.50" , "1433" , "sa" , "Passw0rd1") == true)
		if (SqlSet.TestConnection("mysql" , "192.168.245.50" , "3306" , "od" , "odconnect") == true)
		 {
			 System.out.println("Сервер СУБД доступен!");
		 }
		 else System.out.println(SqlSet.eventException);
				 	 
	
	
	}

}
