package auctionHouse.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class NetworkHandler implements IConnectionHandler{

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler,
			INetworkManager manager) {

	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler,
			INetworkManager manager) {

		IdHandler.getId(netHandler.clientUsername);
		if(NoteHandler.getNotes(IdHandler.returnId(netHandler.clientUsername)).length > 0)
		{
			netHandler.getPlayer().addChatMessage("You have: "+NoteHandler.getNotes(IdHandler.returnId(netHandler.clientUsername))+" Auctions finished");
			netHandler.getPlayer().addChatMessage("You can access them by using /ah notes");
		}
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server,
			int port, INetworkManager manager) {

		
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler,
			MinecraftServer server, INetworkManager manager) {		
	}

	@Override
	public void connectionClosed(INetworkManager manager) {
		
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler,
			INetworkManager manager, Packet1Login login) {
		
	}

}
