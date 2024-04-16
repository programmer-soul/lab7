package main;

import javax.xml.stream.*;
import java.io.Serializable;

/**
 * Класс Координаты
 * @author Matvei Baranov
 */
public class Coordinates implements Serializable {
    private double x;
    private Integer y;
    /**
     * Конструктор класса
     */
    public Coordinates(double x,Integer y){
        this.x=x;
        this.y=y;
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
    public Integer getY(){
        return y;
    }
    /**
     * @return возвращает координаты строкой
     */
    public String toString()
    {
        return "("+x+";"+y+")";
    }
    /**
     * Сохраняет свой данные в открытый стрим в формате XML
     * @param out открытый стрим для записи
     */
    public boolean SaveXML (XMLStreamWriter out){
        try {
            out.writeStartElement("Coordinates");

            out.writeStartElement("x");
            out.writeCharacters(Double.toString(x));
            out.writeEndElement();

            out.writeStartElement("y");
            out.writeCharacters(Integer.toString(y));
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
     * @return Проверка строки на валидность координаты X.
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
     * @return Проверка строки на валидность координаты Y.
     */
    public static boolean validateY(String S){
        try
        {
            Integer num = Integer.parseInt(S);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
