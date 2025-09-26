package com.dairymoose.biomech;

import net.minecraft.nbt.CompoundTag;

public class HandActiveStatus {

	public static String LEFT_ACTIVE = "LeftHandActive";
	public static String RIGHT_ACTIVE = "RightHandActive";
	
	public boolean leftHandActive = false;
	public boolean rightHandActive = false;
	
	public static CompoundTag serialize(HandActiveStatus has) {
		CompoundTag root = new CompoundTag();
		
		root.putBoolean(LEFT_ACTIVE, has.leftHandActive);
		root.putBoolean(RIGHT_ACTIVE, has.rightHandActive);
		
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
		
		if (!valid)
			return null;
		return has;
	}
	
}
