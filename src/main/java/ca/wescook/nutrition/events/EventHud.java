package ca.wescook.nutrition.events;

import ca.wescook.nutrition.gui.NutritionHud;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHud {
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onDrawHungerHud(RenderGameOverlayEvent.Pre event) {
		if (event.getType() != ElementType.FOOD) {
			return;
		}
		
		NutritionHud.drawNuritionOverlay(event.getResolution(), Gui.ICONS);
	}
}
