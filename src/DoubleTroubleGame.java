import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class DoubleTroubleGame extends JFrame {
    private int greenMarkers = 3, yellowMarkers = 7, orangeMarkers = 5;
    private final JLabel statusLabel = new JLabel("Choose who starts: Player or Computer");
    private final JButton[] buttons = new JButton[3];
    private boolean playerTurn = true;
    private final Random random = new Random();

    public DoubleTroubleGame() {
        super("Double Trouble");
        setLayout(new GridLayout(2, 1));
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new FlowLayout());

        buttons[0] = new JButton("Green: 3");
        buttons[1] = new JButton("Yellow: 7");
        buttons[2] = new JButton("Orange: 5");

        for (int i = 0; i < 3; i++) {
            int color = i;
            buttons[i].addActionListener(e -> playerMove(color));
            gamePanel.add(buttons[i]);
        }

        add(statusLabel);
        add(gamePanel);

        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Choose Starter Dialog
        Object[] options = {"Player", "Computer"};
        int n = JOptionPane.showOptionDialog(this, "Who goes first?",
                "Choose Starter", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (n == 1) {
            playerTurn = false;
            computerMove();
        }
    }

    private void playerMove(int color) {
        if (!playerTurn) return;

        String input = JOptionPane.showInputDialog(this, "Enter number of markers to remove:");
        try {
            int num = Integer.parseInt(input);
            if (makeMove(color, num)) {
                playerTurn = false;
                updateButtons();
                if (!isGameOver()) computerMove();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid move, try again.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }

    private void computerMove() {
        // Dummy implementation - Replace with strategy logic
        SwingUtilities.invokeLater(() -> {
            int color = random.nextInt(3);
            int markersCount = getMarkerCount(color);

            // Ensure we do not attempt to make a move if there are no markers left of this color
            if (markersCount > 0) {
                int num = 1 + random.nextInt(markersCount);
                makeMove(color, num);
            } else {
                // This branch tries another color if the initially chosen one has no markers left.
                // It's a simple way to avoid the exception but consider implementing a smarter strategy for choosing the color and number of markers.
                for (int i = 0; i < 3; i++) {
                    if (getMarkerCount(i) > 0) {
                        color = i;
                        break; // Found a color with markers left, exit the loop.
                    }
                }
                int num = 1 + random.nextInt(getMarkerCount(color)); // Now, this should be safe
                makeMove(color, num);
            }

            updateButtons();
            playerTurn = true;
            statusLabel.setText("Your turn. Choose a color to remove markers from.");
            if (isGameOver()) {
                JOptionPane.showMessageDialog(this, "Computer wins!");
            }
        });
    }


    private boolean makeMove(int color, int num) {
        switch (color) {
            case 0: if (num <= greenMarkers && num > 0) { greenMarkers -= num; return true; } break;
            case 1: if (num <= yellowMarkers && num > 0) { yellowMarkers -= num; return true; } break;
            case 2: if (num <= orangeMarkers && num > 0) { orangeMarkers -= num; return true; } break;
        }
        return false;
    }

    private int getMarkerCount(int color) {
        switch (color) {
            case 0: return greenMarkers;
            case 1: return yellowMarkers;
            case 2: return orangeMarkers;
        }
        return 0; // Should never happen
    }

    private void updateButtons() {
        buttons[0].setText("Green: " + greenMarkers);
        buttons[1].setText("Yellow: " + yellowMarkers);
        buttons[2].setText("Orange: " + orangeMarkers);

        if (isGameOver()) {
            statusLabel.setText("Game Over. You win!");
            for (JButton button : buttons) {
                button.setEnabled(false);
            }
        }
    }

    private boolean isGameOver() {
        return greenMarkers == 0 && yellowMarkers == 0 && orangeMarkers == 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DoubleTroubleGame::new);
    }
}
