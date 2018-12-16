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
        List<Integer> tree = getTree();
        part1(tree);
    }

    private static void part1(List<Integer> tree) {
        System.out.println(metadataSum(new TreeTraversal(tree)));
    }

    private static int metadataSum(TreeTraversal tree) {
        int childNodes = tree.read();
        int metadataEntries = tree.read();

        int sum = 0;
        for (int i = 0; i < childNodes; i++) {
            sum += metadataSum(tree);
        }

        for (int i = 0; i < metadataEntries; i++) {
            sum += tree.read();
        }

        return sum;
    }

    private static List<Integer> getTree() {
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

        return tree;
    }

    private static class TreeTraversal {

        private List<Integer> tree;
        private int pos;

        TreeTraversal(List<Integer> tree) {
            this.tree = tree;
        }

        int read() {
            return tree.get(pos++);
        }
    }
}
