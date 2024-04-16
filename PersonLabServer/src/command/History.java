package command;
import dao.UserDAO;
import main.Commands;
import main.ResponseManager;
import main.ResponsePerson;
import main.SQLManager;

/**
 * Команда 'history'. Вывести последние введённые 14 команд.
 * @author Matvei Baranov
 */
public class History extends Command{
    private final Commands commands;
    public History(Commands commands) {
        super("history", "вывести последние введённые 14 команд");
        this.commands = commands;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        if (parametr.isEmpty()){
            return commands.history();
        }
        else
        {
            ResponseManager responsemanager= new ResponseManager();
            responsemanager.addResponse("У этой команды не должно быть параметров!");
            return responsemanager;
        }
    }
}
