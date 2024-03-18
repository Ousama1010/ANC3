package sokoban.model;

public abstract class ObjectGame {
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ObjectGame that = (ObjectGame) other;
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
