package net.augustus.vendor.launcher;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ByteProcessor;
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

public class NettyPacketBuf extends ByteBuf {
   private final ByteBuf buf;

   @Override
   public int readIntLE() {
      return this.buf.readIntLE();
   }

   @Override
   public long getUnsignedIntLE(int n) {
      return this.buf.getUnsignedIntLE(n);
   }

   @Override
   public ByteBuf getBytes(int n, byte[] bytes, int n2, int n3) {
      return this.buf.getBytes(n, bytes, n2, n3);
   }

   @Override
   public int getMediumLE(int n) {
      return this.buf.getMediumLE(n);
   }

   @Override
   public ByteBuf writeInt(int n) {
      return this.buf.writeInt(n);
   }

   public byte[][] readByteArrayArray() {
      int n = 0;
      boolean flag = false;
      Object obj = null;
      n = this.readInt();
      obj = new byte[n][];

      for (int i = 0; i < n; i++) {
         ((byte[][])obj)[i] = this.readByteArray();
      }

      return (byte[][])obj;
   }

   public void writeEnumValue(Enum<?> obj) {
      this.writeInt(obj.ordinal());
   }

   @Override
   public int readBytes(FileChannel fileChannel, long l, int n) throws IOException {
      return this.buf.readBytes(fileChannel, l, n);
   }

   @Override
   public ByteBuf writeFloat(float f) {
      return this.buf.writeFloat(f);
   }

