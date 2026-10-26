public class CartesianTreeNode {
    public int value;
    public int index;
    public CartesianTreeNode left;
    public CartesianTreeNode right;

    public CartesianTreeNode(int value, int index){
        this.value= value;
        this.index=index;
        this.left=null;
        this.right=null;
    }
}