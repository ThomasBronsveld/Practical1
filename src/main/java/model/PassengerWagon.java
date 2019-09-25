package model;

/**
 * @Project $(PROJECT_NAME)
 * @Author Thomas Bronsveld <Thomas.Bronsveld@hva.nl>
 */
public class PassengerWagon extends Wagon {

    private int numberOfSeats;
    public PassengerWagon(int wagonId, int numberOfSeats) {
        super(wagonId);
        this.numberOfSeats = numberOfSeats;
    }
    @Override
    public int getNumberOfWagonsAttached() {

        Wagon current = this;
        int count = 0;

        while(current.hasNextWagon()){
            count++;
            current = current.getNextWagon();
        }
        return count;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }
}
