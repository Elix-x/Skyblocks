package code.elix_x.mods.skyblocks;

import code.elix_x.excore.utils.mod.IMod;
import code.elix_x.excore.utils.proxy.IProxy;
import code.elix_x.mods.skyblocks.block.SkyBlock;
import code.elix_x.mods.skyblocks.item.ItemBlockSkyblock;
import code.elix_x.mods.skyblocks.tile.SkyblockTileEntity;
import code.elix_x.mods.skyblocks.worldgen.CloudsGenerator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
@Mod(modid = SkyblocksBase.MODID, name = SkyblocksBase.NAME, version = SkyblocksBase.VERSION)
public class SkyblocksBase implements IMod<SkyblocksBase, IProxy<SkyblocksBase>> {

	public static final String MODID = "skyblocks";
	public static final String NAME = "Skyblocks";
	public static final String VERSION = "@VERSION@";

	public static final ResourceLocation SKYBLOCK = new ResourceLocation(MODID, "skyblock");

	@Instance(MODID)
	public static SkyblocksBase INSTANCE;

	@SidedProxy(modId = MODID, clientSide = "code.elix_x.mods.skyblocks.proxy.ClientProxy", serverSide = "code.elix_x.mods.skyblocks.proxy.ServerProxy")
	public static IProxy<SkyblocksBase> proxy;

	public List<Block> skyblocks = new ArrayList<>();
	public List<Item> skyblockItems = new ArrayList<>();

	@Override
	public IProxy<SkyblocksBase> getProxy(){
		return proxy;
	}

	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		boolean[] relative = new boolean[]{true, false};
		int[] times = new int[]{0, 6000, 12000, 18000};

		for(boolean rel : relative) for(int t : times){
			ResourceLocation reg = new ResourceLocation(SKYBLOCK.getResourceDomain(), String.format("%s_%s_%s", SKYBLOCK.getResourcePath(), rel ? "relative" : "fixed", t));
			Block skyblock = new SkyBlock(t, rel).setRegistryName(reg);
			skyblocks.add(skyblock);
			skyblockItems.add(new ItemBlockSkyblock(skyblock).setRegistryName(reg));
		}
		GameRegistry.registerTileEntity(SkyblockTileEntity.class, SKYBLOCK.toString());

		File configFile = new File(event.getModConfigurationDirectory(), NAME + ".cfg");
		Configuration config = new Configuration(configFile, VERSION);
		config.load();
		if(config.getBoolean("Enabled", "CLOUDS", true, "Enable / disable skyblock clouds generation")) GameRegistry.registerWorldGenerator(new CloudsGenerator(config, skyblocks.get(0).getDefaultState()), 0);
		config.save();
		proxy.preInit(event);
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event){
		INSTANCE.skyblocks.forEach(event.getRegistry()::register);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event){
		INSTANCE.skyblockItems.forEach(event.getRegistry()::register);
	}

	@EventHandler
	@Override
	public void init(FMLInitializationEvent event){
		proxy.init(event);
	}

	@Override
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit(event);
	}

}
