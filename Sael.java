package sael;

import javax.swing.*;
import java.awt.*;
import java.io.*;

// node for the linked list
class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
    }
}

// handles the linked list operations
class LinkedList {
    Node head, tail;

    // add a new node
    void add(int data) {
        Node n = new Node(data);

        if (head == null)
            head = n;
        else
            tail.next = n;

        tail = n;
    }

    // reverse the linked list
    void reverse() {
        Node prev = null, current = head;

        while (current != null) {
            // save the next node
            Node next = current.next;

            // change the pointer direction
            current.next = prev;

            // move the pointers
            prev = current;
            current = next;
        }

        // update head and tail
        tail = head;
        head = prev;
    }

    // convert the list into an array
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

    // show the list with NULL
    String show() {
        StringBuilder s = new StringBuilder();

        for (Node n = head; n != null; n = n.next)
            s.append(n.data).append(" -> ");

        return s.append("NULL").toString();
    }

    // clear the list
    void clear() {
        head = tail = null;
    }
}

// draws the linked list diagram
class ListDiagram extends JPanel {
    int[] values = new int[0];
    int currentIdx = -1, prevIdx = -1;

    // diagram sizes
    static final int X0 = 30, BOX_Y = 60;
    static final int BOX_W = 80, BOX_H = 50, GAP = 60;

    // colors used for the diagram
    static final Color BOX_FILL = new Color(230, 220, 245);
    static final Color BOX_LINE = new Color(120, 80, 170);
    static final Color CURRENT_COLOR = new Color(40, 100, 210);
    static final Color NEXT_COLOR = new Color(230, 120, 20);
    static final Color PREV_COLOR = new Color(170, 50, 150);

