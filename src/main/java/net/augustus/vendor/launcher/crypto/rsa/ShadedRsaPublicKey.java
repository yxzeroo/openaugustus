package net.augustus.vendor.launcher.crypto.rsa;

import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import java.security.PublicKey;

public interface ShadedRsaPublicKey extends ShadedRsaKey, PublicKey {
   long serialVersionUID = -8727434096241101194L;

   ShadedBigInteger getPublicExponent(long l);
}
