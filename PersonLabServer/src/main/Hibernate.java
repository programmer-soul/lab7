package main;

import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import dao.PersonDAO;
import dao.UserDAO;

public class Hibernate {

  private static SessionFactory sessionFactory;

  private static SessionFactory buildSessionFactory(String url, String user, String password, Logger logger) {
    try {
      //Create Properties, can be read from property files too
      Properties props = new Properties();
      props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
      props.put("hibernate.connection.driver_class", "org.postgresql.Driver");

      props.put("hibernate.connection.username", user);
      props.put("hibernate.connection.password", password);
      props.put("hibernate.connection.url", url);

      props.put("hibernate.connection.pool_size", "10");
      props.put("hibernate.current_session_context_class", "thread");
      props.put("hibernate.connection.autocommit", "true");
      props.put("hibernate.show_sql", "true");
      props.put("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");
      props.put("hibernate.hbm2ddl.auto", "validate");

      Configuration configuration = new Configuration();
      configuration.setProperties(props);

      configuration.addPackage("dao");
      configuration.addAnnotatedClass(dao.UserDAO.class);
      configuration.addAnnotatedClass(dao.PersonDAO.class);

      ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
      logger.info("Hibernate Java Config serviceRegistry created");

      return configuration.buildSessionFactory(serviceRegistry);
    }
    catch (Throwable ex) {
      logger.log(Level.SEVERE,"Initial SessionFactory creation failed.",ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  public static SessionFactory getSessionFactory(String url, String user, String password, Logger logger) {
    if(sessionFactory == null) sessionFactory = buildSessionFactory(url, user, password, logger);
    return sessionFactory;
  }
}
