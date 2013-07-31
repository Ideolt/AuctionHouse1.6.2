package auctionHouse.handlers.command.bank;

import auctionHouse.Config;
import auctionHouse.handlers.AuctionHandler;
import auctionHouse.handlers.EcoHandler;
import auctionHouse.handlers.IdHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class DepositHandler 
{

	public static ItemStack FirstEcoUnit = new ItemStack(Config.ECO_FIRST_UNIT_ID,1,Config.ECO_FIRST_UNIT_META);
	public static ItemStack SecondEcoUnit = new ItemStack(Config.ECO_SECOND_UNIT_ID,1,Config.ECO_SECOND_UNIT_META);
	public static ItemStack ThirdEcoUnit = new ItemStack(Config.ECO_THIRD_UNIT_ID,1,Config.ECO_THIRD_UNIT_META);
	
	public static String cmdDesc = "Use /bank deposit <amount> While holding the proper item in your hand ";
	public static String cmdDesc2 = "to deposit it as credits to your acount. If you want to deposit [x] number ";
	public static String cmdDesc3 = "of items from your hand use /bank deposit [x] The Correct items are: ";
	public static String cmdDesc4 = ""+FirstEcoUnit.getDisplayName()+" Worth [1 Credit] "+SecondEcoUnit.getDisplayName()+" Worth [9 Credits] "+ThirdEcoUnit.getDisplayName()+" Worth [81 Credit]";
	
	public static void handleDeposit(ICommandSender var1, String[] var2) {
		
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
		int amount = 0;
				
		if(var2.length >= 2)
		{
			if(AuctionHandler.isNumber(var2[1]))//Checking for bad input [or dumbasses...]
				amount = Integer.parseInt(var2[1]);
			else
			{
				pl.addChatMessage("bad command imput");
				pl.addChatMessage(cmdDesc);
				pl.addChatMessage(cmdDesc2);
				pl.addChatMessage(cmdDesc3);
				pl.addChatMessage(cmdDesc4);
				return;
			}
			if(amount > pl.inventory.getCurrentItem().stackSize)
				amount = pl.inventory.getCurrentItem().stackSize;
		}
		else
			amount = pl.inventory.getCurrentItem().stackSize;
		
		if(pl.inventory.getCurrentItem().itemID == Config.ECO_FIRST_UNIT_ID && pl.inventory.getCurrentItem().getItemDamage() == Config.ECO_FIRST_UNIT_META)
		{
			int x = 0;
			
			while(x < amount)
			{
			pl.inventory.consumeInventoryItem(Config.ECO_FIRST_UNIT_ID);
			x++;
			}
			
			EcoHandler.setBalance(IdHandler.returnId(name),EcoHandler.getBalance(IdHandler.returnId(name))+amount*1);
			EcoHandler.saveBalance(IdHandler.returnId(name));
			
			System.out.println("[AH Bank] Player "+name+" has deposited "+amount+" Credits to his acount");
			return;
		}
		else if(pl.inventory.getCurrentItem().itemID == Config.ECO_SECOND_UNIT_ID && pl.inventory.getCurrentItem().getItemDamage() == Config.ECO_SECOND_UNIT_META)
		{
			int x = 0;
			
			while(x < amount)
			{
			pl.inventory.consumeInventoryItem(Config.ECO_SECOND_UNIT_ID);
			x++;
			}
			
			EcoHandler.setBalance(IdHandler.returnId(name),EcoHandler.getBalance(IdHandler.returnId(name))+amount*9);
			EcoHandler.saveBalance(IdHandler.returnId(name));
			
			System.out.println("[AH Bank] Player "+name+" has deposited "+amount*9+" Credits to his acount");
			return;
		}
		else if(pl.inventory.getCurrentItem().itemID == Config.ECO_THIRD_UNIT_ID && pl.inventory.getCurrentItem().getItemDamage() == Config.ECO_THIRD_UNIT_META)
		{
			int x = 0;
			
			while(x < amount)
			{
			pl.inventory.consumeInventoryItem(Config.ECO_THIRD_UNIT_ID);
			x++;
			}
			
			EcoHandler.setBalance(IdHandler.returnId(name),EcoHandler.getBalance(IdHandler.returnId(name))+amount*81);
			EcoHandler.saveBalance(IdHandler.returnId(name));
			
			System.out.println("[AH Bank] Player "+name+" has deposited "+amount*81+" Credits to his acount");
			return;
		}else
			pl.addChatMessage("Wrong item in your hand, you can use the command like this:");
		
		pl.addChatMessage(cmdDesc);
		pl.addChatMessage(cmdDesc2);
		pl.addChatMessage(cmdDesc3);
		pl.addChatMessage(cmdDesc4);
		
	}
	
}
