package wdl;

import java.lang.reflect.Field;

public class ReflectionUtils {

   public static Object stealAndGetField(Object obj, Class clazz) {
      Class clazz2;
      if (obj instanceof Class) {
         clazz2 = (Class)obj;
         obj = null;
      } else {
         clazz2 = obj.getClass();
      }

      try {
         Field field = stealField(clazz2, clazz);
         return clazz.cast(field.get(obj));
      } catch (Exception e) {
         throw new RuntimeException("WorldDownloader: Couldn't get Field of type \"" + clazz + "\" from object \"" + obj + "\" !", e);
      }
   }

   public static Object stealAndGetField(Object obj, Class clazz, Class clazz2) {

      try {
         Field field = stealField(clazz, clazz2);
         return clazz2.cast(field.get(obj));
      } catch (Exception e) {
         throw new RuntimeException("WorldDownloader: Couldn't get Field of type \"" + clazz2 + "\" from object \"" + obj + "\" !", e);
      }
   }

   public static void stealAndSetField(Object obj, Class clazz, Object obj2) {
      Class clazz2;
      if (obj instanceof Class) {
         clazz2 = (Class)obj;
         obj = null;
      } else {
         clazz2 = obj.getClass();
      }

      try {
         Field field = stealField(clazz2, clazz);
         field.set(obj, obj2);
      } catch (Exception e) {
         throw new RuntimeException("WorldDownloader: Couldn't set Field of type \"" + clazz + "\" from object \"" + obj + "\" to " + obj2 + "!", e);
      }
   }

   public static Field stealField(Class clazz, Class clazz2) {
      Field[] declaredFields = clazz.getDeclaredFields();

      for (Field field : declaredFields) {
         if (field.getType().equals(clazz2)) {
            try {
               field.setAccessible(true);
               return field;
            } catch (Exception e) {
               throw new RuntimeException("WorldDownloader: Couldn't steal Field of type \"" + clazz2 + "\" from class \"" + clazz + "\" !", e);
            }
         }
      }

      throw new RuntimeException("WorldDownloader: Couldn't steal Field of type \"" + clazz2 + "\" from class \"" + clazz + "\" !");
   }

   public static void stealAndSetField(Object obj, Class clazz, Class clazz2, Object obj2) {

      try {
         Field field = stealField(clazz, clazz2);
         field.set(obj, obj2);
      } catch (Exception e) {
         throw new RuntimeException("WorldDownloader: Couldn't set Field of type \"" + clazz2 + "\" from object \"" + obj + "\" to " + obj2 + "!", e);
      }
   }
}
