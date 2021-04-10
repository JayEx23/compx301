//
// Names:   Jesse Reyneke-Barnard   ,   Eugene Chew
// IDs:     1351388                 ,   1351553
//

//
// Replaceable minheap interface, usable for generic type
//
public interface IMinHeap<T> {
    // These public method signatures must be implemented in any minheap
    // which implements IMinHeap
    public void insert(T value);

    public void remove();

    public void replace(T value);

    public T peek();

    public void load(T[] values);

    public void reheap();
}