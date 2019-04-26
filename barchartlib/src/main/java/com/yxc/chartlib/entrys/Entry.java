
package com.yxc.chartlib.entrys;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;

import com.yxc.chartlib.util.DecimalUtil;

/**
 * Class representing one entry in the chart. Might contain multiple values.
 * Might only contain a single value depending on the used constructor.
 * 
 * @author Philipp Jahoda
 */
public class Entry extends BaseEntry implements Parcelable {

    public static final int TYPE_UNSELECTED = 0;//没有选中

    public static final int TYPE_SINGLE_TAP_UP_SELECTED = 1;//单击选中

    public static final int TYPE_LONG_PRESS_SELECTED = 2;//长按选中


    public int isSelected = TYPE_UNSELECTED;

    public boolean isSelected() {
        return isSelected == TYPE_SINGLE_TAP_UP_SELECTED || isSelected == TYPE_LONG_PRESS_SELECTED;
    }

    /** the x value */
    private float x = 0f;

    public Entry() {

    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     */
    public Entry(float x, float y) {
        super(y);
        this.x = x;
    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     * @param data Spot for additional data this Entry represents.
     */
    public Entry(float x, float y, Object data) {
        super(y, data);
        this.x = x;
    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     * @param icon icon image
     */
    public Entry(float x, float y, Drawable icon) {
        super(y, icon);
        this.x = x;
    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     * @param icon icon image
     * @param data Spot for additional data this Entry represents.
     */
    public Entry(float x, float y, Drawable icon, Object data) {
        super(y, icon, data);
        this.x = x;
    }

    /**
     * Returns the x-value of this Entry object.
     * 
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x-value of this Entry object.
     * 
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * returns an exact copy of the entry
     * 
     * @return
     */
    public Entry copy() {
        Entry e = new Entry(x, getY(), getData());
        return e;
    }

    /**
     * Compares value, xIndex and data of the entries. Returns true if entries
     * are equal in those points, false if not. Does not check by hash-code like
     * it's done by the "equals" method.
     * 
     * @param e
     * @return
     */
    public boolean equalTo(Entry e) {

        if (e == null)
            return false;

        if (e.getData() != this.getData())
            return false;

        if (Math.abs(e.x - this.x) > DecimalUtil.FLOAT_EPSILON)
            return false;

        if (Math.abs(e.getY() - this.getY()) > DecimalUtil.FLOAT_EPSILON)
            return false;

        return true;
    }

    /**
     * returns a string representation of the entry containing x-index and value
     */
    @Override
    public String toString() {
        return "Entry, x: " + x + " y: " + getY();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.x);
        dest.writeFloat(this.getY());
        if (getData() != null) {
            if (getData() instanceof Parcelable) {
                dest.writeInt(1);
                dest.writeParcelable((Parcelable) this.getData(), flags);
            } else {
                throw new ParcelFormatException("Cannot parcel an Entry with non-parcelable data");
            }
        } else {
            dest.writeInt(0);
        }
    }

    protected Entry(Parcel in) {
        this.x = in.readFloat();
        this.setY(in.readFloat());
        if (in.readInt() == 1) {
            this.setData(in.readParcelable(Object.class.getClassLoader()));
        }
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        public Entry createFromParcel(Parcel source) {
            return new Entry(source);
        }

        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };



}
