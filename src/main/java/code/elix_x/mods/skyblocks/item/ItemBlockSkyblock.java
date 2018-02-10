package code.elix_x.mods.skyblocks.item;

import code.elix_x.mods.skyblocks.block.SkyBlock;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockSkyblock extends ItemBlock {

	public ItemBlockSkyblock(Block block){
		super(block);
		setMaxDamage(0);
		if(!block.getBlockState().getProperties().isEmpty()) setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage){
		return hasSubtypes ? damage : 0;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		tooltip.add(String.format("t%s%s", ((SkyBlock) getBlock()).isRelative() ? "+" : "=", ((SkyBlock) getBlock()).getTime()));
	}
}
