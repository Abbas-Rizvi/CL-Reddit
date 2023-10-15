package Server;

public class test {

    public static void main(String[] args) {

        DatabaseConnect db = new DatabaseConnect("test.db");

        db.setupDatabase();
        db.registerUser("Jeffery", "123456", "encrypt");
        //db.registerUser("williams", "123456", "encrypt2");

        db.loginUser("Jeffery", "123456");

        db.listUsers();
    }
}
    
