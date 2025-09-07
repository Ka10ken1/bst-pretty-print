package com.epam.rd.autocode.bstprettyprint;


import java.util.ArrayList;
import java.util.List;

class MyPrintableTree implements PrintableTree
{
    private static final char RIGHT_UP_DOWN = '┤';
    private static final char RIGHT_DOWN = '┐';
    private static final char LEFT_DOWN = '┌';
    private static final char BRANCH_CHAR = '│';
    private TreeNode root;

    @Override
    public void add(final int i) {
        if (root == null) {
            root = new TreeNode(i, null);
        } else {
            root.add(i);
        }
    }

    @Override
    public String prettyPrint()
    {
        StringBuilder builder = new StringBuilder();
        List<Integer> branchesPositions = new ArrayList<>();
        for (PrintNode printNode : getValues(root)) {
            char[] chars = printNode.toString().toCharArray();
            insertBranches(chars, branchesPositions);
            updateBranchesList(chars, branchesPositions);
            builder.append(String.valueOf(chars));
        }
        return builder.toString();
    }

    private List<PrintNode> getValues(final TreeNode node)
    {
        if (node == null) {
            return new ArrayList<>();
        }
        List<PrintNode> values = new ArrayList<>(getValues(node.leftChild()));
        values.add(new PrintNode(node));
        values.addAll(getValues(node.rightChild()));
        return values;
    }

    private void insertBranches(final char[] chars, final List<Integer> branchesPositions)
    {
        for (int i = 0; i < chars.length; i++) {
            if (branchesPositions.contains(i) && chars[i] == ' ') {
                chars[i] = '│';
            }
        }
    }

    private void updateBranchesList(final char[] chars, final List<Integer> branchesPositions)
    {
        for (int i = 0; i < chars.length; i++) {
            if (branchesPositions.contains(i) && chars[i] != BRANCH_CHAR) {
                branchesPositions.remove(Integer.valueOf(i));
            }
            if (chars[i] == RIGHT_DOWN || chars[i] == LEFT_DOWN
                    || chars[i] == RIGHT_UP_DOWN) {
                branchesPositions.add(i);
            }
        }
    }
}

class TreeNode
{
    private final int value;
    private final TreeNode parent;
    private TreeNode leftChild;
    private TreeNode rightChild;

    public TreeNode(final int value, final TreeNode parent) {
        this.value = value;
        this.parent = parent;
    }

    public int value() {
        return value;
    }

    public TreeNode leftChild() {
        return leftChild;
    }

    public TreeNode rightChild() {
        return rightChild;
    }

    public TreeNode parent() {
        return parent;
    }

    void add(final int i) {
        if (i < value) {
            if (leftChild == null) {
                leftChild = new TreeNode(i, this);
            } else {
                leftChild.add(i);
            }
        } else  if (i > value) {
            if (rightChild == null) {
                rightChild = new TreeNode(i, this);
            } else {
                rightChild.add(i);
            }
        }
    }
}


class PrintNode
{
    private static final String RIGHT_UP_DOWN = "┤";
    private static final String RIGHT_UP = "┘";
    private static final String RIGHT_DOWN = "┐";
    private static final String LEFT_DOWN = "┌";
    private static final String LEFT_UP = "└";
    private static final String EMPTY_STRING = "";
    private static final char LINE_FEED = '\n';
    private static final String WHITESPACE = " ";
    private final int value;
    private final int numberOfWhitespaces;
    private ParentRelations parentRelations;
    private ChildrenRelations childrenRelations;

    public PrintNode(final TreeNode node) {
        value = node.value();
        determineAncestorRelations(node);
        determineDescendantRelations(node);
        numberOfWhitespaces = determineNumberOfWhitespaces(node) - 1;
    }

    private void determineAncestorRelations(final TreeNode node)
    {
        if (node.parent() == null) {
            parentRelations = ParentRelations.ROOT;
        } else if (node.parent().value() > node.value()) {
            parentRelations = ParentRelations.LEFT;
        } else if (node.parent().value() < node.value()) {
            parentRelations = ParentRelations.RIGHT;
        }
    }

    private void determineDescendantRelations(final TreeNode node)
    {
        if (node.leftChild() == null && node.rightChild() == null) {
            childrenRelations = ChildrenRelations.NONE;
        } else if (node.leftChild() == null) {
            childrenRelations = ChildrenRelations.RIGHT;
        } else if (node.rightChild() == null) {
            childrenRelations = ChildrenRelations.LEFT;
        } else {
            childrenRelations = ChildrenRelations.BOTH;
        }
    }

    private int determineNumberOfWhitespaces(final TreeNode node)
    {
        if (node.parent() == null) {
            return 0;
        }
        int number = getDigitsNumber(node.parent().value()) + 1;
        return number + determineNumberOfWhitespaces(node.parent());
    }

    private int getDigitsNumber(final int number) {
        return String.valueOf(number).length();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(WHITESPACE.repeat(Math.max(0, numberOfWhitespaces)))
                .append(getParentCharacter())
                .append(value)
                .append(getChildCharacter())
                .append(LINE_FEED);
        return builder.toString();
    }

    private String getParentCharacter() {
        if (parentRelations == ParentRelations.LEFT) {
            return LEFT_DOWN;
        } else if (parentRelations == ParentRelations.RIGHT) {
            return LEFT_UP;
        } else {
            return EMPTY_STRING;
        }
    }

    private String getChildCharacter() {
        if (childrenRelations == ChildrenRelations.BOTH) {
            return RIGHT_UP_DOWN;
        } else if (childrenRelations == ChildrenRelations.LEFT) {
            return RIGHT_UP;
        } else if (childrenRelations == ChildrenRelations.RIGHT) {
            return RIGHT_DOWN;
        }else {
            return EMPTY_STRING;
        }
    }

    enum ParentRelations {
        LEFT, RIGHT, ROOT
    }

    enum ChildrenRelations {
        LEFT, RIGHT, BOTH, NONE
    }
}