   public int readVarInt() {
      int n = 0;
      int n2 = 0;
      byte b = 0;
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
   public ByteBuf resetWriterIndex() {
      return this.buf.resetWriterIndex();
   }

   @Override
   public short getShort(int n) {
      return this.buf.getShort(n);
   }

   public Map<String, byte[]> readStringByteArrayMap() {
      Object obj = null;
      Object obj2 = null;
      boolean flag = false;
      int n = 0;
      Object obj3 = null;
      n = this.readInt();
      obj3 = new HashMap(n);

      for (int i = 0; i < n; i++) {
         obj = this.readUtf8String();
         obj2 = this.readByteArray();
         ((HashMap)obj3).put((String)obj, (byte[])obj2);
      }

      return (HashMap)obj3;
   }

   @Override
   public int compareTo(ByteBuf other) {
      return this.buf.compareTo(other);
   }

   @Override
   public String toString(Charset charset) {
      return this.buf.toString(charset);
   }

   @Override
   public ByteBuf writeDouble(double d) {
      return this.buf.writeDouble(d);
   }

   @Override
   public ByteBuf writeBytes(ByteBuf buf2, int n) {
      return this.buf.writeBytes(buf2, n);
   }

   @Override
   public ByteBuf setBytes(int n, ByteBuffer buf2) {
      return this.buf.setBytes(n, buf2);
   }

   public static int getVarIntSize(int n) {
      boolean flag = false;

      for (int i = 1; i < 5; i++) {
         if ((n & -1 << i * 7) == 0) {
            return i;
         }
      }

      return 5;
   }

   @Override
   public ByteBuf setShortLE(int n, int n2) {
      return this.buf.setShortLE(n, n2);
   }

   public String readNullTerminatedString() {
      byte b = 0;
      Object obj = null;
      obj = new StringBuilder();

      while ((b = this.buf.readByte()) != 0) {
         ((StringBuilder)obj).append((char)b);
      }

      return ((StringBuilder)obj).toString();
   }

   @Override
   public boolean equals(Object obj) {
      return this.buf.equals(obj);
   }

   public void writeNullTerminatedString(String str) {
      this.buf.writeBytes(str.getBytes(StandardCharsets.US_ASCII));
      this.buf.writeByte(0);
   }

   @Override
   public ByteBuf setInt(int n, int n2) {
      return this.buf.setInt(n, n2);
   }

   @Override
   public ByteBuf setByte(int n, int n2) {
      return this.buf.setByte(n, n2);
   }

   @Override
   public ByteBuf readBytes(ByteBuffer buf2) {
      return this.buf.readBytes(buf2);
   }

   @Override
   public int getInt(int n) {
      return this.buf.getInt(n);
   }

   @Override
   public int maxCapacity() {
      return this.buf.maxCapacity();
   }

   @Override
   public ByteBuf writeBytes(byte[] bytes, int n, int n2) {
      return this.buf.writeBytes(bytes, n, n2);
   }

   @Override
   public long readUnsignedIntLE() {
      return this.buf.readUnsignedIntLE();
   }

   @Override
   public int getUnsignedShort(int n) {
      return this.buf.getUnsignedShort(n);
   }

   @Override
   public ByteBuf retain() {
      return this.buf.retain();
   }

   @Override
   public ByteBuf unwrap() {
      return this.buf.unwrap();
   }

   @Override
   public ByteBuf copy() {
      return this.buf.copy();
   }

   @Override
   public ByteBuf writeShortLE(int n) {
      return this.buf.writeShortLE(n);
   }

   @Override
   public int writeCharSequence(CharSequence text, Charset charset) {
      return this.buf.writeCharSequence(text, charset);
   }

   @Override
   public int writerIndex() {
      return this.buf.writerIndex();
   }

   @Override
   public ByteBuf ensureWritable(int n) {
      return this.buf.ensureWritable(n);
   }

   @Override
   public ByteBuf getBytes(int n, ByteBuf buf2, int n2) {
      return this.buf.getBytes(n, buf2, n2);
   }

   @Override
   public int nioBufferCount() {
      return this.buf.nioBufferCount();
   }

   @Override
   public ByteBuf getBytes(int n, byte[] bytes) {
      return this.buf.getBytes(n, bytes);
   }

   @Override
   public boolean isDirect() {
      return this.buf.isDirect();
   }

   @Override
   public int indexOf(int n, int n2, byte b) {
      return this.buf.indexOf(n, n2, b);
   }

   @Override
   public long getLongLE(int n) {
      return this.buf.getLongLE(n);
   }

   @Override
   public ByteOrder order() {
      return this.buf.order();
   }

   @Override
   public ByteBuf writerIndex(int n) {
      return this.buf.writerIndex(n);
   }

   @Override
   public ByteBuf readBytes(ByteBuf buf2, int n, int n2) {
      return this.buf.readBytes(buf2, n, n2);
   }

   public void writeStringByteArrayMap(Map<String, byte[]> map) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      this.writeInt(map.size());
      obj4 = map.entrySet().iterator();

      while (((Iterator)obj4).hasNext()) {
         obj = (Entry)((Iterator)obj4).next();
         obj2 = (String)((Entry)obj).getKey();
         obj3 = (byte[])((Entry)obj).getValue();
         this.writeUtf8String((String)obj2);
         this.writeByteArray((byte[])obj3);
      }
   }

   public List<byte[]> readByteArrayList() {
      boolean flag = false;
      int n = 0;
      Object obj = null;
      n = this.readInt();
      obj = new ArrayList(n);

      for (int i = 0; i < n; i++) {
         ((ArrayList)obj).add(this.readByteArray());
      }

      return (ArrayList)obj;
   }

   @Override
   public ByteBuf getBytes(int n, OutputStream out, int n2) throws IOException {
      return this.buf.getBytes(n, out, n2);
   }

   @Override
   public ByteBuf setChar(int n, int n2) {
      return this.buf.setChar(n, n2);
   }

   @Override
   public int writableBytes() {
      return this.buf.writableBytes();
   }

   @Override
   public CharSequence readCharSequence(int n, Charset charset) {
      return this.buf.readCharSequence(n, charset);
   }

   @Override
   public ByteBufAllocator alloc() {
      return this.buf.alloc();
   }

   public void writeUuid(UUID uuid) {
      this.buf.writeLong(uuid.getMostSignificantBits());
      this.buf.writeLong(uuid.getLeastSignificantBits());
   }

