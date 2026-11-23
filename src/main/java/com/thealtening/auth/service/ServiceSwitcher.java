package com.thealtening.auth.service;

import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.URL;

public final class ServiceSwitcher {

   public ServiceSwitcher() {
      YggdrasilMinecraftSessionService.WHITELISTED_DOMAINS = new String[]{
         ".minecraft.net", ".mojang.com", ".thealtening.com"
      };
   }

   public AlteningServiceType switchToService(AlteningServiceType alteningServiceType) {
      try {
         String authServer = alteningServiceType.getAuthServer();
         YggdrasilUserAuthentication.BASE_URL = authServer;
         YggdrasilUserAuthentication.ROUTE_AUTHENTICATE = new URL(authServer + "authenticate");
         YggdrasilUserAuthentication.ROUTE_INVALIDATE = new URL(authServer + "invalidate");
         YggdrasilUserAuthentication.ROUTE_REFRESH = new URL(authServer + "refresh");
         YggdrasilUserAuthentication.ROUTE_VALIDATE = new URL(authServer + "validate");
         YggdrasilUserAuthentication.ROUTE_SIGNOUT = new URL(authServer + "signout");
         String sessionServer = alteningServiceType.getSessionServer();
         YggdrasilMinecraftSessionService.BASE_URL = sessionServer + "session/minecraft/";
         YggdrasilMinecraftSessionService.JOIN_URL = new URL(sessionServer + "session/minecraft/join");
         YggdrasilMinecraftSessionService.CHECK_URL = new URL(sessionServer + "session/minecraft/hasJoined");
         return alteningServiceType;
      } catch (Exception e) {
         e.printStackTrace();
         return AlteningServiceType.MOJANG;
      }
   }
}
