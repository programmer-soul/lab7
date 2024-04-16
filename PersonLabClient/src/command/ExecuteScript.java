package command;
import main.Commands;
/**
 * Команда 'execute_script'. Считать и исполнить скрипт из указанного файла.
 * @author Matvei Baranov
 */
public class ExecuteScript extends Command {
    private final Commands commands;
    public ExecuteScript(Commands commands){
        super("execute_script file_name","считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        this.commands=commands;
    }
    @Override
    public boolean execute(String commandName,String parametr,boolean script) {
        if (!parametr.isEmpty()){
            if (script){
                System.out.println("Скрипт уже исполняется");
            }
            else{
                return commands.ExecuteScript(parametr);
            }
        }
        else{
            System.out.println("У этой команды обязательный параметр имя файла!");
        }
        return false;
    }
}
