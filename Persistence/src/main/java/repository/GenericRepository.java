package repository;

public interface GenericRepository<ID,E>{
    void add (E element);
    void update (ID id, E element);
    void delete (ID id);
    Iterable<E> getAll();
}
