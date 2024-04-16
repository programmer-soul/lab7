package command;
import dao.UserDAO;
import main.*;

/**
 * Команда 'remove_key'. Удаляет элемент из коллекции по id.
 * @author Matvei Baranov
 */
public class RemoveKey extends Command{
    private final Persons persons;
    public RemoveKey(Persons persons) {
        super("remove_key {id}","удалить элемент из коллекции по его ключу");
        this.persons = persons;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        ResponseManager responsemanager= new ResponseManager();
        if (!parametr.isEmpty()){
            if (Person.validateID(parametr)){
                if (persons.remove(Integer.parseInt(parametr),user,sqlManager)){
                    responsemanager.addResponse("Успешно удален элемент "+parametr);
                }
                else{
                    responsemanager.addResponse("Ошибка удаления элемента "+parametr);
                }
            }
            else{
                responsemanager.addResponse("Ошибка ID");
            }
        }
        else{
            responsemanager.addResponse("У этой команды обязательный параметр ID!");
        }
        return responsemanager;
    }
}
