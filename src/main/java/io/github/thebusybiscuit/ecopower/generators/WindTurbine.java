package io.github.thebusybiscuit.ecopower.generators;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.blocks.Vein;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WindTurbine extends SlimefunItem implements EnergyNetProvider {

    private static final BlockFace[] airFaces = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    private final Set<Location> validTurbines = ConcurrentHashMap.newKeySet();
    private final int generatedPower;

    public WindTurbine(Category category, SlimefunItemStack item, int generatedPower, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        this.generatedPower = generatedPower;
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.GENERATOR;
    }

    @Override
    public int getGeneratedOutput(Location l, Config config) {
        int power = validTurbines.remove(l) ? generatedPower : 0;

        Slimefun.runSync(() -> {
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

        for (BlockFace face : airFaces) {
            if (b.getRelative(face).getType() != Material.AIR) {
                return false;
            }
        }

        l.getWorld().spawnParticle(Particle.SPELL, l.getX() + 0.5, l.getY(), l.getZ() + 0.5, 4, 0, 0.4, 0, 0.01);
        return true;
    }
}
