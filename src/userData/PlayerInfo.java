
package userData;

import java.awt.Image;
import javax.imageio.ImageIO;

public class PlayerInfo {
 public Image player;
 private String username;
 private String email;
 private String pass;
 private int score;
 public int rating;
 private static int id = 100;
 private int uid;
 public boolean saveGame;
 public PlayerInfo(String name, String email , String pass, int rating){
     
     this.email = email;
     this.pass = pass;
     this.score = 0;
     this.rating = rating;
     this.uid = ++id;
     this.username = name;    
     this.saveGame = false;
     try{
         player = ImageIO.read(getClass().getResource("/userData/player.jpg"));
     }catch(Exception e){
         System.out.println("Error loading image: " + e.getMessage());
     }
 }
 
 public String getName(){
     return this.username;
 }
 
 public String getPassword(){
     return this.pass;
 }
 
 public String getEmail(){
     return this.email;
 }
 
 public int getUid(){
     return this.uid;
 }
 
 public int getRating(){
    return this.rating;
 }
 
 public void addRating(int rating){
     
     this.rating+=rating;
     
 }
 public boolean checkName(String name){
     return this.username.equals(name);
 }
 
 public boolean checkEmail(String email){
     return this.email.equals(email);
 }
 
 public boolean checkPass(String pass){
     return this.pass.equals(pass);
 }
 
 
 
 public void addScore(int score){
     this.score += score;
 }
 
 public int getScore(){
     return this.score;
 }
 
 public void removeScore(int score){
     this.score += score;
 }
 
 public PlayerInfo getuser(){
     return this;
 }
 
}
