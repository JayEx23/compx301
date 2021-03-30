//
// CreateRuns class
//
public class CreateRuns {
    //
    // Main method
    //
    public static void main(String[] args) {
        String msgUsage = new String("Usage: java CreateRuns [heap-size]");
        // Process command line arguments
        if (args.length > 1) { // Check length of command line args
            System.out.println(msgUsage);
            return;
        }
        int heapSize = 0;
        try { // Check heap size command line args
            heapSize = Integer.parseInt(args[0]);
        } catch(Exception e) {
            System.out.println(e);
            System.out.println(msgUsage);
            return;
        }
        System.out.println(heapSize);
    }
}