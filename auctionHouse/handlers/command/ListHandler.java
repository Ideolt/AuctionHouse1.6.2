package auctionHouse.handlers.command;

import java.util.Iterator;
import java.util.Set;

import auctionHouse.handlers.AuctionHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class ListHandler {

	public static String cmdDesc = "Use /ah list [page number] to see all the auctions currently running";
	
	public static void handleList(ICommandSender var1, String[] var2) {
		
		EntityPlayer pl;
		
		if(var1 instanceof EntityPlayer)
		{
			pl = (EntityPlayer) var1;
		}
		else
		{
			return;
		}
		
		int pageToDisplay = 0;
		
		if(var2.length >= 2 && AuctionHandler.isNumber(var2[1]))
			pageToDisplay = Integer.parseInt(var2[1]);
		else
			pageToDisplay = 1;
		
		int numberOfPages = 1;

		
		Set AuctionIdSet = AuctionHandler.Auctions.keySet();
		Iterator AuctionIt = AuctionIdSet.iterator();
		
		int x = 0; 
		int[] Ids = new int[AuctionHandler.Auctions.size()];
				
		while(AuctionIt.hasNext())
		{
			Ids[x] = (Integer) AuctionIt.next();
			x++;
		}
	
		int auctionsToDisplay = 0;
				
		int auctionId = 0;
		
		
		while (AuctionHandler.Auctions.size() - numberOfPages*10 > 10)
			numberOfPages++;

		if(pageToDisplay > numberOfPages || pageToDisplay < 1)
			pageToDisplay = 1;
		
		//Decides how many auctions there will be displayed
		
		if(pageToDisplay < numberOfPages)
			auctionsToDisplay = 10;
			else
			auctionsToDisplay = AuctionHandler.Auctions.size() - (numberOfPages-1)*10;	
		
		//auction info
		String name = "";
		int amount = 0;
		long price = 0;
		long timeLeft = 0;
		long buyout = 0;
		
		
		//preparing the info to display
		
		pl.addChatMessage("- - - - - - - - - AuctionHouse - - - - - - - - -");
		pl.addChatMessage("ID | Item Name | Amount | Price | Time Left | BuyOut Price");
		pl.addChatMessage(" ");
		
		auctionId = (pageToDisplay-1)*10;
		
		if(auctionsToDisplay < 1)
			pl.addChatMessage("No Auctions for now");
		else
		{
			while(auctionsToDisplay >= 1)
			{
				name = AuctionHandler.getAuction(Ids[auctionId]).itemName;
				amount = AuctionHandler.getAuction(Ids[auctionId]).itemAmount;
				price = AuctionHandler.getAuction(Ids[auctionId]).priceNow;
				timeLeft = AuctionHandler.getAuction(Ids[auctionId]).timeLeft;
				buyout = AuctionHandler.getAuction(Ids[auctionId]).buyOutPrice;
			
				if(buyout == 0 && timeLeft == 0)
				{
					pl.addChatMessage(Ids[auctionId]+" | "+name+" | "+amount+" | "+price+" | Finished |  - ");
					pl.addChatMessage(" ");
				}
				else if(timeLeft == 0)
				{
					pl.addChatMessage(Ids[auctionId]+" | "+name+" | "+amount+" | "+price+" | Finished | "+buyout);
					pl.addChatMessage(" ");
				}
				else if(buyout == 0)
				{
					pl.addChatMessage(Ids[auctionId]+" | "+name+" | "+amount+" | "+price+" | "+timeLeft+" | - ");
					pl.addChatMessage(" ");
				}
				else
				{
					pl.addChatMessage(Ids[auctionId]+" | "+name+" | "+amount+" | "+price+" | "+timeLeft+" | "+buyout);
					pl.addChatMessage(" ");
				}
				
			
				auctionsToDisplay--;
				auctionId++;

			}
		}
		
		pl.addChatMessage(" ");
		pl.addChatMessage("- - - - - - - - - Page["+pageToDisplay+"/"+numberOfPages+"] - - - - - - - - -");
	}

}
