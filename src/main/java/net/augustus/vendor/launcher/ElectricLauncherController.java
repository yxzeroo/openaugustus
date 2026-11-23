package net.augustus.vendor.launcher;

import net.augustus.vendor.launcher.crypto.Sha256Digest;
import net.augustus.vendor.launcher.net.LauncherClientHandler;
import net.augustus.vendor.launcher.packet.C2SActivateClientPacket;
import net.augustus.vendor.launcher.packet.C2SHwidResetPacket;
import net.augustus.vendor.launcher.packet.C2SLoginPacket;
import net.augustus.vendor.launcher.packet.C2SRegisterPacket;
import net.augustus.vendor.launcher.packet.C2SRemoteClassRequestPacket;
import net.augustus.vendor.launcher.packet.C2SStartClientPacket;
import net.augustus.vendor.launcher.security.ClassIntegrityWatchdog;
import net.augustus.vendor.launcher.util.Base64Codec;
import net.augustus.vendor.launcher.util.Base64Decoder;
import net.augustus.vendor.launcher.util.Base64Encoder;
import net.augustus.vendor.launcher.util.IdentityByteTable;
import com.sun.management.OperatingSystemMXBean;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ElectricLauncherController implements Initializable {
   private final LauncherStopwatch tryTimeHelper;
   private final LauncherStopwatch timeHelper;
   @FXML
   public Button startClientButton;
   @FXML
   private TextField usernameLoginField;
   private long startTime;
   @FXML
   private TextField usernameRegisterField;
   @FXML
   public Button loginButton;
   @FXML
   private Text registerPassword2Error;
   @FXML
   private TextField hwidResetUsername;
   @FXML
   public Text infoText;
   @FXML
   public Text activateClientResponse;
   @FXML
   private Text loginFailedText;
   @FXML
   private Text hwidErrorMessage;
   private boolean sent;
   @FXML
   public Text startText;
   @FXML
   private PasswordField registerActivationKey;
   private boolean wasConnectedToServer;
   @FXML
   private PasswordField passwordLoginField;
   private static ElectricLauncherController instance;
   @FXML
   public Button registerButton;
   public static byte[] yeets;
   @FXML
   private PasswordField hwidActivationKey;
   private final Sha256Digest sha256;
   @FXML
   public TabPane registerPane;
   @FXML
   public Button activateKeyButton;
   @FXML
   private PasswordField passwordRegisterField1;
   @FXML
   private Text registerUsernameError;
   @FXML
   private PasswordField passwordRegisterField2;
   @FXML
   private PasswordField hwidResetPassword;
   @FXML
   private AnchorPane loginBox;
   @FXML
   private PasswordField activationField;
   private boolean started;
   @FXML
   private Text registerFailedText;
   @FXML
   private CheckBox rememberLoginButton;
   @FXML
   private Text registerPassword1Error;
   @FXML
   public Button hwidResetButton;
   @FXML
   public TabPane clientTabPane;
   private final CopyOnWriteArrayList<Runnable> action;
   private String userName;
   @FXML
   public ListView<String> clientList;

   @FXML
   public void onHwidReset(MouseEvent mouseEvent) {
      Runnable runnable = () -> {
         String str = this.hwidActivationKey.getText().trim();
         String text = this.hwidResetUsername.getText();
         String str2 = this.hwidResetPassword.getText().trim();
         if (!str.isEmpty()) {
            if (InputCredentialValidator.validateUsername((Text)null, text, (byte)3)) {
               if (InputCredentialValidator.validatePassword((Text)null, str2, (byte)8)) {
                  try {
                     if (this.timeHelper.hasReached(1000L)) {
                        byte[] bytes = Base64Codec.getRfc4648().decode3(str);
                        if (bytes.length > 132) {
                           this.hwidErrorMessage.setText("Please convert your activation-key!");
                           this.hwidErrorMessage.setFill(Paint.valueOf("#f70202"));
                           return;
                        }

                        this.timeHelper.reset();
                        String str3 = System.getenv("PROCESSOR_LEVEL")
                           + System.getenv("NUMBER_OF_PROCESSORS")
                           + System.getenv("PROCESSOR_IDENTIFIER")
                           + File.listRoots()[0].getTotalSpace()
                           + System.getenv("PROCESSOR_REVISION");
                        this.bypass();
                        LauncherClientHandler instance2 = LauncherClientHandler.getInstance();
                        byte[] bytes4 = this.sha256.digest(text.getBytes(StandardCharsets.UTF_8));
                        Sha256Digest sha2563 = this.sha256;
                        byte[] bytes5 = str2.getBytes(StandardCharsets.UTF_8);
                        bytes5 = sha2563.digest(bytes5);
                        byte[] bytes9 = this.sha256.digest(this.generateLicenseKey().getBytes(StandardCharsets.UTF_8));
                        Sha256Digest sha2565 = this.sha256;
                        byte[] bytes11 = str3.getBytes(StandardCharsets.UTF_8);
                        instance2.sendPacket(new C2SHwidResetPacket(bytes4, bytes5, bytes9, sha2565.digest(bytes11), bytes));
                     } else {
                        this.hwidErrorMessage.setText("Click slower! (3 seconds delay)");
                        this.hwidErrorMessage.setFill(Paint.valueOf("#f70202"));
                     }

                     return;
                  } catch (Exception e) {
                     this.hwidErrorMessage.setText("No valid activation-key!");
                     this.hwidErrorMessage.setFill(Paint.valueOf("#f70202"));
                     return;
                  }
               }
            }

            this.hwidErrorMessage.setText("No valid UserName / Password!");
            this.hwidErrorMessage.setFill(Paint.valueOf("#f70202"));
         } else {
            this.hwidErrorMessage.setText("Please enter a activation-key!");
            this.hwidErrorMessage.setFill(Paint.valueOf("#f70202"));
         }
      };
      if (this.wasConnectedToServer) {
         runnable.run();
      } else {
         this.action.add(runnable);
      }
   }

   public Text getLoginFailedText() {
      return this.loginFailedText;
   }

   @FXML
   public void onCheckBoxClick(MouseEvent mouseEvent) {
      CredentialStorage.saveRememberLogin(this.rememberLoginButton.isSelected());
   }

   @FXML
   public void startSelected(MouseEvent mouseEvent) {
      int selectedIndex = this.clientList.getSelectionModel().getSelectedIndex();
      if (selectedIndex >= 0) {
         String str = ((String)this.clientList.getItems().get(selectedIndex)).toLowerCase(Locale.ROOT);
         CredentialStorage.ensureDirectoryExists(CredentialStorage.dataDirectory + "data");
         File file = new File(CredentialStorage.dataDirectory + "data/" + str + "/nativezip.zip");
         File file2 = new File(CredentialStorage.dataDirectory + "data/" + str + "/resources.jar");
         (new IdentityByteTable()).populate();
         String str2 = "00-00-00-00-00-00";
         ((OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
         FileStore fileStore = FileSystems.getDefault().getFileStores().iterator().next();
         Base64Encoder rfc46482 = Base64Codec.getRfc46482();
         String str3 = "";
         String str4 = System.getenv("PROCESSOR_LEVEL")
            + System.getenv("NUMBER_OF_PROCESSORS")
            + System.getenv("PROCESSOR_IDENTIFIER")
            + File.listRoots()[0].getTotalSpace()
            + System.getenv("PROCESSOR_REVISION");

         try {
            StringBuilder sb = new StringBuilder()
               .append(str4)
               .append(System.getenv("PROCESSOR_LEVEL"))
               .append(System.getenv("PROCESSOR_ARCHITECTURE"))
               .append(System.getenv("HOME"))
               .append(System.getenv("NUMBER_OF_PROCESSORS"))
               .append(System.getenv("TMP"))
               .append(System.getenv("PROCESSOR_REVISION"))
               .append(str2)
               .append(System.getenv("COMPUTERNAME"))
               .append(System.getenv("APPDATA"))
               .append(File.listRoots()[0].getTotalSpace())
               .append(System.getenv("DISPLAY"))
               .append(System.getenv("PROCESSOR_IDENTIFIER"))
               .append(System.getenv("USERNAME"))
               .append(System.getenv("HOMEPATH"));
            str3 = sb.append(rfc46482.encodeToString(fileStore.toString().getBytes(StandardCharsets.UTF_8))).append(rfc46482.encodeToString(fileStore.getAttribute("volume:vsn").toString().getBytes(StandardCharsets.UTF_8))).toString();
         } catch (IOException e) {
         }

         if (this.timeHelper.hasReached(1000L)) {
            if (!this.started) {
               if (LauncherClientHandler.getInstance().handshakeCompleted) {
                  LauncherClientHandler instance2 = LauncherClientHandler.getInstance();
                  Sha256Digest sha2562 = this.sha256;
                  byte[] bytes3 = this.userName.getBytes(StandardCharsets.UTF_8);
                  byte[] bytes5 = sha2562.digest(bytes3);
                  Sha256Digest sha2563 = this.sha256;
                  byte[] bytes6 = "aplksdflasdjflasd".getBytes(StandardCharsets.UTF_8);
                  bytes3 = sha2563.digest(bytes6);
                  bytes6 = this.sha256.digest("jkhkgugjkjhjkgjk".getBytes(StandardCharsets.UTF_8));
                  byte[] bytes12 = this.sha256.digest("alsdjhfdlkshoa".getBytes(StandardCharsets.UTF_8));
                  Sha256Digest sha2566 = this.sha256;
                  byte[] bytes14 = "alsjdlkasjdaa".getBytes(StandardCharsets.UTF_8);
                  instance2.sendPacket(new C2SRegisterPacket(bytes5, bytes3, bytes6, bytes12, sha2566.digest(bytes14), this.userName));
               }

               this.started = true;
               this.timeHelper.reset();
               yeets = this.sha256.digest(str3.getBytes(StandardCharsets.UTF_8));
               this.infoText.setFill(Paint.valueOf("#22d451"));
               this.infoText.setText("Checking resource files...");
               String str5 = str3;
               CompletableFuture.runAsync(() -> {
                  byte[] bytes17 = new byte[32];
                  if (file2.exists()) {
                     try {
                        bytes17 = MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(file2.toPath()));
                     } catch (Exception e2) {
                     }
                  }

                  byte[] bytes19 = new byte[32];
                  if (file.exists()) {
                     try {
                        bytes19 = MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(file.toPath()));
                     } catch (Exception e3) {
                     }
                  }

                  LauncherClientHandler instance5 = LauncherClientHandler.getInstance();
                  byte[] sessionToken = LauncherClientHandler.getInstance().sessionToken;
                  Sha256Digest sha2568 = this.sha256;
                  byte[] bytes22 = str5.getBytes(StandardCharsets.UTF_8);
                  instance5.sendPacket(new C2SStartClientPacket(str, bytes17, bytes19, sessionToken, sha2568.digest(bytes22)));
                  this.startText.setText("Started client successfully!");
                  this.infoText.setText("Waiting for server response...");
                  this.startText.setFill(Paint.valueOf("#22d451"));
               });
            } else {
               this.startText.setText("Client already started!");
               this.startText.setFill(Paint.valueOf("#f70202"));
            }
         } else if (!this.started) {
            this.startText.setText("Click slower! (3 seconds delay)");
            this.startText.setFill(Paint.valueOf("#f70202"));
         }
      } else {
         this.startText.setText("Please select a client");
         this.startText.setFill(Paint.valueOf("#f70202"));
      }
   }

   @FXML
   public void onLogin(MouseEvent mouseEvent) {
      Runnable runnable = () -> {
         this.loginFailedText.setText("");
         String text = this.usernameLoginField.getText();
         String text2 = this.passwordLoginField.getText();
         if (InputCredentialValidator.validatePassword((Text)null, text2, (byte)8)) {
            if (InputCredentialValidator.validateUsername((Text)null, text, (byte)3)) {
               if (this.timeHelper.hasReached(1000L)) {
                  CredentialStorage.saveCredentials(text, text2);
                  this.timeHelper.reset();
                  this.bypass();
                  LauncherClientHandler instance2 = LauncherClientHandler.getInstance();
                  Sha256Digest sha2562 = this.sha256;
                  byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
                  byte[] bytes3 = sha2562.digest(bytes);
                  bytes = this.sha256.digest(text2.getBytes(StandardCharsets.UTF_8));
                  Sha256Digest sha2564 = this.sha256;
                  byte[] bytes7 = this.generateLicenseKey().getBytes(StandardCharsets.UTF_8);
                  instance2.sendPacket(new C2SLoginPacket(bytes3, bytes, sha2564.digest(bytes7), text));
                  this.userName = text;
               } else {
                  this.loginFailedText.setText("Click slower! (3 seconds delay)");
                  this.loginFailedText.setFill(Paint.valueOf("#f70202"));
               }

               return;
            }
         }

         this.loginFailedText.setText("No valid UserName / Password!");
         this.loginFailedText.setFill(Paint.valueOf("#f70202"));
      };
      if (this.wasConnectedToServer) {
         runnable.run();
      } else {
         this.action.add(runnable);
      }
   }

   @FXML
   public void openTOS(ActionEvent actionEvent) {
      try {
         Desktop.getDesktop().browse(new URI("https://www.electriclauncher.de/tos"));
      } catch (URISyntaxException | IOException e) {
      }
   }

   private boolean hasServerConnection() {
      return LauncherClientHandler.getInstance() != null && !LauncherClientHandler.getInstance().isAutoRelease();
   }

   public String generateLicenseKey() {
      String str = "00-00-00-00-00-00";
      ((OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
      FileStore fileStore = FileSystems.getDefault().getFileStores().iterator().next();
      Base64Encoder rfc46482 = Base64Codec.getRfc46482();
      String str2 = System.getenv("PROCESSOR_LEVEL")
         + System.getenv("NUMBER_OF_PROCESSORS")
         + System.getenv("PROCESSOR_IDENTIFIER")
         + File.listRoots()[0].getTotalSpace()
         + System.getenv("PROCESSOR_REVISION");

      try {
         StringBuilder sb = new StringBuilder()
            .append(str2)
            .append(System.getenv("PROCESSOR_LEVEL"))
            .append(System.getenv("PROCESSOR_ARCHITECTURE"))
            .append(System.getenv("HOME"))
            .append(System.getenv("NUMBER_OF_PROCESSORS"))
            .append(System.getenv("TMP"))
            .append(System.getenv("PROCESSOR_REVISION"))
            .append(str)
            .append(System.getenv("COMPUTERNAME"))
            .append(System.getenv("APPDATA"))
            .append(File.listRoots()[0].getTotalSpace())
            .append(System.getenv("DISPLAY"))
            .append(System.getenv("PROCESSOR_IDENTIFIER"))
            .append(System.getenv("USERNAME"))
            .append(System.getenv("HOMEPATH"));
         sb = sb.append(rfc46482.encodeToString(fileStore.toString().getBytes(StandardCharsets.UTF_8)));
         return sb.append(rfc46482.encodeToString(fileStore.getAttribute("volume:vsn").toString().getBytes(StandardCharsets.UTF_8))).toString();
      } catch (IOException e) {
         return null;
      }
   }

   public Text getHwidErrorMessage() {
      return this.hwidErrorMessage;
   }

   public static ElectricLauncherController getInstance() {
      if (instance != null) {
         ElectricLauncherController controller = instance;
         if (!ClassIntegrityWatchdog.matchesRegisteredInstance("2", controller)) {
            C2SRemoteClassRequestPacket packet = new C2SRemoteClassRequestPacket(LauncherClientHandler.getInstance().sessionToken, "net/g/");
            LauncherClientHandler.getInstance().sendPacket(packet);
         }
      }

      return instance;
   }

   public void initialize(URL url, ResourceBundle resourceBundle) {
      String str = this.getClass().getResource("/style.css").toExternalForm();
      this.startClientButton.getStylesheets().add(str);
      this.activateKeyButton.getStylesheets().add(str);
      this.loginButton.getStylesheets().add(str);
      this.registerButton.getStylesheets().add(str);
      this.hwidResetButton.getStylesheets().add(str);
      instance = this;
      ClassIntegrityWatchdog.registerInstance("2", this);
      CredentialStorage.loadRememberLogin(this.rememberLoginButton);
      if (this.rememberLoginButton.isSelected()) {
         PasswordField passwordLoginField2 = this.passwordLoginField;
         CredentialStorage.loadCredentials(this.usernameLoginField, passwordLoginField2);
      }

      this.registerPane.setVisible(true);
      this.clientTabPane.setVisible(false);
      this.startClientButton.setVisible(false);
      Timeline timeline = new Timeline(new KeyFrame[]{new KeyFrame(Duration.millis(50.0), actionEvent -> {
         boolean flag = this.hasServerConnection();
         if (!flag) {
            if (this.startTime < 0L) {
               this.startTime = System.currentTimeMillis();
            }

            Paint paint = Paint.valueOf("#f70202");
            String str2 = "Establishing connection to server... (" + LauncherClientHandler.formatDecimal((double)(System.currentTimeMillis() - this.startTime) / 1000.0, 1) + ")";
            this.loginFailedText.setText(str2);
            this.loginFailedText.setFill(paint);
            this.getRegisterFailedText().setText(str2);
            this.getRegisterFailedText().setFill(paint);
            this.hwidErrorMessage.setText(str2);
            this.hwidErrorMessage.setFill(paint);
         } else {
            if (!this.wasConnectedToServer) {
               Paint paint2 = Paint.valueOf("#22d451");
               String str3 = "Established connection successfully!";
               this.loginFailedText.setText(str3);
               this.loginFailedText.setFill(paint2);
               this.getRegisterFailedText().setText(str3);
               this.getRegisterFailedText().setFill(paint2);
               this.hwidErrorMessage.setText(str3);
               this.hwidErrorMessage.setFill(paint2);
            }

            if (!this.action.isEmpty()) {
               Runnable runnable = this.action.get(this.action.size() - 1);
               runnable.run();
               this.action.clear();
            }
         }

         this.wasConnectedToServer = flag;
      }, new KeyValue[0])});
      timeline.setCycleCount(-1);
      timeline.play();
   }

   public ElectricLauncherController() {
      super();
      this.started = false;
      this.timeHelper = new LauncherStopwatch();
      this.sent = false;
      this.tryTimeHelper = new LauncherStopwatch();
      this.wasConnectedToServer = false;
      this.action = new CopyOnWriteArrayList<>();
      this.startTime = -10L;
      this.sha256 = new Sha256Digest();
   }

   @FXML
   public void onRegister(MouseEvent mouseEvent) {
      Runnable runnable = () -> {
         this.registerFailedText.setText("");
         this.registerPassword1Error.setText("");
         this.registerPassword2Error.setText("");
         this.registerUsernameError.setText("");
         String text = this.usernameRegisterField.getText();
         String text2 = this.passwordRegisterField1.getText();
         String text3 = this.passwordRegisterField2.getText();
         boolean flag = true;
         if (!text2.equals(text3)) {
            this.registerPassword2Error.setText("Not the same password!");
            this.registerPassword2Error.setFill(Paint.valueOf("#f70202"));
            flag = false;
         }

         if (flag) {
            if (InputCredentialValidator.validatePassword(this.registerPassword1Error, text2, (byte)8)) {
               if (InputCredentialValidator.validateUsername(this.registerUsernameError, text, (byte)3)) {
                  try {
                     byte[] bytes = Base64Codec.getRfc4648().decode3(this.registerActivationKey.getText().trim());
                     String str3 = "00-00-00-00-00-00";
                     ((OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
                     FileStore fileStore = FileSystems.getDefault().getFileStores().iterator().next();
                     Base64Encoder rfc46482 = Base64Codec.getRfc46482();
                     String str4 = "";
                     String str5 = System.getenv("PROCESSOR_LEVEL")
                        + System.getenv("NUMBER_OF_PROCESSORS")
                        + System.getenv("PROCESSOR_IDENTIFIER")
                        + File.listRoots()[0].getTotalSpace()
                        + System.getenv("PROCESSOR_REVISION");

                     try {
                        StringBuilder sb = new StringBuilder()
                           .append(str5)
                           .append(System.getenv("PROCESSOR_LEVEL"))
                           .append(System.getenv("PROCESSOR_ARCHITECTURE"))
                           .append(System.getenv("HOME"))
                           .append(System.getenv("NUMBER_OF_PROCESSORS"))
                           .append(System.getenv("TMP"))
                           .append(System.getenv("PROCESSOR_REVISION"))
                           .append(str3)
                           .append(System.getenv("COMPUTERNAME"))
                           .append(System.getenv("APPDATA"))
                           .append(File.listRoots()[0].getTotalSpace())
                           .append(System.getenv("DISPLAY"))
                           .append(System.getenv("PROCESSOR_IDENTIFIER"))
                           .append(System.getenv("USERNAME"))
                           .append(System.getenv("HOMEPATH"));
                        str4 = sb.append(rfc46482.encodeToString(fileStore.toString().getBytes(StandardCharsets.UTF_8))).append(rfc46482.encodeToString(fileStore.getAttribute("volume:vsn").toString().getBytes(StandardCharsets.UTF_8))).toString();
                     } catch (IOException e) {
                     }

                     if (this.timeHelper.hasReached(1000L)) {
                        this.timeHelper.reset();
                        this.bypass();
                        LauncherClientHandler instance2 = LauncherClientHandler.getInstance();
                        Sha256Digest sha2562 = this.sha256;
                        byte[] bytes4 = text.getBytes(StandardCharsets.UTF_8);
                        byte[] bytes6 = sha2562.digest(bytes4);
                        bytes4 = this.sha256.digest(text2.getBytes(StandardCharsets.UTF_8));
                        byte[] bytes11 = this.sha256.digest(str4.getBytes(StandardCharsets.UTF_8));
                        Sha256Digest sha2565 = this.sha256;
                        byte[] bytes13 = str5.getBytes(StandardCharsets.UTF_8);
                        instance2.sendPacket(new C2SRegisterPacket(bytes6, bytes4, bytes, bytes11, sha2565.digest(bytes13), text));
                     } else {
                        this.getRegisterFailedText().setText("Click slower! (3 seconds delay)");
                        this.getRegisterFailedText().setFill(Paint.valueOf("#f70202"));
                     }
                  } catch (Exception e2) {
                     this.getRegisterFailedText().setText("No valid activation key!");
                     this.getRegisterFailedText().setFill(Paint.valueOf("#f70202"));
                  }
               }
            }
         }
      };
      if (this.wasConnectedToServer) {
         runnable.run();
      } else {
         this.action.add(runnable);
      }
   }

   private void bypass() {
   }

   public Text getRegisterFailedText() {
      return this.registerFailedText;
   }

   @FXML
   public void activateClient(MouseEvent mouseEvent) {
      String text = this.activationField.getText();
      if (text.isEmpty()) {
         this.activateClientResponse.setText("Please enter a activation-key!");
         this.activateClientResponse.setFill(Paint.valueOf("#f70202"));
      } else {
         try {
            LauncherClientHandler instance2 = LauncherClientHandler.getInstance();
            Base64Decoder rfc4648 = Base64Codec.getRfc4648();
            instance2.sendPacket(new C2SActivateClientPacket(rfc4648.decode3(text), LauncherClientHandler.getInstance().sessionToken));
         } catch (Exception e) {
            this.activateClientResponse.setText("No valid activation-key!");
            this.activateClientResponse.setFill(Paint.valueOf("#f70202"));
         }
      }
   }

}
