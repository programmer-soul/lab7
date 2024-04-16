package command;
import main.Commands;
import main.Persons;
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
    public boolean execute(String commandName,String parametr,boolean script) {
        if (parametr.isEmpty()){
            commands.sendCommandAndReceiveResponse(commandName,parametr);
        }
        else
        {
            if (parametr.equals("onstart")){
                return commands.sendCommandAndReceiveResponse(commandName,"");
            }
            else
            {
                System.out.println("У этой команды не должно быть параметров!");
            }

        }
        return false;
    }
}
