package auctionHouse.handlers.command;

import auctionHouse.handlers.AuctionHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SellHandler 
{
	public static String cmdDesc = "Use /ah sell <price> <time> [buyout Price] <-[optional] while holding ";
	public static String cmdDesc2 = "the item you want to auction in your hand to register a new auction";
	
	public static void handleSell(ICommandSender var1, String[] var2)
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
					
		if(var2.length < 3)
			{
				pl.addChatMessage("Too few arguments in your command");
				pl.addChatMessage(cmdDesc);
				pl.addChatMessage(cmdDesc2);

				return;
			}
		
		long price = 0;
		long time = 0;
		long buyoutPrice = 0;
		
		if(AuctionHandler.isNumber(var2[1]))//Checking for bad input [or dumbasses...]
			price = Long.parseLong(var2[1]);
		else
		{
			pl.addChatMessage("bad command imput");
			pl.addChatMessage(cmdDesc);
			pl.addChatMessage(cmdDesc2);
			return;
		}
		
		if(AuctionHandler.isNumber(var2[2]))
			time = Long.parseLong(var2[2]);
		else
		{
			pl.addChatMessage("bad command imput");
			pl.addChatMessage(cmdDesc);
			pl.addChatMessage(cmdDesc2);
			return;
		}
		
		
		
		if(var2.length >= 4 && AuctionHandler.isNumber(var2[3]))
			buyoutPrice = Long.parseLong(var2[3]);
			
		AuctionHandler.registerAuction(pl.inventory.getCurrentItem(), name, time, price, buyoutPrice);
		
		int stackSize = pl.inventory.getCurrentItem().stackSize;
		int itemId = pl.inventory.getCurrentItem().itemID;
		
		while(stackSize > 0)
		{
			pl.inventory.consumeInventoryItem(itemId);
			stackSize--;
		}
			
		pl.addChatMessage("[AH] Auction registered successfully");
	}

}
