//
// Names:   Jesse Reyneke-Barnard   ,   Eugene Chew
// IDs:     1351388                 ,   1351553
//

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

//
// CreateRuns classs
//
public class CreateRuns {
    //
    // Main method: program entry point
    // Takes in args: command line arguments
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
        } catch (Exception e) {
            System.err.println(e);
            System.err.println(msgUsage);
            e.printStackTrace();
            return;
        }

        load(heapSize);
    }

    //
    // Load standard input, perform replacement selection and output to stnadard
    // output. Takes in heapsize: int number specifying size of heap
    //
    public static void load(int heapSize) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        MyMinHeap minheap = new MyMinHeap(heapSize);
        String[] lines = new String[heapSize];
        try {
            // store initial lines for heap load operation
            for (int i = 0; i < heapSize; i++) {
                String line = reader.readLine();
                if (line != null) {
                    lines[i] = line;
                } else {
                    break;
                }
            }
            // Load initial lines into heap and reheap
            minheap.load(lines);
            minheap.reheap();

            String line = minheap.peek();
            String prevLine = line;
            while (line != null) { // while there are still lines to read
                // checks to see if the current line is greater than the previous
                // line
                if (line.compareTo(prevLine) >= 0) {
                    writer.write(line + "\n");
                    String newLine = reader.readLine();
                    minheap.replace(newLine);

                    // Get values for next loop
                    prevLine = line;
                    line = minheap.peek();
                } else {
                    minheap.remove();
                    line = minheap.peek();
                }

                if (minheap.getSize() == 0) { // check if there are still items in the minHeap
                    writer.write("{RUN}\n"); // Write explicit run separator
                    minheap.reset();
                    minheap.reheap();

                    line = minheap.peek();
                    prevLine = line;
                }
            }

            writer.flush();
            reader.close();
            writer.close();
        } catch (Exception e) {
            // print error
            System.err.println(e);
            e.printStackTrace();
        }
    }
}