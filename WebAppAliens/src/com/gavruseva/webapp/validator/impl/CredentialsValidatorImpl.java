package com.gavruseva.webapp.validator.impl;

import com.gavruseva.webapp.validator.CredentialsValidator;

public class CredentialsValidatorImpl implements CredentialsValidator {
  private static final String LOGIN_REGEX = "^[a-zA-Z0-9_-]+$";
  @Override
  public boolean isFullyValid(String login, String password) {
    return isLoginValid(login) && isPasswordValid(password);
  }
  @Override
  public boolean isLoginValid(String login){
    return !login.isEmpty() && login.matches(LOGIN_REGEX);
  }
  @Override
  public boolean isPasswordValid(String password){
    return !password.isEmpty();
  }
}
