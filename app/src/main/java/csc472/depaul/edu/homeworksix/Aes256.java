package csc472.depaul.edu.homeworksix;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class Aes256
{
    private static final String AES_MODE = "AES/CBC/PKCS7Padding";
    private static final String CHARSET = "UTF-8";

    private static final String AES_256_HASH_ALGORITHM = "SHA-256";

    //blank IV
    private static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    private Aes256()
    {
    }

    private static SecretKeySpec generateKey(final String password) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        SecretKeySpec secretKeySpec = null;

        final MessageDigest digest = MessageDigest.getInstance(AES_256_HASH_ALGORITHM);
        if (digest != null)
        {
            final byte[] bytes = password.getBytes("UTF-8");
            if (bytes != null)
            {
                digest.update(bytes, 0, bytes.length);
                final byte[] key = digest.digest();
                if (key != null)
                {
                    secretKeySpec = new SecretKeySpec(key, "AES");
                }
            }
        }

        return secretKeySpec;
    }

    public String encrypt(final String password, String message) throws GeneralSecurityException
    {
        String encryptedData = "";

        try
        {
            final SecretKeySpec key = generateKey(password);
            if (key != null)
            {
                final Cipher cipher = Cipher.getInstance(AES_MODE);
                if (cipher != null)
                {
                    final IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
                    if (ivSpec != null)
                    {
                        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

                        final byte[] cipherText = cipher.doFinal(message.getBytes(CHARSET));
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

        return encryptedData;
    }

    public String decrypt(final String sSecret, String base64EncodedCipherText) throws GeneralSecurityException
    {
        String decryptedData = "";

        try
        {
            final SecretKeySpec key = generateKey(sSecret);
            if (key != null)
            {
                final byte[] decodedCipherText = Base64.decode(base64EncodedCipherText, Base64.NO_WRAP);
                if (decodedCipherText != null)
                {
                    final Cipher cipher = Cipher.getInstance(AES_MODE);
                    if (cipher != null)
                    {
                        final IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
                        if (ivSpec != null)
                        {
                            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

                            final byte[] decryptedBytes = cipher.doFinal(decodedCipherText);
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

        return decryptedData;
    }
}
