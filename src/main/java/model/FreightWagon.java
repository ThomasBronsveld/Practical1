package model;

import java.util.Iterator;

/**
 * @Project $(PROJECT_NAME)
 * @Author Thomas Bronsveld <Thomas.Bronsveld@hva.nl>
 */
public class FreightWagon extends Wagon {

    private int maxWeight = 0;
    public FreightWagon(int wagonId, int maxWeight) {
        super(wagonId);
        this.maxWeight = maxWeight;
    }

    public int getMaxWeight(){
        return this.maxWeight;
    }
}
