package com.snappiestjack.automationbuffers.blocks.multibuffer;

import com.snappiestjack.automationbuffers.setup.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class MultiBufferContainer extends Container {

    private TileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;

    private final int topleftinvslotx = 8;
    private final int topleftinvsloty = 91;
    private final int invslotw = 18; // Width of inventory slot

    public MultiBufferContainer(int windowId, World world, BlockPos blockPos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(Registration.MULTIBUFFER_CONTAINER.get(), windowId);
        tileEntity = world.getTileEntity(blockPos);
        this.playerEntity = playerEntity;
        this.playerInventory = new InvWrapper(playerInventory);

        addOwnSlots();
        addPlayerSlots();
    }

    private void addOwnSlots() {
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            int x = topleftinvslotx;
            int y = topleftinvsloty - 90;
            // Slots for the MultiBuffer
            for (int slotIndex = 0; slotIndex < handler.getSlots(); slotIndex++) {
                if (slotIndex % 3 == 0) { // When starting a new row (including the first)
                    x = topleftinvslotx;
                    y += invslotw;
                }
                x += invslotw;
                addSlot(new SlotItemHandler(handler, slotIndex, x, y));
            }
        });
    }

    private void addPlayerSlots() {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = topleftinvslotx + col * 18;
                int y = topleftinvsloty + row * 18;
                this.addSlot(new SlotItemHandler(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = topleftinvslotx + row * 18;
            int y = topleftinvsloty + 58 ;
            this.addSlot(new SlotItemHandler(playerInventory, row, x, y));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerInv) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerInv, Registration.MULTIBUFFER.get());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();
            if (index < 3) {
                if (!this.mergeItemStack(stack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemstack);
            } else {
                if (!this.mergeItemStack(stack, 0, 3, false)) {
                    return ItemStack.EMPTY;
                } else if (index < 30) {
                    if (!this.mergeItemStack(stack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 39 && !this.mergeItemStack(stack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }

    public FluidStack getFluid() {
        return ((MultiBufferTile)tileEntity).getFluid();
    }

}
