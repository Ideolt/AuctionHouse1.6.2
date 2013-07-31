package auctionHouse.handlers.command;

import auctionHouse.handlers.AuctionHandler;
import auctionHouse.handlers.EcoHandler;
import auctionHouse.handlers.IdHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class BuyoutHandler 
{
	public static 	String cmdDesc = "Use /ah buyout <ID> to buyout an item if posible";
	
	public static void handleBuyout(ICommandSender var1, String[] var2) 
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
		
		if(var2.length < 2)
		{
			pl.addChatMessage("Too few arguments in your command");
			pl.addChatMessage(cmdDesc);
			return;
		}
		
		int auctionId = 0;
		
		if(AuctionHandler.isNumber(var2[1]))
			auctionId = Integer.parseInt(var2[1]);
		else
		{
			pl.addChatMessage("Bad command input");
			pl.addChatMessage(cmdDesc);
			return;
		}
		
		
		String name = var1.getCommandSenderName();
		
		if(AuctionHandler.getAuction(auctionId) == null)
		{
			pl.addChatMessage("There are no auctions by that ID");
			return;
		}
		
		if(AuctionHandler.getAuction(auctionId).buyOutPrice == 0)
		{
			pl.addChatMessage("Buyout not posible [Auction owner doesn't want to sell it this way]");
			return;
		}
		
		if(AuctionHandler.getAuction(auctionId).ownerName.equalsIgnoreCase(name))
		{
			pl.addChatMessage("You can't buyout your own items...");
			return;
		}
		
		if(EcoHandler.getBalance(IdHandler.returnId(name)) < AuctionHandler.getAuction(auctionId).buyOutPrice)
		{
			pl.addChatMessage("Not enough cash... Get more money, try again");
			return;
		}
		
		//returns any reserved money
		if(AuctionHandler.getAuction(auctionId).ownerName != AuctionHandler.getAuction(auctionId).winnerName)
		{
			long resedMoney = AuctionHandler.getAuction(auctionId).priceNow;
			int moneyOwner = IdHandler.returnId(AuctionHandler.getAuction(auctionId).winnerName);
			EcoHandler.setBalance(moneyOwner, EcoHandler.getBalance(moneyOwner)+resedMoney);
			EcoHandler.saveBalance(moneyOwner);
		}
		
		EcoHandler.setBalance(IdHandler.returnId(name), EcoHandler.getBalance(IdHandler.returnId(name)) - AuctionHandler.getAuction(auctionId).buyOutPrice);
		EcoHandler.saveBalance(IdHandler.returnId(name));
		AuctionHandler.getAuction(auctionId).setWinner(name);
		AuctionHandler.getAuction(auctionId).buyOutDone = true;
		AuctionHandler.getAuction(auctionId).setTime(0);		
		
	}

}