   @Override
   public ByteBuffer nioBuffer(int n, int n2) {
      return this.buf.nioBuffer(n, n2);
   }

   @Override
   public ByteBuf setBytes(int n, byte[] bytes, int n2, int n3) {
      return this.buf.setBytes(n, bytes, n2, n3);
   }

   @Override
   public int arrayOffset() {
      return this.buf.arrayOffset();
   }

   @Override
   public ByteBuf setIntLE(int n, int n2) {
      return this.buf.setIntLE(n, n2);
   }

   public void writeByteArrayList(List<byte[]> list) {
      Object obj = null;
      Object obj2 = null;
      this.writeInt(list.size());
      obj = list.iterator();

      while (((Iterator)obj).hasNext()) {
         obj2 = (byte[])((Iterator)obj).next();
         this.writeByteArray((byte[])obj2);
      }
   }

   @Override
   public boolean isReadOnly() {
      return this.buf.isReadOnly();
   }

   @Override
   public int hashCode() {
      return this.buf.hashCode();
   }

   @Override
   public boolean isWritable() {
      return this.buf.isWritable();
   }

   public int readEnumOrdinal() {
      return this.readInt();
   }

   @Override
   public ByteBuf setLong(int n, long l) {
      return this.buf.setLong(n, l);
   }

   @Override
   public ByteBuf writeByte(int n) {
      return this.buf.writeByte(n);
   }

   @Override
   public ByteBuf readBytes(ByteBuf buf2, int n) {
      return this.buf.readBytes(buf2, n);
   }

   @Override
   public long memoryAddress() {
      return this.buf.memoryAddress();
   }

   @Override
   public ByteBuf getBytes(int n, ByteBuf buf2) {
      return this.buf.getBytes(n, buf2);
   }

   @Override
   public ByteBuffer[] nioBuffers(int n, int n2) {
      return this.buf.nioBuffers(n, n2);
   }

   @Override
   public int getMedium(int n) {
      return this.buf.getMedium(n);
   }

   @Override
   public ByteBuffer nioBuffer() {
      return this.buf.nioBuffer();
   }

   @Override
   public ByteBuffer internalNioBuffer(int n, int n2) {
      return this.buf.internalNioBuffer(n, n2);
   }

   @Override
   public short readShortLE() {
      return this.buf.readShortLE();
   }

   @Override
   public ByteBuf readBytes(byte[] bytes, int n, int n2) {
      return this.buf.readBytes(bytes, n, n2);
   }

   @Override
   public ByteBuf setBytes(int n, byte[] bytes) {
      return this.buf.setBytes(n, bytes);
   }

   @Override
   public ByteBuf setLongLE(int n, long l) {
      return this.buf.setLongLE(n, l);
   }

   @Override
   public CharSequence getCharSequence(int n, int n2, Charset charset) {
      return this.buf.getCharSequence(n, n2, charset);
   }

   public void writeByteArrayArray(byte[][] bytesArr) {
      boolean flag = false;
      Object obj = null;
      Object obj2 = null;
      int n = 0;
      this.writeInt(bytesArr.length);
      obj2 = bytesArr;
      n = ((byte[][])obj2).length;

      for (int i = 0; i < n; i++) {
         obj = ((byte[][])obj2)[i];
         this.writeByteArray((byte[])obj);
      }
   }

   @Override
   public byte getByte(int n) {
      return this.buf.getByte(n);
   }

   @Override
   public int writeBytes(ScatteringByteChannel scatteringByteChannel, int n) throws IOException {
      return this.buf.writeBytes(scatteringByteChannel, n);
   }

   @Override
   public boolean release() {
      return this.buf.release();
   }

   public NettyPacketBuf(ByteBuf buf2) {
      this.buf = buf2;
   }

   @Override
   public double readDouble() {
      return this.buf.readDouble();
   }

   @Override
   public short getShortLE(int n) {
      return this.buf.getShortLE(n);
   }

