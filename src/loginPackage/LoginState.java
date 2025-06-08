package loginPackage;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import abstractPackage.parentState;
import chessgame.StateManager;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import userData.PlayerInfo;
import userData.UserManager;

class LoginForm extends JPanel {
    private Color textColor = new Color(255, 255, 255);
    private Image piece;
    private Image backgroundImage;
    private Image chessBoardImage;
    private UserManager userManager;
    private CountDownLatch loginLatch;
    
    // Add a flag to track current view state
    private boolean isLoginView = true;
    
    // Text elements that will change between views
    private String welcomeText = "Welcome back!";
    private String subtitleText1 = "You can sign in to access with your";
    private String subtitleText2 = "existing account.";
    
    public LoginForm(UserManager userManager, CountDownLatch loginLatch) {
        this.userManager = userManager;
        this.loginLatch = loginLatch;
        
        // Load the background image
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/loginPackage/blueBack.png"));
            chessBoardImage = ImageIO.read(getClass().getResource("/loginPackage/chessBoard.png"));
            this.piece = ImageIO.read(getClass().getResource("/loginPackage/myPiece.png"));
            
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Set layout to null for absolute positioning
        setLayout(null);
        setPreferredSize(new Dimension(1920, 1080));
        
        // Create and show the initial login view
        createLoginView();
    }
    
