package main;

import java.io.Serializable;
import java.util.Objects;
/**
 * Класс Ответ от сервера для обмена данными между клиентом и сервером
 * @author Matvei Baranov
 */
public class Response implements Serializable {
  private int value;
  private final String message;
  public Response(int value,String message) {
    this.value = value;
    this.message = message;
  }
  public Response(String message) {
    this.value = 0;
    this.message = message;
  }
  public String getMessage() {
    return message;
  }
  public int getValue() {
    return value;
  }
  public void setValue(int value){
     this.value =value;
  }
  public void show(){ System.out.println(message); }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Response response = (Response) o;
    return Objects.equals(message, response.message);
  }
  @Override
  public int hashCode() {
    return Objects.hash(message);
  }

  @Override
  public String toString() {
    return "Response{" + "message='" + message +  '\'' + "}";
  }
}
