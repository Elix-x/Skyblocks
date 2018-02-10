package code.elix_x.mods.skyblocks.proxy;

import code.elix_x.excore.utils.proxy.IProxy;
import code.elix_x.mods.skyblocks.SkyblocksBase;
import code.elix_x.mods.skyblocks.client.renderer.SkyblockTileEntityRenderer;
import code.elix_x.mods.skyblocks.tile.SkyblockTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = SkyblocksBase.MODID, value = Side.CLIENT)
public class ClientProxy implements IProxy<SkyblocksBase> {

	@Override
	public void preInit(FMLPreInitializationEvent event){

	}

	@Override
	public void init(FMLInitializationEvent event){

	}

	@Override
	public void postInit(FMLPostInitializationEvent event){
		Minecraft.getMinecraft().getFramebuffer().enableStencil();
		ClientRegistry.bindTileEntitySpecialRenderer(SkyblockTileEntity.class, new SkyblockTileEntityRenderer());
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event){
		SkyblocksBase.INSTANCE.skyblocks.forEach(skyblock -> ModelLoader.setCustomStateMapper(skyblock, new StateMapperBase(){

			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state){
				return new ModelResourceLocation(SkyblocksBase.SKYBLOCK, "normal");
			}

		}));
		SkyblocksBase.INSTANCE.skyblockItems.forEach(item -> ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(SkyblocksBase.SKYBLOCK, "normal")));
	}

}
