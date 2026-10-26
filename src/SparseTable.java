public class SparseTable {
    private int[][] st;
    private int[] log2;
    private int n; //size of array
    private int k;  //maximum level (log2(n))

    public SparseTable(int[] array){
        n=array.length;
        k=1;
        while((1<<k)<=n){
            k++;
        }
        st=new int[k+1][n];
        log2=new int[n+1];
        //precomputing algorithms
        log2[1]=0;
        for(int i=2;i<=n;i++){
            log2[i]=log2[i/2]+1;
        }
        //level 0- each element alone
        for (int j=0;j<n;j++){
            st[0][j]=array[j];
        }
        //computing the rest of the levels
        for (int i=1;i<=k;i++){
            for (int j=0;j+(1<<i)<=n;j++){
                st[i][j]=Math.min(st[i-1][j],st[i-1][j+(1<<(i-1))]);
            }
        }
    }

    public int query(int left, int right){
        int i=log2[right-left+1];
        return Math.min(st[i][left],st[i][right-(1<<i)+1]);
    }

    public void printTable() {
        System.out.println("Sparse Table:");
        for (int i = 0; i <= k; i++) {
            if (n - (1 << i) + 1 <= 0) break;
            System.out.print("st[" + i + "]: ");
            for (int j = 0; j + (1 << i) <= n; j++)
                System.out.print(st[i][j] + " ");
            System.out.println();
        }
    }

}
