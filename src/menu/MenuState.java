package menu;

import abstractPackage.parentState;
import chessgame.StateManager;
import static chessgame.StateManager.GameState.GAME_STATE;
import static chessgame.StateManager.GameState.LOGIN_STATE;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Comparator;
import java.util.concurrent.CountDownLatch;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import userData.PlayerInfo;
import userData.UserManager;

public class MenuState extends parentState {
    
    private JPanel cardPanel;
    public Image stat,star;
    private CardLayout cardLayout;
    private final String MENU_PANEL = "MENU";
    private final String PLAYER_PANEL = "PLAYERS";
    private final String LEADERBOARD_PANEL = "LEADERBOARD";
    //private CountDownLatch loginLatch = new CountDownLatch(1);
    
    @Override
    public StateManager.GameState run(UserManager userManager, JFrame frame, StateManager state) {
        // Set the frame to full screen or maximize it
        CountDownLatch loginLatch = new CountDownLatch(1);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().removeAll();
        try{
            stat = ImageIO.read(getClass().getResource("/menu/stat.png"));
            star = ImageIO.read(getClass().getResource("/menu/rating.png"));
        }
        catch(Exception e){
            
        }
        // Create card layout to switch between menu and player selection
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Create the menu panel
        
        JPanel menuPanel = createMenuPanel(frame,userManager,loginLatch,state);
        
        // Create the player selection panel
        JPanel playerPanel = createPlayerPanel(frame,userManager,loginLatch,state);
        
        JPanel leaderboardPanel = createLeaderboardPanel(frame,userManager,loginLatch,state);
        // Add both panels to the card layout
        
        cardPanel.add(menuPanel, MENU_PANEL);
        cardPanel.add(playerPanel, PLAYER_PANEL);
        cardPanel.add(leaderboardPanel, LEADERBOARD_PANEL); // ADD THIS LINE
        // Add the card panel to the frame
        frame.add(cardPanel);
        frame.setTitle("CHESS MASTER");
        frame.setVisible(true);
        
        // Show the menu panel first
        
        cardLayout.show(cardPanel, MENU_PANEL);
        
        // Refresh the frame
        frame.revalidate();
        frame.repaint();
        try {
            // Block until authentication is complete
            loginLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        frame.getContentPane().removeAll();
        return state.myStates;
    }
    
    private JPanel createMenuPanel(JFrame frame, UserManager userManager,CountDownLatch loginLatch, StateManager state) {
        // Create the main menu panel
        JPanel mainPanel = new JPanel() {
            private Image backgroundImage;
            private Image boardImage;
            private Image pieceImage;
            
            {
                try {
                    backgroundImage = ImageIO.read(getClass().getResource("/menu/blueBack.png"));
                    boardImage = ImageIO.read(getClass().getResource("/menu/chessBoard.png"));
                    pieceImage = ImageIO.read(getClass().getResource("/menu/myPiece.png"));
                    
                } catch (Exception e) {
                    System.out.println("Error loading image: " + e.getMessage());
                }
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Draw the background image if available
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g.drawImage(pieceImage, 250, 150, 400, 350, this);
                    g.drawImage(boardImage, 100, 0, 1500, 800, this);
                }
                
            }
        };
        
        mainPanel.setLayout(null);
        
        // Create a gradient container panel for the menu
        JPanel menuContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient colors
                GradientPaint gradient = new GradientPaint(
    0, 0, new Color(30, 58, 95),  // semi-transparent steel blue
    0, getHeight(), new Color(59, 90, 138) // opaque steel blue
);

                
                g2d.setPaint(gradient);
                
                // Draw a rounded rectangle with the gradient
                RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), 20, 20
                );
                g2d.fill(roundedRect);
                
                // Add a subtle glow border
                g2d.setColor(new Color(100, 200, 255, 40));
                g2d.setStroke(new java.awt.BasicStroke(2f));
                g2d.draw(roundedRect);
                
                g2d.dispose();
            }
        };
        
        menuContainer.setLayout(null);
        menuContainer.setOpaque(false);
        
        // Calculate position based on frame size
        int containerWidth = 500;
        int containerHeight = 760;
        int containerX = (frame.getWidth() - containerWidth) / 2;
        menuContainer.setBounds(containerX, 20, containerWidth, containerHeight);
        
        // Create the title panel
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setBounds(30, 20, 450, 100);
        
        // Game title
        JLabel titleLabel = new JLabel("Chess Master");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 57));
        titleLabel.setForeground(Color.WHITE); // Light blue
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(titleLabel);
        
          JPanel statPanel = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (stat != null) {
            g.drawImage(stat, 390, 0, 70, 70, this);
        }
    }
    

        };
          statPanel.setLayout(new BoxLayout(statPanel, BoxLayout.Y_AXIS));
          statPanel.setOpaque(false); // Use false if you want transparency around the image
          statPanel.setBounds(30, 120, 450, 70);
          
          JLabel statTitle = new JLabel("PLAYER STATS");
          statTitle.setFont(new Font("Arial Black", Font.BOLD, 45));
          statTitle.setForeground(new Color(255, 215, 0)); // Light blue
          statTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
          statPanel.add(statTitle);
        
          
          JPanel ratingPanel = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (star != null) {
            g.drawImage(star, 320, 25, 30, 30, this);
        }
    }
    

        };
          
          
        // Create the stat options panel
        
        JPanel imagePanel = new JPanel() {
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

          imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
          imagePanel.setOpaque(false); // Use false if you want transparency around the image
          imagePanel.setBounds(110, 190, 280, 280);
          
        
          ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.Y_AXIS));
          ratingPanel.setOpaque(false); // Use false if you want transparency around the image
          ratingPanel.setBounds(30, 480, 450, 70);
          
          JLabel ratingTitle = new JLabel(userManager.currentPlayer.getEmail()+" | "+userManager.currentPlayer.getName()+" | "+userManager.currentPlayer.getUid());
          ratingTitle.setFont(new Font("Arial Black", Font.BOLD, 15));
          ratingTitle.setForeground(Color.WHITE); // Light blue
          ratingTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
          ratingPanel.add(ratingTitle);
          ratingPanel.add(Box.createVerticalStrut(-10));
          JLabel ratingScore = new JLabel("Rating: " + userManager.currentPlayer.getRating());
          ratingScore.setFont(new Font("Arial Black", Font.BOLD, 25));
          ratingScore.setForeground(new Color(255, 215, 0));
          ratingScore.setAlignmentX(Component.CENTER_ALIGNMENT);
          ratingPanel.add(Box.createVerticalStrut(10)); // spacing
          ratingPanel.add(ratingScore);
          
          
        // Create the menu options panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBounds(150, 540, 200, 250);
        
        // Add menu options
        
        
        
        String[] menuOptions = {"Ranking","New Game", "Exit"};
        JPanel linePanel = new JPanel();
                linePanel.setPreferredSize(new Dimension(200, 2));
                linePanel.setMaximumSize(new Dimension(200, 2));
                linePanel.setBackground(new Color(255, 50, 50));
                linePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                menuPanel.add(linePanel);
                menuPanel.add(Box.createVerticalStrut(20));
        for (String option : menuOptions) {
            
            
            JButton menuItem = createMenuOption(option);
            
            // Add action to "Load Game" option to show player selection
            
            if (option.equals("Ranking")) {
    menuItem.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            cardLayout.show(cardPanel, LEADERBOARD_PANEL);
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            menuItem.setBackground(new Color(255, 0, 0,255));
            menuItem.setOpaque(true);
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            menuItem.setBackground(new Color(0, 150, 50));
            menuItem.setOpaque(true);
        }
    });
}
            
            // Add action to "New Game" option to show player selection
            if (option.equals("New Game")) {
                 
                menuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        cardLayout.show(cardPanel, PLAYER_PANEL);
                    }
                    
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        //menuItem.setForeground(new Color(255, 50, 50));
                        menuItem.setBackground(new Color(255, 0, 0,255)); // Changed hover color to lighter red
                        menuItem.setOpaque(true);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        //menuItem.setForeground(Color.WHITE);
                        menuItem.setBackground(new Color(0, 150, 50));
                        menuItem.setOpaque(true);
                    }
                });
            }
            
             if (option.equals("Exit")) {
                menuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.exit(0);
                    }
                    
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        menuItem.setBackground(new Color(255, 0, 0,255)); // Changed hover color to blue
                        menuItem.setOpaque(true);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                       menuItem.setBackground(new Color(0, 150, 50));
                       menuItem.setOpaque(true);
                    }
                });
            }
            
            menuPanel.add(menuItem);
            menuPanel.add(Box.createVerticalStrut(20)); // Space between options
            
          }
        
        // Add components to container
        //menuContainer.add(statPanel);
        menuContainer.add(titlePanel);
        menuContainer.add(statPanel);
        menuContainer.add(imagePanel);
        menuContainer.add(ratingPanel);
        menuContainer.add(menuPanel);
        
        // Add container to main panel
        
        
        // Back button
        
       // Create header panel with back button - THIS IS THE FIX
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        headerPanel.setBackground(new Color(30, 30, 40, 200)); // Semi-transparent background
        
        // Create back button
        JButton backButton = new JButton("← Back to Menu");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(60, 60, 80));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state.myStates = LOGIN_STATE;
                userManager.opponentPlayer = null;
                userManager.currentPlayer.saveGame = false;
                loginLatch.countDown();
            }
        });
        
        // Add button to header panel
        headerPanel.add(backButton);
        
        // Set explicit bounds for the header panel - position at top of screen
        headerPanel.setBounds(0, 0, 130, 40);
        headerPanel.setOpaque(false);
        // Add header panel to main panel AFTER adding menuContainer (for proper z-ordering)
        mainPanel.add(headerPanel);
        mainPanel.add(menuContainer);
        
        return mainPanel;
    }
    

    private JButton createMenuOption(String text) {
        JButton menuItem = new JButton(text);
        
        menuItem.setForeground(Color.WHITE);
        menuItem.setBackground(new Color(0, 150, 50));
        menuItem.setFocusPainted(false);
        menuItem.setBorderPainted(false);
        menuItem.setFont(new Font("Arial", Font.BOLD, 30));
        menuItem.setPreferredSize(new Dimension(200, 40));
        menuItem.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuItem.setContentAreaFilled(false);
        menuItem.setOpaque(true);
        menuItem.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return menuItem;
    }
    private JPanel createLeaderboardPanel(JFrame frame, UserManager userManager, CountDownLatch loginLatch, StateManager state) {
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(new Color(22, 28, 36)); // Dark blue background
    
    // Create a header panel with title and back button
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(new Color(30, 30, 40));
    headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
    
    // Back button
    JButton backButton = new JButton("← Back to Menu");
    backButton.setForeground(Color.WHITE);
    backButton.setBackground(new Color(60, 60, 80));
    backButton.setFocusPainted(false);
    backButton.setBorderPainted(false);
    backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.show(cardPanel, MENU_PANEL);
        }
    });
    
    // Title
    JLabel titleLabel = new JLabel("Leaderboard", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
    titleLabel.setForeground(Color.WHITE);
    
    headerPanel.add(backButton, BorderLayout.WEST);
    headerPanel.add(titleLabel, BorderLayout.CENTER);
    
    // Main content panel
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setBackground(new Color(22, 28, 36));
    contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    
    // Red header panel with "Top Players"
    JPanel topPlayersPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Create red gradient
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(220, 38, 38),
                getWidth(), 0, new Color(185, 28, 28)
            );
            
            g2d.setPaint(gradient);
            
            // Draw rounded rectangle with gradient
            RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), 15, 15
            );
            g2d.fill(roundedRect);
            
            g2d.dispose();
        }
    };
    
    topPlayersPanel.setLayout(new BoxLayout(topPlayersPanel, BoxLayout.Y_AXIS));
    topPlayersPanel.setOpaque(false);
    topPlayersPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    topPlayersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
    
    // Trophy and title
    JPanel titlePanel = new JPanel();
    titlePanel.setOpaque(false);
    titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    
    JLabel trophyLeft = new JLabel("🏆");
    trophyLeft.setFont(new Font("Dialog", Font.PLAIN, 24));
    
    JLabel topPlayersTitle = new JLabel("Top 10 Players");
    topPlayersTitle.setFont(new Font("Arial", Font.BOLD, 24));
    topPlayersTitle.setForeground(Color.WHITE);
    
    JLabel trophyRight = new JLabel("🏆");
    trophyRight.setFont(new Font("Dialog", Font.PLAIN, 24));
    
    titlePanel.add(trophyLeft);
    titlePanel.add(Box.createHorizontalStrut(10));
    titlePanel.add(topPlayersTitle);
    titlePanel.add(Box.createHorizontalStrut(10));
    titlePanel.add(trophyRight);
    
    // Subtitle
    JLabel subtitleLabel = new JLabel("Rankings based on current ratings");
    subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    subtitleLabel.setForeground(new Color(245, 158, 11)); // Yellow
    subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    topPlayersPanel.add(titlePanel);
    topPlayersPanel.add(Box.createVerticalStrut(5));
    topPlayersPanel.add(subtitleLabel);
    
    // Leaderboard content panel with slate background
    JPanel leaderboardContent = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw rounded rectangle background
            g2d.setColor(new Color(45, 55, 72)); // Slate color
            RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), 15, 15
            );
            g2d.fill(roundedRect);
            
            g2d.dispose();
        }
    };
    
    leaderboardContent.setLayout(new BoxLayout(leaderboardContent, BoxLayout.Y_AXIS));
    leaderboardContent.setOpaque(false);
    leaderboardContent.setBorder(new EmptyBorder(15, 15, 15, 15));
    
    // Sort players by rating in descending order
    userManager.players.sort(Comparator.comparingInt(p -> -p.getRating()));
    
    // Add player cards
    int rank = 1;
    for (PlayerInfo player : userManager.players) {
        if(rank <= 10){
        leaderboardContent.add(createStyledLeaderboardPlayerCard(rank, player, userManager.currentPlayer));
        
        // Add spacing between cards
        if (rank < userManager.players.size()) {
            leaderboardContent.add(Box.createVerticalStrut(10));
        }
        rank++;
        }
    }
    
    // Stats summary panel
    JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
    statsPanel.setOpaque(false);
    statsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
    
    // Find current player's rank
    int currentPlayerRank = 1;
    for (int i = 0; i < userManager.players.size(); i++) {
        if (userManager.players.get(i).getUid() == userManager.currentPlayer.getUid()) {
            currentPlayerRank = i + 1;
            break;
        }
    }
    
    // Create stat boxes
    statsPanel.add(createStatBox("Total Players", String.valueOf(userManager.players.size()), Color.WHITE));
    statsPanel.add(createStatBox("Highest Rating", String.valueOf(userManager.players.get(0).getRating()), new Color(245, 158, 11)));
    statsPanel.add(createStatBox("Your Rank", "#" + currentPlayerRank, new Color(52, 211, 153)));
    
    // Create main scrollable content
