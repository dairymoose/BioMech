package com.dairymoose.biomech.packet.serverbound;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor.BlockTargetInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerboundMiningArmBlockTargetPacket implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ServerboundMiningArmBlockTargetPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "mining_arm_block_target"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundMiningArmBlockTargetPacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundMiningArmBlockTargetPacket::write, ServerboundMiningArmBlockTargetPacket::new);

	private boolean hasBlockTarget;
	private BlockPos target;
	private Vec3 hitLocation;

	public ServerboundMiningArmBlockTargetPacket() {
	}

	public ServerboundMiningArmBlockTargetPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ServerboundMiningArmBlockTargetPacket(BlockPos target, Vec3 hitLocation) {
		if (hitLocation == null) {
			hitLocation = new Vec3(0, 0, 0);
		}

		if (target == null)
			this.hasBlockTarget = false;
		else
			this.hasBlockTarget = true;
		this.target = target;
		this.hitLocation = hitLocation;
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.hasBlockTarget = byteBuf.readBoolean();
		if (hasBlockTarget) {
			this.target = byteBuf.readBlockPos();
			this.hitLocation = new Vec3(byteBuf.readVector3f());
		}
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeBoolean(hasBlockTarget);
		if (hasBlockTarget) {
			byteBuf.writeBlockPos(target);
			byteBuf.writeVector3f(hitLocation.toVector3f());
		}
	}

	@Override
	public CustomPacketPayload.Type<ServerboundMiningArmBlockTargetPacket> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			BioMech.LOGGER.trace("Handle ServerboundMiningArmBlockTargetPacket");
			Player player = context.player();
			if (player.level() != null) {
				if (hasBlockTarget) {
					AbstractMiningArmArmor.blockTargetMap.put(player, new BlockTargetInfo(target, hitLocation));
					AbstractMiningArmArmor.entityTargetMap.remove(player);
				} else {
					AbstractMiningArmArmor.blockTargetMap.remove(player);
				}
			}
		});
	}
}
