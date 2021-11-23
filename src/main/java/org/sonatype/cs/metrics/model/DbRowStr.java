package org.sonatype.cs.metrics.model;

public class DbRowStr {

    private String pointA;
    private String pointB;
    private String pointC;
    private String pointD;
    private String pointE;
    private String pointF;
    private String pointG;
    private String pointH;
    private String pointI;
    private String pointJ;
    private String pointK;
    private String pointL;
    private String pointM;

    public DbRowStr(){}

    public String getPointM() {
        return pointM;
    }

    public void setPointM(String pointM) {
        this.pointM = pointM;
    }

    public String getPointL() {
        return pointL;
    }

    public void setPointL(String pointL) {
        this.pointL = pointL;
    }

    public String getPointK() {
        return pointK;
    }

    public void setPointK(String pointK) {
        this.pointK = pointK;
    }

    public String getPointJ() {
        return pointJ;
    }

    public void setPointJ(String pointJ) {
        this.pointJ = pointJ;
    }

    public String getPointI() {
        return pointI;
    }

    public void setPointI(String pointI) {
        this.pointI = pointI;
    }

    public DbRowStr(String pointA, String pointB, String pointC, String pointD, String pointE, String pointF, String pointG, String pointH,
                    String pointI, String pointJ, String pointK, String pointL, String pointM) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;
        this.pointD = pointD;
        this.pointE = pointE;
        this.pointF = pointF;
        this.pointG = pointG;
        this.pointH = pointH;
        this.setPointI(pointI);
        this.setPointJ(pointJ);
        this.setPointK(pointK);
        this.setPointL(pointL);
        this.setPointM(pointM);
    }

    public String getPointH() {
		return pointH;
	}

	public void setPointH(String pointH) {
		this.pointH = pointH;
	}

	public String getPointA() {
        return pointA;
    }

    public void setPointA(String pointA) {
        this.pointA = pointA;
    }

    public String getPointB() {
        return pointB;
    }

    public void setPointB(String pointB) {
        this.pointB = pointB;
    }

    public String getPointC() {
        return pointC;
    }

    public void setPointC(String pointC) {
        this.pointC = pointC;
    }

    public String getPointD() {
        return pointD;
    }

    public void setPointD(String pointD) {
        this.pointD = pointD;
    }

    public String getPointE() {
        return pointE;
    }

    public void setPointE(String pointE) {
        this.pointE = pointE;
    }

    public String getPointF() {
        return pointF;
    }

    public void setPointF(String pointF) {
        this.pointF = pointF;
    }

    public String getPointG() {
        return pointG;
    }

    public void setPointG(String pointG) {
        this.pointG = pointG;
    }

    @Override
    public String toString() {
        return "DbRowStr [pointA=" + pointA + ", pointB=" + pointB + ", pointC=" + pointC + ", pointD=" + pointD
                + ", pointE=" + pointE + ", pointF=" + pointF + "]";
    }
    
}
