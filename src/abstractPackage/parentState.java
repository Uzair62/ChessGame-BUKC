package abstractPackage;

import chessgame.StateManager;
import javax.swing.JFrame;
import userData.UserManager;


public abstract class parentState {
 
    public abstract StateManager.GameState run(UserManager use, JFrame frame,StateManager state);
    
}
