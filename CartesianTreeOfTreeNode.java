public class CartesianTreeOfTreeNode {
    public CartesianTreeOfTreeEdge edge;  //internal node
    public int vertex;  //leaf
    public CartesianTreeOfTreeNode left;
    public CartesianTreeOfTreeNode right;
    public boolean leaf;
    public CartesianTreeOfTreeNode parent;
    public int depth;

    //constructor of leaf-- node of initial tree
    public CartesianTreeOfTreeNode(int vertex) {
        this.vertex=vertex;
        this.left=null;
        this.right=null;
        this.leaf=true;  //leaf
        this.parent=null;
        this.depth=0;
    }

    //constructor of internal node-- edge of initial tree
    public CartesianTreeOfTreeNode(CartesianTreeOfTreeEdge edge) {
        this.edge=edge;
        this.left=null;
        this.right=null;
        this.leaf=false;   //internal node
        this.parent=null;
        this.depth=0;
    }
}
