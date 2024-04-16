package dao;

import java.time.*;
import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import main.Color;
import main.Coordinates;
import main.Person;
import main.Location;

/**
 * Класс Человек для хранения SQL
 * @author Matvei Baranov
 */
@Entity(name="persons")
@Table(name="persons", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
public class PersonDAO implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable=false, unique=true, length=11)
    private int id;
    @NotBlank(message = "ФИО не должно быть пустым.")
    @Column(name="name", nullable=false)
    private String name;
    @NotBlank(message = "Номер паспорта не должен быть пустым.")
    @Column(name="passport", nullable=false)
    private String passportID;
    @Column(name="creation_date", nullable=false)
    private ZonedDateTime creationDate;//LocalDateTime
    @Min(value = 1L, message = "Рост должен быть больше нуля.")
    @Column(name="height", nullable=false)
    private long height;
    @Min(value = 1L, message = "Вес должен быть больше нуля.")
    @Column(name="weight", nullable=false)
    private Integer weight;
    @Column(name="coord_x", nullable=false)
    private double coordX;
    @Column(name="coord_y", nullable=false)
    private Integer coordY;
    @Column(name="eye_color")
    @Enumerated(EnumType.STRING)
    private Color eyeColor;
    @Column(name="x", nullable=false)
    private double x;
    @Column(name="y", nullable=false)
    private Float y;
    @Column(name="z", nullable=false)
    private double z;
    @ManyToOne
    @JoinColumn(name="creator_id", nullable=false)
    private UserDAO creator;
    /**
     * Конструктор класса
     */
    public PersonDAO(){
    }
    public PersonDAO(Person person){
        this.id=person.getID();
        this.name=person.getName();
        this.coordX=person.getCoordX();
        this.coordY=person.getCoordY();
        this.creationDate=person.getCreationDate();//.toLocalDateTime();
        this.height=person.getHeight();
        this.weight=person.getWeight();
        this.passportID=person.getPassportID();
        this.eyeColor=person.getEyeColor();
        this.x=person.getX();
        this.y=person.getY();
        this.z=person.getZ();
    }
    public void update(Person person) {
        this.name=person.getName();
        this.coordX=person.getCoordX();
        this.coordY=person.getCoordY();
        this.creationDate=person.getCreationDate();//.toLocalDateTime();
        this.height=person.getHeight();
        this.weight=person.getWeight();
        this.passportID=person.getPassportID();
        this.eyeColor=person.getEyeColor();
        this.x=person.getX();
        this.y=person.getY();
        this.z=person.getZ();
    }
    public Person toPerson(){
        //return new Person(id,name,new Coordinates(coordX,coordY),ZonedDateTime.ofLocal(creationDate,ZoneId.systemDefault(),ZoneOffset.UTC),height, weight, passportID, eyeColor, new Location(x,y,z));
        return new Person(id,name,new Coordinates(coordX,coordY),creationDate,height, weight, passportID, eyeColor, new Location(x,y,z));
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
     * печатает все данные в одну строку
     */
    public UserDAO getCreator(){
        return creator;
    }
    public void setCreator(UserDAO creator) {
        this.creator = creator;
    }
}
