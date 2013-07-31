package auctionHouse.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import auctionHouse.Config;
import auctionHouse.basic.Auction;
import auctionHouse.basic.AuctionWorker;

import net.minecraft.item.ItemStack;

public class AuctionHandler 
{
	//ids are the same for auctions and workers
	public static Map<Integer,Auction> Auctions = new HashMap<Integer,Auction>();
	public static Map<Integer,AuctionWorker> Workers = new HashMap<Integer,AuctionWorker>();
	public static ScheduledThreadPoolExecutor WorkerPool = new ScheduledThreadPoolExecutor(Config.MAX_AUCTIONS_RUNNING);

	private static int lastUsedId;
	
	public static int getUsableId()
	{
		lastUsedId = lastUsedId+1;
		
		return lastUsedId;
	}
	
	public static void loadAuctions()
	{
		int lastId = 0; //we will need this for mapping
		
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
    	      st.executeUpdate("CREATE TABLE IF NOT EXISTS '"+Config.TABLE_PREFIX+"_auctions' (" +
    	      					"'id' int NOT NULL," +
    	      					"'item_name' varchar(50) NOT NULL," +
    	      					"'item_id' int(5) NOT NULL," +
    	      					"'item_meta' tinyint(5) NOT NULL," +
    	      					"'item_amount' tinyint(3) NOT NULL," +
    	      					"'item_owner' varchar(50) NOT NULL," +
    	      					"'item_winner' varchar(50) NOT NULL," +
    	      					"'time_left' int(10) NOT NULL," +
    	      					"'price_now' int(10) NOT NULL," +
    	      					"'buy_out_price' int(10) NOT NULL );");
        				
				
    	      
          	  rs = st.executeQuery("SELECT * FROM "+Config.TABLE_PREFIX+"_auctions;");
          	
          	  	if(rs.next())
          	  	{
          	  		registerAuction(Integer.parseInt(rs.getString(1)), rs.getString(2), Integer.parseInt(rs.getString(3)), Integer.parseInt(rs.getString(4)), Integer.parseInt(rs.getString(5)),
          	  											rs.getString(6), rs.getString(7), Long.parseLong(rs.getString(8)), Long.parseLong(rs.getString(9)), Long.parseLong(rs.getString(10)));
          	  		
          	  		lastId = Integer.parseInt(rs.getString(1));
          	  	}
          	  	
          	  	//seting the last used id to the id that's left
          	  	lastUsedId = lastId;
    	    }
    	    catch ( Exception e ) 
    	    {
    	      
    	    	System.err.println("AH AuctionHandler SQLite (Load) Error: " + e.getMessage() );
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
          	      System.err.println("AH AuctionHandler SQLite (Load) Con Close Error: " + e.getMessage() );
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
                st.executeUpdate("CREATE TABLE IF NOT EXISTS '"+Config.TABLE_PREFIX+"_auctions' (" +
      								"'id' int NOT NULL," +
      								"'item_name' varchar(50) NOT NULL," +
      								"'item_id' int(5) NOT NULL," +
      								"'item_meta' tinyint(5) NOT NULL," +
      								"'item_amount' tinyint(3) NOT NULL," +
      								"'item_owner' varchar(50) NOT NULL," +
      								"'item_winner' varchar(50) NOT NULL," +
      								"'time_left' int(10) NOT NULL," +
      								"'price_now' int(10) NOT NULL," +
      								"'buy_out_price' int(10) NOT NULL );");
                
                rs = st.executeQuery("SELECT * FROM "+Config.TABLE_PREFIX+"_auctions;");
                
                if(rs.next())
                {
                	registerAuction(Integer.parseInt(rs.getString(1)), rs.getString(2), Integer.parseInt(rs.getString(3)), Integer.parseInt(rs.getString(4)), Integer.parseInt(rs.getString(5)),
								rs.getString(6), rs.getString(7), Long.parseLong(rs.getString(8)), Long.parseLong(rs.getString(9)), Long.parseLong(rs.getString(10)));
                
                	lastId = Integer.parseInt(rs.getString(1));
                }
                	
                lastUsedId = lastId;
            }
            catch (SQLException e) 
            {
            	//Until the logger will be here :)
    			System.err.println("AH AuctionHandler Mysql (Load) Error: " + e.getMessage());
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
            			System.err.println("AH AuctionHandler Mysql (Load) Con Close Error: " + e.getMessage());
                	}
            }
    	}
    	else
    	{
    		//Until the logger will be here :)
    		System.out.println("AH EcoHandler Error in your configs (Backend)");
    	}
	}
	
	public static void saveAuction(int id)
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
				
				//cheks if the row exists inserts it if it does'nt or updates it if it does
				rs = st.executeQuery("SELECT * FROM "+Config.TABLE_PREFIX+"_auctions WHERE id = '"+id+"' ;");
				if(rs.next())
				{
					//this will be used only by the worker, so we only need to update timeLeft,winnerName,priceNow everything else will always stay the same until it will end (be deleted)
					st.executeUpdate("UPDATE "+Config.TABLE_PREFIX+"_auctions SET item_winner = '"+Auctions.get(id).winnerName+"', time_left = '"+Auctions.get(id).timeLeft+"', price_now = '"+Auctions.get(id).priceNow+"' WHERE id = '"+id+"'");
				}
				else
				{
				
					String itemName = Auctions.get(id).itemName;
					int itemId = Auctions.get(id).itemId; 
					int itemMeta = Auctions.get(id).itemMeta;
					int itemAmount = Auctions.get(id).itemAmount;
					String ownerName = Auctions.get(id).ownerName;
					String winnerName = Auctions.get(id).winnerName;
					long timeLeft = Auctions.get(id).timeLeft;
					long priceNow = Auctions.get(id).priceNow;
					long buyOutPrice = Auctions.get(id).buyOutPrice;
					
					String prep = "INSERT INTO '"+Config.TABLE_PREFIX+"_auctions'('id','item_name','item_id','item_meta','item_amount','item_owner','item_winner','time_left','price_now','buy_out_price') VALUES "+
							"("+id+",'"+itemName+"','"+itemId+"','"+itemMeta+"','"+itemAmount+"','"+ownerName+"','"+winnerName+"','"+timeLeft+"','"+priceNow+"','"+buyOutPrice+"');";

					st.executeUpdate(prep);
				}               	
			}
			catch (Exception e ) 
			{
  	      			System.err.println("AH AuctionHandler SQLite Auction (Save) Error: " + e.getMessage() );
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
        			System.err.println("AH AuctionHandler SQLite Auction (Save) Con Close Error: " + e.getMessage());
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
				
				//cheks if the row exists inserts it if it does'nt or updates it if it does
				rs = st.executeQuery("SELECT * FROM "+Config.TABLE_PREFIX+"_auctions WHERE id = '"+id+"' ;");
				
				if(rs.next())
				{
					//this will be used only by the worker, so we only need to update timeLeft,winnerName,priceNow everything else will always stay the same until it will end (be deleted)
					st.executeUpdate("UPDATE "+Config.TABLE_PREFIX+"_auctions SET item_winner = '"+Auctions.get(id).winnerName+"', time_left = '"+Auctions.get(id).timeLeft+"', price_now = '"+Auctions.get(id).priceNow+"' WHERE id = '"+id+"'");
				}
				else
				{
				
					String itemName = Auctions.get(id).itemName;
					int itemId = Auctions.get(id).itemId; 
					int itemMeta = Auctions.get(id).itemMeta;
					int itemAmount = Auctions.get(id).itemAmount;
					String ownerName = Auctions.get(id).ownerName;
					String winnerName = Auctions.get(id).winnerName;
					long timeLeft = Auctions.get(id).timeLeft;
					long priceNow = Auctions.get(id).priceNow;
					long buyOutPrice = Auctions.get(id).buyOutPrice;
					
					String prep = "INSERT INTO '"+Config.TABLE_PREFIX+"_auctions'('id','item_name','item_id','item_meta','item_amount','item_owner','item_winner','time_left','price_now','buy_out_price') VALUES "+
							"('"+id+"','"+itemName+"','"+itemId+"','"+itemMeta+"','"+itemAmount+"','"+ownerName+"','"+winnerName+"','"+timeLeft+"','"+priceNow+"','"+buyOutPrice+"');";

					st.executeUpdate(prep);
				}
			}
			catch (SQLException e) 
			{
				//Until the logger will be here :)
				System.err.println("AH AuctionHandler Mysql (Save) Error: " + e.getMessage());
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
            			System.err.println("AH AuctionHandler Mysql (Save) Con Close Error: " + e.getMessage());
					}
			}
		}
		else
		{
    	//Until the logger will be here :)
		System.out.println("AH AuctionHandler Error in your configs (Backend)");
		}
		
	}

	
	public static Auction getAuction(int id)
	{
		if(Auctions.containsKey(id))
			return Auctions.get(id);
		else 
			return null;
	}
	
	public static void registerAuction(ItemStack item,String owner,long time,long price,long buyOut)
	{
		if(!(Workers.size() == Config.MAX_AUCTIONS_RUNNING))
		{
			int usableId = getUsableId();
			
			Auctions.put(usableId, new Auction(item,owner,time,price,buyOut));
			Workers.put(usableId, new AuctionWorker(usableId));
		}
		else
		{
			System.out.println("AH AuctionHandler Max Running auction cap reached");
		}
	}
	
	/**load use ONLY */
	public static void registerAuction(int id ,String name,int itemId,int meta,int amount,String owner,String winner,long time,long price,long buyPrice)
	{
		Auctions.put(id,new Auction(name,itemId,meta,amount,owner,winner,time,price,buyPrice));
		Workers.put(id, new AuctionWorker(id));
	}

	public static void removeAuction(int id)
	{
		Auctions.remove(id);
		Workers.remove(id);
	}


	public static boolean isNumber(String string) {
	    try {
	        Integer.valueOf(string);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	
	
}
