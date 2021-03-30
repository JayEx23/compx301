import java.io.BufferedReader;
import java.io.InputStreamReader;

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
        System.out.println(heapSize); // !Debug print statement
        read();
    }

    //
    // Read standard input
    //
    public static void read() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            // Read until end of standard input
            // e.g. `cat test.txt | java CreatRuns 10`
            // should read all lines from test.txt and then stop
            String line = new String(reader.readLine());
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}