package command;
import dao.UserDAO;
import main.Persons;
import main.ResponseManager;
import main.ResponsePerson;
import main.SQLManager;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 * @author Matvei Baranov
 */
public class Show extends Command{
    private final Persons persons;
    public Show(Persons persons) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.persons = persons;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        if (parametr.isEmpty()){
            return persons.show();
        }
        else
        {
            ResponseManager responsemanager= new ResponseManager();
            responsemanager.addResponse("У этой команды не должно быть параметров!");
            return responsemanager;
        }
    }
}
