package io.github.thebusybiscuit.ecopower;

import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.ecopower.generators.HighEnergySolarGenerator;
import io.github.thebusybiscuit.ecopower.generators.LightningReceptor;
import io.github.thebusybiscuit.ecopower.generators.LunarGenerator;
import io.github.thebusybiscuit.ecopower.generators.SteamTurbine;
import io.github.thebusybiscuit.ecopower.generators.SteamTurbineMultiblock;
import io.github.thebusybiscuit.ecopower.generators.WindTurbine;
import io.github.thebusybiscuit.ecopower.generators.WindTurbineMultiblock;
import io.github.thebusybiscuit.ecopower.items.SteelRotor;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.SolarHelmet;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class EcoPowerPlugin extends JavaPlugin implements SlimefunAddon {

    @Override
    public void onEnable() {
        Config cfg = new Config(this);

        if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
            new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/EcoPower/master").start();
        }

        new Metrics(this, 8154);

        ItemStack categoryItem = new CustomItemStack(SlimefunUtils.getCustomHead("240775c3ad75763613f32f04986881bbe4eee4366d0c57f17f7c7514e2d0a77d"), "&2Eco-Power Generators");
        ItemGroup itemGroup = new ItemGroup(new NamespacedKey(this, "generators"), categoryItem, 4);

        SlimefunItemStack rotor = new SlimefunItemStack("STEEL_ROTOR", "c51944b488e11cda65177d5911d651282b3012665e63b8929e1b6a4744b7ca8", "&bSteel Rotor");
        
        new SteelRotor(itemGroup, rotor, new ItemStack[] {
                null, SlimefunItems.STEEL_INGOT, null,
                SlimefunItems.STEEL_INGOT, new ItemStack(Material.IRON_BLOCK), SlimefunItems.STEEL_INGOT,
                null, SlimefunItems.STEEL_INGOT, null
        }, new SlimefunItemStack(rotor, 2)).register(this);

        SteamTurbine simpleTurbine = registerSteamTurbine(itemGroup, "STEAM_TURBINE", "aefd921cb61594324f3c09d7ac7d38185d2734333968f3ac38382cddf15f6d71", "&eSimple Steam Turbine", MachineTier.MEDIUM, 4, new ItemStack[] {
                null, rotor, null,
                SlimefunItems.STEEL_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_INGOT,
                null, SlimefunItems.COPPER_WIRE, null
        });
        
        SteamTurbine advancedTurbine = registerSteamTurbine(itemGroup, "STEAM_TURBINE_2", "161aad79fb748bff1e6e94d4b6a5a277cc961c1a9abfe2a4ed88baab8a2b5971", "&cAdvanced Steam Turbine", MachineTier.ADVANCED, 6, new ItemStack[] {
                null, rotor, null,
                SlimefunItems.BRASS_INGOT, simpleTurbine.getItem(), SlimefunItems.BRASS_INGOT,
                SlimefunItems.BRASS_INGOT, SlimefunItems.COPPER_WIRE, SlimefunItems.BRASS_INGOT
        });
        
        registerSteamTurbine(itemGroup, "STEAM_TURBINE_3", "b65e29a67860d82f66afe1060ec8a9ceacc8c7afe108f5d42f52ba854b0a62dc", "&4Carbonado Steam Turbine", MachineTier.END_GAME, 13, new ItemStack[] {
                null, rotor, null,
                SlimefunItems.CARBONADO, advancedTurbine.getItem(), SlimefunItems.CARBONADO,
                SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT
        });

        WindTurbine simpleWindTurbine = registerWindTurbine(itemGroup, "WIND_TURBINE", "d23e4ce096e00eae6aba10d356b785c3fecc5aa3d7dad4a4a2a27ed7750df981", "&eSimple Wind Turbine", MachineTier.MEDIUM, 5, new ItemStack[] {
                null, rotor, null,
                SlimefunItems.STEEL_THRUSTER, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_THRUSTER,
                null, SlimefunItems.COPPER_WIRE, null
        });

        WindTurbine advancedWindTurbine = registerWindTurbine(itemGroup, "WIND_TURBINE_2", "2df9e595dbeac33f43b37dd4ffbc234ea0fa7c3f98aad77dc906ce5d6783c79d", "&cAdvanced Wind Turbine", MachineTier.ADVANCED, 11, new ItemStack[] {
                null, rotor, null,
                SlimefunItems.ELECTRO_MAGNET, simpleWindTurbine.getItem(), SlimefunItems.ELECTRO_MAGNET,
                SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.COPPER_WIRE, SlimefunItems.ALUMINUM_BRASS_INGOT
        });

        registerWindTurbine(itemGroup, "WIND_TURBINE_3", "3fcef461b43f06ef9d58c94065bbf41b77a10050520b44082d5f66f6dbe71da0", "&4Carbonado Wind Turbine", MachineTier.END_GAME, 23, new ItemStack[] {
                SlimefunItems.FERROSILICON, rotor, SlimefunItems.FERROSILICON,
                SlimefunItems.ELECTRIC_MOTOR, advancedWindTurbine.getItem(), SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.CARBONADO, SlimefunItems.FERROSILICON, SlimefunItems.CARBONADO
        });
        
        registerLightningReceptor(itemGroup, "LIGHTNING_RECEPTOR", "&eLightning Receptor", 512, 8192, new ItemStack[] {
                null, new ItemStack(Material.END_ROD), null,
                SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BLISTERING_INGOT_3,
                SlimefunItems.REINFORCED_PLATE, SlimefunItems.ENERGY_REGULATOR, SlimefunItems.REINFORCED_PLATE
        });
        
        LunarGenerator lunarGenerator = registerLunarGenerator(itemGroup, "LUNAR_GENERATOR", "&5Lunar Generator", 128, new ItemStack[] {
                new ItemStack(Material.PHANTOM_MEMBRANE), SlimefunItems.SOLAR_GENERATOR_4, new ItemStack(Material.PHANTOM_MEMBRANE),
                SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.CARBONADO, SlimefunItems.DAMASCUS_STEEL_INGOT,
                SlimefunItems.COPPER_WIRE, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.COPPER_WIRE
        });
        
        SolarGenerator solarGenerator = registerHighEnergySolarGenerator(itemGroup, "HIGH_ENERGY_SOLAR_GENERATOR", "c4fe135c311f7086edcc5e6dbc4ef4b23f819fddaa42f827dac46e3574de2287", "&9High-Energy Solar Generator", 256, new ItemStack[] {
                SlimefunItems.SOLAR_GENERATOR_2, lunarGenerator.getItem(), SlimefunItems.SOLAR_GENERATOR_2,
                SlimefunItems.CARBONADO, SlimefunItems.POWER_CRYSTAL, SlimefunItems.CARBONADO,
                SlimefunItems.BLISTERING_INGOT_3, new ItemStack(Material.NETHER_STAR), SlimefunItems.BLISTERING_INGOT_3
        });
        
        registerSolarHelmet(itemGroup, "HIGH_ENERGY_SOLAR_HELMET", "&9High-Energy Solar Helmet", 5, new ItemStack[] {
                null, solarGenerator.getItem(), null,
                SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT,
                SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT
        });
        
        registerHighEnergySolarGenerator(itemGroup, "RADIANT_SOLAR_GENERATOR", "240775c3ad75763613f32f04986881bbe4eee4366d0c57f17f7c7514e2d0a77d", "&9Radiant Solar Generator", 512, new ItemStack[] {
                lunarGenerator.getItem(), solarGenerator.getItem(), lunarGenerator.getItem(),
                SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.POWER_CRYSTAL, SlimefunItems.BLISTERING_INGOT_3,
                SlimefunItems.REINFORCED_PLATE, SlimefunItems.CARBONADO, SlimefunItems.REINFORCED_PLATE
        });
    }

    private WindTurbine registerWindTurbine(ItemGroup itemGroup, String id, String texture, String name, MachineTier tier, int power, ItemStack[] recipe) {
        SlimefunItemStack turbineItem = new SlimefunItemStack(id, texture, name, "&7Component of the " + name + " Generator");
        WindTurbine turbine = new WindTurbine(itemGroup, turbineItem, power, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        turbine.register(this);

        SlimefunItemStack multiblockItem = new SlimefunItemStack(id + "_MULTIBLOCK", texture, name + " Generator", "", LoreBuilder.machine(tier, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(power * 2));
        new WindTurbineMultiblock(itemGroup, multiblockItem, turbine).register(this);
        return turbine;
    }

    private SteamTurbine registerSteamTurbine(ItemGroup itemGroup, String id, String texture, String name, MachineTier tier, int power, ItemStack[] recipe) {
        SlimefunItemStack turbineItem = new SlimefunItemStack(id, texture, name, "&7Component of the " + name + " Generator");
        SteamTurbine turbine = new SteamTurbine(itemGroup, turbineItem, power, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        turbine.register(this);

        SlimefunItemStack multiblockItem = new SlimefunItemStack(id + "_MULTIBLOCK", texture, name + " Generator", "", LoreBuilder.machine(tier, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(power * 2));
        new SteamTurbineMultiblock(itemGroup, multiblockItem, turbine).register(this);
        return turbine;
    }
    
    private LightningReceptor registerLightningReceptor(ItemGroup itemGroup, String id, String name, int min, int max, ItemStack[] recipe) {
        final String texture = "31a3cd9b016b1228ec01fd6f0992c64f3b9b7b29773fa46439ab3f3c8a347704";
        
        SlimefunItemStack item = new SlimefunItemStack(id, texture, name, "", "&fThis machine can channel energy", "&ffrom lightning strikes during", "&fa thunderstorm", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.power(min, " - " + max + " J per lightning strike"));
        LightningReceptor receptor = new LightningReceptor(itemGroup, item, min, max, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        receptor.register(this);
        return receptor;
    }
    
    private LunarGenerator registerLunarGenerator(ItemGroup itemGroup, String id, String name, int power, ItemStack[] recipe) {
        final String texture = "afdd9e588d2461d2d3d058cb3e0af2b3a3367607aa14d124ed92a833f25fb112";
        SlimefunItemStack item = new SlimefunItemStack(id, texture, name, "", "&fThis Lunar Generator only", "&fruns at night!", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(power * 2));
    
        LunarGenerator generator = new LunarGenerator(itemGroup, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, power);
        generator.register(this);
        return generator;
    }
    
    private HighEnergySolarGenerator registerHighEnergySolarGenerator(ItemGroup itemGroup, String id, String texture, String name, int power, ItemStack[] recipe) {
        SlimefunItemStack item = new SlimefunItemStack(id, texture, name, "", "&fThis Solar Generator runs", "&fall day and night!", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(power * 2));
    
        HighEnergySolarGenerator generator = new HighEnergySolarGenerator(itemGroup, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, power);
        generator.register(this);
        return generator;
    }
    
    private SolarHelmet registerSolarHelmet(ItemGroup itemGroup, String id, String name, int power, ItemStack[] recipe) {
        SlimefunItemStack item = new SlimefunItemStack(id, Material.IRON_HELMET, name, "", "&fThis Solar Helmet charges", "&fany held or worn items", "", LoreBuilder.power(power, "/charge"));
        
        SolarHelmet solarHelmet = new SolarHelmet(itemGroup, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, power);
        solarHelmet.register(this);
        return solarHelmet;
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
