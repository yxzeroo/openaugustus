package net.augustus.modules.modes.clicking;

import net.augustus.ClientManagers;
import net.augustus.clickgui.graph.GraphPointBuilder;
import net.augustus.events.EventClick;
import net.augustus.modules.combat.helper.ClickModuleContext;
import net.augustus.settings.ActionSetting;
import net.augustus.settings.GraphSetting;
import net.augustus.settings.SettingChangeListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.TextLabelSetting;
import net.augustus.settings.graph.GraphPoint;
import net.augustus.utils.MutableReference;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.math.DoubleSampleWindow;
import net.augustus.utils.math.MathStatisticsUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Consumer;
import net.augustus.modules.Module;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class DistributionClickPatternMode extends AbstractClickPatternMode {
   private final BooleansSetting distributionClickPattern;
   private final ClickPatternManager clickPatternManager;
   private final StringValue resolution;
   private final StringValue resatimport;
   private ClickPattern selectedImportPattern;
   private GraphSetting pressedDistributionGraph;
   private GraphSetting unpressedDistributionGraph;
   private BooleanValue preventTripleclicks;
   private GraphPointBuilder graphPointBuilder;
   private TextLabelSetting averageCps;
   private TextLabelSetting averageDcps;
   private StringValue buffersize;
   private TextLabelSetting averageFlawedKurtosis;
   private TextLabelSetting minFlawedKurtosis;
   private TextLabelSetting averageKurtosis;
   private TextLabelSetting minKurtosis;
   private TextLabelSetting worstCps;
   private TextLabelSetting probabilities;
   private long nextClickTimeMs;
   private long releaseTimeMs;
   private static final int MIN_GRAPH_MAX_Y = 10;
   private int lastClickTick;

   public DistributionClickPatternMode(Module module, DoubleValue doubleValue) {
       super(module, doubleValue, "Distribution");
      this.graphPointBuilder = new GraphPointBuilder();
      this.nextClickTimeMs = 0L;
      this.releaseTimeMs = 0L;
      boolean flag = false;
      this.graphPointBuilder.xEditable(flag);
      boolean flag2 = true;
      this.graphPointBuilder.yEditable(flag2);
      GraphPointBuilder graphPointBuilder2 = this.graphPointBuilder;
      Runnable runnable = () -> this.resetAnalysisLabelsAndScale();
      graphPointBuilder2.onChange(runnable);
      boolean flag3 = true;
      this.graphPointBuilder.highlightZero(flag3);
      DoubleValue doubleValue2 = new DoubleValue(module, "Pressed", doubleValue);
      SettingVisibilityCondition condition = () -> this.isSelected();
      DoubleValue doubleValue3 = doubleValue2.visibleWhen(condition);
      DoubleValue doubleValue4 = new DoubleValue(module, "Unpressed", doubleValue);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      DoubleValue doubleValue5 = doubleValue4.visibleWhen(condition2);
      this.pressedDistributionGraph = new GraphSetting(module, "Pressedtime Distribution", new ArrayList(), 0.0, 200.0, 0.0, 10.0, doubleValue3, this.graphPointBuilder);
      this.unpressedDistributionGraph = new GraphSetting(module, "Unpressedtime Distribution", new ArrayList(), 0.0, 200.0, 0.0, 10.0, doubleValue5, this.graphPointBuilder);
      GraphSetting graphSetting = this.pressedDistributionGraph;
      this.addGraphShiftActions(module, doubleValue3, graphSetting);
      GraphSetting graphSetting2 = this.unpressedDistributionGraph;
      this.addGraphShiftActions(module, doubleValue5, graphSetting2);
      BooleanValue booleanValue = new BooleanValue(module, "Prevent tripleclicks", true, doubleValue);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.preventTripleclicks = (BooleanValue)booleanValue.withVisibility(condition3);
      DoubleValue doubleValue6 = new DoubleValue(module, "Import", doubleValue);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      DoubleValue doubleValue7 = doubleValue6.visibleWhen(condition4);
      DoubleValue doubleValue8 = new DoubleValue(module, "Analysis", doubleValue);
      SettingVisibilityCondition condition5 = () -> this.isSelected();
      DoubleValue doubleValue9 = doubleValue8.visibleWhen(condition5);
      new ActionSetting(module, "Analyze", () -> this.runDistributionAnalysis(), doubleValue9);
      this.averageCps = new TextLabelSetting(module, doubleValue9, "Average CPS: -");
      this.averageDcps = new TextLabelSetting(module, doubleValue9, "Average DCPS: -");
      this.buffersize = new StringValue(module, "Buffersize", 50.0, 4.0, 1000.0, 0, doubleValue9);
      this.averageFlawedKurtosis = new TextLabelSetting(module, doubleValue9, "Average flawed Kurtosis: -");
      this.minFlawedKurtosis = new TextLabelSetting(module, doubleValue9, "Min flawed Kurtosis: -");
      String str = "Frap moment";
      this.averageFlawedKurtosis.setDescription(str);
      String str2 = "Frap moment";
      this.minFlawedKurtosis.setDescription(str2);
      this.averageKurtosis = new TextLabelSetting(module, doubleValue9, "Average Kurtosis: -");
      this.minKurtosis = new TextLabelSetting(module, doubleValue9, "Min Kurtosis: -");
      this.worstCps = new TextLabelSetting(module, doubleValue9, "Worst CPS: -");
      this.probabilities = new TextLabelSetting(module, doubleValue9, "Probabilities: -");
      BooleansSetting booleansSetting = new BooleansSetting(module, "DistributionClickPattern", "", new String[0], doubleValue7);
      String str3 = "ClickPattern";
      this.distributionClickPattern = (BooleansSetting)booleansSetting.withDisplayName(str3);
      ClickPatternManagerBuilder builder = ClickPatternManager.builder();
      BooleansSetting booleansSetting2 = this.distributionClickPattern;
      Consumer consumer = obj -> this.selectedImportPattern = (ClickPattern)obj;
      BooleansSetting booleansSetting3 = booleansSetting2;
      this.clickPatternManager = builder.withSelectorAndCallback(booleansSetting3, consumer).build();
      StringValue stringValue = new StringValue(module, "Resolution", 20.0, 1.0, 100.0, 0, doubleValue7);
      SettingVisibilityCondition condition6 = () -> this.isSelected();
      this.resolution = stringValue.visibleWhen(condition6);
      BooleansSetting booleansSetting4 = this.distributionClickPattern;
      SettingChangeListener listener = () -> {
         ClickPatternManager clickPatternManager2 = this.clickPatternManager;
         String selectedValue = this.distributionClickPattern.getSelectedValue();
         this.selectedImportPattern = clickPatternManager2.getPattern(selectedValue);
      };
      SettingChangeListener listener2 = listener;
      booleansSetting4.withChangeListener(listener2);
      this.clickPatternManager.startWatchingAndLoad();
      StringValue stringValue2 = new StringValue(module, "resatimport", this.resolution.getValue(), this.resolution.getMinValue(), this.resolution.getMaxValue(), 0, doubleValue);
      SettingVisibilityCondition condition7 = () -> false;
      this.resatimport = stringValue2.visibleWhen(condition7);
      new ActionSetting(module, "Import pattern", () -> this.importSelectedPattern(), doubleValue7);
      this.initDefaultDistribution();
      this.resetAnalysisLabelsAndScale();
   }

   private void addGraphShiftActions(Module module, DoubleValue doubleValue, GraphSetting graphSetting) {
      new ActionSetting(module, "Shift left", () -> this.shiftGraphLeft(graphSetting), doubleValue);
      new ActionSetting(module, "Shift right", () -> this.shiftGraphRight(graphSetting), doubleValue);
   }

   private void shiftGraphPoints(GraphSetting graphSetting, int n) {
      if (n > 0) {
         for (int i = 0; i < graphSetting.getPoints().size(); i++) {
            GraphPoint graphPoint = (GraphPoint)graphSetting.getPoints().get(i);
            if (graphSetting.getPoints().size() > i + n) {
               double y = ((GraphPoint)graphSetting.getPoints().get(i + n)).getY();
               graphPoint.setY(y);
            } else {
               double d = 0.0;
               graphPoint.setY(d);
            }
         }
      } else {
         for (int j = graphSetting.getPoints().size() - 1; j >= 0; j--) {
            GraphPoint graphPoint2 = (GraphPoint)graphSetting.getPoints().get(j);
            if (j > 0) {
               double y2 = ((GraphPoint)graphSetting.getPoints().get(j + n)).getY();
               graphPoint2.setY(y2);
            } else {
               double d2 = 0.0;
               graphPoint2.setY(d2);
            }
         }
      }

      this.resetAnalysisLabelsAndScale();
   }

   @Override
   public int computeClicks(EventClick event) {
      int n = 0;
      long l = System.currentTimeMillis();
      if (this.lastClickTick != mc.thePlayer.ticksExisted - 1) {
         this.nextClickTimeMs = l - 50L;
         long l2 = l - 50L;
         this.advanceClicksUntil(l2);
      }

      n = this.advanceClicksUntil(l);
      if (n >= 3 && this.preventTripleclicks.getValue()) {
         n = 2;
         this.releaseTimeMs = l - 1L;
      }

      boolean flag = this.releaseTimeMs >= l;
      ((ClickModuleContext)this.parentModule).setHoldingAttack(flag);
      n = ((ClickModuleContext)this.parentModule).applyHitReduction(n);
      if (ClientManagers.moduleManager.smartClicking.isSuppressingClicks() || n != n) {
         n = 0;
         ClickModuleContext parentModule2 = (ClickModuleContext)this.parentModule;
         boolean flag2 = false;
         parentModule2.setHoldingAttack(flag2);
      }

      this.lastClickTick = mc.thePlayer.ticksExisted;
      return n;
   }

   private int advanceClicksUntil(long l) {
      int n = 0;

      while (l >= this.nextClickTimeMs) {
         n++;
         GraphSetting graphSetting = this.pressedDistributionGraph;
         int n2 = Math.max(1, this.sampleDelayFromGraph(graphSetting));
         GraphSetting graphSetting2 = this.unpressedDistributionGraph;
         int n3 = Math.max(1, this.sampleDelayFromGraph(graphSetting2));
         this.releaseTimeMs = this.nextClickTimeMs + (long)n2;
         this.nextClickTimeMs += (long)(n2 + n3);
      }

      return n;
   }

   public int sampleDelayFromGraph(GraphSetting graphSetting) {
      List points = graphSetting.getPoints();
      double d = 0.0;

      for (int i = 0; i < points.size(); i++) {
         GraphPoint graphPoint = (GraphPoint)points.get(i);
         d += graphPoint.getY();
      }

      if (!points.isEmpty() && d != 0.0) {
         float f = 1.0F;
         float f2 = 0.0F;
         double d2 = (double)DefaultRandomGenerator.instance.nextFloat(f2, f) * d;
         int value = (int)this.resatimport.getValue();

         for (int j = 0; j < points.size(); j++) {
            GraphPoint graphPoint2 = (GraphPoint)points.get(j);
            d2 -= graphPoint2.getY();
            if (d2 <= 0.0) {
               float f3 = 1.0F;
               float f4 = 0.0F;
               return (int)((double)(DefaultRandomGenerator.instance.nextFloat(f4, f3) * 200.0F / (float)value) + graphPoint2.getX());
            }
         }

         float f5 = 1.0F;
         float f6 = 0.0F;
         return (int)((double)(DefaultRandomGenerator.instance.nextFloat(f6, f5) * (float)value) + ((GraphPoint)points.get(points.size() - 1)).getX());
      } else {
         return 50;
      }
   }

   @Override
   public void onTick2() {
   }

   public void resetAnalysisLabelsAndScale() {
      String str = "Average CPS: -";
      this.averageCps.setText(str);
      String str2 = "Average DCPS: -";
      this.averageDcps.setText(str2);
      String str3 = "Average Kurtosis: -";
      this.averageKurtosis.setText(str3);
      String str4 = "Min Kurtosis: -";
      this.minKurtosis.setText(str4);
      String str5 = "Flawed average Kurtosis: -";
      this.averageFlawedKurtosis.setText(str5);
      String str6 = "Flawed min Kurtosis: -";
      this.minFlawedKurtosis.setText(str6);
      String str7 = "Worstcase CPS: -";
      this.worstCps.setText(str7);
      String str8 = "Probabilities: -";
      this.probabilities.setText(str8);
      int n = Math.max(10, Math.max(findMaxGraphY(this.pressedDistributionGraph), findMaxGraphY(this.unpressedDistributionGraph)) + 1);
      double d = (double)n;
      this.pressedDistributionGraph.setMaxY(d);
      double d2 = (double)n;
      this.unpressedDistributionGraph.setMaxY(d2);
   }

   private static int findMaxGraphY(GraphSetting graphSetting) {
      int minvalue = Integer.MIN_VALUE;

      for (int i = 0; i < graphSetting.getPoints().size(); i++) {
         minvalue = (int)Math.max((double)minvalue, ((GraphPoint)graphSetting.getPoints().get(i)).getY());
      }

      return minvalue;
   }

   public void runDistributionAnalysis() {
      long l = 0L;
      long l2 = 0L;
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      ClickDelayMeanAccumulator accumulator = new ClickDelayMeanAccumulator();
      ClickDelayMeanAccumulator accumulator2 = new ClickDelayMeanAccumulator();
      MinKurtosisTracker minKurtosisTracker = new MinKurtosisTracker();
      MinKurtosisTracker minKurtosisTracker2 = new MinKurtosisTracker();
      DoubleSampleWindow doubleSampleWindow = new DoubleSampleWindow((int)this.buffersize.getValue());
      DoubleSampleWindow doubleSampleWindow2 = new DoubleSampleWindow(20);
      TreeMap treeMap = new TreeMap();
      boolean value = this.preventTripleclicks.getValue();

      for (int i = 0; i < 1000000; i++) {
         int n5 = 0;

         while (l >= l2) {
            n5++;
            GraphSetting graphSetting = this.pressedDistributionGraph;
            int n6 = Math.max(1, this.sampleDelayFromGraph(graphSetting));
            GraphSetting graphSetting2 = this.unpressedDistributionGraph;
            int n7 = Math.max(1, this.sampleDelayFromGraph(graphSetting2));
            l2 += (long)(n6 + n7);
         }

         if (n5 >= 3 && value) {
            n5 = 2;
         }

         for (int j = 0; j < n5; j++) {
            double d = (double)(50 * n3);
            doubleSampleWindow.add(d);
            n3 = 0;
         }

         if (doubleSampleWindow.isFull()) {
            double d2 = MathStatisticsUtil.computeExcessKurtosis(doubleSampleWindow.getSamples());
            double d3 = MathStatisticsUtil.computeAlternateKurtosis(doubleSampleWindow.getSamples());
            accumulator.addSample(d2);
            minKurtosisTracker.recordSample(d2);
            accumulator2.addSample(d3);
            minKurtosisTracker2.recordSample(d3);
            doubleSampleWindow.clear();
         }

         l += 50L;
         if (n5 >= 2) {
            n++;
         }

         n2 += n5;
         n3++;
         double d4 = (double)n5;
         doubleSampleWindow2.add(d4);
         if (doubleSampleWindow2.isFull()) {
            MutableReference mutableReference = (MutableReference)(treeMap.computeIfAbsent((int)doubleSampleWindow2.sum(), obj -> new MutableReference(0)));
            Integer num = (Integer)mutableReference.getValue() + 1;
            mutableReference.setValue(num);
            n4++;
         }
      }

      int n8 = 1;
      int n9 = 1;

      for (int k = 0; k < this.pressedDistributionGraph.getPoints().size(); k++) {
         GraphPoint graphPoint = (GraphPoint)this.pressedDistributionGraph.getPoints().get(k);
         if (graphPoint.getY() != 0.0) {
            n8 = Math.max((int)graphPoint.getX(), n8);
         }
      }

      for (int m = 0; m < this.unpressedDistributionGraph.getPoints().size(); m++) {
         GraphPoint graphPoint2 = (GraphPoint)this.unpressedDistributionGraph.getPoints().get(m);
         if (graphPoint2.getY() != 0.0) {
            n9 = Math.max((int)graphPoint2.getX(), n9);
         }
      }

      TextLabelSetting averageCps2 = this.averageCps;
      String str = "Average CPS: " + MathStatisticsUtil.roundToDecimalPlaces((double)n2 / ((double)l2 / 1000.0), 2);
      averageCps2.setText(str);
      averageCps2 = this.averageDcps;
      String str2 = "Average DCPS: " + MathStatisticsUtil.roundToDecimalPlaces((double)n / ((double)l2 / 1000.0), 2);
      averageCps2.setText(str2);
      averageCps2 = this.averageKurtosis;
      String str3 = "Average Kurtosis: " + MathStatisticsUtil.roundToDecimalPlaces(accumulator.getMean(), 2);
      averageCps2.setText(str3);
      averageCps2 = this.minKurtosis;
      String str4 = "Min Kurtosis: " + MathStatisticsUtil.roundToDecimalPlaces(minKurtosisTracker.getMinValue(), 2);
      averageCps2.setText(str4);
      averageCps2 = this.averageFlawedKurtosis;
      String str5 = "Flawed average Kurtosis: " + MathStatisticsUtil.roundToDecimalPlaces(accumulator2.getMean(), 2);
      averageCps2.setText(str5);
      averageCps2 = this.minFlawedKurtosis;
      String str6 = "Flawed min Kurtosis: " + MathStatisticsUtil.roundToDecimalPlaces(minKurtosisTracker2.getMinValue(), 2);
      averageCps2.setText(str6);
      String str7 = "Worstcase CPS: " + 1000 / (n8 + n9);
      this.worstCps.setText(str7);
      StringBuilder sb = new StringBuilder("Probabilities (sampled):\n");

      for (int n10 : (Iterable<Integer>)(treeMap.navigableKeySet())) {
         sb.append(n10)
            .append(" CPS - ")
            .append(MathStatisticsUtil.roundToDecimalPlaces((double)((Integer)((MutableReference)treeMap.get(n10)).getValue()).intValue() / (double)n4 * 100.0, 5))
            .append("%\n");
      }

      averageCps2 = this.probabilities;
      String str8 = sb.toString();
      averageCps2.setText(str8);
   }

   public void importSelectedPattern() {
      if (this.selectedImportPattern != null) {
         List points = this.pressedDistributionGraph.getPoints();
         ArrayList pressedDelays = this.selectedImportPattern.pressedDelays;
         int value = (int)this.resolution.getValue();
         ArrayList list = pressedDelays;
         List list2 = points;
         this.populateHistogramPoints(list2, list, value);
         points = this.unpressedDistributionGraph.getPoints();
         pressedDelays = this.selectedImportPattern.unpressedDelays;
         int value2 = (int)this.resolution.getValue();
         ArrayList list3 = pressedDelays;
         List list4 = points;
         this.populateHistogramPoints(list4, list3, value2);
         StringValue resatimport2 = this.resatimport;
         double value3 = this.resolution.getValue();
         resatimport2.setValue(value3);
         this.resetAnalysisLabelsAndScale();
      }
   }

   private void populateHistogramPoints(List list, List list2, int n) {
      list.clear();
      ArrayList list3 = new ArrayList();
      HashMap map = new HashMap();
      int n2 = 200 / n;

      for (int i = 0; i + n2 <= 200; i += n2) {
         ClickDelayHistogramBin clickDelayHistogramBin = new ClickDelayHistogramBin(i, i + n2 - 1);
         list3.add(clickDelayHistogramBin);

         for (int j = i; j < i + n2; j++) {
            map.put(j, clickDelayHistogramBin);
         }
      }

      int n3 = 0;

      for (int k = 0; k < list2.size(); k++) {
         int n4 = (Integer)list2.get(k);
         ClickDelayHistogramBin clickDelayHistogramBin2 = (ClickDelayHistogramBin)map.get(n4);
         if (clickDelayHistogramBin2 != null) {
            int n5 = ClickDelayHistogramBin.accessSampleCount(clickDelayHistogramBin2) + 1;
            clickDelayHistogramBin2.setSampleCount(n5);
            n3++;
         }
      }

      for (int m = 0; m < list3.size(); m++) {
         ClickDelayHistogramBin clickDelayHistogramBin3 = (ClickDelayHistogramBin)list3.get(m);
         GraphPointBuilder graphPointBuilder2 = this.graphPointBuilder;
         double d = (double)(ClickDelayHistogramBin.accessMinDelay(clickDelayHistogramBin3) + ClickDelayHistogramBin.accessMaxDelay(clickDelayHistogramBin3)) / 2.0;
         double d2 = (double)ClickDelayHistogramBin.accessSampleCount(clickDelayHistogramBin3) * 100.0 / (double)n3;
         double d3 = d;
         list.add(graphPointBuilder2.buildAt(d3, d2));
      }
   }

   private void initDefaultDistribution() {
      this.importSelectedPattern();
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public boolean simulatesMousePress() {
      return true;
   }

   private void shiftGraphRight(GraphSetting graphSetting) {
      byte b = -1;
      this.shiftGraphPoints(graphSetting, b);
   }

   private void shiftGraphLeft(GraphSetting graphSetting) {
      byte b = 1;
      this.shiftGraphPoints(graphSetting, b);
   }

}
