public class MyMergeMinHeap implements IMinHeap<String[]> {

    private int _size;
    private String[][] _minHeap;

    // The constructor for minHeap object
    public MyMergeMinHeap(int size) {
        if (size < 1) { // Use default size
            _minHeap = new String[32][2];
            _size = 0;
        } else { // Use specified value
            _minHeap = new String[size][2];
            _size = 0;
        }

    }

    // Insert into heap
    public void insert(String[] value) {
        if (_size == _minHeap.length) {
            System.out.println("Warning: minheap is full -- could not insert value!");
        } else {
            _size++;
            _minHeap[_size - 1][0] = value[0];
            _minHeap[_size - 1][1] = value[1];
            upheap();
        }
    }

    // Remove from heap
    public void remove() {
        if (_size > 0) {
            int root = 0;
            int tail = _size - 1;

            swap(root, tail);
            _size--;
            downheap(root);
        } else {
            System.out.println("Warning: heap is empty -- could not complete remove operation");
        }
    }

    // peek
    public String peek() {
        return _minHeap[0][0];
    }

    // peek source
    public String peekSource() {
        return _minHeap[0][1];
    }

    // load values into heap without regard for heap order
    // args - values: a string collection to load into our heap, along with names of sources for
    // where these values came from
    public void load(String[][] values) {
        // Check if input values are in correct format
        if (values[0].length < 2 && values[0].length > 2) {
            System.out.println("Warning: merge minheap can only accept format String[x][2]");
            return;
        }
        _size = 0;
        // Load values
        for (int sourceIndex = 0; sourceIndex < 2; sourceIndex ++) {
            for (int i = 0; i < _minHeap.length; i++) {
                if (i >= values.length) {
                    break;
                }
                _minHeap[i][sourceIndex] = values[i][sourceIndex];
                _size++;
            }
        }
        // Warn user if input values could not all fit in minheap
        if (values.length > _minHeap.length) {
            System.out.println("Warning: minheap overflow -- could only load " + _size + " values!");
        }
    }

    // Replace root in heap with new value and maintain heap order
    // args - value: the new value
    public void replace(String[] value) {
        _minHeap[0][0] = value[0];
        _minHeap[0][1] = value[1];
        if (value[0] == null) {
            remove();
        } else { downheap(0); }
    }

    // Put heap array back into heap order
    public void reheap() {
        int i = lastParentIndex();
        while (i >= 0) {
            downheap(i);
            i--;
        }
    }

    // Get size
    public int getSize() {
        return _size;
    }

    // Reset minheap size
    public void reset() {
        for (int i = 0; i < _minHeap.length; i++) {
            if (_minHeap[i][0] != null) {
                swap(_size, i);
                _size ++;
            }
        }
    }

    // Upheap array
    private void upheap() {
        // Iterate from bottom to top
        int i = _size - 1;
        while (i > 0) {
            int parentIndex = parentIndex(i);
            String current = _minHeap[i][0];
            String parent = _minHeap[parentIndex][0];
            if (current.compareTo(parent) < 0) { // If current is less than parent
                swap(parentIndex, i);
                i = parentIndex;
            } else {
                return; // Upheap complete
            }
        }
    }

    // Downheap from provided index
    private void downheap(int current) {
        // Iterate from the given index
        if (_size < 2) {
            return;
        }
        while (current <= lastParentIndex()) {
            int leftChildIndex = leftChildIndex(current);
            int rightChildIndex = rightChildIndex(current);
            int replaceIndex = leftChildIndex;

            if (_size > rightChildIndex(current)) { // We know that there is a child, check right child exists
                if (_minHeap[rightChildIndex][0].compareTo(_minHeap[leftChildIndex][0]) < 0) {
                    replaceIndex = rightChildIndex;
                }
            }

            if (_minHeap[current][0].compareTo(_minHeap[replaceIndex][0]) > 0) {
                swap(current, replaceIndex);
                current = replaceIndex;
            } else {
                return; // Downheap complete
            }
        }
    }

    // Swaps the position of 2 index/items in the array
    private void swap(int j, int k) {
        String tempVal = _minHeap[j][0];
        String tempSource = _minHeap[j][1];

        _minHeap[j][0] = _minHeap[k][0];
        _minHeap[j][1] = _minHeap[k][1];

        _minHeap[k][0] = tempVal;
        _minHeap[k][1] = tempSource;
    }

    // Get parent index
    private int parentIndex(int current) {
        return (current - 1) / 2; // (current-1) because our heap array's first item starts at index 0
    }

    // Get left child index
    private int leftChildIndex(int current) {
        return ((current + 1) * 2) - 1; // (current+1*2)-1 because our heap array's first item starts at index 0
    }

    // Get right child index
    private int rightChildIndex(int current) {
        return ((current + 1) * 2); // (current+1)*2 because our heap array's first item starts at index 0
    }

    // Get index of last parent item in heap
    private int lastParentIndex() {
        return parentIndex(_size - 1); // (_size-1) instead of _size because our heap starts at index=0
    }
}