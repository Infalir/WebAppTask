package com.gavruseva.webapp.command.user;

public class AuthenticationParameters {
  public final static String LOGIN = "loginName";
  public final static String PASSWORD = "password";

  public final static String ERROR_MESSAGE = "errorMessage";
  public final static String ERROR_MESSAGE_TEXT = "Incorrect username or password, fill in all fields";
  public final static String AUTHENTICATION_ERROR_TEXT = "Incorrect login or password!";
  public final static String REGISTER_ERROR_MESSAGE_IF_EXIST = "Choose another username";
  public final static String REGISTER_ERROR = "errorRegister";
  public final static String COMMAND_WELCOME = "/controller?command=welcome";

  public final static String NAME_FOR_REGISTER = "newLoginName";
  public final static String PASSWORD_FOR_REGISTER = "newPassword";
}
