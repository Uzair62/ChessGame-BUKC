package chessgame;

import abstractPackage.parentState;
import chess1.Chess;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import loginPackage.LoginState;
import userData.UserManager;
import java.awt.Image;
import java.io.IOException;
import menu.MenuState;
import chessgame.StateManager;
import static chessgame.StateManager.GameState.GAME_STATE;
import static chessgame.StateManager.GameState.LOGIN_STATE;
import static chessgame.StateManager.GameState.MENU_STATE;



public class ChessGame {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Chess Game");
        frame.setSize(1920, 1080);

        // ✅ Load and set logo icon
        try {
            // Assumes logo.png is in the same package (chessgame)
            Image logo = ImageIO.read(ChessGame.class.getResource("logo.jpg"));
            frame.setIconImage(logo);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        
        StateManager state = new StateManager();
        state.myStates = LOGIN_STATE;
        UserManager users = new UserManager();
        parentState chessState = new Chess();
        parentState menuState = new MenuState();
        parentState loginState = new LoginState();
        
        Stack<parentState> states = new Stack<>();
        
        
        
        
        while(true){
            if(state.myStates == LOGIN_STATE){
                states.add(loginState);
                state.myStates = states.peek().run(users, frame, state);
                states.pop();
            }
            else if(state.myStates == MENU_STATE){
                states.add(menuState);
                state.myStates = states.peek().run(users, frame, state);
                states.pop();
            }
            else if(state.myStates == GAME_STATE){
                
                states.add(chessState);
                state.myStates = states.peek().run(users, frame, state);
                //states.pop();
                if(state.myStates == MENU_STATE){
                    System.out.println("MENU");
                }else if(state.myStates == LOGIN_STATE){
                    System.out.println("LOGIN");
                }
                else if(state.myStates == GAME_STATE){
                    System.out.println("GAME");
                }
            }
        }
        
        
        
    }
}
