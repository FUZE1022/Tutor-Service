package Util;

import java.io.*;

public final class Serializer {
    private Serializer() {}
    public static void serializeToFile(Object object, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(object);
        }
    }
    public static Object deserializeFromFile(String filePath) throws IOException, ClassNotFoundException{
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
           return ois.readObject();
        }
    }
}
