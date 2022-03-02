package life.qbic.samplecleaner.database;

import java.util.Properties;
import javax.annotation.PreDestroy;
import life.qbic.samplecleaner.tracking.SampleLocation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <b><class short description - 1 Line!></b>
 *
 * <p><More detailed description - When to use, what it solves, etc.></p>
 *
 * @since <version tag>
 */
@Component
public class HibernateSession implements SessionProvider {

    final SessionFactory sessionFactory;

    @Autowired
    public HibernateSession(DataBaseConfig config) {
        var hibernateConfig = new Configuration();
        var properties = new Properties();
        properties.put(Environment.DRIVER, config.driver);
        properties.put(Environment.URL, config.url);
        properties.put(Environment.USER, config.user);
        properties.put(Environment.PASS, config.password);
        properties.put(Environment.DIALECT, config.sqlDialect);
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

        hibernateConfig.setProperties(properties);
        hibernateConfig.addAnnotatedClass(SampleLocation.class);

        sessionFactory = hibernateConfig.buildSessionFactory();
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @PreDestroy
    void destroy() {
        sessionFactory.close();
    }
}
