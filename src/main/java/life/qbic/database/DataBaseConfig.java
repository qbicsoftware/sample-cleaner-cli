package life.qbic.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "databases.users")
public class DataBaseConfig {

  @Value("${databases.users.user.name}")
  String user;
  @Value("${databases.users.user.password}")
  String password;
  @Value("${databases.users.database.url}")
  String url;
  @Value("${databases.users.database.dialect}")
  String sqlDialect;
  @Value("${databases.users.database.driver}")
  String driver;

}
