package net.augustus.vendor.launcher.packet;

import net.augustus.vendor.launcher.NettyPacketBuf;
import java.util.ArrayList;
import java.util.List;

public class C2SHardwareInfoPacket implements LauncherPacket {
   private List<byte[]> networkInterfaceDisplayNames;
   private byte[] systemManufacturer;
   private byte[] logicalProcessorCount;
   private List<byte[]> graphicsCardDeviceIds;
   private byte[] physicalProcessorCount;
   private List<byte[]> memoryManufacturers;
   private int displayCount;
   private List<byte[]> memoryBankLabels;
   private List<byte[]> memoryClockSpeeds;
   private List<byte[]> graphicsCardVendors;
   private byte[] systemSerialNumber;
   private byte[] totalMemory;
   private List<byte[]> soundCardCodecs;
   private int soundCardCount;
   private List<byte[]> networkInterfaceMacAddresses;
   private List<byte[]> diskSerials;
   private byte[] processorVendor;
   private List<byte[]> diskSizes;
   private int powerSourceCount;
   private byte[] systemModel;
   private int networkInterfaceCount;
   private List<byte[]> memoryCapacities;
   private byte[] processorMaxFreq;
   private byte[] processorIdentifier;
   private List<byte[]> graphicsCardNames;
   private List<byte[]> powerSourceManufacturers;
   private List<byte[]> networkInterfaceNames;
   private byte[] hardwareUuid;
   private List<byte[]> memoryTypes;
   private byte[] baseboardSerialNumber;
   private List<byte[]> graphicsCardVrams;
   private List<byte[]> diskModels;
   private int physicalMemoryCount;
   private List<byte[]> powerSourceDeviceNames;
   private int graphicsCardCount;
   private List<byte[]> powerSourceNames;
   private List<byte[]> soundCardNames;
   private int diskStoreCount;
   private List<byte[]> displayEdids;
   private byte[] processorId;
   private List<byte[]> powerSourceSerialNumbers;

   public C2SHardwareInfoPacket(int n, byte b, int n2) {
      this.memoryManufacturers = new ArrayList<>();
      this.memoryTypes = new ArrayList<>();
      this.memoryBankLabels = new ArrayList<>();
      this.memoryCapacities = new ArrayList<>();
      this.memoryClockSpeeds = new ArrayList<>();
      this.displayEdids = new ArrayList<>();
      this.diskStoreCount = 0;
      this.diskSizes = new ArrayList<>();
      this.diskSerials = new ArrayList<>();
      this.diskModels = new ArrayList<>();
      this.graphicsCardCount = 0;
      this.graphicsCardVrams = new ArrayList<>();
      this.graphicsCardDeviceIds = new ArrayList<>();
      this.graphicsCardNames = new ArrayList<>();
      this.graphicsCardVendors = new ArrayList<>();
      this.powerSourceCount = 0;
      this.powerSourceNames = new ArrayList<>();
      this.powerSourceDeviceNames = new ArrayList<>();
      this.powerSourceManufacturers = new ArrayList<>();
      this.powerSourceSerialNumbers = new ArrayList<>();
      this.networkInterfaceCount = 0;
      this.networkInterfaceNames = new ArrayList<>();
      this.networkInterfaceDisplayNames = new ArrayList<>();
      this.networkInterfaceMacAddresses = new ArrayList<>();
      this.soundCardCount = 0;
      this.soundCardNames = new ArrayList<>();
      this.soundCardCodecs = new ArrayList<>();
   }

