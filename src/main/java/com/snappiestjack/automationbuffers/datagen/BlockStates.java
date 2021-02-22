package com.snappiestjack.automationbuffers.datagen;

import com.snappiestjack.automationbuffers.AutomationBuffers;
import com.snappiestjack.automationbuffers.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Function;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, AutomationBuffers.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerFirstBlock();
    }

    private void registerFirstBlock() {
        ResourceLocation txt = new ResourceLocation(AutomationBuffers.MODID, "block/multibuffer");
        BlockModelBuilder modelFirstblock = models().cube("multibuffer", txt, txt, new ResourceLocation(AutomationBuffers.MODID, "block/multibuffer_front"), txt, txt, txt);
        orientedBlock(Registration.MULTIBUFFER.get(), state -> { return modelFirstblock; });
    }

    private void orientedBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.get(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir.getAxis() == Direction.Axis.Y ?  dir.getAxisDirection().getOffset() * -90 : 0)
                            .rotationY(dir.getAxis() != Direction.Axis.Y ? ((dir.getHorizontalIndex() + 2) % 4) * 90 : 0)
                            .build();
                });
    }

}