package org.gangel.cloud.dataservice.controller;

import org.gangel.cloud.dataservice.entity.Product;
import org.gangel.cloud.dataservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api2/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepo;
    
    @RequestMapping(method=RequestMethod.GET, path="/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        return productRepo.findOne(id);
    }
    
    @RequestMapping(method=RequestMethod.POST, path="/") 
    public void addNewProduct(Product product) {
        productRepo.save(product);
    }
    
}
