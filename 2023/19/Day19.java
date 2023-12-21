import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Advent of Code - Day 19
 * https://adventofcode.com/2023/day/19
 */
public class Day19 {

    public static void main(String[] args) throws Exception {
        Input input = getInput();
        part1(input);
        part2(input);
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

    private static void part2(Input input) {
        List<Frame> acceptedFrames = new ArrayList<>();
        LinkedList<Frame> queue = new LinkedList<>();
        queue.addLast(new Frame("in", 1, 4000, 1, 4000, 1, 4000, 1, 4000));
        while (!queue.isEmpty()) {
            Frame currentFrame = queue.removeFirst();
            if (currentFrame.workflowName.equals("A")) {
                acceptedFrames.add(currentFrame);
                continue;
            }

            if (currentFrame.workflowName.equals("R")) {
                continue;
            }

            Workflow workflow = input.getWorkflow(currentFrame.workflowName);
            int xMin = currentFrame.xMin;
            int xMax = currentFrame.xMax;
            int mMin = currentFrame.mMin;
            int mMax = currentFrame.mMax;
            int aMin = currentFrame.aMin;
            int aMax = currentFrame.aMax;
            int sMin = currentFrame.sMin;
            int sMax = currentFrame.sMax;
            for (WorkflowStep step : workflow.steps) {
                if (xMin > xMax || mMin > mMax || aMin > aMax || sMin > sMax) {
                    throw new IllegalStateException();
                }

                if (step.extractor == Extractor.X) {
                    int xMinNext = xMin;
                    int xMaxNext = xMax;
                    if (step.condition == Condition.LT) {
                        xMaxNext = Math.min(step.expectedValue - 1, xMaxNext);
                    } else if (step.condition == Condition.GT) {
                        xMinNext = Math.max(step.expectedValue + 1, xMinNext);
                    }

                    if (xMinNext <= xMaxNext) {
                        queue.add(new Frame(step.nextWorkflowName, xMinNext, xMaxNext, mMin, mMax, aMin, aMax, sMin, sMax));
                    }
                } else if (step.extractor == Extractor.M) {
                    int mMinNext = mMin;
                    int mMaxNext = mMax;
                    if (step.condition == Condition.LT) {
                        mMaxNext = Math.min(step.expectedValue - 1, mMaxNext);
                    } else if (step.condition == Condition.GT) {
                        mMinNext = Math.max(step.expectedValue + 1, mMinNext);
                    }

                    if (mMinNext <= mMaxNext) {
                        queue.add(new Frame(step.nextWorkflowName, xMin, xMax, mMinNext, mMaxNext, aMin, aMax, sMin, sMax));
                    }
                } else if (step.extractor == Extractor.A) {
                    int aMinNext = aMin;
                    int aMaxNext = aMax;
                    if (step.condition == Condition.LT) {
                        aMaxNext = Math.min(step.expectedValue - 1, aMaxNext);
                    } else if (step.condition == Condition.GT) {
                        aMinNext = Math.max(step.expectedValue + 1, aMinNext);
                    }

                    if (aMinNext <= aMaxNext) {
                        queue.add(new Frame(step.nextWorkflowName, xMin, xMax, mMin, mMax, aMinNext, aMaxNext, sMin, sMax));
                    }
                } else if (step.extractor == Extractor.S) {
                    int sMinNext = sMin;
                    int sMaxNext = sMax;
                    if (step.condition == Condition.LT) {
                        sMaxNext = Math.min(step.expectedValue - 1, sMaxNext);
                    } else if (step.condition == Condition.GT) {
                        sMinNext = Math.max(step.expectedValue + 1, sMinNext);
                    }

                    if (sMinNext <= sMaxNext) {
                        queue.add(new Frame(step.nextWorkflowName, xMin, xMax, mMin, mMax, aMin, aMax, sMinNext, sMaxNext));
                    }
                } else if (step.condition == Condition.ALWAYS) {
                    queue.add(new Frame(step.nextWorkflowName, xMin, xMax, mMin, mMax, aMin, aMax, sMin, sMax));
                    break;
                } else {
                    throw new IllegalStateException();
                }

                if (step.extractor == Extractor.X) {
                    if (step.condition == Condition.LT) {
                        xMin = Math.max(step.expectedValue, xMin);
                    } else if (step.condition == Condition.GT) {
                        xMax = Math.min(step.expectedValue, xMax);
                    }
                } else if (step.extractor == Extractor.M) {
                    if (step.condition == Condition.LT) {
                        mMin = Math.max(step.expectedValue, mMin);
                    } else if (step.condition == Condition.GT) {
                        mMax = Math.min(step.expectedValue, mMax);
                    }
                } else if (step.extractor == Extractor.A) {
                    if (step.condition == Condition.LT) {
                        aMin = Math.max(step.expectedValue, aMin);
                    } else if (step.condition == Condition.GT) {
                        aMax = Math.min(step.expectedValue, aMax);
                    }
                } else if (step.extractor == Extractor.S) {
                    if (step.condition == Condition.LT) {
                        sMin = Math.max(step.expectedValue, sMin);
                    } else if (step.condition == Condition.GT) {
                        sMax = Math.min(step.expectedValue, sMax);
                    }
                } else {
                    throw new IllegalStateException();
                }
            }
        }

        long count = 0;
        for (Frame frame : acceptedFrames) {
            count += (long) (frame.xMax - frame.xMin + 1)
                * (frame.mMax - frame.mMin + 1)
                * (frame.aMax - frame.aMin + 1)
                * (frame.sMax - frame.sMin + 1);
        }

        System.out.println("Part 2: " + count);
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

    private static class Frame {

        final String workflowName;

        final int xMin;
        final int xMax;

        final int mMin;
        final int mMax;

        final int aMin;
        final int aMax;

        final int sMin;
        final int sMax;

        Frame(String workflowName, int xMin, int xMax, int mMin, int mMax, int aMin, int aMax, int sMin, int sMax) {
            this.workflowName = workflowName;
            this.xMin = xMin;
            this.xMax = xMax;
            this.mMin = mMin;
            this.mMax = mMax;
            this.aMin = aMin;
            this.aMax = aMax;
            this.sMin = sMin;
            this.sMax = sMax;
        }
    }
}
