package com.dairymoose.biomech.packet.serverbound;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.PlayerDataContainer;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.PortableStorageUnitArmor;
import com.dairymoose.biomech.menu.PortableStorageUnitMenu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerboundOpenPortableStorageUnitPacket implements CustomPacketPayload, MenuProvider {
	public static final CustomPacketPayload.Type<ServerboundOpenPortableStorageUnitPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "open_portable_storage_unit"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenPortableStorageUnitPacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundOpenPortableStorageUnitPacket::write, ServerboundOpenPortableStorageUnitPacket::new);

	public ServerboundOpenPortableStorageUnitPacket() {
	}

	public ServerboundOpenPortableStorageUnitPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public void read(FriendlyByteBuf byteBuf) {
		;
	}

	public void write(FriendlyByteBuf byteBuf) {
		;
	}

	@Override
	public CustomPacketPayload.Type<ServerboundOpenPortableStorageUnitPacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ServerboundOpenPortableStorageUnitPacket");
			Player player = context.player();
			if (player.level() != null) {
				player.openMenu(this);
			}
		});
	}

	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
		BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
		if (playerData != null) {
			if (playerData.getForSlot(MechPart.Back).itemStack.getItem() instanceof PortableStorageUnitArmor psu) {
				return new PortableStorageUnitMenu(containerId, inventory, new PlayerDataContainer(playerData));
			}
		}
		return null;
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("");
	}
}
