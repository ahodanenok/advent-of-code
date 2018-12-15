import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * Advent of Code - Day 7
 * https://adventofcode.com/2018/day/7
 */
public class Day7 {

    public static void main(String[] args) {
        List<Step> steps = getSteps();
        part1(steps);
    }

    private static void part1(List<Step> steps) {
        Map<Step, Integer> edgesCount = new HashMap<Step, Integer>(steps.size());
        for (int i = 0; i < steps.size(); i++) {
            edgesCount.put(steps.get(i), steps.get(i).dependsOn.size());
        }

        StringBuilder stepsOrder = new StringBuilder();
        while (!edgesCount.isEmpty()) {
            List<Step> availableSteps = new ArrayList<Step>();
            for (Map.Entry<Step, Integer> entry : edgesCount.entrySet()) {
                if (entry.getValue() == 0) {
                    availableSteps.add(entry.getKey());
                }
            }

            if (!availableSteps.isEmpty()) {
                Collections.sort(availableSteps);
                Step nextStep = availableSteps.get(0);

                stepsOrder.append(nextStep.name);
                edgesCount.remove(nextStep);

                for (Step step : steps) {
                    if (step.dependsOn.contains(nextStep)) {
                        edgesCount.put(step, edgesCount.get(step) - 1);
                    }
                }
            }
        }

        System.out.println(stepsOrder);
    }

    private static List<Step> getSteps() {
        List<Step> steps = new ArrayList<Step>();
        Map<Character, Step> stepsByName = new HashMap<Character, Step>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            char stepName = line.charAt(36);
            char dependsOnStepName = line.charAt(5);

            if (!stepsByName.containsKey(stepName)) {
                stepsByName.put(stepName, new Step(stepName));
            }
            
            if (!stepsByName.containsKey(dependsOnStepName)) {
                stepsByName.put(dependsOnStepName, new Step(dependsOnStepName));
            }

            Step step = stepsByName.get(stepName);
            Step dependsOnStep = stepsByName.get(dependsOnStepName);
            step.dependsOn.add(dependsOnStep);
            if (!steps.contains(step)) {
                steps.add(step);
            }
            if (!steps.contains(dependsOnStep)) {
                steps.add(dependsOnStep);
            }
        }

        return steps;
    }

    private static class Step implements Comparable<Step> {

        private final char name;
        private final List<Step> dependsOn;

        Step(char name) {
            this.name = name;
            this.dependsOn = new ArrayList<Step>();
        }

        @Override
        public boolean equals(Object obj) {
            return ((Step) obj).name == name;
        }

        public int hashCode() {
            return (int) name;
        }

        @Override
        public String toString() {
            return String.valueOf(name);
        }

        @Override
        public int compareTo(Step step) {
            return Character.compare(name, step.name);
        }
    }
}
