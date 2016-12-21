package code.elix_x.mods.skyblocks.proxy;

import code.elix_x.excore.utils.proxy.IProxy;
import code.elix_x.mods.skyblocks.SkyblocksBase;
import code.elix_x.mods.skyblocks.client.renderer.SkyblockTileEntityRenderer;
import code.elix_x.mods.skyblocks.tile.SkyblockTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy<SkyblocksBase> {

	@Override
	public void preInit(FMLPreInitializationEvent event){

	}

	@Override
	public void init(FMLInitializationEvent event){
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(SkyblocksBase.INSTANCE.skyblock), 0, new ModelResourceLocation(SkyblocksBase.SKYBLOCK, "inventory"));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event){
		Minecraft.getMinecraft().getFramebuffer().enableStencil();
		ClientRegistry.bindTileEntitySpecialRenderer(SkyblockTileEntity.class, new SkyblockTileEntityRenderer());
	}

}
