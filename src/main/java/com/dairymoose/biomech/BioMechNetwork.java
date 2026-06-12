package com.dairymoose.biomech;

import com.dairymoose.biomech.packet.clientbound.ClientboundEnergySyncPacket;
import com.dairymoose.biomech.packet.clientbound.ClientboundHandStatusPacket;
import com.dairymoose.biomech.packet.clientbound.ClientboundPressHotkeyPacket;
import com.dairymoose.biomech.packet.clientbound.ClientboundProjectileDodgePacket;
import com.dairymoose.biomech.packet.clientbound.ClientboundUpdateSlottedItemPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundHandStatusPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundHurtMePacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundMiningArmBlockTargetPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundMiningArmEntityTargetPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundMobilityTreadsPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundOpenPortableStorageUnitPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundPressHotkeyPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundResetFallDamagePacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundTeleportationCrystalPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundUpdateVisibilityPacket;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = BioMech.MODID, bus = EventBusSubscriber.Bus.MOD)
public class BioMechNetwork {
	private static final String PROTOCOL_VERSION = "1";

	@SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

		registrar.playToClient(ClientboundUpdateSlottedItemPacket.TYPE, ClientboundUpdateSlottedItemPacket.STREAM_CODEC, ClientboundUpdateSlottedItemPacket::handle);
		registrar.playToServer(ServerboundHandStatusPacket.TYPE, ServerboundHandStatusPacket.STREAM_CODEC, ServerboundHandStatusPacket::handle);
		registrar.playToClient(ClientboundHandStatusPacket.TYPE, ClientboundHandStatusPacket.STREAM_CODEC, ClientboundHandStatusPacket::handle);
		registrar.playToClient(ClientboundEnergySyncPacket.TYPE, ClientboundEnergySyncPacket.STREAM_CODEC, ClientboundEnergySyncPacket::handle);
		registrar.playToServer(ServerboundMobilityTreadsPacket.TYPE, ServerboundMobilityTreadsPacket.STREAM_CODEC, ServerboundMobilityTreadsPacket::handle);
		registrar.playToServer(ServerboundMiningArmEntityTargetPacket.TYPE, ServerboundMiningArmEntityTargetPacket.STREAM_CODEC, ServerboundMiningArmEntityTargetPacket::handle);
		registrar.playToClient(ClientboundProjectileDodgePacket.TYPE, ClientboundProjectileDodgePacket.STREAM_CODEC, ClientboundProjectileDodgePacket::handle);
		registrar.playToServer(ServerboundMiningArmBlockTargetPacket.TYPE, ServerboundMiningArmBlockTargetPacket.STREAM_CODEC, ServerboundMiningArmBlockTargetPacket::handle);
		registrar.playToServer(ServerboundOpenPortableStorageUnitPacket.TYPE, ServerboundOpenPortableStorageUnitPacket.STREAM_CODEC, ServerboundOpenPortableStorageUnitPacket::handle);
		registrar.playToServer(ServerboundTeleportationCrystalPacket.TYPE, ServerboundTeleportationCrystalPacket.STREAM_CODEC, ServerboundTeleportationCrystalPacket::handle);
		registrar.playToServer(ServerboundPressHotkeyPacket.TYPE, ServerboundPressHotkeyPacket.STREAM_CODEC, ServerboundPressHotkeyPacket::handle);
		registrar.playToClient(ClientboundPressHotkeyPacket.TYPE, ClientboundPressHotkeyPacket.STREAM_CODEC, ClientboundPressHotkeyPacket::handle);
		registrar.playToServer(ServerboundUpdateVisibilityPacket.TYPE, ServerboundUpdateVisibilityPacket.STREAM_CODEC, ServerboundUpdateVisibilityPacket::handle);
		registrar.playToServer(ServerboundResetFallDamagePacket.TYPE, ServerboundResetFallDamagePacket.STREAM_CODEC, ServerboundResetFallDamagePacket::handle);
		registrar.playToServer(ServerboundHurtMePacket.TYPE, ServerboundHurtMePacket.STREAM_CODEC, ServerboundHurtMePacket::handle);
	}

	public static void sendToAll(CustomPacketPayload payload) {
		PacketDistributor.sendToAllPlayers(payload);
	}

	public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
		PacketDistributor.sendToPlayer(player, payload);
	}

	public static void sendToServer(CustomPacketPayload payload) {
		PacketDistributor.sendToServer(payload);
	}
}
