package org.sonatype.cs.metrics.model;

public class PayloadItem {

    private boolean exists;
    private String item;

    public PayloadItem() {}

    public PayloadItem(String payloadItem) {
        String trimmedItem = payloadItem.trim();
        this.setExists(trimmedItem.length() > 0);
        this.setItem(trimmedItem);
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
