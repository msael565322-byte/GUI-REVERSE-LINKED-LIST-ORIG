package sael;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}

class LinkedList {
    Node head, tail;

    void add(int data) {
        Node n = new Node(data);

        if (head == null)
            head = n;
        else
            tail.next = n;

        tail = n;
    }

    void reverse() {
        Node prev = null;
        Node cur = head;

        while (cur != null) {
            Node next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }

        tail = head;
        head = prev;
    }

    // values of the list, used to draw the boxes
    int[] toArray() {
        int count = 0;

        for (Node n = head; n != null; n = n.next)
            count++;

        int[] arr = new int[count];

        int i = 0;

        for (Node n = head; n != null; n = n.next)
            arr[i++] = n.data;

        return arr;
    }

    String show() {
        StringBuilder s = new StringBuilder();

        for (Node n = head; n != null; n = n.next)
            s.append(n.data).append(" -> ");

        return s.append("NULL").toString();
    }

    void clear() {
        head = null;
        tail = null;
    }
}


// diagram panel(draws boxes + arrows + NULL + tags)
class ListDiagram extends JPanel {

    int[] values = new int[0];

    int currentIdx = -1;

    static final int X0 = 30;
    static final int BOX_Y = 60;
    static final int BOX_W = 80;
    static final int BOX_H = 50;
    static final int GAP = 60;

    static final Color BOX_FILL =
            new Color(230, 220, 245);

    static final Color BOX_LINE =
            new Color(120, 80, 170);

    static final Color CURRENT_COLOR =
            new Color(40, 100, 210);

    static final Color NEXT_COLOR =
            new Color(230, 120, 20);

