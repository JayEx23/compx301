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

    // Upheap array
    private void upheap() {
        // Iterate from bottom to top
        int i = _size;
        while (i > 1) {
            int parentIndex = (i-1)/2; // "i-2" because our heap array's first item starts at index 0
            String current = new String(_minHeap[i]);
            String parent = new String(_minHeap[parentIndex]);
            if (current.compareTo(parent) < 0) { // If current is less than parent
                // Perform swap
                _minHeap[parentIndex] = current;
                _minHeap[i] = parent;
            } else {
                return; // Upheap complete
            }
        }
    }

    // Insert into heap
    public void insert(String value) {
        _size ++;
        _minHeap[_size-1] = value;
        upheap();
    }

    // Print heap array in raw format
    public void print() {
        for (int i = 0; i < _size; i++) {
            System.out.println(_minHeap[i]);
        }
    }
}
