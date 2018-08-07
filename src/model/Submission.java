package model;

public class Submission {
    private String login;
    private String nazwa;
    private String data;
    private Integer confirm;

    public Submission() {
    }

    public Submission(String login, String nazwa, String data, Integer confirm) {
        this.login = login;
        this.nazwa = nazwa;
        this.data = data;
        this.confirm = confirm;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getConfirm() {
        return confirm;
    }

    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
    }
}