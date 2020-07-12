package io.github.thebusybiscuit.ecopower.generators;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

public class SteamTurbine extends SimpleSlimefunItem<GeneratorTicker> implements EnergyNetComponent {

    private final int generatedPower;

    public SteamTurbine(Category category, SlimefunItemStack item, int generatedPower, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        this.generatedPower = generatedPower;
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.GENERATOR;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public GeneratorTicker getItemHandler() {
        return new GeneratorTicker() {

            // A Thread-safe concurrent HashSet
            private final Set<Location> validTurbines = ConcurrentHashMap.newKeySet();

            @Override
            public double generateEnergy(Location l, SlimefunItem item, Config data) {
                int power = validTurbines.remove(l) ? generatedPower : 0;

                Slimefun.runSync(() -> {
                    if (validateLocation(l)) {
                        // Mark the turbine as valid (if valid)
                        validTurbines.add(l);
                    }
                });

                return power;
            }

            private boolean validateLocation(Location l) {
                Block water = l.getBlock().getRelative(BlockFace.DOWN);

                // A Bubble Column is water above a Magma Block
                if (water.getType() != Material.BUBBLE_COLUMN) {
                    return false;
                }

                water.setType(Material.AIR);
                l.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, l.getX() + 0.5, l.getY(), l.getZ() + 0.5, 3, 0, 0.4, 0, 0.01);
                return true;
            }

            @Override
            public boolean explode(Location l) {
                return false;
            }
        };
    }

}
