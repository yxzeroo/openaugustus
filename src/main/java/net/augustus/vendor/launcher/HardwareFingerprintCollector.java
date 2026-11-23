package net.augustus.vendor.launcher;

import net.augustus.vendor.launcher.packet.C2SHardwareInfoPacket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import oshi.SystemInfo;
import oshi.hardware.Baseboard;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.PowerSource;
import oshi.hardware.SoundCard;
import oshi.hardware.CentralProcessor.ProcessorIdentifier;

public class HardwareFingerprintCollector {

   private static List<PhysicalMemory> getPhysicalMemorySafe(GlobalMemory globalMemory) {
      try {
         return globalMemory.getPhysicalMemory();
      } catch (Throwable e) {
         return null;
      }
   }

   private static Baseboard getBaseboardSafe(ComputerSystem computerSystem) {
      try {
         return computerSystem.getBaseboard();
      } catch (Throwable e) {
         return null;
      }
   }

   private static List<PowerSource> getPowerSourcesSafe(HardwareAbstractionLayer hardwareAbstractionLayer) {
      try {
         return hardwareAbstractionLayer.getPowerSources();
      } catch (Throwable e) {
         return null;
      }
   }

   private static List<HWDiskStore> getDiskStoresSafe(HardwareAbstractionLayer hardwareAbstractionLayer) {
      try {
         return hardwareAbstractionLayer.getDiskStores();
      } catch (Throwable e) {
         return null;
      }
   }

   private static ComputerSystem getComputerSystemSafe(HardwareAbstractionLayer hardwareAbstractionLayer) {
      try {
         return hardwareAbstractionLayer.getComputerSystem();
      } catch (Throwable e) {
         return null;
      }
   }

   private static String decryptString(AesCrypto aesCrypto, byte[] bytes, byte[] bytes2) {
      byte[] bytes3 = decryptBytes(aesCrypto, bytes, bytes2);
      return bytes3 != null ? new String(bytes3, StandardCharsets.UTF_8) : null;
   }

   private static long bytesToLong(byte[] bytes) {
      long l = 0L;
      for (int i = 0; i < 8; i++) {
         l <<= 8;
         l |= (long)(bytes[i] & 255);
      }
      return l;
   }

   private static GlobalMemory getMemorySafe(HardwareAbstractionLayer hardwareAbstractionLayer) {
      try {
         return hardwareAbstractionLayer.getMemory();
      } catch (Throwable e) {
         return null;
      }
   }

   private static ProcessorIdentifier getProcessorIdentifierSafe(CentralProcessor centralProcessor) {
      try {
         return centralProcessor.getProcessorIdentifier();
      } catch (Throwable e) {
         return null;
      }
   }

   private static byte[] encryptString(AesCrypto aesCrypto, byte[] bytes, String str) {
      if (str.isEmpty()) {
         str = "Empty123";
      }

      return encryptBytes(aesCrypto, bytes, str.getBytes(StandardCharsets.UTF_8));
   }

   private static CentralProcessor getProcessorSafe(HardwareAbstractionLayer hardwareAbstractionLayer) {
      try {
         return hardwareAbstractionLayer.getProcessor();
      } catch (Throwable e) {
         return null;
      }
   }

   private static List<GraphicsCard> getGraphicsCardsSafe(HardwareAbstractionLayer hardwareAbstractionLayer) {
      try {
         return hardwareAbstractionLayer.getGraphicsCards();
      } catch (Throwable e) {
         return null;
      }
   }

   private static long decryptLong(AesCrypto aesCrypto, byte[] bytes, byte[] bytes2) {
      byte[] bytes3 = decryptBytes(aesCrypto, bytes, bytes2);
      return bytes3 != null ? bytesToLong(bytes3) : 0L;
   }

   private static byte[] encryptEmptyPlaceholder(AesCrypto aesCrypto, byte[] bytes) {
      return encryptBytes(aesCrypto, bytes, "Empty123".getBytes(StandardCharsets.UTF_8));
   }

   private static List<NetworkIF> getNetworkInterfacesSafe(HardwareAbstractionLayer hardwareAbstractionLayer) {
      try {
         return hardwareAbstractionLayer.getNetworkIFs();
      } catch (Throwable e) {
         return null;
      }
   }

   private static HardwareAbstractionLayer getHardwareSafe(SystemInfo systemInfo) {
      try {
         return systemInfo.getHardware();
      } catch (Throwable e) {
         return null;
      }
   }

