import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        MyMinHeap mh;
        try {
            System.out.println("Please input heap size");
            String size = reader.nextLine();
            mh = new MyMinHeap(Integer.parseInt(size));
            mh.print();
        } catch (Exception e) {
            reader.close();
            return;
        }
        while (true) {
            try {
                String line = new String(reader.nextLine());
                String[] parts = line.split(",");
                String instruction = new String(parts[0]);

                if (instruction.equals("i")) { // i, <value>: insert a value
                    String value = new String(parts[1]);
                    mh.insert(value);
                } else if (instruction.equals("rem")) { // rem: remove
                    mh.remove();
                } else if (instruction.equals("rep")) { // rep <value>: replace with value
                    String value = new String(parts[1]);
                    mh.replace(value);
                } else if (instruction.equals("load")) { // load, <val1>, <val2>, <val3>, ...: load values
                    String values[] = new String[parts.length-1];
                    for (int i = 0; i < values.length; i++) {
                        values[i] = parts[i+1];
                    }
                    mh.load(values);
                } else if (instruction.equals("reh")) { // reh: reheap
                    mh.reheap();
                } else if (instruction.equals("p")) { // p: peek
                    System.out.println(mh.peek());
                } else if (instruction.equals("q")) { // q: quit test program
                    reader.close();
                    return;
                }
                mh.print();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}