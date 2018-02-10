package code.elix_x.mods.skyblocks.block;

import code.elix_x.mods.skyblocks.tile.SkyblockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SkyBlock extends Block {

	public static final int TIMEINTERVAL = 6000;

	protected int time;
	protected boolean relative;

	public SkyBlock(int time, boolean relative){
		super(Material.CLOTH);
		this.time = time;
		this.relative = relative;

		setUnlocalizedName("skyblock");
		setCreativeTab(CreativeTabs.DECORATIONS);
		setLightOpacity(0);
	}

	public int getTime(){
		return time;
	}

	public boolean isRelative(){
		return relative;
	}

	public int getSkyblockTime(World world, IBlockState state){
		return (relative ? (int) world.getWorldTime() : 0) + time;
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
