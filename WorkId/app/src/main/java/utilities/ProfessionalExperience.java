package utilities;

import java.io.Serializable;

/**
 * Created by smartin on 14/03/2016.
 */
public class ProfessionalExperience implements Serializable{
    public String place,position,iniDate,endDate;

    public ProfessionalExperience(String place,String position,String iniDate,String endDate){
        this.place=place;
        this.position=position;
        this.iniDate=iniDate;
        this.endDate=endDate;
    }
}
