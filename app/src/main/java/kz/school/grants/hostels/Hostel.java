package kz.school.grants.hostels;

public class Hostel {

    private String hostelId;
    private String hostelImage;
    private String hostelRating;
    private String hostelName;
    private String hostelPrice;
    private String hostelLocation;
    private String hostelGender;

    public Hostel() {

    }

    public Hostel(String hostelId, String hostelImage, String hostelRating, String hostelName, String hostelPrice, String hostelLocation, String hostelGender) {
        this.hostelId = hostelId;
        this.hostelImage = hostelImage;
        this.hostelRating = hostelRating;
        this.hostelName = hostelName;
        this.hostelPrice = hostelPrice;
        this.hostelLocation = hostelLocation;
        this.hostelGender = hostelGender;
    }

    public String getHostelId() {
        return hostelId;
    }

    public void setHostelId(String hostelId) {
        this.hostelId = hostelId;
    }

    public String getHostelImage() {
        return hostelImage;
    }

    public void setHostelImage(String hostelImage) {
        this.hostelImage = hostelImage;
    }

    public String getHostelRating() {
        return hostelRating;
    }

    public void setHostelRating(String hostelRating) {
        this.hostelRating = hostelRating;
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public String getHostelPrice() {
        return hostelPrice;
    }

    public void setHostelPrice(String hostelPrice) {
        this.hostelPrice = hostelPrice;
    }

    public String getHostelLocation() {
        return hostelLocation;
    }

    public void setHostelLocation(String hostelLocation) {
        this.hostelLocation = hostelLocation;
    }

    public String getHostelGender() {
        return hostelGender;
    }

    public void setHostelGender(String hostelGender) {
        this.hostelGender = hostelGender;
    }
}
