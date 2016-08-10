package utilities;

/**
 * Created by smartin on 29/02/2016.
 */
public class Category {
    private int image,number;
    private String title;

    public Category(int image,String title,int number){
        this.image=image;
        this.number=number;
        this.title=title;
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

    public int getNumber(){ return number; }

    public void setNumber(int number1){ number=number1;}
}
