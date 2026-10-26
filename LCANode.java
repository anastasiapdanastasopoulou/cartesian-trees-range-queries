public class LCANode {
    public int value;
    public int index;
    public int depth;
    public LCANode left;
    public LCANode right;
    public LCANode parent;

    public LCANode(int value, int index, int depth, LCANode parent){
        this.value=value;
        this.index=index;
        this.depth= depth;
        this.left=null;
        this.right=null;
        this.parent=parent;
    }
}
