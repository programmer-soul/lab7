package main;
import dao.UserDAO;
import dao.PersonDAO;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import java.util.List;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SQLManager {
    private final SessionFactory sessionFactory;
    private final Logger logger;

    public SQLManager(SessionFactory sessionFactory,Logger logger) {
        this.sessionFactory = sessionFactory;
        this.logger=logger;
    }

    public int addPerson(Person person, UserDAO user) {
        logger.info("Добавление нового человека " + person.getName());
        var personDAO = new PersonDAO(person);
        personDAO.setCreator(user);

        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(personDAO);
        session.getTransaction().commit();
        session.close();

        logger.info("Добавление человека успешно выполнено.");

        var newId = personDAO.getID();
        logger.info("Новый id человека это " + newId);
        return newId;
    }
    public boolean updatePerson(Person person, UserDAO user) {
        logger.info("Обновление человека id#" + person.getID());
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        var personDAO = session.get(PersonDAO.class, person.getID());
        if (personDAO!=null && personDAO.getCreator().getId()==user.getId()){
            personDAO.update(person);
            session.update(personDAO);
            session.getTransaction().commit();
            session.close();
            logger.info("Обновление человека выполнено!");
            return true;
        }
        else{
            session.getTransaction().commit();
            session.close();
            logger.info("Обновление человека не выполнено!");
            return false;
        }
    }
    public boolean clearPerson(UserDAO user) {
        logger.info("Очищение коллекции пользователя id#" + user.getId() + " из базы данных.");
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        var query = session.createQuery("DELETE FROM persons WHERE creator.id = :creator");
        query.setParameter("creator", user.getId());
        var deletedSize = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        logger.info("Удалено " + deletedSize + " человек из коллекции.");
        return (deletedSize>0);
    }
    public boolean removePerson(int id, UserDAO user) {
        logger.info("Удаление человека №" + id + " пользователя id#" + user.getId() + ".");

        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        var query = session.createQuery("DELETE FROM persons WHERE creator.id = :creator AND id = :id");
        query.setParameter("creator", user.getId());
        query.setParameter("id", id);

        var deletedSize = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        logger.info("Удалено " + deletedSize + " человек.");

        return (deletedSize>0);
    }
    public List<PersonDAO> loadPersons() {
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        var cq = session.getCriteriaBuilder().createQuery(PersonDAO.class);
        var rootEntry = cq.from(PersonDAO.class);
        var all = cq.select(rootEntry);
        var result = session.createQuery(all).getResultList();
        session.getTransaction().commit();
        session.close();
        return result;
    }
    public int addUser(String login, String password) {
        try{
            logger.info("Создание нового пользователя " + login);
            var newId = 0;
            var passwordHash = generateSHA224(password);
            var user = new UserDAO();
            user.setName(login);
            user.setPassword(passwordHash);
            var session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            var query = session.createQuery("SELECT u FROM users u WHERE u.name = :name");
            query.setParameter("name", login);
            List<UserDAO> result = (List<UserDAO>) query.list();
            if (result.isEmpty()) {
                session.persist(user);
                newId = user.getId();
                logger.info("Пользователь успешно создан, id#" + newId);
            }
            else{
                logger.info("Ошибка добавления пользователя. Пользователь с таким логином уже есть!");
            }
            session.getTransaction().commit();
            session.close();
            return newId;
        }
        catch ( Exception e) {
            logger.log(Level.SEVERE,"Ошибка добавления пользователя "+e);
            return 0;
        }
    }
    public ResponseManager authenticateCommand(String login, String password) {
        logger.info("Аутентификация пользователя " + login);
        ResponseManager responsemanager = new ResponseManager();
        try{
            var session = sessionFactory.getCurrentSession();
            session.beginTransaction();

            var query = session.createQuery("SELECT u FROM users u WHERE u.name = :name");
            query.setParameter("name", login);

            List<UserDAO> result = (List<UserDAO>) query.list();

            if (result.isEmpty()) {
                session.getTransaction().rollback();
                session.close();
                logger.info("Неправильный логин пользователя " + login);
                responsemanager.addResponse("Неправильный логин пользователя!");
                return responsemanager;
            }

            var user = result.get(0);
            session.getTransaction().commit();
            session.close();

            var id = user.getId();
            var expectedHashedPassword = user.getPassword();
            var actualHashedPassword = generateSHA224(password);

            if (expectedHashedPassword.equals(actualHashedPassword)) {
                logger.info("Пользователь " + login + " аутентифицирован c id#" + id);
                responsemanager.addResponse(id,"Пользователь " + login + " аутентифицирован c id#" + id);
                return responsemanager;
            }
            logger.info("Неправильный пароль для пользователя " + login +". Ожидалось '" + expectedHashedPassword + "', получено '" + actualHashedPassword + "'");
            responsemanager.addResponse("Неправильный пароль для пользователя!");
        }
        catch ( Exception e) {
            logger.log(Level.SEVERE,"Ошибка аутентификации пользователя "+e);
            responsemanager.addResponse("Ошибка аутентификации пользователя!");
        }
        return responsemanager;
    }

    public int authenticateUser(String login, String password)  {
        try{
            logger.info("Аутентификация пользователя " + login);
            var session = sessionFactory.getCurrentSession();
            session.beginTransaction();

            var query = session.createQuery("SELECT u FROM users u WHERE u.name = :name");
            query.setParameter("name", login);

            List<UserDAO> result = (List<UserDAO>) query.list();

            if (result.isEmpty()) {
                session.getTransaction().rollback();
                session.close();
                logger.info("Неправильный логин пользователя " + login);
                return 0;
            }

            var user = result.get(0);
            session.getTransaction().commit();
            session.close();

            var id = user.getId();
            var expectedHashedPassword = user.getPassword();
            var actualHashedPassword = generateSHA224(password);

            if (expectedHashedPassword.equals(actualHashedPassword)) {;
                logger.info("Пользователь " + login + " аутентифицирован c id#" + id);
                return id;
            }

            logger.info("Неправильный пароль для пользователя " + login + ". Ожидалось '" + expectedHashedPassword + "', получено '" + actualHashedPassword + "'");
            return 0;

        }
        catch ( Exception e) {
            logger.log(Level.SEVERE,"Ошибка аутентификации пользователя "+e);
            return 0;
        }
    }

    public String generateSHA224(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            byte[] messageDigest = md.digest(input.getBytes());// массив байтов из строки
            BigInteger no = new BigInteger(1, messageDigest);// массив байтов в BigInteger
            String hashtext = no.toString(16);//Конвертируем в шестнатиричное
            while (hashtext.length() < 32) {// Длина должна быть 32 символа
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE,"Ошибка генерации SHA-224");
            return "";
        }
    }
}
