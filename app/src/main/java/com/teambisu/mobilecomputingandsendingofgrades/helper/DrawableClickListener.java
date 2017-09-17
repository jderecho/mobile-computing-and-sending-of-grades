package com.teambisu.mobilecomputingandsendingofgrades.helper;

/**
 * Created by John Manuel on 17/09/2017.
 */

public interface DrawableClickListener {

    public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
    public void onClick(DrawablePosition target);
}
