package models;


import java.time.LocalDate;




public class Volunteer {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private LocalDate birthday;
    private boolean isTraining;

    public Volunteer(String firstName, String lastName,
                     String phoneNumber, String email, LocalDate birthday, boolean isTraining) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthday = birthday;
        this.isTraining = isTraining;
    }

    public Volunteer(String firstName, String lastName, String phoneNumber, String email, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public boolean isTraining() {
        return isTraining;
    }

    public void setTraining(boolean training) {
        isTraining = training;
    }
}