    ListDiagram() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 150));
    }

    // display the list
    void setValues(int[] values) {
        setValues(values, -1, -1);
    }

    // display the current node
    void setValues(int[] values, int currentIdx) {
        setValues(values, currentIdx, currentIdx - 1);
    }

    // display the current and previous nodes
    void setValues(int[] values, int currentIdx, int prevIdx) {
        this.values = values;
        this.currentIdx = currentIdx;
        this.prevIdx = prevIdx;

        int width = X0 + values.length * (BOX_W + GAP) + 150;

        setPreferredSize(new Dimension(Math.max(width, 400), 150));
        revalidate();
        repaint();

        // automatically scroll to the current node
        if (currentIdx >= 0) {
            int x = X0 + currentIdx * (BOX_W + GAP);

            SwingUtilities.invokeLater(() ->
                scrollRectToVisible(
                    new Rectangle(x - 80, 0,
                        BOX_W + GAP + 200, getHeight())
                )
            );
        }
    }

    // draw the linked list
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);

        Graphics2D g = (Graphics2D) g0;

        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        int n = values.length;

        // show empty if there are no nodes
        if (n == 0) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Arial", Font.ITALIC, 16));
            g.drawString("Empty", X0, BOX_Y + 30);
            return;
        }

        g.setStroke(new BasicStroke(2f));

        // draw each node
        for (int i = 0; i < n; i++) {
            int x = X0 + i * (BOX_W + GAP);

            g.setColor(BOX_FILL);
            g.fillRect(x, BOX_Y, BOX_W, BOX_H);

            g.setColor(BOX_LINE);
            g.drawRect(x, BOX_Y, BOX_W, BOX_H);

            // show the node value
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 18));

            FontMetrics fm = g.getFontMetrics();
            String text = String.valueOf(values[i]);

            g.drawString(
                text,
                x + (BOX_W - fm.stringWidth(text)) / 2,
                BOX_Y + (BOX_H + fm.getAscent()) / 2 - 3
            );

            // draw arrow to the next node
            drawArrow(
                g,
                x + BOX_W,
                BOX_Y + BOX_H / 2,
                x + BOX_W + GAP,
                BOX_Y + BOX_H / 2
            );
        }

        // show NULL at the end
        int nullX = X0 + n * (BOX_W + GAP) + 8;

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        FontMetrics fm = g.getFontMetrics();

        g.drawString(
            "NULL",
            nullX,
            BOX_Y + BOX_H / 2 + fm.getAscent() / 2 - 2
        );

        // show previous pointer
        if (prevIdx >= 0 && prevIdx < n) {
            int prevX =
                X0 + prevIdx * (BOX_W + GAP) + BOX_W / 2;

            drawTag(g, "prev", prevX, PREV_COLOR);
        }

        // show current and next pointers
        if (currentIdx >= 0 && currentIdx < n) {
            int currentX =
                X0 + currentIdx * (BOX_W + GAP) + BOX_W / 2;

            drawTag(g, "current", currentX, CURRENT_COLOR);

            int nextIdx = currentIdx + 1;

            int nextX;

            if (nextIdx < n) {
                nextX =
                    X0 + nextIdx * (BOX_W + GAP) + BOX_W / 2;
            } else {
                nextX =
                    nullX + fm.stringWidth("NULL") / 2;
            }

            drawTag(g, "next", nextX, NEXT_COLOR);
        }
    }

    // draw pointer labels
    void drawTag(Graphics2D g, String text,
                 int centerX, Color color) {

        g.setColor(color);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        FontMetrics fm = g.getFontMetrics();

        g.drawString(
            text,
            centerX - fm.stringWidth(text) / 2,
            22
        );

        drawArrow(
            g,
            centerX,
            28,
            centerX,
            BOX_Y - 2
        );
    }

    // draw an arrow
    void drawArrow(Graphics2D g,
                   int x1, int y1,
                   int x2, int y2) {

        double angle = Math.atan2(y2 - y1, x2 - x1);
        int head = 10;

        int bx = (int) (x2 - head * Math.cos(angle));
        int by = (int) (y2 - head * Math.sin(angle));

        g.drawLine(x1, y1, bx, by);

        Polygon p = new Polygon();

        p.addPoint(x2, y2);

        p.addPoint(
            (int) (x2 - head * Math.cos(angle - Math.PI / 6)),
            (int) (y2 - head * Math.sin(angle - Math.PI / 6))
        );

        p.addPoint(
            (int) (x2 - head * Math.cos(angle + Math.PI / 6)),
            (int) (y2 - head * Math.sin(angle + Math.PI / 6))
        );

        g.fillPolygon(p);
    }
}

// main GUI
public class Sael extends JFrame {

    LinkedList list = new LinkedList();

    JTextField nodeNumber = new JTextField(5);
    JTextField input = new JTextField(12);

    ListDiagram original = new ListDiagram();
    ListDiagram reversed = new ListDiagram();

    JLabel count =
        new JLabel("Nodes: 0 / 0", SwingConstants.CENTER);

    JLabel executionTime =
        new JLabel(
            "Execution time: 0 nanoseconds",
            SwingConstants.RIGHT
        );

    JButton setNodes = new JButton("Set Nodes");
    JButton add = new JButton("Add Node");
    JButton reverse = new JButton("Reverse");
    JButton clear = new JButton("Clear");
    JButton load = new JButton("Load File");

    int max = 0, current = 0;

    public Sael() {
        setTitle("Reverse Linked List");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // title
        JLabel title =
            new JLabel("SINGLY LINKED LIST",
                       SwingConstants.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 28));

        // number of nodes input
        JPanel nodePanel = new JPanel();

        nodePanel.add(new JLabel("Number of Nodes:"));
        nodePanel.add(nodeNumber);
        nodePanel.add(setNodes);

        // node value input
        JLabel inputLabel =
            new JLabel("Enter a number:",
                       SwingConstants.CENTER);

        inputLabel.setFont(
            new Font("Arial", Font.BOLD, 16));

        input.setFont(
            new Font("Arial", Font.PLAIN, 18));

