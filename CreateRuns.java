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
        System.out.println(heapSize); // !Debug print statement
        load(heapSize);
    }

    //
    // Read standard input
    //
    public static void load(int heapSize) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        MyMinHeap selectionSorter = new MyMinHeap(heapSize);
        try {
            // Read until end of standard input
            // e.g. `cat test.txt | java CreatRuns 10`
            // should read all lines from test.txt and then stop
            String line = new String(reader.readLine()); // Get first line
            int counter = 0;
            while (line != null) {
                if (counter <= heapSize) {
                    // Load initial values into heap
                    selectionSorter.insert(line);
                    counter ++;
                } else {
                    selectinSort(selectionSorter, counter);         
                }
                line = reader.readLine(); // Get next line
            }
            reader.close();
            writer.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public static void selectinSort(MyMinHeap selectionSorter, int counter, String line) {
        // Start creating a run
        String peek = new String(selectionSorter.peek());
        String prevPeek = new String(peek);
        while (peek != null && counter > 0) {
            if (peek.compareTo(prevPeek) >= 0) {
                // Add to run
                selectionSorter.replace(line);
                line = reader.readLine();
                writer.write(peek);
            } else {
                // Save for a next run
                selectionSorter.remove();
                counter --;
                if (counter == 0) {
                    selectionSorter.reheap();
                }
            }
            prevPeek = peek;
            peek = selectionSorter.peek();
        }
    }
}