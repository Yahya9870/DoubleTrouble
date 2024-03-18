import java.awt.*;
import java.awt.event.ActionEvent;
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
        setLayout(new BorderLayout());
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(1, 3));

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(getButtonText(i));
            Color color = getColor(i);
            buttons[i].setBackground(color);
            buttons[i].setForeground(getContrastingColor(color));
            buttons[i].setOpaque(true);
            buttons[i].setBorderPainted(false);
            int finalI = i;
            buttons[i].addActionListener(e -> playerMove(finalI));
            gamePanel.add(buttons[i]);
        }

        add(statusLabel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        chooseStarter();
    }

    private void chooseStarter() {
        Object[] options = {"Player", "Computer"};
        int n = JOptionPane.showOptionDialog(this, "Who goes first?",
                "Choose Starter", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (n == 1) {
            playerTurn = false;
            computerMove();
        }
    }
    private boolean isGameOver() {
        return greenMarkers == 0 && yellowMarkers == 0 && orangeMarkers == 0;
    }


    private void playerMove(int color) {
        if (playerTurn) {
            String input = JOptionPane.showInputDialog("Enter number of markers to remove:");
            try {
                int num = Integer.parseInt(input);
                if (num > 0 && makeMove(color, num)) {
                    updateStatus();
                    if (isGameOver()) {
                        JOptionPane.showMessageDialog(this, "You win!");
                    } else {
                        playerTurn = false;
                        computerMove();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid number of markers.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            }
        }
    }

    private void computerMove() {
        SwingUtilities.invokeLater(() -> {
            if (isGameOver()) {
                JOptionPane.showMessageDialog(this, "Game Over. You win!");
                return;
            }

            // Simplified strategy for demonstration purposes
            int color = -1;
            int maxMarkers = 0;
            // Choose the color with the most markers to try and leave more options open
            for (int i = 0; i < 3; i++) {
                int count = getMarkerCount(i);
                if (count > maxMarkers) {
                    maxMarkers = count;
                    color = i;
                }
            }

            // If no color has been chosen (i.e., all markers are 0, which shouldn't happen here because we check gameOver before),
            // or as a fallback if somehow all counts were 0, just randomly pick a color with available markers
            if (color == -1) {
                do {
                    color = random.nextInt(3);
                } while (getMarkerCount(color) == 0);
            }

            int markersToRemove = 1 + random.nextInt(getMarkerCount(color)); // Always remove at least one marker

            // Making the move
            if (makeMove(color, markersToRemove)) {
                updateButtons();
                playerTurn = true;
                updateStatus();
                if (isGameOver()) {
                    JOptionPane.showMessageDialog(this, "Computer wins. Try again!");
                }
            } else {
                // This else block should never be reached because we check for game over
                // and valid moves. It's here as a safeguard.
                JOptionPane.showMessageDialog(this, "An error occurred in computer's move.");
            }
        });
    }


    private int calculateBestMove(int color) {
        // This is a simplified logic for computer's move.
        // Ideally, this method should contain the logic to calculate the best move.
        int markers = getMarkerCount(color);
        return markers > 0 ? 1 : 0; // Dummy logic: remove one marker if possible
    }

    private void updateButtons() {
        buttons[0].setText("Green: " + greenMarkers);
        buttons[1].setText("Yellow: " + yellowMarkers);
        buttons[2].setText("Orange: " + orangeMarkers);

        // Additional logic to handle the game over state could also go here
        if (isGameOver()) {
            statusLabel.setText("Game Over.");
            for (JButton button : buttons) {
                button.setEnabled(false); // Disable buttons when the game is over
            }
            if(playerTurn){
                JOptionPane.showMessageDialog(this, "Congratulations, you win!");
            } else {
                JOptionPane.showMessageDialog(this, "Computer wins. Try again!");
            }
        }
    }


    private boolean makeMove(int color, int num) {
        switch (color) {
            case 0: if (num <= greenMarkers && num > 0) { greenMarkers -= num; return true; }
            case 1: if (num <= yellowMarkers && num > 0) { yellowMarkers -= num; return true; }
            case 2: if (num <= orangeMarkers && num > 0) { orangeMarkers -= num; return true; }
        }
        return false;
    }

    private void updateStatus() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(getButtonText(i));
        }
        if (isGameOver()) {
            for (JButton button : buttons) {
                button.setEnabled(false);
            }
            statusLabel.setText("Game Over.");
        } else {
            statusLabel.setText(playerTurn ? "Your move." : "Computer's move.");
        }
    }

    private int getMarkerCount(int color) {
        return switch (color) {
            case 0 -> greenMarkers;
            case 1 -> yellowMarkers;
            case 2 -> orangeMarkers;
            default -> 0;
        };
    }

    private String getButtonText(int color) {
        return switch (color) {
            case 0 -> "Green: " + greenMarkers;
            case 1 -> "Yellow: " + yellowMarkers;
            case 2 -> "Orange: " + orangeMarkers;
            default -> "";
        };
    }

    private Color getColor(int color) {
        return switch (color) {
            case 0 -> Color.GREEN;
            case 1 -> Color.YELLOW;
            case 2 -> Color.ORANGE;
            default -> Color.BLACK;
        };
    }

    private Color getContrastingColor(Color color) {
        double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DoubleTroubleGame::new);
    }
}
