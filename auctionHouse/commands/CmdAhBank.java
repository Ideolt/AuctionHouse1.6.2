package auctionHouse.commands;

import java.util.ArrayList;
import java.util.List;

import auctionHouse.Permissions;
import auctionHouse.handlers.command.bank.BalanceHandler;
import auctionHouse.handlers.command.bank.DepositHandler;
import auctionHouse.handlers.command.bank.SendHandler;
import auctionHouse.handlers.command.bank.WithdrawHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CmdAhBank extends CommandBase
{
	public List cmdAlias = new ArrayList<String>();

	@Override
	public String getCommandName() 
	{
		return "AhBank";
	}

	@Override
	public List getCommandAliases()
    {
        cmdAlias.add("bank");
        cmdAlias.add("money");
		
		return cmdAlias;
    }
	
	@Override
	public void processCommand(ICommandSender var1, String[] var2) 
	{
		if(var2[0].equalsIgnoreCase("balance"))
		{
			BalanceHandler.handleBalance(var1,var2);
			return;
		}
		
		if(var2[0].equalsIgnoreCase("send"))
		{
			SendHandler.handleSend(var1,var2);
			return;
		}
		
		if(var2[0].equalsIgnoreCase("deposit"))
		{
			DepositHandler.handleDeposit(var1,var2);
			return;
		}
		
		if(var2[0].equalsIgnoreCase("withdraw"))
		{
			WithdrawHandler.handleWhithdrow(var1,var2);
			return;
		}
		
	}
	
	@Override
    public boolean canCommandSenderUseCommand(ICommandSender cs)
    {
        return cs instanceof EntityPlayerMP && Permissions.canAccess(cs, "AuctionHouse.bank.all");
    }

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		
		return "Use /ah help for help";
	}
	

}
