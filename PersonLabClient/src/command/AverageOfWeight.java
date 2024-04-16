package command;
import main.Commands;
import main.Persons;
/**
 * Команда 'average_of_weight'. Вывести среднее значение поля weight для всех элементов коллекции.
 * @author Matvei Baranov
 */
public class AverageOfWeight extends Command{
    private final Commands commands;
    public AverageOfWeight(Commands commands) {
        super("average_of_weight", "вывести среднее значение поля weight для всех элементов коллекции");
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
