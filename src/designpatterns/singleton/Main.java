package designpatterns.singleton;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DatabaseConnectionV1 db = new DatabaseConnectionV1();
        DatabaseConnectionV1 db2 = new DatabaseConnectionV1();
        System.out.println(db == db2);
//      DatabaseConnection_V2 db3= new DatabaseConnection_V2(); //Private constructor hence getting error;
        DatabaseConnectionV3 db4= DatabaseConnectionV3.getConnection();
        DatabaseConnectionV3 db5= DatabaseConnectionV3.getConnection();
        System.out.println(db4 == db5);
    }
}