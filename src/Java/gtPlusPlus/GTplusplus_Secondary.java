package gtPlusPlus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.*;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.ORES;
import gtPlusPlus.core.world.darkworld.Dimension_DarkWorld;
import gtPlusPlus.core.world.darkworld.biome.Biome_DarkWorld;
import gtPlusPlus.core.world.darkworld.block.DarkWorldContentLoader;
import gtPlusPlus.core.world.darkworld.gen.gt.WorldGen_GT_Base;
import gtPlusPlus.core.world.darkworld.gen.gt.WorldGen_Ores;
import gtPlusPlus.xmod.gregtech.HANDLER_GT;
import gtPlusPlus.xmod.gregtech.api.util.GTPP_Config;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;

@MCVersion(value = "1.7.10")
@Mod(modid = Everglades.MODID, name = Everglades.NAME, version = Everglades.VERSION, dependencies = "required-after:Forge; after:dreamcraft; after:IC2; after:ihl; required-after:gregtech; required-after:miscutils;")
public class GTplusplus_Secondary implements ActionListener {

	//Mod Instance
	@Mod.Instance(Everglades.MODID)
	public static GTplusplus_Secondary instance;

	// Dark World Handler
	protected static volatile Biome_DarkWorld DarkWorld_Biome;
	protected static volatile Dimension_DarkWorld DarkWorld_Dimension;
	public static int globalDarkWorldPortalSpawnTimer = 0;

	// Pre-Init
	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		Logger.INFO("Loading " + Everglades.MODID + " V" + Everglades.VERSION);

		//Setup
		setVars(event);
		
		setDarkBiome(new Biome_DarkWorld());
		DarkWorld_Dimension = new Dimension_DarkWorld();
		
		// Load Dark World
		getDarkBiome().instance = instance;
		DarkWorld_Dimension.instance = instance;
		getDarkBiome().preInit(event);
		DarkWorld_Dimension.preInit(event);
		
