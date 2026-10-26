public class TreeQuery {
    private CartesianTreeOfTree tree;
    private CartesianTreeOfTreeLCA lca;

    public TreeQuery(CartesianTreeOfTree tree){
        this.tree=tree;
        this.lca=new CartesianTreeOfTreeLCA(tree.getRoot());
    }


    public CartesianTreeOfTreeEdge query(int u,int v){
        CartesianTreeOfTreeNode leafU=lca.findLeaf(tree.getRoot(),u);
        CartesianTreeOfTreeNode leafV=lca.findLeaf(tree.getRoot(),v);
        if(leafU==null || leafV==null){
            System.out.println("Node not found");
            return null;
        }
        CartesianTreeOfTreeNode lcaNode=lca.query(leafU,leafV);
        if(lcaNode==null){
            System.out.println("LCA not found");
            return null;
        }
        return lcaNode.edge;
    }


}