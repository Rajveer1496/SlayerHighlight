package com.yourname.slayeroverlay;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import java.io.IOException;

public class SlayerConfigGUI extends GuiScreen {
    private GuiTextField boxWidthField;
    private GuiTextField boxHeightField;
    private GuiButton saveButton;
    private GuiButton closeButton;
    
    public SlayerConfigGUI() {
        System.out.println("[SlayerOverlay] SlayerConfigGUI constructor called");
    }
    
    @Override
    public void initGui() {
        System.out.println("[SlayerOverlay] initGui() called - Screen size: " + this.width + "x" + this.height);
        
        try {
            Keyboard.enableRepeatEvents(true);
            
            int centerX = this.width / 2;
            int centerY = this.height / 2;
            
            // Create simple text fields for testing
            boxWidthField = new GuiTextField(0, this.fontRendererObj, centerX - 50, centerY - 40, 100, 20);
            boxHeightField = new GuiTextField(1, this.fontRendererObj, centerX - 50, centerY - 10, 100, 20);
            
            // Set initial values
            boxWidthField.setText(String.valueOf(SlayerConfig.boxWidth));
            boxHeightField.setText(String.valueOf(SlayerConfig.boxHeight));
            
            // Make fields focusable
            boxWidthField.setFocused(false);
            boxHeightField.setFocused(false);
            
            // Create buttons
            saveButton = new GuiButton(1, centerX - 60, centerY + 20, 50, 20, "Save");
            closeButton = new GuiButton(2, centerX + 10, centerY + 20, 50, 20, "Close");
            
            this.buttonList.add(saveButton);
            this.buttonList.add(closeButton);
            
            System.out.println("[SlayerOverlay] GUI initialized successfully with " + this.buttonList.size() + " buttons");
            
        } catch (Exception e) {
            System.out.println("[SlayerOverlay] Exception in initGui:");
            e.printStackTrace();
        }
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        System.out.println("[SlayerOverlay] Button clicked: " + button.id + " - " + button.displayString);
        
        try {
            if (button.id == 1) { // Save
                saveConfig();
                SlayerConfig.saveConfig();
                System.out.println("[SlayerOverlay] Configuration saved");
            } else if (button.id == 2) { // Close
                System.out.println("[SlayerOverlay] Closing GUI");
                this.mc.displayGuiScreen(null);
            }
        } catch (Exception e) {
            System.out.println("[SlayerOverlay] Exception in actionPerformed:");
            e.printStackTrace();
        }
    }
    
    private void saveConfig() {
        try {
            String widthText = boxWidthField.getText();
            String heightText = boxHeightField.getText();
            
            if (!widthText.isEmpty()) {
                SlayerConfig.boxWidth = Float.parseFloat(widthText);
            }
            if (!heightText.isEmpty()) {
                SlayerConfig.boxHeight = Float.parseFloat(heightText);
            }
            
            System.out.println("[SlayerOverlay] Config saved - Width: " + SlayerConfig.boxWidth + ", Height: " + SlayerConfig.boxHeight);
            
        } catch (NumberFormatException e) {
            System.out.println("[SlayerOverlay] Invalid number format in config!");
        }
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        try {
            super.keyTyped(typedChar, keyCode);
            
            if (boxWidthField != null) {
                boxWidthField.textboxKeyTyped(typedChar, keyCode);
            }
            if (boxHeightField != null) {
                boxHeightField.textboxKeyTyped(typedChar, keyCode);
            }
            
        } catch (Exception e) {
            System.out.println("[SlayerOverlay] Exception in keyTyped:");
            e.printStackTrace();
        }
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            
            if (boxWidthField != null) {
                boxWidthField.mouseClicked(mouseX, mouseY, mouseButton);
            }
            if (boxHeightField != null) {
                boxHeightField.mouseClicked(mouseX, mouseY, mouseButton);
            }
            
        } catch (Exception e) {
            System.out.println("[SlayerOverlay] Exception in mouseClicked:");
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        try {
            // Draw background
            this.drawDefaultBackground();
            
            // Draw title
            String title = "Slayer Overlay Configuration (Debug)";
            this.drawCenteredString(this.fontRendererObj, title, this.width / 2, this.height / 2 - 70, 0xFFFFFF);
            
            // Draw labels
            int centerX = this.width / 2;
            int centerY = this.height / 2;
            
            this.drawString(this.fontRendererObj, "Box Width:", centerX - 100, centerY - 35, 0xFFFFFF);
            this.drawString(this.fontRendererObj, "Box Height:", centerX - 100, centerY - 5, 0xFFFFFF);
            
            // Draw text fields
            if (boxWidthField != null) {
                boxWidthField.drawTextBox();
            }
            if (boxHeightField != null) {
                boxHeightField.drawTextBox();
            }
            
            // Draw buttons
            super.drawScreen(mouseX, mouseY, partialTicks);
            
        } catch (Exception e) {
            System.out.println("[SlayerOverlay] Exception in drawScreen:");
            e.printStackTrace();
        }
    }
    
    @Override
    public void updateScreen() {
        try {
            if (boxWidthField != null) {
                boxWidthField.updateCursorCounter();
            }
            if (boxHeightField != null) {
                boxHeightField.updateCursorCounter();
            }
        } catch (Exception e) {
            System.out.println("[SlayerOverlay] Exception in updateScreen:");
            e.printStackTrace();
        }
    }
    
    @Override
    public void onGuiClosed() {
        System.out.println("[SlayerOverlay] GUI closed");
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}