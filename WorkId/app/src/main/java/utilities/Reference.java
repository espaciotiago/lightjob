package utilities;

import java.io.Serializable;

/**
 * Created by smartin on 07/04/2016.
 */
public class Reference implements Serializable{
    public static int FAMILIAR=0;
    public static int PERSONAL=1;
    public static int PROFESSIONAL=2;

    private int type;
    private String name;
    private String relation;
    private String occupation;
    private String contact;

    public Reference(int type, String name, String relation, String occupation, String contact) {
        this.type = type;
        this.name = name;
        this.relation = relation;
        this.occupation = occupation;
        this.contact = contact;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
