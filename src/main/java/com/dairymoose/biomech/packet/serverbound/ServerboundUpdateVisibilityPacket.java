package com.dairymoose.biomech.packet.serverbound;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerboundUpdateVisibilityPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ServerboundUpdateVisibilityPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "update_visibility"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundUpdateVisibilityPacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundUpdateVisibilityPacket::write, ServerboundUpdateVisibilityPacket::new);

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

	@Override
	public CustomPacketPayload.Type<ServerboundUpdateVisibilityPacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ServerboundUpdateVisibilityPacket");
			Player player = context.player();
			if (player.level() != null && player instanceof ServerPlayer serverPlayer) {
				BioMechPlayerData playerData = BioMech.globalPlayerData.get(serverPlayer.getUUID());

				if (playerData != null) {
					BioMechPlayerData incomingPlayerData = BioMechPlayerData.deserialize(playerDataTag);

					if (incomingPlayerData != null) {
						for (SlottedItem slottedItem : playerData.getAllSlots()) {
							SlottedItem incomingItem = incomingPlayerData.getForSlot(slottedItem.mechPart);
							slottedItem.visible = incomingItem.visible;
						}

						BioMech.sendItemSlotUpdateForPlayer(serverPlayer);
					}
				}
			}
		});
	}
}
