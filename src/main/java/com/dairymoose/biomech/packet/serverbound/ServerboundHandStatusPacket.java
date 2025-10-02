package com.dairymoose.biomech.packet.serverbound;

import java.util.function.Supplier;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.packet.clientbound.ClientboundHandStatusPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class ServerboundHandStatusPacket implements Packet<ServerGamePacketListener> {
	private HandActiveStatus has;

	public ServerboundHandStatusPacket() {
	}
	
	public ServerboundHandStatusPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ServerboundHandStatusPacket(HandActiveStatus has) {
		this.has = has;
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.has = HandActiveStatus.deserialize(byteBuf.readNbt());
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeNbt(HandActiveStatus.serialize(has));
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
				if (has != null) {
					HandActiveStatus playerHas = BioMech.handActiveMap.put(serverHandler.player.getUUID(), has);
					
					BioMechNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new ClientboundHandStatusPacket(serverHandler.player.getUUID(), playerHas));
				}
			}
		}
	}
}