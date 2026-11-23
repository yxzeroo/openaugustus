package net.augustus.ui;

import net.augustus.ui.widgets.ScrollBar;
import net.augustus.utils.render.BlurShadowManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class AugustusGuiScreen extends GuiScreen {
   private final List passwordFields;
   private boolean renderDefaultBackground;
   private final List textFields;
   private GuiScreen parentScreen2;
   private int nextControlId;
   private final Map buttonClickHandlers = new HashMap();

   public static void disableScissor() {
      GL11.glDisable(3089);
   }

   public void navigateBack() {
      Minecraft mc2 = this.mc;
      GuiScreen parentScreen3 = this.getParentScreen();
      mc2.displayGuiScreen(parentScreen3);
   }

   public void drawCenteredString(String str, float f, float f2) {
      byte b = -1;
      this.drawCenteredString(str, f, f2, b);
   }

   public void disableDefaultBackground() {
      this.renderDefaultBackground = false;
   }

   @Override
   public void onResize(Minecraft mc2, int n, int n2) {
      super.onResize(mc2, n, n2);
   }

   private void onBackButtonClicked(GuiButton button) {
      this.navigateBack();
   }

   public AugustusGuiScreen() {
      this.textFields = new ArrayList();
      this.passwordFields = new ArrayList();
      this.nextControlId = 0;
      this.renderDefaultBackground = true;
   }

   public AugustusGuiScreen(GuiScreen screen) {
      super(screen);
      this.textFields = new ArrayList();
      this.passwordFields = new ArrayList();
      this.nextControlId = 0;
      this.renderDefaultBackground = true;
   }

   public GuiButton addButton(int n, int n2, int n3, int n4, String str, GuiButtonClickHandler guiButtonClickHandler) {
      int nextControlId2 = this.nextControlId;
      GuiButton button = new GuiButton(nextControlId2, n, n2, n3, n4, str);
      this.buttonList.add(button);
      this.buttonClickHandlers.put(nextControlId2, guiButtonClickHandler);
      this.nextControlId++;
      return button;
   }

   public void initContent() {
   }

   public int getCenterY() {
      return this.height / 2;
   }

   public static void enableScissor(int n, int n2, int n3, int n4) {
      int n5 = n3 - n;
      int n6 = n4 - n2;
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      int scaleFactor = sr.getScaleFactor();
      int n7 = sr.getScaledHeight() - n4;
      GL11.glScissor(n * scaleFactor, n7 * scaleFactor, n5 * scaleFactor, n6 * scaleFactor);
      GL11.glEnable(3089);
   }

   public int getCenterX() {
      return this.width / 2;
   }

   public void drawTooltip(List list, int n, int n2) {
      this.drawHoveringText(list, n + 5, n2 + 5);
   }

   @Override
   public void keyTyped(char typedChar, int keyCode) throws IOException {
      String controlFlowGuard = ScrollBar.getControlFlowGuard();
      if (keyCode == 1 && this.getParentScreen() != null) {
         this.navigateBack();
      } else {
         super.keyTyped(typedChar, keyCode);

         for (GuiTextField guiTextField : (Iterable<GuiTextField>)(this.textFields)) {
            guiTextField.textboxKeyTyped(typedChar, keyCode);
         }

         for (GuiPasswordField guiPasswordField : (Iterable<GuiPasswordField>)(this.passwordFields)) {
            guiPasswordField.textboxKeyTyped(typedChar, keyCode);
         }
      }
   }

   public void drawCenteredString(String str, float f, float f2, int n) {
      Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(str, f - (float)Minecraft.getMinecraft().fontRendererObj.getStringWidth(str) / 2.0F, f2, n);
   }

   public void drawString(String str, float f, float f2, int n) {
      Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(str, f, f2, n);
   }

   public void openScreen(GuiScreen screen) {
      Minecraft.getMinecraft().displayGuiScreen(screen);
   }

   public void drawString(String str, float f, float f2) {
      byte b = -1;
      this.drawString(str, f, f2, b);
   }

   public void setParentScreen(GuiScreen screen) {
      this.parentScreen2 = screen;
   }

   @Override
   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      ScrollBar.getControlFlowGuard();

      for (GuiTextField guiTextField : (Iterable<GuiTextField>)(this.textFields)) {
         guiTextField.mouseClicked(mouseX, mouseY, mouseButton);
      }

      for (GuiPasswordField guiPasswordField : (Iterable<GuiPasswordField>)(this.passwordFields)) {
         guiPasswordField.mouseClicked(mouseX, mouseY, mouseButton);
      }
   }

   public void drawCenteredScaledString(String str, float f, float f2, float f3) {
      float f4 = f - (float)Minecraft.getMinecraft().fontRendererObj.getStringWidth(str) / 2.0F;
      byte b = -1;
      float f5 = f4;
      this.drawScaledString(str, f5, f2, f3, b);
   }

   public void drawScaledString(String str, float f, float f2, float f3, int n) {
      GL11.glPushMatrix();
      GL11.glScalef(f3, f3, f3);
      Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(str, f, f2, n);
      GL11.glPopMatrix();
   }

   public GuiScreen getParentScreen() {
      return this.parentScreen2;
   }

   public void drawContent(int n, int n2, float f) {
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      ScrollBar.getControlFlowGuard();

      for (GuiTextField guiTextField : (Iterable<GuiTextField>)(this.textFields)) {
         guiTextField.updateCursorCounter();
      }

      for (GuiPasswordField guiPasswordField : (Iterable<GuiPasswordField>)(this.passwordFields)) {
         guiPasswordField.updateCursorCounter();
      }
   }

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      String controlFlowGuard = ScrollBar.getControlFlowGuard();
      if (this.renderDefaultBackground) {
         this.drawDefaultBackground();
         ScaledResolution sr = new ScaledResolution(this.mc);
         boolean flag = true;
         BlurShadowManager.flushRenderQueues(sr, flag);
      }

      for (GuiTextField guiTextField : (Iterable<GuiTextField>)(this.textFields)) {
         guiTextField.drawTextBox();
      }

      for (GuiPasswordField guiPasswordField : (Iterable<GuiPasswordField>)(this.passwordFields)) {
         guiPasswordField.drawTextBox();
      }

      this.drawContent(mouseX, mouseY, partialTicks);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public GuiPasswordField addPasswordField(int n, int n2, int n3, int n4) {
      int nextControlId2 = this.nextControlId;
      GuiPasswordField guiPasswordField = new GuiPasswordField(nextControlId2, Minecraft.getMinecraft().fontRendererObj, n, n2, n3, n4);
      short s = 1337;
      guiPasswordField.setMaxStringLength(s);
      this.nextControlId++;
      this.passwordFields.add(guiPasswordField);
      return guiPasswordField;
   }

   @Override
   public final void initGui() {
      super.initGui();
      ScrollBar.getControlFlowGuard();
      this.buttonClickHandlers.clear();
      this.buttonList.clear();
      this.textFields.clear();
      this.passwordFields.clear();
      if (this.getParentScreen() != null) {
         int n = this.height - 26;
         GuiButtonClickHandler guiButtonClickHandler = button -> this.onBackButtonClicked(button);
         String str = "<-";
         byte b = 20;
         byte b2 = 20;
         int n2 = n;
         byte b3 = 5;
         this.addButton(b3, n2, b2, b, str, guiButtonClickHandler);
      }

      this.initContent();
   }

   public void drawScaledString(String str, float f, float f2, float f3) {
      byte b = -1;
      this.drawScaledString(str, f, f2, f3, b);
   }

   public int getStringWidth(String str) {
      return Minecraft.getMinecraft().fontRendererObj.getStringWidth(str);
   }

   public GuiTextField addTextField(int n, int n2, int n3, int n4) {
      int nextControlId2 = this.nextControlId;
      GuiTextField guiTextField = new GuiTextField(nextControlId2, Minecraft.getMinecraft().fontRendererObj, n, n2, n3, n4);
      guiTextField.setMaxStringLength(1337);
      this.nextControlId++;
      this.textFields.add(guiTextField);
      return guiTextField;
   }

   @Override
   public void actionPerformed(GuiButton button) throws IOException {
      super.actionPerformed(button);

      try {
         ((GuiButtonClickHandler)this.buttonClickHandlers.get(button.id)).onClick(button);
      } catch (Exception e) {
      }
   }

}