		//Load/Set Custom Ore Gen
		HANDLER_GT.sCustomWorldgenFile = new GTPP_Config(new Configuration(new File(new File(event.getModConfigurationDirectory(), "GTplusplus"), "WorldGeneration.cfg")));
		
	}

	@EventHandler
	public void load(final FMLInitializationEvent e) {
		Logger.INFO("Begin resource allocation for " + Everglades.MODID + " V" +Everglades.VERSION);
		
		//Load Dark World and Biome
		GameRegistry.registerWorldGenerator(new WorldGen_GT_Base(), Short.MAX_VALUE);
		getDarkBiome().load();
		DarkWorld_Dimension.load();

	}
	
	public static void GenerateOreMaterials() {
		MaterialGenerator.generateOreMaterial(ORES.CROCROITE);
		MaterialGenerator.generateOreMaterial(ORES.GEIKIELITE);
		MaterialGenerator.generateOreMaterial(ORES.NICHROMITE);
		MaterialGenerator.generateOreMaterial(ORES.TITANITE);
		MaterialGenerator.generateOreMaterial(ORES.ZIMBABWEITE);
		MaterialGenerator.generateOreMaterial(ORES.ZIRCONILITE);
		MaterialGenerator.generateOreMaterial(ORES.GADOLINITE_CE);
		MaterialGenerator.generateOreMaterial(ORES.GADOLINITE_Y);
		MaterialGenerator.generateOreMaterial(ORES.LEPERSONNITE);
		MaterialGenerator.generateOreMaterial(ORES.SAMARSKITE_Y);
		MaterialGenerator.generateOreMaterial(ORES.SAMARSKITE_YB);
		MaterialGenerator.generateOreMaterial(ORES.XENOTIME);
		MaterialGenerator.generateOreMaterial(ORES.YTTRIAITE);
		MaterialGenerator.generateOreMaterial(ORES.YTTRIALITE);
		MaterialGenerator.generateOreMaterial(ORES.YTTROCERITE);
		MaterialGenerator.generateOreMaterial(ORES.ZIRCON);
		MaterialGenerator.generateOreMaterial(ORES.POLYCRASE);
		MaterialGenerator.generateOreMaterial(ORES.ZIRCOPHYLLITE);
		MaterialGenerator.generateOreMaterial(ORES.ZIRKELITE);
		MaterialGenerator.generateOreMaterial(ORES.LANTHANITE_LA);
		MaterialGenerator.generateOreMaterial(ORES.LANTHANITE_CE);
		MaterialGenerator.generateOreMaterial(ORES.LANTHANITE_ND);
		MaterialGenerator.generateOreMaterial(ORES.AGARDITE_Y);
		MaterialGenerator.generateOreMaterial(ORES.AGARDITE_CD);
		MaterialGenerator.generateOreMaterial(ORES.AGARDITE_LA);
		MaterialGenerator.generateOreMaterial(ORES.AGARDITE_ND);
		MaterialGenerator.generateOreMaterial(ORES.HIBONITE);
		MaterialGenerator.generateOreMaterial(ORES.CERITE);
		MaterialGenerator.generateOreMaterial(ORES.FLUORCAPHITE);
		MaterialGenerator.generateOreMaterial(ORES.FLORENCITE);
		MaterialGenerator.generateOreMaterial(ORES.CRYOLITE);		
	}

	void setVars(FMLPreInitializationEvent event){
		//Init WorldGen config.
        HANDLER_GT.sCustomWorldgenFile = new GTPP_Config(new Configuration(new File(new File(event.getModConfigurationDirectory(), "GTplusplus"), "WorldGeneration.cfg")));
		
		if (DimensionManager.isDimensionRegistered(Dimension_DarkWorld.DIMID)){
			Dimension_DarkWorld.DIMID = DimensionManager.getNextFreeDimId();
		}
		
		/*
		 * Set World Generation Values
		 */
		WorldGen_Ores.generateValidOreVeins();
		WorldGen_GT_Base.oreveinPercentage = 64;
		WorldGen_GT_Base.oreveinAttempts = 32;
		WorldGen_GT_Base.oreveinMaxPlacementAttempts = 6;	
		if (CORE.DEBUG || CORE.DEVENV){
			WorldGen_GT_Base.debugWorldGen = true;
		}		
		DarkWorldContentLoader.run();
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		getDarkBiome().serverLoad(event);
		DarkWorld_Dimension.serverLoad(event);
	}

	/*@Override
	public int getBurnTime(ItemStack fuel) {
		if (DarkWorld_Biome.addFuel(fuel) != 0)
			return DarkWorld_Biome.addFuel(fuel);
		if (DarkWorld_Dimension.addFuel(fuel) != 0)
			return DarkWorld_Dimension.addFuel(fuel);
		return 0;
	}*/

	/*@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		chunkX = chunkX * 16;
		chunkZ = chunkZ * 16;		

		if (world.provider.dimensionId == Dimension_DarkWorld.DIMID) {
			DarkWorld_Biome.generateSurface(world, random, chunkX, chunkZ);
		}
		
		//What does this even do?
		if (world.provider.dimensionId == -1) {
			DarkWorld_Biome.generateNether(world, random, chunkX, chunkZ);
		}
		if (world.provider.dimensionId == 0) {
			DarkWorld_Biome.generateSurface(world, random, chunkX, chunkZ);
		}

	}*/

	@EventHandler
	public static void postInit(final FMLPostInitializationEvent e) {
		Logger.INFO("Finished loading Toxic Everglades plugin for GT++.");
	}

	public static synchronized Biome_DarkWorld getDarkBiome() {
		return DarkWorld_Biome;
	}

	public static synchronized void setDarkBiome(Biome_DarkWorld darkWorld_Biome) {
		DarkWorld_Biome = darkWorld_Biome;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
