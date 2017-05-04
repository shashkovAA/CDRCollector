package sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ttest {

	    /**
	     * @param args the command line arguments
	     * @throws java.lang.Exception
	     */
	    public static void main(String[] args) throws Exception{
	       
	        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        String url="jdbc:sqlserver://10.5.11.150:1433;database=AdventureWorks";
	        Connection con = DriverManager.getConnection(url,"sa","1234");
	        
	        String sql="{call dbo.uspGetManagerEmployees(?)}";
	       
	        CallableStatement st = con.prepareCall(sql);
	        st.setInt(1, 12);
	        ResultSet rs = st.executeQuery();
	        while(rs.next())
	            System.out.printf("%s %s %s %s %s\n", rs.getString(1),rs.getString(3),rs.getString(4),rs.getString(6),rs.getString(7));
	    }
	    
	}

