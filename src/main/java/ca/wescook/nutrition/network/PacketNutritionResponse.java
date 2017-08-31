package ca.wescook.nutrition.network;

import ca.wescook.nutrition.capabilities.CapInterface;
import ca.wescook.nutrition.capabilities.CapProvider;
import ca.wescook.nutrition.gui.ModGuiHandler;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.proxy.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashMap;
import java.util.Map;

public class PacketNutritionResponse {
	// Message Subclass
	public static class Message implements IMessage {
		// Server vars only
		EntityPlayer serverPlayer;

		// Client vars only
		Map<Nutrient, Float> clientNutrients;
		Map<Nutrient, Boolean> clientNutrientsEnabled;

		public Message() {}

		// Message data is passed along from server
		public Message(EntityPlayer player) {
			serverPlayer = player; // Get server player
		}

		// Then serialized into bytes (on server)
		@Override
		public void toBytes(ByteBuf buf) {
			// Loop through nutrients from server player, and add to buffer
		    CapInterface capability = serverPlayer.getCapability(CapProvider.NUTRITION_CAPABILITY, null);
			Map<Nutrient, Float> nutrientData = capability.get();
			Map<Nutrient, Boolean> nutrientDataEnabled = capability.getEnabled();
			for (Map.Entry<Nutrient, Float> entry : nutrientData.entrySet()) {
				ByteBufUtils.writeUTF8String(buf, entry.getKey().name); // Write name as identifier
				buf.writeFloat(entry.getValue()); // Write float as value
				buf.writeBoolean(nutrientDataEnabled.get(entry.getKey())); // Write boolean as value
			}
		}

		// Then deserialized (on the client)
		@Override
		public void fromBytes(ByteBuf buf) {
			// Loop through buffer stream to build nutrition data
			clientNutrients = new HashMap<>();
			clientNutrientsEnabled = new HashMap<>();
			while(buf.isReadable()) {
				String identifier = ByteBufUtils.readUTF8String(buf);
				Float value = buf.readFloat();
				Boolean enabled = buf.readBoolean();
				clientNutrients.put(NutrientList.getByName(identifier), value);
				clientNutrientsEnabled.put(NutrientList.getByName(identifier), enabled);
			}
		}
	}

	// Message Handler Subclass
	// This is the client response to the information
	public static class Handler implements IMessageHandler<Message, IMessage> {
		@Override
		public IMessage onMessage(final Message message, final MessageContext context) {
			FMLCommonHandler.instance().getWorldThread(context.netHandler).addScheduledTask(() -> {
				// Update local dummy nutrition data
				ClientProxy.nutrientData = message.clientNutrients;
				ClientProxy.nutrientDataEnabled = message.clientNutrientsEnabled;

				// If GUI is still open, update GUI
				GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
				if (currentScreen != null && currentScreen.equals(ModGuiHandler.nutritionGui))
					ModGuiHandler.nutritionGui.redrawLabels();
			});

			return null;
		}
	}
}
