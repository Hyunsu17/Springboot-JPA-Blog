package com.cos.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Builder
@Getter
@ToString
@Table(name = "TB_WORKOUT_ROUTINE")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROUTINE_SQ")
    private int ID;

    @Column(name = "ROUTINE_NM")
    private String name;

    @ManyToOne
    @JoinColumn(name = "USER_SQ")
    private User user;

    protected Routine() {

    }
}