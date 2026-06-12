package com.dairymoose.biomech.packet.serverbound;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BroadcastType;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.packet.clientbound.ClientboundPressHotkeyPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerboundPressHotkeyPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ServerboundPressHotkeyPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "press_hotkey_server"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundPressHotkeyPacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundPressHotkeyPacket::write, ServerboundPressHotkeyPacket::new);

	public MechPart mechPart;
	public boolean isHotkeyDown;
	public BroadcastType broadcastType;
	public int bonusData;

	public ServerboundPressHotkeyPacket() {
	}

	public ServerboundPressHotkeyPacket(ArmorBase base, boolean isHotkeyDown, int bonusData, BroadcastType broadcastType) {
		this.mechPart = base.getMechPart();
		this.isHotkeyDown = isHotkeyDown;
		this.broadcastType = broadcastType;
		this.bonusData = bonusData;
	}

	public ServerboundPressHotkeyPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.mechPart = MechPart.values()[byteBuf.readInt()];
		this.isHotkeyDown = byteBuf.readBoolean();
		this.broadcastType = BroadcastType.values()[byteBuf.readInt()];
		this.bonusData = byteBuf.readInt();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeInt(mechPart.ordinal());
		byteBuf.writeBoolean(isHotkeyDown);
		byteBuf.writeInt(broadcastType.ordinal());
		byteBuf.writeInt(bonusData);
	}

	@Override
	public CustomPacketPayload.Type<ServerboundPressHotkeyPacket> type() {
		return TYPE;
	}

	public static int MINUTES_ALLOWED_TO_TELEPORT_BACK = 10;
	public static int TICKS_ALLOWED_TO_TELEPORT_BACK = (MINUTES_ALLOWED_TO_TELEPORT_BACK * 60) * 20;

	class TeleportBackInfo {
		public ServerLevel level;
		public Vec3 location;
		public int timestamp;
	}

	public static Map<UUID, TeleportBackInfo> teleportBackMap = new ConcurrentHashMap<>();

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ServerboundPressHotkeyPacket");
			Player player = context.player();
			if (player.level() != null) {
				try {
					BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
					if (playerData != null) {
						if (playerData.getForSlot(this.mechPart).itemStack.getItem() instanceof ArmorBase base) {
							base.onHotkeyPressed(player, playerData, this.isHotkeyDown, bonusData, true);
						}
						if (this.broadcastType == BroadcastType.SEND_TO_ALL_CLIENTS) {
							BioMechNetwork.sendToAll(new ClientboundPressHotkeyPacket(player, mechPart, isHotkeyDown, bonusData));
						}
					}
				} catch (Exception e) {
					BioMech.LOGGER.error("Error handling serverbound hotkey packet", e);
				}
			}
		});
	}
}
