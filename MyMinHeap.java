public class MyMinHeap {

    private int _size;
    private String[] _minHeap;

    // The constructor for minHeap object
    public MyMinHeap(int size) {
        if (size < 1) {
            System.out.println("Please provide a heap size greater than 1");
            return;
        }
        _size = size;
        _minHeap = new String[_size];
    }
}