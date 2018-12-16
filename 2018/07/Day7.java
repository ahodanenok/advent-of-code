import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
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
        part2(steps);
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

    private static void part2(List<Step> steps) {
        Map<Step, Integer> edgesCount = new HashMap<Step, Integer>(steps.size());
        for (int i = 0; i < steps.size(); i++) {
            edgesCount.put(steps.get(i), steps.get(i).dependsOn.size());
        }

        LinkedList<Step> queue = new LinkedList<Step>();
        Construction construction = new Construction(5, 60);
        while (true) {
            List<Step> finishedSteps = construction.getFinishedSteps();
            if (!finishedSteps.isEmpty()) {
                for (Step step : steps) {
                    for (Step finishedStep : finishedSteps) {
                        if (step.dependsOn.contains(finishedStep)) {
                            edgesCount.put(step, edgesCount.get(step) - 1);
                        }
                    }
                }
            }

            List<Step> availableSteps = new ArrayList<Step>();
            for (Map.Entry<Step, Integer> entry : edgesCount.entrySet()) {
                if (entry.getValue() == 0) {
                    availableSteps.add(entry.getKey());
                }
            }

            for (Step step : availableSteps) {
                edgesCount.remove(step);
            }

            queue.addAll(availableSteps);
            while (!queue.isEmpty() && construction.hasAvailableWorker()) {
                construction.assign(queue.removeFirst());
            }

            if (!construction.done()) {
                construction.tick();
            } else {
                break;
            }
        }

        System.out.println(construction.time());
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

    private static class Construction {

        int stepTime;
        int[] workersTime;
        Step[] workersSteps;
        int time = 0;

        Construction(int workers, int stepTime) {
            this.workersTime = new int[workers];
            this.workersSteps = new Step[workers];
            this.stepTime = stepTime;
        }

        List<Step> getFinishedSteps() {
            List<Step> steps = new ArrayList<Step>();

            for (int i = 0; i < workersSteps.length; i++) {
                if (workersSteps[i] != null && workersTime[i] == stepDuration(workersSteps[i])) {
                    steps.add(workersSteps[i]);
                    workersTime[i] = 0;
                    workersSteps[i] = null;
                }
            }

            return steps;
        }

        private int stepDuration(Step step) {
            return stepTime + (step.name - 'A') + 1;
        }

        boolean done() {
            for (int i = 0; i < workersSteps.length; i++) {
                if (workersSteps[i] != null) {
                    return false;
                }
            }

            return true;
        }

        int time() {
            return time;
        }

        boolean hasAvailableWorker() {
            for (int i = 0; i < workersSteps.length; i++) {
                if (workersSteps[i] == null) {
                    return true;
                }
            }

            return false;
        }

        void assign(Step step) {
            for (int i = 0; i < workersSteps.length; i++) {
                if (workersSteps[i] == null) {
                    workersSteps[i] = step;
                    break;
                }
            }
        }

        void tick() {
            time++;
            for (int i = 0; i < workersSteps.length; i++) {
                if (workersSteps[i] != null) {
                    workersTime[i]++;
                }
            }
        }
    }
}
