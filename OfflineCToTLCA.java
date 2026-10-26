import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;

public class OfflineCToTLCA {

    private static class Query {
        int other;
        int id;

        Query(int other,int id) {
            this.other=other;
            this.id=id;
        }
    }

    private CartesianTreeOfTreeNode root;
    private IdentityHashMap<CartesianTreeOfTreeNode,Integer> nodeId;
    private CartesianTreeOfTreeNode[] nodes;
    private int[] leafId;

    private int[] parent;
    private int[] rank;
    private int[] ancestor;
    private boolean[] finished;

    private List<List<Query>> queries;
    private CartesianTreeOfTreeEdge[] answers;

    public OfflineCToTLCA(CartesianTreeOfTree tree,int nodeNum,int queryCount) {
        root=tree.getRoot();
        nodeId=new IdentityHashMap<>();
        leafId=new int[nodeNum];
        Arrays.fill(leafId,-1);

        buildIndex();

        parent=new int[nodes.length];
        rank=new int[nodes.length];
        ancestor=new int[nodes.length];
        finished=new boolean[nodes.length];

        queries=new ArrayList<>();
        for(int i=0;i<nodes.length;i++) queries.add(new ArrayList<>());

        answers=new CartesianTreeOfTreeEdge[queryCount];
    }

    private void buildIndex() {
        List<CartesianTreeOfTreeNode> list=new ArrayList<>();

        if(root==null) {
            nodes=new CartesianTreeOfTreeNode[0];
            return;
        }

        ArrayDeque<CartesianTreeOfTreeNode> stack=new ArrayDeque<>();
        stack.push(root);

        while(!stack.isEmpty()) {
            CartesianTreeOfTreeNode node=stack.pop();
            int id=list.size();

            nodeId.put(node,id);
            list.add(node);

            if(node.leaf) leafId[node.vertex]=id;

            if(node.right!=null) stack.push(node.right);
            if(node.left!=null) stack.push(node.left);
        }

        nodes=list.toArray(new CartesianTreeOfTreeNode[0]);
    }

    public void addQuery(int u,int v,int id) {
        int idU=leafId[u];
        int idV=leafId[v];

        if(idU==-1 || idV==-1) return;

        queries.get(idU).add(new Query(idV,id));
        queries.get(idV).add(new Query(idU,id));
    }

    public CartesianTreeOfTreeEdge[] solve() {
        if(root!=null) tarjan(root);
        return answers;
    }

    private void tarjan(CartesianTreeOfTreeNode node) {
        int u=nodeId.get(node);

        makeSet(u);
        ancestor[find(u)]=u;

        if(node.left!=null) {
            tarjan(node.left);
            int left=nodeId.get(node.left);
            union(u,left);
            ancestor[find(u)]=u;
        }

        if(node.right!=null) {
            tarjan(node.right);
            int right=nodeId.get(node.right);
            union(u,right);
            ancestor[find(u)]=u;
        }

        finished[u]=true;

        for(Query query:queries.get(u)) {
            if(finished[query.other]) {
                int lcaId=ancestor[find(query.other)];
                CartesianTreeOfTreeNode lcaNode=nodes[lcaId];
                answers[query.id]=lcaNode.leaf ? null : lcaNode.edge;
            }
        }
    }

    private void makeSet(int x) {
        parent[x]=x;
        rank[x]=0;
    }

    private int find(int x) {
        if(parent[x]!=x) parent[x]=find(parent[x]);
        return parent[x];
    }

    private void union(int a,int b) {
        int rootA=find(a);
        int rootB=find(b);
        if(rootA==rootB) return;
        if(rank[rootA]<rank[rootB]) parent[rootA]=rootB;
        else if(rank[rootA]>rank[rootB]) parent[rootB]=rootA;
        else {
            parent[rootB]=rootA;
            rank[rootA]++;
        }
    }
}