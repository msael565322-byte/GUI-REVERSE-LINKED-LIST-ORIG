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

    // Function to reverse the linked list

    public static SinglyLinkedListNode reverse(SinglyLinkedListNode head) {

        SinglyLinkedListNode prev = null;

        SinglyLinkedListNode current = head;

        while (current != null) {

            // Save the next node

            SinglyLinkedListNode next = current.next;

            // Reverse the pointer

            current.next = prev;

            // Move prev forward

            prev = current;

            // Move current forward

            current = next;

        }

        // Return the new head

        return prev;

    }

    // Display the linked list

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

        System.out.println("=== REVERSE LINKED LIST ===");

        // Ask for number of nodes

        System.out.print("Enter number of nodes: ");

        int n = scanner.nextInt();

        if (n <= 0) {

            System.out.println("The list is empty.");

            scanner.close();

            return;

        }

        // Create the linked list

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

        // Display original list

        System.out.println();

        System.out.print("Original List: ");

        printList(head);

        // Start measuring time

        long startTime = System.nanoTime();

        // Reverse the list

        head = reverse(head);

        // Stop measuring time

        long endTime = System.nanoTime();

        // Calculate runtime in nanoseconds

        long runtime = endTime - startTime;

        // Display reversed list

        System.out.print("Reversed List: ");

        printList(head);

        // Display runtime in nanoseconds

        System.out.println("Runtime: " + runtime + " nanoseconds");

        scanner.close();

    }

}