    ListDiagram() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 130));
    }

    // no tags
    void setValues(int[] values) {
        setValues(values, -1);
    }

    // with current/next tags on currentIdx
    void setValues(int[] values, int currentIdx) {

        this.values = values;
        this.currentIdx = currentIdx;

        int width =
                X0 + values.length * (BOX_W + GAP) + 100;

        setPreferredSize(
                new Dimension(Math.max(width, 400), 130));

        revalidate();
        repaint();

        // keep the current tag visible when the list gets long
        if (currentIdx >= 0) {

            int x =
                    X0 + currentIdx * (BOX_W + GAP);

            SwingUtilities.invokeLater(() ->
                    scrollRectToVisible(
                            new Rectangle(
                                    x - 20,
                                    0,
                                    BOX_W + GAP + 150,
                                    getHeight())));
        }
    }

    @Override
    protected void paintComponent(Graphics g0) {

        super.paintComponent(g0);

        Graphics2D g = (Graphics2D) g0;

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int n = values.length;

        if (n == 0) {

            g.setColor(Color.GRAY);

            g.setFont(
                    new Font(
                            "Arial",
                            Font.ITALIC,
                            16));

            g.drawString(
                    "Empty",
                    X0,
                    BOX_Y + 30);

            return;
        }

        g.setStroke(new BasicStroke(2f));

        // boxes and arrows
        for (int i = 0; i < n; i++) {

            int x =
                    X0 + i * (BOX_W + GAP);

            g.setColor(BOX_FILL);

            g.fillRect(
                    x,
                    BOX_Y,
                    BOX_W,
                    BOX_H);

            g.setColor(BOX_LINE);

            g.drawRect(
                    x,
                    BOX_Y,
                    BOX_W,
                    BOX_H);

            // data text
            g.setColor(Color.BLACK);

            g.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            18));

            FontMetrics fm =
                    g.getFontMetrics();

            String text =
                    String.valueOf(values[i]);

            g.drawString(
                    text,
                    x + (BOX_W -
                            fm.stringWidth(text)) / 2,
                    BOX_Y +
                            (BOX_H +
                                    fm.getAscent()) / 2 -
                            3);

            // arrow to next box
            drawArrow(
                    g,
                    x + BOX_W,
                    BOX_Y + BOX_H / 2,
                    x + BOX_W + GAP,
                    BOX_Y + BOX_H / 2);
        }

        // NULL
        int nullX =
                X0 +
                n * (BOX_W + GAP) +
                8;

        g.setColor(Color.BLACK);

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16));

        FontMetrics fm =
                g.getFontMetrics();

        g.drawString(
                "NULL",
                nullX,
                BOX_Y +
                        BOX_H / 2 +
                        fm.getAscent() / 2 -
                        2);

        // current/next tags
        if (currentIdx >= 0 &&
                currentIdx < n) {

            int currentX =
                    X0 +
                    currentIdx *
                            (BOX_W + GAP) +
                    BOX_W / 2;

            int nextIdx =
                    currentIdx + 1;

            int nextX;

            if (nextIdx < n)

                nextX =
                        X0 +
                        nextIdx *
                                (BOX_W + GAP) +
                        BOX_W / 2;

            else

                nextX =
                        nullX +
                        fm.stringWidth("NULL") / 2;

            drawTag(
                    g,
                    "current",
                    currentX,
                    CURRENT_COLOR);

            drawTag(
                    g,
                    "next",
                    nextX,
                    NEXT_COLOR);
        }
    }

    // label above a node
    void drawTag(
            Graphics2D g,
            String text,
            int centerX,
            Color color) {

        g.setColor(color);

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14));

        FontMetrics fm =
                g.getFontMetrics();

        g.drawString(
                text,
                centerX -
                        fm.stringWidth(text) / 2,
                22);

        drawArrow(
                g,
                centerX,
                28,
                centerX,
                BOX_Y - 2);
    }

    // line with a filled arrowhead
    void drawArrow(
            Graphics2D g,
            int x1,
            int y1,
            int x2,
            int y2) {

        double angle =
                Math.atan2(
                        y2 - y1,
                        x2 - x1);

        int head = 10;

        int bx =
                (int)
                        (x2 -
                                head *
                                        Math.cos(angle));

        int by =
                (int)
                        (y2 -
                                head *
                                        Math.sin(angle));

        g.drawLine(
                x1,
                y1,
                bx,
                by);

        Polygon p =
                new Polygon();

        p.addPoint(
                x2,
                y2);

        p.addPoint(
                (int)
                        (x2 -
                                head *
                                        Math.cos(
                                                angle -
                                                        Math.PI / 6)),
                (int)
                        (y2 -
                                head *
                                        Math.sin(
                                                angle -
                                                        Math.PI / 6)));

        p.addPoint(
                (int)
                        (x2 -
                                head *
                                        Math.cos(
                                                angle +
                                                        Math.PI / 6)),
                (int)
                        (y2 -
                                head *
                                        Math.sin(
                                                angle +
                                                        Math.PI / 6)));

        g.fillPolygon(p);
    }
}


public class sael extends JFrame {

    LinkedList list = new LinkedList();

    JTextField nodeNumber =
            new JTextField(5);

    JTextField input =
            new JTextField(12);

    ListDiagram original =
            new ListDiagram();

    ListDiagram reversed =
            new ListDiagram();

    JLabel count =
            new JLabel(
                    "Nodes: 0 / 0",
                    SwingConstants.CENTER);

    JLabel executionTime =
            new JLabel(
                    "Execution time: 0 nanoseconds",
                    SwingConstants.RIGHT);

    JButton setNodes =
            new JButton("Set Nodes");

    JButton add =
            new JButton("Add Node");

    JButton reverse =
            new JButton("Reverse");

    JButton clear =
            new JButton("Clear");

    JButton load =
            new JButton("Load File");

    int max = 0;
    int current = 0;


