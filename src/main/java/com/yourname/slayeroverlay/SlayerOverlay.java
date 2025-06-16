package com.yourname.slayeroverlay;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.ClientCommandHandler;

@Mod(modid = "slayeroverlay", name = "Slayer Overlay", version = "1.0.0", clientSideOnly = true)
public class SlayerOverlay {
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Load configuration
        SlayerConfig.loadConfig();
        
        // Initialize keybindings (if you still have them)
        if (hasKeybindings()) {
            KeyBindings.init();
        }
        
        // Register event handlers
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
        
        // Register commands - only the config command now
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());
        
        // Register debug command if it exists
        if (hasDebugCommand()) {
            ClientCommandHandler.instance.registerCommand(new DebugCommand());
        }
        
        System.out.println("[SlayerOverlay] Mod initialized successfully!");
        System.out.println("[SlayerOverlay] Use '/slayerconfig help' for configuration options");
    }
    
    // Helper method to check if KeyBindings class exists
    private boolean hasKeybindings() {
        try {
            Class.forName("com.yourname.slayeroverlay.KeyBindings");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    // Helper method to check if DebugCommand class exists
    private boolean hasDebugCommand() {
        try {
            Class.forName("com.yourname.slayeroverlay.DebugCommand");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}