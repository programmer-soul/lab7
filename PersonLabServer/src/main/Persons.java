package main;

import dao.UserDAO;

import java.util.*;
import java.time.*;
import javax.xml.stream.*;
/**
 * Класс оперирует коллекцией людей
 * @author Matvei Baranov
 */
public class Persons {
    //** Коллекция */
    private final LinkedHashMap<Integer,Person> person_collection;
    //** Дата/время инициализации */
    private final ZonedDateTime InitDateTime;
    //** Дата/время последнего сохранения */
    private ZonedDateTime lastSaveDateTime;
    //** Дата/время последней загрузки */
    private ZonedDateTime lastLoadDateTime;
    /**
     * Конструктор класса
     */
    Persons(){
        person_collection=new LinkedHashMap<>();
        InitDateTime = ZonedDateTime.now(ZoneOffset.systemDefault());
    }
    /**
     * вывести в стандартный поток вывода все элементы коллекции в строковом представлении
     */
    public synchronized ResponseManager show(){
        ResponseManager responsemanager= new ResponseManager();
        Set<Map.Entry<Integer,Person>> set = person_collection.entrySet();// Получаем набор элементов
        for (Map.Entry<Integer, Person> me : set) {
            Person per = me.getValue();
            responsemanager.addResponsePerson(per);
        }
        if(responsemanager.fullsize()==0){
            responsemanager.addResponse("В коллекции нет элементов!");
        }
        else{
            responsemanager.sort();
            responsemanager.addResponse(responsemanager.sizePerson(),"В коллекции "+responsemanager.sizePerson()+" элемента(ов)");
        }
        return responsemanager;
    }
    /**
     * вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
     */
    public ResponseManager info(){
        ResponseManager responsemanager= new ResponseManager();
        responsemanager.addResponse("Сведения о коллекции:");
        responsemanager.addResponse("  Тип коллекции: " + person_collection.getClass().getName());
        responsemanager.addResponse("  Количество элементов: " + person_collection.size());
        responsemanager.addResponse("  Дата/время последнего сохранения: " + lastSaveDateTime);
        responsemanager.addResponse("  Дата/время последней загрузки: " + lastLoadDateTime);
        responsemanager.addResponse("  Дата/время инициализации: " + InitDateTime);
        return responsemanager;
    }
    /**
     * Сохраняет коллекцию в открытый стрим в формате XML
     * @param out открытый стрим для записи
     */
    public boolean saveXML(XMLStreamWriter out){
        try {
            lastSaveDateTime = ZonedDateTime.now(ZoneOffset.systemDefault());
            Set<Map.Entry<Integer, Person>> set = person_collection.entrySet();// Получаем набор элементов
            for (Map.Entry<Integer, Person> me : set) {
                Person per = me.getValue();
                if (!per.saveXML(out)) return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * вывести сумму значения поля height для всех элементов коллекции
     */
    public synchronized long SumOfHeight(){
        Set<Map.Entry<Integer, Person>> set = person_collection.entrySet();// Получаем набор элементов
        Iterator<Map.Entry<Integer, Person>> iter = set.iterator();// Получаем итератор
        long totalheight=0;
        while(iter.hasNext()) {
            Map.Entry<Integer, Person> me = iter.next();
            Person per=me.getValue();
            totalheight+=per.getHeight();
        }
        return totalheight;
    }
    /**
     * вернуть среднее значение поля weight для всех элементов коллекции
     */
    public synchronized long AverageOfWeight(){
        Set<Map.Entry<Integer,Person>> set = person_collection.entrySet();// Получаем набор элементов
        Iterator<Map.Entry<Integer,Person>> iter = set.iterator();// Получаем итератор
        long totalweight=0;
        while(iter.hasNext()) {
            Map.Entry<Integer,Person> me = iter.next();
            Person per=me.getValue();
            totalweight+=per.getWeight();
        }
        if (!person_collection.isEmpty()) {
            totalweight /= person_collection.size();
        }
        return totalweight;
    }
    /**
     * @return максимальный id в коллекции
     */
    public synchronized Integer getMaxID(){
        Integer id=0;
        if (!person_collection.isEmpty()) {
            Set<Integer> setkeys = person_collection.keySet();
            Integer[] keys = setkeys.toArray(new Integer[0]);
            id = Arrays.stream(keys).max(Integer::compare).get();
        }
        return id;
    }
    /**
     * Добавить элемент коллекции
     */
    public synchronized ResponseManager insert(Person per, UserDAO user, SQLManager sqlManager){
        var newId = sqlManager.addPerson(per, user);
        ResponseManager responsemanager= new ResponseManager();
        if (newId>0){
            responsemanager.addResponse(1,"Успешно добавлен элемент "+per.getID());
            int bsize=person_collection.size();
            per.setID(newId);
            person_collection.put(newId,per);
            Person newperson=person_collection.get(newId);
            responsemanager.addResponsePerson(newperson);
        }
        else{
            responsemanager.addResponse(0,"Ошибка добавления!");
        }
        return responsemanager;
    }
    public synchronized void load(SQLManager sqlManager) {
        //logger.info("Загрузка начата...");
        startLoad();
        var pers = sqlManager.loadPersons();
        for (int i = 0; i < pers.size(); i++) {
            Person per=pers.get(i).toPerson();
            person_collection.put(per.getID(),per);
        }
        //logger.info("Загрузка завершена!");
    }
    /**
     * Обновить элемент коллекции
     */
    public synchronized ResponseManager update(Person per, UserDAO user, SQLManager sqlManager){
        ResponseManager responsemanager= new ResponseManager();
        if (sqlManager.updatePerson(per, user)){
            person_collection.put(per.getID(),per);
            Person newperson=person_collection.get(per.getID());
            responsemanager.addResponsePerson(newperson);
            responsemanager.addResponse(1,"Элемент обновлен "+per.getID());
        }
        else{
            responsemanager.addResponse(0,"Элемент не обновлен "+per.getID());
        }
        return responsemanager;
    }
    /**
     * Заменить значение по ключу, если значение меньше заданного id
     */
    public synchronized ResponseManager replace_if_lower(Person person, UserDAO user, SQLManager sqlManager){
        ResponseManager responsemanager= new ResponseManager();
        if (!person_collection.isEmpty()) {
            Set<Integer> setkeys = person_collection.keySet();
            Integer[] keys = setkeys.toArray(new Integer[0]);
            for (Integer key : keys) {
                if (key < person.getID()) {
                    Person per = new Person(key, person.getName(),new Coordinates(person.getCoordX(),person.getCoordY()), person.getCreationDate(), person.getHeight(), person.getWeight(), person.getPassportID(), person.getEyeColor(), new Location(person.getX(),person.getY(),person.getZ()));
                    ResponseManager responsemanagerUPD=update(per,user,sqlManager);
                    if (responsemanagerUPD.getValue()==1){
                        responsemanager.addList(responsemanagerUPD);
                        person_collection.put(per.getID(), per);
                        responsemanager.addResponsePerson(per);
                    }
                }
            }
        }
        if(responsemanager.sizePerson()==0){
            responsemanager.addResponse("Нет элементов для обновления!");
        }
        else{
            responsemanager.sort();
            responsemanager.addResponse(responsemanager.sizePerson(),"Обновлено "+responsemanager.sizePerson()+" элемента(ов)");
        }
        return responsemanager;
    }
    /**
     * Очищает коллекцию
     */
    public synchronized void clear(UserDAO user, SQLManager sqlManager){
        if (sqlManager.clearPerson(user)) {
            person_collection.clear();
            var pers = sqlManager.loadPersons();
            for (int i = 0; i < pers.size(); i++) {
                Person per=pers.get(i).toPerson();
                person_collection.put(per.getID(),per);
            }
        }
    }
    /**
     * Фиксируем время начала загрузки XML
     */
    public void startLoad() {
        lastLoadDateTime = ZonedDateTime.now(ZoneOffset.systemDefault());
    }
    /**
     * Удалить из коллекции все элементы, ключ которых превышаюет заданный
     */
    public synchronized ResponseManager RemoveGreaterKey(Integer id, UserDAO user, SQLManager sqlManager){
        ResponseManager responsemanager= new ResponseManager();
        if (!person_collection.isEmpty()) {
            Set<Integer> setkeys = person_collection.keySet();
            Integer[] keys = setkeys.toArray(new Integer[0]);
            for (Integer key : keys) {
                if (key > id) {
                    if (remove(key, user, sqlManager)){
                        responsemanager.addResponsePerson(person_collection.get(key));
                        person_collection.remove(key);
                    }
                }
            }
        }
        if(responsemanager.sizePerson()==0){
            responsemanager.addResponse("Нет элементов для удаления!");
        }
        else{
            responsemanager.sort();
            responsemanager.addResponse(responsemanager.sizePerson(),"Удалено "+responsemanager.sizePerson()+" элемента(ов)");
        }

        return responsemanager;
    }
    /**
     * Вывести объект из коллекции с максимальным id
     */
    public synchronized ResponseManager MaxByID(){
        ResponseManager responsemanager= new ResponseManager();
        Integer maxID=getMaxID();
        if (maxID>0){
            Person per=person_collection.get(maxID);
            responsemanager.addResponsePerson(per);
        }
        if(responsemanager.fullsize()==0){
            responsemanager.addResponse("В коллекции нет элементов!");
        }
        else{
            responsemanager.sort();
            responsemanager.addResponse(responsemanager.sizePerson(),"Максимальным id "+maxID);
        }
        return responsemanager;
    }
    /**
     * Удалить объект из коллекции с заданным id
     */
    public synchronized boolean remove(int id, UserDAO user, SQLManager sqlManager){
        if (sqlManager.removePerson(id,user)){
            person_collection.remove(id);
            return true;
        }
        else{
            return false;
        }
    }
}
