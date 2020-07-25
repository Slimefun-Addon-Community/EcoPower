package io.github.thebusybiscuit.ecopower.generators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;

/**
 * This is the multiblock variant of the {@link SteamTurbine}, as shown in the {@link SlimefunGuide}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SteamTurbineMultiblock extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    public SteamTurbineMultiblock(Category category, SlimefunItemStack item, SteamTurbine turbine) {
        super(category, item, RecipeType.MULTIBLOCK, new ItemStack[] {
                null, turbine.getItem(), null,
                null, new CustomItem(Material.WATER_BUCKET, "&fWater (Bubble Column)"), null,
                null, new ItemStack(Material.MAGMA_BLOCK), null
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
