package net.augustus.modules.legit;

import java.awt.Color;
import java.util.function.BiFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.modules.world.BridgeClickController;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.settings.ModeSetting;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.modules.modes.autoeagle.UnlegitAutoEagleMode;
import net.augustus.modules.modes.autoeagle.MoonWalkAutoEagleMode;
import net.augustus.modules.modes.autoeagle.EagleAutoEagleMode;
import net.augustus.utils.world.BlockUtil;
import net.augustus.Augustus;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

public class AutoEagle extends Module implements ClickListener, SilentSlotListener {
   public final ModeSetting mode;
   private final StringValue minPitchToEagle;
   public final BooleanValue fastPlace;
   private final RandomizedRangeSettings cps;
   private final BooleansSetting dcpsMode;
   private final RandomizedRangeSettings dcps;
   private final BooleanValue alwaysPlaceWhenPossible;
   private final BooleanValue delayClick;
   private final BooleanValue clickPause;
   private final BooleanValue oneClick;
   public final BooleanValue onlyWhileSneak;
   private final BooleanValue autoSwap;
   private final BridgeClickController bridgeClickController;
   private boolean firstPlaceAfterPress;
   public boolean eagling;
   private boolean wasBlockBelow;
   private int postEdgeClickTicks;
   private int preEdgeLookaheadTicks;
   private int postEdgeDelayTicks;
   private final TimeHelper timeHelper;

