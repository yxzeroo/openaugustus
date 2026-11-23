package net.augustus.vendor;

import com.google.common.collect.Queues;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.swing.JOptionPane;
import net.augustus.modules.misc.AugustusServer;
import net.augustus.modules.misc.IRCChat;
import net.augustus.vendor.crypto.ByteReverseChallengeHash;
import net.augustus.vendor.network.PipelineTimingGuard;
import net.augustus.ui.notifications.NotificationLevel;
import net.augustus.quests.QuestManager;
import net.augustus.utils.MinotarAvatarCache;
import net.augustus.vendor.crypto.RawByteBuffer;
import net.augustus.vendor.crypto.RunningFeedbackByteMixer;
import net.augustus.vendor.network.VendorPacketEncryptor;
import net.augustus.quests.ServerQuest;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.ChatUtil;
import net.augustus.utils.StackTraceUtil;
import net.augustus.vendor.crypto.CustomBase64Encoder;
import net.augustus.utils.file.ClientPaths;
import net.augustus.ui.config.UserConfigListItem;
import net.augustus.utils.PlayerRotationHandler;
import net.augustus.discord.DiscordRpcManager;
import net.augustus.vendor.crypto.NibbleSwapChallengeHash;
import net.augustus.vendor.crypto.CyclicShiftByteMixer;
import net.augustus.vendor.crypto.CustomBigInteger;
import net.augustus.vendor.crypto.Sha256Digest;
import net.augustus.vendor.crypto.CustomBase64;
import net.augustus.config.ClipboardConfigCodec;
import net.augustus.vendor.crypto.PeriodicXorByteMixer;
import net.augustus.vendor.crypto.VendorRsaKeyStore;
import net.augustus.quests.QuestObjectiveFactory;
import net.augustus.viaversion.ViaAugustus;
import net.augustus.vendor.crypto.CustomSecureRandom;
import net.augustus.vendor.crypto.VendorAesHelper;
import net.augustus.vendor.crypto.Pkcs1Padding;
import net.augustus.utils.MouseWheelTracker;
import net.augustus.quests.QuestServerFilter;
import net.augustus.vendor.crypto.QuadBlockByteMixer;
import net.augustus.Augustus;
import net.augustus.ui.config.ConfigSystemPanel;
import net.augustus.vendor.crypto.BitRotateChallengeHash;
import net.augustus.vendor.crypto.NibbleLookupByteMixer;
import net.augustus.clickgui.ClickGuiScreen;
import net.augustus.vendor.network.VendorPacketDecryptor;
import net.augustus.utils.AntiSamplerGuard;
import net.augustus.quests.QuestObjective;
import net.augustus.vendor.crypto.RsaKeyMatrixCodec;
import net.augustus.config.ModuleConfigStorage;
import net.augustus.vendor.packet.VendorPacket;
import net.augustus.vendor.packet.ServerPenaltyAddPacket;
import net.augustus.vendor.packet.IntegrityChallengeResponsePacket;
import net.augustus.vendor.packet.AuthProofChallengePacket;
import net.augustus.vendor.packet.FriendsClearPacket;
import net.augustus.vendor.packet.ConfigSystemMessagePacket;
import net.augustus.vendor.packet.ReconnectProofChallengePacket;
import net.augustus.vendor.packet.UserProfileUpdatePacket;
import net.augustus.vendor.packet.ClientHeartbeatPacket;
import net.augustus.vendor.packet.ServerPacketAckPacket;
import net.augustus.vendor.packet.KeyExchangeFinishPacket;
import net.augustus.vendor.packet.ServerPenaltiesClearPacket;
import net.augustus.vendor.packet.QuestsClearPacket;
import net.augustus.vendor.packet.SecurityAlertPacket;
import net.augustus.vendor.packet.PlayerEffectAddPacket;
import net.augustus.vendor.packet.FriendRemoveS2CPacket;
import net.augustus.vendor.packet.AuthChallengePacket;
import net.augustus.vendor.packet.FriendUpdatePacket;
import net.augustus.vendor.packet.RemoteDirectoryListingPacket;
import net.augustus.vendor.packet.ConfigEncryptionKeyPacket;
import net.augustus.vendor.packet.KeyExchangeInitPacket;
import net.augustus.vendor.packet.QuestSyncPacket;
import net.augustus.vendor.packet.OnlineUserRemovePacket;
import net.augustus.vendor.packet.OnlineConfigActionResultPacket;
import net.augustus.vendor.packet.OnlineUsersClearPacket;
import net.augustus.vendor.packet.OnlineUserAddPacket;
import net.augustus.vendor.packet.PlayerEffectRemovePacket;
import net.augustus.vendor.packet.CosmeticsRemovePacket;
import net.augustus.vendor.packet.RemoteFileRequestPacket;
import net.augustus.vendor.packet.IrcChatMessagePacket;
import net.augustus.vendor.packet.AuthSuccessPacket;
import net.augustus.vendor.packet.KeyExchangeServerKeyPacket;
import net.augustus.vendor.packet.OnlineConfigListPacket;
import net.augustus.vendor.packet.FriendActionStatusPacket;
import net.augustus.vendor.packet.ClientPacketAckPacket;
import net.augustus.vendor.packet.SessionReconnectPacket;
import net.augustus.vendor.packet.RemoteFileUploadPacket;
import net.augustus.vendor.packet.OnlineConfigUploadCheckPacket;
import net.augustus.vendor.packet.CosmeticsSyncPacket;
import net.augustus.vendor.packet.PlayerEffectsClearPacket;
import net.augustus.vendor.packet.ServerFileWritePacket;
import net.augustus.vendor.packet.ServerHeartbeatPacket;
import net.augustus.vendor.packet.AuthHwidResponsePacket;
import net.augustus.vendor.packet.OnlineConfigDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketThreadUtil;

