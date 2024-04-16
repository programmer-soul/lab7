package main;

import java.io.Serializable;
import java.util.Objects;
/**
 * Класс Запрос от клиента для обмена данными между клиентом и сервером
 * @author Matvei Baranov
 */
public class Request implements Serializable {
  private final String command;
  private final String parametr;
  private final String username;
  private final String password;

  public Request(String command,String parametr, String username, String password) {
    this.command = command;
    this.parametr = parametr;
    this.username = username;
    this.password = password;
  }

  public String getCommand() {
    return command;
  }
  public String getParametr() {
    return parametr;
  }
  public String getUsername() {
    return username;
  }
  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Request request = (Request) o;
    return Objects.equals(command, request.command) && Objects.equals(username, request.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(command, username, password);
  }

  @Override
  public String toString() {
    return "Request{" + "Command='" + command + '\'' +  ", username=" + username + "}";
  }
}
