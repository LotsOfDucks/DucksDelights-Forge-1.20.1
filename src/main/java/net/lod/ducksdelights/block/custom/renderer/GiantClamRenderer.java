package net.lod.ducksdelights.block.custom.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lod.ducksdelights.block.custom.GiantClamBlock;
import net.lod.ducksdelights.block.custom.blockstate_properties.ModBlockStateProperties;
import net.lod.ducksdelights.block.custom.blockstate_properties.enums.ClamTexture;
import net.lod.ducksdelights.block.entity.GiantClamBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GiantClamRenderer implements BlockEntityRenderer<GiantClamBlockEntity> {
    private final ItemRenderer itemRenderer;


    public GiantClamRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    public void render(GiantClamBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity.getBlockState().getValue(ModBlockStateProperties.OPEN)) {
            NonNullList<ItemStack> itemList = pBlockEntity.getItems();
            int blockPos = (int) pBlockEntity.getBlockPos().asLong();

            for (int slot = 0; slot < itemList.size(); ++slot) {
                ItemStack itemStack = itemList.get(slot);
                if (itemStack != ItemStack.EMPTY) {
                    pPoseStack.pushPose();

                    if (pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.EAST || pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.WEST) {
                        pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
                        pPoseStack.translate(-0.5F, 0.6F, 0.5F);
                    } else {
                        pPoseStack.translate(0.5F, 0.6F, 0.5F);
                    }
                    pPoseStack.scale(0.375F, 0.375F, 0.375F);
                    this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBuffer, pBlockEntity.getLevel(), blockPos + slot);
                    pPoseStack.popPose();
                }
            }
        }
    }
}
