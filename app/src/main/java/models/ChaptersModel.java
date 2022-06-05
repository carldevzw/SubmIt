package models;

public class ChaptersModel {

    private String Name, Number, URL;
    private boolean Reviewed;

    public ChaptersModel(String chaptName, String chaptNum, String URL, boolean revStauts) {
        this.Name = chaptName;
        this.Number = chaptNum;
        this.URL= URL;
        this.Reviewed = revStauts;
    }



    public ChaptersModel() {

    }

    public String getURL() {
        return URL;
    }

    public String getName() {
        return Name;
    }

    public String getNumber() {
        return Number;
    }

    public boolean isReviewed() {
        return Reviewed;
    }
}
