
package peices;

import chess1.Board;
import java.awt.image.BufferedImage;


public class Knight extends Peice {
    public Knight(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xpos = col*board.tileSize;
        this.ypos = row*board.tileSize;
        this.isWhite = isWhite;
        this.name = "Knight";
        this.sprite = sheet.getSubimage(4*sheetScale, isWhite ? sheetScale1 + 20 : 40, sheetScale, sheetScale).getScaledInstance(board.tileSize, board.tileSize - 10,BufferedImage.SCALE_SMOOTH );
    }
    
    @Override
    public boolean isValidMovement(int col, int row){
        return Math.abs(col - this.col)*Math.abs(row - this.row) == 2;
    }
}
