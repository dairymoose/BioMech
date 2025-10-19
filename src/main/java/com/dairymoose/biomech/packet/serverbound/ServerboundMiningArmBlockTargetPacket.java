package com.dairymoose.biomech.packet.serverbound;

import java.util.function.Supplier;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor.BlockTargetInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundMiningArmBlockTargetPacket implements Packet<ServerGamePacketListener> {
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

	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        ServerPlayer sender = ctx.get().getSender();
	        this.handle((ServerGamePacketListener)ctx.get().getNetworkManager().getPacketListener());
	    });
	    ctx.get().setPacketHandled(true);
	}
	
	public void handle(ServerGamePacketListener packetListener) {
		BioMech.LOGGER.trace("Handle ServerboundMiningArmEntityTargetPacket");
		if (packetListener instanceof ServerGamePacketListenerImpl) {
			ServerGamePacketListenerImpl serverHandler = (ServerGamePacketListenerImpl)packetListener;
			Level world = serverHandler.player.level();
			if (world != null) {
				if (hasBlockTarget) {
					AbstractMiningArmArmor.blockTargetMap.put(serverHandler.player, new BlockTargetInfo(target, hitLocation));
					AbstractMiningArmArmor.entityTargetMap.remove(serverHandler.player);
				}
				else {
					AbstractMiningArmArmor.blockTargetMap.remove(serverHandler.player);
				}
			}
		}
	}
}