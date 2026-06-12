package com.dairymoose.biomech.packet.clientbound;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientboundEnergySyncPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ClientboundEnergySyncPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "energy_sync"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundEnergySyncPacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundEnergySyncPacket::write, ClientboundEnergySyncPacket::new);

	private float suitEnergy;
	private float suitEnergyMax;
	private long remainingTicksForEnergyRegen;

	public ClientboundEnergySyncPacket() {
	}

	public ClientboundEnergySyncPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ClientboundEnergySyncPacket(float suitEnergy, float suitEnergyMax, long ticksUntilEnergyRegenPossible) {
		this.suitEnergy = suitEnergy;
		this.suitEnergyMax = suitEnergyMax;
		this.remainingTicksForEnergyRegen = ticksUntilEnergyRegenPossible;
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.suitEnergy = byteBuf.readFloat();
		this.suitEnergyMax = byteBuf.readFloat();
		this.remainingTicksForEnergyRegen = byteBuf.readLong();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeFloat(suitEnergy);
		byteBuf.writeFloat(suitEnergyMax);
		byteBuf.writeLong(remainingTicksForEnergyRegen);
	}

	@Override
	public CustomPacketPayload.Type<ClientboundEnergySyncPacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ClientboundEnergySyncPacket");
			BioMechPlayerData playerData = BioMech.globalPlayerData.get(context.player().getUUID());
			if (playerData != null) {
				playerData.suitEnergyMax = suitEnergyMax;
				playerData.setSuitEnergy(suitEnergy);
				if (remainingTicksForEnergyRegen > 0) {
					playerData.lastUsedEnergyTick = playerData.tickCount - (BioMechPlayerData.ticksRequiredToRegenEnergy - remainingTicksForEnergyRegen);
				}
			}
		});
	}
}
