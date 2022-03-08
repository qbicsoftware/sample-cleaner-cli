package life.qbic.samplecleaner;

import life.qbic.samplecleaner.tracking.SampleLocation;
import life.qbic.samplecleaner.tracking.SampleLocationRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootApplication
public class App implements CommandLineRunner {

  final ApplicationContext applicationContext;

  final SampleLocationRepository sampleLocationRepository;

  static final Pattern sampleCodePattern = Pattern.compile("^Q.*");

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
      Set<String> whiteListedSamples = parseWhiteList(filePath);
      LOG.info(String.format("%d samples are whitelisted ...", whiteListedSamples.size()));

      String whiteListedSample = whiteListedSamples.iterator().next();
      String projectCode = extractProjectCode(whiteListedSample);

      var trackedSamples = sampleLocationRepository.getSampleLocations(projectCode);
      LOG.info(
          String.format(
              "Collected %d tracked samples for project %s ...",
              trackedSamples.size(), projectCode));

      var samplesToDelete = extractSamplesToDelete(whiteListedSamples, trackedSamples);
      deleteAllSamples(samplesToDelete);
      LOG.info(String.format("Deleted %d samples...", samplesToDelete.size()));

    } catch (Exception e) {
      LOG.error(e.getMessage());
    }
  }

  private List<SampleLocation> extractSamplesToDelete(
      Set<String> whiteListedSamples, List<SampleLocation> trackedSamples) {
    return trackedSamples.stream()
        .filter(sampleLocation -> !whiteListedSamples.contains(sampleLocation.getSampleId()))
        .collect(Collectors.toList());
  }

  private void deleteAllSamples(List<SampleLocation> samples) {
    for (var sample : samples) {
      sampleLocationRepository.deleteSample(sample.getSampleId());
    }
  }

  private String extractProjectCode(String sampleCode) {
    return sampleCode.substring(0, 5);
  }

  private static Set<String> parseWhiteList(String pathToFile) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get(pathToFile));
    return lines.stream()
        .map(App::extractFirstColumn)
        .filter(App::isSampleCode)
        .collect(Collectors.toSet());
  }

  private static String extractFirstColumn(String line) {
    return line.split("\t")[0];
  }

  private static boolean isSampleCode(String code) {
    Matcher matcher = sampleCodePattern.matcher(code);
    return matcher.find();
  }
}
