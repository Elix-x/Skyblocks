package code.elix_x.mods.skyblocks;

import java.io.File;

import code.elix_x.excore.utils.mod.IMod;
import code.elix_x.excore.utils.proxy.IProxy;
import code.elix_x.excore.utils.recipes.RecipeStringTranslator;
import code.elix_x.mods.skyblocks.block.SkyBlock;
import code.elix_x.mods.skyblocks.tile.SkyblockTileEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

	public SkyBlock skyblock;

	@Override
	public IProxy<SkyblocksBase> getProxy(){
		return proxy;
	}

	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		GameRegistry.register((skyblock = new SkyBlock()).setRegistryName(SKYBLOCK));
		GameRegistry.register(new ItemBlock(skyblock).setRegistryName(SKYBLOCK));
		GameRegistry.registerTileEntity(SkyblockTileEntity.class, SKYBLOCK.toString());

		File configFile = new File(event.getModConfigurationDirectory(), NAME + ".cfg");
		Configuration config = new Configuration(configFile, VERSION);
		config.load();
		GameRegistry.addRecipe(RecipeStringTranslator.fromString(new ItemStack(skyblock, config.getInt("amount", "CRAFTING", 6, 1, 64, "The amount of skyblocks obtained as result of crafting them")), RecipeStringTranslator.validateFromConfig(config.getStringList("recipe", "CRAFTING", RecipeStringTranslator.toString("GEG", "GSG", "GEG", 'G', "blockGlass", 'E', Items.ENDER_EYE, 'S', "endstone"), "The recipe for skyblock"))));
		config.save();
		proxy.preInit(event);
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