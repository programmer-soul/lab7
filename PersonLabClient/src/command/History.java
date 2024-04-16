package command;
import main.Commands;
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
    public boolean execute(String commandName,String parametr,boolean script) {
        if (parametr.isEmpty()){
            commands.sendCommandAndReceiveResponse(commandName,parametr);
        }
        else
        {
            System.out.println("У этой команды не должно быть параметров!");
        }
        return false;
    }
}
