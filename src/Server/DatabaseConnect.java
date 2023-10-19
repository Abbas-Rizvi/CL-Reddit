package Server;

import java.sql.*;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class DatabaseConnect {

    private String dbName;
    private String username;

    public DatabaseConnect(String dbName) {
        dbName = this.dbName;
    }

    public Connection connect() {

        // create connection object
        // link to working dir
        String db_url = "jdbc:sqlite:" + dbName;
        Connection connection = null;

        try {

            // Register SQLite driver
            Class.forName("org.sqlite.JDBC");

            try {
                connection = DriverManager.getConnection(db_url);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (ClassNotFoundException e) {

            // print error
            System.out.println(e.getMessage());

        }

        return connection;
    }

    // -- setupDatabase --
    // check for existing database
    // if none exists, create one
    public void setupDatabase() {

        try (Connection connection = this.connect();
                Statement statement = connection.createStatement()) {

            // Create a table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS tbl_user_logins ("
                    + "USERNAME TEXT PRIMARY KEY, "
                    + "PASSWORD TEXT, "
                    + "GPG_PUB TEXT)";

            statement.execute(createTableSQL);

            // Create a table
            createTableSQL = "CREATE TABLE IF NOT EXISTS tbl_posts ("
                    + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "USERNAME TEXT, "
                    + "SCORE INT,"
                    + "SUBJECT TEXT,"
                    + "BODY TEXT)";

            statement.execute(createTableSQL);

            // Print the success message
            System.out.println("Sqlite database setup successfully.");

        } catch (SQLException e) {

            // print error
            System.out.println(e.getMessage());
        }

    }

    // --- registerUser ---
    // used to register new user
    // returns 0 for success, 1 if user already exists, 2 otherwise
    public int registerUser(String username, String password) {

        // prepare insert statement
        String sqlString = "INSERT INTO tbl_user_logins (USERNAME, PASSWORD) VALUES (?, ?)";
        String hashPswd = BCrypt.hashpw(password, BCrypt.gensalt());

        // connect to database
        // create insert statement template
        try (Connection connection = this.connect();
                PreparedStatement prepStatement = connection.prepareStatement(sqlString)) {

            // input variables
            prepStatement.setString(1, username);
            prepStatement.setString(2, hashPswd);

            try {

                // execute insert statement
                prepStatement.executeUpdate();

            } catch (SQLException e) {

                if (e.getErrorCode() == 19) {
                    System.out.println("User " + username + " already exists!");
                    return 2;
                } else
                    System.out.println(e.getMessage());

                return 1;
            }

            // Print the success message
            System.out.println("User has been registered!");
            return 0;

        } catch (SQLException e) {

            // print error
            System.out.println(e.getMessage());
        }

        // return fail
        return 2;

    }

    public int checkUserExist(String username) {

        return 0;
    }

    public int loginUser(String username, String password) {

        String sqlString = "SELECT USERNAME, password FROM tbl_user_logins WHERE username='" + username + "'";
        String hashPswd = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection connection = this.connect();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sqlString)) {

            rs.next();

            if (rs.getString("USERNAME") == null) {
                System.out.println("User " + username + " does not exist");
                return 1;
            }

            if (rs.getString("USERNAME").compareTo(username) == 0 &&
                    BCrypt.checkpw(password, rs.getString("PASSWORD"))) {

                // rs.getString("PASSWORD").compareTo(hashPswd) == 0) {

                System.out.println("User " + username + " has been logged in!");
                this.username = username;
                return 0;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 1;
    }

    public void listUsers() {

        String sqlString = "SELECT username FROM tbl_user_logins";

        try (Connection connection = this.connect();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sqlString)) {

            while (rs.next()) {
                System.out.println(
                        rs.getString("username") + "\t");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public int newPost(String username, String subject, String body) {

        // prepare insert statement
        String sqlString = "INSERT INTO tbl_posts (USERNAME, SCORE, SUBJECT, BODY) VALUES (?, ?, ?, ?)";

        // connect to database
        // create insert statement template
        try (Connection connection = this.connect();
                PreparedStatement prepStatement = connection.prepareStatement(sqlString)) {

            // input variables
            prepStatement.setString(1, username);
            prepStatement.setInt(2, 1);
            prepStatement.setString(3, subject);
            prepStatement.setString(4, body);

            try {

                // execute insert statement
                prepStatement.executeUpdate();

            } catch (SQLException e) {

                System.out.println(e.getMessage());

                return 1;
            }

            // Print the success message
            System.out.println("Post has been added!");
            return 0;

        } catch (SQLException e) {

            // print error
            System.out.println(e.getMessage());
        }

        // return fail
        return 1;

    }

    public String[] listPosts() {

        String sqlString = "SELECT ID, USERNAME, SCORE, SUBJECT, BODY FROM tbl_posts";

        try (Connection connection = this.connect();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sqlString)) {

            ArrayList<String> listItems = new ArrayList<String>();
            String merged = "";

            while (rs.next()) {
                merged += (rs.getString("ID") + ";"
                        + rs.getString("USERNAME") + ";"
                        + rs.getString("SCORE") + ";"
                        + rs.getString("SUBJECT") + ";"
                        + rs.getString("BODY"));

                listItems.add(merged);
                merged = "";

            }

            // while (rs.next()) {
            // System.out.println(
            // rs.getString("ID") + "\t"
            // + rs.getString("USERNAME") + "\t\t"
            // + rs.getString("SUBJECT") + "\t\t"
            // + rs.getString("SCORE") + "\t");
            // }

            return listItems.toArray(new String[listItems.size()]);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public int upVotePost(int postNum) {

        String sql = "UPDATE tbl_posts SET "
                + "score = score + 1 "
                + "WHERE id = ?";
        try (Connection conn = this.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            stmt.setInt(1, postNum);

            // update
            stmt.executeUpdate();

            return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 1;

    }

}
