package com.dairymoose.biomech.packet.clientbound;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.armor.InterceptorArmsArmor;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientboundProjectileDodgePacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ClientboundProjectileDodgePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "projectile_dodge"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundProjectileDodgePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundProjectileDodgePacket::write, ClientboundProjectileDodgePacket::new);

	int playerId;

	public ClientboundProjectileDodgePacket() {
	}

	public ClientboundProjectileDodgePacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ClientboundProjectileDodgePacket(Player player) {
		this.playerId = player.getId();
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.playerId = byteBuf.readInt();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeInt(playerId);
	}

	@Override
	public CustomPacketPayload.Type<ClientboundProjectileDodgePacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ClientboundProjectileDodgePacket");
			Level world = context.player().level();
			if (world != null) {
				Entity e = world.getEntity(playerId);
				if (e instanceof Player p) {
					InterceptorArmsArmor.dodgedProjectileSet.add(p);
				}
			}
		});
	}
}