   @Override
   public ByteBuf slice(int n, int n2) {
      return this.buf.slice(n, n2);
   }

   @Override
   public ByteBuf writeBytes(ByteBuf buf2) {
      return this.buf.writeBytes(buf2);
   }

   @Override
   public int getUnsignedShortLE(int n) {
      return this.buf.getUnsignedShortLE(n);
   }

   @Override
   public ByteBuf setDouble(int n, double d) {
      return this.buf.setDouble(n, d);
   }

   @Override
   public ByteBuf setShort(int n, int n2) {
      return this.buf.setShort(n, n2);
   }

   @Override
   public int capacity() {
      return this.buf.capacity();
   }

   @Override
   public int refCnt() {
      return this.buf.refCnt();
   }

   @Override
   public ByteBuf setIndex(int n, int n2) {
      return this.buf.setIndex(n, n2);
   }

   @Override
   public ByteBuf retainedSlice(int n, int n2) {
      return this.buf.retainedSlice(n, n2);
   }

   @Override
   public ByteBuf touch() {
      return this.buf.touch();
   }

   @Override
   public ByteBuf order(ByteOrder byteOrder) {
      return this.buf.order(byteOrder);
   }

   @Override
   public int forEachByteDesc(int n, int n2, ByteProcessor byteProcessor) {
      return this.buf.forEachByteDesc(n, n2, byteProcessor);
   }

   @Override
   public boolean isReadable() {
      return this.buf.isReadable();
   }

   @Override
   public int maxWritableBytes() {
      return this.buf.maxWritableBytes();
   }

   @Override
   public ByteBuf duplicate() {
      return this.buf.duplicate();
   }

   @Override
   public long readLong() {
      return this.buf.readLong();
   }

   public void writeVarInt(int n) {

      while ((n & -128) != 0) {
         this.writeByte(n & 127 | 128);
         n >>>= 7;
      }

      this.writeByte(n);
   }

   @Override
   public int readMediumLE() {
      return this.buf.readMediumLE();
   }

   @Override
   public int ensureWritable(int n, boolean flag) {
      return this.buf.ensureWritable(n, flag);
   }

   @Override
   public int forEachByteDesc(ByteProcessor byteProcessor) {
      return this.buf.forEachByteDesc(byteProcessor);
   }

   @Override
   public ByteBuf copy(int n, int n2) {
      return this.buf.copy(n, n2);
   }

   @Override
   public int getBytes(int n, FileChannel fileChannel, long l, int n2) throws IOException {
      return this.buf.getBytes(n, fileChannel, l, n2);
   }

   @Override
   public ByteBuf setBytes(int n, ByteBuf buf2) {
      return this.buf.setBytes(n, buf2);
   }

   @Override
   public int getUnsignedMedium(int n) {
      return this.buf.getUnsignedMedium(n);
   }

   @Override
   public ByteBuf setBoolean(int n, boolean flag) {
      return this.buf.setBoolean(n, flag);
   }

   @Override
   public int getBytes(int n, GatheringByteChannel gatheringByteChannel, int n2) throws IOException {
      return this.buf.getBytes(n, gatheringByteChannel, n2);
   }

   @Override
   public ByteBuf readerIndex(int n) {
      return this.buf.readerIndex(n);
   }

   @Override
   public boolean getBoolean(int n) {
      return this.buf.getBoolean(n);
   }

   @Override
   public ByteBuf retainedDuplicate() {
      return this.buf.retainedDuplicate();
   }

   @Override
   public ByteBuf markReaderIndex() {
      return this.buf.markReaderIndex();
   }

   @Override
   public int readUnsignedMedium() {
      return this.buf.readUnsignedMedium();
   }

   @Override
   public int setBytes(int n, InputStream in, int n2) throws IOException {
      return this.buf.setBytes(n, in, n2);
   }

   @Override
   public long getLong(int n) {
      return this.buf.getLong(n);
   }

