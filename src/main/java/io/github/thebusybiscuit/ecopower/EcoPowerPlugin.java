package io.github.thebusybiscuit.ecopower;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.ecopower.generators.SteamTurbine;
import io.github.thebusybiscuit.ecopower.generators.SteamTurbineMultiblock;
import io.github.thebusybiscuit.ecopower.items.SteelRotor;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.bstats.bukkit.Metrics;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.Updater;

public class EcoPowerPlugin extends JavaPlugin implements SlimefunAddon {

    @Override
    public void onEnable() {
        Config cfg = new Config(this);

        if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
            Updater updater = new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/EcoPower/master");
            updater.start();
        }

        new Metrics(this, 8154);

        ItemStack categoryItem = new CustomItem(SlimefunUtils.getCustomHead("240775c3ad75763613f32f04986881bbe4eee4366d0c57f17f7c7514e2d0a77d"), "&2Eco-Power Generators");
        Category category = new Category(new NamespacedKey(this, "generators"), categoryItem);

        SlimefunItemStack rotor = new SlimefunItemStack("STEEL_ROTOR", "c51944b488e11cda65177d5911d651282b3012665e63b8929e1b6a4744b7ca8", "&bSteel Rotor");
        new SteelRotor(category, rotor, new ItemStack[] {
                null, SlimefunItems.STEEL_INGOT, null,
                SlimefunItems.STEEL_INGOT, new ItemStack(Material.IRON_BLOCK), SlimefunItems.STEEL_INGOT,
                null, SlimefunItems.STEEL_INGOT, null
        }, new SlimefunItemStack(rotor, 2)).register(this);
        
        registerSteamTurbine(category, "STEAM_TURBINE", "&aSimple Steam Turbine", MachineTier.MEDIUM, 4, new ItemStack[] {
                null, rotor, null,
                SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT,
                null, SlimefunItems.COPPER_WIRE, null
        });
    }

    private void registerSteamTurbine(Category category, String id, String name, MachineTier tier, int power, ItemStack[] recipe) {
        final String texture = "aefd921cb61594324f3c09d7ac7d38185d2734333968f3ac38382cddf15f6d71";
        
        SlimefunItemStack turbineItem = new SlimefunItemStack(id, texture, name, "&7Component of the " + name + "Generator", LoreBuilder.machine(tier, MachineType.MACHINE));
        SteamTurbine turbine = new SteamTurbine(category, turbineItem, power, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        turbine.register(this);

        SlimefunItemStack multiblockItem = new SlimefunItemStack(id + "_MULTIBLOCK", texture, name + " Generator", "", LoreBuilder.machine(tier, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(power * 2));
        new SteamTurbineMultiblock(category, multiblockItem, turbine).register(this);
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/TheBusyBiscuit/EcoPower/issues";
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
