package com.dairymoose.biomech.packet.serverbound;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.TeleportationCrystalArmor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class ServerboundTeleportationCrystalPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ServerboundTeleportationCrystalPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "teleportation_crystal"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundTeleportationCrystalPacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundTeleportationCrystalPacket::write, ServerboundTeleportationCrystalPacket::new);

	public ServerboundTeleportationCrystalPacket() {
	}

	public ServerboundTeleportationCrystalPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public void read(FriendlyByteBuf byteBuf) {
		;
	}

	public void write(FriendlyByteBuf byteBuf) {
		;
	}

	@Override
	public CustomPacketPayload.Type<ServerboundTeleportationCrystalPacket> type() {
		return TYPE;
	}

	public static int MINUTES_ALLOWED_TO_TELEPORT_BACK = 10;
	public static int TICKS_ALLOWED_TO_TELEPORT_BACK = (MINUTES_ALLOWED_TO_TELEPORT_BACK * 60) * 20;

	class TeleportBackInfo {
		public ServerLevel level;
		public Vec3 location;
		public int timestamp;
	}

	public static Map<UUID, TeleportBackInfo> teleportBackMap = new ConcurrentHashMap<>();

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.debug("Handle ServerboundTeleportationCrystalPacket");
			Player player = context.player();
			Level world = player.level();
			if (world != null) {
				BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
				if (playerData != null) {
					if (playerData.getForSlot(MechPart.Back).itemStack.getItem() instanceof TeleportationCrystalArmor tele) {
						if (player instanceof ServerPlayer sp) {
							if (world instanceof ServerLevel level) {
								try {
									TeleportBackInfo teleBack = teleportBackMap.get(player.getUUID());
									String teleText = "";

									if (teleBack != null) {
										int timeDiff = BioMech.currentServerTick - teleBack.timestamp;
										if (timeDiff > TICKS_ALLOWED_TO_TELEPORT_BACK) {
											teleBack = null;
										}
									}

									ServerLevel teleportLevel = null;
									Vec3 teleportDestination = null;
									if (teleBack == null) {
										ServerLevel respawnLevel = ServerLifecycleHooks.getCurrentServer().getLevel(sp.getRespawnDimension());
										Optional<Vec3> optional;
										BlockPos respawnPos = sp.getRespawnPosition();
										if (respawnPos == null) {
											respawnPos = level.getSharedSpawnPos();
										}
										BioMech.LOGGER.debug("Teleport home for: " + player + " with respawnPos=" + sp.getRespawnPosition());
										optional = Optional.of(Vec3.atBottomCenterOf(respawnPos));
										if (optional.isPresent()) {
											TeleportBackInfo info = new TeleportBackInfo();
											info.level = (ServerLevel) player.level();
											info.location = player.position();
											info.timestamp = BioMech.currentServerTick;
											teleportBackMap.put(player.getUUID(), info);
											teleportDestination = optional.get();
											teleportLevel = respawnLevel;
											teleText = "Teleported home";
										}
									} else {
										BioMech.LOGGER.debug("Teleport back for: " + player);
										teleportBackMap.remove(player.getUUID());
										teleportDestination = teleBack.location;
										teleportLevel = teleBack.level;
										teleText = "Teleported back to original location";
									}

									if (teleportLevel != null && teleportDestination != null) {
										BioMech.LOGGER.debug("Teleport to: " + teleportDestination + " for player=" + player);

										int particleCount = 15;
										for (int i = 0; i < particleCount; ++i) {
											Vec3 loc = player.position().add(new Vec3(2.0 * (Math.random() - 0.5), 1.3 + 0.25 * Math.random(), 2.0 * (Math.random() - 0.5)));
											level.sendParticles(ParticleTypes.ELECTRIC_SPARK, loc.x, loc.y, loc.z, 1, 0.0f, 0.0f, 0.0f, 1.0f);
										}

										sp.teleportTo(teleportLevel, teleportDestination.x, teleportDestination.y, teleportDestination.z, player.getYRot(), player.getXRot());
										if (teleText.length() > 0)
											sp.sendSystemMessage(Component.literal(teleText));
									} else {
										BioMech.LOGGER.error("teleport failed: " + teleportLevel + "/" + teleportDestination);
									}
								} catch (Exception e) {
									BioMech.LOGGER.error("Error during teleport for player " + player, e);
								}
							}
						}
					}
				}
			}
		});
	}
}
