package com.dairymoose.biomech.packet.serverbound;

import java.util.function.Supplier;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.MobilityTreadsArmor;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor;
import com.dairymoose.biomech.packet.clientbound.ClientboundHandStatusPacket;

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

public class ServerboundMiningArmEntityTargetPacket implements Packet<ServerGamePacketListener> {
	private int entityId;

	public ServerboundMiningArmEntityTargetPacket() {
	}
	
	public ServerboundMiningArmEntityTargetPacket(FriendlyByteBuf buffer) {
		this.read(buffer);
	}

	public ServerboundMiningArmEntityTargetPacket(Entity entity) {
		if (entity == null)
			this.entityId = -1;
		else
			this.entityId = entity.getId();
	}

	public void read(FriendlyByteBuf byteBuf) {
		this.entityId = byteBuf.readInt();
	}

	public void write(FriendlyByteBuf byteBuf) {
		byteBuf.writeInt(entityId);
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
				Entity entity = null;
				if (this.entityId != -1) {
					entity = world.getEntity(this.entityId);
				}
				
				if (entity != null) {
					AbstractMiningArmArmor.entityTargetMap.put(serverHandler.player, entity);
					AbstractMiningArmArmor.blockTargetMap.remove(serverHandler.player);
				}
				else
					AbstractMiningArmArmor.entityTargetMap.remove(serverHandler.player);
			}
		}
	}
}