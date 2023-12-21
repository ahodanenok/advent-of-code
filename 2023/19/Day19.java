import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 19
 * https://adventofcode.com/2023/day/19
 */
public class Day19 {

    public static void main(String[] args) throws Exception {
        Input input = getInput();
        part1(input);
    }

    private static void part1(Input input) {
        List<Part> acceptedParts = new ArrayList<>();
        for (Part part : input.parts) {
            String currentWorkflowName = "in";
            while (!currentWorkflowName.equals("A") && !currentWorkflowName.equals("R")) {
                for (WorkflowStep step : input.getWorkflow(currentWorkflowName).steps) {
                    if (step.matches(part)) {
                        currentWorkflowName = step.nextWorkflowName;
                        break;
                    }
                }
            }

            if (currentWorkflowName.equals("A")) {
                acceptedParts.add(part);
            }
        }

        int sum = 0;
        for (Part part : acceptedParts) {
            sum += part.x;
            sum += part.m;
            sum += part.a;
            sum += part.s;
        }

        System.out.println("Part 1: " + sum);
    }


    private static Input getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Input input = new Input();

            String line;
            while (!(line = reader.readLine()).isBlank()) {
                line = line.trim();
                String name = line.substring(0, line.indexOf('{'));
                List<WorkflowStep> steps = new ArrayList<>();
                String[] parts = line.substring(line.indexOf('{') + 1, line.length() - 1).split(",");
                for (String part : parts) {
                    String[] stepParts = part.split(":");
                    if (stepParts.length == 1) {
                        steps.add(new WorkflowStep(part.trim(), Extractor.NONE, Condition.ALWAYS, -1));
                    } else if (stepParts[0].contains(">")) {
                        steps.add(new WorkflowStep(
                            stepParts[1].trim(),
                            Extractor.valueOf(stepParts[0].split(">")[0].trim().toUpperCase()),
                            Condition.GT,
                            Integer.parseInt(stepParts[0].split(">")[1].trim())));
                    } else if (stepParts[0].contains("<")) {
                        steps.add(new WorkflowStep(
                            stepParts[1].trim(),
                            Extractor.valueOf(stepParts[0].split("<")[0].trim().toUpperCase()),
                            Condition.LT,
                            Integer.parseInt(stepParts[0].split("<")[1].trim())));
                    }
                }

                input.workflows.add(new Workflow(name, steps));
            }
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] parts = line.substring(1, line.length() - 1).split(",");
                input.parts.add(new Part(
                    Integer.parseInt(parts[0].split("=")[1].trim()),
                    Integer.parseInt(parts[1].split("=")[1].trim()),
                    Integer.parseInt(parts[2].split("=")[1].trim()),
                    Integer.parseInt(parts[3].split("=")[1].trim())));
            }

            return input;
        }
    }

    private static class Input {

        final List<Workflow> workflows = new ArrayList<>();
        final List<Part> parts = new ArrayList<>();

        Workflow getWorkflow(String name) {
            return workflows.stream()
                .filter(w -> w.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Workflow not found: " + name));
        }
    }

    private static class Workflow {

        final String name;
        final List<WorkflowStep> steps;

        Workflow(String name, List<WorkflowStep> steps) {
            this.name = name;
            this.steps = steps;
        }
    }

    private static class WorkflowStep {

        final String nextWorkflowName;
        final Extractor extractor;
        final Condition condition;
        final int expectedValue;

        WorkflowStep(String nextWorkflowName, Extractor extractor, Condition condition, int expectedValue) {
            this.nextWorkflowName = nextWorkflowName;
            this.extractor = extractor;
            this.condition = condition;
            this.expectedValue = expectedValue;
        }

        boolean matches(Part part) {
            return condition.check(extractor.get(part), expectedValue);
        }
    }

    private enum Condition {

        LT {
            @Override
            boolean check(int value, int expectedValue) {
                return value < expectedValue;
            }
        },
        GT {
            @Override
            boolean check(int value, int expectedValue) {
                return value > expectedValue;
            }
        },
        ALWAYS {
            @Override
            boolean check(int value, int expectedValue) {
                return true;
            }
        };

        abstract boolean check(int value, int expectedValue);
    }

    private enum Extractor {

        X {
            @Override
            int get(Part part) {
                return part.x;
            }
        },
        M {
            @Override
            int get(Part part) {
                return part.m;
            }
        },
        A {
            @Override
            int get(Part part) {
                return part.a;
            }
        },
        S {
            @Override
            int get(Part part) {
                return part.s;
            }
        },
        NONE {
            @Override
            int get(Part part) {
                return -1;
            }
        };

        abstract int get(Part part);
    }

    private static class Part {

        final int x;
        final int m;
        final int a;
        final int s;

        Part(int x, int m, int a, int s) {
            this.x = x;
            this.m = m;
            this.a = a;
            this.s = s;
        }
    }
}
