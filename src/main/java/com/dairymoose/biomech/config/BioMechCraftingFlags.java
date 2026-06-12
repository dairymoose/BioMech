package com.dairymoose.biomech.config;

import java.util.HashMap;
import java.util.Map;

public final class BioMechCraftingFlags {
	private static Map<String, Boolean> craftingFlags = new HashMap<String, Boolean>();

  public BioMechCraftingFlags() {
	// The recipe-condition codec is registered via the CONDITION_CODECS DeferredRegister in BioMech.
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
