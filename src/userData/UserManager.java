
package userData;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.sql.*;
public class UserManager {
    
    public ArrayList<PlayerInfo> players = new ArrayList<>();
    public PlayerInfo currentPlayer;
    public PlayerInfo opponentPlayer;
    private static int id;
    private Connection conn;
    private Statement stmt;    
    public UserManager(){
        try {
            // Access DB pathtry {
            // Load UCanAccess driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            // Get the path to database.accdb located in the same package
            URL dbURL = UserManager.class.getResource("UserInfo.accdb");

            if (dbURL == null) {
                throw new RuntimeException("database.accdb not found in userData package.");
            }

            String dbPath = Paths.get(dbURL.toURI()).toString();
            String url = "jdbc:ucanaccess://" + dbPath;

            // Connect
            this.conn = DriverManager.getConnection(url);
            this.stmt = conn.createStatement();

            // Query to read Name, Email, Password
            String query = "SELECT Name, Email, Password, Rating FROM Users";
            ResultSet rs = stmt.executeQuery(query);

            // Loop through results
            while (rs.next()) {
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String password = rs.getString("Password");
                int rating = rs.getInt("Rating");
                addUser(new PlayerInfo(name,email,password,rating));
                System.out.println("Name: " + name + ", Email: " + email + ", Password: " + password+ ", Rating: " + rating);
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentPlayer = null;
    }
    
    public void addUser(PlayerInfo player){
        id++;
        players.add(player);
        System.out.println("Value of id: "+id);
    }
    
    public void changeRating(PlayerInfo currentPlayer, PlayerInfo opponentPlayer) throws SQLException{
        String sql = "UPDATE Users SET Rating = ? WHERE Name = ?";
        
        // Update current player's rating only
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, currentPlayer.getRating());
        pstmt.setString(2, currentPlayer.getName());
        pstmt.executeUpdate();

        // Update opponent player's rating only
        pstmt.setInt(1, opponentPlayer.getRating());
        pstmt.setString(2, opponentPlayer.getName());
        pstmt.executeUpdate();

        System.out.println("Ratings updated successfully.");
    }
    
    public void addUserToDB(PlayerInfo player) throws SQLException{
        
        String sql = "INSERT INTO Users (Name, Email, Password, Rating) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, player.getName());
        pstmt.setString(2, player.getEmail());
        pstmt.setString(3, player.getPassword());
        pstmt.setInt(4, player.getRating());

        int rowsInserted = pstmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("User added successfully to DB.");
        }

        
    }
    
    public boolean searchUserEmail(String email){
        for(PlayerInfo player : players){
            if(player.checkEmail(email)){
                return false;
            }
        }
        
        return true;
    }
    
    public boolean searchUserName(String name){
        for(PlayerInfo player : players){
            if(player.checkName(name)){
                return false;
            }
        }
        
        return true;
    }
    
    
    
    public PlayerInfo checkUser(String nameEmail,String pass){
        for(PlayerInfo player : players){
            if(player.checkName(nameEmail) || player.checkEmail(nameEmail) ){
                if(player.checkPass(pass)){
                    return player;
                }
                else return null;
            }
        }
        return null;
    }
    
    
    public boolean checkPass(PlayerInfo player, String pass){
        return player.checkPass(pass);
    }
    
}
