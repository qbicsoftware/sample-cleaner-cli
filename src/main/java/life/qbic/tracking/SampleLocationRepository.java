package life.qbic.tracking;

import java.util.ArrayList;
import java.util.List;
import life.qbic.database.SessionProvider;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleLocationRepository {

  SessionProvider sessionProvider;

  @Autowired
  SampleLocationRepository(SessionProvider sessionProvider) {
    this.sessionProvider = sessionProvider;
  }

  List<SampleLocation> getAllSampleLocations() {
    List<SampleLocation> sampleLocations = new ArrayList<>();
    try (Session session = sessionProvider.getCurrentSession()) {
      session.beginTransaction();
      Query<SampleLocation> query = session.createQuery("FROM SampleLocation");
      sampleLocations.addAll(query.list());
    } catch (HibernateException exception) {
      exception.printStackTrace();
    }
    return sampleLocations;
  }

}
