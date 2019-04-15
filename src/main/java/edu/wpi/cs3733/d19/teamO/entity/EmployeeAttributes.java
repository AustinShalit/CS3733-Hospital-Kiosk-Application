package edu.wpi.cs3733.d19.teamO.entity;

import java.util.Objects;

import com.google.gson.Gson;

public class EmployeeAttributes {
  private boolean canFulfillAudioVisual;
  private boolean canFulfillExternalTransport;
  private boolean canFulfillFlorist;
  private boolean canFulfillGift;
  private boolean canFulfillInternalTransport;
  private boolean canFulfillInterpreter;
  private boolean canFulfillITSupport;
  private boolean canFulfillPatientInfo;
  private boolean canFulfillPrescription;
  private boolean canFulfillReligious;
  private boolean canFulfillSanitation;
  private boolean canFulfillSecurity;
  private boolean canFulfillSupportAnimal;
  Employee.EmployeeType employeeType;

  public EmployeeAttributes(boolean canFulfillAudioVisual,
                            boolean canFulfillExternalTransport,
                            boolean canFulfillFlorist,
                            boolean canFulfillGift,
                            boolean canFulfillInternalTransport,
                            boolean canFulfillInterpreter,
                            boolean canFulfillITSupport,
                            boolean canFulfillPatientInfo,
                            boolean canFulfillPrescription,
                            boolean canFulfillReligious,
                            boolean canFulfillSanitation,
                            boolean canFulfillSecurity,
                            boolean canFulfillSupportAnimal,
                            Employee.EmployeeType employeeType) {
    this.canFulfillAudioVisual = canFulfillAudioVisual;
    this.canFulfillExternalTransport = canFulfillExternalTransport;
    this.canFulfillFlorist = canFulfillFlorist;
    this.canFulfillGift = canFulfillGift;
    this.canFulfillInternalTransport = canFulfillInternalTransport;
    this.canFulfillInterpreter = canFulfillInterpreter;
    this.canFulfillITSupport = canFulfillITSupport;
    this.canFulfillPatientInfo = canFulfillPatientInfo;
    this.canFulfillPrescription = canFulfillPrescription;
    this.canFulfillReligious = canFulfillReligious;
    this.canFulfillSanitation = canFulfillSanitation;
    this.canFulfillSecurity = canFulfillSecurity;
    this.canFulfillSupportAnimal = canFulfillSupportAnimal;
    this.employeeType = employeeType;
  }

  public EmployeeAttributes() {
    this(false, false, false, false, false, false, false, false, false, false, false, false, false, Employee.EmployeeType.DEFAULT);
  }

  public EmployeeAttributes(Employee.EmployeeType employeeType) {
    this(false, false, false, false, false, false, false, false, false, false, false, false, false, employeeType);
  }

  public static EmployeeAttributes fromString(String from) {
    return new Gson().fromJson(from, EmployeeAttributes.class);
  }

  public boolean getCanFulfillAudioVisual() {
    return canFulfillAudioVisual;
  }

  public void setCanFulfillAudioVisual(boolean canFulfillAudioVisual) {
    this.canFulfillAudioVisual = canFulfillAudioVisual;
  }

  public boolean getCanFulfillExternalTransport() {
    return canFulfillExternalTransport;
  }

  public void setCanFulfillExternalTransport(boolean canFulfillExternalTransport) {
    this.canFulfillExternalTransport = canFulfillExternalTransport;
  }

  public boolean getCanFulfillFlorist() {
    return canFulfillFlorist;
  }

  public void setCanFulfillFlorist(boolean canFulfillFlorist) {
    this.canFulfillFlorist = canFulfillFlorist;
  }

  public boolean getCanFulfillGift() {
    return canFulfillGift;
  }

  public void setCanFulfillGift(boolean canFulfillGift) {
    this.canFulfillGift = canFulfillGift;
  }

  public boolean getCanFulfillInternalTransport() {
    return canFulfillInternalTransport;
  }

  public void setCanFulfillInternalTransport(boolean canFulfillInternalTransport) {
    this.canFulfillInternalTransport = canFulfillInternalTransport;
  }

  public boolean getCanFulfillInterpreter() {
    return canFulfillInterpreter;
  }

  public void setCanFulfillInterpreter(boolean canFulfillInterpreter) {
    this.canFulfillInterpreter = canFulfillInterpreter;
  }

  public boolean getCanFulfillITSupport() {
    return canFulfillITSupport;
  }

  public void setCanFulfillITSupport(boolean canFulfillITSupport) {
    this.canFulfillITSupport = canFulfillITSupport;
  }

  public boolean getCanFulfillPatientInfo() {
    return canFulfillPatientInfo;
  }

  public void setCanFulfillPatientInfo(boolean canFulfillPatientInfo) {
    this.canFulfillPatientInfo = canFulfillPatientInfo;
  }

  public boolean getCanFulfillPrescription() {
    return canFulfillPrescription;
  }

  public void setCanFulfillPrescription(boolean canFulfillPrescription) {
    this.canFulfillPrescription = canFulfillPrescription;
  }

  public boolean getCanFulfillReligious() {
    return canFulfillReligious;
  }

  public void setCanFulfillReligious(boolean canFulfillReligious) {
    this.canFulfillReligious = canFulfillReligious;
  }

  public boolean getCanFulfillSanitation() {
    return canFulfillSanitation;
  }

  public void setCanFulfillSanitation(boolean canFulfillSanitation) {
    this.canFulfillSanitation = canFulfillSanitation;
  }

  public boolean getCanFulfillSecurity() {
    return canFulfillSecurity;
  }

  public void setCanFulfillSecurity(boolean canFulfillSecurity) {
    this.canFulfillSecurity = canFulfillSecurity;
  }

  public boolean getCanFulfillSupportAnimal() {
    return canFulfillSupportAnimal;
  }

  public void setCanFulfillSupportAnimal(boolean canFulfillSupportAnimal) {
    this.canFulfillSupportAnimal = canFulfillSupportAnimal;
  }

  public Employee.EmployeeType getEmployeeType() {
    return employeeType;
  }

  public void setEmployeeType(Employee.EmployeeType employeeType) {
    this.employeeType = employeeType;
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmployeeAttributes that = (EmployeeAttributes) o;
    return canFulfillAudioVisual == that.canFulfillAudioVisual
        && canFulfillExternalTransport == that.canFulfillExternalTransport
        && canFulfillFlorist == that.canFulfillFlorist
        && canFulfillGift == that.canFulfillGift
        && canFulfillInternalTransport == that.canFulfillInternalTransport
        && canFulfillInterpreter == that.canFulfillInterpreter
        && canFulfillITSupport == that.canFulfillITSupport
        && canFulfillPatientInfo == that.canFulfillPatientInfo
        && canFulfillPrescription == that.canFulfillPrescription
        && canFulfillReligious == that.canFulfillReligious
        && canFulfillSanitation == that.canFulfillSanitation
        && canFulfillSecurity == that.canFulfillSecurity
        && canFulfillSupportAnimal == that.canFulfillSupportAnimal
        && employeeType.equals(that.employeeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(canFulfillAudioVisual, canFulfillExternalTransport, canFulfillFlorist,
        canFulfillGift, canFulfillInternalTransport, canFulfillInterpreter,
        canFulfillITSupport, canFulfillPatientInfo, canFulfillPrescription,
        canFulfillReligious, canFulfillSanitation, canFulfillSecurity,
        canFulfillSupportAnimal, employeeType);
  }
}
