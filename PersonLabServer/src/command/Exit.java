package command;
import dao.UserDAO;
import main.ResponseManager;
import main.ResponsePerson;
import main.SQLManager;

/**
 * Команда 'exit'. Завершает выполнение.
 * @author Matvei Baranov
 */
public class Exit extends Command{
    public Exit(){
        super("exit","завершить программу (без сохранения в файл)");
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        ResponseManager responsemanager= new ResponseManager();
        responsemanager.addResponse("Команда исполняется на клиенте!");
        return responsemanager;
    }
}
