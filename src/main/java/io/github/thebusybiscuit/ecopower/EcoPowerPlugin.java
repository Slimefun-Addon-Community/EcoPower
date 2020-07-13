package io.github.thebusybiscuit.ecopower;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.ecopower.generators.HighEnergySolarGenerator;
import io.github.thebusybiscuit.ecopower.generators.LunarGenerator;
import io.github.thebusybiscuit.ecopower.generators.SteamTurbine;
import io.github.thebusybiscuit.ecopower.generators.SteamTurbineMultiblock;
import io.github.thebusybiscuit.ecopower.items.SteelRotor;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;
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
        Category category = new Category(new NamespacedKey(this, "generators"), categoryItem, 4);

        SlimefunItemStack rotor = new SlimefunItemStack("STEEL_ROTOR", "c51944b488e11cda65177d5911d651282b3012665e63b8929e1b6a4744b7ca8", "&bSteel Rotor");
        new SteelRotor(category, rotor, new ItemStack[] {
                null, SlimefunItems.STEEL_INGOT, null,
                SlimefunItems.STEEL_INGOT, new ItemStack(Material.IRON_BLOCK), SlimefunItems.STEEL_INGOT,
                null, SlimefunItems.STEEL_INGOT, null
        }, new SlimefunItemStack(rotor, 2)).register(this);
        
        SteamTurbine simpleTurbine = registerSteamTurbine(category, "STEAM_TURBINE", "&eSimple Steam Turbine", MachineTier.MEDIUM, 4, new ItemStack[] {
                null, rotor, null,
                SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT,
                null, SlimefunItems.COPPER_WIRE, null
        });
        
        SteamTurbine advancedTurbine = registerSteamTurbine(category, "STEAM_TURBINE_2", "&cAdvanced Steam Turbine", MachineTier.ADVANCED, 6, new ItemStack[] {
                null, rotor, null,
                SlimefunItems.BRASS_INGOT, simpleTurbine.getItem(), SlimefunItems.BRASS_INGOT,
                SlimefunItems.BRASS_INGOT, SlimefunItems.COPPER_WIRE, SlimefunItems.BRASS_INGOT
        });
        
        registerSteamTurbine(category, "STEAM_TURBINE_3", "&4Carbonado Steam Turbine", MachineTier.END_GAME, 13, new ItemStack[] {
                null, rotor, null,
                SlimefunItems.CARBONADO, advancedTurbine.getItem(), SlimefunItems.CARBONADO,
                SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT
        });
        
        LunarGenerator lunarGenerator = registerLunarGenerator(category, "LUNAR_GENERATOR", "afdd9e588d2461d2d3d058cb3e0af2b3a3367607aa14d124ed92a833f25fb112", "&5Lunar Generator", 128, new ItemStack[] {
                new ItemStack(Material.PHANTOM_MEMBRANE), SlimefunItems.SOLAR_GENERATOR_4, new ItemStack(Material.PHANTOM_MEMBRANE),
                SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.CARBONADO, SlimefunItems.DAMASCUS_STEEL_INGOT,
                SlimefunItems.COPPER_WIRE, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.COPPER_WIRE
        });
        
        SolarGenerator solarGenerator = registerHighEnergySolarGenerator(category, "HIGH_ENERGY_SOLAR_GENERATOR", "c4fe135c311f7086edcc5e6dbc4ef4b23f819fddaa42f827dac46e3574de2287", "&9High-Energy Solar Generator", 256, new ItemStack[] {
                SlimefunItems.SOLAR_GENERATOR_2, lunarGenerator.getItem(), SlimefunItems.SOLAR_GENERATOR_2,
                SlimefunItems.CARBONADO, SlimefunItems.POWER_CRYSTAL, SlimefunItems.CARBONADO,
                SlimefunItems.BLISTERING_INGOT_3, new ItemStack(Material.NETHER_STAR), SlimefunItems.BLISTERING_INGOT_3
        });
        
        registerHighEnergySolarGenerator(category, "RADIANT_SOLAR_GENERATOR", "240775c3ad75763613f32f04986881bbe4eee4366d0c57f17f7c7514e2d0a77d", "&9Radiant Solar Generator", 512, new ItemStack[] {
                lunarGenerator.getItem(), solarGenerator.getItem(), lunarGenerator.getItem(),
                SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BLISTERING_INGOT_3,
                SlimefunItems.REINFORCED_PLATE, SlimefunItems.CARBONADO, SlimefunItems.REINFORCED_PLATE
        });
    }

    private SteamTurbine registerSteamTurbine(Category category, String id, String name, MachineTier tier, int power, ItemStack[] recipe) {
        final String texture = "aefd921cb61594324f3c09d7ac7d38185d2734333968f3ac38382cddf15f6d71";
        
        SlimefunItemStack turbineItem = new SlimefunItemStack(id, texture, name, "&7Component of the " + name + " Generator");
        SteamTurbine turbine = new SteamTurbine(category, turbineItem, power, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        turbine.register(this);

        SlimefunItemStack multiblockItem = new SlimefunItemStack(id + "_MULTIBLOCK", texture, name + " Generator", "", LoreBuilder.machine(tier, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(power * 2));
        new SteamTurbineMultiblock(category, multiblockItem, turbine).register(this);
        return turbine;
    }
    
    private LunarGenerator registerLunarGenerator(Category category, String id, String texture, String name, int power, ItemStack[] recipe) {
        SlimefunItemStack item = new SlimefunItemStack(id, texture, name, "", "&fThis Lunar Generator only", "&7runs at night!", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(power * 2));
    
        LunarGenerator generator = new LunarGenerator(category, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, power);
        generator.register(this);
        return generator;
    }
    
    private HighEnergySolarGenerator registerHighEnergySolarGenerator(Category category, String id, String texture, String name, int power, ItemStack[] recipe) {
        SlimefunItemStack item = new SlimefunItemStack(id, texture, name, "", "&fThis Solar Generator runs", "&7all day and night!", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(power * 2));
    
        HighEnergySolarGenerator generator = new HighEnergySolarGenerator(category, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, power);
        generator.register(this);
        return generator;
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
