package net.augustus.vendor.launcher.net;

import net.augustus.vendor.launcher.AesCrypto;
import net.augustus.vendor.launcher.Sha3Hash;
import net.augustus.vendor.launcher.crypto.AesCipherUtil;
import net.augustus.vendor.launcher.crypto.keccak.Sha3Type;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.security.InvalidKeyException;
import java.security.ProviderException;
import java.util.Arrays;
import java.util.List;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

public class PacketCipherEncoder extends MessageToMessageEncoder {
   private final int[] T2;
   private byte[] decoyKeySlot000;
   private byte[] decoyKeySlot001;
   private byte[] decoyKeySlot002;
   private byte[] decoyKeySlot003;
   private byte[] decoyKeySlot004;
   private byte[] decoyKeySlot005;
   private byte[] decoyKeySlot006;
   private byte[] decoyKeySlot007;
   private byte[] decoyKeySlot008;
   private final AesCrypto keyDeriverCipher;
   private byte[] decoyKeySlot009;
   private byte[] decoyKeySlot010;
   private byte[] decoyKeySlot011;
   private int[] roundKeys;
   private final int[] T5;
   private byte[] decoyKeySlot012;
   private byte[] decoyKeySlot013;
   private byte[] decoyKeySlot014;
   private byte[] decoyKeySlot015;
   private byte[] decoyKeySlot016;
   private byte[] decoyKeySlot017;
   private byte[] decoyKeySlot018;
   private byte[] decoyKeySlot019;
   private byte[] decoyKeySlot020;
   private byte[] decoyKeySlot021;
   private byte[] decoyKeySlot022;
   private byte[] decoyKeySlot023;
   private byte[] decoyKeySlot024;
   private byte[] decoyKeySlot025;
   private byte[] decoyKeySlot026;
   private final int[] T7;
   private byte[] decoyKeySlot027;
   private byte[] decoyKeySlot028;
   private byte[] decoyKeySlot029;
   private boolean added;
   private byte[] decoyKeySlot030;
   private byte[] decoyKeySlot031;
   private byte[] decoyKeySlot032;
   private byte[] decoyKeySlot033;
   private byte[] decoyKeySlot034;
   private byte[] decoyKeySlot035;
   private byte[] decoyKeySlot036;
   private byte[] decoyKeySlot037;
   private byte[] decoyKeySlot038;
   private byte[] decoyKeySlot039;
   private byte[] decoyKeySlot040;
   private byte[] decoyKeySlot041;
   private byte[] decoyKeySlot042;
   private byte[] decoyKeySlot043;
   private byte[] decoyKeySlot044;
   private byte[] decoyKeySlot045;
   private byte[] decoyKeySlot046;
   private byte[] decoyKeySlot047;
   private byte[] decoyKeySlot048;
   private byte[] activeEncryptKey;
   private final int[] U4;
   private byte[] decoyKeySlot049;
   private byte[] decoyKeySlot050;
   private byte[] decoyKeySlot051;
   private byte[] decoyKeySlot052;
   private byte[] decoyKeySlot053;
   private byte[] decoyKeySlot054;
   private byte[] decoyKeySlot055;
   private byte[] decoyKeySlot056;
   private byte[] decoyKeySlot057;
   private byte[] decoyKeySlot058;
   private byte[] decoyKeySlot059;
   private byte[] decoyKeySlot060;
   private byte[] decoyKeySlot061;
   private byte[] decoyKeySlot062;
   private byte[] decoyKeySlot063;
   private byte[] decoyKeySlot064;
   private byte[] decoyKeySlot065;
   private byte[] decoyKeySlot066;
   private byte[] decoyKeySlot067;
   private byte[] decoyKeySlot068;
   private byte[] decoyKeySlot069;
   private byte[] decoyKeySlot070;
   private byte[] decoyKeySlot071;
   private final PipelineStageGuard stageGuard;
   private final int[] T1;
   private final int[] T8;
   private byte[] decoyKeySlot072;
   private byte[] decoyKeySlot073;
   private byte[] decoyKeySlot074;
   private byte[] decoyKeySlot075;
   private byte[] decoyKeySlot076;
   private byte[] decoyKeySlot077;
   private byte[] decoyKeySlot078;
   private byte[] decoyKeySlot079;
   private byte[] decoyKeySlot080;
   private byte[] decoyKeySlot081;
   private byte[] decoyKeySlot082;
   private byte[] decoyKeySlot083;
   private byte[] decoyKeySlot084;
   private int[] alog;
   private boolean has14Rounds;
   private byte[] decoyKeySlot085;
   private byte[] decoyKeySlot086;
   private byte[] decoyKeySlot087;
   private byte[] decoyKeySlot088;
   private byte[] decoyKeySlot089;
   private byte[] decoyKeySlot090;
   private byte[] decoyKeySlot091;
   private byte[] decoyKeySlot092;
   private byte[] decoyKeySlot093;
   private byte[] decoyKeySlot094;
   private byte[] decoyKeySlot095;
   private byte[] decoyKeySlot096;
   private byte[] decoyKeySlot097;
   private byte[] decoyKeySlot098;
   private byte[] decoyKeySlot099;
   private byte[] keyGuardSlotB;
   private byte[] decoyKeySlot100;
   private byte[] decoyKeySlot101;
   private byte[] decoyKeySlot102;
   private byte[] decoyKeySlot103;
   private final int[] T6;
   private byte[] decoyKeySlot104;
   private byte[] decoyKeySlot105;
   private byte[] decoyKeySlot106;
   private byte[] decoyKeySlot107;
   private byte[] decoyKeySlot108;
   private byte[] decoyKeySlot109;
   private byte[] decoyKeySlot110;
   private byte[] decoyKeySlot111;
   private byte[] decoyKeySlot112;
   private byte[] decoyKeySlot113;
   private byte[] decoyKeySlot114;
   private byte[] decoyKeySlot115;
   private byte[] decoyKeySlot116;
   private byte[] decoyKeySlot117;
   private byte[] decoyKeySlot118;
   private byte[] decoyKeySlot119;
   private byte[] decoyKeySlot120;
   private int[][] sessionK;
   private byte[] keyGuardSlotA;
   private byte[] decoyKeySlot121;
   private byte[] decoyKeySlot122;
   private byte[] decoyKeySlot123;
   private byte[] decoyKeySlot124;
   private final int[] U1;
   private byte[] decoyKeySlot125;
   private byte[] decoyKeySlot126;
   private byte[] decoyKeySlot127;
   private byte[] decoyKeySlot128;
   private byte[] decoyKeySlot129;
   private byte[] decoyKeySlot130;
   private byte[] decoyKeySlot131;
   private byte[] decoyKeySlot132;
   private byte[] decoyKeySlot133;
   private final byte[] invSBox;
   private final int[] T3;
   private byte[] decoyKeySlot134;
   private byte[] decoyKeySlot135;
   private byte[] decoyKeySlot136;
   private byte[] decoyKeySlot137;
   private byte[] decoyKeySlot138;
   private byte[] decoyKeySlot139;
   private final byte[] rcon;
   private byte[] decoyKeySlot140;
   private byte[] decoyKeySlot141;
   private byte[] decoyKeySlot142;
   private byte[] decoyKeySlot143;
   private byte[] decoyKeySlot144;
   private byte[] decoyKeySlot145;
   private byte[] decoyKeySlot146;
   private byte[] decoyKeySlot147;
   private byte[] decoyKeySlot148;
   private byte[] decoyKeySlot149;
   private byte[] decoyKeySlot150;
   private byte[] decoyKeySlot151;
   private final int[] U3;
   private byte[] decoyKeySlot152;
   private byte[] decoyKeySlot153;
   private byte[] decoyKeySlot154;
   private byte[] decoyKeySlot155;
   private byte[] decoyKeySlot156;
   private byte[] decoyKeySlot157;
   private final int[] U2;
   private byte[] decoyKeySlot158;
   private int[] log;
   private byte[] decoyKeySlot159;
   private byte[] decoyKeySlot160;
   private byte[] decoyKeySlot161;
   private byte[] decoyKeySlot162;
   private byte[] decoyKeySlot163;
   private byte[] decoyKeySlot164;
   private byte[] decoyKeySlot165;
   private byte[] decoyKeySlot166;
   private byte[] decoyKeySlot167;
   private byte[] decoyKeySlot168;
   private byte[] decoyKeySlot169;
   private byte[] decoyKeySlot170;
   private byte[] decoyKeySlot171;
   private byte[] decoyKeySlot172;
   private byte[] decoyKeySlot173;
   private byte[] decoyKeySlot174;
   private byte[] decoyKeySlot175;
   private byte[] decoyKeySlot176;
   private int roundLimit;
   private byte[] decoyKeySlot177;
   private byte[] decoyKeySlot178;
   private byte[] decoyKeySlot179;
   private byte[] decoyKeySlot180;
   private byte[] decoyKeySlot181;
   private byte[] decoyKeySlot182;
   private byte[] decoyKeySlot183;
   private byte[] decoyKeySlot184;
   private byte[] decoyKeySlot185;
   public byte[] cbcFeedbackIv;
   private byte[] decoyKeySlot186;
   private final byte[] sBox;
   private byte[] decoyKeySlot187;
   private byte[] decoyKeySlot188;
   private byte[] decoyKeySlot189;
   private byte[] decoyKeySlot190;
   private byte[] decoyKeySlot191;
   private byte[] decoyKeySlot192;
   private byte[] decoyKeySlot193;
   private byte[] decoyKeySlot194;
   private byte[] cbcScratchBlock;
   private final int[] T4;
   private byte[] decoyKeySlot195;
   private byte[] decoyKeySlot196;
   private byte[] decoyKeySlot197;
   private static int[] controlFlowMarker;

