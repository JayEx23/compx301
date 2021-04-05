import java.io.BufferedReader;
import java.io.BufferedWriter;
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

        for (int i = 0; i < 26; i++) {
            

        }

        load(heapSize);
    }

    //
    // Read standard input
    //
    public static void load(int heapSize) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        MyMinHeap minheap = new MyMinHeap(heapSize);
        String[] lines = new String[heapSize];
        int heapCount = 0;
        try {
            // Load initial lines into heap
            for (int i = 0; i < heapSize; i++) {
                String line = reader.readLine();
                if (line != null) {
                    lines[i] = line;
                    heapCount ++;
                }
                else { break; }
            }
            minheap.load(lines);
            minheap.reheap();
            
            String line = minheap.peek();
            String prevLine = line;
            while (line != "{EOF}") {
                if (line.compareTo(prevLine) >= 0) {
                    writer.write(line + "\n");
                    String newLine = reader.readLine();
                    if (newLine != null) {
                        minheap.replace(newLine);
                    } else {
                        minheap.replace("{EOF}");
                    }
                } else {
                    minheap.remove();
                    heapCount --;
                }

                if (heapCount < 1) {
                    writer.write("{RUN}\n");
                    heapCount = heapSize;
                    minheap.resetSize();
                    minheap.reheap();
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