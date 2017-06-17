package code.elix_x.mods.skyblocks.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SkyblockTileEntity extends TileEntity {

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared(){
		return Double.POSITIVE_INFINITY;
	}

}
