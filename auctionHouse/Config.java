package auctionHouse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

public class Config 
{	
	
	//------------- File paths --------------
	
	private static String DIR_PATH = "./config/AuctionHouse";
	private static String CONFIGS = "./config/AuctionHouse/AHConfig.properties";
	
	//------------- Properties ------------
	
	public static String AH_BACKEND;
	public static String LOCALHOST_ADRESS;
	public static String LOCALHOST_PORT;
	public static String MYSQL_USERNAME;
	public static String MYSQL_PASSWORD;
	public static String DATABASE_NAME;
	public static String TABLE_PREFIX;
	public static int MAX_AUCTIONS_RUNNING;
	public static int ECO_FIRST_UNIT_ID;
	public static int ECO_FIRST_UNIT_META;
	public static int ECO_SECOND_UNIT_ID;
	public static int ECO_SECOND_UNIT_META;
	public static int ECO_THIRD_UNIT_ID;
	public static int ECO_THIRD_UNIT_META;
	
	//------------- Functions -------------
	
	public static void loadConfigs()
    {
		File Directory = new File(DIR_PATH);
        File ConfigFile = new File(CONFIGS);

		if (!Directory.exists())
			Directory.mkdir();
			
		if (!ConfigFile.exists())
		{
			try
			{
				ConfigFile.createNewFile();
				writeConfig(ConfigFile);
				readConfigs(ConfigFile);
			}
			catch (IOException e)
			{
				//Until the logger will be here :)
				System.out.println("AH ConfigHandler File Creation Error: " + e.getMessage());
			}
		}
		else
		{
			readConfigs(ConfigFile);
		}
		
        
    }
	

	public static void writeConfig(File ConfigFile)
	{
		try
		{
			PrintWriter fw = new PrintWriter(ConfigFile);
			
			fw.println("#                ---- AH Configs ----                 ");
			fw.println("#                   Created by Ideo                   ");
			fw.println("#                     Version 0.1                     ");
			fw.println("#             I hope you will enjoy it :]             ");
			fw.println("");
			fw.println("# What do you want to use as a back end? (MySQL,SQLite)");
			fw.println("Backend = sqlite");
			fw.println("");
			fw.println("#The adress of mysql localhost (you can just write localhost)");
			fw.println("#Change only if you are using a different ip for your Mysql mashine");
			fw.println("LocalhostAdress = localhost");
			fw.println("");
			fw.println("#Mysql port:");
			fw.println("LocalhostPort = 3306");
			fw.println("");
			fw.println("#Mysql Username:");
			fw.println("MysqlUsername = root");
			fw.println("");
			fw.println("#Mysql password:");
			fw.println("MysqlPassword = root");
			fw.println("");
			fw.println("#The database you would like to use: ");
			fw.println("DatabaseName = AH");
			fw.println("");
			fw.println("#What prefix to your tables would you like ? (like AH_Bids AH_Auc etc.)");
			fw.println("TablePrefix = Ah");
			fw.println("");
			fw.println("#Maximum number of Auctions running in the system");
			fw.println("#WARNING it will couse lag if people will do a lot of them!");
			fw.println("MaxAuctionRunning = 1000");
			fw.println("");
			fw.println("#[note]I will make a Auctions per player feature[/note]");
			fw.println("");
			fw.println("");
			fw.println("#			 ---- AH Bank Configs ----               ");
			fw.println("");
			fw.println("");
			fw.println("#This is the default Economy handler for AH [Uses real items as currency]");
			fw.println("#if it will be needed i will add a Economy Unit (credits by default) Name changer and everything...");
			fw.println("");
			fw.println("#Define your currency [Default: Gold Nugget = 1 Credit, Ingot = 9 Credits, Block = 81 Credit]");
			fw.println("");
			fw.println("#First Economy Units ID [worth 1 credit]");
			fw.println("FirstEcoUnitId = 371");
			fw.println("");			
			fw.println("#First Economy Units META (the numbers after : )[leave blank or 0 if not needed]");
			fw.println("FirstEcoUnitMeta = 0");
			fw.println("");
			fw.println("#Second Economy Units ID [worth 9 credits]");
			fw.println("SecondEcoUnitId = 266");
			fw.println("");			
			fw.println("#Second Economy Units META (the numbers after : )[leave blank or 0 if not needed]");
			fw.println("SecondEcoUnitMeta = 0");
			fw.println("");
			fw.println("#Third Economy Units ID [worth 81 credit]");
			fw.println("ThirdEcoUnitId = 41");
			fw.println("");			
			fw.println("#Third Economy Units META (the numbers after : )[leave blank or 0 if not needed]");
			fw.println("ThirdEcoUnitMeta = 0");
			fw.close();
		}
		catch(Exception e)
		{
			
        	//Until the logger will be here :)
			System.out.println("AH ConfigHandler Write error: "+e.getMessage());
		}
		
	}
	
	public static void readConfigs(File ConfigFile)
	{
		try{
	
			Properties AHProperties = new Properties();
			InputStream is = new FileInputStream(ConfigFile);
			AHProperties.load(is);
			is.close();
			
			// ------------ Configuration ------------
			
			AH_BACKEND = AHProperties.getProperty("Backend","sqlite");
			LOCALHOST_ADRESS = AHProperties.getProperty("LocalhostAdress","localhost");
			LOCALHOST_PORT = AHProperties.getProperty("LocalhostPort", "3306");
			MYSQL_USERNAME = AHProperties.getProperty("MysqlUsername","root");
			MYSQL_PASSWORD = AHProperties.getProperty("MysqlPassword","root");
			DATABASE_NAME = AHProperties.getProperty("DatabaseName", "AH");
			TABLE_PREFIX = AHProperties.getProperty("TablePrefix", "Ah");
			MAX_AUCTIONS_RUNNING = Integer.parseInt(AHProperties.getProperty("MaxAuctionsRunning", "1000"));
			ECO_FIRST_UNIT_ID = Integer.parseInt(AHProperties.getProperty("FirstEcoUnitId", "371"));
			ECO_FIRST_UNIT_META = Integer.parseInt(AHProperties.getProperty("FirstEcoUnitMeta", "0"));
			ECO_SECOND_UNIT_ID = Integer.parseInt(AHProperties.getProperty("SecondEcoUnitId", "266"));
			ECO_SECOND_UNIT_META = Integer.parseInt(AHProperties.getProperty("SecondEcoUnitMeta", "0"));
			ECO_THIRD_UNIT_ID = Integer.parseInt(AHProperties.getProperty("ThirdEcoUnitId", "41"));
			ECO_THIRD_UNIT_META = Integer.parseInt(AHProperties.getProperty("ThirdEcoUnitMeta", "0"));
			
		}catch(Exception e){
			
        	//Until the logger will be here :)
			System.out.println("AH ConfigHandler Config read error: "+e.getMessage());
			
		}
		
	}

}
