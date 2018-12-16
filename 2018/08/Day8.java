import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2018/day/8
 */
public class Day8 {

    public static void main(String[] args) {
        Tree tree = buildTree(getTreeDefinition());
        part1(tree);
        part2(tree);
    }

    private static void part1(Tree tree) {
        System.out.println(metadataSum(tree));
    }

    private static void part2(Tree tree) {
        System.out.println(nodeValue(tree));
    }

    private static int nodeValue(Tree tree) {
        int value = 0;
        if (tree.children.length == 0) {
            for (int i = 0; i < tree.metadata.length; i++) {
                value += tree.metadata[i];
            }
        } else {
            for (int i = 0; i < tree.metadata.length; i++) {
                int childIndex = tree.metadata[i] - 1;
                if (childIndex < tree.children.length) {
                    value += nodeValue(tree.children[childIndex]);
                }
            }
        }

        return value;
    }

    private static int metadataSum(Tree tree) {
        int sum = 0;
        for (int i =0; i < tree.children.length; i++) {
            sum += metadataSum(tree.children[i]);
        }

        for (int i = 0; i < tree.metadata.length; i++) {
            sum += tree.metadata[i];
        }

        return sum;
    }

    private static TreeDefinition getTreeDefinition() {
        List<Integer> tree;

        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String treeDef = scanner.nextLine();
            tree = new ArrayList<Integer>();
            for (String n : treeDef.split("\\s+")) {
                tree.add(Integer.parseInt(n.trim()));
            }
        } else {
            tree = Collections.emptyList();
        }

        return new TreeDefinition(tree);
    }

    private static Tree buildTree(TreeDefinition def) {
        int childNodes = def.read();
        int metadataEntries = def.read();

        Tree tree = new Tree();
        tree.children = new Tree[childNodes];
        for (int i = 0; i < childNodes; i++) {
            tree.children[i] = buildTree(def);
        }

        tree.metadata = new int[metadataEntries];
        for (int i = 0; i < metadataEntries; i++) {
            tree.metadata[i] = def.read();
        }

        return tree;
    }

    private static class Tree {

        private int[] metadata;
        private Tree[] children;
    }

    private static class TreeDefinition {

        private List<Integer> tree;
        private int pos;

        TreeDefinition(List<Integer> tree) {
            this.tree = tree;
        }

        int read() {
            return tree.get(pos++);
        }
    }
}
