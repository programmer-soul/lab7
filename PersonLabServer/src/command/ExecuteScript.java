package command;
import dao.UserDAO;
import main.Commands;
import main.ResponseManager;
import main.ResponsePerson;
import main.SQLManager;

/**
 * Команда 'execute_script'. Считать и исполнить скрипт из указанного файла.
 * @author Matvei Baranov
 */
public class ExecuteScript extends Command {
    private final Commands commands;
    public ExecuteScript(Commands commands){
        super("execute_script file_name","считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        this.commands=commands;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
            ResponseManager responsemanager= new ResponseManager();
            responsemanager.addResponse("Команда исполняется на клиенте!");
            return responsemanager;
    }
}