JPanel mainContent = new JPanel();
mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
mainContent.setBackground(new Color(22, 28, 36));
mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));

// Add all content to main content panel
mainContent.add(topPlayersPanel);
mainContent.add(Box.createVerticalStrut(15));
mainContent.add(leaderboardContent);
mainContent.add(Box.createVerticalStrut(20));
mainContent.add(statsPanel);

// Create scroll pane
JScrollPane scrollPane = new JScrollPane(mainContent);
scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
scrollPane.setBorder(null);
scrollPane.setBackground(new Color(22, 28, 36));
scrollPane.getViewport().setBackground(new Color(22, 28, 36));
scrollPane.getVerticalScrollBar().setUnitIncrement(16);

// Style the scrollbar to match your theme
scrollPane.getVerticalScrollBar().setBackground(new Color(45, 55, 72));

// Add components to main panel
mainPanel.add(headerPanel, BorderLayout.NORTH);
mainPanel.add(scrollPane, BorderLayout.CENTER);
    
    return mainPanel;
}

// Updated player card method with proper styling
private JPanel createStyledLeaderboardPlayerCard(int rank, PlayerInfo player, PlayerInfo currentPlayer) {
    boolean isCurrentPlayer = player.getUid() == currentPlayer.getUid();
    
    JPanel rowPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw rounded rectangle background
            if (isCurrentPlayer) {
                g2d.setColor(new Color(59, 130, 246, 80)); // Blue highlight for current player
            } else {
                g2d.setColor(new Color(45, 55, 72, 150)); // Darker slate for others
            }
            
            RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), 10, 10
            );
            g2d.fill(roundedRect);
            
            // Draw border for current player
            if (isCurrentPlayer) {
                g2d.setColor(new Color(59, 130, 246));
                g2d.setStroke(new BasicStroke(2f));
                g2d.draw(roundedRect);
            }
            
            g2d.dispose();
        }
    };
    
    rowPanel.setLayout(new BorderLayout(10, 0));
    rowPanel.setOpaque(false);
    rowPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
    
    // Left panel for rank and avatar
    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    leftPanel.setOpaque(false);
    
    // Rank indicator in circle
    JPanel rankCircle = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw circle
            g2d.setColor(new Color(30, 41, 59));
            g2d.fillOval(0, 0, getWidth(), getHeight());
            
            g2d.dispose();
        }
    };
    
    rankCircle.setPreferredSize(new Dimension(40, 40));
    rankCircle.setLayout(new GridBagLayout());
    rankCircle.setOpaque(false);
    
    JLabel rankLabel = new JLabel();
    if (rank == 1) {
        rankLabel.setText("💎");
        rankLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        rankLabel.setForeground(new Color(185, 242, 255));
    } else if (rank == 2) {
        rankLabel.setText("👑");
        rankLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        rankLabel.setForeground(new Color(180, 180, 180));
    } else if (rank == 3) {
        rankLabel.setText("👑");
        rankLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        rankLabel.setForeground(new Color(255, 215, 0));
    }else if (rank == 4) {
        rankLabel.setText("🥈⁴");
        rankLabel.setFont(new Font("Dialog", Font.BOLD, 17));
        rankLabel.setForeground(new Color(192, 192, 192));
    } else if (rank == 5) {
        rankLabel.setText("🥉⁵");
        rankLabel.setFont(new Font("Dialog", Font.BOLD, 17));
        rankLabel.setForeground(new Color(205, 127, 50));
    } else {
        rankLabel.setText(String.valueOf(rank));
        rankLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rankLabel.setForeground(Color.WHITE);
    }
    
    rankCircle.add(rankLabel);
    
    // Avatar circle
    JPanel avatarCircle = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw white circle
            g2d.setColor(Color.WHITE);
            g2d.fillOval(0, 0, getWidth(), getHeight());
            
            g2d.dispose();
        }
    };
    
    avatarCircle.setPreferredSize(new Dimension(40, 40));
    avatarCircle.setOpaque(false);
    
    leftPanel.add(rankCircle);
    leftPanel.add(avatarCircle);
    
    // Center panel for player info
    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.setOpaque(false);
    
    // Player name with "YOU" badge if current player
    JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    namePanel.setOpaque(false);
    
    JLabel nameLabel = new JLabel(player.getName());
    nameLabel.setForeground(Color.WHITE);
    nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
    namePanel.add(nameLabel);
    
    if (isCurrentPlayer) {
        JLabel youLabel = new JLabel("YOU");
        youLabel.setOpaque(true);
        youLabel.setBackground(new Color(59, 130, 246));
        youLabel.setForeground(Color.WHITE);
        youLabel.setFont(new Font("Arial", Font.BOLD, 10));
        youLabel.setBorder(new EmptyBorder(2, 6, 2, 6));
        namePanel.add(youLabel);
    }
    
    // Player email
    JLabel emailLabel = new JLabel(player.getEmail());
    emailLabel.setForeground(new Color(156, 163, 175));
    emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    
    centerPanel.add(namePanel);
    centerPanel.add(Box.createVerticalStrut(5));
    centerPanel.add(emailLabel);
    
    // Right panel for rating
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.setOpaque(false);
    
    // Rating with star
    JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
    ratingPanel.setOpaque(false);
    
    JLabel starLabel = new JLabel("★");
    starLabel.setForeground(new Color(245, 158, 11)); // Yellow
    starLabel.setFont(new Font("Dialog", Font.BOLD, 16));
    
    JLabel ratingLabel = new JLabel(String.valueOf(player.getRating()));
    ratingLabel.setForeground(new Color(245, 158, 11)); // Yellow
    ratingLabel.setFont(new Font("Arial", Font.BOLD, 18));
    
    ratingPanel.add(starLabel);
    ratingPanel.add(ratingLabel);
    
    // "Rating" text
    JLabel ratingTextLabel = new JLabel("Rating");
    ratingTextLabel.setForeground(new Color(156, 163, 175));
    ratingTextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    ratingTextLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
    
    rightPanel.add(ratingPanel);
    rightPanel.add(Box.createVerticalStrut(5));
    rightPanel.add(ratingTextLabel);
    
    // Add all panels to the row
    rowPanel.add(leftPanel, BorderLayout.WEST);
    rowPanel.add(centerPanel, BorderLayout.CENTER);
    rowPanel.add(rightPanel, BorderLayout.EAST);
    
    return rowPanel;
}

