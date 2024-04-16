package command;
import dao.UserDAO;
import main.ResponseManager;
import main.ResponsePerson;
import main.Commands;
import main.SQLManager;

/**
 * Команда 'authenticate'. Аутентифицирует пользователя по логину и паролю.
 * @author Matvei Baranov
 */

public class Authenticate extends Command{
    private final Commands commands;
    public Authenticate(Commands commands) {
        super("authenticate", "Aутентифицировать пользователя");
        this.commands = commands;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        if (parametr.isEmpty()){
            return commands.AuthenticateUser(user, sqlManager);
        }
        else
        {
            ResponseManager responsemanager= new ResponseManager();
            responsemanager.addResponse("У этой команды не должно быть параметров!");
            return responsemanager;
        }
    }
}
