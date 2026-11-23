package net.augustus.settings;

import net.augustus.Augustus;
import net.augustus.config.FeatureListJsonCodec;
import net.augustus.modules.ModuleToggleListener;
import net.augustus.modules.modes.AbstractSubMode;
import net.augustus.modules.movement.helper.MotionModifierProvider;
import net.augustus.utils.UnaryFunction;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.DirectoryWatchService;
import net.augustus.utils.file.DirectoryWatcher;
import com.google.gson.JsonObject;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.augustus.modules.Module;
import net.augustus.modules.misc.Dummy;

public class ModeSetting extends BooleansSetting implements ModuleToggleListener {
   private final ConcurrentHashMap modesByName;
   private final Set customModeNames;
   private final HashMap modesByClass;
   private AbstractSubMode abstractSubMode;
   private final List builtInModeNames;
   private DoubleValue modeValuesGroup;
   private final HashMap builtInModes;
   private final String customModesDir;
   private static final FeatureListJsonCodec CUSTOM_MODE_CODEC = new FeatureListJsonCodec();
   private Module ownerModule;

   @Override
   public void resetToDefault() {
      super.resetToDefault();
      String selectedValue = this.getSelectedValue();
      this.abstractSubMode = this.resolveSubMode(selectedValue);
   }

   public AbstractSubMode getAbstractSubMode() {
      return this.abstractSubMode;
   }

   private AbstractSubMode resolveSubMode(String str) {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      AbstractSubMode subMode = (AbstractSubMode)this.modesByName.get(str);
      if (subMode == null) {
         subMode = (AbstractSubMode)this.modesByName.values().stream().findFirst().orElse(null);
      }

      return subMode;
   }

   public ModeSetting visibleWhen(SettingVisibilityCondition condition) {
      this.visibilityCondition = condition;
      return this;
   }

   private boolean isNestedModeGroupVisible() {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      return this.modeValuesGroup.hasVisibleChildren() && this.isVisible();
   }

   public boolean isSubModeActive(Class clazz) {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      AbstractSubMode subMode = (AbstractSubMode)this.modesByClass.get(clazz);
      return subMode != null && subMode.isEnabled();
   }

   private void onModeValueChanged() {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      if (this.abstractSubMode != null && this.getParentModule().isToggled()) {
         boolean flag = true;
         this.abstractSubMode.setEnabled(flag);
      }

      if (this.changeListener != null) {
         this.changeListener.onChanged();
      }
   }

   public ModeSetting(Module module, String str, String str2, BiFunction[] biFunctions) {
       super(module, str, str2, new String[0]);
      this.builtInModes = new HashMap();
      this.builtInModeNames = new ArrayList();
      this.modesByName = new ConcurrentHashMap();
      this.customModeNames = ConcurrentHashMap.newKeySet();
      this.modesByClass = new HashMap();
      this.ownerModule = module;
      DoubleValue doubleValue = new DoubleValue(module, str + "ModeValuesGroup", true);
      String str3 = "ModeValues";
      this.modeValuesGroup = (DoubleValue)doubleValue.withDisplayName(str3);
      DoubleValue modeValuesGroup2 = this.modeValuesGroup;
      SettingVisibilityCondition condition = () -> this.isModeGroupVisible();
      this.modeValuesGroup = modeValuesGroup2.visibleWhen(condition);
      Stream<BiFunction> stream = Arrays.stream(biFunctions);
      stream.forEach(biFunction -> {
         AbstractSubMode subMode = (AbstractSubMode)biFunction.apply(module, this.modeValuesGroup);
         Supplier supplier = () -> {
            return subMode.getName().equals(this.abstractSubMode.getName());
         };
         subMode.setVisibilitySupplier(supplier);
         this.builtInModeNames.add(subMode.getName());
         this.modesByName.put(subMode.getName(), subMode);
         this.builtInModes.put(subMode.getName(), subMode);
         this.modesByClass.put(subMode.getClass(), subMode);
      });
      AbstractSubMode.getControlFlowState();
      String[] strings = (String[])this.builtInModeNames.toArray(new String[0]);
      this.setOptions(strings);
      this.abstractSubMode = this.resolveSubMode(str2);
      module.getToggleListeners().add(this);
      this.customModesDir = ClientFileUtil.WORKING_DIR
         + Augustus.getInstance().getName().toLowerCase(Locale.ROOT)
         + File.separator
         + "motionmodifier"
         + File.separator
         + this.ownerModule.getName().toLowerCase();
   }

