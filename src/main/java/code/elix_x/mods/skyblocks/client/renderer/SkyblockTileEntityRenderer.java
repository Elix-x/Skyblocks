package code.elix_x.mods.skyblocks.client.renderer;

import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import code.elix_x.excomms.reflection.ReflectionHelper.AClass;
import code.elix_x.excore.utils.client.render.wtw.WTWRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SkyblockTileEntityRenderer extends TileEntitySpecialRenderer {

	public SkyblockTileEntityRenderer(){
		MinecraftForge.EVENT_BUS.register(this);
	}

	private Queue<Runnable> skyBlocks = new LinkedList<Runnable>();

	@SubscribeEvent
	public void renderLast(RenderWorldLastEvent event){
		if(!skyBlocks.isEmpty()) WTWRenderer.render(() -> {
			while(!skyBlocks.isEmpty())
				skyBlocks.poll().run();
		}, () -> renderSky(event.getPartialTicks()));
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage){
		skyBlocks.add(() -> renderStencil(x, y, z));
	}

	void renderStencil(double x, double y, double z){
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		Tessellator tess = Tessellator.getInstance();
		VertexBuffer buff = tess.getBuffer();
		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

		buff.pos(0, 0, 0).endVertex();
		buff.pos(1, 0, 0).endVertex();
		buff.pos(1, 0, 1).endVertex();
		buff.pos(0, 0, 1).endVertex();

		buff.pos(0, 0, 0).endVertex();
		buff.pos(0, 1, 0).endVertex();
		buff.pos(1, 1, 0).endVertex();
		buff.pos(1, 0, 0).endVertex();

		buff.pos(0, 0, 0).endVertex();
		buff.pos(0, 0, 1).endVertex();
		buff.pos(0, 1, 1).endVertex();
		buff.pos(0, 1, 0).endVertex();

		buff.pos(0, 1, 0).endVertex();
		buff.pos(0, 1, 1).endVertex();
		buff.pos(1, 1, 1).endVertex();
		buff.pos(1, 1, 0).endVertex();

		buff.pos(0, 0, 1).endVertex();
		buff.pos(1, 0, 1).endVertex();
		buff.pos(1, 1, 1).endVertex();
		buff.pos(0, 1, 1).endVertex();

		buff.pos(1, 0, 0).endVertex();
		buff.pos(1, 1, 0).endVertex();
		buff.pos(1, 1, 1).endVertex();
		buff.pos(1, 0, 1).endVertex();

		tess.draw();
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}

	void renderSky(float partialTicks){
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
			VertexBuffer buffer = tess.getBuffer();
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
	}

}
