public class CartesianTreeOfTreeLCA {
    private CartesianTreeOfTreeNode root;
    public CartesianTreeOfTreeLCA(CartesianTreeOfTreeNode root){
        this.root=root;
        depth_parent(root,null,0);
    }

    private void depth_parent(CartesianTreeOfTreeNode node,CartesianTreeOfTreeNode parent,int depth){
        if(node==null) return;
        node.parent=parent;
        node.depth=depth;
        depth_parent(node.left,node,depth+1);
        depth_parent(node.right,node,depth+1);
    }

    public CartesianTreeOfTreeNode findLeaf(CartesianTreeOfTreeNode node,int vertex){
        if(node==null) return null;
        if(node.leaf && node.vertex==vertex) return node;
        CartesianTreeOfTreeNode left=findLeaf(node.left,vertex);
        if(left!=null) return left;
        return findLeaf(node.right,vertex);
    }

    public CartesianTreeOfTreeNode query(CartesianTreeOfTreeNode a, CartesianTreeOfTreeNode b){
        if(a==null || b==null) return null;
        while(a.depth>b.depth) a=a.parent;
        while(b.depth>a.depth) b=b.parent;
        while(a!=b){
            a=a.parent;
            b=b.parent;
        }
        return a;
    }
}
