package com.dairymoose.biomech.item.armor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

import com.dairymoose.biomech.BioMech.ClientModEvents;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BroadcastType;
import com.dairymoose.biomech.client.screen.BioMechStationScreen;
import com.dairymoose.biomech.item.armor.arm.MiningLaserArmArmor;
import com.dairymoose.biomech.packet.serverbound.ServerboundPressHotkeyPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundTeleportationCrystalPacket;

import mod.azure.azurelib.AzureLib;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
	protected float armY = 2.0f;
	protected float headY = 0.0f;
	protected double backArmorTranslation = 0.0f;
	protected float hpBoostAmount = 0.0f;
	protected float xpBoostAmount = 0.0f;
	protected boolean viewBobDisabled = false;
	protected float viewBobArmSwayModifier = 1.0f;
	
	protected float projectileAvoidPct = 0.0f;
	protected float damageAvoidPct = 0.0f;
	protected float damageAbsorbPct = 0.0f;
	protected float criticalStrikeBoost = 0.0f;
	protected float nearbyEnemyDamageBoost = 0.0f;
	protected float explosionDamageReduction = 0.0f;
	protected float absorbedDamageEnergyMult = 20.0f;
	protected float flatDamageBoost = 0.0f;
	protected float bonusBlockReach = 0.0f;
	protected float bonusEntityReach = 0.0f;
	protected float forceFieldDuration = 0.0f;
	protected float forceFieldCooldown = 0.0f;
	
	protected boolean hasAttributeModifier = false;
	
	public boolean configDisabled = false;
	
	public ArmorBase(ArmorMaterial material, Type type, Properties props) {
		super(NOTHING_MATERIAL, type, props);
	}
	
	protected void sendHotkeyToServer(Player player, boolean keysIsDown, BroadcastType broadcastType, boolean serverOriginator) {
		if (serverOriginator)
			return;
		
		if (player.level().isClientSide)
			BioMechNetwork.INSTANCE.sendToServer(new ServerboundPressHotkeyPacket(this, keysIsDown, broadcastType));
	}
	
	public void onHotkeyPressed(Player player, BioMechPlayerData playerData, boolean keyIsDown, boolean serverOriginator) {
		
	}
	
	public void onHotkeyHeld(Player player, BioMechPlayerData playerData) {
		
	}
	
	//itemStack is the first person arm itemStack, need to call BioMech.getThirdPersonArmItemStack for 3rd person
	public void onHandTick(boolean active, ItemStack itemStack, Player player, MechPart handPart, float partialTick, boolean bothHandsInactive, boolean bothHandsActive) {
		
	}
	
	public boolean onPlayerDamageTaken(DamageSource damageSource, float amount, ItemStack itemStack, Player player, MechPart handPart) {
		return false;
	}
	
	SlottedItem empty = new SlottedItem(null);
	@Override
	public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		this.biomechInventoryTick(empty, itemStack, level, entity, slotId, isSelected);
	}
	
	//itemStack is the third person itemStack (i.e., the real itemStack)
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		;
	}
	
	public boolean isDisabled() {
		return this.configDisabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.configDisabled = disabled;
	}
	
	public float getForcefieldCooldown() {
		return this.forceFieldCooldown;
	}
	
	public float getForcefieldDuration() {
		return this.forceFieldDuration;
	}
	
	public boolean getHasAttributeModifier() {
		return this.hasAttributeModifier;
	}
	
	public float getBonusBlockReach() {
		return this.bonusBlockReach;
	}
	
	public float getBonusEntityReach() {
		return this.bonusEntityReach;
	}
	
	public float getFlatDamageBoost() {
		return this.flatDamageBoost;
	}
	
	public float getArmY() {
		return this.armY;
	}
	
	public float getHeadY() {
		return this.headY;
	}
	
	public float getAbsorbedDamageEnergyMult() {
		return this.absorbedDamageEnergyMult;
	}
	
	public float getViewBobArmSwayModifier() {
		return this.viewBobArmSwayModifier;
	}
	
	public boolean isViewBobDisabled() {
		return this.viewBobDisabled;
	}
	
	public double getBackArmorTranslation() {
		return this.backArmorTranslation;
	}
	
	public float getXpBoostAmount() {
		return this.xpBoostAmount;
	}
	
	public float getHpBoostAmount() {
		return this.hpBoostAmount;
	}
	
	public float getExplosionDamageReduction() {
		return this.explosionDamageReduction;
	}
	
	public float getNearbyEnemyDamageBoost() {
		return this.nearbyEnemyDamageBoost;
	}
	
	public float getCriticalStrikeBoost() {
		return this.criticalStrikeBoost;
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
	
	public static boolean mousingOverLeftArm = false;
	private String replaceTooltips(String input) {
		if (input == null)
			return null;
		if (input.length() <= 0)
			return input;
		
		String replaced = input;
		
		if (input.contains("{")) {
			NumberFormat nf = new DecimalFormat("#.#");
			replaced = replaced.replaceAll("\\{alt\\}", ClientModEvents.HOTKEY_ENABLE_ARM_FUNCTION.getKey().getDisplayName().getString());
			if (mousingOverLeftArm)
				replaced = replaced.replaceAll("\\{lmb\\}", ClientModEvents.HOTKEY_LEFT_ARM.getKey().getDisplayName().getString());
			else
				replaced = replaced.replaceAll("\\{lmb\\}", ClientModEvents.HOTKEY_RIGHT_ARM.getKey().getDisplayName().getString());
			replaced = replaced.replaceAll("\\{rmb\\}", ClientModEvents.HOTKEY_LEFT_ARM.getKey().getDisplayName().getString());
			replaced = replaced.replaceAll("Left Button", "Left Mouse-Click");
			replaced = replaced.replaceAll("Right Button", "Right Mouse-Click");
			replaced = replaced.replaceAll("\\{dr\\}", nf.format(100.0f*this.getDamageAbsorbPercent()));
			replaced = replaced.replaceAll("\\{avoid\\}", nf.format(100.0f*this.getDamageAvoidPercent()));
			replaced = replaced.replaceAll("\\{absorb_energy_harm\\}", nf.format(this.getAbsorbedDamageEnergyMult()));
			replaced = replaced.replaceAll("\\{crit_boost\\}", nf.format(100.0f*this.getCriticalStrikeBoost()));
			replaced = replaced.replaceAll("\\{near_damage_boost\\}", nf.format(100.0f*this.getNearbyEnemyDamageBoost()));
			replaced = replaced.replaceAll("\\{nearby_enemy_range\\}", nf.format(HerosHeadpieceArmor.nearbyEnemiesDiameter));
			replaced = replaced.replaceAll("\\{explosion_dr\\}", nf.format(100.0f*this.getExplosionDamageReduction()));
			replaced = replaced.replaceAll("\\{key_back\\}", ClientModEvents.HOTKEY_ACTIVATE_BACK_ITEM.getKey().getDisplayName().getString());
			replaced = replaced.replaceAll("\\{key_head\\}", ClientModEvents.HOTKEY_ACTIVATE_HEAD_ITEM.getKey().getDisplayName().getString());
			replaced = replaced.replaceAll("\\{key_chest\\}", ClientModEvents.HOTKEY_ACTIVATE_CHEST_ITEM.getKey().getDisplayName().getString());
			replaced = replaced.replaceAll("\\{key_leggings\\}", ClientModEvents.HOTKEY_ACTIVATE_LEGGINGS_ITEM.getKey().getDisplayName().getString());
			replaced = replaced.replaceAll("\\{mining_laser_max_time\\}", nf.format(MiningLaserArmArmor.SECONDS_UNTIL_MAX_LASER));
			replaced = replaced.replaceAll("\\{mining_laser_max_power\\}", nf.format(MiningLaserArmArmor.MAX_POWER));
			replaced = replaced.replaceAll("\\{teleport_hold_time\\}", nf.format(TeleportationCrystalArmor.TELEPORT_HOLD_TIME_TICKS/20.0f));
			replaced = replaced.replaceAll("\\{teleport_back_time\\}", nf.format(ServerboundTeleportationCrystalPacket.MINUTES_ALLOWED_TO_TELEPORT_BACK));
			replaced = replaced.replaceAll("\\{hp_boost\\}", nf.format(this.getHpBoostAmount()));
			replaced = replaced.replaceAll("\\{xp_boost\\}", nf.format(100.0f*this.getXpBoostAmount()));
			replaced = replaced.replaceAll("\\{flat_damage_boost\\}", nf.format(this.getFlatDamageBoost()));
			replaced = replaced.replaceAll("\\{bonus_block_reach\\}", nf.format(this.getBonusBlockReach()));
			replaced = replaced.replaceAll("\\{bonus_entity_reach\\}", nf.format(this.getBonusEntityReach()));
			replaced = replaced.replaceAll("\\{forcefield_duration\\}", nf.format(this.getForcefieldDuration()));
			replaced = replaced.replaceAll("\\{forcefield_cooldown\\}", nf.format(this.getForcefieldCooldown()));
		}
		
		return replaced;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> comp, TooltipFlag flags) {
		super.appendHoverText(stack, level, comp, flags);
		if (configDisabled) {
			comp.add(Component.literal("Looting disabled by config!"));
		}
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
		MutableComponent t4 = Component.translatableWithFallback("item.biomech." + ForgeRegistries.ITEMS.getKey(this).getPath() + ".tooltip4", "");
		MutableComponent t5 = Component.translatableWithFallback("item.biomech." + ForgeRegistries.ITEMS.getKey(this).getPath() + ".tooltip5", "");
		String t1Text = this.replaceTooltips(t1.getString());
		if (!"".equals(t1Text)) {
			comp.add(Component.literal(t1Text));
		}
		String t2Text = this.replaceTooltips(t2.getString());
		if (!"".equals(t2Text)) {
			comp.add(Component.literal(t2Text));
		}
		String t3Text = this.replaceTooltips(t3.getString());
		if (!"".equals(t3Text)) {
			comp.add(Component.literal(t3Text));
		}
		String t4Text = this.replaceTooltips(t4.getString());
		if (!"".equals(t4Text)) {
			comp.add(Component.literal(t4Text));
		}
		String t5Text = this.replaceTooltips(t5.getString());
		if (!"".equals(t5Text)) {
			comp.add(Component.literal(t5Text));
		}
	}
	
}
