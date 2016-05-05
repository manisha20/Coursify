package salazar.com.coursify;

/**
 * Created by Salazar on 27-04-2016.
 */
public class UserProfile {
    String name;
    String firstName;
    String lastName;
    String fileForProPic;
    Boolean flag=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileForProPic() {
        return fileForProPic;
    }

    public void setFileForProPic(String fileForProPic) {
        this.fileForProPic = fileForProPic;
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

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
