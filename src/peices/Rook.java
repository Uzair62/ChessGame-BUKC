
package peices;

import chess1.Board;
import java.awt.image.BufferedImage;


public class Rook extends Peice {
    
    public Rook(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xpos = col*board.tileSize;
        this.ypos = row*board.tileSize;
        this.isWhite = isWhite;
        this.name = "Rook";
        this.sprite = sheet.getSubimage(0*sheetScale, isWhite ? sheetScale1 + 20 : 40, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize - 10,BufferedImage.SCALE_SMOOTH );
    }
    
    @Override
    public boolean isValidMovement(int col, int row){
        return this.col == col || this.row == row;
    }
    
    @Override
    public boolean moveCollidesWithPiece(int col, int row){
        //Right
        if(this.col > col)
            for(int c = this.col - 1; c > col; c--)
                if(board.getPeice(c, this.row) != null)
                    return true;
        //Left
        if(this.col < col)
            for(int c = this.col + 1; c < col; c++)
                if(board.getPeice(c, this.row) != null)
                    return true;
        //Up
        if(this.row > row)
            for(int r = this.row - 1; r > row; r--)
                if(board.getPeice(this.col, r) != null)
                    return true;
        //Down
        if(this.row < row)
            for(int r = this.row + 1; r < row; r++)
                if(board.getPeice(this.col, r) != null)
                    return true;
        
        return false;
    }
    
    
}
