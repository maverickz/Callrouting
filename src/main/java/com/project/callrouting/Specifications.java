package com.project.callrouting;

public class Specifications {
    protected String name = "";
    protected String ip_subnet = "";
    protected int throughput = 0;
    protected double cost = 0.0;
 
    public Specifications() {
    }
 
    public Specifications(String name, String ip_subnet, int throughput, double cost) {
        setName(name);
        setIpSubnet(ip_subnet);
        setThroughput(throughput);
        setCost(cost);
    }
 
    public String getIpSubnet() {
        return ip_subnet;
    }
 
    public void setIpSubnet(String ip_subnet) {
        this.ip_subnet = ip_subnet;
    }
 
    public int getThroughput() {
        return throughput;
    }
 
    public void setThroughput(int throughput) {
        this.throughput = throughput;
    }
 
    public double getCost() {
        return cost;
    }
 
    public void setCost(double cost) {
        this.cost = cost;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
} // class