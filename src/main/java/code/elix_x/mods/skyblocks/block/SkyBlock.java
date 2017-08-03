package code.elix_x.mods.skyblocks.block;

import code.elix_x.mods.skyblocks.tile.SkyblockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SkyBlock extends Block {

	public static final int TIMEINTERVAL = 6000;
	public static final IProperty<Integer> TIME = PropertyInteger.create("time", 0, 3);
	public static final IProperty<Boolean> FIXED = PropertyBool.create("fixed");

	public SkyBlock(){
		super(Material.CLOTH);
		setUnlocalizedName("skyblock");
		setCreativeTab(CreativeTabs.DECORATIONS);
		setLightOpacity(0);
	}

	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, TIME, FIXED);
	}

	@Override
	public int getMetaFromState(IBlockState state){
		return ((state.getValue(FIXED) ? 1 : 0) << 2) | state.getValue(TIME);
	}

	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(FIXED, (meta & 0b0100) != 0).withProperty(TIME, meta & 0b0011);
	}

	@Override
	public int damageDropped(IBlockState state){
		return getMetaFromState(state);
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items){
		for(boolean fixed : FIXED.getAllowedValues()) for(int time : TIME.getAllowedValues()) items.add(new ItemStack(this, 1, getMetaFromState(getDefaultState().withProperty(FIXED, fixed).withProperty(TIME, time))));
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
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side){
		return side == EnumFacing.UP ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
	}

}
