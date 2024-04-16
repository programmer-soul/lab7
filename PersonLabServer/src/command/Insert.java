package command;
import dao.UserDAO;
import main.*;

/**
 * Команда 'insert'. Добавить новый элемент с заданным ключом.
 * @author Matvei Baranov
 */
public class Insert extends Command{
    private final Persons persons;

    public Insert(Persons persons){
        super("insert {id} {element}","добавить новый элемент с заданным ключом");
        this.persons = persons;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        return persons.insert(person.toPerson(),user, sqlManager);
    }
}
