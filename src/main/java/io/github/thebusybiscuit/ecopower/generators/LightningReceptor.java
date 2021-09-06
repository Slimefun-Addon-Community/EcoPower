package io.github.thebusybiscuit.ecopower.generators;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LightningStrike;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;

/**
 * The {@link LightningReceptor} attracts a {@link LightningStrike} during thunderstorms
 * and harnesses it' electrical energy.
 * 
 * @author TheBusyBiscuit
 *
 */
public class LightningReceptor extends SlimefunItem implements EnergyNetProvider {

    private static final long MIN_DELAY = TimeUnit.MINUTES.toMillis(8);
    private static final int CHANCE = 8;

    private final Map<Location, Long> lastLightningStrike = new HashMap<>();
    private final int minPower;
    private final int maxPower;

    public LightningReceptor(ItemGroup itemGroup, SlimefunItemStack item, int min, int max, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

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
            JavaPlugin plugin = getAddon().getJavaPlugin();
            Bukkit.getScheduler().runTask(plugin, () -> l.getWorld().strikeLightningEffect(l));
            return ThreadLocalRandom.current().nextInt(minPower, maxPower);
        }

        return 0;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

}
