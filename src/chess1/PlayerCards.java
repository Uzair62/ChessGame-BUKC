package chess1;

import chessgame.StateManager;
import static chessgame.StateManager.GameState.MENU_STATE;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.concurrent.CountDownLatch;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import userData.UserManager;


public class PlayerCards {
    
    private Image stat;
    private Image star;
    private CountDownLatch loginLatch = new CountDownLatch(1);
    public void drawCards(JFrame frame, UserManager userManager, StateManager state){
        
        try{
            stat = ImageIO.read(getClass().getResource("/chess1/stat.png"));
            star = ImageIO.read(getClass().getResource("/chess1/rating.png"));
        }
        catch(Exception e){
            
        }
        
        
        frame.setLayout(new BorderLayout());
        
        // Create the full-width turn manager panel using the new class
        TurnManagerPanel fullWidthTurnPanel = new TurnManagerPanel();
        
        JPanel currentPlayerPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient colors
    GradientPaint gradient = new GradientPaint(
    0, 0, new Color(46, 46, 46, 230),  // semi-transparent charcoal grey
    0, getHeight(), new Color(46, 46, 46) // opaque charcoal grey
);



                g2d.setPaint(gradient);
                
                // Draw a rounded rectangle with the gradient
                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                    10, 10, getWidth()-20, getHeight()-50, 20, 20
                );
                g2d.fill(roundedRect);
                
