package csc472.depaul.edu.homeworksix;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class Cryption
{
    static Cryption cryption = null;

    private Aes256 aes256 = null;

    static
    {
        cryption = new Cryption();
    }

    private Cryption()
    {
    }

    public static Cryption getCryption()
    {
        return cryption;
    }

    private Aes256 getAes256()
    {
        if (aes256 == null)
        {
            aes256 = new Aes256();
        }

        return aes256;
    }

    public String aes256Encrypt(final String secret, String sUnencryptedData) throws GeneralSecurityException
    {
        return getAes256().encrypt(secret, sUnencryptedData);
    }

    public String aes256Decrypt(final String secret, String sEncryptedData) throws GeneralSecurityException
    {
        return getAes256().decrypt(secret, sEncryptedData);
    }

    private final class Aes256
    {
        private static final String AES_MODE = "AES/CBC/PKCS7Padding";
        private static final String CHARSET = "UTF-8";

        private static final String AES_256_HASH_ALGORITHM = "SHA-256";

        //blank IV
        private final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        private Aes256()
        {
        }

        private SecretKeySpec generateKey(final String sPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException
        {
            SecretKeySpec secretKeySpec = null;

            if (sPassword != null)
            {
                final MessageDigest digest = MessageDigest.getInstance(AES_256_HASH_ALGORITHM);
                if (digest != null)
                {
                    byte[] bytes = sPassword.getBytes("UTF-8");
                    if (bytes != null)
                    {
                        digest.update(bytes, 0, bytes.length);
                        byte[] key = digest.digest();
                        if (key != null)
                        {
                            secretKeySpec = new SecretKeySpec(key, "AES");
                        }
                    }
                }
            }

            return secretKeySpec;
        }

        public String encrypt(final String sSecret, String sUnencryptedData) throws GeneralSecurityException
        {
            String encryptedData = null;

            if ((sSecret != null) && (sUnencryptedData != null))
            {
                try
                {
                    final SecretKeySpec key = generateKey(sSecret);
                    if (key != null)
                    {
                        final Cipher cipher = Cipher.getInstance(AES_MODE);
                        if (cipher != null)
                        {
                            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
                            if (ivSpec != null)
                            {
                                cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

                                byte[] cipherText = cipher.doFinal(sUnencryptedData.getBytes(CHARSET));
                                if (cipherText != null)
                                {
                                    encryptedData = Base64.encodeToString(cipherText, Base64.NO_WRAP);
                                }
                            }
                        }
                    }
                }
                catch (UnsupportedEncodingException e)
                {
                    throw new GeneralSecurityException(e);
                }
            }

            return encryptedData;
        }

        public String decrypt(final String sSecret, String sEncryptedData) throws GeneralSecurityException
        {
            String decryptedData = null;

            if ((sSecret != null) && (sEncryptedData != null))
            {
                try
                {
                    final SecretKeySpec key = generateKey(sSecret);
                    if (key != null)
                    {
                        byte[] decodedCipherText = Base64.decode(sEncryptedData, Base64.NO_WRAP);
                        if (decodedCipherText != null)
                        {
                            final Cipher cipher = Cipher.getInstance(AES_MODE);
                            if (cipher != null)
                            {
                                IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
                                if (ivSpec != null)
                                {
                                    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

                                    byte[] decryptedBytes = cipher.doFinal(decodedCipherText);
                                    if (decryptedBytes != null)
                                    {
                                        decryptedData = new String(decryptedBytes, CHARSET);
                                    }
                                }
                            }
                        }
                    }
                }
                catch (UnsupportedEncodingException e)
                {

                    throw new GeneralSecurityException(e);
                }
            }

            return decryptedData;
        }
    }

}
