package pkg4_2zip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author Sara
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the input address (Note that the address should be absolute address like 'C:\\user\\desktop\\input.log')");
        String input_address = scan.nextLine();
        System.out.println("Enter the output address (Note that the address should be absolute address like 'C:\\user\\desktop')");
        String output_address = scan.nextLine();

        // Read whole file and calculate frequency of each word, also save words in a list
        BufferedReader input_buffered = new BufferedReader(new FileReader(input_address));

        HashMap<String, Object> hash = new HashMap();
        String line;
        hash.put(" ", 0);
        hash.put("\n", 0);
        List<String> list = new LinkedList();
        while ((line = input_buffered.readLine()) != null) {
            String[] line_array = line.split(" ");
            for (String st : line_array) {
                list.add(st);
                list.add(" ");
                if (hash.containsKey(st)) {
                    hash.put(st, (Integer) hash.get(st) + 1);
                } else {
                    hash.put(st, 1);
                }
                hash.put(" ", (Integer) hash.get(" ") + 1);
            }
            list.remove(list.size() - 1);
            list.add("\n");
            hash.put("\n", (Integer) hash.get("\n") + 1);
        }
        input_buffered.close();

        // Put each word and its frequency in priority queue
        MyComparator comparator = new MyComparator();
        PriorityQueue<Node> pq = new PriorityQueue(comparator);
        for (String ch : hash.keySet()) {
            Node node = new Node(ch, (Integer) hash.get(ch));
            pq.offer(node);
        }

        // Create huffman tree
        for (int i = 0; i < hash.size() - 1; i++) {
            Node x = (Node) pq.poll();
            Node y = (Node) pq.poll();
            Node z = new Node(null, x.getFreq() + y.getFreq());
            z.setLeft(x);
            z.setRight(y);
            pq.add(z);
        }
        Node root = (Node) pq.poll();

        // Write tree and meta data in a file
        BufferedWriter tree_buffered = new BufferedWriter(new FileWriter(output_address + "\\tree.txt"));
        BufferedWriter meta_data_buffered = new BufferedWriter(new FileWriter(output_address + "\\meta_data.txt"));

        preorderWalk(root, tree_buffered, meta_data_buffered);

        tree_buffered.close();
        meta_data_buffered.close();

        // Make a lookup table from symbols and their encodings
        buildTable(root, "", hash);

        // Convert whole file to binary representation
        System.out.println("Please wait, this may take some time");
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            list.set(i, (String) hash.get(list.get(i)));
            sum += list.get(i).length();
        }

        int need_zero = sum % 8;
        if (need_zero != 0) {
            String zeros = "";
            for (int i = 0; i < 8 - need_zero; i++) {
                zeros += "0";
            }
            list.set(list.size() - 1, list.get(list.size() - 1) + zeros);
        }

        // Code input with huffman coding table
        BufferedWriter encode_buffered = new BufferedWriter(new FileWriter(output_address + "\\encoded.txt"));

        int index = 0;
        while (index < list.size() - 1) {
            if (list.get(index).length() <= 8) {
                String eight_bit = list.get(index);
                while (eight_bit.length() != 8) {
                    index++;
                    int my_zero = 8 - eight_bit.length();
                    if (list.get(index).length() >= (8 - eight_bit.length())) {
                        String loan = list.get(index).substring(0, my_zero);
                        list.set(index, list.get(index).substring(my_zero));
                        eight_bit += loan;
                        index--;
                    } else {
                        String loan = list.get(index).substring(0);
                        list.set(index, "");
                        eight_bit += loan;
                    }
                }
                char c = (char) Integer.parseInt(eight_bit, 2);
                encode_buffered.write(c);
                index++;
            } else {
                String eight_bit = list.get(index).substring(0, 8);
                list.set(index, list.get(index).substring(8));
                char c = (char) Integer.parseInt(eight_bit, 2);
                encode_buffered.write(c);
            }
        }
        encode_buffered.close();

        System.out.println("Your files is ready.");
    }

    private static void preorderWalk(Node node, BufferedWriter tree, BufferedWriter meta_data) throws IOException {
        if (node != null) {
            if (node.getCharacter() == null) {
                tree.write("0");
                preorderWalk(node.getLeft(), tree, meta_data);
                tree.write("1");
                preorderWalk(node.getRight(), tree, meta_data);
            } else {
                tree.write("/");
                meta_data.write(node.getCharacter() + "$");
            }
        }
    }

    private static void buildTable(Node node, String st, HashMap hash) {
        if (node.isLeaf()) {
            hash.put(node.getCharacter(), st);
        } else {
            buildTable(node.getLeft(), st + "0", hash);
            buildTable(node.getRight(), st + "1", hash);
        }
    }

    private static class MyComparator implements Comparator<Node> {

        @Override
        public int compare(Node a, Node b) {
            if (a.getFreq() > b.getFreq()) {
                return 1;
            } else if (a.getFreq() < b.getFreq()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}