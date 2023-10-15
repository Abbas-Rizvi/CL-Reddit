package Server;

import java.sql.*;

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

            // Create a table (You can modify this query according to your requirements)
            String createTableSQL = "CREATE TABLE IF NOT EXISTS tbl_user_logins ("
                    + "USERNAME TEXT PRIMARY KEY, "
                    + "PASSWORD TEXT, "
                    + "GPG_PUB TEXT)";

            statement.execute(createTableSQL);

            // Print the success message
            System.out.println("Sqlite database connected successfully.");

        } catch (SQLException e) {

            // print error
            System.out.println(e.getMessage());
        }

    }

    // --- registerUser ---
    // used to register new user
    // returns 0 for success, 1 if user already exists, 2 otherwise
    public int registerUser(String username, String password, String gpg) {

        // prepare insert statement
        String sqlString = "INSERT INTO tbl_user_logins (USERNAME, PASSWORD, GPG_PUB) VALUES (?, ?, ?)";

        // connect to database
        // create insert statement template
        try (Connection connection = this.connect();
                PreparedStatement prepStatement = connection.prepareStatement(sqlString)) {

            // input variables
            prepStatement.setString(1, username);
            prepStatement.setString(2, password);
            prepStatement.setString(3, gpg);

            try {

                // execute insert statement
                prepStatement.executeUpdate();

            } catch (SQLException e) {

                if (e.getErrorCode() == 19)
                    System.out.println("User " + username + " already exists!");
                else
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

        String sqlString = "SELECT USERNAME, password FROM tbl_user_logins WHERE username='" + username +"'";

        try (Connection connection = this.connect();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sqlString)) {

            // if (rs.getFetchSize() == 0) {
                // return 1;
            // }

            rs.next();
            System.out.println(rs.getString("USERNAME"));
            System.out.println(rs.getString("PASSWORD"));

            if (rs.getString("USERNAME").compareTo(username) == 0 &&
                    rs.getString("PASSWORD").compareTo(password) == 0) {

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

}