package lobog;

import java.util.Scanner;

class SinglyLinkedListNode {

    int data;

    SinglyLinkedListNode next;
    SinglyLinkedListNode(int data) {

        this.data = data;
        this.next = null;

    }

}

public class palobog {

    // function to reverse the linked list
    public static SinglyLinkedListNode reverse(SinglyLinkedListNode head) {
        SinglyLinkedListNode prev = null;
        SinglyLinkedListNode current = head;
        while (current != null) {

            SinglyLinkedListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        return prev;
    }

    // display the linked list
    public static void printList(SinglyLinkedListNode head) {
        SinglyLinkedListNode current = head;
        while (current != null) {
            System.out.print(current.data);
            if (current.next != null) {
                System.out.print(" -> ");
            }
            current = current.next;
        }
        System.out.println(" -> NULL");
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // asking for number of nodes
        System.out.print("Enter number of nodes: ");
        int n = scanner.nextInt();
        if (n <= 0) {

            System.out.println("The list is empty.");
            scanner.close();
            return;
        }

        // create the linked list
        SinglyLinkedListNode head = null;
        SinglyLinkedListNode tail = null;

        System.out.println("Enter " + n + " values:");

        for (int i = 0; i < n; i++) {
            System.out.print("Node " + (i + 1) + ": ");
            int value = scanner.nextInt();

            SinglyLinkedListNode newNode =
                    new SinglyLinkedListNode(value);

            if (head == null) {
                head = newNode;
                tail = newNode;

            } else {

                tail.next = newNode;
                tail = newNode;

            }

        }

        // display original list
        System.out.println();
        System.out.print("Original List: ");

        printList(head);

        // start measuring time
        long startTime = System.nanoTime();

        // reverse the list
        head = reverse(head);

        // the stopping measuring time
        long endTime = System.nanoTime();

        // calculate the runtime in nanoseconds
        long runtime = endTime - startTime;

        // display the reversed list
        System.out.print("Reversed List: ");
        printList(head);

        // display the runtime in nanoseconds
        System.out.println("Runtime: " + runtime + " nanoseconds");
        scanner.close();

    }

}