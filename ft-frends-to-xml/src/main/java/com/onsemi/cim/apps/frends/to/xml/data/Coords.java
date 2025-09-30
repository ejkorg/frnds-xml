package com.onsemi.cim.apps.frends.to.xml.data;

import java.util.Objects;

/**
 *
 * @author fg7x8c
 */
public class Coords {
    
    private Short x;
    private Short y;

    public Coords() {
    }

    public Coords(Short x, Short y) {
        this.x = x;
        this.y = y;
    }

    public Short getX() {
        return x;
    }

    public void setX(Short x) {
        this.x = x;
    }

    public Short getY() {
        return y;
    }

    public void setY(Short y) {
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.x);
        hash = 97 * hash + Objects.hashCode(this.y);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coords other = (Coords) obj;
        if (this.x == null && other.x != null) return false;
        if (this.x != null && !this.x.equals(other.x)) return false;
        if (this.y == null && other.y != null) return false;
        if (this.y != null && !this.y.equals(other.y)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Coords{" + "x=" + x + ", y=" + y + '}';
    }

}
