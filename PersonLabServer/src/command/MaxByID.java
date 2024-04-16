package command;
import dao.UserDAO;
import main.Persons;
import main.ResponseManager;
import main.ResponsePerson;
import main.SQLManager;

/**
 * Команда 'max_by_id'. Вывести объект из коллекции с максимальным id.
 * @author Matvei Baranov
 */
public class MaxByID extends Command{
    private final Persons persons;
    public MaxByID(Persons persons) {
        super("max_by_id", "вывести объект из коллекции с максимальным id");
        this.persons = persons;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        if (parametr.isEmpty()){
            return persons.MaxByID();
        }
        else
        {
            ResponseManager responsemanager= new ResponseManager();
            responsemanager.addResponse("У этой команды не должно быть параметров!");
            return responsemanager;
        }
    }
}
