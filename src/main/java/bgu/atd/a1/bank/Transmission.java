package bgu.atd.a1.bank;

import bgu.atd.a1.Action;

import java.util.ArrayList;
import java.util.List;


public class Transmission extends Action<String> {
    int amount;
    String receiver;
    String sender;
    String receiverBank;
    String senderBank;

    public Transmission(int amount, String receiver, String sender, String receiverBank, String senderBank) {
        this.amount = amount;
        this.receiver = receiver;
        this.sender = sender;
        this.receiverBank = receiverBank;
        this.senderBank = senderBank;
    }

    @Override
    protected void start() {
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> confAction = new Confirmation(sender, receiver, receiverBank, new BankStates());
        actions.add(confAction);

        then(actions, () ->{
            Boolean confirmationResult = actions.get(0).getResult().get();
            if (confirmationResult){
                complete("transmission succeed");
                System.out.println("transmission succeed");
            }
            else {
                complete("transmission failed");
                System.out.println("transmission failed");
            }
        });
        sendMessage(confAction, receiverBank, new BankStates());
    }
}