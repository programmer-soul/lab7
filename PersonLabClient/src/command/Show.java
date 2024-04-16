package command;
import main.Commands;
import main.Persons;
/**
 * Команда 'show'. Выводит все элементы коллекции.
 * @author Matvei Baranov
 */
public class Show extends Command{
    private final Commands commands;
    public Show(Commands commands) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.commands = commands;
    }
    @Override
    public boolean execute(String commandName,String parametr,boolean script) {
        if (parametr.isEmpty()){
            commands.sendCommandAndReceiveResponsePerson(commandName,parametr);
        }
        else
        {
            System.out.println("У этой команды не должно быть параметров!");
        }
        return false;
    }
}