        JPanel inputPanel =
            new JPanel(new GridLayout(3, 1, 5, 5));

        inputPanel.add(inputLabel);
        inputPanel.add(input);
        inputPanel.add(count);

        // original list panel
        JPanel originalPanel =
            new JPanel(new BorderLayout());

        JLabel originalTitle =
            new JLabel("Original Linked List:");

        originalTitle.setFont(
            new Font("Arial", Font.BOLD, 14));

        originalPanel.add(
            originalTitle, BorderLayout.NORTH);

        originalPanel.add(
            makeScroll(original),
            BorderLayout.CENTER);

        // reversed list panel
        JPanel reversedPanel =
            new JPanel(new BorderLayout());

        JLabel reversedTitle =
            new JLabel("Reversed Linked List:");

        reversedTitle.setFont(
            new Font("Arial", Font.BOLD, 14));

        reversedPanel.add(
            reversedTitle, BorderLayout.NORTH);

        reversedPanel.add(
            makeScroll(reversed),
            BorderLayout.CENTER);

        // put both lists together
        JPanel lists =
            new JPanel(new GridLayout(2, 1, 10, 10));

        lists.add(originalPanel);
        lists.add(reversedPanel);

        // button colors
        add.setBackground(new Color(60, 120, 200));
        add.setForeground(Color.WHITE);

        reverse.setBackground(new Color(50, 150, 80));
        reverse.setForeground(Color.WHITE);

        clear.setBackground(new Color(200, 70, 70));
        clear.setForeground(Color.WHITE);

        load.setBackground(new Color(80, 140, 190));
        load.setForeground(Color.WHITE);

        // buttons
        JPanel buttonPanel = new JPanel();

        buttonPanel.add(add);
        buttonPanel.add(reverse);
        buttonPanel.add(clear);
        buttonPanel.add(load);

        JPanel buttons =
            new JPanel(new BorderLayout());

        buttons.add(
            buttonPanel,
            BorderLayout.CENTER);

        executionTime.setBorder(
            BorderFactory.createEmptyBorder(
                0, 5, 0, 5));

        buttons.add(
            executionTime,
            BorderLayout.EAST);

        // main panel
        JPanel main =
            new JPanel(new BorderLayout(10, 10));

        main.setBorder(
            BorderFactory.createEmptyBorder(
                15, 15, 15, 15));

        main.add(title, BorderLayout.NORTH);

        JPanel center =
            new JPanel(new BorderLayout(10, 10));

        center.add(
            nodePanel,
            BorderLayout.NORTH);

        JPanel middle =
            new JPanel(new BorderLayout(5, 5));

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

        main.add(
            buttons,
            BorderLayout.SOUTH);

        add(main);

        // buttons are disabled at the start
        add.setEnabled(false);
        reverse.setEnabled(false);
        input.setEnabled(false);

