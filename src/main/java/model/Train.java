package model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Train implements Iterable<Wagon>{
    private Locomotive engine;
    private Wagon firstWagon;
    private String destination;
    private String origin;
    private int numberOfWagons;

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    public void setFirstWagon(Wagon firstWagon) {
        this.firstWagon = firstWagon;
    }

    public void resetNumberOfWagons() {
       /*  when wagons are hooked to or detached from a train,
         the number of wagons of the train should be reset
         this method does the calculation */
        if (this.hasNoWagons()) {
            this.numberOfWagons = 0;
        }
        this.numberOfWagons = this.getFirstWagon().getNumberOfWagonsAttached() + 1;
    }

    public int getNumberOfWagons() {
        return numberOfWagons;
    }


    /* three helper methods that are usefull in other methods */

    public boolean hasNoWagons() {
        return (firstWagon == null);
    }

    public boolean isPassengerTrain() {
        return firstWagon instanceof PassengerWagon;
    }

    public boolean isFreightTrain() {
        return firstWagon instanceof FreightWagon;
    }

    public int getPositionOfWagon(int wagonId) {
        // find a wagon on a train by id, return the position (first wagon had position 1)
        // if not found, than return -1

        int position = -1;

        if (this.firstWagon.getWagonId() == wagonId) {
            position = 1;
        } else {
            Wagon current = this.firstWagon;
            int count = 1;
            while (current.hasNextWagon()) {
                count++;
                if (current.getNextWagon().getWagonId() == wagonId) {
                    position = count;
                    break;
                } else {
                    current = current.getNextWagon();
                }
            }

        }
        return position;
    }


    public Wagon getWagonOnPosition(int position) throws IndexOutOfBoundsException {
        /* find the wagon on a given position on the train
         position of wagons start at 1 (firstWagon of train)
         use exceptions to handle a position that does not exist */

        if (this.hasNoWagons()) {
            throw new IndexOutOfBoundsException("This train has no wagons attached");
        }
        if (this.numberOfWagons < position) {
            throw new IndexOutOfBoundsException("You've given a position greater than the number of wagons connected to the train");
        }

        if (position == 1) {
            return this.firstWagon;
        }

        Wagon current = this.firstWagon;
        int count = 1;
        while (current.hasNextWagon()) {
            count++;
            if (count == position) {
                return current.getNextWagon();
            } else {
                current = current.getNextWagon();
            }

        }

        return null;
    }

    public int getNumberOfSeats() {
        /* give the total number of seats on a passenger train
         for freight trains the result should be 0 */

        int numberOfSeats = 0;

        if (this.getFirstWagon() instanceof PassengerWagon) {

            Wagon current = this.getFirstWagon();

            while (current != null) {
                PassengerWagon test = (PassengerWagon) current;
                numberOfSeats += test.getNumberOfSeats();
                current = current.getNextWagon();
            }
        }
        return numberOfSeats;
    }

    public int getTotalMaxWeight() {
        /* give the total maximum weight of a freight train
         for passenger trains the result should be 0 */

        int maxWeight = 0;
        if (this.getFirstWagon() instanceof FreightWagon) {

            Wagon current = this.getFirstWagon();

            while (current != null) {
                FreightWagon test = (FreightWagon) current;
                maxWeight += test.getMaxWeight();
                current = current.getNextWagon();
            }
        }
        return maxWeight;

    }

    public Locomotive getEngine() {
        return engine;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(engine.toString());
        Wagon next = this.getFirstWagon();
        while (next != null) {
            result.append(next.toString());
            next = next.getNextWagon();
        }
        result.append(String.format(" with %d wagons and %d seats from %s to %s", numberOfWagons, getNumberOfSeats(), origin, destination));
        return result.toString();
    }

    @Override
    public Iterator<Wagon> iterator() {
        return new Iterator<Wagon>() {

            Wagon head = firstWagon;
            @Override
            public boolean hasNext() {
                return head.hasNextWagon();
            }

            @Override
            public Wagon next() {
                head = head.getNextWagon();
                return head;
            }
        };
    }
}
