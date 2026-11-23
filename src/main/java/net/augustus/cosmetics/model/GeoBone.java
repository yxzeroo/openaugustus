package net.augustus.cosmetics.model;

import java.util.ArrayList;
import java.util.List;

public class GeoBone implements IGeoBone {
   public String name;
   private float scaleX;
   public float rotationPointZ;
   public float rotationPointX;
   private float rotateAngleY;
   public Boolean neverRender;
   public Boolean mirror;
   private float positionOffsetZ;
   private float rotateAngleZ;
   private float positionOffsetY;
   public boolean hidden;
   private float positionOffsetX;
   public List childCubes;
   private GeoBoneSnapshot initialSnapshot;
   public GeoBone parent;
   public Double inflate;
   public List childBones = new ArrayList();
   public Boolean reset;
   public float rotationPointY;
   public Object extraData;
   public int subModelIndex;
   private float scaleZ;
   private float rotateAngleX;
   private float scaleY;

   @Override
   public void setRotationY(float f) {
      this.rotateAngleY = f;
   }

   @Override
   public void setPivotY(float f) {
      this.rotationPointY = f;
   }

   @Override
   public float getScaleY() {
      return this.scaleY;
   }

   @Override
   public void setScaleY(float f) {
      this.scaleY = f;
   }

   @Override
   public float getScaleX() {
      return this.scaleX;
   }

   @Override
   public boolean isHidden() {
      return this.hidden;
   }

   @Override
   public void setName(String str) {
      this.name = str;
   }

   @Override
   public void setRotationX(float f) {
      this.rotateAngleX = f;
   }

   @Override
   public float getRotationX() {
      return this.rotateAngleX;
   }

   @Override
   public float getPositionY() {
      return this.positionOffsetY;
   }

   @Override
   public float getPivotZ() {
      return this.rotationPointZ;
   }

   @Override
   public float getPositionX() {
      return this.positionOffsetX;
   }

   @Override
   public void setPositionY(float f) {
      this.positionOffsetY = f;
   }

   @Override
   public float getRotationY() {
      return this.rotateAngleY;
   }

   @Override
   public void setScaleX(float f) {
      this.scaleX = f;
   }

   @Override
   public float getRotationZ() {
      return this.rotateAngleZ;
   }

   @Override
   public float getPivotY() {
      return this.rotationPointY;
   }

   @Override
   public void setRotationZ(float f) {
      this.rotateAngleZ = f;
   }

   @Override
   public void setPivotZ(float f) {
      this.rotationPointZ = f;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public void setPositionX(float f) {
      this.positionOffsetX = f;
   }

   @Override
   public void setHidden(boolean flag) {
      this.hidden = flag;
   }

   @Override
   public void setPivotX(float f) {
      this.rotationPointX = f;
   }

   @Override
   public float getScaleZ() {
      return this.scaleZ;
   }

   @Override
   public float getPivotX() {
      return this.rotationPointX;
   }

   @Override
   public void setScaleZ(float f) {
      this.scaleZ = f;
   }

   @Override
   public void saveInitialSnapshot() {
      if (this.initialSnapshot == null) {
         this.initialSnapshot = new GeoBoneSnapshot(this, true);
      }
   }

   public GeoBone() {
      this.childCubes = new ArrayList();
      this.subModelIndex = -1;
      this.scaleX = 1.0F;
      this.scaleY = 1.0F;
      this.scaleZ = 1.0F;
   }

   @Override
   public GeoBoneSnapshot getInitialSnapshot() {
      return this.initialSnapshot;
   }

   @Override
   public void setPositionZ(float f) {
      this.positionOffsetZ = f;
   }

   @Override
   public float getPositionZ() {
      return this.positionOffsetZ;
   }
}
