package org.gangel.orders.httpservice.controller;

import org.gangel.common.services.AbstractController;
import org.gangel.common.services.AbstractService;
import org.gangel.orders.dto.ProductTO;
import org.gangel.orders.entity.Product;
import org.gangel.orders.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ProductController.ENDPOINT)
public class ProductController extends AbstractController<Product, ProductTO, Long>{

    public final static String ENDPOINT = "/api2/products"; 
    
    @Autowired
    private ProductService service;
    
    @Override
    protected AbstractService<Product, ProductTO, Long> getService() {
        return this.service;
    }

    @Override
    protected String getEndpointRoot() {
        return ENDPOINT;
    }
    
}
