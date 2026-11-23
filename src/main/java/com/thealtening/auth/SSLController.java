package com.thealtening.auth;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public final class SSLController {
   private static final TrustManager[] ALL_TRUSTING_TRUST_MANAGER = new TrustManager[]{new AllTrustingX509TrustManager()};
   private static final HostnameVerifier ALTENING_HOSTING_VERIFIER;
   private final SSLSocketFactory allTrustingFactory;
   private final SSLSocketFactory originalFactory;
   private final HostnameVerifier originalHostVerifier;

   static {
      ALTENING_HOSTING_VERIFIER = (str, sslSession) -> verifyAlteningHostname(str, sslSession);
   }

   public SSLController() {
      SSLContext sslContext = null;

      try {
         sslContext = SSLContext.getInstance("SSL");
         sslContext.init(null, ALL_TRUSTING_TRUST_MANAGER, new SecureRandom());
      } catch (KeyManagementException | NoSuchAlgorithmException e) {
         e.printStackTrace();
      }

      this.allTrustingFactory = sslContext.getSocketFactory();
      this.originalFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
      this.originalHostVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
   }

   private void updateCertificateValidation(SSLSocketFactory sslSocketFactory, HostnameVerifier hostnameVerifier) {
      HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
      HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
   }

   public void disableCertificateValidation() {
      this.updateCertificateValidation(this.allTrustingFactory, ALTENING_HOSTING_VERIFIER);
   }

   private static boolean verifyAlteningHostname(String str, SSLSession sslSession) {
      return str.equals("authserver.thealtening.com") || str.equals("sessionserver.thealtening.com");
   }

   public void enableCertificateValidation() {
      this.updateCertificateValidation(this.originalFactory, this.originalHostVerifier);
   }
}