   public void writeUtf8String(String str) {
      Object obj = null;
      obj = str.getBytes(StandardCharsets.UTF_8);
      if (((byte[])obj).length <= 32767) {
         this.buf.writeInt(((byte[])obj).length);
         this.buf.writeBytes((byte[])obj);
      }
   }

   @Override
   public int getIntLE(int n) {
      return this.buf.getIntLE(n);
   }

   @Override
   public short readShort() {
      return this.buf.readShort();
   }

   @Override
   public ByteBuf markWriterIndex() {
      return this.buf.markWriterIndex();
   }

   @Override
   public int bytesBefore(int n, int n2, byte b) {
      return this.buf.bytesBefore(n, n2, b);
   }

   @Override
   public int bytesBefore(int n, byte b) {
      return this.buf.bytesBefore(n, b);
   }

   public byte[] readByteArray() {
      int n = 0;
      Object obj = null;
      n = this.readInt();
      obj = new byte[n];
      this.buf.readBytes((byte[])obj);
      return (byte[])obj;
   }

   @Override
   public ByteBuf writeChar(int n) {
      return this.buf.writeChar(n);
   }

   @Override
   public ByteBuf setMediumLE(int n, int n2) {
      return this.buf.setMediumLE(n, n2);
   }

   @Override
   public ByteBuf getBytes(int n, ByteBuf buf2, int n2, int n3) {
      return this.buf.getBytes(n, buf2, n2, n3);
   }

   @Override
   public int readUnsignedShortLE() {
      return this.buf.readUnsignedShortLE();
   }

   @Override
   public ByteBuf readBytes(int n) {
      return this.buf.readBytes(n);
   }

   @Override
   public ByteBuf readSlice(int n) {
      return this.buf.readSlice(n);
   }

   public void writeByteArray(byte[] bytes) {
      this.writeInt(bytes.length);
      this.buf.writeBytes(bytes);
   }

   @Override
   public int forEachByte(int n, int n2, ByteProcessor byteProcessor) {
      return this.buf.forEachByte(n, n2, byteProcessor);
   }

   @Override
   public int readerIndex() {
      return this.buf.readerIndex();
   }

   @Override
   public String toString() {
      return this.buf.toString();
   }

   @Override
   public ByteBuf setBytes(int n, ByteBuf buf2, int n2) {
      return this.buf.setBytes(n, buf2, n2);
   }

   @Override
   public long getUnsignedInt(int n) {
      return this.buf.getUnsignedInt(n);
   }

   @Override
   public ByteBuf discardSomeReadBytes() {
      return this.buf.discardSomeReadBytes();
   }

   @Override
   public ByteBuf capacity(int n) {
      return this.buf.capacity(n);
   }

   @Override
   public ByteBuf setFloat(int n, float f) {
      return this.buf.setFloat(n, f);
   }

   @Override
   public int readMedium() {
      return this.buf.readMedium();
   }

   public String readUtf8String() {
      int n = 0;
      Object obj = null;
      n = this.buf.readInt();
      obj = this.toString(this.buf.readerIndex(), n, StandardCharsets.UTF_8);
      this.readerIndex(this.readerIndex() + n);
      return (String)obj;
   }

   @Override
   public boolean isReadable(int n) {
      return this.buf.isReadable(n);
   }

   @Override
   public ByteBuf writeLongLE(long l) {
      return this.buf.writeLongLE(l);
   }

   @Override
   public boolean readBoolean() {
      return this.buf.readBoolean();
   }

   @Override
   public ByteBuf retainedSlice() {
      return this.buf.retainedSlice();
   }

   @Override
   public int forEachByte(ByteProcessor byteProcessor) {
      return this.buf.forEachByte(byteProcessor);
   }

   @Override
   public float readFloat() {
      return this.buf.readFloat();
   }

   @Override
   public ByteBuf slice() {
      return this.buf.slice();
   }

   @Override
   public int writeBytes(InputStream in, int n) throws IOException {
      return this.buf.writeBytes(in, n);
   }

