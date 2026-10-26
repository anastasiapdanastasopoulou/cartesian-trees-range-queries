import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CartesianTreeOfTree {
    private CartesianTreeOfTreeNode root;
    private int edgeNum;
    private int nodeNum;

    public CartesianTreeOfTree(int nodeNum, List<CartesianTreeOfTreeEdge> edges) {
        this.nodeNum = nodeNum;
        this.edgeNum = edges.size();
        root = build(edges);
    }
    
    private static class IndexedEdge{
        CartesianTreeOfTreeEdge edge;
        int order;
        IndexedEdge(CartesianTreeOfTreeEdge edge, int order){
            this.edge=edge;
            this.order=order;
        }
    }

    public CartesianTreeOfTreeNode build(List<CartesianTreeOfTreeEdge> edges) {
        if(nodeNum==0){
            return null;
        }
        CartesianTreeOfTreeNode[] componentTree= new CartesianTreeOfTreeNode[nodeNum];
        for(int i=0; i<nodeNum;i++){
            componentTree[i]=new CartesianTreeOfTreeNode(i);
        }
        if(edges.isEmpty()){
            return componentTree[0];
        }
        List<IndexedEdge> sortedEdges=new ArrayList<>();
        for(int i=0;i<edges.size();i++){
            sortedEdges.add(new IndexedEdge(edges.get(i),i));
        }
        sortedEdges.sort(new Comparator<IndexedEdge>(){
            @Override 
            public int compare(IndexedEdge a, IndexedEdge b){
                int byWeight=Integer.compare(b.edge.weight,a.edge.weight);
                if(byWeight!=0){
                    return byWeight;
                }
                return Integer.compare(b.order,a.order);
            }
        }
        );
        UnionFind uf= new UnionFind(nodeNum);
        for(IndexedEdge indexed: sortedEdges){
            CartesianTreeOfTreeEdge e=indexed.edge;
            int root1=uf.find(e.node1);
            int root2=uf.find(e.node2);
            CartesianTreeOfTreeNode newNode= new CartesianTreeOfTreeNode(e);
            newNode.left=componentTree[root1];
            newNode.right=componentTree[root2];
            int newRoot=uf.union(root1,root2);
            componentTree[newRoot]=newNode;
        }
        return componentTree[uf.find(0)];
    }


    private static class UnionFind{
        private final int[] parent;
        private final int[] rank;
        UnionFind(int n){
            parent=new int[n];
            rank=new int[n];
            for(int i=0;i<n;i++){
                parent[i]=i;
                rank[i]=0;
            }
        }
        int find(int x){
            if(parent[x]!=x){
                parent[x]=find(parent[x]);
            }
            return parent[x];
        }
        int union(int x, int y){
            int rootX=find(x);
            int rootY=find(y);
            if(rootX==rootY){
                return rootX;
            }
            if(rank[rootX]<rank[rootY]){
                parent[rootX]=rootY;
                return rootY;
            }
            else if(rank[rootX]>rank[rootY]){
                parent[rootY]=rootX;
                return rootX;
            }else{
                parent[rootY]=rootX;
                rank[rootX]++;
                return rootX;
            }
        }
    }
    public void printTree(){
        printTree(root,"",false);
    }
    private void printTree(CartesianTreeOfTreeNode node,String prefix,boolean isLeft){
        if(node==null) return;
        String symbol;
        if(isLeft){
            symbol="├── ";
        }
        else{
            symbol="└── ";
        }
        if(node.leaf){
            System.out.println(prefix+symbol+"Leaf: node="+ node.vertex);
        }
        else{
            System.out.println(prefix+symbol+"Edge: ("+node.edge.node1+"-"+node.edge.node2+", weight="+node.edge.weight+")");
        }
        String newprefix;
        if(isLeft){
            newprefix=prefix+"│   ";
        }
        else{
            newprefix=prefix+"    ";
        }
        printTree(node.left,newprefix,true);
        printTree(node.right,newprefix,false);
    }

    public CartesianTreeOfTreeNode getRoot(){
        return root;
    }
}