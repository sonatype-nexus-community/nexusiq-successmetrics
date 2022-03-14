package org.sonatype.cs.getmetrics.model;

public class PayloadItem {
    private String item;

    public PayloadItem() {}

    public PayloadItem(String payloadItem) {
        this.setItem(payloadItem.trim());
    }

    public boolean isExists() {
        return this.item.length() > 0;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
