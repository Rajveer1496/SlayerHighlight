package com.yourname.slayeroverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ConfigCommand extends CommandBase {
    
    @Override
    public String getCommandName() {
        return "slayerconfig";
    }
    
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/slayerconfig <setting> <value> - Configure slayer overlay settings";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0; // No permission required
    }
    
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
    
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            showHelp(sender);
            return;
        }
        
        String setting = args[0].toLowerCase();
        
        try {
            switch (setting) {
                case "help":
                    showHelp(sender);
                    break;
                case "status":
                    showStatus(sender);
                    break;
                case "reset":
                    resetConfig(sender);
                    break;
                case "boxwidth":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig boxwidth <value>"));
                        return;
                    }
                    setBoxWidth(sender, args[1]);
                    break;
                case "boxheight":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig boxheight <value>"));
                        return;
                    }
                    setBoxHeight(sender, args[1]);
                    break;
                case "downwardextension":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig downwardextension <value>"));
                        return;
                    }
                    setDownwardExtension(sender, args[1]);
                    break;
                case "fillcolor":
                    if (args.length < 4) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig fillcolor <r> <g> <b>"));
                        return;
                    }
                    setFillColor(sender, args[1], args[2], args[3]);
                    break;
                case "outlinecolor":
                    if (args.length < 4) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig outlinecolor <r> <g> <b>"));
                        return;
                    }
                    setOutlineColor(sender, args[1], args[2], args[3]);
                    break;
                case "beamcolor":
                    if (args.length < 4) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig beamcolor <r> <g> <b>"));
                        return;
                    }
                    setBeamColor(sender, args[1], args[2], args[3]);
                    break;
                case "fillalpha":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig fillalpha <0-255>"));
                        return;
                    }
                    setFillAlpha(sender, args[1]);
                    break;
                case "outlinealpha":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig outlinealpha <0-255>"));
                        return;
                    }
                    setOutlineAlpha(sender, args[1]);
                    break;
                case "beamalpha":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig beamalpha <0-255>"));
                        return;
                    }
                    setBeamAlpha(sender, args[1]);
                    break;
                case "beam":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig beam <true/false>"));
                        return;
                    }
                    toggleBeam(sender, args[1]);
                    break;
                case "beamheight":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig beamheight <value>"));
                        return;
                    }
                    setBeamHeight(sender, args[1]);
                    break;
                case "beamwidth":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig beamwidth <value>"));
                        return;
                    }
                    setBeamWidth(sender, args[1]);
                    break;
                case "pulse":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig pulse <true/false>"));
                        return;
                    }
                    togglePulse(sender, args[1]);
                    break;
                case "pulsespeed":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig pulsespeed <value>"));
                        return;
                    }
                    setPulseSpeed(sender, args[1]);
                    break;
                case "pulseintensity":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig pulseintensity <value>"));
                        return;
                    }
                    setPulseIntensity(sender, args[1]);
                    break;
                case "outlinewidth":
                    if (args.length < 2) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Usage: /slayerconfig outlinewidth <value>"));
                        return;
                    }
                    setOutlineWidth(sender, args[1]);
                    break;
                default:
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Unknown setting: " + setting));
                    showHelp(sender);
                    break;
            }
        } catch (Exception e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Error: " + e.getMessage()));
        }
    }
    
    private void showHelp(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "=== Slayer Overlay Configuration ==="));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "/slayerconfig help - Show this help"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "/slayerconfig status - Show current settings"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "/slayerconfig reset - Reset to defaults"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Box Settings:"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  boxwidth <value> - Set box width"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  boxheight <value> - Set box height"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  downwardextension <value> - Set downward extension"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Color Settings (RGB 0-255):"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  fillcolor <r> <g> <b> - Set fill color"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  outlinecolor <r> <g> <b> - Set outline color"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  beamcolor <r> <g> <b> - Set beam color"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Alpha Settings (0-255):"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  fillalpha <value> - Set fill transparency"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  outlinealpha <value> - Set outline transparency"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  beamalpha <value> - Set beam transparency"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Beam Settings:"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  beam <true/false> - Enable/disable beam"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  beamheight <value> - Set beam height"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  beamwidth <value> - Set beam width"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Animation Settings:"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  pulse <true/false> - Enable/disable pulsing"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  pulsespeed <value> - Set pulse speed"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  pulseintensity <value> - Set pulse intensity"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "  outlinewidth <value> - Set outline width"));
    }
    
    private void showStatus(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "=== Current Slayer Overlay Settings ==="));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Box: " + SlayerConfig.boxWidth + "x" + SlayerConfig.boxHeight + " (down: " + SlayerConfig.downwardExtension + ")"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Fill Color: RGB(" + SlayerConfig.fillColorR + "," + SlayerConfig.fillColorG + "," + SlayerConfig.fillColorB + ") Alpha: " + SlayerConfig.fillAlpha));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Outline Color: RGB(" + SlayerConfig.outlineColorR + "," + SlayerConfig.outlineColorG + "," + SlayerConfig.outlineColorB + ") Alpha: " + SlayerConfig.outlineAlpha));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Outline Width: " + SlayerConfig.outlineWidth));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Beam: " + (SlayerConfig.enableBeam ? "Enabled" : "Disabled") + " (" + SlayerConfig.beamWidth + "x" + SlayerConfig.beamHeight + ")"));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Beam Color: RGB(" + SlayerConfig.beamColorR + "," + SlayerConfig.beamColorG + "," + SlayerConfig.beamColorB + ") Alpha: " + SlayerConfig.beamAlpha));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Pulse: " + (SlayerConfig.enablePulse ? "Enabled" : "Disabled") + " (Speed: " + SlayerConfig.pulseSpeed + ", Intensity: " + SlayerConfig.pulseIntensity + ")"));
    }
    
    private void resetConfig(ICommandSender sender) {
        SlayerConfig.resetToDefaults();
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Configuration reset to defaults!"));
    }
    
    private void setBoxWidth(ICommandSender sender, String value) {
        try {
            float width = Float.parseFloat(value);
            if (width <= 0) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Box width must be positive!"));
                return;
            }
            SlayerConfig.boxWidth = width;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Box width set to " + width));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid number: " + value));
        }
    }
    
    private void setBoxHeight(ICommandSender sender, String value) {
        try {
            float height = Float.parseFloat(value);
            if (height <= 0) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Box height must be positive!"));
                return;
            }
            SlayerConfig.boxHeight = height;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Box height set to " + height));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid number: " + value));
        }
    }
    
    private void setDownwardExtension(ICommandSender sender, String value) {
        try {
            float extension = Float.parseFloat(value);
            if (extension < 0) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Downward extension cannot be negative!"));
                return;
            }
            SlayerConfig.downwardExtension = extension;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Downward extension set to " + extension));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid number: " + value));
        }
    }
    
    private void setFillColor(ICommandSender sender, String r, String g, String b) {
        try {
            int red = Integer.parseInt(r);
            int green = Integer.parseInt(g);
            int blue = Integer.parseInt(b);
            
            if (!isValidColorValue(red) || !isValidColorValue(green) || !isValidColorValue(blue)) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Color values must be between 0-255!"));
                return;
            }
            
            SlayerConfig.fillColorR = red;
            SlayerConfig.fillColorG = green;
            SlayerConfig.fillColorB = blue;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Fill color set to RGB(" + red + "," + green + "," + blue + ")"));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid color values!"));
        }
    }
    
    private void setOutlineColor(ICommandSender sender, String r, String g, String b) {
        try {
            int red = Integer.parseInt(r);
            int green = Integer.parseInt(g);
            int blue = Integer.parseInt(b);
            
            if (!isValidColorValue(red) || !isValidColorValue(green) || !isValidColorValue(blue)) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Color values must be between 0-255!"));
                return;
            }
            
            SlayerConfig.outlineColorR = red;
            SlayerConfig.outlineColorG = green;
            SlayerConfig.outlineColorB = blue;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Outline color set to RGB(" + red + "," + green + "," + blue + ")"));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid color values!"));
        }
    }
    
    private void setBeamColor(ICommandSender sender, String r, String g, String b) {
        try {
            int red = Integer.parseInt(r);
            int green = Integer.parseInt(g);
            int blue = Integer.parseInt(b);
            
            if (!isValidColorValue(red) || !isValidColorValue(green) || !isValidColorValue(blue)) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Color values must be between 0-255!"));
                return;
            }
            
            SlayerConfig.beamColorR = red;
            SlayerConfig.beamColorG = green;
            SlayerConfig.beamColorB = blue;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Beam color set to RGB(" + red + "," + green + "," + blue + ")"));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid color values!"));
        }
    }
    
    private void setFillAlpha(ICommandSender sender, String value) {
        try {
            int alpha = Integer.parseInt(value);
            if (!isValidColorValue(alpha)) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Alpha must be between 0-255!"));
                return;
            }
            SlayerConfig.fillAlpha = alpha;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Fill alpha set to " + alpha));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid number: " + value));
        }
    }
    
    private void setOutlineAlpha(ICommandSender sender, String value) {
        try {
            int alpha = Integer.parseInt(value);
            if (!isValidColorValue(alpha)) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Alpha must be between 0-255!"));
                return;
            }
            SlayerConfig.outlineAlpha = alpha;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Outline alpha set to " + alpha));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid number: " + value));
        }
    }
    
    private void setBeamAlpha(ICommandSender sender, String value) {
        try {
            int alpha = Integer.parseInt(value);
            if (!isValidColorValue(alpha)) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Alpha must be between 0-255!"));
                return;
            }
            SlayerConfig.beamAlpha = alpha;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Beam alpha set to " + alpha));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid number: " + value));
        }
    }
    
    private void toggleBeam(ICommandSender sender, String value) {
        boolean enable = value.toLowerCase().equals("true") || value.toLowerCase().equals("on") || value.equals("1");
        SlayerConfig.enableBeam = enable;
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Beam " + (enable ? "enabled" : "disabled")));
    }
    
    private void setBeamHeight(ICommandSender sender, String value) {
        try {
            float height = Float.parseFloat(value);
            if (height <= 0) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Beam height must be positive!"));
                return;
            }
            SlayerConfig.beamHeight = height;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Beam height set to " + height));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid number: " + value));
        }
    }
    
    private void setBeamWidth(ICommandSender sender, String value) {
        try {
            float width = Float.parseFloat(value);
            if (width <= 0) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Beam width must be positive!"));
                return;
            }
            SlayerConfig.beamWidth = width;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Beam width set to " + width));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid number: " + value));
        }
    }
    
    private void togglePulse(ICommandSender sender, String value) {
        boolean enable = value.toLowerCase().equals("true") || value.toLowerCase().equals("on") || value.equals("1");
        SlayerConfig.enablePulse = enable;
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Pulse " + (enable ? "enabled" : "disabled")));
    }
    
    private void setPulseSpeed(ICommandSender sender, String value) {
        try {
            float speed = Float.parseFloat(value);
            if (speed <= 0) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Pulse speed must be positive!"));
                return;
            }
            SlayerConfig.pulseSpeed = speed;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Pulse speed set to " + speed));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid number: " + value));
        }
    }
    
    private void setPulseIntensity(ICommandSender sender, String value) {
        try {
            float intensity = Float.parseFloat(value);
            if (intensity < 0 || intensity > 1) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Pulse intensity must be between 0-1!"));
                return;
            }
            SlayerConfig.pulseIntensity = intensity;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Pulse intensity set to " + intensity));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid number: " + value));
        }
    }
    
    private void setOutlineWidth(ICommandSender sender, String value) {
        try {
            float width = Float.parseFloat(value);
            if (width <= 0) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Outline width must be positive!"));
                return;
            }
            SlayerConfig.outlineWidth = width;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[SlayerOverlay] Outline width set to " + width));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[SlayerOverlay] Invalid number: " + value));
        }
    }
    
    private boolean isValidColorValue(int value) {
        return value >= 0 && value <= 255;
    }
}