    public sael() {

        setTitle("Reverse Linked List");

        setSize(900, 650);

        setDefaultCloseOperation(
                EXIT_ON_CLOSE);

        setLocationRelativeTo(null);


        // title
        JLabel title =
                new JLabel(
                        "SINGLY LINKED LIST",
                        SwingConstants.CENTER);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28));


        // number of nodes
        JPanel nodePanel =
                new JPanel();

        nodePanel.add(
                new JLabel(
                        "Number of Nodes:"));

        nodePanel.add(nodeNumber);

        nodePanel.add(setNodes);


        // input
        JLabel inputLabel =
                new JLabel(
                        "Enter a number:",
                        SwingConstants.CENTER);

        inputLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16));

        input.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        18));

        JPanel inputPanel =
                new JPanel(
                        new GridLayout(
                                3,
                                1,
                                5,
                                5));

        inputPanel.add(inputLabel);
        inputPanel.add(input);
        inputPanel.add(count);


        // original list diagram
        JLabel originalTitle =
                new JLabel(
                        "Original Linked List:");

        originalTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14));

        JPanel originalPanel =
                new JPanel(
                        new BorderLayout());

        originalPanel.add(
                originalTitle,
                BorderLayout.NORTH);

        originalPanel.add(
                makeScroll(original),
                BorderLayout.CENTER);


        // reverse list diagram
        JLabel reversedTitle =
                new JLabel(
                        "Reversed Linked List:");

        reversedTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14));

        JPanel reversedPanel =
                new JPanel(
                        new BorderLayout());

        reversedPanel.add(
                reversedTitle,
                BorderLayout.NORTH);

        reversedPanel.add(
                makeScroll(reversed),
                BorderLayout.CENTER);


        // boxes list
        JPanel lists =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                10,
                                10));

        lists.add(originalPanel);
        lists.add(reversedPanel);


        // button colors
        add.setBackground(
                new Color(60, 120, 200));

        add.setForeground(Color.WHITE);

        reverse.setBackground(
                new Color(50, 150, 80));

        reverse.setForeground(Color.WHITE);

        clear.setBackground(
                new Color(200, 70, 70));

        clear.setForeground(Color.WHITE);

        load.setBackground(
                new Color(80, 140, 190));

        load.setForeground(Color.WHITE);


        // button panel
        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(add);
        buttonPanel.add(reverse);
        buttonPanel.add(clear);
        buttonPanel.add(load);


        JPanel buttons =
                new JPanel(
                        new BorderLayout());

        buttons.add(
                buttonPanel,
                BorderLayout.CENTER);

        executionTime.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        5,
                        0,
                        5));

        buttons.add(
                executionTime,
                BorderLayout.EAST);


        // panel
        JPanel main =
                new JPanel(
                        new BorderLayout(
                                10,
                                10));

        main.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15));

        main.add(
                title,
                BorderLayout.NORTH);


        JPanel center =
                new JPanel(
                        new BorderLayout(
                                10,
                                10));

        center.add(
                nodePanel,
                BorderLayout.NORTH);


        JPanel middle =
                new JPanel(
                        new BorderLayout(
                                5,
                                5));

        middle.add(
                inputPanel,
                BorderLayout.NORTH);

        middle.add(
                lists,
                BorderLayout.CENTER);

        center.add(
                middle,
                BorderLayout.CENTER);

        main.add(
                center,
                BorderLayout.CENTER);


        // bottom buttons
        main.add(
                buttons,
                BorderLayout.SOUTH);

        add(main);


        // initial state
        add.setEnabled(false);

        reverse.setEnabled(false);

        input.setEnabled(false);


        // buttons
        setNodes.addActionListener(
                e -> setNodeLimit());

        add.addActionListener(
                e -> addNode());

        reverse.addActionListener(
                e -> reverseList());

        clear.addActionListener(
                e -> clearList());

        load.addActionListener(
                e -> loadFromFile());

        input.addActionListener(
                e -> addNode());
    }


    // scroll pane
    JScrollPane makeScroll(
            ListDiagram d) {

        return new JScrollPane(
                d,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }


    void setNodeLimit() {

        try {

            max =
                    Integer.parseInt(
                            nodeNumber
                                    .getText()
                                    .trim());

            if (max <= 0)
                throw new Exception();

            list.clear();

            current = 0;

            original.setValues(
                    new int[0]);

            reversed.setValues(
                    new int[0]);

            executionTime.setText(
                    "Execution time: 0 nanoseconds");

            count.setText(
                    "Nodes: 0 / " + max);

            autoSaveToFile();

            nodeNumber.setEnabled(false);

            setNodes.setEnabled(false);

            input.setEnabled(true);

            add.setEnabled(true);

            reverse.setEnabled(false);

            input.requestFocus();

            JOptionPane.showMessageDialog(
                    this,
                    "Number of Nodes Set",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid number of nodes.",
                    "Message",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    void addNode() {

        if (current >= max)
            return;

        try {

            int value =
                    Integer.parseInt(
                            input
                                    .getText()
                                    .trim());

            list.add(value);

            current++;

            original.setValues(
                    list.toArray(),
                    current - 1);

            reversed.setValues(
                    new int[0]);

            count.setText(
                    "Nodes: "
                            + current
                            + " / "
                            + max);

            autoSaveToFile();

            input.setText("");


            if (current == max) {

                add.setEnabled(false);

                reverse.setEnabled(true);

                input.setEnabled(false);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid integer.",
                    "Message",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    void reverseList() {

        long startTime =
                System.nanoTime();

        list.reverse();

        long endTime =
                System.nanoTime();

        long elapsedTime =
                endTime - startTime;

        reversed.setValues(
                list.toArray());

        original.setValues(
                original.values);

        executionTime.setText(
                "Execution time: "
                        + elapsedTime
                        + " nanoseconds");


        JOptionPane.showMessageDialog(
                this,
                "Linked List Reversed Successfully!",
                "Message",
                JOptionPane.INFORMATION_MESSAGE);


        autoSaveToFile();

        reverse.setEnabled(false);
    }


    // load text file
    void loadFromFile() {

        JFileChooser fileChooser =
                new JFileChooser();

        fileChooser.setDialogTitle(
                "Select Linked List Text File");

        int result =
                fileChooser.showOpenDialog(this);

        if (result !=
                JFileChooser.APPROVE_OPTION)
            return;

        java.io.File selectedFile =
                fileChooser.getSelectedFile();

        try (java.io.BufferedReader reader =
                     new java.io.BufferedReader(
                             new java.io.FileReader(
                                     selectedFile))) {

            StringBuilder content =
                    new StringBuilder();

            String line;

            while ((line =
                    reader.readLine()) != null) {

                content.append(line)
                        .append("\n");
            }

            JTextArea textArea =
                    new JTextArea(
                            content.toString());

            textArea.setEditable(false);

            textArea.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            15));

            textArea.setLineWrap(true);

            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane =
                    new JScrollPane(textArea);

            scrollPane.setPreferredSize(
                    new Dimension(
                            600,
                            350));

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Loaded File: "
                            + selectedFile.getName(),
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading file: "
                            + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    // save to text file
    // This now replaces the old file contents
    // and does NOT write "=== REVERSE LINKED LIST ==="
    void autoSaveToFile() {

        String fileName =
                "linkedlist_data.txt";

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(
                                     fileName,
                                     false))) {

            writer.write(
                    "Number of Nodes: "
                            + current);

            writer.newLine();
            writer.newLine();


            writer.write(
                    "Original Linked List:");

            writer.newLine();

            writer.write(
                    original.values.length == 0
                            ? "NULL"
                            : arrayToString(
                                    original.values));

            writer.newLine();
            writer.newLine();


            writer.write(
                    "Reversed Linked List:");

            writer.newLine();

            writer.write(
                    reversed.values.length == 0
                            ? "Not reversed yet"
                            : arrayToString(
                                    reversed.values));

            writer.newLine();
            writer.newLine();


            // Only write execution time
            // if list has been reversed
            if (reversed.values.length > 0) {

                writer.write(
                        executionTime.getText());

                writer.newLine();
                writer.newLine();
            }

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error automatically saving file: "
                            + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    // Helper method
    String arrayToString(int[] arr) {

        StringBuilder sb =
                new StringBuilder();

        for (int val : arr) {

            sb.append(val)
                    .append(" -> ");
        }

        return sb.append("NULL")
                .toString();
    }


    // clear
    void clearList() {

        list.clear();

        max = 0;

        current = 0;

        nodeNumber.setText("");

        input.setText("");

        original.setValues(
                new int[0]);

        reversed.setValues(
                new int[0]);

        count.setText(
                "Nodes: 0 / 0");

        executionTime.setText(
                "Execution time: 0 nanoseconds");

        autoSaveToFile();

        nodeNumber.setEnabled(true);

        setNodes.setEnabled(true);

        input.setEnabled(false);

        add.setEnabled(false);

        reverse.setEnabled(false);
    }


    // main
    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new sael()
                        .setVisible(true));
    }
}
