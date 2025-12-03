package main.com.gavruseva.webapp.validator;

public interface CredentialsValidator {
  boolean isFullyValid(String login, String password);
  boolean isLoginValid(String login);
  boolean isPasswordValid(String password);
}
