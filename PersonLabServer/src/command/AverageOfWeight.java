package command;
import dao.UserDAO;
import main.Persons;
import main.ResponseManager;
import main.ResponsePerson;
import main.SQLManager;

/**
 * Команда 'average_of_weight'. Вывести среднее значение поля weight для всех элементов коллекции.
 * @author Matvei Baranov
 */
public class AverageOfWeight extends Command{
    private final Persons persons;
    public AverageOfWeight(Persons persons) {
        super("average_of_weight", "вывести среднее значение поля weight для всех элементов коллекции");
        this.persons = persons;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        ResponseManager responsemanager= new ResponseManager();
        if (parametr.isEmpty()){
            Long Avr=persons.AverageOfWeight();
            responsemanager.addResponse(Avr.intValue(),"Средний вес "+Avr);
        }
        else
        {
            responsemanager.addResponse("У этой команды не должно быть параметров!");
        }
        return responsemanager;
    }
}
