package com.snappiestjack.automationbuffers.blocks.multibuffer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.snappiestjack.automationbuffers.AutomationBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiBufferScreen extends ContainerScreen<MultiBufferContainer> {

    private ResourceLocation GUI = new ResourceLocation(AutomationBuffers.MODID, "textures/gui/multibuffer_gui.png");

    public MultiBufferScreen(MultiBufferContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (isPointInRegion(98, 21, 16, 47, mouseX, mouseY)) {
            FluidStack fluid = container.getFluid();
            if(!fluid.isEmpty()) {

                String fluidAmount = String.valueOf(fluid.getAmount());
                List<ITextComponent> tooltip = Collections.singletonList(new TranslationTextComponent("tooltip.fluidmb", fluid.getDisplayName(), new StringTextComponent(fluidAmount)));
                func_243308_b(matrixStack, tooltip, mouseX, mouseY);
            }
        }
        super.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, "MultiBuffer", 8, 5, 0x404040);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, "Inventory", 8, 79, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {

        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize + 10);

        renderFluidTank(getFluid(),MultiBufferTile.TANK_CAPACITY,relX + 98, relY + 21, 0, 16, 47);
    }

    public static void renderFluidTank(FluidStack fluidStack, int tankCapacity, double x, double y, double zLevel, double width, double height) {
        int amount = fluidStack.getAmount();
        if (fluidStack.getFluid() == null || amount <= 0) {
            return;
        }

        ResourceLocation stillTexture = fluidStack.getFluid().getAttributes().getStillTexture(fluidStack);
        TextureAtlasSprite icon = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(stillTexture);

        int renderAmount = (int) Math.max(Math.min(height, amount * height / tankCapacity), 1);
        int posY = (int) (y + height - renderAmount);

        Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        int color = fluidStack.getFluid().getAttributes().getColor();
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        GlStateManager.color4f(r, g, b,1.0f);

        GlStateManager.enableBlend();
        for (int i = 0; i < width; i += 16) {
            for (int j = 0; j < renderAmount; j += 16) {
                int drawWidth = (int) Math.min(width - i, 16);
                int drawHeight = Math.min(renderAmount - j, 16);

                int drawX = (int) (x + i);
                int drawY = posY + j;

                float minU = icon.getMinU();
                float maxU = icon.getMaxU();
                float minV = icon.getMinV();
                float maxV = icon.getMaxV();

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder tes = tessellator.getBuffer();
                tes.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                tes.pos(drawX, drawY + drawHeight, 0).tex(minU, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.pos(drawX + drawWidth, drawY + drawHeight, 0).tex(minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.pos(drawX + drawWidth, drawY, 0).tex(minU + (maxU - minU) * drawWidth / 16F, minV).endVertex();
                tes.pos(drawX, drawY, 0).tex(minU, minV).endVertex();
                tessellator.draw();
            }
        }
        GlStateManager.disableBlend();
        GlStateManager.color4f(1, 1, 1,1.0f);
    }

    protected FluidStack getFluid() {
        return container.getFluid();
    }

}
