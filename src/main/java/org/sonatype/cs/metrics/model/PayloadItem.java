package org.sonatype.cs.metrics.model;

public class PayloadItem {

    private boolean exists;
    private String item;

    public PayloadItem() {}

    public PayloadItem(String payloadItem, boolean exists) {
        String item = payloadItem.trim();

        if (item.length() > 0){
            this.setExists(true);
        }
        else {
            this.setExists(false);
        }

        this.setItem(item);
    }

    public boolean isExists() {
        return exists;
    } 

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}