package com.example.coilvision_2.engineer;

public class DashboardData {
    private String login;
    private int num_f;
    private boolean isRead;
    private String textObject;
    private String report;
    private int graphNumber;

    private int reportNumber;

    public DashboardData(String login, int num_f, boolean isRead, String textObject, String report, int graphNumber, int reportNumber) {
        this.login = login;
        this.num_f = num_f;
        this.isRead = isRead;
        this.textObject = textObject;
        this.report = report;
        this.graphNumber = graphNumber;
        this.reportNumber = reportNumber;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getNum_f() {
        return num_f;
    }

    public void setNum_f(int num_f) {
        this.num_f = num_f;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getTextObject() {
        return textObject;
    }

    public void setTextObject(String textObject) {
        this.textObject = textObject;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public int getGraphNumber() {
        return graphNumber;
    }

    public void setGraphNumber(int graphNumber) {
        this.graphNumber = graphNumber;
    }

    public int getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(int reportNumber) {
        this.reportNumber = reportNumber;
    }

    @Override
    public String toString() {
        return login;
    }

}
