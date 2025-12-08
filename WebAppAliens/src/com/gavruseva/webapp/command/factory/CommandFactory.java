package com.gavruseva.webapp.command.factory;

import com.gavruseva.webapp.command.Command;
import com.gavruseva.webapp.command.WelcomeCommand;
import com.gavruseva.webapp.command.user.LoginCommand;
import com.gavruseva.webapp.command.user.RegisterNewUserCommand;
import com.gavruseva.webapp.command.user.SignOutCommand;

public class CommandFactory {
  public static Command create(String command) {
    command = command.toUpperCase();
    System.out.println(command);
    CommandType commandEnum = CommandType.valueOf(command);
    Command resultCommand;
    switch (commandEnum) {
      case LOGIN: {
        resultCommand = new LoginCommand();
        break;
      }
      case REGISTER_NEW_USER: {
        resultCommand = new RegisterNewUserCommand();
        break;
      }
      case SIGN_OUT: {
        resultCommand = new SignOutCommand();
        break;
      }
      case WELCOME: {
        resultCommand = new WelcomeCommand();
        break;
      }
      default: {
        throw new IllegalArgumentException("Invalid command");
      }
    }
    return resultCommand;
  }
}