   private static List<SoundCard> getSoundCardsSafe(HardwareAbstractionLayer hardwareAbstractionLayer) {
      try {
         return hardwareAbstractionLayer.getSoundCards();
      } catch (Throwable e) {
         return null;
      }
   }

   private static byte[] decryptBytes(AesCrypto aesCrypto, byte[] bytes, byte[] bytes2) {
      aesCrypto.init(true, bytes);
      byte[] bytes3 = aesCrypto.encrypt(bytes2, 0, bytes2.length);
      aesCrypto.resetIv();
      return bytes3;
   }

   private static byte[] encryptLong(AesCrypto aesCrypto, byte[] bytes, long l) {
      return encryptBytes(aesCrypto, bytes, longToBytes(l));
   }

   private static List<Display> getDisplaysSafe(HardwareAbstractionLayer hardwareAbstractionLayer) {
      try {
         return hardwareAbstractionLayer.getDisplays();
      } catch (Throwable e) {
         return null;
      }
   }

   public static C2SHardwareInfoPacket collectHardwareInfo(byte[] bytes) {
      int n = 0;
      Object obj = null;
      boolean flag = false;
      Object obj2 = null;
      int n2 = 0;
      Object obj3 = null;
      Object obj4 = null;
      boolean flag2 = false;
      Object obj5 = null;
      Object obj6 = null;
      long l = 0L;
      int n3 = 0;
      boolean flag3 = false;
      Object obj7 = null;
      long l2 = 0L;
      int n4 = 0;
      Object obj8 = null;
      boolean flag4 = false;
      Object obj9 = null;
      Object obj10 = null;
      Object obj11 = null;
      Object obj12 = null;
      int n5 = 0;
      boolean flag5 = false;
      Object obj13 = null;
      Object obj14 = null;
      Object obj15 = null;
      Object obj16 = null;
      Object obj17 = null;
      Object obj18 = null;
      Object obj19 = null;
      Object obj20 = null;
      Object obj21 = null;
      int n6 = 0;
      Object obj22 = null;
      Object obj23 = null;
      Object obj24 = null;
      Object obj25 = null;
      Object obj26 = null;
      Object obj27 = null;
      boolean flag6 = false;
      Object obj28 = null;
      Object obj29 = null;
      Object obj30 = null;
      long l3 = 0L;
      long l4 = 0L;
      boolean flag7 = false;
      Object obj31 = null;
      Object obj32 = null;
      Object obj33 = null;
      Object obj34 = null;
      Object obj35 = null;
      Object obj36 = null;
      Object obj37 = null;
      Object obj38 = null;
      Object obj39 = null;
      Object obj40 = null;
      Object obj41 = null;
      Object obj42 = null;
      Object obj43 = null;
      int n7 = 0;
      Object obj44 = null;
      obj5 = new AesCrypto();
      l2 = 0L;
      n2 = 0;
      obj42 = new ArrayList();
      obj14 = new ArrayList();
      obj16 = new ArrayList();
      obj21 = new ArrayList();
      obj = new ArrayList();
      AesCrypto aesCrypto = (AesCrypto)obj5;
      obj36 = encryptEmptyPlaceholder(aesCrypto, bytes);
      aesCrypto = (AesCrypto)obj5;
      obj19 = encryptEmptyPlaceholder(aesCrypto, bytes);
      aesCrypto = (AesCrypto)obj5;
      obj43 = encryptEmptyPlaceholder(aesCrypto, bytes);
      aesCrypto = (AesCrypto)obj5;
      obj39 = encryptEmptyPlaceholder(aesCrypto, bytes);
      aesCrypto = (AesCrypto)obj5;
      obj22 = encryptEmptyPlaceholder(aesCrypto, bytes);
      l = 0L;
      l4 = 0L;
      l3 = 0L;
      aesCrypto = (AesCrypto)obj5;
      obj38 = encryptEmptyPlaceholder(aesCrypto, bytes);
      aesCrypto = (AesCrypto)obj5;
      obj17 = encryptEmptyPlaceholder(aesCrypto, bytes);
      aesCrypto = (AesCrypto)obj5;
      obj33 = encryptEmptyPlaceholder(aesCrypto, bytes);
      n = 0;
      obj32 = new ArrayList();
      n6 = 0;
      obj29 = new ArrayList();
      obj26 = new ArrayList();
      obj3 = new ArrayList();
      n3 = 0;
      obj8 = new ArrayList();
      obj34 = new ArrayList();
      obj7 = new ArrayList();
      obj11 = new ArrayList();
      n5 = 0;
      obj9 = new ArrayList();
      obj20 = new ArrayList();
      obj2 = new ArrayList();
      obj13 = new ArrayList();
      n4 = 0;
      obj35 = new ArrayList();
      obj40 = new ArrayList();
      obj41 = new ArrayList();
      n7 = 0;
      obj15 = new ArrayList();
      obj27 = new ArrayList();
      obj44 = new SystemInfo();
      obj23 = getHardwareSafe((SystemInfo)obj44);
      if ((HardwareAbstractionLayer)obj23 != null) {
         obj12 = getMemorySafe((HardwareAbstractionLayer)obj23);
         if ((GlobalMemory)obj12 != null) {
            l2 = ((GlobalMemory)obj12).getTotal();
            obj25 = getPhysicalMemorySafe((GlobalMemory)obj12);
            if ((List)obj25 != null) {
               n2 = Math.min(((List)obj25).size(), 39);

               for (int i = 0; i < n2; i++) {
                  obj28 = (PhysicalMemory)((List)obj25).get(i);
                  ArrayList list = (ArrayList)obj42;
                  AesCrypto aesCrypto2 = (AesCrypto)obj5;
                  byte[] bytes10 = bytes;
                  list.add(encryptString(aesCrypto2, bytes10, ((PhysicalMemory)obj28).getManufacturer()));
                  ArrayList list2 = (ArrayList)obj14;
                  AesCrypto aesCrypto3 = (AesCrypto)obj5;
                  bytes10 = bytes;
                  list2.add(encryptString(aesCrypto3, bytes10, ((PhysicalMemory)obj28).getMemoryType()));
                  ArrayList list3 = (ArrayList)obj16;
                  AesCrypto aesCrypto4 = (AesCrypto)obj5;
                  bytes10 = bytes;
                  list3.add(encryptString(aesCrypto4, bytes10, ((PhysicalMemory)obj28).getBankLabel()));
                  ArrayList list4 = (ArrayList)obj21;
                  AesCrypto aesCrypto5 = (AesCrypto)obj5;
                  bytes10 = bytes;
                  list4.add(encryptLong(aesCrypto5, bytes10, ((PhysicalMemory)obj28).getCapacity()));
                  ArrayList list5 = (ArrayList)obj;
                  AesCrypto aesCrypto6 = (AesCrypto)obj5;
                  bytes10 = bytes;
                  list5.add(encryptLong(aesCrypto6, bytes10, ((PhysicalMemory)obj28).getClockSpeed()));
               }
            }
         }

         obj25 = getComputerSystemSafe((HardwareAbstractionLayer)obj23);
         if ((ComputerSystem)obj25 != null) {
            aesCrypto = (AesCrypto)obj5;
            obj36 = encryptString(aesCrypto, bytes, ((ComputerSystem)obj25).getHardwareUUID());
            aesCrypto = (AesCrypto)obj5;
            obj19 = encryptString(aesCrypto, bytes, ((ComputerSystem)obj25).getSerialNumber());
            aesCrypto = (AesCrypto)obj5;
            obj43 = encryptString(aesCrypto, bytes, ((ComputerSystem)obj25).getManufacturer());
            aesCrypto = (AesCrypto)obj5;
            obj39 = encryptString(aesCrypto, bytes, ((ComputerSystem)obj25).getModel());
            obj30 = getBaseboardSafe((ComputerSystem)obj25);
            if ((Baseboard)obj30 != null) {
               aesCrypto = (AesCrypto)obj5;
               obj22 = encryptString(aesCrypto, bytes, ((Baseboard)obj30).getSerialNumber());
            }
         }

         obj30 = getProcessorSafe((HardwareAbstractionLayer)obj23);
         if ((CentralProcessor)obj30 != null) {
            l = (long)((CentralProcessor)obj30).getLogicalProcessorCount();
            l4 = (long)((CentralProcessor)obj30).getPhysicalProcessorCount();
            l3 = ((CentralProcessor)obj30).getMaxFreq();
            obj28 = getProcessorIdentifierSafe((CentralProcessor)obj30);
            if ((ProcessorIdentifier)obj28 != null) {
               aesCrypto = (AesCrypto)obj5;
               obj38 = encryptString(aesCrypto, bytes, ((ProcessorIdentifier)obj28).getVendor());
               aesCrypto = (AesCrypto)obj5;
               obj17 = encryptString(aesCrypto, bytes, ((ProcessorIdentifier)obj28).getProcessorID());
               aesCrypto = (AesCrypto)obj5;
               obj33 = encryptString(aesCrypto, bytes, ((ProcessorIdentifier)obj28).getIdentifier());
            }
         }

         obj28 = getDisplaysSafe((HardwareAbstractionLayer)obj23);
         if ((List)obj28 != null) {
            n = Math.min(((List)obj28).size(), 39);

            for (int j = 0; j < n; j++) {
               obj24 = (Display)((List)obj28).get(j);
               ((ArrayList)obj32).add(encryptBytes((AesCrypto)obj5, bytes, ((Display)obj24).getEdid()));
            }
         }

         obj6 = getDiskStoresSafe((HardwareAbstractionLayer)obj23);
         if ((List)obj6 != null) {
            n6 = Math.min(((List)obj6).size(), 39);

            for (int k = 0; k < n6; k++) {
               obj4 = (HWDiskStore)((List)obj6).get(k);
               ArrayList list7 = (ArrayList)obj29;
               AesCrypto aesCrypto8 = (AesCrypto)obj5;
               byte[] bytes34 = bytes;
               list7.add(encryptLong(aesCrypto8, bytes34, ((HWDiskStore)obj4).getSize()));
               ArrayList list8 = (ArrayList)obj26;
               AesCrypto aesCrypto9 = (AesCrypto)obj5;
               bytes34 = bytes;
               list8.add(encryptString(aesCrypto9, bytes34, ((HWDiskStore)obj4).getSerial()));
               ArrayList list9 = (ArrayList)obj3;
               AesCrypto aesCrypto10 = (AesCrypto)obj5;
               bytes34 = bytes;
               list9.add(encryptString(aesCrypto10, bytes34, ((HWDiskStore)obj4).getModel()));
            }
         }

         obj24 = getGraphicsCardsSafe((HardwareAbstractionLayer)obj23);
         if ((List)obj24 != null) {
            n3 = Math.min(((List)obj24).size(), 39);

            for (int m = 0; m < n3; m++) {
               obj31 = (GraphicsCard)((List)obj24).get(m);
               ArrayList list10 = (ArrayList)obj8;
               AesCrypto aesCrypto11 = (AesCrypto)obj5;
               byte[] bytes38 = bytes;
               list10.add(encryptLong(aesCrypto11, bytes38, ((GraphicsCard)obj31).getVRam()));
               ArrayList list11 = (ArrayList)obj34;
               AesCrypto aesCrypto12 = (AesCrypto)obj5;
               bytes38 = bytes;
               list11.add(encryptString(aesCrypto12, bytes38, ((GraphicsCard)obj31).getDeviceId()));
               ArrayList list12 = (ArrayList)obj7;
               AesCrypto aesCrypto13 = (AesCrypto)obj5;
               bytes38 = bytes;
               list12.add(encryptString(aesCrypto13, bytes38, ((GraphicsCard)obj31).getName()));
               ArrayList list13 = (ArrayList)obj11;
               AesCrypto aesCrypto14 = (AesCrypto)obj5;
               bytes38 = bytes;
               list13.add(encryptString(aesCrypto14, bytes38, ((GraphicsCard)obj31).getVendor()));
            }
         }

         obj4 = getPowerSourcesSafe((HardwareAbstractionLayer)obj23);
         if ((List)obj4 != null) {
            n5 = Math.min(((List)obj4).size(), 39);

            for (int idx = 0; idx < n5; idx++) {
               obj18 = (PowerSource)((List)obj4).get(idx);
               ArrayList list14 = (ArrayList)obj9;
               AesCrypto aesCrypto15 = (AesCrypto)obj5;
               byte[] bytes43 = bytes;
               list14.add(encryptString(aesCrypto15, bytes43, ((PowerSource)obj18).getName()));
               ArrayList list15 = (ArrayList)obj20;
               AesCrypto aesCrypto16 = (AesCrypto)obj5;
               bytes43 = bytes;
               list15.add(encryptString(aesCrypto16, bytes43, ((PowerSource)obj18).getDeviceName()));
               ArrayList list16 = (ArrayList)obj2;
               AesCrypto aesCrypto17 = (AesCrypto)obj5;
               bytes43 = bytes;
               list16.add(encryptString(aesCrypto17, bytes43, ((PowerSource)obj18).getManufacturer()));
               ArrayList list17 = (ArrayList)obj13;
               AesCrypto aesCrypto18 = (AesCrypto)obj5;
               bytes43 = bytes;
               list17.add(encryptString(aesCrypto18, bytes43, ((PowerSource)obj18).getSerialNumber()));
            }
         }

         obj31 = getNetworkInterfacesSafe((HardwareAbstractionLayer)obj23);
         if ((List)obj31 != null) {
            n4 = Math.min(((List)obj31).size(), 39);

            for (int index = 0; index < n4; index++) {
               obj10 = (NetworkIF)((List)obj31).get(index);
               ArrayList list18 = (ArrayList)obj35;
               AesCrypto aesCrypto19 = (AesCrypto)obj5;
               byte[] bytes48 = bytes;
               list18.add(encryptString(aesCrypto19, bytes48, ((NetworkIF)obj10).getName()));
               ArrayList list19 = (ArrayList)obj40;
               AesCrypto aesCrypto20 = (AesCrypto)obj5;
               bytes48 = bytes;
               list19.add(encryptString(aesCrypto20, bytes48, ((NetworkIF)obj10).getDisplayName()));
               ArrayList list20 = (ArrayList)obj41;
               AesCrypto aesCrypto21 = (AesCrypto)obj5;
               bytes48 = bytes;
               list20.add(encryptString(aesCrypto21, bytes48, ((NetworkIF)obj10).getMacaddr()));
            }
         }

         obj18 = getSoundCardsSafe((HardwareAbstractionLayer)obj23);
         if ((List)obj18 != null) {
            n7 = Math.min(((List)obj18).size(), 39);

            for (int i2 = 0; i2 < n7; i2++) {
               obj37 = (SoundCard)((List)obj18).get(i2);
               ArrayList list21 = (ArrayList)obj15;
               AesCrypto aesCrypto22 = (AesCrypto)obj5;
               byte[] bytes52 = bytes;
               list21.add(encryptString(aesCrypto22, bytes52, ((SoundCard)obj37).getName()));
               ArrayList list22 = (ArrayList)obj27;
               AesCrypto aesCrypto23 = (AesCrypto)obj5;
               bytes52 = bytes;
               list22.add(encryptString(aesCrypto23, bytes52, ((SoundCard)obj37).getCodec()));
            }
         }
      }

      byte[] bytes56 = encryptLong((AesCrypto)obj5, bytes, l2);
      ArrayList list23 = (ArrayList)obj42;
      ArrayList list24 = (ArrayList)obj14;
      ArrayList list25 = (ArrayList)obj16;
      ArrayList list26 = (ArrayList)obj21;
      ArrayList list27 = (ArrayList)obj;
      byte[] bytes57 = (byte[])obj36;
      byte[] bytes58 = (byte[])obj19;
      byte[] bytes59 = (byte[])obj43;
      byte[] bytes60 = (byte[])obj39;
      byte[] bytes61 = (byte[])obj22;
      byte[] bytes63 = encryptLong((AesCrypto)obj5, bytes, l);
      byte[] bytes65 = encryptLong((AesCrypto)obj5, bytes, l4);
      AesCrypto aesCrypto27 = (AesCrypto)obj5;
      obj12 = new C2SHardwareInfoPacket(
         bytes56,
         n2,
         list23,
         list24,
         list25,
         list26,
         list27,
         bytes57,
         bytes58,
         bytes59,
         bytes60,
         bytes61,
         bytes63,
         bytes65,
         encryptLong(aesCrypto27, bytes, l3),
         (byte[])obj38,
         (byte[])obj17,
         (byte[])obj33,
         n,
         (ArrayList)obj32,
         n6,
         (ArrayList)obj29,
         (ArrayList)obj26,
         (ArrayList)obj3,
         n3,
         (ArrayList)obj8,
         (ArrayList)obj34,
         (ArrayList)obj7,
         (ArrayList)obj11,
         n5,
         (ArrayList)obj9,
         (ArrayList)obj20,
         (ArrayList)obj2,
         (ArrayList)obj13,
         n4,
         (ArrayList)obj35,
         (ArrayList)obj40,
         (ArrayList)obj41,
         n7,
         (ArrayList)obj15,
         (ArrayList)obj27
      );
      return (C2SHardwareInfoPacket)obj12;
   }

   private static byte[] longToBytes(long l) {
      Object obj = null;
      boolean flag = false;
      obj = new byte[8];

      for (int i = 7; i >= 0; i--) {
         ((byte[])obj)[i] = (byte)((int)(l & 255L));
         l >>= 8;
      }

      return (byte[])obj;
   }

   private static byte[] encryptBytes(AesCrypto aesCrypto, byte[] bytes, byte[] bytes2) {
      Object obj = null;
      AesCrypto aesCrypto2 = aesCrypto;
      aesCrypto2.init(false, bytes);
      aesCrypto2 = aesCrypto;
      obj = aesCrypto2.encrypt(bytes2, (byte)0, bytes2.length);
      aesCrypto.resetIv();
      return (byte[])obj;
   }


}
