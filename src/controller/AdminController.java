package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Course;
import model.Submission;
import model.User;
import service.DBConnect;
import service.DialogWindow;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminController {
    //Obiekty globalne
    DBConnect db;
    PreparedStatement ps;
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private ObservableList<Submission> submissionList = FXCollections.observableArrayList();


    @FXML
    private TableView<User> tbl_users;

    @FXML
    private TableColumn<User, String> col_users_name;

    @FXML
    private TableColumn<User, String> col_users_lastname;

    @FXML
    private TableColumn<User, String> col_users_login;

    @FXML
    private TableColumn<User, String> col_users_password;

    @FXML
    private Button btn_delete_user;

    @FXML
    private Button btn_change_password;

    @FXML
    private TextField tf_new_password;

    @FXML
    private TextField tf_new_password2;

    @FXML
    private Button btn_confirm;

    @FXML
    void change_user_passwordAction(MouseEvent event) {
    btn_change_password.setDisable(true);
    btn_delete_user.setDisable(true);
    tf_new_password.setVisible(true);
    tf_new_password2.setVisible(true);
    btn_confirm.setVisible(true);

    }

    @FXML
    void confirm_userAction(MouseEvent event) throws SQLException {
        db = new DBConnect();
        Connection conn = db.getCon();
        if(tf_new_password.getText().equals(tf_new_password2.getText())) {
            ps = conn.prepareStatement("UPDATE users SET password = ? WHERE login =?");
            ps.setString(1,tf_new_password.getText());
            ps.setString(2,tbl_users.getSelectionModel().getSelectedItem().getLogin());
            ps.executeUpdate();
            conn.commit();
            globalSelect();
        }
        else{
            tf_new_password.clear();
            tf_new_password2.clear();
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR,"Błąd","Błędne hasło","Wprowadź 2 takie same hasłą");
        }
//        ps.setString(1,tbl_users.getSelectionModel().getSelectedItem().getLogin());
//        ps.executeUpdate();
//        conn.commit();
//        globalSelect();

        btn_change_password.setDisable(false);
        btn_delete_user.setDisable(false);
        tf_new_password.setVisible(false);
        tf_new_password2.setVisible(false);
        btn_confirm.setVisible(false);

    }

    @FXML
    void delete_user_Action(MouseEvent event) throws SQLException {
    db = new DBConnect();
    Connection conn = db.getCon();
    ps = conn.prepareStatement("DELETE FROM users WHERE login =?");
    ps.setString(1,tbl_users.getSelectionModel().getSelectedItem().getLogin());
    ps.executeUpdate();
    conn.commit();
    globalSelect();
    }

    @FXML
    void menuAuthors(ActionEvent event) {
        DialogWindow dw = new DialogWindow(
                Alert.AlertType.INFORMATION,
                "O autorach",
                "Autorzy aplikacji",
                "Grupa kuranstów REAKTOR PWN"
        );
    }

    @FXML
    void menuInstruction(ActionEvent event) {
        DialogWindow dw = new DialogWindow(
                Alert.AlertType.INFORMATION,
                "Instrukcja",
                "Instrukcja korzystania z aplikacji",
                "1. Logowania \n 2. Zapis na kurs \n 3. \n 4."
        );
    }

    @FXML
    void menu_logout(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) btn_confirm.getScene().getWindow();
        currentStage.close();
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginView.fxml"));
        Scene scene = new Scene(root);
        Stage loginStage = new Stage();
        loginStage.setScene(scene);
        loginStage.setTitle("Panel logowania");
        loginStage.show();
    }

    //uzupelnienie kategorii w combobox name
    private void globalSelect() throws SQLException {
        userList.clear();
        db = new DBConnect();
        Connection conn = db.getCon();
        ps = conn.prepareStatement("SELECT name,lastname,login,password FROM users");
        ResultSet result = ps.executeQuery();
        System.out.println("XXX");
        //wypełnienie zawartości w userList
        while(result.next()){
            User user = new User(result.getString("name"),result.getString("lastname"),
                    result.getString("login"),result.getString("password"));
            userList.add(user);
        }
        //uzupelnienie zawartosci  tableColumn
        col_users_name.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        col_users_lastname.setCellValueFactory(new PropertyValueFactory<User, String>("lastname"));
        col_users_login.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        col_users_password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));

        //wprowadzenie obiektów klasy modelu do tableVIEW
        tbl_users.setItems(userList);
    }


    @FXML
    private Button btn_insert_course;

    @FXML
    private TextArea ta_agenda;

    @FXML
    private TableView<Course> tbl_course;

    @FXML
    private TableColumn<Course,String> col_course_name2;

    @FXML
    private TableColumn<Course,String> col_course_agenda;

    @FXML
    private TableColumn<Course,String> col_course_category;

    @FXML
    private TableColumn<Course,String> col_course_data;

    @FXML
    private TextField tf_course_name;

    @FXML
    private TextField tf_course_category;

    @FXML
    private DatePicker dp_course_date;

    @FXML
    private Button btn_confirm_submission;

    @FXML
    private TableView<Submission> tbl_course1;

    @FXML
    private TableColumn<Submission,String> col_zapis_log;

    @FXML
    private TableColumn<Submission,String> col_zapis_nazwa;

    @FXML
    private TableColumn<Submission,String> col_zapis_date;

    @FXML
    private TableColumn<Submission,Integer> col_zapis_conf;


    @FXML
    void btnActivation(MouseEvent event) {
    }


    @FXML
    void confirmAction(MouseEvent event) throws SQLException {
        db = new DBConnect();
        Connection conn = db.getCon();
        ps = conn.prepareStatement("UPDATE submission SET confirm = 1 WHERE login =?");
        ps.setString(1,tbl_course1.getSelectionModel().getSelectedItem().getLogin());
        ps.executeUpdate();
        conn.commit();
        tbl_course1.refresh();
        globalSubmissionSelect();
    }

    @FXML
    void submissionAction(MouseEvent event) {
        try {
            if (tbl_course1.getSelectionModel().getSelectedItem().getConfirm() == 1) {
                btn_confirm_submission.setVisible(false);
            } else if (tbl_course1.getSelectionModel().getSelectedItem().getConfirm() == 0) {
                btn_confirm_submission.setVisible(true);
            }
        }
        catch (NullPointerException e){
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR, "Błąd","Błąd danych",
                    "Musisz wybrać rekord do zmiany statusu");
        }

    }



    @FXML
    void add_course_action(MouseEvent event) throws SQLException {
        String date = new String();
        date = String.valueOf(dp_course_date.getValue());
      //  System.out.println(date);
        if(tf_course_name.getText().equals("") || tf_course_category.getText().equals("")
                || ta_agenda.getText().equals("") || date.equals("null")){
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR,"ERROR",
                    "Puste pole","Wypełnij wszystkie pola");
        }else {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Potwierdzenie dodania kursu");
            confirmDialog.setHeaderText("Czy na pewno chcesz dodać kurs?");
            confirmDialog.setContentText("Jeśli chcesz dodać kurs, wybierz OK, jeśli nie - anuluj");
            Optional<ButtonType> decision = confirmDialog.showAndWait();

            if(decision.get() == ButtonType.OK) {
                db = new DBConnect();
                Connection conn = db.getCon();
                //dopis do tabelki course
                ps = conn.prepareStatement(
                        "INSERT into course(course_name, course_agenda, course_category) " +
                                "VALUES (?, ? ,?)");
                ps.setString(1, tf_course_name.getText());
                ps.setString(2, ta_agenda.getText());
                ps.setString(3, tf_course_category.getText());
                ps.executeUpdate();
                //dopis do tabelki edition
                ps = conn.prepareStatement(
                        "INSERT into edition(date, course_id_k1) " +
                                "VALUES (? , (SELECT max(id_k) FROM course))");
                ps.setString(1, date);
                ps.executeUpdate();
                conn.commit();
                tf_course_name.clear();
                tf_course_category.clear();
                ta_agenda.clear();
                globalCourseSelect();

            }
        }
    }


    private void globalCourseSelect() throws SQLException {
        courseList.clear();
        db = new DBConnect();
        Connection conn = db.getCon();
        ps = conn.prepareStatement("SELECT course_name,course_agenda,course_category, date FROM course\n" +
                "join edition on (course.ID_k = edition.course_ID_k1)");
        ResultSet result = ps.executeQuery();
        System.out.println("XXX");
        //wypełnienie zawartości w userList
        while(result.next()){
            Course course = new Course(result.getString("course_name"),result.getString("course_agenda"),
                    result.getString("course_category"),result.getString("date"));
            courseList.add(course);
        }
        //uzupelnienie zawartosci  tableColumn
        col_course_name2.setCellValueFactory(new PropertyValueFactory<Course, String>("course_name"));
        col_course_agenda.setCellValueFactory(new PropertyValueFactory<Course, String>("course_agenda"));
        col_course_category.setCellValueFactory(new PropertyValueFactory<Course, String>("course_category"));
        col_course_data.setCellValueFactory(new PropertyValueFactory<Course, String>("date"));

        //wprowadzenie obiektów klasy modelu do tableVIEW
        tbl_course.setItems(courseList);
    }


    private void globalSubmissionSelect() throws SQLException {
        submissionList.clear();
        db = new DBConnect();
        Connection conn = db.getCon();
        ps = conn.prepareStatement("SELECT login, course_name,date, confirm " +
                "FROM users JOIN submission on(id_user = users_ID_user) " +
                "JOIN course on(id_k = submission.course_ID_k) " +
                "                JOIN edition on (id_k = edition.course_ID_k1) " +
                "                order by confirm ASC;");
        ResultSet result = ps.executeQuery();
        //wypełnienie zawartości w userList
        while(result.next()){
            Submission submission = new Submission(result.getString("login"),result.getString("course_name"),
                    result.getString("date"), result.getInt("confirm"));
            submissionList.add(submission);
        }
        //uzupelnienie zawartosci  tableColumn
        col_zapis_log.setCellValueFactory(new PropertyValueFactory<Submission, String>("login"));
        col_zapis_nazwa.setCellValueFactory(new PropertyValueFactory<Submission, String>("nazwa"));
        col_zapis_date.setCellValueFactory(new PropertyValueFactory<Submission, String>("data"));
        col_zapis_conf.setCellValueFactory(new PropertyValueFactory<Submission, Integer>("confirm"));

        //wprowadzenie obiektów klasy modelu do tableVIEW
        tbl_course1.setItems(submissionList);
    }




    public void initialize() throws SQLException {
        globalSelect();
        globalCourseSelect();
        globalSubmissionSelect();
    }
}
