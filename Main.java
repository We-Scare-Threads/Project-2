//Task 4: Command Line

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a task name as a command line argument.");
            System.out.println("Example: `java main -S 1` ");
            return;
        }

        String option = args[0]; // first argument
        String task = args[1]; // first argument
        if(option.equals("-s") || option.equals("-S")) {
            switch (task) {
                case "1":
                    AccessMatrix.main(new String[]{});
                    break;
                case "2":
                    AccessList.main(new String[]{});
                    break;
                case "3":
                    CapList.main(new String[]{});
                    break;
                default:
                    System.out.println("Unknown task: " + task);
                    System.out.println("Available tasks: -S1 (Access Matrix), -S2 (Access List), -S3 (Capability List)");
            }
        }
        else {
            System.out.println("Unknown option: " + option);
            System.out.println("Available options: -S 1 (Access Matrix), -S 2 (Access List), -S 3 (Capability List)");
        }
    }
}