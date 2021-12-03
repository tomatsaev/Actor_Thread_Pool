package bgu.atd.a1.bank;

import bgu.atd.a1.Action;

public class Confirmation extends Action<Boolean> {
    String receiver;
    String sender;
    String receiverBank;
    BankStates bankStates;

    public Confirmation(String receiver, String sender, String receiverBank, BankStates bankStates) {
        this.receiver = receiver;
        this.sender = sender;
        this.receiverBank = receiverBank;
        this.bankStates = bankStates;
    }

    @Override
    protected void start() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bankStates.addRecord(receiver + " got from " + sender + " to " + receiverBank);
        System.out.println(receiver + " got from " + sender + " to " + receiverBank);
        complete(true);
    }
}