   public ModeSetting(Module module, String str, String str2, DoubleValue doubleValue, BiFunction[] biFunctions) {
       super(module, str, str2, new String[0], doubleValue);
      this.builtInModes = new HashMap();
      AbstractSubMode.getControlFlowState();
      this.builtInModeNames = new ArrayList();
      this.modesByName = new ConcurrentHashMap();
      this.customModeNames = ConcurrentHashMap.newKeySet();
      this.modesByClass = new HashMap();
      this.ownerModule = module;
      DoubleValue doubleValue2 = new DoubleValue(module, str + "ModeValuesGroup", true, doubleValue);
      String str3 = "ModeValues";
      this.modeValuesGroup = (DoubleValue)doubleValue2.withDisplayName(str3);
      doubleValue2 = this.modeValuesGroup;
      SettingVisibilityCondition condition = () -> this.isNestedModeGroupVisible();
      this.modeValuesGroup = doubleValue2.visibleWhen(condition);
      Stream<BiFunction> stream = Arrays.stream(biFunctions);
      stream.forEach(biFunction -> {
         AbstractSubMode subMode = (AbstractSubMode)biFunction.apply(module, this.modeValuesGroup);
         Supplier supplier = () -> this.isSelectedSubMode(subMode);
         subMode.setVisibilitySupplier(supplier);
         this.builtInModeNames.add(subMode.getName());
         this.modesByName.put(subMode.getName(), subMode);
         this.builtInModes.put(subMode.getName(), subMode);
         this.modesByClass.put(subMode.getClass(), subMode);
      });
      String[] strings = (String[])this.builtInModeNames.toArray(new String[0]);
      this.setOptions(strings);
      this.abstractSubMode = this.resolveSubMode(str2);
      module.getToggleListeners().add(this);
      this.customModesDir = ClientFileUtil.WORKING_DIR
         + Augustus.getInstance().getName().toLowerCase(Locale.ROOT)
         + File.separator
         + "motionmodifier"
         + File.separator
         + this.ownerModule.getName().toLowerCase();
   }

   private void loadCustomModeAndRefresh(File file, CustomSubModeFactory customSubModeFactory) {
      ConcurrentHashMap modesByName2 = this.modesByName;
      this.registerCustomModeFromFile(modesByName2, file, customSubModeFactory);
      this.refreshModeOptions();
   }

   @Override
   public void onModuleToggled(boolean flag) {
      this.abstractSubMode.setEnabled(flag);
   }

   private Boolean isSelectedSubMode(AbstractSubMode subMode) {
      return subMode.getName().equals(this.abstractSubMode.getName());
   }

   public void reloadCustomModes(CustomSubModeFactory customSubModeFactory) {
      ClientFileUtil.ensureDirectory(this.customModesDir);
      File file = new File(this.customModesDir);
      AbstractSubMode.getControlFlowState();
      File[] files = file.listFiles(File::isFile);
      if (files != null) {
         HashMap map = new HashMap(this.builtInModes);

         for (File file2 : files) {
            this.registerCustomModeFromFile(map, file2, customSubModeFactory);
         }

         this.modesByName.clear();
         this.modesByName.putAll(map);
         this.refreshModeOptions();
      }
   }

   private void refreshModeOptions() {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      boolean flag = this.modesByName.keySet().stream().anyMatch(obj -> this.matchesSelectedMode((String)obj));
      if (!flag && !this.modesByName.isEmpty()) {
         String str = (String)this.modesByName.keySet().iterator().next();
         this.setSelectedValue(str);
      }

      String[] strings = ((Map<String, ?>)this.modesByName).keySet().stream().sorted(Comparator.comparing(String::toLowerCase)).toArray(String[]::new);
      this.setOptions(strings);
   }

   private void registerCustomModeFromFile(Map map, File file, CustomSubModeFactory customSubModeFactory) {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      String name = file.getName();
      short s2 = 28597;
      if (name.endsWith(".json")) {
         String str = name.substring(0, Math.max(name.length() - 5, 0));
         if (!map.containsKey(str) || this.customModeNames.contains(str)) {
            if (this.customModeNames.contains(str)) {
               AbstractSubMode subMode = (AbstractSubMode)map.get(str);
               boolean flag = false;
               subMode.setEnabled(flag);
            }

            DoubleValue modeValuesGroup2 = this.modeValuesGroup;
            Module ownerModule2 = this.ownerModule;
            MotionModifierProvider motionModifierProvider = (MotionModifierProvider)customSubModeFactory.create(ownerModule2, modeValuesGroup2, str);
            AbstractSubMode subMode2 = (AbstractSubMode)motionModifierProvider;
            Supplier supplier = () -> {
               return subMode2.getName().equals(this.abstractSubMode.getName());
            };
            subMode2.setVisibilitySupplier(supplier);
            Dummy dummy = motionModifierProvider.getDummy();
            this.loadCustomModeConfig(file, dummy);
            map.put(str, subMode2);
            this.customModeNames.add(str);
         }
      }
   }

