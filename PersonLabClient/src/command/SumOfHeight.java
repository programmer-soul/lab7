package command;
import main.Commands;
import main.Persons;
/**
 * Команда 'sum_of_height'. Вывести сумму значения поля height для всех элементов коллекции.
 * @author Matvei Baranov
 */
public class SumOfHeight extends Command{
    private final Commands commands;
    public SumOfHeight(Commands commands) {
        super("sum_of_height", "вывести сумму значения поля height для всех элементов коллекции");
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
