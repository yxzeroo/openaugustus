package net.augustus.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class BipedSkeletonRenderer extends AbstractSkeletonRenderer {
   public ModelRenderer getHead2(ModelBiped modelBiped) {
      return modelBiped.bipedHead;
   }

   public ModelRenderer getBody2(ModelBiped modelBiped) {
      return modelBiped.bipedBody;
   }

   public ModelRenderer getRightLeg2(ModelBiped modelBiped) {
      return modelBiped.bipedRightLeg;
   }

   public ModelRenderer getLeftLeg2(ModelBiped modelBiped) {
      return modelBiped.bipedLeftLeg;
   }

   public ModelRenderer getRightArm2(ModelBiped modelBiped) {
      return modelBiped.bipedRightArm;
   }

   public ModelRenderer getLeftArm2(ModelBiped modelBiped) {
      return modelBiped.bipedLeftArm;
   }

   @Override
   public ModelRenderer getLeftArm(ModelBase modelBase) {
      ModelBiped modelBiped = (ModelBiped)modelBase;
      return this.getLeftArm2(modelBiped);
   }

   @Override
   public ModelRenderer getRightArm(ModelBase modelBase) {
      ModelBiped modelBiped = (ModelBiped)modelBase;
      return this.getRightArm2(modelBiped);
   }

   @Override
   public ModelRenderer getLeftLeg(ModelBase modelBase) {
      ModelBiped modelBiped = (ModelBiped)modelBase;
      return this.getLeftLeg2(modelBiped);
   }

   @Override
   public ModelRenderer getRightLeg(ModelBase modelBase) {
      ModelBiped modelBiped = (ModelBiped)modelBase;
      return this.getRightLeg2(modelBiped);
   }

   @Override
   public ModelRenderer getBody(ModelBase modelBase) {
      ModelBiped modelBiped = (ModelBiped)modelBase;
      return this.getBody2(modelBiped);
   }

   @Override
   public ModelRenderer getHead(ModelBase modelBase) {
      ModelBiped modelBiped = (ModelBiped)modelBase;
      return this.getHead2(modelBiped);
   }
}
