package controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.DBConnect;
import service.DialogWindow;
import service.LoginCOunter;


import java.io.IOException;
import java.sql.*;

public class LoginController {
    DBConnect dao;
    static int ID_user = 0;
        @FXML
        private TextField tf_login;

        @FXML
        private TextField tf_password;

        @FXML
        private PasswordField pf_password;

        @FXML
        private CheckBox cb_show;

        @FXML
        private Button btn_login;

        @FXML
        void loginAction(MouseEvent event) throws SQLException, IOException {
            externalLoginAction();
        }

        @FXML
        void showPasswordAction(MouseEvent event) {
            if(cb_show.isSelected()){
                //System.out.println("Zaznaczony");
                String pass = pf_password.getText();
                tf_password.setVisible(true);
                pf_password.setVisible(false);
                tf_password.setText(pass);
                pf_password.clear();
            }
            else{

                //System.out.println("Odznaczony");
                tf_password.setVisible(false);
                pf_password.setVisible(true);
                pf_password.setText(tf_password.getText());
                tf_password.clear();

                //przygotowanie zapytania do logowania

            }

        }



    @FXML
    void keyLoginAction(KeyEvent event) throws SQLException, IOException {
            if(event.getCode().equals(KeyCode.ENTER)) {
                externalLoginAction();
            }
    }

    private void externalLoginAction() throws SQLException, IOException {
            String password = new String();
            Stage currentStage = (Stage) btn_login.getScene().getWindow();  //Dostep do aktualnego okna
            if(cb_show.isSelected()){
                password = tf_password.getText();
            }
            else{
                password = pf_password.getText();
            }

        PreparedStatement ps=conn.prepareStatement("SELECT permission, ID_user from users WHERE login = ? AND password = ?");
        ps.setString(1, tf_login.getText());
        ps.setString(2, password);
        ResultSet result = ps.executeQuery();
        int permission = -1;
        if(result.next()){
            permission = result.getInt("permission");
            ID_user = result.getInt("ID_user");
        }

        if(permission == 1){
            System.out.println("panel administratora");
            LoginCOunter.counter = 2;
            Parent root = FXMLLoader.load(getClass().getResource("/view/adminView.fxml"));
            Scene scene = new Scene(root);
            Stage adminStage = new Stage();
            adminStage.setScene(scene);
            adminStage.setTitle("Panel administratora");
            adminStage.show();
            currentStage.close();

        }

        else if(permission == 0){
            LoginCOunter.counter = 2;
            Parent root = FXMLLoader.load(getClass().getResource("/view/userView.fxml"));
            Scene scene = new Scene(root);
            Stage userStage = new Stage();
            userStage.setScene(scene);
            userStage.setTitle("Panel uzytkownika");
            userStage.show();
            currentStage.close();
        }
        else{
            LoginCOunter.countAction();
            DialogWindow dw = new DialogWindow(Alert.AlertType.WARNING,"Błąd","Błąd logowania",
                    "Błędny login lub hasło");
            System.out.println("Błąd logowania");
        }

/*
        if(tf_login.getText().equals("admin") && (tf_password.getText().equals("admin") || pf_password.getText().equals("admin"))){
            System.out.println("admin");
        }

        else if(tf_login.getText().equals("user") && (tf_password.getText().equals("user") || pf_password.getText().equals("user"))){
            System.out.println("user");
        }
        else{
            System.out.println("błąd logowania");
        }
       */
    }
    Connection conn;
    public void initialize(){
        dao = new DBConnect();
        conn = dao.getCon();
    }


}
