public class LCA {
    private LCANode root;
    private int n;
    public LCA(CartesianTree tree, int n){
        this.n=n;
        root= makeTree(tree.getRoot(),null,0);
    }
    private LCANode makeTree(CartesianTreeNode node,LCANode parent, int depth){
        if (node==null) return null;
        LCANode lcanode=new LCANode(node.value,node.index,depth,parent);
        lcanode.left=makeTree(node.left,lcanode,depth+1);
        lcanode.right=makeTree(node.right,lcanode,depth+1);
        return lcanode;
    }
    private LCANode findNode(LCANode node,int index){
        if (node==null) return null;
        if(node.index==index) return node;
        LCANode left=findNode(node.left,index);
        if (left!=null) return left;
        return findNode(node.right,index);
    }
    public LCANode query(int indexA,int indexB){
        LCANode a= findNode(root,indexA);
        LCANode b=findNode(root,indexB);
        if(a==null || b==null){
            System.out.println("Node not found");
            return null;
        }
        while(a.depth>b.depth) a=a.parent;
        while(b.depth>a.depth) b=b.parent;
        while(a!=b){
            a=a.parent;
            b=b.parent;
        }
        return a;
    }
    private void printTree(LCANode node,String prefix, boolean isLeft){
        if(node==null) return;
        String symbol;
        if(isLeft){
            symbol="├──";
        }else{
            symbol="└──";
        }
        System.out.println(prefix+symbol+"value="+node.value+" index="+node.index+" depth="+node.depth);
        String newPrefix;
        if(isLeft){
            newPrefix=prefix+"│   ";
        }else{
            newPrefix=prefix+"    ";
        }
        printTree(node.left,newPrefix,true);
        printTree(node.right,newPrefix,false);
    }
    public void printTree(){
        printTree(root,"",false);
    }
    public LCANode getRoot(){
        return root;
    }
}
