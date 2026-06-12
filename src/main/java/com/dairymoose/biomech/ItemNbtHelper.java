package com.dairymoose.biomech;

import java.util.function.Consumer;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/**
 * Compatibility helper bridging the old pre-1.20.5 item-NBT API (getOrCreateTag / getTag)
 * onto the 1.20.5+ Data Components system. BioMech stores its ad-hoc per-stack flags inside
 * the vanilla {@link DataComponents#CUSTOM_DATA} component (a CompoundTag).
 *
 * Because data components are immutable, mutations must be read-modify-write. Use
 * {@link #update(ItemStack, Consumer)} when changing values; use {@link #getTag(ItemStack)}
 * (a defensive copy) for reads.
 */
public final class ItemNbtHelper {
	private ItemNbtHelper() {
	}

	/** Returns a mutable copy of the stack's custom-data tag (never null). */
	public static CompoundTag getTag(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
	}

	/**
	 * Returns a mutable copy of the stack's custom-data tag (never null). Because data components are
	 * immutable, the returned tag is a COPY: after mutating it you must call
	 * {@link #setTag(ItemStack, CompoundTag)} to persist the changes back onto the stack.
	 */
	public static CompoundTag getOrCreateTag(ItemStack stack) {
		return getTag(stack);
	}

	/** Returns a mutable copy of the stack's custom-data tag, or null if none is present (mirrors old ItemStack.getTag()). */
	public static CompoundTag getTagOrNull(ItemStack stack) {
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		return data == null ? null : data.copyTag();
	}

	/** Returns true when the stack carries any custom-data tag. */
	public static boolean hasTag(ItemStack stack) {
		return stack.has(DataComponents.CUSTOM_DATA);
	}

	/** Writes the given tag onto the stack, removing the component entirely when empty. */
	public static void setTag(ItemStack stack, CompoundTag tag) {
		if (tag == null || tag.isEmpty()) {
			stack.remove(DataComponents.CUSTOM_DATA);
		} else {
			stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
		}
	}

	/** Read-modify-write helper replacing {@code stack.getOrCreateTag().putX(...)} call chains. */
	public static void update(ItemStack stack, Consumer<CompoundTag> updater) {
		CompoundTag tag = getTag(stack);
		updater.accept(tag);
		setTag(stack, tag);
	}
}
