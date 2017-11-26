package ca.wescook.nutrition.effects;

import ca.wescook.nutrition.capabilities.CapInterface;
import ca.wescook.nutrition.capabilities.CapProvider;
import ca.wescook.nutrition.nutrients.Nutrient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EffectsManager {
	// Called from EventPlayerUpdate.
	public static void reapplyEffects(EntityPlayer player) {
		List<Effect> effects = removeDuplicates(getEffectsInThreshold(player));

		for (Effect effect : effects) {
			player.addPotionEffect(new PotionEffect(effect.potion, 615, effect.amplifier, true, false));
		}
	}

	// Returns which effects match threshold conditions
	private static List<Effect> getEffectsInThreshold(EntityPlayer player) {
		// Get info
		CapInterface capability = player.getCapability(CapProvider.NUTRITION_CAPABILITY, null);
		Map<Nutrient, Float> playerNutrition = capability.get();
		Map<Nutrient, Boolean> playerNutritionEnabled = capability.getEnabled();

		// Effects being turned on
		List<Effect> effectsInThreshold = new ArrayList<>();

		// Read in list of potion effects to apply
		for (Effect effect : EffectsList.get()) {

			// Apply effect based on "detect" condition
			switch (effect.detect) {
				// If any nutrient is within the threshold
				case "any": {
					// Loop relevant nutrients
					for (Nutrient nutrient : effect.nutrients) {
						// If any are found within threshold
						if (playerNutritionEnabled.get(nutrient)
								&& playerNutrition.get(nutrient) >= effect.minimum &&
								playerNutrition.get(nutrient) <= effect.maximum) {
							effectsInThreshold.add(effect); // Add effect, once
							break;
						}
					}
				}
				break;

				// If the average of all nutrients is within the threshold
				case "average": {
					// Reset counter each new loop
					Float total = 0f;
					int size = 0;
					Float average;

					// Loop relevant nutrients
					for (Nutrient nutrient : effect.nutrients) {
						if (playerNutritionEnabled.get(nutrient)) {
							total += playerNutrition.get(nutrient); // Add each value to total
							size++;
						}
					}

					// Divide by number of nutrients for average (division by zero check)
					average = (size != 0) ? total / size : -1f;

					// Check average is inside the threshold
					if (average >= effect.minimum && average <= effect.maximum)
						effectsInThreshold.add(effect);
				}
				break;

				// If all nutrients are within the threshold
				case "all": {
					// Condition starts true, and must be triggered to fail
					boolean allWithinThreshold = true;

					// Loop relevant nutrients
					for (Nutrient nutrient : effect.nutrients) {
						if (!playerNutritionEnabled.get(nutrient) ||
								!(playerNutrition.get(nutrient) >= effect.minimum &&
								playerNutrition.get(nutrient) <= effect.maximum)) // If nutrient isn't within threshold
							allWithinThreshold = false; // Fail check
					}

					// If check wasn't failed, set effect
					if (allWithinThreshold)
						effectsInThreshold.add(effect);
				}
				break;

				// For each nutrient within the threshold, the amplifier increases by one
				case "cumulative":  {
					// Reset counter each new loop
					int cumulativeCount = 0;

					// Loop relevant nutrients
					for (Nutrient nutrient : effect.nutrients) {
						// For each nutrient found within threshold
						if (playerNutritionEnabled.get(nutrient) &&
								playerNutrition.get(nutrient) >= effect.minimum &&
								playerNutrition.get(nutrient) <= effect.maximum)
							cumulativeCount++;
					}

					// Save number of nutrients found as amplifier
					// We're saving this for the entire effect, which is crazy hacky.
					// However it's otherwise unused, and the simplest way of storing this information.
					effect.amplifier = (cumulativeCount * effect.cumulativeModifier) - 1;

					// If any were found, set effect
					if (cumulativeCount > 0) {
						effectsInThreshold.add(effect); // Add effect, once
					}
				}
				break;
			}
		}

		return effectsInThreshold;
	}

	// Determines highest amplifier for duplicates, and removes them
	private static List<Effect> removeDuplicates(List<Effect> effectsInput) {
		List<Effect> effectsOutput = new ArrayList<>();
		boolean foundMatch = false;
		for (Effect effectIn : effectsInput) { // Loop through supplied effects
			for (Effect effectOut : effectsOutput) { // Loop through curated list, if it exists
				if (effectIn.potion == effectOut.potion) { // Potion types match (eg. Weakness I and Weakness II)
					if (effectIn.amplifier > effectOut.amplifier) { // New effect has a higher amplifier
						int listIndex = effectsOutput.indexOf(effectOut); // Get index of position in list
						effectsOutput.add(listIndex, effectIn); // Replace entry
					}
					foundMatch = true;
					break;
				}
			}

			// If potion wasn't already found, add to list
			if (!foundMatch)
				effectsOutput.add(effectIn);
		}

		return effectsOutput;
	}
}
