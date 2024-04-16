package command;
import main.Commands;
import main.Persons;
/**
 * Команда 'clear'. Очищает коллекцию.
 * @author Matvei Baranov
 */

public class Clear extends Command{
    private final Commands commands;
    public Clear(Commands commands) {
        super("clear", "очистить коллекцию");
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
