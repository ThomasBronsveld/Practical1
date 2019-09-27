import model.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TrainsTest {

    private ArrayList<PassengerWagon> pwList;
    private ArrayList<FreightWagon> fwList;
    private Train firstPassengerTrain;
    private Train secondPassengerTrain;
    private Train firstFreightTrain;
    private Train secondFreightTrain;
    private Train thirdFreightTrain;

   @BeforeEach
    private void makeListOfPassengerWagons() {
        pwList = new ArrayList<>();
        pwList.add(new PassengerWagon(3, 100));
        pwList.add(new PassengerWagon(24, 100));
        pwList.add(new PassengerWagon(17, 140));
        pwList.add(new PassengerWagon(32, 150));
        pwList.add(new PassengerWagon(38, 140));
        pwList.add(new PassengerWagon(11, 100));
    }
    @BeforeEach
    private void makeListOfFreightWagons(){
        fwList = new ArrayList<>();
        fwList.add(new FreightWagon(10, 1000));
        fwList.add(new FreightWagon(30, 3000));
        fwList.add(new FreightWagon(40, 2000));
        fwList.add(new FreightWagon(50, 40000));
        fwList.add(new FreightWagon(65, 90000));
        fwList.add(new FreightWagon(33, 33000));
        fwList.add(new FreightWagon(99, 6000));
        fwList.add(new FreightWagon(101, 11000));
    }

    private void makeTrains() {
        Locomotive thomas = new Locomotive(2453, 7);
        Locomotive gordon = new Locomotive(5277, 8);
        Locomotive emily = new Locomotive(4383, 11);
        Locomotive rebecca = new Locomotive(2275, 4);

        firstPassengerTrain = new Train(thomas, "Amsterdam", "Haarlem");

        for (Wagon w : pwList) {
            Shunter.hookWagonOnTrainRear(firstPassengerTrain, w);
        }
        secondPassengerTrain = new Train(gordon, "Joburg", "Cape Town");

        firstFreightTrain = new Train(emily, "Rotterdam", "Antwerpen");
        for (Wagon w : fwList){
            Shunter.hookWagonOnTrainRear(firstFreightTrain, w);
        }

        secondFreightTrain = new Train(rebecca, "Antwerpen", "Rotterdam");
        thirdFreightTrain = new Train(rebecca, "Antwerper", "Rotterdam");
    }

    @Test
    public void checkNumberOfWagonsOnTrain() {
        makeTrains();
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have 6 wagons");
        System.out.println(firstPassengerTrain);
    }

    @Test
    public void checkNumberOfSeatsOnTrain() {
        makeTrains();
        assertEquals( 730, firstPassengerTrain.getNumberOfSeats());
        System.out.println(firstPassengerTrain);
    }

    @Test
    public void checkPositionOfWagons() {
        makeTrains();
        int position = 1;
        for (PassengerWagon pw : pwList) {
            assertEquals( position, firstPassengerTrain.getPositionOfWagon(pw.getWagonId()));
            position++;
        }

    }

    @Test
    public void checkHookOneWagonOnTrainFront() {
        makeTrains();
        Shunter.hookWagonOnTrainFront(firstPassengerTrain, new PassengerWagon(21, 140));
        assertEquals( 7, firstPassengerTrain.getNumberOfWagons(), "Train should have 7 wagons");
        assertEquals( 1, firstPassengerTrain.getPositionOfWagon(21));

    }

    @Test
    public void checkHookRowWagonsOnTrainRearFalse() {
        makeTrains();
        Wagon w1 = new PassengerWagon(11, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(43, 140));
        Shunter.hookWagonOnTrainRear(firstPassengerTrain, w1);
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have still have 6 wagons, capacity reached");
        assertEquals( -1, firstPassengerTrain.getPositionOfWagon(43));
    }

    @Test
    public void checkMoveOneWagon() {
        makeTrains();
        Shunter.moveOneWagon(firstPassengerTrain, secondPassengerTrain, pwList.get(3));
        assertEquals(5, firstPassengerTrain.getNumberOfWagons(), "Train should have 5 wagons");
        assertEquals( -1, firstPassengerTrain.getPositionOfWagon(32));
        assertEquals(1, secondPassengerTrain.getNumberOfWagons(), "Train should have 1 wagon");
        assertEquals( 1, secondPassengerTrain.getPositionOfWagon(32));

    }

    @Test
    public void checkMoveRowOfWagons() {
        makeTrains();
        Wagon w1 = new PassengerWagon(11, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(43, 140));
        Shunter.hookWagonOnTrainRear(secondPassengerTrain, w1);
        Shunter.moveAllFromTrain(firstPassengerTrain, secondPassengerTrain, pwList.get(2));
        assertEquals(2, firstPassengerTrain.getNumberOfWagons(), "Train should have 2 wagons");
        assertEquals( 2, firstPassengerTrain.getPositionOfWagon(24));
        assertEquals(6, secondPassengerTrain.getNumberOfWagons(), "Train should have 6 wagons");
        assertEquals( 4, secondPassengerTrain.getPositionOfWagon(32));
    }

    @Test
    public void checkWeightOnTrain(){
        makeTrains();
        assertEquals(186000, firstFreightTrain.getTotalMaxWeight(), "The weight should be 186000");
    }

    @Test
    public void moveOneWeightWagon(){
       makeTrains();
       Shunter.moveOneWagon(firstFreightTrain, secondFreightTrain, fwList.get(2));
       assertEquals(184000, firstFreightTrain.getTotalMaxWeight(), "The weight should be 184000");
       assertEquals(2000, secondFreightTrain.getTotalMaxWeight(), "The weight should be 2000");
    }

    @Test()
    public void trainExceptions(){
         makeTrains();
         assertThrows(IndexOutOfBoundsException.class, () -> {
             thirdFreightTrain.getWagonOnPosition(2);
         });
         assertThrows(IndexOutOfBoundsException.class, () -> {
             secondFreightTrain.getWagonOnPosition(700);
         });

    }

    @Test
    public void falseChecks(){
       makeTrains();
       assertFalse(Shunter.hookWagonOnWagon(fwList.get(2), pwList.get(2)));
       assertFalse(Shunter.hookWagonOnTrainRear(firstFreightTrain, pwList.get(2)));
//        assertFalse(Shunter.hookWagonOnTrainRear(firstFreightTrain, pwList.get(2)));

    }
}