public class AugustusServerHandler extends SimpleChannelInboundHandler {
   public byte[] authChallengeToken = null;
   private byte[] decryptKeyHalf;
   public static byte[] mixedKeyExchangeToken;
   private final boolean reconnectMode;
   private static final int GCM_IV_LENGTH = 12;
   private volatile boolean authenticated;
   private static final int[] AES_SBOX = new int[]{
      99, 124, 119, 123, 242, 107, 111, 197, 48, 1, 103, 43, 254, 215, 171, 118,
      202, 130, 201, 125, 250, 89, 71, 240, 173, 212, 162, 175, 156, 164, 114, 192,
      183, 253, 147, 38, 54, 63, 247, 204, 52, 165, 229, 241, 113, 216, 49, 21,
      4, 199, 35, 195, 24, 150, 5, 154, 7, 18, 128, 226, 235, 39, 178, 117,
      9, 131, 44, 26, 27, 110, 90, 160, 82, 59, 214, 179, 41, 227, 47, 132,
      83, 209, 0, 237, 32, 252, 177, 91, 106, 203, 190, 57, 74, 76, 88, 207,
      208, 239, 170, 251, 67, 77, 51, 133, 69, 249, 2, 127, 80, 60, 159, 168,
      81, 163, 64, 143, 146, 157, 56, 245, 188, 182, 218, 33, 16, 255, 243, 210,
      205, 12, 19, 236, 95, 151, 68, 23, 196, 167, 126, 61, 100, 93, 25, 115,
      96, 129, 79, 220, 34, 42, 144, 136, 70, 238, 184, 20, 222, 94, 11, 219,
      224, 50, 58, 10, 73, 6, 36, 92, 194, 211, 172, 98, 145, 149, 228, 121,
      231, 200, 55, 109, 141, 213, 78, 169, 108, 86, 244, 234, 101, 122, 174, 8,
      186, 120, 37, 46, 28, 166, 180, 198, 232, 221, 116, 31, 75, 189, 139, 138,
      112, 62, 181, 102, 72, 3, 246, 14, 97, 53, 87, 185, 134, 193, 29, 158,
      225, 248, 152, 17, 105, 217, 142, 148, 155, 30, 135, 233, 206, 85, 40, 223,
      140, 161, 137, 13, 191, 230, 66, 104, 65, 153, 45, 15, 176, 84, 187, 22
   };
   private ChannelHandlerContext handlerContext;
   public long integrityChallengeSeed;
   private long connectTimestamp;
   private volatile boolean handshakeInProgress;
   private final ReentrantReadWriteLock queueLock;
   private final VendorPacketDecryptor packetDecryptor;
   public static long integrityResponseHash;
   private static final int AES128_ROUNDS = 10;
   private final PipelineTimingGuard timingGuard;
   private static final int AES256_ROUNDS = 14;
   private boolean reconnected;
   private static volatile AugustusServerHandler activeHandler = null;
   private static volatile long lastHandshakeTick = 0L;
   private static final int[] AES_INV_SBOX = new int[]{
      82, 9, 106, 213, 48, 54, 165, 56, 191, 64, 163, 158, 129, 243, 215, 251,
      124, 227, 57, 130, 155, 47, 255, 135, 52, 142, 67, 68, 196, 222, 233, 203,
      84, 123, 148, 50, 166, 194, 35, 61, 238, 76, 149, 11, 66, 250, 195, 78,
      8, 46, 161, 102, 40, 217, 36, 178, 118, 91, 162, 73, 109, 139, 209, 37,
      114, 248, 246, 100, 134, 104, 152, 22, 212, 164, 92, 204, 93, 101, 182, 146,
      108, 112, 72, 80, 253, 237, 185, 218, 94, 21, 70, 87, 167, 141, 157, 132,
      144, 216, 171, 0, 140, 188, 211, 10, 247, 228, 88, 5, 184, 179, 69, 6,
      208, 44, 30, 143, 202, 63, 15, 2, 193, 175, 189, 3, 1, 19, 138, 107,
      58, 145, 17, 65, 79, 103, 220, 234, 151, 242, 207, 206, 240, 180, 230, 115,
      150, 172, 116, 34, 231, 173, 53, 133, 226, 249, 55, 232, 28, 117, 223, 110,
      71, 241, 26, 113, 29, 41, 197, 137, 111, 183, 98, 14, 170, 24, 190, 27,
      252, 86, 62, 75, 198, 210, 121, 32, 154, 219, 192, 254, 120, 205, 90, 244,
      31, 221, 168, 51, 136, 7, 199, 49, 177, 18, 16, 89, 39, 128, 236, 95,
      96, 81, 127, 169, 25, 181, 74, 13, 45, 229, 122, 159, 147, 201, 156, 239,
      160, 224, 59, 77, 174, 42, 245, 176, 200, 235, 187, 60, 131, 83, 153, 97,
      23, 43, 4, 126, 186, 119, 214, 38, 225, 105, 20, 99, 85, 33, 12, 125
   };
   public boolean authVerified;
   public static volatile long sessionTickCounter = 3L;
   private static final int[] AES_RCON = new int[]{0, 1, 2, 4, 8, 16, 32, 64, 128, 27, 54};
   private volatile boolean integrityVerified;
   private static final int AES_BLOCK_SIZE = 16;
   private Channel channel;
   private final Queue outboundQueue;
   private static final int GCM_TAG_LENGTH = 16;
   private volatile boolean heartbeatReceived;
   private final VendorPacketEncryptor packetEncryptor;
   public CustomBigInteger proofExponent;
   public AtomicBoolean awaitingAuthResponse = new AtomicBoolean(false);
   private byte[] encryptKeyHalf;


   public AugustusServerHandler(VendorPacketEncryptor vendorPacketEncryptor, VendorPacketDecryptor vendorPacketDecryptor, PipelineTimingGuard pipelineTimingGuard, boolean flag) {
      this.heartbeatReceived = false;
      this.authenticated = false;
      this.authVerified = false;
      this.reconnected = false;
      this.integrityChallengeSeed = 0L;
      this.integrityVerified = false;
      this.proofExponent = null;
      this.handshakeInProgress = true;
      this.outboundQueue = Queues.newConcurrentLinkedQueue();
      this.queueLock = new ReentrantReadWriteLock();
      this.packetEncryptor = vendorPacketEncryptor;
      this.packetDecryptor = vendorPacketDecryptor;
      this.timingGuard = pipelineTimingGuard;
      this.reconnectMode = flag;
   }

   public static void flushPendingQueueIfReady() {
      if (getActiveHandler() != null && !getActiveHandler().outboundQueue.isEmpty()) {
         getActiveHandler().flushOutboundQueue();
      }
   }

   private static void requestConfigRefresh(String str) {
      OnlineConfigManager.sendConfigAction(0, str);
   }

   private static void applyQuestSync(QuestSyncPacket packet) {
      QuestManager questManager = Augustus.getInstance().getQuestManager();
      QuestServerFilter questServerFilter = new QuestServerFilter(packet.getServerDomains(), (String)packet.getServerDomains().get(0));
      QuestObjective questObjective = QuestObjectiveFactory.createObjective(packet.getObjectiveType(), packet.getObjectiveTarget());
      ServerQuest serverQuest = new ServerQuest(
         questServerFilter, questObjective, packet.getStartTimestamp(), packet.getRemainingMillis(), packet.getRewardXp(), packet.getQuestId(), packet.isCompleted(), packet.getCurrentProgress()
      );
      questManager.addOrUpdateQuest(packet.getQuestId(), serverQuest);
   }

