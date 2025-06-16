package com.yourname.slayeroverlay;

public class SlayerConfig {
    // Box dimensions
    public static float boxWidth = 4.0f;
    public static float boxHeight = 6.0f;
    public static float downwardExtension = 3.0f;
    
    // Box colors (RGB values 0-255)
    public static int fillColorR = 0;
    public static int fillColorG = 0;
    public static int fillColorB = 204; // Default dark blue
    public static int fillAlpha = 77; // Default 30% alpha (77/255 ≈ 0.3)
    
    public static int outlineColorR = 0;
    public static int outlineColorG = 51;
    public static int outlineColorB = 255; // Default bright blue
    public static int outlineAlpha = 230; // Default 90% alpha
    
    // Beam settings
    public static boolean enableBeam = true;
    public static float beamHeight = 100.0f;
    public static float beamWidth = 0.5f;
    public static int beamColorR = 0;
    public static int beamColorG = 128;
    public static int beamColorB = 255; // Default blue beam
    public static int beamAlpha = 204; // Default 80% alpha
    
    // Animation settings
    public static boolean enablePulse = true;
    public static float pulseSpeed = 0.01f;
    public static float pulseIntensity = 0.3f;
    
    // Outline settings
    public static float outlineWidth = 3.0f;
    
    // Helper methods to convert to OpenGL colors (0.0-1.0)
    public static float getFillColorR() { return fillColorR / 255.0f; }
    public static float getFillColorG() { return fillColorG / 255.0f; }
    public static float getFillColorB() { return fillColorB / 255.0f; }
    public static float getFillAlpha() { return fillAlpha / 255.0f; }
    
    public static float getOutlineColorR() { return outlineColorR / 255.0f; }
    public static float getOutlineColorG() { return outlineColorG / 255.0f; }
    public static float getOutlineColorB() { return outlineColorB / 255.0f; }
    public static float getOutlineAlpha() { return outlineAlpha / 255.0f; }
    
    public static float getBeamColorR() { return beamColorR / 255.0f; }
    public static float getBeamColorG() { return beamColorG / 255.0f; }
    public static float getBeamColorB() { return beamColorB / 255.0f; }
    public static float getBeamAlpha() { return beamAlpha / 255.0f; }
    
    // Save/Load methods (basic implementation)
    public static void saveConfig() {
        // This could be expanded to save to a file
        System.out.println("[SlayerOverlay] Configuration saved!");
    }
    
    public static void loadConfig() {
        // This could be expanded to load from a file
        System.out.println("[SlayerOverlay] Configuration loaded!");
    }
    
    public static void resetToDefaults() {
        boxWidth = 2.0f;
        boxHeight = 3.0f;
        downwardExtension = 4.0f;
        
        fillColorR = 0;
        fillColorG = 0;
        fillColorB = 204;
        fillAlpha = 90;
        
        outlineColorR = 0;
        outlineColorG = 51;
        outlineColorB = 255;
        outlineAlpha = 230;
        
        enableBeam = true;
        beamHeight = 100.0f;
        beamWidth = 0.5f;
        beamColorR = 0;
        beamColorG = 128;
        beamColorB = 255;
        beamAlpha = 204;
        
        enablePulse = true;
        pulseSpeed = 0.01f;
        pulseIntensity = 0.3f;
        outlineWidth = 3.0f;
    }
}