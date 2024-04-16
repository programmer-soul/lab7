package dao;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс Пользователи для хранения SQL
 * @author Matvei Baranov
 */
@Entity(name="users")
@Table(name="users", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
public class UserDAO implements Serializable {
  public UserDAO() {
    this.id=0;
  }
  public UserDAO(String name,String password) {
    this.id=0;
    this.name=name;
    this.password=password;
  }
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id", nullable=false, unique=true, length=11)
  private int id;

  @Column(name="name", length=40, unique=true, nullable=false)
  private String name;

  @Column(name="password_hash", length=64, nullable=false)
  private String password;

  @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
  @JoinColumn(name="creator_id")
  private List<PersonDAO> persons = new ArrayList<>();

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {this.name = name;}

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<PersonDAO> getPersons() {
    return persons;
  }

  public void setPersons(List<PersonDAO> persons) {
    this.persons = persons;
  }
}
