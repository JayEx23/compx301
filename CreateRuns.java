import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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

        load(heapSize);
    }

    //
    // Read standard input
    //
    public static void load(int heapSize) {
        // BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("./test.txt"));
        } catch(Exception e) {
            System.out.println(e);
            return;
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        MyMinHeap minheap = new MyMinHeap(heapSize);
        String[] lines = new String[heapSize];
        try {
            // Load initial lines into heap
            for (int i = 0; i < heapSize; i++) {
                String line = reader.readLine();
                if (line != null) {
                    lines[i] = line;
                }
                else { break; }
            }
            minheap.load(lines);
            minheap.reheap();
            
            String line = minheap.peek();
            String prevLine = line;
            while (line != null) {
                if (line.compareTo(prevLine) >= 0) {
                    writer.write(line + "\n");
                    String newLine = reader.readLine();
                    minheap.replace(newLine);
                } else {
                    minheap.remove();
                    line = minheap.peek();
                    continue;
                }

                if (minheap.getSize() == 0) {
                    writer.write("{RUN}\n");
                    minheap.reset();
                    minheap.reheap();
                    
                    line = minheap.peek();
                    prevLine = line;
                    continue;
                }

                // Get values for next loop
                prevLine = line;
                line = minheap.peek();
            }

            writer.flush();
            reader.close();
            writer.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}