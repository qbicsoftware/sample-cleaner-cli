package life.qbic.samplecleaner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

  final String file = "/Users/jenniferboedker/IdeaProjects/sample-cleaner-cli/src/main/resources/experiment-details-grid-sample-q_sample_preparation.tsv";
  private final List<String> whiteListedSamples = new ArrayList<>();

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
    System.out.println("Parsing whitelist ...");
    try{
      parseWhiteList(file);
      sampleLocationRepository.removeOutdatedSamples(whiteListedSamples,getProjectCode());
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }

  private String getProjectCode(){
    String projectCode = whiteListedSamples.get(0).substring(0,5);
    return projectCode;
  }

  private void parseWhiteList(String pathToFile){
    try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
      String line;
      while ((line = br.readLine()) != null) {
        // process the line.
        String[] columns = line.split("\t");
        String sampleCode = columns[0];

        if(sampleCode.startsWith("Q")){
          whiteListedSamples.add(sampleCode);
        }
      }
    }
    catch(IOException ioException){
      System.out.println("Error while reading input file");
    //todo throw exception here
    }
  }
}
