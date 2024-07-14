package telran.io;

public interface Persistable {
 void save(String filePathStr);
 void restore(String filePathStr);
}