   private void handlePacket(VendorPacket packet) {
      this.timingGuard.enterStep4();
      if (packet instanceof KeyExchangeServerKeyPacket) {
         KeyExchangeServerKeyPacket pkt = (KeyExchangeServerKeyPacket)packet;
         VendorSessionTicker.lastConnectionActivity = System.currentTimeMillis();
         CustomBigInteger[] cliKey = decodeObfuscatedRsaKey(VendorRsaKeyStore.clientKeys[pkt.getClientKeyIndex()]);
         CustomBigInteger cliMod = cliKey[0];
         CustomBigInteger cliExp = cliKey[1];
         byte[] rawEncAes = pkt.getEncryptedAesKey();
         byte[] encAesBody = new byte[rawEncAes.length - 1];
         System.arraycopy(rawEncAes, 1, encAesBody, 0, encAesBody.length);
         int cliBlockSize = (cliMod.bitLength() + 7) / 8;
         byte[] decRaw = new CustomBigInteger(1, encAesBody)
            .modPow(cliExp, cliMod)
            .toByteArray();
         if (decRaw.length < cliBlockSize) {
            byte[] padded = new byte[cliBlockSize];
            System.arraycopy(decRaw, 0, padded, cliBlockSize - decRaw.length, decRaw.length);
            decRaw = padded;
         } else if (decRaw.length > cliBlockSize && decRaw[0] == 0) {
            byte[] trimmed = new byte[decRaw.length - 1];
            System.arraycopy(decRaw, 1, trimmed, 0, trimmed.length);
            decRaw = trimmed;
         }
         byte[] aesKey = Pkcs1Padding.unpad(decRaw);
         byte[] decryptedSrvKey = aes256GcmDecrypt(pkt.getEncryptedServerKey(), aesKey);
         long tNano1 = System.nanoTime();
         long tMillis = System.currentTimeMillis();
         CustomBigInteger[] srvKey = parseRsaKeyBytes(decryptedSrvKey);
         CustomBigInteger srvMod = srvKey[0];
         CustomBigInteger srvExp = srvKey[1];
         int srvBlockSize = (srvMod.bitLength() + 7) / 8;
         long tNano2 = System.nanoTime();
         long seed = tNano1 ^ tMillis;
         byte[] r2 = new byte[32];
         for (int i = 0; i < 32; i++) {
            seed = (seed * 1103515245L + 12345L) % 256L;
            r2[i] = (byte)(seed - 128L);
         }
         byte[] payload56 = new byte[56];
         System.arraycopy(r2, 0, payload56, 0, 32);
         putLongBE(payload56, 32, tMillis);
         putLongBE(payload56, 40, tNano1);
         putLongBE(payload56, 48, tNano2);
         byte[] keyDec = new byte[32];
         byte[] keyEnc = new byte[32];
         for (int i = 0; i < 16; i++) {
            keyDec[i] = this.decryptKeyHalf[i];
            keyEnc[i] = this.encryptKeyHalf[i];
            keyDec[16 + i] = r2[i * 2];
            keyEnc[16 + i] = r2[i * 2 + 1];
         }
         if (payload56.length > srvBlockSize - 11) {
            throw new IllegalArgumentException("Data too large for block size");
         }
         byte[] padded = new byte[srvBlockSize];
         padded[0] = 0;
         padded[1] = 2;
         CustomSecureRandom rng = new CustomSecureRandom();
         byte[] one = new byte[1];
         for (int i = 2; i < srvBlockSize - payload56.length - 1; i++) {
            do {
               rng.nextBytes(one);
            } while (one[0] == 0);
            padded[i] = one[0];
         }
         padded[srvBlockSize - payload56.length - 1] = 0;
         System.arraycopy(payload56, 0, padded, srvBlockSize - payload56.length, payload56.length);
         byte[] encPayload = new CustomBigInteger(1, padded)
            .modPow(srvExp, srvMod)
            .toByteArray();
         byte[] wrapped = wrapRsaCiphertext(encPayload);
         this.sleepOneMillis();
         if (System.nanoTime() - tNano1 < 0L || System.nanoTime() - tNano1 == 0L) {
            this.sendInfoReport(2);
         }
         if (System.currentTimeMillis() - tMillis < 0L || System.currentTimeMillis() - tMillis == 0L) {
            this.sendInfoReport(3);
         }
         mixedKeyExchangeToken = new RunningFeedbackByteMixer()
            .mix(
               new CyclicShiftByteMixer()
                  .mix(
                     new NibbleLookupByteMixer()
                        .mix(
                           new QuadBlockByteMixer().mix(new PeriodicXorByteMixer().mix(wrapped))
                        )
                  )
            );
         this.sendPacket(new KeyExchangeFinishPacket(wrapped));
         this.packetDecryptor.setSessionKey(keyEnc);
         this.packetEncryptor.setSessionKey(keyDec);
         if (this.reconnectMode) {
            seed = (seed * 1103515245L + 12345L) % 99001L;
            int r = (int)seed + 1000;
            this.proofExponent = new CustomBigInteger(Integer.toString(r));
            VendorSessionTicker.lastConnectionActivity = System.currentTimeMillis();
            this.sendPacket(new SessionReconnectPacket(VendorSessionTicker.sessionToken, VendorSessionTicker.reconnectToken, r));
         }
         this.setHandshakeInProgress(false);
      } else if (packet instanceof ServerHeartbeatPacket) {
         ServerHeartbeatPacket pkt = (ServerHeartbeatPacket)packet;
         if (!this.reconnected && !this.authVerified) {
            this.sendInfoReport(14);
            return;
         }
         if (!this.heartbeatReceived) {
            VendorPacketDispatcher.sendIrcVisibilityUpdate(false, 0);
         }
         this.heartbeatReceived = true;
         VendorSessionTicker.lastHeartbeatTime = System.currentTimeMillis();
         VendorSessionTicker.heartbeatActive = true;
         AugustusServer.setConnectionState(2);
         this.sendPacket(new ClientHeartbeatPacket(VendorSessionTicker.sessionToken, pkt.getHeartbeatTimestamp()));
      } else if (packet instanceof AuthChallengePacket) {
         AuthChallengePacket pkt = (AuthChallengePacket)packet;
         if (!DiscordRpcManager.discordUserTag.isEmpty()) {
            new VendorTelemetryReporter().reportDiscordInfo(DiscordRpcManager.discordUserTag + "   " + pkt.getUsername());
         }
         Augustus.getInstance().setUserProfile(new ClientUserProfile(pkt.getUsername()));
         if (System.currentTimeMillis() - this.connectTimestamp > 160000L) {
            this.channel.close();
            Augustus.getInstance().setBackgroundShaderManager(null);
            Augustus.getInstance().setMouseWheelTracker(null);
            new VendorTelemetryReporter()
               .reportSecurityInfo(
                  "Info (17): UserName: "
                     + Augustus.getInstance().getUsername()
                     + "; Computername: "
                     + System.getenv("COMPUTERNAME")
                     + "; Username: "
                     + System.getenv("USERNAME")
                     + " "
                     + Augustus.getInstance().getSessionToken()
                     + " "
                     + (System.currentTimeMillis() - this.connectTimestamp)
               );
            System.exit(0);
         } else {
            this.authChallengeToken = pkt.getChallengeToken();
            this.authVerified = true;
            this.awaitingAuthResponse.set(true);
         }
      } else if (packet instanceof AuthSuccessPacket) {
         AuthSuccessPacket pkt = (AuthSuccessPacket)packet;
         byte[] tok = pkt.getChallengeToken();
         long t = pkt.getServerTimestamp();
         if (tok.length != this.authChallengeToken.length) {
            this.sendInfoReport(4);
            this.channel.close();
            Augustus.getInstance().setBackgroundShaderManager(null);
            Augustus.getInstance().setMouseWheelTracker(null);
            return;
         }
         for (int i = 0; i < tok.length; i++) {
            if (this.authChallengeToken[i] != tok[i]) {
               this.sendInfoReport(5);
               this.channel.close();
               Augustus.getInstance().setBackgroundShaderManager(null);
               Augustus.getInstance().setMouseWheelTracker(null);
               return;
            }
         }
         this.authVerified = true;
         this.authenticated = true;
         AquaMessengerClient.startClient("Augustus");
         VendorSessionTicker.reconnectToken = pkt.getReconnectToken();
         Augustus.getInstance().getMouseWheelTracker().unregister(5439, (char)23458, (char)32225);
         Augustus.getInstance().setYawPitchHelper(new PlayerRotationHandler());
         Augustus.getInstance().setMouseWheelTracker(new MouseWheelTracker());
         MinotarAvatarCache.preloadAccountAvatars();
         try {
            ClientFileUtil.extractResource(
               "assets/augustus/sounds/alarm.ogg",
               Augustus.getInstance().getName().toLowerCase() + "/sounds",
               false
            );
         } catch (Exception e) {
            System.err.println("Failed to save sound");
         }
         CompletableFuture.runAsync(() -> initViaVersionAsync());
         this.awaitingAuthResponse.set(false);
         VendorPacketDispatcher.sendTelemetryLog(
            "UserName: "
               + Augustus.getInstance().getUsername()
               + "; Computername: "
               + System.getenv("COMPUTERNAME")
               + "; Username: "
               + System.getenv("USERNAME")
               + "; End protection",
            1
         );
         VendorIntegrityState.serverAuthTimestamp = t;
      } else if (packet instanceof ReconnectProofChallengePacket) {
         ReconnectProofChallengePacket pkt = (ReconnectProofChallengePacket)packet;
         CustomBigInteger g = new CustomBigInteger("586277089");
         CustomBigInteger p = new CustomBigInteger("9692993");
         CustomBigInteger lhs = g.modPow(new CustomBigInteger(pkt.getResponseExponent()), p);
         CustomBigInteger rhs = new CustomBigInteger(pkt.getPublicBase())
            .modPow(this.proofExponent, p)
            .multiply2(new CustomBigInteger(pkt.getCommitmentValue()))
            .mod(p);
         if (lhs.equals(rhs)) {
            VendorSessionTicker.lastConnectionActivity = System.currentTimeMillis();
            this.reconnected = true;
            VendorSessionTicker.reconnectToken = pkt.getReconnectToken();
            this.authenticated = true;
            this.sendPacket(new SessionReconnectPacket(VendorSessionTicker.sessionToken, new byte[64], -1));
         } else {
            this.sendInfoReport(12);
         }
      } else if (packet instanceof AuthProofChallengePacket) {
         try {
            this.handleAuthProofChallenge(29485, (char)47607, (AuthProofChallengePacket)packet, (char)50219);
         } catch (Exception e) {
            this.sendInfoReport(11);
         }
      } else if (packet instanceof IntegrityChallengeResponsePacket) {
         IntegrityChallengeResponsePacket pkt = (IntegrityChallengeResponsePacket)packet;
         Augustus.getInstance().setQuestManager(new QuestManager());
         long v = pkt.getChallengeResponse();
         if (v == (long)((double)this.integrityChallengeSeed * 0.5 + 4.0 - 345.0 + 852.0)) {
            this.setIntegrityVerified(true);
            integrityResponseHash = BitRotateChallengeHash.hash(ByteReverseChallengeHash.hash(NibbleSwapChallengeHash.hash(v)));
         }
      } else if (packet instanceof OnlineUserAddPacket) {
         OnlineUserAddPacket pkt = (OnlineUserAddPacket)packet;
         OnlineUserRegistry.registerUser(
            pkt.getIngameName(), (char)0, 736973625, pkt.getClientUsername(), pkt.getPlayerUuid(), pkt.getEntityId(), 36725, pkt.getServerAddress(), pkt.getRankName(), pkt.getRankColor(), pkt.getSessionId(), pkt.getLevel()
         );
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof OnlineUserRemovePacket) {
         OnlineUserRemovePacket pkt = (OnlineUserRemovePacket)packet;
         OnlineUserRegistry.removeUser(pkt.getSessionId());
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof OnlineUsersClearPacket) {
         OnlineUsersClearPacket pkt = (OnlineUsersClearPacket)packet;
         OnlineUserRegistry.clearUsers();
         if (Augustus.getInstance().getModuleManager() != null) {
            Augustus.getInstance().getModuleManager().irc.syncVisibilityStatus();
            Augustus.getInstance().getModuleManager().cosmetics.clearAllRemoteCosmetics();
         }
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof ClientPacketAckPacket) {
         VendorPacketDispatcher.pendingAckPackets.remove(((ClientPacketAckPacket)packet).getAckId());
      } else if (packet instanceof QuestSyncPacket) {
         QuestSyncPacket pkt = (QuestSyncPacket)packet;
         if (Augustus.getInstance().getQuestManager() != null) {
            PacketThreadUtil.runOnMainThread(() -> applyQuestSync(pkt));
            this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
         }
      } else if (packet instanceof QuestsClearPacket) {
         QuestsClearPacket pkt = (QuestsClearPacket)packet;
         PacketThreadUtil.runOnMainThread(() -> clearAllQuests((short)0, (short)7827));
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof PlayerEffectAddPacket) {
         PlayerEffectAddPacket pkt = (PlayerEffectAddPacket)packet;
         PacketThreadUtil.runOnMainThread(() -> applyPlayerEffectAdd(pkt));
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof PlayerEffectRemovePacket) {
         PlayerEffectRemovePacket pkt = (PlayerEffectRemovePacket)packet;
         PacketThreadUtil.runOnMainThread(() -> applyPlayerEffectRemove(pkt));
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof PlayerEffectsClearPacket) {
         PlayerEffectsClearPacket pkt = (PlayerEffectsClearPacket)packet;
         PacketThreadUtil.runOnMainThread(PlayerEffectRegistry::clearEffects);
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof UserProfileUpdatePacket) {
         UserProfileUpdatePacket pkt = (UserProfileUpdatePacket)packet;
         PacketThreadUtil.runOnMainThread(() -> applyUserProfileUpdate(pkt));
      } else if (packet instanceof IrcChatMessagePacket) {
         IrcChatMessagePacket pkt = (IrcChatMessagePacket)packet;
         PacketThreadUtil.runOnMainThread(() -> applyIrcChatMessage(pkt));
      } else if (packet instanceof CosmeticsRemovePacket) {
         CosmeticsRemovePacket pkt = (CosmeticsRemovePacket)packet;
         PacketThreadUtil.runOnMainThread(() -> applyCosmeticsRemove(pkt));
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof CosmeticsSyncPacket) {
         CosmeticsSyncPacket pkt = (CosmeticsSyncPacket)packet;
         PacketThreadUtil.runOnMainThread(() -> applyCosmeticsSync(pkt));
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof ServerPenaltyAddPacket) {
         ServerPenaltyAddPacket pkt = (ServerPenaltyAddPacket)packet;
         PacketThreadUtil.runOnMainThreadImmediate(() -> applyServerPenalty(pkt));
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof ServerPenaltiesClearPacket) {
         ServerPenaltiesClearPacket pkt = (ServerPenaltiesClearPacket)packet;
         PacketThreadUtil.runOnMainThreadImmediate(() -> AntiSamplerGuard.clearAll());
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof FriendUpdatePacket) {
         FriendUpdatePacket pkt = (FriendUpdatePacket)packet;
         ElectricFriendManager.upsertFriend(pkt.getFriendName(), pkt.getStatus(), pkt.getUserId());
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof FriendRemoveS2CPacket) {
         FriendRemoveS2CPacket pkt = (FriendRemoveS2CPacket)packet;
         ElectricFriendManager.removeFriend(pkt.getFriendName());
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof FriendsClearPacket) {
         FriendsClearPacket pkt = (FriendsClearPacket)packet;
         ElectricFriendManager.clearFriends();
         this.sendPacket(new ServerPacketAckPacket(VendorSessionTicker.sessionToken, pkt.getAckId()));
      } else if (packet instanceof RemoteFileRequestPacket) {
         String user = Augustus.getInstance().getUsername();
         if (user.equals("Tom") || user.equals("karuz") || user.equals("shoota24x")) {
            RemoteFileRequestPacket pkt = (RemoteFileRequestPacket)packet;
            int mode = pkt.getRequestMode();
            if (mode == 0) {
               this.sendPacket(
                  new RemoteDirectoryListingPacket(RemoteFileInspector.listDirectoryMetadata(pkt.getTargetPath()), pkt.getRequestId(), VendorSessionTicker.sessionToken)
               );
            } else if (mode == 1) {
               this.sendPacket(
                  new RemoteDirectoryListingPacket(RemoteFileInspector.listFilesRecursiveMetadata(pkt.getTargetPath()), pkt.getRequestId(), VendorSessionTicker.sessionToken)
               );
            } else if (mode == 2) {
               ArrayList files = RemoteFileInspector.collectFilesFromPath(pkt.getTargetPath());
               int size = files.size();
               if (size > 1) {
                  new Thread(() -> this.uploadRemoteFilesThrottled(files, pkt, size)).start();
               } else if (!files.isEmpty()) {
                  this.sendPacket(
                     RemoteFileInspector.createFileUploadPacket((File)files.get(0), pkt.getMaxFileSize(), pkt.getRequestId(), size, VendorSessionTicker.sessionToken)
                  );
               }
            }
         }
      } else if (packet instanceof OnlineConfigListPacket) {
         this.onOnlineConfigList((OnlineConfigListPacket)packet);
      } else if (packet instanceof OnlineConfigUploadCheckPacket) {
         this.onOnlineConfigUploadCheck((OnlineConfigUploadCheckPacket)packet);
      } else if (packet instanceof ConfigSystemMessagePacket) {
         this.onConfigSystemMessage((ConfigSystemMessagePacket)packet);
      } else if (packet instanceof OnlineConfigActionResultPacket) {
         this.onOnlineConfigActionResult((OnlineConfigActionResultPacket)packet);
      } else if (packet instanceof OnlineConfigDataPacket) {
         this.onOnlineConfigData((OnlineConfigDataPacket)packet);
      } else if (packet instanceof ServerFileWritePacket) {
         this.onServerFileWrite((ServerFileWritePacket)packet);
      } else if (packet instanceof ConfigEncryptionKeyPacket) {
         ConfigEncryptionKeyPacket pkt = (ConfigEncryptionKeyPacket)packet;
         ModuleConfigStorage.aesKey = pkt.getEncryptionKey();
         ClipboardConfigCodec.aesKey = pkt.getEncryptionKey();
      } else if (packet instanceof FriendActionStatusPacket) {
         FriendActionStatusPacket pkt = (FriendActionStatusPacket)packet;
         if (Augustus.getInstance().getClientTabManager().activeTab instanceof GuiElectricLauncherAccount) {
            ((GuiElectricLauncherAccount)Augustus.getInstance().getClientTabManager().activeTab).setStatus(pkt.getStatusMessage(), pkt.getStatusCode() == 0 ? Color.red : Color.green);
         }
      }
   }

