package auctionHouse.handlers.command;

import auctionHouse.handlers.AuctionHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CancelHandler 
{
	public static String cmdDesc = "Use /ah cancel <auctionId> to cancel an auction that belongs to you ";
	public static String cmdDesc2 ="WARNING! if some one has already bided on it, it will not be canceled!";
	
	public static void handleCancel(ICommandSender var1, String[] var2) 
	{
		
		EntityPlayer pl;
		
		if(var1 instanceof EntityPlayer)
		{
			pl = (EntityPlayer) var1;
		}
		else
		{
			return;
		}
		
		String name = var1.getCommandSenderName();
		
		if(var2.length < 2)
		{
			pl.addChatMessage("Too few arguments in your command");
			pl.addChatMessage(cmdDesc);
			pl.addChatMessage(cmdDesc2);

			return;
		}
		
		int auctionId = 0;
		
		if(AuctionHandler.isNumber(var2[1]))
			auctionId = Integer.parseInt(var2[1]);
		else
		{
			pl.addChatMessage("Bad command input");
			pl.addChatMessage(cmdDesc);
			pl.addChatMessage(cmdDesc2);
			return;
		}
		
		if(AuctionHandler.getAuction(auctionId).ownerName != name)
		{
			pl.addChatMessage("You can't cancel an auction that does not belong to you");
			return;
		}
		
		if(!AuctionHandler.getAuction(auctionId).winnerName.equalsIgnoreCase(name))
		{
			pl.addChatMessage("Some one already bided on your auction, you can't cancel it now.");
			return;
		}
		
		AuctionHandler.getAuction(auctionId).setPrice(0);
		AuctionHandler.getAuction(auctionId).setTime(0);
		
	}

}
