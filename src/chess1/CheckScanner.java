
package chess1;

import peices.Peice;





public class CheckScanner {

Board board;

public CheckScanner(Board board){
    this.board = board;
}


public boolean isKingChecked(Move move){
    
    
    Peice king = board.findKing(move.peice.isWhite);
    assert king != null;
    
    int kingCol = king.col;
    int kingRow = king.row;
    
    if(board.selectedPeice != null && board.selectedPeice.name.equals("King")){
        kingCol = move.newCol;
        kingRow = move.newRow;
    }
    
    
    
    return  hitByRook  (move.newCol, move.newRow, king, kingCol, kingRow, 0, 1) || //up
            hitByRook  (move.newCol, move.newRow, king, kingCol, kingRow, 1, 0) || //right
            hitByRook  (move.newCol, move.newRow, king, kingCol, kingRow, 0, -1) || //down
            hitByRook  (move.newCol, move.newRow, king, kingCol, kingRow, -1, 0) || //left
            
            hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, -1) || // upLeft
            hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, -1) || // upRight
            hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, 1) || // downright
            hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, 1) || // downleft
            
            hitByKnight(move.newCol, move.newRow, king, kingCol, kingRow) ||
            hitByPawn(move.newCol, move.newRow, king, kingCol, kingRow) ||
            hitByKing(king, kingCol, kingRow) ;
            
}

    

private boolean hitByRook(int col, int row, Peice king, int kingCol, int kingRow, int colVal, int rowVal){
    for(int i = 1; i < 8; i++){
        if(kingCol + (i * colVal) == col && kingRow + (i * rowVal) == row){
            break;
        }
        Peice peice = board.getPeice(kingCol + (i * colVal), kingRow + (i * rowVal));
        if(peice != null && peice != board.selectedPeice){
            if(!(board.sameTeam(peice, king)) && ( peice.name.equals("Rook") || peice.name.equals("Queen") ) ){
                return true;
            }
            break;
        }
    }
    
    return false;
}



private boolean hitByBishop(int col, int row, Peice king, int kingCol, int kingRow, int colVal, int rowVal){
    for(int i = 1; i < 8; i++){
        if(kingCol - (i * colVal) == col && kingRow - (i * rowVal) == row){
            break;
        }
        Peice peice = board.getPeice(kingCol - (i * colVal), kingRow - (i * rowVal));
        if(peice != null && peice != board.selectedPeice){
            if(!(board.sameTeam(peice, king)) && ( peice.name.equals("Bishop") || peice.name.equals("Queen") ) ){
                return true;
            }
            break;
        }
    }
    
    return false;
}


private boolean hitByKnight(int col, int row, Peice king, int kingCol, int kingRow){
    
    return  checkKnight(board.getPeice(kingCol - 1, kingRow - 2), king, col, row) ||
            checkKnight(board.getPeice(kingCol + 1, kingRow - 2), king, col, row) ||
            checkKnight(board.getPeice(kingCol + 2, kingRow - 1), king, col, row) ||
            checkKnight(board.getPeice(kingCol + 2, kingRow + 1), king, col, row) ||
            checkKnight(board.getPeice(kingCol + 1, kingRow + 2), king, col, row) ||
            checkKnight(board.getPeice(kingCol - 1, kingRow + 2), king, col, row) ||
            checkKnight(board.getPeice(kingCol - 2, kingRow + 1), king, col, row) ||
            checkKnight(board.getPeice(kingCol - 2, kingRow - 1), king, col, row);
    
}


private boolean checkKnight(Peice p, Peice k, int col, int row){
    return p != null && !board.sameTeam(p ,k) && p.name.equals("Knight") && !(p.col == col && p.row == row);
}


private boolean hitByKing(Peice king, int kingCol, int kingRow){
    
    return  checkKing(board.getPeice(kingCol - 1, kingRow - 1), king) ||
            checkKing(board.getPeice(kingCol + 1, kingRow - 1), king) ||
            checkKing(board.getPeice(kingCol, kingRow - 1), king) ||
            checkKing(board.getPeice(kingCol - 1, kingRow ), king) ||
            checkKing(board.getPeice(kingCol + 1, kingRow ), king) ||
            checkKing(board.getPeice(kingCol - 1, kingRow + 1), king) ||
            checkKing(board.getPeice(kingCol + 1, kingRow + 1), king) ||
            checkKing(board.getPeice(kingCol , kingRow + 1), king) ;
}

private boolean checkKing(Peice p, Peice k){
    
    return p != null && !board.sameTeam(p ,k) && p.name.equals("King");
}


private boolean hitByPawn(int col, int row, Peice king, int kingCol, int kingRow){
    int colorVal = king.isWhite ? -1 : 1;
    return checkPawn(board.getPeice(kingCol + 1, kingRow + colorVal), king, col, row) ||
           checkPawn(board.getPeice(kingCol - 1, kingRow + colorVal), king, col, row)  ;
}

private boolean checkPawn(Peice p, Peice k, int col, int row){
    return p != null && !board.sameTeam(p ,k) && p.name.equals("Pawn");
}


public boolean isGameOver(Peice king){
    
    for(Peice peice : board.peiceList){
        if(board.sameTeam(peice, king)){
            board.selectedPeice = peice == king ? king : null;
            for( int row = 0; row < board.rows; row++){
                for( int col = 0; col < board.cols; col++){
                    Move move = new Move (board, peice, col, row);
                    if(board.validMove(move)){
                        return false;
                    }
                }
            }
        }
    }
    
    return true;
    
}


}
