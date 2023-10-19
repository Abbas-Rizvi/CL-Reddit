package Server;

public class test {

    public static void main(String[] args) {

        DatabaseConnect db = new DatabaseConnect("test.db");

        db.setupDatabase();
        db.registerUser("Jimmy", "123456");
        
        //db.registerUser("williams", "123456", "encrypt2");

        if (db.loginUser("Jimmy", "123456") == 0)
            System.out.println("Logged In!");
        else
            System.out.println("Login failed");

        db.listUsers();

        db.newPost("Jimmy", "DOG", "MONKEY MONKEY MONKEY MONKEY");
        System.out.println(db.upVotePost(2));
        db.listPosts();
    }
}
    
