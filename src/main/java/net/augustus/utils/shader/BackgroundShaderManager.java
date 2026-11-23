package net.augustus.utils.shader;

import net.augustus.Augustus;
import net.augustus.config.ClientSettingsConfigFile;
import net.augustus.render.GpuVendorDetector;
import net.augustus.settings.property.StringProperty;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.ClientPaths;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;
import net.minecraft.block.state.pattern.BlockStateHelper;
import org.apache.commons.io.FileUtils;

public class BackgroundShaderManager {
   private final StringProperty selectedShader;
   private final HashMap shadersByName;
   private final List sortedShaders;
   private BackgroundShader currentShader;

   public BackgroundShader getCurrentShader() {
      return this.currentShader;
   }

   private void extractResourceFile(String str, String str2, boolean flag) {
      if (str != null && !str.equals("")) {
         str = str.replace('\\', '/');
         InputStream resourceAsStream = BlockStateHelper.class.getClassLoader().getResourceAsStream(str);
         if (resourceAsStream == null) {
            throw new IllegalArgumentException("The embedded resource '" + str + "' cannot be found in client");
         } else {
            File file = new File(str);
            File file2 = new File(ClientFileUtil.WORKING_DIR + str2 + "/" + file.getName());
            if (!file2.getParentFile().exists()) {
               try {
                  Files.createDirectories(file2.getParentFile().toPath());
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }

            try {
               if (!file2.exists() || flag) {
                  FileUtils.copyInputStreamToFile(resourceAsStream, file2);
                  resourceAsStream.close();
               }
            } catch (IOException e2) {
               System.err.println(Level.SEVERE + "Could not save " + file2.getName() + " to " + file2.getAbsolutePath());
            }
         }
      } else {
         throw new IllegalArgumentException("ResourcePath cannot be null or empty");
      }
   }

   public List getSortedShaders() {
      return this.sortedShaders;
   }

   public String getCurrentShaderName() {
      return this.currentShader.getName().toLowerCase(Locale.ROOT);
   }

   public void selectShader(String str) {
      BackgroundShader backgroundShader = (BackgroundShader)this.shadersByName.get(str.toLowerCase(Locale.ROOT));
      if (backgroundShader != null) {
         this.currentShader = backgroundShader;
      }
   }

   private boolean supportsExtendedShaders() {
      return GpuVendorDetector.isNvidia();
   }

   public BackgroundShaderManager() {
       super();
      this.shadersByName = new HashMap();
      StringProperty stringProperty = new StringProperty(ClientSettingsConfigFile.abstractFeature, "SelectedShader", "");
      Consumer consumer = obj -> this.selectShader((String)obj);
      StringProperty stringProperty2 = (StringProperty)stringProperty.withOnValueChanged(consumer);
      Supplier supplier = () -> this.getCurrentShaderName();
      this.selectedShader = (StringProperty)stringProperty2.withValueSupplier(supplier);
      String str = Augustus.getInstance().getName().toLowerCase(Locale.ROOT);
      String str2 = str + "/shaders";
      boolean flag = false;
      String str3 = str2;
      String str4 = "assets/augustus/shaders/abraxas.frag";
      this.extractResourceFile(str4, str3, flag);
      str2 = str + "/shaders";
      boolean flag2 = false;
      String str5 = str2;
      String str6 = "assets/augustus/shaders/galaxy.frag";
      this.extractResourceFile(str6, str5, flag2);
      str2 = str + "/shaders";
      boolean flag3 = false;
      String str7 = str2;
      String str8 = "assets/augustus/shaders/galaxy2.frag";
      this.extractResourceFile(str8, str7, flag3);
      str2 = str + "/shaders";
      boolean flag4 = false;
      String str9 = str2;
      String str10 = "assets/augustus/shaders/purplewater.frag";
      this.extractResourceFile(str10, str9, flag4);
      String str11 = str + "/shaders";
      boolean flag5 = false;
      String str12 = str11;
      String str13 = "assets/augustus/shaders/liquidbounce.frag";
      this.extractResourceFile(str13, str12, flag5);
      str11 = str + "/shaders";
      boolean flag6 = false;
      String str14 = str11;
      String str15 = "assets/augustus/shaders/redwaterlike.frag";
      this.extractResourceFile(str15, str14, flag6);
      str11 = str + "/shaders";
      boolean flag7 = false;
      String str16 = str11;
      String str17 = "assets/augustus/shaders/sexy.frag";
      this.extractResourceFile(str17, str16, flag7);
      str11 = str + "/shaders";
      boolean flag8 = false;
      String str18 = str11;
      String str19 = "assets/augustus/shaders/metal.frag";
      this.extractResourceFile(str19, str18, flag8);
      str11 = str + "/shaders";
      boolean flag9 = false;
      String str20 = str11;
      String str21 = "assets/augustus/shaders/snowstar.frag";
      this.extractResourceFile(str21, str20, flag9);
      str11 = str + "/shaders";
      boolean flag10 = false;
      String str22 = str11;
      String str23 = "assets/augustus/shaders/spider.frag";
      this.extractResourceFile(str23, str22, flag10);
      str11 = str + "/shaders";
      boolean flag11 = false;
      String str24 = str11;
      String str25 = "assets/augustus/shaders/trinity.frag";
      this.extractResourceFile(str25, str24, flag11);
      str11 = str + "/shaders";
      boolean flag12 = false;
      String str26 = str11;
      String str27 = "assets/augustus/shaders/bluefog.frag";
      this.extractResourceFile(str27, str26, flag12);
      if (this.supportsExtendedShaders()) {
         str11 = str + "/shaders";
         boolean flag13 = false;
         String str28 = str11;
         String str29 = "assets/augustus/shaders/cloudtunnel.frag";
         this.extractResourceFile(str29, str28, flag13);
      }

      str11 = str + "/shaders";
      boolean flag14 = false;
      String str30 = str11;
      String str31 = "assets/augustus/shaders/geometry.frag";
      this.extractResourceFile(str31, str30, flag14);
      str11 = str + "/shaders";
      boolean flag15 = false;
      String str32 = str11;
      String str33 = "assets/augustus/shaders/ukraine.frag";
      this.extractResourceFile(str33, str32, flag15);
      str11 = str + "/shaders";
      boolean flag16 = false;
      String str34 = str11;
      String str35 = "assets/augustus/shaders/town.frag";
      this.extractResourceFile(str35, str34, flag16);
      if (this.supportsExtendedShaders()) {
         str11 = str + "/shaders";
         boolean flag17 = false;
         String str36 = str11;
         String str37 = "assets/augustus/shaders/labyrinth.frag";
         this.extractResourceFile(str37, str36, flag17);
      }

      str11 = str + "/shaders";
      boolean flag18 = false;
      String str38 = str11;
      String str39 = "assets/augustus/shaders/minecraft.frag";
      this.extractResourceFile(str39, str38, flag18);
      str11 = str + "/shaders";
      boolean flag19 = false;
      String str40 = str11;
      String str41 = "assets/augustus/shaders/starfog.frag";
      this.extractResourceFile(str41, str40, flag19);
      str11 = str + "/shaders";
      boolean flag20 = false;
      String str42 = str11;
      String str43 = "assets/augustus/shaders/polarlights.frag";
      this.extractResourceFile(str43, str42, flag20);
      if (this.supportsExtendedShaders()) {
         str11 = str + "/shaders";
         boolean flag21 = false;
         String str44 = str11;
         String str45 = "assets/augustus/shaders/squeezysound.frag";
         this.extractResourceFile(str45, str44, flag21);
      }

      str11 = str + "/shaders";
      boolean flag22 = false;
      String str46 = str11;
      String str47 = "assets/augustus/shaders/burningsun.frag";
      this.extractResourceFile(str47, str46, flag22);
      if (this.supportsExtendedShaders()) {
         str11 = str + "/shaders";
         boolean flag23 = false;
         String str48 = str11;
         String str49 = "assets/augustus/shaders/mandelbrot.frag";
         this.extractResourceFile(str49, str48, flag23);
      }

      str11 = str + "/shaders";
      boolean flag24 = false;
      String str50 = str11;
      String str51 = "assets/augustus/shaders/Augustus1.frag";
      this.extractResourceFile(str51, str50, flag24);
      if (this.supportsExtendedShaders()) {
         str11 = str + "/shaders";
         boolean flag25 = false;
         String str52 = str11;
         String str53 = "assets/augustus/shaders/Augustus2.frag";
         this.extractResourceFile(str53, str52, flag25);
      }

      str11 = str + "/shaders";
      boolean flag26 = false;
      String str54 = str11;
      String str55 = "assets/augustus/shaders/Clean.frag";
      this.extractResourceFile(str55, str54, flag26);
      str11 = str + "/shaders";
      boolean flag27 = false;
      String str56 = str11;
      String str57 = "assets/augustus/shaders/Dynamic.frag";
      this.extractResourceFile(str57, str56, flag27);
      str11 = str + "/shaders";
      boolean flag28 = false;
      String str58 = str11;
      String str59 = "assets/augustus/shaders/rise1.frag";
      this.extractResourceFile(str59, str58, flag28);
      str11 = str + "/shaders";
      boolean flag29 = false;
      String str60 = str11;
      String str61 = "assets/augustus/shaders/water.frag";
      this.extractResourceFile(str61, str60, flag29);
      str11 = str + "/shaders";
      boolean flag30 = false;
      String str62 = str11;
      String str63 = "assets/augustus/shaders/fade.frag";
      this.extractResourceFile(str63, str62, flag30);
      str11 = str + "/shaders";
      boolean flag31 = false;
      String str64 = str11;
      String str65 = "assets/augustus/shaders/rise2.frag";
      this.extractResourceFile(str65, str64, flag31);
      str11 = str + "/shaders";
      boolean flag32 = false;
      String str66 = str11;
      String str67 = "assets/augustus/shaders/rise3.frag";
      this.extractResourceFile(str67, str66, flag32);
      str11 = str + "/shaders";
      boolean flag33 = false;
      String str68 = str11;
      String str69 = "assets/augustus/shaders/purple.frag";
      this.extractResourceFile(str69, str68, flag33);
      str11 = str + "/shaders";
      boolean flag34 = false;
      String str70 = str11;
      String str71 = "assets/augustus/shaders/purplefade.frag";
      this.extractResourceFile(str71, str70, flag34);
      str11 = str + "/shaders";
      boolean flag35 = false;
      String str72 = str11;
      String str73 = "assets/augustus/shaders/circle.frag";
      this.extractResourceFile(str73, str72, flag35);
      str11 = str + "/shaders";
      boolean flag36 = false;
      String str74 = str11;
      String str75 = "assets/augustus/shaders/clean2.frag";
      this.extractResourceFile(str75, str74, flag36);
      str11 = str + "/shaders";
      boolean flag37 = false;
      String str76 = str11;
      String str77 = "assets/augustus/shaders/space3.frag";
      this.extractResourceFile(str77, str76, flag37);
      str11 = str + "/shaders";
      boolean flag38 = false;
      String str78 = str11;
      String str79 = "assets/augustus/shaders/dynamic2.frag";
      this.extractResourceFile(str79, str78, flag38);
      str11 = str + "/shaders";
      boolean flag39 = false;
      String str80 = str11;
      String str81 = "assets/augustus/shaders/dynamic3.frag";
      this.extractResourceFile(str81, str80, flag39);
      str11 = str + "/shaders";
      boolean flag40 = false;
      String str82 = str11;
      String str83 = "assets/augustus/shaders/cube2.frag";
      this.extractResourceFile(str83, str82, flag40);
      str11 = str + "/shaders";
      boolean flag41 = false;
      String str84 = str11;
      String str85 = "assets/augustus/shaders/core2.frag";
      this.extractResourceFile(str85, str84, flag41);
      str11 = str + "/shaders";
      boolean flag42 = false;
      String str86 = str11;
      String str87 = "assets/augustus/shaders/retrowave.frag";
      this.extractResourceFile(str87, str86, flag42);
      str11 = str + "/shaders";
      boolean flag43 = false;
      String str88 = str11;
      String str89 = "assets/augustus/shaders/donut.frag";
      this.extractResourceFile(str89, str88, flag43);
      str11 = str + "/shaders";
      boolean flag44 = false;
      String str90 = str11;
      String str91 = "assets/augustus/shaders/cube.frag";
      this.extractResourceFile(str91, str90, flag44);
      str11 = str + "/shaders";
      boolean flag45 = false;
      String str92 = str11;
      String str93 = "assets/augustus/shaders/bubbles.frag";
      this.extractResourceFile(str93, str92, flag45);
      str11 = str + "/shaders";
      boolean flag46 = false;
      String str94 = str11;
      String str95 = "assets/augustus/shaders/core.frag";
      this.extractResourceFile(str95, str94, flag46);
      str11 = str + "/shaders";
      boolean flag47 = false;
      String str96 = str11;
      String str97 = "assets/augustus/shaders/heart.frag";
      this.extractResourceFile(str97, str96, flag47);
      str11 = str + "/shaders";
      boolean flag48 = false;
      String str98 = str11;
      String str99 = "assets/augustus/shaders/river.frag";
      this.extractResourceFile(str99, str98, flag48);
      str11 = str + "/shaders";
      boolean flag49 = false;
      String str100 = str11;
      String str101 = "assets/augustus/shaders/space.frag";
      this.extractResourceFile(str101, str100, flag49);
      str11 = str + "/shaders";
      boolean flag50 = false;
      String str102 = str11;
      String str103 = "assets/augustus/shaders/BasicStarfield2.frag";
      this.extractResourceFile(str103, str102, flag50);
      str11 = str + "/shaders";
      boolean flag51 = false;
      String str104 = str11;
      String str105 = "assets/augustus/shaders/LilaFrag.frag";
      this.extractResourceFile(str105, str104, flag51);
      str11 = str + "/shaders";
      boolean flag52 = false;
      String str106 = str11;
      String str107 = "assets/augustus/shaders/NShader1.frag";
      this.extractResourceFile(str107, str106, flag52);
      str11 = str + "/shaders";
      boolean flag53 = false;
      String str108 = str11;
      String str109 = "assets/augustus/shaders/NShader2.frag";
      this.extractResourceFile(str109, str108, flag53);
      str11 = str + "/shaders";
      boolean flag54 = false;
      String str110 = str11;
      String str111 = "assets/augustus/shaders/RandomGalaxy2.frag";
      this.extractResourceFile(str111, str110, flag54);
      str11 = str + "/shaders";
      boolean flag55 = false;
      String str112 = str11;
      String str113 = "assets/augustus/shaders/RandomGalaxy3.frag";
      this.extractResourceFile(str113, str112, flag55);
      str11 = str + "/shaders";
      boolean flag56 = false;
      String str114 = str11;
      String str115 = "assets/augustus/shaders/RedOrangeDiashow.frag";
      this.extractResourceFile(str115, str114, flag56);
      str11 = str + "/shaders";
      boolean flag57 = false;
      String str116 = str11;
      String str117 = "assets/augustus/shaders/TheEvolution.frag";
      this.extractResourceFile(str117, str116, flag57);
      str11 = str + "/shaders";
      boolean flag58 = false;
      String str118 = str11;
      String str119 = "assets/augustus/shaders/auroras.frag";
      this.extractResourceFile(str119, str118, flag58);
      str11 = str + "/shaders";
      boolean flag59 = false;
      String str120 = str11;
      String str121 = "assets/augustus/shaders/bash.frag";
      this.extractResourceFile(str121, str120, flag59);
      if (this.supportsExtendedShaders()) {
         str11 = str + "/shaders";
         boolean flag60 = false;
         String str122 = str11;
         String str123 = "assets/augustus/shaders/bash117.frag";
         this.extractResourceFile(str123, str122, flag60);
         str11 = str + "/shaders";
         boolean flag61 = false;
         String str124 = str11;
         String str125 = "assets/augustus/shaders/blackhole.frag";
         this.extractResourceFile(str125, str124, flag61);
         str11 = str + "/shaders";
         boolean flag62 = false;
         String str126 = str11;
         String str127 = "assets/augustus/shaders/BlueArt.frag";
         this.extractResourceFile(str127, str126, flag62);
         str11 = str + "/shaders";
         boolean flag63 = false;
         String str128 = str11;
         String str129 = "assets/augustus/shaders/core3.frag";
         this.extractResourceFile(str129, str128, flag63);
         str11 = str + "/shaders";
         boolean flag64 = false;
         String str130 = str11;
         String str131 = "assets/augustus/shaders/core4.frag";
         this.extractResourceFile(str131, str130, flag64);
         str11 = str + "/shaders";
         boolean flag65 = false;
         String str132 = str11;
         String str133 = "assets/augustus/shaders/Cubictrip.frag";
         this.extractResourceFile(str133, str132, flag65);
         str11 = str + "/shaders";
         boolean flag66 = false;
         String str134 = str11;
         String str135 = "assets/augustus/shaders/dvdlogo.frag";
         this.extractResourceFile(str135, str134, flag66);
         str11 = str + "/shaders";
         boolean flag67 = false;
         String str136 = str11;
         String str137 = "assets/augustus/shaders/dynamic5.frag";
         this.extractResourceFile(str137, str136, flag67);
         str11 = str + "/shaders";
         boolean flag68 = false;
         String str138 = str11;
         String str139 = "assets/augustus/shaders/FavCurrent.frag";
         this.extractResourceFile(str139, str138, flag68);
         str11 = str + "/shaders";
         boolean flag69 = false;
         String str140 = str11;
         String str141 = "assets/augustus/shaders/fireworks.frag";
         this.extractResourceFile(str141, str140, flag69);
         str11 = str + "/shaders";
         boolean flag70 = false;
         String str142 = str11;
         String str143 = "assets/augustus/shaders/foggy.frag";
         this.extractResourceFile(str143, str142, flag70);
         str11 = str + "/shaders";
         boolean flag71 = false;
         String str144 = str11;
         String str145 = "assets/augustus/shaders/foggy2.frag";
         this.extractResourceFile(str145, str144, flag71);
         str11 = str + "/shaders";
         boolean flag72 = false;
         String str146 = str11;
         String str147 = "assets/augustus/shaders/fractal.frag";
         this.extractResourceFile(str147, str146, flag72);
         str11 = str + "/shaders";
         boolean flag73 = false;
         String str148 = str11;
         String str149 = "assets/augustus/shaders/galaxystorm.frag";
         this.extractResourceFile(str149, str148, flag73);
         str11 = str + "/shaders";
         boolean flag74 = false;
         String str150 = str11;
         String str151 = "assets/augustus/shaders/idk1.frag";
         this.extractResourceFile(str151, str150, flag74);
         str11 = str + "/shaders";
         boolean flag75 = false;
         String str152 = str11;
         String str153 = "assets/augustus/shaders/LenniZwei.frag";
         this.extractResourceFile(str153, str152, flag75);
         str11 = str + "/shaders";
         boolean flag76 = false;
         String str154 = str11;
         String str155 = "assets/augustus/shaders/pulse.frag";
         this.extractResourceFile(str155, str154, flag76);
         str11 = str + "/shaders";
         boolean flag77 = false;
         String str156 = str11;
         String str157 = "assets/augustus/shaders/random_galaxy.frag";
         this.extractResourceFile(str157, str156, flag77);
         str11 = str + "/shaders";
         boolean flag78 = false;
         String str158 = str11;
         String str159 = "assets/augustus/shaders/randomboxes.frag";
         this.extractResourceFile(str159, str158, flag78);
         str11 = str + "/shaders";
         boolean flag79 = false;
         String str160 = str11;
         String str161 = "assets/augustus/shaders/retrostarnight.frag";
         this.extractResourceFile(str161, str160, flag79);
         str11 = str + "/shaders";
         boolean flag80 = false;
         String str162 = str11;
         String str163 = "assets/augustus/shaders/ricky.frag";
         this.extractResourceFile(str163, str162, flag80);
         str11 = str + "/shaders";
         boolean flag81 = false;
         String str164 = str11;
         String str165 = "assets/augustus/shaders/rkfadeside.frag";
         this.extractResourceFile(str165, str164, flag81);
         str11 = str + "/shaders";
         boolean flag82 = false;
         String str166 = str11;
         String str167 = "assets/augustus/shaders/singularity.frag";
         this.extractResourceFile(str167, str166, flag82);
         str11 = str + "/shaders";
         boolean flag83 = false;
         String str168 = str11;
         String str169 = "assets/augustus/shaders/snowflakes.frag";
         this.extractResourceFile(str169, str168, flag83);
         str11 = str + "/shaders";
         boolean flag84 = false;
         String str170 = str11;
         String str171 = "assets/augustus/shaders/space2.frag";
         this.extractResourceFile(str171, str170, flag84);
         str11 = str + "/shaders";
         boolean flag85 = false;
         String str172 = str11;
         String str173 = "assets/augustus/shaders/star.frag";
         this.extractResourceFile(str173, str172, flag85);
         str11 = str + "/shaders";
         boolean flag86 = false;
         String str174 = str11;
         String str175 = "assets/augustus/shaders/theshy.frag";
         this.extractResourceFile(str175, str174, flag86);
      }

      str11 = str + "/shaders";
      boolean flag87 = false;
      String str176 = str11;
      String str177 = "assets/augustus/shaders/bloodmaxiter.frag";
      this.extractResourceFile(str177, str176, flag87);
      str11 = str + "/shaders";
      boolean flag88 = false;
      String str178 = str11;
      String str179 = "assets/augustus/shaders/bluefade.frag";
      this.extractResourceFile(str179, str178, flag88);
      str11 = str + "/shaders";
      boolean flag89 = false;
      String str180 = str11;
      String str181 = "assets/augustus/shaders/bluefade2.frag";
      this.extractResourceFile(str181, str180, flag89);
      str11 = str + "/shaders";
      boolean flag90 = false;
      String str182 = str11;
      String str183 = "assets/augustus/shaders/cryptonic_1_12.frag";
      this.extractResourceFile(str183, str182, flag90);
      str11 = str + "/shaders";
      boolean flag91 = false;
      String str184 = str11;
      String str185 = "assets/augustus/shaders/deadtome.frag";
      this.extractResourceFile(str185, str184, flag91);
      str11 = str + "/shaders";
      boolean flag92 = false;
      String str186 = str11;
      String str187 = "assets/augustus/shaders/debug.frag";
      this.extractResourceFile(str187, str186, flag92);
      str11 = str + "/shaders";
      boolean flag93 = false;
      String str188 = str11;
      String str189 = "assets/augustus/shaders/eject.frag";
      this.extractResourceFile(str189, str188, flag93);
      str11 = str + "/shaders";
      boolean flag94 = false;
      String str190 = str11;
      String str191 = "assets/augustus/shaders/ejectlila.frag";
      this.extractResourceFile(str191, str190, flag94);
      str11 = str + "/shaders";
      boolean flag95 = false;
      String str192 = str11;
      String str193 = "assets/augustus/shaders/fixed_rednight.frag";
      this.extractResourceFile(str193, str192, flag95);
      str11 = str + "/shaders";
      boolean flag96 = false;
      String str194 = str11;
      String str195 = "assets/augustus/shaders/full_polarlights.frag";
      this.extractResourceFile(str195, str194, flag96);
      str11 = str + "/shaders";
      boolean flag97 = false;
      String str196 = str11;
      String str197 = "assets/augustus/shaders/ghosttravel.frag";
      this.extractResourceFile(str197, str196, flag97);
      str11 = str + "/shaders";
      boolean flag98 = false;
      String str198 = str11;
      String str199 = "assets/augustus/shaders/lbday.frag";
      this.extractResourceFile(str199, str198, flag98);
      str11 = str + "/shaders";
      boolean flag99 = false;
      String str200 = str11;
      String str201 = "assets/augustus/shaders/lennidark.frag";
      this.extractResourceFile(str201, str200, flag99);
      str11 = str + "/shaders";
      boolean flag100 = false;
      String str202 = str11;
      String str203 = "assets/augustus/shaders/master.frag";
      this.extractResourceFile(str203, str202, flag100);
      str11 = str + "/shaders";
      boolean flag101 = false;
      String str204 = str11;
      String str205 = "assets/augustus/shaders/maxiter.frag";
      this.extractResourceFile(str205, str204, flag101);
      str11 = str + "/shaders";
      boolean flag102 = false;
      String str206 = str11;
      String str207 = "assets/augustus/shaders/neonsunrise.frag";
      this.extractResourceFile(str207, str206, flag102);
      str11 = str + "/shaders";
      boolean flag103 = false;
      String str208 = str11;
      String str209 = "assets/augustus/shaders/panorama.frag";
      this.extractResourceFile(str209, str208, flag103);
      str11 = str + "/shaders";
      boolean flag104 = false;
      String str210 = str11;
      String str211 = "assets/augustus/shaders/pixelgradient.frag";
      this.extractResourceFile(str211, str210, flag104);
      str11 = str + "/shaders";
      boolean flag105 = false;
      String str212 = str11;
      String str213 = "assets/augustus/shaders/prestige.frag";
      this.extractResourceFile(str213, str212, flag105);
      str11 = str + "/shaders";
      boolean flag106 = false;
      String str214 = str11;
      String str215 = "assets/augustus/shaders/rednight.frag";
      this.extractResourceFile(str215, str214, flag106);
      str11 = str + "/shaders";
      boolean flag107 = false;
      String str216 = str11;
      String str217 = "assets/augustus/shaders/rednightnew.frag";
      this.extractResourceFile(str217, str216, flag107);
      str11 = str + "/shaders";
      boolean flag108 = false;
      String str218 = str11;
      String str219 = "assets/augustus/shaders/retrosuntravel.frag";
      this.extractResourceFile(str219, str218, flag108);
      str11 = str + "/shaders";
      boolean flag109 = false;
      String str220 = str11;
      String str221 = "assets/augustus/shaders/rickandmortylike.frag";
      this.extractResourceFile(str221, str220, flag109);
      str11 = str + "/shaders";
      boolean flag110 = false;
      String str222 = str11;
      String str223 = "assets/augustus/shaders/rings.frag";
      this.extractResourceFile(str223, str222, flag110);
      str11 = str + "/shaders";
      boolean flag111 = false;
      String str224 = str11;
      String str225 = "assets/augustus/shaders/rkblue.frag";
      this.extractResourceFile(str225, str224, flag111);
      str11 = str + "/shaders";
      boolean flag112 = false;
      String str226 = str11;
      String str227 = "assets/augustus/shaders/rknight.frag";
      this.extractResourceFile(str227, str226, flag112);
      str11 = str + "/shaders";
      boolean flag113 = false;
      String str228 = str11;
      String str229 = "assets/augustus/shaders/sandstorm.frag";
      this.extractResourceFile(str229, str228, flag113);
      str11 = str + "/shaders";
      boolean flag114 = false;
      String str230 = str11;
      String str231 = "assets/augustus/shaders/snowtravel.frag";
      this.extractResourceFile(str231, str230, flag114);
      str11 = str + "/shaders";
      boolean flag115 = false;
      String str232 = str11;
      String str233 = "assets/augustus/shaders/startrip.frag";
      this.extractResourceFile(str233, str232, flag115);
      str11 = str + "/shaders";
      boolean flag116 = false;
      String str234 = str11;
      String str235 = "assets/augustus/shaders/tunnel.frag";
      this.extractResourceFile(str235, str234, flag116);
      str11 = str + "/shaders";
      boolean flag117 = false;
      String str236 = str11;
      String str237 = "assets/augustus/shaders/unicode.frag";
      this.extractResourceFile(str237, str236, flag117);
      str11 = str + "/shaders";
      boolean flag118 = false;
      String str238 = str11;
      String str239 = "assets/augustus/shaders/dynamic4.frag";
      this.extractResourceFile(str239, str238, flag118);
      str11 = str + "/shaders";
      boolean flag119 = false;
      String str240 = str11;
      String str241 = "assets/augustus/shaders/Deutschland.frag";
      this.extractResourceFile(str241, str240, flag119);
      str11 = str + "/shaders";
      boolean flag120 = false;
      String str242 = str11;
      String str243 = "assets/augustus/shaders/matrix.frag";
      this.extractResourceFile(str243, str242, flag120);
      str11 = str + "/settings";
      boolean flag121 = false;
      String str244 = str11;
      String str245 = "assets/augustus/ip_changer_fritzbox.vbs";
      this.extractResourceFile(str245, str244, flag121);
      str11 = str + "/settings";
      boolean flag122 = false;
      String str246 = str11;
      String str247 = "assets/augustus/reconnect.sh";
      this.extractResourceFile(str247, str246, flag122);
      File file = new File(ClientPaths.CLIENT_DIR + "/shaders");
      File[] files = file.listFiles();

      for (File file2 : files) {
         if (file2.getName().contains(".")) {
            String[] strings = file2.getName().split("\\.");
            if (strings.length > 1 && strings[1].equals("frag")) {
               BackgroundShader backgroundShader = new BackgroundShader();
               char[] chars = strings[0].toCharArray();
               chars[0] = Character.toUpperCase(chars[0]);
               String absolutePath = file2.getAbsolutePath();
               String str248 = new String(chars);
               String str249 = absolutePath;
               backgroundShader.loadFromFile(str249, str248);
               this.shadersByName.put(strings[0].toLowerCase(Locale.ROOT), backgroundShader);
            }
         }
      }

      BackgroundShader backgroundShader2 = (BackgroundShader)this.shadersByName.get("bluefog");
      if (backgroundShader2 == null) {
         backgroundShader2 = (BackgroundShader)this.shadersByName.values().iterator().next();
      }

      this.currentShader = backgroundShader2;
      this.sortedShaders = (List)this.shadersByName.values().stream().sorted(Comparator.comparing(obj2 -> ((BackgroundShader)obj2).getName())).collect(Collectors.toList());
   }

}
