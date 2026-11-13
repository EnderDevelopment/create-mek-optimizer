package com.enderdevelopment.factoryflow;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;

@Mod("factoryflow")
public class FactoryFlowMod {
    private static final int DEFAULT_RADIUS = 32;
    private static final int MAX_RADIUS = 128;

    public FactoryFlowMod() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("factoryflow")
            .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
            .then(Commands.literal("status").executes(context -> reportStatus(context.getSource().getPlayerOrException())))
            .then(Commands.literal("items").executes(context -> reportItems(context.getSource().getPlayerOrException(), DEFAULT_RADIUS))
                .then(Commands.argument("radius", IntegerArgumentType.integer(1, MAX_RADIUS))
                    .executes(context -> reportItems(context.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(context, "radius")))))
            .then(Commands.literal("clearitems")
                .executes(context -> clearItems(context.getSource().getPlayerOrException(), DEFAULT_RADIUS))
                .then(Commands.argument("radius", IntegerArgumentType.integer(1, MAX_RADIUS))
                    .executes(context -> clearItems(context.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(context, "radius"))))));
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
            player.sendSystemMessage(Component.literal("FactoryFlow ready: /factoryflow status, items, clearitems"));
        }
    }

    private int reportStatus(ServerPlayer player) {
        int dimensions = 0;
        int loadedItems = 0;
        for (ServerLevel level : player.level().getServer().getAllLevels()) {
            dimensions++;
            loadedItems += level.getEntities(EntityType.ITEM, item -> true).size();
        }
        player.sendSystemMessage(Component.literal("FactoryFlow: " + dimensions + " loaded dimensions, " + loadedItems + " dropped items"));
        player.sendSystemMessage(Component.literal("Automation tip: keep item outputs covered and use /factoryflow items near a busy line."));
        return Command.SINGLE_SUCCESS;
    }

    private int reportItems(ServerPlayer player, int radius) {
        List<? extends ItemEntity> items = nearbyItems(player, radius);
        int stacks = items.size();
        int count = items.stream().mapToInt(item -> item.getItem().getCount()).sum();
        player.sendSystemMessage(Component.literal("FactoryFlow: " + stacks + " item entities, " + count + " items within " + radius + " blocks"));
        return stacks;
    }

    private int clearItems(ServerPlayer player, int radius) {
        List<? extends ItemEntity> items = nearbyItems(player, radius);
        for (ItemEntity item : items) {
            item.discard();
        }
        player.sendSystemMessage(Component.literal("FactoryFlow: removed " + items.size() + " dropped item entities within " + radius + " blocks"));
        return items.size();
    }

    private List<? extends ItemEntity> nearbyItems(ServerPlayer player, int radius) {
        double range = radius;
        return player.level().getEntities(EntityType.ITEM,
            item -> item.distanceToSqr(player) <= range * range);
    }
}
