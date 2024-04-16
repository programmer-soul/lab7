package main;

import javax.xml.stream.*;
import java.io.Serializable;

/**
 * Класс Местоположение
 * @author Matvei Baranov
 */
public class Location implements Serializable {
    private double x;
    private Float y;
    private double z;
    /**
     * Конструктор класса
     */
    public Location(double x,Float y,double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    /**
     * @return возвращает x
     */
    public double getX(){
        return x;
    }
    /**
     * @return возвращает y
     */
    public Float getY(){
        return y;
    }
    /**
     * @return возвращает z
     */
    public double getZ(){
        return z;
    }
    /**
     * @return возвращает местоположение строкой
     */
    public String toString() {
        return "["+x+";"+y+";"+x+"]";
    }
    /**
     * Сохраняет свой данные в открытый стрим в формате XML
     * @param out открытый стрим для записи
     */
    public boolean SaveXML(XMLStreamWriter out){
        try {
            out.writeStartElement("Location");

            out.writeStartElement("x");
            out.writeCharacters(Double.toString(x));
            out.writeEndElement();

            out.writeStartElement("y");
            out.writeCharacters(Float.toString(y));
            out.writeEndElement();

            out.writeStartElement("z");
            out.writeCharacters(Double.toString(z));
            out.writeEndElement();

            out.writeEndElement();
            return true;
        }
        catch (XMLStreamException e) {
            return false;
        }
    }
    /**
     * @param S строка для проверки
     * @return Проверка строки на валидность местоположения X.
     */
    public static boolean validateX(String S){
        try
        {
            Double num = Double.parseDouble(S);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    /**
     * @param S строка для проверки
     * @return Проверка строки на валидность местоположения Y.
     */
    public static boolean validateY(String S){
        try
        {
            Float num = Float.parseFloat(S);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    /**
     * @param S строка для проверки
     * @return Проверка строки на валидность местоположения Z.
     */
    public static boolean validateZ(String S){
        try
        {
            Double num = Double.parseDouble(S);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
