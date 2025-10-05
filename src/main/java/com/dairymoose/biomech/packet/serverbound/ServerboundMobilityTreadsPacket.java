package com.dairymoose.biomech.packet.serverbound;

import java.util.function.Supplier;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.MobilityTreadsArmor;
import com.dairymoose.biomech.packet.clientbound.ClientboundHandStatusPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class ServerboundMobilityTreadsPacket implements Packet<ServerGamePacketListener> {
	private boolean speedBoost;

	public ServerboundMobilityTreadsPacket() {
	}
	
	public ServerboundMobilityTreadsPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ServerboundMobilityTreadsPacket(boolean speedBoost) {
		this.speedBoost = speedBoost;
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.speedBoost = byteBuf.readBoolean();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeBoolean(speedBoost);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        ServerPlayer sender = ctx.get().getSender();
	        this.handle((ServerGamePacketListener)ctx.get().getNetworkManager().getPacketListener());
	    });
	    ctx.get().setPacketHandled(true);
	}
	
	public void handle(ServerGamePacketListener packetListener) {
		BioMech.LOGGER.debug("Handle ServerboundMobilityTreadsPacket");
		if (packetListener instanceof ServerGamePacketListenerImpl) {
			ServerGamePacketListenerImpl serverHandler = (ServerGamePacketListenerImpl)packetListener;
			Level world = serverHandler.player.level();
			if (world != null) {
				BioMechPlayerData playerData = BioMech.globalPlayerData.get(serverHandler.player.getUUID());
				if (playerData != null) {
					ItemStack itemStack = playerData.getForSlot(MechPart.Leggings).itemStack;
					if (itemStack.getItem() instanceof MobilityTreadsArmor armor) {
						itemStack.getOrCreateTag().putBoolean("WantSpeedBoost", this.speedBoost);
					}
				}
			}
		}
	}
}