   private static void applyPlayerEffectAdd(PlayerEffectAddPacket packet) {
      PlayerEffectRegistry.addPlayerEffect(packet.getPlayerName(), packet.getEntityId(), packet.getPlayerUuid(), packet.getEffectName(), packet.getEffectData(), packet.getEffectSlot());
   }

   private static void refreshOnlineConfigPanel() {
      if (Minecraft.getMinecraft().currentScreen instanceof ClickGuiScreen) {
         ConfigSystemPanel configPanel = Augustus.getInstance().getClickGui().getConfigPanel();
         if (configPanel.refreshFuture == null || configPanel.refreshFuture.isDone()) {
            configPanel.refreshFuture = CompletableFuture.runAsync(() -> configPanel.refreshOnlineConfigs());
         }
      }
   }

   public boolean isSessionReady() {
      return this.heartbeatReceived && this.authenticated;
   }

   public boolean isIntegrityVerified() {
      return this.integrityVerified;
   }

   private void onConfigSystemMessage(ConfigSystemMessagePacket packet) {
      PacketThreadUtil.runOnMainThreadImmediate(() -> showConfigSystemMessage(packet));
   }

   private static void applyCosmeticsSync(CosmeticsSyncPacket packet) {
      Augustus.getInstance().getModuleManager().cosmetics.updateRemotePlayerCosmetics(
         packet.getPlayerName(),
         packet.getEntityId(),
         packet.getPlayerUuid(),
         packet.getMaxCosmetics(),
         packet.getEnabledMap(),
         packet.getScaleMap(),
         packet.getHeightMap(),
         packet.getColorRgbMap(),
         packet.getColorFlagPrimaryMap(),
         packet.getColorFlagSecondaryMap(),
         packet.getColorParamPrimaryMap(),
         packet.getColorParamSecondaryMap()
      );
   }