   @Override
   public int setBytes(int n, FileChannel fileChannel, long l, int n2) throws IOException {
      return this.buf.setBytes(n, fileChannel, l, n2);
   }

   @Override
   public ByteBuf getBytes(int n, ByteBuffer buf2) {
      return this.buf.getBytes(n, buf2);
   }

   @Override
   public ByteBuf writeBytes(ByteBuffer buf2) {
      return this.buf.writeBytes(buf2);
   }

   @Override
   public ByteBuf writeShort(int n) {
      return this.buf.writeShort(n);
   }

   @Override
   public int readBytes(GatheringByteChannel gatheringByteChannel, int n) throws IOException {
      return this.buf.readBytes(gatheringByteChannel, n);
   }

   @Override
   public short readUnsignedByte() {
      return this.buf.readUnsignedByte();
   }

   @Override
   public ByteBuf touch(Object obj) {
      return this.buf.touch(obj);
   }

   @Override
   public int getUnsignedMediumLE(int n) {
      return this.buf.getUnsignedMediumLE(n);
   }

   @Override
   public boolean release(int n) {
      return this.buf.release(n);
   }

   @Override
   public ByteBuf skipBytes(int n) {
      return this.buf.skipBytes(n);
   }

   @Override
   public ByteBuf writeMedium(int n) {
      return this.buf.writeMedium(n);
   }

   @Override
   public ByteBuf writeLong(long l) {
      return this.buf.writeLong(l);
   }

   @Override
   public float getFloat(int n) {
      return this.buf.getFloat(n);
   }

   @Override
   public int readUnsignedMediumLE() {
      return this.buf.readUnsignedMediumLE();
   }

   @Override
   public ByteBuf discardReadBytes() {
      return this.buf.discardReadBytes();
   }

   @Override
   public ByteBuf writeBytes(byte[] bytes) {
      return this.buf.writeBytes(bytes);
   }

   @Override
   public int readUnsignedShort() {
      return this.buf.readUnsignedShort();
   }

   @Override
   public int setCharSequence(int n, CharSequence text, Charset charset) {
      return this.buf.setCharSequence(n, text, charset);
   }

   @Override
   public ByteBuf setMedium(int n, int n2) {
      return this.buf.setMedium(n, n2);
   }

   @Override
   public ByteBuf readBytes(ByteBuf buf2) {
      return this.buf.readBytes(buf2);
   }

   @Override
   public char getChar(int n) {
      return this.buf.getChar(n);
   }

   @Override
   public int readInt() {
      return this.buf.readInt();
   }

   @Override
   public ByteBuf writeMediumLE(int n) {
      return this.buf.writeMediumLE(n);
   }

   @Override
   public ByteBuf writeZero(int n) {
      return this.buf.writeZero(n);
   }

   @Override
   public short getUnsignedByte(int n) {
      return this.buf.getUnsignedByte(n);
   }

   @Override
   public ByteBuf writeIntLE(int n) {
      return this.buf.writeIntLE(n);
   }

   @Override
   public ByteBuf readBytes(OutputStream out, int n) throws IOException {
      return this.buf.readBytes(out, n);
   }

   @Override
   public long readUnsignedInt() {
      return this.buf.readUnsignedInt();
   }

   @Override
   public ByteBuf readBytes(byte[] bytes) {
      return this.buf.readBytes(bytes);
   }

   @Override
   public char readChar() {
      return this.buf.readChar();
   }

   @Override
   public byte[] array() {
      return this.buf.array();
   }

   @Override
   public ByteBuf writeBoolean(boolean flag) {
      return this.buf.writeBoolean(flag);
   }

   @Override
   public ByteBuf asReadOnly() {
      return this.buf.asReadOnly();
   }

   @Override
   public ByteBuffer[] nioBuffers() {
      return this.buf.nioBuffers();
   }

   @Override
   public ByteBuf resetReaderIndex() {
      return this.buf.resetReaderIndex();
   }

