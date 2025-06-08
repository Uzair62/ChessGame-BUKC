
package peices;

import chess1.Board;
import java.awt.image.BufferedImage;


public class Bishop extends Peice {
    
    public Bishop(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xpos = col*board.tileSize;
        this.ypos = row*board.tileSize;
        this.isWhite = isWhite;
        this.name = "Bishop";
        this.sprite = sheet.getSubimage(1*sheetScale, isWhite ? sheetScale1 + 20 : 40, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize - 10,BufferedImage.SCALE_SMOOTH );
    }
    
    @Override
    public boolean isValidMovement(int col, int row){
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }
    
    
    @Override
     public boolean moveCollidesWithPiece(int col, int row){
        //Up Left
        if(this.col > col && this.row > row)
            for(int i = 1; i < Math.abs(this.col - col); i++)
                if(board.getPeice(this.col-i, this.row-i) != null)
                        return true;
                    
        //Up Right
        if(this.col < col && this.row > row)
            for(int i = 1; i < Math.abs(this.col - col); i++)
                if(board.getPeice(this.col + i, this.row-i) != null)
                        return true;
        //Down left
        if(this.col < col && this.row < row)
            for(int i = 1; i < Math.abs(this.col - col); i++)
                if(board.getPeice(this.col + i, this.row + i) != null)
                        return true;
        //Down Right
        if(this.col > col && this.row < row)
            for(int i = 1; i < Math.abs(this.col - col); i++)
                if(board.getPeice(this.col - i, this.row + i) != null)
                        return true;
        
        return false;
    }
   
    
    
}
