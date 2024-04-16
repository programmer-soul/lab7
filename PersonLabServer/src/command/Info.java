package command;
import dao.UserDAO;
import main.Persons;
import main.ResponseManager;
import main.ResponsePerson;
import main.SQLManager;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 * @author Matvei Baranov
 */
public class Info extends Command{
    private final Persons persons;
    public Info(Persons persons) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.persons = persons;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        if (parametr.isEmpty()){
            return persons.info();
        }
        else
        {
            ResponseManager responsemanager= new ResponseManager();
            responsemanager.addResponse("У этой команды не должно быть параметров!");
            return responsemanager;
        }
    }
}
