package com.dairymoose.biomech.packet.clientbound;

import java.util.function.Supplier;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.item.armor.InterceptorArmsArmor;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundProjectileDodgePacket implements Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> {
	int playerId;
	
	public ClientboundProjectileDodgePacket() {
	}
	
	public ClientboundProjectileDodgePacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}
	
	public ClientboundProjectileDodgePacket(Player player) {
		this.playerId = player.getId();
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.playerId = byteBuf.readInt();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeInt(playerId);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        this.handle((net.minecraft.network.protocol.game.ClientGamePacketListener)ctx.get().getNetworkManager().getPacketListener());
	    });
	    ctx.get().setPacketHandled(true);
	}
	
	@SuppressWarnings("deprecation")
	public void handle(net.minecraft.network.protocol.game.ClientGamePacketListener handler) {
		BioMech.LOGGER.trace("Handle ClientboundProjectileDodgePacket");
		if (handler instanceof net.minecraft.client.multiplayer.ClientPacketListener) {
			DistExecutor.runWhenOn(Dist.CLIENT, () -> {return new Runnable() {
				@Override
				public void run() {
					net.minecraft.client.multiplayer.ClientPacketListener clientHandler = (net.minecraft.client.multiplayer.ClientPacketListener)handler;
					Entity e = clientHandler.getLevel().getEntity(playerId);
					if (e instanceof Player p) {
						InterceptorArmsArmor.dodgedProjectileSet.add(p);
					}
				}
				};});
		}
	}
}