import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptimalLCA {

    private CartesianTreeNode root;
    private int originalSize;
    private CartesianTreeNode[] eulerNodes;
    private int[] eulerDepth;
    private int[] firstOccurence;
    private int blockSize;
    private int blockCount;
    private int[][][] blockTables;
    private int[] blockMinimumIndex;
    private int[][] blockSparseTable;
    private int[] blockLogs;

    public OptimalLCA(CartesianTree tree, int n) {
        this.root = tree.getRoot();
        this.originalSize = n;
        firstOccurence = new int[n];
        Arrays.fill(firstOccurence, -1);
        buildEulerTour();
        buildRMQStructure();
    }

    private static class Frame {
        CartesianTreeNode node;
        int depth;
        int state;

        Frame(CartesianTreeNode node, int depth) {
            this.node = node;
            this.depth = depth;
            this.state = 0;
        }
    }

    private void buildEulerTour() {
        if (root == null) {
            eulerNodes = new CartesianTreeNode[0];
            eulerDepth = new int[0];
            return;
        }

        List<CartesianTreeNode> nodes = new ArrayList<>();
        List<Integer> depths = new ArrayList<>();
        Deque<Frame> stack = new ArrayDeque<>();

        stack.push(new Frame(root, 0));

        while (!stack.isEmpty()) {
            Frame frame = stack.peek();

            if (frame.state == 0) {
                addEulerEntry(frame.node, frame.depth, nodes, depths);
                frame.state = 1;

                if (frame.node.left != null) {
                    stack.push(
                            new Frame(
                                    frame.node.left,
                                    frame.depth + 1
                            )
                    );
                }

            } else if (frame.state == 1) {
                if (frame.node.left != null) {
                    addEulerEntry(
                            frame.node,
                            frame.depth,
                            nodes,
                            depths
                    );
                }

                frame.state = 2;

                if (frame.node.right != null) {
                    stack.push(new Frame(frame.node.right,frame.depth + 1));
                }

            } else {
                if (frame.node.right != null) {
                    addEulerEntry(frame.node,frame.depth,nodes,depths);
                }

                stack.pop();
            }
        }

        eulerNodes = nodes.toArray(new CartesianTreeNode[0]);
        eulerDepth = new int[depths.size()];

        for (int i = 0; i < depths.size(); i++) {
            eulerDepth[i] = depths.get(i);
        }
    }

    private void addEulerEntry(
            CartesianTreeNode node,
            int depth,
            List<CartesianTreeNode> nodes,
            List<Integer> depths) {

        int position = nodes.size();

        nodes.add(node);
        depths.add(depth);

        if (firstOccurence[node.index] == -1) {
            firstOccurence[node.index] = position;
        }
    }

    private void buildRMQStructure() {
        int m = eulerDepth.length;

        if (m == 0) {
            return;
        }

        int logM =31 - Integer.numberOfLeadingZeros(m);

        blockSize =Math.max(1, logM / 2);

        blockCount = (m + blockSize - 1) / blockSize;

        blockTables =new int[blockCount][][];

        blockMinimumIndex =new int[blockCount];

        Map<Long, int[][]> typeTables =new HashMap<>();

        for (int block = 0; block < blockCount; block++) {

            int start =block * blockSize;

            int end =Math.min(start + blockSize,m);

            int length =end - start;

            int mask = 0;

            for (int i = 1; i < length; i++) {

                int diff =eulerDepth[start + i]- eulerDepth[start + i - 1];

                if (diff == 1) {
                    mask |= (1 << (i - 1));
                } else if (diff != -1) {
                    throw new IllegalStateException("Euler depth difference is not +/-1");
                }
            }

            long key =(((long) length) << 32)|(mask & 0xffffffffL);

            int[][] table =typeTables.get(key);

            if (table == null) {
                table =buildTypeTable(length,mask);

                typeTables.put(key,table);
            }

            blockTables[block] = table;

            int minimum = start;

            for (int i = start + 1; i < end; i++) {
                if (eulerDepth[i]< eulerDepth[minimum]) {

                    minimum = i;
                }
            }

            blockMinimumIndex[block] =minimum;
        }

        buildBlockSparseTable();
    }

    private int[][] buildTypeTable(int length,int mask) {

        int[] canonicalDepth =new int[length];

        canonicalDepth[0] = 0;

        for (int i = 1; i < length; i++) {

            boolean goesDown =(mask & (1 << (i - 1))) != 0;

            canonicalDepth[i] =canonicalDepth[i - 1]+ (goesDown ? 1 : -1);
        }

        int[][] table =new int[length][length];

        for (int left = 0; left < length; left++) {

            int minimum = left;

            for (int right = left;right < length;right++) {

                if (canonicalDepth[right]< canonicalDepth[minimum]) {

                    minimum = right;
                }

                table[left][right] =minimum;
            }
        }

        return table;
    }

    private void buildBlockSparseTable() {

        blockLogs =new int[blockCount + 1];

        for (int i = 2;i <= blockCount;i++) {

            blockLogs[i] =blockLogs[i / 2] + 1;
        }

        int levels =blockLogs[blockCount] + 1;

        blockSparseTable =new int[levels][blockCount];

        for (int block = 0;block < blockCount;block++) {

            blockSparseTable[0][block] =blockMinimumIndex[block];
        }

        for (int level = 1;level < levels;level++) {

            int length =1 << level;

            int half =length >> 1;

            for (int i = 0;i + length <= blockCount;i++) {

                int first =blockSparseTable[level - 1][i];

                int second =blockSparseTable[level - 1][i + half];

                blockSparseTable[level][i] =betterIndex(first, second);
            }
        }
    }

    public CartesianTreeNode query(int indexA,int indexB) {

        if (indexA < 0 ||indexB < 0 ||indexA >= originalSize ||indexB >= originalSize) {

            return null;
        }

        int left =firstOccurence[indexA];

        int right =firstOccurence[indexB];

        if (left == -1 ||right == -1) {

            return null;
        }

        if (left > right) {
            int temp = left;
            left = right;
            right = temp;
        }

        int resultPosition =rmq(left, right);

        return eulerNodes[resultPosition];
    }

    private int rmq(int left,int right) {

        int leftBlock =left / blockSize;

        int rightBlock =right / blockSize;

        if (leftBlock == rightBlock) {
            return queryInsideBlock(leftBlock,left,right);
        }

        int leftEnd =Math.min((leftBlock + 1) * blockSize- 1,eulerDepth.length - 1);

        int result =queryInsideBlock(leftBlock,left,leftEnd);

        int rightStart =rightBlock * blockSize;

        int rightResult =queryInsideBlock(rightBlock,rightStart,right);

        result =betterIndex(result,rightResult);

        int firstMiddleBlock =leftBlock + 1;

        int lastMiddleBlock =rightBlock - 1;

        if (firstMiddleBlock<= lastMiddleBlock) {

            int middleResult =queryWholeBlocks(firstMiddleBlock,lastMiddleBlock);

            result =betterIndex(result,middleResult);
        }

        return result;
    }

    private int queryInsideBlock(int block,int globalLeft,int globalRight) {

        int blockStart =block * blockSize;

        int localLeft =globalLeft - blockStart;

        int localRight =globalRight - blockStart;

        int localMinimum =blockTables[block][localLeft][localRight];

        return blockStart+ localMinimum;
    }

    private int queryWholeBlocks(int leftBlock,int rightBlock) {

        int count =rightBlock- leftBlock+ 1;

        int k =blockLogs[count];

        int first =blockSparseTable[k][leftBlock];

        int secondStart =rightBlock- (1 << k)+ 1;

        int second =blockSparseTable[k][secondStart];
        return betterIndex(first,second);
    }

    private int betterIndex(int first,int second) {
        if (eulerDepth[first]<= eulerDepth[second]) {
            return first;
        }

        return second;
    }

    public void printInfo() {
        System.out.println("Euler tour size: "+ eulerNodes.length);
        System.out.println("Block size: "+ blockSize);
        System.out.println("Number of blocks: "+ blockCount);
    }
}