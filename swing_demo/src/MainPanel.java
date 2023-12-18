import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainPanel extends JPanel {

    public JButton button;

    public MainPanel() {
        super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(GameManager.SIZE, GameManager.SIZE));

        setBackground(Color.white);

        JLabel label = new JLabel();
        label.setBorder(new EmptyBorder(new Insets(10, 0, GameManager.SIZE / 3, 0)));
        label.setText("swing snake demo");
        label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        label.setVisible(true);

        button = new JButton();
        button.setText("play");
        button.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        button.setVisible(true);

        super.add(label);
        super.add(button);
        repaint();
    }
}
