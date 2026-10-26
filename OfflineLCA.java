import java.util.ArrayList;
import java.util.List;

public class OfflineLCA {

    private static class Query {
        int other;
        int id;

        Query(int other, int id) {
            this.other = other;
            this.id = id;
        }
    }

    private int[] parent;
    private int[] rank;
    private int[] ancestor;
    private boolean[] finished;

    private List<Query>[] queries;
    private int[] answers;

    public OfflineLCA(int n, int queryCount) {
        parent = new int[n];
        rank = new int[n];
        ancestor = new int[n];
        finished = new boolean[n];

        queries = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            queries[i] = new ArrayList<>();
        }

        answers = new int[queryCount];
    }

    public void addQuery(int u, int v, int id) {
        queries[u].add(new Query(v, id));
        queries[v].add(new Query(u, id));
    }

    public int[] solve(CartesianTreeNode root) {
        tarjan(root);
        return answers;
    }

    private void tarjan(CartesianTreeNode u) {
        makeSet(u.index);

        ancestor[find(u.index)] = u.index;

        if (u.left != null) {
            tarjan(u.left);

            union(u.index, u.left.index);

            ancestor[find(u.index)] = u.index;
        }

        if (u.right != null) {
            tarjan(u.right);

            union(u.index, u.right.index);

            ancestor[find(u.index)] = u.index;
        }

        finished[u.index] = true;

        for (Query query : queries[u.index]) {
            int v = query.other;

            if (finished[v]) {
                answers[query.id] = ancestor[find(v)];
            }
        }
    }

    private void makeSet(int x) {
        parent[x] = x;
        rank[x] = 0;
    }

    private int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }

        return parent[x];
    }

    private void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if (rootA == rootB) {
            return;
        }

        if (rank[rootA] < rank[rootB]) {
            parent[rootA] = rootB;
        } else if (rank[rootA] > rank[rootB]) {
            parent[rootB] = rootA;
        } else {
            parent[rootB] = rootA;
            rank[rootA]++;
        }
    }
}