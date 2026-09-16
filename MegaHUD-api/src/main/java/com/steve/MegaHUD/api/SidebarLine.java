package com.steve.MegaHUD.api;


import java.util.UUID;

/**
 * A line of text and/or numbers in the Sidebar. Each line's position in the Sidebar (top to bottom)
 * is determined by its 0-based index provided in {@link MegaHudService#setSidebarLine(UUID, int, SidebarLine)}.
 * Consider keeping a {@code Map<Integer, SidebarLine>} to keep track of {@link SidebarLine}s and their indices.
 * <p>
 * **Note: To preserve the {@code index = position} rule, this setup avoids the use of the raw-number vanilla
 * {@code score} attribute, so place any numbers inside the Strings!
 */
public final class SidebarLine {

    private final String leftText;
    private final String rightText;
    private final RightSideDisplay rightSideDisplay;

    /**
     * An empty {@link SidebarLine} (Typically for spacing)
     */
    public static final SidebarLine BLANK = new SidebarLine("", "", RightSideDisplay.BLANK);


    private SidebarLine(String leftText, String rightText, RightSideDisplay rightSideDisplay) {
        this.leftText = leftText;
        this.rightText = rightText;
        this.rightSideDisplay = rightSideDisplay;
    }

    /**
     * Create a {@link SidebarLine} with left-aligned text and nothing on the right.
     */
    public static SidebarLine leftSideOnly(String leftText) {
        return new SidebarLine(leftText, "", RightSideDisplay.BLANK);
    }

    /**
     * Convenience method for creating a {@link SidebarLine} with only right-aligned text.
     */
    public static SidebarLine rightSideOnly(String rightText) {
        return new SidebarLine("", rightText, RightSideDisplay.TEXT);
    }

    /**
     * Create a {@link SidebarLine} with both left-aligned and right-aligned text.
     * Either side can be made blank by passing an empty string.
     */
    public static SidebarLine bothSides(String leftText, String rightText) {
        return new SidebarLine(leftText, rightText, RightSideDisplay.TEXT);
    }

    public String getLeftText() {
        return this.leftText;
    }

    public String getRightText() {
        return this.rightText;
    }

    public RightSideDisplay getRightSideDisplay() {
        return this.rightSideDisplay;
    }


    public enum RightSideDisplay {
        TEXT,
        BLANK
    }
}
