package life.qbic.samplecleaner.tracking;

import java.util.ArrayList;
import java.util.List;
import life.qbic.samplecleaner.database.SessionProvider;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleLocationRepository {

  private final SessionProvider sessionProvider;
  private static final String SAMPLELOCATION_LIKE_CODE_QUERY = "FROM SampleLocation WHERE sampleId LIKE :projectCode";
  private static final String SAMPLELOCATION_DELETE_QUERY = "DELETE FROM SampleLocation WHERE sampleId= :sample";

  private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(
      SampleLocationRepository.class);

  @Autowired
  public SampleLocationRepository(SessionProvider sessionProvider) {
    this.sessionProvider = sessionProvider;
  }

  /**
   * Returns all sample locations for a given project code
   *
   * @param projectCode The project code for which the tracked samples shall be fetched
   * @return a list of {@link SampleLocation} objects found for the project
   */
  public List<SampleLocation> getSampleLocations(String projectCode) {
    List<SampleLocation> sampleLocations;
    try (Session session = sessionProvider.getCurrentSession()) {
      session.beginTransaction();

      Query<SampleLocation> query = session.createQuery(SAMPLELOCATION_LIKE_CODE_QUERY);
      query.setParameter("projectCode", projectCode + "%");
      sampleLocations = new ArrayList<>(query.list());

      session.getTransaction().commit();
    } catch (HibernateException exception) {
      throw new HibernateException("Error while trying to remove outdated samples", exception);
    }
    return sampleLocations;
  }

  /**
   * Deletes the tracking information for a given sample
   * @param sampleCode Respective openbis sample code
   */
  public void deleteSample(String sampleCode) {
    try (Session session = sessionProvider.getCurrentSession()) {
      session.beginTransaction();

      Query<SampleLocation> query = session.createQuery(SAMPLELOCATION_DELETE_QUERY);
      query.setParameter("sample", sampleCode);
      query.executeUpdate();

      session.getTransaction().commit();
    } catch (HibernateException exception) {
      throw new HibernateException("Error while trying to delete sample tracking information", exception);
    }
  }

}
