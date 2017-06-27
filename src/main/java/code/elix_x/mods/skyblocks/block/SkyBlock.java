package code.elix_x.mods.skyblocks.block;

import code.elix_x.mods.skyblocks.tile.SkyblockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SkyBlock extends Block {

	public static final IProperty<Boolean> POWERED = PropertyBool.create("powered");

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
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, POWERED);
	}

	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state){
		return 0;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos){
		return super.getActualState(state, world, pos).withProperty(POWERED, false);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state){
		return !state.getValue(POWERED);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return state.getValue(POWERED) ? EnumBlockRenderType.MODEL : EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

}
