package command;
/**
 * Интерфейс для всех выполняемых команд
 * @author Matvei Baranov
 */
public interface Executable {
    /**
     * Получить название
     * @return название
     */
    String getName();
    /**
     * Получить описание
     * @return описание
     */
    String getDescription();
    /**
     * Выполнить
     * @param commandName команда для выполнения
     * @param parametr параметр команды
     * @param script команда из скрипта*
     * @return результат выполнения
     */
    boolean execute(String commandName,String parametr,boolean script);
}