   @Override
   public void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
      this.encodePacket(ctx, (ByteBuf)msg, out);
   }

   public void initCipher(boolean flag, byte[] bytes) {
      int[] controlFlowMarker2 = getControlFlowMarker();

      try {
         this.makeKey(bytes);
         int[] ints = this.sessionK[flag ? 1 : 0];
         this.roundKeys = ints;
      } catch (InvalidKeyException e) {
      }
   }

   private int[] expandToSubKey(int[][] intsArr, boolean flag) {
      Object obj = null;
      getControlFlowMarker();
      int n = 0;
      boolean flag2 = false;
      n = intsArr.length;
      obj = new int[n * 4];
      if (!flag) {
         for (int i = 0; i < n; i++) {
            System.arraycopy(intsArr[i], 0, (int[])obj, i * 4, 4);
         }
      } else {
         System.arraycopy(intsArr[n - 1], 0, (int[])obj, 0, 4);

         for (int j = 1; j < n; j++) {
            System.arraycopy(intsArr[j - 1], 0, (int[])obj, j * 4, 4);
         }
      }

      return (int[])obj;
   }

   public PacketCipherEncoder(PipelineStageGuard pipelineStageGuard) {
       super();
      byte[] bytes = new byte[1];
      this.keyGuardSlotA = bytes;
      byte[] bytes2 = new byte[1];
      this.decoyKeySlot151 = bytes2;
      byte[] bytes3 = new byte[1];
      this.decoyKeySlot111 = bytes3;
      byte[] bytes4 = new byte[1];
      this.decoyKeySlot088 = bytes4;
      byte[] bytes5 = new byte[1];
      this.decoyKeySlot028 = bytes5;
      byte[] bytes6 = new byte[1];
      this.decoyKeySlot087 = bytes6;
      byte[] bytes7 = new byte[1];
      this.decoyKeySlot167 = bytes7;
      byte[] bytes8 = new byte[1];
      this.decoyKeySlot071 = bytes8;
      byte[] bytes9 = new byte[1];
      this.decoyKeySlot033 = bytes9;
      byte[] bytes10 = new byte[1];
      this.decoyKeySlot148 = bytes10;
      byte[] bytes11 = new byte[1];
      this.decoyKeySlot045 = bytes11;
      byte[] bytes12 = new byte[1];
      this.decoyKeySlot150 = bytes12;
      byte[] bytes13 = new byte[1];
      this.decoyKeySlot060 = bytes13;
      byte[] bytes14 = new byte[1];
      this.decoyKeySlot038 = bytes14;
      byte[] bytes15 = new byte[1];
      this.decoyKeySlot190 = bytes15;
      byte[] bytes16 = new byte[1];
      this.decoyKeySlot196 = bytes16;
      byte[] bytes17 = new byte[1];
      this.decoyKeySlot166 = bytes17;
      byte[] bytes18 = new byte[1];
      this.decoyKeySlot057 = bytes18;
      byte[] bytes19 = new byte[1];
      this.decoyKeySlot085 = bytes19;
      byte[] bytes20 = new byte[1];
      this.decoyKeySlot075 = bytes20;
      byte[] bytes21 = new byte[1];
      this.decoyKeySlot049 = bytes21;
      byte[] bytes22 = new byte[1];
      this.decoyKeySlot135 = bytes22;
      byte[] bytes23 = new byte[1];
      this.decoyKeySlot183 = bytes23;
      byte[] bytes24 = new byte[1];
      this.decoyKeySlot036 = bytes24;
      byte[] bytes25 = new byte[1];
      this.decoyKeySlot101 = bytes25;
      byte[] bytes26 = new byte[1];
      this.decoyKeySlot187 = bytes26;
      byte[] bytes27 = new byte[1];
      this.decoyKeySlot058 = bytes27;
      byte[] bytes28 = new byte[1];
      this.decoyKeySlot021 = bytes28;
      byte[] bytes29 = new byte[1];
      this.decoyKeySlot039 = bytes29;
      byte[] bytes30 = new byte[1];
      this.decoyKeySlot017 = bytes30;
      byte[] bytes31 = new byte[1];
      this.decoyKeySlot064 = bytes31;
      byte[] bytes32 = new byte[1];
      this.decoyKeySlot120 = bytes32;
      byte[] bytes33 = new byte[1];
      this.decoyKeySlot186 = bytes33;
      byte[] bytes34 = new byte[1];
      this.decoyKeySlot188 = bytes34;
      byte[] bytes35 = new byte[1];
      this.decoyKeySlot141 = bytes35;
      byte[] bytes36 = new byte[1];
      this.decoyKeySlot182 = bytes36;
      byte[] bytes37 = new byte[1];
      this.decoyKeySlot104 = bytes37;
      byte[] bytes38 = new byte[1];
      this.decoyKeySlot177 = bytes38;
      byte[] bytes39 = new byte[1];
      this.decoyKeySlot119 = bytes39;
      byte[] bytes40 = new byte[1];
      this.decoyKeySlot032 = bytes40;
      byte[] bytes41 = new byte[1];
      this.decoyKeySlot179 = bytes41;
      byte[] bytes42 = new byte[1];
      this.decoyKeySlot051 = bytes42;
      byte[] bytes43 = new byte[1];
      this.decoyKeySlot043 = bytes43;
      byte[] bytes44 = new byte[1];
      this.decoyKeySlot063 = bytes44;
      byte[] bytes45 = new byte[1];
      this.decoyKeySlot155 = bytes45;
      byte[] bytes46 = new byte[1];
      this.decoyKeySlot098 = bytes46;
      byte[] bytes47 = new byte[1];
      this.decoyKeySlot139 = bytes47;
      byte[] bytes48 = new byte[1];
      this.decoyKeySlot160 = bytes48;
      byte[] bytes49 = new byte[1];
      this.decoyKeySlot197 = bytes49;
      byte[] bytes50 = new byte[1];
      this.decoyKeySlot124 = bytes50;
      byte[] bytes51 = new byte[1];
      this.decoyKeySlot092 = bytes51;
      byte[] bytes52 = new byte[1];
      this.decoyKeySlot044 = bytes52;
      byte[] bytes53 = new byte[1];
      this.decoyKeySlot069 = bytes53;
      byte[] bytes54 = new byte[1];
      this.decoyKeySlot022 = bytes54;
      byte[] bytes55 = new byte[1];
      this.decoyKeySlot161 = bytes55;
      byte[] bytes56 = new byte[1];
      this.decoyKeySlot068 = bytes56;
      byte[] bytes57 = new byte[1];
      this.decoyKeySlot130 = bytes57;
      byte[] bytes58 = new byte[1];
      this.decoyKeySlot181 = bytes58;
      byte[] bytes59 = new byte[1];
      this.decoyKeySlot053 = bytes59;
      byte[] bytes60 = new byte[1];
      this.decoyKeySlot170 = bytes60;
      byte[] bytes61 = new byte[1];
      this.decoyKeySlot010 = bytes61;
      byte[] bytes62 = new byte[1];
      this.decoyKeySlot173 = bytes62;
      byte[] bytes63 = new byte[1];
      this.decoyKeySlot065 = bytes63;
      byte[] bytes64 = new byte[1];
      this.decoyKeySlot070 = bytes64;
      byte[] bytes65 = new byte[1];
      this.decoyKeySlot164 = bytes65;
      byte[] bytes66 = new byte[1];
      this.decoyKeySlot083 = bytes66;
      byte[] bytes67 = new byte[1];
      this.decoyKeySlot050 = bytes67;
      byte[] bytes68 = new byte[1];
      this.decoyKeySlot100 = bytes68;
      byte[] bytes69 = new byte[1];
      this.decoyKeySlot142 = bytes69;
      byte[] bytes70 = new byte[1];
      this.decoyKeySlot020 = bytes70;
      byte[] bytes71 = new byte[1];
      this.decoyKeySlot073 = bytes71;
      byte[] bytes72 = new byte[1];
      this.decoyKeySlot090 = bytes72;
      byte[] bytes73 = new byte[1];
      this.decoyKeySlot054 = bytes73;
      byte[] bytes74 = new byte[1];
      this.decoyKeySlot162 = bytes74;
      byte[] bytes75 = new byte[1];
      this.decoyKeySlot121 = bytes75;
      byte[] bytes76 = new byte[1];
      this.decoyKeySlot128 = bytes76;
      byte[] bytes77 = new byte[1];
      this.decoyKeySlot056 = bytes77;
      byte[] bytes78 = new byte[1];
      this.decoyKeySlot023 = bytes78;
      byte[] bytes79 = new byte[1];
      this.decoyKeySlot154 = bytes79;
      byte[] bytes80 = new byte[1];
      this.decoyKeySlot062 = bytes80;
      byte[] bytes81 = new byte[1];
      this.decoyKeySlot168 = bytes81;
      byte[] bytes82 = new byte[1];
      this.decoyKeySlot118 = bytes82;
      byte[] bytes83 = new byte[1];
      this.decoyKeySlot102 = bytes83;
      byte[] bytes84 = new byte[1];
      this.decoyKeySlot194 = bytes84;
      byte[] bytes85 = new byte[1];
      this.decoyKeySlot115 = bytes85;
      byte[] bytes86 = new byte[1];
      this.decoyKeySlot105 = bytes86;
      byte[] bytes87 = new byte[1];
      this.decoyKeySlot094 = bytes87;
      byte[] bytes88 = new byte[1];
      this.decoyKeySlot000 = bytes88;
      byte[] bytes89 = new byte[1];
      this.decoyKeySlot047 = bytes89;
      byte[] bytes90 = new byte[1];
      this.decoyKeySlot095 = bytes90;
      byte[] bytes91 = new byte[1];
      this.decoyKeySlot126 = bytes91;
      byte[] bytes92 = new byte[1];
      this.decoyKeySlot012 = bytes92;
      byte[] bytes93 = new byte[1];
      this.decoyKeySlot007 = bytes93;
      byte[] bytes94 = new byte[1];
      this.decoyKeySlot091 = bytes94;
      byte[] bytes95 = new byte[1];
      this.decoyKeySlot002 = bytes95;
      byte[] bytes96 = new byte[1];
      this.decoyKeySlot109 = bytes96;
      byte[] bytes97 = new byte[1];
      this.decoyKeySlot113 = bytes97;
      byte[] bytes98 = new byte[1];
      this.decoyKeySlot059 = bytes98;
      byte[] bytes99 = new byte[1];
      this.decoyKeySlot195 = bytes99;
      byte[] bytes100 = new byte[1];
      this.decoyKeySlot084 = bytes100;
      byte[] bytes101 = new byte[1];
      this.decoyKeySlot122 = bytes101;
      byte[] bytes102 = new byte[1];
      this.decoyKeySlot008 = bytes102;
      byte[] bytes103 = new byte[1];
      this.decoyKeySlot189 = bytes103;
      byte[] bytes104 = new byte[1];
      this.decoyKeySlot030 = bytes104;
      byte[] bytes105 = new byte[1];
      this.decoyKeySlot146 = bytes105;
      getControlFlowMarker();
      byte[] bytes106 = new byte[1];
      this.decoyKeySlot152 = bytes106;
      byte[] bytes107 = new byte[1];
      this.decoyKeySlot016 = bytes107;
      byte[] bytes108 = new byte[1];
      this.decoyKeySlot081 = bytes108;
      byte[] bytes109 = new byte[1];
      this.decoyKeySlot144 = bytes109;
      byte[] bytes110 = new byte[1];
      this.decoyKeySlot024 = bytes110;
      byte[] bytes111 = new byte[1];
      this.decoyKeySlot077 = bytes111;
      byte[] bytes112 = new byte[1];
      this.decoyKeySlot140 = bytes112;
      byte[] bytes113 = new byte[1];
      this.decoyKeySlot014 = bytes113;
      byte[] bytes114 = new byte[1];
      this.decoyKeySlot129 = bytes114;
      byte[] bytes115 = new byte[1];
      this.decoyKeySlot134 = bytes115;
      byte[] bytes116 = new byte[1];
      this.decoyKeySlot048 = bytes116;
      byte[] bytes117 = new byte[1];
      this.decoyKeySlot147 = bytes117;
      byte[] bytes118 = new byte[1];
      this.decoyKeySlot018 = bytes118;
      byte[] bytes119 = new byte[1];
      this.decoyKeySlot133 = bytes119;
      byte[] bytes120 = new byte[1];
      this.decoyKeySlot046 = bytes120;
      byte[] bytes121 = new byte[1];
      this.decoyKeySlot193 = bytes121;
      byte[] bytes122 = new byte[1];
      this.decoyKeySlot052 = bytes122;
      byte[] bytes123 = new byte[1];
      this.decoyKeySlot178 = bytes123;
      byte[] bytes124 = new byte[1];
      this.decoyKeySlot110 = bytes124;
      byte[] bytes125 = new byte[1];
      this.decoyKeySlot176 = bytes125;
      byte[] bytes126 = new byte[1];
      this.decoyKeySlot013 = bytes126;
      byte[] bytes127 = new byte[1];
      this.decoyKeySlot159 = bytes127;
      byte[] bytes128 = new byte[1];
      this.decoyKeySlot005 = bytes128;
      byte[] bytes129 = new byte[1];
      this.decoyKeySlot174 = bytes129;
      byte[] bytes130 = new byte[1];
      this.decoyKeySlot089 = bytes130;
      byte[] bytes131 = new byte[1];
      this.decoyKeySlot143 = bytes131;
      byte[] bytes132 = new byte[1];
      this.decoyKeySlot099 = bytes132;
      byte[] bytes133 = new byte[1];
      this.decoyKeySlot042 = bytes133;
      byte[] bytes134 = new byte[1];
      this.decoyKeySlot137 = bytes134;
      byte[] bytes135 = new byte[1];
      this.decoyKeySlot127 = bytes135;
      byte[] bytes136 = new byte[1];
      this.decoyKeySlot136 = bytes136;
      byte[] bytes137 = new byte[1];
      this.decoyKeySlot086 = bytes137;
      byte[] bytes138 = new byte[1];
      this.decoyKeySlot117 = bytes138;
      byte[] bytes139 = new byte[1];
      this.decoyKeySlot009 = bytes139;
      byte[] bytes140 = new byte[1];
      this.decoyKeySlot079 = bytes140;
      byte[] bytes141 = new byte[1];
      this.decoyKeySlot055 = bytes141;
      byte[] bytes142 = new byte[1];
      this.decoyKeySlot037 = bytes142;
      byte[] bytes143 = new byte[1];
      this.decoyKeySlot078 = bytes143;
      byte[] bytes144 = new byte[1];
      this.decoyKeySlot175 = bytes144;
      byte[] bytes145 = new byte[1];
      this.decoyKeySlot093 = bytes145;
      byte[] bytes146 = new byte[1];
      this.decoyKeySlot025 = bytes146;
      byte[] bytes147 = new byte[1];
      this.decoyKeySlot112 = bytes147;
      byte[] bytes148 = new byte[1];
      this.decoyKeySlot011 = bytes148;
      byte[] bytes149 = new byte[1];
      this.decoyKeySlot074 = bytes149;
      byte[] bytes150 = new byte[1];
      this.decoyKeySlot163 = bytes150;
      byte[] bytes151 = new byte[1];
      this.decoyKeySlot169 = bytes151;
      byte[] bytes152 = new byte[1];
      this.decoyKeySlot103 = bytes152;
      byte[] bytes153 = new byte[1];
      this.decoyKeySlot031 = bytes153;
      byte[] bytes154 = new byte[1];
      this.decoyKeySlot072 = bytes154;
      byte[] bytes155 = new byte[1];
      this.decoyKeySlot132 = bytes155;
      byte[] bytes156 = new byte[1];
      this.decoyKeySlot001 = bytes156;
      byte[] bytes157 = new byte[1];
      this.decoyKeySlot015 = bytes157;
      byte[] bytes158 = new byte[1];
      this.decoyKeySlot158 = bytes158;
      byte[] bytes159 = new byte[1];
      this.decoyKeySlot076 = bytes159;
      byte[] bytes160 = new byte[1];
      this.decoyKeySlot003 = bytes160;
      byte[] bytes161 = new byte[1];
      this.decoyKeySlot026 = bytes161;
      byte[] bytes162 = new byte[1];
      this.decoyKeySlot171 = bytes162;
      byte[] bytes163 = new byte[1];
      this.decoyKeySlot006 = bytes163;
      byte[] bytes164 = new byte[1];
      this.decoyKeySlot035 = bytes164;
      byte[] bytes165 = new byte[1];
      this.decoyKeySlot185 = bytes165;
      byte[] bytes166 = new byte[1];
      this.decoyKeySlot080 = bytes166;
      byte[] bytes167 = new byte[1];
      this.decoyKeySlot097 = bytes167;
      byte[] bytes168 = new byte[1];
      this.decoyKeySlot180 = bytes168;
      byte[] bytes169 = new byte[1];
      this.decoyKeySlot116 = bytes169;
      byte[] bytes170 = new byte[1];
      this.decoyKeySlot149 = bytes170;
      byte[] bytes171 = new byte[1];
      this.decoyKeySlot114 = bytes171;
      byte[] bytes172 = new byte[1];
      this.decoyKeySlot040 = bytes172;
      byte[] bytes173 = new byte[1];
      this.decoyKeySlot019 = bytes173;
      byte[] bytes174 = new byte[1];
      this.decoyKeySlot034 = bytes174;
      byte[] bytes175 = new byte[1];
      this.decoyKeySlot041 = bytes175;
      byte[] bytes176 = new byte[1];
      this.decoyKeySlot191 = bytes176;
      byte[] bytes177 = new byte[1];
      this.decoyKeySlot184 = bytes177;
      byte[] bytes178 = new byte[1];
      this.decoyKeySlot066 = bytes178;
      byte[] bytes179 = new byte[1];
      this.decoyKeySlot156 = bytes179;
      byte[] bytes180 = new byte[1];
      this.decoyKeySlot125 = bytes180;
      byte[] bytes181 = new byte[1];
      this.decoyKeySlot027 = bytes181;
      byte[] bytes182 = new byte[1];
      this.decoyKeySlot107 = bytes182;
      byte[] bytes183 = new byte[1];
      this.decoyKeySlot138 = bytes183;
      byte[] bytes184 = new byte[1];
      this.decoyKeySlot165 = bytes184;
      byte[] bytes185 = new byte[1];
      this.decoyKeySlot029 = bytes185;
      byte[] bytes186 = new byte[1];
      this.decoyKeySlot061 = bytes186;
      byte[] bytes187 = new byte[1];
      this.decoyKeySlot192 = bytes187;
      byte[] bytes188 = new byte[1];
      this.decoyKeySlot004 = bytes188;
      byte[] bytes189 = new byte[1];
      this.decoyKeySlot067 = bytes189;
      byte[] bytes190 = new byte[1];
      this.decoyKeySlot145 = bytes190;
      byte[] bytes191 = new byte[1];
      this.decoyKeySlot108 = bytes191;
      byte[] bytes192 = new byte[1];
      this.decoyKeySlot157 = bytes192;
      byte[] bytes193 = new byte[1];
      this.decoyKeySlot096 = bytes193;
      byte[] bytes194 = new byte[1];
      this.keyGuardSlotB = bytes194;
      byte[] bytes195 = new byte[1];
      this.decoyKeySlot123 = bytes195;
      byte[] bytes196 = new byte[1];
      this.decoyKeySlot106 = bytes196;
      byte[] bytes197 = new byte[1];
      this.decoyKeySlot131 = bytes197;
      byte[] bytes198 = new byte[1];
      this.decoyKeySlot172 = bytes198;
      byte[] bytes199 = new byte[1];
      this.decoyKeySlot082 = bytes199;
      byte[] bytes200 = new byte[1];
      this.decoyKeySlot153 = bytes200;
      byte[] bytes201 = new byte[1];
      this.activeEncryptKey = bytes201;
      this.keyDeriverCipher = new AesCrypto();
      boolean flag = false;
      this.added = flag;
      boolean flag2 = false;
      this.has14Rounds = flag2;
      int[][] intsArr = (int[][])null;
      this.sessionK = intsArr;
      Object obj = null;
      this.roundKeys = (int[])obj;
      byte b = 0;
      this.roundLimit = b;
      int[] ints = new int[256];
      this.alog = ints;
      int[] ints2 = new int[256];
      this.log = ints2;
      this.sBox = new byte[256];
      this.invSBox = new byte[256];
      this.T1 = new int[256];
      this.T2 = new int[256];
      this.T3 = new int[256];
      this.T4 = new int[256];
      this.T5 = new int[256];
      this.T6 = new int[256];
      this.T7 = new int[256];
      this.T8 = new int[256];
      this.U1 = new int[256];
      this.U2 = new int[256];
      this.U3 = new int[256];
      this.U4 = new int[256];
      this.rcon = new byte[30];
      this.stageGuard = pipelineStageGuard;
      byte[] bytes202 = new byte[this.getBlockSize()];
      this.cbcScratchBlock = bytes202;
      byte[] bytes203 = new byte[this.getBlockSize()];
      this.cbcFeedbackIv = bytes203;
      short s = 283;
      int n = 0;
      this.alog[0] = 1;

      for (int i = 1; i < 256; i++) {
         n = this.alog[i - 1] << 1 ^ this.alog[i - 1];
         if ((n & 256) != 0) {
            n ^= s;
         }

         this.alog[i] = n;
      }

      int n2 = 1;

      while (n2 < 255) {
         this.log[this.alog[n2]] = n2++;
      }

      byte[][] bytesArr = new byte[][]{
         {1, 1, 1, 1, 1, 0, 0, 0},
         {0, 1, 1, 1, 1, 1, 0, 0},
         {0, 0, 1, 1, 1, 1, 1, 0},
         {0, 0, 0, 1, 1, 1, 1, 1},
         {1, 0, 0, 0, 1, 1, 1, 1},
         {1, 1, 0, 0, 0, 1, 1, 1},
         {1, 1, 1, 0, 0, 0, 1, 1},
         {1, 1, 1, 1, 0, 0, 0, 1}
      };
      byte[] bytes204 = new byte[]{0, 1, 1, 0, 0, 0, 1, 1};
      byte[][] bytesArr2 = new byte[256][8];
      bytesArr2[1][7] = 1;

      for (int j = 2; j < 256; j++) {
         n = this.alog[255 - this.log[j]];

         for (int k = 0; k < 8; k++) {
            bytesArr2[j][k] = (byte)(n >>> 7 - k & 1);
         }
      }

      byte[][] bytesArr3 = new byte[256][8];

      for (int m = 0; m < 256; m++) {
         for (int idx = 0; idx < 8; idx++) {
            bytesArr3[m][idx] = bytes204[idx];

            for (int index = 0; index < 8; index++) {
               bytesArr3[m][idx] = (byte)(bytesArr3[m][idx] ^ bytesArr[idx][index] * bytesArr2[m][index]);
            }
         }
      }

      for (int i2 = 0; i2 < 256; i2++) {
         this.sBox[i2] = (byte)(bytesArr3[i2][0] << 7);

         for (int i3 = 1; i3 < 8; i3++) {
            this.sBox[i2] = (byte)(this.sBox[i2] ^ bytesArr3[i2][i3] << 7 - i3);
         }

         this.invSBox[this.sBox[i2] & 255] = (byte)i2;
      }

      byte[][] bytesArr4 = new byte[][]{{2, 1, 1, 3}, {3, 2, 1, 1}, {1, 3, 2, 1}, {1, 1, 3, 2}};
      byte[][] bytesArr5 = new byte[4][8];

      for (int i4 = 0; i4 < 4; i4++) {
         for (int i5 = 0; i5 < 4; i5++) {
            bytesArr5[i4][i5] = bytesArr4[i4][i5];
         }

         bytesArr5[i4][i4 + 4] = 1;
      }

      byte[][] bytesArr6 = new byte[4][4];

      for (int i6 = 0; i6 < 4; i6++) {
         byte b2 = bytesArr5[i6][i6];
         if (b2 == 0) {
            int n3 = i6 + 1;

            while (bytesArr5[n3][i6] == 0) {
               n3++;
            }

            for (int i7 = 0; i7 < 8; i7++) {
               byte b3 = bytesArr5[i6][i7];
               bytesArr5[i6][i7] = bytesArr5[n3][i7];
               bytesArr5[n3][i7] = b3;
            }

            b2 = bytesArr5[i6][i6];
         }

         for (int i8 = 0; i8 < 8; i8++) {
            if (bytesArr5[i6][i8] != 0) {
               bytesArr5[i6][i8] = (byte)this.alog[(255 + this.log[bytesArr5[i6][i8] & 255] - this.log[b2 & 255]) % 255];
            }
         }

         for (int i9 = 0; i9 < 4; i9++) {
            if (i6 != i9) {
               for (int i10 = i6 + 1; i10 < 8; i10++) {
                  bytesArr5[i9][i10] = (byte)(bytesArr5[i9][i10] ^ this.mul(bytesArr5[i6][i10], bytesArr5[i9][i6]));
               }

               bytesArr5[i9][i6] = 0;
            }
         }
      }

      for (int i11 = 0; i11 < 4; i11++) {
         for (int i12 = 0; i12 < 4; i12++) {
            bytesArr6[i11][i12] = bytesArr5[i11][i12 + 4];
         }
      }

      for (int i13 = 0; i13 < 256; i13++) {
         byte b4 = this.sBox[i13];
         this.T1[i13] = this.mul4(b4, bytesArr4[0]);
         this.T2[i13] = this.mul4(b4, bytesArr4[1]);
         this.T3[i13] = this.mul4(b4, bytesArr4[2]);
         this.T4[i13] = this.mul4(b4, bytesArr4[3]);
         b4 = this.invSBox[i13];
         this.T5[i13] = this.mul4(b4, bytesArr6[0]);
         this.T6[i13] = this.mul4(b4, bytesArr6[1]);
         this.T7[i13] = this.mul4(b4, bytesArr6[2]);
         this.T8[i13] = this.mul4(b4, bytesArr6[3]);
         this.U1[i13] = this.mul4(i13, bytesArr6[0]);
         this.U2[i13] = this.mul4(i13, bytesArr6[1]);
         this.U3[i13] = this.mul4(i13, bytesArr6[2]);
         this.U4[i13] = this.mul4(i13, bytesArr6[3]);
      }

      this.rcon[0] = 1;
      int n4 = 1;

      for (int i14 = 1; i14 < 30; i14++) {
         n4 = this.mul(2, n4);
         this.rcon[i14] = (byte)n4;
      }

      Object obj2 = null;
      this.log = (int[])obj2;
      Object obj3 = null;
      this.alog = (int[])obj3;
   }

   private int getRounds(int n) {
      return (n >> 2) + 6;
   }

   public byte[] encrypt(byte[] bytes, int n, int n2) {
      int[] controlFlowMarker2 = getControlFlowMarker();
      if (n2 <= 0) {
         return null;
      } else {
         try {
            bytes = this.prepareInput(bytes, n, n2, false);
         } catch (ShortBufferException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
         }

         n2 = bytes.length;
         byte[] bytes2 = new byte[n2];
         this.checkBlockAlignment(n2, this.getBlockSize());
         this.encryptCbc(bytes, n, n2, bytes2, 0);
         if (bytes2.length % this.getBlockSize() != 0) {
            System.out.println("Writing " + bytes2.length);
         }

         return bytes2;
      }
   }

   private void implEncryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      int n3 = 0;
      getControlFlowMarker();
      int n4 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n5 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n6 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n7 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];

      while (n3 < this.roundLimit) {
         int n8 = this.T1[n4 >>> 24] ^ this.T2[n5 >>> 16 & 0xFF] ^ this.T3[n6 >>> 8 & 0xFF] ^ this.T4[n7 & 0xFF] ^ this.roundKeys[n3++];
         int n9 = this.T1[n5 >>> 24] ^ this.T2[n6 >>> 16 & 0xFF] ^ this.T3[n7 >>> 8 & 0xFF] ^ this.T4[n4 & 0xFF] ^ this.roundKeys[n3++];
         int n10 = this.T1[n6 >>> 24] ^ this.T2[n7 >>> 16 & 0xFF] ^ this.T3[n4 >>> 8 & 0xFF] ^ this.T4[n5 & 0xFF] ^ this.roundKeys[n3++];
         n7 = this.T1[n7 >>> 24] ^ this.T2[n4 >>> 16 & 0xFF] ^ this.T3[n5 >>> 8 & 0xFF] ^ this.T4[n6 & 0xFF] ^ this.roundKeys[n3++];
         n4 = n8;
         n5 = n9;
         n6 = n10;
      }

      int n11 = this.roundKeys[n3++];
      bytes2[n2++] = (byte)(this.sBox[n4 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n5 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n6 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2++] = (byte)(this.sBox[n7 & 0xFF] ^ n11);
      n11 = this.roundKeys[n3++];
      bytes2[n2++] = (byte)(this.sBox[n5 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n6 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n7 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2++] = (byte)(this.sBox[n4 & 0xFF] ^ n11);
      n11 = this.roundKeys[n3++];
      bytes2[n2++] = (byte)(this.sBox[n6 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n7 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n4 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2++] = (byte)(this.sBox[n5 & 0xFF] ^ n11);
      n11 = this.roundKeys[n3++];
      bytes2[n2++] = (byte)(this.sBox[n7 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n4 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n5 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2] = (byte)(this.sBox[n6 & 0xFF] ^ n11);
   }

   private int encryptCbc(byte[] bytes, int n, int n2, byte[] bytes2, int n3) {
      int n4 = 0;
      getControlFlowMarker();
      int n5 = 0;
      int n6 = 0;
      long l = -3973984876722295909L;
      long l2 = 0L;
      l = 638859782304525967L ^ (long)"묨愈욄搳붬ۑ\ufff4ꑈ恘\udcd6ﵻꬩ돢㱙뜄䋮몆㋬룏\uf1e9".hashCode();
      l2 = 6099968798576080657L ^ (long)"粏\udba3拾\uea39ၡ\udd38촼샍퍕뾐떡쾷㘉駿䏹됳Ⴅ\ue85d៱鴥蕆譬幢૾슅\uf0ae奰螑".hashCode();

      while (true) {
         n6 = (int)(l % 13L);
         if (6 <= n6) {
            if (n6 <= 6) {
               l = l - -4114393246648552447L ^ -6428261423251312344L;
               l2 = (l2 ^ -2633338696705798543L) - 6020723794940389984L;
            } else if (9 <= n6) {
               if (n6 <= 9) {
                  l = l - 1996988126884733190L ^ 3807952596324655682L;
                  l2 = l2 + 7715432327545657218L ^ 7040379623933080443L;
               } else if (11 <= n6) {
                  if (n6 > 11) {
                     return n2;
                  }

                  n4 = n + n2;
                  l = l - -7986940968196221284L ^ 8154907381817219288L;
                  l2 = l2 + -8524000677292390607L ^ 5684668078188089872L;
               } else {
                  System.arraycopy(bytes2, n3, null, 0, this.getBlockSize());
                  l = (l ^ -2940183632908454184L) + -8987389019293096659L;
                  l2 = l2 - -2921178837478190867L ^ -9051292349076431595L;
               }
            } else if (7 >= n6) {
               if (n5 >= this.getBlockSize()) {
                  l = l - 5316912954732798956L ^ 7813854302758062227L;
                  l2 = (l2 ^ -1399344583756577536L) + 8746833932359420656L;
               } else {
                  l = (l ^ -3772780032061179007L) - -6250893295974214179L;
                  l2 = (l2 ^ 53676486844488697L) - -6111610610209565520L;
               }
            } else {
               this.encryptBlock(null, 0, bytes2, n3);
               l = (l ^ 935300300324250778L) + -1097768312144062493L;
               l2 = l2 - -3057910571193413328L ^ -2443264575076305878L;
            }
         } else if (n6 >= 2) {
            if (n6 <= 2) {
               if (n >= n4) {
                  l = (l ^ 8490766414115013163L) + -5328117093712893930L;
                  l2 = (l2 ^ 4457557177242580536L) + -12582883300742484L;
               } else {
                  l = (l ^ -7585826914604898879L) - 8013791921216019846L;
                  l2 = l2 + -4752539019157203389L ^ 6969027152241565477L;
               }
            } else if (4 <= n6) {
               if (4 >= n6) {
                  n5 = 0;
                  l = (l ^ 2528143767163067452L) - -7931898987540689765L;
                  l2 = l2 - -4739741680079451270L ^ -5797703201205026886L;
               } else {
                  n5++;
                  l = l - -2406933502058492639L ^ 3442322606878865452L;
                  l2 = (l2 ^ -5380675747274654558L) - -1563969438741032906L;
               }
            } else {
               ((byte[])null)[n5] ^= ((byte[])bytes)[n5 + n];
               l = (l ^ 4457196301950659314L) - 130488747361005769L;
               l2 = l2 + -5181209099290321608L ^ 7258037213536056828L;
            }
         } else if (n6 <= 0) {
            n += this.getBlockSize();
            l = (l ^ -518096718490022411L) + -2823448136779282319L;
            l2 = l2 - -8874838099195228003L ^ 5717388901503548929L;
         } else {
            n3 += this.getBlockSize();
            l = (l ^ -7948589729734521207L) - -8324475963364917221L;
            l2 = (l2 ^ -764801763854258822L) + -6174946816825166821L;
         }
      }
   }

   public void resetIv() {
      boolean flag = true;
      long l = -527380693120113810L;
      int[] controlFlowMarker2 = getControlFlowMarker();
      l = 7416219365747155458L ^ (long)"鄱淐ᵳ⚨ਹ㵴蒲㶿鰏۞ਜꓔ턟粿\uf027䮄꧒瓾̷鼐".hashCode();
      byte[] bytes = new byte[this.getBlockSize()];
      this.cbcFeedbackIv = bytes;
      l = (l ^ 5374082427265060163L) + 17286346306474301L;
      byte[] bytes2 = new byte[this.getBlockSize()];
      this.cbcScratchBlock = bytes2;
      l = (l ^ 2405274973022208420L) - -2156170588943391545L;
   }

   private int mul4(int n, byte[] bytes) {
      int n2 = 0;
      getControlFlowMarker();
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      int n6 = 2;
      long l = -8208200072209929073L;
      long l2 = 0L;
      l = -1772508146076397082L ^ (long)"Ⳁ\uec75兠迅\ue709ᩄ礖盉궊穠\uf7db\ueb9bڛἍᨇ\uef9f\uf69a돟趓⩇⁸襳裉阉爹".hashCode();
      l2 = 8641078200526745664L ^ (long)"ᷞ둽\ud97d᮲裘ㆩΌ係眄䠂⽌\ue401ᥒ疕ᢰ嬺鲍䖦ꗈᤓ瑅甘ꏻ撉⊏蟭슒㙆".hashCode();

      int n7;
      while (true) {
         n6 = (int)(l % 5L);
         if (n6 < 2) {
            if (0 < n6) {
               return 0;
            }

            n7 = this.alog[(n + this.log[((byte[])bytes)[0] & 255]) % 255] & 0xFF;
            break;
         }

         if (n6 <= 2) {
            if (n != 0) {
               l = l + 226074478036867620L ^ 335285128969461031L;
               l2 = (l2 ^ -3236475240847269207L) + 8633006436935926985L;
            } else {
               l = l - -4826428335465744041L ^ 3760243804271572129L;
               l2 = l2 - -2713762159493308466L ^ 1174013422073124519L;
            }
         } else if (3 >= n6) {
            if (bytes[0] == 0) {
               l = l + 5511357910979645326L ^ 5952489630426262899L;
               l2 = (l2 ^ -971844485194888763L) + -764875235303859734L;
               n7 = 0;
               break;
            }

            l = l - 6135549784087286164L ^ -776557853425316947L;
            l2 = (l2 ^ 8953448135120323673L) + 7972590528785420110L;
         } else {
            n = this.log[n & 0xFF];
            l = (l ^ 1419395388205055439L) - -1998487207319875548L;
            l2 = (l2 ^ -3943038801177360680L) + 4766992129200076602L;
         }
      }

      n2 = n7;
      n3 = ((byte[])bytes)[1] != 0 ? ((byte[])null)[(n + ((byte[])null)[((byte[])bytes)[1] & 255]) % 255] & 0xFF : 0;
      n5 = ((byte[])bytes)[2] != 0 ? ((byte[])null)[(n + ((byte[])null)[((byte[])bytes)[2] & 255]) % 255] & 0xFF : 0;
      n4 = ((byte[])bytes)[3] != 0 ? ((byte[])null)[(n + ((byte[])null)[((byte[])bytes)[3] & 255]) % 255] & 0xFF : 0;
      return n2 << 24 | n3 << 16 | n5 << 8 | n4;
   }

   private byte[] prepareInput(byte[] bytes, int n, int n2, boolean flag) throws IllegalBlockSizeException, ShortBufferException {
      getControlFlowMarker();
      int n3 = 0;
      int n4 = 0;
      byte b = 0;
      int n5 = 0;
      byte[] bytes2 = null;
      int n6 = 0;
      int n7 = 15;
      long l = -599820121169284076L;
      long l2 = 0L;
      l = 5283095972557391500L ^ (long)"끿蘡晘堿\ue4b1箩⹖복\udec8氺掦㘒娙굷춦俈漳黉麱쌥㼘앏ﺢ퐝卜\udb01\uf091㷲".hashCode();
      l2 = -7703780970464761220L ^ (long)"瀙\u001e埸齓\ued93\ue0a5餵逽罽㜔흃鼦ȑ㎀\uef56䀪\uf422ߟ汅閡츂\uef33".hashCode();

      while (true) {
         n7 = (int)(l % 19L);
         if (n7 >= 9) {
            if (n7 <= 9) {
               n6 = n5;
               l = l + -7123247819047840326L ^ 3712720791623255551L;
               l2 = (l2 ^ 204897252175671522L) + -885057085790257014L;
            } else if (n7 >= 14) {
               if (n7 <= 14) {
                  return bytes;
               }

               if (n7 >= 16) {
                  if (n7 <= 16) {
                     if (n4 <= 0) {
                        l = l - 0L ^ 0L;
                        l2 = l2 - 0L ^ 0L;
                     } else {
                        l = l + 0L ^ 0L;
                        l2 = l2 - 0L ^ 0L;
                     }
                  } else {
                     if (17 >= n7) {
                        return bytes2;
                     }

                     b = 0;
                     l = l + 2911526302389305522L ^ 202694798018999565L;
                     l2 = l2 + 6286460044525966105L ^ -2609230672405821874L;
                  }
               } else {
                  n5 = Math.addExact(b, n2);
                  l = (l ^ 7640971586054838829L) + -1883525769544736900L;
                  l2 = l2 + 6892960139699489156L ^ -7248762143982482505L;
               }
            } else if (11 <= n7) {
               if (n7 <= 11) {
                  if (n2 == 0) {
                     l = l - 1855736908929652207L ^ -3320241197752875287L;
                     l2 = (l2 ^ -389482748193646812L) - 3883649778861242587L;
                  } else {
                     l = l + -51926871269187454L ^ 3262221456540418652L;
                     l2 = (l2 ^ -2618985768730666184L) + 332820879813092478L;
                  }
               } else if (n7 <= 12) {
                  n4 = 0;
                  l = l + -972587580906388475L ^ 4961210302369179355L;
                  l2 = (l2 ^ 9161156377281791117L) + -7953157796485873738L;
               } else {
                  n4 = this.padLength(n6);
                  l = (l ^ 2059616656697914711L) + -6323616967945492128L;
                  l2 = l2 + -7194898257378646304L ^ 7629714958314309441L;
               }
            } else {
               n3 = this.getBlockSize();
               l = (l ^ 3336786742097777524L) - -1605985821875430326L;
               l2 = (l2 ^ 1610049253147013085L) + 4052451226869452381L;
            }
         } else if (4 <= n7) {
            if (n7 <= 4) {
               System.arraycopy(bytes, n, bytes2, b, n2);
               l = l + -7719704957456578723L ^ -7741861604693762717L;
               l2 = l2 - -3920723613777257632L ^ -3450769532136777189L;
            } else if (n7 >= 6) {
               if (n7 <= 6) {
                  if (!flag) {
                     l = (l ^ 6118534222838776359L) - 7829797126130442461L;
                     l2 = l2 - 1653677266318564166L ^ -185216141935744714L;
                  } else {
                     l = (l ^ 0L) + 0L;
                     l2 = l2 - 0L ^ 0L;
                  }
               } else {
                  if (n7 > 7) {
                     throw new IllegalBlockSizeException("Input length must be multiple of " + n3 + " when decrypting with padded cipher");
                  }

                  if (n4 == n3) {
                     l = l - 0L ^ 0L;
                     l2 = l2 + 0L ^ 0L;
                  } else {
                     l = l + 0L ^ 0L;
                     l2 = (l2 ^ 0L) + 0L;
                  }
               }
            } else if (n4 == 0) {
               l = l + -3168677435628067063L ^ -3838840810738267879L;
               l2 = l2 - -137493035010651727L ^ 7850983834837670819L;
            } else {
               l = l + -202317463748922192L ^ 6047245123194191187L;
               l2 = (l2 ^ -8447626944385461046L) - 8706374805986086715L;
            }
         } else if (1 <= n7) {
            if (1 >= n7) {
               if (flag) {
                  l = (l ^ 0L) - 0L;
                  l2 = (l2 ^ 0L) - 0L;
               } else {
                  l = (l ^ -7160616936310573849L) - 8341410477520879187L;
                  l2 = l2 - -2279841825657380272L ^ -2775806261739493522L;
               }
            } else if (2 >= n7) {
               bytes2 = new byte[Math.addExact(n5, n4)];
               l = (l ^ 8595157087275162200L) + -5164402516489042822L;
               l2 = l2 - 3597415301969251503L ^ -1726132125462606925L;
            } else {
               this.padWithLen(bytes2, Math.addExact(b, n2), n4);
               l = l - 6078422575708450572L ^ -4256717983101797563L;
               l2 = (l2 ^ 6287484259276412563L) - -2453802288186278548L;
            }
         } else {
            int n8 = n2 % n3;
            l = (l ^ -5682534165698771126L) - -6018789790317281603L;
            l2 = l2 + 3958262220253420419L ^ -1392455380159478470L;
         }
      }
   }

   public void encryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      this.implEncryptBlock(bytes, n, bytes2, n2);
   }

   private void implDecryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      int n3 = 4;
      int n4 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n5 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      getControlFlowMarker();
      int n6 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n7 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n] & 255) ^ this.roundKeys[n3++];
      if (this.added) {
         int n8 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
         int n9 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
         int n10 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
         n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
         n4 = this.T5[n8 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n10 >>> 8 & 0xFF] ^ this.T8[n9 & 0xFF] ^ this.roundKeys[n3++];
         n5 = this.T5[n9 >>> 24] ^ this.T6[n8 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n10 & 0xFF] ^ this.roundKeys[n3++];
         n6 = this.T5[n10 >>> 24] ^ this.T6[n9 >>> 16 & 0xFF] ^ this.T7[n8 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
         n7 = this.T5[n7 >>> 24] ^ this.T6[n10 >>> 16 & 0xFF] ^ this.T7[n9 >>> 8 & 0xFF] ^ this.T8[n8 & 0xFF] ^ this.roundKeys[n3++];
         if (this.has14Rounds) {
            n8 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
            n9 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
            n10 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
            n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
            n4 = this.T5[n8 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n10 >>> 8 & 0xFF] ^ this.T8[n9 & 0xFF] ^ this.roundKeys[n3++];
            n5 = this.T5[n9 >>> 24] ^ this.T6[n8 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n10 & 0xFF] ^ this.roundKeys[n3++];
            n6 = this.T5[n10 >>> 24] ^ this.T6[n9 >>> 16 & 0xFF] ^ this.T7[n8 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
            n7 = this.T5[n7 >>> 24] ^ this.T6[n10 >>> 16 & 0xFF] ^ this.T7[n9 >>> 8 & 0xFF] ^ this.T8[n8 & 0xFF] ^ this.roundKeys[n3++];
         }
      }

      int n11 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
      int n12 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
      int n13 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
      n4 = this.T5[n11 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n13 >>> 8 & 0xFF] ^ this.T8[n12 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.T5[n12 >>> 24] ^ this.T6[n11 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n13 & 0xFF] ^ this.roundKeys[n3++];
      n6 = this.T5[n13 >>> 24] ^ this.T6[n12 >>> 16 & 0xFF] ^ this.T7[n11 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n13 >>> 16 & 0xFF] ^ this.T7[n12 >>> 8 & 0xFF] ^ this.T8[n11 & 0xFF] ^ this.roundKeys[n3++];
      n11 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
      n12 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
      n13 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
      n4 = this.T5[n11 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n13 >>> 8 & 0xFF] ^ this.T8[n12 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.T5[n12 >>> 24] ^ this.T6[n11 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n13 & 0xFF] ^ this.roundKeys[n3++];
      n6 = this.T5[n13 >>> 24] ^ this.T6[n12 >>> 16 & 0xFF] ^ this.T7[n11 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n13 >>> 16 & 0xFF] ^ this.T7[n12 >>> 8 & 0xFF] ^ this.T8[n11 & 0xFF] ^ this.roundKeys[n3++];
      n11 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
      n12 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
      n13 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
      n4 = this.T5[n11 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n13 >>> 8 & 0xFF] ^ this.T8[n12 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.T5[n12 >>> 24] ^ this.T6[n11 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n13 & 0xFF] ^ this.roundKeys[n3++];
      n6 = this.T5[n13 >>> 24] ^ this.T6[n12 >>> 16 & 0xFF] ^ this.T7[n11 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n13 >>> 16 & 0xFF] ^ this.T7[n12 >>> 8 & 0xFF] ^ this.T8[n11 & 0xFF] ^ this.roundKeys[n3++];
      n11 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
      n12 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
      n13 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
      n4 = this.T5[n11 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n13 >>> 8 & 0xFF] ^ this.T8[n12 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.T5[n12 >>> 24] ^ this.T6[n11 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n13 & 0xFF] ^ this.roundKeys[n3++];
      n6 = this.T5[n13 >>> 24] ^ this.T6[n12 >>> 16 & 0xFF] ^ this.T7[n11 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n13 >>> 16 & 0xFF] ^ this.T7[n12 >>> 8 & 0xFF] ^ this.T8[n11 & 0xFF] ^ this.roundKeys[n3++];
      n11 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
      n12 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
      n13 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.roundKeys[0];
      bytes2[n2++] = (byte)(this.invSBox[n11 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n7 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n13 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2++] = (byte)(this.invSBox[n12 & 0xFF] ^ n5);
      n5 = this.roundKeys[1];
      bytes2[n2++] = (byte)(this.invSBox[n12 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n11 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n7 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2++] = (byte)(this.invSBox[n13 & 0xFF] ^ n5);
      n5 = this.roundKeys[2];
      bytes2[n2++] = (byte)(this.invSBox[n13 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n12 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n11 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2++] = (byte)(this.invSBox[n7 & 0xFF] ^ n5);
      n5 = this.roundKeys[3];
      bytes2[n2++] = (byte)(this.invSBox[n7 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n13 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n12 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2] = (byte)(this.invSBox[n11 & 0xFF] ^ n5);
   }

   public void setSessionKey(byte[] bytes) {
      this.scatterSessionKey(bytes);
      this.keyGuardSlotA = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-75, 91, -64, -69, 12, 51, -95, -29, 89, -112, 123, 115, -52, -122, -7, -49, -31, -99, 115, -49, 24, 91, -96, 36, 56, -57, 69, -86, 105, -96, -46, 12}, this.keyDeriverCipher));
      this.decoyKeySlot151 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-118, -6, -117, -59, -48, 19, 22, 4, -28, -86, 64, -33, -97, 19, 40, 58, -91, 6, -65, -46, -107, -125, 85, 56, -23, 125, -99, 31, 31, -9, 31, -107}, this.keyDeriverCipher));
      this.decoyKeySlot111 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-67, 27, 86, -28, -99, 71, -21, 106, 66, 100, -54, -28, 110, 40, -85, -39, -120, 107, 43, -7, -89, 28, 50, -38, -8, 60, 36, 11, -57, -95, 53, -84}, this.keyDeriverCipher));
      this.decoyKeySlot088 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{106, 77, -60, 101, 96, -1, 95, 93, 25, -1, -74, -65, -31, 32, 24, -11, -113, -116, 97, -40, 9, -65, 21, -54, -22, 113, 112, -101, 123, -118, 69, -7}, this.keyDeriverCipher));
      this.decoyKeySlot028 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-34, -1, -3, 116, -85, 9, -121, 88, -51, 40, 56, 121, -124, -64, -20, -45, -121, 62, -17, -101, 89, 94, -30, -105, -82, -24, -104, 94, 27, 20, -23, 99}, this.keyDeriverCipher));
      this.decoyKeySlot087 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-15, -85, -17, 108, 65, 5, 10, 61, 90, 15, -3, -33, -101, 98, -82, 96, -124, -4, -61, 55, -11, -67, -95, 70, -115, 118, 105, 74, -105, -117, -16, 31}, this.keyDeriverCipher));
      this.decoyKeySlot167 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-120, -20, -65, 54, 69, 41, 83, 76, 121, 109, -92, -12, -94, -120, 39, -65, -16, -38, 68, 43, 14, 34, -82, 84, 105, -38, -115, 89, -50, 51, 59, 18}, this.keyDeriverCipher));
      this.decoyKeySlot071 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{29, 40, -4, -39, -31, -47, -67, 65, -20, 63, -76, -41, 3, 29, 71, 28, 77, 75, -30, 53, 4, -62, -67, -102, 77, 105, 52, -56, -23, 55, 46, 110}, this.keyDeriverCipher));
      this.decoyKeySlot033 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-58, 27, -123, -3, 70, 62, -56, 2, 97, 126, -36, -27, -98, 28, -97, -52, 109, -74, 63, -59, -49, -41, -126, 78, 71, -32, 24, 0, 22, 89, 107, -68}, this.keyDeriverCipher));
      this.decoyKeySlot148 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{95, -28, -4, -54, 30, -1, 97, -3, -85, -77, 83, 39, -128, 89, 103, 47, -85, -1, -31, 17, 89, 101, -66, -79, -20, 47, -47, 120, 42, 34, -50, 18}, this.keyDeriverCipher));
      this.decoyKeySlot045 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-78, -88, 19, 125, 97, 55, -56, 111, -70, 16, -38, 90, -81, 50, 14, -122, 67, -35, -123, 106, -49, -108, -57, 4, -34, -112, 103, 24, 28, -97, -53, 84}, this.keyDeriverCipher));
      this.decoyKeySlot150 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{6, -111, -26, -90, 50, -99, -5, -125, 35, -23, -67, -44, -27, -41, -74, -97, -113, -72, -71, 57, 26, -86, -120, -109, 109, 80, 34, -70, 72, 127, -112, 94}, this.keyDeriverCipher));
      this.decoyKeySlot060 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-69, -29, 63, 118, -42, 85, 1, -81, 50, -74, -115, -113, -112, 105, 51, -44, -101, 75, 76, -16, 14, 92, 81, 8, 22, 118, 56, 49, 62, 43, -119, -15}, this.keyDeriverCipher));
      this.decoyKeySlot038 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{102, 86, -32, -73, 89, -18, 78, -118, -95, 10, 43, -121, 98, 26, -26, -57, -4, 30, -54, 109, -85, -15, 113, -99, -127, -56, -65, -128, -24, 126, -120, -99}, this.keyDeriverCipher));
      this.decoyKeySlot190 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{62, -60, 53, 117, 22, 75, 121, 51, -41, 59, 51, -96, -13, -117, 105, -59, 7, 104, 119, 105, -25, 76, 94, 122, -105, 23, -92, -78, -8, 103, -102, -1}, this.keyDeriverCipher));
      this.decoyKeySlot196 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-106, -37, -13, 58, -22, 62, -49, -34, -78, -86, 50, 99, 125, -123, -13, 2, -69, -122, -18, 41, 114, 29, 116, 13, 6, -125, -111, -7, 87, 41, -31, -92}, this.keyDeriverCipher));
      this.decoyKeySlot166 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{60, -69, -108, -124, -111, -72, -110, 109, 68, 77, 1, 11, -9, -112, 70, -73, 108, -20, 29, -68, 127, -71, 20, -115, 126, 54, 14, 0, -6, 115, 71, 14}, this.keyDeriverCipher));
      this.decoyKeySlot057 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{3, 7, -41, -5, 57, -58, -76, -108, -81, -98, -50, -54, 84, 91, 79, 95, -52, -26, 83, -22, -53, -28, -65, -7, -33, -112, 16, 21, 125, -75, 24, -31}, this.keyDeriverCipher));
      this.decoyKeySlot085 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-36, -81, -68, -15, 59, -103, -4, 4, 77, -66, -48, -47, -44, 34, -112, -94, 96, -44, 43, -95, 14, 116, -71, -90, -38, 102, 76, -64, -65, -68, -43, -92}, this.keyDeriverCipher));
      this.decoyKeySlot075 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-126, 87, -14, 24, -103, 13, -14, -14, -10, 43, 20, -48, 84, -97, -33, 2, 67, -116, -37, -77, 35, 55, 86, -110, 106, -94, 105, 86, 15, 110, -111, 44}, this.keyDeriverCipher));
      this.decoyKeySlot049 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-97, 96, 39, 98, -109, 82, -122, -77, 81, -44, -119, 45, 59, 15, 31, 30, 20, -69, -7, -70, 81, 6, -60, -99, -85, -41, 118, 98, -124, 110, -6, 87}, this.keyDeriverCipher));
      this.decoyKeySlot135 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{9, -65, -121, -4, 111, 83, 102, -118, 74, -114, -97, 101, 41, -125, -50, 22, -81, -75, 73, 73, 2, -82, -70, -126, -30, 100, -82, -17, 75, -59, -10, 121}, this.keyDeriverCipher));
      this.decoyKeySlot183 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{77, -17, 18, -67, 82, 91, 100, 19, 63, -117, 79, -55, 117, 6, 2, -80, 63, 101, -75, -105, -92, 102, 116, 117, 83, 121, 83, 35, 90, -117, 120, -19}, this.keyDeriverCipher));
      this.decoyKeySlot036 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-100, 17, 63, 25, -17, 22, -106, -30, -70, 6, 122, -37, 113, 8, -56, -126, -99, 57, -106, -10, -60, 67, 61, -95, -32, -103, -73, 40, 122, 108, 63, 87}, this.keyDeriverCipher));
      this.decoyKeySlot101 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-16, 70, 78, 113, 83, 49, 41, -38, -125, -90, 99, -121, -114, 50, 17, 1, -51, 42, -21, 0, -3, -112, 118, -44, -15, 70, -101, -123, 5, 127, -14, 103}, this.keyDeriverCipher));
      this.decoyKeySlot187 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{126, 127, 15, 40, 9, -111, 41, -102, -70, -123, 24, 22, 127, 63, -57, -121, 88, -63, -114, 111, 113, -72, 20, 76, -82, -21, -79, 23, -68, -62, -66, 96}, this.keyDeriverCipher));
      this.decoyKeySlot058 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-26, -100, -51, -113, 56, -92, -14, 0, -10, 56, 56, -109, 122, -109, -35, -103, -30, 35, -8, -63, 57, -69, 43, -113, 0, 54, 70, 0, 10, -47, 116, 24}, this.keyDeriverCipher));
      this.decoyKeySlot021 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-38, -106, -22, 30, 89, 96, -75, -82, 80, -43, 74, 61, 53, 55, 80, 85, -6, 67, 47, 84, 45, 121, 21, -88, -77, 43, 48, 45, 31, 75, 62, 103}, this.keyDeriverCipher));
      this.decoyKeySlot039 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{114, 43, 110, 29, 85, -74, 60, -30, 15, 117, -101, 66, 44, -8, 42, -44, -87, -91, -36, 41, -96, -41, -50, -30, 41, 115, -61, 118, 62, 98, -76, -88}, this.keyDeriverCipher));
      this.decoyKeySlot017 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-30, -41, 12, 43, 12, -41, -58, 67, -58, 54, 81, 98, -44, -17, 69, -66, 29, -84, 106, -91, -36, -81, -34, 30, -61, -38, 42, -116, -17, 52, -76, 64}, this.keyDeriverCipher));
      this.decoyKeySlot064 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{40, -109, -93, -91, -38, -94, -101, -96, -73, -82, 45, -12, -28, 10, 64, 104, 119, 56, -30, -101, -52, -39, 22, 2, 18, -111, 115, -127, 116, 35, -90, 2}, this.keyDeriverCipher));
      this.decoyKeySlot120 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-66, -124, 33, -55, -12, -95, 26, 122, 25, 3, -112, -1, 22, 103, 106, -13, 81, 36, 22, -56, 47, 76, -5, -74, 37, 25, -15, 21, -6, -65, 16, -56}, this.keyDeriverCipher));
      this.decoyKeySlot186 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-75, 120, -49, 28, -22, 123, -83, -90, 58, -115, 34, -19, 25, 53, -114, -89, -120, -89, -80, 121, -55, 103, -120, -109, 53, -89, -68, 101, -52, 97, 93, -108}, this.keyDeriverCipher));
      this.decoyKeySlot188 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-40, 36, 3, -97, 92, 12, -105, -48, 37, -2, -12, 67, -72, -16, -47, 90, -24, -42, 21, 75, -18, -101, -37, -15, 122, -17, 111, -9, 85, -60, 7, 69}, this.keyDeriverCipher));
      this.decoyKeySlot141 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{79, -10, 26, -76, -3, 16, 54, -20, 19, -35, 57, -98, 69, -127, -110, 107, -113, 75, 83, -26, 39, -68, -45, -63, 28, 44, -77, 95, 1, -7, 50, 2}, this.keyDeriverCipher));
      this.decoyKeySlot182 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-53, -62, -107, -51, 67, -114, 75, -85, 121, 74, 98, -16, -9, 6, 35, -47, -107, 30, 112, 63, -10, -28, -27, -101, -27, -91, 81, -52, -105, 27, -57, 77}, this.keyDeriverCipher));
      this.decoyKeySlot104 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-95, -41, -29, -99, 77, -53, 68, 72, -33, -39, 47, 7, -8, -82, -108, -107, 55, 39, 10, -24, -6, -46, -41, 102, 7, 123, -27, -46, 0, 97, -87, 71}, this.keyDeriverCipher));
      this.decoyKeySlot177 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{89, -120, 102, -76, -41, -113, 12, 126, -39, 115, 69, 29, -6, -23, 2, 62, 47, -45, 25, 30, 55, 56, -105, 110, -19, 13, 68, 33, -49, -51, -114, -98}, this.keyDeriverCipher));
      this.decoyKeySlot119 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{25, -51, -35, -33, 33, -110, -93, -3, 38, 97, 94, 101, -40, -53, -48, 42, -20, 14, -74, 126, 69, 125, 104, -116, -74, -1, -86, -52, 86, -13, 59, -25}, this.keyDeriverCipher));
      this.decoyKeySlot032 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-95, 101, -87, 24, 73, 18, -126, -97, 100, -93, 9, -69, -74, 48, -125, -26, -94, 33, -77, -46, -3, -40, -79, -93, -112, 35, 56, -24, 102, -41, -128, -25}, this.keyDeriverCipher));
      this.decoyKeySlot179 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{24, -95, 65, -125, -126, -91, 118, -1, -49, 60, -45, -41, 46, -76, 83, -44, -100, 79, -86, -120, -5, -66, -6, 86, 9, 38, 92, -35, -101, -107, -42, 16}, this.keyDeriverCipher));
      this.decoyKeySlot051 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{16, -101, 55, 102, 2, -29, 24, 47, 40, -117, 19, 60, 95, -116, 65, -59, -3, -10, -122, 102, -60, 42, -2, -90, -126, 82, 120, 113, 16, -101, -91, -10}, this.keyDeriverCipher));
      this.decoyKeySlot043 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-37, 39, 44, 108, 123, -25, -39, -87, 120, 99, 38, -9, 62, 37, -75, -10, -38, -24, 37, -1, 93, 31, 78, -7, 89, 64, -43, -55, -126, -84, -65, -78}, this.keyDeriverCipher));
      this.decoyKeySlot063 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{89, 113, 75, -38, -68, 101, 69, 3, -92, 51, 121, 118, 4, 67, 77, -47, 117, 52, -63, 27, -92, -66, -66, -65, 50, 117, -99, -93, -16, -48, 36, -48}, this.keyDeriverCipher));
      this.decoyKeySlot155 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-56, 107, -6, 37, 62, -89, -75, 117, -42, 24, -88, 110, -103, -14, -21, 123, 40, 83, -52, 96, 88, -114, 93, -5, -103, 106, -107, 65, 100, 115, 102, -46}, this.keyDeriverCipher));
      this.decoyKeySlot098 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{105, -76, -60, -61, -65, 127, -58, -37, -81, 112, -52, -39, -13, 28, 79, 77, -96, -119, 69, 89, 45, -67, 80, -22, 122, 39, -77, 48, -67, -116, -7, -57}, this.keyDeriverCipher));
      this.decoyKeySlot139 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{74, 106, -50, 126, 121, 1, -71, 17, -58, 18, 89, 95, -26, -88, -126, -81, -123, 94, 58, 81, 80, -2, 26, 81, 85, -97, 31, -5, 89, 33, -9, 110}, this.keyDeriverCipher));
      this.decoyKeySlot160 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{60, 61, 43, 124, -6, 96, 99, 62, 103, 101, 98, -96, -18, -59, 126, 127, -36, -58, -31, -12, 35, 47, -84, -12, 84, 45, 97, -98, -99, 27, 116, 47}, this.keyDeriverCipher));
      this.decoyKeySlot197 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-41, 23, 77, 107, -30, 123, 109, 120, -33, -54, -99, -114, -16, 114, 94, 64, -77, -48, 95, -33, -45, -5, 1, -72, 76, -90, -128, 32, 57, -74, 58, 8}, this.keyDeriverCipher));
      this.decoyKeySlot124 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-87, -29, -94, 114, 28, -32, 79, -104, 81, 112, -41, 23, 47, 79, 29, -75, -72, -78, -72, -11, -82, -38, 1, -48, 80, 74, 29, -79, 57, -101, -13, -98}, this.keyDeriverCipher));
      this.decoyKeySlot092 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-127, 63, -46, -88, 33, 82, 6, -62, 96, 108, -108, 5, 104, -106, 107, 20, 22, -14, 95, -9, -116, -8, -70, -19, 70, -20, -73, 45, 95, 60, 69, 21}, this.keyDeriverCipher));
      this.decoyKeySlot044 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-103, -72, 43, 99, 74, 35, -13, 79, -23, 27, 102, 40, -55, 4, -114, 104, 0, 94, 125, 112, -119, -2, -43, 32, 26, 7, 118, 100, 64, -92, 7, -49}, this.keyDeriverCipher));
      this.decoyKeySlot069 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-73, 16, -36, -72, 22, 59, 5, 27, 97, 114, 47, 73, 22, 102, 65, -1, -106, -31, -127, -72, -80, 40, -65, -9, -79, -48, -101, -27, -117, 0, 29, 119}, this.keyDeriverCipher));
      this.decoyKeySlot022 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-88, -109, 120, -14, -36, 124, -84, -104, -85, 15, -108, -31, 84, -69, 109, 57, 55, 25, 68, -85, 27, -10, -11, 109, -127, -26, -51, 27, -46, -12, -18, -2}, this.keyDeriverCipher));
      this.decoyKeySlot161 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-104, 87, 28, 85, -71, -78, -38, 48, -66, -113, 36, 111, 30, -79, -70, 66, 120, -11, -59, 113, -106, 97, -42, -54, 23, -51, 95, 119, -111, 53, -1, -32}, this.keyDeriverCipher));
      this.decoyKeySlot068 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{1, 120, -126, -74, 43, -110, 57, -72, 39, 67, -105, -109, 109, 56, -58, 119, 33, -124, -17, -115, 41, 29, -90, 58, -14, 45, 7, -78, -33, 101, -16, -5}, this.keyDeriverCipher));
      this.decoyKeySlot130 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-41, -117, -80, 32, 121, -67, -83, -122, -49, -23, -103, 102, 84, -18, 32, 124, -123, 22, -84, -33, 23, -108, 100, 49, 51, 44, 42, 38, 47, 62, 9, -107}, this.keyDeriverCipher));
      this.decoyKeySlot181 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{70, -9, -76, 72, 46, -53, 105, 103, -28, 96, 31, 49, -31, -46, 46, -128, 75, -80, 108, 77, 61, -75, -44, 53, 101, -7, -53, 98, 67, -44, -79, -82}, this.keyDeriverCipher));
      this.decoyKeySlot053 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-104, -44, 39, 72, -13, 7, 32, -12, -75, 77, -68, 57, 35, -37, -33, 55, 43, -16, -85, 121, -73, -71, -63, -80, 52, -69, -105, -79, -125, 12, 119, 102}, this.keyDeriverCipher));
      this.decoyKeySlot170 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-119, -1, 93, 76, 83, -119, -87, 119, -100, -53, -116, 111, 71, -40, 98, -40, 10, 1, -77, -39, -78, -42, 27, -8, -37, -85, -105, 100, -100, 9, 70, 75}, this.keyDeriverCipher));
      this.decoyKeySlot010 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-12, 6, -28, -107, -81, 50, -31, 69, 39, 5, 120, -83, 109, 100, 123, 114, -119, -10, -15, -49, 73, 119, -73, -120, 63, 13, 121, -9, -18, -12, -49, 2}, this.keyDeriverCipher));
      this.decoyKeySlot173 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-66, -36, -41, 24, 5, -125, 99, -121, -13, -113, 113, -102, -108, 125, -104, 25, 14, 69, 38, 36, -39, -32, 101, 14, -124, 126, 47, -31, -22, 56, -115, 110}, this.keyDeriverCipher));
      this.decoyKeySlot065 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-111, 11, -97, 45, -104, -87, 17, 41, 121, 76, 109, 57, 44, -42, 44, 59, 92, 92, -44, -47, -119, 40, -40, -62, 113, 124, 74, -69, 51, 119, 5, -124}, this.keyDeriverCipher));
      this.decoyKeySlot070 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{5, -91, -68, -56, 93, -74, 93, -94, 81, 47, -61, -94, -11, 24, 81, 31, 3, 54, -104, -126, 95, -73, -45, -63, 75, 114, -54, -69, 123, -83, -115, -1}, this.keyDeriverCipher));
      this.decoyKeySlot164 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{23, -5, 72, 115, -101, 61, 59, -27, -62, 4, 38, -66, -116, 20, -37, 93, -62, -53, -45, -116, 83, -41, -56, 104, -74, -91, -15, -125, -10, 66, -23, -82}, this.keyDeriverCipher));
      this.decoyKeySlot083 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-107, 122, 7, 38, -20, -95, -121, 69, -41, 110, -30, 41, 121, 3, 0, 124, -97, -93, 50, 106, -14, 92, -86, -17, 81, -109, -70, 20, 115, 89, -97, 108}, this.keyDeriverCipher));
      this.decoyKeySlot050 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{121, -7, -85, 95, -100, 11, -49, 20, -63, 81, -59, 121, -71, -80, -113, 126, -116, -118, -22, 105, -76, 87, 59, -123, 15, -53, -42, -79, -64, -90, 78, 10}, this.keyDeriverCipher));
      this.decoyKeySlot100 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-77, -106, -52, -8, 112, -30, 118, -125, -58, 90, 126, -37, 116, -19, 114, 70, -113, 108, -11, -123, 90, -39, 126, -121, -110, 44, -38, 51, 30, 93, 112, -3}, this.keyDeriverCipher));
      this.decoyKeySlot142 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-116, 94, 72, 122, -64, 71, 63, -76, -90, -45, 95, 64, -78, 25, -76, -42, 5, 90, -70, -115, -108, -19, 7, 104, 46, -97, -14, -109, -11, 93, 89, -88}, this.keyDeriverCipher));
      this.decoyKeySlot020 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{67, -59, -84, -110, -86, -89, -23, -18, -111, 106, 37, 126, -23, 56, 82, 26, 125, 90, 17, -105, -58, -54, 82, 5, 39, 13, 71, 68, -53, -75, 23, -107}, this.keyDeriverCipher));
      this.decoyKeySlot073 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{54, -14, 61, -120, 25, -125, 121, -19, 101, 56, 23, -7, 47, 13, -73, -64, -101, 101, 11, 102, -110, 37, 66, -64, -79, 92, -122, 32, -89, 111, -67, -57}, this.keyDeriverCipher));
      this.decoyKeySlot090 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{90, 80, -50, -45, 64, 125, 25, -88, 76, -32, 56, -60, 86, 30, 114, -41, -66, 99, 71, -94, -103, 32, 101, -8, 1, 38, 27, 0, -117, 11, 20, -111}, this.keyDeriverCipher));
      this.decoyKeySlot054 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{75, 49, 10, 51, 101, -122, 83, -37, -27, 124, 80, 18, 14, -39, 104, -94, -24, -101, 68, -39, 15, 106, 91, -65, 2, 79, -21, -97, -43, 127, 73, -34}, this.keyDeriverCipher));
      this.decoyKeySlot162 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{121, 53, 63, 47, 20, -21, 14, -63, 77, -104, -66, -47, -4, -89, 11, 22, 60, 14, 95, 78, -84, -87, -102, 5, 88, -40, 45, 27, -6, 21, 79, -7}, this.keyDeriverCipher));
      this.decoyKeySlot121 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{74, 117, 10, -11, -3, 106, -90, -53, 99, 20, -10, 115, -90, 117, -78, 48, 87, -35, 62, 104, -48, 25, -72, 58, 42, -127, -58, -20, -88, 6, 51, -88}, this.keyDeriverCipher));
      this.decoyKeySlot128 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{63, 21, 16, 35, -61, -78, -127, -19, 58, 84, -34, 83, 106, -72, -112, -9, -33, 80, 124, -21, -51, 39, -7, 31, 118, -39, 17, 46, -25, -8, 48, 82}, this.keyDeriverCipher));
      this.decoyKeySlot056 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-94, 80, -91, -6, -30, 1, -117, -72, 67, -51, -128, 25, 89, 48, 75, -89, -9, 20, -77, 34, -91, 44, 79, -89, -114, -48, 42, -56, 22, -118, -99, -19}, this.keyDeriverCipher));
      this.decoyKeySlot023 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-88, -7, -73, -72, -116, 126, -110, 49, 51, -125, 93, -125, -89, -71, 16, -92, -126, -15, -83, -69, -41, -100, 63, 102, 115, -91, 92, -112, -25, -69, -34, -106}, this.keyDeriverCipher));
      this.decoyKeySlot154 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-55, 80, 126, -43, -83, 69, -72, -46, -48, -101, 38, 27, -79, -35, -110, 1, -76, 116, -13, -107, 23, 58, -44, 2, -12, -25, 33, -60, -66, -21, -5, -66}, this.keyDeriverCipher));
      this.decoyKeySlot062 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{78, 122, -72, -75, -52, -15, -31, -74, 125, 114, -41, -67, -110, -16, -80, 120, -96, -66, -29, 107, 32, 116, 89, 103, 59, -51, -71, -37, 31, -104, -101, 55}, this.keyDeriverCipher));
      this.decoyKeySlot168 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{110, -125, -1, -93, -59, 19, -73, 116, -32, -45, 45, 0, -12, 35, -127, -108, -113, 63, -77, -91, 33, -92, 67, -100, -50, -52, -18, 52, 14, 1, 81, 42}, this.keyDeriverCipher));
      this.decoyKeySlot118 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-11, -6, -34, -46, 78, 57, 18, 42, -112, 64, 111, 25, 38, 56, 13, 13, 69, -113, -24, 34, -40, -101, 3, 84, -99, 94, -51, 28, -118, 78, -118, -57}, this.keyDeriverCipher));
      this.decoyKeySlot102 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{84, -8, -45, -120, -35, 99, 87, -7, 37, -58, -125, 95, -65, 5, 29, 123, -15, 99, -94, -81, 84, -108, 66, 97, 66, 57, -100, 15, -39, 51, 5, 18}, this.keyDeriverCipher));
      this.decoyKeySlot194 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-14, -15, 80, -42, -59, 36, -26, 36, 119, -60, 105, 104, 20, -56, 101, 26, -6, 22, 18, -49, -21, -26, 36, 81, -34, -124, -26, 63, 9, 16, 109, 77}, this.keyDeriverCipher));
      this.decoyKeySlot115 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-123, -123, 36, 57, -5, -36, -59, -30, 10, 118, -22, -70, -46, -93, 23, 59, -121, -9, -67, 103, -88, 115, 26, 12, -6, -64, -67, -101, -80, -74, -48, -124}, this.keyDeriverCipher));
      this.decoyKeySlot105 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{77, 117, -11, -44, -119, 97, 118, 21, 4, -74, -62, -99, -33, -107, -26, -81, 39, -74, 85, 123, -10, -29, 25, -96, -5, -56, 2, 18, -13, -33, 93, 37}, this.keyDeriverCipher));
      this.decoyKeySlot094 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-87, 18, 35, -92, 4, 96, -41, 111, 32, 115, 109, -71, 26, -42, -84, 59, -73, -81, 22, 79, -83, -71, -119, -75, 38, 37, 81, 115, -5, 113, -101, -8}, this.keyDeriverCipher));
      this.decoyKeySlot000 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-57, -79, -76, -66, 30, -78, 112, -47, 11, -2, 1, -127, -91, 8, 3, 77, -46, 82, 84, 74, 94, -116, -4, 14, 121, 111, 78, 22, 117, -10, -41, 1}, this.keyDeriverCipher));
      this.decoyKeySlot047 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-107, 108, -32, 42, 55, 88, 16, -34, 125, -87, -48, 103, -61, 86, -8, 77, -110, -74, -47, -86, -116, -83, -18, 64, 66, -20, -2, -39, -60, -53, 90, -128}, this.keyDeriverCipher));
      this.decoyKeySlot095 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{41, 51, 9, -61, -84, -18, 87, -30, -87, 21, -85, -114, 25, -110, -42, -121, -127, -72, 61, 34, -101, 43, -101, 18, -79, -122, 120, 117, 108, -6, 19, -68}, this.keyDeriverCipher));
      this.decoyKeySlot126 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{79, -27, -118, -55, 72, 74, 24, 27, 44, -112, -71, 61, -41, -56, 113, 89, -95, -90, 35, 112, 122, -116, 61, -109, -72, -122, -4, -110, -89, 81, -89, -65}, this.keyDeriverCipher));
      this.decoyKeySlot012 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{72, -45, -63, -18, -104, -57, 36, -14, 15, -55, 109, -124, -35, -92, -52, -60, 52, -28, 23, -118, -127, -42, -58, 37, 88, 65, 124, 84, -17, -24, 32, 23}, this.keyDeriverCipher));
      this.decoyKeySlot007 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{85, -80, -25, -12, 16, 79, 56, 37, 47, 13, -60, 32, -89, -105, 120, -124, -11, 72, 105, -15, 80, 82, -102, 31, -2, 58, 40, -97, 37, -43, -42, 38}, this.keyDeriverCipher));
      this.decoyKeySlot091 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-26, 10, 29, 60, 6, -126, -60, 125, 94, 113, -80, 3, -49, 56, 4, -35, -92, 47, 79, 117, -118, -44, 97, -96, -1, -113, -100, 126, -28, -80, -36, 34}, this.keyDeriverCipher));
      this.decoyKeySlot002 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{77, -29, -101, -51, -117, -126, -120, -37, 87, 121, -104, -9, 94, 17, 74, -64, -68, 44, 94, 44, 19, -1, -13, 79, 43, -24, -14, 34, 80, -23, -40, -26}, this.keyDeriverCipher));
      this.decoyKeySlot109 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-105, 100, 122, -35, -31, -95, 0, -110, 6, -30, 69, -108, -86, 95, -45, -41, -114, -88, 96, 36, 0, 39, -68, -87, -24, 55, 98, -62, -84, -54, 102, -119}, this.keyDeriverCipher));
      this.decoyKeySlot113 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-72, 5, -7, 64, -79, 84, -29, 19, -64, -19, 95, 106, -48, -37, 70, 22, 49, 51, 50, 13, 61, 103, -121, -4, -4, -127, -51, -3, -60, -37, -30, -100}, this.keyDeriverCipher));
      this.decoyKeySlot059 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-24, -67, -127, -10, -36, 95, 13, 61, 109, 37, -123, -123, -86, -85, -16, 41, -71, -123, 124, 108, -21, -90, 14, -90, -81, 116, 21, 80, 4, -60, -113, -99}, this.keyDeriverCipher));
      this.decoyKeySlot195 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-87, 117, -115, 77, -107, -112, 71, 36, 46, 74, 117, -73, -38, 94, 69, 26, -51, 72, 108, 18, -64, -26, -93, -95, 90, 125, -78, -45, 34, -126, 51, -46}, this.keyDeriverCipher));
      this.decoyKeySlot084 = (new Sha3Hash(Sha3Type.SHA3_256)).digest(AesCipherUtil.decrypt(bytes, new byte[]{-60, -125, 86, 122, -62, -26, -67, -87, -107, -112, -56, 79, 57, 93, -34, -71, -82, 35, -109, 72, 121, 51, -54, -30, -102, -8, -8, 65, 103, 42, -103, 43}, this.keyDeriverCipher));
   }

   public void decryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      this.implDecryptBlock(bytes, n, bytes2, n2);
   }

   private ByteBuf encryptBuffer(ByteBuf buf) {
      Object obj = null;
      Object obj2 = null;
      getControlFlowMarker();
      Object obj3 = null;
      byte b = 7;
      long l = 8164495558191749022L;
      l = 5592543772260421888L ^ (long)"욾ꁈ蘻\ud9bd吘␙竗藴염윇䚤謫豇ꕢޖ煷鿣\udaf4⭅蝌ꫳ\uf6dcᅎ".hashCode();
      obj = null;
      l = l - 1972970275617237395L ^ 3507957645232857224L;
      obj3 = new byte[buf.readableBytes()];
      l = l + -2197186958168536238L ^ -7553546193674691725L;
      buf.readBytes((byte[])obj3);
      l = l - -402686278986747018L ^ 5608463647594179404L;
      this.stageGuard.enterStage2();
      l = (l ^ -3775629597566545732L) - -4923139188630828297L;
      if (this.keyGuardSlotA.length >= 32) {
         l = l + 2958154165765381839L ^ 1268261690650445410L;
      } else {
         l = l + -6283420589169556174L ^ -6158191141836888510L;
         if (this.keyGuardSlotB.length < 32) {
            l = (l ^ 1275484461737682541L) + 2078030374327452126L;
            obj = Unpooled.copiedBuffer((byte[])obj3);
            l = (l ^ -5652038690328456133L) + 4725949417016245182L;
            l = l + -5659622428056595717L ^ -7033363141895953793L;
            return (ByteBuf)obj;
         }

         l = (l ^ -5681995233699421886L) + -8479093200376929153L;
      }

      this.initCipher(false, this.activeEncryptKey);
      l = (l ^ -2772406373733845834L) + 8894021655723652457L;
      obj2 = this.encrypt((byte[])obj3, 0, ((byte[])obj3).length);
      l = l - -8585179172074855459L ^ -4084323360565031545L;
      this.resetIv();
      l = l - -4014161316390399025L ^ -8090427294046834691L;
      obj = Unpooled.copiedBuffer((byte[])obj2);
      l = (l ^ 3967014895042439494L) - -2399357643077711368L;
      return (ByteBuf)obj;
   }

   private int decryptCbc(byte[] bytes, int n, int n2, byte[] bytes2, int n3) {
      int n4 = 0;
      int n5 = 0;
      int n6 = 8;
      long l = 6218939149525010727L;
      long l2 = 0L;
      getControlFlowMarker();
      l = -3022475051756937391L ^ (long)"夼륚婦耆락ਥ氜\u20f9䝈节ឍ섊ⶆ\u208f\uf671\uf21a㟴瀍㽗ᴝ".hashCode();
      l2 = -3661896344131446440L ^ (long)"ڮ휺訐쎋儳룖뫚깱﨤ꡋ㩢न캉춄\ue68e⌠榝Ề샜幞".hashCode();

      while (true) {
         n6 = (int)(l % 13L);
         if (n6 >= 6) {
            if (n6 <= 6) {
               return n2;
            }

            if (9 <= n6) {
               if (n6 <= 9) {
                  n4 = n + n2;
                  l = l - -1181451495989527676L ^ 809238685710213379L;
                  l2 = l2 + 1561990873122835873L ^ -5517467129420980742L;
               } else if (n6 >= 11) {
                  if (11 >= n6) {
                     System.arraycopy(bytes, n, null, 0, this.getBlockSize());
                     l = l - 8768487522428287087L ^ -3361397404799474140L;
                     l2 = (l2 ^ 2631039582000152632L) - -8526065029328236488L;
                  } else {
                     n += this.getBlockSize();
                     l = (l ^ 3132996780781612227L) - -4696892399780388068L;
                     l2 = (l2 ^ -2845806673677839089L) + 2106346858948045962L;
                  }
               } else {
                  n5 = 0;
                  l = l + -3883615392715428537L ^ -1797400115292033551L;
                  l2 = l2 + 3782639700086207840L ^ -351887892962901040L;
               }
            } else if (7 >= n6) {
               this.decryptBlock(bytes, n, null, 0);
               l = l + 5153258446796177394L ^ -4633096654677337888L;
               l2 = (l2 ^ 5079109714764807812L) + -3715780030622691767L;
            } else if (n >= n4) {
               l = l - 2145464719202749196L ^ 4194837171851593900L;
               l2 = l2 + 3147513951289504952L ^ 8864921617273131784L;
            } else {
               l = (l ^ -7491992806311215227L) - 3623514324633361992L;
               l2 = l2 - -393643933056808677L ^ -4642192896690197204L;
            }
         } else if (n6 >= 2) {
            if (n6 <= 2) {
               bytes2[n5 + n3] = (byte)(((byte[])null)[n5] ^ ((byte[])null)[n5]);
               l = l - -325310548569110484L ^ 8290798341412596711L;
               l2 = (l2 ^ -6705721834559690796L) - -8290640610031115522L;
            } else if (n6 >= 4) {
               if (4 >= n6) {
                  l = (l ^ -5246956242953235495L) + 8943995081645368918L;
                  l2 = (l2 ^ 5402053230400727524L) - -1752887558703023381L;
               } else {
                  n3 += this.getBlockSize();
                  l = (l ^ 7771880476716551771L) - -3197382824282914809L;
                  l2 = l2 + 2837064557191329047L ^ -841066690123382415L;
               }
            } else if (n5 >= this.getBlockSize()) {
               l = (l ^ -3002463642815888301L) - -7907451084542922320L;
               l2 = l2 - 3989604038626427096L ^ 14857485840197707L;
            } else {
               l = l + -8743686710018094568L ^ -682611920528272567L;
               l2 = l2 + -6400149640914623466L ^ -1362313144333188332L;
            }
         } else if (0 >= n6) {
            l = l + 3182929597145929081L ^ -6374650475060017477L;
            l2 = l2 - -8399693766991150377L ^ 8878370464121787443L;
         } else {
            n5++;
            l = l + 6619450703241131139L ^ -8000614602322330442L;
            l2 = (l2 ^ 958475980117697547L) - -5349368582303800102L;
         }
      }
   }

   public int unpad(byte[] bytes, int n, int n2) {
      byte b = 0;
      boolean flag = false;
      int n3 = 0;
      int n4 = 0;
      getControlFlowMarker();
      int n5 = 0;
      if (bytes != null && n2 != 0) {
         n4 = Math.addExact(n, n2);
         b = bytes[n4 - 1];
         n5 = b & 255;
         if (n5 >= 1 && n5 <= this.getBlockSize()) {
            n3 = n4 - n5;
            if (n3 < n) {
               return -1;
            } else {
               for (int i = n3; i < n4; i++) {
                  if (bytes[i] != b) {
                     return -1;
                  }
               }

               return n3;
            }
         } else {
            return -1;
         }
      } else {
         return 0;
      }
   }

   public void checkBlockAlignment(int n, int n2) {
      int[] controlFlowMarker2 = getControlFlowMarker();
      if (n % n2 != 0) {
         throw new ProviderException("Internal error in input buffering");
      }
   }

   public void padWithLen(byte[] bytes, int n, int n2) throws ShortBufferException {
      byte b = 0;
      int n3 = 0;
      int[] controlFlowMarker2 = getControlFlowMarker();
      if (bytes != null) {
         n3 = Math.addExact(n, n2);
         if (n3 <= bytes.length) {
            b = (byte)(n2 & 0xFF);
            Arrays.fill(bytes, n, n3, b);
         } else {
            throw new ShortBufferException("Buffer too small to hold padding");
         }
      }
   }

   public byte[] decrypt(byte[] bytes, int n, int n2) {
      int n3 = 0;
      Object obj = null;
      int[] controlFlowMarker2 = getControlFlowMarker();
      if (n2 > 0) {
         obj = new byte[n2];
         this.checkBlockAlignment(n2, this.getBlockSize());
         this.decryptCbc(bytes, n, n2, (byte[])obj, 0);
         n3 = this.unpad((byte[])obj, 0, bytes.length);
         return Arrays.copyOf((byte[])obj, n3);
      } else {
         return null;
      }
   }

   public void scatterSessionKey(byte[] bytes) {
      AesCrypto aesCrypto = new AesCrypto();
      this.activeEncryptKey = bytes;
      Sha3Hash sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes2 = new byte[]{
         -53, 59, 23, 97, -109, 55, -99, -117, -109, -118, 40, 26, -4, -73, -67, -29, -61, -40, 66, 45, 28, 28, -89, 79, -32, -31, 101, -58, 54, -123, -65, 86
      };
      byte[] bytes3 = AesCipherUtil.decrypt(bytes, bytes2, aesCrypto);
      byte[] bytes4 = sha3Hash.digest(bytes3);
      this.decoyKeySlot122 = bytes4;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes5 = new byte[]{
         -63, -27, -95, 109, 37, -95, -93, -63, 79, 51, -122, 82, 91, -120, 87, 26, -60, -128, -57, 94, 119, -102, -59, -124, -81, 61, 2, -119, 6, 12, 53, 67
      };
      byte[] bytes6 = AesCipherUtil.decrypt(bytes, bytes5, aesCrypto);
      byte[] bytes7 = sha3Hash.digest(bytes6);
      this.decoyKeySlot008 = bytes7;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes8 = new byte[]{
         -46, 89, 92, 10, -37, 111, -24, -96, 66, -14, -66, 90, 17, 69, 82, 11, -20, 85, -65, 111, 36, 35, 29, 44, -95, -19, -48, 8, -31, 14, 83, -38
      };
      byte[] bytes9 = AesCipherUtil.decrypt(bytes, bytes8, aesCrypto);
      byte[] bytes10 = sha3Hash.digest(bytes9);
      this.decoyKeySlot189 = bytes10;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes11 = new byte[]{
         -37, 0, 41, -33, -125, 9, -25, 11, 100, -38, 41, -86, -91, 94, -100, 9, 14, -95, 78, 89, 92, -58, -14, -52, -104, 47, 2, -17, 44, -98, -106, -91
      };
      byte[] bytes12 = AesCipherUtil.decrypt(bytes, bytes11, aesCrypto);
      byte[] bytes13 = sha3Hash.digest(bytes12);
      this.decoyKeySlot030 = bytes13;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes14 = new byte[]{
         116, -30, -11, -24, -31, 46, -92, 71, 12, -43, -126, 69, -128, -56, 10, 90, -104, 42, -112, -59, 108, -15, 39, 72, -45, 2, 31, -58, -98, 95, 15, 32
      };
      byte[] bytes15 = AesCipherUtil.decrypt(bytes, bytes14, aesCrypto);
      byte[] bytes16 = sha3Hash.digest(bytes15);
      this.decoyKeySlot146 = bytes16;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes17 = new byte[]{
         -127,
         -74,
         -30,
         -128,
         95,
         -46,
         -113,
         10,
         -90,
         -38,
         -92,
         -4,
         -8,
         -108,
         -72,
         88,
         -68,
         -90,
         -5,
         26,
         74,
         -24,
         85,
         -118,
         -55,
         -118,
         -64,
         -95,
         72,
         6,
         36,
         -77
      };
      byte[] bytes18 = AesCipherUtil.decrypt(bytes, bytes17, aesCrypto);
      byte[] bytes19 = sha3Hash.digest(bytes18);
      this.decoyKeySlot152 = bytes19;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes20 = new byte[]{
         -113,
         124,
         -111,
         -24,
         -4,
         -103,
         -18,
         -54,
         74,
         28,
         -86,
         31,
         -111,
         -126,
         67,
         -25,
         -68,
         -123,
         72,
         -58,
         83,
         44,
         85,
         -50,
         117,
         111,
         84,
         -3,
         35,
         -124,
         -119,
         84
      };
      byte[] bytes21 = AesCipherUtil.decrypt(bytes, bytes20, aesCrypto);
      byte[] bytes22 = sha3Hash.digest(bytes21);
      this.decoyKeySlot016 = bytes22;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes23 = new byte[]{
         -72, 50, -18, -27, 41, -101, 8, -72, -103, -63, -73, 27, 64, 4, -49, 77, -61, 51, -81, 77, 47, -13, -93, 7, 5, 49, -123, -62, 69, 110, -89, -15
      };
      byte[] bytes24 = AesCipherUtil.decrypt(bytes, bytes23, aesCrypto);
      byte[] bytes25 = sha3Hash.digest(bytes24);
      this.decoyKeySlot081 = bytes25;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes26 = new byte[]{
         -70, 32, 88, -57, 21, -30, 27, -58, 94, 110, -2, -36, 84, -47, 49, -108, 127, 87, 5, -16, 123, 93, 121, -58, -88, 24, 27, -5, 108, 19, 10, -79
      };
      byte[] bytes27 = AesCipherUtil.decrypt(bytes, bytes26, aesCrypto);
      byte[] bytes28 = sha3Hash.digest(bytes27);
      this.decoyKeySlot144 = bytes28;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes29 = new byte[]{
         -119, 41, -84, 4, -104, 36, -44, -59, -88, -91, 54, 26, 109, -28, -125, -113, 61, 70, -40, -40, -25, 25, -121, 2, -16, -114, -61, 68, 93, -31, 7, -3
      };
      byte[] bytes30 = AesCipherUtil.decrypt(bytes, bytes29, aesCrypto);
      byte[] bytes31 = sha3Hash.digest(bytes30);
      this.decoyKeySlot024 = bytes31;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes32 = new byte[]{
         90, 65, -102, -37, 116, 55, -72, 71, -63, 45, -67, -87, 124, 118, 58, -6, 121, 84, 121, -52, -120, 48, -5, 25, -43, 65, -103, 7, 51, 105, -67, 110
      };
      byte[] bytes33 = AesCipherUtil.decrypt(bytes, bytes32, aesCrypto);
      byte[] bytes34 = sha3Hash.digest(bytes33);
      this.decoyKeySlot077 = bytes34;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes35 = new byte[]{
         111,
         -113,
         -93,
         -44,
         -40,
         -65,
         -124,
         -63,
         -53,
         63,
         -83,
         102,
         -1,
         45,
         -23,
         50,
         -56,
         98,
         -12,
         -2,
         89,
         39,
         31,
         -105,
         -74,
         -69,
         127,
         -99,
         48,
         -10,
         79,
         -127
      };
      byte[] bytes36 = AesCipherUtil.decrypt(bytes, bytes35, aesCrypto);
      byte[] bytes37 = sha3Hash.digest(bytes36);
      this.decoyKeySlot140 = bytes37;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes38 = new byte[]{
         66, -59, -67, -95, -50, 29, 78, 93, 103, 78, -24, -53, -44, 99, 11, -77, 56, -77, 110, 92, -33, 83, 71, 76, 94, -72, 5, -18, 104, 58, -39, -74
      };
      byte[] bytes39 = AesCipherUtil.decrypt(bytes, bytes38, aesCrypto);
      byte[] bytes40 = sha3Hash.digest(bytes39);
      this.decoyKeySlot014 = bytes40;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes41 = new byte[]{
         103, 76, 30, -9, -62, 25, 55, -104, -125, 12, 94, 32, -19, -115, 22, -96, 110, 114, -119, 104, -42, -48, 112, 109, 7, -48, 78, -24, 32, 105, -73, 31
      };
      byte[] bytes42 = AesCipherUtil.decrypt(bytes, bytes41, aesCrypto);
      byte[] bytes43 = sha3Hash.digest(bytes42);
      this.decoyKeySlot129 = bytes43;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes44 = new byte[]{
         65, 9, 15, -118, 53, 44, -78, -72, -13, 99, 119, -65, 109, -84, -32, -29, -98, 78, -58, -105, 51, 122, 56, 56, -47, 3, 64, 3, 105, -91, 116, 77
      };
      byte[] bytes45 = AesCipherUtil.decrypt(bytes, bytes44, aesCrypto);
      byte[] bytes46 = sha3Hash.digest(bytes45);
      this.decoyKeySlot134 = bytes46;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes47 = new byte[]{
         76, -37, 98, -12, 103, -49, -63, -65, 76, 18, 19, -68, 78, 52, -128, -85, -92, -61, -98, -113, -79, 121, 7, 121, -81, -57, 68, 39, -124, 76, -3, -65
      };
      byte[] bytes48 = AesCipherUtil.decrypt(bytes, bytes47, aesCrypto);
      byte[] bytes49 = sha3Hash.digest(bytes48);
      this.decoyKeySlot048 = bytes49;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes50 = new byte[]{
         78, 0, 24, 0, 62, 117, 107, 55, 76, -24, 126, 50, -27, -76, -118, 76, 119, -86, 110, -103, -88, 121, 113, -106, 103, 74, -93, -20, -87, -63, -87, -9
      };
      byte[] bytes51 = AesCipherUtil.decrypt(bytes, bytes50, aesCrypto);
      byte[] bytes52 = sha3Hash.digest(bytes51);
      this.decoyKeySlot147 = bytes52;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes53 = new byte[]{
         17, -37, -93, -80, 62, 88, 104, -112, 119, -104, 127, 59, 83, -102, -45, 29, -78, -8, -128, -94, 85, -51, -124, 0, -117, 4, 86, 54, 96, -82, -1, -91
      };
      byte[] bytes54 = AesCipherUtil.decrypt(bytes, bytes53, aesCrypto);
      byte[] bytes55 = sha3Hash.digest(bytes54);
      this.decoyKeySlot018 = bytes55;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes56 = new byte[]{
         81,
         -93,
         -81,
         -37,
         -44,
         22,
         -61,
         -55,
         -14,
         73,
         -65,
         -92,
         66,
         127,
         -104,
         -43,
         -67,
         -29,
         -26,
         -107,
         -110,
         -31,
         -97,
         -61,
         -68,
         61,
         -44,
         -106,
         34,
         -30,
         104,
         -22
      };
      byte[] bytes57 = AesCipherUtil.decrypt(bytes, bytes56, aesCrypto);
      byte[] bytes58 = sha3Hash.digest(bytes57);
      this.decoyKeySlot133 = bytes58;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes59 = new byte[]{
         -51, -63, -37, 26, 43, 94, -56, 87, 90, -104, -22, -23, -120, -60, 33, -73, 34, -56, 37, 96, -126, -90, 43, -1, 33, 36, -47, -102, 81, 56, -83, 86
      };
      byte[] bytes60 = AesCipherUtil.decrypt(bytes, bytes59, aesCrypto);
      byte[] bytes61 = sha3Hash.digest(bytes60);
      this.decoyKeySlot046 = bytes61;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes62 = new byte[]{
         -44,
         -128,
         -1,
         -69,
         32,
         110,
         -64,
         106,
         -117,
         0,
         -46,
         53,
         88,
         -110,
         101,
         -84,
         38,
         35,
         62,
         -46,
         -40,
         23,
         119,
         -124,
         -40,
         92,
         116,
         118,
         120,
         101,
         -118,
         126
      };
      byte[] bytes63 = AesCipherUtil.decrypt(bytes, bytes62, aesCrypto);
      byte[] bytes64 = sha3Hash.digest(bytes63);
      this.decoyKeySlot193 = bytes64;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes65 = new byte[]{
         51, -36, 20, 38, -105, 68, -110, -104, -40, 79, 78, -4, -58, -91, -70, 104, 108, 22, 60, 126, 61, -73, 69, -67, -120, -108, 2, 34, 13, 95, 68, 55
      };
      byte[] bytes66 = AesCipherUtil.decrypt(bytes, bytes65, aesCrypto);
      byte[] bytes67 = sha3Hash.digest(bytes66);
      this.decoyKeySlot052 = bytes67;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes68 = new byte[]{
         11, -114, -20, -106, -85, -113, 92, 28, -49, -4, 66, 48, 116, -84, 120, 81, 121, -95, -124, -9, 57, 18, -31, 2, 60, -30, 3, -95, 71, 106, -126, 63
      };
      byte[] bytes69 = AesCipherUtil.decrypt(bytes, bytes68, aesCrypto);
      byte[] bytes70 = sha3Hash.digest(bytes69);
      this.decoyKeySlot178 = bytes70;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes71 = new byte[]{
         60, 81, 82, -80, 115, -3, 66, 63, 116, 58, 4, -11, -56, 105, 8, -111, 31, -118, -87, 93, -112, 1, -87, 127, -54, 22, 95, 124, 112, -114, -19, -16
      };
      byte[] bytes72 = AesCipherUtil.decrypt(bytes, bytes71, aesCrypto);
      byte[] bytes73 = sha3Hash.digest(bytes72);
      this.decoyKeySlot110 = bytes73;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes74 = new byte[]{
         9, -61, -36, 112, -128, 98, -35, -83, -71, -72, -15, 60, -101, 85, -104, 86, -4, 68, -4, -53, 82, 13, 24, 75, 32, 75, 90, -99, 42, 85, -99, -15
      };
      byte[] bytes75 = AesCipherUtil.decrypt(bytes, bytes74, aesCrypto);
      byte[] bytes76 = sha3Hash.digest(bytes75);
      this.decoyKeySlot176 = bytes76;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes77 = new byte[]{
         20, 81, 66, 54, 109, -124, -7, 20, 98, 0, -52, 103, -51, -94, 121, -72, 89, 19, -125, 62, 84, 118, -21, -61, -115, -52, -96, -98, 106, 99, 25, -45
      };
      byte[] bytes78 = AesCipherUtil.decrypt(bytes, bytes77, aesCrypto);
      byte[] bytes79 = sha3Hash.digest(bytes78);
      this.decoyKeySlot013 = bytes79;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes80 = new byte[]{
         71,
         126,
         -109,
         -116,
         -121,
         14,
         -109,
         -82,
         72,
         91,
         -98,
         -24,
         -47,
         -127,
         110,
         -45,
         -67,
         21,
         -19,
         107,
         117,
         -77,
         115,
         -108,
         -71,
         -37,
         -12,
         88,
         62,
         82,
         -9,
         -4
      };
      byte[] bytes81 = AesCipherUtil.decrypt(bytes, bytes80, aesCrypto);
      byte[] bytes82 = sha3Hash.digest(bytes81);
      this.decoyKeySlot159 = bytes82;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes83 = new byte[]{
         23, -77, 111, -31, 92, -104, 51, -11, 65, 83, 118, -72, 17, 38, 60, -5, -28, -124, -1, -85, -85, 92, 115, -61, -99, 36, -7, 104, -89, 101, -106, -128
      };
      byte[] bytes84 = AesCipherUtil.decrypt(bytes, bytes83, aesCrypto);
      byte[] bytes85 = sha3Hash.digest(bytes84);
      this.decoyKeySlot005 = bytes85;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes86 = new byte[]{
         -66, 50, -23, 21, -86, -56, 114, 108, 63, -92, 8, 68, -114, 57, 59, 37, 103, 96, -98, 82, -22, -3, 22, 109, 1, -65, -11, 1, 90, -66, -34, 98
      };
      byte[] bytes87 = AesCipherUtil.decrypt(bytes, bytes86, aesCrypto);
      byte[] bytes88 = sha3Hash.digest(bytes87);
      this.decoyKeySlot174 = bytes88;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes89 = new byte[]{
         96, -119, -3, 56, 67, -57, 110, -73, 125, -104, -84, 46, 13, 56, 89, 72, -34, 60, 109, 99, -119, -72, 27, 122, -34, -85, -114, 61, 80, 10, 82, -29
      };
      byte[] bytes90 = AesCipherUtil.decrypt(bytes, bytes89, aesCrypto);
      byte[] bytes91 = sha3Hash.digest(bytes90);
      this.decoyKeySlot089 = bytes91;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes92 = new byte[]{
         114, -13, -67, 85, 35, -57, 83, -2, -94, 48, -55, 111, 97, 50, -1, 125, -15, -123, 14, -77, -9, 87, -72, -120, 97, 34, -18, 3, 11, -117, -122, 112
      };
      byte[] bytes93 = AesCipherUtil.decrypt(bytes, bytes92, aesCrypto);
      byte[] bytes94 = sha3Hash.digest(bytes93);
      this.decoyKeySlot143 = bytes94;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes95 = new byte[]{
         -95, 53, -52, 43, -31, 114, -119, 55, -48, -4, -27, 127, -17, -54, 117, 121, 1, 116, 76, -45, -88, 97, 78, -47, 27, 43, -26, 78, 114, -47, 30, -66
      };
      byte[] bytes96 = AesCipherUtil.decrypt(bytes, bytes95, aesCrypto);
      byte[] bytes97 = sha3Hash.digest(bytes96);
      this.decoyKeySlot099 = bytes97;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes98 = new byte[]{
         97,
         87,
         -95,
         -108,
         -50,
         -21,
         -18,
         -115,
         12,
         -16,
         -32,
         30,
         -75,
         121,
         -28,
         -111,
         98,
         -23,
         -15,
         112,
         -112,
         39,
         22,
         -89,
         -3,
         -48,
         4,
         -115,
         -83,
         -101,
         12,
         34
      };
      byte[] bytes99 = AesCipherUtil.decrypt(bytes, bytes98, aesCrypto);
      byte[] bytes100 = sha3Hash.digest(bytes99);
      this.decoyKeySlot042 = bytes100;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes101 = new byte[]{
         -33,
         -3,
         112,
         -83,
         -66,
         -33,
         -87,
         -81,
         -37,
         -26,
         28,
         -97,
         -80,
         -39,
         -79,
         108,
         103,
         -14,
         119,
         -53,
         122,
         7,
         -3,
         97,
         -61,
         16,
         61,
         -118,
         80,
         -122,
         125,
         -10
      };
      byte[] bytes102 = AesCipherUtil.decrypt(bytes, bytes101, aesCrypto);
      byte[] bytes103 = sha3Hash.digest(bytes102);
      this.decoyKeySlot137 = bytes103;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes104 = new byte[]{
         -85,
         108,
         -112,
         88,
         -119,
         51,
         -28,
         -4,
         123,
         -99,
         75,
         33,
         36,
         -119,
         118,
         -103,
         -42,
         67,
         124,
         71,
         -116,
         -98,
         67,
         -100,
         -106,
         33,
         -25,
         -28,
         -17,
         -121,
         94,
         97
      };
      byte[] bytes105 = AesCipherUtil.decrypt(bytes, bytes104, aesCrypto);
      byte[] bytes106 = sha3Hash.digest(bytes105);
      this.decoyKeySlot127 = bytes106;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes107 = new byte[]{
         -50,
         34,
         -71,
         98,
         -116,
         -99,
         23,
         -85,
         75,
         -126,
         9,
         -125,
         96,
         20,
         26,
         -125,
         -83,
         18,
         107,
         -83,
         -23,
         -52,
         36,
         99,
         -25,
         -121,
         -112,
         -84,
         119,
         -40,
         31,
         118
      };
      byte[] bytes108 = AesCipherUtil.decrypt(bytes, bytes107, aesCrypto);
      byte[] bytes109 = sha3Hash.digest(bytes108);
      this.decoyKeySlot136 = bytes109;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes110 = new byte[]{
         -38, 80, -39, 76, 49, 30, 110, 33, 89, -124, -31, 10, 127, 43, 106, 118, 93, 15, 16, 79, -7, -64, -85, -27, -95, -84, -93, 123, 15, -52, -77, 82
      };
      byte[] bytes111 = AesCipherUtil.decrypt(bytes, bytes110, aesCrypto);
      byte[] bytes112 = sha3Hash.digest(bytes111);
      this.decoyKeySlot086 = bytes112;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes113 = new byte[]{
         10, 75, -51, -41, 111, 56, -100, -78, -97, -26, -61, -72, 113, -78, -42, 71, 93, 17, 45, -57, 68, -25, 117, 75, -87, 72, -41, 97, -19, -77, -55, 47
      };
      byte[] bytes114 = AesCipherUtil.decrypt(bytes, bytes113, aesCrypto);
      byte[] bytes115 = sha3Hash.digest(bytes114);
      this.decoyKeySlot117 = bytes115;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes116 = new byte[]{
         -66, 34, 0, 76, 10, -125, 19, -68, 39, -114, 0, -83, -4, -93, -126, -86, 88, 85, 48, -41, 119, 96, 34, -126, 6, 80, 5, -67, 40, 39, -53, -25
      };
      byte[] bytes117 = AesCipherUtil.decrypt(bytes, bytes116, aesCrypto);
      byte[] bytes118 = sha3Hash.digest(bytes117);
      this.decoyKeySlot009 = bytes118;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes119 = new byte[]{
         52, 62, 99, -113, 32, 59, -99, 109, -55, -99, -4, 99, 28, -65, 102, -90, 26, 103, 47, 124, -95, -27, 16, 74, -1, -111, -12, 17, -74, 99, -115, 66
      };
      byte[] bytes120 = AesCipherUtil.decrypt(bytes, bytes119, aesCrypto);
      byte[] bytes121 = sha3Hash.digest(bytes120);
      this.decoyKeySlot079 = bytes121;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes122 = new byte[]{
         106, -90, 80, 97, 63, 6, -117, 107, -44, -84, 29, -117, 35, 17, 22, -40, -36, -26, -92, -55, -16, -107, 25, 69, -125, 87, -80, 53, -29, 48, -125, 80
      };
      byte[] bytes123 = AesCipherUtil.decrypt(bytes, bytes122, aesCrypto);
      byte[] bytes124 = sha3Hash.digest(bytes123);
      this.decoyKeySlot055 = bytes124;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes125 = new byte[]{
         -79,
         15,
         -18,
         -98,
         -127,
         119,
         85,
         -109,
         48,
         -115,
         -115,
         -42,
         76,
         -121,
         94,
         -71,
         -86,
         -68,
         8,
         107,
         39,
         -34,
         -119,
         -63,
         22,
         -116,
         76,
         -21,
         102,
         68,
         -122,
         104
      };
      byte[] bytes126 = AesCipherUtil.decrypt(bytes, bytes125, aesCrypto);
      byte[] bytes127 = sha3Hash.digest(bytes126);
      this.decoyKeySlot037 = bytes127;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes128 = new byte[]{
         -93, 125, -54, -78, -25, 64, -41, -4, 107, -10, -9, -113, -60, -125, -96, 24, 52, 6, -47, -73, 48, -35, 84, 68, -80, -99, -80, 17, -53, 116, 120, -98
      };
      byte[] bytes129 = AesCipherUtil.decrypt(bytes, bytes128, aesCrypto);
      byte[] bytes130 = sha3Hash.digest(bytes129);
      this.decoyKeySlot078 = bytes130;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes131 = new byte[]{
         52, -20, 32, -103, -52, 11, 110, -60, 123, -37, -78, 24, 58, 97, 12, -11, -119, 124, -38, -31, 0, 88, 119, 31, -49, -99, -35, 34, 96, -24, -30, 85
      };
      byte[] bytes132 = AesCipherUtil.decrypt(bytes, bytes131, aesCrypto);
      byte[] bytes133 = sha3Hash.digest(bytes132);
      this.decoyKeySlot175 = bytes133;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes134 = new byte[]{
         -68, 74, -52, 51, -66, 92, 52, 26, 20, -18, 76, 120, 33, 41, -14, 92, 95, -111, -37, 86, -15, -59, 28, -9, -80, -12, -22, 114, -82, 57, -48, 42
      };
      byte[] bytes135 = AesCipherUtil.decrypt(bytes, bytes134, aesCrypto);
      byte[] bytes136 = sha3Hash.digest(bytes135);
      this.decoyKeySlot093 = bytes136;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes137 = new byte[]{
         71, 19, 47, 47, -91, 113, 29, -74, -3, 14, 30, 124, 89, 105, -114, 82, -54, 100, -45, 60, -55, -17, -108, -16, 126, -30, -16, 27, 121, -12, -81, 5
      };
      byte[] bytes138 = AesCipherUtil.decrypt(bytes, bytes137, aesCrypto);
      byte[] bytes139 = sha3Hash.digest(bytes138);
      this.decoyKeySlot025 = bytes139;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes140 = new byte[]{
         -111,
         -117,
         112,
         -85,
         -101,
         -21,
         46,
         -41,
         85,
         -127,
         -48,
         -95,
         1,
         -79,
         -126,
         -109,
         -110,
         126,
         125,
         -11,
         -5,
         -76,
         111,
         80,
         -8,
         -90,
         122,
         -88,
         -78,
         -59,
         -10,
         -30
      };
      byte[] bytes141 = AesCipherUtil.decrypt(bytes, bytes140, aesCrypto);
      byte[] bytes142 = sha3Hash.digest(bytes141);
      this.decoyKeySlot112 = bytes142;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes143 = new byte[]{
         51, -67, -36, -82, -86, 96, 58, -93, 49, 18, 104, 39, -19, 63, -17, -42, -53, -75, 9, 81, 49, 90, -51, 34, 46, -24, -5, -79, -123, 34, -57, 98
      };
      byte[] bytes144 = AesCipherUtil.decrypt(bytes, bytes143, aesCrypto);
      byte[] bytes145 = sha3Hash.digest(bytes144);
      this.decoyKeySlot011 = bytes145;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes146 = new byte[]{
         -123, -63, -121, -107, 9, 36, -92, -122, 113, 97, -73, 113, 127, 21, -114, 44, 93, -88, 122, 126, 13, -92, 96, 53, 46, 72, 108, 70, -40, 31, 107, 124
      };
      byte[] bytes147 = AesCipherUtil.decrypt(bytes, bytes146, aesCrypto);
      byte[] bytes148 = sha3Hash.digest(bytes147);
      this.decoyKeySlot074 = bytes148;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes149 = new byte[]{
         77,
         -61,
         -122,
         88,
         -51,
         16,
         -55,
         14,
         11,
         61,
         72,
         -31,
         -60,
         -21,
         -64,
         -59,
         89,
         54,
         -37,
         -27,
         -93,
         59,
         16,
         -106,
         -15,
         -27,
         -68,
         -104,
         105,
         -121,
         -69,
         -39
      };
      byte[] bytes150 = AesCipherUtil.decrypt(bytes, bytes149, aesCrypto);
      byte[] bytes151 = sha3Hash.digest(bytes150);
      this.decoyKeySlot163 = bytes151;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes152 = new byte[]{
         -101,
         90,
         80,
         -14,
         -7,
         -110,
         -92,
         -26,
         103,
         -16,
         -125,
         -52,
         74,
         119,
         94,
         -93,
         -35,
         19,
         -34,
         -122,
         97,
         -7,
         -36,
         64,
         91,
         -14,
         124,
         56,
         -58,
         -19,
         112,
         -87
      };
      byte[] bytes153 = AesCipherUtil.decrypt(bytes, bytes152, aesCrypto);
      byte[] bytes154 = sha3Hash.digest(bytes153);
      this.decoyKeySlot169 = bytes154;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes155 = new byte[]{
         120,
         115,
         98,
         21,
         -98,
         -21,
         110,
         109,
         125,
         -112,
         98,
         4,
         -112,
         82,
         -43,
         75,
         -123,
         119,
         55,
         -106,
         82,
         8,
         120,
         -123,
         -70,
         -97,
         95,
         -114,
         -100,
         -11,
         -109,
         -106
      };
      byte[] bytes156 = AesCipherUtil.decrypt(bytes, bytes155, aesCrypto);
      byte[] bytes157 = sha3Hash.digest(bytes156);
      this.decoyKeySlot103 = bytes157;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes158 = new byte[]{
         21, 38, -10, 92, -90, 10, 15, 53, 63, 8, 69, 43, 61, 74, 15, 122, -120, 86, 119, -24, -81, -123, -71, -26, 41, -41, 47, -101, 54, 94, 115, 74
      };
      byte[] bytes159 = AesCipherUtil.decrypt(bytes, bytes158, aesCrypto);
      byte[] bytes160 = sha3Hash.digest(bytes159);
      this.decoyKeySlot031 = bytes160;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes161 = new byte[]{
         -112,
         39,
         88,
         -91,
         -57,
         22,
         62,
         -79,
         40,
         -58,
         -30,
         -65,
         -80,
         -121,
         109,
         -30,
         -128,
         -36,
         -49,
         78,
         -29,
         -44,
         -117,
         62,
         -22,
         48,
         34,
         -117,
         -76,
         -60,
         -28,
         -44
      };
      byte[] bytes162 = AesCipherUtil.decrypt(bytes, bytes161, aesCrypto);
      byte[] bytes163 = sha3Hash.digest(bytes162);
      this.decoyKeySlot072 = bytes163;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes164 = new byte[]{
         56, 67, -117, -84, -26, 46, -78, -28, 55, 89, 47, 25, -82, -80, -74, -117, 111, -22, -94, -37, -97, -13, 78, 109, 9, -119, 57, -83, 86, -103, 74, 125
      };
      byte[] bytes165 = AesCipherUtil.decrypt(bytes, bytes164, aesCrypto);
      byte[] bytes166 = sha3Hash.digest(bytes165);
      this.decoyKeySlot132 = bytes166;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes167 = new byte[]{
         106, 91, 40, -112, 6, 99, 95, -84, 23, -53, 101, -73, 69, 25, -26, 84, 53, 85, -20, -3, -97, 118, -123, 103, -55, -36, 82, -75, 98, -48, -65, -16
      };
      byte[] bytes168 = AesCipherUtil.decrypt(bytes, bytes167, aesCrypto);
      byte[] bytes169 = sha3Hash.digest(bytes168);
      this.decoyKeySlot001 = bytes169;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes170 = new byte[]{
         120, 88, 7, 47, 32, 110, 15, 31, 87, -36, 79, -43, -37, -39, -27, -22, -52, 5, 39, 20, 114, -25, -28, 60, -95, -84, -77, 41, -123, -53, -95, -116
      };
      byte[] bytes171 = AesCipherUtil.decrypt(bytes, bytes170, aesCrypto);
      byte[] bytes172 = sha3Hash.digest(bytes171);
      this.decoyKeySlot015 = bytes172;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes173 = new byte[]{
         4, 124, 116, 63, 3, -15, 11, 20, -20, -86, 123, -17, 11, 31, -88, 89, 103, 48, 29, 39, -74, 65, -56, -70, -98, 80, -95, 59, 74, 83, -112, 73
      };
      byte[] bytes174 = AesCipherUtil.decrypt(bytes, bytes173, aesCrypto);
      byte[] bytes175 = sha3Hash.digest(bytes174);
      this.decoyKeySlot158 = bytes175;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes176 = new byte[]{
         8, -12, 104, -26, 121, 63, -14, -37, 90, 43, 76, -120, 31, 94, 27, -77, -84, 15, -94, -126, -92, 6, 99, 92, 99, 126, -14, 94, 15, 24, 72, -77
      };
      byte[] bytes177 = AesCipherUtil.decrypt(bytes, bytes176, aesCrypto);
      byte[] bytes178 = sha3Hash.digest(bytes177);
      this.decoyKeySlot076 = bytes178;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes179 = new byte[]{
         56, 30, 29, -103, -114, -54, -74, -95, -82, -4, -108, 94, -114, 14, -98, -106, 26, 108, 101, 117, 47, 5, -33, -80, 48, 112, 3, 43, 77, -80, 126, -117
      };
      byte[] bytes180 = AesCipherUtil.decrypt(bytes, bytes179, aesCrypto);
      byte[] bytes181 = sha3Hash.digest(bytes180);
      this.decoyKeySlot003 = bytes181;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes182 = new byte[]{
         -60, 39, 108, 88, -85, -25, 69, 6, -37, 27, 37, 47, -66, 90, 49, -68, 112, 114, -50, -2, 25, -29, 7, -106, 64, 61, -94, 68, 29, 58, 40, -85
      };
      byte[] bytes183 = AesCipherUtil.decrypt(bytes, bytes182, aesCrypto);
      byte[] bytes184 = sha3Hash.digest(bytes183);
      this.decoyKeySlot026 = bytes184;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes185 = new byte[]{
         -55, -4, -108, 44, 57, -18, 68, 81, -35, 11, -125, 50, 56, 4, 109, -23, -116, 27, -60, 80, -11, -9, 91, -51, 91, 90, -34, -92, 110, 12, -106, 122
      };
      byte[] bytes186 = AesCipherUtil.decrypt(bytes, bytes185, aesCrypto);
      byte[] bytes187 = sha3Hash.digest(bytes186);
      this.decoyKeySlot171 = bytes187;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes188 = new byte[]{
         120, 30, -64, 13, 81, -106, 80, 31, 79, -101, 56, -66, -11, 120, 78, 3, 5, -46, 9, 24, 47, -99, -95, -58, 58, 99, 84, 36, -88, -68, 109, -26
      };
      byte[] bytes189 = AesCipherUtil.decrypt(bytes, bytes188, aesCrypto);
      byte[] bytes190 = sha3Hash.digest(bytes189);
      this.decoyKeySlot006 = bytes190;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes191 = new byte[]{
         97, 26, -87, -22, 18, 35, 124, 96, 25, 5, 127, -5, 94, -48, -67, -113, -121, 97, -90, -41, -85, 3, 58, -4, -111, -23, 103, -64, -52, 98, -20, 97
      };
      byte[] bytes192 = AesCipherUtil.decrypt(bytes, bytes191, aesCrypto);
      byte[] bytes193 = sha3Hash.digest(bytes192);
      this.decoyKeySlot035 = bytes193;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes194 = new byte[]{
         61, -121, 107, -46, -35, -14, -13, 6, -6, 48, 61, 53, 119, -20, 61, -88, -58, -92, 44, -34, 48, 32, -118, 66, 95, -76, 66, -65, -54, -78, -24, -96
      };
      byte[] bytes195 = AesCipherUtil.decrypt(bytes, bytes194, aesCrypto);
      byte[] bytes196 = sha3Hash.digest(bytes195);
      this.decoyKeySlot185 = bytes196;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes197 = new byte[]{
         -97, -28, -10, -5, -109, -74, 32, 76, 82, -99, -21, 114, -95, -107, -111, 100, 5, 127, -47, 64, -105, -8, 49, 25, -44, 50, 125, 126, 58, -56, 88, 111
      };
      byte[] bytes198 = AesCipherUtil.decrypt(bytes, bytes197, aesCrypto);
      byte[] bytes199 = sha3Hash.digest(bytes198);
      this.decoyKeySlot080 = bytes199;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes200 = new byte[]{
         39, -64, 72, 18, 54, 37, 44, -124, 9, -54, -51, 96, -49, 48, -54, -104, -55, -109, -54, 117, -56, -21, -59, 84, 67, -112, -2, -121, -1, -66, -35, 8
      };
      byte[] bytes201 = AesCipherUtil.decrypt(bytes, bytes200, aesCrypto);
      byte[] bytes202 = sha3Hash.digest(bytes201);
      this.decoyKeySlot097 = bytes202;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes203 = new byte[]{
         23, 67, 81, 95, 114, -93, -7, 77, 3, -15, -80, 123, -37, 61, 116, -40, 52, 63, -96, -107, -25, -15, 14, -72, -76, 111, -118, 81, 105, -75, -111, 103
      };
      byte[] bytes204 = AesCipherUtil.decrypt(bytes, bytes203, aesCrypto);
      byte[] bytes205 = sha3Hash.digest(bytes204);
      this.decoyKeySlot180 = bytes205;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes206 = new byte[]{
         16,
         -68,
         -73,
         -44,
         -113,
         -87,
         -93,
         107,
         121,
         -89,
         -99,
         -31,
         -97,
         38,
         114,
         74,
         101,
         4,
         -122,
         -9,
         4,
         -15,
         -74,
         -47,
         95,
         -103,
         -29,
         -18,
         121,
         -61,
         -99,
         -58
      };
      byte[] bytes207 = AesCipherUtil.decrypt(bytes, bytes206, aesCrypto);
      byte[] bytes208 = sha3Hash.digest(bytes207);
      this.decoyKeySlot116 = bytes208;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes209 = new byte[]{
         47, 94, 47, 17, -120, 93, 15, 126, 45, -120, 8, -86, 7, 12, -82, -50, -43, 82, 1, 77, -117, 81, 121, 63, 23, -47, -9, 122, 9, 94, -1, 62
      };
      byte[] bytes210 = AesCipherUtil.decrypt(bytes, bytes209, aesCrypto);
      byte[] bytes211 = sha3Hash.digest(bytes210);
      this.decoyKeySlot149 = bytes211;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes212 = new byte[]{
         -44, -24, 5, -25, 13, -45, 13, 122, 40, -128, 83, -16, 0, -114, 28, -114, 84, -44, 99, -6, 35, 65, 21, 64, -33, 114, 113, -118, 83, 8, -126, 89
      };
      byte[] bytes213 = AesCipherUtil.decrypt(bytes, bytes212, aesCrypto);
      byte[] bytes214 = sha3Hash.digest(bytes213);
      this.decoyKeySlot114 = bytes214;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes215 = new byte[]{
         -62, 8, -22, -8, 118, 19, 126, 68, 2, 11, -74, -93, -1, -35, -67, 48, -125, 22, -11, 115, 56, 94, -30, 115, -35, 81, -54, -76, 112, -75, 96, 42
      };
      byte[] bytes216 = AesCipherUtil.decrypt(bytes, bytes215, aesCrypto);
      byte[] bytes217 = sha3Hash.digest(bytes216);
      this.decoyKeySlot040 = bytes217;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes218 = new byte[]{
         -30,
         118,
         -7,
         -36,
         -88,
         117,
         -18,
         65,
         86,
         23,
         -93,
         -63,
         122,
         3,
         -77,
         -32,
         -103,
         -68,
         -106,
         -65,
         69,
         108,
         -125,
         -20,
         -44,
         64,
         93,
         98,
         -106,
         98,
         35,
         -109
      };
      byte[] bytes219 = AesCipherUtil.decrypt(bytes, bytes218, aesCrypto);
      byte[] bytes220 = sha3Hash.digest(bytes219);
      this.decoyKeySlot019 = bytes220;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes221 = new byte[]{
         3, -67, 41, -57, -73, -109, 123, 126, -88, -79, -116, 48, 37, -6, -124, 66, -50, 120, 34, 57, 76, -47, 36, -109, -46, 1, -53, -80, 64, -47, 14, 15
      };
      byte[] bytes222 = AesCipherUtil.decrypt(bytes, bytes221, aesCrypto);
      byte[] bytes223 = sha3Hash.digest(bytes222);
      this.decoyKeySlot034 = bytes223;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes224 = new byte[]{
         10, -93, 69, 109, -52, -104, -64, 120, 19, -33, 23, 3, -40, 107, -34, -66, 72, 50, -13, -35, -30, 93, 2, 111, -16, -67, -127, 30, 17, -104, -106, 86
      };
      byte[] bytes225 = AesCipherUtil.decrypt(bytes, bytes224, aesCrypto);
      byte[] bytes226 = sha3Hash.digest(bytes225);
      this.decoyKeySlot041 = bytes226;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes227 = new byte[]{
         121, 82, -105, -109, 36, 124, -73, -68, -29, -63, -89, 87, 110, -77, 7, -47, 4, 29, 96, -41, 52, -24, 29, 13, 33, -6, -10, 3, 99, 38, -125, 17
      };
      byte[] bytes228 = AesCipherUtil.decrypt(bytes, bytes227, aesCrypto);
      byte[] bytes229 = sha3Hash.digest(bytes228);
      this.decoyKeySlot191 = bytes229;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes230 = new byte[]{
         -46, -39, -27, -2, 94, 84, -32, -78, -90, 8, -2, 81, 124, -127, 122, 65, -22, -20, 87, -49, -84, 21, 36, -54, -69, 90, -127, 127, -115, 104, 77, -60
      };
      byte[] bytes231 = AesCipherUtil.decrypt(bytes, bytes230, aesCrypto);
      byte[] bytes232 = sha3Hash.digest(bytes231);
      this.decoyKeySlot184 = bytes232;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes233 = new byte[]{
         118, -121, -11, -25, -20, 115, -67, 108, 5, 27, -85, 11, 46, 119, 51, 43, 95, 74, -112, -63, -36, 123, 1, 92, -107, 115, 65, 75, -109, -56, 44, 81
      };
      byte[] bytes234 = AesCipherUtil.decrypt(bytes, bytes233, aesCrypto);
      byte[] bytes235 = sha3Hash.digest(bytes234);
      this.decoyKeySlot066 = bytes235;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes236 = new byte[]{
         -89, 56, -33, -1, 73, -98, -72, -79, -57, 38, -125, -26, 53, 38, 113, 34, 63, -72, 8, -97, -15, 4, 38, 54, 19, 1, -28, -40, 83, 9, 47, -81
      };
      byte[] bytes237 = AesCipherUtil.decrypt(bytes, bytes236, aesCrypto);
      byte[] bytes238 = sha3Hash.digest(bytes237);
      this.decoyKeySlot156 = bytes238;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes239 = new byte[]{
         -3, 111, 15, -117, 124, 22, -13, 45, 22, -121, -83, -37, 66, -69, -115, -33, -27, 9, -118, 96, -2, 117, 4, -43, 77, 93, -122, -105, 116, 108, -25, 58
      };
      byte[] bytes240 = AesCipherUtil.decrypt(bytes, bytes239, aesCrypto);
      byte[] bytes241 = sha3Hash.digest(bytes240);
      this.decoyKeySlot125 = bytes241;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes242 = new byte[]{
         35,
         63,
         -106,
         -75,
         -70,
         -62,
         -106,
         -2,
         60,
         127,
         -78,
         28,
         -31,
         18,
         121,
         -107,
         -15,
         -92,
         -101,
         -12,
         -58,
         47,
         76,
         -39,
         56,
         -110,
         50,
         -91,
         124,
         124,
         -108,
         61
      };
      byte[] bytes243 = AesCipherUtil.decrypt(bytes, bytes242, aesCrypto);
      byte[] bytes244 = sha3Hash.digest(bytes243);
      this.decoyKeySlot027 = bytes244;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes245 = new byte[]{
         124, 72, -74, 48, 106, 43, 94, -26, -85, -101, -87, -106, -81, -125, -81, 21, -24, 6, -55, -114, -71, -6, -38, 70, 61, -81, -41, -84, 39, -74, 19, 29
      };
      byte[] bytes246 = AesCipherUtil.decrypt(bytes, bytes245, aesCrypto);
      byte[] bytes247 = sha3Hash.digest(bytes246);
      this.decoyKeySlot107 = bytes247;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes248 = new byte[]{
         -39, -58, -21, 14, -111, -48, -56, 68, 66, -27, 122, -21, 70, -85, -74, -10, 22, -23, -36, 37, 100, 107, 71, 81, -65, -5, -82, -61, -85, 127, -92, -78
      };
      byte[] bytes249 = AesCipherUtil.decrypt(bytes, bytes248, aesCrypto);
      byte[] bytes250 = sha3Hash.digest(bytes249);
      this.decoyKeySlot138 = bytes250;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes251 = new byte[]{
         -113, 109, -9, 101, 101, -88, 53, -72, -65, -120, 3, 62, -40, 122, 86, 29, 100, 122, -104, -62, 106, -94, -67, -102, 82, -80, 22, -128, 75, 26, 93, 60
      };
      byte[] bytes252 = AesCipherUtil.decrypt(bytes, bytes251, aesCrypto);
      byte[] bytes253 = sha3Hash.digest(bytes252);
      this.decoyKeySlot165 = bytes253;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes254 = new byte[]{
         63, 24, 8, 65, 107, -13, 65, -61, 29, -47, 10, -81, 10, -86, 92, 120, -25, 62, 120, 36, 46, -6, -88, -2, 95, 67, 55, 2, 30, 20, -23, -89
      };
      byte[] bytes255 = AesCipherUtil.decrypt(bytes, bytes254, aesCrypto);
      byte[] bytes256 = sha3Hash.digest(bytes255);
      this.decoyKeySlot029 = bytes256;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes257 = new byte[]{
         39, -24, -117, 55, 118, -78, 38, -89, 71, 120, -3, 31, -124, 90, -115, -96, -4, 50, -2, -36, 64, -121, -80, 59, 78, -121, 27, 97, -17, -21, 41, 8
      };
      byte[] bytes258 = AesCipherUtil.decrypt(bytes, bytes257, aesCrypto);
      byte[] bytes259 = sha3Hash.digest(bytes258);
      this.decoyKeySlot061 = bytes259;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes260 = new byte[]{
         -24, 106, 16, -19, 56, -80, 15, -69, 29, -97, -76, 6, 117, -70, 49, -71, 84, -5, 33, 62, -24, -101, 40, -18, -31, 53, 69, -119, 71, 30, -89, -111
      };
      byte[] bytes261 = AesCipherUtil.decrypt(bytes, bytes260, aesCrypto);
      byte[] bytes262 = sha3Hash.digest(bytes261);
      this.decoyKeySlot192 = bytes262;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes263 = new byte[]{
         38, 108, 38, -4, -63, -21, -95, -44, -115, 30, 55, 48, -44, 105, 101, -83, 105, -75, -36, 68, 110, 50, -86, -13, 56, 123, -117, 25, 36, -8, -39, 50
      };
      byte[] bytes264 = AesCipherUtil.decrypt(bytes, bytes263, aesCrypto);
      byte[] bytes265 = sha3Hash.digest(bytes264);
      this.decoyKeySlot004 = bytes265;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes266 = new byte[]{
         -10,
         36,
         31,
         -108,
         -78,
         -113,
         -125,
         -76,
         8,
         -56,
         -93,
         -72,
         -102,
         -108,
         83,
         -1,
         -128,
         -88,
         38,
         -16,
         91,
         -48,
         -9,
         -103,
         101,
         -126,
         72,
         -70,
         71,
         92,
         124,
         12
      };
      byte[] bytes267 = AesCipherUtil.decrypt(bytes, bytes266, aesCrypto);
      byte[] bytes268 = sha3Hash.digest(bytes267);
      this.decoyKeySlot067 = bytes268;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes269 = new byte[]{
         109, 22, -11, -48, -82, -25, 86, 31, -71, -46, 29, -93, -9, -5, -15, 87, 62, 81, -78, 95, 116, 39, -31, -50, 44, -113, 80, -22, -121, -68, 114, -7
      };
      byte[] bytes270 = AesCipherUtil.decrypt(bytes, bytes269, aesCrypto);
      byte[] bytes271 = sha3Hash.digest(bytes270);
      this.decoyKeySlot145 = bytes271;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes272 = new byte[]{
         33, -78, -70, -74, 20, 118, 26, 36, 62, -62, 32, -126, 106, 46, -7, -76, 41, -35, -107, -57, -18, -84, 9, -96, 90, -117, -108, -92, 3, -62, 0, 85
      };
      byte[] bytes273 = AesCipherUtil.decrypt(bytes, bytes272, aesCrypto);
      byte[] bytes274 = sha3Hash.digest(bytes273);
      this.decoyKeySlot108 = bytes274;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes275 = new byte[]{
         57, -46, 40, -2, 119, -124, 87, -9, -106, -26, 101, -54, 58, 119, 111, 55, 43, 71, -34, -66, 19, -89, -50, -48, -64, -92, 6, 82, 0, 81, 120, 48
      };
      byte[] bytes276 = AesCipherUtil.decrypt(bytes, bytes275, aesCrypto);
      byte[] bytes277 = sha3Hash.digest(bytes276);
      this.decoyKeySlot157 = bytes277;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes278 = new byte[]{
         -84,
         -12,
         -112,
         35,
         -57,
         -83,
         126,
         -57,
         25,
         -37,
         -57,
         -127,
         -127,
         -70,
         66,
         -43,
         -65,
         -111,
         112,
         -120,
         14,
         -111,
         -52,
         -71,
         63,
         7,
         22,
         -44,
         -2,
         -3,
         -60,
         -85
      };
      byte[] bytes279 = AesCipherUtil.decrypt(bytes, bytes278, aesCrypto);
      byte[] bytes280 = sha3Hash.digest(bytes279);
      this.decoyKeySlot096 = bytes280;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes281 = new byte[]{
         3, -120, -50, 24, -49, -53, 8, -35, 18, 38, -85, -63, 23, -78, 107, 92, 20, 28, 118, -109, -34, -8, -90, -17, 23, -61, 28, -123, 20, 64, 101, 53
      };
      byte[] bytes282 = AesCipherUtil.decrypt(bytes, bytes281, aesCrypto);
      byte[] bytes283 = sha3Hash.digest(bytes282);
      this.keyGuardSlotB = bytes283;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes284 = new byte[]{
         31, -60, -35, 120, -110, 119, -7, -123, -30, 100, -54, -1, -99, -46, -120, 87, 81, 1, -30, 29, -17, 106, -99, 40, -20, 84, 66, 2, -80, 67, 65, -16
      };
      byte[] bytes285 = AesCipherUtil.decrypt(bytes, bytes284, aesCrypto);
      byte[] bytes286 = sha3Hash.digest(bytes285);
      this.decoyKeySlot123 = bytes286;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes287 = new byte[]{
         40, 104, -73, 89, 17, -43, -53, -55, -8, -68, 34, 39, -24, 102, -128, -2, 74, 3, 85, 7, -71, -87, -105, -121, -5, -126, -25, 98, -9, -11, 85, 89
      };
      byte[] bytes288 = AesCipherUtil.decrypt(bytes, bytes287, aesCrypto);
      byte[] bytes289 = sha3Hash.digest(bytes288);
      this.decoyKeySlot106 = bytes289;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes290 = new byte[]{
         120,
         -128,
         -1,
         -124,
         -99,
         -36,
         -59,
         -48,
         -12,
         42,
         -102,
         90,
         -25,
         19,
         48,
         -58,
         102,
         -39,
         -67,
         -99,
         74,
         67,
         14,
         63,
         -49,
         31,
         -55,
         -30,
         -25,
         119,
         101,
         -103
      };
      byte[] bytes291 = AesCipherUtil.decrypt(bytes, bytes290, aesCrypto);
      byte[] bytes292 = sha3Hash.digest(bytes291);
      this.decoyKeySlot131 = bytes292;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes293 = new byte[]{
         -92, -26, 11, -11, -95, 3, -82, -50, 4, 118, 57, 36, 1, -46, -50, 26, -97, 44, -48, 80, 96, 66, 64, -86, 3, 13, -120, 103, -123, 77, 2, -27
      };
      byte[] bytes294 = AesCipherUtil.decrypt(bytes, bytes293, aesCrypto);
      byte[] bytes295 = sha3Hash.digest(bytes294);
      this.decoyKeySlot172 = bytes295;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes296 = new byte[]{
         -123, -118, -53, 79, 52, 71, -97, 39, 22, 2, -123, -90, 93, 87, -9, 84, 71, 2, -62, -60, 74, 28, 31, 5, 42, -47, 31, -92, -81, 102, 19, -105
      };
      byte[] bytes297 = AesCipherUtil.decrypt(bytes, bytes296, aesCrypto);
      byte[] bytes298 = sha3Hash.digest(bytes297);
      this.decoyKeySlot082 = bytes298;
      sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes299 = new byte[]{
         84, -113, 39, 53, 41, 3, 59, 34, -96, -58, -4, -112, 45, -51, -113, -33, 31, 110, -82, 43, 84, -60, 40, -110, 0, 107, 125, -55, -7, -18, 70, -113
      };
      byte[] bytes300 = AesCipherUtil.decrypt(bytes, bytes299, aesCrypto);
      byte[] bytes301 = sha3Hash.digest(bytes300);
      this.decoyKeySlot153 = bytes301;
   }

   public void encodePacket(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) {
      list.add(this.encryptBuffer(buf));
   }

   public int padLength(int n) {
      return this.getBlockSize() - n % this.getBlockSize();
   }

   public int getBlockSize() {
      return 16;
   }

   private int mul(int n, int n2) {
      int n3 = 0;
      long l = -1669078582359431110L;
      getControlFlowMarker();
      long l2 = 0L;
      l = 6340846096454868037L ^ (long)"ಒڹ멺◰랎䂾ご呏텵陫ꪖ孶韒遭㱇⋄ⱔ\udf0c迪㑿↓䒱".hashCode();
      l2 = 2936492293890375610L ^ (long)"뱔峺綎Ӏ\ued50⭜Έ瀬蠶㐗䞨ရ፣\ue010ທೌ봭㨯葑ၘꀚ".hashCode();

      int n4;
      while (true) {
         n3 = (int)(l % 3L);
         if (n3 >= 1) {
            if (1 >= n3) {
               n4 = this.alog[(this.log[n & 0xFF] + this.log[n2 & 0xFF]) % 255];
               break;
            }

            if (n2 != 0) {
               l = (l ^ 7534130449326644618L) + -466054378544367889L;
               l2 = l2 + 2208858882961205996L ^ 5062141779077413802L;
               continue;
            }

            l = (l ^ -975793510101009738L) - -7989648806440637891L;
            l2 = (l2 ^ 8689760726379358997L) - 3391454980967219833L;
         } else {
            if (n != 0) {
               l = l + 4693720040257328694L ^ -467707375167518213L;
               l2 = (l2 ^ -7834707238577004078L) + -4310979934451954100L;
               continue;
            }

            l = (l ^ 0L) - 0L;
            l2 = (l2 ^ 0L) + 0L;
         }

         n4 = 0;
         break;
      }

      return n4;
   }

   private void makeKey(byte[] bytes) throws InvalidKeyException {
      int rounds = this.getRounds(bytes.length);
      int n = (rounds + 1) * 4;
      byte b = 4;
      int[][] intsArr = new int[rounds + 1][4];
      getControlFlowMarker();
      int[][] intsArr2 = new int[rounds + 1][4];
      int n2 = bytes.length / 4;
      int[] ints = new int[n2];
      int n3 = 0;

      for (int i = 0; n3 < n2; i += 4) {
         ints[n3] = bytes[i] << 24 | (bytes[i + 1] & 255) << 16 | (bytes[i + 2] & 255) << 8 | bytes[i + 3] & 255;
         n3++;
      }

      int n4 = 0;

      for (int j = 0; j < n2 && n4 < n; n4++) {
         intsArr[n4 / 4][n4 % 4] = ints[j];
         intsArr2[rounds - n4 / 4][n4 % 4] = ints[j];
         j++;
      }

      int n5 = 0;

      while (n4 < n) {
         int n6 = ints[n2 - 1];
         ints[0] ^= this.sBox[n6 >>> 16 & 0xFF] << 24
            ^ (this.sBox[n6 >>> 8 & 0xFF] & 255) << 16
            ^ (this.sBox[n6 & 0xFF] & 255) << 8
            ^ this.sBox[n6 >>> 24] & 255
            ^ this.rcon[n5++] << 24;
         if (n2 != 8) {
            n3 = 1;

            for (int k = 0; n3 < n2; k++) {
               ints[n3] ^= ints[k];
               n3++;
            }
         } else {
            n3 = 1;

            for (int m = 0; n3 < n2 / 2; m++) {
               ints[n3] ^= ints[m];
               n3++;
            }

            n6 = ints[n2 / 2 - 1];
            ints[n2 / 2] = ints[n2 / 2]
               ^ this.sBox[n6 & 0xFF] & 255
               ^ (this.sBox[n6 >>> 8 & 0xFF] & 255) << 8
               ^ (this.sBox[n6 >>> 16 & 0xFF] & 255) << 16
               ^ this.sBox[n6 >>> 24] << 24;
            int n7 = n2 / 2;

            for (int idx = n7 + 1; idx < n2; n7++) {
               ints[idx] ^= ints[n7];
               idx++;
            }
         }

         for (int index = 0; index < n2 && n4 < n; n4++) {
            intsArr[n4 / 4][n4 % 4] = ints[index];
            intsArr2[rounds - n4 / 4][n4 % 4] = ints[index];
            index++;
         }
      }

      for (int i2 = 1; i2 < rounds; i2++) {
         for (int i3 = 0; i3 < b; i3++) {
            int n8 = intsArr2[i2][i3];
            intsArr2[i2][i3] = this.U1[n8 >>> 24 & 0xFF] ^ this.U2[n8 >>> 16 & 0xFF] ^ this.U3[n8 >>> 8 & 0xFF] ^ this.U4[n8 & 0xFF];
         }
      }

      int[] ints2 = this.expandToSubKey(intsArr, false);
      int[] ints3 = this.expandToSubKey(intsArr2, true);
      boolean flag = rounds >= 12;
      this.added = flag;
      boolean flag2 = rounds == 14;
      this.has14Rounds = flag2;
      int n9 = rounds * 4;
      this.roundLimit = n9;
      int[][] intsArr3 = new int[][]{ints2, ints3};
      this.sessionK = intsArr3;
   }

   public static void setControlFlowMarker(int[] ints) {
      controlFlowMarker = ints;
   }

   public static int[] getControlFlowMarker() {
      return controlFlowMarker;
   }

   static {
      setControlFlowMarker(null);
   }

}
