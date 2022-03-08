package life.qbic.samplecleaner.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "databases.users")
public class DataBaseConfig {

  @Value("${databases.users.user.name}")
  public String user;
  @Value("${databases.users.user.password}")
  public String password;
  @Value("${databases.users.database.url}")
  public String url;
  @Value("${databases.users.database.dialect}")
  public String sqlDialect;
  @Value("${databases.users.database.driver}")
  public String driver;

}