   @Override
   public byte readByte() {
      return this.buf.readByte();
   }

   @Override
   public ByteBuf writeBytes(ByteBuf buf2, int n, int n2) {
      return this.buf.writeBytes(buf2, n, n2);
   }

   @Override
   public ByteBuf retain(int n) {
      return this.buf.retain(n);
   }

   @Override
   public boolean isWritable(int n) {
      return this.buf.isWritable(n);
   }

   @Override
   public int writeBytes(FileChannel fileChannel, long l, int n) throws IOException {
      return this.buf.writeBytes(fileChannel, l, n);
   }

   @Override
   public ByteBuf readRetainedSlice(int n) {
      return this.buf.readRetainedSlice(n);
   }

   @Override
   public boolean hasArray() {
      return this.buf.hasArray();
   }

   @Override
   public String toString(int n, int n2, Charset charset) {
      return this.buf.toString(n, n2, charset);
   }

   @Override
   public ByteBuf setBytes(int n, ByteBuf buf2, int n2, int n3) {
      return this.buf.setBytes(n, buf2, n2, n3);
   }

   @Override
   public int bytesBefore(byte b) {
      return this.buf.bytesBefore(b);
   }

   @Override
   public long readLongLE() {
      return this.buf.readLongLE();
   }

   @Override
   public ByteBuf setZero(int n, int n2) {
      return this.buf.setZero(n, n2);
   }

   public UUID readUuid() {
      return new UUID(this.buf.readLong(), this.buf.readLong());
   }

   @Override
   public boolean hasMemoryAddress() {
      return this.buf.hasMemoryAddress();
   }

   @Override
   public int readableBytes() {
      return this.buf.readableBytes();
   }

   @Override
   public int setBytes(int n, ScatteringByteChannel scatteringByteChannel, int n2) throws IOException {
      return this.buf.setBytes(n, scatteringByteChannel, n2);
   }

   @Override
   public ByteBuf clear() {
      return this.buf.clear();
   }

   @Override
   public double getDouble(int n) {
      return this.buf.getDouble(n);
   }



   static {
      Object obj = null;
      boolean flag = false;
      String str = "Mqrlvb";
      byte b = 116;
      Object obj2 = null;
      Object obj3 = null;
      boolean flag2 = false;
      Object obj4 = null;
      byte b2 = 111;
      boolean flag3 = true;
      Object obj5 = null;
      Object obj6 = null;
      boolean flag4 = true;
      boolean flag5 = false;
      boolean flag6 = true;
      boolean flag7 = false;
      Object obj7 = null;
      Object obj8 = null;
      boolean flag8 = false;
      Object obj9 = null;
      Object obj10 = null;
      boolean flag9 = true;
      Object obj11 = null;
      Object obj12 = null;
      boolean flag10 = false;
      Object obj13 = null;
      boolean flag11 = true;
      boolean flag12 = true;
      boolean flag13 = false;
      Object obj14 = null;
      byte b3 = 69;
      Object obj15 = null;
      boolean flag14 = false;
      Object obj16 = null;
      Object obj17 = null;
      Object obj18 = null;
      Object obj19 = null;
      boolean flag15 = false;
      boolean flag16 = false;
      boolean flag17 = false;
      Object obj20 = null;
      boolean flag18 = false;
      Object obj21 = null;
      Object obj22 = null;
      String str2 = "vEe2Mb";
      Object obj23 = null;
      boolean flag19 = false;
      Object obj24 = null;
      byte b4 = 46;
      boolean flag20 = true;
      String str3 = "zH8Jq";
      Object obj25 = null;
      byte b5 = 77;
      Object obj26 = null;
      Object obj27 = null;
      boolean flag21 = true;
      boolean flag22 = true;
      Object obj28 = null;
      boolean flag23 = false;
      boolean flag24 = false;
      Object obj29 = null;
      String str4 = "NQVoh";
      Object obj30 = null;
      Object obj31 = null;
      byte b6 = 11;
   }

}
