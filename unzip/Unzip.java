package unzip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Sara
 */
public class Unzip {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the input address (Note that the address should be absolute address like 'C:\\user\\desktop')");
        String input_address = scan.nextLine();
        System.out.println("Enter the output address (Note that the address should be absolute address like 'C:\\user\\desktop')");
        String output_address = scan.nextLine();

        // Open and read the inputs
        BufferedReader input_buffered = new BufferedReader(new FileReader(input_address + "\\encoded.txt"));
        BufferedReader tree_buffered = new BufferedReader(new FileReader(input_address + "\\tree.txt"));
        BufferedReader meta_data_buffered = new BufferedReader(new FileReader(input_address + "\\meta_data.txt"));
        
        System.out.println("Please wait, this may take some time");
        
        int character;
        String word = "";
        List<String> meta_list = new LinkedList();
        while ((character = meta_data_buffered.read()) != -1) {
            char ch = (char) character;
            if (ch == '$') {
                meta_list.add(word);
                word = "";
            } else {
                word += ch;
            }
        }

        String treeST = "";
        while ((character = tree_buffered.read()) != -1) {
            char ch = (char) character;
            treeST += ch;
        }
        String[] tree = treeST.split("");

        // Convert characters to equivalent binary code        
        List<String> list = new LinkedList();
        while ((character = input_buffered.read()) != -1) {
            int ascii = (int) ((char) character);
            String binString = Integer.toBinaryString(ascii);
            int need_zero = binString.length() % 8;
            if (binString.length() % 8 != 0) {
                String zeros = "";
                for (int i = 0; i < 8 - need_zero; i++) {
                    zeros += "0";
                }
                binString = zeros + binString;
            }
            list.add(binString);
        }
        input_buffered.close();

        // Create huffman tree
        int childCounter = 0;
        Node root = new Node(null, true);
        Node node = root;
        for (int i = 0; i < tree.length; i++) {
            if (node != null) {
                if (node.getLeft() == null || node.getRight() == null) {
                    if (tree[i].equals("0")) {
                        Node child = new Node(null, false);
                        node.setLeft(child);
                        child.setParent(node);
                        node = child;
                    } else if (tree[i].equals("1")) {
                        Node child = new Node(null, false);
                        node.setRight(child);
                        child.setParent(node);
                        node = child;
                    } else {
                        node.setCharacter(meta_list.get(childCounter++));
                        node = node.getParent();
                    }
                } else if (!node.isRoot()) {
                    node = node.getParent();
                    i--;
                }
            }
        }

        while (node != null && !node.isRoot()) {
            node = node.getParent();
        }
        root = node;

        // Read binary text and walk on the tree and write it on file
        BufferedWriter output_buffered = new BufferedWriter(new FileWriter(output_address + "\\decodedFile.txt"));
        int index = 0;
        while (index < list.size()) {
            String[] word_binary = list.get(index++).split("");
            for (int i = 0; i <= word_binary.length; i++) {
                if (node.getCharacter() != null) {
                    output_buffered.write(node.getCharacter());
                    node = root;
                }
                if (i < word_binary.length) {
                    if ("0".equals(word_binary[i])) {
                        node = node.getLeft();
                    } else {
                        node = node.getRight();
                    }
                }
            }
        }
        output_buffered.close();

        System.out.println("Your file is ready.");
    }
}
