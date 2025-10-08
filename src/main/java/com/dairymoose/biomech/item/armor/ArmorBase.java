package com.dairymoose.biomech.item.armor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.client.screen.BioMechStationScreen;
import com.dairymoose.biomech.item.anim.MiningLaserDispatcher;

import mod.azure.azurelib.AzureLib;
import mod.azure.azurelib.rewrite.animation.AzAnimator;
import mod.azure.azurelib.rewrite.animation.AzAnimatorAccessor;
import mod.azure.azurelib.rewrite.animation.primitive.AzBakedAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

public class ArmorBase extends ArmorItem {

	protected boolean addToLootTable = true;
	protected MechPart mechPart = null;
	protected boolean hidePlayerModel = false;
	protected boolean alwaysHidePlayerHat = false;
	protected int suitEnergy = 0;
	protected float suitEnergyPerSec = 0.0f;
	private static ArmorMaterial NOTHING_MATERIAL = new NothingMaterial();
	protected float armDistance = 5.0f;
	
	protected float projectileAvoidPct = 0.0f;
	protected float damageAvoidPct = 0.0f;
	protected float damageAbsorbPct = 0.0f;
	
	public ArmorBase(ArmorMaterial material, Type type, Properties props) {
		super(NOTHING_MATERIAL, type, props);
	}
	
	public void onHandTick(boolean active, ItemStack itemStack, Player player, MechPart handPart, float partialTick, boolean bothHandsInactive, boolean bothHandsActive) {
		
	}
	
	public boolean onPlayerDamageTaken(DamageSource damageSource, float amount, ItemStack itemStack, Player player, MechPart handPart) {
		return false;
	}

	public float getProjectileAvoidPercent() {
		return this.projectileAvoidPct;
	}
	
	public float getDamageAvoidPercent() {
		return this.damageAvoidPct;
	}
	
	public float getDamageAbsorbPercent() {
		return this.damageAbsorbPct;
	}
	
	public float getArmDistance() {
		return this.armDistance;
	}
	
	public boolean shouldAddToLootTable() {
		return addToLootTable;
	}
	
	public float getSuitEnergy() {
		return this.suitEnergy;
	}
	
	public float getSuitEnergyPerSec() {
		return this.suitEnergyPerSec;
	}
	
	public Item getLeftArmItem() {
		return null;
	}

	public boolean shouldHidePlayerModel() {
		return this.hidePlayerModel;
	}
	
	public boolean alwaysHidePlayerHat() {
		return this.alwaysHidePlayerHat;
	}
	
	public MechPart getMechPart() {
		return this.mechPart;
	}
	
	@Override
	public boolean isDamageable(ItemStack stack) {
		if (FMLEnvironment.dist == Dist.CLIENT && !stack.getOrCreateTag().contains(AzureLib.ITEM_UUID_TAG)) {
			//another AzureLib bug - items sometimes don't have a UUID and the library crashes
			stack.getOrCreateTag().putUUID(AzureLib.ITEM_UUID_TAG, UUID.randomUUID());
		}
		return super.isDamageable(stack);
	}
	
	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
		return false;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level p_40395_, Player p_40396_, InteractionHand p_40397_) {
		return InteractionResultHolder.pass(p_40396_.getItemInHand(p_40397_));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> comp, TooltipFlag flags) {
		super.appendHoverText(stack, level, comp, flags);
		if (level != null && level.isClientSide && Minecraft.getInstance().screen instanceof BioMechStationScreen) {
			comp.add(Component.translatable("item.biomech.generic.tooltip2"));
		}
		else {
			comp.add(Component.translatable("item.biomech.generic.tooltip"));
		}
		if (this.mechPart != null) {
			MutableComponent slotTt = Component.translatable("tooltip.biomech.slot.tooltip");
			String partName = this.mechPart.name();
			if (mechPart == MechPart.RightArm) {
				partName = "Arm";
			}
			comp.add(Component.literal(slotTt.getString() + ": " + partName));
		}
		if (suitEnergy != 0) {
			MutableComponent suitEnergyTt = Component.translatable("tooltip.biomech.suitenergy");
			comp.add(Component.literal("§2+" + suitEnergy + " " + suitEnergyTt.getString() + "§0"));
		}
		if (suitEnergyPerSec != 0.0f) {
			NumberFormat nf = new DecimalFormat("##.##");
			MutableComponent suitEnergyTt = Component.translatable("tooltip.biomech.suitenergypersec");
			
			String prefix = "§b+";
			if (suitEnergyPerSec < 0.0f) {
				prefix = "§4";
			}
			comp.add(Component.literal(prefix + nf.format(suitEnergyPerSec) + " " + suitEnergyTt.getString() + "§0"));
		}
		comp.add(Component.empty());
		MutableComponent t1 = Component.translatableWithFallback("item.biomech." + ForgeRegistries.ITEMS.getKey(this).getPath() + ".tooltip1", "");
		MutableComponent t2 = Component.translatableWithFallback("item.biomech." + ForgeRegistries.ITEMS.getKey(this).getPath() + ".tooltip2", "");
		MutableComponent t3 = Component.translatableWithFallback("item.biomech." + ForgeRegistries.ITEMS.getKey(this).getPath() + ".tooltip3", "");
		if (!"".equals(t1.getString())) {
			comp.add(t1);
		}
		if (!"".equals(t2.getString())) {
			comp.add(t2);
		}
		if (!"".equals(t3.getString())) {
			comp.add(t3);
		}
	}
	
}
