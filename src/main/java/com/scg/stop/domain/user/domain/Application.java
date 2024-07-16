package com.scg.stop.domain.user.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Application {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String division;

    private String position;

//    @OneToOne(fetch = LAZY, cascade = ALL)
//    @JoinColumn(name = "user_id")
//    private User user;
}
