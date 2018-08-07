package model;

public class Course {
    private String course_name;
    private String course_agenda;
    private String course_category;
    private String date;

    public Course() {
    }

    public Course(String course_name, String course_agenda, String course_category, String date) {
        this.course_name = course_name;
        this.course_agenda = course_agenda;
        this.course_category = course_category;
        this.date = date;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_agenda() {
        return course_agenda;
    }

    public void setCourse_agenda(String course_agenda) {
        this.course_agenda = course_agenda;
    }

    public String getCourse_category() {
        return course_category;
    }

    public void setCourse_category(String course_category) {
        this.course_category = course_category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
