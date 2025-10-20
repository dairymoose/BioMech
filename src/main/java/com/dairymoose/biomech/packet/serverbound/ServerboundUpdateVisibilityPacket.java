package com.dairymoose.biomech.packet.serverbound;

import java.util.function.Supplier;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.packet.clientbound.ClientboundHandStatusPacket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class ServerboundUpdateVisibilityPacket implements Packet<ServerGamePacketListener> {
	private CompoundTag playerDataTag;

	public ServerboundUpdateVisibilityPacket() {
	}
	
	public ServerboundUpdateVisibilityPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ServerboundUpdateVisibilityPacket(CompoundTag playerDataTag) {
		this.playerDataTag = playerDataTag;
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.playerDataTag = byteBuf.readNbt();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeNbt(playerDataTag);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        ServerPlayer sender = ctx.get().getSender();
	        this.handle((ServerGamePacketListener)ctx.get().getNetworkManager().getPacketListener());
	    });
	    ctx.get().setPacketHandled(true);
	}
	
	public void handle(ServerGamePacketListener packetListener) {
		BioMech.LOGGER.trace("Handle ServerboundHandStatusPacket");
		if (packetListener instanceof ServerGamePacketListenerImpl) {
			ServerGamePacketListenerImpl serverHandler = (ServerGamePacketListenerImpl)packetListener;
			Level world = serverHandler.player.level();
			if (world != null) {
				BioMechPlayerData playerData = BioMech.globalPlayerData.get(serverHandler.player.getUUID());
				
				if (playerData != null) {
					BioMechPlayerData incomingPlayerData = BioMechPlayerData.deserialize(playerDataTag);
					
					if (incomingPlayerData != null) {
						for (SlottedItem slottedItem : playerData.getAllSlots()) {
							SlottedItem incomingItem = incomingPlayerData.getForSlot(slottedItem.mechPart);
							slottedItem.visible = incomingItem.visible;
						}
						
						BioMech.sendItemSlotUpdateForPlayer(serverHandler.player);
					}
				}
			}
		}
	}
}