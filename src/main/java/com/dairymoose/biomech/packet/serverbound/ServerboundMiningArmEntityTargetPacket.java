package com.dairymoose.biomech.packet.serverbound;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor.EntityTargetInfo;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerboundMiningArmEntityTargetPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ServerboundMiningArmEntityTargetPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "mining_arm_entity_target"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundMiningArmEntityTargetPacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundMiningArmEntityTargetPacket::write, ServerboundMiningArmEntityTargetPacket::new);

	private int entityId;
	private Vec3 hitLocation;

	public ServerboundMiningArmEntityTargetPacket() {
	}

	public ServerboundMiningArmEntityTargetPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ServerboundMiningArmEntityTargetPacket(Entity entity, Vec3 hitLocation) {
		if (hitLocation == null) {
			hitLocation = new Vec3(0, 0, 0);
		}

		if (entity == null)
			this.entityId = -1;
		else
			this.entityId = entity.getId();
		this.hitLocation = hitLocation;
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.entityId = byteBuf.readInt();
		this.hitLocation = new Vec3(byteBuf.readVector3f());
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeInt(entityId);
		byteBuf.writeVector3f(hitLocation.toVector3f());
	}

	@Override
	public CustomPacketPayload.Type<ServerboundMiningArmEntityTargetPacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ServerboundMiningArmEntityTargetPacket");
			Player player = context.player();
			if (player.level() != null) {
				Entity entity = null;
				if (this.entityId != -1) {
					entity = player.level().getEntity(this.entityId);
				}

				if (entity != null) {
					AbstractMiningArmArmor.entityTargetMap.put(player, new EntityTargetInfo(entity, hitLocation));
					AbstractMiningArmArmor.blockTargetMap.remove(player);
				} else {
					AbstractMiningArmArmor.entityTargetMap.remove(player);
				}
			}
		});
	}
}
