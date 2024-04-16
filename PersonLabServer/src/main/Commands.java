package main;
import command.Authenticate;
import dao.PersonDAO;
import dao.UserDAO;
import org.apache.commons.lang3.ObjectUtils;

import java.io.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Класс оперирует коллекцией команд, находит и исполняет команду
 * исполняет скрипт, хранит и показывает историю команд
 * @author Matvei Baranov
 */
public class Commands {
    //** Класс Люди */
    Persons persons;
    //** Коллекция команд */
    private final HashMap<String,command.Command> commandCollection;
    //** Массив строк для истории */
    private final String[] historyStr=new String[14];
    //** Текущий размер истории */
    private int historySize=0;
    //** Индекс в истории */
    private int historyIndex=0;
    //** Открытый буфер для чтения файла скрипта */
    private BufferedReader scriptReader;
    //** Текущая строка скрипта */
    private String line;
    //** Массив строк для параметров добавления */
    private boolean loading=true;
    /**
     * Конструктор класса
     */
    public Commands() {
        persons = new Persons();
        commandCollection= new HashMap<>();
        commandCollection.put("help",new command.Help(this));
        commandCollection.put("info",new command.Info(persons));
        commandCollection.put("show",new command.Show(persons));
        commandCollection.put("history",new command.History(this));
        commandCollection.put("insert",new command.Insert(persons));
        commandCollection.put("update",new command.Update(persons));
        commandCollection.put("remove_key",new command.RemoveKey(persons));
        commandCollection.put("clear",new command.Clear(persons));
        commandCollection.put("execute_script",new command.ExecuteScript(this));
        commandCollection.put("exit",new command.Exit());
        commandCollection.put("remove_greater_key",new command.RemoveGreaterKey(persons));
        commandCollection.put("replace_if_lower",new command.ReplaceIfLower(this,persons));
        commandCollection.put("sum_of_height",new command.SumOfHeight(persons));
        commandCollection.put("average_of_weight",new command.AverageOfWeight(persons));
        commandCollection.put("max_by_id",new command.MaxByID(persons));
        commandCollection.put("authenticate",new command.Authenticate(this));
        commandCollection.put("register",new command.Register(this));
    }
    /**
     * Возвращает словарь команд
     * @return Словарь команд.
     */
    public Map<String,command.Command> getCommands() {
        return commandCollection;
    }
    /**
     * Добавляет команду в историю
     * @param S команда.
     */
    private void addToHistory(String S){
        if (historySize<14){
            historySize++;
        }
        historyStr[historyIndex]=S;
        historyIndex++;
        if (historyIndex==14){
            historyIndex=0;
        }
    }
    /**
     * Выдаёт на экран последние 14 выполненных команд
     */
    public ResponseManager history(){
        ResponseManager responsemanager= new ResponseManager();
        if (historySize<14){
            for(int i=0;i<historySize;i++){
                responsemanager.addResponse(historyStr[i]);
            }
        }
        else{
            for(int i=historyIndex;i<14;i++){
                responsemanager.addResponse(historyStr[i]);
            }
            for(int i=0;i<historyIndex;i++){
                responsemanager.addResponse(historyStr[i]);
            }
        }
        if (responsemanager.size()==0){
            responsemanager.addResponse("Нет истории команд");
        }
        return responsemanager;
    }
    /**
     * @param usexml для загрузки XML файла.
     * @return следующая строка скрипта обработана
     */
    public boolean getScriptLine(boolean usexml){
            try{
                if (line !=null){
                    line=scriptReader.readLine();
                    System.out.println(line);
                    return true;
                }
                else{
                    return false;
                }
            }
            catch(IOException ex) {
                System.out.println(ex.getMessage());
                return false;
            }
    }

    public void load(SQLManager sqlManager) {
        if (loading) {
            persons.load(sqlManager);
            loading=false;
        }
    }

    /**
     * @param comstr команда для выполнения
     * @param parametr параметр команды
     * @param script команда из скрипта
     * @return Результат исполнения.
     */
    public ResponseManager execute(String comstr,String parametr,ResponsePerson person, UserDAO user, SQLManager sqlManager,boolean script){
        addToHistory(comstr);
        if (user.getId()==0 && !Objects.equals(comstr, "authenticate")&& !Objects.equals(comstr, "register")) {
            ResponseManager responsemanager = new ResponseManager();
            responsemanager.addResponse("Команда без авторизации");
            return responsemanager;
        }
        command.Command com = commandCollection.get(comstr);
        if (com == null) {
            ResponseManager responsemanager = new ResponseManager();
            responsemanager.addResponse("Неверная команда");
            return responsemanager;
        }
        return com.execute(comstr,parametr,person,user,sqlManager,script);
    }
    //public command.Save getSaveCommmand(){
    //    return (command.Save)commandCollection.get("save");
    //}

    public ResponseManager AuthenticateUser(UserDAO user,SQLManager sqlManager){
        ResponseManager responsemanager=sqlManager.authenticateCommand(user.getName(), user.getPassword());
        int userid=responsemanager.getValue();
        if (userid>0){
            user.setId(userid);
        }
        return responsemanager;
    }
}
