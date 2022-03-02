package life.qbic.samplecleaner.database;

import org.hibernate.Session;

/**
 * Provides a Hibernate session to perform transactions with the persistence layer.
 *
 * @since 1.3.0
 */
public interface SessionProvider {

    public Session getCurrentSession();
}
