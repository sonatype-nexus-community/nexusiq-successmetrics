package org.sonatype.cs.getmetrics.model;

import java.util.Objects;

public class PayloadItem {
    private String item;

    public PayloadItem() {}

    public PayloadItem(String payloadItem) {
        this.setItem(payloadItem.trim());
    }

    public boolean exists() {
        return item != null && !item.isEmpty();
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = Objects.requireNonNull(item, "Item cannot be null");
    }
}
