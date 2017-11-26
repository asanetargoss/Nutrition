package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.nutrients.Nutrient;

import java.util.List;
import java.util.Map;

// Capability Interface that describes what methods the Implementations should understand.
public interface CapInterface {
	// Return all nutrients
	Map<Nutrient, Float> get();
	
	/* Return enabled state for all nutrients (true by default). Only nutrients whose state is enabled can be modified.
	 * Unlike the "enabled" config option, which sets whether a nutrient is loaded at all, this enabled state
	 * determines whether or not an individual player relies on a nutrient. This allows for players to have
	 * different dietary roles. It is up to third-party plugins to disable nutrients.
	 */
    Map<Nutrient, Boolean> getEnabled();
    
    // Return all nutrient decay multipliers
    Map<Nutrient, Float> getDecay();

	// Return specific nutrient
	Float get(Nutrient nutrient);
	
	// Return specific nutrient enabled state
    Boolean getEnabled(Nutrient nutrient);
    
    // Return specific nutrient decay multiplier
    Float getDecay(Nutrient nutrient);
    
    // Get how many nutrients the player has total, regardless of their enabled state
    int getNutrientCount();
    
    // Get how many nutrients are in the enabled state for this player
    int getEnabledCount();

	// Overwrite all nutrients
	void set(Map<Nutrient, Float> nutrientData, boolean sync);

	// Overwrite specific nutrient
	void set(Nutrient nutrient, Float value, boolean sync);
	
	// Overwrite all nutrient enabled states
    void setEnabled(Map<Nutrient, Boolean> nutrientEnabledData, boolean sync);

    // Overwrite specific nutrient enabled state
    void setEnabled(Nutrient nutrient, Boolean enabled, boolean sync);
    
    // Overwrite all nutrient decay rate multipliers
    void setDecay(Map<Nutrient, Float> nutrientDecayData, boolean sync);

    // Overwrite specific nutrient enabled state
    void setDecay(Nutrient nutrient, Float nutrientDecay, boolean sync);

	// Increase nutrition of specific nutrient
	void add(Nutrient nutrient, float amount, boolean sync);

	// Increase nutrition of list of nutrients
	void add(List<Nutrient> nutrientData, float amount, boolean sync);

	// Decrease nutrition of specific nutrient
	void subtract(Nutrient nutrient, float amount, boolean sync);

	// Decrease nutrition of list of nutrients
	void subtract(List<Nutrient> nutrientData, float amount, boolean sync);

	// Penalize all skills on death
	void deathPenalty();

	// Sync nutrition data to local dummy
	void resync();
}
