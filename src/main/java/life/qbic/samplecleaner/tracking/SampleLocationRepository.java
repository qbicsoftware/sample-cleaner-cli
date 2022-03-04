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
   * Removes all samples from the tracking database that are not part of the whitelist
   * @param whiteListSamples A list of samples that shall be tracked
   */
  public void removeOutdatedSamples(List<String> whiteListSamples, String projectCode){
    try (Session session = sessionProvider.getCurrentSession()) {
      session.beginTransaction();
      System.out.println("Starting session ...");
      //1. find out which tracked samples are not on the whitelist
      List<SampleLocation> trackedSamples = getSampleLocations(session,projectCode);
      System.out.printf("Collected %d tracked samples for project %s ... %n",trackedSamples.size(),projectCode);
      //2. remove the samples
      for (SampleLocation sample:trackedSamples) {
        if(!whiteListSamples.contains(sample.sampleId)){
          //deleteSampleTracking(session,sample.sampleId);
          System.out.printf("Deleted sample with ID %s ... %n",sample.sampleId);
        }
      }
      session.getTransaction().commit();
      System.out.println("Finished session ...");
    } catch (HibernateException exception) {
      exception.printStackTrace();
      throw new RuntimeException("Error while trying to remove outdated samples");
    }
  }

  /**
   * Returns all sample locations for a given project code
   * @param projectCode The project code for which the tracked samples shall be fetched
   * @return a list of {@link SampleLocation} objects found for the project
   */
  private List<SampleLocation> getSampleLocations(Session session, String projectCode) {
    List<SampleLocation> sampleLocations = new ArrayList<>();

      Query<SampleLocation> query = session.createQuery("FROM SampleLocation WHERE sampleId LIKE :projectCode");
      query.setParameter("projectCode", projectCode + "%");

      sampleLocations.addAll(query.list());

    return sampleLocations;
  }

  /**
   * Deletes the tracking information for a given sample
   * @param sampleCode
   */
  private void deleteSampleTracking(Session session, String sampleCode){
    Query<SampleLocation> query = session.createQuery("DELETE FROM SampleLocation WHERE sampleId= :sample");
    query.setParameter("sample",sampleCode);
    query.executeUpdate();
  }
}
