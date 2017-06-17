package code.elix_x.mods.skyblocks.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class CloudsGenerator implements IWorldGenerator {

	private final float rainfallMin;
	private final float rainfallMax;
	private final int spawnRarity;
	private final int spawnHeight;
	private final int spawnRange;

	private final CloudGenerator tinyCloudGen;
	private final CloudGenerator smallCloudGen;
	private final CloudGenerator mediumCloudGen;
	private final CloudGenerator largeCloudGen;
	private final CloudGenerator hugeCloudGen;

	public CloudsGenerator(Configuration config, IBlockState skyblock){
		this.rainfallMin = config.getFloat("Rainfall Min", "CLOUDS", 0, 0, 1, "Minimum biome rainfall to generate clouds");
		this.rainfallMax = config.getFloat("Rainfall Max", "CLOUDS", 0, Biomes.PLAINS.getRainfall(), 1, "Maximum biome rainfall to generate clouds");
		this.spawnRarity = config.getInt("Rarity", "CLOUDS", 10, 0, 100, "Spawn rarity of clouds");
		this.spawnHeight = config.getInt("Height", "CLOUDS", 212, 0, 255, "Spawn height of clouds");
		this.spawnRange = config.getInt("Range", "CLOUDS", 13, 0, 255, "Spawn range of clouds");

		this.tinyCloudGen = new CloudGenerator(3, skyblock, false);
		this.smallCloudGen = new CloudGenerator(10, skyblock, false);
		this.mediumCloudGen = new CloudGenerator(18, skyblock, false);
		this.largeCloudGen = new CloudGenerator(27, skyblock, false);
		this.hugeCloudGen = new CloudGenerator(37, skyblock, false);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider){
		if(world.provider.getDimension() == 0){
			int xPos = chunkX * 16 + 8;
			int zPos = chunkZ * 16 + 8;

			BlockPos chunkPos = new BlockPos(xPos, 0, zPos);

			BlockPos position;

			Biome biome = world.getChunkFromBlockCoords(chunkPos).getBiome(chunkPos, world.getBiomeProvider());

			if(biome == null){
				return;
			}

			if(rainfallMin <= biome.getRainfall() && biome.getRainfall() <= rainfallMax && random.nextInt(spawnRarity) == 0){
				int xSpawn = xPos + random.nextInt(16);
				int ySpawn = random.nextInt(spawnRange) + spawnHeight;
				int zSpawn = zPos + random.nextInt(16);
				position = new BlockPos(xSpawn, ySpawn, zSpawn);

				int size = random.nextInt(25);

				if(size < 11){
					this.tinyCloudGen.generateCloud(random, world, position);
				} else if(size < 16){
					this.smallCloudGen.generateCloud(random, world, position);
				} else if(size < 19){
					this.mediumCloudGen.generateCloud(random, world, position);
				} else if(size < 22){
					this.largeCloudGen.generateCloud(random, world, position);
				} else{
					this.hugeCloudGen.generateCloud(random, world, position);
				}
			}
		}
	}

	//Courtesy of Natura by progwml6 & contributors!
	private class CloudGenerator {

		final int cloudSize;
		final IBlockState cloud;
		final boolean flatCloud;

		public CloudGenerator(int cloudSize, IBlockState cloud, boolean flatCloud){
			this.cloudSize = cloudSize;
			this.cloud = cloud;
			this.flatCloud = flatCloud;
		}

		public void generateCloud(Random random, World world, BlockPos pos){
			int rand = random.nextInt(3) - 1;
			int rand1 = random.nextInt(3) - 1;

			for(int block = 0; block < this.cloudSize; block++){
				int xIter = pos.getX() + (random.nextInt(3) - 1) + rand;
				int yIter = pos.getY();
				int zIter = pos.getZ() + (random.nextInt(3) - 1) + rand1;

				if(random.nextBoolean() && !this.flatCloud || this.flatCloud && random.nextInt(10) == 0){
					yIter = pos.getY() + random.nextInt(3) - 1;
				}

				for(int x = xIter; x < xIter + random.nextInt(4) + 3 * (this.flatCloud ? 3 : 1); x++){
					int mathX = xIter - x;

					for(int y = yIter; y < yIter + random.nextInt(1) + 2; y++){
						int mathY = yIter - y;

						for(int z = zIter; z < zIter + random.nextInt(4) + 3 * (this.flatCloud ? 3 : 1); z++){
							int mathZ = zIter - z;

							if(Math.abs(mathX) + Math.abs(mathY) + Math.abs(mathZ) < 4 * (this.flatCloud ? 3 : 1) + random.nextInt(2)){
								BlockPos blockpos = new BlockPos(x, y, z);
								IBlockState state = world.getBlockState(blockpos);

								if(state.getBlock() == Blocks.AIR){
									world.setBlockState(blockpos, this.cloud, 2);
								}
							}
						}
					}
				}
			}
		}
	}

}
