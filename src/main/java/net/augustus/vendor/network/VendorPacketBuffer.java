package net.augustus.vendor.network;

import net.augustus.vendor.OnlineConfigMetadata;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ByteProcessor;
import io.netty.util.ReferenceCounted;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

public class VendorPacketBuffer extends ByteBuf {
   private final ByteBuf delegateBuf;
   private static String opaqueStateTag;

   @Override
   public ByteBuf setZero(int n, int n2) {
      return this.delegateBuf.setZero(n, n2);
   }

   @Override
   public String toString() {
      return this.delegateBuf.toString();
   }

   @Override
   public ByteBuf writeInt(int n) {
      return this.delegateBuf.writeInt(n);
   }

   @Override
   public ByteBuf asReadOnly() {
      return this.delegateBuf.asReadOnly();
   }

   @Override
   public int readUnsignedShortLE() {
      return this.delegateBuf.readUnsignedShortLE();
   }

   @Override
   public int readBytes(GatheringByteChannel gatheringByteChannel, int n) throws IOException {
      return this.delegateBuf.readBytes(gatheringByteChannel, n);
   }

   @Override
   public short getShort(int n) {
      return this.delegateBuf.getShort(n);
   }

   public List readByteArrayList() {
      int n = 0;
      getOpaqueStateTag();
      Object obj = null;
      boolean flag = false;
      n = this.readInt();
      obj = new ArrayList(n);

      for (int i = 0; i < n; i++) {
         ((ArrayList)obj).add(this.readByteArray());
      }

      return (ArrayList)obj;
   }

   @Override
   public long getUnsignedInt(int n) {
      return this.delegateBuf.getUnsignedInt(n);
   }

   @Override
   public int hashCode() {
      return this.delegateBuf.hashCode();
   }

   @Override
   public ByteBuffer[] nioBuffers(int n, int n2) {
      return this.delegateBuf.nioBuffers(n, n2);
   }

   @Override
   public ByteBuf retain(int n) {
      return this.delegateBuf.retain(n);
   }

   @Override
   public ByteBuf writeBytes(ByteBuffer buf) {
      return this.delegateBuf.writeBytes(buf);
   }

   @Override
   public long readLongLE() {
      return this.delegateBuf.readLongLE();
   }

   @Override
   public ByteBuf getBytes(int n, byte[] bytes) {
      return this.delegateBuf.getBytes(n, bytes);
   }

   public String readUtfString() {
      int n = 0;
      Object obj = null;
      String opaqueStateTag2 = getOpaqueStateTag();
      n = this.delegateBuf.readInt();
      VendorPacketBuffer vendorPacketBuffer = this;
      int n2 = this.delegateBuf.readerIndex();
      Charset utF8 = StandardCharsets.UTF_8;
      int n3 = n2;
      obj = vendorPacketBuffer.toString(n3, n, utF8);
      int n4 = this.readerIndex() + n;
      this.readerIndex(n4);
      return (String)obj;
   }

   public void writeBooleanMap(Map map) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      getOpaqueStateTag();
      boolean flag = false;
      VendorPacketBuffer vendorPacketBuffer = this;
      int size = map.size();
      vendorPacketBuffer.writeInt(size);
      obj3 = map.entrySet().iterator();

