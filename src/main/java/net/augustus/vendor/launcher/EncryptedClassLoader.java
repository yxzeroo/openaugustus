package net.augustus.vendor.launcher;

import net.augustus.vendor.launcher.crypto.AesCipherUtil;
import net.augustus.vendor.launcher.crypto.IteratedSha256KeyDeriver;
import net.augustus.vendor.launcher.crypto.Sha256Digest;
import net.augustus.vendor.launcher.crypto.keccak.Sha3Type;
import net.augustus.vendor.launcher.crypto.math.RationalFraction;
import net.augustus.vendor.launcher.net.LauncherClientHandler;
import net.augustus.vendor.launcher.net.LauncherNetworkClient;
import net.augustus.vendor.launcher.packet.C2SClassLoaderReadyPacket;
import net.augustus.vendor.launcher.packet.C2SRemoteClassRequestPacket;
import net.augustus.vendor.launcher.security.ClassLoaderAntiDebugCheck;
import net.augustus.vendor.launcher.security.VandermondeLicenseSolver;
import net.augustus.vendor.launcher.util.Base64Codec;
import net.augustus.vendor.launcher.util.Base64Decoder;
import net.augustus.vendor.launcher.util.Base64Encoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import javax.swing.JOptionPane;

public class EncryptedClassLoader extends ClassLoader {
   private static byte[] decoyKeyTable000;
   private static byte[] decoyKeyTable001;
   private static byte[] decoyKeyTable002;
   private static byte[] decoyKeyTable003;
   private static byte[] decoyKeyTable004;
   private static byte[] decoyKeyTable005;
   private static byte[] decoyKeyTable006;
   private static byte[] decoyKeyTable007;
   private static byte[] decoyKeyTable008;
   private static byte[] decoyKeyTable009;
   private static byte[] decoyKeyTable010;
   private static byte[] decoyKeyGroupB00;
   private static byte[] decoyKeyTable011;
   private static byte[] decoyKeyTable012;
   private static byte[] decoyKeyTable013;
   private static byte[] decoyKeyTable014;
   private static byte[] decoyKeyTable015;
   private static byte[] decoyKeyTable016;
   private static byte[] decoyTokenGroupA00;
   private static byte[] decoyKeyTable017;
   private static byte[] decoyKeyTable018;
   private static byte[] decoyKeyTable019;
   private static byte[] decoyKeyTable020;
   private static byte[] decoyKeyTable021;
   private static byte[] decoyKeyTable022;
   private static byte[] decoyKeyTable023;
   private static byte[] decoyKeyTable024;
   private static byte[] decoyKeyTable025;
   private static byte[] decoyKeyTable026;
   private static byte[] decoyKeyTable027;
   private static byte[] decoyKeyTable028;
   private static byte[] decoyKeyTable029;
   private static byte[] decoyKeyTable030;
   private static byte[] decoyKeyTable031;
   private static byte[] decoyKeyTable032;
   private static byte[] decoyKeyTable033;
   private static byte[] decoyKeyTable034;
   private static byte[] decoyKeyTable035;
   private static byte[] decoyKeyTable036;
   private static byte[] decoyKeyGroupA00;
   private static Map<String, byte[]> loadedClassHashes = new HashMap<>();
   private static byte[] decoyKeyTable037;
   private static long classLoadCounter = 0L;
   private static byte[] decoyKeyTable038;
   private static byte[] decoyKeyTable039;
   private static byte[] decoyKeyTable040;
   private static byte[] decoyKeyGroupB01;
   private static byte[] decoyKeyTable041;
   private static byte[] decoyKeyTable042;
   private static byte[] decoyKeyTable043;
   private static byte[] decoyKeyTable044;
   private static byte[] decoyKeyTable045;
   private static byte[] decoyKeyTable046;
   private static byte[] decoyKeyTable047;
   private static byte[] decoyKeyTable048;
   private static byte[] decoyKeyTable049;
   private static byte[] decoyKeyTable050;
   private static byte[] decoyKeyTable051;
   private static byte[] decoyKeyGroupC00;
   private static byte[] decoyKeyTable052;
   private static byte[] decoyKeyTable053;
   private static byte[] decoyKeyTable054;
   private static byte[] decoyKeyTable055;
   private static byte[] decoyKeyTable056;
   private static byte[] decoyKeyTable057;
   private static byte[] decoyKeyTable058;
   private static byte[] sessionAuthToken;
   private static byte[] decoyKeyTable059;
   private static byte[] decoyKeyTable060;
   private static byte[] decoyKeyTable061;
   private static byte[] decoyKeyGroupC01;
   private static byte[] decoyKeyTable062;
   private static byte[] decoyKeyTable063;
   private static byte[] decoyKeyGroupA01;
   private static byte[] decoyKeyTable064;
   private static byte[] decoyKeyTable065;
   private static byte[] decoyKeyTable066;
   private static byte[] decoyKeyTable067;
   private static byte[] decoyKeyTable068;
   private static byte[] decoyKeyTable069;
   private static byte[] decoyKeyTable070;
   private static byte[] decoyKeyGroupB02;
   private static byte[] decoyKeyTable071;
   private static byte[] decoyKeyTable072;
   private static byte[] decoyKeyGroupA02;
   private static byte[] decoyKeyTable073;
   private static byte[] decoyKeyTable074;
   private static byte[] decoyKeyTable075;
   private static byte[] decoyKeyTable076;
   private static byte[] entryNameKey;
   private static byte[] decoyKeyTable077;
   private static byte[] decoyTokenGroupA01;
   private static byte[] decoyKeyTable078;
   private static byte[] decoyKeyTable079;
   private static byte[] decoyKeyTable080;
   private static byte[] decoyKeyTable081;
   private static byte[] decoyKeyTable082;
   private static byte[] decoyKeyTable083;
   private static byte[] decoyKeyTable084;
   private static byte[] decoyKeyTable085;
   private static byte[] decoyKeyTable086;
   private static byte[] decoyKeyTable087;
   private static byte[] decoyKeyTable088;
   private static byte[] decoyKeyTable089;
   private static byte[] decoyKeyTable090;
   private static byte[] decoyKeyTable091;
   private static byte[] decoyKeyTable092;
   private static byte[] decoyKeyTable093;
   private static byte[] decoyKeyTable094;
   private static byte[] decoyKeyTable095;
   private static byte[] decoyKeyTable096;
   private static byte[] decoyKeyTable097;
   private static byte[] decoyKeyTable098;
   private static byte[] decoyKeyTable099;
   private static byte[] decoyKeyTable100;
   private static byte[] decoyTokenGroupB00;
   private static byte[] decoyKeyTable101;
   public static volatile byte[] loginSessionToken;
   private static byte[] decoyKeyTable102;
   private static byte[] decoyKeyTable103;
   private static byte[] decoyKeyTable104;
   private static byte[] decoyKeyTable105;
   private static byte[] decoyKeyTable106;
   private static byte[] decoyKeyTable107;
   private static byte[] decoyTokenGroupB01;
   private static byte[] decoyKeyTable108;
   private static byte[] decoyKeyTable109;
   private static byte[] decoyKeyTable110;
   private static byte[] decoyKeyTable111;
   private static byte[] decoyKeyTable112;
   private static byte[] decoyKeyTable113;
   private static byte[] decoyKeyTable114;
   private static byte[] decoyKeyTable115;
   private static byte[] decoyKeyTable116;
   private static byte[] decoyKeyTable117;
   private static byte[] decoyKeyTable118;
   private static byte[] decoyKeyTable119;
   private static byte[] decoyKeyTable120;
   private static byte[] decoyKeyTable121;
   private static byte[] decoyKeyTable122;
   private static byte[] decoyKeyTable123;
   private static byte[] decoyKeyTable124;
   private static byte[] decoyKeyTable125;
   private static byte[] decoyKeyTable126;
   private static byte[] decoyKeyTable127;
   private static byte[] decoyKeyTable128;
   private static byte[] decoyKeyTable129;
   private static byte[] decoyKeyTable130;
   private static byte[] decoyKeyTable131;
   private static byte[] decoyKeyTable132;
   private static byte[] decoyKeyTable133;
   private static byte[] decoyKeyTable134;
   private static byte[] decoyKeyTable135;
   private static byte[] decoyKeyTable136;
   private static byte[] decoyKeyTable137;
   private static byte[] decoyKeyGroupB03;
   private static byte[] decoyKeyTable138;
   private static byte[] decoyKeyTable139;
   private static byte[] keyScatterSlot00;
   private static byte[] decoyKeyTable140;
   private static byte[] decoyKeyTable141;
   private static byte[] decoyKeyTable142;
   private static byte[] tokenScatterSlot00;
   private static byte[] decoyKeyTable143;
   private static byte[] decoyKeyTable144;
   private static byte[] decoyKeyTable145;
   private static byte[] decoyKeyTable146;
   private static byte[] decoyKeyTable147;
   private static byte[] decoyKeyTable148;
   private static byte[] classDecryptionKey;
   private static byte[] decoyKeyGroupC02;
   private static byte[] decoyKeyTable149;
   private static byte[] decoyKeyTable150;
   private static byte[] decoyKeyTable151;
   private static byte[] decoyKeyTable152;
   private static byte[] decoyKeyTable153;
   private static byte[] decoyKeyTable154;
   private static byte[] decoyKeyTable155;
   private static byte[] decoyKeyTable156;
   private static byte[] decoyKeyTable157;
   private static byte[] decoyKeyTable158;
   private static byte[] decoyTokenGroupC00;
   private static byte[] decoyKeyTable159;
   private static byte[] decoyKeyTable160;
   private static byte[] tokenScatterSlot01;
   private static byte[] decoyKeyTable161;
   private static byte[] decoyKeyGroupB04;
   private static byte[] decoyKeyTable162;
   private static byte[] tokenScatterSlot02;
   private static byte[] decoyKeyTable163;
   private static byte[] decoyKeyTable164;
   private static byte[] decoyKeyTable165;
   private static byte[] decoyKeyTable166;
   private static byte[] decoyKeyTable167;
   private static final Sha256Digest SHA256_DIGEST;
   private static byte[] decoyKeyTable168;
   private static byte[] decoyKeyGroupB05;
   private static byte[] decoyKeyTable169;
   private static byte[] decoyKeyTable170;
   private static byte[] decoyKeyTable171;
   private static byte[] decoyKeyTable172;
   private static byte[] decoyKeyTable173;
   private static byte[] decoyKeyTable174;
   private static byte[] decoyKeyTable175;
   private static byte[] decoyKeyTable176;
   private static byte[] decoyTokenGroupC01;
   private static byte[] decoyKeyTable177;
   private static byte[] decoyKeyTable178;
   private static byte[] decoyKeyTable179;
   private static byte[] decoyKeyTable180;
   private static byte[] decoyKeyTable181;
   private static byte[] decoyKeyTable182;
   private static byte[] decoyKeyTable183;
   private static byte[] decoyKeyTable184;
   private static byte[] decoyKeyTable185;
   private static byte[] decoyKeyTable186;
   private static byte[] decoyKeyTable187;
   private static byte[] decoyKeyTable188;
   private static byte[] decoyKeyTable189;
   private static byte[] decoyKeyGroupA03;
   private static byte[] decoyKeyTable190;
   private final ClassLoader parentClassLoader;
   private static byte[] decoyKeyTable191;
   private static byte[] decoyKeyGroupA04;
   private static byte[] decoyKeyTable192;
   private static byte[] decoyKeyTable193;
   private static byte[] decoyKeyTable194;
   private static byte[] decoyKeyTable195;
   private static byte[] decoyKeyTable196;
   private static byte[] decoyKeyTable197;
   private static byte[] decoyKeyTable198;
   private static byte[] decoyKeyTable199;
   private static byte[] decoyKeyGroupC03;
   private static byte[] decoyKeyTable200;
   private static byte[] decoyKeyTable201;
   private static byte[] decoyKeyTable202;
   private static byte[] decoyKeyTable203;
   private static byte[] decoyKeyTable204;
   private static byte[] decoyKeyTable205;
   private static byte[] decoyKeyTable206;
   private static byte[] decoyKeyTable207;
   private static byte[] decoyKeyTable208;
   private static byte[] decoyKeyTable209;
   private static byte[] decoyKeyTable210;
   private static byte[] decoyKeyTable211;
   private static byte[] decoyKeyTable212;
   private static byte[] decoyKeyTable213;
   private static byte[] decoyKeyTable214;
   private static byte[] tokenScatterSlot03;
   private static byte[] decoyKeyTable215;
   private static byte[] decoyKeyTable216;
   private static byte[] decoyKeyTable217;
   private static byte[] decoyKeyTable218;
   private static byte[] decoyKeyTable219;
   private static byte[] decoyKeyTable220;
   private static byte[] decoyKeyTable221;
   private static byte[] decoyKeyGroupA05;
   private static byte[] decoyKeyTable222;
   private static byte[] decoyKeyTable223;
   private static byte[] decoyKeyTable224;
   private static byte[] decoyKeyTable225;
   private static byte[] decoyKeyTable226;
   private static byte[] decoyKeyTable227;
   private static byte[] decoyKeyTable228;
   private static byte[] decoyKeyTable229;
   private static byte[] decoyKeyTable230;
   private static byte[] decoyKeyTable231;
   private static byte[] decoyKeyTable232;
   private static byte[] decoyKeyTable233;
   private static byte[] decoyKeyTable234;
   private static byte[] decoyKeyTable235;
   private static byte[] decoyKeyTable236;
   private static byte[] decoyKeyTable237;
   private static byte[] decoyKeyTable238;
   private static byte[] decoyKeyTable239;
   private static byte[] decoyKeyTable240;
   private static byte[] decoyKeyTable241;
   private static byte[] decoyKeyTable242;
   private static byte[] decoyKeyTable243;
   private static byte[] decoyKeyTable244;
   private static byte[] decoyKeyTable245;
   private static byte[] decoyKeyTable246;
   private static byte[] decoyKeyTable247;
   private static byte[] decoyKeyTable248;
   private static byte[] decoyKeyTable249;
   private static byte[] decoyKeyTable250;
   private static byte[] decoyKeyTable251;
   private static byte[] decoyKeyTable252;
   private static byte[] decoyKeyTable253;
   private static byte[] decoyKeyTable254;
   private static byte[] decoyKeyTable255;
   private static byte[] decoyKeyTable256;
   private static byte[] decoyKeyGroupB06;
   private static byte[] decoyKeyTable257;
   private static byte[] decoyKeyTable258;
   private static byte[] decoyKeyTable259;
   private static byte[] decoyKeyTable260;
   private static byte[] decoyKeyTable261;
   private static final Sha3Hash SHA3_HASH;
   private static byte[] decoyKeyTable262;
   private static byte[] decoyKeyTable263;
   private static byte[] decoyKeyTable264;
   private static byte[] decoyKeyTable265;
   private static byte[] decoyKeyTable266;
   private static byte[] decoyKeyTable267;
   private static byte[] decoyKeyTable268;
   private static byte[] decoyKeyTable269;
   private static byte[] decoyKeyTable270;
   private static byte[] decoyKeyTable271;
   private static byte[] decoyKeyTable272;
   private static byte[] decoyKeyTable273;
   private static byte[] decoyKeyTable274;
   private static byte[] decoyKeyTable275;
   private static byte[] decoyKeyTable276;
   private static byte[] decoyKeyTable277;
   private static byte[] decoyKeyTable278;
   private static byte[] decoyKeyTable279;
   private static byte[] decoyKeyTable280;
   private static byte[] decoyTokenGroupC02;
   private static byte[] decoyKeyTable281;
   private static byte[] decoyKeyTable282;
   private static byte[] decoyKeyTable283;
   private static byte[] decoyKeyTable284;
   private static byte[] keyScatterSlot01;
   private static byte[] decoyKeyTable285;
   private static byte[] decoyKeyTable286;
   private static byte[] decoyKeyTable287;
   private static byte[] decoyKeyTable288;
   private static byte[] decoyTokenGroupC03;
   private static byte[] decoyKeyTable289;
   private static byte[] decoyKeyGroupA06;
   private static byte[] decoyTokenGroupB02;
   private static byte[] decoyKeyTable290;
   private static byte[] decoyKeyTable291;
   private static byte[] decoyKeyTable292;
   private static byte[] decoyTokenGroupA02;
   private static byte[] decoyKeyTable293;
   private static byte[] decoyKeyTable294;
   private static byte[] decoyKeyTable295;
   private static byte[] decoyTokenGroupA03;
   private static byte[] tokenScatterSlot04;
   private static byte[] decoyKeyTable296;
   private static byte[] decoyKeyTable297;
   private static byte[] decoyKeyTable298;
   private static byte[] decoyKeyTable299;
   private static byte[] decoyKeyTable300;
   private static byte[] decoyKeyTable301;
   private static byte[] decoyKeyTable302;
   private static byte[] decoyKeyTable303;
   private static byte[] decoyKeyTable304;
   private static byte[] decoyKeyTable305;
   private static byte[] decoyKeyTable306;
   private static byte[] decoyKeyTable307;
   private static byte[] decoyTokenGroupA04;
   private static byte[] decoyKeyTable308;
   private static byte[] decoyKeyTable309;
   private static byte[] decoyKeyTable310;
   private static byte[] decoyKeyTable311;
   private static byte[] decoyKeyTable312;
   private static byte[] decoyKeyTable313;
   private static byte[] decoyKeyTable314;
   private static byte[] decoyKeyTable315;
   private static byte[] decoyTokenGroupB03;
   private static byte[] decoyKeyTable316;
   private static byte[] decoyTokenGroupB04;
   private static byte[] decoyKeyTable317;
   private static byte[] decoyKeyTable318;
   private static byte[] decoyKeyTable319;
   private static byte[] decoyKeyTable320;
   private static byte[] decoyKeyTable321;
   private static byte[] decoyKeyTable322;
   private static byte[] decoyKeyTable323;
   private static byte[] decoyKeyTable324;
   private static byte[] decoyKeyTable325;
   private static byte[] decoyKeyTable326;
   private static byte[] decoyKeyTable327;
   private static byte[] decoyKeyTable328;
   private static byte[] decoyKeyTable329;
   private static byte[] decoyKeyGroupC04;
   private static byte[] decoyKeyTable330;
   private static byte[] decoyKeyTable331;
   private static byte[] decoyKeyTable332;
   private static byte[] decoyKeyTable333;
   private static byte[] decoyKeyTable334;
   private static byte[] decoyKeyTable335;
   private static byte[] decoyKeyTable336;
   private static byte[] decoyKeyTable337;
   private static byte[] decoyKeyGroupC05;
   private static byte[] decoyKeyTable338;
   private static byte[] decoyKeyTable339;
   private static byte[] decoyKeyTable340;
   private static byte[] decoyKeyTable341;
   private static byte[] decoyKeyTable342;
   private static byte[] decoyKeyTable343;
   private static byte[] decoyKeyTable344;
   private static byte[] decoyKeyTable345;
   private static byte[] decoyKeyTable346;
   private static byte[] decoyKeyTable347;
   private static byte[] decoyKeyTable348;
   private static byte[] decoyKeyTable349;
   private static byte[] decoyKeyTable350;
   private static byte[] decoyKeyTable351;
   private static byte[] decoyKeyTable352;
   private static byte[] decoyKeyTable353;
   private static byte[] decoyKeyTable354;
   private static byte[] decoyKeyTable355;
   private static byte[] decoyKeyTable356;
   private static byte[] decoyKeyTable357;
   private static byte[] decoyKeyTable358;
   private static byte[] decoyKeyTable359;
   private static byte[] decoyKeyTable360;
   private static byte[] decoyKeyTable361;
   private static byte[] decoyKeyTable362;
   private static byte[] decoyKeyTable363;
   private static byte[] decoyKeyTable364;
   private static byte[] decoyKeyTable365;
   private static byte[] decoyTokenGroupB05;
   private static byte[] decoyKeyTable366;
   private static byte[] tokenScatterSlot05;
   private static byte[] decoyKeyTable367;
   private static byte[] decoyKeyTable368;
   private static byte[] decoyKeyTable369;
   private static byte[] decoyKeyTable370;
   private static byte[] decoyKeyTable371;
   private static byte[] decoyKeyGroupA07;
   private static byte[] decoyTokenGroupA05;
   private static byte[] decoyKeyTable372;
   private static byte[] decoyKeyTable373;
   private static byte[] decoyKeyTable374;
   private static byte[] decoyKeyTable375;
   private static byte[] decoyKeyTable376;
   private static byte[] decoyKeyTable377;
   private static byte[] decoyKeyTable378;
   private static byte[] decoyKeyTable379;
   private static byte[] keyScatterSlot02;
   private static byte[] decoyKeyTable380;
   private static byte[] decoyKeyTable381;
   private static byte[] decoyTokenGroupC04;
   private static byte[] decoyKeyTable382;
   private static byte[] decoyKeyTable383;
   private static byte[] decoyKeyTable384;
   private static byte[] keyScatterSlot03;
   private static byte[] decoyKeyTable385;
   private static byte[] decoyKeyTable386;
   private static byte[] decoyKeyTable387;
   private static byte[] decoyKeyTable388;
   private static byte[] decoyKeyTable389;
   private static byte[] decoyKeyTable390;
   private static byte[] decoyKeyGroupA08;
   private static byte[] decoyKeyTable391;
   private static byte[] decoyKeyGroupC06;
   private static byte[] decoyKeyTable392;
   private static byte[] decoyKeyTable393;
   private static byte[] decoyKeyTable394;
   private static byte[] decoyTokenGroupA06;
   private static byte[] decoyKeyTable395;
   private static byte[] decoyKeyGroupA09;
   private static byte[] decoyKeyTable396;
   private static byte[] decoyKeyTable397;
   private static byte[] decoyKeyTable398;
   private static byte[] decoyKeyTable399;
   private static byte[] decoyKeyTable400;
   private static byte[] decoyKeyTable401;
   private static byte[] decoyKeyTable402;
   private static byte[] decoyKeyTable403;
   private static byte[] decoyKeyTable404;
   private static byte[] decoyKeyTable405;
   private static byte[] decoyKeyTable406;
   private static byte[] decoyKeyTable407;
   private static byte[] decoyKeyTable408;
   private static byte[] decoyKeyTable409;
   private static byte[] decoyKeyTable410;
   private static byte[] decoyKeyTable411;
   private static byte[] decoyKeyTable412;
   private static byte[] decoyKeyTable413;
   private static byte[] decoyKeyTable414;
   private static byte[] decoyKeyTable415;
   private static byte[] decoyKeyTable416;
   private static byte[] decoyTokenGroupC05;
   private static byte[] decoyKeyTable417;
   private static byte[] decoyKeyTable418;
   private static byte[] decoyKeyTable419;
   private static byte[] decoyTokenGroupC06;
   private static byte[] decoyKeyTable420;
   private static byte[] decoyKeyTable421;
   private static byte[] decoyKeyTable422;
   private static byte[] decoyKeyTable423;
   private static byte[] decoyKeyTable424;
   private static byte[] decoyKeyTable425;
   private static byte[] decoyKeyTable426;
   private static byte[] decoyKeyTable427;
   private static byte[] decoyKeyTable428;
   private static byte[] decoyKeyTable429;
   private static byte[] decoyKeyTable430;
   private static byte[] decoyKeyTable431;
   private static byte[] decoyKeyTable432;
   private static byte[] decoyKeyTable433;
   private static byte[] decoyKeyTable434;
   private static byte[] decoyKeyTable435;
   private static byte[] decoyKeyTable436;
   private static byte[] decoyKeyTable437;
   private static byte[] keyScatterSlot04;
   private static byte[] decoyKeyTable438;
   private static byte[] decoyKeyTable439;
   private static byte[] decoyKeyTable440;
   private static byte[] decoyKeyTable441;
   private static byte[] decoyKeyTable442;
   private static byte[] decoyKeyTable443;
   private static byte[] decoyKeyTable444;
   private static byte[] decoyKeyTable445;
   private static byte[] decoyKeyTable446;
   private static byte[] decoyKeyGroupA10;
   private static byte[] decoyKeyTable447;
   private static byte[] decoyKeyTable448;
   private static byte[] decoyKeyTable449;
   private static byte[] decoyKeyTable450;
   private static byte[] decoyKeyTable451;
   private static byte[] decoyKeyTable452;
   private static byte[] decoyKeyTable453;
   private static byte[] decoyKeyGroupC07;
   private static byte[] decoyKeyGroupC08;
   private static byte[] decoyKeyTable454;
   private static byte[] decoyKeyTable455;
   private static byte[] decoyKeyTable456;
   private static byte[] decoyKeyGroupC09;
   private static byte[] decoyKeyTable457;
   private static byte[] decoyKeyTable458;
   private static byte[] decoyKeyTable459;
   private static byte[] decoyKeyTable460;
   private static byte[] decoyKeyTable461;
   private static byte[] decoyKeyGroupC10;
   private static byte[] tokenScatterSlot06;
   private static byte[] decoyKeyTable462;
   private static byte[] decoyKeyTable463;
   private static byte[] decoyKeyTable464;
   private static byte[] decoyKeyTable465;
   private static byte[] decoyKeyTable466;
   private static byte[] decoyKeyTable467;
   private static byte[] decoyKeyTable468;
   private static byte[] decoyKeyTable469;
   private static byte[] decoyTokenGroupC07;
   private static byte[] decoyKeyTable470;
   private static byte[] decoyKeyTable471;
   private static byte[] decoyKeyTable472;
   private static byte[] decoyKeyTable473;
   private static byte[] decoyKeyTable474;
   private static byte[] decoyKeyTable475;
   private static byte[] decoyKeyTable476;
   private static byte[] decoyKeyTable477;
   private static byte[] decoyKeyTable478;
   private static byte[] decoyTokenGroupB06;
   private static byte[] decoyTokenGroupA07;
   private static byte[] decoyKeyTable479;
   private static byte[] decoyKeyTable480;
   private static byte[] decoyKeyTable481;
   private static byte[] decoyKeyTable482;
   private static byte[] decoyKeyTable483;
   private static byte[] decoyTokenGroupC08;
   private static byte[] decoyKeyTable484;
   private static byte[] decoyKeyTable485;
   private static byte[] decoyKeyTable486;
   private static byte[] decoyKeyTable487;
   private static byte[] decoyKeyTable488;
   private static byte[] decoyKeyTable489;
   private static byte[] decoyKeyGroupA11;
   private static byte[] decoyKeyTable490;
   private static byte[] decoyKeyGroupC11;
   private static byte[] decoyKeyTable491;
   private static byte[] decoyKeyTable492;
   private static byte[] decoyKeyTable493;
   private static byte[] decoyKeyTable494;
   private static byte[] decoyKeyTable495;
   private static byte[] decoyKeyTable496;
   private static byte[] decoyKeyTable497;
   private static byte[] decoyKeyTable498;
   private static byte[] decoyKeyTable499;
   private static byte[] decoyKeyTable500;
   private static byte[] tokenScatterSlot07;
   private static byte[] decoyKeyTable501;
   private static byte[] decoyKeyTable502;
   private static byte[] decoyTokenGroupC09;
   private static byte[] decoyKeyTable503;
   private static final byte OBFUSCATION_XOR_SEED = -111;
   private static byte[] decoyKeyTable504;
   private static byte[] decoyKeyTable505;
   private static byte[] decoyKeyTable506;
   private static byte[] decoyKeyTable507;
   private static byte[] decoyKeyTable508;
   private static byte[] decoyKeyTable509;
   private static byte[] decoyKeyTable510;
   private static byte[] decoyKeyTable511;
   private static byte[] decoyKeyTable512;
   private static byte[] decoyKeyTable513;
   private static byte[] decoyTokenGroupB07;
   private static byte[] decoyKeyTable514;
   private static byte[] decoyKeyTable515;
   private static byte[] keyScatterSlot05;
   private static byte[] decoyKeyTable516;
   private static byte[] decoyKeyTable517;
   private static byte[] decoyKeyTable518;
   private static byte[] decoyKeyTable519;
   private static byte[] decoyKeyTable520;
   private static byte[] decoyKeyTable521;
   private static byte[] decoyKeyTable522;
   private static byte[] decoyKeyTable523;
   private static byte[] decoyKeyTable524;
   private static byte[] decoyKeyTable525;
   private static byte[] decoyKeyTable526;
   private static byte[] decoyKeyTable527;
   private static byte[] decoyKeyTable528;
   private static byte[] decoyKeyTable529;
   private static byte[] decoyKeyTable530;
   private static byte[] decoyKeyTable531;
   private static byte[] decoyKeyTable532;
   private static byte[] decoyKeyTable533;
   private static byte[] decoyKeyTable534;
   private static byte[] decoyKeyTable535;
   private static byte[] decoyKeyTable536;
   private static byte[] decoyKeyTable537;
   private static byte[] decoyKeyTable538;
   private static byte[] decoyKeyTable539;
   private static byte[] decoyKeyTable540;
   private static byte[] decoyTokenGroupB08;
   private static byte[] decoyKeyTable541;
   private static byte[] decoyKeyTable542;
   private static byte[] decoyKeyGroupB07;
   private static byte[] decoyKeyTable543;
   private static byte[] decoyKeyTable544;
   private static byte[] decoyKeyTable545;
   private static byte[] decoyKeyTable546;
   private static byte[] decoyKeyTable547;
   private static byte[] decoyKeyTable548;
   private static byte[] decoyKeyTable549;
   private static byte[] decoyKeyTable550;
   private static byte[] decoyKeyTable551;
   private static byte[] decoyKeyTable552;
   private static byte[] decoyKeyTable553;
   private static byte[] decoyKeyTable554;
   private static byte[] decoyKeyTable555;
   private static byte[] decoyKeyTable556;
   private static byte[] decoyKeyTable557;
   private static byte[] keyScatterSlot06;
   private static byte[] decoyKeyTable558;
   private static byte[] decoyKeyTable559;
   private static byte[] decoyKeyTable560;
   private static byte[] tokenScatterSlot08;
   private static byte[] decoyKeyTable561;
   private static byte[] tokenScatterSlot09;
   private static byte[] decoyKeyGroupB08;
   private static byte[] decoyKeyTable562;
   private static byte[] decoyKeyTable563;
   private static byte[] decoyKeyTable564;
   private static byte[] decoyKeyTable565;
   private static byte[] decoyKeyTable566;
   private static byte[] decoyKeyTable567;
   private static byte[] keyScatterSlot07;
   private static byte[] decoyKeyTable568;
   private static byte[] decoyKeyTable569;
   private static byte[] decoyKeyTable570;
   private static byte[] decoyKeyTable571;
   private static byte[] decoyKeyTable572;
   private static byte[] decoyKeyTable573;
   private static byte[] decoyKeyTable574;
   private static byte[] decoyKeyTable575;
   private static byte[] decoyKeyTable576;
   private static byte[] decoyKeyTable577;
   private static byte[] decoyKeyTable578;
   private static byte[] decoyKeyTable579;
   private static byte[] decoyKeyTable580;
   private static byte[] decoyKeyTable581;
   private static byte[] decoyKeyTable582;
   private static byte[] decoyKeyTable583;
   private static byte[] decoyTokenGroupC10;
   private static byte[] decoyKeyTable584;
   private static byte[] decoyKeyTable585;
   private static byte[] decoyKeyTable586;
   private static byte[] decoyKeyTable587;
   private static byte[] decoyKeyTable588;
   private static byte[] decoyKeyTable589;
   private static byte[] decoyKeyTable590;
   private static byte[] decoyKeyTable591;
   private static byte[] decoyKeyTable592;
   private static byte[] decoyKeyTable593;
   private static byte[] decoyKeyTable594;
   private static byte[] decoyKeyTable595;
   private static byte[] decoyKeyTable596;
   private static byte[] decoyKeyTable597;
   private static byte[] keyScatterSlot08;
   private static byte[] decoyKeyTable598;
   private static byte[] decoyKeyTable599;
   private static byte[] decoyKeyTable600;
   private static byte[] decoyKeyTable601;
   private static byte[] decoyKeyTable602;
   private static byte[] decoyKeyTable603;
   private static byte[] tokenScatterSlot10;
   private static byte[] decoyKeyTable604;
   private static final AesCrypto AES_CRYPTO;
   private static byte[] decoyKeyTable605;
   private static byte[] decoyKeyTable606;
   private static byte[] decoyKeyTable607;
   private static byte[] decoyTokenGroupB09;
   private static byte[] decoyKeyTable608;
   private static byte[] keyScatterSlot09;
   private static byte[] decoyKeyTable609;
   private static byte[] decoyKeyTable610;
   private static byte[] decoyKeyTable611;
   private static byte[] decoyKeyTable612;
   private static byte[] decoyKeyTable613;
   private static byte[] decoyKeyTable614;
   private static byte[] decoyKeyTable615;
   private static byte[] decoyKeyTable616;
   private static byte[] decoyTokenGroupB10;
   private static byte[] decoyKeyTable617;
   private static byte[] decoyKeyTable618;
   private static byte[] decoyKeyTable619;
   private static byte[] decoyKeyTable620;
   private static byte[] decoyKeyTable621;
   private static byte[] decoyKeyTable622;
   private static byte[] decoyKeyTable623;
   private static byte[] decoyKeyTable624;
   private static byte[] decoyKeyTable625;
   private static byte[] decoyKeyTable626;
   private static byte[] decoyKeyTable627;
   private static byte[] decoyKeyTable628;
   private static byte[] decoyKeyTable629;
   private static byte[] decoyKeyTable630;
   private static byte[] decoyKeyTable631;
   private static byte[] decoyKeyTable632;
   private static byte[] decoyKeyTable633;
   private static byte[] decoyKeyGroupA12;
   private static byte[] decoyKeyTable634;
   private static byte[] decoyKeyTable635;
   private static byte[] decoyKeyTable636;
   private static byte[] decoyKeyTable637;
   private static byte[] keyScatterSlot10;
   private static byte[] decoyKeyTable638;
   private static byte[] decoyKeyTable639;
   private static byte[] decoyKeyTable640;
   private static byte[] tokenScatterSlot11;
   private static byte[] decoyKeyTable641;
   private static byte[] decoyKeyTable642;
   private static byte[] decoyTokenGroupB11;
   private static byte[] decoyKeyGroupA13;
   private static byte[] decoyKeyTable643;
   private static byte[] decoyKeyTable644;
   private static byte[] decoyTokenGroupB12;
   private static byte[] decoyKeyTable645;
   private static byte[] decoyKeyTable646;
   private static byte[] decoyTokenGroupA08;
   private static byte[] decoyKeyTable647;
   private static byte[] decoyKeyTable648;
   private static byte[] decoyKeyTable649;
   private static byte[] decoyKeyTable650;
   private static byte[] decoyKeyTable651;
   private static byte[] decoyKeyTable652;
   private static byte[] decoyKeyTable653;
   private static byte[] decoyKeyTable654;
   private static byte[] decoyKeyTable655;
   private static byte[] decoyKeyTable656;
   private static byte[] decoyKeyTable657;
   private static byte[] decoyKeyTable658;
   private static byte[] decoyKeyTable659;
   private static byte[] decoyKeyTable660;
   private static byte[] decoyKeyTable661;
   private static byte[] decoyKeyTable662;
   private static byte[] decoyKeyTable663;
   private static byte[] decoyKeyTable664;
   private static byte[] decoyKeyTable665;
   private static byte[] decoyKeyGroupB09;
   private static byte[] decoyKeyTable666;
   private static byte[] decoyKeyTable667;
   private static byte[] decoyKeyTable668;
   private static byte[] decoyKeyTable669;
   private static byte[] decoyTokenGroupB13;
   private static byte[] decoyKeyTable670;
   private static byte[] decoyKeyTable671;
   private static byte[] decoyKeyTable672;
   private static byte[] decoyKeyTable673;
   private static byte[] decoyKeyTable674;
   private static byte[] decoyKeyTable675;
   private static byte[] decoyKeyTable676;
   private static byte[] decoyKeyTable677;
   private static byte[] decoyKeyTable678;
   private static byte[] decoyKeyTable679;
   private static byte[] decoyKeyTable680;
   private static byte[] decoyKeyTable681;
   private static byte[] decoyKeyTable682;
   private static byte[] decoyKeyTable683;
   private static byte[] decoyKeyTable684;
   private static byte[] decoyKeyTable685;
   private static byte[] decoyKeyTable686;
   private static byte[] decoyKeyGroupB10;
   private static byte[] keyScatterSlot11;
   private static byte[] decoyKeyTable687;
   private static byte[] decoyKeyTable688;
   private static byte[] decoyKeyTable689;
   private static byte[] decoyKeyTable690;
   private static byte[] decoyKeyTable691;
   private static byte[] decoyKeyTable692;
   private static byte[] decoyKeyTable693;
   private static byte[] decoyKeyTable694;
   private static byte[] decoyTokenGroupA09;
   private static byte[] decoyKeyTable695;
   private static byte[] decoyKeyTable696;
   private static byte[] decoyKeyTable697;
   private static byte[] decoyKeyTable698;
   private static byte[] decoyKeyTable699;
   private static byte[] decoyKeyTable700;
   private static byte[] decoyKeyTable701;
   private static byte[] decoyKeyTable702;
   private static byte[] decoyKeyTable703;
   private static byte[] decoyKeyTable704;
   private static byte[] decoyKeyGroupB11;
   private static byte[] decoyKeyTable705;
   private static byte[] decoyKeyTable706;
   private static byte[] decoyKeyTable707;
   private static byte[] decoyKeyTable708;
   private static byte[] decoyKeyTable709;
   private static byte[] decoyTokenGroupA10;
   private static byte[] decoyKeyTable710;
   private static byte[] decoyKeyTable711;
   private static byte[] decoyKeyTable712;
   private static byte[] decoyTokenGroupA11;
   private static byte[] decoyKeyTable713;
   private static byte[] decoyKeyTable714;
   private static byte[] decoyKeyTable715;
   private static byte[] decoyKeyTable716;
   private static byte[] decoyKeyTable717;
   private static byte[] decoyKeyTable718;
   private static byte[] decoyKeyTable719;
   private static byte[] decoyKeyTable720;
   private static byte[] decoyKeyTable721;
   private static byte[] decoyKeyTable722;
   private static byte[] decoyKeyTable723;
   private static byte[] decoyKeyTable724;
   private static byte[] decoyKeyTable725;
   private static byte[] decoyKeyTable726;
   private static byte[] decoyKeyTable727;
   private static byte[] decoyKeyTable728;
   private static byte[] decoyKeyGroupC12;
   private static byte[] decoyKeyTable729;
   private static byte[] decoyKeyTable730;
   private static byte[] decoyKeyTable731;
   private static byte[] decoyKeyTable732;
   private static byte[] decoyKeyTable733;
   private static byte[] decoyKeyTable734;
   private static byte[] decoyKeyTable735;
   private static byte[] decoyKeyTable736;
   private static byte[] decoyKeyTable737;
   private static byte[] decoyKeyTable738;
   private static byte[] decoyKeyTable739;
   private static byte[] decoyKeyTable740;
   private static byte[] decoyKeyTable741;
   private static byte[] decoyKeyTable742;
   private static byte[] decoyKeyTable743;
   private static byte[] decoyKeyTable744;
   private static byte[] decoyKeyTable745;
   private static byte[] decoyKeyTable746;
   private static byte[] decoyKeyTable747;
   private static byte[] decoyKeyTable748;
   private static byte[] decoyKeyTable749;
   private static byte[] decoyKeyTable750;
   private static byte[] decoyKeyTable751;
   private static byte[] decoyTokenGroupA12;
   private static byte[] decoyKeyTable752;
   private static byte[] decoyKeyTable753;
   private static byte[] decoyKeyTable754;
   private static byte[] decoyKeyTable755;
   private static byte[] decoyKeyTable756;
   private static byte[] decoyKeyTable757;
   private static byte[] decoyKeyTable758;
   private static byte[] decoyKeyTable759;
   private static byte[] decoyKeyTable760;
   private static byte[] decoyKeyTable761;
   private static byte[] decoyKeyTable762;
   private static byte[] decoyKeyTable763;
   private static byte[] decoyKeyTable764;
   private static byte[] decoyKeyTable765;
   private static byte[] decoyKeyTable766;
   private static byte[] decoyKeyTable767;
   private static byte[] decoyKeyTable768;
   private static byte[] decoyKeyTable769;
   private static byte[] decoyKeyTable770;
   private static byte[] decoyKeyTable771;
   private static byte[] decoyKeyTable772;
   private static byte[] decoyKeyTable773;
   private static byte[] decoyKeyTable774;
   private static byte[] decoyKeyTable775;
   private static byte[] decoyKeyTable776;
   private static byte[] decoyKeyTable777;
   private static byte[] decoyKeyTable778;
   private static byte[] decoyKeyTable779;
   private static byte[] decoyKeyTable780;
   private static byte[] decoyKeyTable781;
   private static byte[] decoyKeyTable782;
   private static byte[] decoyKeyTable783;
   private static byte[] decoyKeyTable784;
   private static byte[] decoyKeyTable785;
   private static byte[] decoyKeyTable786;
   private static byte[] decoyKeyTable787;
   private static byte[] decoyTokenGroupC11;
   private static byte[] decoyKeyTable788;
   private static byte[] decoyTokenGroupA13;
   private static byte[] decoyKeyTable789;
   private static byte[] decoyKeyTable790;
   private static byte[] decoyKeyTable791;
   private static byte[] decoyKeyTable792;
   private static byte[] decoyKeyTable793;
   private static byte[] decoyKeyTable794;
   private static byte[] decoyTokenGroupC12;
   private static byte[] decoyKeyTable795;
   private static byte[] decoyKeyTable796;
   private static byte[] decoyKeyTable797;
   private static byte[] decoyKeyGroupC13;
   private static byte[] decoyKeyTable798;
   private static byte[] decoyKeyTable799;
   private static byte[] decoyKeyTable800;
   private static byte[] decoyKeyTable801;
   private static byte[] decoyKeyTable802;
   private static byte[] decoyKeyTable803;
   private static byte[] decoyKeyTable804;
   private static byte[] decoyKeyTable805;
   private static byte[] decoyKeyGroupB12;
   private static byte[] decoyKeyTable806;
   private static byte[] decoyKeyTable807;
   private static byte[] decoyKeyTable808;
   private static byte[] decoyKeyTable809;
   private static byte[] tokenScatterSlot12;
   private static byte[] decoyKeyTable810;
   private static byte[] decoyKeyTable811;
   private static byte[] decoyKeyTable812;
   private static byte[] decoyKeyTable813;
   private static byte[] decoyKeyTable814;
   private static byte[] decoyKeyTable815;
   private static byte[] decoyKeyTable816;
   private static byte[] decoyKeyTable817;
   private static byte[] decoyKeyTable818;
   private static byte[] decoyTokenGroupC13;
   private static byte[] decoyKeyTable819;
   private static byte[] decoyTokenGroupB14;
   private static byte[] decoyKeyGroupA14;
   private static byte[] decoyKeyTable820;
   private static byte[] decoyKeyTable821;
   private static byte[] decoyKeyTable822;
   private static byte[] decoyTokenGroupA14;
   private static byte[] decoyKeyTable823;
   private static byte[] decoyKeyTable824;
   private static byte[] decoyKeyTable825;
   private static byte[] decoyKeyTable826;
   private static byte[] decoyKeyTable827;
   private static byte[] keyScatterSlot12;
   private static byte[] decoyKeyTable828;
   private static byte[] decoyKeyTable829;
   private static byte[] decoyKeyTable830;
   private static byte[] decoyKeyTable831;
   public static volatile byte[] serverClassLoaderKey = null;
   private static byte[] decoyKeyTable832;
   private static byte[] decoyKeyGroupB13;
   private static byte[] decoyKeyTable833;
   private static byte[] decoyKeyTable834;
   private static byte[] decoyKeyTable835;
   private static byte[] decoyKeyTable836;
   private static byte[] decoyKeyTable837;
   private static byte[] decoyKeyTable838;
   private static byte[] decoyKeyTable839;
   private static byte[] decoyKeyTable840;
   private static byte[] decoyKeyTable841;
   private static byte[] decoyKeyTable842;
   private static byte[] decoyKeyTable843;
   private static byte[] decoyKeyTable844;
   private static byte[] decoyTokenGroupC14;
   private static byte[] decoyKeyTable845;
   private static byte[] decoyKeyTable846;
   private static byte[] decoyKeyGroupB14;
   private static byte[] decoyKeyTable847;
   private static byte[] decoyKeyTable848;
   private static byte[] decoyKeyTable849;
   private static byte[] decoyKeyTable850;
   private static byte[] decoyKeyTable851;
   private static byte[] decoyKeyTable852;
   private static byte[] decoyKeyTable853;
   private static byte[] decoyKeyTable854;
   private static byte[] decoyKeyTable855;
   private static byte[] decoyKeyTable856;
   private static byte[] decoyKeyGroupC14;
   private static byte[] tokenScatterSlot13;
   private static byte[] decoyKeyTable857;
   private static final long a = 9733683442469L;

   @Override
   public InputStream getResourceAsStream(String str) {
      boolean flag = false;
      boolean flag2 = false;
      boolean flag3 = false;
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      boolean flag4 = false;
      Object obj5 = null;
      Object obj6 = null;
      Object obj7 = null;
      ClientArchiveHolder.getControlFlowMarker();
      Object obj8 = null;
      Object obj9 = null;
      char ch = '\u0000';
      Object obj10 = null;
      int n = 0;
      Object obj11 = null;
      if (str.contains("assets/augustus/pictures/clientLogo.png")) {
         obj10 = "9NUXmzVHpxyPt@K%M38YYpfw%qKSz4Jdv9KkrTP3tgRc$z".getBytes(StandardCharsets.UTF_8);
         Sha256Digest sha256Digest = SHA256_DIGEST;
         byte[] bytes = (byte[])obj10;
         byte[] bytes2 = bytes;
         byte[] bytes3 = SHA256_DIGEST.digest(bytes2);
         byte[] bytes4 = bytes3;
         return new ByteArrayInputStream(sha256Digest.digest(bytes4));
      } else if (str.contains("assets/augustus/pictures/augustusLogo.png")) {
         if (serverClassLoaderKey == null) {
            JOptionPane.showMessageDialog(null, "Something went wrong. Please restart the client. If this coninues contact the support.");
            System.exit(0);
         }

         StringBuilder sb = new StringBuilder();
         Base64Encoder rfc46482 = Base64Codec.getRfc46482();
         byte[] bytes5 = serverClassLoaderKey;
         sb = sb.append(rfc46482.encodeToString(bytes5)).append("##SS88##");
         rfc46482 = Base64Codec.getRfc46482();
         byte[] bytes6 = sessionAuthToken;
         return new ByteArrayInputStream(sb.append(rfc46482.encodeToString(bytes6)).toString().getBytes(StandardCharsets.UTF_8));
      } else if (!str.contains("123456https://")) {
         EncryptedClassLoader encryptedClassLoader = this;
         String str2 = str;
         String str3 = str2;
         return encryptedClassLoader.openResourceStream(str3);
      } else {
         obj10 = str.split("123456https://");
         Base64Decoder rfc4648 = Base64Codec.getRfc4648();
         String str4 = ((String[])obj10)[2];
         String str5 = str4;
         byte[] bytes7 = rfc4648.decode3(str5);
         String str6 = "X&W8v%z6uG$&f4&b&6U9^ChNXL8U5tgXuGm4Gvqy&D3HkDxfRW6Fj9XB4md5";
         byte[] bytes8 = IteratedSha256KeyDeriver.deriveKey(SHA256_DIGEST, str6);
         obj9 = new String(AesCipherUtil.encrypt(bytes7, bytes8));
         ch = ((String[])obj10)[1].charAt(0);
         n = Integer.parseInt(ch + "");
         Sha256Digest sha256Digest2 = SHA256_DIGEST;
         byte[] bytes9 = ((String)obj9).getBytes(StandardCharsets.UTF_8);
         byte[] bytes10 = bytes9;
         obj2 = sha256Digest2.digest(bytes10);

         for (int i = 0; i <= n; i++) {
            byte[] bytes11 = (byte[])obj2;
            byte[] bytes12 = bytes11;
            obj2 = SHA256_DIGEST.digest(bytes12);
         }

         Base64Decoder rfc46483 = Base64Codec.getRfc4648();
         byte[] bytes13 = ((String[])obj10)[1].substring(1).getBytes(StandardCharsets.ISO_8859_1);
         byte[] bytes14 = bytes13;
         obj11 = rfc46483.decode(bytes14);

         for (int j = 0; j < ((byte[])obj2).length; j++) {
            if (((byte[])obj11)[j] != ((byte[])obj2)[j]) {
               return null;
            }
         }

         obj3 = ((String)obj9).split(",");
         obj8 = new ArrayList();
         obj7 = null;

         for (int k = 0; k < ((String[])obj3).length; k++) {
            if (k % 2 != 0) {
               obj6 = new RationalFraction(((String[])obj3)[k]);
               ((ArrayList)obj8).add(new RationalFraction[]{(RationalFraction)obj7, (RationalFraction)obj6});
            } else {
               obj7 = new RationalFraction(((String[])obj3)[k]);
            }
         }

         obj4 = VandermondeLicenseSolver.interpolatePolynomial((ArrayList)obj8);
         obj = "";

         for (int m = 0; m < ((ArrayList)obj4).size(); m++) {
            obj = (String)obj + ((RationalFraction[])((ArrayList)obj4).get(m))[0].getNumerator() + ",";
            obj = (String)obj + ((RationalFraction[])((ArrayList)obj4).get(m))[0].getDenominator();
            if (m + 1 < ((ArrayList)obj4).size()) {
               obj = (String)obj + ",";
            }
         }

         obj = (String)obj + "\n";

         for (int idx = 0; idx < ((ArrayList)obj8).size(); idx++) {
            obj5 = (RationalFraction[])((ArrayList)obj8).get(idx);
            obj = (String)obj + ((RationalFraction[])obj5)[0].getNumerator() + ",";
            obj = (String)obj + ((RationalFraction[])obj5)[0].getDenominator();
            if (idx + 1 < ((ArrayList)obj8).size()) {
               obj = (String)obj + ",";
            }
         }

         obj = (String)obj + "\n";

         for (int index = 0; index < ((ArrayList)obj8).size(); index++) {
            obj5 = (RationalFraction[])((ArrayList)obj8).get(index);
            obj = (String)obj + ((RationalFraction[])obj5)[1].getNumerator() + ",";
            obj = (String)obj + ((RationalFraction[])obj5)[1].getDenominator();
            if (index + 1 < ((ArrayList)obj8).size()) {
               obj = (String)obj + ",";
            }
         }

         byte[] bytes15 = ((String)obj).getBytes(StandardCharsets.UTF_8);
         String str7 = "6L1#ZemNgNnGXJm#aZQknKNQ8cu9gCwzZ^RdpCR9k&dg32LuS5yT7JPn1$cJ";
         byte[] bytes16 = IteratedSha256KeyDeriver.deriveKey(SHA256_DIGEST, str7);
         return new ByteArrayInputStream(AesCipherUtil.decrypt(bytes15, bytes16));
      }
   }

   public static void setClassDecryptionKey(byte[] bytes) {
      AesCrypto aesCrypto = new AesCrypto();
      Sha3Hash sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      classDecryptionKey = bytes;
      entryNameKey = sha3Hash.digest(bytes);
      byte[] bytes2 = new byte[]{
         110, 41, 83, 57, 21, -50, -35, 56, -20, 123, 49, 8, 73, -78, -28, 21, -89, -80, -46, -83, 124, 122, -40, 76, -85, -124, -126, -101, 66, 45, 118, 86
      };
      byte[] bytes3 = AesCipherUtil.decrypt(bytes, bytes2, aesCrypto);
      keyScatterSlot02 = sha3Hash.digest(bytes3);
      populateDecoyKeysGroupA(keyScatterSlot02);
      byte[] bytes4 = keyScatterSlot02;
      keyScatterSlot07 = sha3Hash.digest(bytes4);
      byte[] bytes5 = new byte[]{
         -79,
         43,
         -106,
         -72,
         125,
         91,
         -77,
         -100,
         -30,
         109,
         7,
         82,
         -43,
         -106,
         -74,
         -18,
         89,
         -86,
         -64,
         120,
         -110,
         14,
         116,
         102,
         57,
         -13,
         -55,
         -78,
         -39,
         122,
         55,
         -119
      };
      byte[] bytes6 = AesCipherUtil.decrypt(bytes, bytes5, aesCrypto);
      keyScatterSlot00 = sha3Hash.digest(bytes6);
      byte[] bytes7 = keyScatterSlot00;
      keyScatterSlot03 = sha3Hash.digest(bytes7);
      populateDecoyKeysGroupB(keyScatterSlot00);
      byte[] bytes8 = new byte[]{
         -106, 14, 103, -125, 60, 113, -80, -99, -89, -126, 6, 112, 74, -73, 104, 91, -54, -82, 2, 17, 23, 46, -28, -67, 33, -70, 0, -75, 127, 64, -87, 65
      };
      byte[] bytes9 = AesCipherUtil.decrypt(bytes, bytes8, aesCrypto);
      keyScatterSlot05 = sha3Hash.digest(bytes9);
      byte[] bytes10 = keyScatterSlot05;
      keyScatterSlot04 = sha3Hash.digest(bytes10);
      byte[] bytes11 = new byte[]{
         103,
         113,
         -119,
         62,
         -28,
         -19,
         43,
         68,
         -83,
         -126,
         -121,
         42,
         59,
         27,
         109,
         -121,
         -84,
         -60,
         122,
         -61,
         -120,
         -79,
         74,
         10,
         -39,
         25,
         -55,
         -123,
         102,
         62,
         -57,
         -47
      };
      byte[] bytes12 = AesCipherUtil.decrypt(bytes, bytes11, aesCrypto);
      keyScatterSlot09 = sha3Hash.digest(bytes12);
      byte[] bytes13 = keyScatterSlot09;
      keyScatterSlot10 = sha3Hash.digest(bytes13);
      byte[] bytes14 = new byte[]{
         98,
         -97,
         88,
         53,
         110,
         68,
         -2,
         -74,
         -44,
         -11,
         -85,
         31,
         -107,
         -103,
         -12,
         -125,
         100,
         -76,
         -13,
         -76,
         -16,
         -71,
         -11,
         -122,
         70,
         -113,
         -90,
         -103,
         -66,
         -120,
         -24,
         65
      };
      byte[] bytes15 = AesCipherUtil.decrypt(bytes, bytes14, aesCrypto);
      keyScatterSlot06 = sha3Hash.digest(bytes15);
      byte[] bytes16 = keyScatterSlot06;
      keyScatterSlot08 = sha3Hash.digest(bytes16);
      byte[] bytes17 = new byte[]{
         -39,
         27,
         112,
         91,
         119,
         -103,
         74,
         -21,
         -67,
         -111,
         -53,
         93,
         -61,
         -116,
         -86,
         105,
         -85,
         112,
         -62,
         -110,
         34,
         106,
         -47,
         -85,
         -85,
         -48,
         -5,
         -125,
         111,
         -52,
         92,
         -37
      };
      byte[] bytes18 = AesCipherUtil.decrypt(bytes, bytes17, aesCrypto);
      keyScatterSlot01 = sha3Hash.digest(bytes18);
      byte[] bytes19 = keyScatterSlot01;
      keyScatterSlot11 = sha3Hash.digest(bytes19);
      byte[] bytes20 = new byte[]{
         -79, -63, 16, 22, -27, -44, 51, -56, -3, 10, -100, 99, 77, 94, 76, -36, 18, -109, 87, -95, -126, -67, 0, -118, 65, 6, -41, 102, -123, -22, 84, 112
      };
      byte[] bytes21 = AesCipherUtil.decrypt(bytes, bytes20, aesCrypto);
      keyScatterSlot12 = sha3Hash.digest(bytes21);
      byte[] bytes22 = keyScatterSlot12;
      decoyKeyTable555 = sha3Hash.digest(bytes22);
      byte[] bytes23 = new byte[]{
         106, 115, 19, 108, 112, 24, 50, 75, 69, 60, 102, -56, -127, 44, -75, -110, 116, 33, 38, 102, 33, 86, 94, 16, 77, -48, 100, -74, -13, -8, 84, 95
      };
      byte[] bytes24 = AesCipherUtil.decrypt(bytes, bytes23, aesCrypto);
      decoyKeyTable534 = sha3Hash.digest(bytes24);
      byte[] bytes25 = decoyKeyTable534;
      decoyKeyTable820 = sha3Hash.digest(bytes25);
      byte[] bytes26 = new byte[]{
         100, -101, 17, 123, -6, -61, 90, 117, 89, -53, 117, 72, -24, 68, -85, -33, 80, 31, -20, -45, 100, 47, -21, 81, -93, 110, 93, 123, -51, 127, 22, -38
      };
      byte[] bytes27 = AesCipherUtil.decrypt(bytes, bytes26, aesCrypto);
      decoyKeyTable825 = sha3Hash.digest(bytes27);
      byte[] bytes28 = decoyKeyTable825;
      decoyKeyTable646 = sha3Hash.digest(bytes28);
      byte[] bytes29 = new byte[]{
         -48, 118, 61, -108, 95, -123, 48, -7, -33, 35, -44, 17, 109, 24, 61, 5, -76, 75, -70, -49, 116, 60, -28, 7, 125, -70, 47, -112, -114, 126, 118, 27
      };
      byte[] bytes30 = AesCipherUtil.decrypt(bytes, bytes29, aesCrypto);
      decoyKeyTable201 = sha3Hash.digest(bytes30);
      byte[] bytes31 = decoyKeyTable201;
      decoyKeyTable290 = sha3Hash.digest(bytes31);
      byte[] bytes32 = new byte[]{
         8, 30, 115, -86, 59, 0, 14, -120, -70, -67, -70, -96, -42, 85, 45, -30, -100, -49, -94, 20, -46, 109, -38, -111, -115, -117, -64, -93, 57, 24, 0, 22
      };
      byte[] bytes33 = AesCipherUtil.decrypt(bytes, bytes32, aesCrypto);
      decoyKeyTable310 = sha3Hash.digest(bytes33);
      byte[] bytes34 = decoyKeyTable310;
      decoyKeyTable706 = sha3Hash.digest(bytes34);
      byte[] bytes35 = new byte[]{
         111, -78, 108, -111, -42, 115, -104, -50, 22, 40, -40, 31, 105, 13, 34, -99, -36, -112, 106, 46, 125, 41, 19, 108, 41, 60, 100, 0, -23, 119, 113, 26
      };
      byte[] bytes36 = AesCipherUtil.decrypt(bytes, bytes35, aesCrypto);
      decoyKeyTable119 = sha3Hash.digest(bytes36);
      byte[] bytes37 = decoyKeyTable119;
      decoyKeyTable425 = sha3Hash.digest(bytes37);
      byte[] bytes38 = new byte[]{
         -118,
         86,
         -57,
         44,
         106,
         -55,
         -120,
         -78,
         -54,
         -53,
         44,
         41,
         100,
         -81,
         -127,
         112,
         82,
         127,
         114,
         -100,
         -96,
         -16,
         -32,
         -85,
         85,
         -72,
         -55,
         106,
         -122,
         -117,
         -88,
         13
      };
      byte[] bytes39 = AesCipherUtil.decrypt(bytes, bytes38, aesCrypto);
      decoyKeyTable732 = sha3Hash.digest(bytes39);
      byte[] bytes40 = decoyKeyTable732;
      decoyKeyTable235 = sha3Hash.digest(bytes40);
      byte[] bytes41 = new byte[]{
         45, 16, -20, -2, -69, 72, -81, 68, 102, -119, -124, 87, 4, 101, 103, 101, 31, -82, 93, -117, -68, 14, 84, 95, -54, -114, 114, -40, -104, -84, 32, 110
      };
      byte[] bytes42 = AesCipherUtil.decrypt(bytes, bytes41, aesCrypto);
      decoyKeyTable458 = sha3Hash.digest(bytes42);
      byte[] bytes43 = decoyKeyTable458;
      decoyKeyTable150 = sha3Hash.digest(bytes43);
      byte[] bytes44 = new byte[]{
         -25, 53, 74, 125, 127, 114, 17, -120, 114, 3, -82, 24, 90, -121, 79, -77, 51, -26, 46, -88, 12, -125, 103, 97, -2, 48, 89, -107, 64, -52, 51, 112
      };
      byte[] bytes45 = AesCipherUtil.decrypt(bytes, bytes44, aesCrypto);
      decoyKeyTable457 = sha3Hash.digest(bytes45);
      byte[] bytes46 = decoyKeyTable457;
      decoyKeyTable834 = sha3Hash.digest(bytes46);
      byte[] bytes47 = new byte[]{
         -34, 93, -39, -111, -126, 118, 75, 28, 34, 4, 60, -126, 49, 35, -106, 29, 3, 28, -110, -70, -76, -72, 0, -119, -12, 15, -120, 126, 32, 62, -80, -92
      };
      byte[] bytes48 = AesCipherUtil.decrypt(bytes, bytes47, aesCrypto);
      decoyKeyTable485 = sha3Hash.digest(bytes48);
      byte[] bytes49 = decoyKeyTable485;
      decoyKeyTable556 = sha3Hash.digest(bytes49);
      byte[] bytes50 = new byte[]{
         94, -31, -64, -72, -90, 36, -99, -59, 87, -124, 6, -98, 93, -69, -91, -33, -73, 82, -31, 117, -34, -21, 76, -10, 88, -22, -101, 14, 111, -105, 59, -87
      };
      byte[] bytes51 = AesCipherUtil.decrypt(bytes, bytes50, aesCrypto);
      decoyKeyTable120 = sha3Hash.digest(bytes51);
      byte[] bytes52 = decoyKeyTable120;
      decoyKeyTable630 = sha3Hash.digest(bytes52);
      byte[] bytes53 = new byte[]{
         -127, 26, 96, -128, -17, -33, -45, 25, 19, 109, -8, 57, -115, 111, 11, 75, -81, 67, -119, -84, 113, 88, 29, 66, 73, 20, -112, -107, -91, 24, 50, -124
      };
      byte[] bytes54 = AesCipherUtil.decrypt(bytes, bytes53, aesCrypto);
      decoyKeyTable165 = sha3Hash.digest(bytes54);
      byte[] bytes55 = decoyKeyTable165;
      decoyKeyTable362 = sha3Hash.digest(bytes55);
      byte[] bytes56 = new byte[]{
         65,
         -29,
         103,
         -105,
         73,
         -78,
         -92,
         73,
         75,
         -103,
         -92,
         -3,
         111,
         82,
         -60,
         -46,
         -25,
         126,
         -5,
         112,
         -22,
         -103,
         125,
         77,
         104,
         -123,
         -62,
         -40,
         19,
         112,
         -104,
         99
      };
      byte[] bytes57 = AesCipherUtil.decrypt(bytes, bytes56, aesCrypto);
      decoyKeyTable274 = sha3Hash.digest(bytes57);
      byte[] bytes58 = decoyKeyTable274;
      decoyKeyTable481 = sha3Hash.digest(bytes58);
      byte[] bytes59 = new byte[]{
         123, 10, -99, 80, 127, -12, -111, -25, 9, -65, -92, 112, 55, 110, -99, -98, 21, 63, 21, -18, -41, 97, -69, 7, -64, -117, -24, -24, -8, 70, 17, -100
      };
      byte[] bytes60 = AesCipherUtil.decrypt(bytes, bytes59, aesCrypto);
      decoyKeyTable270 = sha3Hash.digest(bytes60);
      byte[] bytes61 = decoyKeyTable270;
      decoyKeyTable207 = sha3Hash.digest(bytes61);
      byte[] bytes62 = new byte[]{
         0, -28, -96, 52, 55, -21, -27, -56, -66, -74, -127, 35, -61, -108, 64, -112, 81, -83, 39, -61, 105, 126, -13, 114, 59, 58, 97, 37, 27, 72, -107, 109
      };
      byte[] bytes63 = AesCipherUtil.decrypt(bytes, bytes62, aesCrypto);
      decoyKeyTable831 = sha3Hash.digest(bytes63);
      byte[] bytes64 = decoyKeyTable831;
      decoyKeyTable487 = sha3Hash.digest(bytes64);
      byte[] bytes65 = new byte[]{
         84, -13, -25, 84, -109, -106, 82, 123, 72, 65, 22, 16, 97, -41, 82, -26, -90, 72, -124, -36, -35, 42, 126, -71, -39, 67, 68, -103, -98, 0, -16, -62
      };
      byte[] bytes66 = AesCipherUtil.decrypt(bytes, bytes65, aesCrypto);
      decoyKeyTable350 = sha3Hash.digest(bytes66);
      byte[] bytes67 = decoyKeyTable350;
      decoyKeyTable255 = sha3Hash.digest(bytes67);
      byte[] bytes68 = new byte[]{
         119, -8, 8, 101, -105, -110, 53, 120, -125, 114, 4, 21, 111, 117, -97, -127, 6, -45, 68, 76, 29, 34, -24, 40, 46, -59, 15, -102, -33, 63, 77, -43
      };
      byte[] bytes69 = AesCipherUtil.decrypt(bytes, bytes68, aesCrypto);
      decoyKeyTable670 = sha3Hash.digest(bytes69);
      byte[] bytes70 = decoyKeyTable670;
      decoyKeyTable067 = sha3Hash.digest(bytes70);
      byte[] bytes71 = new byte[]{
         10, -117, 2, -41, 10, 104, -24, 47, -116, -97, -71, -128, -94, -47, 55, -65, 89, 65, -127, 50, 96, -112, -45, 107, -37, -5, -95, 14, 94, 54, 6, 16
      };
      byte[] bytes72 = AesCipherUtil.decrypt(bytes, bytes71, aesCrypto);
      decoyKeyTable666 = sha3Hash.digest(bytes72);
      byte[] bytes73 = decoyKeyTable666;
      decoyKeyTable289 = sha3Hash.digest(bytes73);
      byte[] bytes74 = new byte[]{
         105,
         4,
         107,
         100,
         -42,
         95,
         -121,
         -44,
         110,
         -104,
         64,
         -77,
         -18,
         62,
         -20,
         100,
         58,
         78,
         75,
         -55,
         -111,
         -116,
         106,
         -122,
         -53,
         -53,
         95,
         -32,
         -104,
         -101,
         10,
         -14
      };
      byte[] bytes75 = AesCipherUtil.decrypt(bytes, bytes74, aesCrypto);
      decoyKeyTable604 = sha3Hash.digest(bytes75);
      byte[] bytes76 = decoyKeyTable604;
      decoyKeyTable203 = sha3Hash.digest(bytes76);
      byte[] bytes77 = new byte[]{
         -56, -85, -10, 10, 83, 28, 95, 113, 0, -47, 84, 98, -60, 121, 43, 105, -12, -115, 5, 28, 95, 16, 32, -87, -7, -55, -20, 125, -13, 87, -100, -5
      };
      byte[] bytes78 = AesCipherUtil.decrypt(bytes, bytes77, aesCrypto);
      decoyKeyTable466 = sha3Hash.digest(bytes78);
      byte[] bytes79 = decoyKeyTable466;
      decoyKeyTable108 = sha3Hash.digest(bytes79);
      byte[] bytes80 = new byte[]{
         36, 60, 14, 58, -89, -99, 19, 88, -75, 91, 5, -26, -60, -6, 48, 87, -1, 121, 67, -3, -52, -120, -12, 110, 80, 8, -86, -50, 70, 71, 67, -122
      };
      byte[] bytes81 = AesCipherUtil.decrypt(bytes, bytes80, aesCrypto);
      decoyKeyTable513 = sha3Hash.digest(bytes81);
      byte[] bytes82 = decoyKeyTable513;
      decoyKeyTable110 = sha3Hash.digest(bytes82);
      byte[] bytes83 = new byte[]{
         59, 46, -111, -25, -87, 28, -123, -91, 41, 12, 56, -81, -102, 23, 88, -53, 25, -41, -71, 17, -110, -115, 72, 18, 109, -74, -43, -32, -82, -3, 115, 54
      };
      byte[] bytes84 = AesCipherUtil.decrypt(bytes, bytes83, aesCrypto);
      decoyKeyTable581 = sha3Hash.digest(bytes84);
      byte[] bytes85 = decoyKeyTable581;
      decoyKeyTable678 = sha3Hash.digest(bytes85);
      byte[] bytes86 = new byte[]{
         -98, 23, -103, 38, -14, 86, 50, 1, -62, 34, 122, -107, -103, -49, -19, -59, -57, 60, 12, 2, 101, -15, 55, -54, 99, 93, 6, -95, -107, 126, 121, -52
      };
      byte[] bytes87 = AesCipherUtil.decrypt(bytes, bytes86, aesCrypto);
      decoyKeyTable475 = sha3Hash.digest(bytes87);
      byte[] bytes88 = decoyKeyTable475;
      decoyKeyTable185 = sha3Hash.digest(bytes88);
      byte[] bytes89 = new byte[]{
         -65, 39, 112, -97, -33, 33, 22, -42, -59, 72, -101, -103, 110, 97, -98, 10, -36, 103, -19, -115, 64, -54, 127, 75, -31, 1, -70, -90, -98, -91, 12, -26
      };
      byte[] bytes90 = AesCipherUtil.decrypt(bytes, bytes89, aesCrypto);
      decoyKeyTable161 = sha3Hash.digest(bytes90);
      byte[] bytes91 = decoyKeyTable161;
      decoyKeyTable210 = sha3Hash.digest(bytes91);
      byte[] bytes92 = new byte[]{
         107, 85, -51, 113, -74, 16, -41, -27, -72, 41, 39, 69, -32, 89, -114, 18, -40, 69, -119, -108, -6, 25, 78, 85, 104, 63, 98, 23, 84, -42, -103, -30
      };
      byte[] bytes93 = AesCipherUtil.decrypt(bytes, bytes92, aesCrypto);
      decoyKeyTable594 = sha3Hash.digest(bytes93);
      byte[] bytes94 = decoyKeyTable594;
      decoyKeyTable804 = sha3Hash.digest(bytes94);
      byte[] bytes95 = new byte[]{
         -114, 70, -60, -94, 11, 46, -16, -86, -100, -122, 97, 42, 34, -120, 20, 31, 80, -35, -8, 63, -125, -54, 4, -30, 62, 91, -65, -19, -114, 109, 82, -96
      };
      byte[] bytes96 = AesCipherUtil.decrypt(bytes, bytes95, aesCrypto);
      decoyKeyTable489 = sha3Hash.digest(bytes96);
      byte[] bytes97 = decoyKeyTable489;
      decoyKeyTable488 = sha3Hash.digest(bytes97);
      byte[] bytes98 = new byte[]{
         103, -23, -99, 96, -111, -74, 118, 81, 48, 69, -100, 77, -104, -61, -42, -68, -23, 23, -53, -52, 73, -32, 95, 6, 13, -99, -19, 44, -27, 48, 56, -123
      };
      byte[] bytes99 = AesCipherUtil.decrypt(bytes, bytes98, aesCrypto);
      decoyKeyTable667 = sha3Hash.digest(bytes99);
      byte[] bytes100 = decoyKeyTable667;
      decoyKeyTable217 = sha3Hash.digest(bytes100);
      byte[] bytes101 = new byte[]{
         -81,
         84,
         126,
         113,
         -37,
         -106,
         -42,
         103,
         -59,
         -107,
         32,
         -91,
         71,
         118,
         -54,
         13,
         70,
         -55,
         -98,
         58,
         43,
         -54,
         -69,
         49,
         -62,
         36,
         -27,
         -35,
         23,
         -32,
         -106,
         124
      };
      byte[] bytes102 = AesCipherUtil.decrypt(bytes, bytes101, aesCrypto);
      decoyKeyTable839 = sha3Hash.digest(bytes102);
      byte[] bytes103 = decoyKeyTable839;
      decoyKeyTable733 = sha3Hash.digest(bytes103);
      byte[] bytes104 = new byte[]{
         11, 34, 58, -57, -26, 80, -64, -84, -48, 125, 41, 71, -95, 9, 14, 46, 97, -18, 76, 54, 75, 32, 101, -29, 106, -104, -36, 27, -40, -119, 53, 120
      };
      byte[] bytes105 = AesCipherUtil.decrypt(bytes, bytes104, aesCrypto);
      decoyKeyTable810 = sha3Hash.digest(bytes105);
      byte[] bytes106 = decoyKeyTable810;
      decoyKeyTable250 = sha3Hash.digest(bytes106);
      byte[] bytes107 = new byte[]{
         22, -34, -9, 65, 106, -69, 104, 1, 32, 98, -17, -102, -44, -102, 71, 108, 66, -128, -9, 5, 114, 42, 92, 21, -94, -20, -127, 123, -55, 93, -120, -104
      };
      byte[] bytes108 = AesCipherUtil.decrypt(bytes, bytes107, aesCrypto);
      decoyKeyTable855 = sha3Hash.digest(bytes108);
      byte[] bytes109 = decoyKeyTable855;
      decoyKeyTable617 = sha3Hash.digest(bytes109);
      byte[] bytes110 = new byte[]{
         96, -54, -89, -76, -79, -54, -47, 47, 111, 62, 34, 29, 126, -53, -34, -65, -98, 69, -106, 83, 40, 50, 54, -98, 43, -2, 43, 56, -94, 7, 102, -102
      };
      byte[] bytes111 = AesCipherUtil.decrypt(bytes, bytes110, aesCrypto);
      decoyKeyTable295 = sha3Hash.digest(bytes111);
      byte[] bytes112 = decoyKeyTable295;
      decoyKeyTable351 = sha3Hash.digest(bytes112);
      byte[] bytes113 = new byte[]{
         -104, 116, -16, -95, -40, -5, -79, 126, 80, -18, 16, -124, -27, 55, -27, -81, 83, -86, -52, 58, -76, 43, 45, 40, -111, -90, -128, 35, -20, 120, 35, 11
      };
      byte[] bytes114 = AesCipherUtil.decrypt(bytes, bytes113, aesCrypto);
      decoyKeyTable502 = sha3Hash.digest(bytes114);
      byte[] bytes115 = decoyKeyTable502;
      decoyKeyTable047 = sha3Hash.digest(bytes115);
      byte[] bytes116 = new byte[]{
         -41, 22, 57, 82, 18, -104, -98, 52, -12, -95, 117, 82, 10, 111, -106, -1, 67, 73, -108, 74, -64, 39, 45, 109, 103, 89, -76, 96, 35, 97, 84, -100
      };
      byte[] bytes117 = AesCipherUtil.decrypt(bytes, bytes116, aesCrypto);
      decoyKeyTable266 = sha3Hash.digest(bytes117);
      byte[] bytes118 = decoyKeyTable266;
      decoyKeyTable730 = sha3Hash.digest(bytes118);
      byte[] bytes119 = new byte[]{
         57, -74, 105, -46, 108, -54, -19, 13, 4, 66, -44, 50, -107, 9, 13, -59, -63, 57, 50, 108, -94, -119, -1, 63, -82, 110, -64, -117, -20, -106, 106, 58
      };
      byte[] bytes120 = AesCipherUtil.decrypt(bytes, bytes119, aesCrypto);
      decoyKeyTable145 = sha3Hash.digest(bytes120);
      byte[] bytes121 = decoyKeyTable145;
      decoyKeyTable621 = sha3Hash.digest(bytes121);
      byte[] bytes122 = new byte[]{
         -80, 13, -26, 61, -53, 69, 100, -119, -51, -95, -14, -6, -99, -38, -22, 103, -14, -80, 77, 85, 16, -95, 50, 6, -108, -26, 85, -15, -73, -19, 57, -21
      };
      byte[] bytes123 = AesCipherUtil.decrypt(bytes, bytes122, aesCrypto);
      decoyKeyTable421 = sha3Hash.digest(bytes123);
      byte[] bytes124 = decoyKeyTable421;
      decoyKeyTable298 = sha3Hash.digest(bytes124);
      byte[] bytes125 = new byte[]{
         103,
         -11,
         -103,
         -16,
         30,
         114,
         -66,
         -101,
         -75,
         -15,
         -89,
         -103,
         -6,
         -125,
         24,
         31,
         82,
         -118,
         -115,
         43,
         65,
         14,
         -111,
         -60,
         -110,
         112,
         -6,
         -5,
         -60,
         48,
         -14,
         -52
      };
      byte[] bytes126 = AesCipherUtil.decrypt(bytes, bytes125, aesCrypto);
      decoyKeyTable772 = sha3Hash.digest(bytes126);
      byte[] bytes127 = decoyKeyTable772;
      decoyKeyTable380 = sha3Hash.digest(bytes127);
      byte[] bytes128 = new byte[]{
         112, 118, -82, -48, -94, 55, 24, 76, 8, -117, -20, -16, 111, 125, 84, -99, -125, -116, 24, 29, 1, 20, 79, 110, -34, 24, 120, -17, -56, -49, -121, 3
      };
      byte[] bytes129 = AesCipherUtil.decrypt(bytes, bytes128, aesCrypto);
      decoyKeyTable282 = sha3Hash.digest(bytes129);
      byte[] bytes130 = decoyKeyTable282;
      decoyKeyTable139 = sha3Hash.digest(bytes130);
      byte[] bytes131 = new byte[]{
         5, 20, -96, 93, -99, -77, 50, -27, 117, -112, -92, 116, -48, -44, -40, 36, -3, 104, 68, -67, -69, -124, -35, 66, 111, -49, 120, -112, 88, -12, 61, 21
      };
      byte[] bytes132 = AesCipherUtil.decrypt(bytes, bytes131, aesCrypto);
      decoyKeyTable111 = sha3Hash.digest(bytes132);
      byte[] bytes133 = decoyKeyTable111;
      decoyKeyTable727 = sha3Hash.digest(bytes133);
      byte[] bytes134 = new byte[]{
         -64, -124, 28, 42, 47, -54, 7, 121, -121, -79, -16, -71, 81, -9, 93, -33, -100, -95, -111, -15, -76, 88, 41, 109, 61, 57, -47, 25, -39, 5, -48, -91
      };
      byte[] bytes135 = AesCipherUtil.decrypt(bytes, bytes134, aesCrypto);
      decoyKeyTable166 = sha3Hash.digest(bytes135);
      byte[] bytes136 = decoyKeyTable166;
      decoyKeyTable563 = sha3Hash.digest(bytes136);
      byte[] bytes137 = new byte[]{
         67, 110, 40, -42, 14, -27, -127, 63, 59, -81, 41, 89, -110, -127, 123, -1, 112, -89, -14, -91, -92, 54, 114, 74, 48, 88, 50, 102, 96, 37, -23, 59
      };
      byte[] bytes138 = AesCipherUtil.decrypt(bytes, bytes137, aesCrypto);
      decoyKeyTable634 = sha3Hash.digest(bytes138);
      byte[] bytes139 = decoyKeyTable634;
      decoyKeyTable789 = sha3Hash.digest(bytes139);
      byte[] bytes140 = new byte[]{
         37,
         -89,
         118,
         -84,
         100,
         -23,
         -95,
         77,
         -122,
         112,
         123,
         67,
         -69,
         -69,
         122,
         75,
         -15,
         -64,
         61,
         85,
         -9,
         -95,
         -49,
         -6,
         -127,
         103,
         -101,
         102,
         72,
         125,
         -31,
         -36
      };
      byte[] bytes141 = AesCipherUtil.decrypt(bytes, bytes140, aesCrypto);
      decoyKeyTable343 = sha3Hash.digest(bytes141);
      byte[] bytes142 = decoyKeyTable343;
      decoyKeyTable105 = sha3Hash.digest(bytes142);
      byte[] bytes143 = new byte[]{
         -24, 39, -64, 85, 72, -61, -4, 52, -23, -109, -40, 76, 15, -123, 58, -96, 58, 0, 53, 68, -97, 99, -104, 99, 52, -26, -98, -39, 22, -82, 63, -64
      };
      byte[] bytes144 = AesCipherUtil.decrypt(bytes, bytes143, aesCrypto);
      decoyKeyTable281 = sha3Hash.digest(bytes144);
      byte[] bytes145 = decoyKeyTable281;
      decoyKeyTable159 = sha3Hash.digest(bytes145);
      byte[] bytes146 = new byte[]{
         -115, -93, -118, -31, 69, 83, 102, -44, -52, 68, -9, 110, 56, 13, 49, 6, -44, 48, 13, 87, 14, -111, -113, -5, -122, 90, -119, 60, 89, -32, 31, 63
      };
      byte[] bytes147 = AesCipherUtil.decrypt(bytes, bytes146, aesCrypto);
      decoyKeyTable040 = sha3Hash.digest(bytes147);
      byte[] bytes148 = decoyKeyTable040;
      decoyKeyTable610 = sha3Hash.digest(bytes148);
      byte[] bytes149 = new byte[]{
         33, -106, 82, 71, -55, -9, -40, 0, 54, -13, -56, 41, 106, 68, 124, -96, -56, 112, 84, 14, 113, -72, -35, 103, 122, 53, -81, -84, -115, -28, 0, 92
      };
      byte[] bytes150 = AesCipherUtil.decrypt(bytes, bytes149, aesCrypto);
      decoyKeyTable357 = sha3Hash.digest(bytes150);
      byte[] bytes151 = decoyKeyTable357;
      decoyKeyTable404 = sha3Hash.digest(bytes151);
      byte[] bytes152 = new byte[]{
         24, -73, -113, 11, -2, 78, 112, -34, 112, 124, 67, -23, -24, -23, -45, -89, 25, 31, -34, 66, 40, -70, -114, 92, 81, -34, 73, -128, 79, -56, -127, -52
      };
      byte[] bytes153 = AesCipherUtil.decrypt(bytes, bytes152, aesCrypto);
      decoyKeyTable069 = sha3Hash.digest(bytes153);
      byte[] bytes154 = decoyKeyTable069;
      decoyKeyTable239 = sha3Hash.digest(bytes154);
      byte[] bytes155 = new byte[]{
         106, 89, -69, 13, -83, 34, 55, -35, -69, 44, 88, 38, 84, 6, -33, 87, -41, 101, 10, 7, -34, -103, 116, 122, 102, -17, -12, 32, 9, 109, 33, 9
      };
      byte[] bytes156 = AesCipherUtil.decrypt(bytes, bytes155, aesCrypto);
      decoyKeyTable795 = sha3Hash.digest(bytes156);
      byte[] bytes157 = decoyKeyTable795;
      decoyKeyTable180 = sha3Hash.digest(bytes157);
      byte[] bytes158 = new byte[]{
         19, 101, 69, -68, 4, -12, -99, -72, -11, 45, -121, 110, -83, -21, 40, 23, -72, -5, 24, -128, 78, -85, -19, -127, 91, 117, 45, 95, 33, 101, 1, 97
      };
      byte[] bytes159 = AesCipherUtil.decrypt(bytes, bytes158, aesCrypto);
      decoyKeyTable034 = sha3Hash.digest(bytes159);
      byte[] bytes160 = decoyKeyTable034;
      decoyKeyTable132 = sha3Hash.digest(bytes160);
      byte[] bytes161 = new byte[]{
         18,
         -89,
         95,
         39,
         94,
         36,
         -46,
         -23,
         -7,
         69,
         -109,
         -108,
         -13,
         14,
         -39,
         120,
         39,
         -10,
         35,
         -101,
         -115,
         -76,
         -104,
         89,
         59,
         85,
         -99,
         -113,
         -99,
         123,
         -51,
         -79
      };
      byte[] bytes162 = AesCipherUtil.decrypt(bytes, bytes161, aesCrypto);
      decoyKeyTable815 = sha3Hash.digest(bytes162);
      byte[] bytes163 = decoyKeyTable815;
      decoyKeyTable500 = sha3Hash.digest(bytes163);
      byte[] bytes164 = new byte[]{
         -111, 118, 37, 119, -52, -44, 26, 101, 111, -25, -125, -106, 107, 77, 121, -75, 4, -94, -61, 64, -27, 1, 74, 11, 31, 58, -95, -96, -63, -39, 110, -97
      };
      byte[] bytes165 = AesCipherUtil.decrypt(bytes, bytes164, aesCrypto);
      decoyKeyTable113 = sha3Hash.digest(bytes165);
      byte[] bytes166 = decoyKeyTable113;
      decoyKeyTable232 = sha3Hash.digest(bytes166);
      byte[] bytes167 = new byte[]{
         15, -87, 126, 86, -110, -75, -122, 35, -111, -78, 76, 1, -32, -92, 53, -78, -119, 108, 87, -78, 65, 43, 25, -81, 77, 64, 102, 87, 65, 27, -65, 72
      };
      byte[] bytes168 = AesCipherUtil.decrypt(bytes, bytes167, aesCrypto);
      decoyKeyTable081 = sha3Hash.digest(bytes168);
      byte[] bytes169 = decoyKeyTable081;
      decoyKeyTable767 = sha3Hash.digest(bytes169);
      byte[] bytes170 = new byte[]{
         17, 115, 10, -54, -29, -45, -85, -21, -85, 76, -41, -41, 83, -52, -65, -57, 62, -30, 33, -113, 20, -76, 71, 3, 83, -4, -29, -26, 108, 78, -15, 40
      };
      byte[] bytes171 = AesCipherUtil.decrypt(bytes, bytes170, aesCrypto);
      decoyKeyTable329 = sha3Hash.digest(bytes171);
      byte[] bytes172 = decoyKeyTable329;
      decoyKeyTable070 = sha3Hash.digest(bytes172);
      byte[] bytes173 = new byte[]{
         -14, 31, 80, -50, 49, -110, 72, 56, -68, -95, -61, 94, -39, 37, 122, -40, -38, -38, 93, 74, -24, -50, -4, -44, -10, 13, 101, 85, -19, 40, -122, 81
      };
      byte[] bytes174 = AesCipherUtil.decrypt(bytes, bytes173, aesCrypto);
      decoyKeyTable657 = sha3Hash.digest(bytes174);
      byte[] bytes175 = decoyKeyTable657;
      decoyKeyTable396 = sha3Hash.digest(bytes175);
      byte[] bytes176 = new byte[]{
         -92, 95, 20, -5, 34, -120, 58, -70, 80, -12, 13, 87, -21, 116, -4, -9, -42, -93, 40, -56, -116, 68, -76, 2, 7, -28, 20, -117, 0, 91, 80, -59
      };
      byte[] bytes177 = AesCipherUtil.decrypt(bytes, bytes176, aesCrypto);
      decoyKeyTable676 = sha3Hash.digest(bytes177);
      byte[] bytes178 = decoyKeyTable676;
      decoyKeyTable728 = sha3Hash.digest(bytes178);
      byte[] bytes179 = new byte[]{
         71, -37, 90, 76, -115, 74, -50, 17, -13, 48, 102, -33, 110, -69, 33, 13, -70, 78, -93, -120, -6, -52, 62, -69, 15, 54, -61, -79, -123, -39, 94, -125
      };
      byte[] bytes180 = AesCipherUtil.decrypt(bytes, bytes179, aesCrypto);
      decoyKeyTable709 = sha3Hash.digest(bytes180);
      byte[] bytes181 = decoyKeyTable709;
      decoyKeyTable059 = sha3Hash.digest(bytes181);
      byte[] bytes182 = new byte[]{
         -3, 89, 76, 56, -38, 45, 101, 103, -23, -37, 121, -86, -45, 107, -5, 100, 69, 40, -71, -111, 88, 59, 74, 35, 58, -32, 38, -70, -78, 29, -112, 110
      };
      byte[] bytes183 = AesCipherUtil.decrypt(bytes, bytes182, aesCrypto);
      decoyKeyTable452 = sha3Hash.digest(bytes183);
      byte[] bytes184 = decoyKeyTable452;
      decoyKeyTable450 = sha3Hash.digest(bytes184);
      byte[] bytes185 = new byte[]{
         -3, 17, 22, 59, -105, 108, 87, 16, -28, 126, 21, -79, 67, 54, -21, -84, 92, 100, -76, -76, -114, 67, -62, -56, -25, -44, -43, 114, -82, 35, -43, 7
      };
      byte[] bytes186 = AesCipherUtil.decrypt(bytes, bytes185, aesCrypto);
      decoyKeyTable431 = sha3Hash.digest(bytes186);
      byte[] bytes187 = decoyKeyTable431;
      decoyKeyTable554 = sha3Hash.digest(bytes187);
      byte[] bytes188 = new byte[]{
         110, -53, -102, -13, 12, -127, 85, -77, 64, 2, -99, 87, -126, 32, 95, 38, -58, 58, -8, 121, 59, 51, -93, -102, 96, -88, -109, -96, 111, 27, -43, -95
      };
      byte[] bytes189 = AesCipherUtil.decrypt(bytes, bytes188, aesCrypto);
      decoyKeyTable543 = sha3Hash.digest(bytes189);
      byte[] bytes190 = decoyKeyTable543;
      decoyKeyTable541 = sha3Hash.digest(bytes190);
      byte[] bytes191 = new byte[]{
         -12, 18, -64, 21, 111, 93, 53, -37, 115, 107, 90, -85, -63, 41, -16, 51, 106, -109, -14, 31, 73, 117, -100, -89, -11, -115, -14, -32, 38, -87, -57, 51
      };
      byte[] bytes192 = AesCipherUtil.decrypt(bytes, bytes191, aesCrypto);
      decoyKeyTable376 = sha3Hash.digest(bytes192);
      byte[] bytes193 = decoyKeyTable376;
      decoyKeyTable734 = sha3Hash.digest(bytes193);
      byte[] bytes194 = new byte[]{
         93, -37, 55, -90, 82, -77, 61, -122, -54, 63, 57, -84, 60, 24, 59, -36, 61, 39, 32, -20, -105, -103, 95, -3, -33, -5, -112, -80, 8, -59, -32, 60
      };
      byte[] bytes195 = AesCipherUtil.decrypt(bytes, bytes194, aesCrypto);
      decoyKeyTable628 = sha3Hash.digest(bytes195);
      byte[] bytes196 = decoyKeyTable628;
      decoyKeyTable639 = sha3Hash.digest(bytes196);
      byte[] bytes197 = new byte[]{
         19, 48, 86, 43, 73, -115, -19, -110, -55, -96, -43, 36, 74, -108, -25, 17, -74, 11, -124, 34, 122, -41, 99, -24, 42, 45, 53, 86, 122, -88, 18, -38
      };
      byte[] bytes198 = AesCipherUtil.decrypt(bytes, bytes197, aesCrypto);
      decoyKeyTable822 = sha3Hash.digest(bytes198);
      byte[] bytes199 = decoyKeyTable822;
      decoyKeyTable497 = sha3Hash.digest(bytes199);
      byte[] bytes200 = new byte[]{
         -10, 94, -119, 74, 120, 40, 109, -16, 94, -40, 69, 61, -108, 20, 91, -116, 35, -71, -15, -99, -42, 54, -104, 83, -101, 96, 34, -33, 89, -111, -72, -59
      };
      byte[] bytes201 = AesCipherUtil.decrypt(bytes, bytes200, aesCrypto);
      decoyKeyTable234 = sha3Hash.digest(bytes201);
      byte[] bytes202 = decoyKeyTable234;
      decoyKeyTable557 = sha3Hash.digest(bytes202);
      byte[] bytes203 = new byte[]{
         -94,
         -110,
         -114,
         -125,
         26,
         36,
         127,
         -20,
         -62,
         -8,
         -125,
         127,
         17,
         -116,
         -83,
         112,
         37,
         114,
         -77,
         -50,
         37,
         -47,
         -87,
         23,
         5,
         -76,
         49,
         -122,
         -119,
         -12,
         104,
         -101
      };
      byte[] bytes204 = AesCipherUtil.decrypt(bytes, bytes203, aesCrypto);
      decoyKeyTable167 = sha3Hash.digest(bytes204);
      byte[] bytes205 = decoyKeyTable167;
      decoyKeyTable299 = sha3Hash.digest(bytes205);
      byte[] bytes206 = new byte[]{
         -33,
         -14,
         105,
         72,
         45,
         -87,
         -76,
         -128,
         -63,
         -109,
         56,
         9,
         -103,
         70,
         -120,
         -91,
         84,
         116,
         97,
         -21,
         19,
         -100,
         117,
         -8,
         -110,
         -90,
         62,
         86,
         -98,
         -59,
         80,
         -105
      };
      byte[] bytes207 = AesCipherUtil.decrypt(bytes, bytes206, aesCrypto);
      decoyKeyTable669 = sha3Hash.digest(bytes207);
      byte[] bytes208 = decoyKeyTable669;
      decoyKeyTable436 = sha3Hash.digest(bytes208);
      byte[] bytes209 = new byte[]{
         -105, -70, -124, 91, 62, -85, 46, -46, 2, 85, 21, -23, 65, -64, 118, 59, 102, -104, -91, -16, -1, -49, -26, -10, 76, -24, 83, -86, -99, -23, 38, 104
      };
      byte[] bytes210 = AesCipherUtil.decrypt(bytes, bytes209, aesCrypto);
      decoyKeyTable242 = sha3Hash.digest(bytes210);
      byte[] bytes211 = decoyKeyTable242;
      decoyKeyTable761 = sha3Hash.digest(bytes211);
      populateDecoyKeysGroupC(decoyKeyTable242);
      byte[] bytes212 = new byte[]{
         19, -58, -116, 22, -32, 100, -49, 32, 98, 20, 59, -128, 100, 39, -14, -65, -58, 54, 43, 97, 34, 41, -110, 3, -92, -9, 112, 85, 120, -29, -62, 37
      };
      byte[] bytes213 = AesCipherUtil.decrypt(bytes, bytes212, aesCrypto);
      decoyKeyTable687 = sha3Hash.digest(bytes213);
      byte[] bytes214 = decoyKeyTable687;
      decoyKeyTable253 = sha3Hash.digest(bytes214);
   }

   @Override
   public URL getResource(String str) {
      try {
         return new URL(null, "bytes:///" + str, new EncryptedResourceUrlStreamHandler(this));
      } catch (MalformedURLException e) {
         throw new RuntimeException(e);
      }
   }

   public static void populateDecoyKeysGroupA(byte[] bytes) {
      AesCrypto aesCrypto = new AesCrypto();
      Sha3Hash sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes2 = new byte[]{
         -113, 35, -37, -81, -86, -95, -109, -124, -124, 111, 116, -94, -59, 29, 67, -65, 24, -98, 40, 18, 7, 121, 52, -22, 49, 105, 10, 81, 35, 15, -62, -111
      };
      byte[] bytes3 = AesCipherUtil.decrypt(bytes, bytes2, aesCrypto);
      decoyKeyGroupA10 = sha3Hash.digest(bytes3);
      byte[] bytes4 = decoyKeyGroupA10;
      decoyKeyGroupA11 = sha3Hash.digest(bytes4);
      byte[] bytes5 = new byte[]{
         -14, 125, 46, -41, 29, -32, -92, 24, 18, 115, -51, 100, 24, -49, -71, 10, -51, -66, -37, 73, 61, 101, 82, -117, -116, 32, -56, 25, -90, -8, -6, -56
      };
      byte[] bytes6 = AesCipherUtil.decrypt(bytes, bytes5, aesCrypto);
      decoyKeyGroupA05 = sha3Hash.digest(bytes6);
      byte[] bytes7 = decoyKeyGroupA05;
      decoyKeyGroupA01 = sha3Hash.digest(bytes7);
      byte[] bytes8 = new byte[]{
         101, 103, 60, -125, 41, 85, -91, 76, -44, 12, -74, 17, -47, 115, 41, 96, 121, 121, -75, 124, -76, 125, -71, 31, 15, -80, -46, -121, 88, -80, 47, -63
      };
      byte[] bytes9 = AesCipherUtil.decrypt(bytes, bytes8, aesCrypto);
      decoyKeyGroupA04 = sha3Hash.digest(bytes9);
      byte[] bytes10 = decoyKeyGroupA04;
      decoyKeyGroupA07 = sha3Hash.digest(bytes10);
      byte[] bytes11 = new byte[]{
         -9,
         125,
         -123,
         -16,
         -77,
         -8,
         -47,
         99,
         112,
         -109,
         -127,
         -34,
         121,
         98,
         124,
         -39,
         101,
         -29,
         -101,
         -94,
         -91,
         37,
         5,
         24,
         -70,
         -62,
         -101,
         62,
         71,
         -95,
         -17,
         30
      };
      byte[] bytes12 = AesCipherUtil.decrypt(bytes, bytes11, aesCrypto);
      decoyKeyGroupA14 = sha3Hash.digest(bytes12);
      byte[] bytes13 = decoyKeyGroupA14;
      decoyKeyGroupA03 = sha3Hash.digest(bytes13);
      byte[] bytes14 = new byte[]{
         80, -90, -113, -16, -43, -44, 87, 38, 40, 89, -23, 104, -18, 32, -36, 21, 63, 23, 21, 29, 77, 40, 4, 18, -15, -29, -9, 78, -47, 105, 92, -101
      };
      byte[] bytes15 = AesCipherUtil.decrypt(bytes, bytes14, aesCrypto);
      decoyKeyGroupA06 = sha3Hash.digest(bytes15);
      byte[] bytes16 = decoyKeyGroupA06;
      decoyKeyGroupA00 = sha3Hash.digest(bytes16);
      byte[] bytes17 = new byte[]{
         113, -8, 78, -28, 39, -36, 57, -21, -70, 26, -120, -38, -83, -28, -3, 25, -5, 35, -109, 59, 4, 124, -42, 97, -112, 59, 127, 32, 43, -64, 114, -70
      };
      byte[] bytes18 = AesCipherUtil.decrypt(bytes, bytes17, aesCrypto);
      decoyKeyGroupA13 = sha3Hash.digest(bytes18);
      ClientArchiveHolder.getControlFlowMarker();
      byte[] bytes19 = decoyKeyGroupA13;
      decoyKeyGroupA09 = sha3Hash.digest(bytes19);
      byte[] bytes20 = new byte[]{
         6, -1, -24, 56, 121, 37, 17, -55, -122, 73, -38, 101, 55, -67, 119, -119, -65, -39, -53, -62, -68, 66, 119, 18, 52, 108, -25, -64, -37, 36, -13, 10
      };
      byte[] bytes21 = AesCipherUtil.decrypt(bytes, bytes20, aesCrypto);
      decoyKeyGroupA02 = sha3Hash.digest(bytes21);
      byte[] bytes22 = decoyKeyGroupA02;
      decoyKeyGroupA08 = sha3Hash.digest(bytes22);
      byte[] bytes23 = new byte[]{
         -29, 32, -8, -111, 127, 57, -123, -8, -66, -22, 27, -13, 32, -124, 15, 54, 108, -109, -30, 34, -45, 53, -62, -80, -4, 9, 91, 46, -77, 122, 122, -69
      };
      byte[] bytes24 = AesCipherUtil.decrypt(bytes, bytes23, aesCrypto);
      decoyKeyGroupA12 = sha3Hash.digest(bytes24);
      byte[] bytes25 = decoyKeyGroupA12;
      decoyKeyTable851 = sha3Hash.digest(bytes25);
      byte[] bytes26 = new byte[]{
         -75, 109, -64, -36, 26, -59, -1, 35, 86, -107, 22, 77, 10, -89, -9, -79, 123, -11, -42, 52, -25, -14, -75, 112, -75, 77, -90, 96, 34, 15, -107, 18
      };
      byte[] bytes27 = AesCipherUtil.decrypt(bytes, bytes26, aesCrypto);
      decoyKeyTable407 = sha3Hash.digest(bytes27);
      byte[] bytes28 = decoyKeyTable407;
      decoyKeyTable399 = sha3Hash.digest(bytes28);
      byte[] bytes29 = new byte[]{
         89, 3, -124, -39, -110, -25, 61, -90, 53, -51, 12, -122, -75, 19, 73, -7, -54, -116, 51, -128, 106, 67, -32, 104, 125, 39, 38, 126, -30, 85, 92, -102
      };
      byte[] bytes30 = AesCipherUtil.decrypt(bytes, bytes29, aesCrypto);
      decoyKeyTable751 = sha3Hash.digest(bytes30);
      byte[] bytes31 = decoyKeyTable751;
      decoyKeyTable349 = sha3Hash.digest(bytes31);
      byte[] bytes32 = new byte[]{
         48, 10, -31, 86, -74, 114, -13, 1, -102, 0, 9, -11, -120, -103, -124, 11, 90, 78, 20, 55, -93, -82, 88, 2, 87, -4, 15, 49, 38, 0, 94, -27
      };
      byte[] bytes33 = AesCipherUtil.decrypt(bytes, bytes32, aesCrypto);
      decoyKeyTable300 = sha3Hash.digest(bytes33);
      byte[] bytes34 = decoyKeyTable300;
      decoyKeyTable636 = sha3Hash.digest(bytes34);
      byte[] bytes35 = new byte[]{
         -4, 47, -105, -45, -65, -84, 111, -20, 72, -57, -103, 35, -94, 16, -6, 33, -125, 100, 123, 2, -19, -58, 53, 49, -122, 87, 27, 21, 30, -47, 73, -86
      };
      byte[] bytes36 = AesCipherUtil.decrypt(bytes, bytes35, aesCrypto);
      decoyKeyTable740 = sha3Hash.digest(bytes36);
      byte[] bytes37 = decoyKeyTable740;
      decoyKeyTable277 = sha3Hash.digest(bytes37);
      byte[] bytes38 = new byte[]{
         76, 37, 95, -1, -6, 94, 11, -27, -106, -66, -111, -66, -112, 17, 39, -12, -101, 100, -92, -72, -54, 91, -52, 51, -17, 89, -35, -85, -49, -107, 59, -15
      };
      byte[] bytes39 = AesCipherUtil.decrypt(bytes, bytes38, aesCrypto);
      decoyKeyTable257 = sha3Hash.digest(bytes39);
      byte[] bytes40 = decoyKeyTable257;
      decoyKeyTable696 = sha3Hash.digest(bytes40);
      byte[] bytes41 = new byte[]{
         30, -45, 17, 87, 73, -68, -99, -34, 60, 82, -117, 21, -30, 105, -19, -18, -3, 90, -81, -94, 43, -54, -115, -31, 24, 23, 9, 113, -25, -5, -6, 69
      };
      byte[] bytes42 = AesCipherUtil.decrypt(bytes, bytes41, aesCrypto);
      decoyKeyTable849 = sha3Hash.digest(bytes42);
      byte[] bytes43 = decoyKeyTable849;
      decoyKeyTable398 = sha3Hash.digest(bytes43);
      byte[] bytes44 = new byte[]{
         -106, -25, -37, 111, 15, -73, 54, -49, -57, -59, 113, -79, -74, 32, 89, 53, 85, -118, -105, 42, 56, -85, -102, 1, -107, 123, -52, 32, -36, 5, 97, -124
      };
      byte[] bytes45 = AesCipherUtil.decrypt(bytes, bytes44, aesCrypto);
      decoyKeyTable742 = sha3Hash.digest(bytes45);
      byte[] bytes46 = decoyKeyTable742;
      decoyKeyTable779 = sha3Hash.digest(bytes46);
      byte[] bytes47 = new byte[]{
         -14, -7, 60, -50, -25, 63, 35, -20, -11, 66, -53, 104, 79, 120, -77, -94, 127, -78, -36, 9, -114, 40, -30, -114, -59, -90, -5, -16, 70, -74, 47, 27
      };
      byte[] bytes48 = AesCipherUtil.decrypt(bytes, bytes47, aesCrypto);
      decoyKeyTable317 = sha3Hash.digest(bytes48);
      byte[] bytes49 = decoyKeyTable317;
      decoyKeyTable236 = sha3Hash.digest(bytes49);
      byte[] bytes50 = new byte[]{
         87, 3, -94, 112, 29, -30, 84, 33, -95, 80, -109, 87, -2, -4, 101, -75, -68, -1, -85, 22, 82, 36, -112, 98, 16, 2, -66, 104, -1, 80, 53, -96
      };
      byte[] bytes51 = AesCipherUtil.decrypt(bytes, bytes50, aesCrypto);
      decoyKeyTable577 = sha3Hash.digest(bytes51);
      byte[] bytes52 = decoyKeyTable577;
      decoyKeyTable828 = sha3Hash.digest(bytes52);
      byte[] bytes53 = new byte[]{
         -31, -91, 38, 48, -27, -127, 31, -41, 51, 10, 93, 45, -25, 41, 13, -85, 101, -53, 84, -34, 85, 67, -52, 112, 65, -102, 107, -122, -30, -8, 76, 29
      };
      byte[] bytes54 = AesCipherUtil.decrypt(bytes, bytes53, aesCrypto);
      decoyKeyTable749 = sha3Hash.digest(bytes54);
      byte[] bytes55 = decoyKeyTable749;
      decoyKeyTable082 = sha3Hash.digest(bytes55);
      byte[] bytes56 = new byte[]{
         -75,
         -110,
         99,
         103,
         -27,
         -26,
         -89,
         113,
         125,
         -100,
         -26,
         -28,
         112,
         87,
         -61,
         29,
         48,
         -125,
         -41,
         -115,
         47,
         27,
         -121,
         96,
         -128,
         22,
         -64,
         -106,
         -127,
         -6,
         -59,
         -118
      };
      byte[] bytes57 = AesCipherUtil.decrypt(bytes, bytes56, aesCrypto);
      decoyKeyTable611 = sha3Hash.digest(bytes57);
      byte[] bytes58 = decoyKeyTable611;
      decoyKeyTable415 = sha3Hash.digest(bytes58);
      byte[] bytes59 = new byte[]{
         56, -1, -32, 10, 81, 9, 7, -4, -60, -44, 72, -46, 55, 98, 0, -98, 104, 92, -87, -47, 56, -2, -118, -124, 74, -117, 36, 79, 96, 65, 45, 23
      };
      byte[] bytes60 = AesCipherUtil.decrypt(bytes, bytes59, aesCrypto);
      decoyKeyTable424 = sha3Hash.digest(bytes60);
      byte[] bytes61 = decoyKeyTable424;
      decoyKeyTable748 = sha3Hash.digest(bytes61);
      byte[] bytes62 = new byte[]{
         0, 124, -88, 79, -27, -66, -11, 27, -23, 48, -78, 41, -91, 38, 60, 30, 1, 69, -109, 89, -38, -88, 50, -123, -17, 38, 7, -42, -92, -32, 69, 117
      };
      byte[] bytes63 = AesCipherUtil.decrypt(bytes, bytes62, aesCrypto);
      decoyKeyTable522 = sha3Hash.digest(bytes63);
      byte[] bytes64 = decoyKeyTable522;
      decoyKeyTable416 = sha3Hash.digest(bytes64);
      byte[] bytes65 = new byte[]{
         97, 62, 77, -27, 20, 112, 5, 43, -64, 78, -58, 51, 107, -121, -58, -12, -59, 102, -99, -85, -26, 80, -53, -34, 30, 16, -29, 83, -75, 59, -48, -62
      };
      byte[] bytes66 = AesCipherUtil.decrypt(bytes, bytes65, aesCrypto);
      decoyKeyTable447 = sha3Hash.digest(bytes66);
      byte[] bytes67 = decoyKeyTable447;
      decoyKeyTable225 = sha3Hash.digest(bytes67);
      byte[] bytes68 = new byte[]{
         124, 48, 57, -71, 114, -25, -106, 63, -47, -102, 106, -48, -6, -33, 85, 117, 38, 70, 57, 32, -88, -85, 95, 115, -94, -33, -50, 43, 102, 15, -68, 67
      };
      byte[] bytes69 = AesCipherUtil.decrypt(bytes, bytes68, aesCrypto);
      decoyKeyTable582 = sha3Hash.digest(bytes69);
      byte[] bytes70 = decoyKeyTable582;
      decoyKeyTable752 = sha3Hash.digest(bytes70);
      byte[] bytes71 = new byte[]{
         -9,
         58,
         -65,
         -81,
         -110,
         -85,
         -56,
         90,
         -19,
         -84,
         -73,
         -39,
         88,
         -78,
         -97,
         -40,
         -55,
         -103,
         -11,
         103,
         63,
         85,
         88,
         -79,
         101,
         -103,
         -98,
         -45,
         9,
         -112,
         -111,
         -52
      };
      byte[] bytes72 = AesCipherUtil.decrypt(bytes, bytes71, aesCrypto);
      decoyKeyTable158 = sha3Hash.digest(bytes72);
      byte[] bytes73 = decoyKeyTable158;
      decoyKeyTable364 = sha3Hash.digest(bytes73);
      byte[] bytes74 = new byte[]{
         84, -83, -24, 58, -103, -36, -89, -76, 20, -81, -62, -90, 48, -61, -37, -85, -46, -32, -41, 18, -89, 54, -115, 17, -18, -42, -7, -71, 29, -45, 2, -52
      };
      byte[] bytes75 = AesCipherUtil.decrypt(bytes, bytes74, aesCrypto);
      decoyKeyTable365 = sha3Hash.digest(bytes75);
      byte[] bytes76 = decoyKeyTable365;
      decoyKeyTable590 = sha3Hash.digest(bytes76);
      byte[] bytes77 = new byte[]{
         -44, 72, -66, -19, -12, 124, 109, -98, 40, 112, 2, 114, -89, 99, 68, -17, 32, -98, 17, -89, 66, 41, 38, 74, 25, -37, -79, 117, 75, -46, -108, 9
      };
      byte[] bytes78 = AesCipherUtil.decrypt(bytes, bytes77, aesCrypto);
      decoyKeyTable107 = sha3Hash.digest(bytes78);
      byte[] bytes79 = decoyKeyTable107;
      decoyKeyTable806 = sha3Hash.digest(bytes79);
      byte[] bytes80 = new byte[]{
         19, -2, -63, -71, 44, -94, -77, 98, -52, -3, -6, -74, -43, -106, 87, -16, 1, 43, 67, 23, 95, -104, 7, 31, -65, -109, -103, 79, -70, 96, -121, -110
      };
      byte[] bytes81 = AesCipherUtil.decrypt(bytes, bytes80, aesCrypto);
      decoyKeyTable084 = sha3Hash.digest(bytes81);
      byte[] bytes82 = decoyKeyTable084;
      decoyKeyTable260 = sha3Hash.digest(bytes82);
      byte[] bytes83 = new byte[]{
         4, 103, -113, 46, 117, 62, 120, -35, 10, -59, 66, 8, 45, -15, 40, -2, 89, -128, -56, 50, -67, -111, 110, -91, -122, -126, -37, 110, 15, 110, 81, -38
      };
      byte[] bytes84 = AesCipherUtil.decrypt(bytes, bytes83, aesCrypto);
      decoyKeyTable774 = sha3Hash.digest(bytes84);
      byte[] bytes85 = decoyKeyTable774;
      decoyKeyTable570 = sha3Hash.digest(bytes85);
      byte[] bytes86 = new byte[]{
         83, -113, 87, 48, 82, 17, -71, 38, -80, -92, 49, 28, 46, -57, 92, -13, -66, 102, -74, -66, -63, 89, 88, -13, 18, 26, 85, -29, -122, 55, 106, -14
      };
      byte[] bytes87 = AesCipherUtil.decrypt(bytes, bytes86, aesCrypto);
      decoyKeyTable189 = sha3Hash.digest(bytes87);
      byte[] bytes88 = decoyKeyTable189;
      decoyKeyTable842 = sha3Hash.digest(bytes88);
      byte[] bytes89 = new byte[]{
         35, 11, -62, 102, 40, 37, -1, -77, 36, 52, -81, 22, 53, 83, -35, -42, 61, 64, -96, -28, -4, -77, 93, -48, -33, 89, 45, -73, 76, -73, -29, -58
      };
      byte[] bytes90 = AesCipherUtil.decrypt(bytes, bytes89, aesCrypto);
      decoyKeyTable016 = sha3Hash.digest(bytes90);
      byte[] bytes91 = decoyKeyTable016;
      decoyKeyTable490 = sha3Hash.digest(bytes91);
      byte[] bytes92 = new byte[]{
         56, 115, -88, 58, 33, -14, 102, 27, 3, -35, -23, 18, 57, 63, -80, 13, -118, 4, -121, 103, 12, 83, 19, -28, -55, 120, 39, -106, 103, 107, -5, 86
      };
      byte[] bytes93 = AesCipherUtil.decrypt(bytes, bytes92, aesCrypto);
      decoyKeyTable813 = sha3Hash.digest(bytes93);
      byte[] bytes94 = decoyKeyTable813;
      decoyKeyTable051 = sha3Hash.digest(bytes94);
      byte[] bytes95 = new byte[]{
         -67,
         -55,
         83,
         -39,
         -75,
         33,
         -108,
         -108,
         -110,
         108,
         -61,
         89,
         -18,
         -111,
         -119,
         -26,
         -29,
         -103,
         2,
         -124,
         -121,
         48,
         -81,
         3,
         80,
         -123,
         48,
         -55,
         -35,
         -11,
         18,
         25
      };
      byte[] bytes96 = AesCipherUtil.decrypt(bytes, bytes95, aesCrypto);
      decoyKeyTable000 = sha3Hash.digest(bytes96);
      byte[] bytes97 = decoyKeyTable000;
      decoyKeyTable213 = sha3Hash.digest(bytes97);
      byte[] bytes98 = new byte[]{
         1, 95, 38, 50, -75, -76, -98, -63, -29, 91, -81, -49, -5, -74, 6, 23, -9, -22, -64, 35, -111, -18, -50, 124, 13, 63, -122, 49, 47, -17, 2, -49
      };
      byte[] bytes99 = AesCipherUtil.decrypt(bytes, bytes98, aesCrypto);
      decoyKeyTable083 = sha3Hash.digest(bytes99);
      byte[] bytes100 = decoyKeyTable083;
      decoyKeyTable058 = sha3Hash.digest(bytes100);
      byte[] bytes101 = new byte[]{
         14, 11, -122, -9, 98, -48, 107, 1, -57, 38, 31, 30, -27, -87, -108, -114, 55, -4, -51, 74, 36, -34, 71, 108, -103, -1, 58, 8, -70, 85, -50, 111
      };
      byte[] bytes102 = AesCipherUtil.decrypt(bytes, bytes101, aesCrypto);
      decoyKeyTable521 = sha3Hash.digest(bytes102);
      byte[] bytes103 = decoyKeyTable521;
      decoyKeyTable245 = sha3Hash.digest(bytes103);
      byte[] bytes104 = new byte[]{
         73,
         -78,
         -23,
         32,
         -122,
         116,
         110,
         -125,
         -21,
         44,
         94,
         52,
         -92,
         -21,
         125,
         36,
         -60,
         61,
         -103,
         -1,
         39,
         -100,
         -103,
         -44,
         47,
         -128,
         37,
         -106,
         56,
         -79,
         -91,
         -120
      };
      byte[] bytes105 = AesCipherUtil.decrypt(bytes, bytes104, aesCrypto);
      decoyKeyTable072 = sha3Hash.digest(bytes105);
      byte[] bytes106 = decoyKeyTable072;
      decoyKeyTable049 = sha3Hash.digest(bytes106);
      byte[] bytes107 = new byte[]{
         -74,
         101,
         -68,
         50,
         43,
         -28,
         106,
         27,
         103,
         21,
         -52,
         -87,
         -127,
         -20,
         94,
         -102,
         114,
         76,
         -120,
         -52,
         -94,
         -67,
         41,
         2,
         -66,
         -124,
         -11,
         43,
         -42,
         126,
         -1,
         115
      };
      byte[] bytes108 = AesCipherUtil.decrypt(bytes, bytes107, aesCrypto);
      decoyKeyTable720 = sha3Hash.digest(bytes108);
      byte[] bytes109 = decoyKeyTable720;
      decoyKeyTable523 = sha3Hash.digest(bytes109);
      byte[] bytes110 = new byte[]{
         -6, 98, 94, 125, 30, -46, 113, -120, -49, -34, 10, 19, 23, 66, -2, 44, -128, 94, 21, -10, -57, -76, 106, 54, -22, -83, 39, 121, -54, -24, 48, 57
      };
      byte[] bytes111 = AesCipherUtil.decrypt(bytes, bytes110, aesCrypto);
      decoyKeyTable001 = sha3Hash.digest(bytes111);
      byte[] bytes112 = decoyKeyTable001;
      decoyKeyTable175 = sha3Hash.digest(bytes112);
      byte[] bytes113 = new byte[]{
         -11, -62, 117, -125, -29, -78, -41, 83, 74, 29, -80, 78, 127, -27, -35, -67, -114, 35, -8, 27, -54, -20, 48, 83, -113, -67, 4, 106, 125, 97, 57, -98
      };
      byte[] bytes114 = AesCipherUtil.decrypt(bytes, bytes113, aesCrypto);
      decoyKeyTable652 = sha3Hash.digest(bytes114);
      byte[] bytes115 = decoyKeyTable652;
      decoyKeyTable763 = sha3Hash.digest(bytes115);
      byte[] bytes116 = new byte[]{
         -4, 35, -90, -62, -111, -19, -16, -86, 75, 89, -35, -29, 78, 26, -80, -106, -23, 7, -49, -6, 33, -106, 1, 19, 87, 37, -60, -74, -20, -71, 39, 40
      };
      byte[] bytes117 = AesCipherUtil.decrypt(bytes, bytes116, aesCrypto);
      decoyKeyTable044 = sha3Hash.digest(bytes117);
      byte[] bytes118 = decoyKeyTable044;
      decoyKeyTable605 = sha3Hash.digest(bytes118);
      byte[] bytes119 = new byte[]{
         32, -65, 83, -2, 45, 95, 40, 94, 99, -120, -23, 7, -71, 77, 77, 96, 13, 69, -45, -18, 84, 68, 36, 49, -111, -84, 88, -22, -91, -15, 12, -33
      };
      byte[] bytes120 = AesCipherUtil.decrypt(bytes, bytes119, aesCrypto);
      decoyKeyTable456 = sha3Hash.digest(bytes120);
      byte[] bytes121 = decoyKeyTable456;
      decoyKeyTable846 = sha3Hash.digest(bytes121);
      byte[] bytes122 = new byte[]{
         117, 116, -105, -61, -120, 97, -56, 24, -68, 51, 16, 71, -101, 77, -61, -113, -103, 72, -77, 11, -64, -78, -17, 100, 120, 18, 43, -12, 90, 66, 4, 37
      };
      byte[] bytes123 = AesCipherUtil.decrypt(bytes, bytes122, aesCrypto);
      decoyKeyTable754 = sha3Hash.digest(bytes123);
      byte[] bytes124 = decoyKeyTable754;
      decoyKeyTable845 = sha3Hash.digest(bytes124);
      byte[] bytes125 = new byte[]{
         41, 110, -23, -92, -61, -1, 13, -105, 50, -110, -122, 84, -46, 71, -28, 70, 1, -23, 87, -105, -45, -124, 97, -14, -65, -89, 32, -44, -12, 93, -25, 114
      };
      byte[] bytes126 = AesCipherUtil.decrypt(bytes, bytes125, aesCrypto);
      decoyKeyTable454 = sha3Hash.digest(bytes126);
      byte[] bytes127 = decoyKeyTable454;
      decoyKeyTable710 = sha3Hash.digest(bytes127);
      byte[] bytes128 = new byte[]{
         -9,
         -16,
         69,
         -88,
         -54,
         -17,
         -47,
         43,
         -115,
         -107,
         -74,
         31,
         24,
         -109,
         -27,
         93,
         63,
         -17,
         -67,
         44,
         -92,
         -32,
         122,
         -116,
         -128,
         48,
         20,
         92,
         -3,
         -13,
         -76,
         -49
      };
      byte[] bytes129 = AesCipherUtil.decrypt(bytes, bytes128, aesCrypto);
      decoyKeyTable540 = sha3Hash.digest(bytes129);
      byte[] bytes130 = decoyKeyTable540;
      decoyKeyTable283 = sha3Hash.digest(bytes130);
      byte[] bytes131 = new byte[]{
         -76, -104, 53, 83, 12, -84, -51, 51, -56, -41, -37, -57, -123, 1, 105, 79, 62, -96, 89, 118, 10, 113, 24, 47, -55, 112, 49, -71, 93, 38, 41, 85
      };
      byte[] bytes132 = AesCipherUtil.decrypt(bytes, bytes131, aesCrypto);
      decoyKeyTable360 = sha3Hash.digest(bytes132);
      byte[] bytes133 = decoyKeyTable360;
      decoyKeyTable430 = sha3Hash.digest(bytes133);
      byte[] bytes134 = new byte[]{
         47, -15, 32, 92, 58, -46, -72, 120, -84, -85, -97, -87, -86, 86, -123, 88, -44, -126, 95, 94, 0, 15, 93, 1, 56, -92, -95, 80, 104, 87, 109, -18
      };
      byte[] bytes135 = AesCipherUtil.decrypt(bytes, bytes134, aesCrypto);
      decoyKeyTable037 = sha3Hash.digest(bytes135);
      byte[] bytes136 = decoyKeyTable037;
      decoyKeyTable169 = sha3Hash.digest(bytes136);
      byte[] bytes137 = new byte[]{
         -10, 85, -20, -72, 35, -79, -31, 98, 8, -24, 53, -12, -89, -73, -44, -103, 45, 63, 20, -67, 35, -42, -105, -37, 48, 2, 96, 116, -40, 96, -23, 31
      };
      byte[] bytes138 = AesCipherUtil.decrypt(bytes, bytes137, aesCrypto);
      decoyKeyTable296 = sha3Hash.digest(bytes138);
      byte[] bytes139 = decoyKeyTable296;
      decoyKeyTable410 = sha3Hash.digest(bytes139);
      byte[] bytes140 = new byte[]{
         -36, 3, -9, -30, 6, 75, 111, -96, 42, -105, 86, 118, -13, -82, 67, -3, 42, -19, 104, 111, -49, 81, 9, 86, -71, -39, 96, 26, 49, 85, -125, 80
      };
      byte[] bytes141 = AesCipherUtil.decrypt(bytes, bytes140, aesCrypto);
      decoyKeyTable553 = sha3Hash.digest(bytes141);
      byte[] bytes142 = decoyKeyTable553;
      decoyKeyTable532 = sha3Hash.digest(bytes142);
      byte[] bytes143 = new byte[]{
         0, 23, -35, 39, 31, -127, 21, 22, 106, 92, -90, -69, 86, 121, 63, 58, 55, 57, 107, -52, -88, 88, 86, 103, 69, -85, 90, 22, -35, 23, 92, 26
      };
      byte[] bytes144 = AesCipherUtil.decrypt(bytes, bytes143, aesCrypto);
      decoyKeyTable140 = sha3Hash.digest(bytes144);
      byte[] bytes145 = decoyKeyTable140;
      decoyKeyTable405 = sha3Hash.digest(bytes145);
      byte[] bytes146 = new byte[]{
         -16, -19, 71, -115, -31, 81, 120, 122, -59, -38, 110, -17, 4, -53, 72, -43, 4, 114, -12, -64, -26, 91, -123, -71, -113, 97, 60, -30, 53, 73, 106, -32
      };
      byte[] bytes147 = AesCipherUtil.decrypt(bytes, bytes146, aesCrypto);
      decoyKeyTable003 = sha3Hash.digest(bytes147);
      byte[] bytes148 = decoyKeyTable003;
      decoyKeyTable496 = sha3Hash.digest(bytes148);
      byte[] bytes149 = new byte[]{
         -107, -12, 80, -2, -3, -25, 24, -83, -50, -32, -70, -16, -78, -118, -51, -121, 60, 68, -73, 109, -82, 58, 60, 67, 117, -122, -119, 63, -61, 94, 70, 79
      };
      byte[] bytes150 = AesCipherUtil.decrypt(bytes, bytes149, aesCrypto);
      decoyKeyTable833 = sha3Hash.digest(bytes150);
      byte[] bytes151 = decoyKeyTable833;
      decoyKeyTable663 = sha3Hash.digest(bytes151);
      byte[] bytes152 = new byte[]{
         91, 99, 121, -61, 49, 63, -101, 76, -24, -25, 27, 49, -35, 45, -100, -109, 5, 88, -54, 124, -82, 20, -75, 93, 59, -13, 39, -19, 60, 97, 97, 35
      };
      byte[] bytes153 = AesCipherUtil.decrypt(bytes, bytes152, aesCrypto);
      decoyKeyTable061 = sha3Hash.digest(bytes153);
      byte[] bytes154 = decoyKeyTable061;
      decoyKeyTable539 = sha3Hash.digest(bytes154);
      byte[] bytes155 = new byte[]{
         -32, 124, 10, -29, 70, 83, 122, -92, 46, 35, 83, 46, -28, -60, 52, -48, -125, 89, -74, -46, -118, 127, 112, 59, 104, -67, -106, -14, -112, -77, 69, 59
      };
      byte[] bytes156 = AesCipherUtil.decrypt(bytes, bytes155, aesCrypto);
      decoyKeyTable856 = sha3Hash.digest(bytes156);
      byte[] bytes157 = decoyKeyTable856;
      decoyKeyTable106 = sha3Hash.digest(bytes157);
      byte[] bytes158 = new byte[]{
         -31, -26, 8, -5, 32, -55, -110, -4, 81, 61, -100, -57, -83, -85, 61, 22, 104, -74, 6, -28, 112, 113, 2, -62, -28, 86, 80, -64, 35, 81, -70, -22
      };
      byte[] bytes159 = AesCipherUtil.decrypt(bytes, bytes158, aesCrypto);
      decoyKeyTable116 = sha3Hash.digest(bytes159);
      byte[] bytes160 = decoyKeyTable116;
      decoyKeyTable391 = sha3Hash.digest(bytes160);
      byte[] bytes161 = new byte[]{
         41, -46, -112, 110, 102, 63, -63, 13, -128, 32, -79, -110, -102, 38, 81, 64, -91, -69, -52, -15, -89, 92, 20, 53, -89, 3, 92, 40, 15, -13, 53, -112
      };
      byte[] bytes162 = AesCipherUtil.decrypt(bytes, bytes161, aesCrypto);
      decoyKeyTable526 = sha3Hash.digest(bytes162);
      byte[] bytes163 = decoyKeyTable526;
      decoyKeyTable215 = sha3Hash.digest(bytes163);
      byte[] bytes164 = new byte[]{
         113, 44, -22, 119, 30, -123, 56, 70, 78, -62, 78, 118, -59, -36, 83, 76, 13, 22, -57, -113, -70, -87, 66, -53, -1, -101, 88, -72, -72, -14, 34, -108
      };
      byte[] bytes165 = AesCipherUtil.decrypt(bytes, bytes164, aesCrypto);
      decoyKeyTable758 = sha3Hash.digest(bytes165);
      byte[] bytes166 = decoyKeyTable758;
      decoyKeyTable022 = sha3Hash.digest(bytes166);
      byte[] bytes167 = new byte[]{
         -47, 69, 4, 91, -114, -22, 50, 23, -74, 40, -19, -109, 57, 21, 58, 104, 44, 87, -53, -85, -62, -106, 86, 48, 88, -91, 92, 7, -41, -51, -14, -10
      };
      byte[] bytes168 = AesCipherUtil.decrypt(bytes, bytes167, aesCrypto);
      decoyKeyTable012 = sha3Hash.digest(bytes168);
      byte[] bytes169 = decoyKeyTable012;
      decoyKeyTable716 = sha3Hash.digest(bytes169);
      byte[] bytes170 = new byte[]{
         56, 45, 100, 45, 125, -14, -69, 102, -76, -31, -61, -28, 100, -125, 28, -97, 18, -100, -67, -123, -1, 88, 20, 66, -119, -82, 31, 122, 36, -112, -27, 7
      };
      byte[] bytes171 = AesCipherUtil.decrypt(bytes, bytes170, aesCrypto);
      decoyKeyTable665 = sha3Hash.digest(bytes171);
      byte[] bytes172 = decoyKeyTable665;
      decoyKeyTable658 = sha3Hash.digest(bytes172);
      byte[] bytes173 = new byte[]{
         121,
         35,
         -29,
         -58,
         -39,
         52,
         0,
         54,
         -127,
         -108,
         93,
         -81,
         -53,
         -62,
         -110,
         93,
         21,
         -47,
         116,
         100,
         14,
         9,
         -41,
         -104,
         20,
         -91,
         123,
         -93,
         -6,
         -112,
         -128,
         -79
      };
      byte[] bytes174 = AesCipherUtil.decrypt(bytes, bytes173, aesCrypto);
      decoyKeyTable002 = sha3Hash.digest(bytes174);
      byte[] bytes175 = decoyKeyTable002;
      decoyKeyTable229 = sha3Hash.digest(bytes175);
      byte[] bytes176 = new byte[]{
         -101, 106, 110, 32, -108, 5, 55, 14, 27, -112, -122, 33, -76, 124, -102, -18, -55, 58, 53, -72, 50, 104, 73, 126, -51, -110, 1, 94, -117, -8, -32, -59
      };
      byte[] bytes177 = AesCipherUtil.decrypt(bytes, bytes176, aesCrypto);
      decoyKeyTable389 = sha3Hash.digest(bytes177);
      byte[] bytes178 = decoyKeyTable389;
      decoyKeyTable550 = sha3Hash.digest(bytes178);
      byte[] bytes179 = new byte[]{
         24, -81, -126, 10, -113, -98, -4, 45, 21, -37, 45, 80, 63, 76, -43, -36, -118, 113, -5, -29, -84, 47, 120, 27, -75, 109, -7, -88, 0, -56, -76, -123
      };
      byte[] bytes180 = AesCipherUtil.decrypt(bytes, bytes179, aesCrypto);
      decoyKeyTable062 = sha3Hash.digest(bytes180);
      byte[] bytes181 = decoyKeyTable062;
      decoyKeyTable808 = sha3Hash.digest(bytes181);
      byte[] bytes182 = new byte[]{
         126, 98, -14, -110, -64, -51, 124, -9, 99, -94, 121, 104, 89, -41, -75, 89, 40, 37, 82, 25, -104, -83, -85, -65, 29, 90, 58, 123, 92, -22, -63, -18
      };
      byte[] bytes183 = AesCipherUtil.decrypt(bytes, bytes182, aesCrypto);
      decoyKeyTable243 = sha3Hash.digest(bytes183);
      byte[] bytes184 = decoyKeyTable243;
      decoyKeyTable800 = sha3Hash.digest(bytes184);
      byte[] bytes185 = new byte[]{
         47, 30, 5, -24, 111, -124, 65, -43, -74, 52, 19, 47, 86, 55, 74, -94, -12, 73, -21, -21, 53, 51, -103, 125, 56, -31, 39, 105, 52, -30, -47, 83
      };
      byte[] bytes186 = AesCipherUtil.decrypt(bytes, bytes185, aesCrypto);
      decoyKeyTable223 = sha3Hash.digest(bytes186);
      byte[] bytes187 = decoyKeyTable223;
      decoyKeyTable146 = sha3Hash.digest(bytes187);
      byte[] bytes188 = new byte[]{
         65, 108, 100, -83, -39, -127, -35, 9, -66, 0, -89, 54, 28, 123, -2, -36, -53, 62, -86, -24, -31, 32, 108, 73, 87, 91, -6, 84, -65, -21, -48, 125
      };
      byte[] bytes189 = AesCipherUtil.decrypt(bytes, bytes188, aesCrypto);
      decoyKeyTable576 = sha3Hash.digest(bytes189);
      byte[] bytes190 = decoyKeyTable576;
      decoyKeyTable580 = sha3Hash.digest(bytes190);
      byte[] bytes191 = new byte[]{
         -43, 8, -45, -125, -76, 67, -85, 86, -6, 123, 22, 95, 70, -72, 86, -2, -96, -33, 58, 111, -2, 19, 34, -78, 60, 0, -40, 114, -111, 101, -122, 113
      };
      byte[] bytes192 = AesCipherUtil.decrypt(bytes, bytes191, aesCrypto);
      decoyKeyTable518 = sha3Hash.digest(bytes192);
      byte[] bytes193 = decoyKeyTable518;
      decoyKeyTable525 = sha3Hash.digest(bytes193);
      byte[] bytes194 = new byte[]{
         -34,
         93,
         -14,
         -53,
         -122,
         42,
         -84,
         8,
         -25,
         43,
         -66,
         -125,
         -97,
         -27,
         -77,
         -1,
         -120,
         29,
         61,
         -21,
         102,
         17,
         -109,
         -67,
         63,
         -81,
         -116,
         103,
         -128,
         -32,
         0,
         81
      };
      byte[] bytes195 = AesCipherUtil.decrypt(bytes, bytes194, aesCrypto);
      decoyKeyTable498 = sha3Hash.digest(bytes195);
      byte[] bytes196 = decoyKeyTable498;
      decoyKeyTable306 = sha3Hash.digest(bytes196);
      byte[] bytes197 = new byte[]{
         115,
         -102,
         -91,
         -122,
         58,
         -103,
         -72,
         -55,
         -29,
         -62,
         53,
         35,
         -94,
         -112,
         107,
         -83,
         52,
         21,
         -44,
         -117,
         -74,
         119,
         20,
         -111,
         15,
         85,
         3,
         108,
         81,
         89,
         -53,
         -112
      };
      byte[] bytes198 = AesCipherUtil.decrypt(bytes, bytes197, aesCrypto);
      decoyKeyTable719 = sha3Hash.digest(bytes198);
      byte[] bytes199 = decoyKeyTable719;
      decoyKeyTable625 = sha3Hash.digest(bytes199);
      byte[] bytes200 = new byte[]{
         101, -102, -90, 59, 96, -59, -121, 119, -16, 21, 75, 19, -127, 85, 115, 13, 27, 102, 96, 61, 19, 71, 94, -38, -12, -121, 70, 80, 45, 40, 92, -67
      };
      byte[] bytes201 = AesCipherUtil.decrypt(bytes, bytes200, aesCrypto);
      decoyKeyTable574 = sha3Hash.digest(bytes201);
      byte[] bytes202 = decoyKeyTable574;
      decoyKeyTable305 = sha3Hash.digest(bytes202);
      byte[] bytes203 = new byte[]{
         -41,
         -47,
         -18,
         102,
         100,
         -105,
         -107,
         -53,
         94,
         42,
         -48,
         -95,
         97,
         64,
         -72,
         120,
         90,
         -125,
         119,
         -14,
         121,
         80,
         40,
         127,
         -117,
         37,
         72,
         -109,
         125,
         65,
         -17,
         -64
      };
      byte[] bytes204 = AesCipherUtil.decrypt(bytes, bytes203, aesCrypto);
      decoyKeyTable371 = sha3Hash.digest(bytes204);
      byte[] bytes205 = decoyKeyTable371;
      decoyKeyTable196 = sha3Hash.digest(bytes205);
      byte[] bytes206 = new byte[]{
         -126, -52, -103, -34, 45, 104, 60, -102, 87, -58, -71, 21, 115, -8, 54, -45, -103, 16, -60, -76, -90, -6, -71, 4, 20, 89, -75, -53, -101, -6, 98, -36
      };
      byte[] bytes207 = AesCipherUtil.decrypt(bytes, bytes206, aesCrypto);
      decoyKeyTable462 = sha3Hash.digest(bytes207);
      byte[] bytes208 = decoyKeyTable462;
      decoyKeyTable618 = sha3Hash.digest(bytes208);
      byte[] bytes209 = new byte[]{
         70, -110, 112, -38, -34, 27, 87, -103, 78, 25, -126, -86, 121, -8, -104, -8, 10, 44, 61, -29, -80, 101, 6, 48, 46, 96, -53, -124, -76, 74, 95, -64
      };
      byte[] bytes210 = AesCipherUtil.decrypt(bytes, bytes209, aesCrypto);
      decoyKeyTable836 = sha3Hash.digest(bytes210);
      byte[] bytes211 = decoyKeyTable836;
      decoyKeyTable818 = sha3Hash.digest(bytes211);
      byte[] bytes212 = new byte[]{
         -12,
         -84,
         -24,
         -22,
         62,
         -75,
         26,
         -123,
         39,
         119,
         -86,
         -53,
         14,
         -65,
         108,
         -102,
         88,
         -72,
         35,
         -19,
         17,
         -90,
         94,
         102,
         101,
         -123,
         -68,
         48,
         111,
         -37,
         -123,
         -18
      };
      byte[] bytes213 = AesCipherUtil.decrypt(bytes, bytes212, aesCrypto);
      decoyKeyTable078 = sha3Hash.digest(bytes213);
      byte[] bytes214 = decoyKeyTable078;
      decoyKeyTable527 = sha3Hash.digest(bytes214);
      byte[] bytes215 = new byte[]{
         -96, 100, 75, 82, 57, 121, 50, 43, 47, 96, 9, -78, 41, 51, -44, -102, 24, 82, 94, -49, -87, 42, -42, -3, -53, -41, 99, 32, 88, 92, -38, -18
      };
      byte[] bytes216 = AesCipherUtil.decrypt(bytes, bytes215, aesCrypto);
      decoyKeyTable202 = sha3Hash.digest(bytes216);
      byte[] bytes217 = decoyKeyTable202;
      decoyKeyTable046 = sha3Hash.digest(bytes217);
      byte[] bytes218 = new byte[]{
         39, -111, 78, -92, 35, -119, -112, 99, 8, 0, -62, -38, 68, 54, -103, 100, 121, -48, -115, 14, 55, -20, -62, -16, 92, -126, -63, 20, 16, -55, -118, 67
      };
      byte[] bytes219 = AesCipherUtil.decrypt(bytes, bytes218, aesCrypto);
      decoyKeyTable309 = sha3Hash.digest(bytes219);
      byte[] bytes220 = decoyKeyTable309;
      decoyKeyTable664 = sha3Hash.digest(bytes220);
      byte[] bytes221 = new byte[]{
         8, -118, 116, 112, 90, 115, -115, 83, -27, 25, 102, -45, -67, 11, 89, 81, -70, -108, 119, 33, -110, -44, 111, 15, -57, -110, 32, 76, 51, -116, 105, 30
      };
      byte[] bytes222 = AesCipherUtil.decrypt(bytes, bytes221, aesCrypto);
      decoyKeyTable459 = sha3Hash.digest(bytes222);
      byte[] bytes223 = decoyKeyTable459;
      decoyKeyTable101 = sha3Hash.digest(bytes223);
      byte[] bytes224 = new byte[]{
         -33,
         -113,
         -22,
         -52,
         42,
         -90,
         -100,
         -124,
         64,
         102,
         -115,
         -68,
         -78,
         -125,
         -126,
         19,
         97,
         -51,
         50,
         115,
         -80,
         -77,
         -121,
         -19,
         -102,
         92,
         48,
         -113,
         23,
         -82,
         -55,
         -88
      };
      byte[] bytes225 = AesCipherUtil.decrypt(bytes, bytes224, aesCrypto);
      decoyKeyTable008 = sha3Hash.digest(bytes225);
      byte[] bytes226 = decoyKeyTable008;
      decoyKeyTable701 = sha3Hash.digest(bytes226);
      byte[] bytes227 = new byte[]{
         -41, 8, 101, -117, 59, 26, -53, 125, 92, 117, -74, -121, -44, -77, 112, 43, 46, 4, 74, 77, 29, -97, 2, -71, -127, -95, -66, 39, -94, -73, -97, -40
      };
      byte[] bytes228 = AesCipherUtil.decrypt(bytes, bytes227, aesCrypto);
      decoyKeyTable814 = sha3Hash.digest(bytes228);
      byte[] bytes229 = decoyKeyTable814;
      decoyKeyTable755 = sha3Hash.digest(bytes229);
      byte[] bytes230 = new byte[]{
         103, -77, 72, 60, -128, -54, 12, -79, -96, 98, -35, 40, 27, 79, 127, 108, 89, -86, 26, 105, 127, -94, -82, -107, 36, 62, 61, -50, -69, -18, 30, 77
      };
      byte[] bytes231 = AesCipherUtil.decrypt(bytes, bytes230, aesCrypto);
      decoyKeyTable615 = sha3Hash.digest(bytes231);
      byte[] bytes232 = decoyKeyTable615;
      decoyKeyTable052 = sha3Hash.digest(bytes232);
      byte[] bytes233 = new byte[]{
         27, 6, -29, 94, -112, 47, 48, -111, -42, 9, -109, 37, -20, 123, -55, -52, 107, -105, 50, -40, 63, 64, -10, -121, 57, 67, -69, 85, 16, -97, -123, -93
      };
      byte[] bytes234 = AesCipherUtil.decrypt(bytes, bytes233, aesCrypto);
      decoyKeyTable793 = sha3Hash.digest(bytes234);
      byte[] bytes235 = decoyKeyTable793;
      decoyKeyTable451 = sha3Hash.digest(bytes235);
      byte[] bytes236 = new byte[]{
         -18, 60, 36, 99, 90, 100, 98, -95, -127, 119, -37, -10, 100, -91, -81, 95, 1, 54, 121, -65, 60, 46, -49, 117, 98, -80, 19, -60, 5, 47, 65, 85
      };
      byte[] bytes237 = AesCipherUtil.decrypt(bytes, bytes236, aesCrypto);
      decoyKeyTable322 = sha3Hash.digest(bytes237);
      byte[] bytes238 = decoyKeyTable322;
      decoyKeyTable004 = sha3Hash.digest(bytes238);
      byte[] bytes239 = new byte[]{
         -40, -12, -55, -35, 64, -124, -27, -67, 86, -103, 96, 49, -51, 36, -21, -12, 18, -89, 94, -5, 59, -35, 73, 49, 122, 61, 87, -77, 28, 122, -25, 54
      };
      byte[] bytes240 = AesCipherUtil.decrypt(bytes, bytes239, aesCrypto);
      decoyKeyTable256 = sha3Hash.digest(bytes240);
      byte[] bytes241 = decoyKeyTable256;
      decoyKeyTable384 = sha3Hash.digest(bytes241);
      byte[] bytes242 = new byte[]{
         -43, 88, -45, -45, -128, 34, 14, 126, -94, 101, -15, -49, -12, -10, -90, -12, 115, 110, 124, 26, 51, 49, 92, -81, -30, 125, -2, 11, -28, 1, -29, 82
      };
      byte[] bytes243 = AesCipherUtil.decrypt(bytes, bytes242, aesCrypto);
      decoyKeyTable103 = sha3Hash.digest(bytes243);
      byte[] bytes244 = decoyKeyTable103;
      decoyKeyTable307 = sha3Hash.digest(bytes244);
   }

   public static byte[] longToBytes(long l) {
      byte[] bytes = null;
      ClientArchiveHolder.getControlFlowMarker();
      int n = 0;
      byte b = 7;

      while (true) {
         if (b >= 5) {
            if (b <= 5) {
               n--;
               continue;
            }

            if (6 >= b) {
               l >>= 8;
               continue;
            }

            bytes = new byte[8];
            n = 7;
         }

         while (b >= 1) {
            if (1 >= b) {
               return bytes;
            }

            if (n >= 0) {
               ;
            }
         }

         bytes[n] = (byte)((int)(l & 255L));
      }
   }

   public static void populateDecoyKeysGroupC(byte[] bytes) {
      AesCrypto aesCrypto = new AesCrypto();
      Sha3Hash sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes2 = new byte[]{
         -56,
         95,
         -115,
         67,
         105,
         4,
         -61,
         -91,
         -116,
         84,
         -73,
         -55,
         -65,
         -15,
         39,
         -57,
         -103,
         -38,
         -32,
         -45,
         98,
         -62,
         -52,
         -75,
         101,
         -12,
         30,
         -44,
         -124,
         -41,
         -105,
         20
      };
      byte[] bytes3 = AesCipherUtil.decrypt(bytes, bytes2, aesCrypto);
      decoyKeyGroupC06 = sha3Hash.digest(bytes3);
      byte[] bytes4 = decoyKeyGroupC06;
      decoyKeyGroupC09 = sha3Hash.digest(bytes4);
      byte[] bytes5 = new byte[]{
         76, 70, 90, 20, 93, 21, -30, -89, 64, 56, 91, -44, -124, 77, 58, -128, -14, -14, 58, 119, 26, 42, 38, 10, 90, -67, 125, 9, 93, -49, 5, -45
      };
      byte[] bytes6 = AesCipherUtil.decrypt(bytes, bytes5, aesCrypto);
      decoyKeyGroupC14 = sha3Hash.digest(bytes6);
      byte[] bytes7 = decoyKeyGroupC14;
      decoyKeyGroupC00 = sha3Hash.digest(bytes7);
      byte[] bytes8 = new byte[]{
         74, 69, -18, 28, -35, 109, -85, -47, 64, -23, 94, 110, 87, 64, 123, 8, 110, -32, -118, -76, -12, -38, -58, -2, 5, -64, -61, -66, 72, 60, 33, -108
      };
      byte[] bytes9 = AesCipherUtil.decrypt(bytes, bytes8, aesCrypto);
      decoyKeyGroupC11 = sha3Hash.digest(bytes9);
      byte[] bytes10 = decoyKeyGroupC11;
      decoyKeyGroupC07 = sha3Hash.digest(bytes10);
      byte[] bytes11 = new byte[]{
         -28, 15, -34, 103, -50, -3, -62, 33, 20, 92, -37, 33, -13, 20, -4, 9, -9, -102, 88, 72, -125, 82, 36, 2, 58, 22, 83, -76, 20, 37, -69, -14
      };
      byte[] bytes12 = AesCipherUtil.decrypt(bytes, bytes11, aesCrypto);
      decoyKeyGroupC02 = sha3Hash.digest(bytes12);
      byte[] bytes13 = decoyKeyGroupC02;
      decoyKeyGroupC05 = sha3Hash.digest(bytes13);
      byte[] bytes14 = new byte[]{
         29, 17, -114, -85, -64, 9, 73, 71, 117, -4, 98, 95, -40, 42, 95, -97, 105, 71, 50, 109, 76, -72, -24, 96, -119, -87, -3, -99, -62, 13, 25, -2
      };
      byte[] bytes15 = AesCipherUtil.decrypt(bytes, bytes14, aesCrypto);
      decoyKeyGroupC12 = sha3Hash.digest(bytes15);
      byte[] bytes16 = decoyKeyGroupC12;
      decoyKeyGroupC10 = sha3Hash.digest(bytes16);
      byte[] bytes17 = new byte[]{
         96, -4, 29, -119, -47, -20, 87, -75, 55, 93, 34, -37, -114, 67, 44, -74, 108, -96, -73, 32, 40, 81, 83, -122, -39, -11, 39, -61, -35, -78, -4, 64
      };
      byte[] bytes18 = AesCipherUtil.decrypt(bytes, bytes17, aesCrypto);
      decoyKeyGroupC01 = sha3Hash.digest(bytes18);
      byte[] bytes19 = decoyKeyGroupC01;
      decoyKeyGroupC08 = sha3Hash.digest(bytes19);
      byte[] bytes20 = new byte[]{
         100,
         -56,
         -38,
         -111,
         -54,
         -33,
         -78,
         73,
         -76,
         -127,
         7,
         79,
         -66,
         52,
         -120,
         -32,
         108,
         52,
         59,
         -115,
         37,
         -64,
         93,
         -117,
         -31,
         25,
         -72,
         118,
         -27,
         97,
         -2,
         -53
      };
      byte[] bytes21 = AesCipherUtil.decrypt(bytes, bytes20, aesCrypto);
      decoyKeyGroupC04 = sha3Hash.digest(bytes21);
      byte[] bytes22 = decoyKeyGroupC04;
      decoyKeyGroupC13 = sha3Hash.digest(bytes22);
      byte[] bytes23 = new byte[]{
         108, -109, 69, 111, -80, -122, -44, 19, 57, -6, -121, 20, -108, 61, 110, 63, 49, -61, 41, 77, -87, 120, 47, -60, 103, 99, -15, 119, -118, -123, 52, -6
      };
      byte[] bytes24 = AesCipherUtil.decrypt(bytes, bytes23, aesCrypto);
      decoyKeyGroupC03 = sha3Hash.digest(bytes24);
      byte[] bytes25 = decoyKeyGroupC03;
      decoyKeyTable422 = sha3Hash.digest(bytes25);
      byte[] bytes26 = new byte[]{
         81, 84, -25, -81, 89, 63, 50, -97, -4, 113, -26, -28, -89, 81, 111, 48, 60, 76, 6, 80, -12, -66, 110, 46, 107, 111, -30, -83, -95, -16, -87, -86
      };
      byte[] bytes27 = AesCipherUtil.decrypt(bytes, bytes26, aesCrypto);
      decoyKeyTable200 = sha3Hash.digest(bytes27);
      byte[] bytes28 = decoyKeyTable200;
      decoyKeyTable510 = sha3Hash.digest(bytes28);
      byte[] bytes29 = new byte[]{
         49, 116, 20, 69, 70, -55, 33, 51, 49, -54, -72, 127, -74, -70, -60, 19, -7, -54, -51, -73, -29, 0, -96, 41, 44, -71, -72, 13, -78, -90, 75, 31
      };
      byte[] bytes30 = AesCipherUtil.decrypt(bytes, bytes29, aesCrypto);
      decoyKeyTable692 = sha3Hash.digest(bytes30);
      byte[] bytes31 = decoyKeyTable692;
      decoyKeyTable469 = sha3Hash.digest(bytes31);
      byte[] bytes32 = new byte[]{
         33, 51, 2, -15, 91, 46, 62, -89, -118, -100, -126, -2, -7, 126, 12, 100, -75, -82, -33, -101, -26, -57, -112, -114, 100, 44, 70, 73, 20, -68, 75, -61
      };
      byte[] bytes33 = AesCipherUtil.decrypt(bytes, bytes32, aesCrypto);
      decoyKeyTable721 = sha3Hash.digest(bytes33);
      byte[] bytes34 = decoyKeyTable721;
      decoyKeyTable393 = sha3Hash.digest(bytes34);
      byte[] bytes35 = new byte[]{
         113, -33, 66, 92, -60, -12, 37, 91, -8, -70, -24, -39, -97, 87, 59, 100, 87, 79, -37, -118, 18, 6, 5, -93, 0, -92, -51, 69, -127, 42, 29, 45
      };
      byte[] bytes36 = AesCipherUtil.decrypt(bytes, bytes35, aesCrypto);
      decoyKeyTable650 = sha3Hash.digest(bytes36);
      byte[] bytes37 = decoyKeyTable650;
      decoyKeyTable006 = sha3Hash.digest(bytes37);
      byte[] bytes38 = new byte[]{
         47, 116, 54, 33, 38, -41, 105, -102, 34, 17, 77, 84, -102, 55, 127, 57, 124, 60, 29, -47, -26, -116, 71, -7, -93, 76, -106, -121, 121, 116, 8, 34
      };
      byte[] bytes39 = AesCipherUtil.decrypt(bytes, bytes38, aesCrypto);
      decoyKeyTable682 = sha3Hash.digest(bytes39);
      byte[] bytes40 = decoyKeyTable682;
      decoyKeyTable509 = sha3Hash.digest(bytes40);
      byte[] bytes41 = new byte[]{
         33, 79, 11, -34, 19, -57, 36, 79, -87, -56, -9, -88, -96, 124, -68, -23, -48, 26, 12, -56, -108, -14, -71, 71, -56, 120, -14, 24, 55, -76, 41, -94
      };
      byte[] bytes42 = AesCipherUtil.decrypt(bytes, bytes41, aesCrypto);
      decoyKeyTable338 = sha3Hash.digest(bytes42);
      byte[] bytes43 = decoyKeyTable338;
      decoyKeyTable262 = sha3Hash.digest(bytes43);
      byte[] bytes44 = new byte[]{
         -110,
         114,
         -69,
         10,
         123,
         -41,
         -31,
         -60,
         94,
         92,
         -3,
         -22,
         -119,
         -46,
         -29,
         -117,
         -11,
         24,
         -104,
         50,
         116,
         -72,
         52,
         102,
         -55,
         75,
         -81,
         -80,
         -19,
         60,
         86,
         72
      };
      byte[] bytes45 = AesCipherUtil.decrypt(bytes, bytes44, aesCrypto);
      decoyKeyTable170 = sha3Hash.digest(bytes45);
      byte[] bytes46 = decoyKeyTable170;
      decoyKeyTable817 = sha3Hash.digest(bytes46);
      byte[] bytes47 = new byte[]{
         62, -122, -6, 47, -62, 84, -109, 107, 16, 127, 115, -48, -123, -9, 80, 103, 61, 67, -100, 74, 1, -38, -60, 23, 66, -74, -92, -111, -118, -34, -114, 74
      };
      byte[] bytes48 = AesCipherUtil.decrypt(bytes, bytes47, aesCrypto);
      decoyKeyTable702 = sha3Hash.digest(bytes48);
      byte[] bytes49 = decoyKeyTable702;
      decoyKeyTable819 = sha3Hash.digest(bytes49);
      byte[] bytes50 = new byte[]{
         83, 25, 34, 68, -93, -73, 18, 14, -22, 122, 46, 35, -118, -67, -56, 37, -78, 122, 62, -14, 23, -56, 94, -51, 113, 98, -103, 62, -35, -10, 122, -40
      };
      byte[] bytes51 = AesCipherUtil.decrypt(bytes, bytes50, aesCrypto);
      decoyKeyTable654 = sha3Hash.digest(bytes51);
      byte[] bytes52 = decoyKeyTable654;
      decoyKeyTable370 = sha3Hash.digest(bytes52);
      byte[] bytes53 = new byte[]{
         -68, 69, 37, -57, 120, -6, -17, 50, -80, -90, 78, 7, -77, -84, 109, 17, -36, -27, -73, -61, -100, 111, 122, 20, -5, 105, 4, -115, -24, -83, -20, -103
      };
      byte[] bytes54 = AesCipherUtil.decrypt(bytes, bytes53, aesCrypto);
      decoyKeyTable156 = sha3Hash.digest(bytes54);
      byte[] bytes55 = decoyKeyTable156;
      decoyKeyTable333 = sha3Hash.digest(bytes55);
      byte[] bytes56 = new byte[]{
         -111, 85, 121, -20, 75, 75, 17, -5, -83, 103, 9, 95, -34, -32, 127, -66, 53, 23, 83, -119, 66, 39, -73, 1, -87, 9, -55, 64, 7, -108, -73, -94
      };
      byte[] bytes57 = AesCipherUtil.decrypt(bytes, bytes56, aesCrypto);
      decoyKeyTable792 = sha3Hash.digest(bytes57);
      byte[] bytes58 = decoyKeyTable792;
      decoyKeyTable651 = sha3Hash.digest(bytes58);
      byte[] bytes59 = new byte[]{
         53, -113, 69, 49, -73, 86, 43, -40, -43, 16, 74, -40, -1, 97, -84, -14, 114, 3, 85, -112, -108, 62, -68, -112, -16, -85, 35, 88, 44, -62, 16, -33
      };
      byte[] bytes60 = AesCipherUtil.decrypt(bytes, bytes59, aesCrypto);
      decoyKeyTable547 = sha3Hash.digest(bytes60);
      byte[] bytes61 = decoyKeyTable547;
      decoyKeyTable007 = sha3Hash.digest(bytes61);
      byte[] bytes62 = new byte[]{
         81, -6, 64, 117, -29, -54, -104, -126, 75, 33, 7, 107, -10, -109, 110, 88, -45, 19, 13, -32, 33, 2, 51, 25, -62, -72, -127, 1, 69, -5, 119, -123
      };
      byte[] bytes63 = AesCipherUtil.decrypt(bytes, bytes62, aesCrypto);
      decoyKeyTable837 = sha3Hash.digest(bytes63);
      byte[] bytes64 = decoyKeyTable837;
      decoyKeyTable010 = sha3Hash.digest(bytes64);
      byte[] bytes65 = new byte[]{
         125, 87, -126, -74, 23, 9, -124, 14, 6, 23, -87, 61, -98, -49, 111, -35, 51, 63, 121, 85, 36, 112, 102, -123, 12, -60, 124, 43, -71, -58, 39, -41
      };
      byte[] bytes66 = AesCipherUtil.decrypt(bytes, bytes65, aesCrypto);
      decoyKeyTable471 = sha3Hash.digest(bytes66);
      byte[] bytes67 = decoyKeyTable471;
      decoyKeyTable549 = sha3Hash.digest(bytes67);
      byte[] bytes68 = new byte[]{
         -110,
         115,
         -84,
         -23,
         -38,
         -52,
         -10,
         -44,
         -19,
         31,
         62,
         -63,
         25,
         -21,
         -17,
         126,
         -109,
         -120,
         -22,
         97,
         86,
         67,
         -95,
         -90,
         -128,
         -69,
         29,
         59,
         -40,
         -11,
         -123,
         70
      };
      byte[] bytes69 = AesCipherUtil.decrypt(bytes, bytes68, aesCrypto);
      decoyKeyTable850 = sha3Hash.digest(bytes69);
      byte[] bytes70 = decoyKeyTable850;
      decoyKeyTable507 = sha3Hash.digest(bytes70);
      byte[] bytes71 = new byte[]{
         41, 47, 94, 22, -93, -26, -94, 77, 58, 35, -107, 87, -72, 33, -103, 84, -102, -51, 115, -108, 23, 79, -60, -54, 23, -126, -45, 47, 73, 126, 52, -27
      };
      byte[] bytes72 = AesCipherUtil.decrypt(bytes, bytes71, aesCrypto);
      decoyKeyTable050 = sha3Hash.digest(bytes72);
      byte[] bytes73 = decoyKeyTable050;
      decoyKeyTable328 = sha3Hash.digest(bytes73);
      byte[] bytes74 = new byte[]{
         28, -40, 78, -6, -125, -114, 111, 85, 14, 43, 44, 104, 11, -55, 74, 48, -47, -69, -49, -116, 66, -72, -92, -71, 23, 112, 70, 113, 45, -15, -116, 103
      };
      byte[] bytes75 = AesCipherUtil.decrypt(bytes, bytes74, aesCrypto);
      decoyKeyTable785 = sha3Hash.digest(bytes75);
      byte[] bytes76 = decoyKeyTable785;
      decoyKeyTable596 = sha3Hash.digest(bytes76);
      byte[] bytes77 = new byte[]{
         6, -122, 121, -65, 117, 49, 98, 83, 76, 6, -43, 10, 89, -126, 53, -94, 50, -87, -73, 93, -64, -103, 71, 6, 7, -89, -127, 65, 81, 37, -5, -80
      };
      byte[] bytes78 = AesCipherUtil.decrypt(bytes, bytes77, aesCrypto);
      decoyKeyTable369 = sha3Hash.digest(bytes78);
      byte[] bytes79 = decoyKeyTable369;
      decoyKeyTable392 = sha3Hash.digest(bytes79);
      byte[] bytes80 = new byte[]{
         -58, -68, 55, -26, -93, -93, -124, -47, -107, -36, 82, -68, -44, -105, -52, 20, 84, 69, -5, 59, -73, 97, 43, -85, 105, 20, -17, 42, -82, 77, 116, -24
      };
      byte[] bytes81 = AesCipherUtil.decrypt(bytes, bytes80, aesCrypto);
      decoyKeyTable616 = sha3Hash.digest(bytes81);
      byte[] bytes82 = decoyKeyTable616;
      decoyKeyTable311 = sha3Hash.digest(bytes82);
      byte[] bytes83 = new byte[]{
         91, 120, -10, 61, -126, -21, 127, -56, -11, -127, 51, -64, 87, 9, 61, -4, -49, -116, -61, -28, 81, -112, 123, 64, 100, 37, 21, 119, 45, 45, 32, -36
      };
      byte[] bytes84 = AesCipherUtil.decrypt(bytes, bytes83, aesCrypto);
      decoyKeyTable592 = sha3Hash.digest(bytes84);
      byte[] bytes85 = decoyKeyTable592;
      decoyKeyTable686 = sha3Hash.digest(bytes85);
      byte[] bytes86 = new byte[]{
         -76, -34, -42, -8, 36, 111, 117, -91, 30, -23, -110, 61, -75, 76, 76, -56, 2, 16, -26, 0, -29, 50, 76, 16, 42, -44, -102, 11, 52, -21, 29, 4
      };
      byte[] bytes87 = AesCipherUtil.decrypt(bytes, bytes86, aesCrypto);
      decoyKeyTable503 = sha3Hash.digest(bytes87);
      byte[] bytes88 = decoyKeyTable503;
      decoyKeyTable076 = sha3Hash.digest(bytes88);
      byte[] bytes89 = new byte[]{
         -81, 93, 113, 37, -61, 89, -72, 122, 21, 31, -121, -68, -21, -96, 75, 35, 121, -64, 21, 10, -77, -22, -61, -35, -66, 53, -5, -19, 49, -45, -119, -6
      };
      byte[] bytes90 = AesCipherUtil.decrypt(bytes, bytes89, aesCrypto);
      decoyKeyTable680 = sha3Hash.digest(bytes90);
      byte[] bytes91 = decoyKeyTable680;
      decoyKeyTable840 = sha3Hash.digest(bytes91);
      byte[] bytes92 = new byte[]{
         -128,
         81,
         37,
         93,
         114,
         -36,
         -68,
         63,
         -50,
         26,
         57,
         -54,
         65,
         80,
         -128,
         -107,
         32,
         -20,
         -58,
         125,
         -102,
         -38,
         -58,
         -111,
         -33,
         31,
         -52,
         114,
         -105,
         127,
         -124,
         92
      };
      byte[] bytes93 = AesCipherUtil.decrypt(bytes, bytes92, aesCrypto);
      decoyKeyTable378 = sha3Hash.digest(bytes93);
      byte[] bytes94 = decoyKeyTable378;
      decoyKeyTable024 = sha3Hash.digest(bytes94);
   }

   private byte[] getResourceEntryBytes(String str) {
      ClientArchiveHolder instance = ClientArchiveHolder.getInstance();
      return (byte[])instance.getResourceEntries().get(str);
   }

   private static byte[] inflateBytes(long l, byte[] bytes) {
      l = a ^ l;
      int[] controlFlowMarker = ClientArchiveHolder.getControlFlowMarker();

      try {
         Inflater inflater = new Inflater(true);
         inflater.setInput(bytes);
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         byte[] bytes2 = new byte[1024];

         ByteArrayOutputStream byteArrayOutputStream2;
         while (true) {
            Inflater inflater2;
            if (!inflater.finished()) {
               inflater2 = inflater;
            } else {
               inflater2 = inflater;
               if (l > 0L) {
                  inflater.end();
                  byteArrayOutputStream2 = byteArrayOutputStream;
                  break;
               }
            }

            int n = inflater2.inflate(bytes2);
            byteArrayOutputStream2 = byteArrayOutputStream;
            if (l < 0L) {
               break;
            }

            byteArrayOutputStream.write(bytes2, 0, n);
         }

         return byteArrayOutputStream2.toByteArray();
      } catch (DataFormatException e) {
         return null;
      }
   }

   @Override
   public Class<?> findClass(String str) throws ClassNotFoundException {
      long l = a ^ 2935525010044L;
      long l2 = l ^ 70451718304124L;
      Object obj = null;
      Object obj2 = null;
      int n = 0;
      Object obj3 = null;
      Object obj4 = null;
      ClientArchiveHolder.getControlFlowMarker();
      int n2 = 0;
      int n3 = 0;
      Object obj5 = null;
      Object obj6 = null;
      Object obj7 = null;
      int n4 = 0;
      Object obj8 = null;
      int n5 = 0;
      long l3 = 0L;
      l3 = 7300511800450137489L ^ (long)"禥㝄\uf1d8쫤踟ࠗ㇇쒒ꏆ뺀ួ杒䇊킱⤐酾虸檷\uf105띹铦\uf0ecꇎ齸\uf268ﳤṿ豽꒿".hashCode();
      obj5 = str.replace(".", "/") + ".class";
      l3 = (l3 ^ -1456925999993046001L) + 1907422415140171627L;
      classLoadCounter++;
      l3 = l3 - -8628313077325175563L ^ 1635847101859683886L;
      if (classLoadCounter <= 200L) {
         l3 = (l3 ^ -9116527086766669175L) + -8314473268090318248L;
      } else {
         l3 = (l3 ^ -2395221782320989236L) - -5686485438592370655L;
         classLoadCounter = 0L;
         l3 = l3 + -2955658212531038796L ^ -7797112600029191449L;
         checkAntiDebug();
         l3 = l3 - -6397766021523518178L ^ -5361926995320650245L;
      }

      exitBlock:
      if (!((String)obj5).startsWith("net/a/")) {
         l3 = (l3 ^ -2807226219637622178L) + -3309311199735853417L;
         if (classDecryptionKey == null) {
            l3 = (l3 ^ 4066524801596708306L) + 3482500738451710527L;
         } else {
            exitBlock2: {
               l3 = (l3 ^ -6076429020765399108L) + -8858923670754072115L;
               if (((String)obj5).startsWith("net/")) {
                  l3 = (l3 ^ -749705779174999098L) + 5705832144080646568L;
               } else {
                  l3 = (l3 ^ 3832277284681367324L) - 5964981697611910687L;
                  if (((String)obj5).startsWith("cc/")) {
                     l3 = (l3 ^ 5409639661580088400L) + -7234268140474429615L;
                  } else {
                     l3 = (l3 ^ -7717608338741396769L) - 3780711241462070309L;
                     if (((String)obj5).startsWith("dd/")) {
                        l3 = (l3 ^ 2678756206053628855L) - 5914822418350383500L;
                     } else {
                        l3 = (l3 ^ 3455950334744947865L) - -7907538788051908003L;
                        if (!((String)obj5).startsWith("ee/")) {
                           l3 = (l3 ^ -7426209864150949455L) + 6755800160730345960L;
                           break exitBlock2;
                        }

                        l3 = l3 + -3285709026620964599L ^ -7417319319087087282L;
                     }
                  }
               }

               obj7 = ((String)obj5).getBytes(StandardCharsets.UTF_8);
               l3 = l3 + 5517918151045727505L ^ -8242393897057817074L;
               String str2 = (String)obj5;
               byte[] bytes = entryNameKey;
               obj8 = encryptEntryName(str2, bytes);
               l3 = (l3 ^ 689815975808754428L) + -3327566898171642668L;
               obj = ClientArchiveHolder.getInstance().getClientEntries();
               l3 = l3 - -6446610192906181898L ^ -6122982137430197389L;
               obj4 = (byte[])((HashMap)obj).get((String)obj8);
               l3 = l3 - -6227970171634269772L ^ 4151819224679968009L;
               if ((byte[])obj4 == null) {
                  l3 = l3 + -1854206689942464334L ^ 8373111356975338197L;
                  return null;
               }

               l3 = (l3 ^ -1843024154330584186L) + -990389440114277923L;
               ((HashMap)obj).put((String)obj8, new byte[((byte[])obj4).length]);
               l3 = (l3 ^ -7672384581891051449L) - 3742813599778630765L;
               if (((byte[])obj4).length == 0) {
                  l3 = (l3 ^ 4846073999769680829L) + 2957371465858209945L;
               } else {
                  l3 = (l3 ^ 3917218047426995922L) + -8002984314681593365L;
                  n5 = ((byte[])obj4).length;
                  l3 = (l3 ^ -2894434487637060080L) - 294682356037062055L;
                  n4 = ((byte[])obj7).length;
                  l3 = (l3 ^ -3131626823971209274L) + 6094091835579948223L;
                  n = 0;
                  l3 = l3 - 5850104245590610094L ^ -5470692948814296324L;

                  while (n < n5) {
                     l3 = l3 - 4918655179510592223L ^ 4656428532701091202L;
                     ((byte[])obj4)[n] = (byte)(((byte[])obj4)[n] ^ ((byte[])obj7)[(n + n4 / 2) % n4]);
                     l3 = l3 + 8303021267460184261L ^ 3646438967139042739L;
                     n++;
                     l3 = (l3 ^ -5433960374867092017L) + 6134104297419264529L;
                     l3 = l3 + -6406310949346083449L ^ -6534898248514470648L;
                  }

                  l3 = l3 + 5997943621618551104L ^ -987699465798058969L;
                  str2 = (String)obj5;
                  byte[] bytes2 = (byte[])obj7;
                  byte[] bytes3 = (byte[])obj4;
                  byte[] bytes4 = bytes2;
                  deobfuscateClassBytes(str2, n5, bytes4, n4, bytes3);
                  l3 = l3 + 8445860612660061780L ^ 6068966546452991240L;
               }

               Sha3Hash sha3Hash = SHA3_HASH;
               byte[] bytes5 = (byte[])obj7;
               AesCrypto aesCrypto = AES_CRYPTO;
               byte[] bytes6 = classDecryptionKey;
               byte[] bytes7 = AesCipherUtil.decrypt(bytes5, bytes6, aesCrypto);
               obj2 = sha3Hash.digest(bytes7);
               l3 = (l3 ^ 1084930228278588120L) + 3647155103705347255L;
               byte[] bytes8 = (byte[])obj4;
               byte[] bytes9 = (byte[])obj2;
               AesCrypto aesCrypto2 = AES_CRYPTO;
               byte[] bytes10 = bytes9;
               obj3 = AesCipherUtil.encrypt(bytes8, bytes10, aesCrypto2);
               l3 = (l3 ^ -2664740769099208092L) - 30595015467748035L;
               obj3 = inflateBytes(l2, (byte[])obj3);
               l3 = (l3 ^ -3758911825925560705L) + -5652879527013701579L;
               l3 = (l3 ^ 164106261869116845L) + 7714326429585402760L;
               break exitBlock;
            }
         }

         obj6 = ClientArchiveHolder.getInstance().getClientEntries();
         l3 = (l3 ^ -2704490820375963865L) + -5774443660437757730L;
         obj3 = (byte[])((HashMap)obj6).get((String)obj5);
         l3 = (l3 ^ -5552165884051482163L) + -5787041245874510019L;
         if ((byte[])obj3 == null) {
            l3 = l3 - -2789860183671106515L ^ 8580532063311284903L;
         } else {
            l3 = (l3 ^ 31518262872881228L) + -1923178897665342841L;
            ((HashMap)obj6).put((String)obj5, new byte[((byte[])obj3).length]);
            l3 = (l3 ^ 7099229747747170734L) - 5603432299028052617L;
         }
      } else {
         l3 = (l3 ^ 5674333517752008303L) + 7368302867803331296L;
         LauncherClientHandler instance = LauncherClientHandler.getInstance();
         C2SClassLoaderReadyPacket packet = new C2SClassLoaderReadyPacket(loginSessionToken, sessionAuthToken, System.currentTimeMillis());
         instance.sendPacket(packet);
         l3 = l3 - 200349182007624912L ^ 6566280906136188204L;
         obj6 = new RemoteClassFetchBridge();
         l3 = l3 - -3391839776843540086L ^ 3331617541467530193L;
         RemoteClassFetchBridge.initialize();
         l3 = l3 - 8097756276346499080L ^ -8097348151344850855L;
         l3 = (l3 ^ 3686349874123443496L) - -1702643905592018500L;
         String str3 = (String)obj5;
         obj3 = RemoteClassFetchBridge.fetchRemoteClass(loginSessionToken, str3);
         l3 = l3 + -8541714701428607533L ^ -9149778141801611001L;
         l3 = (l3 ^ 7706801641777133526L) + 3485323632590646806L;
      }

      if ((byte[])obj3 == null) {
         l3 = l3 + 8517835938733970086L ^ 7058125261168306750L;
         if (this.parentClassLoader == null) {
            l3 = l3 + 7782267711878943373L ^ -1930442896268204706L;
            throw new ClassNotFoundException();
         } else {
            l3 = (l3 ^ -8695321495051895299L) - -268750940105075233L;
            obj6 = this.parentClassLoader.loadClass(str);
            l3 = l3 - 9195618555422066156L ^ -6255772091063597997L;
            if ((Class)obj6 != null) {
               l3 = l3 - -8237451100830948039L ^ -9144212700563439030L;
               return (Class<?>)obj6;
            } else {
               l3 = (l3 ^ -5666999484360546399L) - -2392393741636364738L;
               throw new ClassNotFoundException();
            }
         }
      } else {
         l3 = l3 + -7490488959400436537L ^ 4125439660497396423L;
         n2 = str.lastIndexOf(46);
         l3 = (l3 ^ -8156011537406639803L) - -4595853659999367639L;
         if (n2 == -1) {
            l3 = (l3 ^ -3564878229369331320L) - 2230343603831074240L;
         } else {
            l3 = (l3 ^ 1462419062835383824L) + 6824545592065854855L;
            obj8 = str.substring(0, n2);
            l3 = (l3 ^ -4536505212722432885L) + 6399906670659475994L;
            if (this.getPackage((String)obj8) != null) {
               l3 = l3 + -5392165832329576571L ^ 7460229960785469947L;
            } else {
               l3 = (l3 ^ 2477333402158488728L) + -3378633627945128839L;
               this.definePackage((String)obj8, null, null, null, null, null, null, null);
               l3 = l3 - -4705118774920651816L ^ -7157945422097953409L;
            }
         }

         String str4 = str;
         byte[] bytes11 = (byte[])obj3;
         loadedClassHashes.put(str4, SHA3_HASH.digest(bytes11));
         l3 = (l3 ^ 5863954937395735831L) + 1032620833572144752L;
         obj8 = this.defineClass(str, (byte[])obj3, 0, ((byte[])obj3).length, this.getClass().getProtectionDomain());
         l3 = l3 + 5977926695237162910L ^ -6583595202965402798L;
         n3 = 0;
         l3 = l3 - -3034895302459060140L ^ 6199443262310997586L;

         while (n3 < ((byte[])obj3).length) {
            l3 = l3 - 3547505697477104L ^ 173885597367065111L;
            ((byte[])obj3)[n3] = 0;
            l3 = l3 + 4670025409217602167L ^ -7505254635013909447L;
            n3++;
            l3 = l3 - 7432058115876597250L ^ -3404422792806580900L;
            l3 = l3 + 2351228441889882480L ^ -160528898477241529L;
         }

         l3 = l3 + 1232455641708548326L ^ 8841700487553351606L;
         return (Class<?>)obj8;
      }
   }

   public static void populateDecoyTokensGroupA(byte[] bytes) {
      AesCrypto aesCrypto = new AesCrypto();
      Sha3Hash sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes2 = new byte[]{
         49,
         59,
         -118,
         3,
         71,
         -107,
         -7,
         -111,
         97,
         -47,
         110,
         -62,
         -49,
         8,
         -91,
         -46,
         -24,
         -83,
         -29,
         53,
         -106,
         27,
         -28,
         93,
         -23,
         -89,
         121,
         -91,
         -105,
         -125,
         13,
         -81
      };
      byte[] bytes3 = AesCipherUtil.decrypt(bytes, bytes2, aesCrypto);
      decoyTokenGroupA07 = sha3Hash.digest(bytes3);
      byte[] bytes4 = decoyTokenGroupA07;
      decoyTokenGroupA03 = sha3Hash.digest(bytes4);
      byte[] bytes5 = new byte[]{
         57, -71, -72, 24, 59, -79, -62, -17, 70, 18, -51, -30, 38, 105, 8, -45, -82, 66, 113, 13, 120, -34, 56, -45, -75, -53, 106, 91, 111, -53, 55, -26
      };
      byte[] bytes6 = AesCipherUtil.decrypt(bytes, bytes5, aesCrypto);
      decoyTokenGroupA01 = sha3Hash.digest(bytes6);
      byte[] bytes7 = decoyTokenGroupA01;
      decoyTokenGroupA04 = sha3Hash.digest(bytes7);
      byte[] bytes8 = new byte[]{
         73, 10, -73, 86, -115, -111, 33, 59, 50, -7, 35, -30, 77, 112, 25, 96, -116, -53, -18, 71, -21, -49, -17, -127, -45, -77, 56, 5, 23, -100, -127, -123
      };
      byte[] bytes9 = AesCipherUtil.decrypt(bytes, bytes8, aesCrypto);
      decoyTokenGroupA14 = sha3Hash.digest(bytes9);
      byte[] bytes10 = decoyTokenGroupA14;
      decoyTokenGroupA12 = sha3Hash.digest(bytes10);
      byte[] bytes11 = new byte[]{
         52, -64, -93, 55, 58, 117, 102, 46, 64, 121, 78, -65, 49, -39, -124, 17, 14, -125, 45, -37, 118, 75, 92, 34, 59, 65, 0, 51, 9, -63, 73, -13
      };
      byte[] bytes12 = AesCipherUtil.decrypt(bytes, bytes11, aesCrypto);
      decoyTokenGroupA02 = sha3Hash.digest(bytes12);
      byte[] bytes13 = decoyTokenGroupA02;
      decoyTokenGroupA11 = sha3Hash.digest(bytes13);
      byte[] bytes14 = new byte[]{
         102, 80, -1, -17, -52, 80, 28, 26, -68, 111, -22, -104, 73, 123, 51, 1, 3, 46, 12, 81, -71, 24, 27, 8, 110, -101, -29, -80, -92, 6, 26, -112
      };
      byte[] bytes15 = AesCipherUtil.decrypt(bytes, bytes14, aesCrypto);
      decoyTokenGroupA06 = sha3Hash.digest(bytes15);
      byte[] bytes16 = decoyTokenGroupA06;
      decoyTokenGroupA13 = sha3Hash.digest(bytes16);
      byte[] bytes17 = new byte[]{
         89, 33, -44, -87, 116, -121, 62, 127, 41, -80, 112, 39, 35, 106, 11, -91, -17, 21, -117, 109, 30, 98, 19, 50, -45, 31, -123, -53, 80, -60, -84, -6
      };
      byte[] bytes18 = AesCipherUtil.decrypt(bytes, bytes17, aesCrypto);
      decoyTokenGroupA00 = sha3Hash.digest(bytes18);
      byte[] bytes19 = decoyTokenGroupA00;
      decoyTokenGroupA10 = sha3Hash.digest(bytes19);
      byte[] bytes20 = new byte[]{
         -105,
         -87,
         -29,
         -66,
         -58,
         98,
         -90,
         -105,
         -127,
         -106,
         -11,
         -52,
         72,
         52,
         103,
         83,
         72,
         -86,
         -84,
         19,
         -66,
         84,
         -104,
         -98,
         -29,
         -110,
         -63,
         -95,
         50,
         -107,
         -102,
         -42
      };
      byte[] bytes21 = AesCipherUtil.decrypt(bytes, bytes20, aesCrypto);
      decoyTokenGroupA05 = sha3Hash.digest(bytes21);
      byte[] bytes22 = decoyTokenGroupA05;
      decoyTokenGroupA09 = sha3Hash.digest(bytes22);
      byte[] bytes23 = new byte[]{
         93, -62, -126, -15, -80, 0, 74, 79, 70, 39, -107, 60, 81, 39, -99, -70, -101, -108, 41, -58, 85, -67, 22, 122, -80, 93, 4, -94, -31, -81, 37, -68
      };
      byte[] bytes24 = AesCipherUtil.decrypt(bytes, bytes23, aesCrypto);
      decoyTokenGroupA08 = sha3Hash.digest(bytes24);
      byte[] bytes25 = decoyTokenGroupA08;
      decoyKeyTable715 = sha3Hash.digest(bytes25);
      byte[] bytes26 = new byte[]{
         82, 55, 87, 12, 94, 73, -120, -102, 60, -7, 87, -100, 86, -111, 36, -121, 60, 79, -105, 80, 111, 19, -7, 103, -39, 79, -64, 101, -116, -77, -37, -54
      };
      byte[] bytes27 = AesCipherUtil.decrypt(bytes, bytes26, aesCrypto);
      decoyKeyTable477 = sha3Hash.digest(bytes27);
      byte[] bytes28 = decoyKeyTable477;
      decoyKeyTable141 = sha3Hash.digest(bytes28);
      byte[] bytes29 = new byte[]{
         -116, -12, 99, -71, -66, 55, 30, 41, 63, 54, 123, -125, -11, -111, 4, 91, -19, 122, 21, 64, -124, -29, 39, -89, -89, 101, 87, 86, 109, -90, 38, -55
      };
      byte[] bytes30 = AesCipherUtil.decrypt(bytes, bytes29, aesCrypto);
      decoyKeyTable722 = sha3Hash.digest(bytes30);
      byte[] bytes31 = decoyKeyTable722;
      decoyKeyTable184 = sha3Hash.digest(bytes31);
      byte[] bytes32 = new byte[]{
         -27, 85, -27, 0, -65, 62, 111, -113, 104, 5, -76, 45, -108, -43, -54, -70, 10, 30, 6, 92, 102, -13, 92, 8, -79, 29, 37, -82, -77, -112, 72, 28
      };
      byte[] bytes33 = AesCipherUtil.decrypt(bytes, bytes32, aesCrypto);
      decoyKeyTable087 = sha3Hash.digest(bytes33);
      byte[] bytes34 = decoyKeyTable087;
      decoyKeyTable731 = sha3Hash.digest(bytes34);
      byte[] bytes35 = new byte[]{
         4, -38, -55, 48, 87, -123, 106, 23, 101, 121, 123, -90, 76, -35, -56, -1, -90, 67, 43, 38, 3, -4, 36, 42, 6, -42, 41, 44, 127, -100, -10, -57
      };
      byte[] bytes36 = AesCipherUtil.decrypt(bytes, bytes35, aesCrypto);
      decoyKeyTable544 = sha3Hash.digest(bytes36);
      byte[] bytes37 = decoyKeyTable544;
      decoyKeyTable647 = sha3Hash.digest(bytes37);
      byte[] bytes38 = new byte[]{
         -115, -59, -19, 65, -71, 102, -36, 32, 44, -63, 114, -12, -38, 110, 75, 38, -84, 89, 90, -98, 54, -31, 82, 97, 25, -115, -68, 27, -87, -14, -84, -102
      };
      byte[] bytes39 = AesCipherUtil.decrypt(bytes, bytes38, aesCrypto);
      decoyKeyTable803 = sha3Hash.digest(bytes39);
      byte[] bytes40 = decoyKeyTable803;
      decoyKeyTable204 = sha3Hash.digest(bytes40);
      byte[] bytes41 = new byte[]{
         63, -57, 49, 75, 111, 127, 114, -128, 101, -1, -14, 105, -111, -46, 112, -25, 38, -7, 46, 56, -26, 114, -101, -50, -74, 45, 68, 60, 122, -30, -87, 12
      };
      byte[] bytes42 = AesCipherUtil.decrypt(bytes, bytes41, aesCrypto);
      decoyKeyTable028 = sha3Hash.digest(bytes42);
      byte[] bytes43 = decoyKeyTable028;
      decoyKeyTable633 = sha3Hash.digest(bytes43);
      byte[] bytes44 = new byte[]{
         98, -116, -101, 26, -16, 50, -115, -89, -40, -95, -50, -72, 77, -78, 111, -123, 31, -120, 83, 42, -16, 59, -113, 63, 115, 76, -84, -42, 4, 83, 4, -5
      };
      byte[] bytes45 = AesCipherUtil.decrypt(bytes, bytes44, aesCrypto);
      decoyKeyTable030 = sha3Hash.digest(bytes45);
      byte[] bytes46 = decoyKeyTable030;
      decoyKeyTable031 = sha3Hash.digest(bytes46);
      byte[] bytes47 = new byte[]{
         -107, 26, 20, -65, -79, -48, -2, -90, 92, 0, -40, 126, 90, -95, -52, -31, 127, 12, -82, -20, -105, -82, 40, -48, 109, 91, -57, -1, -85, -11, 103, -23
      };
      byte[] bytes48 = AesCipherUtil.decrypt(bytes, bytes47, aesCrypto);
      decoyKeyTable327 = sha3Hash.digest(bytes48);
      byte[] bytes49 = decoyKeyTable327;
      decoyKeyTable434 = sha3Hash.digest(bytes49);
      byte[] bytes50 = new byte[]{
         -83, 3, 106, 28, 16, -18, -104, -24, -84, 76, 50, -112, 45, -97, -86, -70, -45, -45, -27, 67, -103, 6, -82, 44, -66, 94, -13, -39, 48, -62, 4, -89
      };
      byte[] bytes51 = AesCipherUtil.decrypt(bytes, bytes50, aesCrypto);
      decoyKeyTable545 = sha3Hash.digest(bytes51);
      byte[] bytes52 = decoyKeyTable545;
      decoyKeyTable784 = sha3Hash.digest(bytes52);
      byte[] bytes53 = new byte[]{
         -27, 61, 79, 80, 83, 29, -83, 74, -48, -74, -1, -54, -122, 31, -125, -41, -66, 26, -12, -29, -17, 3, 121, 60, -112, 84, -8, -77, -127, -30, -47, 118
      };
      byte[] bytes54 = AesCipherUtil.decrypt(bytes, bytes53, aesCrypto);
      decoyKeyTable153 = sha3Hash.digest(bytes54);
      byte[] bytes55 = decoyKeyTable153;
      decoyKeyTable408 = sha3Hash.digest(bytes55);
      byte[] bytes56 = new byte[]{
         -38, -100, -7, -81, -50, 73, -29, 15, 80, -118, 10, -12, 11, -122, -72, -75, -65, -24, 95, 103, -38, 26, -43, -80, 26, -30, -54, 53, -10, 118, 75, 10
      };
      byte[] bytes57 = AesCipherUtil.decrypt(bytes, bytes56, aesCrypto);
      decoyKeyTable359 = sha3Hash.digest(bytes57);
      byte[] bytes58 = decoyKeyTable359;
      decoyKeyTable035 = sha3Hash.digest(bytes58);
      byte[] bytes59 = new byte[]{
         -89, 67, 114, -69, 87, -110, -37, 18, 28, -47, 67, 111, 101, -62, 15, 6, -108, -103, -86, 124, 69, 115, 76, -125, 34, -46, -85, 88, -50, 3, 110, 70
      };
      byte[] bytes60 = AesCipherUtil.decrypt(bytes, bytes59, aesCrypto);
      decoyKeyTable537 = sha3Hash.digest(bytes60);
      byte[] bytes61 = decoyKeyTable537;
      decoyKeyTable844 = sha3Hash.digest(bytes61);
      byte[] bytes62 = new byte[]{
         -76, 83, -54, 105, 103, 11, -19, -116, 119, 79, -23, 82, -18, -128, -100, -27, 31, 51, -83, -30, -93, -95, 44, -47, 0, -31, 86, -13, 106, 74, 62, 9
      };
      byte[] bytes63 = AesCipherUtil.decrypt(bytes, bytes62, aesCrypto);
      decoyKeyTable495 = sha3Hash.digest(bytes63);
      byte[] bytes64 = decoyKeyTable495;
      decoyKeyTable637 = sha3Hash.digest(bytes64);
      byte[] bytes65 = new byte[]{
         59, -15, -19, 47, -91, 107, 97, 95, 35, 17, -64, 107, -75, 26, 24, 126, 67, -77, -108, 110, 69, 115, -8, 58, 77, 117, -124, -34, 73, 26, 119, 72
      };
      byte[] bytes66 = AesCipherUtil.decrypt(bytes, bytes65, aesCrypto);
      decoyKeyTable275 = sha3Hash.digest(bytes66);
      byte[] bytes67 = decoyKeyTable275;
      decoyKeyTable241 = sha3Hash.digest(bytes67);
      byte[] bytes68 = new byte[]{
         30, -63, -60, 9, 22, 60, 60, 63, 110, 24, 89, -97, 75, -45, -126, 18, -29, 92, 116, 78, 37, 46, -6, -75, -78, 26, -122, -107, 118, -19, 60, 30
      };
      byte[] bytes69 = AesCipherUtil.decrypt(bytes, bytes68, aesCrypto);
      decoyKeyTable017 = sha3Hash.digest(bytes69);
      byte[] bytes70 = decoyKeyTable017;
      decoyKeyTable137 = sha3Hash.digest(bytes70);
      byte[] bytes71 = new byte[]{
         -38, 53, 44, -76, -12, 54, 85, -44, 3, 126, 49, -35, 30, -83, -83, -75, -117, 90, -90, -42, -121, 121, -4, 55, 100, -25, 18, -122, 93, -80, 90, 84
      };
      byte[] bytes72 = AesCipherUtil.decrypt(bytes, bytes71, aesCrypto);
      decoyKeyTable546 = sha3Hash.digest(bytes72);
      byte[] bytes73 = decoyKeyTable546;
      decoyKeyTable032 = sha3Hash.digest(bytes73);
      byte[] bytes74 = new byte[]{
         -119, 86, -51, -89, 19, 119, 74, -52, 76, -79, 1, 90, 46, -54, -113, 23, -84, -49, 65, -103, 78, 40, 48, 100, 3, -67, 29, 64, 95, 98, 104, -33
      };
      byte[] bytes75 = AesCipherUtil.decrypt(bytes, bytes74, aesCrypto);
      decoyKeyTable561 = sha3Hash.digest(bytes75);
      byte[] bytes76 = decoyKeyTable561;
      decoyKeyTable248 = sha3Hash.digest(bytes76);
      byte[] bytes77 = new byte[]{
         -113,
         -48,
         -73,
         -72,
         -113,
         -38,
         -35,
         73,
         75,
         -75,
         81,
         -118,
         -90,
         -63,
         112,
         -45,
         -6,
         -18,
         -34,
         106,
         -16,
         -26,
         -73,
         -48,
         -104,
         -24,
         73,
         86,
         -7,
         115,
         99,
         111
      };
      byte[] bytes78 = AesCipherUtil.decrypt(bytes, bytes77, aesCrypto);
      decoyKeyTable476 = sha3Hash.digest(bytes78);
      byte[] bytes79 = decoyKeyTable476;
      decoyKeyTable247 = sha3Hash.digest(bytes79);
      byte[] bytes80 = new byte[]{
         45, 117, 44, -66, -104, -113, 21, 7, 78, 118, 13, 6, -41, -68, -9, -106, -120, -58, 3, 60, -41, 51, 10, -30, -26, -32, 2, 111, 96, -81, 66, -59
      };
      byte[] bytes81 = AesCipherUtil.decrypt(bytes, bytes80, aesCrypto);
      decoyKeyTable323 = sha3Hash.digest(bytes81);
      byte[] bytes82 = decoyKeyTable323;
      decoyKeyTable830 = sha3Hash.digest(bytes82);
      byte[] bytes83 = new byte[]{
         -118, 41, 27, 112, -98, -123, -70, 127, 69, -25, -8, 10, -63, 41, 54, -50, 25, -91, -88, 104, -60, -21, -11, 47, -79, 70, -79, 53, 44, -35, -126, -59
      };
      byte[] bytes84 = AesCipherUtil.decrypt(bytes, bytes83, aesCrypto);
      decoyKeyTable491 = sha3Hash.digest(bytes84);
      byte[] bytes85 = decoyKeyTable491;
      decoyKeyTable244 = sha3Hash.digest(bytes85);
      byte[] bytes86 = new byte[]{
         -46,
         114,
         -104,
         40,
         99,
         82,
         112,
         98,
         -5,
         -86,
         -9,
         -10,
         112,
         -120,
         -84,
         -110,
         -38,
         -45,
         -121,
         117,
         33,
         -94,
         -97,
         120,
         85,
         -23,
         -92,
         -77,
         105,
         -22,
         -125,
         -92
      };
      byte[] bytes87 = AesCipherUtil.decrypt(bytes, bytes86, aesCrypto);
      decoyKeyTable411 = sha3Hash.digest(bytes87);
      byte[] bytes88 = decoyKeyTable411;
      decoyKeyTable418 = sha3Hash.digest(bytes88);
      byte[] bytes89 = new byte[]{
         31, 116, 5, -80, 85, 107, 97, 52, -84, 126, -25, 23, -19, 125, -1, -11, 6, -100, -15, 121, 68, -36, 47, -89, 37, -110, 84, -53, -17, 37, 26, 72
      };
      byte[] bytes90 = AesCipherUtil.decrypt(bytes, bytes89, aesCrypto);
      decoyKeyTable294 = sha3Hash.digest(bytes90);
      byte[] bytes91 = decoyKeyTable294;
      decoyKeyTable182 = sha3Hash.digest(bytes91);
      byte[] bytes92 = new byte[]{
         89, -74, 49, -11, -97, -5, 49, -29, -125, -71, 79, 80, -99, -119, 42, 84, 31, 92, 38, -120, -36, -49, 24, -20, 83, 68, 24, 52, -100, 11, 115, -81
      };
      byte[] bytes93 = AesCipherUtil.decrypt(bytes, bytes92, aesCrypto);
      decoyKeyTable705 = sha3Hash.digest(bytes93);
      byte[] bytes94 = decoyKeyTable705;
      decoyKeyTable271 = sha3Hash.digest(bytes94);
      byte[] bytes95 = new byte[]{
         10, 26, 43, -95, -117, 37, 81, 4, -20, -93, -67, -111, 105, 71, 79, 40, 45, 68, 33, 40, -119, -69, -20, -111, 114, -81, 109, -9, -20, -27, -128, -127
      };
      byte[] bytes96 = AesCipherUtil.decrypt(bytes, bytes95, aesCrypto);
      decoyKeyTable319 = sha3Hash.digest(bytes96);
      byte[] bytes97 = decoyKeyTable319;
      decoyKeyTable765 = sha3Hash.digest(bytes97);
      byte[] bytes98 = new byte[]{
         47, 74, -6, -6, 17, -31, -7, -93, 58, -78, -29, -64, 1, 35, 85, -66, -16, 18, -38, -16, 55, 34, -122, -92, -46, 87, 0, -117, 10, -38, -12, 38
      };
      byte[] bytes99 = AesCipherUtil.decrypt(bytes, bytes98, aesCrypto);
      decoyKeyTable435 = sha3Hash.digest(bytes99);
      byte[] bytes100 = decoyKeyTable435;
      decoyKeyTable562 = sha3Hash.digest(bytes100);
      byte[] bytes101 = new byte[]{
         64, -80, 9, 107, 23, -12, 24, -61, 13, 72, 19, -91, -19, -32, -5, 127, 38, -26, 118, 100, -86, -104, -122, -36, 17, 6, -20, 103, -127, 100, -21, -102
      };
      byte[] bytes102 = AesCipherUtil.decrypt(bytes, bytes101, aesCrypto);
      decoyKeyTable181 = sha3Hash.digest(bytes102);
      byte[] bytes103 = decoyKeyTable181;
      decoyKeyTable560 = sha3Hash.digest(bytes103);
      byte[] bytes104 = new byte[]{
         -109,
         -26,
         125,
         -79,
         -95,
         -111,
         95,
         -24,
         95,
         -2,
         -119,
         -33,
         61,
         1,
         -56,
         -111,
         -10,
         83,
         88,
         -83,
         30,
         -13,
         -114,
         28,
         -104,
         20,
         127,
         -56,
         -89,
         35,
         -97,
         79
      };
      byte[] bytes105 = AesCipherUtil.decrypt(bytes, bytes104, aesCrypto);
      decoyKeyTable826 = sha3Hash.digest(bytes105);
      byte[] bytes106 = decoyKeyTable826;
      decoyKeyTable179 = sha3Hash.digest(bytes106);
      byte[] bytes107 = new byte[]{
         82, 58, 20, -102, -6, 82, 15, 75, 10, 71, -47, 7, 90, 99, -47, 23, -78, -50, 104, -38, -119, 104, -90, 43, 5, 51, -45, -39, -117, -65, -1, -9
      };
      byte[] bytes108 = AesCipherUtil.decrypt(bytes, bytes107, aesCrypto);
      decoyKeyTable638 = sha3Hash.digest(bytes108);
      byte[] bytes109 = decoyKeyTable638;
      decoyKeyTable673 = sha3Hash.digest(bytes109);
      byte[] bytes110 = new byte[]{
         103, 115, 33, 41, 73, -21, -72, -53, -36, 82, 3, 30, -119, -58, -24, 97, 101, 112, 46, -76, 110, -51, -33, 122, -12, -88, 6, -120, -60, -79, -21, -116
      };
      byte[] bytes111 = AesCipherUtil.decrypt(bytes, bytes110, aesCrypto);
      decoyKeyTable183 = sha3Hash.digest(bytes111);
      byte[] bytes112 = decoyKeyTable183;
      decoyKeyTable529 = sha3Hash.digest(bytes112);
      byte[] bytes113 = new byte[]{
         -58, 20, -16, 34, 73, -88, 125, 24, -45, -54, -117, 10, -97, -51, 7, -64, 15, 48, 45, -70, 7, 5, 104, 69, -9, -114, -1, -23, -54, 20, 25, 66
      };
      byte[] bytes114 = AesCipherUtil.decrypt(bytes, bytes113, aesCrypto);
      decoyKeyTable524 = sha3Hash.digest(bytes114);
      byte[] bytes115 = decoyKeyTable524;
      decoyKeyTable852 = sha3Hash.digest(bytes115);
      byte[] bytes116 = new byte[]{
         -39, -106, -53, 85, 103, 59, 75, 39, -78, -44, 87, 109, 98, 92, 30, 62, 117, 91, 79, -27, -126, -39, -52, 15, 22, 100, -22, 56, 17, -123, -22, 24
      };
      byte[] bytes117 = AesCipherUtil.decrypt(bytes, bytes116, aesCrypto);
      decoyKeyTable195 = sha3Hash.digest(bytes117);
      byte[] bytes118 = decoyKeyTable195;
      decoyKeyTable326 = sha3Hash.digest(bytes118);
      byte[] bytes119 = new byte[]{
         -127,
         -11,
         -22,
         -64,
         6,
         -94,
         94,
         -49,
         -114,
         71,
         -78,
         100,
         97,
         45,
         -114,
         -123,
         46,
         124,
         -87,
         -41,
         127,
         -9,
         58,
         -113,
         106,
         -100,
         -13,
         116,
         115,
         -13,
         97,
         46
      };
      byte[] bytes120 = AesCipherUtil.decrypt(bytes, bytes119, aesCrypto);
      decoyKeyTable045 = sha3Hash.digest(bytes120);
      byte[] bytes121 = decoyKeyTable045;
      decoyKeyTable443 = sha3Hash.digest(bytes121);
      byte[] bytes122 = new byte[]{
         -68, -45, -104, 117, -43, 77, 96, 29, -3, 71, -77, -53, 57, 8, -40, 30, 111, -47, 35, 40, -74, 30, 39, -68, -125, 51, -8, 59, 87, -25, 71, 41
      };
      byte[] bytes123 = AesCipherUtil.decrypt(bytes, bytes122, aesCrypto);
      decoyKeyTable591 = sha3Hash.digest(bytes123);
      byte[] bytes124 = decoyKeyTable591;
      decoyKeyTable227 = sha3Hash.digest(bytes124);
      byte[] bytes125 = new byte[]{
         -36, 36, 97, 32, 94, 70, 42, -119, 70, 54, -68, -77, 5, 6, 72, -52, 34, -102, -20, 81, 112, -66, -13, -63, 19, 115, -78, -38, -85, 124, -91, -40
      };
      byte[] bytes126 = AesCipherUtil.decrypt(bytes, bytes125, aesCrypto);
      decoyKeyTable054 = sha3Hash.digest(bytes126);
      byte[] bytes127 = decoyKeyTable054;
      decoyKeyTable194 = sha3Hash.digest(bytes127);
      byte[] bytes128 = new byte[]{
         115, 84, 112, -9, -16, -74, 85, -74, -43, 123, 70, -52, -50, 89, -23, 86, -93, 105, -14, -91, -110, 38, -22, -69, 2, 81, 105, 108, -2, -123, -88, -116
      };
      byte[] bytes129 = AesCipherUtil.decrypt(bytes, bytes128, aesCrypto);
      decoyKeyTable766 = sha3Hash.digest(bytes129);
      byte[] bytes130 = decoyKeyTable766;
      decoyKeyTable429 = sha3Hash.digest(bytes130);
      byte[] bytes131 = new byte[]{
         -22,
         30,
         -102,
         -76,
         -29,
         85,
         -100,
         -38,
         -79,
         21,
         -115,
         -59,
         57,
         28,
         -119,
         -111,
         93,
         -125,
         -87,
         110,
         -100,
         -74,
         -128,
         94,
         -35,
         13,
         -92,
         29,
         -28,
         125,
         52,
         112
      };
      byte[] bytes132 = AesCipherUtil.decrypt(bytes, bytes131, aesCrypto);
      decoyKeyTable511 = sha3Hash.digest(bytes132);
      byte[] bytes133 = decoyKeyTable511;
      decoyKeyTable736 = sha3Hash.digest(bytes133);
      byte[] bytes134 = new byte[]{
         1, 45, -104, -86, -122, -96, 61, 38, 9, -79, -24, 23, 24, -45, -32, -96, 66, -81, 62, 97, 126, -49, -102, 65, -110, -9, 100, 55, -20, -44, 60, 39
      };
      byte[] bytes135 = AesCipherUtil.decrypt(bytes, bytes134, aesCrypto);
      decoyKeyTable468 = sha3Hash.digest(bytes135);
      byte[] bytes136 = decoyKeyTable468;
      decoyKeyTable672 = sha3Hash.digest(bytes136);
      byte[] bytes137 = new byte[]{
         28, 57, -38, -26, -113, 56, 25, 85, -106, -17, -43, 48, -14, 30, 18, -71, -91, -10, -9, -8, 8, -63, -99, -13, -82, -77, -63, -2, 52, -22, -96, 122
      };
      byte[] bytes138 = AesCipherUtil.decrypt(bytes, bytes137, aesCrypto);
      decoyKeyTable427 = sha3Hash.digest(bytes138);
      byte[] bytes139 = decoyKeyTable427;
      decoyKeyTable695 = sha3Hash.digest(bytes139);
      byte[] bytes140 = new byte[]{
         12, 86, -41, 41, -121, 102, 91, -8, -16, -122, -12, 52, 93, -84, 73, 57, 108, -69, 47, 16, -19, -55, -73, -68, -127, 28, 57, 124, -94, -47, -97, -55
      };
      byte[] bytes141 = AesCipherUtil.decrypt(bytes, bytes140, aesCrypto);
      decoyKeyTable075 = sha3Hash.digest(bytes141);
      byte[] bytes142 = decoyKeyTable075;
      decoyKeyTable699 = sha3Hash.digest(bytes142);
      byte[] bytes143 = new byte[]{
         124, 78, -95, -2, -111, -76, 18, -105, -40, 74, -99, 6, 60, 33, 47, 87, 60, 74, 114, 108, -102, -57, 28, -71, -62, 24, -106, 71, -119, 69, -44, -52
      };
      byte[] bytes144 = AesCipherUtil.decrypt(bytes, bytes143, aesCrypto);
      decoyKeyTable198 = sha3Hash.digest(bytes144);
      byte[] bytes145 = decoyKeyTable198;
      decoyKeyTable835 = sha3Hash.digest(bytes145);
      byte[] bytes146 = new byte[]{
         70, -75, -14, 83, 25, 7, 55, -73, 41, 37, -9, 11, 73, -39, 28, -59, -85, 39, 30, -118, 56, 57, 20, 25, -115, 91, -10, 31, -79, 53, 104, 12
      };
      byte[] bytes147 = AesCipherUtil.decrypt(bytes, bytes146, aesCrypto);
      decoyKeyTable383 = sha3Hash.digest(bytes147);
      byte[] bytes148 = decoyKeyTable383;
      decoyKeyTable603 = sha3Hash.digest(bytes148);
      byte[] bytes149 = new byte[]{
         97,
         43,
         5,
         -10,
         119,
         -118,
         12,
         -101,
         30,
         -105,
         -113,
         -75,
         111,
         -49,
         32,
         -101,
         -10,
         100,
         -120,
         -121,
         49,
         -128,
         35,
         -113,
         39,
         -16,
         25,
         31,
         42,
         -4,
         -109,
         -51
      };
      byte[] bytes150 = AesCipherUtil.decrypt(bytes, bytes149, aesCrypto);
      decoyKeyTable178 = sha3Hash.digest(bytes150);
      byte[] bytes151 = decoyKeyTable178;
      decoyKeyTable829 = sha3Hash.digest(bytes151);
      byte[] bytes152 = new byte[]{
         -120, 100, -118, -80, 77, -91, 125, 83, 52, -6, 79, -126, -87, -41, 53, 88, 8, -44, -76, 123, 56, -78, -36, 79, 13, -126, -118, 105, 27, -64, 76, 4
      };
      byte[] bytes153 = AesCipherUtil.decrypt(bytes, bytes152, aesCrypto);
      decoyKeyTable356 = sha3Hash.digest(bytes153);
      byte[] bytes154 = decoyKeyTable356;
      decoyKeyTable426 = sha3Hash.digest(bytes154);
      byte[] bytes155 = new byte[]{
         -116, -46, -48, 104, 66, -73, 100, 72, -52, 112, 45, -47, 125, 18, 61, 63, 96, -62, 97, -72, 6, 19, -128, -12, 14, -36, 124, -6, 69, -39, 120, 50
      };
      byte[] bytes156 = AesCipherUtil.decrypt(bytes, bytes155, aesCrypto);
      decoyKeyTable684 = sha3Hash.digest(bytes156);
      byte[] bytes157 = decoyKeyTable684;
      decoyKeyTable162 = sha3Hash.digest(bytes157);
      byte[] bytes158 = new byte[]{
         62, 28, -22, 63, -19, -110, -37, 82, -11, 5, -82, -46, -116, 56, -101, -98, 95, -46, 81, -87, -80, -23, -60, -65, -95, 108, -56, 114, 4, 89, -103, 46
      };
      byte[] bytes159 = AesCipherUtil.decrypt(bytes, bytes158, aesCrypto);
      decoyKeyTable567 = sha3Hash.digest(bytes159);
      byte[] bytes160 = decoyKeyTable567;
      decoyKeyTable332 = sha3Hash.digest(bytes160);
      byte[] bytes161 = new byte[]{
         -19, -69, 105, 25, 60, 121, -91, -37, -88, -95, -123, -103, -24, -30, -124, 29, 105, 61, -49, 73, 13, 66, 73, 79, 119, -115, 99, -58, 9, -48, -64, 111
      };
      byte[] bytes162 = AesCipherUtil.decrypt(bytes, bytes161, aesCrypto);
      decoyKeyTable437 = sha3Hash.digest(bytes162);
      byte[] bytes163 = decoyKeyTable437;
      decoyKeyTable279 = sha3Hash.digest(bytes163);
      byte[] bytes164 = new byte[]{
         -70, 127, -12, -23, -80, 22, 38, 52, 67, 24, -90, -96, -16, -72, -101, 0, -37, -28, -47, -3, -4, -25, 80, 87, -17, 120, -51, -69, -119, 72, 35, 44
      };
      byte[] bytes165 = AesCipherUtil.decrypt(bytes, bytes164, aesCrypto);
      decoyKeyTable609 = sha3Hash.digest(bytes165);
      byte[] bytes166 = decoyKeyTable609;
      decoyKeyTable095 = sha3Hash.digest(bytes166);
      byte[] bytes167 = new byte[]{
         8, -29, -127, 66, 20, 29, 91, 31, -112, 35, 72, 44, -69, 127, -28, -31, 91, 126, -35, -62, 6, -95, -88, 117, 37, 71, -78, 100, 58, -59, -62, 16
      };
      byte[] bytes168 = AesCipherUtil.decrypt(bytes, bytes167, aesCrypto);
      decoyKeyTable174 = sha3Hash.digest(bytes168);
      byte[] bytes169 = decoyKeyTable174;
      decoyKeyTable154 = sha3Hash.digest(bytes169);
      byte[] bytes170 = new byte[]{
         -30, 8, 82, 114, 42, 58, 59, 111, -46, 68, -15, -79, 34, 5, -54, -126, 115, 35, 74, 59, -122, 62, -10, -19, 14, 118, 51, -93, 15, 55, -104, 95
      };
      byte[] bytes171 = AesCipherUtil.decrypt(bytes, bytes170, aesCrypto);
      decoyKeyTable579 = sha3Hash.digest(bytes171);
      byte[] bytes172 = decoyKeyTable579;
      decoyKeyTable265 = sha3Hash.digest(bytes172);
      byte[] bytes173 = new byte[]{
         -31, -31, -27, 99, -58, -7, 52, 110, 45, 55, -93, -17, 54, 79, -68, -82, -127, -57, -43, 49, 32, 70, 96, 108, -3, 75, 50, 14, 5, 111, -100, -81
      };
      byte[] bytes174 = AesCipherUtil.decrypt(bytes, bytes173, aesCrypto);
      decoyKeyTable708 = sha3Hash.digest(bytes174);
      byte[] bytes175 = decoyKeyTable708;
      decoyKeyTable584 = sha3Hash.digest(bytes175);
      byte[] bytes176 = new byte[]{
         -119,
         -69,
         -111,
         -51,
         -35,
         94,
         127,
         -57,
         -60,
         58,
         -27,
         46,
         -43,
         119,
         33,
         -118,
         48,
         115,
         21,
         28,
         55,
         -91,
         -73,
         87,
         -112,
         -115,
         -93,
         -23,
         4,
         -36,
         -93,
         81
      };
      byte[] bytes177 = AesCipherUtil.decrypt(bytes, bytes176, aesCrypto);
      decoyKeyTable172 = sha3Hash.digest(bytes177);
      byte[] bytes178 = decoyKeyTable172;
      decoyKeyTable854 = sha3Hash.digest(bytes178);
      byte[] bytes179 = new byte[]{
         20, -62, -114, 119, -82, -39, -27, 56, 0, 23, -56, -72, 60, -89, -53, -2, -125, 126, -12, 116, -51, 4, 35, -85, -78, -49, -81, 10, 104, 10, -67, 91
      };
      byte[] bytes180 = AesCipherUtil.decrypt(bytes, bytes179, aesCrypto);
      decoyKeyTable086 = sha3Hash.digest(bytes180);
      byte[] bytes181 = decoyKeyTable086;
      decoyKeyTable608 = sha3Hash.digest(bytes181);
      byte[] bytes182 = new byte[]{
         -74, 14, -77, 23, -70, -52, 101, 61, 107, 35, -44, 31, 50, -37, 117, 1, -45, 38, 99, -122, -30, 51, 3, 13, 31, -57, 40, 18, 97, 36, 59, -70
      };
      byte[] bytes183 = AesCipherUtil.decrypt(bytes, bytes182, aesCrypto);
      decoyKeyTable729 = sha3Hash.digest(bytes183);
      byte[] bytes184 = decoyKeyTable729;
      decoyKeyTable479 = sha3Hash.digest(bytes184);
      byte[] bytes185 = new byte[]{
         64, -24, 94, -128, -75, -120, -85, 103, -29, 36, -36, 127, -56, -58, -91, -28, 59, -80, 69, -100, -58, 68, 8, 65, 97, -64, -8, 73, -18, 25, -45, 53
      };
      byte[] bytes186 = AesCipherUtil.decrypt(bytes, bytes185, aesCrypto);
      decoyKeyTable366 = sha3Hash.digest(bytes186);
      byte[] bytes187 = decoyKeyTable366;
      decoyKeyTable096 = sha3Hash.digest(bytes187);
      byte[] bytes188 = new byte[]{
         -13, 61, 111, 104, 83, -118, -47, -35, -42, 78, 47, 0, -46, -24, 37, -95, -9, -103, -1, -27, -64, 92, 122, -64, -75, -56, -5, 105, 64, 21, -80, 13
      };
      byte[] bytes189 = AesCipherUtil.decrypt(bytes, bytes188, aesCrypto);
      decoyKeyTable066 = sha3Hash.digest(bytes189);
      byte[] bytes190 = decoyKeyTable066;
      decoyKeyTable558 = sha3Hash.digest(bytes190);
      byte[] bytes191 = new byte[]{
         109, -79, -85, -20, -67, -5, -40, -72, 25, -69, -33, -2, 55, -110, -29, -55, -83, 12, 56, 8, -32, 123, 69, 18, 114, 17, 106, -77, -33, 0, -53, 14
      };
      byte[] bytes192 = AesCipherUtil.decrypt(bytes, bytes191, aesCrypto);
      decoyKeyTable438 = sha3Hash.digest(bytes192);
      byte[] bytes193 = decoyKeyTable438;
      decoyKeyTable703 = sha3Hash.digest(bytes193);
      byte[] bytes194 = new byte[]{
         37, -120, -90, 116, 102, 1, 113, 118, 116, -49, 13, -55, -62, 75, -1, -70, 95, 77, 8, 44, -31, 0, 127, -86, 110, -106, 60, 74, 120, -81, 31, 50
      };
      byte[] bytes195 = AesCipherUtil.decrypt(bytes, bytes194, aesCrypto);
      decoyKeyTable385 = sha3Hash.digest(bytes195);
      byte[] bytes196 = decoyKeyTable385;
      decoyKeyTable199 = sha3Hash.digest(bytes196);
      byte[] bytes197 = new byte[]{
         73, 95, -16, 66, -106, 66, 100, 10, 83, -101, 72, 118, 115, -96, 87, -52, 0, -57, -100, 114, -69, -92, -5, -106, -3, -83, -76, 93, 110, -61, 32, -14
      };
      byte[] bytes198 = AesCipherUtil.decrypt(bytes, bytes197, aesCrypto);
      decoyKeyTable264 = sha3Hash.digest(bytes198);
      byte[] bytes199 = decoyKeyTable264;
      decoyKeyTable205 = sha3Hash.digest(bytes199);
      byte[] bytes200 = new byte[]{
         -14, -87, 73, -25, -44, 75, 24, 5, 113, -102, 123, 97, -104, -21, 50, 101, 35, -52, -128, -108, 112, 123, 57, -128, -109, 1, 103, -14, 71, 44, 49, -95
      };
      byte[] bytes201 = AesCipherUtil.decrypt(bytes, bytes200, aesCrypto);
      decoyKeyTable668 = sha3Hash.digest(bytes201);
      byte[] bytes202 = decoyKeyTable668;
      decoyKeyTable400 = sha3Hash.digest(bytes202);
      byte[] bytes203 = new byte[]{
         -42, 96, -33, 19, 91, 97, -6, 32, 10, -66, 0, -40, 126, 121, 30, 47, 19, 17, -86, 63, 21, -66, 32, -42, 103, 84, -76, -120, 123, 6, 19, 93
      };
      byte[] bytes204 = AesCipherUtil.decrypt(bytes, bytes203, aesCrypto);
      decoyKeyTable206 = sha3Hash.digest(bytes204);
      byte[] bytes205 = decoyKeyTable206;
      decoyKeyTable029 = sha3Hash.digest(bytes205);
      byte[] bytes206 = new byte[]{
         92, 61, -105, 121, -67, 14, -28, -59, 75, 72, 94, 102, -18, 127, -38, -70, 2, 18, -10, -96, -61, 52, -31, 16, 41, 85, -42, -73, 101, 33, -41, -36
      };
      byte[] bytes207 = AesCipherUtil.decrypt(bytes, bytes206, aesCrypto);
      decoyKeyTable588 = sha3Hash.digest(bytes207);
      byte[] bytes208 = decoyKeyTable588;
      decoyKeyTable157 = sha3Hash.digest(bytes208);
      populateDecoyTokensGroupB(decoyKeyTable588);
      byte[] bytes209 = new byte[]{
         95, 30, 79, 40, -8, 31, 42, -77, -60, 34, -33, -99, -58, -54, -10, 55, 118, -41, 119, -4, 76, -87, -96, -83, -14, 56, -128, 48, 12, -6, -104, 118
      };
      byte[] bytes210 = AesCipherUtil.decrypt(bytes, bytes209, aesCrypto);
      decoyKeyTable624 = sha3Hash.digest(bytes210);
      byte[] bytes211 = decoyKeyTable624;
      decoyKeyTable013 = sha3Hash.digest(bytes211);
   }

   public static void populateDecoyTokensGroupB(byte[] bytes) {
      AesCrypto aesCrypto = new AesCrypto();
      Sha3Hash sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes2 = new byte[]{
         104, 21, 115, -66, 94, 57, -26, -81, -3, 41, -54, 91, 90, -27, 86, 47, 68, 51, 7, -123, -100, -43, -85, -125, -11, -76, -62, -106, -48, -67, 28, 77
      };
      byte[] bytes3 = AesCipherUtil.decrypt(bytes, bytes2, aesCrypto);
      decoyTokenGroupB07 = sha3Hash.digest(bytes3);
      byte[] bytes4 = decoyTokenGroupB07;
      decoyTokenGroupB14 = sha3Hash.digest(bytes4);
      byte[] bytes5 = new byte[]{
         -23, 52, 105, -18, -57, -2, -118, 28, -97, -55, -86, -72, 116, 97, -110, -72, -47, -63, 46, -93, 33, 113, 69, -46, 3, 57, -7, -41, -87, 21, -62, -60
      };
      byte[] bytes6 = AesCipherUtil.decrypt(bytes, bytes5, aesCrypto);
      decoyTokenGroupB09 = sha3Hash.digest(bytes6);
      byte[] bytes7 = decoyTokenGroupB09;
      decoyTokenGroupB05 = sha3Hash.digest(bytes7);
      byte[] bytes8 = new byte[]{
         -119, -73, -99, 74, 69, 100, 96, -87, 41, -19, -104, -34, -63, 91, 38, -97, 56, -87, 40, -107, -29, -14, 30, -118, 115, -44, 4, 58, -109, -17, 91, 95
      };
      byte[] bytes9 = AesCipherUtil.decrypt(bytes, bytes8, aesCrypto);
      decoyTokenGroupB13 = sha3Hash.digest(bytes9);
      byte[] bytes10 = decoyTokenGroupB13;
      decoyTokenGroupB00 = sha3Hash.digest(bytes10);
      byte[] bytes11 = new byte[]{
         -26, 38, 34, -46, -123, 79, 69, -48, 3, 35, 4, -87, 72, 94, -1, -4, 10, -82, -97, 92, -6, 2, -34, -41, -65, 63, 124, 47, 5, 2, -124, -37
      };
      byte[] bytes12 = AesCipherUtil.decrypt(bytes, bytes11, aesCrypto);
      decoyTokenGroupB06 = sha3Hash.digest(bytes12);
      byte[] bytes13 = decoyTokenGroupB06;
      decoyTokenGroupB04 = sha3Hash.digest(bytes13);
      byte[] bytes14 = new byte[]{
         55, -94, -36, -8, 99, -11, -15, -21, -48, 33, -4, -61, -94, -22, -10, -37, -102, 102, -32, -24, -70, 113, 9, 94, 123, -49, 95, -95, -107, 55, 38, -109
      };
      byte[] bytes15 = AesCipherUtil.decrypt(bytes, bytes14, aesCrypto);
      decoyTokenGroupB02 = sha3Hash.digest(bytes15);
      byte[] bytes16 = decoyTokenGroupB02;
      decoyTokenGroupB11 = sha3Hash.digest(bytes16);
      byte[] bytes17 = new byte[]{
         -87,
         -73,
         -41,
         -72,
         -63,
         -69,
         -116,
         -49,
         -69,
         -66,
         108,
         -54,
         21,
         -18,
         111,
         78,
         -78,
         -39,
         -60,
         81,
         -80,
         -20,
         -29,
         96,
         -82,
         119,
         -111,
         -90,
         -54,
         -9,
         -54,
         -44
      };
      byte[] bytes18 = AesCipherUtil.decrypt(bytes, bytes17, aesCrypto);
      decoyTokenGroupB03 = sha3Hash.digest(bytes18);
      byte[] bytes19 = decoyTokenGroupB03;
      decoyTokenGroupB12 = sha3Hash.digest(bytes19);
      byte[] bytes20 = new byte[]{
         67, -26, 102, 124, -96, -14, -77, 69, -54, 109, 109, 16, -89, -35, 8, -113, 14, 0, -33, -43, 72, 116, 101, 58, 32, 67, 47, -98, -48, 74, 44, -62
      };
      byte[] bytes21 = AesCipherUtil.decrypt(bytes, bytes20, aesCrypto);
      decoyTokenGroupB10 = sha3Hash.digest(bytes21);
      byte[] bytes22 = decoyTokenGroupB10;
      decoyTokenGroupB01 = sha3Hash.digest(bytes22);
      byte[] bytes23 = new byte[]{
         -95, -81, 13, 50, -58, 83, 16, 86, -21, 44, -59, -98, -1, -87, -24, -74, -87, 12, -101, -102, -28, -124, 70, 22, -11, 117, -125, 75, 94, 43, -11, 20
      };
      byte[] bytes24 = AesCipherUtil.decrypt(bytes, bytes23, aesCrypto);
      decoyTokenGroupB08 = sha3Hash.digest(bytes24);
      byte[] bytes25 = decoyTokenGroupB08;
      decoyKeyTable212 = sha3Hash.digest(bytes25);
      byte[] bytes26 = new byte[]{
         108, -89, 20, 19, -125, 86, -101, -41, -83, 64, -74, 61, 14, 113, -77, 89, 95, -80, -117, 52, -79, 100, -120, 27, 26, -125, 0, -84, -65, -89, 73, -23
      };
      byte[] bytes27 = AesCipherUtil.decrypt(bytes, bytes26, aesCrypto);
      decoyKeyTable126 = sha3Hash.digest(bytes27);
      byte[] bytes28 = decoyKeyTable126;
      decoyKeyTable612 = sha3Hash.digest(bytes28);
      byte[] bytes29 = new byte[]{
         87, 51, -107, -45, 28, -75, 88, 74, -72, 75, 49, 70, -51, -51, 55, -110, 125, 4, -60, 17, -35, -117, -83, 7, -49, -112, 38, -8, -16, 71, -125, 19
      };
      byte[] bytes30 = AesCipherUtil.decrypt(bytes, bytes29, aesCrypto);
      decoyKeyTable493 = sha3Hash.digest(bytes30);
      byte[] bytes31 = decoyKeyTable493;
      decoyKeyTable697 = sha3Hash.digest(bytes31);
      byte[] bytes32 = new byte[]{
         -52,
         62,
         -89,
         -46,
         101,
         -45,
         -74,
         123,
         -10,
         7,
         -98,
         -94,
         39,
         -68,
         -81,
         -66,
         120,
         9,
         96,
         -45,
         -112,
         -40,
         120,
         -40,
         -107,
         -42,
         -33,
         8,
         -120,
         -119,
         88,
         -63
      };
      byte[] bytes33 = AesCipherUtil.decrypt(bytes, bytes32, aesCrypto);
      decoyKeyTable109 = sha3Hash.digest(bytes33);
      byte[] bytes34 = decoyKeyTable109;
      decoyKeyTable127 = sha3Hash.digest(bytes34);
      byte[] bytes35 = new byte[]{
         -112, -127, 21, 43, 7, 104, -90, 37, -16, 50, -76, 12, -98, -118, -89, 36, -125, 55, -29, 37, -29, -61, -60, 99, -40, -8, -77, -18, 29, -62, 124, -8
      };
      byte[] bytes36 = AesCipherUtil.decrypt(bytes, bytes35, aesCrypto);
      decoyKeyTable302 = sha3Hash.digest(bytes36);
      byte[] bytes37 = decoyKeyTable302;
      decoyKeyTable043 = sha3Hash.digest(bytes37);
      byte[] bytes38 = new byte[]{
         4, 101, 82, 109, -114, 45, 124, 93, -5, -5, 104, -55, -56, 123, 77, 24, -29, -78, -51, 76, 40, 113, 77, -68, 74, -9, 124, -62, -77, -21, 8, -32
      };
      byte[] bytes39 = AesCipherUtil.decrypt(bytes, bytes38, aesCrypto);
      decoyKeyTable474 = sha3Hash.digest(bytes39);
      byte[] bytes40 = decoyKeyTable474;
      decoyKeyTable080 = sha3Hash.digest(bytes40);
      byte[] bytes41 = new byte[]{
         26, 79, 97, 42, 22, 118, 90, 111, 96, 28, -111, -121, 92, 109, -117, 21, -15, 41, 50, -14, -75, -54, -118, 94, 75, -28, -78, 74, -42, -83, -86, -77
      };
      byte[] bytes42 = AesCipherUtil.decrypt(bytes, bytes41, aesCrypto);
      decoyKeyTable414 = sha3Hash.digest(bytes42);
      byte[] bytes43 = decoyKeyTable414;
      decoyKeyTable433 = sha3Hash.digest(bytes43);
      byte[] bytes44 = new byte[]{
         52, 119, 34, -34, -10, -31, -75, 43, 94, 99, -80, 14, -19, 3, 113, 116, -21, 13, 122, -87, -76, -104, -28, -103, -40, -97, -107, 82, -94, -21, -31, 63
      };
      byte[] bytes45 = AesCipherUtil.decrypt(bytes, bytes44, aesCrypto);
      decoyKeyTable053 = sha3Hash.digest(bytes45);
      byte[] bytes46 = decoyKeyTable053;
      decoyKeyTable104 = sha3Hash.digest(bytes46);
      byte[] bytes47 = new byte[]{
         -11, -107, 37, 32, -17, 76, 51, 96, 84, 68, 103, -117, 44, -46, -63, 112, 20, -27, -63, -35, 17, 62, -66, -40, -21, 55, 47, -62, -85, 72, 78, 19
      };
      byte[] bytes48 = AesCipherUtil.decrypt(bytes, bytes47, aesCrypto);
      decoyKeyTable723 = sha3Hash.digest(bytes48);
      byte[] bytes49 = decoyKeyTable723;
      decoyKeyTable698 = sha3Hash.digest(bytes49);
      byte[] bytes50 = new byte[]{
         23, 88, 41, -2, 127, 122, 35, -65, -5, -34, -21, -85, 117, 110, -126, 54, 8, 117, -52, -47, 118, -70, 107, -110, 117, 66, -123, -98, -59, 31, 108, 102
      };
      byte[] bytes51 = AesCipherUtil.decrypt(bytes, bytes50, aesCrypto);
      decoyKeyTable128 = sha3Hash.digest(bytes51);
      byte[] bytes52 = decoyKeyTable128;
      decoyKeyTable218 = sha3Hash.digest(bytes52);
      byte[] bytes53 = new byte[]{
         76, 60, -122, 109, -109, -70, 25, 8, 29, -37, -23, -117, 20, 126, -93, 125, -48, -78, -115, 3, -105, -97, 12, -9, -25, -55, 117, 33, 75, -44, -26, 118
      };
      byte[] bytes54 = AesCipherUtil.decrypt(bytes, bytes53, aesCrypto);
      decoyKeyTable237 = sha3Hash.digest(bytes54);
      byte[] bytes55 = decoyKeyTable237;
      decoyKeyTable660 = sha3Hash.digest(bytes55);
      byte[] bytes56 = new byte[]{
         -105, -7, 120, 84, 91, 21, -2, 64, 35, 53, 69, -12, -123, 12, -85, 43, -65, -30, 82, -19, -107, -68, 31, -52, 84, -122, -25, -22, 105, 68, -103, 88
      };
      byte[] bytes57 = AesCipherUtil.decrypt(bytes, bytes56, aesCrypto);
      decoyKeyTable301 = sha3Hash.digest(bytes57);
      byte[] bytes58 = decoyKeyTable301;
      decoyKeyTable439 = sha3Hash.digest(bytes58);
      byte[] bytes59 = new byte[]{
         -71, 51, -108, 20, 92, 59, 127, -70, -72, -105, 4, 56, 91, 41, 69, -50, 65, -32, -43, 24, -2, -6, -37, 17, -60, -5, -55, 57, 39, 62, 75, -94
      };
      byte[] bytes60 = AesCipherUtil.decrypt(bytes, bytes59, aesCrypto);
      decoyKeyTable018 = sha3Hash.digest(bytes60);
      byte[] bytes61 = decoyKeyTable018;
      decoyKeyTable432 = sha3Hash.digest(bytes61);
      byte[] bytes62 = new byte[]{
         -5, -96, -4, -105, -69, -24, -48, 95, 34, 82, -31, -84, -125, -53, -93, -75, 52, -14, 56, -111, 9, 124, 106, 74, 39, 57, -122, -1, -109, -53, 64, -94
      };
      byte[] bytes63 = AesCipherUtil.decrypt(bytes, bytes62, aesCrypto);
      decoyKeyTable015 = sha3Hash.digest(bytes63);
      byte[] bytes64 = decoyKeyTable015;
      decoyKeyTable494 = sha3Hash.digest(bytes64);
      byte[] bytes65 = new byte[]{
         -88, 75, 64, 98, 126, -16, 8, 126, 127, 4, 76, 104, -51, -57, -34, 56, 100, -4, 74, -60, -60, -110, 34, 74, 97, -18, -119, 115, 89, -43, -90, 39
      };
      byte[] bytes66 = AesCipherUtil.decrypt(bytes, bytes65, aesCrypto);
      decoyKeyTable816 = sha3Hash.digest(bytes66);
      byte[] bytes67 = decoyKeyTable816;
      decoyKeyTable361 = sha3Hash.digest(bytes67);
      byte[] bytes68 = new byte[]{
         122, 36, -116, 63, -7, -14, 25, 102, -80, 2, 59, -55, 127, 12, 94, -103, 83, 111, -82, 66, -104, 14, -60, -89, 76, 56, -68, 78, -44, -78, -111, -97
      };
      byte[] bytes69 = AesCipherUtil.decrypt(bytes, bytes68, aesCrypto);
      decoyKeyTable336 = sha3Hash.digest(bytes69);
      byte[] bytes70 = decoyKeyTable336;
      decoyKeyTable409 = sha3Hash.digest(bytes70);
      byte[] bytes71 = new byte[]{
         121, -82, -39, 87, -91, -46, 79, -115, -5, 73, 42, -72, 38, -128, 1, 98, 36, 108, 28, 3, -11, 39, 1, 14, 37, -117, 22, 80, -18, -32, 19, 13
      };
      byte[] bytes72 = AesCipherUtil.decrypt(bytes, bytes71, aesCrypto);
      decoyKeyTable335 = sha3Hash.digest(bytes72);
      byte[] bytes73 = decoyKeyTable335;
      decoyKeyTable085 = sha3Hash.digest(bytes73);
      byte[] bytes74 = new byte[]{
         82, 90, 66, 123, -65, 89, 72, 65, 13, -127, -110, -55, 78, 80, 17, 119, 92, -70, 61, 50, -77, 117, 0, 41, 82, -43, 105, -77, -85, 45, -2, 31
      };
      byte[] bytes75 = AesCipherUtil.decrypt(bytes, bytes74, aesCrypto);
      decoyKeyTable077 = sha3Hash.digest(bytes75);
      byte[] bytes76 = decoyKeyTable077;
      decoyKeyTable063 = sha3Hash.digest(bytes76);
      byte[] bytes77 = new byte[]{
         95, 91, 82, 70, 73, -115, -116, 47, -2, -85, 73, -14, 46, -26, -78, -95, 103, -24, 66, 18, 114, -41, -84, 12, -44, 86, 74, -118, -75, -89, 4, -80
      };
      byte[] bytes78 = AesCipherUtil.decrypt(bytes, bytes77, aesCrypto);
      decoyKeyTable155 = sha3Hash.digest(bytes78);
      byte[] bytes79 = decoyKeyTable155;
      decoyKeyTable261 = sha3Hash.digest(bytes79);
      byte[] bytes80 = new byte[]{
         -4, -23, 87, 51, -12, -13, -4, 95, -84, 103, -90, 117, -41, -38, -28, -17, -32, -127, -26, -56, 75, -47, 117, 52, -1, -39, 57, 5, -26, 50, -125, 30
      };
      byte[] bytes81 = AesCipherUtil.decrypt(bytes, bytes80, aesCrypto);
      decoyKeyTable276 = sha3Hash.digest(bytes81);
      byte[] bytes82 = decoyKeyTable276;
      decoyKeyTable129 = sha3Hash.digest(bytes82);
      byte[] bytes83 = new byte[]{
         64,
         74,
         -106,
         115,
         -67,
         56,
         27,
         -10,
         -73,
         49,
         115,
         28,
         -93,
         -54,
         -18,
         121,
         27,
         125,
         -127,
         -11,
         -13,
         -108,
         -123,
         74,
         -34,
         -32,
         104,
         -109,
         -11,
         -123,
         -1,
         -30
      };
      byte[] bytes84 = AesCipherUtil.decrypt(bytes, bytes83, aesCrypto);
      decoyKeyTable397 = sha3Hash.digest(bytes84);
      byte[] bytes85 = decoyKeyTable397;
      decoyKeyTable394 = sha3Hash.digest(bytes85);
      byte[] bytes86 = new byte[]{
         41, -14, -9, 20, 91, 19, 56, -46, 37, -105, 25, -110, 44, 118, 63, -43, -67, -44, -90, -13, 11, 40, -79, -61, 69, 86, -81, -79, -51, -72, 53, 92
      };
      byte[] bytes87 = AesCipherUtil.decrypt(bytes, bytes86, aesCrypto);
      decoyKeyTable473 = sha3Hash.digest(bytes87);
      byte[] bytes88 = decoyKeyTable473;
      decoyKeyTable635 = sha3Hash.digest(bytes88);
      byte[] bytes89 = new byte[]{
         -3, -16, -101, 88, -67, -6, 31, 96, 83, 106, 24, -117, -60, 13, 31, -31, -77, 31, 120, -86, 58, 42, -69, -46, 86, 20, 88, -84, -74, 46, 53, 31
      };
      byte[] bytes90 = AesCipherUtil.decrypt(bytes, bytes89, aesCrypto);
      decoyKeyTable444 = sha3Hash.digest(bytes90);
      byte[] bytes91 = decoyKeyTable444;
      decoyKeyTable382 = sha3Hash.digest(bytes91);
      byte[] bytes92 = new byte[]{
         115,
         94,
         -113,
         80,
         -116,
         -77,
         -62,
         101,
         -41,
         -97,
         65,
         23,
         -1,
         -5,
         68,
         -26,
         110,
         105,
         44,
         -126,
         -96,
         -112,
         84,
         -77,
         101,
         -20,
         -102,
         -112,
         -3,
         -93,
         97,
         37
      };
      byte[] bytes93 = AesCipherUtil.decrypt(bytes, bytes92, aesCrypto);
      decoyKeyTable482 = sha3Hash.digest(bytes93);
      byte[] bytes94 = decoyKeyTable482;
      decoyKeyTable675 = sha3Hash.digest(bytes94);
      byte[] bytes95 = new byte[]{
         15,
         -87,
         -117,
         -13,
         39,
         -19,
         -77,
         -49,
         -14,
         -39,
         50,
         114,
         -64,
         -106,
         81,
         -26,
         -30,
         125,
         -81,
         -86,
         -65,
         26,
         -31,
         -123,
         80,
         94,
         61,
         -47,
         68,
         41,
         123,
         124
      };
      byte[] bytes96 = AesCipherUtil.decrypt(bytes, bytes95, aesCrypto);
      decoyKeyTable788 = sha3Hash.digest(bytes96);
      byte[] bytes97 = decoyKeyTable788;
      decoyKeyTable690 = sha3Hash.digest(bytes97);
      byte[] bytes98 = new byte[]{
         -55, -73, 88, 63, -89, -47, 80, -88, 117, -41, -6, 26, -96, 40, -12, 98, -17, -58, 17, 121, 67, -74, 7, 98, -56, 49, -99, -12, 126, 72, -119, -30
      };
      byte[] bytes99 = AesCipherUtil.decrypt(bytes, bytes98, aesCrypto);
      decoyKeyTable251 = sha3Hash.digest(bytes99);
      byte[] bytes100 = decoyKeyTable251;
      decoyKeyTable775 = sha3Hash.digest(bytes100);
      byte[] bytes101 = new byte[]{
         123, 10, 61, 48, -72, 2, 0, 54, 7, 55, 41, 32, -78, -3, -58, -126, 37, 63, -62, -116, 74, -18, 25, -125, 125, 10, 20, -73, 115, -51, -44, 91
      };
      byte[] bytes102 = AesCipherUtil.decrypt(bytes, bytes101, aesCrypto);
      decoyKeyTable602 = sha3Hash.digest(bytes102);
      byte[] bytes103 = decoyKeyTable602;
      decoyKeyTable853 = sha3Hash.digest(bytes103);
      byte[] bytes104 = new byte[]{
         -122, 79, -2, -40, 14, -49, 76, 102, -11, 35, -10, -116, 26, 35, -51, 45, -28, -77, 101, -92, 28, 35, 69, -95, 87, 11, -26, -5, -81, 2, 65, -86
      };
      byte[] bytes105 = AesCipherUtil.decrypt(bytes, bytes104, aesCrypto);
      decoyKeyTable160 = sha3Hash.digest(bytes105);
      byte[] bytes106 = decoyKeyTable160;
      decoyKeyTable508 = sha3Hash.digest(bytes106);
      byte[] bytes107 = new byte[]{
         -10, -34, -124, -25, 29, 63, 92, -64, -65, -37, -32, 52, 36, -47, 110, -35, -87, 103, 88, -88, -38, -26, -125, 24, 111, 97, -84, 58, -3, 10, 30, 92
      };
      byte[] bytes108 = AesCipherUtil.decrypt(bytes, bytes107, aesCrypto);
      decoyKeyTable079 = sha3Hash.digest(bytes108);
      byte[] bytes109 = decoyKeyTable079;
      decoyKeyTable219 = sha3Hash.digest(bytes109);
      byte[] bytes110 = new byte[]{
         120, 51, -2, 110, -72, 30, 94, 82, -107, 11, -32, 38, -120, 110, 98, 122, 82, -50, 93, -116, 6, -33, 45, -96, -122, -61, 25, -89, -46, 77, -59, 100
      };
      byte[] bytes111 = AesCipherUtil.decrypt(bytes, bytes110, aesCrypto);
      decoyKeyTable691 = sha3Hash.digest(bytes111);
      byte[] bytes112 = decoyKeyTable691;
      decoyKeyTable649 = sha3Hash.digest(bytes112);
      byte[] bytes113 = new byte[]{
         122, -97, -13, 31, -53, 87, -59, -12, 59, 2, 97, -43, -102, -112, -112, 11, 20, 36, 13, 90, 4, -84, -67, -111, -121, -88, 5, 40, 50, 111, -3, -96
      };
      byte[] bytes114 = AesCipherUtil.decrypt(bytes, bytes113, aesCrypto);
      decoyKeyTable348 = sha3Hash.digest(bytes114);
      byte[] bytes115 = decoyKeyTable348;
      decoyKeyTable254 = sha3Hash.digest(bytes115);
      byte[] bytes116 = new byte[]{
         -17, -116, -51, -125, 74, -45, -108, 27, -84, 15, 70, 86, 122, 39, 94, 65, 122, 122, -96, 2, 123, 93, 8, 8, -63, -43, 7, -3, 17, -14, -45, 49
      };
      byte[] bytes117 = AesCipherUtil.decrypt(bytes, bytes116, aesCrypto);
      decoyKeyTable287 = sha3Hash.digest(bytes117);
      byte[] bytes118 = decoyKeyTable287;
      decoyKeyTable330 = sha3Hash.digest(bytes118);
      byte[] bytes119 = new byte[]{
         87, 20, 86, -126, 114, -105, -21, -22, 44, 32, 30, -92, -71, 89, -81, 125, 59, -112, -27, 99, 23, 2, -77, -72, -11, -95, -74, -47, 66, -107, -52, -97
      };
      byte[] bytes120 = AesCipherUtil.decrypt(bytes, bytes119, aesCrypto);
      decoyKeyTable564 = sha3Hash.digest(bytes120);
      byte[] bytes121 = decoyKeyTable564;
      decoyKeyTable857 = sha3Hash.digest(bytes121);
      byte[] bytes122 = new byte[]{
         48, 54, 27, -76, 2, 2, 35, -12, -83, 99, -115, -2, 50, -4, 89, 23, 4, -65, 114, 106, 126, 101, 92, -106, -113, 72, -76, 64, 30, -56, 101, 97
      };
      byte[] bytes123 = AesCipherUtil.decrypt(bytes, bytes122, aesCrypto);
      decoyKeyTable802 = sha3Hash.digest(bytes123);
      byte[] bytes124 = decoyKeyTable802;
      decoyKeyTable738 = sha3Hash.digest(bytes124);
      byte[] bytes125 = new byte[]{
         -41,
         18,
         -118,
         -9,
         33,
         105,
         45,
         105,
         27,
         -29,
         -100,
         82,
         111,
         59,
         70,
         19,
         -10,
         69,
         -50,
         -106,
         101,
         -88,
         -72,
         92,
         -103,
         45,
         -126,
         -81,
         -27,
         44,
         108,
         -124
      };
      byte[] bytes126 = AesCipherUtil.decrypt(bytes, bytes125, aesCrypto);
      decoyKeyTable801 = sha3Hash.digest(bytes126);
      byte[] bytes127 = decoyKeyTable801;
      decoyKeyTable354 = sha3Hash.digest(bytes127);
      byte[] bytes128 = new byte[]{
         -42, -88, 27, -13, -85, 88, 41, 100, -115, 32, 87, 44, -69, -13, 39, 1, -30, 66, -5, 91, -1, -34, 6, -67, 96, -67, 26, -100, -1, -93, 91, -93
      };
      byte[] bytes129 = AesCipherUtil.decrypt(bytes, bytes128, aesCrypto);
      decoyKeyTable269 = sha3Hash.digest(bytes129);
      byte[] bytes130 = decoyKeyTable269;
      decoyKeyTable486 = sha3Hash.digest(bytes130);
      byte[] bytes131 = new byte[]{
         60, 42, -103, -71, 30, -67, 100, 6, 8, 90, 118, -33, -101, 28, 2, -21, 72, 120, 110, 13, -61, -98, 97, 73, -114, -96, 24, -91, 14, 123, -45, 101
      };
      byte[] bytes132 = AesCipherUtil.decrypt(bytes, bytes131, aesCrypto);
      decoyKeyTable100 = sha3Hash.digest(bytes132);
      byte[] bytes133 = decoyKeyTable100;
      decoyKeyTable130 = sha3Hash.digest(bytes133);
      byte[] bytes134 = new byte[]{
         83, 82, 93, 44, 127, 18, -112, -57, -26, 30, -50, 19, -34, -111, -125, 32, -8, 98, -25, 84, -31, -4, 28, 10, 43, 106, 64, 127, -110, -117, -100, 53
      };
      byte[] bytes135 = AesCipherUtil.decrypt(bytes, bytes134, aesCrypto);
      decoyKeyTable483 = sha3Hash.digest(bytes135);
      byte[] bytes136 = decoyKeyTable483;
      decoyKeyTable117 = sha3Hash.digest(bytes136);
      byte[] bytes137 = new byte[]{
         32, 63, 40, 33, -19, -1, -124, 68, -27, 19, -37, 66, 90, -84, -79, -24, -45, -13, -28, -29, 62, 3, -5, 97, -108, 0, -4, 2, 83, -2, 94, -3
      };
      byte[] bytes138 = AesCipherUtil.decrypt(bytes, bytes137, aesCrypto);
      decoyKeyTable231 = sha3Hash.digest(bytes138);
      byte[] bytes139 = decoyKeyTable231;
      decoyKeyTable843 = sha3Hash.digest(bytes139);
      byte[] bytes140 = new byte[]{
         -22, -103, -115, -11, 5, 119, 85, 56, -18, 74, -54, 20, 78, -84, 39, -19, -26, 33, -101, 21, -118, 30, -2, -32, 93, -3, -25, -46, -73, 8, -127, -88
      };
      byte[] bytes141 = AesCipherUtil.decrypt(bytes, bytes140, aesCrypto);
      decoyKeyTable021 = sha3Hash.digest(bytes141);
      byte[] bytes142 = decoyKeyTable021;
      decoyKeyTable390 = sha3Hash.digest(bytes142);
      byte[] bytes143 = new byte[]{
         7, 34, 61, -104, -43, -26, -95, 7, -54, -63, -31, -2, 47, -29, -84, 39, 61, 45, 20, -42, -53, 32, -117, 0, -39, -32, 41, 107, 0, -10, -70, 74
      };
      byte[] bytes144 = AesCipherUtil.decrypt(bytes, bytes143, aesCrypto);
      decoyKeyTable068 = sha3Hash.digest(bytes144);
      byte[] bytes145 = decoyKeyTable068;
      decoyKeyTable171 = sha3Hash.digest(bytes145);
      byte[] bytes146 = new byte[]{
         95, 56, 8, -92, 93, 117, 32, -96, -69, 10, -72, 96, 41, -93, -126, -32, 106, -116, -39, 121, -61, 98, -92, 111, 118, 36, -120, -2, 74, 35, -62, -73
      };
      byte[] bytes147 = AesCipherUtil.decrypt(bytes, bytes146, aesCrypto);
      decoyKeyTable315 = sha3Hash.digest(bytes147);
      byte[] bytes148 = decoyKeyTable315;
      decoyKeyTable598 = sha3Hash.digest(bytes148);
      byte[] bytes149 = new byte[]{
         97, -113, 76, -45, -24, 26, 92, -120, 77, 71, -52, -108, 68, -16, 111, -25, 76, -50, -5, 84, -61, -62, -83, 60, -26, 96, -58, -66, -12, -22, -77, 11
      };
      byte[] bytes150 = AesCipherUtil.decrypt(bytes, bytes149, aesCrypto);
      decoyKeyTable440 = sha3Hash.digest(bytes150);
      byte[] bytes151 = decoyKeyTable440;
      decoyKeyTable659 = sha3Hash.digest(bytes151);
      byte[] bytes152 = new byte[]{
         56, 89, 15, -44, 122, 29, 106, 82, 96, 47, -25, -39, 116, 29, -93, 101, 9, -54, -90, -27, 81, 15, 0, -85, 111, -48, 43, 57, 27, 53, -29, -123
      };
      byte[] bytes153 = AesCipherUtil.decrypt(bytes, bytes152, aesCrypto);
      decoyKeyTable653 = sha3Hash.digest(bytes153);
      byte[] bytes154 = decoyKeyTable653;
      decoyKeyTable190 = sha3Hash.digest(bytes154);
      byte[] bytes155 = new byte[]{
         26, -56, -22, -46, 34, -29, -40, -56, 68, -47, -66, 67, -86, -28, -18, 113, 76, 125, -119, 125, -5, -40, -54, -107, 123, -69, 56, -17, 65, 36, 21, -30
      };
      byte[] bytes156 = AesCipherUtil.decrypt(bytes, bytes155, aesCrypto);
      decoyKeyTable114 = sha3Hash.digest(bytes156);
      byte[] bytes157 = decoyKeyTable114;
      decoyKeyTable014 = sha3Hash.digest(bytes157);
      byte[] bytes158 = new byte[]{
         -24, -62, -123, 101, 120, 109, 115, -99, -18, -82, 65, -40, 84, 54, -15, 1, -9, -43, -86, -72, 51, 109, -41, 122, 65, 105, 45, 71, -38, 119, 86, 125
      };
      byte[] bytes159 = AesCipherUtil.decrypt(bytes, bytes158, aesCrypto);
      decoyKeyTable286 = sha3Hash.digest(bytes159);
      byte[] bytes160 = decoyKeyTable286;
      decoyKeyTable344 = sha3Hash.digest(bytes160);
      byte[] bytes161 = new byte[]{
         -53, 75, 96, -74, -4, -70, 71, -80, 71, 34, 39, 97, 61, -109, -45, 95, 84, -69, -103, -48, 41, 12, -78, 96, -63, -77, 47, 15, 21, -48, 56, -23
      };
      byte[] bytes162 = AesCipherUtil.decrypt(bytes, bytes161, aesCrypto);
      decoyKeyTable313 = sha3Hash.digest(bytes162);
      byte[] bytes163 = decoyKeyTable313;
      decoyKeyTable284 = sha3Hash.digest(bytes163);
      byte[] bytes164 = new byte[]{
         -102, -24, 1, -3, -28, -123, 95, 70, 4, -52, 31, 78, 40, 53, 63, -16, -55, 102, 97, 58, 19, -17, 89, -105, -125, 37, -119, 29, 74, -14, 67, 9
      };
      byte[] bytes165 = AesCipherUtil.decrypt(bytes, bytes164, aesCrypto);
      decoyKeyTable644 = sha3Hash.digest(bytes165);
      byte[] bytes166 = decoyKeyTable644;
      decoyKeyTable832 = sha3Hash.digest(bytes166);
      byte[] bytes167 = new byte[]{
         103, 102, 117, -77, -21, -77, -39, 3, 55, 41, 109, -42, 18, -41, -22, -100, -25, -73, 10, -14, -39, 57, 110, 34, 106, -124, -34, 114, 4, -45, 62, 2
      };
      byte[] bytes168 = AesCipherUtil.decrypt(bytes, bytes167, aesCrypto);
      decoyKeyTable782 = sha3Hash.digest(bytes168);
      byte[] bytes169 = decoyKeyTable782;
      decoyKeyTable606 = sha3Hash.digest(bytes169);
      byte[] bytes170 = new byte[]{
         -110, -37, -99, 65, 79, 119, -80, 104, 110, 65, -45, -54, -34, 109, -87, 17, -91, 63, -122, -5, -92, 27, 123, 76, 68, -59, 13, -96, -47, -2, -102, 16
      };
      byte[] bytes171 = AesCipherUtil.decrypt(bytes, bytes170, aesCrypto);
      decoyKeyTable572 = sha3Hash.digest(bytes171);
      byte[] bytes172 = decoyKeyTable572;
      decoyKeyTable530 = sha3Hash.digest(bytes172);
      byte[] bytes173 = new byte[]{
         4, 8, -23, -26, -25, 47, -4, -37, 49, 62, 88, 0, -32, -8, -12, -92, -88, -40, -66, -85, 57, -119, 109, -28, -66, -26, -50, -87, 69, -37, -39, 1
      };
      byte[] bytes174 = AesCipherUtil.decrypt(bytes, bytes173, aesCrypto);
      decoyKeyTable099 = sha3Hash.digest(bytes174);
      byte[] bytes175 = decoyKeyTable099;
      decoyKeyTable693 = sha3Hash.digest(bytes175);
      byte[] bytes176 = new byte[]{
         20, 34, -15, 51, 47, -111, -102, -42, -44, -89, 11, -53, -114, 86, 16, -30, 11, 97, 77, -108, -7, -92, -119, -59, -123, -57, 88, 39, -87, 13, -114, 47
      };
      byte[] bytes177 = AesCipherUtil.decrypt(bytes, bytes176, aesCrypto);
      decoyKeyTable363 = sha3Hash.digest(bytes177);
      byte[] bytes178 = decoyKeyTable363;
      decoyKeyTable643 = sha3Hash.digest(bytes178);
      byte[] bytes179 = new byte[]{
         103, 118, 28, 3, 113, -64, -49, 8, -15, -11, -33, 116, -84, 23, 46, -37, 113, -99, 70, -54, 29, 43, -52, 103, -46, 114, 62, 117, 45, 15, 113, -73
      };
      byte[] bytes180 = AesCipherUtil.decrypt(bytes, bytes179, aesCrypto);
      decoyKeyTable039 = sha3Hash.digest(bytes180);
      byte[] bytes181 = decoyKeyTable039;
      decoyKeyTable152 = sha3Hash.digest(bytes181);
      byte[] bytes182 = new byte[]{
         23, 114, -4, -92, -2, 72, 36, 24, 54, -26, 101, 62, -125, -3, 82, -15, -87, -49, -44, 79, 46, 116, 68, 20, 54, -67, 64, -84, 107, -26, 55, -75
      };
      byte[] bytes183 = AesCipherUtil.decrypt(bytes, bytes182, aesCrypto);
      decoyKeyTable641 = sha3Hash.digest(bytes183);
      byte[] bytes184 = decoyKeyTable641;
      decoyKeyTable268 = sha3Hash.digest(bytes184);
      byte[] bytes185 = new byte[]{
         48,
         -34,
         49,
         -128,
         49,
         47,
         -24,
         27,
         -40,
         41,
         -99,
         -40,
         -15,
         17,
         -115,
         -16,
         65,
         -48,
         121,
         121,
         105,
         120,
         55,
         -29,
         43,
         -30,
         125,
         -116,
         -126,
         -73,
         -21,
         -11
      };
      byte[] bytes186 = AesCipherUtil.decrypt(bytes, bytes185, aesCrypto);
      decoyKeyTable506 = sha3Hash.digest(bytes186);
      byte[] bytes187 = decoyKeyTable506;
      decoyKeyTable449 = sha3Hash.digest(bytes187);
      byte[] bytes188 = new byte[]{
         -110, 80, 71, -63, -42, 4, -49, 118, -87, -14, 107, -124, 122, 41, 71, 47, -53, -123, 107, 96, -117, 107, 109, 89, -42, 126, -47, 16, 46, 11, 65, 17
      };
      byte[] bytes189 = AesCipherUtil.decrypt(bytes, bytes188, aesCrypto);
      decoyKeyTable640 = sha3Hash.digest(bytes189);
      byte[] bytes190 = decoyKeyTable640;
      decoyKeyTable345 = sha3Hash.digest(bytes190);
      byte[] bytes191 = new byte[]{
         -86,
         87,
         -62,
         -74,
         -112,
         -63,
         -42,
         113,
         62,
         -45,
         -88,
         123,
         -92,
         6,
         121,
         -23,
         -18,
         -105,
         73,
         18,
         -95,
         -119,
         -72,
         32,
         123,
         -98,
         89,
         -111,
         93,
         -107,
         -35,
         63
      };
      byte[] bytes192 = AesCipherUtil.decrypt(bytes, bytes191, aesCrypto);
      decoyKeyTable771 = sha3Hash.digest(bytes192);
      byte[] bytes193 = decoyKeyTable771;
      decoyKeyTable131 = sha3Hash.digest(bytes193);
      byte[] bytes194 = new byte[]{
         -77, 74, -122, -77, -118, -52, -74, -56, -97, 19, 69, 83, 66, -69, -65, -128, -98, 93, -44, 29, 41, -44, -68, -50, -91, 30, -8, -17, -116, 44, 86, -67
      };
      byte[] bytes195 = AesCipherUtil.decrypt(bytes, bytes194, aesCrypto);
      decoyKeyTable776 = sha3Hash.digest(bytes195);
      byte[] bytes196 = decoyKeyTable776;
      decoyKeyTable240 = sha3Hash.digest(bytes196);
      byte[] bytes197 = new byte[]{
         61, 84, 77, -114, -4, 103, 79, 15, -53, -110, -56, -13, -109, -9, 89, -122, -34, 95, 124, -19, -40, -2, 89, 93, -102, -28, 13, 101, 2, -41, 41, -118
      };
      byte[] bytes198 = AesCipherUtil.decrypt(bytes, bytes197, aesCrypto);
      decoyKeyTable138 = sha3Hash.digest(bytes198);
      byte[] bytes199 = decoyKeyTable138;
      decoyKeyTable620 = sha3Hash.digest(bytes199);
      byte[] bytes200 = new byte[]{
         -35, -128, 24, -14, 111, -122, 116, 29, -49, 100, -96, -46, 34, -64, 77, 7, 63, -94, 8, 50, 126, -100, -124, -12, -84, -87, -81, -94, 48, 126, 25, -12
      };
      byte[] bytes201 = AesCipherUtil.decrypt(bytes, bytes200, aesCrypto);
      decoyKeyTable372 = sha3Hash.digest(bytes201);
      byte[] bytes202 = decoyKeyTable372;
      decoyKeyTable124 = sha3Hash.digest(bytes202);
      byte[] bytes203 = new byte[]{
         77, -48, 36, 84, -31, -16, -17, 52, -39, -49, 112, -78, 12, 11, 125, 113, -38, -65, 101, -33, 52, -8, -106, 100, -44, 9, -77, -37, -104, -38, 62, 81
      };
      byte[] bytes204 = AesCipherUtil.decrypt(bytes, bytes203, aesCrypto);
      decoyKeyTable057 = sha3Hash.digest(bytes204);
      byte[] bytes205 = decoyKeyTable057;
      decoyKeyTable337 = sha3Hash.digest(bytes205);
      byte[] bytes206 = new byte[]{
         -124, 43, 19, 7, 50, -39, -38, 19, 9, -104, 104, 66, 67, 24, -29, 23, -8, 89, 101, 119, 66, 83, -77, -3, -66, 93, 70, 42, -111, 90, -101, -65
      };
      byte[] bytes207 = AesCipherUtil.decrypt(bytes, bytes206, aesCrypto);
      decoyKeyTable417 = sha3Hash.digest(bytes207);
      byte[] bytes208 = decoyKeyTable417;
      decoyKeyTable778 = sha3Hash.digest(bytes208);
      byte[] bytes209 = new byte[]{
         -27, 101, 89, -63, 102, -26, 51, -26, 109, -52, -65, 127, 31, 115, 82, -54, 117, -72, -85, -106, 8, 60, 7, 122, -64, -39, -34, 21, -35, 74, -81, 24
      };
      byte[] bytes210 = AesCipherUtil.decrypt(bytes, bytes209, aesCrypto);
      decoyKeyTable472 = sha3Hash.digest(bytes210);
      byte[] bytes211 = decoyKeyTable472;
      decoyKeyTable123 = sha3Hash.digest(bytes211);
      byte[] bytes212 = new byte[]{
         97, 62, 18, -47, 35, -93, -113, 13, -12, -2, -114, 94, 60, -26, 58, -110, 15, 42, -106, 5, -69, -46, -64, 6, -100, 16, -4, 87, 66, -3, -11, -91
      };
      byte[] bytes213 = AesCipherUtil.decrypt(bytes, bytes212, aesCrypto);
      decoyKeyTable711 = sha3Hash.digest(bytes213);
      byte[] bytes214 = decoyKeyTable711;
      decoyKeyTable783 = sha3Hash.digest(bytes214);
      byte[] bytes215 = new byte[]{
         -99, 113, -117, -111, -57, 3, -73, -118, 84, 29, -46, -17, 59, 121, -75, 56, -118, 63, -126, 107, -55, 70, 34, -49, -65, -122, 51, 76, 21, -99, 56, 11
      };
      byte[] bytes216 = AesCipherUtil.decrypt(bytes, bytes215, aesCrypto);
      decoyKeyTable780 = sha3Hash.digest(bytes216);
      byte[] bytes217 = decoyKeyTable780;
      decoyKeyTable402 = sha3Hash.digest(bytes217);
      byte[] bytes218 = new byte[]{
         81, -54, -10, 24, -120, -17, 40, -106, -82, 68, -31, 33, -97, 19, 31, 40, 30, 13, -111, 97, 19, 97, -50, -90, 98, 26, -75, -88, 25, -20, 48, -128
      };
      byte[] bytes219 = AesCipherUtil.decrypt(bytes, bytes218, aesCrypto);
      decoyKeyTable714 = sha3Hash.digest(bytes219);
      byte[] bytes220 = decoyKeyTable714;
      decoyKeyTable071 = sha3Hash.digest(bytes220);
   }

   public InputStream openResourceStream(String str) {
      byte[] bytes = null;
      ClientArchiveHolder.getControlFlowMarker();
      byte b = 1;

      outerLoop:
      while (true) {
         while (true) {
            if (b >= 2) {
               if (2 >= b) {
                  bytes = loadedClassHashes.get(str);
                  if (bytes != null) {
                     ;
                  }
               } else {
                  if (3 >= b) {
                     return null;
                  }

                  EncryptedClassLoader encryptedClassLoader = this;
                  String str2 = str;
                  String str3 = str2;
                  bytes = encryptedClassLoader.getResourceEntryBytes(str3);
               }
               break;
            }

            if (b <= 0) {
               if (str.contains("assets/augustus/pictures/logo_64x64.png")) {
                  break;
               }
            } else {
               str = str.substring(1);
            }
         }

         while (8 > b) {
            if (b > 6) {
               return new ByteArrayInputStream(longToBytes(ClientArchiveHolder.lastEntryTimestamp));
            }

            if (bytes == null) {
               continue outerLoop;
            }
         }

         if (b <= 8) {
            return new ByteArrayInputStream(bytes);
         }

         if (9 < b) {
            return new ByteArrayInputStream(loadedClassHashes.get(str));
         }

         if (str.startsWith("/")) {
            ;
         }
      }
   }

   public static void populateDecoyTokensGroupC(byte[] bytes) {
      AesCrypto aesCrypto = new AesCrypto();
      Sha3Hash sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes2 = decoyTokenGroupC12;
      decoyTokenGroupC04 = sha3Hash.digest(bytes2);
      byte[] bytes3 = new byte[]{
         88, 12, -95, -22, 41, 125, 26, -124, -98, 21, -58, -84, -33, -37, -22, -63, 89, 18, 92, -84, -3, -114, 102, -76, 55, -78, -11, -16, -109, 13, -104, 31
      };
      byte[] bytes4 = AesCipherUtil.decrypt(bytes, bytes3, aesCrypto);
      decoyTokenGroupC00 = sha3Hash.digest(bytes4);
      byte[] bytes5 = decoyTokenGroupC00;
      decoyTokenGroupC03 = sha3Hash.digest(bytes5);
      byte[] bytes6 = new byte[]{
         68, 50, 56, 84, -66, -62, -7, 114, 59, 123, -81, -92, -43, -2, -52, 36, -79, 119, -79, -33, -106, -41, -5, -20, 78, 125, 66, -23, 10, -26, 14, 40
      };
      byte[] bytes7 = AesCipherUtil.decrypt(bytes, bytes6, aesCrypto);
      decoyTokenGroupC01 = sha3Hash.digest(bytes7);
      byte[] bytes8 = decoyTokenGroupC01;
      decoyTokenGroupC13 = sha3Hash.digest(bytes8);
      byte[] bytes9 = new byte[]{
         91, 54, 25, -51, -62, 126, 98, 109, 38, -116, -37, 17, -16, -70, -85, 34, -106, 123, 10, 47, -42, -26, 53, -45, -32, 73, 34, -28, 117, 124, -83, 42
      };
      byte[] bytes10 = AesCipherUtil.decrypt(bytes, bytes9, aesCrypto);
      decoyTokenGroupC06 = sha3Hash.digest(bytes10);
      byte[] bytes11 = decoyTokenGroupC06;
      decoyTokenGroupC08 = sha3Hash.digest(bytes11);
      byte[] bytes12 = new byte[]{
         -11, -103, -73, 44, -33, -125, 59, -27, -6, 48, -60, 59, 22, 9, -54, 89, 4, -72, -128, -5, -103, -44, -110, 9, -9, 79, 86, 67, -79, -123, 2, -78
      };
      byte[] bytes13 = AesCipherUtil.decrypt(bytes, bytes12, aesCrypto);
      decoyTokenGroupC07 = sha3Hash.digest(bytes13);
      byte[] bytes14 = decoyTokenGroupC07;
      decoyTokenGroupC10 = sha3Hash.digest(bytes14);
      byte[] bytes15 = new byte[]{
         71,
         46,
         -37,
         110,
         -128,
         -53,
         96,
         -119,
         -24,
         -97,
         -77,
         -121,
         -97,
         -40,
         96,
         -6,
         71,
         -92,
         -97,
         -64,
         42,
         -30,
         -57,
         54,
         -100,
         -40,
         35,
         113,
         -1,
         69,
         30,
         -115
      };
      byte[] bytes16 = AesCipherUtil.decrypt(bytes, bytes15, aesCrypto);
      decoyTokenGroupC02 = sha3Hash.digest(bytes16);
      byte[] bytes17 = decoyTokenGroupC02;
      decoyTokenGroupC11 = sha3Hash.digest(bytes17);
      byte[] bytes18 = new byte[]{
         -111, 16, -55, -86, 98, 58, -16, -88, -40, 12, 53, -80, 57, -5, -31, -70, 23, 96, -40, 47, 91, 105, -92, 9, -35, 35, -71, 24, 41, 122, -96, 37
      };
      byte[] bytes19 = AesCipherUtil.decrypt(bytes, bytes18, aesCrypto);
      decoyTokenGroupC05 = sha3Hash.digest(bytes19);
      byte[] bytes20 = decoyTokenGroupC05;
      decoyTokenGroupC09 = sha3Hash.digest(bytes20);
      byte[] bytes21 = new byte[]{
         15, 12, 83, -31, 69, 127, 92, -59, -15, -73, -42, -76, -66, 78, -124, -4, -19, 87, -54, -58, 43, -50, 63, 44, -60, -71, 90, 103, 70, 104, 62, 35
      };
      byte[] bytes22 = AesCipherUtil.decrypt(bytes, bytes21, aesCrypto);
      decoyTokenGroupC14 = sha3Hash.digest(bytes22);
      byte[] bytes23 = decoyTokenGroupC14;
      decoyKeyTable824 = sha3Hash.digest(bytes23);
      byte[] bytes24 = new byte[]{
         108, -93, -128, -72, 28, 119, 55, 38, -57, 1, 5, -95, 62, -126, -108, 108, 34, 8, 79, 39, -74, -24, 85, -37, -42, 82, 38, 71, -72, 59, -121, -115
      };
      byte[] bytes25 = AesCipherUtil.decrypt(bytes, bytes24, aesCrypto);
      decoyKeyTable278 = sha3Hash.digest(bytes25);
      byte[] bytes26 = decoyKeyTable278;
      decoyKeyTable230 = sha3Hash.digest(bytes26);
      byte[] bytes27 = new byte[]{
         -18,
         -63,
         -116,
         -72,
         78,
         -80,
         62,
         105,
         -68,
         -4,
         -101,
         -45,
         -74,
         121,
         -36,
         1,
         126,
         -19,
         24,
         118,
         -103,
         122,
         -19,
         -22,
         -123,
         -59,
         -67,
         -112,
         -113,
         -53,
         -14,
         -20
      };
      byte[] bytes28 = AesCipherUtil.decrypt(bytes, bytes27, aesCrypto);
      decoyKeyTable827 = sha3Hash.digest(bytes28);
      byte[] bytes29 = decoyKeyTable827;
      decoyKeyTable135 = sha3Hash.digest(bytes29);
      byte[] bytes30 = new byte[]{
         -76, 41, -45, 84, 37, 1, 39, -90, 69, -44, 115, -105, -97, -124, 35, -128, 30, 116, -123, -88, 67, 28, -120, 38, -55, -51, -8, 90, 101, 77, 108, -91
      };
      byte[] bytes31 = AesCipherUtil.decrypt(bytes, bytes30, aesCrypto);
      decoyKeyTable312 = sha3Hash.digest(bytes31);
      byte[] bytes32 = decoyKeyTable312;
      decoyKeyTable192 = sha3Hash.digest(bytes32);
      byte[] bytes33 = new byte[]{
         32, 44, -122, 5, 83, -112, 18, -68, -52, 86, 26, -25, -114, -35, 56, 46, -72, 54, 41, -56, -13, -46, 108, -8, -23, -24, 45, 51, 120, -102, -15, -4
      };
      byte[] bytes34 = AesCipherUtil.decrypt(bytes, bytes33, aesCrypto);
      decoyKeyTable671 = sha3Hash.digest(bytes34);
      byte[] bytes35 = decoyKeyTable671;
      decoyKeyTable484 = sha3Hash.digest(bytes35);
      byte[] bytes36 = new byte[]{
         -80, 4, 10, -128, -13, -104, 45, -128, 45, -72, 70, 0, -98, -38, 41, 105, 122, 83, 64, 50, 57, -126, 45, 86, -55, 114, -101, -40, 104, 63, 74, 71
      };
      byte[] bytes37 = AesCipherUtil.decrypt(bytes, bytes36, aesCrypto);
      decoyKeyTable768 = sha3Hash.digest(bytes37);
      byte[] bytes38 = decoyKeyTable768;
      decoyKeyTable342 = sha3Hash.digest(bytes38);
      byte[] bytes39 = new byte[]{
         -81, -67, -28, -26, 91, 90, -54, 31, -38, -84, -29, -24, 91, -50, -98, 56, -5, 82, -82, -91, 126, -45, 62, 65, 81, -38, -15, -25, 98, 92, -96, 54
      };
      byte[] bytes40 = AesCipherUtil.decrypt(bytes, bytes39, aesCrypto);
      decoyKeyTable753 = sha3Hash.digest(bytes40);
      byte[] bytes41 = decoyKeyTable753;
      decoyKeyTable689 = sha3Hash.digest(bytes41);
      byte[] bytes42 = new byte[]{
         70, 32, -78, -65, 87, 73, 76, -25, -114, -47, 123, 99, -47, -14, 78, 50, 92, 31, 14, -77, -28, -30, -46, 55, -103, 117, 8, -35, -111, -54, 78, -110
      };
      byte[] bytes43 = AesCipherUtil.decrypt(bytes, bytes42, aesCrypto);
      decoyKeyTable092 = sha3Hash.digest(bytes43);
      byte[] bytes44 = decoyKeyTable092;
      decoyKeyTable455 = sha3Hash.digest(bytes44);
      byte[] bytes45 = new byte[]{
         73, 102, 77, 98, -44, 52, 115, 58, 44, 57, -60, 56, -21, -42, 71, -4, -6, 0, -28, 61, -69, -36, -90, 111, 94, 118, -11, -69, 55, 110, -114, 114
      };
      byte[] bytes46 = AesCipherUtil.decrypt(bytes, bytes45, aesCrypto);
      decoyKeyTable321 = sha3Hash.digest(bytes46);
      byte[] bytes47 = decoyKeyTable321;
      decoyKeyTable677 = sha3Hash.digest(bytes47);
      byte[] bytes48 = new byte[]{
         -31,
         -22,
         -109,
         -112,
         -128,
         18,
         -123,
         71,
         92,
         54,
         25,
         -19,
         120,
         111,
         92,
         16,
         -10,
         -128,
         -128,
         -51,
         119,
         -76,
         99,
         -111,
         -58,
         55,
         93,
         -103,
         28,
         108,
         -15,
         -121
      };
      byte[] bytes49 = AesCipherUtil.decrypt(bytes, bytes48, aesCrypto);
      decoyKeyTable339 = sha3Hash.digest(bytes49);
      byte[] bytes50 = decoyKeyTable339;
      decoyKeyTable090 = sha3Hash.digest(bytes50);
      byte[] bytes51 = new byte[]{
         -99, 71, 7, 28, -87, -24, 26, 46, -89, -44, 68, 40, -44, 101, -13, 107, -124, 64, 110, 76, 119, 99, -27, 94, -27, 114, 94, -68, 55, 109, -92, 24
      };
      byte[] bytes52 = AesCipherUtil.decrypt(bytes, bytes51, aesCrypto);
      decoyKeyTable133 = sha3Hash.digest(bytes52);
      byte[] bytes53 = decoyKeyTable133;
      decoyKeyTable724 = sha3Hash.digest(bytes53);
      byte[] bytes54 = new byte[]{
         22, -94, -89, -68, -108, -52, -95, 52, 43, 106, -14, 19, 78, 21, 123, -34, 80, 44, 6, -5, 61, 7, -43, -22, -61, -38, -40, -96, 81, 115, 92, -51
      };
      byte[] bytes55 = AesCipherUtil.decrypt(bytes, bytes54, aesCrypto);
      decoyKeyTable048 = sha3Hash.digest(bytes55);
      byte[] bytes56 = decoyKeyTable048;
      decoyKeyTable648 = sha3Hash.digest(bytes56);
      byte[] bytes57 = new byte[]{
         -101,
         -91,
         -57,
         5,
         117,
         -126,
         -27,
         -53,
         15,
         -42,
         -79,
         -110,
         8,
         -126,
         113,
         -99,
         79,
         77,
         126,
         102,
         -101,
         -46,
         -66,
         1,
         -50,
         -72,
         -23,
         -63,
         -51,
         116,
         92,
         102
      };
      byte[] bytes58 = AesCipherUtil.decrypt(bytes, bytes57, aesCrypto);
      decoyKeyTable163 = sha3Hash.digest(bytes58);
      byte[] bytes59 = decoyKeyTable163;
      decoyKeyTable504 = sha3Hash.digest(bytes59);
      byte[] bytes60 = new byte[]{
         16,
         85,
         108,
         36,
         72,
         53,
         -66,
         23,
         -41,
         -56,
         120,
         -76,
         -88,
         -118,
         -68,
         -115,
         -109,
         11,
         56,
         -37,
         45,
         -64,
         34,
         -85,
         -41,
         -120,
         10,
         51,
         103,
         -87,
         -118,
         -72
      };
      byte[] bytes61 = AesCipherUtil.decrypt(bytes, bytes60, aesCrypto);
      decoyKeyTable448 = sha3Hash.digest(bytes61);
      byte[] bytes62 = decoyKeyTable448;
      decoyKeyTable288 = sha3Hash.digest(bytes62);
      byte[] bytes63 = new byte[]{
         -106, 42, -16, -125, 122, 59, -6, -56, -26, 117, 124, 66, -15, 5, 1, 17, -90, -91, 13, -67, -117, -113, 0, 99, 88, 21, -92, 99, 60, -67, -39, -104
      };
      byte[] bytes64 = AesCipherUtil.decrypt(bytes, bytes63, aesCrypto);
      decoyKeyTable595 = sha3Hash.digest(bytes64);
      byte[] bytes65 = decoyKeyTable595;
      decoyKeyTable467 = sha3Hash.digest(bytes65);
      byte[] bytes66 = new byte[]{
         45, 51, -2, 116, 114, 122, -62, -112, -3, 13, -15, 1, 86, -42, 54, -71, -117, 44, 0, 1, 101, -90, -56, 55, -126, -92, -127, 122, -128, 70, 28, 67
      };
      byte[] bytes67 = AesCipherUtil.decrypt(bytes, bytes66, aesCrypto);
      decoyKeyTable193 = sha3Hash.digest(bytes67);
      byte[] bytes68 = decoyKeyTable193;
      decoyKeyTable756 = sha3Hash.digest(bytes68);
      byte[] bytes69 = new byte[]{
         118,
         44,
         -98,
         -51,
         -62,
         13,
         -110,
         -94,
         -37,
         -76,
         127,
         23,
         102,
         -34,
         99,
         -23,
         -64,
         50,
         -93,
         56,
         -29,
         110,
         11,
         -52,
         -79,
         -56,
         19,
         -68,
         108,
         58,
         -119,
         -121
      };
      byte[] bytes70 = AesCipherUtil.decrypt(bytes, bytes69, aesCrypto);
      decoyKeyTable587 = sha3Hash.digest(bytes70);
      byte[] bytes71 = decoyKeyTable587;
      decoyKeyTable238 = sha3Hash.digest(bytes71);
      byte[] bytes72 = new byte[]{
         0, -52, -92, 20, -70, -32, 63, 118, -62, -64, 88, 121, -93, -110, -19, 117, 69, -13, -25, -3, 113, -123, 117, 50, 19, -4, -116, 7, -13, -41, -121, -8
      };
      byte[] bytes73 = AesCipherUtil.decrypt(bytes, bytes72, aesCrypto);
      decoyKeyTable347 = sha3Hash.digest(bytes73);
      byte[] bytes74 = decoyKeyTable347;
      decoyKeyTable325 = sha3Hash.digest(bytes74);
      byte[] bytes75 = new byte[]{
         7, 77, -12, 92, 64, 108, -6, -34, 37, 107, 71, -20, 109, -39, -28, 62, 75, -116, 109, -104, 84, 70, 11, 69, 2, 94, 105, 60, 19, -114, -114, 121
      };
      byte[] bytes76 = AesCipherUtil.decrypt(bytes, bytes75, aesCrypto);
      decoyKeyTable750 = sha3Hash.digest(bytes76);
      byte[] bytes77 = decoyKeyTable750;
      decoyKeyTable419 = sha3Hash.digest(bytes77);
      byte[] bytes78 = new byte[]{
         21, 71, 97, 78, 2, -96, -28, 36, -68, 112, -6, 48, -59, -41, 116, -123, -127, 16, -119, -113, 84, 72, -2, 51, 125, 116, -27, 111, -78, -74, 126, -40
      };
      byte[] bytes79 = AesCipherUtil.decrypt(bytes, bytes78, aesCrypto);
      decoyKeyTable314 = sha3Hash.digest(bytes79);
      byte[] bytes80 = decoyKeyTable314;
      decoyKeyTable551 = sha3Hash.digest(bytes80);
      byte[] bytes81 = new byte[]{
         84, -40, -41, 77, 10, 8, -40, 78, 74, -91, -88, -53, 34, 58, -26, -101, 11, -122, 97, -71, -35, -22, -4, 64, 127, 26, 70, 34, 79, 73, -93, -32
      };
      byte[] bytes82 = AesCipherUtil.decrypt(bytes, bytes81, aesCrypto);
      decoyKeyTable460 = sha3Hash.digest(bytes82);
      byte[] bytes83 = decoyKeyTable460;
      decoyKeyTable208 = sha3Hash.digest(bytes83);
      byte[] bytes84 = new byte[]{
         109,
         -101,
         -104,
         -53,
         7,
         47,
         -65,
         58,
         -108,
         90,
         -101,
         -83,
         70,
         -99,
         -18,
         116,
         115,
         48,
         -114,
         -17,
         105,
         46,
         112,
         -20,
         -89,
         -42,
         46,
         -52,
         -24,
         73,
         -88,
         -81
      };
      byte[] bytes85 = AesCipherUtil.decrypt(bytes, bytes84, aesCrypto);
      decoyKeyTable614 = sha3Hash.digest(bytes85);
      byte[] bytes86 = decoyKeyTable614;
      decoyKeyTable655 = sha3Hash.digest(bytes86);
      byte[] bytes87 = new byte[]{
         -128, 63, -37, 115, -75, 50, 5, 115, 5, -15, -124, 86, 74, -109, 124, -56, 85, -78, 93, 64, -126, 45, -29, 66, -34, 95, -111, -59, 48, -11, 60, 104
      };
      byte[] bytes88 = AesCipherUtil.decrypt(bytes, bytes87, aesCrypto);
      decoyKeyTable535 = sha3Hash.digest(bytes88);
      byte[] bytes89 = decoyKeyTable535;
      decoyKeyTable143 = sha3Hash.digest(bytes89);
      byte[] bytes90 = new byte[]{
         4, -15, -8, 72, -50, 1, 14, 47, -5, 95, -61, -22, -97, 15, -94, 21, 14, 50, -79, -60, 83, 82, -97, -45, -88, 91, -4, -39, 35, -79, 5, -89
      };
      byte[] bytes91 = AesCipherUtil.decrypt(bytes, bytes90, aesCrypto);
      decoyKeyTable739 = sha3Hash.digest(bytes91);
      byte[] bytes92 = decoyKeyTable739;
      decoyKeyTable519 = sha3Hash.digest(bytes92);
      byte[] bytes93 = new byte[]{
         5, 54, 47, -72, 39, -15, -85, 10, -104, 125, -9, -19, -33, -78, 37, -60, 62, -54, 25, 26, 90, 71, 5, 13, -21, -45, -101, -79, 28, 105, 36, 98
      };
      byte[] bytes94 = AesCipherUtil.decrypt(bytes, bytes93, aesCrypto);
      decoyKeyTable176 = sha3Hash.digest(bytes94);
      byte[] bytes95 = decoyKeyTable176;
      decoyKeyTable020 = sha3Hash.digest(bytes95);
   }

   private static void deobfuscateClassBytes(String str, int n, byte[] bytes, int n2, byte[] bytes2) {
      int n3 = 0;
      byte b = 0;
      int n4 = 0;
      int n5 = 0;
      byte b2 = 11;
      int[] controlFlowMarker = ClientArchiveHolder.getControlFlowMarker();

      outerLoop:
      while (true) {
         if (5 <= b2) {
            if (b2 <= 5) {
               bytes2[n5] = (byte)((bytes2[n5] ^ (byte)(n4 + -111)) - b);
            } else if (8 <= b2) {
               if (8 >= b2) {
                  n4--;
               } else {
                  if (9 >= b2) {
                     bytes2[n4] = (byte)(bytes2[n4] ^ bytes[n4 % n2]);
                     continue;
                  }

                  if (n4 < 0) {
                     continue;
                  }
               }
            } else if (b2 <= 6) {
               n3 = bytes[n4 % n2] & 7;
            } else if (n4 < n) {
               continue;
            }
         } else if (b2 >= 2) {
            if (b2 <= 2) {
               n4 = 0;
               continue;
            }

            if (3 < b2) {
               n4 = Math.abs(str.hashCode()) % 2 + 2 - 1;
               continue;
            }

            n5 = 0;
         } else if (b2 > 0) {
            n4++;
         }

         while (17 <= b2) {
            if (17 >= b2) {
               if (n <= 0) {
                  continue outerLoop;
               }
            } else if (b2 >= 20) {
               if (20 >= b2) {
                  if (n3 > 0) {
                     ;
                  }
               } else {
                  if (b2 <= 21) {
                     n5++;
                     continue outerLoop;
                  }

                  bytes2[n5] = (byte)((bytes2[n5] & 255) >>> n3 | (bytes2[n5] & 255) << 8 - n3);
               }
            } else {
               if (b2 > 18) {
                  if (n5 < n) {
                     ;
                  }
                  continue outerLoop;
               }

               n5 = (bytes[(n4 + 1) % n2] & 255) % n;
            }
         }

         if (14 <= b2) {
            if (b2 > 14) {
               if (15 >= b2) {
                  return;
               }

               b = bytes[(n5 + n4) % n2];
            }
         } else if (b2 <= 12) {
            if (n5 > 0) {
               ByteRotator.rotateRight(bytes2, n5);
            }
         }
      }
   }

   private static void cleanDirectBuffer(long l, ByteBuffer buf) {
      int[] controlFlowMarker = ClientArchiveHolder.getControlFlowMarker();
      if (buf != null && buf.isDirect()) {
         try {
            Method method = buf.getClass().getMethod("cleaner");
            method.setAccessible(true);
            Object obj = method.invoke(buf);
            if (obj != null) {
               Method method2 = obj.getClass().getMethod("clean");
               method2.invoke(obj);
            }
         } catch (Throwable e) {
         }
      }
   }

   public static void populateDecoyKeysGroupB(byte[] bytes) {
      AesCrypto aesCrypto = new AesCrypto();
      Sha3Hash sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      byte[] bytes2 = new byte[]{
         32,
         -69,
         -120,
         -61,
         80,
         -108,
         43,
         -46,
         78,
         -122,
         104,
         77,
         55,
         -3,
         -8,
         -119,
         76,
         -96,
         65,
         126,
         -90,
         -51,
         -40,
         -12,
         63,
         -107,
         -9,
         -24,
         78,
         -56,
         106,
         -127
      };
      byte[] bytes3 = AesCipherUtil.decrypt(bytes, bytes2, aesCrypto);
      decoyKeyGroupB02 = sha3Hash.digest(bytes3);
      byte[] bytes4 = decoyKeyGroupB02;
      decoyKeyGroupB14 = sha3Hash.digest(bytes4);
      byte[] bytes5 = new byte[]{
         67, 88, 53, 109, 74, 55, -48, -6, 103, -3, -95, 42, -120, -29, 36, 94, -122, 24, 71, -97, -5, -13, 124, -27, -68, -39, -128, -11, 82, -44, -84, -52
      };
      byte[] bytes6 = AesCipherUtil.decrypt(bytes, bytes5, aesCrypto);
      decoyKeyGroupB11 = sha3Hash.digest(bytes6);
      byte[] bytes7 = decoyKeyGroupB11;
      decoyKeyGroupB10 = sha3Hash.digest(bytes7);
      byte[] bytes8 = new byte[]{
         11, 86, -9, -29, -30, -83, -67, 98, -110, 51, 66, -44, 68, -81, -15, 127, 109, -85, 86, -127, -11, -88, 48, 54, 20, 48, -55, 45, 33, 72, -114, -54
      };
      byte[] bytes9 = AesCipherUtil.decrypt(bytes, bytes8, aesCrypto);
      decoyKeyGroupB04 = sha3Hash.digest(bytes9);
      byte[] bytes10 = decoyKeyGroupB04;
      decoyKeyGroupB01 = sha3Hash.digest(bytes10);
      byte[] bytes11 = new byte[]{
         19, 29, 102, 18, -88, -103, 40, 3, -120, -2, 116, -88, 86, -51, 42, -75, 24, 9, -97, 53, -60, -85, 115, -58, 2, -62, 34, -69, -27, 115, 36, 45
      };
      byte[] bytes12 = AesCipherUtil.decrypt(bytes, bytes11, aesCrypto);
      decoyKeyGroupB08 = sha3Hash.digest(bytes12);
      byte[] bytes13 = decoyKeyGroupB08;
      decoyKeyGroupB06 = sha3Hash.digest(bytes13);
      byte[] bytes14 = new byte[]{
         126, -86, -72, -122, 66, 116, 53, -21, -32, 49, 55, 91, 104, -24, -127, -51, -17, 114, 67, -120, 88, 82, 90, -5, 22, -80, -86, 45, 13, -79, -30, 87
      };
      byte[] bytes15 = AesCipherUtil.decrypt(bytes, bytes14, aesCrypto);
      decoyKeyGroupB09 = sha3Hash.digest(bytes15);
      byte[] bytes16 = decoyKeyGroupB09;
      decoyKeyGroupB12 = sha3Hash.digest(bytes16);
      byte[] bytes17 = new byte[]{
         -101, -85, 9, -123, 4, 125, -20, -32, 112, -97, -74, -1, 40, -56, 81, 66, 74, -107, -117, -118, -76, -61, -92, -42, -85, 95, 64, 93, -35, 53, 46, -116
      };
      byte[] bytes18 = AesCipherUtil.decrypt(bytes, bytes17, aesCrypto);
      decoyKeyGroupB03 = sha3Hash.digest(bytes18);
      byte[] bytes19 = decoyKeyGroupB03;
      decoyKeyGroupB05 = sha3Hash.digest(bytes19);
      byte[] bytes20 = new byte[]{
         56, 0, -61, 44, 84, -106, -40, -15, 86, -62, -64, 23, -66, 112, 17, -97, -127, -51, 42, -13, 61, 109, -7, -83, 69, 100, 65, 33, 42, 95, -91, -81
      };
      byte[] bytes21 = AesCipherUtil.decrypt(bytes, bytes20, aesCrypto);
      decoyKeyGroupB07 = sha3Hash.digest(bytes21);
      byte[] bytes22 = decoyKeyGroupB07;
      decoyKeyGroupB13 = sha3Hash.digest(bytes22);
      byte[] bytes23 = new byte[]{
         -28, -87, -51, 116, 88, -82, 3, 28, 97, -100, 37, -80, 80, 58, 90, -3, 23, -90, -14, -79, 56, -89, -52, -80, -111, 95, -71, -4, 99, -25, 74, -51
      };
      byte[] bytes24 = AesCipherUtil.decrypt(bytes, bytes23, aesCrypto);
      decoyKeyGroupB00 = sha3Hash.digest(bytes24);
      byte[] bytes25 = decoyKeyGroupB00;
      decoyKeyTable516 = sha3Hash.digest(bytes25);
      byte[] bytes26 = new byte[]{
         88, -85, 98, -68, -90, -33, -102, 58, -98, -71, 19, 109, -100, 52, -127, -24, -3, 120, -83, -21, 0, -16, 73, -97, 101, -42, -7, -85, -69, -73, 87, 49
      };
      byte[] bytes27 = AesCipherUtil.decrypt(bytes, bytes26, aesCrypto);
      decoyKeyTable717 = sha3Hash.digest(bytes27);
      byte[] bytes28 = decoyKeyTable717;
      decoyKeyTable134 = sha3Hash.digest(bytes28);
      byte[] bytes29 = new byte[]{
         -33, -16, 9, -22, 34, -48, -80, -95, 69, -75, 26, -128, 84, 81, 41, -107, 66, -90, 62, 38, -39, -114, -13, -98, -56, -65, -99, -104, 24, 54, 105, 97
      };
      byte[] bytes30 = AesCipherUtil.decrypt(bytes, bytes29, aesCrypto);
      decoyKeyTable125 = sha3Hash.digest(bytes30);
      byte[] bytes31 = decoyKeyTable125;
      decoyKeyTable629 = sha3Hash.digest(bytes31);
      byte[] bytes32 = new byte[]{
         104, 1, -99, -42, -13, 83, -60, 23, 105, 29, -56, -33, 87, 18, 98, -121, 71, 61, 30, 88, 12, 62, -58, -110, -30, -124, 105, 89, 19, 126, 57, -128
      };
      byte[] bytes33 = AesCipherUtil.decrypt(bytes, bytes32, aesCrypto);
      decoyKeyTable055 = sha3Hash.digest(bytes33);
      byte[] bytes34 = decoyKeyTable055;
      decoyKeyTable025 = sha3Hash.digest(bytes34);
      byte[] bytes35 = new byte[]{
         104, -26, -97, -117, -2, 35, -116, 22, -94, 27, -80, -88, -108, -46, -42, 64, -17, -93, 26, 92, 44, -95, -116, 78, 27, 55, 35, 119, -36, 13, 16, 42
      };
      byte[] bytes36 = AesCipherUtil.decrypt(bytes, bytes35, aesCrypto);
      decoyKeyTable173 = sha3Hash.digest(bytes36);
      byte[] bytes37 = decoyKeyTable173;
      decoyKeyTable375 = sha3Hash.digest(bytes37);
      byte[] bytes38 = new byte[]{
         -87, -87, -66, -39, -95, 14, -52, -39, -8, 3, -123, -60, 118, 117, 21, 21, 105, 18, 80, -123, -128, -73, 44, 51, 98, 65, -20, 93, -16, 58, -27, -48
      };
      byte[] bytes39 = AesCipherUtil.decrypt(bytes, bytes38, aesCrypto);
      decoyKeyTable745 = sha3Hash.digest(bytes39);
      byte[] bytes40 = decoyKeyTable745;
      decoyKeyTable461 = sha3Hash.digest(bytes40);
      byte[] bytes41 = new byte[]{
         98, -40, 112, 64, 61, -24, 69, 58, -16, 108, 70, -34, 56, 65, 7, -5, 59, 120, -124, -110, -116, 35, -80, -4, -59, 111, 10, 68, 55, 49, 118, -72
      };
      byte[] bytes42 = AesCipherUtil.decrypt(bytes, bytes41, aesCrypto);
      decoyKeyTable346 = sha3Hash.digest(bytes42);
      byte[] bytes43 = decoyKeyTable346;
      decoyKeyTable272 = sha3Hash.digest(bytes43);
      byte[] bytes44 = new byte[]{
         -9, -21, 13, 22, 93, -111, 55, -23, -118, 23, -40, -96, 30, 66, -99, -91, 48, -113, 52, -68, -7, 114, 30, 122, -102, 43, -5, 42, -63, -36, -23, -127
      };
      byte[] bytes45 = AesCipherUtil.decrypt(bytes, bytes44, aesCrypto);
      decoyKeyTable464 = sha3Hash.digest(bytes45);
      byte[] bytes46 = decoyKeyTable464;
      decoyKeyTable263 = sha3Hash.digest(bytes46);
      byte[] bytes47 = new byte[]{
         -13,
         108,
         36,
         -110,
         33,
         -116,
         -89,
         73,
         111,
         -41,
         36,
         -114,
         87,
         -111,
         -1,
         -101,
         50,
         -117,
         60,
         14,
         -121,
         -82,
         -97,
         -88,
         59,
         110,
         80,
         69,
         101,
         35,
         4,
         -100
      };
      byte[] bytes48 = AesCipherUtil.decrypt(bytes, bytes47, aesCrypto);
      decoyKeyTable805 = sha3Hash.digest(bytes48);
      byte[] bytes49 = decoyKeyTable805;
      decoyKeyTable542 = sha3Hash.digest(bytes49);
      byte[] bytes50 = new byte[]{
         -17, 4, -52, -103, 93, 49, -118, -76, -125, -128, 125, 94, 41, -59, -63, 44, -48, -120, 24, -25, -27, 3, 90, -3, -77, -1, 8, 109, 0, -31, 88, -124
      };
      byte[] bytes51 = AesCipherUtil.decrypt(bytes, bytes50, aesCrypto);
      decoyKeyTable480 = sha3Hash.digest(bytes51);
      byte[] bytes52 = decoyKeyTable480;
      decoyKeyTable209 = sha3Hash.digest(bytes52);
      byte[] bytes53 = new byte[]{
         -66, -41, 51, -107, -41, 30, 119, -27, 80, -62, 4, -14, -3, 68, 20, -38, 20, -83, -86, 85, -31, -119, -124, 24, -119, -39, 96, -95, -111, -33, 8, -27
      };
      byte[] bytes54 = AesCipherUtil.decrypt(bytes, bytes53, aesCrypto);
      decoyKeyTable149 = sha3Hash.digest(bytes54);
      byte[] bytes55 = decoyKeyTable149;
      decoyKeyTable798 = sha3Hash.digest(bytes55);
      byte[] bytes56 = new byte[]{
         -2, 116, -86, -16, -113, 3, -47, -81, 20, 31, -68, -56, 70, -31, -125, -86, 120, 28, -68, -61, 53, 5, 68, 83, -82, 49, -82, -21, -53, 51, 23, -10
      };
      byte[] bytes57 = AesCipherUtil.decrypt(bytes, bytes56, aesCrypto);
      decoyKeyTable121 = sha3Hash.digest(bytes57);
      byte[] bytes58 = decoyKeyTable121;
      decoyKeyTable796 = sha3Hash.digest(bytes58);
      byte[] bytes59 = new byte[]{
         -73,
         46,
         -99,
         -68,
         -54,
         88,
         63,
         -107,
         -15,
         -69,
         63,
         -117,
         -106,
         113,
         54,
         101,
         -121,
         48,
         109,
         -116,
         74,
         -73,
         -57,
         19,
         119,
         -105,
         -25,
         11,
         -11,
         46,
         44,
         93
      };
      byte[] bytes60 = AesCipherUtil.decrypt(bytes, bytes59, aesCrypto);
      decoyKeyTable168 = sha3Hash.digest(bytes60);
      byte[] bytes61 = decoyKeyTable168;
      decoyKeyTable568 = sha3Hash.digest(bytes61);
      byte[] bytes62 = new byte[]{
         -113,
         83,
         -86,
         -126,
         -65,
         -96,
         -77,
         46,
         -6,
         -87,
         -25,
         -44,
         -79,
         -126,
         110,
         10,
         3,
         -123,
         -73,
         -109,
         -14,
         -88,
         -15,
         3,
         -46,
         -24,
         116,
         125,
         -107,
         -62,
         -86,
         19
      };
      byte[] bytes63 = AesCipherUtil.decrypt(bytes, bytes62, aesCrypto);
      decoyKeyTable841 = sha3Hash.digest(bytes63);
      byte[] bytes64 = decoyKeyTable841;
      decoyKeyTable324 = sha3Hash.digest(bytes64);
      byte[] bytes65 = new byte[]{
         -77, -79, 48, -25, 53, 124, 101, -76, -53, 121, 83, -96, -106, -60, 106, -100, -61, -98, -56, 83, -88, 93, -7, 85, 11, 68, -1, 127, -74, -126, 24, -63
      };
      byte[] bytes66 = AesCipherUtil.decrypt(bytes, bytes65, aesCrypto);
      decoyKeyTable601 = sha3Hash.digest(bytes66);
      byte[] bytes67 = decoyKeyTable601;
      decoyKeyTable661 = sha3Hash.digest(bytes67);
      byte[] bytes68 = new byte[]{
         -92, -60, -30, 113, -23, -17, -41, 63, -39, 74, -3, 20, 43, 115, -59, 35, 16, 93, 89, 75, 116, 71, 114, 40, -37, -37, 28, -94, -31, -128, 105, -124
      };
      byte[] bytes69 = AesCipherUtil.decrypt(bytes, bytes68, aesCrypto);
      decoyKeyTable187 = sha3Hash.digest(bytes69);
      byte[] bytes70 = decoyKeyTable187;
      decoyKeyTable662 = sha3Hash.digest(bytes70);
      byte[] bytes71 = new byte[]{
         65, -28, -74, 56, 16, 5, -25, -15, -69, 126, -82, 3, 32, -43, 89, -17, 48, -72, 86, -52, -124, -54, -72, -34, 104, -97, -11, -89, 56, 74, -117, 98
      };
      byte[] bytes72 = AesCipherUtil.decrypt(bytes, bytes71, aesCrypto);
      decoyKeyTable387 = sha3Hash.digest(bytes72);
      byte[] bytes73 = decoyKeyTable387;
      decoyKeyTable571 = sha3Hash.digest(bytes73);
      byte[] bytes74 = new byte[]{
         118, 19, -44, -60, -128, 69, 25, 9, -27, -85, -125, -75, 32, 88, -100, -31, -96, 88, 14, 0, -118, 90, -70, -4, 86, -117, -72, 53, 45, 105, -100, -28
      };
      byte[] bytes75 = AesCipherUtil.decrypt(bytes, bytes74, aesCrypto);
      decoyKeyTable623 = sha3Hash.digest(bytes75);
      byte[] bytes76 = decoyKeyTable623;
      decoyKeyTable353 = sha3Hash.digest(bytes76);
      byte[] bytes77 = new byte[]{
         -10, 87, 40, -75, 26, -110, -64, 118, -57, 76, -120, 29, 16, 20, 126, 12, -13, -72, -78, 10, -124, 112, 39, -92, -77, 67, 69, -57, -47, -22, -29, 66
      };
      byte[] bytes78 = AesCipherUtil.decrypt(bytes, bytes77, aesCrypto);
      decoyKeyTable762 = sha3Hash.digest(bytes78);
      byte[] bytes79 = decoyKeyTable762;
      decoyKeyTable492 = sha3Hash.digest(bytes79);
      byte[] bytes80 = new byte[]{
         1, 21, 3, 39, 33, -21, -125, 9, -32, -41, 82, -68, 78, 120, -76, 9, -50, 55, -45, 0, -86, -124, -118, 14, -119, 120, -121, 127, 117, 45, -103, -106
      };
      byte[] bytes81 = AesCipherUtil.decrypt(bytes, bytes80, aesCrypto);
      decoyKeyTable685 = sha3Hash.digest(bytes81);
      byte[] bytes82 = decoyKeyTable685;
      decoyKeyTable578 = sha3Hash.digest(bytes82);
      byte[] bytes83 = new byte[]{
         -31, -27, -67, 31, -80, -101, 33, -62, 34, 9, 95, 106, -8, -82, -70, -110, 59, 87, 42, 48, 10, 31, -115, 56, -116, 114, 121, -61, -83, 55, -18, -84
      };
      byte[] bytes84 = AesCipherUtil.decrypt(bytes, bytes83, aesCrypto);
      decoyKeyTable273 = sha3Hash.digest(bytes84);
      byte[] bytes85 = decoyKeyTable273;
      decoyKeyTable656 = sha3Hash.digest(bytes85);
      byte[] bytes86 = new byte[]{
         -29, 57, -122, 126, 114, 71, 108, 88, -68, 9, 71, 64, -28, -111, 12, -64, 38, -100, 117, 35, 61, -109, -20, -109, 40, -107, -7, 39, 75, -65, -89, -56
      };
      byte[] bytes87 = AesCipherUtil.decrypt(bytes, bytes86, aesCrypto);
      decoyKeyTable741 = sha3Hash.digest(bytes87);
      byte[] bytes88 = decoyKeyTable741;
      decoyKeyTable151 = sha3Hash.digest(bytes88);
      byte[] bytes89 = new byte[]{
         106, 95, 66, -80, -14, -63, -67, -61, -128, -123, 79, 7, -46, 48, 86, 62, -25, -103, -77, 22, -104, -38, 93, 113, 52, 53, 97, 116, 87, -64, -36, -4
      };
      byte[] bytes90 = AesCipherUtil.decrypt(bytes, bytes89, aesCrypto);
      decoyKeyTable395 = sha3Hash.digest(bytes90);
      byte[] bytes91 = decoyKeyTable395;
      decoyKeyTable297 = sha3Hash.digest(bytes91);
      byte[] bytes92 = new byte[]{
         81, -71, 92, 29, 110, 11, -86, -63, 70, -37, -57, 12, -10, -101, 99, -29, 105, -102, -29, -73, 53, 31, 101, 49, 45, 27, 71, -103, 124, -115, 122, 18
      };
      byte[] bytes93 = AesCipherUtil.decrypt(bytes, bytes92, aesCrypto);
      decoyKeyTable355 = sha3Hash.digest(bytes93);
      byte[] bytes94 = decoyKeyTable355;
      decoyKeyTable536 = sha3Hash.digest(bytes94);
      byte[] bytes95 = new byte[]{
         -66, -119, 71, 94, -121, 57, 68, -118, -11, -46, 1, -79, 102, 86, -31, 113, 14, -109, -1, 44, 123, -104, 102, 87, -30, 45, -4, -71, 91, -58, -30, -116
      };
      byte[] bytes96 = AesCipherUtil.decrypt(bytes, bytes95, aesCrypto);
      decoyKeyTable781 = sha3Hash.digest(bytes96);
      byte[] bytes97 = decoyKeyTable781;
      decoyKeyTable528 = sha3Hash.digest(bytes97);
      byte[] bytes98 = new byte[]{
         114, 99, -110, -102, 41, -96, 103, -82, -26, -75, -61, -111, 24, -83, 55, 36, -97, -47, 17, 17, 2, 111, -20, -56, 85, -47, 119, 75, 70, 34, -53, 1
      };
      byte[] bytes99 = AesCipherUtil.decrypt(bytes, bytes98, aesCrypto);
      decoyKeyTable413 = sha3Hash.digest(bytes99);
      byte[] bytes100 = decoyKeyTable413;
      decoyKeyTable258 = sha3Hash.digest(bytes100);
      byte[] bytes101 = new byte[]{
         86, -14, -109, 95, 53, -74, -115, 118, 15, -57, 29, -91, -98, 120, -114, -78, 37, 121, -77, -86, 99, 16, -43, -66, 47, -54, 62, 109, 97, 105, -25, -25
      };
      byte[] bytes102 = AesCipherUtil.decrypt(bytes, bytes101, aesCrypto);
      decoyKeyTable674 = sha3Hash.digest(bytes102);
      byte[] bytes103 = decoyKeyTable674;
      decoyKeyTable214 = sha3Hash.digest(bytes103);
      byte[] bytes104 = new byte[]{
         -24, 108, 30, 105, 89, 44, 9, 96, -2, 114, -66, 121, -102, 119, -37, 23, -71, 48, 100, -77, -9, 24, 64, -29, 34, -75, 94, -66, -117, 78, 84, -21
      };
      byte[] bytes105 = AesCipherUtil.decrypt(bytes, bytes104, aesCrypto);
      decoyKeyTable041 = sha3Hash.digest(bytes105);
      byte[] bytes106 = decoyKeyTable041;
      decoyKeyTable188 = sha3Hash.digest(bytes106);
      byte[] bytes107 = new byte[]{
         -13, -28, -81, 109, 91, 42, 53, 63, 45, 88, -100, -28, 64, -52, 103, 119, 61, -42, 104, -51, -27, 92, -16, -12, 31, 95, 26, 115, 10, 44, -52, -122
      };
      byte[] bytes108 = AesCipherUtil.decrypt(bytes, bytes107, aesCrypto);
      decoyKeyTable428 = sha3Hash.digest(bytes108);
      byte[] bytes109 = decoyKeyTable428;
      decoyKeyTable533 = sha3Hash.digest(bytes109);
      byte[] bytes110 = new byte[]{
         -115, -108, 43, -65, 123, 41, -112, 67, 91, 1, 25, 58, -12, 76, 66, 79, 95, 67, -97, -105, -117, -118, -8, 4, 24, -15, -13, -118, 61, -28, 96, 64
      };
      byte[] bytes111 = AesCipherUtil.decrypt(bytes, bytes110, aesCrypto);
      decoyKeyTable786 = sha3Hash.digest(bytes111);
      byte[] bytes112 = decoyKeyTable786;
      decoyKeyTable023 = sha3Hash.digest(bytes112);
      byte[] bytes113 = new byte[]{
         23, -34, -90, 26, 66, 54, 77, -57, 46, -25, 84, -31, -4, 13, -123, -6, 99, -106, 108, 57, 45, -119, -94, -71, 70, 11, 109, 57, 27, 104, -47, 72
      };
      byte[] bytes114 = AesCipherUtil.decrypt(bytes, bytes113, aesCrypto);
      decoyKeyTable593 = sha3Hash.digest(bytes114);
      byte[] bytes115 = decoyKeyTable593;
      decoyKeyTable812 = sha3Hash.digest(bytes115);
      byte[] bytes116 = new byte[]{
         -50, -6, -13, 113, 68, 6, 92, 38, -115, 70, -13, -15, -102, 12, -84, 26, 87, 1, 15, 26, -13, -36, 62, -20, 36, 29, -48, 71, 65, -110, 42, -34
      };
      byte[] bytes117 = AesCipherUtil.decrypt(bytes, bytes116, aesCrypto);
      decoyKeyTable388 = sha3Hash.digest(bytes117);
      byte[] bytes118 = decoyKeyTable388;
      decoyKeyTable501 = sha3Hash.digest(bytes118);
      byte[] bytes119 = new byte[]{
         119, 77, -107, -114, -57, 44, 33, 85, 84, 8, 33, -22, 94, -116, -7, 103, 70, -10, 127, 96, -94, 15, 23, -128, -83, -92, -87, -14, 107, -48, -62, -79
      };
      byte[] bytes120 = AesCipherUtil.decrypt(bytes, bytes119, aesCrypto);
      decoyKeyTable712 = sha3Hash.digest(bytes120);
      byte[] bytes121 = decoyKeyTable712;
      decoyKeyTable478 = sha3Hash.digest(bytes121);
      byte[] bytes122 = new byte[]{
         16, 6, -70, 22, -80, 82, -90, 64, 120, -13, -118, -75, 107, -7, 125, -76, -96, 30, 111, 23, 124, 66, 37, 102, -67, 100, 15, -10, -72, 65, 58, 122
      };
      byte[] bytes123 = AesCipherUtil.decrypt(bytes, bytes122, aesCrypto);
      decoyKeyTable381 = sha3Hash.digest(bytes123);
      byte[] bytes124 = decoyKeyTable381;
      decoyKeyTable191 = sha3Hash.digest(bytes124);
      byte[] bytes125 = new byte[]{
         57, 73, -15, -4, 3, -16, 58, 126, 117, 12, 89, -22, -56, 114, 64, 46, -86, 41, -127, 59, 6, 54, 88, -19, -61, 40, -92, -4, -30, 101, -84, 36
      };
      byte[] bytes126 = AesCipherUtil.decrypt(bytes, bytes125, aesCrypto);
      decoyKeyTable505 = sha3Hash.digest(bytes126);
      byte[] bytes127 = decoyKeyTable505;
      decoyKeyTable679 = sha3Hash.digest(bytes127);
      byte[] bytes128 = new byte[]{
         -74, -83, 21, 103, -72, 11, 91, -90, -109, -70, 87, -89, 109, 80, 20, -56, -66, -106, 119, -26, -122, 108, 26, 53, -60, 99, 84, 70, 48, -41, 6, 29
      };
      byte[] bytes129 = AesCipherUtil.decrypt(bytes, bytes128, aesCrypto);
      decoyKeyTable038 = sha3Hash.digest(bytes129);
      byte[] bytes130 = decoyKeyTable038;
      decoyKeyTable688 = sha3Hash.digest(bytes130);
      byte[] bytes131 = new byte[]{
         53, 106, 98, 97, -63, -34, -119, -26, -63, 95, 100, 99, -54, 3, -117, 79, 119, -104, -20, 63, -25, -124, 98, 86, -3, -16, -59, -4, -73, 81, -53, -128
      };
      byte[] bytes132 = AesCipherUtil.decrypt(bytes, bytes131, aesCrypto);
      decoyKeyTable009 = sha3Hash.digest(bytes132);
      byte[] bytes133 = decoyKeyTable009;
      decoyKeyTable726 = sha3Hash.digest(bytes133);
      byte[] bytes134 = new byte[]{
         90, 127, -19, 5, -4, 0, -59, -45, -104, -73, 81, 45, 92, -108, 7, -111, 40, 53, -18, 10, 46, -86, 1, 56, -17, -52, -7, 124, 9, 12, 69, -118
      };
      byte[] bytes135 = AesCipherUtil.decrypt(bytes, bytes134, aesCrypto);
      decoyKeyTable626 = sha3Hash.digest(bytes135);
      byte[] bytes136 = decoyKeyTable626;
      decoyKeyTable177 = sha3Hash.digest(bytes136);
      byte[] bytes137 = new byte[]{
         98, -36, -117, 113, 124, 39, 79, 85, 115, 124, -127, -15, -27, -58, 59, 99, 10, -110, 5, 43, -74, 46, -40, -83, -2, 51, -53, 80, 114, -18, 82, 65
      };
      byte[] bytes138 = AesCipherUtil.decrypt(bytes, bytes137, aesCrypto);
      decoyKeyTable142 = sha3Hash.digest(bytes138);
      byte[] bytes139 = decoyKeyTable142;
      decoyKeyTable848 = sha3Hash.digest(bytes139);
      byte[] bytes140 = new byte[]{
         77, 82, -18, -116, 80, -93, -99, 105, 80, -77, 84, 83, -39, -3, 61, 35, 100, -112, 47, 105, -23, 78, -42, 57, -54, -42, -122, -126, 117, 111, 24, 116
      };
      byte[] bytes141 = AesCipherUtil.decrypt(bytes, bytes140, aesCrypto);
      decoyKeyTable118 = sha3Hash.digest(bytes141);
      byte[] bytes142 = decoyKeyTable118;
      decoyKeyTable316 = sha3Hash.digest(bytes142);
      byte[] bytes143 = new byte[]{
         -108, -109, -26, 45, 47, 10, -96, -59, 57, -48, 116, -124, 127, 13, -78, 51, -66, -17, 92, -95, -91, 70, 55, -4, -74, 77, 59, 84, 60, 69, 78, -64
      };
      byte[] bytes144 = AesCipherUtil.decrypt(bytes, bytes143, aesCrypto);
      decoyKeyTable320 = sha3Hash.digest(bytes144);
      byte[] bytes145 = decoyKeyTable320;
      decoyKeyTable821 = sha3Hash.digest(bytes145);
      byte[] bytes146 = new byte[]{
         -59, -110, 62, 119, 90, -107, 80, -67, -120, 58, -46, 55, 1, 13, -35, 121, -77, -59, 68, -48, 45, -108, -97, -48, 65, -28, 119, 59, -29, -58, 9, -47
      };
      byte[] bytes147 = AesCipherUtil.decrypt(bytes, bytes146, aesCrypto);
      decoyKeyTable746 = sha3Hash.digest(bytes147);
      byte[] bytes148 = decoyKeyTable746;
      decoyKeyTable352 = sha3Hash.digest(bytes148);
      byte[] bytes149 = new byte[]{
         98, 102, 9, -66, -111, -108, -22, 3, 111, 56, 81, -9, -125, -52, -57, 58, -120, -7, -91, -127, -120, -24, -124, 105, -74, -23, 80, 98, 39, -42, 15, 97
      };
      byte[] bytes150 = AesCipherUtil.decrypt(bytes, bytes149, aesCrypto);
      decoyKeyTable042 = sha3Hash.digest(bytes150);
      byte[] bytes151 = decoyKeyTable042;
      decoyKeyTable759 = sha3Hash.digest(bytes151);
      byte[] bytes152 = new byte[]{
         -7, -85, 95, -81, -15, -123, 3, 52, 33, 4, 66, -16, 73, 30, -62, -114, 74, 97, -1, 101, 114, 55, -58, 17, 21, 63, 108, 91, -47, -19, 21, 2
      };
      byte[] bytes153 = AesCipherUtil.decrypt(bytes, bytes152, aesCrypto);
      decoyKeyTable186 = sha3Hash.digest(bytes153);
      byte[] bytes154 = decoyKeyTable186;
      decoyKeyTable548 = sha3Hash.digest(bytes154);
      byte[] bytes155 = new byte[]{
         70, 91, 23, 33, -6, -34, -123, 12, 98, 49, -10, -93, 101, 70, 28, -37, 108, 84, 67, 10, -45, 49, -54, -35, 71, 34, -28, 127, 93, 1, -29, 21
      };
      byte[] bytes156 = AesCipherUtil.decrypt(bytes, bytes155, aesCrypto);
      decoyKeyTable575 = sha3Hash.digest(bytes156);
      byte[] bytes157 = decoyKeyTable575;
      decoyKeyTable552 = sha3Hash.digest(bytes157);
      byte[] bytes158 = new byte[]{
         -3, -84, 76, -53, -120, 6, -117, -102, 55, -111, 78, -7, -21, -123, -40, -21, -16, 23, 91, 10, -7, 73, -72, -18, 66, 100, 38, 19, 103, 85, 79, -92
      };
      byte[] bytes159 = AesCipherUtil.decrypt(bytes, bytes158, aesCrypto);
      decoyKeyTable632 = sha3Hash.digest(bytes159);
      byte[] bytes160 = decoyKeyTable632;
      decoyKeyTable136 = sha3Hash.digest(bytes160);
      byte[] bytes161 = new byte[]{
         121, 72, -78, -3, -105, 24, -61, 86, -54, 80, -104, -93, 72, -30, -30, -1, 35, 21, 11, 64, 41, 88, -85, -59, 49, -91, 13, -71, 1, -111, -81, 72
      };
      byte[] bytes162 = AesCipherUtil.decrypt(bytes, bytes161, aesCrypto);
      decoyKeyTable368 = sha3Hash.digest(bytes162);
      byte[] bytes163 = decoyKeyTable368;
      decoyKeyTable091 = sha3Hash.digest(bytes163);
   }

   public static void setSessionAuthToken(byte[] bytes) {
      AesCrypto aesCrypto = new AesCrypto();
      Sha3Hash sha3Hash = new Sha3Hash(Sha3Type.SHA3_256);
      populateDecoyTokensGroupA(
         new byte[]{
            -56,
            -118,
            -21,
            94,
            116,
            38,
            102,
            84,
            126,
            74,
            99,
            -43,
            105,
            -20,
            -45,
            -33,
            76,
            58,
            -105,
            86,
            -24,
            -64,
            29,
            -84,
            -108,
            122,
            66,
            88,
            4,
            -120,
            -128,
            -73
         }
      );
      sessionAuthToken = bytes;
      tokenScatterSlot12 = sha3Hash.digest(bytes);
      ClientArchiveHolder.getControlFlowMarker();
      byte[] bytes2 = new byte[]{
         -56, -118, -21, 94, 116, 38, 102, 84, 126, 74, 99, -43, 105, -20, -45, -33, 76, 58, -105, 86, -24, -64, 29, -84, -108, 122, 66, 88, 4, -120, -128, -73
      };
      byte[] bytes3 = AesCipherUtil.decrypt(bytes, bytes2, aesCrypto);
      tokenScatterSlot03 = sha3Hash.digest(bytes3);
      byte[] bytes4 = tokenScatterSlot03;
      tokenScatterSlot00 = sha3Hash.digest(bytes4);
      byte[] bytes5 = new byte[]{
         81, -58, -2, -28, 29, -121, -26, -37, 71, 111, 100, 90, 65, -79, -9, 19, -16, 9, 51, -18, -71, -70, -89, -21, 98, -103, 57, 53, 10, 59, -73, -119
      };
      byte[] bytes6 = AesCipherUtil.decrypt(bytes, bytes5, aesCrypto);
      tokenScatterSlot01 = sha3Hash.digest(bytes6);
      byte[] bytes7 = tokenScatterSlot01;
      tokenScatterSlot13 = sha3Hash.digest(bytes7);
      byte[] bytes8 = new byte[]{
         -74, -77, -27, 42, -89, 69, 41, 127, -95, 3, 63, -127, -20, -75, 66, 92, -3, 34, 57, -90, 124, -102, 115, -81, -63, 106, -43, 87, 35, -6, 94, 50
      };
      byte[] bytes9 = AesCipherUtil.decrypt(bytes, bytes8, aesCrypto);
      tokenScatterSlot08 = sha3Hash.digest(bytes9);
      byte[] bytes10 = tokenScatterSlot08;
      tokenScatterSlot06 = sha3Hash.digest(bytes10);
      byte[] bytes11 = new byte[]{
         92, -98, -12, 126, 124, -63, -117, -50, 50, 61, -47, -12, 19, -36, 73, 39, -100, -118, -112, -21, -69, 47, -95, -77, -18, 31, -55, -19, 39, -36, 0, 15
      };
      byte[] bytes12 = AesCipherUtil.decrypt(bytes, bytes11, aesCrypto);
      tokenScatterSlot09 = sha3Hash.digest(bytes12);
      byte[] bytes13 = tokenScatterSlot09;
      tokenScatterSlot02 = sha3Hash.digest(bytes13);
      byte[] bytes14 = new byte[]{
         -74,
         -123,
         -46,
         -20,
         -55,
         -93,
         -59,
         92,
         62,
         -39,
         82,
         -63,
         32,
         -53,
         -33,
         26,
         118,
         55,
         -13,
         91,
         -97,
         -29,
         -112,
         -55,
         -1,
         -53,
         -103,
         32,
         -76,
         88,
         -115,
         -76
      };
      byte[] bytes15 = AesCipherUtil.decrypt(bytes, bytes14, aesCrypto);
      tokenScatterSlot07 = sha3Hash.digest(bytes15);
      byte[] bytes16 = tokenScatterSlot07;
      tokenScatterSlot11 = sha3Hash.digest(bytes16);
      byte[] bytes17 = new byte[]{
         89, 126, -96, -57, -15, 70, -84, -21, 40, -30, 120, -126, 36, -23, 88, -45, 94, -47, 45, 104, 89, -46, -95, -25, 73, 61, -25, 62, 59, -37, -72, 70
      };
      byte[] bytes18 = AesCipherUtil.decrypt(bytes, bytes17, aesCrypto);
      tokenScatterSlot10 = sha3Hash.digest(bytes18);
      byte[] bytes19 = tokenScatterSlot10;
      tokenScatterSlot04 = sha3Hash.digest(bytes19);
      byte[] bytes20 = new byte[]{
         116, -92, -73, 36, -31, -57, 56, -116, 93, 14, 115, 41, -111, -64, 25, -76, -111, 127, 5, -3, -95, -95, 101, 36, 58, 71, 30, 61, 82, 18, 67, -86
      };
      byte[] bytes21 = AesCipherUtil.decrypt(bytes, bytes20, aesCrypto);
      tokenScatterSlot05 = sha3Hash.digest(bytes21);
      byte[] bytes22 = tokenScatterSlot05;
      decoyKeyTable367 = sha3Hash.digest(bytes22);
      byte[] bytes23 = new byte[]{
         66, 90, 106, 84, 14, -4, -60, -92, 63, -64, 44, 13, 96, 60, -107, -87, -6, -119, 77, 102, -25, -50, 15, -105, -76, -4, -75, 59, -103, 77, 112, -75
      };
      byte[] bytes24 = AesCipherUtil.decrypt(bytes, bytes23, aesCrypto);
      decoyKeyTable585 = sha3Hash.digest(bytes24);
      byte[] bytes25 = decoyKeyTable585;
      decoyKeyTable102 = sha3Hash.digest(bytes25);
      byte[] bytes26 = new byte[]{
         -90, -80, -15, -5, -115, -94, -57, 50, 10, -12, 114, -103, 92, 19, 32, 115, 52, -47, 55, 26, -30, -116, 21, -26, 108, 87, 87, 85, 85, 14, 124, 53
      };
      byte[] bytes27 = AesCipherUtil.decrypt(bytes, bytes26, aesCrypto);
      decoyKeyTable465 = sha3Hash.digest(bytes27);
      byte[] bytes28 = decoyKeyTable465;
      decoyKeyTable246 = sha3Hash.digest(bytes28);
      byte[] bytes29 = new byte[]{
         -55, -5, -111, 53, -26, 1, -38, -124, -65, -102, 54, 57, -79, -55, -37, -41, -7, 69, 30, 111, 40, 86, 122, -93, 59, 96, -106, 52, -76, 38, 5, 24
      };
      byte[] bytes30 = AesCipherUtil.decrypt(bytes, bytes29, aesCrypto);
      decoyKeyTable222 = sha3Hash.digest(bytes30);
      byte[] bytes31 = decoyKeyTable222;
      decoyKeyTable760 = sha3Hash.digest(bytes31);
      byte[] bytes32 = new byte[]{
         61, 60, -61, -83, 74, -113, 7, 111, 65, 31, -59, -92, -97, 47, -120, -78, -116, -47, -126, -79, 80, -29, 95, 20, 42, -70, 103, -93, 34, -44, 121, 82
      };
      byte[] bytes33 = AesCipherUtil.decrypt(bytes, bytes32, aesCrypto);
      decoyKeyTable026 = sha3Hash.digest(bytes33);
      byte[] bytes34 = decoyKeyTable026;
      decoyKeyTable216 = sha3Hash.digest(bytes34);
      byte[] bytes35 = new byte[]{
         117, 34, 101, -20, 56, 92, 68, 18, 14, 19, 34, -77, -25, -53, -128, -67, -81, 6, 75, -84, 64, -79, 38, -36, 111, -24, 69, 6, 33, -98, -66, -111
      };
      byte[] bytes36 = AesCipherUtil.decrypt(bytes, bytes35, aesCrypto);
      decoyKeyTable645 = sha3Hash.digest(bytes36);
      byte[] bytes37 = decoyKeyTable645;
      decoyKeyTable220 = sha3Hash.digest(bytes37);
      byte[] bytes38 = new byte[]{
         -72, -25, 45, -126, -124, -87, -44, -53, 37, 91, 116, -56, 24, 8, -3, -76, 72, -125, 24, 106, 66, -107, -114, 100, 35, 18, 62, 78, -63, -71, -66, -115
      };
      byte[] bytes39 = AesCipherUtil.decrypt(bytes, bytes38, aesCrypto);
      decoyKeyTable064 = sha3Hash.digest(bytes39);
      byte[] bytes40 = decoyKeyTable064;
      decoyKeyTable700 = sha3Hash.digest(bytes40);
      byte[] bytes41 = new byte[]{
         -36, -43, 61, -7, -5, -69, 52, 78, 57, 12, -51, 120, -60, 27, 113, -2, 58, -46, 18, 91, -127, 2, 98, -53, 110, -50, -86, -73, -45, -8, -90, -108
      };
      byte[] bytes42 = AesCipherUtil.decrypt(bytes, bytes41, aesCrypto);
      decoyKeyTable613 = sha3Hash.digest(bytes42);
      byte[] bytes43 = decoyKeyTable613;
      decoyKeyTable285 = sha3Hash.digest(bytes43);
      byte[] bytes44 = new byte[]{
         -54, -26, -44, 4, 53, -76, 40, -78, -56, 39, 19, 55, -70, 42, -39, -10, -54, 90, -128, -37, -36, -45, -89, -62, -88, -65, 61, -119, -27, -94, -19, 71
      };
      byte[] bytes45 = AesCipherUtil.decrypt(bytes, bytes44, aesCrypto);
      decoyKeyTable446 = sha3Hash.digest(bytes45);
      byte[] bytes46 = decoyKeyTable446;
      decoyKeyTable423 = sha3Hash.digest(bytes46);
      byte[] bytes47 = new byte[]{
         74,
         -57,
         55,
         -37,
         -101,
         20,
         -105,
         127,
         -58,
         -65,
         114,
         90,
         -105,
         -111,
         104,
         89,
         -48,
         -80,
         -87,
         -63,
         71,
         50,
         99,
         56,
         109,
         73,
         -67,
         -3,
         94,
         -104,
         -30,
         -82
      };
      byte[] bytes48 = AesCipherUtil.decrypt(bytes, bytes47, aesCrypto);
      decoyKeyTable583 = sha3Hash.digest(bytes48);
      byte[] bytes49 = decoyKeyTable583;
      decoyKeyTable704 = sha3Hash.digest(bytes49);
      byte[] bytes50 = new byte[]{
         -100,
         99,
         123,
         88,
         -3,
         -110,
         -116,
         -59,
         -44,
         44,
         -39,
         46,
         -105,
         -21,
         -69,
         -90,
         -126,
         -8,
         -6,
         81,
         -64,
         99,
         -120,
         -87,
         63,
         110,
         4,
         -123,
         -107,
         112,
         7,
         -45
      };
      byte[] bytes51 = AesCipherUtil.decrypt(bytes, bytes50, aesCrypto);
      decoyKeyTable622 = sha3Hash.digest(bytes51);
      byte[] bytes52 = decoyKeyTable622;
      decoyKeyTable681 = sha3Hash.digest(bytes52);
      byte[] bytes53 = new byte[]{
         49, 34, -62, -8, 33, -58, -22, -26, -73, 64, 121, 82, 80, 117, 27, 90, 68, 118, 119, 40, -61, 46, -18, 127, -89, 116, -117, -11, -2, 14, 20, 84
      };
      byte[] bytes54 = AesCipherUtil.decrypt(bytes, bytes53, aesCrypto);
      decoyKeyTable627 = sha3Hash.digest(bytes54);
      byte[] bytes55 = decoyKeyTable627;
      decoyKeyTable292 = sha3Hash.digest(bytes55);
      byte[] bytes56 = new byte[]{
         -46, 104, 6, -11, 95, -109, -125, 111, -40, 122, -64, 70, 75, -64, -12, -58, 1, -66, 115, 13, 20, -19, 75, -128, -111, -118, 118, 20, 44, 74, -77, 7
      };
      byte[] bytes57 = AesCipherUtil.decrypt(bytes, bytes56, aesCrypto);
      decoyKeyTable770 = sha3Hash.digest(bytes57);
      byte[] bytes58 = decoyKeyTable770;
      decoyKeyTable823 = sha3Hash.digest(bytes58);
      byte[] bytes59 = new byte[]{
         11, -51, 3, -94, 99, 53, 33, 27, -9, -112, 3, -82, -6, -37, 95, -13, 15, 35, 32, -65, -121, -5, -14, 22, -22, 64, 53, 9, 39, -85, -21, 70
      };
      byte[] bytes60 = AesCipherUtil.decrypt(bytes, bytes59, aesCrypto);
      decoyKeyTable065 = sha3Hash.digest(bytes60);
      byte[] bytes61 = decoyKeyTable065;
      decoyKeyTable060 = sha3Hash.digest(bytes61);
      byte[] bytes62 = new byte[]{
         -114, 57, 30, -63, -85, -118, 83, 118, 114, 33, -29, -2, 53, 90, -57, 26, -49, -117, -19, -26, 19, -84, 43, 43, -3, 51, 29, 45, 37, 43, -123, -112
      };
      byte[] bytes63 = AesCipherUtil.decrypt(bytes, bytes62, aesCrypto);
      decoyKeyTable586 = sha3Hash.digest(bytes63);
      byte[] bytes64 = decoyKeyTable586;
      decoyKeyTable228 = sha3Hash.digest(bytes64);
      byte[] bytes65 = new byte[]{
         -12, 71, 48, 63, 94, -2, -5, -56, 47, 7, 21, -86, -114, -33, -22, -116, 56, 78, 12, 111, 117, 0, 7, 38, -128, -65, 50, -50, 94, 74, -125, 14
      };
      byte[] bytes66 = AesCipherUtil.decrypt(bytes, bytes65, aesCrypto);
      decoyKeyTable531 = sha3Hash.digest(bytes66);
      byte[] bytes67 = decoyKeyTable531;
      decoyKeyTable318 = sha3Hash.digest(bytes67);
      byte[] bytes68 = new byte[]{
         -1, -13, 47, 120, -9, 43, -37, -31, -41, -60, -33, -73, -126, 112, 83, -30, 87, 43, 109, -60, 106, 95, 3, -55, -100, 70, -54, 54, 13, -37, 83, -114
      };
      byte[] bytes69 = AesCipherUtil.decrypt(bytes, bytes68, aesCrypto);
      decoyKeyTable308 = sha3Hash.digest(bytes69);
      byte[] bytes70 = decoyKeyTable308;
      decoyKeyTable056 = sha3Hash.digest(bytes70);
      byte[] bytes71 = new byte[]{
         -73,
         109,
         -59,
         -119,
         -127,
         -12,
         96,
         -93,
         82,
         40,
         -65,
         -32,
         -93,
         75,
         121,
         126,
         -128,
         -80,
         58,
         94,
         -122,
         50,
         -69,
         52,
         19,
         37,
         114,
         -10,
         58,
         -96,
         -39,
         -93
      };
      byte[] bytes72 = AesCipherUtil.decrypt(bytes, bytes71, aesCrypto);
      decoyKeyTable252 = sha3Hash.digest(bytes72);
      byte[] bytes73 = decoyKeyTable252;
      decoyKeyTable005 = sha3Hash.digest(bytes73);
      byte[] bytes74 = new byte[]{
         -99, -61, 92, -61, -70, -63, -22, 106, 58, 34, 94, -96, 81, 32, 26, 44, -43, -47, 13, 94, 111, -43, 71, 22, -97, -109, -20, -66, 42, 49, -57, -11
      };
      byte[] bytes75 = AesCipherUtil.decrypt(bytes, bytes74, aesCrypto);
      decoyKeyTable683 = sha3Hash.digest(bytes75);
      byte[] bytes76 = decoyKeyTable683;
      decoyKeyTable619 = sha3Hash.digest(bytes76);
      byte[] bytes77 = new byte[]{
         25, -116, 16, -94, -104, 25, 93, 91, 76, -110, -123, 25, 78, 54, -107, 26, 13, 65, -94, 114, 20, 113, 48, 116, -88, 94, 54, -102, 15, 61, -49, -68
      };
      byte[] bytes78 = AesCipherUtil.decrypt(bytes, bytes77, aesCrypto);
      decoyKeyTable233 = sha3Hash.digest(bytes78);
      byte[] bytes79 = decoyKeyTable233;
      decoyKeyTable089 = sha3Hash.digest(bytes79);
      byte[] bytes80 = new byte[]{
         -45,
         11,
         -35,
         34,
         118,
         61,
         -29,
         -68,
         60,
         70,
         108,
         109,
         -125,
         110,
         -102,
         68,
         124,
         -26,
         -103,
         27,
         -109,
         -26,
         -66,
         4,
         -70,
         126,
         97,
         -63,
         -127,
         -125,
         113,
         28
      };
      byte[] bytes81 = AesCipherUtil.decrypt(bytes, bytes80, aesCrypto);
      decoyKeyTable713 = sha3Hash.digest(bytes81);
      byte[] bytes82 = decoyKeyTable713;
      decoyKeyTable097 = sha3Hash.digest(bytes82);
      byte[] bytes83 = new byte[]{
         35, -123, -72, 36, 112, 106, 85, 96, 123, -67, -5, 95, 47, -84, 20, 112, -118, 96, -12, 120, -47, 108, 91, -23, -37, -27, 70, -109, -36, 27, -18, 45
      };
      byte[] bytes84 = AesCipherUtil.decrypt(bytes, bytes83, aesCrypto);
      decoyKeyTable589 = sha3Hash.digest(bytes84);
      byte[] bytes85 = decoyKeyTable589;
      decoyKeyTable115 = sha3Hash.digest(bytes85);
      byte[] bytes86 = new byte[]{
         -53, 103, -24, 24, -88, 17, 45, 69, -47, -101, 87, -12, -74, -124, 89, 59, 84, 126, -102, 82, 11, -104, 66, 91, 15, -53, 3, 126, -65, -122, -19, 79
      };
      byte[] bytes87 = AesCipherUtil.decrypt(bytes, bytes86, aesCrypto);
      decoyKeyTable340 = sha3Hash.digest(bytes87);
      byte[] bytes88 = decoyKeyTable340;
      decoyKeyTable807 = sha3Hash.digest(bytes88);
      byte[] bytes89 = new byte[]{
         -79,
         -121,
         -69,
         93,
         -119,
         52,
         42,
         -112,
         99,
         -95,
         51,
         -15,
         -103,
         -73,
         82,
         -69,
         -124,
         80,
         -94,
         -57,
         -11,
         -62,
         116,
         -74,
         -54,
         -87,
         -98,
         -11,
         116,
         108,
         -35,
         -99
      };
      byte[] bytes90 = AesCipherUtil.decrypt(bytes, bytes89, aesCrypto);
      decoyKeyTable211 = sha3Hash.digest(bytes90);
      byte[] bytes91 = decoyKeyTable211;
      decoyKeyTable094 = sha3Hash.digest(bytes91);
      byte[] bytes92 = new byte[]{
         -57, -93, -46, 115, -52, -32, -7, 4, 119, -62, 101, 127, -41, 62, -71, -80, 125, -48, -50, 94, 44, 101, -44, 118, -61, 19, 48, -78, -71, -94, -70, 54
      };
      byte[] bytes93 = AesCipherUtil.decrypt(bytes, bytes92, aesCrypto);
      decoyKeyTable420 = sha3Hash.digest(bytes93);
      byte[] bytes94 = decoyKeyTable420;
      decoyKeyTable811 = sha3Hash.digest(bytes94);
      byte[] bytes95 = new byte[]{
         118,
         -112,
         -73,
         124,
         -5,
         -127,
         102,
         13,
         65,
         27,
         -113,
         90,
         17,
         60,
         -44,
         -117,
         45,
         -120,
         17,
         -94,
         -46,
         65,
         48,
         -20,
         -16,
         -77,
         -16,
         -106,
         112,
         40,
         -42,
         107
      };
      byte[] bytes96 = AesCipherUtil.decrypt(bytes, bytes95, aesCrypto);
      decoyKeyTable517 = sha3Hash.digest(bytes96);
      byte[] bytes97 = decoyKeyTable517;
      decoyKeyTable565 = sha3Hash.digest(bytes97);
      byte[] bytes98 = new byte[]{
         13, 88, -91, 107, 100, -7, 95, -32, 13, 84, 93, 4, 30, 33, -100, -27, -57, 56, -107, 17, 78, -112, 45, 93, 14, -35, 49, 49, -3, -91, 49, -8
      };
      byte[] bytes99 = AesCipherUtil.decrypt(bytes, bytes98, aesCrypto);
      decoyKeyTable694 = sha3Hash.digest(bytes99);
      byte[] bytes100 = decoyKeyTable694;
      decoyKeyTable442 = sha3Hash.digest(bytes100);
      byte[] bytes101 = new byte[]{
         68, -121, 74, 8, 20, -22, 108, -114, -51, 22, -107, -6, -8, -74, -66, 124, -62, -67, 54, 15, -86, -41, -108, 70, 112, 92, 71, -16, 93, 79, 49, -56
      };
      byte[] bytes102 = AesCipherUtil.decrypt(bytes, bytes101, aesCrypto);
      decoyKeyTable514 = sha3Hash.digest(bytes102);
      byte[] bytes103 = decoyKeyTable514;
      decoyKeyTable412 = sha3Hash.digest(bytes103);
      byte[] bytes104 = new byte[]{
         69, -90, -92, -64, 89, 51, 93, 52, 26, 17, 32, 116, -94, -66, 0, -69, 41, 63, 34, 35, -6, -69, 7, 76, -22, -83, -14, 79, 2, -48, 62, 46
      };
      byte[] bytes105 = AesCipherUtil.decrypt(bytes, bytes104, aesCrypto);
      decoyKeyTable769 = sha3Hash.digest(bytes105);
      byte[] bytes106 = decoyKeyTable769;
      decoyKeyTable607 = sha3Hash.digest(bytes106);
      byte[] bytes107 = new byte[]{
         -3,
         -91,
         -84,
         16,
         -114,
         -113,
         -55,
         -96,
         -99,
         -37,
         -124,
         -71,
         -117,
         -53,
         117,
         96,
         127,
         3,
         -111,
         8,
         -97,
         -25,
         105,
         -91,
         -17,
         101,
         -33,
         -20,
         -78,
         111,
         106,
         67
      };
      byte[] bytes108 = AesCipherUtil.decrypt(bytes, bytes107, aesCrypto);
      decoyKeyTable403 = sha3Hash.digest(bytes108);
      byte[] bytes109 = decoyKeyTable403;
      decoyKeyTable631 = sha3Hash.digest(bytes109);
      byte[] bytes110 = new byte[]{
         -45, 68, 87, 95, -123, -91, 127, 65, -87, -65, -114, 77, 64, -112, -41, 48, 60, 68, 94, -118, 126, 88, 5, 71, 43, 64, 7, 28, -18, -42, 19, 29
      };
      byte[] bytes111 = AesCipherUtil.decrypt(bytes, bytes110, aesCrypto);
      decoyKeyTable226 = sha3Hash.digest(bytes111);
      byte[] bytes112 = decoyKeyTable226;
      decoyKeyTable036 = sha3Hash.digest(bytes112);
      byte[] bytes113 = new byte[]{
         115,
         -91,
         21,
         -79,
         77,
         -104,
         -41,
         -74,
         -87,
         -118,
         18,
         -21,
         45,
         70,
         19,
         -28,
         -110,
         -95,
         -77,
         14,
         -16,
         11,
         -114,
         -35,
         98,
         -82,
         -81,
         -7,
         -34,
         -79,
         121,
         58
      };
      byte[] bytes114 = AesCipherUtil.decrypt(bytes, bytes113, aesCrypto);
      decoyKeyTable757 = sha3Hash.digest(bytes114);
      byte[] bytes115 = decoyKeyTable757;
      decoyKeyTable566 = sha3Hash.digest(bytes115);
      byte[] bytes116 = new byte[]{
         -63, 33, 58, -47, 108, 73, 1, 87, 16, 72, 9, 9, -32, 110, 8, 47, 87, -25, -43, -116, -79, 72, -15, -83, -127, -9, 60, 6, 5, -123, 23, -35
      };
      byte[] bytes117 = AesCipherUtil.decrypt(bytes, bytes116, aesCrypto);
      decoyKeyTable809 = sha3Hash.digest(bytes117);
      byte[] bytes118 = decoyKeyTable809;
      decoyKeyTable470 = sha3Hash.digest(bytes118);
      byte[] bytes119 = new byte[]{
         49, 81, -124, -106, -92, 113, -80, -47, 35, -54, 50, 106, -122, -32, 22, 100, 70, 117, 118, 6, 122, 111, 47, 118, -55, 58, -28, -7, 42, 22, 79, -65
      };
      byte[] bytes120 = AesCipherUtil.decrypt(bytes, bytes119, aesCrypto);
      decoyKeyTable453 = sha3Hash.digest(bytes120);
      byte[] bytes121 = decoyKeyTable453;
      decoyKeyTable293 = sha3Hash.digest(bytes121);
      byte[] bytes122 = new byte[]{
         34, 86, -42, 45, 2, 7, 65, -79, 12, 23, -107, 47, -20, 2, -29, 87, -16, 66, -37, 2, 17, -20, -86, -89, -123, -65, -3, 125, 5, 26, -118, -96
      };
      byte[] bytes123 = AesCipherUtil.decrypt(bytes, bytes122, aesCrypto);
      decoyKeyTable122 = sha3Hash.digest(bytes123);
      byte[] bytes124 = decoyKeyTable122;
      decoyKeyTable573 = sha3Hash.digest(bytes124);
      byte[] bytes125 = new byte[]{
         49, 116, -76, -91, -14, -119, 70, -80, 78, -100, 116, -4, 94, -92, -46, 4, 67, -96, 84, 30, -119, 15, -105, -69, -49, 44, -80, 120, -23, 1, 102, -76
      };
      byte[] bytes126 = AesCipherUtil.decrypt(bytes, bytes125, aesCrypto);
      decoyKeyTable847 = sha3Hash.digest(bytes126);
      byte[] bytes127 = decoyKeyTable847;
      decoyKeyTable377 = sha3Hash.digest(bytes127);
      byte[] bytes128 = new byte[]{
         -111, 35, -58, -19, 0, -19, -38, -5, -1, 64, -111, -119, -109, 115, 7, 20, 67, 93, 117, 81, 121, 15, -64, -99, 21, -8, 42, 123, -84, 69, 102, -59
      };
      byte[] bytes129 = AesCipherUtil.decrypt(bytes, bytes128, aesCrypto);
      decoyKeyTable599 = sha3Hash.digest(bytes129);
      byte[] bytes130 = decoyKeyTable599;
      decoyKeyTable838 = sha3Hash.digest(bytes130);
      byte[] bytes131 = new byte[]{
         40,
         -87,
         -77,
         -117,
         78,
         -83,
         0,
         -54,
         102,
         -124,
         107,
         -54,
         -77,
         -23,
         -111,
         -68,
         118,
         93,
         8,
         103,
         -29,
         -98,
         -100,
         -39,
         28,
         -48,
         -121,
         21,
         -85,
         127,
         -71,
         -30
      };
      byte[] bytes132 = AesCipherUtil.decrypt(bytes, bytes131, aesCrypto);
      decoyKeyTable787 = sha3Hash.digest(bytes132);
      byte[] bytes133 = decoyKeyTable787;
      decoyKeyTable073 = sha3Hash.digest(bytes133);
      byte[] bytes134 = new byte[]{
         32, -124, -89, -75, 95, -78, 33, -95, -74, 63, -52, -2, 84, -104, 10, 52, -114, -95, 5, -36, -58, -2, -110, -27, -90, 81, -13, -103, -128, -4, -80, 97
      };
      byte[] bytes135 = AesCipherUtil.decrypt(bytes, bytes134, aesCrypto);
      decoyKeyTable445 = sha3Hash.digest(bytes135);
      byte[] bytes136 = decoyKeyTable445;
      decoyKeyTable144 = sha3Hash.digest(bytes136);
      byte[] bytes137 = new byte[]{
         44, 108, -73, -126, 44, -78, 25, 25, 122, -56, 94, -69, -11, -82, 81, -9, 14, 51, 41, 53, -86, -118, 111, 7, -70, -25, -87, 66, 36, 109, -17, 77
      };
      byte[] bytes138 = AesCipherUtil.decrypt(bytes, bytes137, aesCrypto);
      decoyKeyTable373 = sha3Hash.digest(bytes138);
      byte[] bytes139 = decoyKeyTable373;
      decoyKeyTable737 = sha3Hash.digest(bytes139);
      byte[] bytes140 = new byte[]{
         -113, -25, -43, 74, 17, 24, 103, -77, -65, 48, 38, -10, -103, 28, 46, 48, 93, -24, -88, 100, 55, -52, 37, -82, 5, -37, 49, -19, 89, 67, -82, 58
      };
      byte[] bytes141 = AesCipherUtil.decrypt(bytes, bytes140, aesCrypto);
      decoyKeyTable197 = sha3Hash.digest(bytes141);
      byte[] bytes142 = decoyKeyTable197;
      decoyKeyTable559 = sha3Hash.digest(bytes142);
      byte[] bytes143 = new byte[]{
         -121, -47, 114, -95, -17, -9, 61, -110, -75, -98, 64, -128, 24, 56, -43, 68, -119, 12, 113, -70, 16, -92, -35, 36, -86, 10, 62, 67, 48, 24, -34, 12
      };
      byte[] bytes144 = AesCipherUtil.decrypt(bytes, bytes143, aesCrypto);
      decoyKeyTable112 = sha3Hash.digest(bytes144);
      byte[] bytes145 = decoyKeyTable112;
      decoyKeyTable224 = sha3Hash.digest(bytes145);
      byte[] bytes146 = new byte[]{
         -107, 84, -49, -78, 48, 127, 62, 78, -44, -127, -40, 1, 80, -34, -51, -22, 94, -48, -106, 22, -4, 125, -102, 91, -13, 56, 82, -123, 50, -128, 84, -98
      };
      byte[] bytes147 = AesCipherUtil.decrypt(bytes, bytes146, aesCrypto);
      decoyKeyTable777 = sha3Hash.digest(bytes147);
      byte[] bytes148 = decoyKeyTable777;
      decoyKeyTable794 = sha3Hash.digest(bytes148);
      byte[] bytes149 = new byte[]{
         -103, 36, 23, -68, 47, 21, -83, -113, 44, 57, -124, 99, 18, 93, -40, -51, 27, 108, -36, 62, -79, 76, -91, -114, -19, -23, -56, 78, 13, 17, -123, 115
      };
      byte[] bytes150 = AesCipherUtil.decrypt(bytes, bytes149, aesCrypto);
      decoyKeyTable401 = sha3Hash.digest(bytes150);
      byte[] bytes151 = decoyKeyTable401;
      decoyKeyTable600 = sha3Hash.digest(bytes151);
      byte[] bytes152 = new byte[]{
         -57, -59, 116, 78, 83, 119, -54, 76, -111, -24, -107, -117, 58, -46, 114, 47, 116, 5, -83, 104, 22, -119, 100, 28, 107, 45, 78, 107, -27, 26, 85, 85
      };
      byte[] bytes153 = AesCipherUtil.decrypt(bytes, bytes152, aesCrypto);
      decoyKeyTable098 = sha3Hash.digest(bytes153);
      byte[] bytes154 = decoyKeyTable098;
      decoyKeyTable538 = sha3Hash.digest(bytes154);
      byte[] bytes155 = new byte[]{
         51, 18, 80, -114, -42, -40, 29, -120, -118, 50, -54, 113, 37, 82, 101, -127, -23, 59, 82, 35, -23, 19, 17, 29, -39, 107, -79, -22, -25, 34, -118, -121
      };
      byte[] bytes156 = AesCipherUtil.decrypt(bytes, bytes155, aesCrypto);
      decoyKeyTable033 = sha3Hash.digest(bytes156);
      byte[] bytes157 = decoyKeyTable033;
      decoyKeyTable164 = sha3Hash.digest(bytes157);
      byte[] bytes158 = new byte[]{
         -44,
         -73,
         124,
         -26,
         123,
         125,
         -111,
         101,
         33,
         -109,
         124,
         -18,
         -36,
         -51,
         9,
         110,
         -119,
         -26,
         40,
         -99,
         -35,
         77,
         -48,
         -3,
         -96,
         -88,
         -89,
         -64,
         -53,
         -108,
         -110,
         39
      };
      byte[] bytes159 = AesCipherUtil.decrypt(bytes, bytes158, aesCrypto);
      decoyKeyTable148 = sha3Hash.digest(bytes159);
      byte[] bytes160 = decoyKeyTable148;
      decoyKeyTable304 = sha3Hash.digest(bytes160);
      byte[] bytes161 = new byte[]{
         25, 49, -69, 103, 4, -5, -103, 65, -88, -16, 1, -126, 74, 68, 31, 62, -108, -87, -120, 19, -6, 83, -84, -15, 54, 33, 127, 125, 85, -117, -62, 105
      };
      byte[] bytes162 = AesCipherUtil.decrypt(bytes, bytes161, aesCrypto);
      decoyKeyTable259 = sha3Hash.digest(bytes162);
      byte[] bytes163 = decoyKeyTable259;
      decoyKeyTable341 = sha3Hash.digest(bytes163);
      byte[] bytes164 = new byte[]{
         -69, -19, -22, -115, -43, 50, -68, -1, 114, 109, 47, 65, 96, 21, 51, 65, -108, -105, 93, 12, 72, -10, 57, -121, -113, 25, 99, -104, -83, 69, 60, 103
      };
      byte[] bytes165 = AesCipherUtil.decrypt(bytes, bytes164, aesCrypto);
      decoyKeyTable773 = sha3Hash.digest(bytes165);
      byte[] bytes166 = decoyKeyTable773;
      decoyKeyTable303 = sha3Hash.digest(bytes166);
      byte[] bytes167 = new byte[]{
         -56, -63, -65, 113, 9, 58, -44, -40, 83, -124, 11, -103, 127, -107, -28, 56, 48, 39, 97, 122, -85, -13, 55, -49, -85, 127, 19, -101, -38, -80, -34, 0
      };
      byte[] bytes168 = AesCipherUtil.decrypt(bytes, bytes167, aesCrypto);
      decoyKeyTable074 = sha3Hash.digest(bytes168);
      byte[] bytes169 = decoyKeyTable074;
      decoyKeyTable280 = sha3Hash.digest(bytes169);
      byte[] bytes170 = new byte[]{
         43, 78, 9, 76, 107, -36, 37, 45, -64, 9, -19, -94, 83, 117, -101, 70, -40, 22, -60, 15, 70, -72, -113, -53, -77, -76, 48, 18, -29, 55, 46, -115
      };
      byte[] bytes171 = AesCipherUtil.decrypt(bytes, bytes170, aesCrypto);
      decoyKeyTable707 = sha3Hash.digest(bytes171);
      byte[] bytes172 = decoyKeyTable707;
      decoyKeyTable379 = sha3Hash.digest(bytes172);
      byte[] bytes173 = new byte[]{
         -128,
         28,
         -57,
         -31,
         -37,
         -97,
         -30,
         52,
         -75,
         -62,
         -94,
         -85,
         74,
         -122,
         -82,
         38,
         84,
         -8,
         -128,
         -8,
         85,
         25,
         -66,
         -120,
         -72,
         -78,
         37,
         -128,
         -38,
         80,
         -57,
         -46
      };
      byte[] bytes174 = AesCipherUtil.decrypt(bytes, bytes173, aesCrypto);
      decoyKeyTable267 = sha3Hash.digest(bytes174);
      byte[] bytes175 = decoyKeyTable267;
      decoyKeyTable441 = sha3Hash.digest(bytes175);
      byte[] bytes176 = new byte[]{
         36,
         -105,
         45,
         -87,
         62,
         -57,
         -126,
         -25,
         -53,
         92,
         -50,
         -2,
         -30,
         75,
         81,
         -26,
         124,
         75,
         112,
         125,
         16,
         97,
         -105,
         -121,
         124,
         -21,
         -56,
         -93,
         126,
         112,
         -20,
         -93
      };
      byte[] bytes177 = AesCipherUtil.decrypt(bytes, bytes176, aesCrypto);
      decoyKeyTable515 = sha3Hash.digest(bytes177);
      byte[] bytes178 = decoyKeyTable515;
      decoyKeyTable463 = sha3Hash.digest(bytes178);
      byte[] bytes179 = new byte[]{
         -37, -56, 31, -40, 54, -18, 43, -111, 11, -108, 24, 0, -10, 6, -40, 122, 110, 96, -3, 14, -56, 109, -20, -56, 83, -48, 76, -41, -43, -75, -8, 18
      };
      byte[] bytes180 = AesCipherUtil.decrypt(bytes, bytes179, aesCrypto);
      decoyKeyTable569 = sha3Hash.digest(bytes180);
      byte[] bytes181 = decoyKeyTable569;
      decoyKeyTable718 = sha3Hash.digest(bytes181);
      byte[] bytes182 = new byte[]{
         -105,
         87,
         78,
         102,
         -38,
         -121,
         -115,
         -125,
         46,
         -104,
         -10,
         -29,
         -107,
         -45,
         35,
         25,
         115,
         109,
         -53,
         -109,
         126,
         76,
         106,
         87,
         40,
         84,
         120,
         27,
         -18,
         -105,
         -68,
         -31
      };
      byte[] bytes183 = AesCipherUtil.decrypt(bytes, bytes182, aesCrypto);
      decoyKeyTable027 = sha3Hash.digest(bytes183);
      byte[] bytes184 = decoyKeyTable027;
      decoyKeyTable791 = sha3Hash.digest(bytes184);
      byte[] bytes185 = new byte[]{
         -108, 98, -15, 34, 75, -84, -55, -23, -119, -92, 84, -3, 89, -25, -37, 95, 67, 45, -48, 119, -91, 125, 65, 34, 82, 107, -84, -68, 109, -128, -105, 92
      };
      byte[] bytes186 = AesCipherUtil.decrypt(bytes, bytes185, aesCrypto);
      decoyKeyTable406 = sha3Hash.digest(bytes186);
      byte[] bytes187 = decoyKeyTable406;
      decoyKeyTable088 = sha3Hash.digest(bytes187);
      byte[] bytes188 = new byte[]{
         27, 24, -36, 65, -70, -54, 53, -24, 45, 54, -103, -128, -70, 106, -12, 114, 23, -65, 55, -112, 43, 53, -66, -22, -76, -46, 54, 11, 15, 65, -79, -83
      };
      byte[] bytes189 = AesCipherUtil.decrypt(bytes, bytes188, aesCrypto);
      decoyKeyTable735 = sha3Hash.digest(bytes189);
      byte[] bytes190 = decoyKeyTable735;
      decoyKeyTable093 = sha3Hash.digest(bytes190);
      byte[] bytes191 = new byte[]{
         65,
         105,
         -107,
         -31,
         104,
         39,
         -23,
         103,
         -51,
         27,
         -16,
         74,
         63,
         103,
         8,
         -30,
         -54,
         35,
         -107,
         -88,
         -40,
         126,
         -29,
         -24,
         -89,
         -103,
         33,
         106,
         -6,
         111,
         -114,
         94
      };
      byte[] bytes192 = AesCipherUtil.decrypt(bytes, bytes191, aesCrypto);
      decoyKeyTable747 = sha3Hash.digest(bytes192);
      byte[] bytes193 = decoyKeyTable747;
      decoyKeyTable334 = sha3Hash.digest(bytes193);
      byte[] bytes194 = new byte[]{
         35, -50, 88, 29, -35, 28, -15, -87, -88, -51, 33, 106, 73, -34, -33, -33, -41, -106, -20, 107, 96, 117, -50, 127, 107, -58, 28, 44, 70, 118, -99, -70
      };
      byte[] bytes195 = AesCipherUtil.decrypt(bytes, bytes194, aesCrypto);
      decoyKeyTable358 = sha3Hash.digest(bytes195);
      byte[] bytes196 = decoyKeyTable358;
      decoyKeyTable642 = sha3Hash.digest(bytes196);
      byte[] bytes197 = new byte[]{
         19,
         -31,
         -11,
         -58,
         -78,
         -40,
         -5,
         -117,
         -66,
         -24,
         -123,
         -67,
         -106,
         -61,
         -87,
         -72,
         -2,
         121,
         -97,
         118,
         -74,
         -59,
         125,
         119,
         120,
         51,
         -56,
         76,
         44,
         -18,
         -95,
         -70
      };
      byte[] bytes198 = AesCipherUtil.decrypt(bytes, bytes197, aesCrypto);
      decoyKeyTable764 = sha3Hash.digest(bytes198);
      byte[] bytes199 = decoyKeyTable764;
      decoyKeyTable374 = sha3Hash.digest(bytes199);
      byte[] bytes200 = new byte[]{
         -7, -86, -119, 123, -35, 95, -33, 22, -47, -99, -106, 50, 106, -58, 18, 21, 85, 14, 107, 117, 111, 49, -16, 74, 69, 14, -67, -109, 104, -100, -1, -51
      };
      byte[] bytes201 = AesCipherUtil.decrypt(bytes, bytes200, aesCrypto);
      decoyKeyTable386 = sha3Hash.digest(bytes201);
      byte[] bytes202 = decoyKeyTable386;
      decoyKeyTable790 = sha3Hash.digest(bytes202);
      byte[] bytes203 = new byte[]{
         -76, 14, 121, -79, 84, -76, 86, -107, 90, 1, 88, -7, 79, 60, -32, -91, -26, 80, -128, -42, -66, 77, -59, -119, 34, -47, -85, 43, 20, 16, -90, -24
      };
      byte[] bytes204 = AesCipherUtil.decrypt(bytes, bytes203, aesCrypto);
      decoyKeyTable725 = sha3Hash.digest(bytes204);
      byte[] bytes205 = decoyKeyTable725;
      decoyKeyTable797 = sha3Hash.digest(bytes205);
      byte[] bytes206 = new byte[]{
         22, 18, -59, 81, -116, 15, -38, -57, 23, -76, 107, 53, 33, 63, -126, -15, 110, -36, -57, -94, 127, -91, 91, 69, 107, -97, -93, -122, 96, -3, 77, -122
      };
      byte[] bytes207 = AesCipherUtil.decrypt(bytes, bytes206, aesCrypto);
      decoyKeyTable249 = sha3Hash.digest(bytes207);
      byte[] bytes208 = decoyKeyTable249;
      decoyKeyTable520 = sha3Hash.digest(bytes208);
      byte[] bytes209 = new byte[]{
         -53,
         -62,
         -115,
         91,
         21,
         108,
         -19,
         -91,
         42,
         68,
         -111,
         -47,
         -22,
         -77,
         -45,
         118,
         -90,
         75,
         73,
         -88,
         -29,
         81,
         -126,
         -121,
         -34,
         2,
         -98,
         123,
         101,
         66,
         -2,
         -81
      };
      byte[] bytes210 = AesCipherUtil.decrypt(bytes, bytes209, aesCrypto);
      decoyKeyTable221 = sha3Hash.digest(bytes210);
      byte[] bytes211 = decoyKeyTable221;
      decoyKeyTable147 = sha3Hash.digest(bytes211);
      byte[] bytes212 = new byte[]{
         121, 35, 7, -14, 10, 79, -37, 54, 28, 43, -76, 71, -100, -23, -11, 33, -45, 77, -126, -19, -46, 26, -88, 17, -72, -77, -22, 50, 69, -87, 1, 60
      };
      byte[] bytes213 = AesCipherUtil.decrypt(bytes, bytes212, aesCrypto);
      decoyKeyTable499 = sha3Hash.digest(bytes213);
      byte[] bytes214 = decoyKeyTable499;
      decoyKeyTable512 = sha3Hash.digest(bytes214);
      byte[] bytes215 = new byte[]{
         61,
         -38,
         -100,
         -88,
         112,
         -110,
         -58,
         117,
         43,
         -128,
         90,
         -110,
         33,
         -49,
         -19,
         -3,
         97,
         79,
         103,
         23,
         -123,
         117,
         42,
         -48,
         61,
         87,
         -83,
         49,
         -116,
         -81,
         -119,
         -94
      };
      byte[] bytes216 = AesCipherUtil.decrypt(bytes, bytes215, aesCrypto);
      decoyKeyTable019 = sha3Hash.digest(bytes216);
      byte[] bytes217 = decoyKeyTable019;
      decoyKeyTable799 = sha3Hash.digest(bytes217);
      byte[] bytes218 = new byte[]{
         77,
         -106,
         -37,
         69,
         63,
         -104,
         -56,
         -53,
         -26,
         -96,
         34,
         52,
         -112,
         -11,
         97,
         -112,
         -103,
         96,
         -63,
         -101,
         47,
         -3,
         -44,
         -4,
         -52,
         95,
         -109,
         -78,
         -81,
         -26,
         -79,
         1
      };
      byte[] bytes219 = AesCipherUtil.decrypt(bytes, bytes218, aesCrypto);
      decoyKeyTable744 = sha3Hash.digest(bytes219);
      byte[] bytes220 = decoyKeyTable744;
      decoyKeyTable291 = sha3Hash.digest(bytes220);
      byte[] bytes221 = new byte[]{
         -119, -120, -22, 77, 49, -1, 34, -73, 72, -65, -119, -45, 11, 47, 92, -121, 61, 40, -9, 11, -66, -82, -63, 105, -36, -31, -32, -108, 3, 108, -49, -114
      };
      byte[] bytes222 = AesCipherUtil.decrypt(bytes, bytes221, aesCrypto);
      decoyKeyTable597 = sha3Hash.digest(bytes222);
      byte[] bytes223 = decoyKeyTable597;
      decoyKeyTable743 = sha3Hash.digest(bytes223);
      byte[] bytes224 = new byte[]{
         -25, -52, -52, 77, -122, 100, 24, 53, 97, -17, 71, -6, 13, -57, -84, 60, -124, -122, -39, 54, -121, 4, 33, 10, -48, -36, -94, -88, -11, 93, 63, -115
      };
      byte[] bytes225 = AesCipherUtil.decrypt(bytes, bytes224, aesCrypto);
      decoyKeyTable331 = sha3Hash.digest(bytes225);
      byte[] bytes226 = decoyKeyTable331;
      decoyKeyTable011 = sha3Hash.digest(bytes226);
      byte[] bytes227 = new byte[]{
         88, 95, 63, 88, -59, 27, -43, 43, -105, 30, -30, 65, -25, 47, -6, 97, 48, -42, 99, -5, -9, -36, 98, -22, 12, -1, 111, -26, -32, -12, 19, -6
      };
      byte[] bytes228 = AesCipherUtil.decrypt(bytes, bytes227, aesCrypto);
      decoyTokenGroupC12 = sha3Hash.digest(bytes228);
      populateDecoyTokensGroupC(bytes);
   }

   public EncryptedClassLoader() {
       super(null);
      this.parentClassLoader = getSystemClassLoader();
      checkAntiDebug();
   }

   public static String encryptEntryName(String str, byte[] bytes) {
      Sha3Hash sha3Hash = SHA3_HASH;
      byte[] bytes2 = str.getBytes(StandardCharsets.UTF_8);
      byte[] bytes3 = sha3Hash.digest(bytes2);
      AesCrypto aesCrypto = AES_CRYPTO;
      return AesCipherUtil.bytesToHex(AesCipherUtil.decrypt(bytes3, bytes, aesCrypto));
   }

   private static void checkAntiDebug() {
      ClientArchiveHolder.getControlFlowMarker();
      byte b = 3;
      long l = 0L;
      l = -6895410982136776328L ^ (long)"ୁ瓤ᎍ婂堡뛏䐇겅妡㽯쐓\udabeꑮꃀ뀸脖벷䣹ﱲဃ\ue6b6".hashCode();
      if (!LauncherNetworkClient.getInstance().isDebugModeEnabled()) {
         l = l - -135749960419368358L ^ -1167047964188788405L;
      } else {
         l = l + 3375533976195475994L ^ -1716783292700248957L;
      }

      while (b > 1) {
         if (b <= 2) {
            if (!ClassLoaderAntiDebugCheck.isDebuggerOrAgentDetected()) {
               l = (l ^ 2428618241867797800L) + 5137775764094745788L;
            } else {
               l = l + -7461754994579782015L ^ -5435929335536323309L;
            }
         } else {
            LauncherClientHandler instance = LauncherClientHandler.getInstance();
            C2SRemoteClassRequestPacket packet = new C2SRemoteClassRequestPacket(loginSessionToken, "net/c/");
            instance.sendPacket(packet);
            l = (l ^ -1244168998751804237L) - 1640783819389497421L;
         }
      }
   }

   static {
      AES_CRYPTO = new AesCrypto();
      SHA3_HASH = new Sha3Hash(Sha3Type.SHA3_256);
      SHA256_DIGEST = new Sha256Digest();
   }

}
