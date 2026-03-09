import gui.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("Запуск");
        SwingUtilities.invokeLater(() -> {
            MainFrame.main(args);
        });
    }
}