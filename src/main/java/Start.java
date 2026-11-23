import java.util.Arrays;
import net.minecraft.client.main.Main;

public class Start {

   public static Object[] concat(Object[] array, Object[] array2) {
      Object[] array3 = Arrays.copyOf(array, array.length + array2.length);
      System.arraycopy(array2, 0, array3, array.length, array2.length);
      return array3;
   }

   public static void main(String[] args) {
      Main.main(
         (String[])concat(new String[]{"--version", "beta", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args)
      );
   }
}
