package com.dairymoose.biomech.packet.clientbound;

import java.util.UUID;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundUpdateSlottedItemPacket implements Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> {
	private UUID uuid;
	private CompoundTag playerDataTag;
	private static final Logger LOGGER = LogManager.getLogger();

	public ClientboundUpdateSlottedItemPacket() {
	}
	
	public ClientboundUpdateSlottedItemPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ClientboundUpdateSlottedItemPacket(UUID uuid, CompoundTag playerDataTag) {
		this.uuid = uuid;
		this.playerDataTag = playerDataTag;
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.uuid = byteBuf.readUUID();
		this.playerDataTag = byteBuf.readNbt();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeUUID(uuid);
		byteBuf.writeNbt(playerDataTag);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        this.handle((net.minecraft.network.protocol.game.ClientGamePacketListener)ctx.get().getNetworkManager().getPacketListener());
	    });
	    ctx.get().setPacketHandled(true);
	}
	
	@SuppressWarnings("deprecation")
	public void handle(net.minecraft.network.protocol.game.ClientGamePacketListener handler) {
		LOGGER.debug("Handle ClientboundUpdateSlottedItemPacket");
		if (handler instanceof net.minecraft.client.multiplayer.ClientPacketListener) {
			DistExecutor.runWhenOn(Dist.CLIENT, () -> {return new Runnable() {
				@Override
				public void run() {
					net.minecraft.client.multiplayer.ClientPacketListener clientHandler = (net.minecraft.client.multiplayer.ClientPacketListener)handler;
					BioMechPlayerData playerData = BioMechPlayerData.deserialize(playerDataTag);
					BioMech.globalPlayerData.put(uuid, playerData);
				}
				};});
		}
	}
}