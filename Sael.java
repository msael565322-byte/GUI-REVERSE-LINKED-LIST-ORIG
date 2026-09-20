package sael;

import javax.swing.*;
import java.awt.*;

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

public class Sael extends JFrame {

    LinkedList list = new LinkedList();

    JTextField nodeNumber = new JTextField(5);

    // Bigger Node Input box
    JTextField input = new JTextField(12);

    JTextArea original = new JTextArea();
    JTextArea reversed = new JTextArea();

    JLabel count = new JLabel("Nodes: 0 / 0",
            SwingConstants.CENTER);

    JButton setNodes = new JButton("Set Nodes");
    JButton add = new JButton("Add Node");
    JButton reverse = new JButton("Reverse");
    JButton clear = new JButton("Clear");

    int max = 0;
    int current = 0;

    public Sael() {

        setTitle("Reverse Linked List");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // TITLE
        JLabel title = new JLabel(
                "SINGLY LINKED LIST",
                SwingConstants.CENTER);

        title.setFont(
                new Font("Arial", Font.BOLD, 28));

        // NUMBER OF NODES
        JPanel nodePanel = new JPanel();

        nodePanel.add(
                new JLabel("Number of Nodes:"));

        nodePanel.add(nodeNumber);
        nodePanel.add(setNodes);

        // INPUT
        JLabel inputLabel = new JLabel(
                "Enter a number:",
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

        // ORIGINAL LIST
        original.setEditable(false);
        original.setFont(
                new Font("Monospaced", Font.BOLD, 18));

        JPanel originalPanel =
                new JPanel(new BorderLayout());

        originalPanel.add(
                new JLabel("Original Linked List:"),
                BorderLayout.NORTH);

        originalPanel.add(
                new JScrollPane(original),
                BorderLayout.CENTER);

        // REVERSED LIST
        reversed.setEditable(false);
        reversed.setFont(
                new Font("Monospaced", Font.BOLD, 18));

        JPanel reversedPanel =
                new JPanel(new BorderLayout());

        reversedPanel.add(
                new JLabel("Reversed Linked List:"),
                BorderLayout.NORTH);

        reversedPanel.add(
                new JScrollPane(reversed),
                BorderLayout.CENTER);

        // LIST BOXES
        JPanel lists =
                new JPanel(new GridLayout(2, 1, 10, 10));

        lists.add(originalPanel);
        lists.add(reversedPanel);

        // BUTTON COLORS
        add.setBackground(
                new Color(60, 120, 200));
        add.setForeground(Color.WHITE);

        reverse.setBackground(
                new Color(50, 150, 80));
        reverse.setForeground(Color.WHITE);

        clear.setBackground(
                new Color(200, 70, 70));
        clear.setForeground(Color.WHITE);

        // BUTTONS
        JPanel buttons = new JPanel();

        buttons.add(add);
        buttons.add(reverse);
        buttons.add(clear);

        // MAIN PANEL
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

        // INITIAL STATE
        add.setEnabled(false);
        reverse.setEnabled(false);
        input.setEnabled(false);

        // BUTTON ACTIONS
        setNodes.addActionListener(
                e -> setNodeLimit());

        add.addActionListener(
                e -> addNode());

        reverse.addActionListener(
                e -> reverseList());

        clear.addActionListener(
                e -> clearList());

        input.addActionListener(
                e -> addNode());
    }

    // SET NUMBER OF NODES
    void setNodeLimit() {

        try {

            max = Integer.parseInt(
                    nodeNumber.getText().trim());

            if (max <= 0)
                throw new Exception();

            list.clear();
            current = 0;

            original.setText("");
            reversed.setText("");

            count.setText(
                    "Nodes: 0 / " + max);

            nodeNumber.setEnabled(false);
            setNodes.setEnabled(false);

            input.setEnabled(true);
            add.setEnabled(true);
            reverse.setEnabled(false);

            input.requestFocus();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid number of nodes.");
        }
    }

    // ADD NODE
    void addNode() {

        if (current >= max)
            return;

        try {

            int value = Integer.parseInt(
                    input.getText().trim());

            // Add node
            list.add(value);

            current++;

            String result = list.show();

            // GUI
            original.setText(result);
            reversed.setText("");

            count.setText(
                    "Nodes: " + current +
                    " / " + max);

            input.setText("");

            // ALL NODES ENTERED
            if (current == max) {

                add.setEnabled(false);
                reverse.setEnabled(true);
                input.setEnabled(false);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid integer.");
        }
    }

    // REVERSE LIST
    void reverseList() {

        // Reverse the list
        list.reverse();

        // Display reversed list
        String newList = list.show();

        reversed.setText(newList);

        reverse.setEnabled(false);
    }

    // CLEAR
    void clearList() {

        list.clear();

        max = 0;
        current = 0;

        nodeNumber.setText("");
        input.setText("");

        original.setText("");
        reversed.setText("");

        count.setText("Nodes: 0 / 0");

        nodeNumber.setEnabled(true);
        setNodes.setEnabled(true);

        input.setEnabled(false);
        add.setEnabled(false);
        reverse.setEnabled(false);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new Sael().setVisible(true));
    }
}
