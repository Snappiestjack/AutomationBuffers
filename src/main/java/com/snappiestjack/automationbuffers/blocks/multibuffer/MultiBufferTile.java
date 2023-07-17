package com.snappiestjack.automationbuffers.blocks.multibuffer;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.snappiestjack.automationbuffers.setup.Registration.MULTIBUFFER_TILE;

public class MultiBufferTile extends TileEntity implements ITickableTileEntity {

    static final int TANK_CAPACITY = 20000;
    static final int NUMBER_OF_TANKS = 1; // Unused
    static final int NUMBER_OF_ITEM_SLOTS = 9;

    private ItemStackHandler itemHandler = createItemHandler();
    private FluidTank internalTank = createFluidTank();

    private LazyOptional<ItemStackHandler> itemCapabilityHandler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> internalTank);

    public MultiBufferTile() {
        super(MULTIBUFFER_TILE.get());
    }

    @Override
    public void tick() {
        if(world.isRemote) {
            return;
        }

        world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
        markDirty();
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(NUMBER_OF_ITEM_SLOTS) {

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }

        };
    }

    private FluidTank createFluidTank() {
        return new FluidTank(TANK_CAPACITY){
            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                markDirty();
            }

            @Override
            public void setFluid(FluidStack stack) {
                super.setFluid(stack);
                this.onContentsChanged();
            }
        };
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        internalTank.readFromNBT(tag.getCompound("tank"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.put("inv", itemHandler.serializeNBT());
        tag.put("tank", internalTank.writeToNBT(new CompoundNBT()));
        return tag;
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        int tileEntityType = 42;
        return new SUpdateTileEntityPacket(this.pos, tileEntityType, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState blockState = world.getBlockState(pos);
        read(blockState, pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT parentNBTTagCompound) {
        this.read(blockState, parentNBTTagCompound);
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemCapabilityHandler.cast();
        }
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return fluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public FluidStack getFluid() {
        return internalTank.getFluid();
    }
}