package com.dairymoose.biomech;

import net.minecraft.nbt.CompoundTag;

public class HandActiveStatus {

	public static String LEFT_ACTIVE = "LeftHandActive";
	public static String RIGHT_ACTIVE = "RightHandActive";
	public static String MODIFIER_ACTIVE = "ModifierActive";
	public static String JUMP_ACTIVE = "JumpActive";
	
	public boolean leftHandActive = false;
	public boolean rightHandActive = false;
	public boolean modifierKeyActive = false;
	public boolean jumpActive = false;
	
	public static CompoundTag serialize(HandActiveStatus has) {
		CompoundTag root = new CompoundTag();
		
		root.putBoolean(LEFT_ACTIVE, has.leftHandActive);
		root.putBoolean(RIGHT_ACTIVE, has.rightHandActive);
		root.putBoolean(MODIFIER_ACTIVE, has.modifierKeyActive);
		root.putBoolean(JUMP_ACTIVE, has.jumpActive);
		
		return root;
	}
	
	public static HandActiveStatus deserialize(CompoundTag tag) {
		boolean valid = true;
		
		HandActiveStatus has = new HandActiveStatus();
		
		if (tag.contains(LEFT_ACTIVE))
			has.leftHandActive = tag.getBoolean(LEFT_ACTIVE);
		else
			valid = false;
		
		if (tag.contains(RIGHT_ACTIVE))
			has.rightHandActive = tag.getBoolean(RIGHT_ACTIVE);
		else
			valid = false;
		
		if (tag.contains(MODIFIER_ACTIVE))
			has.modifierKeyActive = tag.getBoolean(MODIFIER_ACTIVE);
		else
			valid = false;
		
		if (tag.contains(JUMP_ACTIVE))
			has.jumpActive = tag.getBoolean(JUMP_ACTIVE);
		else
			valid = false;
		
		if (!valid)
			return null;
		return has;
	}
	
}
