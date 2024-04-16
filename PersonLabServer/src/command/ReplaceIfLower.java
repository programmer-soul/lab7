package command;
import dao.UserDAO;
import main.*;

/**
 * Команда 'replace_if_lower'. Заменить значение по ключу, если значение меньше заданного.
 * @author Matvei Baranov
 */
public class ReplaceIfLower extends Command{
    private final Commands commands;
    private final Persons persons;
    public ReplaceIfLower(Commands commands,Persons persons){
        super("replace_if_lower {id} {element}","заменить значение по ключу, если значение меньше заданного");
        this.commands = commands;
        this.persons = persons;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        return persons.replace_if_lower(person.toPerson(), user, sqlManager);
    }
}
