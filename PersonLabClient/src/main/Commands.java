package main;
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
    //private int historysize=0;
    //** Индекс в истории */
    //private int historyindex=0;
    //** Открытый буфер для чтения файла скрипта */
    private BufferedReader scriptReader;
    //** Текущая строка скрипта */
    private String line;
    //** Массив строк для параметров добавления */
    public final String[] xmlStr =new String[11];
    //** Индекс в Массиве строк */
    private int xmlIndex;
    private UDPDatagramClient client;
    private String username;
    private String password;
    /**
     * Конструктор класса
     */
    public Commands() {
        persons = new Persons();
        commandCollection= new HashMap<>();
        commandCollection.put("help",new command.Help(this));
        commandCollection.put("info",new command.Info(this));
        commandCollection.put("show",new command.Show(this));
        commandCollection.put("history",new command.History(this));
        commandCollection.put("insert",new command.Insert(this));
        commandCollection.put("update",new command.Update(this));
        commandCollection.put("remove_key",new command.RemoveKey(this));
        commandCollection.put("clear",new command.Clear(this));
        commandCollection.put("execute_script",new command.ExecuteScript(this));
        commandCollection.put("exit",new command.Exit());
        commandCollection.put("remove_greater_key",new command.RemoveGreaterKey(this));
        commandCollection.put("replace_if_lower",new command.ReplaceIfLower(this));
        commandCollection.put("sum_of_height",new command.SumOfHeight(this));
        commandCollection.put("average_of_weight",new command.AverageOfWeight(this));
        commandCollection.put("max_by_id",new command.MaxByID(this));
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
    /*private void addToHistory(String S){
        if (historysize<14){
            historysize++;
        }
        historyStr[historyindex]=S;
        historyindex++;
        if (historyindex==14){
            historyindex=0;
        }
    }*/
    /**
     * Выдаёт на экран последние 14 выполненных команд
     */
    /*public void history(){
        if (historysize<14){
            for(int i=0;i<historysize;i++){
                System.out.println(historyStr[i]);
            }
        }
        else{
            for(int i=historyindex;i<14;i++){
                System.out.println(historyStr[i]);
            }
            for(int i=0;i<historyindex;i++){
                System.out.println(historyStr[i]);
            }

        }
    }*/
    /**
     * Cчитать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме
     * @param filename файл скрипта.
     * @return возращает Истина если была команда exit и надо выйти.
     */
    public boolean ExecuteScript(String filename){
        try{
            FileReader scriptfile = new FileReader(filename);
            scriptReader = new BufferedReader(scriptfile);
            line=scriptReader.readLine();
            while (line !=null)
            {
                System.out.println(line);
                if (execute(client,line,true)){
                    scriptfile.close();
                    return true;
                }
                line=scriptReader.readLine();
            }
            scriptReader.close();
            scriptfile.close();
            return false;
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    /**
     * @param usexml для загрузки XML файла.
     * @return следующая строка скрипта обработана
     */
    public boolean getScriptLine(boolean usexml){
        if (usexml){
            if (xmlIndex<10){
                line= xmlStr[xmlIndex++];
                return true;
            }
            else{
                line="";
                return false;
            }
        }
        else{
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
    }
    /**
     * @param script - вызывается из скрипта
     * @return возвращает признак, был ли добавлен элемент
     */
    public boolean insert(boolean script){
        return insert(0,script,false,false,false);
    }
    /**
     * @param id - уникальный номер (ключ) в коллекции
     * @param script - вызывается из скрипта
     * @param update - это обновление элемента
     * @param lower - replace_if_lower - заменить значение по ключу, если значение меньше заданного id
     * @param usexml - вызывается для загрузки XML
     * @return возвращает признак, был ли добавлен/изменён элемент с таким id
     */
    public boolean insert(Integer id,boolean script,boolean update,boolean lower,boolean usexml){
        String name;
        long height;
        Integer weight;
        String passportID;
        Color eyeColor;
        double X;
        int Y;
        double locationX;
        float locationY;
        double locationZ;
        xmlIndex=0;
        if (script || usexml){
            if (getScriptLine(usexml) && Person.validateName(line)){
                name=line;
            }
            else{
                System.out.println("Ошибка поля Имени");
                return false;
            }
            if (getScriptLine(usexml) && Coordinates.validateX(line)){
                X=Double.parseDouble(line);
            }
            else{
                System.out.println("Ошибка поля Координата X");
                return false;
            }
            if (getScriptLine(usexml) && Coordinates.validateY(line)){
                Y=Integer.parseInt(line);
            }
            else{
                System.out.println("Ошибка поля Координата Y");
                return false;
            }
            if (getScriptLine(usexml) && Person.validateHeight(line)){
                height=Long.parseLong(line);
            }
            else{
                System.out.println("Ошибка поля рост");
                return false;
            }
            if (getScriptLine(usexml) && Person.validateWeight(line)){
                weight=Integer.parseInt(line);
            }
            else{
                System.out.println("Ошибка поля вес");
                return false;
            }
            if (getScriptLine(usexml) && Person.validatePassportID(line)){
                passportID=line;
            }
            else{
                System.out.println("Ошибка поля Имени");
                return false;
            }
            if (getScriptLine(usexml) && Color.validateColor(line)){
                eyeColor = Color.valueOf(line);
            }
            else{
                System.out.println("Ошибка поля цвет глаз");
                return false;
            }
            if (getScriptLine(usexml) && Location.validateX(line)){
                locationX=Double.parseDouble(line);
            }
            else{
                System.out.println("Ошибка поля локация X");
                return false;
            }

            if (getScriptLine(usexml) && Location.validateY(line)){
                locationY=Float.parseFloat(line);
            }
            else{
                System.out.println("Ошибка поля локация X");
                return false;
            }
            if (getScriptLine(usexml) && Location.validateZ(line)){
                locationZ=Double.parseDouble(line);
            }
            else{
                System.out.println("Ошибка поля локация X");
                return false;
            }
        }
        else{
            name=Input.inputString("Введите имя:",false);
            X=Input.inputDouble("Введите координату X:",true);
            Y=Input.inputInt("Введите координату Y:",true,true);
            height=Input.inputLong("Введите рост:",false,false);
            weight=Input.inputInt("Введите вес:",false,false);
            passportID=Input.inputString("Введите серия-номер паспорта:",false);
            eyeColor=Input.inputColor("Введите цвет глаз (BLACK,BLUE,ORANGE,WHITE):");
            locationX=Input.inputDouble("Введите локацию X:",true);
            locationY=Input.inputFloat("Введите локацию Y:",true);
            locationZ=Input.inputDouble("Введите локацию Z:",true);
        }
        Coordinates coordinates=new Coordinates(X,Y);
        Location location=new Location(locationX,locationY,locationZ);
        ZonedDateTime creationDate = ZonedDateTime.now(ZoneOffset.systemDefault());
        if (usexml){
            DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
            creationDate = ZonedDateTime.parse(xmlStr[10],formatter);
            Person per= new Person(id,name,coordinates,creationDate,height,weight,passportID,eyeColor,location);
            persons.insert(per);
            return true;
        }

        ResponsePerson per= new ResponsePerson(id,name,coordinates,creationDate,height,weight,passportID,eyeColor,location);
        if (lower){
            sendCommandWithPersonAndReceiveResponsePerson("replace_if_lower","",per);
        }
        else{
            if (update){
                sendCommandWithPersonAndReceiveResponsePerson("update","",per);
            }else{
                sendCommandWithPersonAndReceiveResponsePerson("insert","",per);
            }
        }
        return true;
    }

    public boolean sendCommandAndReceiveResponse(String comstr,String parametr) {
        try {
            ResponseList response = (ResponseList) client.sendCommandAndReceiveResponse(new Request(comstr, parametr, this.username, this.password));
            response.show();
            if (Objects.equals(comstr, "authenticate")){
                if (response.getValue()==0){
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            //logger.info("Невозможно подключиться к серверу.", e);
            System.out.println("Невозможно подключиться к серверу!");
            return false;
        }
    }
    public boolean sendCommandAndReceiveResponsePerson(String comstr,String parametr) {
        try {
            ResponseList  response = (ResponseList ) client.sendCommandAndReceiveResponse(new Request(comstr, parametr, this.username, this.password));
            response.show();
            if (response.get(0).getValue()>0){
                ResponsePersonList personlist = (ResponsePersonList) client.ReceiveResponsePerson();
                personlist.show();
            }
            return true;
        } catch (IOException e) {
            //logger.info("Невозможно подключиться к серверу.", e);
            System.out.println("Невозможно подключиться к серверу!");
            return false;
        }
    }
    public boolean sendCommandWithPersonAndReceiveResponsePerson(String comstr,String parametr,ResponsePerson person) {
        try {
            ResponseList response = (ResponseList) client.sendCommandWithPersonAndReceiveResponse(new RequestPerson(comstr, parametr, person, this.username, this.password));
            response.show();
            if (response.get(0).getValue()>0){
                ResponsePersonList personlist = (ResponsePersonList) client.ReceiveResponsePerson();
                personlist.show();
            }
            return true;
        } catch (IOException e) {
            //logger.info("Невозможно подключиться к серверу.", e);
            System.out.println("Невозможно подключиться к серверу!");
            return false;
        }
    }
    /**
     * @param comstr команда для выполнения
     * @param script команда из скрипта
     * @return Найденный элемент (null если нен найден).
     */
    public boolean execute(UDPDatagramClient client, String comstr,boolean script){
        this.client=client;
        //addToHistory(comstr);
        String parametr="";
        int i=comstr.indexOf(' ');
        if (i>=0){
            parametr=comstr.substring(i+1);
            comstr=comstr.substring(0,i);
        }
        command.Command com= commandCollection.get(comstr);
        if (com == null) {
            System.out.println("Неверная команда");
            return false;
        }
        return com.execute(comstr,parametr,script);
    }

    public void setUserAuth(String username,String password){
        this.username=username;
        this.password=password;
    }
}
