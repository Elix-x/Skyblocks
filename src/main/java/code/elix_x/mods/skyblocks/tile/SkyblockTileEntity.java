package code.elix_x.mods.skyblocks.tile;

import code.elix_x.mods.skyblocks.SkyblocksBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SkyblockTileEntity extends TileEntity {

	public int getSkyblockTime(){
		IBlockState state = getBlockType().getStateFromMeta(getBlockMetadata());
		return SkyblocksBase.INSTANCE.skyblock.getSkyblockTime(world, state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared(){
		return Double.POSITIVE_INFINITY;
	}

}
