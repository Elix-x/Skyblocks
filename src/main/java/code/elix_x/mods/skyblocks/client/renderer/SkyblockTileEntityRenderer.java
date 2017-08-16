package code.elix_x.mods.skyblocks.client.renderer;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.excore.utils.client.render.wtw.WTWRenderer;
import code.elix_x.mods.skyblocks.tile.SkyblockTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class SkyblockTileEntityRenderer extends TileEntitySpecialRenderer<SkyblockTileEntity> {

	public SkyblockTileEntityRenderer(){
		MinecraftForge.EVENT_BUS.register(this);
	}

	private Queue<Consumer<BufferBuilder>> skyBlocks = new LinkedList<>();

	@Override
	public void render(SkyblockTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
		World world = te.getWorld();
		BlockPos pos = te.getPos();
		IBlockState state = world.getBlockState(pos);
		if(world.isBlockIndirectlyGettingPowered(pos) == 0)
			if(state.shouldSideBeRendered(world, pos, EnumFacing.DOWN) || state.shouldSideBeRendered(world, pos, EnumFacing.NORTH) || state.shouldSideBeRendered(world, pos, EnumFacing.WEST) || state.shouldSideBeRendered(world, pos, EnumFacing.UP) || state.shouldSideBeRendered(world, pos, EnumFacing.SOUTH) || state.shouldSideBeRendered(world, pos, EnumFacing.EAST))
				skyBlocks.add(buffer -> renderStencil(buffer, te.getWorld(), te.getPos(), x, y, z));
	}

	void renderStencil(BufferBuilder buff, IBlockAccess world, BlockPos pos, double x, double y, double z){
		IBlockState state = world.getBlockState(pos);
		final double diff = (x*x + y*y + z*z)/325125+0.001f;
		final double min = -diff;
		final double max = 1 + diff;
		double minX = x + min;
		double minY = y + min;
		double minZ = z + min;
		double maxX = x + max;
		double maxY = y + max;
		double maxZ = z + max;
		if(state.shouldSideBeRendered(world, pos, EnumFacing.DOWN)){
			buff.pos(minX, minY, minZ).endVertex();
			buff.pos(maxX, minY, minZ).endVertex();
			buff.pos(maxX, minY, maxZ).endVertex();
			buff.pos(minX, minY, maxZ).endVertex();
		}
		if(state.shouldSideBeRendered(world, pos, EnumFacing.NORTH)){
			buff.pos(minX, minY, minZ).endVertex();
			buff.pos(minX, maxY, minZ).endVertex();
			buff.pos(maxX, maxY, minZ).endVertex();
			buff.pos(maxX, minY, minZ).endVertex();
		}
		if(state.shouldSideBeRendered(world, pos, EnumFacing.WEST)){
			buff.pos(minX, minY, minZ).endVertex();
			buff.pos(minX, minY, maxZ).endVertex();
			buff.pos(minX, maxY, maxZ).endVertex();
			buff.pos(minX, maxY, minZ).endVertex();
		}
		if(state.shouldSideBeRendered(world, pos, EnumFacing.UP)){
			buff.pos(minX, maxY, minZ).endVertex();
			buff.pos(minX, maxY, maxZ).endVertex();
			buff.pos(maxX, maxY, maxZ).endVertex();
			buff.pos(maxX, maxY, minZ).endVertex();
		}
		if(state.shouldSideBeRendered(world, pos, EnumFacing.SOUTH)){
			buff.pos(minX, minY, maxZ).endVertex();
			buff.pos(maxX, minY, maxZ).endVertex();
			buff.pos(maxX, maxY, maxZ).endVertex();
			buff.pos(minX, maxY, maxZ).endVertex();
		}
		if(state.shouldSideBeRendered(world, pos, EnumFacing.EAST)){
			buff.pos(maxX, minY, minZ).endVertex();
			buff.pos(maxX, maxY, minZ).endVertex();
			buff.pos(maxX, maxY, maxZ).endVertex();
			buff.pos(maxX, minY, maxZ).endVertex();
		}
	}

	@SubscribeEvent
	public void renderLast(RenderWorldLastEvent event){
		if(!skyBlocks.isEmpty()) WTWRenderer.Phase.STENCIL.render(() -> renderStencil(event.getPartialTicks()), () -> renderSky(event.getPartialTicks()));
	}

	private void renderStencil(float partialTicks){
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buffer = tess.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		while(skyBlocks.peek() != null) skyBlocks.poll().accept(buffer);
		tess.draw();
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}

	private void renderSky(float partialTicks){
		Minecraft mc = Minecraft.getMinecraft();
		EntityRenderer renderer = mc.entityRenderer;
		AClass<EntityRenderer> entityRenderer = new AClass<>(EntityRenderer.class);
		float fov = entityRenderer.<Float> getDeclaredMethod(new String[]{"getFOVModifier", "func_78481_a"}, float.class, boolean.class).setAccessible(true).invoke(renderer, partialTicks, true);

		GlStateManager.pushMatrix();

		new AClass<>(EntityRenderer.class).getDeclaredMethod(new String[]{"setupFog", "func_78468_a"}, int.class, float.class).setAccessible(true).invoke(renderer, -1, partialTicks);

		{
			GlStateManager.pushMatrix();
			GlStateManager.disableDepth();
			GlStateManager.depthFunc(GL11.GL_ALWAYS);
			renderer.setupOverlayRendering();

			GlStateManager.disableTexture2D();
			float red = entityRenderer.<Float> getDeclaredField("fogColorRed", "field_175080_Q").setAccessible(true).get(renderer);
			float green = entityRenderer.<Float> getDeclaredField("fogColorGreen", "field_175082_R").setAccessible(true).get(renderer);
			float blue = entityRenderer.<Float> getDeclaredField("fogColorBlue", "field_175081_S").setAccessible(true).get(renderer);
			Tessellator tess = new Tessellator(20);
			BufferBuilder buffer = tess.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			buffer.pos(0, 0, 1).color(red, green, blue, 0.5f).endVertex();
			buffer.pos(0, mc.displayHeight, 1).color(red, green, blue, 0.5f).endVertex();
			buffer.pos(mc.displayWidth, mc.displayHeight, 1).color(red, green, blue, 0.5f).endVertex();
			buffer.pos(mc.displayWidth, 0, 1).color(red, green, blue, 0.5f).endVertex();
			tess.draw();
			GlStateManager.enableTexture2D();

			GlStateManager.depthFunc(GL11.GL_LESS);
			GlStateManager.popMatrix();
		}

		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.loadIdentity();
		Project.gluPerspective(fov, (float) mc.displayWidth / (float) mc.displayHeight, 0.05F, mc.gameSettings.renderDistanceChunks * 16 * 4.0F);
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);

		EntityPlayer player = mc.player;
		double prevPosY = player.prevPosY;
		double posY = player.posY;
		player.prevPosY = 256;
		player.posY = 256;

		GlStateManager.color(0, 0, 0);
		mc.renderGlobal.renderSky(partialTicks, 2);

		player.prevPosY = prevPosY;
		player.posY = posY;

		GlStateManager.disableFog();
		GlStateManager.depthFunc(GL11.GL_LEQUAL);

		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.loadIdentity();
		Project.gluPerspective(fov, (float) mc.displayWidth / (float) mc.displayHeight, 0.05F, mc.gameSettings.renderDistanceChunks * 16 * MathHelper.SQRT_2);
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);

		GlStateManager.popMatrix();

		GlStateManager.enableDepth();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	}

}
