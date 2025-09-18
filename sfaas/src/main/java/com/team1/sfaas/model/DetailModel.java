package com.team1.sfaas.model;

public class DetailModel {
    private String machineName;
    private String machineStartDate;
    private boolean isRun;

    // ---- machine information
    private int data1Value;
    private String data1Name;
    private String data1Date;

    // ---- data1 information
    private int data2Value;
    private String data2Name;
    private String data2Date;

    // ---- data2 information
    private int machineRate;

    // Getters and Setters

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineStartDate() {
        return machineStartDate;
    }

    public void setMachineStartDate(String machineStartDate) {
        this.machineStartDate = machineStartDate;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public int getData1Value() {
        return data1Value;
    }

    public void setData1Value(int data1Value) {
        this.data1Value = data1Value;
    }

    public String getData1Name() {
        return data1Name;
    }

    public void setData1Name(String data1Name) {
        this.data1Name = data1Name;
    }

    public String getData1Date() {
        return data1Date;
    }

    public void setData1Date(String data1Date) {
        this.data1Date = data1Date;
    }

    public int getData2Value() {
        return data2Value;
    }

    public void setData2Value(int data2Value) {
        this.data2Value = data2Value;
    }

    public String getData2Name() {
        return data2Name;
    }

    public void setData2Name(String data2Name) {
        this.data2Name = data2Name;
    }

    public String getData2Date() {
        return data2Date;
    }

    public void setData2Date(String data2Date) {
        this.data2Date = data2Date;
    }

    public int getMachineRate() {
        return machineRate;
    }

    public void setMachineRate(int machineRate) {
        this.machineRate = machineRate;
    }
}