// Helper method to create stat boxes
private JPanel createStatBox(String label, String value, Color valueColor) {
    JPanel statBox = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw rounded rectangle background
            g2d.setColor(new Color(45, 55, 72));
            RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), 10, 10
            );
            g2d.fill(roundedRect);
            
            g2d.dispose();
        }
    };
    
    statBox.setLayout(new BoxLayout(statBox, BoxLayout.Y_AXIS));
    statBox.setOpaque(false);
    statBox.setBorder(new EmptyBorder(15, 15, 15, 15));
    
    // Value
    JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
    valueLabel.setForeground(valueColor);
    valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
    valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // Label
    JLabel textLabel = new JLabel(label, SwingConstants.CENTER);
    textLabel.setForeground(new Color(156, 163, 175));
    textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    statBox.add(valueLabel);
    statBox.add(Box.createVerticalStrut(5));
    statBox.add(textLabel);
    
    return statBox;
}
    
    private JPanel createPlayerPanel(JFrame frame, UserManager userManager,CountDownLatch loginLatch, StateManager state) {
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(new Color(20, 20, 30));
    
    // Create a header panel with title and back button
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(new Color(30, 30, 40));
    headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
    
    // Back button
    JButton backButton = new JButton("← Back to Menu");
    backButton.setForeground(Color.WHITE);
    backButton.setBackground(new Color(60, 60, 80));
    backButton.setFocusPainted(false);
    backButton.setBorderPainted(false);
    backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.show(cardPanel, MENU_PANEL);
        }
    });
    
    // Title
    JLabel titleLabel = new JLabel("Select Player", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
    titleLabel.setForeground(Color.WHITE);
    
    headerPanel.add(backButton, BorderLayout.WEST);
    headerPanel.add(titleLabel, BorderLayout.CENTER);
    
    // CREATE SCROLLABLE PLAYER CARDS CONTAINER - REPLACE THE OLD CODE HERE
    JPanel cardsContainer = new JPanel();
    cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
    cardsContainer.setBackground(new Color(20, 20, 30));
    cardsContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Create rows of player cards (5 cards per row)
    JPanel currentRow = null;
    int cardsInCurrentRow = 0;
    int maxCardsPerRow = 5;

    for(PlayerInfo player : userManager.players){
        if(!(player.getUid() == userManager.currentPlayer.getUid())){
            
            // Create new row if needed
            if (currentRow == null || cardsInCurrentRow >= maxCardsPerRow) {
                currentRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
                currentRow.setBackground(new Color(20, 20, 30));
                currentRow.setOpaque(false);
                cardsContainer.add(currentRow);
                
                // Add spacing between rows
                if (cardsContainer.getComponentCount() >= 1) {
                    cardsContainer.add(Box.createVerticalStrut(30));
                }
                
                cardsInCurrentRow = 0;
            }
            
            // Add player card to current row
            currentRow.add(createProfileCard(player.getName(), "Rating: "+ Integer.toString(player.getRating()) , "SELECT",userManager));
            cardsInCurrentRow++;
        }
    }

    // Create scroll pane for player cards
    JScrollPane scrollPane = new JScrollPane(cardsContainer);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setBorder(null);
    scrollPane.setBackground(new Color(20, 20, 30));
    scrollPane.getViewport().setBackground(new Color(20, 20, 30));
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    
    // Add a play button at the bottom
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    bottomPanel.setBackground(new Color(30, 30, 40));
    bottomPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
    
    JButton playButton = new JButton("PLAY GAME");
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(new Color(0, 150, 50));
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(false);
        playButton.setFont(new Font("Arial", Font.BOLD, 16));
        playButton.setPreferredSize(new Dimension(200, 40));
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        
                    }
                    
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        playButton.setBackground(new Color(25, 25, 112,255)); // Changed hover color to lighter red
                        playButton.setOpaque(true);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        playButton.setBackground(new Color(0, 150, 50));
                        playButton.setOpaque(true);
                    }
                });
       
         playButton.addActionListener(e -> {
             if(userManager.opponentPlayer!=null){
                 state.myStates = GAME_STATE;
                 loginLatch.countDown();
             }
                 
            
        });
        bottomPanel.add(playButton);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createProfileCard(String username, String rating, String buttonText,UserManager userManager) {
        JPanel card = new GradientPanel(new Color(180, 0, 0), new Color(100, 0, 0));
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(220, 300));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        ImageIcon image;
        // Or if it's an Image, wrap it in ImageIcon:
        
        image = new ImageIcon((Image) userManager.currentPlayer.player);
        
        Image scaledImage = image.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        // Add to imagePanel
        JLabel imageLabel = new JLabel(scaledIcon);
        // Profile image placeholder
        JPanel imagePanel = new RoundedPanel(new Color(40, 40, 50, 100));
        imagePanel.setPreferredSize(new Dimension(190, 190));
        imagePanel.setMaximumSize(new Dimension(190, 190));
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        // Username
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Rating
        JLabel ratingLabel = new JLabel(rating);
        ratingLabel.setForeground(Color.YELLOW);
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Button
        JButton selectButton = createStyledButton(buttonText);
        selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        selectButton.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
             System.out.println("Mouse clicked on select button for user: " + username);
             for(PlayerInfo player : userManager.players){
                 if(player.getName().equals(username)){
                     userManager.opponentPlayer = player.getuser();
                     break;
                 }
             }
             // Your click handling code here
            }

             @Override
              public void mouseEntered(MouseEvent e) {
              System.out.println("Mouse entered on select button for user: " + username);
              selectButton.setBackground(new Color(25, 25, 112,255)); // Changed hover color to lighter red
              selectButton.setOpaque(true);
              // example effect
            }

             @Override
             public void mousePressed(MouseEvent e) {
             System.out.println("Mouse pressed on select button"+ username);
             // Your press handling code here
           }

             @Override
             public void mouseExited(MouseEvent e) {
                 selectButton.setBackground(new Color(0, 150, 50));
                 selectButton.setOpaque(true);
             
        }
        });
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(imagePanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(usernameLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(ratingLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(selectButton);
        
        card.add(contentPanel, BorderLayout.CENTER);
        return card;
    }
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 150, 50));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(100, 30));
        button.setMaximumSize(new Dimension(100, 30));
        return button;
    }
    
    // Custom panel classes
    
    class GradientPanel extends JPanel {
        private final Color color1;
        private final Color color2;
        
        public GradientPanel(Color color1, Color color2) {
            this.color1 = color1;
            this.color2 = color2;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            // Create gradient paint
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
            
            g2d.setPaint(gp);
            
            // Draw rounded rectangle
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, width - 1, height - 1, 20, 20);
            g2d.fill(roundedRectangle);
            
            // Add subtle glow effect
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.1f));
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(3, 3, width - 7, height - 7, 20, 20);
            
            g2d.dispose();
        }
    }
    
    class RoundedPanel extends JPanel {
        private final Color backgroundColor;
        
        public RoundedPanel(Color bgColor) {
            this.backgroundColor = bgColor;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, width, height, 30, 30); // Rounded corners
            
            g2d.dispose();
        }
    }
}
