package com.dairymoose.biomech;

import com.dairymoose.biomech.packet.clientbound.ClientboundEnergySyncPacket;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class BioMechCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("biomech").
                        then(BioMechCmd.register())
        );
    }

    public static class BioMechCmd {
        @SuppressWarnings({ "static-access", "unchecked" })
		static ArgumentBuilder<CommandSourceStack, ?> register() {
            return Commands.literal("energy").
                    requires(cs->cs.hasPermission(0)).
                    then(Commands.argument("value", IntegerArgumentType.integer(0)).
                            executes(BioMechCmd::bioMechSetEnergy)
                        );
        }

        private static int bioMechSetEnergy(final CommandContext<CommandSourceStack> context) {
            final Integer energyValue = context.getArgument("value", Integer.class);
            BioMechPlayerData playerData = BioMech.globalPlayerData.get(context.getSource().getEntity().getUUID());
            if (playerData != null) {
            	playerData.setSuitEnergy(energyValue);
            	if (context.getSource().getEntity() instanceof ServerPlayer sp) {
            		BioMechNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sp), new ClientboundEnergySyncPacket(playerData.getSuitEnergy(), playerData.suitEnergyMax, playerData.remainingTicksForEnergyRegen(sp)));
            	}
            } else {
                context.getSource().sendSuccess(() -> Component.literal("Could not set energy for player"), true);
            }
            return 0;
        }
        
        private static int bioMechSetMaxEnergy(final CommandContext<CommandSourceStack> context) {
            final Integer energyValue = context.getArgument("value", Integer.class);
            BioMechPlayerData playerData = BioMech.globalPlayerData.get(context.getSource().getEntity().getUUID());
            if (playerData != null) {
            	playerData.suitEnergyMax = energyValue;
            } else {
                context.getSource().sendSuccess(() -> Component.literal("Could not set max energy for player"), true);
            }
            return 0;
        }
    }
}
