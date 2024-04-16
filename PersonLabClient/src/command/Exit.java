package command;
/**
 * Команда 'exit'. Завершает выполнение.
 * @author Matvei Baranov
 */
public class Exit extends Command{
    public Exit(){
        super("exit","завершить программу (без сохранения в файл)");
    }
    @Override
    public boolean execute(String commandName,String parametr,boolean script) {
        if (parametr.isEmpty()){
            return true;
        }
        else
        {
            System.out.println("У этой команды не должно быть параметров!");
            return false;
        }
    }
}
