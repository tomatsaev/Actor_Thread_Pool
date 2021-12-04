package bgu.atd.a1.bank;

import bgu.atd.a1.PrivateState;

import java.util.ArrayList;
import java.util.List;

public class BankStates extends PrivateState {
    private Integer balance  = 0;

    public int updateBalance(int value) {
        balance += value;
        return balance;
    }
}

