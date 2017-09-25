package pkg4_2zip;

/**
 *
 * @author Sara
 */
public class Node {
    
    private String character;
    private int freq;
    private Node left, right;

    public Node(String character, int freq) {
        this.character = character;
        this.freq = freq;
        this.left = this.right = null;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
    
    public boolean isLeaf(){
        return right == null && left == null;
    }
}
