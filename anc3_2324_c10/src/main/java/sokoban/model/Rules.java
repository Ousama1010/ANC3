package sokoban.model;

import javafx.beans.property.SetProperty;

public class Rules {
    public static boolean canAddObject(SetProperty<ObjectGame> liste, ObjectGame object) {
        if(object instanceof Box){
            for (ObjectGame objectGame : liste) {
                if(objectGame instanceof Player){
                    return false;
                }
            }
        }else if(object instanceof Ground){
            for (ObjectGame objectGame : liste) {

            }
        }else if(object instanceof Wall){
            for (ObjectGame objectGame : liste) {

            }
        }else if(object instanceof Target){
            for (ObjectGame objectGame : liste) {
                if(objectGame instanceof Wall){
                    return false;
                }
            }
        }else if(object instanceof Player){
            for (ObjectGame objectGame : liste) {

            }
        }

        return true;
    }
}
