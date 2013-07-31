package auctionHouse.handlers.command.bank;

import auctionHouse.Config;
import auctionHouse.handlers.AuctionHandler;
import auctionHouse.handlers.EcoHandler;
import auctionHouse.handlers.IdHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class WithdrawHandler 
{

	public static String cmdDesc = "Use /bank withdraw <amount> to take some (or all) of your credits";
	public static String cmdDesc2 =	" from your balance and convert them to coresponding items ";
	public static String cmdDesc3 =	"Warning! make sure you have enoth room in your inventory first! ";
		
	public static void handleWhithdrow(ICommandSender var1, String[] var2)
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
		long amount = 0;
		
		if(var2.length >= 2)
		{
			if(AuctionHandler.isNumber(var2[1]))//Checking for bad input [or dumbasses...]
				amount = Long.parseLong(var2[1]);
			else
			{
				pl.addChatMessage("bad command imput");
				pl.addChatMessage(cmdDesc);
				pl.addChatMessage(cmdDesc2);
				pl.addChatMessage(cmdDesc3);
				return;
			}

			if(amount > EcoHandler.getBalance(IdHandler.returnId(name)))
				amount = EcoHandler.getBalance(IdHandler.returnId(name));
				if(amount > 5184)//third unit stack is worth this much (if you want to withdraw more it causes null pointers in ItemStack.Java)
					amount = 5184;
		}
		else
		{
			amount = EcoHandler.getBalance(IdHandler.returnId(name)); 
			if(amount > 5184)
				amount = 5184;
		}
		
		EcoHandler.setBalance(IdHandler.returnId(name), EcoHandler.getBalance(IdHandler.returnId(name)) - amount);
		EcoHandler.saveBalance(IdHandler.returnId(name));
		
		long amountSave = amount;
		
		int firstEcoUnitAmount = 0;
		int secondEcoUnitAmount = 0;
		int thirdEcoUnitAmount = 0;
		
		while(amount > 0)
		{
			if(amount - 81 >= 81)
			{
				amount = amount - 81;	
				thirdEcoUnitAmount++;
			}
			else if(amount - 9 >= 9)
			{
				amount = amount - 9;
				secondEcoUnitAmount++;
			}
			else if(amount - 1 >= 0)
			{
				amount--;
				firstEcoUnitAmount++;
			}
			
		}
		
		ItemStack thirdEcoUnit = new ItemStack(Config.ECO_THIRD_UNIT_ID,thirdEcoUnitAmount,Config.ECO_THIRD_UNIT_META); 
		ItemStack secondEcoUnit = new ItemStack(Config.ECO_SECOND_UNIT_ID,secondEcoUnitAmount,Config.ECO_SECOND_UNIT_META); 
		ItemStack firsEcoUnit = new ItemStack(Config.ECO_FIRST_UNIT_ID,firstEcoUnitAmount,Config.ECO_FIRST_UNIT_META); 
		
		
		if(thirdEcoUnitAmount > 0)
			pl.inventory.addItemStackToInventory(thirdEcoUnit);
		
		if(secondEcoUnitAmount > 0)
			pl.inventory.addItemStackToInventory(secondEcoUnit);
		
		if(firstEcoUnitAmount > 0)
			pl.inventory.addItemStackToInventory(firsEcoUnit);
		
		pl.addChatMessage("Transaction complete");
		
		//until the logger will get here :)
		System.out.println("[AH Bank] Player "+name+" has taken "+amountSave+" Credits out of his acount");
		return;
	}

}
