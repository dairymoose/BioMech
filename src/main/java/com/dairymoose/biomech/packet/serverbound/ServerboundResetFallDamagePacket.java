package com.dairymoose.biomech.packet.serverbound;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.item.armor.arm.GrappleArmArmor;
import com.dairymoose.biomech.item.armor.arm.GrappleArmArmor.GrappleInfo;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerboundResetFallDamagePacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ServerboundResetFallDamagePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "reset_fall_damage"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundResetFallDamagePacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundResetFallDamagePacket::write, ServerboundResetFallDamagePacket::new);

	public ServerboundResetFallDamagePacket() {
	}

	public ServerboundResetFallDamagePacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public void read(FriendlyByteBuf byteBuf) {
		;
	}

	public void write(FriendlyByteBuf byteBuf) {
		;
	}

	@Override
	public CustomPacketPayload.Type<ServerboundResetFallDamagePacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ServerboundResetFallDamagePacket");
			Player player = context.player();
			if (player.level() != null) {
				BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
				if (playerData != null) {
					GrappleInfo grappleInfo = GrappleArmArmor.grappleInfoMap.get(player.getUUID());
					if (grappleInfo != null && grappleInfo.hookPos != null && grappleInfo.grappleEntity != null) {
						player.resetFallDistance();
					}
				}
			}
		});
	}
}
