package com.dairymoose.biomech.packet.serverbound;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.packet.clientbound.ClientboundHandStatusPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerboundHandStatusPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ServerboundHandStatusPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "hand_status_server"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundHandStatusPacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundHandStatusPacket::write, ServerboundHandStatusPacket::new);

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

	@Override
	public CustomPacketPayload.Type<ServerboundHandStatusPacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ServerboundHandStatusPacket");
			Player player = context.player();
			if (player.level() != null) {
				if (has != null) {
					HandActiveStatus playerHas = BioMech.handActiveMap.put(player.getUUID(), has);
					BioMechNetwork.sendToAll(new ClientboundHandStatusPacket(player.getUUID(), playerHas));
				}
			}
		});
	}
}
