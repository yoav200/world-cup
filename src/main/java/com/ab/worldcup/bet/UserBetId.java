package com.ab.worldcup.bet;

import com.ab.worldcup.account.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class UserBetId implements Serializable {

    @ManyToOne
    @JoinColumn(name="accountId",referencedColumnName="id")
    private Account account;

    @ManyToOne
    @JoinColumn(name="betId",referencedColumnName="id")
    private Bet bet;
}
