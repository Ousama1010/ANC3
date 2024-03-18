package sokoban.model;

import java.util.Objects;

public class Target extends ObjectGame {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }
    @Override
    public String toString() {
        return ".";
    }
}
