package io.github.thebusybiscuit.ecopower.generators;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;

/**
 * The {@link HighEnergySolarGenerator} is simply a {@link SolarGenerator} which generates
 * a ton of energy.
 * 
 * @author TheBusyBiscuit
 *
 */
public class HighEnergySolarGenerator extends SolarGenerator {

    public HighEnergySolarGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int energy) {
        super(itemGroup, energy, energy, item, recipeType, recipe);
    }

}
