package chess1;
import peices.Peice;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import peices.Bishop;
import peices.King;
import peices.Knight;
import peices.Pawn;
import peices.Queen;
import peices.Rook;
import userData.UserManager;

public class Board extends JPanel {

    public int tileSize = 90;
    int cols = 8, rows = 8;
    ArrayList<Peice> peiceList = new ArrayList<>();
    public Peice selectedPeice;
    Input input = new Input(this);
    public int enpassantTile = -1;
    public CheckScanner checkScanner = new CheckScanner(this);
    private boolean isWhiteToMove = true;
    private boolean isGameOver = false;
    private boolean checkMate = false;
    private boolean isPause = false;
    public boolean isKingCheck = false;
    private TurnManagerPanel externalTurnPanel; // Reference to the external turn panel
    public UserManager users;
    // Modified constructor to accept the external turn panel
    public Board(TurnManagerPanel externalTurnPanel, UserManager users) {
        isWhiteToMove = true;
        this.externalTurnPanel = externalTurnPanel;
        this.users = users;
        // Set exact dimensions for the board - no extra space
        int boardWidth = cols * tileSize;
        int boardHeight = rows * tileSize;
        
        this.setPreferredSize(new Dimension(boardWidth, boardHeight));
        this.setMinimumSize(new Dimension(boardWidth, boardHeight));
        this.setMaximumSize(new Dimension(boardWidth, boardHeight));
        this.setSize(boardWidth, boardHeight);
        // Make the panel focusable to receive key events
        
        addPeices();
        this.setLayout(null);
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        // Set initial turn state
        
        this.addKeyListener(new KeyAdapter() {
    
            @Override
    
            public void keyPressed(KeyEvent e) {
        
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    System.out.println("space");
                    if(!isPause){
                        initPausePanel();
                        isPause = !isPause;
                      // Show the pause menu
                    }
       
                }
    
            }

        });
        
