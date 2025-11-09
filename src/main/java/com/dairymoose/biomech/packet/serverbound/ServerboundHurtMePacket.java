package com.dairymoose.biomech.packet.serverbound;

import java.util.function.Supplier;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.item.armor.arm.GrappleArmArmor;
import com.dairymoose.biomech.item.armor.arm.GrappleArmArmor.GrappleInfo;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundHurtMePacket implements Packet<ServerGamePacketListener> {
	private float hurt;
	
	public ServerboundHurtMePacket() {
	}
	
	public ServerboundHurtMePacket(float hurt) {
		this.hurt = hurt;
	}
	
	public ServerboundHurtMePacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.hurt = byteBuf.readFloat();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeFloat(hurt);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        ServerPlayer sender = ctx.get().getSender();
	        this.handle((ServerGamePacketListener)ctx.get().getNetworkManager().getPacketListener());
	    });
	    ctx.get().setPacketHandled(true);
	}
	
	public void handle(ServerGamePacketListener packetListener) {
		BioMech.LOGGER.trace("Handle ServerboundHurtMePacket");
		if (packetListener instanceof ServerGamePacketListenerImpl) {
			ServerGamePacketListenerImpl serverHandler = (ServerGamePacketListenerImpl)packetListener;
			Level world = serverHandler.player.level();
			if (world != null) {
				Player player = serverHandler.player;
				BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
				if (playerData != null) {
					player.hurt(player.level().damageSources().flyIntoWall(), hurt);
				}
			}
		}
	}
}