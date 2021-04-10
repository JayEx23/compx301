//
// Names:   Jesse Reyneke-Barnard   ,   Eugene Chew
// IDs:     1351388                 ,   1351553
//

//
// This is a string minheap class
//
public class MyMinHeap implements IMinHeap<String> {
    // Declare prvate fields
    private int _size;
    private String[] _minHeap;

    //
    // The constructor for minHeap object
    //
    public MyMinHeap(int size) {
        if (size < 1) { // Use default size
            _minHeap = new String[32];
            _size = 0;
        } else { // Use specified value
            _minHeap = new String[size];
            _size = 0;
        }

    }

    //
    // Insert into heap
    // Takes in value: string value to insert
    //
    public void insert(String value) {
        if (_size == _minHeap.length) {
            System.out.println("Warning: minheap is full -- could not insert value!");
        } else {
            _size++;
            _minHeap[_size - 1] = value;
            upheap();
        }
    }

    //
    // Remove root item in heap
    //
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

    //
    // Return string root item in minheap
    //
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
            _size++;
        }
        // Warn user if input values could not all fit in minheap
        if (values.length > _minHeap.length) {
            System.out.println("Warning: minheap overflow -- could only load " + _size + " values!");
        }
    }

    // Replace root in heap with new value and maintain heap order
    // args - value: the new string value
    public void replace(String value) {
        _minHeap[0] = value;
        if (value == null) {
            // Handle null values by removing them first (moving to back of heap then
            // decreasing size)
            // before downheaping, as downheap operation cannot handle null values
            remove();
        } else {
            downheap(0);
        }
    }

    //
    // Put heap array back into heap order
    //
    public void reheap() {
        int i = lastParentIndex();
        while (i >= 0) {
            downheap(i);
            i--;
        }
    }

    //
    // Return heap artificial size
    //
    public int getSize() {
        return _size;
    }

    //
    // Reset minheap size
    //
    public void reset() {
        for (int i = 0; i < _minHeap.length; i++) {
            if (_minHeap[i] != null) {
                swap(_size, i);
                _size++;
            }
        }
    }

    //
    // Print heap array in debug format for testing purposes
    //
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

    //
    // Upheap array
    //
    private void upheap() {
        // Iterate from bottom to top
        int i = _size - 1;
        while (i > 0) {
            int parentIndex = parentIndex(i);
            String current = _minHeap[i];
            String parent = _minHeap[parentIndex];
            if (current.compareTo(parent) < 0) { // If current is less than parent
                swap(parentIndex, i);
                i = parentIndex;
            } else {
                return; // Upheap complete
            }
        }
    }

    //
    // Downheap from provided index
    //
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

    //
    // Swaps the position of 2 index/items in the array
    //
    private void swap(int j, int k) {
        String temp = _minHeap[j];
        _minHeap[j] = _minHeap[k];
        _minHeap[k] = temp;
    }

    //
    // Get parent index
    //
    private int parentIndex(int current) {
        return (current - 1) / 2; // (current-1) because our heap array's first item starts at index 0
    }

    //
    // Get left child index
    //
    private int leftChildIndex(int current) {
        return ((current + 1) * 2) - 1; // (current+1*2)-1 because our heap array's first item starts at index 0
    }

    //
    // Get right child index
    //
    private int rightChildIndex(int current) {
        return ((current + 1) * 2); // (current+1)*2 because our heap array's first item starts at index 0
    }

    //
    // checks if the current index is a leaf node (i.e no children)
    //
    private boolean isLeaf(int current) {
        if (current > lastParentIndex() && current <= _size) {
            return true;
        }
        return false;
    }

    //
    // Get index of last parent item in heap
    //
    private int lastParentIndex() {
        return parentIndex(_size - 1); // (_size-1) instead of _size because our heap starts at index=0
    }
}