   @Override
   public synchronized void notifyUserChanged() {
      SettingChangeListener listener = () -> this.onUserModeChanged();
      this.dispatchUserChangeListener(listener);
   }

   @Override
   public Setting withVisibility(SettingVisibilityCondition condition) {
      return this.visibleWhen(condition);
   }

   public boolean loadCustomModeConfig(File file, Dummy dummy) {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      String absolutePath = file.getParentFile().getAbsolutePath();
      String name = file.getName();
      Object obj = null;
      String str = name;
      JsonObject json = ClientFileUtil.readJsonObject(absolutePath, str, (Runnable)obj);
      if (json == null) {
         return false;
      } else {
         FeatureListJsonCodec featureListJsonCodec = CUSTOM_MODE_CODEC;
         Module[] modules = new Module[]{dummy};
         featureListJsonCodec.setFeatures2(modules);
         FeatureListJsonCodec featureListJsonCodec2 = CUSTOM_MODE_CODEC;
         UnaryFunction unaryFunction = obj2 -> true;
         boolean flag = true;
         boolean flag2 = featureListJsonCodec2.deserializeFromObject(json, flag, unaryFunction);
         if (!flag2) {
            System.out.println("Failed to read custom mode");
            return false;
         } else {
            return true;
         }
      }
   }

   private void onCustomModeFileDeleted(Path path) {
      File file = path.toFile();
      this.removeCustomModeFile(file);
   }

   public AbstractSubMode getSubMode(String str) {
      return (AbstractSubMode)this.modesByName.get(str);
   }

   public ModeSetting named(String str) {
      this.setDisplayName(str);
      return this;
   }

   public ModeSetting enableCustomModes(CustomSubModeFactory customSubModeFactory) {
      this.reloadCustomModes(customSubModeFactory);
      DirectoryWatcher directoryWatcher = DirectoryWatchService.watch(this.customModesDir);
      Consumer consumer = obj -> this.onCustomModeFileCreated(customSubModeFactory, (Path)obj);
      directoryWatcher = directoryWatcher.onCreate(consumer);
      Consumer consumer2 = obj2 -> {
         File file = ((Path)obj2).toFile();
         CompletableFuture.runAsync(() -> {
            ConcurrentHashMap modesByName2 = this.modesByName;
            this.registerCustomModeFromFile(modesByName2, file, customSubModeFactory);
            this.refreshModeOptions();
         });
      };
      directoryWatcher = directoryWatcher.onModify(consumer2);
      Consumer consumer3 = obj3 -> this.onCustomModeFileDeleted((Path)obj3);
      directoryWatcher.onDelete(consumer3).register();
      return this;
   }

   private boolean matchesSelectedMode(String str) {
      return this.getSelectedValue().equals(str);
   }

   private void removeCustomModeFile(File file) {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      String name = file.getName();
      if (name.endsWith(".json")) {
         String str = name.substring(0, Math.max(name.length() - 5, 0));
         if (this.customModeNames.contains(str)) {
            AbstractSubMode subMode = (AbstractSubMode)this.modesByName.get(str);
            boolean flag = false;
            subMode.setEnabled(flag);
         }

         this.customModeNames.remove(str);
         this.modesByName.remove(str);
         this.refreshModeOptions();
      }
   }

   @Override
   public void setSelectedValue(String str) {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      if (!str.equals(this.getSelectedValue())) {
         if (this.abstractSubMode != null) {
            boolean flag = false;
            this.abstractSubMode.setEnabled(flag);
         }

         this.abstractSubMode = this.resolveSubMode(str);
         if (this.abstractSubMode != null && this.getParentModule().isToggled()) {
            boolean flag2 = true;
            this.abstractSubMode.setEnabled(flag2);
         }
      }

      super.setSelectedValue(str);
   }

   private boolean isModeGroupVisible() {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      return this.modeValuesGroup.hasVisibleChildren() && this.isVisible();
   }

   @Override
   public synchronized void notifyValueChanged() {
      SettingChangeListener listener = () -> this.onModeValueChanged();
      this.dispatchChangeListener(listener);
   }

   private void onUserModeChanged() {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      if (this.abstractSubMode != null && this.getParentModule().isToggled()) {
         boolean flag = true;
         this.abstractSubMode.setEnabled(flag);
      }

      if (this.userChangeListener != null) {
         this.userChangeListener.onChanged();
      }
   }

   @Override
   public Setting withDisplayName(String str) {
      return this.named(str);
   }

   private void onCustomModeFileCreated(CustomSubModeFactory customSubModeFactory, Path path) {
      File file = path.toFile();
      CompletableFuture.runAsync(() -> this.loadCustomModeAndRefresh(file, customSubModeFactory));
   }

}
