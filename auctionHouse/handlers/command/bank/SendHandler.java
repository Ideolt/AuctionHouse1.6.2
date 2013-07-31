package auctionHouse.handlers.command.bank;

import auctionHouse.handlers.AuctionHandler;
import auctionHouse.handlers.EcoHandler;
import auctionHouse.handlers.IdHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class SendHandler {

	public static String cmdDesc = "Use /bank send <playerName> <amount> to send money ";
	public static String cmdDesc2 = "from your balance to another player";
	
	public static void handleSend(ICommandSender var1, String[] var2) {
		
		EntityPlayer pl;
		
		if(var1 instanceof EntityPlayer)
		{
			pl = (EntityPlayer) var1;
		}
		else
		{
			return;
		}
		
		String SenderName = var1.getCommandSenderName();
		
		long amount = 0;
		
		if(AuctionHandler.isNumber(var2[2]))//Checking for bad input [or dumbasses...]
			amount = Long.parseLong(var2[2]);
		else
		{
			pl.addChatMessage("bad command imput");
			pl.addChatMessage(cmdDesc);
			pl.addChatMessage(cmdDesc2);
			return;
		}
		
		String ReceiverName = var2[1];
		
		if(var2.length < 2)
		{
			pl.addChatMessage("To few arguments in your command");
			pl.addChatMessage(cmdDesc);
			pl.addChatMessage(cmdDesc2);
			return;
		}
		
		if(EcoHandler.getBalance(IdHandler.returnId(SenderName)) < amount)
		{
			pl.addChatMessage("Not enoth money in your acount to process command");
			return;
		}
		
		if(IdHandler.getId(ReceiverName) == 0)
		{
			pl.addChatMessage("Unknown player you want to send money to");
			return;
		}
		
		EcoHandler.setBalance(IdHandler.returnId(SenderName), EcoHandler.getBalance(IdHandler.returnId(SenderName)) - amount);
		EcoHandler.setBalance(IdHandler.returnId(ReceiverName), EcoHandler.getBalance(IdHandler.returnId(ReceiverName)) + amount);
		EcoHandler.saveBalance(IdHandler.returnId(SenderName));
		EcoHandler.saveBalance(IdHandler.returnId(ReceiverName));
		
		pl.addChatMessage("Transaction Complete!");
		
		System.out.println("[AH Bank] Player "+SenderName+" has send player "+ReceiverName+"  "+amount+" Credits");
		return;
		
	}

}