        // button actions
        setNodes.addActionListener(e -> setNodeLimit());
        add.addActionListener(e -> addNode());
        reverse.addActionListener(e -> reverseList());
        clear.addActionListener(e -> clearList());
        load.addActionListener(e -> loadFromFile());
        input.addActionListener(e -> addNode());
    }

    // create the scroll area for the diagram
    JScrollPane makeScroll(ListDiagram d) {
        return new JScrollPane(
            d,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
    }

    // set how many nodes can be entered
    void setNodeLimit() {
        try {
            max = Integer.parseInt(
                nodeNumber.getText().trim());

            if (max <= 0)
                throw new Exception();

            list.clear();
            current = 0;

            original.setValues(new int[0]);
            reversed.setValues(new int[0]);

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
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Enter a valid number of nodes.",
                "Message",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // add a node to the list
    void addNode() {
        if (current >= max)
            return;

        try {
            int value =
                Integer.parseInt(
                    input.getText().trim());

            list.add(value);
            current++;

            // update the original diagram
            original.setValues(
                list.toArray(),
                current - 1,
                current - 2
            );

            // clear the reversed diagram
            reversed.setValues(new int[0]);

            count.setText(
                "Nodes: " + current + " / " + max);

            autoSaveToFile();
            input.setText("");

            // enable reverse when all nodes are added
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
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // reverse the list and get the running time
    void reverseList() {
        long startTime = System.nanoTime();

        list.reverse();

        long elapsedTime =
            System.nanoTime() - startTime;

        // show the reversed list
        reversed.setValues(list.toArray());

        // keep the original display
        original.setValues(original.values);

        executionTime.setText(
            "Execution time: " +
            elapsedTime +
            " nanoseconds"
        );

        JOptionPane.showMessageDialog(
            this,
            "Linked List Reversed Successfully!",
            "Message",
            JOptionPane.INFORMATION_MESSAGE
        );

        autoSaveToFile();
        reverse.setEnabled(false);
    }

    // open and display a text file
    void loadFromFile() {
        JFileChooser fileChooser =
            new JFileChooser();

        fileChooser.setDialogTitle(
            "Select Linked List Text File");

        if (fileChooser.showOpenDialog(this) !=
            JFileChooser.APPROVE_OPTION)
            return;

        File selectedFile =
            fileChooser.getSelectedFile();

        try (BufferedReader reader =
                 new BufferedReader(
                     new FileReader(selectedFile))) {

            StringBuilder content =
                new StringBuilder();

            String line;

            // read the file
            while ((line = reader.readLine()) != null)
                content.append(line).append("\n");

            JTextArea textArea =
                new JTextArea(
                    content.toString());

            textArea.setEditable(false);

            textArea.setFont(
                new Font("Arial", Font.PLAIN, 15));

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane =
                new JScrollPane(textArea);

            scrollPane.setPreferredSize(
                new Dimension(600, 350));

            JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Loaded File: " +
                    selectedFile.getName(),
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                this,
                "Error loading file: " +
                    e.getMessage(),
                "File Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // automatically save the current data
    void autoSaveToFile() {
        try (BufferedWriter writer =
                 new BufferedWriter(
                     new FileWriter(
                         "linkedlist_data.txt",
                         false))) {

            writer.write(
                "Number of Nodes: " + current);

            writer.newLine();
            writer.newLine();

            writer.write("Original Linked List:");
            writer.newLine();

            writer.write(
                original.values.length == 0
                    ? "NULL"
                    : arrayToString(original.values)
            );

            writer.newLine();
            writer.newLine();

            writer.write("Reversed Linked List:");
            writer.newLine();

            writer.write(
                reversed.values.length == 0
                    ? "Not reversed yet"
                    : arrayToString(reversed.values)
            );

            writer.newLine();
            writer.newLine();

            // save the execution time after reversing
            if (reversed.values.length > 0) {
                writer.write(
                    executionTime.getText());

                writer.newLine();
                writer.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                this,
                "Error automatically saving file: " +
                    e.getMessage(),
                "File Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // convert an array to linked list format
    String arrayToString(int[] arr) {
        StringBuilder sb =
            new StringBuilder();

        for (int val : arr)
            sb.append(val).append(" -> ");

        return sb.append("NULL").toString();
    }

    // clear everything and reset the GUI
    void clearList() {
        list.clear();
        max = current = 0;

        nodeNumber.setText("");
        input.setText("");

        original.setValues(new int[0]);
        reversed.setValues(new int[0]);

        count.setText("Nodes: 0 / 0");

        executionTime.setText(
            "Execution time: 0 nanoseconds");

        autoSaveToFile();

        // enable the starting controls again
        nodeNumber.setEnabled(true);
        setNodes.setEnabled(true);
        input.setEnabled(false);
        add.setEnabled(false);
        reverse.setEnabled(false);
    }

    // start the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
            () -> new Sael().setVisible(true)
        );
    }
}