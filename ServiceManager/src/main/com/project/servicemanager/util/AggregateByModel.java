package com.project.servicemanager.util;

public class AggregateByModel {
    private int availableStock;
    private int sales;

    public int getAvailableStock() {
        return availableStock;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }
}
