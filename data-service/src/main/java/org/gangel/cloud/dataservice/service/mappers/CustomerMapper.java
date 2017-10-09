package org.gangel.cloud.dataservice.service.mappers;

import org.gangel.cloud.dataservice.dto.CustomerTO;
import org.gangel.cloud.dataservice.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy=ReportingPolicy.WARN, nullValueCheckStrategy=NullValueCheckStrategy.ALWAYS)
public abstract class CustomerMapper extends AbstractMapper<Customer, CustomerTO, Long> {

    public abstract CustomerTO toDTO(Customer source);

    public abstract Customer toEntity(CustomerTO source);
    
} 
