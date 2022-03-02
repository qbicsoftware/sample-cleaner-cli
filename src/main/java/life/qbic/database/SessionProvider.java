package life.qbic.database;

import org.hibernate.Session;

/**
 * Provides a Hibernate session to perform transactions with the persistence layer.
 *
 * @since 1.3.0
 */
public interface SessionProvider {

    Session getCurrentSession();
}
