package com.dairymoose.biomech.packet.clientbound;

import java.util.UUID;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientboundPressHotkeyPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ClientboundPressHotkeyPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "press_hotkey_client"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPressHotkeyPacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundPressHotkeyPacket::write, ClientboundPressHotkeyPacket::new);

	public UUID playerUuid;
	public MechPart mechPart;
	public boolean isHotkeyDown;
	public int bonusData;

	public ClientboundPressHotkeyPacket() {
	}

	public ClientboundPressHotkeyPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ClientboundPressHotkeyPacket(Player player, MechPart mechPart, boolean isHotkeyDown, int bonusData) {
		this.playerUuid = player.getUUID();
		this.mechPart = mechPart;
		this.isHotkeyDown = isHotkeyDown;
		this.bonusData = bonusData;
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.playerUuid = byteBuf.readUUID();
		this.mechPart = MechPart.values()[byteBuf.readInt()];
		this.isHotkeyDown = byteBuf.readBoolean();
		this.bonusData = byteBuf.readInt();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeUUID(playerUuid);
		byteBuf.writeInt(mechPart.ordinal());
		byteBuf.writeBoolean(isHotkeyDown);
		byteBuf.writeInt(bonusData);
	}

	@Override
	public CustomPacketPayload.Type<ClientboundPressHotkeyPacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ClientboundPressHotkeyPacket");
			Level world = Minecraft.getInstance().level;
			if (world != null) {
				try {
					Player player = world.getPlayerByUUID(playerUuid);
					if (player != null) {
						if (!player.isLocalPlayer()) {
							BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
							if (playerData != null) {
								if (playerData.getForSlot(mechPart).itemStack.getItem() instanceof ArmorBase base) {
									base.onHotkeyPressed(player, playerData, isHotkeyDown, bonusData, true);
								}
							}
						}
					}
				} catch (Exception e) {
					BioMech.LOGGER.error("Error handling clientbound hotkey packet", e);
				}
			}
		});
	}
}
