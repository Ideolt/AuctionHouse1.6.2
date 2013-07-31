package auctionHouse.handlers.command;

import auctionHouse.handlers.command.bank.BalanceHandler;
import auctionHouse.handlers.command.bank.DepositHandler;
import auctionHouse.handlers.command.bank.SendHandler;
import auctionHouse.handlers.command.bank.WithdrawHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class HelpHandler 
{

	public static void handleHelp(ICommandSender var1) 
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
		
		pl.addChatMessage("- - - - - - - - - AuctionHouse - - - - - - - - -");
		pl.addChatMessage("Full list of commands:");
		pl.addChatMessage(" ");
		pl.addChatMessage(SellHandler.cmdDesc);
		pl.addChatMessage(SellHandler.cmdDesc2);
		pl.addChatMessage(" ");
		pl.addChatMessage(BidHandler.cmdDesc);
		pl.addChatMessage(" ");
		pl.addChatMessage(BuyoutHandler.cmdDesc);
		pl.addChatMessage(" ");
		pl.addChatMessage(CancelHandler.cmdDesc);
		pl.addChatMessage(CancelHandler.cmdDesc2);
		pl.addChatMessage(" ");
		pl.addChatMessage(MeHandler.cmdDesc);
		pl.addChatMessage(" ");
		pl.addChatMessage(ListHandler.cmdDesc);
		pl.addChatMessage(" ");
		pl.addChatMessage(ShowHandler.cmdDesc);
		pl.addChatMessage(" ");
		pl.addChatMessage(NotesHandler.cmdDesc);
		pl.addChatMessage(NotesHandler.cmdDesc2);
		pl.addChatMessage(" ");
		pl.addChatMessage("- - - - - - - - - AHBank - - - - - - - - -");
		pl.addChatMessage("AH Economy commands: ");
		pl.addChatMessage(" ");
		pl.addChatMessage(BalanceHandler.cmdDesc);
		pl.addChatMessage(" ");
		pl.addChatMessage(SendHandler.cmdDesc);
		pl.addChatMessage(SendHandler.cmdDesc2);
		pl.addChatMessage(" ");
		pl.addChatMessage(DepositHandler.cmdDesc);
		pl.addChatMessage(DepositHandler.cmdDesc2);
		pl.addChatMessage(DepositHandler.cmdDesc3);
		pl.addChatMessage(DepositHandler.cmdDesc4);
		pl.addChatMessage(" ");
		pl.addChatMessage(WithdrawHandler.cmdDesc);
		pl.addChatMessage(WithdrawHandler.cmdDesc2);
		pl.addChatMessage(WithdrawHandler.cmdDesc3);		
		pl.addChatMessage(" ");
		pl.addChatMessage("[AuctionHouse] Have Fun !");
		
		return;
	}
	
	

}
