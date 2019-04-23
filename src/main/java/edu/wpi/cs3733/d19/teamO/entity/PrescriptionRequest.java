package edu.wpi.cs3733.d19.teamO.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class PrescriptionRequest extends ServiceRequest {

	private final String whoRequested;
	private final String patientName;
	private final LocalDate patientDOB;
	private final String medicationName;
	private final String medicationDosage;

	/**
	 * Initialization of PrescriptionRequest object.
	 *
	 * @param id               id of service request
	 * @param timeRequested    date request was submitted
	 * @param timeCompleted    date request was completed
	 * @param whoRequested     name of employee submitting request
	 * @param patientName      name of patient who request is for
	 * @param patientDOB       date of birth of the patient
	 * @param medicationName   name of medication
	 * @param medicationDosage dosage of medication
	 * @param description      directions associated with medication
	 */
	@SuppressWarnings("PMD.ExcessiveParameterList")
	public PrescriptionRequest(int id, LocalDateTime timeRequested, LocalDateTime timeCompleted,
	                           String whoRequested, String patientName, LocalDate patientDOB,
	                           String medicationName, String medicationDosage, String description) {

		super(id, timeRequested, timeCompleted, description);
		this.whoRequested = whoRequested;
		this.patientName = patientName;
		this.patientDOB = patientDOB;
		this.medicationName = medicationName;
		this.medicationDosage = medicationDosage;
	}

	/**
	 * Alternate constructor.
	 */
	public PrescriptionRequest(LocalDateTime timeRequested,
	                           String whoRequested, String patientName,
	                           LocalDate patientDOB, String medicationName, String medicationDosage,
	                           String description) {
		super(timeRequested, description, null);
		this.whoRequested = whoRequested;
		this.patientName = patientName;
		this.patientDOB = patientDOB;
		this.medicationName = medicationName;
		this.medicationDosage = medicationDosage;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PrescriptionRequest)) {
			return false;
		}

		PrescriptionRequest that = (PrescriptionRequest) o;

		return getId() == that.getId()
				&& getTimeRequested() == that.getTimeRequested()
				&& getTimeCompleted() == that.getTimeCompleted()
				&& getWhoCompleted().equals(that.getWhoCompleted())
				&& getPatientName().equals(that.getPatientName())
				&& getPatientDOB() == that.getPatientDOB()
				&& getMedicationName().equals(that.getMedicationName())
				&& getMedicationDosage().equals(that.getMedicationDosage())
				&& getDescription().equals(that.getDescription());
	}


	@Override
	public int hashCode() {
		return Objects.hash(getId(), getTimeRequested(), getTimeCompleted(), getWhoRequested(),
				getPatientName(), getMedicationName(), getMedicationDosage(), getDescription());
	}

	@Override
	public String toString() {
		return "PrescriptionRequest{"
				+ "id=" + getId()
				+ ", timeRequested=" + getTimeRequested()
				+ ", timeCompleted=" + getTimeCompleted()
				+ ", whoRequested=" + getWhoRequested()
				+ ", patientName=" + getPatientName()
				+ ", patientDOB=" + getPatientDOB().toString()
				+ ", medicationName=" + getMedicationName()
				+ ", medicationDosage=" + getMedicationDosage()
				+ ", description=" + getDescription() + '\''
				+ '}';
	}

	public String getPatientName() {
		return patientName;
	}

	public LocalDate getPatientDOB() {
		return patientDOB;
	}

	public String getMedicationName() {
		return medicationName;
	}

	public String getMedicationDosage() {
		return medicationDosage;
	}

	public String getWhoRequested() {
		return whoRequested;
	}

}
