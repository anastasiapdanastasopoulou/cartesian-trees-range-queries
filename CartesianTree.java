import java.util.ArrayDeque;
import java.util.Deque;
public class CartesianTree {
    private CartesianTreeNode root;
    private int[] originalArray;

    public CartesianTree(int[] array){
        if(array==null || array.length==0){
            root=null;
            originalArray=array;
        }
        else{
            originalArray=array;
            root=makeTree(array);
        }
    }


    private CartesianTreeNode makeTree(int[] array){
        Deque<CartesianTreeNode> stack= new ArrayDeque<>();
        for (int i=0; i<array.length;i++){
            CartesianTreeNode current= new CartesianTreeNode(array[i],i);
            CartesianTreeNode lastPopped=null;
            while(!stack.isEmpty() && stack.peek().value>current.value){
                lastPopped=stack.pop();
            }
            current.left=lastPopped;
            if(!stack.isEmpty()){
                stack.peek().right=current;
            }
            stack.push(current);
        }
        return stack.peekLast();
    }


    public void inorder(){     //returns the original array... just to check if the tree is right
        inorder(root);
        System.out.println();
    }
    public void inorder(CartesianTreeNode node){
        if (node==null) return;
        inorder(node.left);
        System.out.print("value= "+ node.value+ " index= "+node.index+ " | ");
        inorder(node.right);
    }


    public void printArray() {
        System.out.print("Original Array: [");
        for (int i = 0; i < originalArray.length; i++) {
            System.out.print(originalArray[i]);
            if (i < originalArray.length - 1)
                System.out.print(", ");
        }
        System.out.println("]");
    }

    public void printTree() {
        printTree(root, "", false);
    }

    private void printTree(CartesianTreeNode node, String prefix, boolean isLeft) {
        if (node == null) return;
        System.out.println(prefix + (isLeft ? "├── " : "└── ") + "value=" + node.value + " index=" + node.index);
        printTree(node.left,  prefix + (isLeft ? "│   " : "    "), true);
        printTree(node.right, prefix + (isLeft ? "│   " : "    "), false);
    }

    public CartesianTreeNode getRoot() {
        return root;
    }

}
