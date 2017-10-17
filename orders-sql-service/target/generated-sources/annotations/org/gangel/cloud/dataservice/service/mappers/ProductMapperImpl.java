package org.gangel.cloud.dataservice.service.mappers;

import javax.annotation.Generated;
import org.gangel.cloud.dataservice.dto.ProductTO;
import org.gangel.orders.entity.Product;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2017-10-17T14:55:14+0200",
    comments = "version: 1.2.0.CR2, compiler: javac, environment: Java 1.8.0_131 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl extends ProductMapper {

    @Override
    public ProductTO toDTO(Product source) {
        if ( source == null ) {
            return null;
        }

        ProductTO productTO = new ProductTO();

        if ( source.getId() != null ) {
            productTO.setId( source.getId() );
        }
        if ( source.getTitle() != null ) {
            productTO.setTitle( source.getTitle() );
        }
        if ( source.getDescription() != null ) {
            productTO.setDescription( source.getDescription() );
        }
        productTO.setPrice( source.getPrice() );

        return productTO;
    }

    @Override
    public Product toEntity(ProductTO source) {
        if ( source == null ) {
            return null;
        }

        Product product = entityFactory( source, Product.class );

        if ( source.getId() != null ) {
            product.setId( source.getId() );
        }
        if ( source.getTitle() != null ) {
            product.setTitle( source.getTitle() );
        }
        if ( source.getDescription() != null ) {
            product.setDescription( source.getDescription() );
        }
        product.setPrice( source.getPrice() );

        return product;
    }
}
