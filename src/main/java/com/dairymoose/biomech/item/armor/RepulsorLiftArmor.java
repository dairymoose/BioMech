package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.anim.RepulsorLiftDispatcher;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RepulsorLiftArmor extends ArmorBase {

	public final RepulsorLiftDispatcher dispatcher;
	
	public static boolean particleEnabled = true;
	
	public RepulsorLiftArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 20;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Leggings;
		this.dispatcher = new RepulsorLiftDispatcher();
		
		this.viewBobDisabled = true;
		this.viewBobArmSwayModifier = 0.0f;
	}

	public static Player currentPlayer = null;
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_REPULSOR_LIFT.get()) || slotId == -1) {
				if (level.isClientSide) {
					if (!player.isSpectator()) {
						BioMech.clientSideItemAnimation(stack, this.dispatcher.PASSIVE_COMMAND.cmd);
						
						if (player.tickCount % 10 == 0) {
							if (particleEnabled) {
								ParticleOptions repulsorParticle = (ParticleOptions) BioMechRegistry.PARTICLE_TYPE_REPULSOR.get();
								Vec3 loc = player.position().add(0.0, 0.40, 0.0);
								Vec3 delta = player.getDeltaMovement();
								RepulsorLiftArmor.currentPlayer = player;
								player.level().addParticle(repulsorParticle, loc.x, loc.y, loc.z, 0.0, -0.15, 0.0);
							}
						}
					}
				}
			}
		}
	}
	
}
