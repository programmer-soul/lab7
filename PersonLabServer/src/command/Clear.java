package command;
import dao.UserDAO;
import main.Persons;
import main.ResponseManager;
import main.ResponsePerson;
import main.SQLManager;

/**
 * Команда 'clear'. Очищает коллекцию.
 * @author Matvei Baranov
 */

public class Clear extends Command{
    private final Persons persons;
    public Clear(Persons persons) {
        super("clear", "очистить коллекцию");
        this.persons = persons;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        ResponseManager responsemanager= new ResponseManager();
        if (parametr.isEmpty()){
            persons.clear(user, sqlManager);
            responsemanager.addResponse("Коллекция очищена");
        }
        else
        {
            responsemanager.addResponse("У этой команды не должно быть параметров!");
        }
        return responsemanager;
    }
}
