package utilities;

import android.os.Parcelable;
import android.support.v7.widget.SearchView;

import java.io.Serializable;

/**
 * Created by smartin on 24/02/2016.
 */
public class Post implements Serializable{
    private int image;
    private String title;
    private String description;
    private String company;
    private String place;
    private int category;

    public Post(int image,String title,String description, String company, String place, int category){
        this.image=image;
        this.title=title;
        this.description=description;
        this.company=company;
        this.place=place;
        this.category=category;
    }

    public int getImage(){
        return image;
    }

    public void setImage(int image1){
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

    public int getCategory(){
        return category;
    }

    public void setCategory(int category1){
        this.category=category1;
    }
}
