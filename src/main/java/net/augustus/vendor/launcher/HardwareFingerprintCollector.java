package net.augustus.vendor.launcher;

import net.augustus.vendor.launcher.crypto.Sha256Digest;
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
      int[] flowState = Sha256Digest.getFlowState();
      if (str.isEmpty()) {
         str = "Empty123";
      }

      AesCrypto aesCrypto2 = aesCrypto;
      byte[] bytes2 = bytes;
      byte[] bytes3 = str.getBytes(StandardCharsets.UTF_8);
      byte[] bytes4 = bytes2;
      return encryptBytes(aesCrypto2, bytes4, bytes3);
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
      byte[] bytes2 = "Empty123".getBytes(StandardCharsets.UTF_8);
      return encryptBytes(aesCrypto, bytes, bytes2);
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
      byte[] bytes2 = longToBytes(l);
      return encryptBytes(aesCrypto, bytes, bytes2);
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
      Sha256Digest.getFlowState();
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
      byte[] bytes2 = bytes;
      obj36 = encryptEmptyPlaceholder(aesCrypto, bytes2);
      aesCrypto = (AesCrypto)obj5;
      byte[] bytes3 = bytes;
      obj19 = encryptEmptyPlaceholder(aesCrypto, bytes3);
      aesCrypto = (AesCrypto)obj5;
      byte[] bytes4 = bytes;
      obj43 = encryptEmptyPlaceholder(aesCrypto, bytes4);
      aesCrypto = (AesCrypto)obj5;
      byte[] bytes5 = bytes;
      obj39 = encryptEmptyPlaceholder(aesCrypto, bytes5);
      aesCrypto = (AesCrypto)obj5;
      byte[] bytes6 = bytes;
      obj22 = encryptEmptyPlaceholder(aesCrypto, bytes6);
      l = 0L;
      l4 = 0L;
      l3 = 0L;
      aesCrypto = (AesCrypto)obj5;
      byte[] bytes7 = bytes;
      obj38 = encryptEmptyPlaceholder(aesCrypto, bytes7);
      aesCrypto = (AesCrypto)obj5;
      byte[] bytes8 = bytes;
      obj17 = encryptEmptyPlaceholder(aesCrypto, bytes8);
      aesCrypto = (AesCrypto)obj5;
      byte[] bytes9 = bytes;
      obj33 = encryptEmptyPlaceholder(aesCrypto, bytes9);
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
                  String manufacturer = ((PhysicalMemory)obj28).getManufacturer();
                  byte[] bytes11 = bytes10;
                  list.add(encryptString(aesCrypto2, bytes11, manufacturer));
                  ArrayList list2 = (ArrayList)obj14;
                  AesCrypto aesCrypto3 = (AesCrypto)obj5;
                  bytes10 = bytes;
                  String memoryType = ((PhysicalMemory)obj28).getMemoryType();
                  byte[] bytes12 = bytes10;
                  list2.add(encryptString(aesCrypto3, bytes12, memoryType));
                  ArrayList list3 = (ArrayList)obj16;
                  AesCrypto aesCrypto4 = (AesCrypto)obj5;
                  bytes10 = bytes;
                  String bankLabel = ((PhysicalMemory)obj28).getBankLabel();
                  byte[] bytes13 = bytes10;
                  list3.add(encryptString(aesCrypto4, bytes13, bankLabel));
                  ArrayList list4 = (ArrayList)obj21;
                  AesCrypto aesCrypto5 = (AesCrypto)obj5;
                  bytes10 = bytes;
                  long capacity = ((PhysicalMemory)obj28).getCapacity();
                  byte[] bytes14 = bytes10;
                  list4.add(encryptLong(aesCrypto5, bytes14, capacity));
                  ArrayList list5 = (ArrayList)obj;
                  AesCrypto aesCrypto6 = (AesCrypto)obj5;
                  bytes10 = bytes;
                  long clockSpeed = ((PhysicalMemory)obj28).getClockSpeed();
                  byte[] bytes15 = bytes10;
                  list5.add(encryptLong(aesCrypto6, bytes15, clockSpeed));
               }
            }
         }

         obj25 = getComputerSystemSafe((HardwareAbstractionLayer)obj23);
         if ((ComputerSystem)obj25 != null) {
            aesCrypto = (AesCrypto)obj5;
            byte[] bytes16 = bytes;
            String hardwareUUID = ((ComputerSystem)obj25).getHardwareUUID();
            byte[] bytes17 = bytes16;
            obj36 = encryptString(aesCrypto, bytes17, hardwareUUID);
            aesCrypto = (AesCrypto)obj5;
            byte[] bytes18 = bytes;
            String serialNumber = ((ComputerSystem)obj25).getSerialNumber();
            byte[] bytes19 = bytes18;
            obj19 = encryptString(aesCrypto, bytes19, serialNumber);
            aesCrypto = (AesCrypto)obj5;
            byte[] bytes20 = bytes;
            String manufacturer2 = ((ComputerSystem)obj25).getManufacturer();
            byte[] bytes21 = bytes20;
            obj43 = encryptString(aesCrypto, bytes21, manufacturer2);
            aesCrypto = (AesCrypto)obj5;
            byte[] bytes22 = bytes;
            String model = ((ComputerSystem)obj25).getModel();
            byte[] bytes23 = bytes22;
            obj39 = encryptString(aesCrypto, bytes23, model);
            obj30 = getBaseboardSafe((ComputerSystem)obj25);
            if ((Baseboard)obj30 != null) {
               aesCrypto = (AesCrypto)obj5;
               byte[] bytes24 = bytes;
               String serialNumber2 = ((Baseboard)obj30).getSerialNumber();
               byte[] bytes25 = bytes24;
               obj22 = encryptString(aesCrypto, bytes25, serialNumber2);
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
               byte[] bytes26 = bytes;
               String vendor = ((ProcessorIdentifier)obj28).getVendor();
               byte[] bytes27 = bytes26;
               obj38 = encryptString(aesCrypto, bytes27, vendor);
               aesCrypto = (AesCrypto)obj5;
               byte[] bytes28 = bytes;
               String processorID = ((ProcessorIdentifier)obj28).getProcessorID();
               byte[] bytes29 = bytes28;
               obj17 = encryptString(aesCrypto, bytes29, processorID);
               aesCrypto = (AesCrypto)obj5;
               byte[] bytes30 = bytes;
               String identifier = ((ProcessorIdentifier)obj28).getIdentifier();
               byte[] bytes31 = bytes30;
               obj33 = encryptString(aesCrypto, bytes31, identifier);
            }
         }

         obj28 = getDisplaysSafe((HardwareAbstractionLayer)obj23);
         if ((List)obj28 != null) {
            n = Math.min(((List)obj28).size(), 39);

            for (int j = 0; j < n; j++) {
               obj24 = (Display)((List)obj28).get(j);
               ArrayList list6 = (ArrayList)obj32;
               AesCrypto aesCrypto7 = (AesCrypto)obj5;
               byte[] bytes32 = bytes;
               byte[] edid = ((Display)obj24).getEdid();
               byte[] bytes33 = bytes32;
               list6.add(encryptBytes(aesCrypto7, bytes33, edid));
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
               long size = ((HWDiskStore)obj4).getSize();
               byte[] bytes35 = bytes34;
               list7.add(encryptLong(aesCrypto8, bytes35, size));
               ArrayList list8 = (ArrayList)obj26;
               AesCrypto aesCrypto9 = (AesCrypto)obj5;
               bytes34 = bytes;
               String serial = ((HWDiskStore)obj4).getSerial();
               byte[] bytes36 = bytes34;
               list8.add(encryptString(aesCrypto9, bytes36, serial));
               ArrayList list9 = (ArrayList)obj3;
               AesCrypto aesCrypto10 = (AesCrypto)obj5;
               bytes34 = bytes;
               String model2 = ((HWDiskStore)obj4).getModel();
               byte[] bytes37 = bytes34;
               list9.add(encryptString(aesCrypto10, bytes37, model2));
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
               long vRam = ((GraphicsCard)obj31).getVRam();
               byte[] bytes39 = bytes38;
               list10.add(encryptLong(aesCrypto11, bytes39, vRam));
               ArrayList list11 = (ArrayList)obj34;
               AesCrypto aesCrypto12 = (AesCrypto)obj5;
               bytes38 = bytes;
               String deviceId = ((GraphicsCard)obj31).getDeviceId();
               byte[] bytes40 = bytes38;
               list11.add(encryptString(aesCrypto12, bytes40, deviceId));
               ArrayList list12 = (ArrayList)obj7;
               AesCrypto aesCrypto13 = (AesCrypto)obj5;
               bytes38 = bytes;
               String name = ((GraphicsCard)obj31).getName();
               byte[] bytes41 = bytes38;
               list12.add(encryptString(aesCrypto13, bytes41, name));
               ArrayList list13 = (ArrayList)obj11;
               AesCrypto aesCrypto14 = (AesCrypto)obj5;
               bytes38 = bytes;
               String vendor2 = ((GraphicsCard)obj31).getVendor();
               byte[] bytes42 = bytes38;
               list13.add(encryptString(aesCrypto14, bytes42, vendor2));
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
               String name2 = ((PowerSource)obj18).getName();
               byte[] bytes44 = bytes43;
               list14.add(encryptString(aesCrypto15, bytes44, name2));
               ArrayList list15 = (ArrayList)obj20;
               AesCrypto aesCrypto16 = (AesCrypto)obj5;
               bytes43 = bytes;
               String deviceName = ((PowerSource)obj18).getDeviceName();
               byte[] bytes45 = bytes43;
               list15.add(encryptString(aesCrypto16, bytes45, deviceName));
               ArrayList list16 = (ArrayList)obj2;
               AesCrypto aesCrypto17 = (AesCrypto)obj5;
               bytes43 = bytes;
               String manufacturer3 = ((PowerSource)obj18).getManufacturer();
               byte[] bytes46 = bytes43;
               list16.add(encryptString(aesCrypto17, bytes46, manufacturer3));
               ArrayList list17 = (ArrayList)obj13;
               AesCrypto aesCrypto18 = (AesCrypto)obj5;
               bytes43 = bytes;
               String serialNumber3 = ((PowerSource)obj18).getSerialNumber();
               byte[] bytes47 = bytes43;
               list17.add(encryptString(aesCrypto18, bytes47, serialNumber3));
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
               String name3 = ((NetworkIF)obj10).getName();
               byte[] bytes49 = bytes48;
               list18.add(encryptString(aesCrypto19, bytes49, name3));
               ArrayList list19 = (ArrayList)obj40;
               AesCrypto aesCrypto20 = (AesCrypto)obj5;
               bytes48 = bytes;
               String displayName = ((NetworkIF)obj10).getDisplayName();
               byte[] bytes50 = bytes48;
               list19.add(encryptString(aesCrypto20, bytes50, displayName));
               ArrayList list20 = (ArrayList)obj41;
               AesCrypto aesCrypto21 = (AesCrypto)obj5;
               bytes48 = bytes;
               String macaddr = ((NetworkIF)obj10).getMacaddr();
               byte[] bytes51 = bytes48;
               list20.add(encryptString(aesCrypto21, bytes51, macaddr));
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
               String name4 = ((SoundCard)obj37).getName();
               byte[] bytes53 = bytes52;
               list21.add(encryptString(aesCrypto22, bytes53, name4));
               ArrayList list22 = (ArrayList)obj27;
               AesCrypto aesCrypto23 = (AesCrypto)obj5;
               bytes52 = bytes;
               String codec = ((SoundCard)obj37).getCodec();
               byte[] bytes54 = bytes52;
               list22.add(encryptString(aesCrypto23, bytes54, codec));
            }
         }
      }

      AesCrypto aesCrypto24 = (AesCrypto)obj5;
      byte[] bytes55 = bytes;
      byte[] bytes56 = encryptLong(aesCrypto24, bytes55, l2);
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
      AesCrypto aesCrypto25 = (AesCrypto)obj5;
      byte[] bytes62 = bytes;
      byte[] bytes63 = encryptLong(aesCrypto25, bytes62, l);
      AesCrypto aesCrypto26 = (AesCrypto)obj5;
      byte[] bytes64 = bytes;
      byte[] bytes65 = encryptLong(aesCrypto26, bytes64, l4);
      AesCrypto aesCrypto27 = (AesCrypto)obj5;
      byte[] bytes66 = bytes;
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
         encryptLong(aesCrypto27, bytes66, l3),
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
      Sha256Digest.getFlowState();
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
      int[] flowState = Sha256Digest.getFlowState();
      AesCrypto aesCrypto2 = aesCrypto;
      byte[] bytes3 = bytes;
      boolean flag = false;
      aesCrypto2.init(flag, bytes3);
      aesCrypto2 = aesCrypto;
      byte[] bytes4 = bytes2;
      int length = bytes2.length;
      byte b = 0;
      byte[] bytes5 = bytes4;
      obj = aesCrypto2.encrypt(bytes5, b, length);
      aesCrypto.resetIv();
      return (byte[])obj;
   }


}
