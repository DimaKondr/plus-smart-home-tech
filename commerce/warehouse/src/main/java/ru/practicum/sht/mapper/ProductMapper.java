package ru.practicum.sht.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.sht.model.WarehouseProduct;
import ru.practicum.sht.request.warehouse.NewProductInWarehouseRequest;

@Component
public class ProductMapper {

    public WarehouseProduct toEntity (NewProductInWarehouseRequest request) {
        return new WarehouseProduct(
                request.getProductId(),
                //request.getFragile() == null ? false : request.getFragile(),
                Boolean.TRUE.equals(request.getFragile()),
                request.getDimension().getWidth(),
                request.getDimension().getHeight(),
                request.getDimension().getDepth(),
                request.getWeight()
        );
    }

}