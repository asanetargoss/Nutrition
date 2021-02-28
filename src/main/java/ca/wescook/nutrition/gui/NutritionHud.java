package ca.wescook.nutrition.gui;

import java.util.ArrayList;
import java.util.List;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;

public class NutritionHud {
	protected static final int ITEM_SIZE = 16;
	protected static final int TEXT_HEIGHT = 8;
	protected static final int HUNGER_BAR_HEIGHT = 10;
	protected static final int NUTRIENT_VISIBILITY_MAX_PERCENT = 70;
	protected static final int NUTRIENT_VISIBILITY_FULL_PERCENT = 60;
	protected static final int NUTRIENT_RED_MAX_PERCENT = 50;
	protected static final int NUTRIENT_RED_FULL_PERCENT = 10;
	
	public static void drawNuritionOverlay(ScaledResolution res, ResourceLocation textureToBindWhenDone) {
		if (ClientProxy.nutrientData == null || ClientProxy.nutrientDataEnabled == null) {
			return;
		}
		
		List<Nutrient> enabledNutrients = new ArrayList<Nutrient>(NutrientList.get().size());
		for (Nutrient nutrient : NutrientList.get()) {
		    if (ClientProxy.nutrientDataEnabled != null && ClientProxy.nutrientDataEnabled.get(nutrient)) {
		    	enabledNutrients.add(nutrient);
		    }
		}
		int n = enabledNutrients.size();
		if (n == 0) {
			return;
		}
		
		int width = res.getScaledWidth();
		int height = res.getScaledHeight();
		int totalNutrientsHeight = n * ITEM_SIZE;
		int startHeight = height - totalNutrientsHeight - GuiIngameForge.right_height + HUNGER_BAR_HEIGHT;
		if (Minecraft.getMinecraft().gameSettings.showSubtitles) {
			startHeight = (height - totalNutrientsHeight) / 2;
		}
		for (int i = 0; i < n; i++) {
			Nutrient nutrient = enabledNutrients.get(i);
			drawNutrientOverlay(nutrient, width, startHeight + (ITEM_SIZE * i), textureToBindWhenDone);
		}
	}
	
	// x = right and y = down/top
	protected static void drawNutrientOverlay(Nutrient nutrient, int right, int top, ResourceLocation textureToBindWhenDone) {
		Float nutrientAmountFloat = ClientProxy.nutrientData.get(nutrient);
		if (nutrientAmountFloat == null) {
			return;
		}
		if (nutrientAmountFloat >= NUTRIENT_VISIBILITY_MAX_PERCENT) {
			return;
		}
		
		// Color with alpha
		int alpha = nutrientAmountFloat <= NUTRIENT_VISIBILITY_FULL_PERCENT ? 255 :
			(int)(255.0F * ((NUTRIENT_VISIBILITY_MAX_PERCENT - nutrientAmountFloat) / (NUTRIENT_VISIBILITY_MAX_PERCENT - NUTRIENT_VISIBILITY_FULL_PERCENT)));
		float redness = nutrientAmountFloat >= NUTRIENT_RED_MAX_PERCENT ? 0.0F :
			nutrientAmountFloat <= NUTRIENT_RED_FULL_PERCENT ? 1.0F :
			(((NUTRIENT_RED_MAX_PERCENT - NUTRIENT_RED_FULL_PERCENT) - (nutrientAmountFloat - NUTRIENT_RED_FULL_PERCENT)) / (NUTRIENT_RED_MAX_PERCENT - NUTRIENT_RED_FULL_PERCENT));
		int red = 255;
		int green = (int)(255.0F * (1.0F - redness));
		int blue = green;
		int nutrientPercentColor = alpha << 24 |
				red << 16 |
				blue << 8 |
				green;
		
		Minecraft mc = Minecraft.getMinecraft();
		
		int nutrientAmount = (int)Math.round(nutrientAmountFloat);
		String nutrientPercentString = Integer.toString(nutrientAmount) + "%";
		int iconWidth = ITEM_SIZE;
		FontRenderer fontRenderer = mc.fontRendererObj;
		int spaceBetweenWidth = fontRenderer.getCharWidth(' ');
		int textWidth = fontRenderer.getStringWidth(nutrientPercentString);
		
		int textStartX = right - textWidth - spaceBetweenWidth - iconWidth;
		int iconStartX = right - iconWidth;
		fontRenderer.drawStringWithShadow(nutrientPercentString, textStartX, top + ((ITEM_SIZE - TEXT_HEIGHT) / 2), nutrientPercentColor);
		
		RenderItem renderItem = mc.getRenderItem();
        IBakedModel bakedModel = renderItem.getItemModelWithOverrides(nutrient.icon, null, null);
		// Normally we would call renderItem.renderItemModelIntoGui(...)
		// However, we want the rendered item to have alpha
        renderItemModelIntoGuiWithAlpha(renderItem, nutrient.icon, iconStartX, top, bakedModel, alpha, textureToBindWhenDone);
	}
	
	protected static void renderItemModelIntoGuiWithAlpha(RenderItem renderItem, ItemStack itemStack, int x, int y, IBakedModel bakedModel, int alpha, ResourceLocation textureToBindWhenDone) {
		GlStateManager.pushMatrix();
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        renderItem.setupGuiTransform(x, y, bakedModel.isGui3d());
        
        bakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedModel, ItemCameraTransforms.TransformType.GUI, false);
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        if (bakedModel.isBuiltInRenderer())
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha / 255.0F);
            GlStateManager.enableRescaleNormal();
            TileEntityItemStackRenderer.instance.renderByItem(itemStack);
        }
        else
        {
        	renderItem.renderModel(bakedModel, alpha << 24 | 255 << 16 | 255 << 8 | 255, itemStack);
            if (itemStack.hasEffect())
            {
                renderItem.renderEffect(bakedModel);
            }
        }

        GlStateManager.popMatrix();
        
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        textureManager.bindTexture(textureToBindWhenDone);
	}
}
