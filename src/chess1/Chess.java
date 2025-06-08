
package chess1;

import abstractPackage.parentState;
import chessgame.StateManager;
import javax.swing.*;
import java.awt.*;
import userData.UserManager;

public class Chess extends parentState{
@Override
    public StateManager.GameState run(UserManager users, JFrame frame,StateManager state){
         
        frame.getContentPane().setBackground(new Color(20, 20, 30));
        
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        
        PlayerCards playerCard = new PlayerCards();
        playerCard.drawCards(frame, users,state);
//        frame.setLayout(new GridBagLayout());
        
        return state.menuState();
    
    }
    
}
