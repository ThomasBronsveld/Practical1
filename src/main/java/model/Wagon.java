package model;

public abstract class Wagon {
    private int wagonId;
    private Wagon previousWagon;
    private Wagon nextWagon;

    public Wagon(int wagonId) {
        this.wagonId = wagonId;
    }

    /**
     * Getter that finds the last wagon of the row which is attack to this wagon.
     * If there are no attached wagon, this wagon will be returned
     */
    public Wagon getLastWagonAttached() {
        // find the last wagon of the row of wagons attached to this wagon
        // if no wagons are attached return this wagon
        Wagon current = this;

        while (current.hasNextWagon()) {
            current = current.nextWagon;
        }
        return current;
    }

    /**
     * Setter for this wagon to be the previous wagon of the next wagon
     *
     * @param nextWagon the selected wagon to be set.
     */
    public void setNextWagon(Wagon nextWagon) {

        this.nextWagon = nextWagon;

        if (nextWagon != null) {
            nextWagon.previousWagon = this;
        }
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public int getWagonId() {
        return wagonId;
    }

    public int getNumberOfWagonsAttached() {

        Wagon current = this;
        int count = 0;

        while (current.hasNextWagon()) {
            count++;
            current = current.nextWagon;
        }
        return count;
    }

    public boolean hasNextWagon() {
        return !(nextWagon == null);
    }

    public boolean hasPreviousWagon() {
        return !(previousWagon == null);
    }

    @Override
    public String toString() {
        return String.format("[Wagon %d]", wagonId);
    }
}
