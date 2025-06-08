package chess1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import peices.Peice;

public class Input extends MouseAdapter implements KeyListener{

    Board board;
    private boolean isDragging = false;

    public Input(Board board) {
        this.board = board;
        board.setFocusable(true);
        board.requestFocusInWindow();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        board.requestFocusInWindow();
        int col = e.getX() / board.tileSize;
        int row = e.getY() / board.tileSize;
        Peice clickedPeice = board.getPeice(col, row);

        if (board.selectedPeice == null) {
            if (clickedPeice != null) {
                board.selectedPeice = clickedPeice;
                board.isKingCheck = false;
                board.repaint(); // show valid moves
            }
        } else if (!isDragging) {
            // Click-to-move
            Move move = new Move(board, board.selectedPeice, col, row);
            if (board.validMove(move)) {
                try {
                    board.makeMove(move);
                    board.isKingCheck = false;
                } catch (SQLException ex) {
                    Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            board.selectedPeice = null;
            board.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.selectedPeice != null) {
            isDragging = true;
            board.selectedPeice.xpos = e.getX() - board.tileSize / 2;
            board.selectedPeice.ypos = e.getY() - board.tileSize / 2;
            board.isKingCheck = false;
            board.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isDragging && board.selectedPeice != null) {
            int col = e.getX() / board.tileSize;
            int row = e.getY() / board.tileSize;
            Move move = new Move(board, board.selectedPeice, col, row);
            if (board.validMove(move)) {
                try {
                    board.makeMove(move);
                    board.isKingCheck = false;
                } catch (SQLException ex) {
                    Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                // Snap back to original tile
                board.selectedPeice.xpos = board.selectedPeice.col * board.tileSize;
                board.selectedPeice.ypos = board.selectedPeice.row * board.tileSize;
            }
            board.selectedPeice = null;
            board.repaint();
        }
        isDragging = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            board.initPausePanel();
            
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
