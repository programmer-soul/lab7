package command;
import dao.UserDAO;
import main.*;

/**
 * Команда 'remove_greater_key'. Удалить из коллекции все элементы, ключ которых превышаюет заданный.
 * @author Matvei Baranov
 */
public class RemoveGreaterKey extends Command{
    private final Persons persons;
    public RemoveGreaterKey(Persons persons) {
        super("remove_greater_key {id}","удалить из коллекции все элементы, ключ которых превышаюет заданный");
        this.persons = persons;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        if (!parametr.isEmpty()){
            if (Person.validateID(parametr)){
                return persons.RemoveGreaterKey(Integer.parseInt(parametr), user, sqlManager);
            }
            else{
                ResponseManager responsemanager= new ResponseManager();
                responsemanager.addResponse("Ошибка ID");
                return responsemanager;
            }
        }
        else{
            ResponseManager responsemanager= new ResponseManager();
            responsemanager.addResponse("У этой команды обязательный параметр ID!");
            return responsemanager;
        }
    }
}
