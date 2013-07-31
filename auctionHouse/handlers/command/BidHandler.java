package auctionHouse.handlers.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import auctionHouse.handlers.AuctionHandler;
import auctionHouse.handlers.EcoHandler;
import auctionHouse.handlers.IdHandler;

public class BidHandler 
{
	public static String cmdDesc = "Use /ah bid <ID> <price> to bid for a specific auction";
	
	public static void handleBid(ICommandSender var1, String[] var2) 
	{
		
		String cmdDesc = "Use /ah bid <ID> <price> to bid for a specific auction";
		EntityPlayer pl;
		
		
		if(var1 instanceof EntityPlayer)
		{
			pl = (EntityPlayer) var1;
		}
		else
		{
			return;
		}
		
		if(var2.length < 3)
		{
			pl.addChatMessage("Too few arguments in your command");
			pl.addChatMessage(cmdDesc);
			return;
		}
		
		
		int auctionId = 0;
		long bidPrice = 0;
		
		if(AuctionHandler.isNumber(var2[1]))
			auctionId = Integer.parseInt(var2[1]);
		else
		{
			pl.addChatMessage("Bad command imput");
			pl.addChatMessage(cmdDesc);
			return;
		}
		
		if(AuctionHandler.isNumber(var2[2]))
			bidPrice = Long.parseLong(var2[2]);
		else
		{
			pl.addChatMessage("Bad command imput");
			pl.addChatMessage(cmdDesc);
			return;
		}

		String name = var1.getCommandSenderName();
		
		if(AuctionHandler.getAuction(auctionId) == null)
		{
			pl.addChatMessage("There are no auctions by that ID");
			return;
		}
		
		if(AuctionHandler.getAuction(auctionId).ownerName.equalsIgnoreCase(name))
		{
			pl.addChatMessage("You can't bid on your own auctions...");
			return;
		}
		
		if(AuctionHandler.getAuction(auctionId).priceNow >= bidPrice)
		{
			pl.addChatMessage("Your bid is too low, bid more to participate in the auction");
			return;
		}
		
		if(bidPrice > EcoHandler.getBalance(IdHandler.returnId(name)))
		{
			
			pl.addChatMessage("Not enough cash... Get more money, try again.");
			 return;
		}
		
		if(AuctionHandler.getAuction(auctionId).timeLeft == 0)
		{
			pl.addChatMessage("The auction is over, no more bids allowed.");
			return;			
		}
		
		//returns the reservation of money if needed
		if(AuctionHandler.getAuction(auctionId).ownerName != AuctionHandler.getAuction(auctionId).winnerName)
		{
			long resedMoney = AuctionHandler.getAuction(auctionId).priceNow;
			int moneyOwner = IdHandler.returnId(AuctionHandler.getAuction(auctionId).winnerName);
			EcoHandler.setBalance(moneyOwner, EcoHandler.getBalance(moneyOwner)+resedMoney);
		}

		//reserves the new money amount
		EcoHandler.setBalance(IdHandler.returnId(name),EcoHandler.getBalance(IdHandler.returnId(name)) - bidPrice);
		
		AuctionHandler.getAuction(auctionId).setPrice(bidPrice);
		AuctionHandler.getAuction(auctionId).setWinner(name);
		
	}

}
