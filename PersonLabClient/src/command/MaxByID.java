package command;
import main.Commands;
import main.Persons;
/**
 * Команда 'max_by_id'. Вывести объект из коллекции с максимальным id.
 * @author Matvei Baranov
 */
public class MaxByID extends Command{
    private final Commands commands;
    public MaxByID(Commands commands) {
        super("max_by_id", "вывести объект из коллекции с максимальным id");
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
