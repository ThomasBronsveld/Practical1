package model;

public class Shunter {


    /* four helper methods than are used in other methods in this class to do checks */
    private static boolean isSuitableWagon(Train train, Wagon wagon) {
        // trains can only exist of passenger wagons or of freight wagons
        if(train.getFirstWagon() == null && train.getEngine().getMaxWagons() >= 1){
            return true;
        }

        if (train.isPassengerTrain() && wagon instanceof PassengerWagon) {
            return true;
        }

        return train.isFreightTrain() && wagon instanceof FreightWagon;
    }

    private static boolean isSuitableWagon(Wagon one, Wagon two) {
        // passenger wagons can only be hooked onto passenger wagons
        return one.getClass().equals(two.getClass());
    }

    private static boolean hasPlaceForWagons(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for a row of wagons
        return train.getEngine().getMaxWagons() >= (train.getNumberOfWagons() + wagon.getNumberOfWagonsAttached() + 1);
    }

    private static boolean hasPlaceForOneWagon(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for one wagon
        return train.getEngine().getMaxWagons() >= train.getNumberOfWagons() + 1;
    }

    public static boolean hookWagonOnTrainRear(Train train, Wagon wagon) {
         /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         find the last wagon of the train
         hook the wagon on the last wagon (see Wagon class)
         adjust number of Wagons of Train */
         if(!hasPlaceForWagons(train, wagon)){
             return false;
         }

        if(train.getFirstWagon() == null) {
            train.setFirstWagon(wagon);

            train.resetNumberOfWagons();
            return true;
        }

        if(isSuitableWagon(train, wagon)){
            train.getWagonOnPosition(train.getNumberOfWagons()).setNextWagon(wagon);
            wagon.setPreviousWagon(train.getWagonOnPosition(train.getNumberOfWagons()));
            train.resetNumberOfWagons();
            return true;
        }

        return false;
    }

    public static boolean hookWagonOnTrainFront(Train train, Wagon wagon) {
        /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         if Train has no wagons hookOn to Locomotive
         if Train has wagons hookOn to Locomotive and hook firstWagon of Train to lastWagon attached to the wagon
         adjust number of Wagons of Train */

        if(hasPlaceForWagons(train, wagon) && isSuitableWagon(train, wagon)){

            if(train.getFirstWagon() == null){
                train.setFirstWagon(wagon);
            } else{
                Wagon prevFirst = train.getFirstWagon();
                train.setFirstWagon(wagon);
                wagon.getLastWagonAttached().setNextWagon(prevFirst);
                prevFirst.setPreviousWagon(wagon.getLastWagonAttached());
            }

            train.resetNumberOfWagons();

            return true;
        }


        return false;

    }

    public static boolean hookWagonOnWagon(Wagon first, Wagon second) {
        /* check if wagons are of the same kind (suitable)
         * if so make second wagon next wagon of first */
        if(isSuitableWagon(first, second)){
            first.setNextWagon(second);
            second.setPreviousWagon(first);
            return true;
        }
        return false;

    }


    public static boolean detachAllFromTrain(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon with all its successor
         recalculate the number of wagons of the train */
        if(train.getPositionOfWagon(wagon.getWagonId()) != -1){
            wagon.getPreviousWagon().setNextWagon(null);
            wagon.setPreviousWagon(null);
            train.resetNumberOfWagons();
            return true;
        }
        return false;

    }

    public static boolean detachOneWagon(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon and hook the nextWagon to the previousWagon
         so, in fact remove the one wagon from the train
        */
        if(train.getPositionOfWagon(wagon.getWagonId()) != -1){

            wagon.getPreviousWagon().setNextWagon(wagon.getNextWagon());
            wagon.getNextWagon().setPreviousWagon(wagon.getPreviousWagon());

            wagon.setNextWagon(null);
            wagon.setPreviousWagon(null);

            train.resetNumberOfWagons();
            wagon.getNumberOfWagonsAttached();
            return true;
        }
        return false;

    }

    public static boolean moveAllFromTrain(Train from, Train to, Wagon wagon) {
        /* check if wagon is on train from
         check if wagon is correct for train and if engine can handle new wagons
         detach Wagon and all successors from train from and hook at the rear of train to
         remember to adjust number of wagons of trains */
        if (isSuitableWagon(to,wagon)
//                && to.getEngine().getMaxWagons() >= (to.getNumberOfWagons() + wagon.getNumberOfWagonsAttached() + 1)
                && hasPlaceForWagons(to, wagon)  ) {
            detachAllFromTrain(from,wagon);
            hookWagonOnTrainRear(to,wagon);

            from.resetNumberOfWagons();
            to.resetNumberOfWagons();
            return true;
        }
        return false;
    }

    public static boolean moveOneWagon(Train from, Train to, Wagon wagon) {
        // detach only one wagon from train from and hook on rear of train to
        // do necessary checks and adjustments to trains and wagon
        if (isSuitableWagon(to,wagon) && hasPlaceForOneWagon(to, wagon) ) {
            detachOneWagon(from,wagon);
            hookWagonOnTrainRear(to,wagon);

            from.resetNumberOfWagons();
            to.resetNumberOfWagons();
            return true;
        }
        return false;

    }
}
