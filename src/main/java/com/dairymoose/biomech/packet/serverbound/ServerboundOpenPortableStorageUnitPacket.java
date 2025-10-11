package com.dairymoose.biomech.packet.serverbound;

import java.util.function.Supplier;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.PlayerDataContainer;
import com.dairymoose.biomech.menu.PortableStorageUnitMenu;
import com.dairymoose.biomech.packet.clientbound.ClientboundHandStatusPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class ServerboundOpenPortableStorageUnitPacket implements Packet<ServerGamePacketListener>, MenuProvider {
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

	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        ServerPlayer sender = ctx.get().getSender();
	        this.handle((ServerGamePacketListener)ctx.get().getNetworkManager().getPacketListener());
	    });
	    ctx.get().setPacketHandled(true);
	}
	
	public void handle(ServerGamePacketListener packetListener) {
		BioMech.LOGGER.trace("Handle ServerboundOpenPortableStorageUnitPacket");
		if (packetListener instanceof ServerGamePacketListenerImpl) {
			ServerGamePacketListenerImpl serverHandler = (ServerGamePacketListenerImpl)packetListener;
			Level world = serverHandler.player.level();
			if (world != null) {
				serverHandler.player.openMenu(this);
			}
		}
	}

	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
		BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
		if (playerData != null) {
			return new PortableStorageUnitMenu(containerId, inventory, new PlayerDataContainer(playerData));
		}
		return null;
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("");
	}
}