package utilities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by smartin on 31/03/2016.
 */
public class User implements Serializable{
    private String name;
    private String email;
    private String password;
    private String location;
    private String title;
    private String resume;
    private String image;
    private String account;
    private ArrayList<AcademicFormation> academics;
    private ArrayList<ProfessionalExperience> professionals;
    private ArrayList<Reference> references;

    public User(String name, String email, String password, String location,
                String title, String resume, String image,String account, ArrayList<AcademicFormation> academics,
                ArrayList<ProfessionalExperience> professionals, ArrayList<Reference> references) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.title = title;
        this.resume = resume;
        this.image=image;
        this.account = account;
        this.academics = academics;
        this.professionals = professionals;
        this.references=references;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String isAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<AcademicFormation> getAcademics() {
        return academics;
    }

    public void setAcademics(ArrayList<AcademicFormation> academics) {
        this.academics = academics;
    }

    public ArrayList<ProfessionalExperience> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(ArrayList<ProfessionalExperience> professionals) {
        this.professionals = professionals;
    }

    public ArrayList<Reference> getReferences() {
        return references;
    }

    public void setReferences(ArrayList<Reference> references) {
        this.references = references;
    }
}
