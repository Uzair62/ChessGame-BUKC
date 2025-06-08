package chess1;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

public class TurnManagerPanel extends JPanel {
    private boolean isWhiteToMove = true;
    
    public TurnManagerPanel() {
        this.setLayout(null);
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(0, 45));
    }
    
    public void setCurrentTurn(boolean isWhiteToMove) {
        this.isWhiteToMove = isWhiteToMove;
        repaint(); // Trigger a repaint when turn changes
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Gradient colors
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(160, 160, 160, 100),
            0, getHeight(), new Color(80, 80, 80, 100)
        );
        
        g2d.setPaint(gradient);
        
        // Draw a rounded rectangle with the gradient spanning full width
        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
            0, 0, getWidth(), getHeight(), 20, 20
        );
        g2d.fill(roundedRect);
        
        // Add a subtle glow border
        g2d.setColor(new Color(100, 200, 255, 40));
        g2d.setStroke(new java.awt.BasicStroke(2f));
        g2d.draw(roundedRect);
        
        // Draw the turn indicator text
        String turnText = isWhiteToMove ? "White's Turn" : "Black's Turn";
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(turnText)) / 2;
        int y = (getHeight() + fm.getAscent()) / 2 - 4;
        g2d.drawString(turnText, x, y);
        
        g2d.dispose();
    }
}
