package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.client.renderer.GlStateManager;

public class ItemCameraTransforms {
   public static final ItemCameraTransforms DEFAULT = new ItemCameraTransforms();
   public static float offsetTranslateX = 0.0F;
   public static float offsetTranslateY = 0.0F;
   public static float offsetTranslateZ = 0.0F;
   public static float offsetRotateX = 0.0F;
   public static float offsetRotateY = 0.0F;
   public static float offsetRotateZ = 0.0F;
   public static float offsetScaleX = 0.0F;
   public static float offsetScaleY = 0.0F;
   public static float offsetScaleZ = 0.0F;
   public final ItemTransformVec3f thirdPerson;
   public final ItemTransformVec3f firstPerson;
   public final ItemTransformVec3f head;
   public final ItemTransformVec3f gui;
   public final ItemTransformVec3f ground;
   public final ItemTransformVec3f fixed;

   public void applyTransform(ItemCameraTransforms.TransformType transformType) {
      ItemTransformVec3f transform = this.getTransform(transformType);
      if (transform != ItemTransformVec3f.DEFAULT) {
         GlStateManager.translate(transform.translation.x + offsetTranslateX, transform.translation.y + offsetTranslateY, transform.translation.z + offsetTranslateZ);
         GlStateManager.rotate(transform.rotation.y + offsetRotateY, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(transform.rotation.x + offsetRotateX, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(transform.rotation.z + offsetRotateZ, 0.0F, 0.0F, 1.0F);
         GlStateManager.scale(transform.scale.x + offsetScaleX, transform.scale.y + offsetScaleY, transform.scale.z + offsetScaleZ);
      }
   }

   public ItemCameraTransforms(ItemCameraTransforms itemCameraTransforms) {
      this.thirdPerson = itemCameraTransforms.thirdPerson;
      this.firstPerson = itemCameraTransforms.firstPerson;
      this.head = itemCameraTransforms.head;
      this.gui = itemCameraTransforms.gui;
      this.ground = itemCameraTransforms.ground;
      this.fixed = itemCameraTransforms.fixed;
   }

   private ItemCameraTransforms() {
      this(
         ItemTransformVec3f.DEFAULT,
         ItemTransformVec3f.DEFAULT,
         ItemTransformVec3f.DEFAULT,
         ItemTransformVec3f.DEFAULT,
         ItemTransformVec3f.DEFAULT,
         ItemTransformVec3f.DEFAULT
      );
   }

   public ItemCameraTransforms(
      ItemTransformVec3f itemTransformVec3f, ItemTransformVec3f itemTransformVec3f2, ItemTransformVec3f itemTransformVec3f3, ItemTransformVec3f itemTransformVec3f4, ItemTransformVec3f itemTransformVec3f5, ItemTransformVec3f itemTransformVec3f6
   ) {
      this.thirdPerson = itemTransformVec3f;
      this.firstPerson = itemTransformVec3f2;
      this.head = itemTransformVec3f3;
      this.gui = itemTransformVec3f4;
      this.ground = itemTransformVec3f5;
      this.fixed = itemTransformVec3f6;
   }

   public boolean hasCustomTransform(ItemCameraTransforms.TransformType transformType) {
      return !this.getTransform(transformType).equals(ItemTransformVec3f.DEFAULT);
   }

   public ItemTransformVec3f getTransform(ItemCameraTransforms.TransformType transformType) {
      switch (ItemCameraTransforms.TransformTypeSwitchMap.ORDINAL_MAP[transformType.ordinal()]) {
         case 1:
            return this.thirdPerson;
         case 2:
            return this.firstPerson;
         case 3:
            return this.head;
         case 4:
            return this.gui;
         case 5:
            return this.ground;
         case 6:
            return this.fixed;
         default:
            return ItemTransformVec3f.DEFAULT;
      }
   }


   public static class TransformTypeSwitchMap {
      public static final int[] ORDINAL_MAP = new int[ItemCameraTransforms.TransformType.values().length];

      static {
         try {
            ORDINAL_MAP[ItemCameraTransforms.TransformType.THIRD_PERSON.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            ORDINAL_MAP[ItemCameraTransforms.TransformType.FIRST_PERSON.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            ORDINAL_MAP[ItemCameraTransforms.TransformType.HEAD.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }

         try {
            ORDINAL_MAP[ItemCameraTransforms.TransformType.GUI.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }

         try {
            ORDINAL_MAP[ItemCameraTransforms.TransformType.GROUND.ordinal()] = 5;
         } catch (NoSuchFieldError e5) {
         }

         try {
            ORDINAL_MAP[ItemCameraTransforms.TransformType.FIXED.ordinal()] = 6;
         } catch (NoSuchFieldError e6) {
         }
      }
   }

   public static class Deserializer implements JsonDeserializer {
      public Object deserialize(JsonElement elem, Type type, JsonDeserializationContext context) throws JsonParseException {
         return this.deserializeItemCameraTransforms(elem, type, context);
      }

      public ItemCameraTransforms deserializeItemCameraTransforms(JsonElement elem, Type type, JsonDeserializationContext context) throws JsonParseException {
         JsonObject asJsonObject = elem.getAsJsonObject();
         ItemTransformVec3f transform = this.getTransform(context, asJsonObject, "thirdperson");
         ItemTransformVec3f transform2 = this.getTransform(context, asJsonObject, "firstperson");
         ItemTransformVec3f transform3 = this.getTransform(context, asJsonObject, "head");
         ItemTransformVec3f transform4 = this.getTransform(context, asJsonObject, "gui");
         ItemTransformVec3f transform5 = this.getTransform(context, asJsonObject, "ground");
         ItemTransformVec3f transform6 = this.getTransform(context, asJsonObject, "fixed");
         return new ItemCameraTransforms(transform, transform2, transform3, transform4, transform5, transform6);
      }

      private ItemTransformVec3f getTransform(JsonDeserializationContext context, JsonObject json, String str) {
         return json.has(str) ? (ItemTransformVec3f)context.deserialize(json.get(str), ItemTransformVec3f.class) : ItemTransformVec3f.DEFAULT;
      }
   }

   public enum TransformType {
      NONE,
      THIRD_PERSON,
      FIRST_PERSON,
      HEAD,
      GUI,
      GROUND,
      FIXED;

   }
}
