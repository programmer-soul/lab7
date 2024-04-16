package main;
/**
 * Класс обработки и формирования ответа от сервера для обмена данными между клиентом и сервером
 * @author Matvei Baranov
 */
public class ResponseManager {
    public final ResponseList responses = new ResponseList();
    public final ResponsePersonList persons = new ResponsePersonList();
    private boolean exit = false;
    private boolean success = false;

    public void addResponse(String message){
        responses.add(new Response(message));
    }
    public void addResponse(int value,String message){
        responses.add(new Response(value,message));
    }
    public void addResponse(String message,boolean success){
        this.success=success;
        responses.add(new Response(message));
    }
    public void addResponsePerson(Person person){
        persons.add(person.toResponse());
    }
    public int getValue() {
        return responses.getValue();
    }
    public void clear(){
        responses.clear();
        persons.clear();
    }
    public void setExit(){
        exit = true;
    }
    public int size(){
        return responses.size();
    }
    public int sizePerson(){
        return persons.size();
    }
    public void sort(){ persons.sort(); }
    public int fullsize(){
        return responses.size()+persons.size();
    }
    public boolean isExit(){
        return exit;
    }
    public boolean isSuccess(){
        return success;
    }
    public boolean isPerson(){ return persons.isPerson(); }
    public Response get(int index){ return responses.get(index); }
    public ResponsePerson getPerson(int index){ return persons.get(index); }
    public void addList(ResponseManager responsemanager){
        for(int i = 0; i< responsemanager.size(); i++) {
            responses.add(responsemanager.get(i));
        }
        responsemanager.clear();
    }
}

