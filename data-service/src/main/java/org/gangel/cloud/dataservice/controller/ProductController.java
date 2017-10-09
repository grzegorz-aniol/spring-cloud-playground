package org.gangel.cloud.dataservice.controller;

import org.gangel.cloud.dataservice.dto.ProductTO;
import org.gangel.cloud.dataservice.entity.Product;
import org.gangel.cloud.dataservice.service.AbstractService;
import org.gangel.cloud.dataservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api2/products")
public class ProductController extends AbstractController<Product, ProductTO, Long>{

    @Autowired
    private ProductService service;
    
    @Override
    protected AbstractService<Product, ProductTO, Long> getService() {
        return this.service;
    }
    
}
