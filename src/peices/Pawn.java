
package peices;

import chess1.Board;
import java.awt.image.BufferedImage;

public class Pawn extends Peice {
    public Pawn(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xpos = col*board.tileSize;
        this.ypos = row*board.tileSize;
        this.isWhite = isWhite;
        this.name = "Pawn";
        this.sprite = sheet.getSubimage(5*sheetScale, isWhite ? sheetScale1 + 20 : 40, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize - 10,BufferedImage.SCALE_SMOOTH );
    }
    
    
    @Override
    public boolean isValidMovement(int col, int row){
        int colorIndex = isWhite? 1 : -1;
        
        //move one tile
        if(this.col == col && row == this.row - colorIndex && board.getPeice(col, row) == null){
            return true;
        }
        //first move
        if(isFisrtMove && this.col == col && row == this.row - 2*colorIndex && board.getPeice(col, row) == null && board.getPeice(col, row + colorIndex) == null){
            return true;
        }
        //capture right
        if(col == this.col + 1 && row == this.row - colorIndex && board.getPeice(col, row) != null){
            return true;
        }
        //capture left
        if(col == this.col - 1 && row == this.row - colorIndex && board.getPeice(col, row) != null){
            return true;
        }
    
        //Enpassant left
        if( board.getTileNum(col, row) == board.enpassantTile && col == this.col - 1 && row == this.row - colorIndex && board.getPeice(col, row + colorIndex) != null){
            return true;
        }
        //Enpassant Right
        if( board.getTileNum(col, row) == board.enpassantTile && col == this.col + 1 && row == this.row - colorIndex && board.getPeice(col, row + colorIndex) != null){
            return true;
        }
        return false;

    }
    
}
