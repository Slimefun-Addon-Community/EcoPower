package io.github.thebusybiscuit.ecopower.generators;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.GeneratorTicker;
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

            @Override
            public double generateEnergy(Location l, SlimefunItem item, Config data) {
                Block water = l.getBlock().getRelative(BlockFace.DOWN);
                Block magma = water.getRelative(BlockFace.DOWN);

                if (water.getType() != Material.WATER || magma.getType() != Material.MAGMA_BLOCK) {
                    return 0;
                }

                water.setType(Material.AIR);
                l.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, l.getX(), l.getY() + 0.5, l.getZ(), 4, 0, 0.7, 0);

                return generatedPower;
            }

            @Override
            public boolean explode(Location l) {
                return false;
            }
        };
    }

}
