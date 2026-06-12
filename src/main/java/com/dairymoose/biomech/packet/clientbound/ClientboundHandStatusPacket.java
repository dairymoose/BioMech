package com.dairymoose.biomech.packet.clientbound;

import java.util.UUID;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.HandActiveStatus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientboundHandStatusPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ClientboundHandStatusPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "hand_status_client"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundHandStatusPacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundHandStatusPacket::write, ClientboundHandStatusPacket::new);

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

	@Override
	public CustomPacketPayload.Type<ClientboundHandStatusPacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ClientboundHandStatusPacket");
			if (has != null) {
				HandActiveStatus playerHas = BioMech.handActiveMap.computeIfAbsent(uuid, (u) -> new HandActiveStatus());
				playerHas.leftHandActive = has.leftHandActive;
				playerHas.rightHandActive = has.rightHandActive;
			}
		});
	}
}
