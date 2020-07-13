package io.github.thebusybiscuit.ecopower.generators;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class LunarGenerator extends SolarGenerator {

    private final int energyGenerated;

    public LunarGenerator(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int energy) {
        super(category, item, recipeType, recipe);

        this.energyGenerated = energy;
    }

    @Override
    public double getDayEnergy() {
        return 0;
    }

    @Override
    public double getNightEnergy() {
        return energyGenerated;
    }

}
