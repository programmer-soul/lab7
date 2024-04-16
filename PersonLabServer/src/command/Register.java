package command;
import dao.UserDAO;
import main.ResponseManager;
import main.ResponsePerson;
import main.Commands;
import main.SQLManager;

/**
 * Команда 'register'. Регистрирует пользователя на сервере
 * @author Matvei Baranov
 */

public class Register extends Command{
    private final Commands commands;
    public Register(Commands commands) {
        super("register", "Регистрирует пользователя на сервере");
        this.commands = commands;
    }
    @Override
    public ResponseManager execute(String commandName, String parametr, ResponsePerson person, UserDAO user, SQLManager sqlManager, boolean script) {
        ResponseManager responsemanager= new ResponseManager();
        if (parametr.isEmpty()){
            responsemanager.addResponse("У этой команды должно быть параметры!");
        }
        else
        {
            String[] pars = parametr.split(" ");
            if (pars.length!=2){
                responsemanager.addResponse("У этой команды должно быть 2 параметра!");
            }
            else{
                int id=sqlManager.addUser(pars[0],pars[1]);
                if (id>0){
                    responsemanager.addResponse(id,"Пользователь добавлен с id#"+id);
                }
                else{
                    responsemanager.addResponse("Ошибка добавления пользователя!");
                }
            }
        }
        return responsemanager;
    }
}
