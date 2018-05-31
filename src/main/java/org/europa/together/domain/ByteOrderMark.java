package org.europa.together.domain;

/**
 * Existing Byte Order Mark (BOM) types.
 */
public enum ByteOrderMark {

    NONE("NONE", new byte[]{}),
    UTF_8("UTF_8", new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}),
    UTF_16LE("UTF_16LE", new byte[]{(byte) 0xFF, (byte) 0xFE}),
    UTF_16BE("UTF_16BE", new byte[]{(byte) 0xFE, (byte) 0xFF}),
    UTF_32LE("UTF_32LE", new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00}),
    UTF_32BE("UTF_32BE", new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF});

    private final String type;
    private final byte[] value;

    //CONSTRUCTOR
    ByteOrderMark(final String type, final byte[] value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Get the Byte vale for a Byte Order.
     *
     * @return value as Byte
     */
    public byte[] getBytes() {
        byte[] val = this.value;
        return val;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
