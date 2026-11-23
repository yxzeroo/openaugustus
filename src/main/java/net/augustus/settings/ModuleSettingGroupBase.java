package net.augustus.settings;

import net.augustus.modules.Module;

public abstract class ModuleSettingGroupBase {
   private final Module parentModule;
   private final DoubleValue parentGroup;

   public ModuleSettingGroupBase(Module module) {
      this.parentModule = module;
      this.parentGroup = null;
   }

   public ModuleSettingGroupBase(Module module, DoubleValue doubleValue) {
      this.parentModule = module;
      this.parentGroup = doubleValue;
   }

   public Module getParentModule() {
      return this.parentModule;
   }

   public DoubleValue getParentGroup() {
      return this.parentGroup;
   }



}