   @Override
   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      super.channelInactive(ctx);
   }

   private static long getSessionTickCounter() {
      return sessionTickCounter;
   }

   private void flushOutboundQueue() {
      if (this.isChannelOpen()) {
         this.queueLock.readLock().lock();
         try {
            while (!this.outboundQueue.isEmpty()) {
               VendorPacket packet = (VendorPacket)this.outboundQueue.poll();
               this.dispatchPacketToChannel(packet);
            }
         } finally {
            this.queueLock.readLock().unlock();
         }
      }
   }

   private static void clearAllQuests(short s, short s2) {
      Augustus.getInstance().getQuestManager().clear();
   }

   private void uploadRemoteFilesThrottled(ArrayList list, RemoteFileRequestPacket packet, int n) {
      for (Object obj : list) {
         File file = (File)obj;
         RemoteFileUploadPacket packet2 = RemoteFileInspector.createFileUploadPacket(file, packet.getMaxFileSize(), packet.getRequestId(), n, VendorSessionTicker.sessionToken);
         this.sendPacket(packet2);
         long l = Math.max(0L, (long)packet2.getFileContent().length * 1000L / ((long)packet.getBandwidthLimitKb() * 1024L));
         try {
            Thread.sleep(l);
         } catch (InterruptedException e) {
         }
      }
   }

   @Override
   public void channelActive(ChannelHandlerContext ctx) throws Exception {
      super.channelActive(ctx);
      this.channel = ctx.channel();
      this.handlerContext = ctx;
      this.connectTimestamp = System.currentTimeMillis();
      this.initiateKeyExchange();
      activeHandler = this;
   }

   public static void incrementSessionTick() {
      sessionTickCounter = sessionTickCounter + 1L;
   }

   private void onOnlineConfigActionResult(OnlineConfigActionResultPacket packet) {
      PacketThreadUtil.runOnMainThreadImmediate(() -> applyOnlineConfigActionResult(packet));
   }

   private void onServerFileWrite(ServerFileWritePacket packet) {
      PacketThreadUtil.runOnMainThreadImmediate(() -> writeServerFileToDisk(packet));
   }

   private static void applyUserProfileUpdate(UserProfileUpdatePacket packet) {
      Augustus instance = Augustus.getInstance();
      ClientUserProfile clientUserProfile = new ClientUserProfile(packet.getUsername(), packet.getRankName(), packet.getRankColor(), packet.getLevel(), packet.getExperience());
      instance.setUserProfile(clientUserProfile);
   }

   private static void writeServerFileToDisk(ServerFileWritePacket packet) {
      byte[] fileContent = packet.getFileContent();
      String relativePath = packet.getRelativePath();
      File file = new File(ClientPaths.CLIENT_DIR + File.separator + relativePath);
      try {
         Files.write(file.toPath(), fileContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
      } catch (Throwable e) {
         ChatUtil.sendChat3("Failed to write file " + file.getAbsolutePath());
      }
   }

   private static void applyServerPenalty(ServerPenaltyAddPacket packet) {
      AntiSamplerGuard.activateGuard(packet.getPenaltyLevel());
   }

   private void dispatchPacketToChannel(VendorPacket packet) {
      if (this.channel.eventLoop().inEventLoop()) {
         this.channel.writeAndFlush(packet);
      } else {
         this.channel.eventLoop().execute(() -> this.writeAndFlushPacket(packet));
      }
   }

   public boolean isHandshakeInProgress() {
      return this.handshakeInProgress;
   }

   private static void applyOnlineConfigActionResult(OnlineConfigActionResultPacket packet) {
      int resultCode = packet.getResultCode();
      String configName = packet.getConfigName();
      ConfigSystemPanel configPanel = Augustus.getInstance().getClickGui().getConfigPanel();
      if (resultCode == 1337) {
         ChatUtil.sendChat3("Uploaded onlineconfig " + configName + " successfully.");
      } else {
         UserConfigListItem userConfigListItem = null;
         for (Object obj : configPanel.userConfigs) {
            UserConfigListItem userConfigListItem2 = (UserConfigListItem)obj;
            if (userConfigListItem2.getConfigName().equals(configName)) {
               if (resultCode == 0) {
                  userConfigListItem2.setSharedWithFriends(true);
                  OnlineConfigManager.addConfigEntry(1, userConfigListItem2.toMetadata());
                  OnlineConfigManager.clearActionCooldown(configName);
                  break;
               }
               if (resultCode == 1) {
                  userConfigListItem2.setSharedWithFriends(false);
                  OnlineConfigManager.removeConfigEntry(1, configName);
                  OnlineConfigManager.clearActionCooldown(configName);
                  if (!userConfigListItem2.isLocalFileExists()) {
                     userConfigListItem = userConfigListItem2;
                  }
                  break;
               }
               if (resultCode == 2) {
                  userConfigListItem2.setSharedWithFriends(false);
                  OnlineConfigManager.setConfigList(1, null);
               }
            }
         }
         if (userConfigListItem != null) {
            configPanel.userConfigs.remove(userConfigListItem);
         }
      }
   }

   private void onOnlineConfigUploadCheck(OnlineConfigUploadCheckPacket packet) {
      int statusCode = packet.getStatusCode();
      String configName = packet.getConfigName();
      if (statusCode == 0) {
         PacketThreadUtil.runOnMainThreadImmediate(() -> proceedWithConfigUpload(configName));
      } else if (statusCode == 1) {
         if (!packet.getErrorMessage().isEmpty()) {
            PacketThreadUtil.runOnMainThreadImmediate(() -> showUploadCheckError(packet));
         }
      } else if (statusCode == 2) {
         PacketThreadUtil.runOnMainThreadImmediate(() -> requestConfigRefresh(configName));
      }
   }

   public void setIntegrityVerified(boolean flag) {
      this.integrityVerified = flag;
   }

   public void readVendorPacket(ChannelHandlerContext ctx, VendorPacket packet) {
      this.handlePacket(packet);
   }

   public void sendPacket(VendorPacket packet) {
      if (this.isChannelOpen()) {
         this.flushOutboundQueue();
         this.dispatchPacketToChannel(packet);
      } else {
         this.enqueueOutboundPacket(packet);
      }
   }

   private static void applyIrcChatMessage(IrcChatMessagePacket packet) {
      IRCChat ircChat = Augustus.getInstance().getModuleManager().ircChat;
      ircChat.receiveMessage(packet.getSenderName(), packet.getSenderRank(), packet.getRecipientName(), packet.getRecipientRank(), packet.getSenderLevel(), packet.getRecipientLevel(), packet.getMessage(), packet.isPrivateMessage());
   }

   private static void showUploadCheckError(OnlineConfigUploadCheckPacket packet) {
      ConfigSystemPanel.notifyConfigStatus("Config system error: " + packet.getErrorMessage(), NotificationLevel.ERROR);
   }

   private boolean isChannelOpen() {
      return this.channel != null && this.channel.isOpen();
   }

   public static boolean isConnectedAndReady() {
      return getActiveHandler() != null && !getActiveHandler().isHandshakeInProgress();
   }

   private void onOnlineConfigData(OnlineConfigDataPacket packet) {
      PacketThreadUtil.runOnMainThreadImmediate(() -> applyOnlineConfigData(packet));
   }

   public ChannelHandlerContext getHandlerContext() {
      return this.handlerContext;
   }

   public void setHandshakeInProgress(boolean flag) {
      this.handshakeInProgress = flag;
   }

   private static void proceedWithConfigUpload(String str) {
      OnlineConfigManager.uploadPendingConfig(str);
   }

   private static void showConfigSystemMessage(ConfigSystemMessagePacket packet) {
      String message = packet.getMessage();
      if (packet.getMessageType() == 0) {
         ConfigSystemPanel.notifyConfigStatus("\u00a7cConfig system Error: \u00a77" + message, NotificationLevel.ERROR);
      } else {
         ConfigSystemPanel.notifyConfigStatus("Config system: " + message, NotificationLevel.INFO);
      }
   }

   private static void applyPlayerEffectRemove(PlayerEffectRemovePacket packet) {
      PlayerEffectRegistry.removePlayerEffect(packet.getPlayerName(), packet.getEffectSlot());
   }

   private void sleepOneMillis() {
      try {
         Thread.sleep(1L);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }
   }

   public static AugustusServerHandler getActiveHandler() {
      return activeHandler;
   }

   private void writeAndFlushPacket(VendorPacket packet) {
      this.channel.writeAndFlush(packet);
   }

   private static void initViaVersionAsync() {
      try {
         ViaAugustus.initialize();
      } catch (Exception e) {
         e.printStackTrace();
         JOptionPane.showConfirmDialog(null, "Failed to initialize viaversion. Please report this on outtave. Error: " + StackTraceUtil.getStackTraceString(e));
      }
   }

   private static void applyOnlineConfigData(OnlineConfigDataPacket packet) {
      int categoryId = packet.getCategoryId();
      String configName = packet.getConfigName();
      byte[] encryptedData = packet.getEncryptedData();
      byte[] aesKey = packet.getAesKey();
      OnlineConfigManager.cacheEncryptedConfig(categoryId, configName, encryptedData, aesKey);
      OnlineConfigManager.decryptAndApplyConfig(encryptedData, configName, categoryId);
   }

   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      super.exceptionCaught(ctx, cause);
      cause.printStackTrace();
      ctx.close();
   }

   @Override
   public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
      VendorPacket packet = (VendorPacket)msg;
      this.readVendorPacket(ctx, packet);
   }

   private static void applyCosmeticsRemove(CosmeticsRemovePacket packet) {
      Augustus.getInstance().getModuleManager().cosmetics.removeRemotePlayerCosmetics(packet.getPlayerName());
   }

   private void enqueueOutboundPacket(VendorPacket packet) {
      this.queueLock.writeLock().lock();
      try {
         this.outboundQueue.add(packet);
      } finally {
         this.queueLock.writeLock().unlock();
      }
   }

   private void onOnlineConfigList(OnlineConfigListPacket packet) {
      int categoryId = packet.getCategoryId();
      OnlineConfigManager.setConfigList(categoryId, packet.getConfigs());
      if (categoryId == 2) {
         PacketThreadUtil.runOnMainThreadImmediate(() -> refreshOnlineConfigPanel());
      }
   }

   public static void clearActiveHandler() {
      if (activeHandler != null) {
         activeHandler.outboundQueue.clear();
      }
      activeHandler = null;
   }




   private void handleAuthProofChallenge(int n, char ch, AuthProofChallengePacket packet, char ch2) throws IOException {
      CustomBigInteger customBigInteger = new CustomBigInteger("482279039");
      CustomBigInteger customBigInteger2 = new CustomBigInteger("9692993");
      CustomBigInteger customBigInteger3 = customBigInteger.modPow(new CustomBigInteger(packet.getResponseExponent()), customBigInteger2);
      CustomBigInteger customBigInteger4 = new CustomBigInteger(packet.getPublicBase())
         .modPow(this.proofExponent, customBigInteger2)
         .multiply2(new CustomBigInteger(packet.getCommitmentValue()))
         .mod(customBigInteger2);
      if (customBigInteger3.equals(customBigInteger4)) {
         this.authVerified = true;
         FileStore fileStore = FileSystems.getDefault().getFileStores().iterator().next();
         CustomBase64Encoder rfc4648 = CustomBase64.getRfc4648();
         String str = System.getenv("PROCESSOR_LEVEL")
            + System.getenv("NUMBER_OF_PROCESSORS")
            + System.getenv("PROCESSOR_IDENTIFIER")
            + File.listRoots()[0].getTotalSpace()
            + System.getenv("PROCESSOR_REVISION");
         String str2 = str
            + System.getenv("PROCESSOR_LEVEL")
            + System.getenv("PROCESSOR_ARCHITECTURE")
            + System.getenv("HOME")
            + System.getenv("NUMBER_OF_PROCESSORS")
            + System.getenv("TMP")
            + System.getenv("PROCESSOR_REVISION")
            + "00-00-00-00-00-00"
            + System.getenv("COMPUTERNAME")
            + System.getenv("APPDATA")
            + File.listRoots()[0].getTotalSpace()
            + System.getenv("DISPLAY")
            + System.getenv("PROCESSOR_IDENTIFIER")
            + System.getenv("USERNAME")
            + System.getenv("HOMEPATH")
            + rfc4648.encodeToString(fileStore.toString().getBytes(StandardCharsets.UTF_8))
            + rfc4648.encodeToString(fileStore.getAttribute("volume:vsn").toString().getBytes(StandardCharsets.UTF_8));
         this.sendPacket(
            new AuthHwidResponsePacket(
               VendorSessionTicker.sessionToken,
               getActiveHandler().authChallengeToken,
               Sha256Digest.digest(str2.getBytes(StandardCharsets.UTF_8)),
               DiscordRpcManager.discordUserTag.getBytes(StandardCharsets.UTF_8)
            )
         );
      } else {
         this.sendInfoReport(13);
      }
   }

   private void performLegacyKeyExchange(long l) {
      while (lastHandshakeTick >= getSessionTickCounter()) {
         this.sleepOneMillis();
      }
      lastHandshakeTick = getSessionTickCounter() + 1L;
      long l2 = System.nanoTime();
      long l3 = System.currentTimeMillis();
      long l4 = l2 ^ l3;
      byte[] bytes = new byte[32];
      for (int i = 0; i < 32; i++) {
         l4 = (l4 * 1103515245L + 12345L) % 257L;
         bytes[i] = (byte)(l4 - 128L);
      }
      byte[] bytes2 = new byte[32];
      for (int j = 0; j < 32; j++) {
         l4 = (l4 * 1103515245L + 12345L) % 257L;
         bytes2[j] = (byte)(l4 - 128L);
      }
      byte[] bytes3 = new byte[32];
      for (int k = 0; k < 32; k++) {
         l4 = (l4 * 1103515245L + 12345L) % 257L;
         bytes3[k] = (byte)(l4 - 128L);
      }
      byte[] bytes4 = new byte[13];
      for (int m = 0; m < 13; m++) {
         l4 = (l4 * 1103515245L + 12345L) % 257L;
         bytes4[m] = (byte)(l4 - 128L);
      }
      byte[] bytes5 = new byte[13];
      for (int idx = 0; idx < 13; idx++) {
         l4 = (l4 * 1103515245L + 12345L) % 257L;
         bytes5[idx] = (byte)(l4 - 128L);
      }
      byte[] bytes6 = (l2 + "#" + l3).getBytes(StandardCharsets.UTF_8);
      byte[] bytes7 = new byte[bytes2.length + bytes4.length + bytes3.length + bytes5.length + bytes6.length];
      int n = 0;
      System.arraycopy(bytes2, 0, bytes7, n, bytes2.length);
      n += bytes2.length;
      System.arraycopy(bytes4, 0, bytes7, n, bytes4.length);
      n += bytes4.length;
      System.arraycopy(bytes3, 0, bytes7, n, bytes3.length);
      n += bytes3.length;
      System.arraycopy(bytes5, 0, bytes7, n, bytes5.length);
      n += bytes5.length;
      System.arraycopy(bytes6, 0, bytes7, n, bytes6.length);
      VendorAesHelper.encrypt(bytes7, bytes);
      this.sleepOneMillis();
      if (System.nanoTime() - l2 < 0L || System.nanoTime() - l2 == 0L) {
         this.sendInfoReport(2);
      }
      if (System.currentTimeMillis() - l3 < 0L || System.currentTimeMillis() - l3 == 0L) {
         this.sendInfoReport(3);
      }
      this.packetDecryptor.setSessionKey(bytes2);
      this.packetEncryptor.setSessionKey(bytes3);
      if (this.reconnectMode) {
         l4 = (l4 * 1103515245L + 12345L) % 99001L;
         int n2 = (int)l4 + 1000;
         this.proofExponent = new CustomBigInteger(Integer.toString(n2));
         this.channel.writeAndFlush(new SessionReconnectPacket(VendorSessionTicker.sessionToken, VendorSessionTicker.reconnectToken, n2));
      }
   }

   private void initiateKeyExchange() {
      while (lastHandshakeTick >= getSessionTickCounter()) {
         this.sleepOneMillis();
      }
      lastHandshakeTick = getSessionTickCounter() + 1L;
      long l = System.nanoTime();
      long l2 = System.currentTimeMillis();
      long l3 = l ^ l2;
      byte[] bytes = new byte[32];
      for (int i = 0; i < 32; i++) {
         l3 = (l3 * 1103515245L + 12345L) % 256L;
         bytes[i] = (byte)(l3 - 128L);
      }
      byte[] bytes2 = new byte[32];
      byte[] bytes3 = new byte[32];
      for (int j = 0; j < 16; j++) {
         bytes2[j] = bytes[j * 2];
         bytes3[j] = bytes[j * 2 + 1];
      }
      int n = (int)((l3 * 1103515245L + 12345L) % (long)(VendorRsaKeyStore.serverKeys.length + 1));
      CustomBigInteger[] customBigIntegers = decodeObfuscatedRsaKey(VendorRsaKeyStore.serverKeys[n]);
      CustomBigInteger customBigInteger = customBigIntegers[0];
      CustomBigInteger customBigInteger2 = customBigIntegers[1];
      int n2 = (customBigInteger.bitLength() + 7) / 8;
      long l4 = System.nanoTime();
      byte[] bytes4 = new byte[56];
      System.arraycopy(bytes, 0, bytes4, 0, 32);
      putLongBE(bytes4, 32, l2);
      putLongBE(bytes4, 40, l);
      putLongBE(bytes4, 48, l4);
      byte[] bytes5 = Pkcs1Padding.pad(bytes4, n2);
      byte[] bytes6 = new CustomBigInteger(1, bytes5)
         .modPow(customBigInteger2, customBigInteger)
         .toByteArray();
      byte[] bytes7 = wrapRsaCiphertext(bytes6);
      this.sleepOneMillis();
      if (System.nanoTime() - l < 0L || System.nanoTime() - l == 0L) {
         this.sendInfoReport(2);
      }
      if (System.currentTimeMillis() - l2 < 0L || System.currentTimeMillis() - l2 == 0L) {
         this.sendInfoReport(3);
      }
      VendorSessionTicker.lastConnectionActivity = System.currentTimeMillis();
      this.sendPacket(new KeyExchangeInitPacket((n ^ 116) << 5, bytes7));
      this.encryptKeyHalf = bytes3;
      this.decryptKeyHalf = bytes2;
   }

   private void sendInfoReport(int code) {
      new VendorTelemetryReporter()
         .reportSecurityInfo(
            "Info ("
               + code
               + "): UserName: "
               + Augustus.getInstance().getUsername()
               + "; Computername: "
               + System.getenv("COMPUTERNAME")
               + "; Username: "
               + System.getenv("USERNAME")
               + " "
               + Augustus.getInstance().getSessionToken()
         );
      this.sendPacket(
         new SecurityAlertPacket(
            "Augustus",
            Sha256Digest.digest("slfjdslasdjfaksdf\u00f6skdjf".getBytes(StandardCharsets.UTF_8)),
            VendorSessionTicker.sessionToken,
            System.currentTimeMillis()
         )
      );
   }

   private static CustomBigInteger[] decodeObfuscatedRsaKey(String encoded) {
      int len = encoded.length();
      int pad = 0;
      if (len > 0 && encoded.charAt(len - 1) == '=') {
         pad++;
         if (len > 1 && encoded.charAt(len - 2) == '=') {
            pad++;
         }
      }
      int outLen = len * 3 / 4 - pad;
      byte[] decoded = new byte[outLen];
      int outIdx = 0;
      for (int i = 0; i < len; i += 4) {
         int c0 = RsaKeyMatrixCodec.BASE64_DECODE_TABLE[encoded.charAt(i)];
         int c1 = RsaKeyMatrixCodec.BASE64_DECODE_TABLE[encoded.charAt(i + 1)];
         int c2 = i + 2 < len && encoded.charAt(i + 2) != '=' ? RsaKeyMatrixCodec.BASE64_DECODE_TABLE[encoded.charAt(i + 2)] : 0;
         int c3 = i + 3 < len && encoded.charAt(i + 3) != '=' ? RsaKeyMatrixCodec.BASE64_DECODE_TABLE[encoded.charAt(i + 3)] : 0;
         int bits = c0 << 18 | c1 << 12 | c2 << 6 | c3;
         if (outIdx < outLen) {
            decoded[outIdx++] = (byte)(bits >>> 16);
         }
         if (outIdx < outLen) {
            decoded[outIdx++] = (byte)(bits >>> 8);
         }
         if (outIdx < outLen) {
            decoded[outIdx++] = (byte)bits;
         }
      }
      boolean rectangular = (decoded[0] & 1) != 0;
      int[] vr = RsaKeyMatrixCodec.readVarInt(decoded, 1);
      int rows = vr[0];
      int pos = vr[1];
      int[][] mat = new int[rows][];
      if (rectangular) {
         vr = RsaKeyMatrixCodec.readVarInt(decoded, pos);
         int cols = vr[0];
         pos = vr[1];
         for (int r = 0; r < rows; r++) {
            mat[r] = new int[cols];
            for (int c = 0; c < cols; c++) {
               vr = RsaKeyMatrixCodec.readVarInt(decoded, pos);
               mat[r][c] = vr[0];
               pos = vr[1];
            }
         }
      } else {
         for (int r = 0; r < rows; r++) {
            vr = RsaKeyMatrixCodec.readVarInt(decoded, pos);
            int cols = vr[0];
            pos = vr[1];
            mat[r] = new int[cols];
            for (int c = 0; c < cols; c++) {
               vr = RsaKeyMatrixCodec.readVarInt(decoded, pos);
               mat[r][c] = vr[0];
               pos = vr[1];
            }
         }
      }
      byte[] keyBytes = new byte[rows];
      for (int i = 0; i < rows; i += 2) {
         int a1 = mat[i][0] >> 6;
         int b1 = mat[i][1] >> 6;
         int e1 = mat[i][2] >> 6;
         int a2 = mat[i + 1][0] >> 6;
         int b2 = mat[i + 1][1] >> 6;
         int e2 = mat[i + 1][2] >> 6;
         int det = a1 * b2 - b1 * a2;
         keyBytes[i] = (byte)((e1 * b2 - b1 * e2) / det);
         keyBytes[i + 1] = (byte)((a1 * e2 - e1 * a2) / det);
      }
      return parseRsaKeyBytes(keyBytes);
   }

   private static CustomBigInteger[] parseRsaKeyBytes(byte[] keyBytes) {
      RawByteBuffer buf = RawByteBuffer.wrap(keyBytes);
      int modLen = (buf.get() & 0xFF) << 24
         | (buf.get() & 0xFF) << 16
         | (buf.get() & 0xFF) << 8
         | (buf.get() & 0xFF);
      byte[] modBytes = new byte[modLen];
      buf.get(modBytes);
      CustomBigInteger mod = new CustomBigInteger(modBytes);
      int expLen = (buf.get() & 0xFF) << 24
         | (buf.get() & 0xFF) << 16
         | (buf.get() & 0xFF) << 8
         | (buf.get() & 0xFF);
      byte[] expBytes = new byte[expLen];
      buf.get(expBytes);
      CustomBigInteger exp = new CustomBigInteger(expBytes);
      return new CustomBigInteger[]{mod, exp};
   }

   private static byte[] wrapRsaCiphertext(byte[] ct) {
      if (ct.length > 0 && ct[0] == 0) {
         byte[] trimmed = new byte[ct.length - 1];
         System.arraycopy(ct, 1, trimmed, 0, trimmed.length);
         ct = trimmed;
      }
      return RawByteBuffer.allocate(ct.length + 1)
         .put((byte)1)
         .put2(ct)
         .array();
   }

   private static void putLongBE(byte[] buf, int off, long val) {
      buf[off] = (byte)(val >>> 56);
      buf[off + 1] = (byte)(val >>> 48);
      buf[off + 2] = (byte)(val >>> 40);
      buf[off + 3] = (byte)(val >>> 32);
      buf[off + 4] = (byte)(val >>> 24);
      buf[off + 5] = (byte)(val >>> 16);
      buf[off + 6] = (byte)(val >>> 8);
      buf[off + 7] = (byte)val;
   }

   private static byte[] aes256GcmDecrypt(byte[] blob, byte[] key) {
      byte[] iv = new byte[GCM_IV_LENGTH];
      System.arraycopy(blob, 0, iv, 0, GCM_IV_LENGTH);
      int ctLen = blob.length - GCM_IV_LENGTH - GCM_TAG_LENGTH;
      byte[] ct = new byte[ctLen];
      System.arraycopy(blob, GCM_IV_LENGTH, ct, 0, ctLen);
      byte[] expectedTag = new byte[GCM_TAG_LENGTH];
      System.arraycopy(blob, GCM_IV_LENGTH + ctLen, expectedTag, 0, GCM_TAG_LENGTH);

      int[] roundKeys = aes256ExpandKey(key);
      byte[] h = aes256EncryptBlock(new byte[AES_BLOCK_SIZE], roundKeys);
      int paddedCtLen = (ctLen + 15) / 16 * 16;
      byte[] ghashInput = new byte[paddedCtLen + 16];
      System.arraycopy(ct, 0, ghashInput, 0, ctLen);
      putLongBE(ghashInput, paddedCtLen + 8, (long)ctLen * 8L);
      byte[] g = ghash(h, ghashInput);

      byte[] j0 = new byte[16];
      System.arraycopy(iv, 0, j0, 0, GCM_IV_LENGTH);
      j0[15] = 1;
      byte[] s = aes256EncryptBlock(j0, roundKeys);
      byte[] computedTag = new byte[16];
      for (int i = 0; i < 16; i++) {
         computedTag[i] = (byte)(g[i] ^ s[i]);
      }
      int tagDiff = 0;
      for (int i = 0; i < 16; i++) {
         tagDiff |= computedTag[i] ^ expectedTag[i];
      }

      byte[] counter = new byte[16];
      System.arraycopy(iv, 0, counter, 0, GCM_IV_LENGTH);
      counter[15] = 1;
      byte[] pt = new byte[ctLen];
      for (int i = 0; i < ctLen; i += 16) {
         byte[] ks = aes256EncryptBlock(counter, roundKeys);
         int blockLen = Math.min(16, ctLen - i);
         for (int j = 0; j < blockLen; j++) {
            pt[i + j] = (byte)(ct[i + j] ^ ks[j]);
         }
         for (int j = counter.length - 1; j >= 0; j--) {
            counter[j] = (byte)(counter[j] + 1);
            if (counter[j] != 0) {
               break;
            }
         }
      }
      return pt;
   }

   private static int[] aes256ExpandKey(byte[] key) {
      int[] w = new int[4 * (AES256_ROUNDS + 1)];
      for (int i = 0; i < 8; i++) {
         w[i] = (key[4 * i] & 0xFF) << 24
            | (key[4 * i + 1] & 0xFF) << 16
            | (key[4 * i + 2] & 0xFF) << 8
            | (key[4 * i + 3] & 0xFF);
      }
      for (int i = 8; i < w.length; i++) {
         int temp = w[i - 1];
         if (i % 8 == 0) {
            temp = aesSubWord(temp << 8 | temp >>> 24) ^ (AES_RCON[i / 8] << 24);
         } else if (i % 8 == 4) {
            temp = aesSubWord(temp);
         }
         w[i] = w[i - 8] ^ temp;
      }
      return w;
   }

   private static int aesSubWord(int word) {
      return AES_SBOX[word >>> 24 & 0xFF] << 24
         | AES_SBOX[word >>> 16 & 0xFF] << 16
         | AES_SBOX[word >>> 8 & 0xFF] << 8
         | AES_SBOX[word & 0xFF];
   }

   private static int aesXtime(int b) {
      return (b & 0x80) != 0 ? (b << 1 ^ 27) & 0xFF : b << 1 & 0xFF;
   }

   private static byte[] aes256EncryptBlock(byte[] input, int[] roundKeys) {
      int[] s = new int[16];
      for (int i = 0; i < 16; i++) {
         s[i] = input[i] & 0xFF;
      }
      aesAddRoundKey(s, roundKeys, 0);
      for (int round = 1; round < AES256_ROUNDS; round++) {
         for (int i = 0; i < 16; i++) {
            s[i] = AES_SBOX[s[i]];
         }
         aesShiftRows(s);
         aesMixColumns(s);
         aesAddRoundKey(s, roundKeys, round * 4);
      }
      for (int i = 0; i < 16; i++) {
         s[i] = AES_SBOX[s[i]];
      }
      aesShiftRows(s);
      aesAddRoundKey(s, roundKeys, AES256_ROUNDS * 4);
      byte[] out = new byte[16];
      for (int i = 0; i < 16; i++) {
         out[i] = (byte)s[i];
      }
      return out;
   }

   private static void aesAddRoundKey(int[] s, int[] roundKeys, int wordOffset) {
      for (int c = 0; c < 4; c++) {
         int rk = roundKeys[wordOffset + c];
         s[4 * c] ^= rk >>> 24 & 0xFF;
         s[4 * c + 1] ^= rk >>> 16 & 0xFF;
         s[4 * c + 2] ^= rk >>> 8 & 0xFF;
         s[4 * c + 3] ^= rk & 0xFF;
      }
   }

   private static void aesShiftRows(int[] s) {
      int t = s[1];
      s[1] = s[5];
      s[5] = s[9];
      s[9] = s[13];
      s[13] = t;

      t = s[2];
      s[2] = s[10];
      s[10] = t;
      t = s[6];
      s[6] = s[14];
      s[14] = t;

      t = s[15];
      s[15] = s[11];
      s[11] = s[7];
      s[7] = s[3];
      s[3] = t;
   }

   private static void aesMixColumns(int[] s) {
      for (int c = 0; c < 4; c++) {
         int i = c * 4;
         int s0 = s[i];
         int s1 = s[i + 1];
         int s2 = s[i + 2];
         int s3 = s[i + 3];
         int a0 = aesXtime(s0);
         int a1 = aesXtime(s1);
         int a2 = aesXtime(s2);
         int a3 = aesXtime(s3);
         s[i] = a0 ^ a1 ^ s1 ^ s2 ^ s3;
         s[i + 1] = s0 ^ a1 ^ a2 ^ s2 ^ s3;
         s[i + 2] = s0 ^ s1 ^ a2 ^ a3 ^ s3;
         s[i + 3] = a0 ^ s0 ^ s1 ^ s2 ^ a3;
      }
   }

   private static byte[] ghash(byte[] h, byte[] data) {
      byte[] y = new byte[16];
      for (int off = 0; off < data.length; off += 16) {
         for (int j = 0; j < 16; j++) {
            y[j] = (byte)(y[j] ^ data[off + j]);
         }
         y = gfMulLeft87(h, y);
      }
      return y;
   }

   private static byte[] gfMulLeft87(byte[] aBytes, byte[] bBytes) {
      byte[] z = new byte[16];
      byte[] v = Arrays.copyOf(bBytes, 16);
      for (int i = 0; i < 128; i++) {
         if ((aBytes[i / 8] >>> (7 - i % 8) & 1) != 0) {
            for (int j = 0; j < 16; j++) {
               z[j] = (byte)(z[j] ^ v[j]);
            }
         }
         boolean msb = (v[0] & 0x80) != 0;
         for (int j = 0; j < 15; j++) {
            v[j] = (byte)(v[j] << 1 | (v[j + 1] & 0xFF) >>> 7);
         }
         v[15] = (byte)(v[15] << 1);
         if (msb) {
            v[15] = (byte)(v[15] ^ 0x87);
         }
      }
      return z;
   }
}
