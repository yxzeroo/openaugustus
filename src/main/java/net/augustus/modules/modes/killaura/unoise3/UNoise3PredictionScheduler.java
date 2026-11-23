package net.augustus.modules.modes.killaura.unoise3;

import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.simulation.TrajectoryPredictionUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;

public class UNoise3PredictionScheduler {
   private UNoise3SettingContext uNoise3SettingContext;
   private int targetFailTicks;
   public UNoise3MovementFilter uNoise3MovementFilter;
   public int lookaheadTicks;
   private RandomizedRangeSettings uNoise3targetpredictionfailticks;
   public boolean forceRecompute;
   private RandomizedRangeSettings uNoise3inputticksoffset;
   private int inputKnowledgeOffset;
   private int reactionDelayTicks;
   private RandomizedRangeSettings uNoise3lookahead;
   private RandomizedRangeSettings uNoise3playerpredictionfailticks;
   private RandomizedRangeSettings uNoise3timeoffset;
   private int inputOffset;
   private RandomizedRangeSettings uNoise3reactiontime;
   private int playerFailTicks;
   public UNoise3PredictionEntry[] yawPredictions;
   private RandomizedRangeSettings uNoise3predictionfailticks;
   public UNoise3PredictionEntry[] pitchPredictions;
   public int baseSnapshotTick;
   private RandomizedRangeSettings uNoise3inputoffset;
   private int predictionFailTicks;
   public int lookaheadEndTick;

   public UNoise3PredictionScheduler(UNoise3SettingContext uNoise3SettingContext2) {
       super();
      this.forceRecompute = false;
      this.uNoise3SettingContext = uNoise3SettingContext2;
      this.uNoise3MovementFilter = new UNoise3MovementFilter(uNoise3SettingContext2);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext2.getModule(), "UNoise3reactiontime", 3.0, 5.0, 0.0, 8.0, 0, uNoise3SettingContext2.getParentGroup());
      String str = "Reactiotime";
      this.uNoise3reactiontime = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str);
      randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext2.getModule(), "UNoise3timeoffset", 0.0, 0.0, 0.0, 3.0, 0, uNoise3SettingContext2.getParentGroup());
      String str2 = "Time offset";
      this.uNoise3timeoffset = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str2);
      randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext2.getModule(), "UNoise3predictionfailticks", 0.0, 0.0, -3.0, 3.0, 0, uNoise3SettingContext2.getParentGroup());
      String str3 = "Prediction Fail Ticks";
      this.uNoise3predictionfailticks = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str3);
      randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext2.getModule(), "UNoise3playerpredictionfailticks", 0.0, 0.0, -3.0, 3.0, 0, uNoise3SettingContext2.getParentGroup());
      String str4 = "Player Prediction Fail Ticks";
      this.uNoise3playerpredictionfailticks = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str4);
      randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext2.getModule(), "UNoise3targetpredictionfailticks", 0.0, 0.0, -3.0, 3.0, 0, uNoise3SettingContext2.getParentGroup());
      String str5 = "Target Prediction Fail Ticks";
      this.uNoise3targetpredictionfailticks = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str5);
      randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext2.getModule(), "UNoise3inputticksoffset", 0.0, 0.0, 0.0, 3.0, 0, uNoise3SettingContext2.getParentGroup());
      String str6 = "Input Knowledge Offset";
      this.uNoise3inputticksoffset = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str6);
      randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext2.getModule(), "UNoise3inputoffset", 0.0, 0.0, -3.0, 3.0, 0, uNoise3SettingContext2.getParentGroup());
      String str7 = "Input Offset";
      this.uNoise3inputoffset = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str7);
      randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext2.getModule(), "UNoise3lookahead", 0.0, 0.0, 0.0, 3.0, 0, uNoise3SettingContext2.getParentGroup());
      String str8 = "Lookahead Time";
      this.uNoise3lookahead = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str8);
   }

   public void updatePredictions(EntityPlayerSP player, EntityLivingBase entity) {
      int[] predictionStateFlags = TrajectoryPredictionUtil.getPredictionStateFlags();
      if (player.ticksExisted - this.baseSnapshotTick >= this.reactionDelayTicks || this.lookaheadTicks > 0 && this.lookaheadEndTick <= player.ticksExisted || this.forceRecompute) {
         boolean flag = true;
         this.baseSnapshotTick = player.ticksExisted - this.uNoise3timeoffset.nextInt(flag);
         boolean flag2 = true;
         this.reactionDelayTicks = this.uNoise3reactiontime.nextInt(flag2);
         boolean flag3 = true;
         this.predictionFailTicks = this.uNoise3predictionfailticks.nextInt(flag3);
         boolean flag4 = true;
         this.playerFailTicks = this.uNoise3playerpredictionfailticks.nextInt(flag4);
         boolean flag5 = true;
         this.targetFailTicks = this.uNoise3targetpredictionfailticks.nextInt(flag5);
         boolean flag6 = true;
         this.inputKnowledgeOffset = this.uNoise3inputticksoffset.nextInt(flag6);
         boolean flag7 = true;
         this.inputOffset = this.uNoise3inputoffset.nextInt(flag7);
         boolean flag8 = true;
         this.lookaheadTicks = this.uNoise3lookahead.nextInt(flag8);
         if (this.lookaheadTicks > 0) {
            this.lookaheadEndTick = this.lookaheadTicks + player.ticksExisted;
         } else {
            this.lookaheadEndTick = 0;
         }

         this.forceRecompute = false;
      }

      int n = player.ticksExisted + this.predictionFailTicks;
      if (this.lookaheadTicks > 0) {
         n = this.lookaheadEndTick;
      }

      int n2 = player.ticksExisted - this.inputKnowledgeOffset;
      int inputOffset2 = this.inputOffset;
      int n3 = n2;
      int baseSnapshotTick2 = this.baseSnapshotTick;
      this.yawPredictions = this.uNoise3MovementFilter.predictYawEntries(player, entity, baseSnapshotTick2, n3, inputOffset2, n);
      int n4 = player.ticksExisted - this.inputKnowledgeOffset;
      int inputOffset3 = this.inputOffset;
      int n5 = n4;
      int baseSnapshotTick3 = this.baseSnapshotTick;
      this.pitchPredictions = this.uNoise3MovementFilter.predictPitchEntries(player, entity, baseSnapshotTick3, n5, inputOffset3, n);
      int predictionFailTicks2 = this.predictionFailTicks;
      this.predictionFailTicks = this.decrementTowardZero(predictionFailTicks2);
      int playerFailTicks2 = this.playerFailTicks;
      this.playerFailTicks = this.decrementTowardZero(playerFailTicks2);
      int targetFailTicks2 = this.targetFailTicks;
      this.targetFailTicks = this.decrementTowardZero(targetFailTicks2);
   }

   private int decrementTowardZero(int n) {
      return (int)(Math.signum((float)n) * (float)(Math.abs(n) - 1));
   }

   public void resetSnapshotTick(int n) {
      boolean flag = true;
      this.baseSnapshotTick = n - this.uNoise3timeoffset.nextInt(flag);
   }

}
