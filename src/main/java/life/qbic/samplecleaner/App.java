package life.qbic.samplecleaner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App implements CommandLineRunner {

  final ApplicationContext applicationContext;

  @Autowired
  public App(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Override
  public void run(String... args) {
    System.out.println(applicationContext);
  }
}
