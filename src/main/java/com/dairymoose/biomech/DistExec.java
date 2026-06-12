package com.dairymoose.biomech;

import java.util.function.Supplier;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

/**
 * Minimal stand-in for Forge's removed {@code net.minecraftforge.fml.DistExecutor}.
 * NeoForge dropped DistExecutor in favour of plain {@link FMLEnvironment#dist} checks;
 * this preserves the original call shape ({@code runWhenOn(Dist, Supplier<Runnable>)})
 * so existing call sites migrate with a single import/name change.
 *
 * The supplied {@link Runnable} (often an anonymous class touching client-only types) is
 * only instantiated and run when the current physical side matches {@code dist}, so it is
 * safe to reference client classes inside it on the dedicated server.
 */
public final class DistExec {
	private DistExec() {
	}

	public static void runWhenOn(Dist dist, Supplier<Runnable> runnableSupplier) {
		if (FMLEnvironment.dist == dist) {
			runnableSupplier.get().run();
		}
	}
}
