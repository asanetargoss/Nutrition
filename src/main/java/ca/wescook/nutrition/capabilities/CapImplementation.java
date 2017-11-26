package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.network.ModPacketHandler;
import ca.wescook.nutrition.network.PacketNutritionResponse;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.utility.Config;
import com.google.common.primitives.Floats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Real implementation of Capability.  Contains logic for each method defined in the Interface.
public class CapImplementation implements CapInterface {
	// Map Nutrient type to value for that nutrient
	private Map<Nutrient, Float> playerNutrition = new HashMap<>();
	// Map Nutrient type to whether that nutrient is enabled for that player (true by default)
	private Map<Nutrient, Boolean> playerNutritionEnabled = new HashMap<>();
	// Nutrient decay rate multiplier per player (1.0 by default)
    private Map<Nutrient, Float> playerNutritionDecay = new HashMap<>();
	private EntityPlayer player;

	CapImplementation(EntityPlayer player) {
		// Store player
		this.player = player;

		// Populate nutrient data with starting nutrition
		for (Nutrient nutrient : NutrientList.get()) {
			playerNutrition.put(nutrient, (float) Config.startingNutrition);
		    playerNutritionEnabled.put(nutrient, true);
		    playerNutritionDecay.put(nutrient, 1.0F);
		}
	}

	public Map<Nutrient, Float> get() {
		return playerNutrition;
	}

	public Float get(Nutrient nutrient) {
		return playerNutrition.get(nutrient);
	}
	
	public Map<Nutrient, Boolean> getEnabled() {
        return playerNutritionEnabled;
    }
	
	public Boolean getEnabled(Nutrient nutrient) {
        return playerNutritionEnabled.get(nutrient);
    }
	
	public Map<Nutrient, Float> getDecay() {
        return playerNutritionDecay;
    }
    
    public Float getDecay(Nutrient nutrient) {
        return playerNutritionDecay.get(nutrient);
    }
	
	public int getNutrientCount() {
        return playerNutrition.size();
    }
	
	public int getEnabledCount() {
        int numEnabled = 0;
        for (Boolean enabled : playerNutritionEnabled.values()) {
            if (enabled) {
                numEnabled++;
            }
        }
        return numEnabled;
    }

	public void set(Nutrient nutrient, Float value, boolean sync) {
	    if (playerNutritionEnabled.get(nutrient)) {
    		playerNutrition.put(nutrient, value);
    		if (sync) resync();
	    }
	}

	public void set(Map<Nutrient, Float> nutrientData, boolean sync) {
		for (Map.Entry<Nutrient, Float> entry : nutrientData.entrySet()) {
		    if (playerNutritionEnabled.get(entry.getKey())) {
		        this.playerNutrition.put(entry.getKey(), entry.getValue());
		    }
		}
		if (sync) resync();
	}
	
	public void setEnabled(Nutrient nutrient, Boolean enabled, boolean sync) {
        playerNutritionEnabled.put(nutrient, enabled);
        if (sync) resync();
    }

    public void setEnabled(Map<Nutrient, Boolean> nutrientEnabledData, boolean sync) {
        for (Map.Entry<Nutrient, Boolean> entry : nutrientEnabledData.entrySet())
            this.playerNutritionEnabled.put(entry.getKey(), entry.getValue());
        if (sync) resync();
    }
    
    public void setDecay(Nutrient nutrient, Float nutrientDecay, boolean sync) {
        playerNutritionDecay.put(nutrient, nutrientDecay);
        if (sync) resync();
    }

    public void setDecay(Map<Nutrient, Float> nutrientDecayData, boolean sync) {
        for (Map.Entry<Nutrient, Float> entry : nutrientDecayData.entrySet())
            this.playerNutritionDecay.put(entry.getKey(), entry.getValue());
        if (sync) resync();
    }

	public void add(Nutrient nutrient, float amount, boolean sync) {
	    if (playerNutritionEnabled.get(nutrient)) {
    		float currentAmount = playerNutrition.get(nutrient);
    		playerNutrition.put(nutrient, Floats.constrainToRange(currentAmount + amount, 0, 100));
    		if (sync) resync();
	    }
	}

	public void add(List<Nutrient> nutrientData, float amount, boolean sync) {
		for (Nutrient nutrient : nutrientData) {
		    if (playerNutritionEnabled.get(nutrient)) {
		    	float currentAmount = playerNutrition.get(nutrient);
		        playerNutrition.put(nutrient, Floats.constrainToRange(currentAmount + amount, 0, 100));
		    }
		}
		if (sync) resync();
	}

	public void subtract(Nutrient nutrient, float amount, boolean sync) {
	    if (playerNutritionEnabled.get(nutrient)) {
    		float currentAmount = playerNutrition.get(nutrient);
    		playerNutrition.put(nutrient, Floats.constrainToRange(currentAmount - amount, 0, 100));
    		if (sync) resync();
	    }
	}

	public void subtract(List<Nutrient> nutrientData, float amount, boolean sync) {
		for (Nutrient nutrient : nutrientData) {
		    if (playerNutritionEnabled.get(nutrient)) {
		    	float currentAmount = playerNutrition.get(nutrient);
		        playerNutrition.put(nutrient, Floats.constrainToRange(currentAmount - amount, 0, 100));
		    }
		}
		if (sync) resync();
	}

	public void deathPenalty() {
		for (Nutrient nutrient : playerNutrition.keySet()) // Loop through player's nutrients
			if (Config.deathPenaltyReset || get(nutrient) > Config.deathPenaltyMin) // If reset is disabled, only reduce to cap when above its value
				set(nutrient, Math.max(Config.deathPenaltyMin, playerNutrition.get(nutrient) - Config.deathPenaltyLoss), false); // Subtract death penalty from each nutrient, to cap
		resync();
	}

	public void resync() {
		if (!player.world.isRemote)
			ModPacketHandler.NETWORK_CHANNEL.sendTo(new PacketNutritionResponse.Message(player), (EntityPlayerMP) player);
	}
}
