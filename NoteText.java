import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
public class NoteText extends JFrame {
    Dimension ScreenSize = getToolkit().getScreenSize().getSize();
    Dimension NoteSize = new Dimension(
            (int) (ScreenSize.getWidth() * 0.618),
            (int) (ScreenSize.getHeight() * 0.618)
    );
    Point NoteLocation = new Point(
            (ScreenSize.width - NoteSize.width) / 2,
            (ScreenSize.height - NoteSize.height) / 2
    );
    File file = null;
    String string = "";
    Stack<String> repeal = new Stack<>();
    Stack<String> reform = new Stack<>();
    String selectString = "";
    JFileChooser myFileChoose = new JFileChooser();
    String fileCoding = "utf-8";
    upLable upLable = new upLable();
    lowLable downLable = new lowLable();
    Font font = new Font("宋体", Font.PLAIN, 25);
    boolean CTRL = false;
    boolean IsChange = false;
    NoteText() {
        this.setSize(NoteSize);
        this.setLocation(NoteLocation);
        this.setTitle("记事本");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(new MyMenu());
        getContentPane().add(new MainText(), BorderLayout.CENTER);
        getContentPane().add(upLable, BorderLayout.NORTH);
        getContentPane().add(downLable, BorderLayout.SOUTH);
        this.repaint();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new NoteText();
    }

    class MyMenu extends JMenuBar {
        MyMenu() {
            file();
            edit();
            format();
            tools();
            setVisible(true);

        }

