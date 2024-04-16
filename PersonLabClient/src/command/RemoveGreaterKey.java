package command;
import main.Commands;
import main.Person;
import main.Persons;
/**
 * Команда 'remove_greater_key'. Удалить из коллекции все элементы, ключ которых превышаюет заданный.
 * @author Matvei Baranov
 */
public class RemoveGreaterKey extends Command{
    private final Commands commands;
    public RemoveGreaterKey(Commands commands) {
        super("remove_greater_key {id}","удалить из коллекции все элементы, ключ которых превышаюет заданный");
        this.commands = commands;
    }
    @Override
    public boolean execute(String commandName,String parametr,boolean script) {
        if (!parametr.isEmpty()){
            if (Person.validateID(parametr)){
                commands.sendCommandAndReceiveResponsePerson(commandName,parametr);
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
