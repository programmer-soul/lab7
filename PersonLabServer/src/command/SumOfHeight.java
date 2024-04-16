package command;
import dao.UserDAO;
import main.Persons;
import main.ResponseManager;
import main.ResponsePerson;
import main.SQLManager;

/**
 * Команда 'sum_of_height'. Вывести сумму значения поля height для всех элементов коллекции.
 * @author Matvei Baranov
 */
public class SumOfHeight extends Command{
    private final Persons persons;
    public SumOfHeight(Persons persons) {
        super("sum_of_height", "вывести сумму значения поля height для всех элементов коллекции");
        this.persons = persons;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        ResponseManager responsemanager= new ResponseManager();
        if (parametr.isEmpty()){
            Long Sum = persons.SumOfHeight();
            responsemanager.addResponse(Sum.intValue(),"Сумма роста всех людей "+Sum);
        }
        else
        {
            responsemanager.addResponse("У этой команды не должно быть параметров!");
        }
        return responsemanager;
    }
}
