package com.dairymoose.biomech.packet.clientbound;

import java.util.UUID;
import java.util.function.Supplier;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundPressHotkeyPacket implements Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> {
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

	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        this.handle((net.minecraft.network.protocol.game.ClientGamePacketListener)ctx.get().getNetworkManager().getPacketListener());
	    });
	    ctx.get().setPacketHandled(true);
	}
	
	@SuppressWarnings("deprecation")
	public void handle(net.minecraft.network.protocol.game.ClientGamePacketListener handler) {
		BioMech.LOGGER.trace("Handle ClientboundPressHotkeyPacket");
		if (handler instanceof net.minecraft.client.multiplayer.ClientPacketListener) {
			DistExecutor.runWhenOn(Dist.CLIENT, () -> {return new Runnable() {
				@Override
				public void run() {
					net.minecraft.client.multiplayer.ClientPacketListener clientHandler = (net.minecraft.client.multiplayer.ClientPacketListener)handler;
					Level world = clientHandler.getLevel();
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
				}
				};});
		}
	}
}