package auctionHouse.handlers.command.bank;

import auctionHouse.handlers.EcoHandler;
import auctionHouse.handlers.IdHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class BalanceHandler {

	public static String cmdDesc = "Use /bank balance to see the remainder of money in your balance";
	
	public static void handleBalance(ICommandSender var1, String[] var2) {
		
		EntityPlayer pl;
		
		if(var1 instanceof EntityPlayer)
		{
			pl = (EntityPlayer) var1;
		}
		else
		{
			return;
		}
		
		pl.addChatMessage("- - - - - - - - - AHBank - - - - - - - - -");
		pl.addChatMessage("Your remaining money: "+EcoHandler.getBalance(IdHandler.returnId(var1.getCommandSenderName()))+" Credits");
		pl.addChatMessage("- - - - - - - - - AHBank - - - - - - - - -");
		return;
		
	}

}
