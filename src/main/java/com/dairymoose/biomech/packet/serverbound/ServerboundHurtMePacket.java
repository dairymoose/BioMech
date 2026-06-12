package com.dairymoose.biomech.packet.serverbound;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerboundHurtMePacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ServerboundHurtMePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "hurt_me"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundHurtMePacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundHurtMePacket::write, ServerboundHurtMePacket::new);

	private float hurt;

	public ServerboundHurtMePacket() {
	}

	public ServerboundHurtMePacket(float hurt) {
		this.hurt = hurt;
	}

	public ServerboundHurtMePacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.hurt = byteBuf.readFloat();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeFloat(hurt);
	}

	@Override
	public CustomPacketPayload.Type<ServerboundHurtMePacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ServerboundHurtMePacket");
			Player player = context.player();
			if (player.level() != null) {
				BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
				if (playerData != null) {
					player.hurt(player.level().damageSources().flyIntoWall(), hurt);
				}
			}
		});
	}
}
