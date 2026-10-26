import java.util.List;
import java.util.ArrayList;
public class NaiveBEQ {
    private int numNodes;
    private List<CartesianTreeOfTreeEdge> edges;
    public NaiveBEQ(int numNodes, List<CartesianTreeOfTreeEdge> edges){
        this.numNodes=numNodes;
        this.edges=edges;
    }
    private List<Integer> findPath(int start,int end){
        List<Integer> visited=new ArrayList<>();
        List<Integer> waiting=new ArrayList<>();
        List<Integer> parent=new ArrayList<>();
        for(int i=0;i<numNodes;i++){
            parent.add(-1);
        }
        waiting.add(start);
        visited.add(start);
        while(!waiting.isEmpty()){
            int current=waiting.remove(0);
            if(current==end) break;
            for(int k=0;k<edges.size();k++){
                CartesianTreeOfTreeEdge e=edges.get(k);
                int neighbor=-1;
                if(e.node1==current && !visited.contains(e.node2)){
                    neighbor=e.node2;
                }else if(e.node2==current && !visited.contains(e.node1)){
                    neighbor=e.node1;
                }
                if (neighbor!=-1){
                    visited.add(neighbor);
                    waiting.add(neighbor);
                    parent.set(neighbor,current);
                }
            }
        }
        List<Integer> path=new ArrayList<>();
        int current=end;
        while(current!=-1){
            path.add(0,current);
            current=parent.get(current);
        }
        return path;
    }

    public CartesianTreeOfTreeEdge query(int u,int v){
        List<Integer> path=findPath(u,v);
        CartesianTreeOfTreeEdge minEdge=null;
        for(int i=0;i<path.size()-1;i++){
            int a=path.get(i);
            int b=path.get(i+1);
            for(int k=0;k<edges.size();k++){
                CartesianTreeOfTreeEdge e=edges.get(k);
                if((e.node1==a && e.node2==b) ||(e.node1==b && e.node2==a)){
                    if(minEdge==null|| e.weight<minEdge.weight){
                        minEdge=e;
                    }
                }
            }
        }
        return minEdge;
    }
}
