package com.dairymoose.biomech.packet.clientbound;

import java.util.UUID;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.HandActiveStatus;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundHandStatusPacket implements Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> {
	private UUID uuid;
	private HandActiveStatus has;

	public ClientboundHandStatusPacket() {
	}
	
	public ClientboundHandStatusPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ClientboundHandStatusPacket(UUID uuid, HandActiveStatus has) {
		this.uuid = uuid;
		this.has = has;
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.uuid = byteBuf.readUUID();
		this.has = HandActiveStatus.deserialize(byteBuf.readNbt());
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeUUID(uuid);
		byteBuf.writeNbt(HandActiveStatus.serialize(has));
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        this.handle((net.minecraft.network.protocol.game.ClientGamePacketListener)ctx.get().getNetworkManager().getPacketListener());
	    });
	    ctx.get().setPacketHandled(true);
	}
	
	@SuppressWarnings("deprecation")
	public void handle(net.minecraft.network.protocol.game.ClientGamePacketListener handler) {
		BioMech.LOGGER.debug("Handle ClientboundHandStatusPacket");
		if (handler instanceof net.minecraft.client.multiplayer.ClientPacketListener) {
			DistExecutor.runWhenOn(Dist.CLIENT, () -> {return new Runnable() {
				@Override
				public void run() {
					if (has != null) {
						net.minecraft.client.multiplayer.ClientPacketListener clientHandler = (net.minecraft.client.multiplayer.ClientPacketListener)handler;
						HandActiveStatus playerHas = BioMech.handActiveMap.computeIfAbsent(uuid, (uuid) -> new HandActiveStatus());
						playerHas.leftHandActive = has.leftHandActive;
						playerHas.rightHandActive = has.rightHandActive;
					}
				}
				};});
		}
	}
}