package jp.co.nri.point.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncryptPasswordUtil {

    public static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encrypt(String plainText) {
        String encryptedPassword = passwordEncoder.encode(plainText);
        return encryptedPassword;
    }

    public static boolean passwordsMatch(String plainPassword, String encryptedPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(plainPassword, encryptedPassword);
    }

    public static void main(String[] args) {
        System.out.println(encrypt("password"));
        System.out.println(encrypt("12345678"));
        System.out.println(encrypt("sinocom11#"));

    }

}
