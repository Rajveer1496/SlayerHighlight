package com.yourname.slayeroverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.BlockPos;

import java.util.Arrays;
import java.util.List;

public class DebugCommand extends CommandBase {
    private final Minecraft mc = Minecraft.getMinecraft();
    
    @Override
    public String getCommandName() {
        return "slayerdebug";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/slayerdebug <list|coords|filter>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // No permission required
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return Arrays.asList("list", "coords", "filter");
        }
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("§cUsage: /slayerdebug <list|coords|filter>"));
            sender.addChatMessage(new ChatComponentText("§7- list: Show all entities"));
            sender.addChatMessage(new ChatComponentText("§7- coords: Show entities with coordinates"));
            sender.addChatMessage(new ChatComponentText("§7- filter: Show only entities with custom names"));
            return;
        }

        if (mc.theWorld == null) {
            sender.addChatMessage(new ChatComponentText("§cNo world loaded!"));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                listAllEntities();
                break;
            case "coords":
                listEntitiesWithCoords();
                break;
            case "filter":
                listFilteredEntities();
                break;
            default:
                sender.addChatMessage(new ChatComponentText("§cUnknown subcommand: " + args[0]));
                sender.addChatMessage(new ChatComponentText("§7Available: list, coords, filter"));
                break;
        }
    }

    private void listFilteredEntities() {
        mc.thePlayer.addChatMessage(new ChatComponentText("§6[SlayerDebug] Filtered entities (custom names only):"));
        
        // Common entity names to filter out
        String[] commonNames = {"Zombie", "Skeleton", "Creeper", "Spider", "Enderman", 
                               "Witch", "Cow", "Pig", "Sheep", "Chicken", "Villager",
                               "Item", "XPOrb", "Arrow", "Boat", "Minecart"};
        
        int count = 0;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == null || entity == mc.thePlayer) continue;
            
            String nameTag = null;
            String entityType = entity.getClass().getSimpleName();
            
            // Get name the same way as in the main detection
            if (entity.hasCustomName()) {
                nameTag = entity.getCustomNameTag();
            }
            
            if (nameTag == null || nameTag.isEmpty()) {
                if (entity instanceof EntityLivingBase) {
                    nameTag = ((EntityLivingBase) entity).getDisplayName().getUnformattedText();
                } else {
                    nameTag = entity.getName();
                }
            }
            
            // Skip if no name or if name is in common names list
            if (nameTag == null || nameTag.isEmpty()) continue;
            
            boolean isCommon = false;
            for (String commonName : commonNames) {
                if (nameTag.equals(commonName)) {
                    isCommon = true;
                    break;
                }
            }
            
            if (isCommon) continue;
            
            // Only show entities with custom names or interesting names
            if (entity.hasCustomName() || nameTag.contains("Slayer") || nameTag.contains("Spawned") || 
                nameTag.contains("❤") || nameTag.contains("♥") || nameTag.length() > 15) {
                
                double distance = entity.getDistanceToEntity(mc.thePlayer);
                String message = String.format("§7- §f%s §7(Name: §e%s§7) Distance: §a%.1f", 
                    entityType, nameTag, distance);
                
                mc.thePlayer.addChatMessage(new ChatComponentText(message));
                count++;
                
                // Limit output to prevent spam
                if (count >= 20) {
                    mc.thePlayer.addChatMessage(new ChatComponentText("§7... and more (showing first 20)"));
                    break;
                }
            }
        }
        
        if (count == 0) {
            mc.thePlayer.addChatMessage(new ChatComponentText("§7No interesting entities found."));
        }
    }

    private void listEntitiesWithCoords() {
        mc.thePlayer.addChatMessage(new ChatComponentText("§6[SlayerDebug] Entities with coordinates:"));
        
        int count = 0;
        double playerX = mc.thePlayer.posX;
        double playerY = mc.thePlayer.posY;
        double playerZ = mc.thePlayer.posZ;
        
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == null || entity == mc.thePlayer) continue;
            
            String nameTag = null;
            String entityType = entity.getClass().getSimpleName();
            
            // Get name the same way as in the main detection
            if (entity.hasCustomName()) {
                nameTag = entity.getCustomNameTag();
            }
            
            if (nameTag == null || nameTag.isEmpty()) {
                if (entity instanceof EntityLivingBase) {
                    nameTag = ((EntityLivingBase) entity).getDisplayName().getUnformattedText();
                } else {
                    nameTag = entity.getName();
                }
            }
            
            double distance = entity.getDistanceToEntity(mc.thePlayer);
            
            String message = String.format("§7- §f%s §7(§e%s§7) Distance: §a%.1f§7 Pos: §b%.1f,%.1f,%.1f", 
                entityType, nameTag != null ? nameTag : "No Name", distance, entity.posX, entity.posY, entity.posZ);
            
            mc.thePlayer.addChatMessage(new ChatComponentText(message));
            count++;
            
            // Limit output to prevent spam
            if (count >= 15) {
                mc.thePlayer.addChatMessage(new ChatComponentText("§7... and more (showing closest 15)"));
                break;
            }
        }
        
        if (count == 0) {
            mc.thePlayer.addChatMessage(new ChatComponentText("§7No entities found."));
        }
    }

    private void listAllEntities() {
        mc.thePlayer.addChatMessage(new ChatComponentText("§6[SlayerDebug] Listing all entities:"));
        
        int count = 0;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == null || entity == mc.thePlayer) continue;
            
            String nameTag = null;
            String entityType = entity.getClass().getSimpleName();
            
            // Get name the same way as in the main detection
            if (entity.hasCustomName()) {
                nameTag = entity.getCustomNameTag();
            }
            
            if (nameTag == null || nameTag.isEmpty()) {
                if (entity instanceof EntityLivingBase) {
                    nameTag = ((EntityLivingBase) entity).getDisplayName().getUnformattedText();
                } else {
                    nameTag = entity.getName();
                }
            }
            
            String message = String.format("§7- §f%s §7(ID: %d, Name: §e%s§7)", 
                entityType, entity.getEntityId(), nameTag != null ? nameTag : "No Name");
            
            mc.thePlayer.addChatMessage(new ChatComponentText(message));
            count++;
            
            // Limit output to prevent spam
            if (count >= 20) {
                mc.thePlayer.addChatMessage(new ChatComponentText("§7... and more (showing first 20)"));
                break;
            }
        }
        
        if (count == 0) {
            mc.thePlayer.addChatMessage(new ChatComponentText("§7No entities found."));
        }
    }
}