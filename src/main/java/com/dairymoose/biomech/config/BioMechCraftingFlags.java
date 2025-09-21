package com.dairymoose.biomech.config;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.crafting.CraftingHelper;

public final class BioMechCraftingFlags {
	private static Map<String, Boolean> craftingFlags = new HashMap<String, Boolean>();
  
  public BioMechCraftingFlags() {
	CraftingHelper.register(new CraftingFlagConditionSerializer());
  }
  
  public void clear() {
    this.craftingFlags.clear();
  }
  
  public static void putFlag(String flag, boolean value) {
	  craftingFlags.put(flag, value);
  }
  
  public static boolean getFlag(String flag) {
	  Boolean result = craftingFlags.get(flag);
	  if (result != null)
		  return result;
	  return false;
  }
}