   public AutoEagle() {
       super("AutoEagle", "AutoAdler", new Color(194, 222, 146), Categorys.LEGIT);
      BiFunction[] biFunctions = new BiFunction[]{
         (obj, obj2) -> new EagleAutoEagleMode((AutoEagle)obj, (DoubleValue)obj2),
         (obj3, obj4) -> new MoonWalkAutoEagleMode((AutoEagle)obj3, (DoubleValue)obj4),
         (obj5, obj6) -> new UnlegitAutoEagleMode((AutoEagle)obj5, (DoubleValue)obj6)
      };
      this.mode = new ModeSetting(this, "Mode", "Eagle", biFunctions);
      this.minPitchToEagle = new StringValue(this, "MinPitchToEagle", 65.0, -90.0, 90.0, 0);
      this.fastPlace = new BooleanValue(this, "FastPlace", true);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "CPS", 10.0, 20.0, 0.0, 40.0, 0)).withoutNotInARow();
      BooleanValue fastPlace2 = this.fastPlace;
      this.fastPlace.getClass();
      BooleanValue booleanValue = fastPlace2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.cps = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      BooleansSetting booleansSetting = new BooleansSetting(this, "DCPSMode", "External", new String[]{"Internal", "External"});
      fastPlace2 = this.fastPlace;
      this.fastPlace.getClass();
      BooleanValue booleanValue2 = fastPlace2;
      SettingVisibilityCondition condition2 = () -> booleanValue2.getValue();
      this.dcpsMode = (BooleansSetting)booleansSetting.withVisibility(condition2);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "DCPS", 2.0, 4.0, 0.0, 20.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition3 = () -> this.isExternalDcpsMode();
      this.dcps = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition3);
      BooleanValue booleanValue3 = new BooleanValue(this, "AlwaysPlaceWhenPossible", true);
      fastPlace2 = this.fastPlace;
      this.fastPlace.getClass();
      BooleanValue booleanValue4 = fastPlace2;
      SettingVisibilityCondition condition4 = () -> booleanValue4.getValue();
      this.alwaysPlaceWhenPossible = (BooleanValue)booleanValue3.withVisibility(condition4);
      BooleanValue booleanValue5 = new BooleanValue(this, "DelayClick", false);
      fastPlace2 = this.fastPlace;
      this.fastPlace.getClass();
      BooleanValue booleanValue6 = fastPlace2;
      SettingVisibilityCondition condition5 = () -> booleanValue6.getValue();
      this.delayClick = (BooleanValue)booleanValue5.withVisibility(condition5);
      BooleanValue booleanValue7 = new BooleanValue(this, "ClickPause", false);
      fastPlace2 = this.fastPlace;
      this.fastPlace.getClass();
      BooleanValue booleanValue8 = fastPlace2;
      SettingVisibilityCondition condition6 = () -> booleanValue8.getValue();
      this.clickPause = (BooleanValue)booleanValue7.withVisibility(condition6);
      BooleanValue booleanValue9 = new BooleanValue(this, "OneClick", false);
      fastPlace2 = this.fastPlace;
      this.fastPlace.getClass();
      BooleanValue booleanValue10 = fastPlace2;
      SettingVisibilityCondition condition7 = () -> booleanValue10.getValue();
      this.oneClick = (BooleanValue)booleanValue9.withVisibility(condition7);
      this.onlyWhileSneak = new BooleanValue(this, "OnlyWhileSneak", true);
      this.autoSwap = (BooleanValue)(new BooleanValue(this, "AutoSwap", false)).markVisualOnly();
      this.bridgeClickController = new BridgeClickController(this.cps, this.dcpsMode, this.dcps, this.alwaysPlaceWhenPossible, this.oneClick, this.delayClick);
      this.firstPlaceAfterPress = false;
      this.postEdgeClickTicks = 0;
      byte b = 2;
      byte b2 = 0;
      this.preEdgeLookaheadTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
      byte b3 = 2;
      byte b4 = 0;
      this.postEdgeDelayTicks = DefaultRandomGenerator.instance.nextInt2(b4, b3);
      this.timeHelper = new TimeHelper();
   }

   @Override
   public void onEnable() {
      this.firstPlaceAfterPress = true;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 7;
      this.toggleEventSubscription(b);
      byte b2 = 34;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onClick(EventClick event) {
      boolean isFullBlock = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ).getBlock().isFullBlock();
      if (isFullBlock && !this.wasBlockBelow) {
         byte b = 1;
         byte b2 = 0;
         this.preEdgeLookaheadTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
         byte b3 = 1;
         byte b4 = 0;
         this.postEdgeDelayTicks = DefaultRandomGenerator.instance.nextInt2(b4, b3);
      }

      if (!isFullBlock && this.wasBlockBelow) {
         this.postEdgeClickTicks = this.postEdgeDelayTicks;
      }

      boolean flag = (this.preEdgeLookaheadTicks >= 0 || !this.wasBlockBelow) && !isFullBlock;
      this.wasBlockBelow = isFullBlock;
      double[] doubles = PlayerUtil.predictPlayerPosition(this.preEdgeLookaheadTicks - 1, mc.thePlayer);
      if (isFullBlock && !mc.theWorld.getBlockState(doubles[0], mc.thePlayer.posY - 1.0, doubles[2]).getBlock().isFullBlock()) {
         flag = true;
      }

      if (isFullBlock && this.postEdgeClickTicks > 0) {
         flag = true;
         this.postEdgeClickTicks--;
      }

      if (!mc.gameSettings.keyBindUseItem.isKeyDown()) {
         this.firstPlaceAfterPress = true;
      }

      if (mc.gameSettings.keyBindUseItem.isKeyDown()
         && this.eagling
         && mc.isNotInGui()
         && mc.objectMouseOver != null
         && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
         && mc.thePlayer.getCurrentEquippedItem() != null
         && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock
         && mc.objectMouseOver.getBlockPos() != null) {
         if (flag || !this.clickPause.getValue()) {
            boolean firstPlaceAfterPress2 = this.firstPlaceAfterPress;
            MovingObjectPosition objectMouseOver = mc.objectMouseOver;
            this.bridgeClickController.handleDefaultBlockClick(objectMouseOver, event, firstPlaceAfterPress2);
            this.firstPlaceAfterPress = false;
         }

         boolean flag2 = true;
         event.setIgnoreVanillaClicks(flag2);
         boolean flag3 = false;
         event.setRightMouseDown(flag3);
      }

      if (event.getRightClicks() > 0) {
         this.timeHelper.reset();
      }
   }

   public boolean isPitchAboveMinimum() {
      return (double)Augustus.getInstance().getYawPitchHelper().realPitch > this.minPitchToEagle.getValue();
   }

   @Override
   public void onSilentSlot() {
      if (this.autoSwap.getValue() && this.fastPlace.getValue()) {
         long l = 350L;
         if (!this.timeHelper.reached2(l) && this.eagling && !BlockUtil.isValidScaffoldBlock(mc.thePlayer.getHeldItem())) {
            for (int i = 0; i < 9; i++) {
               ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i + 36).getStack();
               if (BlockUtil.isValidScaffoldBlock(stack)) {
                  boolean flag = true;
                  byte b = -2;
                  SilentSlotManager.requestSlot(this, b, i, flag);
                  SilentSlotManager.realHotbarSlot = i;
                  break;
               }
            }
         }
      }
   }

   private boolean isExternalDcpsMode() {
      return this.dcpsMode.isVisible() && this.dcpsMode.getSelectedValue().equals("External");
   }

}
