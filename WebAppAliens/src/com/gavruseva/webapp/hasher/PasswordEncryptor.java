package com.gavruseva.webapp.hasher;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptor {
  private static final int WORKLOAD = 13;

  public static String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(WORKLOAD));
  }

  public static boolean verifyPassword(String plainPassword, String storedHash) {
    try {
      return BCrypt.checkpw(plainPassword, storedHash);
    } catch (Exception e) {
      return false;
    }
  }
}
