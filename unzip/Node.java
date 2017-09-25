package unzip;

/**
 *
 * @author Sara
 */
public class Node {
    
    private String character;
    private Node left, right, parent;
    private boolean root;

    public Node(String character, boolean root) {
        this.character = character;
        this.left = this.right = this.parent = null;
        this.root = root;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
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

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
    
    
}
