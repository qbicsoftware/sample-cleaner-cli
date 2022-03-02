package life.qbic.tracking;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "samples_locations")
public class SampleLocation {

  @Id
  @Column(name = "sample_id")
  @NotNull
  String sampleId;

  @Column(name = "location_id")
  int locationId;

  @Column(name = "arrival_time")
  LocalDate arrivalTime;

  @Column(name = "forwarded_time")
  LocalDate forwardedTime;

  @Column(name = "sample_status")
  String sampleStatus;

  @Column(name = "responsible_person_id")
  int responsiblePersonId;

  public SampleLocation(String sampleId, int locationId, LocalDate arrivalTime,
      LocalDate forwardedTime, String sampleStatus, int responsiblePersonId) {
    this.sampleId = sampleId;
    this.locationId = locationId;
    this.arrivalTime = arrivalTime;
    this.forwardedTime = forwardedTime;
    this.sampleStatus = sampleStatus;
    this.responsiblePersonId = responsiblePersonId;
  }

  public SampleLocation() {

  }

  public String getSampleId() {
    return sampleId;
  }

  public void setSampleId(String sampleId) {
    this.sampleId = sampleId;
  }

  public int getLocationId() {
    return locationId;
  }

  public void setLocationId(int locationId) {
    this.locationId = locationId;
  }

  public LocalDate getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(LocalDate arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public LocalDate getForwardedTime() {
    return forwardedTime;
  }

  public void setForwardedTime(LocalDate forwardedTime) {
    this.forwardedTime = forwardedTime;
  }

  public String getSampleStatus() {
    return sampleStatus;
  }

  public void setSampleStatus(String sampleStatus) {
    this.sampleStatus = sampleStatus;
  }

  public int getResponsiblePersonId() {
    return responsiblePersonId;
  }

  public void setResponsiblePersonId(int responsiblePersonId) {
    this.responsiblePersonId = responsiblePersonId;
  }

  @Override
  public String toString() {
    return "SampleLocation{" +
        "sampleId='" + sampleId + '\'' +
        ", locationId=" + locationId +
        ", arrivalTime=" + arrivalTime +
        ", forwardedTime=" + forwardedTime +
        ", sampleStatus='" + sampleStatus + '\'' +
        ", responsiblePersonId=" + responsiblePersonId +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SampleLocation that = (SampleLocation) o;
    return locationId == that.locationId && responsiblePersonId == that.responsiblePersonId
        && Objects.equals(sampleId, that.sampleId) && Objects.equals(arrivalTime,
        that.arrivalTime) && Objects.equals(forwardedTime, that.forwardedTime)
        && Objects.equals(sampleStatus, that.sampleStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sampleId, locationId, arrivalTime, forwardedTime, sampleStatus,
        responsiblePersonId);
  }
}
