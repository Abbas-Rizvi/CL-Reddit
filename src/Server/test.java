package Server;

public class test {

    public static void main(String[] args) {

        DatabaseConnect db = new DatabaseConnect("test.db");

        db.setupDatabase();
        db.registerUser("Jeffery", "123456", "encrypt");
        //db.registerUser("williams", "123456", "encrypt2");

        if (db.loginUser("Jeffery", "123456") == 0)
            System.out.println("Logged In!");
        else
            System.out.println("Login failed");

        db.listUsers();
    }
}
    
