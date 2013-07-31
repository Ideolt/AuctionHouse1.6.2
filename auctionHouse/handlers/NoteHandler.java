package auctionHouse.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import auctionHouse.Config;

public class NoteHandler {

	public static Map<Integer,int[]> Notes = new HashMap<Integer,int[]>();
	
	public static void addNote(int winner, int workerId)
	{
		//adds an auction as a note to complete later + removes the worker from the worker list
		if(Notes.containsKey(winner) && Notes.get(winner).length<30)
		{
			Notes.get(winner)[Notes.get(winner).length] = workerId;
			AuctionHandler.Workers.remove(workerId);
			handleNoteSave(workerId,"save");
		}
		else if(!Notes.containsKey(winner)) 
		{	
			int[] notes = new int[30];//max 30 notes
			notes[0] = workerId;
			
			Notes.put(winner, notes);
			AuctionHandler.Workers.remove(workerId);
			handleNoteSave(workerId,"save");
		}
		else
		{
			return; //can't register more notes(more then 30)
		}
	}
	
	public static int[] getNotes(int userId)
	{
		if(Notes.containsKey(userId))
			return Notes.get(userId);
		else
			return null;
	}
	
	public static void removeNotes(int userId)
	{
		Notes.remove(userId);
	}
	
	public static void loadNotes()
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
                st.executeUpdate("CREATE TABLE IF NOT EXISTS '"+Config.TABLE_PREFIX+"_notes' (" +
                					"'user_id' int NOT NULL," +
                					"'note_id' int NOT NULL);");
                                
          	  	rs = st.executeQuery("SELECT * FROM "+Config.TABLE_PREFIX+"_notes;");
          	
          	  	if(rs.next())
          	  	{
          		addNote(Integer.parseInt(rs.getString(1)),Integer.parseInt(rs.getString(2)));
          	  	}
          	  	
    	    }
    	    catch ( Exception e ) 
    	    {
    	       	System.err.println("AH NoteHandler SQLite (Load) Error: " + e.getMessage() );
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
          	      System.err.println("AH ENoteHandler SQLite (Load) Con Close Error: " + e.getMessage() );
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
                   st.executeUpdate("CREATE TABLE IF NOT EXISTS '"+Config.TABLE_PREFIX+"_notes' (" +
                   					"'user_id' int NOT NULL," +
                   					"'note_id' int NOT NULL);");
                                   
             	  	rs = st.executeQuery("SELECT * FROM "+Config.TABLE_PREFIX+"_notes;");
             	
             	  	if(rs.next())
             	  	{
             		addNote(Integer.parseInt(rs.getString(1)),Integer.parseInt(rs.getString(2)));
             	  	}
            }
            catch (SQLException e) 
            {
            	//Until the logger will be here :)
    			System.err.println("AH NoteHandler Mysql (Load) Error: " + e.getMessage());
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
            			System.err.println("AH NoteHandler Mysql (Load) Con Close Error: " + e.getMessage());
                	}
            }
    	}
    	else
    	{
    		//Until the logger will be here :)
    		System.out.println("AH NoteHandler Error in your configs (Backend)");
    	}
		
	}
	
	public static void handleNoteSave(int auctionId, String action)
	{
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
				if(action.equalsIgnoreCase("save"))
					st.executeUpdate("INSERT INTO '"+Config.TABLE_PREFIX+"_notes'('user_id','note_id') VALUES ('"+auctionId+"','"+Notes.get(auctionId)+"');");
				else if(action.equalsIgnoreCase("delete"))
					st.executeUpdate("DELETE FROM '"+Config.TABLE_PREFIX+"_notes' WHERE note_id = '"+Notes.get(auctionId)+"'");
			}
			catch (Exception e ) 
			{
  	      			System.err.println("AH NoteHandler SQLite Auction (Save) Error: " + e.getMessage() );
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
            	catch(Exception e)
            	{
            		//Until the logger will be here :)
        			System.err.println("AH NoteHandler SQLite note (Save) Con Close Error: " + e.getMessage());
            	}
			}
		
		}
		else if(Config.AH_BACKEND.equalsIgnoreCase("mysql"))
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
				
				if(action.equalsIgnoreCase("save"))
					st.executeUpdate("INSERT INTO '"+Config.TABLE_PREFIX+"_notes'('user_id','note_id') VALUES ("+auctionId+","+Notes.get(auctionId)+");");
				else if(action.equalsIgnoreCase("delete"))
					st.executeUpdate("DELETE FROM '"+Config.TABLE_PREFIX+"_notes' WHERE note_id = '"+Notes.get(auctionId)+"'");
			}
			catch (SQLException e) 
			{
				//Until the logger will be here :)
				System.err.println("AH NoteHandler Mysql note save Error: " + e.getMessage());
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
						System.err.println("AH NoteHandler Mysql note save Con Close Error: " + e.getMessage());
            		}
			}
		}
		else
		{
			//Until the logger will be here :)
			System.out.println("AH NoteHandler Error in your configs (Backend)");
		}		
		
	}
	
}
