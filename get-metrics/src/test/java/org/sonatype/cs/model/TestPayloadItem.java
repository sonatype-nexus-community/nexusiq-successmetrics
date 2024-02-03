package org.sonatype.cs.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sonatype.cs.getmetrics.model.PayloadItem;

public class TestPayloadItem {
    @Test
    public void create() {
        PayloadItem payload = new PayloadItem("a string");
        Assertions.assertTrue(payload.exists());
        Assertions.assertEquals("a string", payload.getItem());
    }

    @Test
    public void updateItemWithContent() {
        PayloadItem payload = new PayloadItem("a string");
        payload.setItem("another string");
        Assertions.assertTrue(payload.exists());
        Assertions.assertEquals("another string", payload.getItem());
    }

    @Test
    public void updateItemWithoutContent() {
        PayloadItem payload = new PayloadItem("a string");
        payload.setItem("");
        Assertions.assertFalse(payload.exists());
        Assertions.assertEquals("", payload.getItem());
    }
}
