package command;
import main.Commands;
/**
 * Команда 'register'. Регистрирует пользователя на сервере
 * @author Matvei Baranov
 */
public class Register extends Command{
    private final Commands commands;
    public Register(Commands commands) {
        super("register", "Регистрирует пользователя на сервере");
        this.commands = commands;
    }
    @Override
    public boolean execute(String commandName,String parametr,boolean script) {
        if (parametr.isEmpty()){
           System.out.println("У этой команды должны быть параметры!");
        }
        else
        {
           commands.sendCommandAndReceiveResponse(commandName,parametr);
        }
        return false;
    }
}
