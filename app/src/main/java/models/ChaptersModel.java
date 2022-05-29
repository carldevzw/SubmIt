package models;

public class ChaptersModel {

    private String chaptName, chaptNum;
    private boolean revStauts;

    public ChaptersModel(String chaptName, String chaptNum, boolean revStauts) {
        this.chaptName = chaptName;
        this.chaptNum = chaptNum;
        this.revStauts = revStauts;
    }

    public String getChaptName() {
        return chaptName;
    }

    public String getChaptNum() {
        return chaptNum;
    }

    public boolean isRevStauts() {
        return revStauts;
    }
}
