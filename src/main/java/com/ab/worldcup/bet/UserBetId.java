package com.ab.worldcup.bet;

import com.ab.worldcup.account.Account;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class UserBetId implements Serializable {

    @ManyToOne
    @JoinColumn(name="accountId",referencedColumnName="id")
    private Account account;

    @ManyToOne
    @JoinColumn(name="betId",referencedColumnName="id")
    private Bet bet;
}
