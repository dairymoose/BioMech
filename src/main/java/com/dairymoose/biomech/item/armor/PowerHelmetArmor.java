package com.dairymoose.biomech.item.armor;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class PowerHelmetArmor extends ArmorBase {

	public PowerHelmetArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.mechPart = MechPart.HELMET;
	}

	@Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player) {
            player.getArmorSlots().forEach(wornArmor -> {
                if (wornArmor != null && wornArmor.is(BioMechRegistry.ITEM_POWER_CHEST.get())) {
                	if (entity instanceof LivingEntity living && !living.isSpectator()) {
                		;
                	}
                }
            });
        }
    }
	
}
	