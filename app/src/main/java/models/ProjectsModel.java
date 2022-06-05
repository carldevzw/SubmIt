package models;

public class ProjectsModel {

    private String Topic, Level, ID, Supervisor;

    public ProjectsModel() {

    }

    public ProjectsModel(String topic, String level, String ID, String supervisor) {
        this.Topic = topic;
        this.Level = level;
        this.ID = ID;
        this.Supervisor = supervisor;

    }

    public String getTopic() {
        return Topic;
    }

    public String getLevel() {
        return Level;
    }

    public String getID() {
        return ID;
    }

    public String getSupervisor() {
        return Supervisor;
    }
}
