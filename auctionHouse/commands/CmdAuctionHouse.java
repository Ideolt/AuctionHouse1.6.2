package auctionHouse.commands;

import java.util.ArrayList;
import java.util.List;

import auctionHouse.Permissions;
import auctionHouse.handlers.AuctionHandler;
import auctionHouse.handlers.command.BidHandler;
import auctionHouse.handlers.command.BuyoutHandler;
import auctionHouse.handlers.command.CancelHandler;
import auctionHouse.handlers.command.HelpHandler;
import auctionHouse.handlers.command.ListHandler;
import auctionHouse.handlers.command.MeHandler;
import auctionHouse.handlers.command.NotesHandler;
import auctionHouse.handlers.command.SellHandler;
import auctionHouse.handlers.command.ShowHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CmdAuctionHouse extends CommandBase
{

	public List cmdAlias = new ArrayList<String>();
	
	@Override
	public String getCommandName()
	{
		
		return "AuctionHouse";
	}
	
	@Override
	public List getCommandAliases()
    {
        cmdAlias.add("ah");
        cmdAlias.add("auction");
		
		return cmdAlias;
    }

	@Override
	public void processCommand(ICommandSender var1, String[] var2)
	{
		
		if(var2[0].equalsIgnoreCase("sell"))
		{
			SellHandler.handleSell(var1,var2);
			return;
		}
		
		if(var2[0].equalsIgnoreCase("list"))
		{
			ListHandler.handleList(var1,var2);
			return;
		}
		
		if(var2[0].equalsIgnoreCase("show"))
		{
			ShowHandler.handleShow(var1,var2);
			return;
		}
		
		if(var2[0].equalsIgnoreCase("bid"))
		{
			BidHandler.handleBid(var1,var2);
			return;
		}
		
		if(var2[0].equalsIgnoreCase("buyout"))
		{
			BuyoutHandler.handleBuyout(var1,var2);
			return;
		}
			
		if(var2[0].equalsIgnoreCase("cancel"))
		{
			CancelHandler.handleCancel(var1,var2);
			return;
		}
		
		if(var2[0].equalsIgnoreCase("me"))
		{
			MeHandler.handleMe(var1,var2);
			return;
		}
		
		if(var2[0].equalsIgnoreCase("help"))
		{
			HelpHandler.handleHelp(var1);
			return;
		}
		
		if(var2[0].equalsIgnoreCase("notes"))
		{
			NotesHandler.handlerNotes(var1,var2);
			return;
		}
		
		EntityPlayer pl;
		
		if(var1 instanceof EntityPlayer)
		{
			pl = (EntityPlayer) var1;
		}
		else
		{
			return;
		}
		
		if(var2[0].equalsIgnoreCase("AC"))
		{
			pl.addChatMessage("running aucions: "+AuctionHandler.Workers.size());
			return;
		}
		
		pl.addChatMessage("Bad command input use /ah help for the list of commands");
		return;
	}
	
	@Override
    public boolean canCommandSenderUseCommand(ICommandSender cs)
    {
        return cs instanceof EntityPlayerMP && Permissions.canAccess(cs, "AuctionHouse.auction.all");
    }

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "Use /ah help to get help";
	}

}
