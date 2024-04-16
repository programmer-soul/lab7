package main;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Класс Ответ от сервера Список со структурой Person для обмена данными между клиентом и сервером
 * @author Matvei Baranov
 */
public class ResponsePersonList implements Serializable {
    private final List<ResponsePerson> persons = new ArrayList<ResponsePerson>();
    public void add(ResponsePerson person){
        persons.add(person);
    }
    public void clear(){ persons.clear(); }
    public int size(){
        return persons.size();
    }
    public void sort(){ persons.sort(new NameComparator()); }
    public boolean isPerson(){ return !persons.isEmpty(); }
    public ResponsePerson get(int index){ return persons.get(index); }
    public void show(){
        for(int i = 0; i< persons.size(); i++) {
            persons.get(i).show();
        }
    }
}
