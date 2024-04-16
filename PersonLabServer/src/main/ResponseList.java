package main;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Класс Ответ от сервера Список для обмена данными между клиентом и сервером
 * @author Matvei Baranov
 */
public class ResponseList implements Serializable {
    private final List<Response> responses = new ArrayList<Response>();
    public void add(Response person){
        responses.add(person);
    }
    public void clear(){ responses.clear(); }
    public int size(){
        return responses.size();
    }
    public void sort(){ responses.sort(new NameComparator()); }
    public boolean isResponse(){ return !responses.isEmpty(); }

    public int getValue() {
        if (responses.size()>0){
            return responses.get(0).getValue();
        }
        else{
            return 0;
        }
    }
    public Response get(int index){ return responses.get(index); }
    public void show(){
        for(int i = 0; i< responses.size(); i++) {
            responses.get(i).show();
        }
    }
}
