package main;
import java.io.Serializable;
/**
 * Класс Запрос от клиента со структурой Person для обмена данными между клиентом и сервером
 * @author Matvei Baranov
 */
public class RequestPerson extends Request {
    public ResponsePerson person;
    public RequestPerson(String command,String parametr, ResponsePerson person, String username, String password) {
        super(command,parametr,username,password);
        this.person=person;
    }
    public Person toPerson(){
        return person.toPerson();
    }
    public void show(){
	person.show();
    }
}
