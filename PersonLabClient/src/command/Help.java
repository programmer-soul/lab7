package command;
import main.Commands;
/**
 * Команда 'help'. Выводит справку по доступным командам.
 * @author Matvei Baranov
 */
public class Help extends Command{
    private final Commands commands;
    public Help(Commands commands) {
        super("help", "вывести справку по доступным командам");
        this.commands = commands;
    }
    @Override
    public boolean execute(String commandName,String parametr,boolean script) {
        if (parametr.isEmpty()){
            commands.getCommands().values().forEach(command -> {
                System.out.println(command.toString());
            });
        }
        else
        {
            System.out.println("У этой команды не должно быть параметров!");
        }
        return false;
    }
}
