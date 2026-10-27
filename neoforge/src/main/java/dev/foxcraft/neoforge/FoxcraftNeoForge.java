package dev.foxcraft.neoforge;

import dev.foxcraft.Foxcraft;
import dev.foxcraft.content.BlockDefinition;
import dev.foxcraft.content.ContentCatalog;
import dev.foxcraft.platform.MinecraftContent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

/** NeoForge lifecycle only. Both loaders consume exactly the same catalog. */
@Mod(Foxcraft.MOD_ID)
public final class FoxcraftNeoForge {
    private final DeferredRegister<Block> blocks = DeferredRegister.create(Registries.BLOCK, Foxcraft.MOD_ID);
    private final DeferredRegister<Item> items = DeferredRegister.create(Registries.ITEM, Foxcraft.MOD_ID);
    private final Map<BlockDefinition, Supplier<Item>> blockItems = new LinkedHashMap<>();

    public FoxcraftNeoForge(IEventBus modBus) {
        for (BlockDefinition definition : ContentCatalog.BLOCKS) {
            Supplier<Block> block = blocks.register(definition.id(), () -> MinecraftContent.createBlock(definition));
            Supplier<Item> item = items.register(definition.id(),
                    () -> new BlockItem(block.get(), new Item.Properties()));
            blockItems.put(definition, item);
        }
        blocks.register(modBus);
        items.register(modBus);
        modBus.addListener(this::fillCreativeTabs);
    }

    private void fillCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        blockItems.forEach((definition, item) -> {
            if (event.getTabKey().equals(MinecraftContent.creativeTab(definition))) {
                event.accept(item.get());
            }
        });
    }
}
