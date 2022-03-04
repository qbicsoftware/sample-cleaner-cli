package life.qbic.samplecleaner.tracking;

import java.util.ArrayList;
import java.util.List;
import life.qbic.samplecleaner.database.SessionProvider;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleLocationRepository {

  SessionProvider sessionProvider;

  @Autowired
  public SampleLocationRepository(SessionProvider sessionProvider) {
    this.sessionProvider = sessionProvider;
  }

  /**
   * Returns all sample locations for a given project code
   * @param projectCode The project code for which the tracked samples shall be fetched
   * @return a list of {@link SampleLocation} objects found for the project
   */
  public List<SampleLocation> getSampleLocations(String projectCode) {
    List<SampleLocation> sampleLocations = new ArrayList<>();
    try (Session session = sessionProvider.getCurrentSession()) {
      session.beginTransaction();

      Query<SampleLocation> query = session.createQuery("FROM SampleLocation WHERE sampleId LIKE :projectCode");
      query.setParameter("projectCode", projectCode + "%");

      sampleLocations.addAll(query.list());
      session.getTransaction().commit();  //todo do we need this here?

    } catch (HibernateException exception) {
      exception.printStackTrace();
    }
    return sampleLocations;
  }

  /**
   * Deletes the tracking information for a given list of sample codes (Q_TEST_SAMPLES)
   * @param invalidSampleCodes
   */
  public void deleteSampleTracking(List<String> invalidSampleCodes){
    try (Session session = sessionProvider.getCurrentSession()) {
      session.beginTransaction();

      for (String sample:invalidSampleCodes) {
        Query<SampleLocation> query = session.createQuery("DELETE FROM SampleLocation WHERE sampleId= :sample");
        query.setParameter("sample",sample);
        query.executeUpdate();
      }

      session.getTransaction().commit();
    } catch (HibernateException exception) {
      exception.printStackTrace();
    }

  }
}
