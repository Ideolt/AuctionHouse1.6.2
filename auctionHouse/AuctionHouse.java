/** Auction House
 * 
 *  A simple auctioning plugin for Minecraft Forge
 *  servers.
 *   
 * Enjoy!
 *
 *@author Ideo
 *@version 0.1
 * 
 * */
package auctionHouse;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import auctionHouse.proxys.CommonProxy;
import auctionHouse.basic.AuctionWorker;
import auctionHouse.commands.CmdAhBank;
import auctionHouse.commands.CmdAuctionHouse;
import auctionHouse.handlers.AuctionHandler;
import auctionHouse.handlers.EcoHandler;
import auctionHouse.handlers.IdHandler;
import auctionHouse.handlers.NoteHandler;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;


@Mod(modid = "AH", name = "AuctionHouse", version = "0.1")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class AuctionHouse
{
	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    @Instance("Generic")
    public static AuctionHouse instance;

    @SidedProxy(clientSide = "auctionHouse.proxys.ClientProxy", serverSide = "auctionHouse.proxys.CommonProxy")
    public static CommonProxy proxy;

    @ServerStarting
    public void serverStart(FMLServerStartingEvent event)
    {
    	MinecraftServer server = MinecraftServer.getServer();
    	ICommandManager command = server.getCommandManager();
    	ServerCommandManager serverCommand = ((ServerCommandManager) command);
    	
    	serverCommand.registerCommand(new CmdAhBank());
    	serverCommand.registerCommand(new CmdAuctionHouse());
    }
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
    	Config.loadConfigs();
    }

    @Init
    public void load(FMLInitializationEvent event)
    {
    	IdHandler.loadIds();
    	NoteHandler.loadNotes();
    	EcoHandler.loadEco();
    	AuctionHandler.loadAuctions();
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
    	//until the logger will get here :)
    	System.out.println("[Auction House] Online! Currently running: "+AuctionHandler.Workers.size()+" auctions");
    }
}