package command;
import dao.UserDAO;
import main.*;

/**
 * Команда 'update'. Обновить значение элемента коллекции, id которого равен заданному.
 * @author Matvei Baranov
 */
public class Update extends Command{
    private final Persons persons;
    public Update(Persons persons){
        super("update {id} {element}","обновить значение элемента коллекции, id которого равен заданному");
        this.persons = persons;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        return persons.update(person.toPerson(),user, sqlManager);
    }
}
