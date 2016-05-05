package salazar.com.coursify;

/**
 * Created by Salazar on 30-04-2016.
 */
public class Review {
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String userName;
    String courseName;
    String ratingBar1String;
    String ratingBar2String;
    String ratingBar3String;
    String ratingBar4String;
    String checkBox;
    String comment;


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRatingBar1String() {
        return ratingBar1String;
    }

    public void setRatingBar1String(String ratingBar1String) {
        this.ratingBar1String = ratingBar1String;
    }

    public String getRatingBar2String() {
        return ratingBar2String;
    }

    public void setRatingBar2String(String ratingBar2String) {
        this.ratingBar2String = ratingBar2String;
    }

    public String getRatingBar3String() {
        return ratingBar3String;
    }

    public void setRatingBar3String(String ratingBar3String) {
        this.ratingBar3String = ratingBar3String;
    }

    public String getRatingBar4String() {
        return ratingBar4String;
    }

    public void setRatingBar4String(String ratingBar4String) {
        this.ratingBar4String = ratingBar4String;
    }

    public String getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(String checkBox) {
        this.checkBox = checkBox;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
