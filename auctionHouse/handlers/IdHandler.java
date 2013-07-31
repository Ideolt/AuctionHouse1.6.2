package auctionHouse.handlers;

import auctionHouse.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.network.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;

public class IdHandler 
{
	public static Map<Integer, String> Players = new HashMap<Integer, String>();
	
	public static void loadIds()
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
              st.executeUpdate("CREATE TABLE IF NOT EXISTS '"+Config.TABLE_PREFIX+"_ids' (" +
  									"'id' int NOT NULL,"+
  									"'name' varchar(50) NOT NULL);");    
              
    	      rs = st.executeQuery("SELECT * FROM "+Config.TABLE_PREFIX+"_ids;");
          	
          	  	if(rs.next())
          	  	{
          		Players.put(Integer.parseInt(rs.getString(1)),rs.getString(2));
          	  	}
          	  	
    	    }
    	    catch ( Exception e ) 
    	    {
    	         	System.err.println("AH IdHandler SQLite ID (Load) Error: " + e.getMessage() );
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
          	      System.err.println("AH IdHandler SQLite (Load) Con Close Error: " + e.getMessage() );
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
                    st.executeUpdate("CREATE TABLE IF NOT EXISTS '"+Config.TABLE_PREFIX+"_ids' (" +
                    					"'id' int NOT NULL,"+
                    					"'name' varchar(50) NOT NULL);");
                    
                	rs = st.executeQuery("SELECT * FROM "+Config.TABLE_PREFIX+"_ids;");
                	
                	if(rs.next())
                	{
                		Players.put(Integer.parseInt(rs.getString(1)),rs.getString(2));
                	}
            }
            catch (SQLException e) 
            {
            	//Until the logger will be here :)
    			System.err.println("AH IdHandler Mysql ID (Load) Error: " + e.getMessage());
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
            			System.err.println("AH IdHandler Mysql (Load) Con Close Error: " + e.getMessage());
                	}
            }
    	}
    	else
    	{
    		//Until the logger will be here :)
    		System.out.println("AH IdHandler Error in your configs (Backend) ");
    	}
    }

	public static void saveId(int Id)
    {    	
    	if(Config.AH_BACKEND.equalsIgnoreCase("sqlite"))
    	{
    		Connection con = null;
        	Statement st = null;
    		
    		try
    		{
      	      Class.forName("org.sqlite.JDBC");
      	      con = DriverManager.getConnection("jdbc:sqlite:config/AuctionHouse/AHdata.db");
      	      st = con.createStatement();
      	     
      	      st.executeUpdate("INSERT INTO '"+Config.TABLE_PREFIX+"_ids'('id','name') VALUES ('"+Id+"','"+Players.get(Id)+"');");                     	
            	  	
      	    }
      	    catch (Exception e ) 
      	    {
      	      
      	    	System.err.println("AH IdHandler SQLite ID (Save) Error: " + e.getMessage() );
      	    }
    		finally 
            {
                	try 
                	{
                		if(con != null)
                			con.close();
                		if(st != null)
                			 st.close();
                	}
                	catch(Exception e)
                	{
                		//Until the logger will be here :)
            			System.err.println("AH IdHandler SQLite ID (Save) Con Close Error: " + e.getMessage());
                	}
           }
    		
    	}
    	else if(Config.AH_BACKEND.equalsIgnoreCase("sqlite"))
    	{
    		Connection con = null;
        	Statement st = null;
		
    		String url = "jdbc:mysql://"+Config.LOCALHOST_ADRESS+":"+Config.LOCALHOST_PORT+"/"+Config.DATABASE_NAME;
            String user = Config.MYSQL_USERNAME;
            String password = Config.MYSQL_PASSWORD;
            
            try 
            {
            	
            	con = DriverManager.getConnection(url, user, password);                
            	st = con.createStatement();
            	
            	st.executeUpdate("INSERT INTO '"+Config.TABLE_PREFIX+"_ids'('id','name') VALUES ('"+Id+"','"+Players.get(Id)+"');");                     	
              	
                                                	
            }
            catch (SQLException e) 
            {
            	//Until the logger will be here :)
    			System.err.println("AH IdHandler Mysql ID (Save) Error: " + e.getMessage());
            }
            finally 
            {
                	try 
                	{
                		if(con != null)
                			con.close();
                		if(st != null)
                			 st.close();
                	}
                	catch(SQLException e)
                	{
                		//Until the logger will be here :)
            			System.err.println("AH IdHandler Mysql ID (Save) Con Close Error: " + e.getMessage());
                	}
           }
    	}
    	else
    	{
        	//Until the logger will be here :)
    		System.out.println("AH IdHandler Error in your configs (Backend)");
    	}
    }
    
	/** used only by the networkHandler */
    public static int getId(String Name)
    {
        if (Players.containsValue(Name))
        {
            return returnId(Name);
        }
        else
        {
        	int Id = Players.size()+1;
            Players.put(Id, Name); //Auto generates Id via Size+1
            saveId(Id);
            System.err.println("New ID registered! Name: " + Name + " ID: " + returnId(Name));
            return returnId(Name);
        }
    }

    public static int returnId(String Name)
    {
        if(Players.containsValue(Name))
        {
        			int x = 0;

        			while (x < Players.size())
        			{
        				if (Players.get(x) != Name)
        				{
        					x++;
        				}
        				else
        				{
        					break;
        				}
        			}

        			return x;
        }
        else
        {
        	return 0;
        }
    }
}
