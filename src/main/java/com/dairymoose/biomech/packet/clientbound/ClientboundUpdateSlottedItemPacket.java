package com.dairymoose.biomech.packet.clientbound;

import java.util.UUID;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class ClientboundUpdateSlottedItemPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ClientboundUpdateSlottedItemPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "update_slotted_item"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateSlottedItemPacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundUpdateSlottedItemPacket::write, ClientboundUpdateSlottedItemPacket::new);

	private UUID uuid;
	private CompoundTag playerDataTag;

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

	@Override
	public CustomPacketPayload.Type<ClientboundUpdateSlottedItemPacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.debug("Handle ClientboundUpdateSlottedItemPacket");
			try {
				BioMechPlayerData incomingPlayerData = BioMechPlayerData.deserialize(playerDataTag);

				BioMechPlayerData existingPlayerData = BioMech.globalPlayerData.get(uuid);
				// in singleplayer the globalPlayerData is shared between client + server - if we overwrite this we lose the items in the backpack (they are not serialized)
				if (ServerLifecycleHooks.getCurrentServer() == null || !ServerLifecycleHooks.getCurrentServer().isSingleplayer()) {
					if (existingPlayerData == null) {
						BioMech.globalPlayerData.put(uuid, incomingPlayerData);
					} else {
						existingPlayerData.overwrite(incomingPlayerData);
					}
				}
			} catch (Exception e) {
				BioMech.LOGGER.error("Failed to deserialize data", e);
			}
		});
	}
}
