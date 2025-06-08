
package peices;

import chess1.Board;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Peice {
    
    public int col , row;
    public int xpos , ypos;
    public boolean isWhite;
    public String name;
    public int value;
    public boolean isFisrtMove = true;
    BufferedImage sheet;
    {
    try{
    
        String packagePath = "/peices/";
            sheet = ImageIO.read(getClass().getResource(packagePath +"peices.png"));


        }catch(IOException e){
    
            e.printStackTrace();

        }
    }
    Image sprite;
    Board board;
    protected int sheetScale = sheet.getWidth()/6;
    protected int sheetScale1 = sheet.getHeight()/2;
    public Peice(Board board){
    
        
        this.board = board;
    
    }
    
    public boolean isValidMovement(int col, int row){
        return true;
    }
    
    public boolean moveCollidesWithPiece(int col, int row){
        return false;
    }
    
    public void paint(Graphics2D g2d){
        g2d.drawImage(sprite, xpos, ypos,null);
    }

    
    
    
}
