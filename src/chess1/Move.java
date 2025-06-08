
package chess1;

import peices.Peice;

public class Move {
    
    int oldCol;
    int oldRow;
    int newCol;
    int newRow;
    Peice capture;
    Peice peice;
    public Move(Board board, Peice peice,int newCol, int newRow){
        this.newCol = newCol;
        this.newRow = newRow;
        this.oldCol = peice.col;
        this.oldCol = peice.row;
        this.peice = peice;
        this.capture = board.getPeice(newCol, newRow);
    }
    
    
}
