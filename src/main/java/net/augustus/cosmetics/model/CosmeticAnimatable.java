package net.augustus.cosmetics.model;

import java.util.EnumMap;

public class CosmeticAnimatable implements GeoAnimatable {
   public GeoAnimationFactory factory;
   private EnumMap triggerAnimations = new EnumMap<>(CosmeticAnimationTrigger.class);
   private String currentAnimation;
   private String defaultAnimation;
   private String controllerName;
   private GeoAnimationController controller;
   private AnimatedGeoModel animatedModel;

   @Override
   public GeoAnimationFactory getFactory() {
      return this.factory;
   }

   public boolean hasAnimation(String str) {
      return this.animatedModel.getAnimationFile().getAnimation(str) != null;
   }

   public AnimatedGeoModel getAnimatedModel() {
      return this.animatedModel;
   }

   public CosmeticAnimatable(AnimatedGeoModel animatedGeoModel) {
      this.factory = new GeoAnimationFactory(this);
      this.controllerName = "mainController";
      this.animatedModel = animatedGeoModel;
   }

   public void setTriggerAnimation(CosmeticAnimationTrigger cosmeticAnimationTrigger) {
      String str = (String)this.triggerAnimations.get(cosmeticAnimationTrigger);
      if (!this.currentAnimation.equals(str)) {
         str = str == null ? this.defaultAnimation : str;
         if (this.hasAnimation(str)) {
            this.currentAnimation = str;
            GeoAnimationController controller2 = this.controller;
            GeoAnimationBuilder geoAnimationBuilder = new GeoAnimationBuilder();
            Boolean bool = true;
            GeoAnimationBuilder geoAnimationBuilder2 = geoAnimationBuilder.addAnimation(str, bool);
            controller2.setAnimation(geoAnimationBuilder2);
         }
      }
   }

   @Override
   public void registerControllers(GeoAnimationData geoAnimationData) {
      this.controller = new GeoAnimationController(this, this.controllerName, 5.0F, geoAnimationEvent -> GeoPlayState.CONTINUE);
      GeoAnimationFile animationFile = this.animatedModel.getAnimationFile();
      String firstAnimationName = animationFile.getFirstAnimationName();
      if (firstAnimationName != null) {
         this.defaultAnimation = firstAnimationName;
         this.currentAnimation = firstAnimationName;
         GeoAnimationController controller2 = this.controller;
         GeoAnimationBuilder geoAnimationBuilder = new GeoAnimationBuilder();
         Boolean bool = true;
         GeoAnimationBuilder geoAnimationBuilder2 = geoAnimationBuilder.addAnimation(firstAnimationName, bool);
         controller2.setAnimation(geoAnimationBuilder2);

         for (CosmeticAnimationTrigger cosmeticAnimationTrigger : CosmeticAnimationTrigger.VALUES) {
            EnumMap triggerAnimations2 = this.triggerAnimations;
            String animationName = cosmeticAnimationTrigger.getAnimationName();
            triggerAnimations2.put(cosmeticAnimationTrigger, this.hasAnimation(animationName) ? cosmeticAnimationTrigger.getAnimationName() : this.defaultAnimation);
         }
      }

      GeoAnimationController controller3 = this.controller;
      geoAnimationData.addAnimationController(controller3);
   }

   public boolean hasDefaultAnimation() {
      return this.defaultAnimation != null;
   }




}
