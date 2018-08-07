package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import service.DBConnect;
import service.DialogWindow;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Observable;

import static controller.LoginController.ID_user;

public class UserController {

    @FXML
    private ComboBox<String> combo_category;

    @FXML
    private ComboBox<String> combo_date;

    @FXML
    private ComboBox<String> combo_name;


    @FXML
    private RadioButton rb_normal;

    @FXML
    private ToggleGroup tg_feed;

    @FXML
    private RadioButton rb_vege;

    @FXML
    private RadioButton rb_gluten;

    @FXML
    private CheckBox cb_fv;

    @FXML
    private TextArea ta_fv;

    @FXML
    private Button btn_submit;

    @FXML
    private TextArea ta_agenda;

    //obiekty globalne
    DBConnect db;
    ObservableList<String> course_names = FXCollections.observableArrayList();
    ObservableList<String> course_categories = FXCollections.observableArrayList();
    ObservableList<String> dates = FXCollections.observableArrayList();
    private String category = "%";
    private String date = "%";

    @FXML
    void categoryFilter(ActionEvent event) throws SQLException {
        category = combo_category.getValue();
        externalFilter(category,date);
    }

    private void externalFilter(String category, String date) throws SQLException {
        PreparedStatement ps;
        Connection conn = db.getCon();

        ps = conn.prepareStatement("SELECT distinct course_name FROM course JOIN edition " +
        "ON(id_k = course_id_k1) WHERE course_category like ? AND date like ? ");
        ps.setString(1,category);
        ps.setString(2,date);
        ResultSet result = ps.executeQuery();
        course_names.clear();
        while (result.next()) {
            course_names.add(result.getString("course_name"));
        }
        combo_name.setItems(course_names);

    }

    @FXML
    void dateFilter(ActionEvent event) throws SQLException {
    date = combo_date.getValue();
    externalFilter(category,date);
    }

    @FXML
    void fcAction(MouseEvent event) {
        if(cb_fv.isSelected()){
            ta_fv.setDisable(false);
        }
        else{
            ta_fv.clear();
            ta_fv.setDisable(true);
        }

    }

    @FXML
    void nameAction(ActionEvent event) throws SQLException {
        String name = combo_name.getValue();
        Connection conn = db.getCon();
        PreparedStatement ps = conn.prepareStatement("SELECT course_agenda FROM course WHERE course_name = ?");
        ps.setString(1,name);
        ResultSet result = ps.executeQuery();
        if(result.next()){
            ta_agenda.setText(result.getString("course_agenda"));
        }

    }

    @FXML
    void submitAction(MouseEvent event) throws SQLException, FileNotFoundException {
        if (combo_name.getValue() == null) {
            DialogWindow dw = new DialogWindow(Alert.AlertType.WARNING, "Błąd",
                    "Nie wybrano kursu", "Musisz wybrać kurs");

        } else {
            System.out.println("Zapis!");
            db = new DBConnect();
            Connection conn = db.getCon();

            String feed = "normalne";
            if(rb_vege.isSelected()){
                feed="wegetariańskie";
            }
            else if(rb_gluten.isSelected()){
                feed = "bezglutenowe";
            }

            int fv = 0;
            if(cb_fv.isSelected()){
                fv = 1;
            }

            PreparedStatement ps;
            ps = conn.prepareStatement("insert into submission(users_id_user, course_id_k, feed,fv_decsision, fv_details)" +
                    " values (?," +
"(SELECT id_k from course WHERE course_name = ?), " +
        "?, " +
        "?, " +
        " ?)");

            ps.setInt(1,ID_user);
            ps.setString(2,combo_name.getValue());
            ps.setString(3,feed);
            ps.setInt(4,fv);
            ps.setString(5,ta_fv.getText());
            ps.executeUpdate();
            conn.commit();

            LocalDateTime a = LocalDateTime.now();
            String as = a.toString().replaceAll(":","");

            PrintWriter pw = new PrintWriter("C:\\Users\\Pawel\\Desktop\\JavaProject\\src\\files\\"+as+".txt");
            pw.println(combo_name.getValue() +" "+feed+" "+fv+" "+ta_fv.getText()+"\n");
            pw.close();
            System.out.println(java.time.LocalDate.now());

        }
        DialogWindow dw = new DialogWindow(Alert.AlertType.INFORMATION,"ZAPISANO","Pomyślnie zapisano na kurs","");
    }

    public void initialize() throws SQLException {
        db = new DBConnect();
        Connection conn = db.getCon();
        PreparedStatement ps;
        ps = conn.prepareStatement("SELECT DISTINCT course_category FROM course");
        ResultSet result1 = ps.executeQuery();
        while (result1.next()) {
            course_categories.add(result1.getString("course_category"));
        }
        combo_category.setItems(course_categories);


    db = new DBConnect();
    ps = conn.prepareStatement("SELECT DISTINCT date FROM edition");
    ResultSet result_date = ps.executeQuery();
        while (result_date.next()) {
        dates.add(result_date.getString("date"));
    }
        combo_date.setItems(dates);

    }
}