        if (externalTurnPanel != null) {
            externalTurnPanel.setCurrentTurn(isWhiteToMove);   
        }
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(cols * tileSize, rows * tileSize);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(cols * tileSize, rows * tileSize);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(cols * tileSize, rows * tileSize);
    }

    public Peice getPeice(int newCol, int newRow) {
        for (Peice peice : peiceList) {
            if (peice.col == newCol && peice.row == newRow) {
                return peice;
            }
        }
        return null;
    }

    public int getTileNum(int col, int row) {
        return row * rows + col;
    }

    public Peice findKing(boolean isWhite) {
        for (Peice peice : peiceList) {
            if (isWhite == peice.isWhite && peice.name.equals("King")) {
                return peice;
            }
        }
        return null;
    }

    public void addPeices() {
        // Black Pieces
        peiceList.add(new Knight(this, 1, 0, false));
        peiceList.add(new Bishop(this, 2, 0, false));
        peiceList.add(new Knight(this, 6, 0, false));
        peiceList.add(new Bishop(this, 5, 0, false));
        peiceList.add(new King(this, 4, 0, false));
//        for (int i = 0; i < 8; i++)
//            peiceList.add(new Pawn(this, i, 1, false));
        peiceList.add(new Queen(this, 3, 0, false));
//        peiceList.add(new Rook(this, 0, 0, false));
//        peiceList.add(new Rook(this, 7, 0, false));
//        // White Pieces
//        peiceList.add(new Knight(this, 1, 7, true));
//        peiceList.add(new Bishop(this, 2, 7, true));
        peiceList.add(new Knight(this, 6, 7, true));
        peiceList.add(new Bishop(this, 5, 7, true));
        peiceList.add(new King(this, 4, 7, true));
//        for (int i = 0; i < 8; i++)
//            peiceList.add(new Pawn(this, i, 6, true));
        peiceList.add(new Queen(this, 3, 7, true));
        peiceList.add(new Rook(this, 0, 7, true));
//        peiceList.add(new Rook(this, 7, 7, true));
    }

    private void updateGameState() throws SQLException {
        Peice king = findKing(isWhiteToMove);
        if (checkScanner.isGameOver(king)) {
            if (checkScanner.isKingChecked(new Move(this, king, king.col, king.row))) {
                
                checkMate = true;
                System.out.println(isWhiteToMove ? "Black Wins" : "White Wins");
                this.repaint();
            } else {
                System.out.println("StaleMate");
                
            }
            isGameOver = true;
            playSound("/chess1/over.wav");
            setRating();
            initGameOverPanel();
            
        }
        
        // Update the external turn panel with the current turn
        if (externalTurnPanel != null) {
            externalTurnPanel.setCurrentTurn(isWhiteToMove);
        }
    }

   private void setRating() {
    int baseChange = 20;

    if (checkMate) {
        if (!isWhiteToMove) { // White wins
            int winnerRating = users.currentPlayer.getRating();
            int loserRating = users.opponentPlayer.getRating();

            int winnerScoreBonus = users.currentPlayer.getScore() / 6;
            int loserScoreBonus = users.opponentPlayer.getScore() / 6;

            int winDelta = (int) ((baseChange + winnerScoreBonus) * getRatingFactor(winnerRating));
            int lossDelta = -(int) ((baseChange - Math.min(loserScoreBonus, baseChange)) * getloserRatingFactor(loserRating));

            users.currentPlayer.addRating(winDelta);
            users.opponentPlayer.addRating(lossDelta);
        } else { // Black wins
            int winnerRating = users.opponentPlayer.getRating();
            int loserRating = users.currentPlayer.getRating();

            int winnerScoreBonus = users.opponentPlayer.getScore() / 6;
            int loserScoreBonus = users.currentPlayer.getScore() / 6;

            int winDelta = (int) ((baseChange + winnerScoreBonus) * getRatingFactor(winnerRating));
            int lossDelta = -(int) ((baseChange - Math.min(loserScoreBonus, baseChange)) * getloserRatingFactor(loserRating));

            users.opponentPlayer.addRating(winDelta);
            users.currentPlayer.addRating(lossDelta);
        }
    } else { // Draw
        int rating1 = users.currentPlayer.getRating();
        int rating2 = users.opponentPlayer.getRating();

        int drawBonus1 = (int) ((users.currentPlayer.getScore() / 10.0) * getRatingFactor(rating1));
        int drawBonus2 = (int) ((users.opponentPlayer.getScore() / 10.0) * getRatingFactor(rating2));

        users.currentPlayer.addRating(drawBonus1);
        users.opponentPlayer.addRating(drawBonus2);
    }
}

    private double getRatingFactor(int rating) {
    // Scale factor: lower ratings get 1.2x, high ratings ~0.8x
    if (rating <= 200) return 2;
    else if (rating <= 400) return 1.6;
    else if (rating <= 600) return 1.4;
    else if (rating <= 800) return 1.2;
    else if (rating <= 1000) return 1.1;
    else if (rating <= 1300) return 1.0;
    else if (rating <= 1600) return 0.9;
    else return 0.8;
}

        private double getloserRatingFactor(int rating) {
    // Scale factor: lower ratings get 1.2x, high ratings ~0.8x
    if (rating <= 200) return 2;
    else if (rating <= 400) return 0.9;
    else if (rating <= 600) return 1.0;
    else if (rating <= 800) return 1.1;
    else if (rating <= 1000) return 1.2;
    else if (rating <= 1300) return 1.4;
    else if (rating <= 1600) return 1.6;
    else return 0.8;
}
public void initPausePanel() {
    JPanel gameOverPanel = new JPanel(null);
    gameOverPanel.setBackground(new Color(0, 0, 0, 150)); // subtle glass look
    gameOverPanel.setBounds(0, 0, cols * tileSize, rows * tileSize);
    gameOverPanel.setVisible(false);

    // Header Label
    JLabel gameOverLabel = new JLabel("Game Paused", SwingConstants.CENTER);
    gameOverLabel.setForeground(Color.WHITE);
    gameOverLabel.setFont(new Font("Arial Black", Font.BOLD, 36));
    gameOverLabel.setBounds(200, 140, 400, 50);
    gameOverPanel.add(gameOverLabel);

    // Stats Panel
    JPanel statsPanel = new JPanel(null) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            super.paintComponent(g2d);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(36, 40, 47, 220),
                0, getHeight(), new Color(20, 22, 28, 240)
            );
            RoundRectangle2D roundedRect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30);
            g2d.setPaint(gradient);
            g2d.fill(roundedRect);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(100, 100, 100));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        }
    };
    statsPanel.setOpaque(false);
    statsPanel.setBounds(165, 210, 440, 220);

    // Status Title
    JLabel winnerLabel = new JLabel("CURRENT STATUS", SwingConstants.CENTER);
    winnerLabel.setFont(new Font("Arial Black", Font.BOLD, 26));
    winnerLabel.setForeground(Color.WHITE);
    winnerLabel.setBounds(0, 20, 440, 30);
    statsPanel.add(winnerLabel);

    // Player 1
    JLabel player1Label = new JLabel(
        "Player 1 | Score: " + users.currentPlayer.getScore() + " | Rating: " + users.currentPlayer.getRating()
    );
    player1Label.setBounds(30, 80, 380, 30);
    player1Label.setFont(new Font("Segoe UI", Font.PLAIN, 20));
    player1Label.setForeground(Color.LIGHT_GRAY);
    statsPanel.add(player1Label);

    // Player 2
    JLabel player2Label = new JLabel(
        "Player 2 | Score: " + users.opponentPlayer.getScore() + " | Rating: " + users.opponentPlayer.getRating()
    );
    player2Label.setBounds(30, 135, 380, 30);
    player2Label.setFont(new Font("Segoe UI", Font.PLAIN, 20));
    player2Label.setForeground(Color.LIGHT_GRAY);
    statsPanel.add(player2Label);

    gameOverPanel.add(statsPanel);

    // Close Button
    JButton closeButton = new JButton("Resume");
    closeButton.setBounds(320, 450, 120, 45);
    closeButton.setFont(new Font("Arial", Font.BOLD, 16));
    closeButton.setForeground(Color.WHITE);
    closeButton.setBackground(new Color(60, 63, 65));
    closeButton.setFocusPainted(false);
    closeButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
    closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

    closeButton.addActionListener(e -> {
        gameOverPanel.setVisible(false);
        isPause = !isPause;
        this.requestFocusInWindow();
    });

    gameOverPanel.add(closeButton);

    this.add(gameOverPanel);
    this.setComponentZOrder(gameOverPanel, 0);
    gameOverPanel.setVisible(true);
    
}

        
    private void initGameOverPanel() throws SQLException {
    JPanel gameOverPanel = new JPanel(null);
    gameOverPanel.setBackground(new Color(0, 0, 0, 150)); // subtle glass look
    gameOverPanel.setBounds(0, 0, cols * tileSize, rows * tileSize);
    gameOverPanel.setVisible(false);

    // Header Label
    JLabel gameOverLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
    gameOverLabel.setForeground(Color.WHITE);
    gameOverLabel.setFont(new Font("Arial Black", Font.BOLD, 36));
    gameOverLabel.setBounds(200, 140, 400, 50);
    gameOverPanel.add(gameOverLabel);

    // Stats Panel
    JPanel statsPanel = new JPanel(null) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            super.paintComponent(g2d);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(36, 40, 47, 220),
                0, getHeight(), new Color(20, 22, 28, 240)
            );
            RoundRectangle2D roundedRect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30);
            g2d.setPaint(gradient);
            g2d.fill(roundedRect);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(100, 100, 100));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        }
    };
    statsPanel.setOpaque(false);
    statsPanel.setBounds(165, 210, 440, 220);

    // Status Title
    JLabel winnerLabel = new JLabel(!checkMate ? "StaleMate!" : isWhiteToMove ? "Black Wins" : "White Wins", SwingConstants.CENTER);
    winnerLabel.setFont(new Font("Arial Black", Font.BOLD, 26));
    winnerLabel.setForeground(Color.WHITE);
    winnerLabel.setBounds(0, 20, 440, 30);
    statsPanel.add(winnerLabel);

    // Player 1
    JLabel player1Label = new JLabel(
        "Player 1 | Score: " + users.currentPlayer.getScore() + " | Rating: " + users.currentPlayer.getRating()
    );
    player1Label.setBounds(30, 80, 380, 30);
    player1Label.setFont(new Font("Segoe UI", Font.PLAIN, 20));
    player1Label.setForeground(Color.LIGHT_GRAY);
    statsPanel.add(player1Label);

    // Player 2
    JLabel player2Label = new JLabel(
        "Player 2 | Score: " + users.opponentPlayer.getScore() + " | Rating: " + users.opponentPlayer.getRating()
    );
    player2Label.setBounds(30, 135, 380, 30);
    player2Label.setFont(new Font("Segoe UI", Font.PLAIN, 20));
    player2Label.setForeground(Color.LIGHT_GRAY);
    statsPanel.add(player2Label);

    gameOverPanel.add(statsPanel);

    // Close Button
    JButton closeButton = new JButton("Close");
    closeButton.setBounds(320, 450, 120, 45);
    closeButton.setFont(new Font("Arial", Font.BOLD, 16));
    closeButton.setForeground(Color.WHITE);
    closeButton.setBackground(new Color(60, 63, 65));
    closeButton.setFocusPainted(false);
    closeButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
    closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

    closeButton.addActionListener(e -> {
        gameOverPanel.setVisible(false);
    });

    gameOverPanel.add(closeButton);

    this.add(gameOverPanel);
    this.setComponentZOrder(gameOverPanel, 0);
    gameOverPanel.setVisible(true);

    users.changeRating(users.currentPlayer, users.opponentPlayer);

}


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        
        
        // Set background color to ensure no white space shows
        g2d.setColor(new Color(236, 236, 209)); // Light square color
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        
        
        // Draw the chess board
        for (int r = 0; r < rows; ++r)
            for (int c = 0; c < cols; ++c) {
                g2d.setColor((c + r) % 2 == 0 ? new Color(236, 236, 209) : new Color(59, 153, 217));
                g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
            }

        // Draw valid move indicators
        if (selectedPeice != null) {
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++) {
                    if (validMove(new Move(this, selectedPeice, c, r))) {
                        g2d.setColor(new Color(144, 238, 144, 170));
                        g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
                    }
                }
        }
        
        //Draw kingCheck
         // Highlight king if in check or checkmate
        if (isGameOver) {
            System.out.println("Outside check");
            Peice king = findKing(isWhiteToMove);
            if (king != null) {
                System.out.println("REDDDDDD");
                g2d.setColor(checkMate? new Color(220, 20, 60) :new Color(220, 20, 60, 150)); // Semi-transparent red
                g2d.fillRect(king.col * tileSize, king.row * tileSize, tileSize, tileSize);
                //isKingCheck = false;
            }
        }

        // Draw pieces
        for (Peice peice : peiceList) {
            peice.paint(g2d);
        }
    }

    boolean validMove(Move move) {
        if (move.peice.isWhite != isWhiteToMove) return false;
        if (isGameOver) return false;
        if (sameTeam(move.peice, move.capture)) return false;
        if (!move.peice.isValidMovement(move.newCol, move.newRow)) return false;
        if (move.peice.moveCollidesWithPiece(move.newCol, move.newRow)) return false;
        if (checkScanner.isKingChecked(move)){
            //playSound("/chess1/check.wav");
            return false;
        }
        //isKingCheck = false;   
        return true;
    }
    
    void makeMove(Move move) throws SQLException {
        isKingCheck = false;
        if (move.peice.name.equals("Pawn")) {
            movePawn(move);
        } else if (move.peice.name.equals("King")) {
            moveKing(move);
        }

        move.peice.col = move.newCol;
        move.peice.row = move.newRow;
        move.peice.xpos = move.newCol * tileSize;
        move.peice.ypos = move.newRow * tileSize;
        move.peice.isFisrtMove = false;
        capture(move.capture);
        isWhiteToMove = !isWhiteToMove;
        playSound("/chess1/move.wav");
        // Update check status after the move
       // updateCheckStatus();
         
        this.repaint();
        updateGameState();
        
        
    }

    public void capture(Peice peice) {
        if(peice != null){
        if(isWhiteToMove){
            if(peice.name.equals("Pawn")){
                users.currentPlayer.addScore(1);
            }else if(peice.name.equals("Knight")){
                users.currentPlayer.addScore(3);
            }else if(peice.name.equals("Bishop")){
                users.currentPlayer.addScore(3);
            }else if(peice.name.equals("Queen")){
                users.currentPlayer.addScore(9);
            }else if(peice.name.equals("Rook")){
                users.currentPlayer.addScore(5);
            }
        }else{
            if(peice.name.equals("Pawn")){
                users.opponentPlayer.addScore(1);
            }else if(peice.name.equals("Knight")){
                users.opponentPlayer.addScore(3);
            }else if(peice.name.equals("Bishop")){
                users.opponentPlayer.addScore(3);
            }else if(peice.name.equals("Queen")){
                users.opponentPlayer.addScore(9);
            }else if(peice.name.equals("Rook")){
                users.opponentPlayer.addScore(5);
            }
        }
        playSound("/chess1/capture.wav");
        }
        
        peiceList.remove(peice);
        
    }

    public boolean sameTeam(Peice p1, Peice p2) {
        if (p1 == null || p2 == null) return false;
        return p1.isWhite == p2.isWhite;
    }

    private void moveKing(Move move) {
        if (Math.abs(move.peice.col - move.newCol) == 2) {
            Peice rook;
            if (move.peice.col < move.newCol) {
                rook = getPeice(7, move.peice.row);
                rook.col = 5;
            } else {
                rook = getPeice(0, move.peice.row);
                rook.col = 3;
            }
            playSound("/chess1/castle.wav");
            rook.xpos = rook.col * tileSize;
        }
    }

    private void movePawn(Move move) {
        int colorIndex = move.peice.isWhite ? 1 : -1;
        if (getTileNum(move.newCol, move.newRow) == enpassantTile) {
            move.capture = getPeice(move.newCol, move.newRow + colorIndex);
        }
        if (Math.abs(move.peice.row - move.newRow) == 2) {
            enpassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            enpassantTile = -1;
        }

        int promotionRow = move.peice.isWhite ? 0 : 7;
        if (move.newRow == promotionRow) {
            promotePawn(move);
        }
    }

    private void playSound(String fileName){
       new Thread(() -> {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(fileName);
            if (audioSrc == null) {
                System.out.println("WAV file not found: " + fileName);
                return;
            }

            InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing WAV: " + e.getMessage());
        }
    }).start();
        
    }
    
    
    private void promotePawn(Move move) {
        peiceList.add(new Queen(this, move.newCol, move.newRow, move.peice.isWhite));
        capture(move.peice);
    }
}
