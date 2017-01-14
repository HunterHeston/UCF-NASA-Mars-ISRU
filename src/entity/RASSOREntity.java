package entity;


import org.apache.log4j.L



/**
 * Created by Hunter on 1/12/2017.
 */
public class RASSOREntity {

    public enum States{

        Standby,
        MineFinding,
        MineSelected,
        HandleCharge,
        MovingToMine,
        AtMine,
        Mining,
        MiningComplete,
        MovingToISRU,
        AtISRU,
        DepositingRegolith,
    }




}
