package com.yourname.slayeroverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class RenderEventHandler {
    private final Minecraft mc = Minecraft.getMinecraft();
    
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) {
            return;
        }
        
        // Only render if slayer detection is enabled
        if (!ClientEventHandler.isSlayerDetectionEnabled) {
            return;
        }
        
        String playerName = mc.thePlayer.getName();
        
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == null || entity == mc.thePlayer) continue;
            
            String nameTag = getEntityNameTag(entity);
            if (nameTag == null) continue;
            
            // Check if this is OUR slayer - NOW MORE STRICT
            if (isSlayer(nameTag) && isOurSlayer(nameTag, playerName)) {
                renderSlayerHighlight(entity, event.partialTicks);
            }
        }
    }
    
    private String getEntityNameTag(Entity entity) {
        String nameTag = null;
        
        // Check custom name tag first
        if (entity.hasCustomName()) {
            nameTag = entity.getCustomNameTag();
        }
        
        // If no custom name, try the entity's display name
        if (nameTag == null || nameTag.isEmpty()) {
            if (entity instanceof EntityLivingBase) {
                nameTag = ((EntityLivingBase) entity).getDisplayName().getUnformattedText();
            } else {
                nameTag = entity.getName();
            }
        }
        
        return nameTag;
    }
    
    private boolean isSlayer(String nameTag) {
        if (nameTag == null) {
            return false;
        }
        
        // Remove color codes from the name tag for better matching
        String cleanNameTag = nameTag.replaceAll("§[0-9a-fk-or]", "");
        
        // STRICT CHECK: Only entities that contain "Spawned by" are considered slayers
        return cleanNameTag.contains("Spawned by");
    }
    
    private boolean isOurSlayer(String nameTag, String playerName) {
        if (nameTag == null || playerName == null) {
            return false;
        }
        
        // Remove color codes from the name tag for better matching
        String cleanNameTag = nameTag.replaceAll("§[0-9a-fk-or]", "");
        
        // Check for various slayer name patterns that include our player name
        return cleanNameTag.contains("Spawned by " + playerName) ||
               cleanNameTag.contains("Spawned by: " + playerName) ||  // Added colon variant
               cleanNameTag.contains("Slayer Spawned by " + playerName) ||
               cleanNameTag.contains("Slayer Spawned by: " + playerName);  // Added colon variant
    }
    
    private void renderSlayerHighlight(Entity entity, float partialTicks) {
        // Get entity position relative to camera
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;
        
        // Setup OpenGL for rendering - IMPORTANT: Disable depth test so it renders through everything
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth(); // This makes it render through blocks and entities
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        // Use configuration values for box dimensions
        double boxWidth = SlayerConfig.boxWidth;
        double boxHeight = SlayerConfig.boxHeight;
        double downwardExtension = SlayerConfig.downwardExtension;
        
        // Pulsing effect (if enabled)
        float pulse = 1.0f;
        if (SlayerConfig.enablePulse) {
            pulse = (float) (Math.sin(System.currentTimeMillis() * SlayerConfig.pulseSpeed) 
                           * SlayerConfig.pulseIntensity + (1.0f - SlayerConfig.pulseIntensity));
        }
        
        // Render solid filled box using config colors
        renderSolidHighlightBox(boxWidth, boxHeight, downwardExtension, 
            SlayerConfig.getFillColorR(), SlayerConfig.getFillColorG(), SlayerConfig.getFillColorB(), 
            SlayerConfig.getFillAlpha() * pulse);
        
        // Render outline using config colors
        renderWireframeBox(boxWidth, boxHeight, downwardExtension, 
            SlayerConfig.getOutlineColorR(), SlayerConfig.getOutlineColorG(), SlayerConfig.getOutlineColorB(), 
            SlayerConfig.getOutlineAlpha());
        
        // Cleanup OpenGL
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        
        // Render waypoint beam (if enabled)
        if (SlayerConfig.enableBeam) {
            renderWaypointBeam(x, y, z, boxHeight, pulse);
        }
    }
    
    private void renderSolidHighlightBox(double width, double height, double downwardExtension, float r, float g, float b, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        double halfWidth = width / 2.0;
        double minY = -downwardExtension; // Extend downward from the name tag
        double maxY = height * 0.5; // Extend upward a bit
        
        // Set color
        GlStateManager.color(r, g, b, alpha);
        
        // Render solid box faces
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        
        // Bottom face
        worldRenderer.pos(-halfWidth, minY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, minY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, minY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, minY, halfWidth).endVertex();
        
        // Top face
        worldRenderer.pos(-halfWidth, maxY, -halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, maxY, halfWidth).endVertex();
        worldRenderer.pos(halfWidth, maxY, halfWidth).endVertex();
        worldRenderer.pos(halfWidth, maxY, -halfWidth).endVertex();
        
        // Front face
        worldRenderer.pos(-halfWidth, minY, -halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, maxY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, maxY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, minY, -halfWidth).endVertex();
        
        // Back face
        worldRenderer.pos(halfWidth, minY, halfWidth).endVertex();
        worldRenderer.pos(halfWidth, maxY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, maxY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, minY, halfWidth).endVertex();
        
        // Left face
        worldRenderer.pos(-halfWidth, minY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, maxY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, maxY, -halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, minY, -halfWidth).endVertex();
        
        // Right face
        worldRenderer.pos(halfWidth, minY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, maxY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, maxY, halfWidth).endVertex();
        worldRenderer.pos(halfWidth, minY, halfWidth).endVertex();
        
        tessellator.draw();
    }
    
    private void renderWireframeBox(double width, double height, double downwardExtension, float r, float g, float b, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        double halfWidth = width / 2.0;
        double minY = -downwardExtension;
        double maxY = height * 0.5;
        
        // Set color
        GlStateManager.color(r, g, b, alpha);
        
        // Render wireframe box with configurable line width
        GL11.glLineWidth(SlayerConfig.outlineWidth);
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        
        // Bottom face
        worldRenderer.pos(-halfWidth, minY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, minY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, minY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, minY, halfWidth).endVertex();
        worldRenderer.pos(halfWidth, minY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, minY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, minY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, minY, -halfWidth).endVertex();
        
        // Top face
        worldRenderer.pos(-halfWidth, maxY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, maxY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, maxY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, maxY, halfWidth).endVertex();
        worldRenderer.pos(halfWidth, maxY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, maxY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, maxY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, maxY, -halfWidth).endVertex();
        
        // Vertical edges
        worldRenderer.pos(-halfWidth, minY, -halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, maxY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, minY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, maxY, -halfWidth).endVertex();
        worldRenderer.pos(halfWidth, minY, halfWidth).endVertex();
        worldRenderer.pos(halfWidth, maxY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, minY, halfWidth).endVertex();
        worldRenderer.pos(-halfWidth, maxY, halfWidth).endVertex();
        
        tessellator.draw();
        GL11.glLineWidth(1.0f); // Reset line width
    }
    
    private void renderWaypointBeam(double x, double y, double z, double height, float pulse) {
        // Render a beam from the slayer to the sky for easy spotting
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        
        // Use config colors for beam
        float beamPulse = pulse;
        if (SlayerConfig.enablePulse) {
            beamPulse = (float) (Math.sin(System.currentTimeMillis() * (SlayerConfig.pulseSpeed * 1.5)) 
                               * SlayerConfig.pulseIntensity + (1.0f - SlayerConfig.pulseIntensity));
        }
        
        GlStateManager.color(SlayerConfig.getBeamColorR(), SlayerConfig.getBeamColorG(), 
                           SlayerConfig.getBeamColorB(), SlayerConfig.getBeamAlpha() * beamPulse);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        
        double beamHeight = SlayerConfig.beamHeight;
        double beamWidth = SlayerConfig.beamWidth;
        
        // Render 4 faces of the beam
        // Front face
        worldRenderer.pos(-beamWidth, height, -beamWidth).endVertex();
        worldRenderer.pos(beamWidth, height, -beamWidth).endVertex();
        worldRenderer.pos(beamWidth, height + beamHeight, -beamWidth).endVertex();
        worldRenderer.pos(-beamWidth, height + beamHeight, -beamWidth).endVertex();
        
        // Back face
        worldRenderer.pos(beamWidth, height, beamWidth).endVertex();
        worldRenderer.pos(-beamWidth, height, beamWidth).endVertex();
        worldRenderer.pos(-beamWidth, height + beamHeight, beamWidth).endVertex();
        worldRenderer.pos(beamWidth, height + beamHeight, beamWidth).endVertex();
        
        // Left face
        worldRenderer.pos(-beamWidth, height, beamWidth).endVertex();
        worldRenderer.pos(-beamWidth, height, -beamWidth).endVertex();
        worldRenderer.pos(-beamWidth, height + beamHeight, -beamWidth).endVertex();
        worldRenderer.pos(-beamWidth, height + beamHeight, beamWidth).endVertex();
        
        // Right face
        worldRenderer.pos(beamWidth, height, -beamWidth).endVertex();
        worldRenderer.pos(beamWidth, height, beamWidth).endVertex();
        worldRenderer.pos(beamWidth, height + beamHeight, beamWidth).endVertex();
        worldRenderer.pos(beamWidth, height + beamHeight, -beamWidth).endVertex();
        
        tessellator.draw();
        
        // Add a second, inner beam for extra visibility (if beam is thick enough)
        if (SlayerConfig.beamWidth > 0.3f) {
            // Make inner beam brighter
            GlStateManager.color(
                Math.min(1.0f, SlayerConfig.getBeamColorR() + 0.3f),
                Math.min(1.0f, SlayerConfig.getBeamColorG() + 0.3f),
                Math.min(1.0f, SlayerConfig.getBeamColorB() + 0.3f),
                SlayerConfig.getBeamAlpha() * beamPulse
            );
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            
            double innerBeamWidth = beamWidth * 0.4;
            
            // Inner beam faces
            worldRenderer.pos(-innerBeamWidth, height, -innerBeamWidth).endVertex();
            worldRenderer.pos(innerBeamWidth, height, -innerBeamWidth).endVertex();
            worldRenderer.pos(innerBeamWidth, height + beamHeight, -innerBeamWidth).endVertex();
            worldRenderer.pos(-innerBeamWidth, height + beamHeight, -innerBeamWidth).endVertex();
            
            worldRenderer.pos(innerBeamWidth, height, innerBeamWidth).endVertex();
            worldRenderer.pos(-innerBeamWidth, height, innerBeamWidth).endVertex();
            worldRenderer.pos(-innerBeamWidth, height + beamHeight, innerBeamWidth).endVertex();
            worldRenderer.pos(innerBeamWidth, height + beamHeight, innerBeamWidth).endVertex();
            
            worldRenderer.pos(-innerBeamWidth, height, innerBeamWidth).endVertex();
            worldRenderer.pos(-innerBeamWidth, height, -innerBeamWidth).endVertex();
            worldRenderer.pos(-innerBeamWidth, height + beamHeight, -innerBeamWidth).endVertex();
            worldRenderer.pos(-innerBeamWidth, height + beamHeight, innerBeamWidth).endVertex();
            
            worldRenderer.pos(innerBeamWidth, height, -innerBeamWidth).endVertex();
            worldRenderer.pos(innerBeamWidth, height, innerBeamWidth).endVertex();
            worldRenderer.pos(innerBeamWidth, height + beamHeight, innerBeamWidth).endVertex();
            worldRenderer.pos(innerBeamWidth, height + beamHeight, -innerBeamWidth).endVertex();
            
            tessellator.draw();
        }
    }
}