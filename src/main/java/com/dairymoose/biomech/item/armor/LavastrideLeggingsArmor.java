package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class LavastrideLeggingsArmor extends ArmorBase {

	public LavastrideLeggingsArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 4;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Leggings;
	}
	
	private boolean checkForLava(Level level, BlockPos pos, boolean includeMagma) {
		BlockState blockState = level.getBlockState(pos);
		BlockState aboveItState = level.getBlockState(pos.above());
		
		boolean lavaFound = false;
		if (includeMagma) {
			lavaFound = (blockState.getFluidState().getFluidType() == ForgeMod.LAVA_TYPE.get() || blockState.is(Blocks.MAGMA_BLOCK)) && aboveItState.getFluidState().getFluidType() != ForgeMod.LAVA_TYPE.get();
		} else {
			lavaFound = blockState.getFluidState().getFluidType() == ForgeMod.LAVA_TYPE.get() && aboveItState.getFluidState().getFluidType() != ForgeMod.LAVA_TYPE.get();
		}
		
		return lavaFound;
	}
	
	private static float hoverHeight = 0.005f;
	private static float hoverMargin = 0.02f;
	private static float deltaYErrorMargin = 1.5f;
	@Override
    public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player) {
        	List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_LAVASTRIDE_LEGGINGS.get()) || slotId == -1) {
            	if (entity instanceof LivingEntity living && !living.isSpectator()) {
            		
            		if (!entity.isCrouching()) {
            			
						// delta.y typically is -0.08
						boolean anyLavaNearby = false;
						BlockPos foundPos = null;
						for (int x = -1; x <= 1; ++x) {
							for (int y = -1; y <= 0; ++y) {
								for (int z = -1; z <= 1; ++z) {
									BlockPos targetPos = BlockPos.containing(
											new Vec3(entity.getX() + x, entity.getY() + y, entity.getZ() + z));

									boolean lavaFound = this.checkForLava(level, targetPos, false);
									if (lavaFound) {
										if (foundPos == null || targetPos.getY() > foundPos.getY()) {
											foundPos = targetPos;
										}
										anyLavaNearby = true;
									}
								}
							}
						}

						BlockPos justBelowPos = BlockPos.containing(new Vec3(entity.getX(), entity.getY() - hoverHeight
								- hoverMargin + deltaYErrorMargin * entity.getDeltaMovement().y, entity.getZ()));
						boolean lavaJustBelow = this.checkForLava(level, justBelowPos, true);
						boolean snapToFoundPos = anyLavaNearby && entity.getDeltaMovement().y <= 1E-3 && foundPos != null;
						if (lavaJustBelow || snapToFoundPos) {
							float fluidBoost = 0.0f;

							double y = justBelowPos.above().getY();
							if (snapToFoundPos) {
								y = foundPos.above().getY();
								fluidBoost = -hoverHeight;
							}
							entity.setPos(entity.position().with(Axis.Y, y + hoverHeight + fluidBoost));
							entity.setDeltaMovement(entity.getDeltaMovement().with(Axis.Y, 0.0));
							entity.setOnGround(true);
							entity.resetFallDistance();
						}
            			
            		}
            		
            	}
			}
        }
    }
	
}
	