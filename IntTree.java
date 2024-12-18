package Unit3;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.platform.engine.support.hierarchical.Node;

public class IntTree {
    private IntTreeNode _root;
    private int _size;
    private String[] levels;

    public static class IntTreeNode {
        private static final java.util.List<IntTreeNode> ALL_NODES = new java.util.ArrayList<IntTreeNode>();

        public static void clearCycleData() {
            clearCycleData(5);
        }

        public static void clearCycleData(int visitsAllowed) {
            for (IntTreeNode node : ALL_NODES) {
                node.visitsLeft = visitsAllowed;
            }
        }

        public int data; // data stored at this node
        public IntTreeNode left; // reference to left subtree
        public IntTreeNode right; // reference to right subtree
        public int visitsLeft;

        // Constructs a leaf node with given data.
        public IntTreeNode(int data) {
            this(data, null, null);
        }

        // Constructs a IntTreeNode with the given data and links.
        public IntTreeNode(int data, IntTreeNode left, IntTreeNode right) {
            ALL_NODES.add(this);
            this.data = data;
            this.left = left;
            this.right = right;
            this.visitsLeft = 5;
        }

        public IntTreeNode getLeft() {
            return _getLeft(true);
        }

        public IntTreeNode _getLeft(boolean checkForCycle) {
            if (checkForCycle) {
                if (left != null) {
                    if (left.visitsLeft > 0) {
                        left.visitsLeft--;
                    }
                    if (left.cycle()) {
                        // throw new IllegalStateException("cycle detected in tree");
                    }
                }
            }
            return left;
        }

        public IntTreeNode getRight() {
            return _getRight(true);
        }

        public IntTreeNode _getRight(boolean checkForCycle) {
            if (checkForCycle) {
                if (right != null) {
                    if (right.visitsLeft > 0) {
                        right.visitsLeft--;
                    }
                    if (right.cycle()) {
                        // throw new IllegalStateException("cycle detected in tree");
                    }
                }
            }
            return right;
        }

        public String toString() {
            if (this.cycle()) {
                return "(cycle!)";
            } else {
                return String.format("[%d]", data);
            }
        }

        private boolean cycle() {
            return visitsLeft == 0;
        }
    }

    public static class NodeInfo {
        public int x;
        public int y;
        public int left;
        public int right;
        public int data;
        public boolean hasLeft;
        public boolean hasRight;

