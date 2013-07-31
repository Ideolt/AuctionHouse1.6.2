package auctionHouse.handlers.command;

import auctionHouse.basic.AuctionWorker;
import auctionHouse.handlers.IdHandler;
import auctionHouse.handlers.NoteHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class NotesHandler
{

	public static String cmdDesc = "Use /ah notes if you have any pending notes ";
	public static String cmdDesc2 =	"(auctions that are finished) to make them finish";
	
	public static void handlerNotes(ICommandSender var1, String[] var2) 
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
		
		if(NoteHandler.getNotes(IdHandler.returnId(name)) == null)
		{
			pl.addChatMessage("You don't have any finished auctions");
			return;
		}
		else
		{
			int[] auctions = NoteHandler.getNotes(IdHandler.returnId(name));
			
			int x = 0;
			while(x < auctions.length)
			{
				AuctionWorker.runWorker(auctions[x]);
				x++;
			}
			NoteHandler.removeNotes(IdHandler.returnId(name));
		}
		
		pl.addChatMessage("Auction execution done. Notes removed");
		return;
	}

}
