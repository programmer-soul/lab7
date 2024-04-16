package command;
/**
 * Абстрактная команда с именем и описанием
 * @author Matvei Baranov
 */
public abstract class Command implements Executable {
    private final String name;
    private final String description;
    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }
    /**
     * @return Название команды.
     */
    public String getName() {
        return name;
    }
    /**
     * @return Описание команды.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @return Название и описание команды.
     */
    @Override
    public String toString() {
        return name + " : " + description;
    }

}
