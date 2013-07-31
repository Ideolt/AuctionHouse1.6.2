package auctionHouse.handlers.command;

import java.util.Iterator;
import java.util.Set;

import auctionHouse.handlers.AuctionHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class ShowHandler {
	
	public static String cmdDesc = "Use /ah show <item name> to see auctions for items whith that name";

	public static void handleShow(ICommandSender var1, String[] var2) {
		
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
		
		//array whith ids matching the search
		int[] goodIds = new int[10]; 
		
		int numberOfPages = 0;
		
		Set AuctionIdSet = AuctionHandler.Auctions.keySet();
		Iterator AuctionIt = AuctionIdSet.iterator();
		
		int x = 0; //temp caunter
		int[] Ids = new int[AuctionHandler.Auctions.size()];
		
		if(AuctionIt.hasNext())
		{
			Ids[x] = (Integer) AuctionIt.next();
			x++;
		}
		
		int auctionsToDisplay = 0;
				
		int auctionId = 0;
		
		x = 0; //the counter/id checker

		//padaryt 1 page max
		while(x < Ids.length)
		{
			if(AuctionHandler.getAuction(Ids[x]).itemName.contains(var2[1]) && goodIds.length < 20)
				goodIds[goodIds.length] = x;
			
			x++;
		}
				
		auctionsToDisplay = goodIds.length - (numberOfPages-1)*10;	
		
		//auction info
		String name = "";
		int amount = 0;
		long price = 0;
		long timeLeft = 0;
		long buyout = 0;
		
		pl.addChatMessage("- - - - - - - - - AuctionHouse - - - - - - - - -");
		pl.addChatMessage("ID | Item Name | Amount | Price | Time Left | BuyOut Price");
		pl.addChatMessage(" ");
		
		auctionId = 0;
		
		if(auctionsToDisplay < 1)
		{
			pl.addChatMessage(" ");
			pl.addChatMessage("No auctions faund");
			pl.addChatMessage(" ");
		}
		else
		{
			while(auctionsToDisplay >= 1)
			{
				name = AuctionHandler.getAuction(goodIds[auctionId]).itemName;
				amount = AuctionHandler.getAuction(goodIds[auctionId]).itemAmount;
				price = AuctionHandler.getAuction(goodIds[auctionId]).priceNow;
				timeLeft = AuctionHandler.getAuction(goodIds[auctionId]).timeLeft;
				buyout = AuctionHandler.getAuction(goodIds[auctionId]).buyOutPrice;
			
				if(buyout == 0)
					pl.addChatMessage(Ids[auctionId]+" | "+name+" | "+amount+" | "+price+" | "+timeLeft+" |  - ");
						else
							pl.addChatMessage(Ids[auctionId]+" | "+name+" | "+amount+" | "+price+" | "+timeLeft+" | "+buyout);
			
				auctionsToDisplay--;
				auctionId++;

			}
		}
		
		pl.addChatMessage("- - - - - - - - - Page[1/1] - - - - - - - - -");
	}

}
