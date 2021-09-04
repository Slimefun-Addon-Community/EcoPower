package io.github.thebusybiscuit.ecopower.generators;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
 * The {@link SteamTurbine} generates energy when placed above bubbling water.
 * 
 * @author TheBusyBiscuit
 * 
 * @see WindTurbine
 *
 */
public class SteamTurbine extends SlimefunItem implements EnergyNetProvider {

    private final Set<Location> validTurbines = ConcurrentHashMap.newKeySet();
    private final int generatedPower;

    public SteamTurbine(ItemGroup itemGroup, SlimefunItemStack item, int generatedPower, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        this.generatedPower = generatedPower;
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.GENERATOR;
    }

    @Override
    public int getGeneratedOutput(Location l, Config config) {
        int power = validTurbines.remove(l) ? generatedPower : 0;

        JavaPlugin plugin = getAddon().getJavaPlugin();
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (validateLocation(l)) {
                // Mark the turbine as valid (if valid)
                validTurbines.add(l);
            }
        });

        return power;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    private boolean validateLocation(Location l) {
        Block water = l.getBlock().getRelative(BlockFace.DOWN);

        // A Bubble Column is water above a Magma Block
        if (water.getType() != Material.BUBBLE_COLUMN) {
            return false;
        }

        water.setType(Material.AIR);
        l.getWorld().playSound(l, Sound.BLOCK_FIRE_EXTINGUISH, 0.05F, 1);
        l.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, l.getX() + 0.5, l.getY(), l.getZ() + 0.5, 1, 0, 0.4, 0, 0.01);
        return true;
    }

}
