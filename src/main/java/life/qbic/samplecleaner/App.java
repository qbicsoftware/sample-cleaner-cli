package life.qbic.samplecleaner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import life.qbic.samplecleaner.tracking.SampleLocation;
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

  final static Pattern sampleCodePattern = Pattern.compile("^Q.*");

  private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(App.class);

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
    LOG.info("Parsing sample id whitelist ...");
    String filePath = args[0];
    try {
      List<String> whiteListedSamples = parseWhiteList(filePath);
      LOG.info(String.format("%d samples are whitelisted ...", whiteListedSamples.size()));

      String projectCode = getProjectCode(whiteListedSamples);

      var trackedSamples = sampleLocationRepository.getSampleLocations(projectCode);
      LOG.info(
              String.format("Collected %d tracked samples for project %s ...", trackedSamples.size(),
                      projectCode));

      var samplesToDelete = extractSamplesToDelete(whiteListedSamples,trackedSamples);
      var deletedSamples = deleteAllSamples(samplesToDelete);
      LOG.info(String.format("Deleted %d samples...",deletedSamples));

    } catch (Exception e) {
      LOG.error(e.getMessage());
    }
  }

  private List<SampleLocation> extractSamplesToDelete(List<String> whiteListedSamples, List<SampleLocation> trackedSamples){
    return trackedSamples.stream()
            .filter(sampleLocation -> !whiteListedSamples.contains(sampleLocation.getSampleId()))
            .collect(Collectors.toList());
  }

  private int deleteAllSamples(List<SampleLocation> samples){
    var deletedSamples = 0;
    for (var sample : samples) {
      sampleLocationRepository.deleteSample(sample.getSampleId());
      deletedSamples++;
    }
    return deletedSamples;
  }

  private String getProjectCode(List<String> samples) {
    return samples.get(0).substring(0, 5);
  }

  private static List<String> parseWhiteList(String pathToFile) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get(pathToFile));
    return lines.stream()
        .map(App::extractFirstColumn)
        .filter(App::isSampleCode)
        .collect(Collectors.toList());
  }

  private static String extractFirstColumn(String line) {
    return line.split("\t")[0];
  }

  private static boolean isSampleCode(String code) {
    Matcher matcher = sampleCodePattern.matcher(code);
    return matcher.find();
  }
}
