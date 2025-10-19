package com.dairymoose.biomech.packet.serverbound;

import java.util.function.Supplier;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor.EntityTargetInfo;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundMiningArmEntityTargetPacket implements Packet<ServerGamePacketListener> {
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
					AbstractMiningArmArmor.entityTargetMap.put(serverHandler.player, new EntityTargetInfo(entity, hitLocation));
					AbstractMiningArmArmor.blockTargetMap.remove(serverHandler.player);
				}
				else
					AbstractMiningArmArmor.entityTargetMap.remove(serverHandler.player);
			}
		}
	}
}