    private void createLoginView() {
        removeAll(); // Clear any existing components
        
        // Reset text for login view
        welcomeText = "Welcome back!";
        subtitleText1 = "You can sign in to access with your";
        subtitleText2 = "existing account.";
        isLoginView = true;
        
        // Create a container for the sign-in form with rounded corners and semi-transparent background
        JPanel formContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create semi-transparent white background
                g2d.setColor(new Color(255, 255, 255, 200)); // White with 80% opacity
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                
                g2d.dispose();
            }
        };
        
        // Set the form container size and position (centered)
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setOpaque(false);
        formContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formContainer.setBounds(520, 190, 500, 600); // Centered position
        
        // Sign In form components
        JLabel signInLabel = new JLabel("Sign In");
        signInLabel.setFont(new Font("Arial Black", Font.BOLD, 40));
        signInLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Email field
        JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.setOpaque(false);
        emailPanel.setMaximumSize(new Dimension(400, 50));
        
        JLabel emailIcon = new JLabel("📧");
        emailIcon.setFont(emailIcon.getFont().deriveFont(25f));
        emailPanel.add(emailIcon, BorderLayout.WEST);
        
        JTextField emailField = new JTextField("Username or email");
        emailField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("Username or email")) {
                    emailField.setText(""); // Clear the placeholder text when the field is focused
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("Username or email"); // Reset the placeholder text when the field is empty
                }
            }
        });
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        emailPanel.add(emailField, BorderLayout.CENTER);
        
        // Password field
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.setMaximumSize(new Dimension(400, 50));
        passwordPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel passwordIcon = new JLabel("🔒");
        passwordIcon.setFont(emailIcon.getFont().deriveFont(25f));
        passwordPanel.add(passwordIcon, BorderLayout.WEST);
        
        JPasswordField passwordField = new JPasswordField("Password");
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // If the text is the placeholder, clear it
                if (String.valueOf(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText(""); // Clear the placeholder text when the field is focused
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // If the password field is empty, reset the placeholder text
                if (passwordField.getPassword().length == 0) {
                    passwordField.setText("Password"); // Reset the placeholder text when the field is empty
                }
            }
        });
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        
        // Remember me and forgot password
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setOpaque(false);
        optionsPanel.setMaximumSize(new Dimension(400, 30));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JCheckBox rememberMe = new JCheckBox("Remember me");
        rememberMe.setOpaque(false);
        optionsPanel.add(rememberMe, BorderLayout.WEST);
        
        JLabel forgotPassword = new JLabel("Forgot password?");
        forgotPassword.setForeground(Color.GRAY);
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        optionsPanel.add(forgotPassword, BorderLayout.EAST);
        
        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false);
        
        // Sign In button
        JButton signInButton = new JButton("Sign In");
        signInButton.setBackground(new Color(0, 0, 128));
        signInButton.setForeground(Color.WHITE);
        signInButton.setFocusPainted(false);
        signInButton.setBorderPainted(false);
        signInButton.setMaximumSize(new Dimension(400, 40));
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        signInButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listener to the sign in button
        signInButton.addActionListener(e -> {
            String username = emailField.getText();
            String password = new String(passwordField.getPassword());
            
            // Check login credentials
            PlayerInfo currentPlayer = userManager.checkUser(username, password);
            if (currentPlayer != null) {
                // Signal that login is successful
                userManager.currentPlayer = currentPlayer;
                loginLatch.countDown();
            } else {
                // Failed login: make the email and password field borders red, and text inside also red
                emailField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.RED),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                emailField.setForeground(Color.RED);  // Change the text color to red
                
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.RED),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                passwordField.setForeground(Color.RED);  // Change the text color to red
                
                // Optionally, display an error message on the errorLabel
                errorLabel.setText("Invalid email or password.");
                errorLabel.setVisible(true);  // Show error label
            }
        });
        
        // New here? Create an account
        JPanel createAccountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createAccountPanel.setOpaque(false);
        createAccountPanel.setMaximumSize(new Dimension(400, 30));
        createAccountPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel newHereLabel = new JLabel("New here?");
        newHereLabel.setForeground(Color.BLACK);
        
        JLabel createAccountLabel = new JLabel("Create an account");
        createAccountLabel.setForeground(new Color(0, 0, 128));
        createAccountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add click listener to switch to registration view
        createAccountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createRegistrationView();
            }
        });
        
        createAccountPanel.add(newHereLabel);
        createAccountPanel.add(createAccountLabel);
        
        // Add components to form panel
        formPanel.add(emailPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(optionsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(signInButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(createAccountPanel);
        
        // Add components to form container
        formContainer.add(signInLabel);
        formContainer.add(formPanel);
        
        // Add form container to main panel
        add(formContainer);
        
        revalidate();
        repaint();
    }
    
    private void createRegistrationView() {
        removeAll(); // Clear any existing components
        
        // Update text for registration view
        welcomeText = "Hello Friend!";
        subtitleText1 = "You can create your account here!";
        subtitleText2 = "";
        isLoginView = false;
        
        // Create a container for the registration form with rounded corners and semi-transparent background
        JPanel formContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create semi-transparent white background
                g2d.setColor(new Color(255, 255, 255, 200)); // White with 80% opacity
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                
                g2d.dispose();
            }
        };
        
        // Set the form container size and position (centered)
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setOpaque(false);
        formContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formContainer.setBounds(520, 190, 500, 600); // Centered position
        
        // Sign Up form components
        JLabel signUpLabel = new JLabel("Sign Up");
        signUpLabel.setFont(new Font("Arial Black", Font.BOLD, 40));
        signUpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Name field
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setOpaque(false);
        namePanel.setMaximumSize(new Dimension(400, 50));
        
        JLabel nameIcon = new JLabel("👤");
        nameIcon.setFont(nameIcon.getFont().deriveFont(25f));
        namePanel.add(nameIcon, BorderLayout.WEST);
        
        JTextField nameField = new JTextField("Full Name");
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("Full Name")) {
                    nameField.setText(""); // Clear the placeholder text when the field is focused
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText("Full Name"); // Reset the placeholder text when the field is empty
                }
            }
        });
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        namePanel.add(nameField, BorderLayout.CENTER);
        
        // Email field
        JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.setOpaque(false);
        emailPanel.setMaximumSize(new Dimension(400, 50));
        emailPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel emailIcon = new JLabel("📧");
        emailIcon.setFont(emailIcon.getFont().deriveFont(25f));
        emailPanel.add(emailIcon, BorderLayout.WEST);
        
        JTextField emailField = new JTextField("Email");
        emailField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("Email")) {
                    emailField.setText(""); // Clear the placeholder text when the field is focused
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("Email"); // Reset the placeholder text when the field is empty
                }
            }
        });
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        emailPanel.add(emailField, BorderLayout.CENTER);
        
        // Password field
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.setMaximumSize(new Dimension(400, 50));
        passwordPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel passwordIcon = new JLabel("🔒");
        passwordIcon.setFont(emailIcon.getFont().deriveFont(25f));
        passwordPanel.add(passwordIcon, BorderLayout.WEST);
        
        JPasswordField passwordField = new JPasswordField("Password");
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // If the text is the placeholder, clear it
                if (String.valueOf(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText(""); // Clear the placeholder text when the field is focused
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // If the password field is empty, reset the placeholder text
                if (passwordField.getPassword().length == 0) {
                    passwordField.setText("Password"); // Reset the placeholder text when the field is empty
                }
            }
        });
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        
        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false);
        
        // Create Account button
        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBackground(new Color(0, 0, 128));
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setFocusPainted(false);
        createAccountButton.setBorderPainted(false);
        createAccountButton.setMaximumSize(new Dimension(400, 40));
        createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccountButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        createAccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listener to the create account button (placeholder only)
        createAccountButton.addActionListener(e -> {
            String username = nameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            // Check login credentials
            boolean check = false ; 
            for(int i = 0; i < email.length() ; i++){
               if(email.charAt(i) == '@'){
                   if(email.charAt(i+1) == 'g' && email.charAt(i+2) == 'm' && email.charAt(i+3) == 'a' && email.charAt(i+4) == 'i' && email.charAt(i+5) == 'l' && email.charAt(i+6) == '.' && email.charAt(i+7) == 'c' && email.charAt(i+8) == 'o' && email.charAt(i+9) == 'm' ){
                       check = true;
                       break;
               }
            }
            }
               if(!check){
                     
                emailField.setForeground(Color.RED);  // Change the text color to red
                errorLabel.setText("Email is Invalid!.");
                errorLabel.setVisible(true);
               }
               else{
                   
               
            boolean checkUserName = userManager.searchUserName(username);
            
            if (userManager.searchUserName(username) && userManager.searchUserEmail(email)) {
                // Signal that login is successful
                PlayerInfo addPlayer = new PlayerInfo(username,email,password,200);
                userManager.addUser(addPlayer);
                try {
                    userManager.addUserToDB(addPlayer);
                } catch (SQLException ex) {
                    Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                createLoginView();
            } else if(!userManager.searchUserName(username)) {
                // Failed login: make the email and password field borders red, and text inside also red
                
                nameField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.RED),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                nameField.setForeground(Color.RED);  // Change the text color to red
                
                // Optionally, display an error message on the errorLabel
                errorLabel.setText("Username is already taken.");
                errorLabel.setVisible(true);  // Show error label
            }
            else if(!userManager.searchUserEmail(email)){
                emailField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.RED),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                emailField.setForeground(Color.RED);  // Change the text color to red
                errorLabel.setText("Email is already taken.");
                errorLabel.setVisible(true);  // Show error label
           
            
            }
            
               
               }
        });
        
        // Already have an account? Sign in
        JPanel signInPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signInPanel.setOpaque(false);
        signInPanel.setMaximumSize(new Dimension(400, 30));
        signInPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel alreadyHaveAccountLabel = new JLabel("Already have an account?");
        alreadyHaveAccountLabel.setForeground(Color.BLACK);
        
        JLabel signInLabel = new JLabel("Sign In");
        signInLabel.setForeground(new Color(0, 0, 128));
        signInLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add click listener to switch back to login view
        signInLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createLoginView();
            }
        });
        
        signInPanel.add(alreadyHaveAccountLabel);
        signInPanel.add(signInLabel);
        
        // Add components to form panel
        formPanel.add(namePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(emailPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(errorLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(createAccountButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(signInPanel);
        
        // Add components to form container
        formContainer.add(signUpLabel);
        formContainer.add(formPanel);
        
        // Add form container to main panel
        add(formContainer);
        
        revalidate();
        repaint();
    }
    
    // Method to set background image programmatically
    public void setBackgroundImage(String imagePath) {
        try {
            this.backgroundImage = ImageIO.read(getClass().getResource("/loginPackage/blueBack.png"));
            this.chessBoardImage = ImageIO.read(getClass().getResource("/loginPackage/chessBoard.png"));
            this.piece = ImageIO.read(getClass().getResource("/loginPackage/myPiece.png"));
            repaint();
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable anti-aliasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Draw background image if available
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Draw default background if image not available
            drawDefaultBackground(g2d);
        }
        if (piece != null) {
            g2d.drawImage(piece, 250, 150, 400, 350, this);
            System.out.println("Hello");
        }
        if (backgroundImage != null) {
            g2d.drawImage(chessBoardImage, 100, 0, 1500, 800, this);
        }
        
        // Draw welcome text overlay in the center-left area
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setColor(new Color(0, 0, 0, 50)); // Very light black for text background
        g2d.fillRoundRect(520, getHeight()/2 - 370, 500, 120, 20, 20);
        
        // Reset composite for text
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        // Draw welcome text
        g2d.setColor(textColor);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(welcomeText);
        g2d.drawString(welcomeText, 520 + (500 - textWidth) / 2, getHeight() / 2 - 320);
        
        // Draw subtitle
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        fm = g2d.getFontMetrics();
        textWidth = fm.stringWidth(subtitleText1);
        g2d.drawString(subtitleText1, 520 + (500 - textWidth) / 2, getHeight() / 2 - 290);
        
        if (!subtitleText2.isEmpty()) {
            textWidth = fm.stringWidth(subtitleText2);
            g2d.drawString(subtitleText2, 520 + (500 - textWidth) / 2, getHeight() / 2 - 265);
        }
        
        g2d.dispose();
    }
    
    private void drawDefaultBackground(Graphics2D g2d) {
        // Fill background with solid light green (simplified - no decorative elements)
        g2d.setColor(new Color(144, 238, 144));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}

public class LoginState extends parentState {
    

    @Override
    public StateManager.GameState run(UserManager userManager, JFrame frame, StateManager state) {
        // Use the existing frame instead of creating a new one
        CountDownLatch loginLatch = new CountDownLatch(1);
        frame.getContentPane().removeAll(); // Clear any existing components
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Create the login form
        LoginForm loginForm = new LoginForm(userManager, loginLatch);
        
        // Add the login form to the existing frame
        frame.add(loginForm);
        frame.setTitle("Chess Game - Login");
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
        
        try {
            // Block until authentication is complete
            loginLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // After login is complete, clear the frame for the next state
        frame.getContentPane().removeAll();
        return getState(state);
    }
    
    public StateManager.GameState getState(StateManager state){
        return state.menuState();
    }
    
}
