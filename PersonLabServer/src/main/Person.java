package main;

import javax.xml.stream.XMLStreamWriter;
import java.time.*;
import javax.xml.stream.*;
/**
 * Класс Человек
 * @author Matvei Baranov
 */
public class Person implements Comparable<Person> {
    private int id;
    private String name;
    private Coordinates coordinates;
    private java.time.ZonedDateTime creationDate;
    private long height;
    private Integer weight;
    private String passportID;
    private Color eyeColor;
    private Location location;
    public java.time.ZonedDateTime getCreationDate(){
        return creationDate;
    }

    public Color getEyeColor(){
        return eyeColor;
    }

    /**
     * @return возвращает координату x
     */
    public double getCoordX(){
        return coordinates.getX();
    }
    /**
     * @return возвращает координату y
     */
    public Integer getCoordY(){
        return coordinates.getY();
    }
    /**
     * @return возвращает x
     */
    public double getX(){
        return location.getX();
    }
    /**
     * @return возвращает y
     */
    public Float getY(){
        return location.getY();
    }
    /**
     * @return возвращает z
     */
    public double getZ(){
        return location.getZ();
    }
    /**
     * Конструктор класса
     */
    public Person(int id,String name,Coordinates coordinates,ZonedDateTime creationDate,long height,Integer weight,String passportID,Color eyeColor,Location location){
        this.id=id;
        this.name=name;
        this.coordinates=coordinates;
        this.creationDate=creationDate;
        this.height=height;
        this.weight=weight;
        this.passportID=passportID;
        this.eyeColor=eyeColor;
        this.location=location;
    }
    public ResponsePerson toResponse(){
        return (new ResponsePerson(id,name,coordinates,creationDate,height,weight,passportID,eyeColor,location));
    }

    /**
     * Сохраняет свой данные в открытый стрим в формате XML
     * @param out открытый стрим для записи
     */
    public boolean saveXML(XMLStreamWriter out){
        try {
            out.writeStartElement("Person");

            out.writeStartElement("id");
            out.writeCharacters(Integer.toString(id));
            out.writeEndElement();

            out.writeStartElement("name");
            out.writeCharacters(name);
            out.writeEndElement();

            if (!coordinates.SaveXML(out)) return false;

            out.writeStartElement("creationDate");
            out.writeCharacters(creationDate.toString());
            out.writeEndElement();

            out.writeStartElement("height");
            out.writeCharacters(Long.toString(height));
            out.writeEndElement();

            out.writeStartElement("weight");
            out.writeCharacters(Integer.toString(weight));
            out.writeEndElement();

            out.writeStartElement("passportID");
            out.writeCharacters(passportID);
            out.writeEndElement();

            out.writeStartElement("eyeColor");
            out.writeCharacters(eyeColor.toString());
            out.writeEndElement();

            if (!location.SaveXML(out)) return false;

            out.writeEndElement();
            out.writeCharacters(System.getProperty("line.separator"));
            return true;
        }
        catch (XMLStreamException e) {
            return false;
        }
    }
    /**
     * @return возвращает id
     */
    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id=id;
    }
    /**
     * @return возвращает имя
     */
    public String getName() {
        return name;
    }
    /**
     * @return возвращает имя
     */
    public String getPassportID() {
        return passportID;
    }
    /**
     * @return возвращает Рост
     */
    public long getHeight() {
        return height;
    }
    /**
     * @return возвращает Вес
     */
    public Integer getWeight() {
        return weight;
    }
    /**
     * @return разницу текущего id и сравниваемого id для сортировки по id
     */
    public int compareTo(Person per) {
        return this.id - per.id; //сортируем по возрастанию
    }
    /**
     * @param S строка для проверки
     * @return Проверка строки на валидность ФИО.
     */
    public static boolean validateName(String S){
        if (S.isEmpty()) {
            return false;
        } else{
            return true;
        }
    }
    /**
     * @param S строка для проверки
     * @return Проверка строки на валидность Номера Паспорта.
     */
    public static boolean validatePassportID(String S){
        if (S.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }
    /**
     * @param S строка для проверки
     * @return Проверка строки на валидность Роста.
     */
    public static boolean validateHeight(String S){
        try
        {
            long num = Long.parseLong(S);
            if (num>0)
                return true;
            else
                return false;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    /**
     * @param S строка для проверки
     * @return Проверка строки на валидность Веса.
     */
    public static boolean validateWeight(String S){
        try
        {
            int num = Integer.parseInt(S);
            if (num>0)
                return true;
            else
                return false;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    /**
     * @param S строка для проверки
     * @return Проверка строки на валидность ID.
     */
    public static boolean validateID(String S){
        try
        {
            int num = Integer.parseInt(S);
            if (num>0)
                return true;
            else
                return false;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    /**
     * печатает все данные в одну строку
     */
    public String ToString() {
        return id+":"+name+" "+passportID+" height:"+height+" weight:"+weight+" eye:"+eyeColor+" "+coordinates.toString()+" "+location.toString();
    }
}
