package org.example.finalproject.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ObjectPlus implements Serializable {
    @Serial
    private static final long serialVersionUID = -5252410712943067953L;
    private static Map<Class<? extends ObjectPlus>, List<ObjectPlus>> allExtents = new HashMap<>();

    public ObjectPlus() {
        Class<? extends ObjectPlus> theClass = this.getClass();
        allExtents.computeIfAbsent(theClass, k -> new ArrayList<>()).add(this);
    }

    public static void writeExtends(ObjectOutputStream stream) throws IOException {
        stream.writeObject(allExtents);
    }

    public static void readExtents(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        allExtents = (Map<Class<? extends ObjectPlus>, List<ObjectPlus>>) stream.readObject();
    }

    public static void showExtent(Class<? extends ObjectPlus> theClass) throws Exception {
        List<ObjectPlus> extent = null;

        if (allExtents.containsKey(theClass)) {
            extent = allExtents.get(theClass);
        } else {
            throw new Exception("Unknown class " + theClass);
        }

        System.out.println("Extent of the class: " + theClass.getSimpleName());

        for (Object obj : extent) {
            System.out.println(obj);
        }
    }

    public static <T> Iterable<T> getExtent(Class<T> type) throws ClassNotFoundException {
        if (allExtents.containsKey(type)) {
            return (Iterable<T>) allExtents.get(type);
        }

        throw new ClassNotFoundException(String.format("%s. Stored extents: %s", type.toString(), allExtents.keySet()));
    }

    public void destroy() {
        List<ObjectPlus> extent = allExtents.get(this.getClass());
        if (extent != null) {
            extent.remove(this);
        }
    }
}
