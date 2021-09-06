package io.github.thebusybiscuit.ecopower.generators;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Tag;
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
 * The {@link WindTurbine} generates energy when placed in the air and on top of fences.
 * 
 * @author poma123
 * 
 * @see SteamTurbine
 *
 */
public class WindTurbine extends SlimefunItem implements EnergyNetProvider {

    private static final BlockFace[] airFaces = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    private final Set<Location> validTurbines = ConcurrentHashMap.newKeySet();
    private final int generatedPower;

    public WindTurbine(ItemGroup itemGroup, SlimefunItemStack item, int generatedPower, RecipeType recipeType, ItemStack[] recipe) {
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
        Block b = l.getBlock();
        Block fence1 = b.getRelative(BlockFace.DOWN);
        Block fence2 = b.getRelative(BlockFace.DOWN, 2);

        if (!Tag.WOODEN_FENCES.isTagged(fence1.getType()) || !Tag.WOODEN_FENCES.isTagged(fence2.getType())) {
            return false;
        }

        for (BlockFace face : airFaces) {
            if (!b.getRelative(face).isEmpty()) {
                return false;
            }
        }

        l.getWorld().spawnParticle(Particle.SPELL, l.getX() + 0.5, l.getY(), l.getZ() + 0.5, 4, 0, 0.4, 0, 0.01);
        return true;
    }
}
