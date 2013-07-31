package auctionHouse.basic;

import net.minecraft.item.ItemStack;

public class Auction
{
	//the basic class that describes an auction	
	
	public String itemName;
	
	public int itemId;
	
	public int itemMeta;
	
	public int itemAmount;
	
	public String ownerName;
	
	public String winnerName;
	
	public long timeLeft;
	
	public long priceNow;
	
	public long buyOutPrice;
	
	public boolean buyOutDone;
	
	public String getItemName()
	{
		return itemName;
	}
	
	public int getItemId()
	{
		return itemId;
	}
	
	public int getItemMeta()
	{
		return itemMeta;
	}
	
	public int getItemAmount()
	{
		return itemAmount;
	}
	
	public long getTimeLeft()
	{
		return timeLeft;
	}
	
	public long getPriceNow()
	{
		return priceNow;
	}
	
	public long getBuyOutPrice()
	{
		return buyOutPrice;
	}
	
	public Auction(ItemStack item,String owner,long time,long price,long buyPrice)
	{
		itemName = item.getDisplayName();
	
		itemId = item.itemID;
		
		itemMeta = item.getItemDamage();
		
		itemAmount = item.stackSize;
		
		ownerName = owner;
		
		winnerName = owner;
		
		timeLeft = time;
		
		priceNow = price;
		
		buyOutPrice = buyPrice;
		
		buyOutDone = false;
	}
	
	/**used for backup proposes only*/
	public Auction(String name,int id,int meta,int amount,String owner,String winner,long time,long price,long buyPrice)
	{
		itemName = name;
		
		itemId = id;
		
		itemMeta = meta;
		
		itemAmount = amount;
		
		ownerName = owner;
		
		winnerName = winner;
		
		timeLeft = time;
		
		priceNow = price;
		
		buyOutPrice = price;
		
		buyOutDone = false;
	}
	
	public void setTime(long time)
	{
		timeLeft =  time;
	}
	
	public void setPrice(long price)
	{
		priceNow = price;
	}
	
	public void setWinner(String name)
	{
		winnerName = name;
	}
	
}
