package utilities;

import android.os.Parcelable;
import android.support.v7.widget.SearchView;

import java.io.Serializable;

/**
 * Created by smartin on 24/02/2016.
 */
public class Post implements Serializable{
    private String image;
    private String title;
    private String description;
    private String company;
    private String place;
    private String salary;
    private String companyMail;
    private String category;

    public Post(String image,String title,String description, String company,
                String place, String category,String companyMail,String salary){
        this.image=image;
        this.title=title;
        this.description=description;
        this.company=company;
        this.place=place;
        this.category=category;
        this.companyMail = companyMail;
        this.salary = salary;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image1){
        image=image1;
    }

    public String  getTitle(){
        return title;
    }

    public void setTitle(String title1){
        title=title1;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description1){
        description=description1;
    }

    public String getCompany(){
        return company;
    }

    public void setCompany(String company1){
        this.company=company1;
    }

    public String getPlace(){
        return place;
    }

    public void setPlace(String place1){
        this.place=place1;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category1){
        this.category=category1;
    }

    public String getCompanyMail() {
        return companyMail;
    }

    public void setCompanyMail(String companyMail) {
        this.companyMail = companyMail;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
