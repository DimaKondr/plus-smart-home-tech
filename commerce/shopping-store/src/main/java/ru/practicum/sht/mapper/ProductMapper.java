package ru.practicum.sht.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.sht.dto.shopping.store.PageProductDto;
import ru.practicum.sht.dto.shopping.store.PageableObject;
import ru.practicum.sht.dto.shopping.store.ProductDto;
import ru.practicum.sht.dto.shopping.store.SortObject;
import ru.practicum.sht.model.Product;

import java.util.List;

@Component
public class ProductMapper {

    public ProductDto toDto(Product entity) {
        if (entity == null) return null;

        return ProductDto.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .description(entity.getDescription())
                .imageSrc(entity.getImageSrc())
                .quantityState(entity.getQuantityState())
                .productState(entity.getProductState())
                .productCategory(entity.getProductCategory())
                .price(entity.getPrice())
                .build();
    }

    public PageProductDto toPageDto(Page<Product> page) {
        return PageProductDto.builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .size(page.getSize())
                .content(mapProductList(page))
                .number(page.getNumber())
                .sort(mapSortObjectList(page))
                .numberOfElements(page.getNumberOfElements())
                .pageable(mapPageableObject(page))
                .empty(page.isEmpty())
                .build();

        //pageDto.setTotalElements(page.getTotalElements());
        //pageDto.setTotalPages(page.getTotalPages());
        //pageDto.setFirst(page.isFirst());
        //pageDto.setLast(page.isLast());
        //pageDto.setSize(page.getSize());
        //pageDto.setNumber(page.getNumber());
        //pageDto.setNumberOfElements(page.getNumberOfElements());
        //pageDto.setEmpty(page.isEmpty());

        /*// Маппим список контента
        List<ProductDto> contentDtos = page.getContent().stream()
                .map(this::toDto)
                .toList();
        pageDto.setContent(contentDtos);*/

        /*// Маппим параметры сортировки для верхнего уровня
        List<SortObject> sortObjects = page.getSort().stream()
                .map(order -> {
                    SortObject so = new SortObject();
                    so.setProperty(order.getProperty());
                    so.setDirection(order.getDirection().name());
                    so.setAscending(order.isAscending());
                    so.setIgnoreCase(order.isIgnoreCase());
                    so.setNullHandling(order.getNullHandling().name());
                    return so;
                })
                .toList();
        pageDto.setSort(sortObjects);*/

        /*// Маппим объект PageableObject
        PageableObject pageableObj = new PageableObject();
        pageableObj.setOffset(page.getPageable().getOffset());
        pageableObj.setPaged(page.getPageable().isPaged());
        pageableObj.setUnpaged(page.getPageable().isUnpaged());
        pageableObj.setPageNumber(page.getPageable().getPageNumber());
        pageableObj.setPageSize(page.getPageable().getPageSize());

        // Сортировка внутри объекта pageable (обычно дублирует верхнеуровневую)
        if (!sortObjects.isEmpty()) {
            pageableObj.setSort(sortObjects.get(0)); // или передать весь объект по логике вашего API
        }

        pageDto.setPageable(pageableObj);*/

        //return pageDto;
    }

    public Product toEntity(ProductDto dto) {
        if (dto == null) return null;

        Product entity = new Product();
        // productId не устанавливаем, так как СУБД сгенерирует его автоматически (UUID)
        entity.setProductName(dto.getProductName());
        entity.setDescription(dto.getDescription());
        entity.setImageSrc(dto.getImageSrc());
        entity.setQuantityState(dto.getQuantityState());
        entity.setProductState(dto.getProductState());
        entity.setProductCategory(dto.getProductCategory());
        entity.setPrice(dto.getPrice());
        return entity;
    }

    private List<ProductDto> mapProductList(Page<Product> page) {
        return page.getContent().stream()
                .map(this::toDto)
                .toList();
    }

    private SortObject toSortObject(Sort.Order order) {
        return SortObject.builder()
                .direction(order.getDirection().name())
                .nullHandling(order.getNullHandling().name())
                .ascending(order.isAscending())
                .property(order.getProperty())
                .ignoreCase(order.isIgnoreCase())
                .build();
    }

    /*private List<SortObject> mapSortObjectList(Page<Product> page) {
        return page.getSort().stream()
                .map(order -> SortObject.builder()
                        .direction(order.getDirection().name())
                        .nullHandling(order.getNullHandling().name())
                        .ascending(order.isAscending())
                        .property(order.getProperty())
                        .ignoreCase(order.isIgnoreCase())
                        .build())
                .toList();
    }*/

    private List<SortObject> mapSortObjectList(Page<Product> page) {
        return page.getSort().stream()
                .map(this::toSortObject)
                .toList();
    }

    private SortObject mapSortObject(Page<Product> page) {
        if (page.getSort().isUnsorted()) {
            return null;
        }

        Sort.Order firstOrder = page.getSort().iterator().next();
        return toSortObject(firstOrder);
    }

    /*private SortObject mapSortObject(Page<Product> page) {
        return SortObject.builder()
                .direction(order.getDirection().name())
                .nullHandling(order.getNullHandling().name())
                .ascending(order.isAscending())
                .property(order.getProperty())
                .ignoreCase(order.isIgnoreCase())
                .build();
    }*/

    private PageableObject mapPageableObject(Page<Product> page) {
        return PageableObject.builder()
                .offset(page.getPageable().getOffset())
                .sort(mapSortObject(page))
                .unpaged(page.getPageable().isUnpaged())
                .paged(page.getPageable().isPaged())
                .pageNumber(page.getPageable().getPageNumber())
                .pageSize(page.getPageable().getPageSize())
                .build();
    }

}