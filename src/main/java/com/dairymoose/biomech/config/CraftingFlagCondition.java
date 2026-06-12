package com.dairymoose.biomech.config;

import com.dairymoose.biomech.BioMech;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.neoforged.neoforge.common.conditions.ICondition;

/**
 * NeoForge 1.21 recipe condition: enables/disables a recipe based on a named BioMech crafting flag.
 * Conditions are now codec-based; the codec is registered into the CONDITION_CODECS registry in BioMech.
 */
public record CraftingFlagCondition(String crafting_flag) implements ICondition {

	public static final MapCodec<CraftingFlagCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
			Codec.STRING.fieldOf("crafting_flag").forGetter(CraftingFlagCondition::crafting_flag))
			.apply(builder, CraftingFlagCondition::new));

	@Override
	public boolean test(IContext context) {
		boolean result = BioMechCraftingFlags.getFlag(crafting_flag);
		if (!result)
			BioMech.LOGGER.debug(crafting_flag + ": disabled");
		return result;
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}
}
