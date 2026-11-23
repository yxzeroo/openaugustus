package javazoom.jl.decoder;

public abstract class Obuffer {
   public static final int OBUFFERSIZE = 2304;
   public static final int MAXCHANNELS = 2;

   public abstract void write_buffer(int n);

   public abstract void set_stop_flag();

   public void appendSamples(int n, float[] floats) {
      int n2 = 0;

      while (n2 < 32) {
         short s = this.clip(floats[n2++]);
         this.append(n, s);
      }
   }

   public abstract void append(int n, short s);

   public abstract void clear_buffer();

   private final short clip(float f) {
      return f > 32767.0F ? 32767 : (f < -32768.0F ? -32768 : (short)((int)f));
   }

   public abstract void close();
}
