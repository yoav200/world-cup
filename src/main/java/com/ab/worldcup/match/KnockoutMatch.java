package com.ab.worldcup.match;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@Immutable
public class KnockoutMatch extends Match{

    @Enumerated(EnumType.STRING)
    private KnockoutMatchCode matchCode;
}
