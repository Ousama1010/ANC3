package sokoban.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import java.util.HashSet;
import java.util.Set;

public class Cell {
    private final SetProperty<ObjectGame> objects = new SimpleSetProperty<>(FXCollections.observableSet());

    Cell(){
      objects.add(new Ground());
    }

    public ObservableSet<ObjectGame> getObjects() {
        return objects.get();
    }

    public void addObject(ObjectGame object) {
        if(object instanceof Ground ||  object instanceof Wall ){
            removeObject();
        }
        //if(object instanceof Player){
        //  objects.add(new Ground());
        //}
        if (Rules.canAddObject(objects, object)) {
            objects.add(object);
        }
    }

    public void removeObject() {
        Set<ObjectGame> objectsToRemove = new HashSet<>(objects);
        for (ObjectGame objectGame : objectsToRemove) {
            objects.remove(objectGame);
        }
    }
    public boolean containsObject(ObjectGame object) {
        for (ObjectGame containedObject : objects.get()) {
            if (containedObject.equals(object)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        objects.clear();
    }
    public int getSizeOfCell(){
        return objects.size();
    }
    public boolean isEmpty() {
        return objects.size() == 1 && objects.contains(new Ground());
    }


}
