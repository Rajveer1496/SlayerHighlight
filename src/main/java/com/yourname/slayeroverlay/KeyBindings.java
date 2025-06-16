package com.yourname.slayeroverlay;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
    public static KeyBinding toggleSlayerDetection;
    
    public static void init() {
        toggleSlayerDetection = new KeyBinding(
            "Toggle Slayer Detection", 
            Keyboard.KEY_H, // Default key H
            "Slayer Overlay"
        );
        
        ClientRegistry.registerKeyBinding(toggleSlayerDetection);
    }
}