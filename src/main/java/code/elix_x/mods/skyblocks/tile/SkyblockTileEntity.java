package code.elix_x.mods.skyblocks.tile;

import code.elix_x.mods.skyblocks.block.SkyBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SkyblockTileEntity extends TileEntity {

	public int getSkyblockTime(){
		IBlockState state = getBlockType().getStateFromMeta(getBlockMetadata());
		return (state.getValue(SkyBlock.FIXED) ? 0 : (int) world.getWorldTime()) + state.getValue(SkyBlock.TIME) * SkyBlock.TIMEINTERVAL;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared(){
		return Double.POSITIVE_INFINITY;
	}

}
