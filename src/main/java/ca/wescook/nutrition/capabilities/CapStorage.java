package ca.wescook.nutrition.capabilities;

import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.HashMap;

// Saves and loads serialized data from disk
public class CapStorage implements Capability.IStorage<CapInterface> {
    public static final String MORE_DATA = "more_data";
    public static final String NUTRIENT_ENABLED = "nutrient_enabled";
    
	// Save serialized data to disk
	@Override
	public NBTBase writeNBT(Capability<CapInterface> capability, CapInterface instance, EnumFacing side) {
		NBTTagCompound playerData = new NBTTagCompound();
		NBTTagCompound morePlayerData = new NBTTagCompound();
        playerData.setTag(MORE_DATA, morePlayerData);
        NBTTagCompound nutrientEnabled = new NBTTagCompound();
        morePlayerData.setTag(NUTRIENT_ENABLED, nutrientEnabled);
        
		for (Nutrient nutrient : NutrientList.get()) {
			playerData.setFloat(nutrient.name, instance.get(nutrient));
			nutrientEnabled.setBoolean(nutrient.name, instance.getEnabled(nutrient));
		}
		
		return playerData;
	}

	// Load serialized data from disk
	@Override
	public void readNBT(Capability<CapInterface> capability, CapInterface instance, EnumFacing side, NBTBase nbt) {
	    // Read nutrient key-value pairs in the root nbt compound tag, and get the rest of the data in the sub-tag "more_data"
	    // It is reasonable to assume that a player will not use "more_data" as a nutrient type
		NBTTagCompound nbtCompound = ((NBTTagCompound) nbt);
		NBTTagCompound moreData = null;
		NBTTagCompound enableData = null;
		{
		    NBTBase nbtMoreData = nbtCompound.getTag(MORE_DATA);
		    if (nbtMoreData != null && nbtMoreData instanceof NBTTagCompound) {
		        moreData = (NBTTagCompound)nbtMoreData;
		        NBTBase nbtEnableData = moreData.getTag(NUTRIENT_ENABLED);
		        if (nbtEnableData != null && nbtEnableData instanceof NBTTagCompound) {
		            enableData = (NBTTagCompound)nbtEnableData;
		        }
		    }
		}
		
		// Read in nutrients from file
		HashMap<Nutrient, Float> clientNutrients = new HashMap<Nutrient, Float>();
		HashMap<Nutrient, Boolean> clientNutrientsEnabled = new HashMap<Nutrient, Boolean>();
        Float value;
        Boolean enabled;
		for (Nutrient nutrient : NutrientList.get()) { // For each nutrient
			if (nbtCompound.hasKey(nutrient.name)) { // If it's found in player file
				value = ((NBTTagCompound) nbt).getFloat(nutrient.name); // Read value in
			}
			else {
				value = (float) Config.startingNutrition; // Set to default
			}
			
			enabled = true; // Starting state
			if (enableData != null && enableData.hasKey(nutrient.name)) {
		        enabled = enableData.getBoolean(nutrient.name); // Read enabled state
			}
			
			// Add to map
			clientNutrients.put(nutrient, value);
			clientNutrientsEnabled.put(nutrient, enabled);
		}

		// Replace nutrient data with map
		// Note: Syncing throws network errors at this stage
		instance.set(clientNutrients, false);
		instance.setEnabled(clientNutrientsEnabled, false);
	}
}
