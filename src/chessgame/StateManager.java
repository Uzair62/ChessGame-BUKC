
package chessgame;

import static chessgame.StateManager.GameState.GAME_STATE;
import static chessgame.StateManager.GameState.LOGIN_STATE;
import static chessgame.StateManager.GameState.MENU_STATE;



public class StateManager {
    
public enum GameState{
 
    LOGIN_STATE,
    MENU_STATE,
    GAME_STATE
    
    
}    

    public GameState myStates;
     
    public GameState loginState(){
        return LOGIN_STATE;
    }
    
    
    public GameState menuState(){
        return MENU_STATE;
    }
    
    
    public GameState gameState(){
        return GAME_STATE;
    }
    
}
