package utilities;

import java.io.Serializable;

/**
 * Created by smartin on 14/03/2016.
 */
public class AcademicFormation implements Serializable {
    public String degree, discipline,place, academic, iniDate, endDate;
    public AcademicFormation(String degree,String discipline,String place,String academic,String iniDate,String endDate){
        this.degree=degree;
        this.discipline=discipline;
        this.place=place;
        this.academic=academic;
        this.iniDate=iniDate;
        this.endDate=endDate;
    }
}
