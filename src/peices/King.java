
package peices;

import chess1.Board;
import chess1.Move;
import java.awt.image.BufferedImage;


public class King extends Peice {
     public King(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xpos = col*board.tileSize;
        this.ypos = row*board.tileSize;
        this.isWhite = isWhite;
        this.name = "King";
        this.sprite = sheet.getSubimage(3*sheetScale, isWhite ? sheetScale1 + 20 : 40, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize - 10,BufferedImage.SCALE_SMOOTH );
    }
     
     @Override
     public boolean isValidMovement(int col, int row){
         
        return Math.abs((col-this.col)*(row-this.row)) == 1 || Math.abs(col-this.col)+Math.abs(row-this.row) == 1 || canCastle(col, row);
    
     }
     
     private boolean canCastle(int col, int row){
         
         if(this.row == row){
             
             if(col == 6){
                 Peice rook = board.getPeice(7, row);
                 if(rook != null && rook.isFisrtMove && isFisrtMove){
                     return board.getPeice(5, row) == null &&
                            board.getPeice(6, row) == null &&
                            (!board.checkScanner.isKingChecked(new Move(board, this, 5, row)));
                            
                 }
             }else if(col == 2){
                 Peice rook = board.getPeice(0, row);
                 if(rook != null && rook.isFisrtMove && isFisrtMove){
                     return board.getPeice(3, row) == null &&
                            board.getPeice(2, row) == null &&
                            board.getPeice(1, row) == null &&
                           (!board.checkScanner.isKingChecked(new Move(board, this, 3, row)));
                            
                 }
             }
             
         }
         
         
         return false;
     }
    
}
