package org.gangel.orders.repository;

public class IdsRange {
    public Long minId;
    public Long maxId;
    public IdsRange(Long min, Long max) {
        this.minId = min;
        this.maxId = max;
    }
}
