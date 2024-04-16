package command;
import main.Person;
import main.Commands;
/**
 * Команда 'insert'. Добавить новый элемент с заданным ключом.
 * @author Matvei Baranov
 */
public class Insert extends Command{
    private final Commands commands;
    public Insert(Commands commands){
        super("insert {id} {element}","добавить новый элемент с заданным ключом");
        this.commands=commands;
    }
    @Override
    public boolean execute(String commandName,String parametr,boolean script) {
        if (!parametr.isEmpty()){
            if (Person.validateID(parametr)){
                commands.insert(Integer.parseInt(parametr),script,false,false,false);
            }
            else{
                System.out.println("Ошибка ID");
            }
        }
        else{
            if (!commands.insert(script)){
                System.out.println("Запись не создана!");
            }
        }

        return false;
    }
}
