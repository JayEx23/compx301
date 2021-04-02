public class MyMinHeap {

    private int _size;
    private String[] _minHeap;

    // The constructor for minHeap object
    public MyMinHeap(int size) {
        if (size < 1) {
            _size = 32;
            _minHeap = new String[_size];
        } else {
            _size = size;
            _minHeap = new String[_size];
        }

    }

    // Upheap array
    private void upheap() {
        // Iterate from bottom to top
        int i = _size;
        while (i > 1) {
            int parentIndex = parentIndex(i);
            String current = new String(_minHeap[i]);
            String parent = new String(_minHeap[parentIndex]);
            if (current.compareTo(parent) < 0) { // If current is less than parent
                // Perform swap
                // _minHeap[parentIndex] = current;
                // _minHeap[i] = parent;
                swap(parentIndex, i);
            } else {
                return; // Upheap complete
            }
        }
    }

    private void downheap(int index) {

    }

    // Insert into heap
    public void insert(String value) {
        _size++;
        _minHeap[_size - 1] = value;
        upheap();
    }

    // Remove from heap
    public void remove(String value) {
        int root = 0;
        int tail = _size;

        try {
            if (_size > 0) {
                swap(root, tail);
                _size--;
            }
        } catch (Exception x) {
            System.err.println(x);
        }
    }

    // Swaps the position of 2 index/items in the array
    private void swap(int j, int k) {
        String temp = new String(_minHeap[j]);
        _minHeap[j] = _minHeap[k];
        _minHeap[k] = temp;
    // peek
    public String peek() {
        return _minHeap[0];
    }

    // load values into heap without regard for heap order
    // args - values: a string collection to load into our heap
    // returns - if all values fit in array return 0 else return
    // index that was next in line but did not fit.
    public int load(String[] values) {
        // Load values
        for (int i = 0; i < _minHeap.length; i++) {
            _minHeap[i] = values[i];
        }
        // Return status of values loaded
        if (values.length > _size) {
            return _size - 1;
        }
        return 0;
    }

    // Print heap array in raw format
    public void print() {
        System.out.println("Size: " + _size);
        for (int i = 0; i < _minHeap.length; i++) {
            System.out.println(_minHeap[i]);
        }
    }

    // Get parent index
    private int parentIndex(int current) {
        return (current - 1) * 2; // "i-2" because our heap array's first item starts at index 0
    }

    // Get left child index
    private int leftChildIndex(int current) {
        return ((current + 1) * 2) - 1;
    }

    // Get right child index
    private int rightChildIndex(int current) {
        return ((current + 1) * 2);
    }
}
