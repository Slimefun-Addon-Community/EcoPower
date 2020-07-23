package io.github.thebusybiscuit.ecopower.generators;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WindTurbineMultiblock extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    public WindTurbineMultiblock(Category category, SlimefunItemStack item, WindTurbine turbine) {
        super(category, item, RecipeType.MULTIBLOCK, new ItemStack[] {
                null, turbine.getItem(), null,
                null, new ItemStack(Material.OAK_FENCE), null,
                null, new ItemStack(Material.OAK_FENCE), null
        });
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();
            e.getPlayer().sendMessage("Psst, this Item is just a dummy. You need to place the actual structure down.");
        };
    }

}
