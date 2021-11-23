package org.sonatype.cs.metrics.model;

public class DbRow {

    private String label;
    private int pointA;
    private int pointB;
    private int pointC;
    private int pointD;

    public DbRow(String label, int pointA, int pointB, int pointC, int pointD) {
        this.label = label;
        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;
        this.pointD = pointD;
    }

    public DbRow(){}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getPointA() {
        return pointA;
    }

    public void setPointA(int pointA) {
        this.pointA = pointA;
    }

    public int getPointB() {
        return pointB;
    }

    public void setPointB(int pointB) {
        this.pointB = pointB;
    }

    public int getPointC() {
        return pointC;
    }

    public void setPointC(int pointC) {
        this.pointC = pointC;
    }

    public int getPointD() {
        return pointD;
    }

    public void setPointD(int pointD) {
        this.pointD = pointD;
    }

    @Override
    public String toString() {
        return "DbRow [label=" + label + ", pointA=" + pointA + ", pointB=" + pointB + ", pointC=" + pointC
                + ", pointD=" + pointD + "]";
    }
    
}
