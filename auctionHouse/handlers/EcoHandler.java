package auctionHouse.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import auctionHouse.Config;

public class EcoHandler 
{

	static Map <Integer,Long> Money = new HashMap<Integer,Long>();
	
	public static  void loadEco()
	{
		
		if (Config.AH_BACKEND.equalsIgnoreCase("sqlite"))
    	{
    		Connection con = null;
    		Statement st = null;
    		ResultSet rs = null;
    		
    	    try
    	    {
    	    	Class.forName("org.sqlite.JDBC");
    	    	con = DriverManager.getConnection("jdbc:sqlite:config/AuctionHouse/AHdata.db");
    	    	
    	    	st = con.createStatement();
    	    	
        	     //creates the table if it doesn't exist and continues
                st.executeUpdate("CREATE TABLE IF NOT EXISTS '"+Config.TABLE_PREFIX+"_balance' (" +
                					"'id' int NOT NULL," +
                					"'balance' int(9) NOT NULL);");
                                
          	  	rs = st.executeQuery("SELECT * FROM "+Config.TABLE_PREFIX+"_balance;");
          	
          	  	if(rs.next())
          	  	{
          		setBalance(Integer.parseInt(rs.getString(1)),Long.parseLong(rs.getString(2)));
          	  	}
          	  	
    	    }
    	    catch ( Exception e ) 
    	    {
    	       	System.err.println("AH EcoHandler SQLite (Load) Error: " + e.getMessage() );
    	    }
    	    finally 
            {
            	try 
            	{
            		if(con != null)
            		    con.close();
            		if(st != null)
            			 st.close();
            		if(rs != null)
            			 rs.close();
            	}
            	catch ( Exception e ) 
            	{
          	      System.err.println("AH EcoHandler SQLite (Load) Con Close Error: " + e.getMessage() );
            	}
            }
    	    
    	}
    	else if (Config.AH_BACKEND.equalsIgnoreCase("mysql"))
    	{
    		
    		Connection con = null;
    		Statement st = null;
    		ResultSet rs = null;
    		
    		String url = "jdbc:mysql://"+Config.LOCALHOST_ADRESS+":"+Config.LOCALHOST_PORT+"/"+Config.DATABASE_NAME;
            String user = Config.MYSQL_USERNAME;
            String password = Config.MYSQL_PASSWORD;
            
            try 
            {
                	con = DriverManager.getConnection(url, user, password);
                	st = con.createStatement();
                	
                	//creates the table if it doesn't exist and continues
                    st.executeUpdate("CREATE TABLE IF NOT EXISTS '"+Config.TABLE_PREFIX+"_balance' (" +
                    					"'id' int NOT NULL," +
        								"'balance' int(9) NOT NULL);"); 
                    
                	rs = st.executeQuery("SELECT * FROM "+Config.TABLE_PREFIX+"_balance;");
                	
                	if(rs.next())
                	{
                		setBalance(Integer.parseInt(rs.getString(1)),Long.parseLong(rs.getString(2)));
                	}
            }
            catch (SQLException e) 
            {
            	//Until the logger will be here :)
    			System.err.println("AH EcoHandler Mysql (Load) Error: " + e.getMessage());
            }
            finally 
            {
                	try 
                	{
                		if(con != null)
                		    con.close();
                		if(st != null)
                			 st.close();
                		if(rs != null)
                			 rs.close();
                	}
                	catch(SQLException e)
                	{
                		//Until the logger will be here :)
            			System.err.println("AH EcoHandler Mysql (Load) Con Close Error: " + e.getMessage());
                	}
            }
    	}
    	else
    	{
    		//Until the logger will be here :)
    		System.out.println("AH EcoHandler Error in your configs (Backend)");
    	}
		
		
	}
	
	public static void saveBalance(int id)
	{
			int i = 1; //used as a counter...
    	
    		if(Config.AH_BACKEND.equalsIgnoreCase("sqlite"))
    		{
    			Connection con = null;
    			Statement st = null;
    			ResultSet rs = null;
    			
    			try
    			{
    				Class.forName("org.sqlite.JDBC");
    				con = DriverManager.getConnection("jdbc:sqlite:config/AuctionHouse/AHdata.db");
    				st = con.createStatement();
    				
    				rs = st.executeQuery("SELECT * FROM "+Config.TABLE_PREFIX+"_balance WHERE id = '"+id+"';");
    				
    				if(!rs.next())
    					st.executeUpdate("INSERT INTO '"+Config.TABLE_PREFIX+"_balance'('id','balance') VALUES ('"+id+"','"+Money.get(id)+"');");
    				else
    					st.executeUpdate("UPDATE "+Config.TABLE_PREFIX+"_balance SET balance = '"+Money.get(id)+"' WHERE id = '"+id+"';");      	                           	
      			}
    			catch (Exception e ) 
    			{
      	      
    				System.err.println("AH EcoHandler SQLite Eco (Save One Balance) Error: " + e.getMessage() );
    			}
    			finally 
    			{
                		try 
                		{
                			if(con != null)
                				con.close();
                			if(st != null)
                				st.close();
                			if(rs != null)
                				st.close();
                		}
                		catch(Exception e)
                		{
                			//Until the logger will be here :)
                			System.err.println("AH EcoHandler SQLite Eco (Save One Balance) Con Close Error: " + e.getMessage());
                		}
    			}	
    		
    		}
    		else if(Config.AH_BACKEND.equalsIgnoreCase("mysql"))
    		{
    			Connection con = null;
    			Statement st = null;
    			ResultSet rs = null;
 		
    			String url = "jdbc:mysql://"+Config.LOCALHOST_ADRESS+":"+Config.LOCALHOST_PORT+"/"+Config.DATABASE_NAME;
    			String user = Config.MYSQL_USERNAME;
    			String password = Config.MYSQL_PASSWORD;
            
    			try 
    			{
            	
    				con = DriverManager.getConnection(url, user, password);                
    				st = con.createStatement();
    				
    				rs = st.executeQuery("SELECT * FROM "+Config.TABLE_PREFIX+"_balance WHERE id = '"+id+"';");
    				
    				if(!rs.next())
    					st.executeUpdate("INSERT INTO '"+Config.TABLE_PREFIX+"_balance'('id','balance') VALUES ('"+id+"','"+Money.get(id)+"');");
    				else
    					st.executeUpdate("UPDATE "+Config.TABLE_PREFIX+"_balance SET balance = '"+Money.get(id)+"' WHERE id = '"+id+"';");                             	
    			}
    			catch (SQLException e) 
    			{
    				//Until the logger will be here :)
    				System.err.println("AH EcoHandler Mysql (Save One Balance) Error: " + e.getMessage());
    			}
    			finally 
    			{
    					try 
    					{
    						if(con != null)
    							con.close();
    						if(st != null)
    							st.close();
    						if(rs != null)
    							rs.close();
    					}
    					catch(SQLException e)
                		{
    						//Until the logger will be here :)
    						System.err.println("AH EcoHandler Mysql (Save One Balance) Con Close Error: " + e.getMessage());
                		}
    			}
    		}
    		else
    		{
    			//Until the logger will be here :)
    			System.out.println("AH EcoHandler Error in your configs (Backend)");
    		}		
	}
	
	public static long getBalance(int id)
	{
		if(Money.containsKey(id))
			return Money.get(id);
		else
		{
			Money.put(id, (long) 0);
			return Money.get(id);
		}
	}
	
	public static void setBalance(int id, long newBalance)
	{
		Money.put(id, newBalance);
	}
	
}
