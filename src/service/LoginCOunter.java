package service;

public class LoginCOunter {
    public static int counter = 2;
    public static void countAction(){
        if(counter == 0){
            counter = 2;
            System.exit(0);
        }
        counter--;
    }
}
