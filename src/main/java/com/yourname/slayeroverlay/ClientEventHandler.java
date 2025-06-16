package com.yourname.slayeroverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.HashSet;
import java.util.Set;

public class ClientEventHandler {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Set<Integer> seenEntities = new HashSet<Integer>();
    private boolean hasNotifiedLoad = false;
    private int tickCounter = 0;
    public static boolean isSlayerDetectionEnabled = false; // Start disabled

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.toggleSlayerDetection.isPressed()) {
            isSlayerDetectionEnabled = !isSlayerDetectionEnabled;
            
            if (mc.thePlayer != null) {
                if (isSlayerDetectionEnabled) {
                    mc.thePlayer.addChatMessage(
                        new ChatComponentText("§a[SlayerOverlay] §eSlayer detection §aENABLED§e! Press " + 
                            KeyBindings.toggleSlayerDetection.getKeyDescription() + " to toggle.")
                    );
                    // Clear seen entities when enabling to detect all slayers fresh
                    seenEntities.clear();
                } else {
                    mc.thePlayer.addChatMessage(
                        new ChatComponentText("§a[SlayerOverlay] §eSlayer detection §cDISABLED§e! Press " + 
                            KeyBindings.toggleSlayerDetection.getKeyDescription() + " to toggle.")
                    );
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || mc.theWorld == null || mc.thePlayer == null) {
            return;
        }

        // Show initial notification only once when we have a valid player
        if (!hasNotifiedLoad) {
            mc.thePlayer.addChatMessage(
                new ChatComponentText("§a[SlayerOverlay] Mod loaded! Press §e" + 
                    KeyBindings.toggleSlayerDetection.getKeyDescription() + "§a to toggle slayer detection.")
            );
            hasNotifiedLoad = true;
        }

        // Only process if slayer detection is enabled
        if (!isSlayerDetectionEnabled) {
            return;
        }

        // Only run every 10 ticks (0.5 seconds) instead of every tick
        tickCounter++;
        if (tickCounter < 10) {
            return;
        }
        tickCounter = 0;

        // Clear seen entities when changing worlds
        if (mc.theWorld.loadedEntityList.isEmpty()) {
            seenEntities.clear();
            return;
        }

        String playerName = mc.thePlayer.getName();

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == null) continue;
            
            int id = entity.getEntityId();
            if (seenEntities.contains(id)) {
                continue;
            }

            String nameTag = getEntityNameTag(entity);

            // Only process entities with names that might be slayers
            if (nameTag == null || nameTag.isEmpty()) {
                continue;
            }

            // Check if this is a slayer and if it's ours - NOW MORE STRICT
            if (isSlayer(nameTag)) {
                seenEntities.add(id);
                
                if (isOurSlayer(nameTag, playerName)) {
                    // This is OUR slayer - send special message
                    mc.thePlayer.addChatMessage(
                        new ChatComponentText(
                            String.format("§a[SlayerOverlay] §6YOUR SLAYER DETECTED: §f%s §c(NOW HIGHLIGHTED!)", nameTag)
                        )
                    );
                } else {
                    // This is someone else's slayer - send different message
                    mc.thePlayer.addChatMessage(
                        new ChatComponentText(
                            String.format("§7[SlayerOverlay] §eOther player's slayer: §f%s", nameTag)
                        )
                    );
                }
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
}