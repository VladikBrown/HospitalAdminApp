package model.entity;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Objects;

public class Patient {

    //alt+insert
    private ObjectId id;
    private String surname;
    @BsonProperty(value = "first_name")
    private String firstName;
    @BsonProperty(value = "second_name")
    private String secondName;
    private Address address;
    //private LocalDate birthdate;
    private Date birthdate;
    //private LocalDate dateOfVisit;
    @BsonProperty(value = "date_of_visit")
    private Date dateOfVisit;
    @BsonProperty(value = "name_of_doctor")
    private String docName;
    private String conclusion;

    public Patient(){
        this.id = new ObjectId();
        this.surname = "";
        this.firstName = "";
        this.secondName = "";
        this.docName = "";
        this.conclusion = "";
    }

    public Patient(String surname, String firstName, String secondName, Address address, Date birthdate,
                   Date dateOfVisit ,String docName, String conclusion) {
        this.surname = surname;
        this.firstName = firstName;
        this.secondName = secondName;
        this.address = address;
        this.birthdate = birthdate;
        this.dateOfVisit = dateOfVisit;
        this.docName = docName;
        this.conclusion = conclusion;
    }

    public ObjectId getId() {
        return id;
    }

    public String getIdString(){
        return id.toString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Date getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(Date dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id) &&
                Objects.equals(surname, patient.surname) &&
                Objects.equals(firstName, patient.firstName) &&
                Objects.equals(secondName, patient.secondName) &&
                Objects.equals(address, patient.address) &&
                Objects.equals(birthdate, patient.birthdate) &&
                Objects.equals(dateOfVisit, patient.dateOfVisit) &&
                Objects.equals(docName, patient.docName) &&
                Objects.equals(conclusion, patient.conclusion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, firstName, secondName, address, birthdate, dateOfVisit, docName, conclusion);
    }

    @Override
    public String toString() {

        return "{" +
                "id='" + id + '\'' +
                ", surname='" + surname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", address=" + address +
                ", birthdate=" + birthdate +
                ", dateOfVisit=" + dateOfVisit +
                ", docName='" + docName + '\'' +
                ", conclusion='" + conclusion + '\'' +
                '}';
    }
}
