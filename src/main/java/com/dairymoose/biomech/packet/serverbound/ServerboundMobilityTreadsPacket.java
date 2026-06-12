package com.dairymoose.biomech.packet.serverbound;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.ItemNbtHelper;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.MobilityTreadsArmor;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerboundMobilityTreadsPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ServerboundMobilityTreadsPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "mobility_treads"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundMobilityTreadsPacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundMobilityTreadsPacket::write, ServerboundMobilityTreadsPacket::new);

	private boolean speedBoost;

	public ServerboundMobilityTreadsPacket() {
	}

	public ServerboundMobilityTreadsPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ServerboundMobilityTreadsPacket(boolean speedBoost) {
		this.speedBoost = speedBoost;
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.speedBoost = byteBuf.readBoolean();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeBoolean(speedBoost);
	}

	@Override
	public CustomPacketPayload.Type<ServerboundMobilityTreadsPacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ServerboundMobilityTreadsPacket");
			Player player = context.player();
			if (player.level() != null) {
				BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
				if (playerData != null) {
					ItemStack itemStack = playerData.getForSlot(MechPart.Leggings).itemStack;
					if (itemStack.getItem() instanceof MobilityTreadsArmor armor) {
						ItemNbtHelper.update(itemStack, t -> t.putBoolean("WantSpeedBoost", this.speedBoost));
					}
				}
			}
		});
	}
}
