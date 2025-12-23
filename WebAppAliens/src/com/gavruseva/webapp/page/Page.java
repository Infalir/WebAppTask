package com.gavruseva.webapp.page;

public enum Page {
  LOGIN_PAGE("/WEB-INF/views/login.jsp"),
  REGISTER_PAGE("/WEB-INF/views/register.jsp"),
  WELCOME_PAGE("/WEB-INF/views/welcome.jsp"),
  ERROR_PAGE("/WEB-INF/views/errorpage.jsp"),
  ORDER_PAGE("/WEB-INF/views/order.jsp"),
  MAIN_PAGE("/WEB-INF/views/main.jsp");
  private final String value;

  Page(String value) {
    this.value = value;
  }

  public String getPage() {
    return value;
  }
}
