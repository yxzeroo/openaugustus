package net.augustus.ui.multiplayer;

public enum ConnectionStatus {
   RESOLVING_SRV("Resolving SRV..."),
   STARTING_NETTY_CONNECTION("Starting Netty Connection..."),
   RESOLVING_IP("Resolving IP..."),
   CONNECTING("Connecting..."),
   SENDING_LOGIN_PACKETS("Sending login packets..."),
   WAITING_FOR_RESPONSE("Waiting for response..."),
   VERIFYING_SESSION("Verifying Session..."),
   ENCRYPTING("Encrypting..."),
   ENABLING_COMPRESSION("Enabling compression..."),
   SUCCESS("Success!");

private final String message;

   private ConnectionStatus(String str) {
      this.message = str;
   }

   public String getMessage() {
      return this.message;
   }
}
