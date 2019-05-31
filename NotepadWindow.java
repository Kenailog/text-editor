import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class NotepadWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    public NotepadWindow() {
        initComponents();
    }

    private void initComponents() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("MyNotepad");
        setExtendedState(MAXIMIZED_BOTH);
        getContentPane().add(new Pane());

        pack();
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new NotepadWindow().setVisible(true);
            }
        });
    }
}