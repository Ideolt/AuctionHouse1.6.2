package auctionHouse.basic;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.world.WorldServer;

import auctionHouse.handlers.AuctionHandler;
import auctionHouse.handlers.EcoHandler;
import auctionHouse.handlers.IdHandler;
import auctionHouse.handlers.NoteHandler;

public class AuctionWorker 
{
	//check for Done Auctions (time 0) gives items etc.
	
	//this is the id of the auction he is checking
	protected static int workerId;
	
	public static void setId(int id)
	{
		workerId = id;
	}
	
	public static int getId()
	{
		return workerId;
	}
	
	public AuctionWorker(int id)
	{
		setId(id);
		AuctionHandler.WorkerPool.execute(worker);
	}

	public static void runWorker(int id)
	{
		setId(id);
		AuctionHandler.WorkerPool.execute(worker);
	}
	
	public static EntityPlayer getPlayer(String name)
	{
		//gathers the player lists

		Object[] worlds = MinecraftServer.getServer().worldServers;
		int x = 0;
		
		while(x < worlds.length)
		{
			WorldServer world = (WorldServer) worlds[x];
			
			List playerList = world.playerEntities;
			
			int y = 0;
			
			while(y < playerList.size())
			{
				EntityPlayer pl = (EntityPlayer) playerList.get(y);
				
				if(pl.username.equalsIgnoreCase(name))
					return pl;
				else y++;
			}
			
			x++;
			
		}
		
		return null;//player not found (offline)
	}
	
	public static Runnable worker = new Runnable()
	{
		public void run() 
		{
			
			int Id = getId();
			Auction workersAuction = AuctionHandler.getAuction(Id);
						
			if (workersAuction.getTimeLeft() != 0)
			{
				workersAuction.setTime(workersAuction.getTimeLeft()-1);
				//more runs needed
				AuctionHandler.WorkerPool.schedule(worker, 1, TimeUnit.SECONDS);
				AuctionHandler.saveAuction(workerId);
			}
			else
			{	
				if(workersAuction.buyOutDone)//buyout version
				{
					int winner = IdHandler.returnId(workersAuction.winnerName);
					
					if(getPlayer(workersAuction.winnerName) == null)
					{
						//the winner is offline makes a note for this auctions completion
						NoteHandler.addNote(winner,workerId);
					}						
					else if(getPlayer(workersAuction.winnerName).inventory.getFirstEmptyStack() == -1)
					{
						//inventory full will complete when here will be room makes a note of it
						NoteHandler.addNote(winner,workerId);
					}						
					else
					{
						ItemStack Item = new ItemStack(workersAuction.itemId,workersAuction.itemAmount,workersAuction.itemMeta);
						getPlayer(workersAuction.winnerName).inventory.addItemStackToInventory(Item);
						AuctionHandler.removeAuction(workerId);//done
					}
				}
				else//normal auction
				{
					int winnerId = IdHandler.getId(workersAuction.winnerName);
					
					if(getPlayer(workersAuction.winnerName) == null)
					{
						//the winner is offline, makes a note for this auctions completion
						NoteHandler.addNote(winnerId,workerId);
						System.out.println("Auction Id: "+Id+" done! Winner not found (offline or not in vanila dimension) storing note.");
					}
					else if(getPlayer(workersAuction.winnerName).inventory.getFirstEmptyStack() == -1)
					{
						//inventory full, auction will complete when there will be room for items, makes a note of it
						NoteHandler.addNote(winnerId,workerId);
						System.out.println("Auction Id: "+Id+" done! Winners inventory full storing note.");
					}						
					else
					{
						if(workersAuction.ownerName.equalsIgnoreCase(workersAuction.winnerName))//winner = owner Auction failed
						{
							ItemStack Item = new ItemStack(workersAuction.itemId,workersAuction.itemAmount,workersAuction.itemMeta);
							getPlayer(workersAuction.winnerName).inventory.addItemStackToInventory(Item);
							AuctionHandler.removeAuction(workerId);
						}
						else//auction successful 
						{
							int owner = IdHandler.returnId(workersAuction.ownerName);
							EcoHandler.setBalance(owner, EcoHandler.getBalance(owner)+workersAuction.priceNow);
							EcoHandler.saveBalance(owner);
						
							ItemStack Item = new ItemStack(workersAuction.itemId,workersAuction.itemAmount,workersAuction.itemMeta);
							getPlayer(workersAuction.winnerName).inventory.addItemStackToInventory(Item);
							AuctionHandler.removeAuction(workerId);
						}

					}
				}
			}
			return;
		}
	};	
}
