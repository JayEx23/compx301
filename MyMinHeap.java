public class MyMinHeap {

    private int _size;
    private String[] _minHeap;

    // The constructor for minHeap object
    public MyMinHeap(int size) {
        if (size < 1) {
            _size = 32;
            _minHeap = new String[_size];
        } else {
            _minHeap = new String[size];
            _size = 0;
        }

    }

    // Insert into heap
    public void insert(String value) {
        if (_size == _minHeap.length) {
            System.out.println("Warning: minheap is full -- could not insert value!");
        } else {
            _size++;
            _minHeap[_size - 1] = value;
            upheap();
        }
    }

    // Remove from heap
    public void remove() {
        if (_size > 0) {
            int root = 0;
            int tail = _size-1;

            swap(root, tail);
            _size--;
            downheap(root);
        } else {
            System.out.println("Warning: heap is empty -- could not complete remove operation");
        }
    }

    // peek
    public String peek() {
        return _minHeap[0];
    }

    // load values into heap without regard for heap order
    // args - values: a string collection to load into our heap
    public void load(String[] values) {
        _size = 0;
        // Load values
        for (int i = 0; i < _minHeap.length; i++) {
            if (i >= values.length) {
                break;
            }
            _minHeap[i] = values[i];
            _size ++;
        }
        if (values.length > _minHeap.length) {
            System.out.println("Warning: minheap overflow -- could only load " + _size + " values!");
        }
    }

    // Replace root in heap with new value and maintain heap order
    // args - value: the new value
    public void replace(String value) {
        _minHeap[0] = value;
        downheap(0);
    }

    // Put heap array back into heap order
    public void reheap() {
        _size = _minHeap.length; // Reset size to be length of space available in heap
        int i = lastParentIndex();
        while (i >= 0) {
            downheap(i);
            i--;
        }
    }

    // Print heap array in debug format
    public void print() {
        System.out.println("Size: " + _size);
        for (int i = 0; i < _minHeap.length; i++) {
            if (i == _size) {
                System.out.println("---size---"); // Visually display where size variable is in effect.
            }            
            // Print:
            System.out.print("[" + i + "]\t"); // index
            System.out.print("[" + (isLeaf(i) ? "L" : "P") + "]\t"); // leaf or parent
            System.out.println(_minHeap[i]); // item
        }
    }

    // Upheap array
    private void upheap() {
        // Iterate from bottom to top
        int i = _size-1;
        while (i > 0) {
            int parentIndex = parentIndex(i);
            String current = new String(_minHeap[i]);
            String parent = new String(_minHeap[parentIndex]);
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
                if (_minHeap[rightChildIndex].compareTo(_minHeap[leftChildIndex]) < 0) {
                    replaceIndex = rightChildIndex;
                }
            }

            if (_minHeap[current].compareTo(_minHeap[replaceIndex]) > 0) {
                swap(current, replaceIndex);
                current = replaceIndex;
            } else {
                return; // Downheap complete
            }
        }
    }

    // Swaps the position of 2 index/items in the array
    private void swap(int j, int k) {
        String temp = new String(_minHeap[j]);
        _minHeap[j] = _minHeap[k];
        _minHeap[k] = temp;
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

    // checks if the current index is a leaf node (i.e no children)
    private boolean isLeaf(int current) {
        if (current > lastParentIndex() && current <= _size) {
            return true;
        }
        return false;
    }

    // Get index of last parent item in heap
    private int lastParentIndex() {
        return parentIndex(_size-1); // (_size-1) instead of _size because our heap starts at index=0
    }
}
