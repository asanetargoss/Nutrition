package ca.wescook.nutrition.events;

import ca.wescook.nutrition.capabilities.CapInterface;
import ca.wescook.nutrition.capabilities.CapProvider;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class EventPlayerLoggedIn {
	@SubscribeEvent
	public void PlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		System.out.println("Hmm");
		CapInterface capability = event.player.getCapability(CapProvider.NUTRITION_CAPABILITY, null);
		if (capability == null) {
			return;
		}
		capability.resync();
	}
}
