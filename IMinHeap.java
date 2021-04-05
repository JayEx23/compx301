public interface IMinHeap {
    public void insert(String value);

    public void remove();

    public void replace(String value);

    public String peek();

    public void load(String[] values);

    public void reheap();
}
