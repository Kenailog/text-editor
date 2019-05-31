import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.*;

import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;

public class Pane extends JPanel {

    private static final long serialVersionUID = 1L;

    private RSyntaxTextArea textArea = new RSyntaxTextArea();
    private RTextScrollPane scrollPane = new RTextScrollPane(textArea);

    private Font font = new Font("Hack", Font.PLAIN, 12);

    private File _file;
    private File configFile = new File("config.properties");
    private JTextField fileName = new JTextField();

    private JMenuBar menuBar = new JMenuBar();
    private JMenu file = new JMenu("File");
    private JMenuItem compileAndRun = new JMenuItem("Compile&Run");
    private JMenuItem runCmd = new JMenuItem("Run cmd");
    private JMenuItem newFile = new JMenuItem("New File");
    private JMenuItem open = new JMenuItem("Open");
    private JMenuItem save = new JMenuItem("Save");
    private JSeparator fileSeparator01 = new JSeparator();

    private JMenu language = new JMenu("Language Mode");
    private ButtonGroup languageButtonGroup = new ButtonGroup();
    private JCheckBoxMenuItem noneLanguage = new JCheckBoxMenuItem("None", true);
    private JCheckBoxMenuItem javaLanguage = new JCheckBoxMenuItem("Java");
    private JCheckBoxMenuItem cPlusPlusLanguage = new JCheckBoxMenuItem("C++");
    private JCheckBoxMenuItem HTMLLanguage = new JCheckBoxMenuItem("HTML");
    private JCheckBoxMenuItem phpLanguage = new JCheckBoxMenuItem("php");

    private JMenu properties = new JMenu("Properties");
    private JMenuItem color = new JMenuItem("Color");
    private JSeparator propertiesSeparator01 = new JSeparator();
    private JMenuItem openConfigFile = new JMenuItem("Edit Configuration File");
    private JMenuItem loadConfigFile = new JMenuItem("Load Configuration File");

    private JMenu loadTheme = new JMenu("Load Theme");
    private ButtonGroup themeButtonGroup = new ButtonGroup();
    private JCheckBoxMenuItem darkTheme = new JCheckBoxMenuItem("Dark");
    private JCheckBoxMenuItem eclipseTheme = new JCheckBoxMenuItem("Eclipse");
    private JCheckBoxMenuItem monokaiTheme = new JCheckBoxMenuItem("Monokai");
    private JCheckBoxMenuItem visualStudioTheme = new JCheckBoxMenuItem("Visual Studio");
    private JCheckBoxMenuItem ideaTheme = new JCheckBoxMenuItem("Idea");
    private JCheckBoxMenuItem defaultTheme = new JCheckBoxMenuItem("Default");

    private Theme theme;

