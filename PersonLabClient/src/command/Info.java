package command;
import main.Commands;
import main.Persons;
/**
 * Команда 'info'. Выводит информацию о коллекции.
 * @author Matvei Baranov
 */
public class Info extends Command{
    private final Commands commands;
    public Info(Commands commands) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
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
