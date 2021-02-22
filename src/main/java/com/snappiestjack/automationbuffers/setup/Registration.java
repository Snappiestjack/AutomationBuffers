package com.snappiestjack.automationbuffers.setup;

import com.snappiestjack.automationbuffers.blocks.multibuffer.MultiBufferBlock;
import com.snappiestjack.automationbuffers.blocks.multibuffer.MultiBufferContainer;
import com.snappiestjack.automationbuffers.blocks.multibuffer.MultiBufferTile;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.snappiestjack.automationbuffers.AutomationBuffers.MODID;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<MultiBufferBlock> MULTIBUFFER = BLOCKS.register("multibuffer", MultiBufferBlock::new);
    public static final RegistryObject<Item> MULTIBUFFER_ITEM = ITEMS.register("multibuffer", () -> new BlockItem(MULTIBUFFER.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<MultiBufferTile>> MULTIBUFFER_TILE = TILES.register("multibuffer", () -> TileEntityType.Builder.create(MultiBufferTile::new, MULTIBUFFER.get()).build(null));
    public static final RegistryObject<ContainerType<MultiBufferContainer>> MULTIBUFFER_CONTAINER = CONTAINERS.register("multibuffer", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new MultiBufferContainer(windowId, world, pos, inv, inv.player);
    }));



}
