import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    private static final String DATA_DIR = "test-data/";
    public static void main(String[] args) {

        // ===== TEST CASES ARRAY =====
        String[] arrayFiles = {
            "array1.txt",
            "array2.txt",
            "array3.txt",
            "array4.txt",
            "array5.txt",
            "array6.txt",
            "array7.txt",
            "array8.txt"
        };

        // ===== TEST CASES TREE =====
        String[] treeFiles = {
            "tree1.txt",
            "tree2.txt",
            "tree3.txt",
            "tree4.txt",
            "tree5.txt",
            "tree6.txt",
            "tree7.txt"
        };

        String[] queryRMQFiles = {
            "queries_rmq1.txt",
            "queries_rmq2.txt",
            "queries_rmq2.txt",
            "queries_rmq2.txt",
            "queries_rmq2.txt",
            "queries_rmq6.txt",
            "queries_rmq7.txt",
            "queries_rmq8.txt"
        };

      
        String[] queryBEQFiles = {
            "queries_beq1.txt",
            "queries_beq2.txt",
            "queries_beq3.txt",
            "queries_beq4.txt",
            "queries_beq5.txt",
            "queries_beq6.txt",
            "queries_beq7.txt"
        };

        
        for (int t = 0; t < arrayFiles.length; t++) {
            System.out.println("\n=====================================");
            System.out.println("ARRAY TEST " + (t+1) + ": " + arrayFiles[t]);
            System.out.println("=====================================");

            int[] array = readArray(arrayFiles[t]);
            if (array == null) continue;

            // Cartesian Tree
            System.out.println("\nCARTESIAN TREE");
            CartesianTree tree = new CartesianTree(array);
           

            // LCA
            LCA lca = new LCA(tree, array.length);
            OptimalLCA optimalLCA=new OptimalLCA(tree,array.length);
           

            // Sparse Table + NaiveRMQ
            SparseTable sparseTable = new SparseTable(array);
            NaiveRMQ naiveRMQ = new NaiveRMQ(array);
            
            if(array.length<=100){
                tree.printArray();
                tree.printTree();
                System.out.print("Inorder: ");
                tree.inorder();
                System.out.println("Root: value="+tree.getRoot().value+ " index="+tree.getRoot().index);
                System.out.println("\nLCA");
                lca.printTree();
                System.out.println("\nRMQ");
                sparseTable.printTable();
            }else{
                System.out.println("Cartesian Tree constructed successfully.");
                System.out.println("LCA structure constructed successfully.");
                System.out.println("Sparse Table constructed successfully.");
                System.out.println("Detailed structures omitted for large input.");
            }

            // RMQ Queries
            runRMQQueries(queryRMQFiles[t], array,tree, naiveRMQ, sparseTable, lca,optimalLCA);
        }

      
        for (int t = 0; t < treeFiles.length; t++) {
            System.out.println("\n=====================================");
            System.out.println("TREE TEST " + (t+1) + ": " + treeFiles[t]);
            System.out.println("=====================================");

            int[] treeInfo = readTreeInfo(treeFiles[t]);
            if (treeInfo == null) continue;

            int nodeNum = treeInfo[0];
            List<CartesianTreeOfTreeEdge> edges = readTreeEdges(treeFiles[t]);
            if (edges == null) continue;

            // Cartesian Tree of Tree
            System.out.println("\nCARTESIAN TREE OF A TREE");
            long startTime=System.nanoTime();
            CartesianTreeOfTree treeOfTree = new CartesianTreeOfTree(nodeNum, edges);
            long endTime=System.nanoTime();
            double constructionTimeMs=(endTime-startTime)/1_000_000.0;
            double constructionTimeSec=constructionTimeMs/1000.0;
            System.out.printf("\nConstruction time: %.3f ms (%.3f s)%n",constructionTimeMs, constructionTimeSec);
            if(nodeNum<=100){
                treeOfTree.printTree();
            }else{
                System.out.println("Cartesian Tree of Tree constructed succesfully.");
                System.out.println("Full structure omitted for large input ("+ nodeNum+ " nodes).");
            }


            // BEQ Queries 
            runBEQQueries(queryBEQFiles[t], nodeNum, treeOfTree, edges);
        }
    }



    private static int[] readArray(String filename) {
        try {
            Scanner sc = new Scanner(new File(DATA_DIR +filename));
            int n = sc.nextInt();
            int[] array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = sc.nextInt();
            }
            sc.close();
            return array;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return null;
        }
    }

    private static int[] readTreeInfo(String filename) {
        try {
            Scanner sc = new Scanner(new File(DATA_DIR+filename));
            int nodeNum = sc.nextInt();
            int edgeNum = sc.nextInt();
            sc.close();
            return new int[]{nodeNum, edgeNum};
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return null;
        }
    }

    private static List<CartesianTreeOfTreeEdge> readTreeEdges(String filename) {
        try {
            Scanner sc = new Scanner(new File(DATA_DIR+filename));
            int nodeNum = sc.nextInt();
            int edgeNum = sc.nextInt();
            List<CartesianTreeOfTreeEdge> edges = new ArrayList<>();
            System.out.println("Nodes: " + nodeNum + " Edges: " + edgeNum);
            for (int i = 0; i < edgeNum; i++) {
                int node1  = sc.nextInt();
                int node2  = sc.nextInt();
                int weight = sc.nextInt();
                edges.add(new CartesianTreeOfTreeEdge(node1, node2, weight));
                if (nodeNum<=100){
                    System.out.println("Edge " + (i+1) + ": " +node1 + "-" + node2 + " weight=" + weight);
                }   
            }
            if (nodeNum>100){
                System.out.println("Edge list omitted for large input.");
            }
            sc.close();
            return edges;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return null;
        }
    }

    private static void runRMQQueries(String filename,int[] array,CartesianTree tree,NaiveRMQ naiveRMQ,SparseTable sparseTable,LCA lca,OptimalLCA optimalLCA) {

        int[] offlineAnswers=runOfflineLCA(filename,array.length,tree);
        if(offlineAnswers==null) return;

        try {
            Scanner sc=new Scanner(new File(DATA_DIR+filename));
            int q=sc.nextInt();

            System.out.println("\n===== RMQ Queries =====");

            for(int i=0;i<q;i++) {

                int left=sc.nextInt();
                int right=sc.nextInt();

                if(left<0 || right<0 || left>=array.length || right>=array.length || left>right) {
                    System.out.println("Invalid bounds: "+left+" "+right);
                    continue;
                }

                int naiveResult=naiveRMQ.query(left,right);
                int sparseResult=sparseTable.query(left,right);
                LCANode lcaResult=lca.query(left,right);
                CartesianTreeNode optimalResult=optimalLCA.query(left,right);

                int offlineIndex=offlineAnswers[i];
                int offlineResult=array[offlineIndex];

                System.out.println("RMQ("+left+", "+right+"):");
                System.out.println("  Naive:       "+naiveResult);
                System.out.println("  Sparse:      "+sparseResult);

                if(lcaResult!=null) System.out.println("  LCA:         "+lcaResult.value);
                if(optimalResult!=null) System.out.println("  Optimal LCA: "+optimalResult.value);

                System.out.println("  Offline LCA: "+offlineResult);

                boolean allSame=(naiveResult==sparseResult) &&
                                (lcaResult!=null && naiveResult==lcaResult.value) &&
                                (optimalResult!=null && naiveResult==optimalResult.value) &&
                                (naiveResult==offlineResult);

                if(allSame) System.out.println("  Match: YES");
                else System.out.println("  Match: NO");
            }

            sc.close();

        } catch(FileNotFoundException e) {
            System.out.println("File not found: "+filename);
        }
    
    }


    private static int[] runOfflineLCA(String filename,int n,CartesianTree tree) {

        try {
            Scanner sc=new Scanner(new File(DATA_DIR+filename));
            int q=sc.nextInt();

            OfflineLCA offlineLCA=new OfflineLCA(n,q);

            for(int i=0;i<q;i++) {

                int left=sc.nextInt();
                int right=sc.nextInt();

                if(left<0 || right<0 || left>=n || right>=n || left>right) continue;

                offlineLCA.addQuery(left,right,i);
            }

            sc.close();

            return offlineLCA.solve(tree.getRoot());

        } catch(FileNotFoundException e) {
            System.out.println("File not found: "+filename);
            return null;
        }
    }
    
    private static void runBEQQueries(String filename,int nodeNum,CartesianTreeOfTree treeOfTree,List<CartesianTreeOfTreeEdge> edges) {

        CartesianTreeOfTreeEdge[] offlineAnswers=runOfflineBEQ(filename,nodeNum,treeOfTree);
        if(offlineAnswers==null) return;

        try {
            Scanner sc=new Scanner(new File(DATA_DIR+filename));
            int q=sc.nextInt();

            System.out.println("\n===== BEQ Queries =====");

            TreeQuery treeQuery=new TreeQuery(treeOfTree);
            NaiveBEQ naiveBEQ=new NaiveBEQ(nodeNum,edges);

            for(int i=0;i<q;i++) {
                int u=sc.nextInt();
                int v=sc.nextInt();

                if(u<0 || v<0 || u>=nodeNum || v>=nodeNum) {
                    System.out.println("Invalid nodes: "+u+" "+v);
                    continue;
                }

                CartesianTreeOfTreeEdge treeResult=treeQuery.query(u,v);
                CartesianTreeOfTreeEdge naiveResult=naiveBEQ.query(u,v);
                CartesianTreeOfTreeEdge offlineResult=offlineAnswers[i];

                System.out.println("BEQ("+u+", "+v+"):");

                if(treeResult!=null)
                    System.out.println("  TreeQuery:   edge "+treeResult.node1+"-"+treeResult.node2+" weight="+treeResult.weight);

                if(naiveResult!=null)
                    System.out.println("  NaiveBEQ:    edge "+naiveResult.node1+"-"+naiveResult.node2+" weight="+naiveResult.weight);

                if(offlineResult!=null)
                    System.out.println("  Offline LCA: edge "+offlineResult.node1+"-"+offlineResult.node2+" weight="+offlineResult.weight);

                boolean same=treeResult!=null && naiveResult!=null && offlineResult!=null &&
                            treeResult.weight==naiveResult.weight &&
                            treeResult.weight==offlineResult.weight;

                if(same) System.out.println("  Match: YES");
                else System.out.println("  Match: NO");
            }

            sc.close();

        } catch(FileNotFoundException e) {
            System.out.println("File not found: "+filename);
        }
    }

    private static CartesianTreeOfTreeEdge[] runOfflineBEQ(String filename,int nodeNum,CartesianTreeOfTree treeOfTree) {

        try {
            Scanner sc=new Scanner(new File(DATA_DIR+filename));
            int q=sc.nextInt();

            OfflineCToTLCA offlineLCA=new OfflineCToTLCA(treeOfTree,nodeNum,q);

            for(int i=0;i<q;i++) {
                int u=sc.nextInt();
                int v=sc.nextInt();

                if(u<0 || v<0 || u>=nodeNum || v>=nodeNum) continue;

                offlineLCA.addQuery(u,v,i);
            }

            sc.close();

            return offlineLCA.solve();

        } catch(FileNotFoundException e) {
            System.out.println("File not found: "+filename);
            return null;
        }
    }

}  