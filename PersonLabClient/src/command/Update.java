package command;
import main.Person;
import main.Commands;
/**
 * Команда 'update'. Обновить значение элемента коллекции, id которого равен заданному.
 * @author Matvei Baranov
 */
public class Update extends Command{
    private final Commands commands;
    public Update(Commands commands){
        super("update {id} {element}","обновить значение элемента коллекции, id которого равен заданному");
        this.commands=commands;
    }
    @Override
    public boolean execute(String commandName,String parametr,boolean script) {
        if (!parametr.isEmpty()){
            if (Person.validateID(parametr)){
                commands.insert(Integer.parseInt(parametr),script,true,false,false);
            }
            else{
                System.out.println("Ошибка ID");
            }
        }
        else{
           System.out.println("У этой команды обязательный параметр ID!");
        }
        return false;
    }
}
