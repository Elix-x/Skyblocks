package code.elix_x.mods.skyblocks.block;

import code.elix_x.mods.skyblocks.tile.SkyblockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class SkyBlock extends Block {

	public SkyBlock(){
		super(Material.CLOTH);
		setUnlocalizedName("skyblock");
		setCreativeTab(CreativeTabs.DECORATIONS);
	}

	@Override
	public boolean hasTileEntity(IBlockState state){
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state){
		return new SkyblockTileEntity();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

}
