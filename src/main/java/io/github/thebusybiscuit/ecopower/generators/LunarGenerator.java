package io.github.thebusybiscuit.ecopower.generators;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;

/**
 * The {@link LunarGenerator} is a {@link SolarGenerator} which <strong>only works at night!</strong>.
 * 
 * @author TheBusyBiscuit
 *
 */
public class LunarGenerator extends SolarGenerator {

    public LunarGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int energy) {
        super(itemGroup, 0, energy, item, recipeType, recipe);
    }

}
