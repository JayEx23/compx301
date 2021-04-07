public interface IMinHeap<T> {
    public void insert(T value);

    public void remove();

    public void replace(T value);

    public String peek();

    public void load(T[] values);

    public void reheap();
}