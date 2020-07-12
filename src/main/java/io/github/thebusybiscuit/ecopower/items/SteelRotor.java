package io.github.thebusybiscuit.ecopower.items;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SteelRotor extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    public SteelRotor(Category category, SlimefunItemStack item, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, recipeOutput);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return PlayerRightClickEvent::cancel;
    }

}
