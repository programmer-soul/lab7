package main;

import java.io.Serializable;
import java.time.ZonedDateTime;
/**
 * Класс Ответ от сервера со структурой Person для обмена данными между клиентом и сервером
 * @author Matvei Baranov
 */
public class ResponsePerson implements Serializable {
    public int id;
    public String name;
    public Coordinates coordinates;
    public java.time.ZonedDateTime creationDate;
    public long height;
    public Integer weight;
    public String passportID;
    public Color eyeColor;
    public Location location;
    /**
     * Конструктор класса
     */
    public ResponsePerson(int id, String name, Coordinates coordinates, ZonedDateTime creationDate, long height, Integer weight, String passportID, Color eyeColor, Location location){
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
    /**
     * @return возвращает Person
     */
    public Person toPerson(){
        return (new Person(id,name,coordinates,creationDate,height,weight,passportID,eyeColor,location));
    }
    public void show(){
        System.out.println(id+":"+name+" "+passportID+" height:"+height+" weight:"+weight+" eye:"+eyeColor+" "+coordinates.toString()+" "+location.toString());
    }
    /**
     * @return возвращает строкой
     */
    public String toString() {
        return name;
    }
}
