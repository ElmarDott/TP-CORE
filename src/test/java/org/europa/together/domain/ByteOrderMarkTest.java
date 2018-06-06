package org.europa.together.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuppressWarnings("unchecked")
public class ByteOrderMarkTest {

    @Test
    void testEnumName() {
        assertEquals("NONE",
                ByteOrderMark.NONE.name());

        assertEquals("UTF_8",
                ByteOrderMark.UTF_8.name());
        assertEquals("UTF_16LE",
                ByteOrderMark.UTF_16LE.name());
        assertEquals("UTF_16BE",
                ByteOrderMark.UTF_16BE.name());

        assertEquals("UTF_32LE",
                ByteOrderMark.UTF_32LE.name());
        assertEquals("UTF_32BE",
                ByteOrderMark.UTF_32BE.name());
    }

    @Test
    public void testGetBytes() {
        assertArrayEquals(new byte[]{},
                ByteOrderMark.NONE.getBytes());

        assertArrayEquals(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF},
                ByteOrderMark.UTF_8.getBytes());
        assertArrayEquals(new byte[]{(byte) 0xFF, (byte) 0xFE},
                ByteOrderMark.UTF_16LE.getBytes());
        assertArrayEquals(new byte[]{(byte) 0xFE, (byte) 0xFF},
                ByteOrderMark.UTF_16BE.getBytes());
        assertArrayEquals(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00},
                ByteOrderMark.UTF_32LE.getBytes());
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF},
                ByteOrderMark.UTF_32BE.getBytes());
    }

    @Test
    void testEnumValues() {
        assertEquals("NONE",
                ByteOrderMark.NONE.toString());

        assertEquals("UTF-8",
                ByteOrderMark.UTF_8.toString());
        assertEquals("UTF-16LE",
                ByteOrderMark.UTF_16LE.toString());
        assertEquals("UTF-16BE",
                ByteOrderMark.UTF_16BE.toString());

        assertEquals("UTF-32LE",
                ByteOrderMark.UTF_32LE.toString());
        assertEquals("UTF-32BE",
                ByteOrderMark.UTF_32BE.toString());
    }
}
