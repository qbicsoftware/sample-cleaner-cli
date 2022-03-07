package life.qbic.samplecleaner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import life.qbic.samplecleaner.tracking.SampleLocationRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App implements CommandLineRunner {

  final ApplicationContext applicationContext;

  final SampleLocationRepository sampleLocationRepository;

  private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(App.class);
  private final List<String> whiteListedSamples = new ArrayList<>();

  @Autowired
  public App(
      ApplicationContext applicationContext, SampleLocationRepository sampleLocationRepository) {
    this.applicationContext = applicationContext;
    this.sampleLocationRepository = sampleLocationRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Override
  public void run(String... args) {
    LOG.info("Parsing whitelist ...");
    String filePath = args[0];
    try {
      parseWhiteList(filePath);
      sampleLocationRepository.removeOutdatedSamples(whiteListedSamples, getProjectCode());
    } catch (Exception e) {
      LOG.error(e.getMessage());
    }
  }

  private String getProjectCode() {
    String projectCode = whiteListedSamples.get(0).substring(0, 5);
    return projectCode;
  }

  private void parseWhiteList(String pathToFile) {
    try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
      String line;
      while ((line = br.readLine()) != null) {
        // process the line.
        String[] columns = line.split("\t");
        String sampleCode = columns[0];

        if (sampleCode.startsWith("Q")) {
          whiteListedSamples.add(sampleCode);
        }
      }
    } catch (IOException ioException) {
      LOG.error("Failed reading input sample file");
      LOG.error(ioException.getMessage());
    }
  }
}