   @SuppressWarnings("unchecked")
   public C2SHardwareInfoPacket(
      byte[] bytes,
      int n,
      List list,
      List list2,
      List list3,
      List list4,
      List list5,
      byte[] bytes2,
      byte[] bytes3,
      byte[] bytes4,
      byte[] bytes5,
      byte[] bytes6,
      byte[] bytes7,
      byte[] bytes8,
      byte[] bytes9,
      byte[] bytes10,
      byte[] bytes11,
      byte[] bytes12,
      int n2,
      List list6,
      int n3,
      List list7,
      List list8,
      List list9,
      int n4,
      List list10,
      List list11,
      List list12,
      List list13,
      int n5,
      List list14,
      List list15,
      List list16,
      List list17,
      int n6,
      List list18,
      List list19,
      List list20,
      int n7,
      List list21,
      List list22
   ) {
      this.totalMemory = bytes;
      this.physicalMemoryCount = n;
      this.memoryManufacturers = list;
      this.memoryTypes = list2;
      this.memoryBankLabels = list3;
      this.memoryCapacities = list4;
      this.memoryClockSpeeds = list5;
      this.hardwareUuid = bytes2;
      this.systemSerialNumber = bytes3;
      this.systemManufacturer = bytes4;
      this.systemModel = bytes5;
      this.baseboardSerialNumber = bytes6;
      this.logicalProcessorCount = bytes7;
      this.physicalProcessorCount = bytes8;
      this.processorMaxFreq = bytes9;
      this.processorVendor = bytes10;
      this.processorId = bytes11;
      this.processorIdentifier = bytes12;
      this.displayCount = n2;
      this.displayEdids = list6;
      this.diskStoreCount = n3;
      this.diskSizes = list7;
      this.diskSerials = list8;
      this.diskModels = list9;
      this.graphicsCardCount = n4;
      this.graphicsCardVrams = list10;
      this.graphicsCardDeviceIds = list11;
      this.graphicsCardNames = list12;
      this.graphicsCardVendors = list13;
      this.powerSourceCount = n5;
      this.powerSourceNames = list14;
      this.powerSourceDeviceNames = list15;
      this.powerSourceManufacturers = list16;
      this.powerSourceSerialNumbers = list17;
      this.networkInterfaceCount = n6;
      this.networkInterfaceNames = list18;
      this.networkInterfaceDisplayNames = list19;
      this.networkInterfaceMacAddresses = list20;
      this.soundCardCount = n7;
      this.soundCardNames = list21;
      this.soundCardCodecs = list22;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.totalMemory = nettyPacketBuf.readByteArray();
      this.physicalMemoryCount = nettyPacketBuf.readInt();
      this.memoryManufacturers = nettyPacketBuf.readByteArrayList();
      this.memoryTypes = nettyPacketBuf.readByteArrayList();
      this.memoryBankLabels = nettyPacketBuf.readByteArrayList();
      this.memoryCapacities = nettyPacketBuf.readByteArrayList();
      this.memoryClockSpeeds = nettyPacketBuf.readByteArrayList();
      this.hardwareUuid = nettyPacketBuf.readByteArray();
      this.systemSerialNumber = nettyPacketBuf.readByteArray();
      this.systemManufacturer = nettyPacketBuf.readByteArray();
      this.systemModel = nettyPacketBuf.readByteArray();
      this.baseboardSerialNumber = nettyPacketBuf.readByteArray();
      this.logicalProcessorCount = nettyPacketBuf.readByteArray();
      this.physicalProcessorCount = nettyPacketBuf.readByteArray();
      this.processorMaxFreq = nettyPacketBuf.readByteArray();
      this.processorVendor = nettyPacketBuf.readByteArray();
      this.processorId = nettyPacketBuf.readByteArray();
      this.processorIdentifier = nettyPacketBuf.readByteArray();
      this.displayCount = nettyPacketBuf.readInt();
      this.displayEdids = nettyPacketBuf.readByteArrayList();
      this.graphicsCardCount = nettyPacketBuf.readInt();
      this.graphicsCardVrams = nettyPacketBuf.readByteArrayList();
      this.graphicsCardDeviceIds = nettyPacketBuf.readByteArrayList();
      this.graphicsCardNames = nettyPacketBuf.readByteArrayList();
      this.graphicsCardVendors = nettyPacketBuf.readByteArrayList();
      this.diskStoreCount = nettyPacketBuf.readInt();
      this.diskSizes = nettyPacketBuf.readByteArrayList();
      this.diskSerials = nettyPacketBuf.readByteArrayList();
      this.diskModels = nettyPacketBuf.readByteArrayList();
      this.powerSourceCount = nettyPacketBuf.readInt();
      this.powerSourceNames = nettyPacketBuf.readByteArrayList();
      this.powerSourceDeviceNames = nettyPacketBuf.readByteArrayList();
      this.powerSourceManufacturers = nettyPacketBuf.readByteArrayList();
      this.powerSourceSerialNumbers = nettyPacketBuf.readByteArrayList();
      this.networkInterfaceCount = nettyPacketBuf.readInt();
      this.networkInterfaceNames = nettyPacketBuf.readByteArrayList();
      this.networkInterfaceDisplayNames = nettyPacketBuf.readByteArrayList();
      this.networkInterfaceMacAddresses = nettyPacketBuf.readByteArrayList();
      this.soundCardCount = nettyPacketBuf.readInt();
      this.soundCardNames = nettyPacketBuf.readByteArrayList();
      this.soundCardCodecs = nettyPacketBuf.readByteArrayList();
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.totalMemory);
      nettyPacketBuf.writeInt(this.physicalMemoryCount);
      nettyPacketBuf.writeByteArrayList(this.memoryManufacturers);
      nettyPacketBuf.writeByteArrayList(this.memoryTypes);
      nettyPacketBuf.writeByteArrayList(this.memoryBankLabels);
      nettyPacketBuf.writeByteArrayList(this.memoryCapacities);
      nettyPacketBuf.writeByteArrayList(this.memoryClockSpeeds);
      nettyPacketBuf.writeByteArray(this.hardwareUuid);
      nettyPacketBuf.writeByteArray(this.systemSerialNumber);
      nettyPacketBuf.writeByteArray(this.systemManufacturer);
      nettyPacketBuf.writeByteArray(this.systemModel);
      nettyPacketBuf.writeByteArray(this.baseboardSerialNumber);
      nettyPacketBuf.writeByteArray(this.logicalProcessorCount);
      nettyPacketBuf.writeByteArray(this.physicalProcessorCount);
      nettyPacketBuf.writeByteArray(this.processorMaxFreq);
      nettyPacketBuf.writeByteArray(this.processorVendor);
      nettyPacketBuf.writeByteArray(this.processorId);
      nettyPacketBuf.writeByteArray(this.processorIdentifier);
      nettyPacketBuf.writeInt(this.displayCount);
      nettyPacketBuf.writeByteArrayList(this.displayEdids);
      nettyPacketBuf.writeInt(this.graphicsCardCount);
      nettyPacketBuf.writeByteArrayList(this.graphicsCardVrams);
      nettyPacketBuf.writeByteArrayList(this.graphicsCardDeviceIds);
      nettyPacketBuf.writeByteArrayList(this.graphicsCardNames);
      nettyPacketBuf.writeByteArrayList(this.graphicsCardVendors);
      nettyPacketBuf.writeInt(this.diskStoreCount);
      nettyPacketBuf.writeByteArrayList(this.diskSizes);
      nettyPacketBuf.writeByteArrayList(this.diskSerials);
      nettyPacketBuf.writeByteArrayList(this.diskModels);
      nettyPacketBuf.writeInt(this.powerSourceCount);
      nettyPacketBuf.writeByteArrayList(this.powerSourceNames);
      nettyPacketBuf.writeByteArrayList(this.powerSourceDeviceNames);
      nettyPacketBuf.writeByteArrayList(this.powerSourceManufacturers);
      nettyPacketBuf.writeByteArrayList(this.powerSourceSerialNumbers);
      nettyPacketBuf.writeInt(this.networkInterfaceCount);
      nettyPacketBuf.writeByteArrayList(this.networkInterfaceNames);
      nettyPacketBuf.writeByteArrayList(this.networkInterfaceDisplayNames);
      nettyPacketBuf.writeByteArrayList(this.networkInterfaceMacAddresses);
      nettyPacketBuf.writeInt(this.soundCardCount);
      nettyPacketBuf.writeByteArrayList(this.soundCardNames);
      nettyPacketBuf.writeByteArrayList(this.soundCardCodecs);
   }



}
