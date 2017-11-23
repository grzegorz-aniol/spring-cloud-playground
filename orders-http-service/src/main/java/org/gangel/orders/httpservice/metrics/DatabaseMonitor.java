package org.gangel.orders.httpservice.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class DatabaseMonitor {

    public static final String DB_LATENCY_VALUE = "database.latency";
    public static final String DB_LATENCY_CHECKTIME = "database.latency.checktime";
    
    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    private GaugeService gaugeService;
    
    @Scheduled(initialDelay=30_000, fixedDelay=30_000)
    @Transactional(readOnly=true)    
    public void ping() {
        long startTime = System.currentTimeMillis();
        em.createNativeQuery("/* ping */ select 1").getFirstResult();
        long latency = System.currentTimeMillis() - startTime;
        gaugeService.submit(DB_LATENCY_VALUE, 1e-3 * latency);
        gaugeService.submit(DB_LATENCY_CHECKTIME, startTime);
    }
    
}
