package com.dairymoose.biomech.item.armor;

import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LavastrideLeggingsArmor extends ArmorBase {

	public LavastrideLeggingsArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 4;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Leggings;
	}
	
	@Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player ) {
            player.getArmorSlots().forEach(wornArmor -> {
                if (wornArmor != null && wornArmor.is(BioMechRegistry.ITEM_LAVASTRIDE_LEGGINGS.get())) {
                	if (entity instanceof LivingEntity living && !living.isSpectator()) {
                		if (!level.isClientSide) {
                			//living.addEffect(new MobEffectInstance(MobEffects.l, 30, newJumpBoost));
                		}
                	}
                }
            });
        }
    }
	
}
	