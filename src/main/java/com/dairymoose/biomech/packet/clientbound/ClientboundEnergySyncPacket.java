package com.dairymoose.biomech.packet.clientbound;

import java.util.function.Supplier;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundEnergySyncPacket implements Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> {
	private float suitEnergy;
	private float suitEnergyMax;

	public ClientboundEnergySyncPacket() {
	}
	
	public ClientboundEnergySyncPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ClientboundEnergySyncPacket(float suitEnergy, float suitEnergyMax) {
		this.suitEnergy = suitEnergy;
		this.suitEnergyMax = suitEnergyMax;
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.suitEnergy = byteBuf.readFloat();
		this.suitEnergyMax = byteBuf.readFloat();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeFloat(suitEnergy);
		byteBuf.writeFloat(suitEnergyMax);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        this.handle((net.minecraft.network.protocol.game.ClientGamePacketListener)ctx.get().getNetworkManager().getPacketListener());
	    });
	    ctx.get().setPacketHandled(true);
	}
	
	@SuppressWarnings("deprecation")
	public void handle(net.minecraft.network.protocol.game.ClientGamePacketListener handler) {
		BioMech.LOGGER.trace("Handle ClientboundEnergySyncPacket");
		if (handler instanceof net.minecraft.client.multiplayer.ClientPacketListener) {
			DistExecutor.runWhenOn(Dist.CLIENT, () -> {return new Runnable() {
				@Override
				public void run() {
					net.minecraft.client.multiplayer.ClientPacketListener clientHandler = (net.minecraft.client.multiplayer.ClientPacketListener)handler;
					BioMechPlayerData playerData = BioMech.globalPlayerData.get(Minecraft.getInstance().player.getUUID());
					if (playerData != null) {
						playerData.suitEnergyMax = suitEnergyMax;
						playerData.setSuitEnergy(suitEnergy);
					}
				}
				};});
		}
	}
}