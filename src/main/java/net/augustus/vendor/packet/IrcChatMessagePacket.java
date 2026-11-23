package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class IrcChatMessagePacket implements VendorPacket {
   private String senderName;
   private int senderLevel;
   private String recipientName;
   private String message;
   private int recipientLevel;
   private String recipientRank;
   private boolean privateMessage;
   private String senderRank;

   public int getRecipientLevel() {
      return this.recipientLevel;
   }

   public String getSenderName() {
      return this.senderName;
   }

   public String getRecipientName() {
      return this.recipientName;
   }

   public int getSenderLevel() {
      return this.senderLevel;
   }

   public boolean isPrivateMessage() {
      return this.privateMessage;
   }

   public IrcChatMessagePacket() {
   }

   public String getSenderRank() {
      return this.senderRank;
   }

   public String getRecipientRank() {
      return this.recipientRank;
   }

   public String getMessage() {
      return this.message;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.senderName = buf.readNullTerminatedAscii();
      this.senderRank = buf.readUtfString();
      this.recipientName = buf.readNullTerminatedAscii();
      this.recipientRank = buf.readUtfString();
      this.message = buf.readUtfString();
      this.privateMessage = buf.readBoolean();
      this.senderLevel = buf.readInt();
      this.recipientLevel = buf.readInt();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeNullTerminatedAscii(this.senderName);
      buf.writeUtfString(this.senderRank);
      buf.writeNullTerminatedAscii(this.recipientName);
      buf.writeUtfString(this.recipientRank);
      buf.writeUtfString(this.message);
      buf.writeBoolean(this.privateMessage);
      buf.writeInt(this.senderLevel);
      buf.writeInt(this.recipientLevel);
   }

   public IrcChatMessagePacket(String str, String str2, int n, String str3, String str4, int n2, String str5, boolean flag) {
      this.senderName = str;
      this.senderRank = str2;
      this.recipientName = str3;
      this.recipientRank = str4;
      this.message = str5;
      this.privateMessage = flag;
      this.senderLevel = n;
      this.recipientLevel = n2;
   }
}