        private void file() {

            while (true) {
                String n = "/newfile.txt";
                int i = 1;
                try {
                    file = new File(System.getProperty("user.dir") +
                            n);
                    break;
                } catch (Exception e) {
                    n = n.split(".")[0] + "(" + i + ")" +
                            n.split(".")[1];
                    i++;
                }
            }
            JMenu jMenu = new JMenu("文件");

            JMenuItem newCreate = new JMenuItem("新建");
            newCreate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = 0;
                    if (string == "")
                        return;
                    result =
                            myFileChoose.showSaveDialog(NoteText.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        file = myFileChoose.getSelectedFile();
                        try {
                            System.out.print(string);
                            BufferedWriter bufferedWriter = new
                                    BufferedWriter(new FileWriter(file));
                            bufferedWriter.write(string);
                            bufferedWriter.close();
                            NoteText.this.upLable.jLabel1.setText("新建成功");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            });
            jMenu.add(newCreate);
            JMenuItem open = new JMenuItem("打开");
            open.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    myFileChoose.showOpenDialog(NoteText.this);
                    file = myFileChoose.getSelectedFile();
                    try {
                        FileInputStream fileInputStream = new
                                FileInputStream(file);
                        BufferedReader bufferedReader = new
                                BufferedReader(new InputStreamReader(fileInputStream, fileCoding));
                        String str = null;
                        string = "";
                        while ((str = bufferedReader.readLine()) !=
                                null)
                            string += str + '\n';
                        IsChange = true;
                        NoteText.this.upLable.jLabel1.setText("打开成功");
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (UnsupportedEncodingException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            jMenu.add(open);
            JMenuItem save = new JMenuItem("保存");
            save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = 0;
                    if (file == null) {
                        result =
                                myFileChoose.showSaveDialog(NoteText.this);
                        if (result == JFileChooser.APPROVE_OPTION)
                            file = myFileChoose.getSelectedFile();
                    }
                    if (result == JFileChooser.APPROVE_OPTION) {
                        try {
                            BufferedWriter bufferedWriter = new
                                    BufferedWriter(new FileWriter(file));
                            bufferedWriter.write(string);
                            bufferedWriter.close();
                            NoteText.this.upLable.jLabel1.setText("保存成功");
                        } catch (IOException ex) {
                        }
                    }
                }
            });
            jMenu.add(save);
            JMenuItem exit = new JMenuItem("退出");
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Runtime.getRuntime().exit(0);
                }
            });
            jMenu.add(exit);
            add(jMenu);
        }
        private void edit() {
            JMenu jMenu = new JMenu("编辑");
            JMenu jMenu1 = new JMenu("编码");
            jMenu1.setToolTipText("");
            JMenuItem utf8 = new JMenuItem("utf-8");
            utf8.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (string != null) {
                        try {
                            string = new
                                    String(string.getBytes(fileCoding), StandardCharsets.UTF_8);
                            NoteText.this.upLable.jLabel1.setText("切换为" + "utf-8");
                        } catch (UnsupportedEncodingException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    fileCoding = "utf-8";
                    NoteText.this.downLable.jLabel2.setText(fileCoding);
                }
            });
            jMenu1.add(utf8);
            JMenuItem gbk = new JMenuItem("GBK");
            gbk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (string != null) {
                        try {
                            string = new
                                    String(string.getBytes(fileCoding), "GBK");
                            NoteText.this.upLable.jLabel1.setText("切换为" + "GBK");
                        } catch (UnsupportedEncodingException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    fileCoding = "GBK";
                    NoteText.this.downLable.jLabel2.setText(fileCoding);
                }
            });
            jMenu1.add(gbk);
            JMenuItem ansi = new JMenuItem("GB2312");
            ansi.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fileCoding = "GB2312";
                    if (string != null && string != "") {
                        try {
                            string = new
                                    String(string.getBytes(fileCoding), "GB2312");
                            NoteText.this.upLable.jLabel1.setText("切换为" + "GB2312");
                        } catch (UnsupportedEncodingException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    NoteText.this.downLable.jLabel2.setText(fileCoding);
                }
            });
            jMenu1.add(ansi);
            jMenu.add(jMenu1);
            JMenuItem color = new JMenuItem("颜色");
            color.setVisible(false);
            jMenu.add(color);
            add(jMenu);
        }
        private void format() {
            JMenu jMenu = new JMenu("格式");
            TextField fontName = new TextField(font.getFontName());
            fontName.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        font = new Font(fontName.getText(),
                                font.getStyle(), font.getSize());
                        IsChange = true;
                    } catch (Exception exception) {
                    }
                }
            });
            jMenu.add(fontName);
            TextField style = new
                    TextField(String.valueOf(font.getStyle()));
            style.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        font = new Font(font.getFontName(),
                                Integer.parseInt(style.getText()), font.getSize());
                        IsChange = true;
                    } catch (Exception exception) {

                    }
                }
            });
            jMenu.add(style);
            TextField size = new
                    TextField(String.valueOf(font.getSize()));
            size.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        font = new Font(font.getFontName(),
                                font.getStyle(), Integer.parseInt(size.getText()));
                        IsChange = true;
                    } catch (Exception exception) {

                    }
                }
            });
            jMenu.add(size);
            add(jMenu);
        }

        private void tools() {
            JMenu jMenu = new JMenu("工具");
            JMenuItem cal = new JMenuItem("计算器");
            cal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AutoCalculate();
                }
            });

            jMenu.add(cal);
            add(jMenu);
        }

    }

    class Text extends JTextArea {
        Text() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setSize(NoteText.this.getSize());
            _addMouseListener();
            _addMouseWheelListener();
            _addKeyListener();
            init();
        }

        private void init() {
            try {
                int lineOfC = getLineOfOffset(getCaretPosition()) + 1;
                int col = getCaretPosition() -
                        getLineStartOffset(lineOfC - 1);
                NoteText.this.downLable.jLabel.setText(lineOfC + "行"
                        + "  " + col + "列");

                NoteText.this.downLable.jLabel1.setText(NoteText.this.file.getName());
                NoteText.this.downLable.jLabel2.setText(fileCoding);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
            Function<Integer, String> function = style -> {
                switch (style) {
                    case (0):
                        return "普通";
                    case (1):
                        return "粗体";
                    case (2):
                        return "斜体";
                    case (3):
                        return "粗斜体";
                }
                return "";
            };
            repeal.push("");
            NoteText.this.upLable.jLabel.setText(font.getFontName() +
                    " " + function.apply(font.getStyle()) + " " + font.getSize());
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (IsChange) {
                        setText(string);
                        setFont(font);
                        NoteText.this.upLable.jLabel.setText(font.getFontName() + " " +
                                function.apply(font.getStyle()) + " " + font.getSize());
                        IsChange = false;
                    }
                    setFont(font);
                    NoteText.this.upLable.jLabel3.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd::hh:mm:ss")));
                    NoteText.this.downLable.jLabel1.setText(NoteText.this.file.getName());
                    NoteText.this.downLable.jLabel2.setText(fileCoding);
                }
            }, 0, 50);
            Timer timer1 = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    NoteText.this.upLable.jLabel1.setText("");
                }
            }, 0, 5000);
        }
        private void _addMouseWheelListener() {
            addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (CTRL) {
                        if (font.getSize() < 100 &&
                                e.getWheelRotation() == -1)
                            font = new Font(font.getFontName(),
                                    font.getStyle(), font.getSize() + 10);
                        else if (font.getSize() > 10 &&
                                e.getWheelRotation() == 1)
                            font = new Font(font.getFontName(),
                                    font.getStyle(), font.getSize() - 10);
                        setFont(font);
                        IsChange = true;
                    }
                }
            });
        }
        private void _addKeyListener() {
            addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    NoteText.this.string = getText();
                }
                @Override
                public void keyPressed(KeyEvent e) {
                    NoteText.this.string = getText();
                    try {
                        int lineOfC =
                                getLineOfOffset(getCaretPosition()) + 1;
                        int col = getCaretPosition() -
                                getLineStartOffset(lineOfC - 1);
                        NoteText.this.downLable.jLabel.setText(lineOfC + "行" + "  " + col +
                                "列");
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (e.isControlDown())
                        NoteText.this.CTRL = true;
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    NoteText.this.string = getText();
                    try {
                        int lineOfC =
                                getLineOfOffset(getCaretPosition()) + 1;
                        int col = getCaretPosition() -
                                getLineStartOffset(lineOfC - 1);
                        NoteText.this.downLable.jLabel.setText(lineOfC + "行" + "  " + col +
                                "列");
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (CTRL && e.getKeyCode() == KeyEvent.VK_Z) {
                        if (!repeal.empty()) {
                            string = repeal.pop();
                            reform.push(string);
                            System.out.println("撤回内容" + string);
                            IsChange = true;
                        }
                    }
                    if (CTRL && e.getKeyCode() == KeyEvent.VK_Y) {
                        if (!reform.empty()) {
                            string = reform.pop();
                            repeal.push(string);
                            IsChange = true;
                        }
                    }
                    if (!e.isControlDown())
                        NoteText.this.CTRL = false;
                    Pattern pattern = Pattern.compile("[\\u4e00\\u9fa5，。、？、！、，、；、：、‘、’、“、”（）—…－、／\\\\ 、\\s\\r]");
                    if
                    (pattern.matcher(String.valueOf(e.getKeyChar())).matches())
                        repeal.push(string);
                }
            });
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (CTRL && e.getKeyCode() == KeyEvent.VK_S) {
                        try {
                            BufferedWriter bufferedWriter = new
                                    BufferedWriter(new FileWriter(file));
                            bufferedWriter.write(string);
                            bufferedWriter.close();
                            NoteText.this.upLable.jLabel1.setText("保存成功");
                        } catch (IOException ex) {
                        }

                    }
                }
            });
        }

        private void _addMouseListener() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    selectString = getSelectedText();
                    Pattern pattern = Pattern.compile("^[\\d\\+\\\\*\\/\\(\\)!^. ]+$");
                    if (selectString != null &&
                            pattern.matcher(selectString).matches())

                        NoteText.this.upLable.jLabel1.setText(String.valueOf(new
                                Operator(selectString)));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    string = getText();
                }

            });
        }
    }

    class MainText extends JScrollPane {
        MainText() {
            super(new Text());
        }

    }

    class upLable extends JPanel {
        JLabel jLabel = new JLabel();
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel();
        JLabel jLabel3 = new JLabel();

        upLable() {
            setLayout(new GridLayout(1, 4, 0, 0));
            add(jLabel);
            add(jLabel1);
            add(jLabel2);
            add(jLabel3);
        }
    }

    class lowLable extends JPanel {
        JLabel jLabel = new JLabel();
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel();
        JLabel jLabel3 = new JLabel();

        lowLable() {
            setLayout(new GridLayout(1, 4, 0, 0));
            add(jLabel);
            add(jLabel1);
            add(jLabel2);
            add(jLabel3);
        }
    }

    class AutoCalculate extends JDialog {
        ShowAnswer showAnswer = new ShowAnswer();

        AutoCalculate() {
            setTitle("自动计算器");
            setSize(400, 400);
            setLocation(500, 250);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(2, 1));
            add(new Text());
            add(showAnswer);
            setVisible(true);
        }


        static class ShowAnswer extends JTextArea {
            ShowAnswer() {
                setText("自动出答案");
                setLineWrap(true);
                setWrapStyleWord(true);
            }
        }
        class Text extends ShowAnswer {
            Text() {
                final boolean[] single = {true};
                addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (single[0]) {
                            single[0] = false;
                            setText(String.valueOf(e.getKeyChar()));
                        }
                        String selectString = Text.this.getText();
                        if (selectString != null) {
                            Pattern pattern =
                                    Pattern.compile("^[\\d+\\-*/()!^%. ]+$");
                            StringBuilder s = new StringBuilder();
                            for (int i = 0; i < selectString.length();
                                 i++) {
                                if (selectString.charAt(i) != ' ')
                                    s.append(selectString.charAt(i));
                            }
                            if
                            (pattern.matcher(selectString).matches()) {
                                String string = new
                                        Operator(s.toString()).toString();
                                showAnswer.setText(string == null ?
                                        s.toString() : string);
                            } else
                                showAnswer.setText("");
                        }
                    }
                });
            }
        }
    }
    class Operator {
        String expression;
        Operator(String string) {
            expression = string;
        }
        public static BigDecimal calPost(LinkedList<String> post) {
            Stack<String> stack = new Stack<>();
            for (String str : post) {
                if ("+-*/^%".contains(str) && !stack.empty()) {
                    BigDecimal num2 = new BigDecimal(stack.pop());
                    BigDecimal num1 = new BigDecimal(stack.pop());
                    stack.push(Objects.requireNonNull(calculateBig(num1, num2,
                            str.charAt(0))).toString());
                } else {
                    if (str.charAt(str.length() - 1) == '!') {
                        Function<String, String> f = s -> {
                            if (s.indexOf('.') != -1)
                                s = s.split("\\.")[0];
                            BigInteger num = new BigInteger(s);
                            BigInteger nums = BigInteger.ONE;
                            for (BigInteger i = BigInteger.ONE;
                                 i.compareTo(num) <= 0; i = i.add(BigInteger.ONE)) {
                                nums = nums.multiply(i);
                            }
                            return nums.toString();
                        };
                        stack.push(f.apply(str.split("!")[0]));
                    } else
                        stack.push(str);
                }
            }
            return new BigDecimal(stack.pop());
        }
        public static LinkedList<String> transform(String str) {
            LinkedList<String> postfix = new LinkedList<>();
            Stack<String> symbol = new Stack<>();
            StringBuilder num = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                if ("0123456789.!".indexOf(str.charAt(i)) != -1)
                    num.append(str.charAt(i));
                else {
                    if (!num.toString().isEmpty()) {
                        postfix.addLast(num.toString());
                        num = new StringBuilder();
                    }
                    if (str.charAt(i) == '-' && (i == 0 || "+*/%^(".indexOf(str.charAt(i - 1)) != -1)) {
                    num.insert(0, "-");
                } else if (symbol.empty())
                symbol.push(String.valueOf(str.charAt(i)));
                    else if (str.charAt(i) == ')') {
                    while (!symbol.empty()) {
                        String ch2 = symbol.pop();
                        if (ch2.charAt(0) == '(')
                            break;
                        postfix.addLast(ch2);
                    }
                } else {
                    while (!symbol.empty()) {
                        String ch2 = symbol.pop();
                        if (comparePro(str.charAt(i),
                                ch2.charAt(0)) > 0) {
                            symbol.push(ch2);

                            symbol.push(String.valueOf(str.charAt(i)));
                            break;
                        } else
                            postfix.addLast(ch2);
                    }
                    if (symbol.empty())

                        symbol.push(String.valueOf(str.charAt(i)));
                }
            }
        }
            if (!num.toString().isEmpty())
                postfix.addLast(num.toString());
            while (!symbol.empty())
                    postfix.addLast(symbol.pop());
            return postfix;
    }

    public static int comparePro(char ch1, char ch2) {
        if (ch1 == '(' || ch2 == '(')
            return 1;
        else {
            int c1 = 0, c2 = 0;
            LinkedList<String> Symbol = new LinkedList<>();
            Symbol.addLast("+-");
            Symbol.addLast("*/%");
            Symbol.addLast("^");
            for (int i = 0; i < Symbol.size(); i++) {
                if (Symbol.get(i).indexOf(ch1) != -1)
                            c1 = i;
                if (Symbol.get(i).indexOf(ch2) != -1)
                    c2 = i;
            }
            return c1 - c2;
        }
    }
    public static BigDecimal calculateBig(BigDecimal bigDecimal1,
                                          BigDecimal bigDecimal2, char op) {
        return switch (op) {
            case ('+') -> bigDecimal1.add(bigDecimal2);
            case ('-') -> bigDecimal1.subtract(bigDecimal2);
            case ('*') -> bigDecimal1.multiply(bigDecimal2);
            case ('/') -> bigDecimal1.divide(bigDecimal2);
            case ('%') -> bigDecimal1.remainder(bigDecimal2);
            case ('^') ->
                    bigDecimal1.pow(Integer.parseInt(String.valueOf(bigDecimal2)));
            default -> null;
        };
    }
    @Override
    public String toString() {
        try {
            return calPost(transform(expression)).toString();
        } catch (Exception exception) {
            return null;
        }
    }
}
}