        public NodeInfo(int _x, int _y, int _left, int _right, int _data, boolean _hasLeft, boolean _hasRight) {
            this.x = _x;
            this.y = _y;
            this.left = _left;
            this.right = _right;
            this.data = _data;
            this.hasLeft = _hasLeft;
            this.hasRight = _hasRight;
        }
    }
    public IntTree(int root) {
        _root = new IntTreeNode(root);
        _size = 1;
    }
    public IntTree() {
        _root = null;
        _size = 0;
    }
    public IntTree(int[] args) {
        for (int i = 0; i < args.length; i++) {
            add(args[i]);
        }
    }
    public boolean add(int root) {
        if (_root == null) {
            _root = new IntTreeNode(root);
            return true;
        }
        IntTreeNode node = _root;
        try {
            while (true) {
                if (root >= node.data) {
                    if (node.right == null) {
                        node.right = new IntTreeNode(root);
                        _size++;
                        return true;
                    } else {
                        node = node.right;
                    }
                } else {
                    if (node.left == null) {
                        node.left = new IntTreeNode(root);
                        _size++;
                        return true;
                    } else {
                        node = node.left;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        
    }
    public void prePrint() {
        recursivePre(_root);
    }
    public void orderedPrint() {
        recursiveOrdered(_root);
    }
    public void postPrint() {
        recursivePost(_root);
    }

    public int height() {
        return recursiveHeight(_root, 1);
    }
    
    private int recursiveHeight(IntTreeNode node, int depth) {
        int num = depth;
        if (node.left != null) {
            int num2 = recursiveHeight(node.left, depth + 1);
            if (num2 > num) {
                num = num2;
            }
        }
        if (node.right != null) {
            int num2 = recursiveHeight(node.right, depth + 1);
            if (num2 > num) {
                num = num2;
            }
        }
        return num;
    }

    private void recursivePre(IntTreeNode node) {
        if (node != null) {
            System.out.println(node.data);
            recursivePre(node.left);
            recursivePre(node.right);
        }
    } 
    private void recursiveOrdered(IntTreeNode node) {
        if (node != null) {
            recursivePre(node.left);
            System.out.println(node.data);
            recursivePre(node.right);
        }
    }
    private void recursivePost(IntTreeNode node) {
        if (node != null) {
            recursivePre(node.left);
            recursivePre(node.right);
            System.out.println(node.data);
        }
    }
   
    private ArrayList<NodeInfo> nodes = new ArrayList<NodeInfo>();
    private int lowestIntX = Integer.MAX_VALUE;
/*
public void prettyPrint() {

IntTreeNode temp = findValues(_root, 0, 0);
nodes = sortList(nodes);

int offset = Math.abs(lowestIntX);
for (int i = 0; i < nodes.size() - 1; i++) {
    NodeInfo curr = nodes.get(i);
    int target = curr.y * 2;
    int siblingDistance = 0;
    if (curr.left > nodes.get(i).right) {
        siblingDistance = nodes.get(i).left * 2;
    } else {
        siblingDistance = nodes.get(i).right * 2;
    }
    levels[target] += addLines(curr.x + siblingDistance / 2, " ") + "[" + curr.data + "]"
            + addLines(siblingDistance / 2, " ");
    if (target + 1 < height() * 2) {
        levels[target + 1] += addLines(curr.x, " ");
        if (curr.hasLeft) {
            levels[target + 1] += "/";
        }
        levels[target + 1] += addLines(siblingDistance, " ");
        if (curr.hasRight) {
            levels[target + 1] += "\\";
        }
    }
    
}

}
*/
    
public void prettyPrint() {
    levels = new String[height() * 2 + 2];
    for (int i = 0; i < levels.length; i++) {
        levels[i] = "";
    }
    recursivePrintAt(_root, 0, 0, 0);
    for (int i = 0; i < levels.length; i++) {
        System.out.println(levels[i]);
    }
}
    
    public int recursivePrintAt(IntTreeNode node, int depth, int offset, int dir) {
        int start = offset;
        boolean hl = false;
        boolean hr = false;
        
        if (node.left != null) {
            start += recursivePrintAt(node.left, depth + 2, offset, -1);
        }
        start += 3;
        levels[depth + 1] += "   ";
        for (int i = 0; i < levels.length; i += 2) {
            if (i != depth) {
                levels[i] += addLines(3, " ");
            } else {
                levels[depth] += "[" + node.data + "]";
            }
        }
        
        if (node.right != null) {
            start += recursivePrintAt(node.right, depth + 2, start, 1) - start;
            hr = true;
        }

        return start;
    }

    private IntTreeNode findValues(IntTreeNode node, int x, int y) {
        IntTreeNode t = new IntTreeNode(0);
        int count = 0;
        boolean hl = false;
        boolean hr = false;
        if (node.left != null) {
            t.left = findValues(node.left, x - 3, y + 1);
            count += t.left.data;
            hl = true;
        }
        count++;
        if (node.right != null) {
            t.right = findValues(node.right, x + 3, y + 1);
            hr = true;
            nodes.add(new NodeInfo(x, y, count, t.right.data, node.data, hl, hr));
        } else {
            nodes.add(new NodeInfo(x, y, count, 0, node.data, hl, hr));
        }
        
        t.data = count;
        
        return t;

    }

    private ArrayList<NodeInfo> sortList(ArrayList<NodeInfo> l) {
        ArrayList<NodeInfo> temp = new ArrayList<NodeInfo>();
        boolean isAdded = false;
        for (int i = 0; i < l.size(); i++) {
            int size = temp.size();
            isAdded = false;
            if (l.get(i).x < lowestIntX) {
                lowestIntX = l.get(i).x;
            }
            for (int j = 0; j < size; j++) {
                if (!isAdded) {
                    if (l.get(i).y < temp.get(j).y) {
                        temp.add(j, l.get(i));
                        isAdded = true;
                    } else if (l.get(i).y == temp.get(j).y && l.get(i).x < temp.get(j).x) {
                        temp.add(j, l.get(i));
                        isAdded = true;
                    }
                } else {
                    break;
                }

            }
            if (!isAdded) {
                temp.add(l.get(i));
            }
        }
        return temp;
    }


    private String addLines(int count, String thing) {
        String answer = "";
        for (int i = 0; i < count; i++) {
            answer += thing;
        }
        return answer;
    }


}
