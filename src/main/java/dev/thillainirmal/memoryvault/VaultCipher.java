package dev.thillainirmal.memoryvault;
import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Component; import javax.crypto.*; import javax.crypto.spec.*; import java.nio.charset.StandardCharsets; import java.security.SecureRandom; import java.util.*;
@Component
public class VaultCipher {
 private final SecretKey key; private final SecureRandom random=new SecureRandom();
 public VaultCipher(@Value("${memoryvault.encryption-key}")String encoded){byte[] bytes=Base64.getDecoder().decode(encoded);if(bytes.length!=32)throw new IllegalArgumentException("MEMORY_ENCRYPTION_KEY must be a base64-encoded 32-byte key");this.key=new SecretKeySpec(bytes,"AES");}
 public String encrypt(String plain){try{byte[] iv=new byte[12];random.nextBytes(iv);Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.ENCRYPT_MODE,key,new GCMParameterSpec(128,iv));byte[] encrypted=c.doFinal(plain.getBytes(StandardCharsets.UTF_8));byte[] packed=new byte[iv.length+encrypted.length];System.arraycopy(iv,0,packed,0,iv.length);System.arraycopy(encrypted,0,packed,iv.length,encrypted.length);return Base64.getEncoder().encodeToString(packed);}catch(Exception e){throw new IllegalStateException("Memory encryption failed",e);}}
 public String decrypt(String value){try{byte[] packed=Base64.getDecoder().decode(value);byte[] iv=Arrays.copyOfRange(packed,0,12);Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.DECRYPT_MODE,key,new GCMParameterSpec(128,iv));return new String(c.doFinal(Arrays.copyOfRange(packed,12,packed.length)),StandardCharsets.UTF_8);}catch(Exception e){throw new IllegalStateException("Memory decryption failed",e);}}
}
