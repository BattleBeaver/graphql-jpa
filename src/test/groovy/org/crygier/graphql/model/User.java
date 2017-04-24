package org.crygier.graphql.model;

import groovy.transform.CompileStatic;
import lombok.Data;
import org.crygier.graphql.annotation.SchemaDocumentation;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@SchemaDocumentation("Entity for mutation testing")
@CompileStatic
public class User {

    @Id
    private Integer id;
    private int age;

    private BigDecimal cashAmount;
    private Double cashAvailable;
    private double cashInBank;

    private Long stepsDoneOverall;
    private long stepsDoneToday;

    private String name;

    private LocalDateTime bornOn;
    private LocalDate lastOnLineOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Double getCashAvailable() {
        return cashAvailable;
    }

    public void setCashAvailable(Double cashAvailable) {
        this.cashAvailable = cashAvailable;
    }

    public double getCashInBank() {
        return cashInBank;
    }

    public void setCashInBank(double cashInBank) {
        this.cashInBank = cashInBank;
    }

    public Long getStepsDoneOverall() {
        return stepsDoneOverall;
    }

    public void setStepsDoneOverall(Long stepsDoneOverall) {
        this.stepsDoneOverall = stepsDoneOverall;
    }

    public long getStepsDoneToday() {
        return stepsDoneToday;
    }

    public void setStepsDoneToday(long stepsDoneToday) {
        this.stepsDoneToday = stepsDoneToday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getBornOn() {
        return bornOn;
    }

    public void setBornOn(LocalDateTime bornOn) {
        this.bornOn = bornOn;
    }

    public LocalDate getLastOnLineOn() {
        return lastOnLineOn;
    }

    public void setLastOnLineOn(LocalDate lastOnLineOn) {
        this.lastOnLineOn = lastOnLineOn;
    }
}