    public Pane() {
        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        fileName.setBorder(null);
        fileName.setBackground(Color.BLACK);
        fileName.setForeground(Color.GRAY);
        fileName.setText("New File");
        fileName.setEditable(false);
        add(fileName, BorderLayout.NORTH);

        menuBar.setBackground(Color.BLACK);
        menuBar.setBorderPainted(false);
        menuBar.setBorder(null);

        file.setForeground(Color.LIGHT_GRAY);
        file.setMnemonic(KeyEvent.VK_ALT);

        language.setForeground(Color.LIGHT_GRAY);
        properties.setForeground(Color.LIGHT_GRAY);

        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        newFile.addActionListener(e -> {
            _file = null;
            fileName.setText("New File");
            textArea.setText(null);
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        });

        file.add(newFile);

        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        save.addActionListener(e -> {
            saveFile();
            fileName.setText(_file.getAbsolutePath());
        });

        file.add(open);

        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        open.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(Pane.this) == JFileChooser.APPROVE_OPTION) {
                _file = fileChooser.getSelectedFile();
                fileName.setText(_file.getAbsolutePath());
                try {
                    textArea.setText(null);
                    Scanner input = new Scanner(_file);
                    while (input.hasNextLine()) {
                        textArea.append(input.nextLine() + "\n");
                    }
                    input.close();
                } catch (FileNotFoundException exc) {
                    exc.printStackTrace();
                }
            }
        });

        file.add(save);
        file.add(fileSeparator01);

        // compileAndRun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
        // KeyEvent.CTRL_DOWN_MASK));
        // compileAndRun.addActionListener(new ActionListener() {

        // @Override
        // public void actionPerformed(ActionEvent e) {

        // }
        // });

        file.add(compileAndRun);

        runCmd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        runCmd.addActionListener(e -> {
            try {
                Runtime.getRuntime().exec(new String[]{"cmd", "/K", "Start"});
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        });

        file.add(runCmd);

        menuBar.add(file);

        noneLanguage.addItemListener(e -> setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE));
        javaLanguage.addItemListener(e -> setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA));
        cPlusPlusLanguage.addItemListener(e -> setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS));
        HTMLLanguage.addItemListener(e -> setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML));
        phpLanguage.addItemListener(e -> setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PHP));

        languageButtonGroup.add(noneLanguage);
        languageButtonGroup.add(javaLanguage);
        languageButtonGroup.add(cPlusPlusLanguage);
        languageButtonGroup.add(HTMLLanguage);
        languageButtonGroup.add(phpLanguage);

        language.add(noneLanguage);
        language.add(javaLanguage);
        language.add(cPlusPlusLanguage);
        language.add(HTMLLanguage);
        language.add(phpLanguage);

        menuBar.add(language);

        color.addActionListener(e -> textArea.setBackground(JColorChooser.showDialog(Pane.this, "Color Chooser", new Color(50, 40, 50))));

        properties.add(color);
        properties.add(propertiesSeparator01);

        openConfigFile.addActionListener(e -> {
            _file = configFile;
            fileName.setText(_file.getAbsolutePath());
            try {
                textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
                textArea.setText(null);
                Scanner input = new Scanner(configFile);
                while (input.hasNextLine()) {
                    textArea.append(input.nextLine() + "\n");
                }
                input.close();
            } catch (FileNotFoundException exc) {
                exc.printStackTrace();
            }
        });

        properties.add(openConfigFile);

        loadConfigFile.addActionListener(e -> setLook());

        properties.add(loadConfigFile);

        darkTheme.addItemListener(e -> setTheme("dark"));
        eclipseTheme.addItemListener(e -> setTheme("eclipse"));
        monokaiTheme.addItemListener(e -> setTheme("monokai"));
        visualStudioTheme.addItemListener(e -> setTheme("vs"));
        ideaTheme.addItemListener(e -> setTheme("idea"));
        defaultTheme.addItemListener(e -> theme = null);

        themeButtonGroup.add(darkTheme);
        themeButtonGroup.add(eclipseTheme);
        themeButtonGroup.add(monokaiTheme);
        themeButtonGroup.add(visualStudioTheme);
        themeButtonGroup.add(ideaTheme);
        themeButtonGroup.add(defaultTheme);

        loadTheme.add(darkTheme);
        loadTheme.add(eclipseTheme);
        loadTheme.add(monokaiTheme);
        loadTheme.add(visualStudioTheme);
        loadTheme.add(ideaTheme);
        loadTheme.add(defaultTheme);

        properties.add(loadTheme);

        menuBar.add(properties);

        add(menuBar, BorderLayout.SOUTH);
        add(scrollPane);

        setLook();
    }

    private void saveFile() {
        if (_file == null) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(Pane.this) == JFileChooser.APPROVE_OPTION) {
                _file = fileChooser.getSelectedFile();
            }
        }

        if (file != null) {
            try {
                Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(_file)));
                textArea.write(writer);
                writer.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }

    private void setTheme(String themeName) {
        try {
            theme = Theme.load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/" + themeName + ".xml"));
            theme.apply(textArea);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private void setSyntaxEditingStyle(String constant) {
        textArea.setSyntaxEditingStyle(constant);
    }

    private void setLook() {
        try {
            FileReader fileReader = new FileReader(configFile);
            Properties properties = new Properties();
            properties.load(fileReader);

            textArea.setBackground(
                    new Color(Integer.parseInt(properties.getProperty("textAreaBackgroundColorRED")),
                            Integer.parseInt(properties.getProperty("textAreaBackgroundColorGREEN")),
                            Integer.parseInt(properties.getProperty("textAreaBackgroundColorBLUE"))));
            textArea.setForeground(
                    new Color(Integer.parseInt(properties.getProperty("textAreaForegroundColorRED")),
                            Integer.parseInt(properties.getProperty("textAreaForegroundColorGREEN")),
                            Integer.parseInt(properties.getProperty("textAreaForegroundColorBLUE"))));
            textArea.setFont(
                    new Font(properties.getProperty("font"),
                            Integer.parseInt(properties.getProperty("fontMode")),
                            Integer.parseInt(properties.getProperty("fontSize"))));
            textArea.setCaretColor(
                    new Color(Integer.parseInt(properties.getProperty("caretColorRED")),
                            Integer.parseInt(properties.getProperty("caretColorGREEN")),
                            Integer.parseInt(properties.getProperty("caretColorBLUE"))));
            textArea.setCaretStyle(Integer.parseInt(properties.getProperty("caretStyleMode")), CaretStyle.BLOCK_STYLE);
            textArea.getCaret().setBlinkRate(Integer.parseInt(properties.getProperty("blinkRate")));
            textArea.setHighlightCurrentLine(Boolean.getBoolean(properties.getProperty("highlightCurrentLine")));
            textArea.setCodeFoldingEnabled(Boolean.getBoolean(properties.getProperty("codeFoldingEnabled")));
            textArea.setPaintTabLines(Boolean.getBoolean(properties.getProperty("paintTabLines")));
            textArea.setTabLineColor(Color.LIGHT_GRAY);
            scrollPane.setVerticalScrollBarPolicy(RTextScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setLineNumbersEnabled(true);
            scrollPane.getGutter().setBackground(Color.BLACK);
            scrollPane.getGutter().setBorder(null);
            scrollPane.getGutter().setFont(font);
            scrollPane.getVerticalScrollBar().setBackground(Color.BLACK);
            scrollPane.getGutter().setBorderColor(Color.GREEN);
            scrollPane.setBorder(null);

            fileReader.close();
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}