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
   * Deletes all samples from the sample tracking database
   * that are not mentioned in the provided whitelist for a given project.
   *
   * @param whiteListSamples A list of samples that shall be tracked
   * @param projectCode a valid QBiC project code
   */
  public void deleteSamples(List<String> whiteListSamples, String projectCode) {
    try (Session session = sessionProvider.getCurrentSession()) {
      session.beginTransaction();
      //1. find out which tracked samples are not on the whitelist
      var trackedSamples = getSampleLocations(session, projectCode);
      LOG.info(
          String.format("Collected %d tracked samples for project %s ...", trackedSamples.size(),
              projectCode));
      //2. remove the samples
      var deletedSamples = 0;
      for (var sample : trackedSamples) {
        if (!whiteListSamples.contains(sample.sampleId)) {
          deleteSampleTracking(session, sample.sampleId);
          deletedSamples++;
        }
      }
      LOG.info(String.format("Deleted %d samples...",deletedSamples));

      session.getTransaction().commit();
    } catch (HibernateException exception) {
      LOG.error(exception.getMessage());
      throw new HibernateException("Error while trying to remove outdated samples", exception);
    }
  }

  /**
   * Returns all sample locations for a given project code
   *
   * @param projectCode The project code for which the tracked samples shall be fetched
   * @return a list of {@link SampleLocation} objects found for the project
   */
  private List<SampleLocation> getSampleLocations(Session session, String projectCode) {
    Query<SampleLocation> query = session.createQuery(SAMPLELOCATION_LIKE_CODE_QUERY);
    query.setParameter("projectCode", projectCode + "%");

    return new ArrayList<>(query.list());
  }

  /**
   * Deletes the tracking information for a given sample
   * @param session Session object from hibernate
   * @param sampleCode Respective openbis sample code
   */
  private void deleteSampleTracking(Session session, String sampleCode) {
    Query<SampleLocation> query = session.createQuery(SAMPLELOCATION_DELETE_QUERY);
    query.setParameter("sample", sampleCode);
    query.executeUpdate();
  }

}
