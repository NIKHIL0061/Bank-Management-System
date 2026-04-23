import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * A panel with rounded corners, optional shadow, and gradient background
 */
public class RoundedPanel extends JPanel {
    private int cornerRadius = 20;
    private Color backgroundColor;
    private boolean shadowEnabled = true;
    
    public RoundedPanel(LayoutManager layout, int radius, Color bg) {
        super(layout);
        this.cornerRadius = radius;
        this.backgroundColor = bg;
        setOpaque(false);
    }
    
    public RoundedPanel(int radius, Color bg) {
        this(null, radius, bg);
    }
    
    public void setShadowEnabled(boolean enabled) {
        this.shadowEnabled = enabled;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        
        // Draw shadow
        if (shadowEnabled) {
            g2.setColor(new Color(0, 0, 0, 50));
            for (int i = 0; i < 5; i++) {
                g2.fillRoundRect(i, i + 2, w - i * 2, h - i * 2, cornerRadius, cornerRadius);
            }
        }
        
        // Draw background
        if (backgroundColor != null) {
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
        } else {
            // Gradient background
            GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255, 240), 
                                                 0, h, new Color(245, 245, 250, 240));
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
        }
        
        g2.dispose();
        super.paintComponent(g);
    }
}