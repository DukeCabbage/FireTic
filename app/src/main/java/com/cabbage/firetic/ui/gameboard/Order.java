package com.cabbage.firetic.ui.gameboard;

public class Order {

    public enum VerticalOrder {
        TOP, CENTER, BOTTOM
    }

    public enum HorizontalOrder {
        LEFT, CENTER, RIGHT
    }

    public static int orderToIndex(VerticalOrder vo, HorizontalOrder ho) {
        int vi = vo.ordinal();
        int hi = ho.ordinal();
        return vi * HorizontalOrder.values().length + hi;
    }

    public static VerticalOrder indexToVerticalOrder(int index) {
        int columnCount = HorizontalOrder.values().length;
        return VerticalOrder.values()[index / columnCount];
    }

    public static HorizontalOrder indexToHorizontalOrder(int index) {
        int columnCount = HorizontalOrder.values().length;
        return HorizontalOrder.values()[index % columnCount];
    }
}
