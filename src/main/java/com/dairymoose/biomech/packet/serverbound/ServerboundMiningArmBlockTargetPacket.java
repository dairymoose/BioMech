package com.dairymoose.biomech.packet.serverbound;

import java.util.function.Supplier;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.item.armor.AbstractMiningArm;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.MobilityTreadsArmor;
import com.dairymoose.biomech.packet.clientbound.ClientboundHandStatusPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class ServerboundMiningArmBlockTargetPacket implements Packet<ServerGamePacketListener> {
	private boolean hasBlockTarget;
	private BlockPos target;

	public ServerboundMiningArmBlockTargetPacket() {
	}
	
	public ServerboundMiningArmBlockTargetPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ServerboundMiningArmBlockTargetPacket(BlockPos target) {
		if (target == null)
			this.hasBlockTarget = false;
		else
			this.hasBlockTarget = true;
		this.target = target;
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.hasBlockTarget = byteBuf.readBoolean();
		if (hasBlockTarget)
			this.target = byteBuf.readBlockPos();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeBoolean(hasBlockTarget);
		if (hasBlockTarget)
			byteBuf.writeBlockPos(target);
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
					AbstractMiningArm.blockTargetMap.put(serverHandler.player, target);
					AbstractMiningArm.entityTargetMap.remove(serverHandler.player);
				}
				else {
					AbstractMiningArm.blockTargetMap.remove(serverHandler.player);
				}
			}
		}
	}
}