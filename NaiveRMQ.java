public class NaiveRMQ {
    private int[] array;
    private int n;

    public NaiveRMQ(int[] array){
        this.array=array;
        this.n=array.length;
    }

    public int query(int left, int right){
        int minVal=array[left];
        for(int i=left+1; i<=right;i++){
            if(array[i]<minVal){
                minVal=array[i];
            }
        }
        return minVal;
    }

    public void printArray(){
        System.out.print("Array: [");
        for (int i=0;i<n;i++){
            System.out.print(array[i]);
            if(i<n-1) System.out.print(", ");
        }
        System.out.print("]");
    }
}