                // Add a subtle glow border
                g2d.setColor(new Color(100, 200, 255, 40));
                g2d.setStroke(new java.awt.BasicStroke(2f));
                g2d.draw(roundedRect);
                g2d.dispose();
            }
        };
        
        currentPlayerPanel.setLayout(null);
        currentPlayerPanel.setOpaque(false);
        currentPlayerPanel.setPreferredSize(new Dimension(410, 300));
        JPanel title1 = new JPanel();
        title1.setLayout(new BoxLayout(title1, BoxLayout.Y_AXIS));
        title1.setOpaque(false);
        title1.setBounds(-10, 50, 450, 100);
        
        // Game title
        JLabel titleLabel = new JLabel(userManager.currentPlayer.getName());
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE); // Light blue
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        title1.add(titleLabel);
        
        JPanel imagePanel1 = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (userManager.currentPlayer.player != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Enable anti-aliasing for smoother edges
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = 0, y = 0, width = 280, height = 280;
            int arc = 50; // Corner arc for rounded effect

            // Create a rounded clip shape
            Shape clip = new RoundRectangle2D.Float(x, y, width, height, arc, arc);
            g2d.setClip(clip);
            
            // Draw the image with the rounded clipping
            g2d.drawImage(userManager.currentPlayer.player, x, y, width, height, this);
            
            g2d.dispose(); // Clean up
              }
    
           }
    

        };

          imagePanel1.setLayout(new BoxLayout(imagePanel1, BoxLayout.Y_AXIS));
          imagePanel1.setOpaque(false); // Use false if you want transparency around the image
          imagePanel1.setBounds(65, 100, 280, 280);
        
          JPanel statPanel1 = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (stat != null) {
            System.out.println("Image is printing");
            g.drawImage(stat, 270, 0, 45, 45, this);
        }
    }
    

        };
          statPanel1.setLayout(new BoxLayout(statPanel1, BoxLayout.Y_AXIS));
          statPanel1.setOpaque(false); // Changed from true to false
          statPanel1.setBounds(60, 400, 350, 50);
          
          JLabel statTitle = new JLabel("PLAYER STATS");
          statTitle.setFont(new Font("Arial Black", Font.BOLD, 30));
          statTitle.setForeground(new Color(255, 215, 0)); // Light blue
          statTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
          statPanel1.add(statTitle);
          
          
          
          JPanel ratingPanel1 = new JPanel() {
           @Override
            protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (star != null) {
                  g.drawImage(star, 250, 0, 30, 30, this);
            }
            }
            };
          ratingPanel1.setLayout(new BoxLayout(ratingPanel1, BoxLayout.Y_AXIS));
          ratingPanel1.setOpaque(false); // Changed from true to false
          ratingPanel1.setBounds(30, 480, 350, 70); 
          
          
          
          JLabel ratingTitle1 = new JLabel("Rating: " + userManager.currentPlayer.getRating());
          ratingTitle1.setFont(new Font("Arial Black", Font.BOLD, 20));
          ratingTitle1.setForeground(new Color(255, 215, 0)); // Light blue
          ratingTitle1.setAlignmentX(Component.CENTER_ALIGNMENT);
          ratingPanel1.add(ratingTitle1);
          ratingPanel1.add(Box.createVerticalStrut(10));
          JLabel currentScore1 = new JLabel("SCORE: " + userManager.currentPlayer.getRating());
          currentScore1.setFont(new Font("Arial Black", Font.BOLD, 15));
          currentScore1.setForeground(new Color(255, 215, 0));
          currentScore1.setAlignmentX(Component.CENTER_ALIGNMENT);
          ratingPanel1.add(Box.createVerticalStrut(10)); // spacing
          //ratingPanel1.add(currentScore1);
          
          
          currentPlayerPanel.add(title1);
          currentPlayerPanel.add(imagePanel1);
          currentPlayerPanel.add(statPanel1);
          currentPlayerPanel.add(ratingPanel1);
          
          
        JPanel opponentPlayerPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient colors
        GradientPaint gradient = new GradientPaint(
    0, 0, new Color(46, 46, 46, 230),  // semi-transparent charcoal grey
    0, getHeight(), new Color(46, 46, 46) // opaque charcoal grey
);


                
                g2d.setPaint(gradient);
                
                // Draw a rounded rectangle with the gradient
                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                    10, 10, getWidth()-20, getHeight()-50, 20, 20
                );
                g2d.fill(roundedRect);
                
                // Add a subtle glow border
                g2d.setColor(new Color(100, 200, 255, 40));
                g2d.setStroke(new java.awt.BasicStroke(2f));
                g2d.draw(roundedRect);
                g2d.dispose();
            }
        };
        
        opponentPlayerPanel.setLayout(null);
        opponentPlayerPanel.setOpaque(false);
        opponentPlayerPanel.setPreferredSize(new Dimension(410, 300));
        
        
        JPanel title2 = new JPanel();
        title2.setLayout(new BoxLayout(title2, BoxLayout.Y_AXIS));
        title2.setOpaque(false);
        title2.setBounds(-10, 50, 450, 100);
        
        // Game title
        JLabel titleLabe2 = new JLabel(userManager.opponentPlayer.getName());
        titleLabe2.setFont(new Font("Arial Black", Font.BOLD, 20));
        titleLabe2.setForeground(Color.WHITE); // Light blue
        titleLabe2.setAlignmentX(Component.CENTER_ALIGNMENT);
        title2.add(titleLabe2);
        
        JPanel imagePanel2 = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (userManager.opponentPlayer.player != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Enable anti-aliasing for smoother edges
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = 0, y = 0, width = 280, height = 280;
            int arc = 50; // Corner arc for rounded effect

            // Create a rounded clip shape
            Shape clip = new RoundRectangle2D.Float(x, y, width, height, arc, arc);
            g2d.setClip(clip);
            
            // Draw the image with the rounded clipping
            g2d.drawImage(userManager.opponentPlayer.player, x, y, width, height, this);
            
            g2d.dispose(); // Clean up
              }
    
           }
    

        };

          imagePanel2.setLayout(new BoxLayout(imagePanel2, BoxLayout.Y_AXIS));
          imagePanel2.setOpaque(false); // Use false if you want transparency around the image
          imagePanel2.setBounds(65, 100, 280, 280);
        
          JPanel statPanel2 = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (stat != null) {
            System.out.println("Image is printing");
            g.drawImage(stat, 270, 0, 45, 45, this);
        }
    }
    

        };
          statPanel2.setLayout(new BoxLayout(statPanel2, BoxLayout.Y_AXIS));
          statPanel2.setOpaque(false); // Changed from true to false
          statPanel2.setBounds(60, 400, 350, 50);
          
          JLabel statTitle2 = new JLabel("PLAYER STATS");
          statTitle2.setFont(new Font("Arial Black", Font.BOLD, 30));
          statTitle2.setForeground(new Color(255, 215, 0)); // Light blue
          statTitle2.setAlignmentX(Component.LEFT_ALIGNMENT);
          statPanel2.add(statTitle2);
          
          
          
          JPanel ratingPanel2 = new JPanel() {
           @Override
            protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (star != null) {
                  g.drawImage(star, 250, 0, 30, 30, this);
            }
            }
            };
          ratingPanel2.setLayout(new BoxLayout(ratingPanel2, BoxLayout.Y_AXIS));
          ratingPanel2.setOpaque(false); // Changed from true to false
          ratingPanel2.setBounds(30, 480, 350, 70); 
          
          
          
          JLabel ratingTitle2 = new JLabel("Rating: " + userManager.opponentPlayer.getRating());
          ratingTitle2.setFont(new Font("Arial Black", Font.BOLD, 20));
          ratingTitle2.setForeground(new Color(255, 215, 0)); // Light blue
          ratingTitle2.setAlignmentX(Component.CENTER_ALIGNMENT);
          ratingPanel2.add(ratingTitle2);
          ratingPanel2.add(Box.createVerticalStrut(10));
          JLabel currentScore2 = new JLabel("SCORE: " + userManager.opponentPlayer.getRating());
          currentScore2.setFont(new Font("Arial Black", Font.BOLD, 15));
          currentScore2.setForeground(new Color(255, 215, 0));
          currentScore2.setAlignmentX(Component.CENTER_ALIGNMENT);
          ratingPanel2.add(Box.createVerticalStrut(10)); // spacing
          //ratingPanel2.add(currentScore2);
          
          
          opponentPlayerPanel.add(title2);
          opponentPlayerPanel.add(imagePanel2);
          opponentPlayerPanel.add(statPanel2);
          opponentPlayerPanel.add(ratingPanel2);
          
        // Create the back button that will be passed to the Board
        JButton backButton = new JButton("← Back to Menu");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(60, 60, 80));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listener to the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll(); // Clear the UI
                frame.revalidate();
                frame.repaint();
                userManager.currentPlayer.saveGame = false;
                userManager.opponentPlayer = null;
                loginLatch.countDown();
            }
        });
        
        // Add the back button to the full-width turn panel
        backButton.setBounds(10, 7, 150, 30);
        fullWidthTurnPanel.add(backButton);
        
        // Add components to the frame
        frame.add(fullWidthTurnPanel, BorderLayout.NORTH); // Full-width panel at top
        frame.add(currentPlayerPanel, BorderLayout.WEST);
        frame.add(opponentPlayerPanel, BorderLayout.EAST);
        
        // Create the board and pass it the turn panel for turn indicator updates
        Board board = new Board(fullWidthTurnPanel, userManager);
        frame.add(board, BorderLayout.CENTER);
        
        // Pack the frame to fit components exactly
//        frame.pack();
//        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
        board.setFocusable(true);
        board.requestFocusInWindow();
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        try {
            // Block until authentication is complete
            loginLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