      while (((Iterator)obj3).hasNext()) {
         obj2 = (Entry)((Iterator)obj3).next();
         obj = (String)((Entry)obj2).getKey();
         flag = (Boolean)((Entry)obj2).getValue();
         vendorPacketBuffer = this;
         String str = (String)obj;
         vendorPacketBuffer.writeUtfString(str);
         this.writeBoolean(flag);
      }
   }

   @Override
   public ByteBuffer[] nioBuffers() {
      return this.delegateBuf.nioBuffers();
   }

   @Override
   public short getUnsignedByte(int n) {
      return this.delegateBuf.getUnsignedByte(n);
   }

   public void writeStringMatrix(String[][] stringsArr) {
      Object obj = null;
      Object obj2 = null;
      getOpaqueStateTag();
      boolean flag = false;
      int n = 0;
      this.delegateBuf.writeInt(stringsArr.length);
      obj = stringsArr;
      n = ((String[][])obj).length;

      for (int i = 0; i < n; i++) {
         obj2 = ((String[][])obj)[i];
         VendorPacketBuffer vendorPacketBuffer = this;
         String[] strings = (String[])obj2;
         vendorPacketBuffer.writeStringArray(strings);
      }
   }

   @Override
   public boolean hasArray() {
      return this.delegateBuf.hasArray();
   }

   @Override
   public ByteBuf ensureWritable(int n) {
      return this.delegateBuf.ensureWritable(n);
   }

   @Override
   public int setBytes(int n, InputStream in, int n2) throws IOException {
      return this.delegateBuf.setBytes(n, in, n2);
   }

   public static int getVarIntSize(int n) {
      boolean flag = false;
      String opaqueStateTag2 = getOpaqueStateTag();

      for (int i = 1; i < 5; i++) {
         if ((n & -1 << i * 7) == 0) {
            return i;
         }
      }

      return 5;
   }

   @Override
   public ByteBuf setBytes(int n, ByteBuf buf, int n2) {
      return this.delegateBuf.setBytes(n, buf, n2);
   }

   @Override
   public ByteBuffer nioBuffer(int n, int n2) {
      return this.delegateBuf.nioBuffer(n, n2);
   }

   public byte[][] readByteMatrix() {
      boolean flag = false;
      int n = 0;
      getOpaqueStateTag();
      Object obj = null;
      n = this.readInt();
      obj = new byte[n][];

      for (int i = 0; i < n; i++) {
         ((byte[][])obj)[i] = this.readByteArray();
      }

      return (byte[][])obj;
   }

   @Override
   public ByteBuf touch(Object obj) {
      return this.delegateBuf.touch(obj);
   }

   @Override
   public ByteBuf writeBoolean(boolean flag) {
      return this.delegateBuf.writeBoolean(flag);
   }

   @Override
   public ByteBuf readBytes(int n) {
      return this.delegateBuf.readBytes(n);
   }

   @Override
   public short getShortLE(int n) {
      return this.delegateBuf.getShortLE(n);
   }

   @Override
   public int readUnsignedMedium() {
      return this.delegateBuf.readUnsignedMedium();
   }

   @Override
   public ByteBuf writeLong(long l) {
      return this.delegateBuf.writeLong(l);
   }

   @Override
   public ByteBuf readSlice(int n) {
      return this.delegateBuf.readSlice(n);
   }

   @Override
   public int writableBytes() {
      return this.delegateBuf.writableBytes();
   }

   @Override
   public boolean isWritable() {
      return this.delegateBuf.isWritable();
   }

   @Override
   public ByteBuf order(ByteOrder byteOrder) {
      return this.delegateBuf.order(byteOrder);
   }

   @Override
   public int readIntLE() {
      return this.delegateBuf.readIntLE();
   }

   @Override
   public ByteBuf retainedDuplicate() {
      return this.delegateBuf.retainedDuplicate();
   }

   public void writeBooleanArray(boolean[] flags) {
      int n = 0;
      getOpaqueStateTag();
      boolean flag = false;
      boolean flag2 = false;
      Object obj = null;
      this.delegateBuf.writeInt(flags.length);
      obj = flags;
      n = ((boolean[])obj).length;

      for (int i = 0; i < n; i++) {
         flag2 = ((boolean[])obj)[i];
         this.writeBoolean(flag2);
      }
   }

   @Override
   public int capacity() {
      return this.delegateBuf.capacity();
   }

   public void writeIntArrayMap(Map map) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      getOpaqueStateTag();
      Object obj4 = null;
      VendorPacketBuffer vendorPacketBuffer = this;
      int size = map.size();
      vendorPacketBuffer.writeInt(size);
      obj2 = map.entrySet().iterator();

      while (((Iterator)obj2).hasNext()) {
         obj3 = (Entry)((Iterator)obj2).next();
         obj4 = (String)((Entry)obj3).getKey();
         obj = (int[])((Entry)obj3).getValue();
         vendorPacketBuffer = this;
         String str = (String)obj4;
         vendorPacketBuffer.writeUtfString(str);
         vendorPacketBuffer = this;
         int[] ints = (int[])obj;
         vendorPacketBuffer.writeIntArray(ints);
      }
   }

   @Override
   public ByteBuf unwrap() {
      return this.delegateBuf.unwrap();
   }

   @Override
   public ByteBuf getBytes(int n, OutputStream out, int n2) throws IOException {
      return this.delegateBuf.getBytes(n, out, n2);
   }

   public UUID readUuid() {
      return new UUID(this.delegateBuf.readLong(), this.delegateBuf.readLong());
   }

   @Override
   public int readMediumLE() {
      return this.delegateBuf.readMediumLE();
   }

   @Override
   public ByteBuf clear() {
      return this.delegateBuf.clear();
   }

   public void writeNullTerminatedAscii(String str) {
      String opaqueStateTag2 = getOpaqueStateTag();
      this.delegateBuf.writeBytes(str.getBytes(StandardCharsets.US_ASCII));
      this.delegateBuf.writeByte(0);
   }

   @Override
   public int readInt() {
      return this.delegateBuf.readInt();
   }

   @Override
   public double getDouble(int n) {
      return this.delegateBuf.getDouble(n);
   }

   @Override
   public ByteBuf writeBytes(ByteBuf buf, int n, int n2) {
      return this.delegateBuf.writeBytes(buf, n, n2);
   }

   @Override
   public char readChar() {
      return this.delegateBuf.readChar();
   }

   @Override
   public ByteBuf retainedSlice(int n, int n2) {
      return this.delegateBuf.retainedSlice(n, n2);
   }

   @Override
   public ByteBuf markWriterIndex() {
      return this.delegateBuf.markWriterIndex();
   }

   @Override
   public boolean isDirect() {
      return this.delegateBuf.isDirect();
   }

   public Map readBooleanArrayMap() {
      Object obj = null;
      Object obj2 = null;
      getOpaqueStateTag();
      int n = 0;
      int n2 = 0;
      Object obj3 = null;
      long l = 0L;
      l = 1981372225247989150L ^ (long)"\u09da얜\uef2bｃᄓ䴕೫Ἥ犲쑍\ue76b䐕킐Ꟙⱕ鳂げ⏍\ue79f뭧据\ud990粷♌䙶笒侜ଗ橓".hashCode();
      VendorPacketBuffer vendorPacketBuffer = this;
      n = vendorPacketBuffer.readInt();
      l = (l ^ 3461460007269052463L) - -7068015369140734702L;
      obj2 = new HashMap(n);
      l = l + -3017222203119655406L ^ 8120046819135230193L;
      n2 = 0;
      l = (l ^ -5126877106507739953L) - -6899447949661006511L;

      while (n2 < n) {
         l = (l ^ -6831338278753148989L) + -4622935267813527331L;
         vendorPacketBuffer = this;
         obj = vendorPacketBuffer.readUtfString();
         l = (l ^ 5980902874654115021L) - 3953817347601328833L;
         obj3 = this.readBooleanArray();
         l = (l ^ 6690419213248406381L) + 7992326183859405642L;
         ((HashMap)obj2).put((String)obj, (boolean[])obj3);
         l = l + -3138820457569005979L ^ -1965019837503226718L;
         n2++;
         l = l - 2819353925247318331L ^ -619543720097281903L;
         l = (l ^ -3348795369733171450L) + 5086544542373694488L;
      }

      l = l + -5752593986155579828L ^ 4985404933229721401L;
      return (HashMap)obj2;
   }

   @Override
   public int forEachByteDesc(int n, int n2, ByteProcessor byteProcessor) {
      return this.delegateBuf.forEachByteDesc(n, n2, byteProcessor);
   }

   @Override
   public int readUnsignedShort() {
      return this.delegateBuf.readUnsignedShort();
   }

   @Override
   public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
      return this.delegateBuf.getBytes(n, gatheringByteChannel, n2);
   }

   @Override
   public ByteBuf setIntLE(int n, int n2) {
      return this.delegateBuf.setIntLE(n, n2);
   }

   @Override
   public int forEachByteDesc(ByteProcessor byteProcessor) {
      return this.delegateBuf.forEachByteDesc(byteProcessor);
   }

   public void writeByteArrayList(List list) {
      Object obj = null;
      getOpaqueStateTag();
      Object obj2 = null;
      VendorPacketBuffer vendorPacketBuffer = this;
      int size = list.size();
      vendorPacketBuffer.writeInt(size);
      obj = list.iterator();

      while (((Iterator)obj).hasNext()) {
         obj2 = (byte[])((Iterator)obj).next();
         vendorPacketBuffer = this;
         byte[] bytes = (byte[])obj2;
         vendorPacketBuffer.writeByteArray(bytes);
      }
   }

   @Override
   public ByteBuf getBytes(int n, ByteBuf buf, int n2, int n3) {
      return this.delegateBuf.getBytes(n, buf, n2, n3);
   }

   @Override
   public ByteBuf writeDouble(double d) {
      return this.delegateBuf.writeDouble(d);
   }

   @Override
   public ByteBuf readBytes(ByteBuf buf) {
      return this.delegateBuf.readBytes(buf);
   }

   @Override
   public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
      return this.delegateBuf.setBytes(n, scatteringByteChannel, n2);
   }

   @Override
   public String toString(int n, int n2, Charset charset) {
      return this.delegateBuf.toString(n, n2, charset);
   }

   @Override
   public int bytesBefore(byte b) {
      return this.delegateBuf.bytesBefore(b);
   }

   @Override
   public ByteBuf setMedium(int n, int n2) {
      return this.delegateBuf.setMedium(n, n2);
   }

   @Override
   public ByteBuf writeZero(int n) {
      return this.delegateBuf.writeZero(n);
   }

   public void writeDoubleMatrix(double[][] doublesArr) {
      Object obj = null;
      getOpaqueStateTag();
      Object obj2 = null;
      boolean flag = false;
      int n = 0;
      this.delegateBuf.writeInt(doublesArr.length);
      obj2 = doublesArr;
      n = ((double[][])obj2).length;

      for (int i = 0; i < n; i++) {
         obj = ((double[][])obj2)[i];
         VendorPacketBuffer vendorPacketBuffer = this;
         double[] doubles = (double[])obj;
         vendorPacketBuffer.writeDoubleArray(doubles);
      }
   }

   @Override
   public ByteBuf setLongLE(int n, long l) {
      return this.delegateBuf.setLongLE(n, l);
   }

   @Override
   public long readLong() {
      return this.delegateBuf.readLong();
   }

   @Override
   public ByteBuf setBytes(int n, ByteBuf buf) {
      return this.delegateBuf.setBytes(n, buf);
   }

   @Override
   public ByteBuf writeShortLE(int n) {
      return this.delegateBuf.writeShortLE(n);
   }

   @Override
   public int forEachByte(ByteProcessor byteProcessor) {
      return this.delegateBuf.forEachByte(byteProcessor);
   }

   @Override
   public ByteBuf markReaderIndex() {
      return this.delegateBuf.markReaderIndex();
   }

   @Override
   public ByteBuf readBytes(ByteBuf buf, int n, int n2) {
      return this.delegateBuf.readBytes(buf, n, n2);
   }

   @Override
   public ByteBuf setBytes(int n, ByteBuf buf, int n2, int n3) {
      return this.delegateBuf.setBytes(n, buf, n2, n3);
   }

   @Override
   public long getUnsignedIntLE(int n) {
      return this.delegateBuf.getUnsignedIntLE(n);
   }

   @Override
   public int maxCapacity() {
      return this.delegateBuf.maxCapacity();
   }

   public void writeIntArray(int[] ints) {
      boolean flag = false;
      getOpaqueStateTag();
      int n = 0;
      Object obj = null;
      int n2 = 0;
      this.delegateBuf.writeInt(ints.length);
      obj = ints;
      n = ((int[])obj).length;

      for (int i = 0; i < n; i++) {
         n2 = ((int[])obj)[i];
         this.writeInt(n2);
      }
   }

   public int readVarInt() {
      byte b = 0;
      int n = 0;
      int n2 = 0;
      String opaqueStateTag2 = getOpaqueStateTag();
      n = 0;
      n2 = 0;

      do {
         b = this.readByte();
         n |= (b & 127) << n2++ * 7;
         if (n2 > 5) {
            throw new RuntimeException("VarInt too big");
         }
      } while ((b & 128) == 128);

      return n;
   }

   @Override
   public ByteBuf setInt(int n, int n2) {
      return this.delegateBuf.setInt(n, n2);
   }

   @Override
   public ByteBuf copy() {
      return this.delegateBuf.copy();
   }

   public ReferenceCounted touch_m1(Object obj) {
      return this.touch(obj);
   }

   @Override
   public boolean isReadable(int n) {
      return this.delegateBuf.isReadable(n);
   }

   @Override
   public int getUnsignedMedium(int n) {
      return this.delegateBuf.getUnsignedMedium(n);
   }

   public String readNullTerminatedAscii() {
      byte b = 0;
      getOpaqueStateTag();
      Object obj = null;
      obj = new StringBuilder();

      while ((b = this.delegateBuf.readByte()) != 0) {
         ((StringBuilder)obj).append((char)b);
      }

      return ((StringBuilder)obj).toString();
   }

   @Override
   public int readMedium() {
      return this.delegateBuf.readMedium();
   }

   @Override
   public ByteBuf writeByte(int n) {
      return this.delegateBuf.writeByte(n);
   }

   @Override
   public ByteBuffer nioBuffer() {
      return this.delegateBuf.nioBuffer();
   }

   @Override
   public int getMedium(int n) {
      return this.delegateBuf.getMedium(n);
   }

   public List readOnlineConfigList() {
      boolean flag = false;
      Object obj = null;
      Object obj2 = null;
      boolean flag2 = false;
      Object obj3 = null;
      getOpaqueStateTag();
      int n = 0;
      Object obj4 = null;
      Object obj5 = null;
      n = this.readInt();
      obj2 = new ArrayList(n);

      for (int i = 0; i < n; i++) {
         obj = this.readUtfString();
         obj4 = this.readUtfString();
         obj3 = this.readUtfString();
         obj5 = this.readUtfString();
         flag2 = this.readBoolean();
         ((ArrayList)obj2).add(new OnlineConfigMetadata((String)obj, (String)obj4, (String)obj3, (String)obj5, flag2));
      }

      return (ArrayList)obj2;
   }

   @Override
   public char getChar(int n) {
      return this.delegateBuf.getChar(n);
   }

   @Override
   public CharSequence readCharSequence(int n, Charset charset) {
      return this.delegateBuf.readCharSequence(n, charset);
   }

   @Override
   public int ensureWritable(int n, boolean flag) {
      return this.delegateBuf.ensureWritable(n, flag);
   }

   @Override
   public int readerIndex() {
      return this.delegateBuf.readerIndex();
   }

   public double[] readDoubleArray() {
      int n = 0;
      boolean flag = false;
      getOpaqueStateTag();
      Object obj = null;
      n = this.delegateBuf.readInt();
      if (n >= 400) {
         return null;
      } else {
         obj = new double[n];

         for (int i = 0; i < n; i++) {
            ((double[])obj)[i] = this.readDouble();
         }

         return (double[])obj;
      }
   }

   @Override
   public ByteBuf setBoolean(int n, boolean flag) {
      return this.delegateBuf.setBoolean(n, flag);
   }

   @Override
   public int compareTo(ByteBuf other) {
      return this.delegateBuf.compareTo(other);
   }

   @Override
   public long getLong(int n) {
      return this.delegateBuf.getLong(n);
   }

   public void writeByteArray(byte[] bytes) {
      String opaqueStateTag2 = getOpaqueStateTag();
      VendorPacketBuffer vendorPacketBuffer = this;
      int length = bytes.length;
      vendorPacketBuffer.writeInt(length);
      this.delegateBuf.writeBytes(bytes);
   }

   @Override
   public byte getByte(int n) {
      return this.delegateBuf.getByte(n);
   }

   @Override
   public long readUnsignedInt() {
      return this.delegateBuf.readUnsignedInt();
   }

   @Override
   public float readFloat() {
      return this.delegateBuf.readFloat();
   }

   @Override
   public ByteBuf setBytes(int n, byte[] bytes) {
      return this.delegateBuf.setBytes(n, bytes);
   }

   public void writeIntMatrix(int[][] intsArr) {
      Object obj = null;
      boolean flag = false;
      getOpaqueStateTag();
      Object obj2 = null;
      int n = 0;
      this.delegateBuf.writeInt(intsArr.length);
      obj2 = intsArr;
      n = ((int[][])obj2).length;

      for (int i = 0; i < n; i++) {
         obj = ((int[][])obj2)[i];
         VendorPacketBuffer vendorPacketBuffer = this;
         int[] ints = (int[])obj;
         vendorPacketBuffer.writeIntArray(ints);
      }
   }

   public void writeBooleanMatrix(boolean[][] flagsArr) {
      boolean flag = false;
      Object obj = null;
      getOpaqueStateTag();
      Object obj2 = null;
      int n = 0;
      this.delegateBuf.writeInt(flagsArr.length);
      obj2 = flagsArr;
      n = ((boolean[][])obj2).length;

      for (int i = 0; i < n; i++) {
         obj = ((boolean[][])obj2)[i];
         VendorPacketBuffer vendorPacketBuffer = this;
         boolean[] flags = (boolean[])obj;
         vendorPacketBuffer.writeBooleanArray(flags);
      }
   }

   @Override
   public int bytesBefore(int n, byte b) {
      return this.delegateBuf.bytesBefore(n, b);
   }

   @Override
   public ByteBuf setBytes(int n, byte[] bytes, int n2, int n3) {
      return this.delegateBuf.setBytes(n, bytes, n2, n3);
   }

   @Override
   public ByteBuf writeMediumLE(int n) {
      return this.delegateBuf.writeMediumLE(n);
   }

   @Override
   public ByteBuf readBytes(byte[] bytes) {
      return this.delegateBuf.readBytes(bytes);
   }

   @Override
   public ByteBuf duplicate() {
      return this.delegateBuf.duplicate();
   }

   @Override
   public ByteBuffer internalNioBuffer(int n, int n2) {
      return this.delegateBuf.internalNioBuffer(n, n2);
   }

   @Override
   public ByteBuf getBytes(int n, ByteBuffer buf) {
      return this.delegateBuf.getBytes(n, buf);
   }

   public ReferenceCounted retain_m1(int n) {
      return this.retain(n);
   }

   @Override
   public int setCharSequence(int n, CharSequence text, Charset charset) {
      return this.delegateBuf.setCharSequence(n, text, charset);
   }

   @Override
   public byte readByte() {
      return this.delegateBuf.readByte();
   }

   @Override
   public ByteBuf copy(int n, int n2) {
      return this.delegateBuf.copy(n, n2);
   }

   @Override
   public ByteBuf readBytes(OutputStream out, int n) throws IOException {
      return this.delegateBuf.readBytes(out, n);
   }

   public boolean[][] readBooleanMatrix() {
      Object obj = null;
      getOpaqueStateTag();
      boolean flag = false;
      int n = 0;
      n = this.delegateBuf.readInt();
      if (n >= 400) {
         return (boolean[][])null;
      } else {
         obj = new boolean[n][];

         for (int i = 0; i < n; i++) {
            ((boolean[][])obj)[i] = this.readBooleanArray();
         }

         return (boolean[][])obj;
      }
   }

   @Override
   public ByteBuf setDouble(int n, double d) {
      return this.delegateBuf.setDouble(n, d);
   }

   @Override
   public ByteBuf slice(int n, int n2) {
      return this.delegateBuf.slice(n, n2);
   }

   public ReferenceCounted retain_m1() {
      return this.retain();
   }

   @Override
   public int readUnsignedMediumLE() {
      return this.delegateBuf.readUnsignedMediumLE();
   }

   @Override
   public int writerIndex() {
      return this.delegateBuf.writerIndex();
   }

   @Override
   public ByteBuf setFloat(int n, float f) {
      return this.delegateBuf.setFloat(n, f);
   }

   @Override
   public boolean isReadable() {
      return this.delegateBuf.isReadable();
   }

   @Override
   public ByteBuf readBytes(ByteBuf buf, int n) {
      return this.delegateBuf.readBytes(buf, n);
   }

   @Override
   public int writeBytes(InputStream in, int n) throws IOException {
      return this.delegateBuf.writeBytes(in, n);
   }

   public void writeStringList(List list) {
      Object obj = null;
      getOpaqueStateTag();
      Object obj2 = null;
      VendorPacketBuffer vendorPacketBuffer = this;
      int size = list.size();
      vendorPacketBuffer.writeInt(size);
      obj2 = list.iterator();

      while (((Iterator)obj2).hasNext()) {
         obj = (String)((Iterator)obj2).next();
         vendorPacketBuffer = this;
         String str = (String)obj;
         vendorPacketBuffer.writeUtfString(str);
      }
   }

   @Override
   public ByteBuf retainedSlice() {
      return this.delegateBuf.retainedSlice();
   }


   @Override
   public ByteBuf getBytes(int n, ByteBuf buf, int n2) {
      return this.delegateBuf.getBytes(n, buf, n2);
   }

   @Override
   public short readUnsignedByte() {
      return this.delegateBuf.readUnsignedByte();
   }

   @Override
   public boolean getBoolean(int n) {
      return this.delegateBuf.getBoolean(n);
   }

   public List readStringList() {
      int n = 0;
      getOpaqueStateTag();
      boolean flag = false;
      Object obj = null;
      n = this.readInt();
      obj = new ArrayList(n);

      for (int i = 0; i < n; i++) {
         ((ArrayList)obj).add(this.readUtfString());
      }

      return (ArrayList)obj;
   }

   @Override
   public ByteBuf setChar(int n, int n2) {
      return this.delegateBuf.setChar(n, n2);
   }

   @Override
   public ByteBuf readBytes(byte[] bytes, int n, int n2) {
      return this.delegateBuf.readBytes(bytes, n, n2);
   }

   public void writeStringArray(String[] strings) {
      boolean flag = false;
      getOpaqueStateTag();
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      this.delegateBuf.writeInt(strings.length);
      obj2 = strings;
      n = ((String[])obj2).length;

      for (int i = 0; i < n; i++) {
         obj = ((String[])obj2)[i];
         VendorPacketBuffer vendorPacketBuffer = this;
         String str = (String)obj;
         vendorPacketBuffer.writeUtfString(str);
      }
   }

   @Override
   public String toString(Charset charset) {
      return this.delegateBuf.toString(charset);
   }

   @Override
   public long memoryAddress() {
      return this.delegateBuf.memoryAddress();
   }

   public VendorPacketBuffer(ByteBuf buf) {
      this.delegateBuf = buf;
   }

   public void writeDoubleArrayMap(Map map) {
      Object obj = null;
      getOpaqueStateTag();
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      VendorPacketBuffer vendorPacketBuffer = this;
      int size = map.size();
      vendorPacketBuffer.writeInt(size);
      obj = map.entrySet().iterator();

      while (((Iterator)obj).hasNext()) {
         obj2 = (Entry)((Iterator)obj).next();
         obj3 = (String)((Entry)obj2).getKey();
         obj4 = (double[])((Entry)obj2).getValue();
         vendorPacketBuffer = this;
         String str = (String)obj3;
         vendorPacketBuffer.writeUtfString(str);
         vendorPacketBuffer = this;
         double[] doubles = (double[])obj4;
         vendorPacketBuffer.writeDoubleArray(doubles);
      }
   }

   @Override
   public int getIntLE(int n) {
      return this.delegateBuf.getIntLE(n);
   }

   public void writeByteMatrix(byte[][] bytesArr) {
      boolean flag = false;
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      String opaqueStateTag2 = getOpaqueStateTag();
      VendorPacketBuffer vendorPacketBuffer = this;
      int length = bytesArr.length;
      vendorPacketBuffer.writeInt(length);
      obj2 = bytesArr;
      n = ((byte[][])obj2).length;

      for (int i = 0; i < n; i++) {
         obj = ((byte[][])obj2)[i];
         vendorPacketBuffer = this;
         byte[] bytes = (byte[])obj;
         vendorPacketBuffer.writeByteArray(bytes);
      }
   }

   @Override
   public float getFloat(int n) {
      return this.delegateBuf.getFloat(n);
   }

   @Override
   public ByteBuf readerIndex(int n) {
      return this.delegateBuf.readerIndex(n);
   }

   @Override
   public ByteBuf writeShort(int n) {
      return this.delegateBuf.writeShort(n);
   }

   @Override
   public boolean readBoolean() {
      return this.delegateBuf.readBoolean();
   }

   @Override
   public double readDouble() {
      return this.delegateBuf.readDouble();
   }

   @Override
   public ByteBuf setIndex(int n, int n2) {
      return this.delegateBuf.setIndex(n, n2);
   }

   @Override
   public ByteBuf setShort(int n, int n2) {
      return this.delegateBuf.setShort(n, n2);
   }

   @Override
   public long getLongLE(int n) {
      return this.delegateBuf.getLongLE(n);
   }

   @Override
   public ByteBuf touch() {
      return this.delegateBuf.touch();
   }

   public String[] readStringArray() {
      Object obj = null;
      getOpaqueStateTag();
      int n = 0;
      boolean flag = false;
      n = this.delegateBuf.readInt();
      if (n >= 400) {
         return null;
      } else {
         obj = new String[n];

         for (int i = 0; i < n; i++) {
            ((String[])obj)[i] = this.readUtfString();
         }

         return (String[])obj;
      }
   }

   @Override
   public int readBytes(FileChannel fileChannel, long l, int n) throws IOException {
      return this.delegateBuf.readBytes(fileChannel, l, n);
   }

   public void writeEnumOrdinal(Enum obj, long l) {
      int n = obj.ordinal();
      this.writeInt(n);
   }

   @Override
   public long readUnsignedIntLE() {
      return this.delegateBuf.readUnsignedIntLE();
   }

   @Override
   public int maxWritableBytes() {
      return this.delegateBuf.maxWritableBytes();
   }

   @Override
   public ByteBuf writeIntLE(int n) {
      return this.delegateBuf.writeIntLE(n);
   }

   @Override
   public int indexOf(int n, int n2, byte b) {
      return this.delegateBuf.indexOf(n, n2, b);
   }

   public double[][] readDoubleMatrix() {
      Object obj = null;
      int n = 0;
      boolean flag = false;
      String opaqueStateTag2 = getOpaqueStateTag();
      n = this.delegateBuf.readInt();
      if (n >= 400) {
         return (double[][])null;
      } else {
         obj = new double[n][];

         for (int i = 0; i < n; i++) {
            ((double[][])obj)[i] = this.readDoubleArray();
         }

         return (double[][])obj;
      }
   }

   @Override
   public int nioBufferCount() {
      return this.delegateBuf.nioBufferCount();
   }

   @Override
   public int writeBytes(ScatteringByteChannel scatteringByteChannel, int n) throws IOException {
      return this.delegateBuf.writeBytes(scatteringByteChannel, n);
   }

   @Override
   public int writeCharSequence(CharSequence text, Charset charset) {
      return this.delegateBuf.writeCharSequence(text, charset);
   }

   @Override
   public int getUnsignedShortLE(int n) {
      return this.delegateBuf.getUnsignedShortLE(n);
   }

   @Override
   public ByteBuf resetReaderIndex() {
      return this.delegateBuf.resetReaderIndex();
   }

   public void writeVarInt(int n) {
      String opaqueStateTag2 = getOpaqueStateTag();

      while ((n & -128) != 0) {
         VendorPacketBuffer vendorPacketBuffer = this;
         int n2 = n & 127 | 128;
         vendorPacketBuffer.writeByte(n2);
         n >>>= 7;
      }

      this.writeByte(n);
   }

   @Override
   public boolean release(int n) {
      return this.delegateBuf.release(n);
   }

   @Override
   public ByteBuf writeLongLE(long l) {
      return this.delegateBuf.writeLongLE(l);
   }

   public void writeUtfString(String str) {
      Object obj = null;
      String opaqueStateTag2 = getOpaqueStateTag();
      obj = str.getBytes(StandardCharsets.UTF_8);
      if (((byte[])obj).length <= 32767) {
         this.delegateBuf.writeInt(((byte[])obj).length);
         this.delegateBuf.writeBytes((byte[])obj);
      }
   }

   public Map readIntArrayMap() {
      Object obj = null;
      Object obj2 = null;
      boolean flag = false;
      Object obj3 = null;
      int n = 0;
      String opaqueStateTag2 = getOpaqueStateTag();
      n = this.readInt();
      obj = new HashMap(n);

      for (int i = 0; i < n; i++) {
         obj3 = this.readUtfString();
         obj2 = this.readIntArray();
         ((HashMap)obj).put((String)obj3, (int[])obj2);
      }

      return (HashMap)obj;
   }

   @Override
   public ByteBuf readBytes(ByteBuffer buf) {
      return this.delegateBuf.readBytes(buf);
   }

   @Override
   public int getUnsignedShort(int n) {
      return this.delegateBuf.getUnsignedShort(n);
   }

   @Override
   public int forEachByte(int n, int n2, ByteProcessor byteProcessor) {
      return this.delegateBuf.forEachByte(n, n2, byteProcessor);
   }

   @Override
   public int writeBytes(FileChannel fileChannel, long l, int n) throws IOException {
      return this.delegateBuf.writeBytes(fileChannel, l, n);
   }

   @Override
   public ByteBuf writeBytes(ByteBuf buf) {
      return this.delegateBuf.writeBytes(buf);
   }

   @Override
   public int getMediumLE(int n) {
      return this.delegateBuf.getMediumLE(n);
   }

   @Override
   public int refCnt() {
      return this.delegateBuf.refCnt();
   }

   public void writeFloatMap(Map map) {
      Object obj = null;
      Object obj2 = null;
      float f = 0.0F;
      Object obj3 = null;
      String opaqueStateTag2 = getOpaqueStateTag();
      VendorPacketBuffer vendorPacketBuffer = this;
      int size = map.size();
      vendorPacketBuffer.writeInt(size);
      obj = map.entrySet().iterator();

      while (((Iterator)obj).hasNext()) {
         obj3 = (Entry)((Iterator)obj).next();
         obj2 = (String)((Entry)obj3).getKey();
         f = (Float)((Entry)obj3).getValue();
         vendorPacketBuffer = this;
         String str = (String)obj2;
         vendorPacketBuffer.writeUtfString(str);
         this.writeFloat(f);
      }
   }

   @Override
   public ByteBuf discardSomeReadBytes() {
      return this.delegateBuf.discardSomeReadBytes();
   }

   public int[][] readIntMatrix() {
      Object obj = null;
      boolean flag = false;
      int n = 0;
      String opaqueStateTag2 = getOpaqueStateTag();
      n = this.delegateBuf.readInt();
      if (n >= 400) {
         return (int[][])null;
      } else {
         obj = new int[n][];

         for (int i = 0; i < n; i++) {
            ((int[][])obj)[i] = this.readIntArray();
         }

         return (int[][])obj;
      }
   }

   @Override
   public ByteBuf getBytes(int n, ByteBuf buf) {
      return this.delegateBuf.getBytes(n, buf);
   }

   public void writeByteArrayMap(Map map) {
      Object obj = null;
      getOpaqueStateTag();
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      VendorPacketBuffer vendorPacketBuffer = this;
      int size = map.size();
      vendorPacketBuffer.writeInt(size);
      obj3 = map.entrySet().iterator();

      while (((Iterator)obj3).hasNext()) {
         obj = (Entry)((Iterator)obj3).next();
         obj4 = (String)((Entry)obj).getKey();
         obj2 = (byte[])((Entry)obj).getValue();
         vendorPacketBuffer = this;
         String str = (String)obj4;
         vendorPacketBuffer.writeUtfString(str);
         vendorPacketBuffer = this;
         byte[] bytes = (byte[])obj2;
         vendorPacketBuffer.writeByteArray(bytes);
      }
   }

   public Map readBooleanMap() {
      boolean flag = false;
      boolean flag2 = false;
      Object obj = null;
      int n = 0;
      Object obj2 = null;
      String opaqueStateTag2 = getOpaqueStateTag();
      n = this.readInt();
      obj2 = new HashMap(n);

      for (int i = 0; i < n; i++) {
         obj = this.readUtfString();
         flag2 = this.readBoolean();
         ((HashMap)obj2).put((String)obj, flag2);
      }

      return (HashMap)obj2;
   }

   @Override
   public ByteBuf slice() {
      return this.delegateBuf.slice();
   }

   @Override
   public int arrayOffset() {
      return this.delegateBuf.arrayOffset();
   }

   public void writeBooleanArrayMap(Map map) {
      Object obj = null;
      getOpaqueStateTag();
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      VendorPacketBuffer vendorPacketBuffer = this;
      int size = map.size();
      vendorPacketBuffer.writeInt(size);
      obj4 = map.entrySet().iterator();

      while (((Iterator)obj4).hasNext()) {
         obj3 = (Entry)((Iterator)obj4).next();
         obj = (String)((Entry)obj3).getKey();
         obj2 = (boolean[])((Entry)obj3).getValue();
         vendorPacketBuffer = this;
         String str = (String)obj;
         vendorPacketBuffer.writeUtfString(str);
         vendorPacketBuffer = this;
         boolean[] flags = (boolean[])obj2;
         vendorPacketBuffer.writeBooleanArray(flags);
      }
   }

   @Override
   public boolean hasMemoryAddress() {
      return this.delegateBuf.hasMemoryAddress();
   }

   @Override
   public int getInt(int n) {
      return this.delegateBuf.getInt(n);
   }

   @Override
   public byte[] array() {
      return this.delegateBuf.array();
   }

   @Override
   public ByteBuf setLong(int n, long l) {
      return this.delegateBuf.setLong(n, l);
   }

   @Override
   public short readShortLE() {
      return this.delegateBuf.readShortLE();
   }

   @Override
   public int bytesBefore(int n, int n2, byte b) {
      return this.delegateBuf.bytesBefore(n, n2, b);
   }

   public Object readDefaultEnumConstant(Class clazz, long l) {
      return clazz.getEnumConstants()[0];
   }

   @Override
   public CharSequence getCharSequence(int n, int n2, Charset charset) {
      return this.delegateBuf.getCharSequence(n, n2, charset);
   }

   @Override
   public boolean isReadOnly() {
      return this.delegateBuf.isReadOnly();
   }

   @Override
   public int readableBytes() {
      return this.delegateBuf.readableBytes();
   }

   public String[][] readStringMatrix() {
      boolean flag = false;
      Object obj = null;
      getOpaqueStateTag();
      int n = 0;
      n = this.delegateBuf.readInt();
      if (n >= 400) {
         return (String[][])null;
      } else {
         obj = new String[n][];

         for (int i = 0; i < n; i++) {
            ((String[][])obj)[i] = this.readStringArray();
         }

         return (String[][])obj;
      }
   }

   @Override
   public ByteBuf writeChar(int n) {
      return this.delegateBuf.writeChar(n);
   }

   @Override
   public boolean equals(Object obj) {
      return this.delegateBuf.equals(obj);
   }

   @Override
   public ByteBuf retain() {
      return this.delegateBuf.retain();
   }

   @Override
   public ByteBuf writeBytes(ByteBuf buf, int n) {
      return this.delegateBuf.writeBytes(buf, n);
   }

   @Override
   public ByteOrder order() {
      return this.delegateBuf.order();
   }

   public ReferenceCounted touch_m1() {
      return this.touch();
   }

   @Override
   public ByteBuf writerIndex(int n) {
      return this.delegateBuf.writerIndex(n);
   }

   @Override
   public ByteBuf capacity(int n) {
      return this.delegateBuf.capacity(n);
   }

   @Override
   public ByteBufAllocator alloc() {
      return this.delegateBuf.alloc();
   }

   public Map readFloatMap() {
      Object obj = null;
      int n = 0;
      getOpaqueStateTag();
      boolean flag = false;
      Object obj2 = null;
      float f = 0.0F;
      n = this.readInt();
      obj2 = new HashMap(n);

      for (int i = 0; i < n; i++) {
         obj = this.readUtfString();
         f = this.readFloat();
         ((HashMap)obj2).put((String)obj, f);
      }

      return (HashMap)obj2;
   }

   @Override
   public ByteBuf discardReadBytes() {
      return this.delegateBuf.discardReadBytes();
   }

   @Override
   public ByteBuf resetWriterIndex() {
      return this.delegateBuf.resetWriterIndex();
   }

   @Override
   public ByteBuf readRetainedSlice(int n) {
      return this.delegateBuf.readRetainedSlice(n);
   }

   public int[] readIntArray() {
      boolean flag = false;
      Object obj = null;
      int n = 0;
      String opaqueStateTag2 = getOpaqueStateTag();
      n = this.delegateBuf.readInt();
      if (n >= 400) {
         return null;
      } else {
         obj = new int[n];

         for (int i = 0; i < n; i++) {
            ((int[])obj)[i] = this.readInt();
         }

         return (int[])obj;
      }
   }

   @Override
   public ByteBuf writeFloat(float f) {
      return this.delegateBuf.writeFloat(f);
   }

   @Override
   public int getBytes(int n, FileChannel fileChannel, long l, int n2) throws IOException {
      return this.delegateBuf.getBytes(n, fileChannel, l, n2);
   }

   public Map readDoubleArrayMap() {
      Object obj = null;
      getOpaqueStateTag();
      Object obj2 = null;
      Object obj3 = null;
      int n = 0;
      boolean flag = false;
      n = this.readInt();
      obj = new HashMap(n);

      for (int i = 0; i < n; i++) {
         obj2 = this.readUtfString();
         obj3 = this.readDoubleArray();
         ((HashMap)obj).put((String)obj2, (double[])obj3);
      }

      return (HashMap)obj;
   }

   public boolean[] readBooleanArray() {
      boolean flag = false;
      getOpaqueStateTag();
      Object obj = null;
      int n = 0;
      n = this.delegateBuf.readInt();
      if (n >= 400) {
         return null;
      } else {
         obj = new boolean[n];

         for (int i = 0; i < n; i++) {
            ((boolean[])obj)[i] = this.readBoolean();
         }

         return (boolean[])obj;
      }
   }

   @Override
   public boolean release() {
      return this.delegateBuf.release();
   }

   @Override
   public short readShort() {
      return this.delegateBuf.readShort();
   }

   @Override
   public ByteBuf setShortLE(int n, int n2) {
      return this.delegateBuf.setShortLE(n, n2);
   }

   public void writeDoubleArray(double[] doubles) {
      boolean flag = false;
      double d = 0.0;
      getOpaqueStateTag();
      Object obj = null;
      int n = 0;
      this.delegateBuf.writeInt(doubles.length);
      obj = doubles;
      n = ((double[])obj).length;

      for (int i = 0; i < n; i++) {
         d = ((double[])obj)[i];
         this.writeDouble(d);
      }
   }

   @Override
   public ByteBuf setMediumLE(int n, int n2) {
      return this.delegateBuf.setMediumLE(n, n2);
   }

   @Override
   public int getUnsignedMediumLE(int n) {
      return this.delegateBuf.getUnsignedMediumLE(n);
   }

   @Override
   public ByteBuf skipBytes(int n) {
      return this.delegateBuf.skipBytes(n);
   }

   public Map readByteArrayMap() {
      int n = 0;
      Object obj = null;
      getOpaqueStateTag();
      Object obj2 = null;
      Object obj3 = null;
      boolean flag = false;
      n = this.readInt();
      obj = new HashMap(n);

      for (int i = 0; i < n; i++) {
         obj3 = this.readUtfString();
         obj2 = this.readByteArray();
         ((HashMap)obj).put((String)obj3, (byte[])obj2);
      }

      return (HashMap)obj;
   }

   @Override
   public ByteBuf writeBytes(byte[] bytes) {
      return this.delegateBuf.writeBytes(bytes);
   }

   @Override
   public int setBytes(int n, FileChannel fileChannel, long l, int n2) throws IOException {
      return this.delegateBuf.setBytes(n, fileChannel, l, n2);
   }

   @Override
   public ByteBuf getBytes(int n, byte[] bytes, int n2, int n3) {
      return this.delegateBuf.getBytes(n, bytes, n2, n3);
   }

   @Override
   public boolean isWritable(int n) {
      return this.delegateBuf.isWritable(n);
   }

   @Override
   public ByteBuf writeBytes(byte[] bytes, int n, int n2) {
      return this.delegateBuf.writeBytes(bytes, n, n2);
   }

   @Override
   public ByteBuf setByte(int n, int n2) {
      return this.delegateBuf.setByte(n, n2);
   }

   public byte[] readByteArray() {
      Object obj = null;
      int n = 0;
      String opaqueStateTag2 = getOpaqueStateTag();
      n = this.readInt();
      obj = new byte[n];
      this.delegateBuf.readBytes((byte[])obj);
      return (byte[])obj;
   }

   @Override
   public ByteBuf setBytes(int n, ByteBuffer buf) {
      return this.delegateBuf.setBytes(n, buf);
   }

   @Override
   public ByteBuf writeMedium(int n) {
      return this.delegateBuf.writeMedium(n);
   }

   public void writeUuid(short s, int n, char ch, UUID uuid) {
      String opaqueStateTag2 = getOpaqueStateTag();
      this.delegateBuf.writeLong(uuid.getMostSignificantBits());
      this.delegateBuf.writeLong(uuid.getLeastSignificantBits());
   }

   public static void setOpaqueStateTag(String str) {
      opaqueStateTag = str;
   }

   public static String getOpaqueStateTag() {
      return opaqueStateTag;
   }

   static {
      setOpaqueStateTag((String)null);
   }

}
