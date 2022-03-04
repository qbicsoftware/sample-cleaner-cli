package life.qbic.samplecleaner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import life.qbic.samplecleaner.tracking.SampleLocation;
import life.qbic.samplecleaner.tracking.SampleLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App implements CommandLineRunner {

  final ApplicationContext applicationContext;

  final SampleLocationRepository sampleLocationRepository;

  @Autowired
  public App(ApplicationContext applicationContext, SampleLocationRepository sampleLocationRepository) {
    this.applicationContext = applicationContext;
    this.sampleLocationRepository = sampleLocationRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Override
  public void run(String... args) {
    System.out.println(args[1]);
    String file = "/Users/jenniferboedker/IdeaProjects/sample-cleaner-cli/src/main/resources/experiment-details-grid-sample-q_sample_preparation.tsv";
    List<SampleLocation> sampleLocations = sampleLocationRepository.getSampleLocations("QSTTS");
    System.out.println("Number of sample locations found: "+ sampleLocations.size());
  }

  private List<String> parseSampleList(String pathToFile){
    try(FileInputStream input = new FileInputStream(pathToFile)){

    }catch(IOException ioException){

    }
    return null;
  }
}
