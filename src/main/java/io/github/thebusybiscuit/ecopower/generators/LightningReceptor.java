package io.github.thebusybiscuit.ecopower.generators;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.GeneratorTicker;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class LightningReceptor extends SlimefunItem implements EnergyNetProvider {

    private static final long MIN_DELAY = TimeUnit.MINUTES.toMillis(8);
    private static final int CHANCE = 8;

    private final Map<Location, Long> lastLightningStrike = new HashMap<>();
    private final int minPower;
    private final int maxPower;

    public LightningReceptor(Category category, SlimefunItemStack item, int min, int max, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        this.minPower = min;
        this.maxPower = max;
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.GENERATOR;
    }

    @Override
    public int getGeneratedOutput(Location l, Config config) {
        if (!l.getWorld().isThundering()) {
            return 0;
        }

        Long previousLightningStrike = lastLightningStrike.get(l);

        if (previousLightningStrike != null && System.currentTimeMillis() - previousLightningStrike.longValue() < MIN_DELAY) {
            return 0;
        }

        if (ThreadLocalRandom.current().nextInt(100) < CHANCE) {
            lastLightningStrike.put(l, System.currentTimeMillis());
            Slimefun.runSync(() -> l.getWorld().strikeLightningEffect(l));
            return ThreadLocalRandom.current().nextInt(minPower, maxPower);
        }

        return 0;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

}
