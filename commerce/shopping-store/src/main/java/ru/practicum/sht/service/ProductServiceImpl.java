package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.sht.dto.shopping.store.PageProductDto;
import ru.practicum.sht.dto.shopping.store.ProductCategory;
import ru.practicum.sht.dto.shopping.store.ProductDto;
import ru.practicum.sht.dto.shopping.store.ProductState;
import ru.practicum.sht.exception.shopping.store.ProductNotFoundException;
import ru.practicum.sht.mapper.ProductMapper;
import ru.practicum.sht.model.Product;
import ru.practicum.sht.repository.ShoppingStoreRepository;
import ru.practicum.sht.request.shopping.store.SetProductQuantityStateRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ShoppingStoreRepository shoppingStoreRepository;
    private final ProductMapper productMapper;

    @Override
    public PageProductDto getProducts(ProductCategory category, int page, int size, List<String> sortParameters) {
        List<Sort.Order> orders = new ArrayList<>();

        for (int i = 0; i < sortParameters.size(); i++) {
            String current = sortParameters.get(i);

            if (current.contains(",")) {
                String[] parts = current.split(",");
                String property = parts[0];
                Sort.Direction direction = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1]))
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, property));
            } else if ("desc".equalsIgnoreCase(current) || "asc".equalsIgnoreCase(current)) {
                if (!orders.isEmpty()) {
                    Sort.Order lastOrder = orders.remove(orders.size() - 1);
                    Sort.Direction direction = "desc".equalsIgnoreCase(current)
                            ? Sort.Direction.DESC
                            : Sort.Direction.ASC;
                    orders.add(new Sort.Order(direction, lastOrder.getProperty()));
                }
            } else {
                orders.add(new Sort.Order(Sort.Direction.ASC, current));
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        Page<Product> productPage = shoppingStoreRepository.findByProductCategoryAndProductState(
                category,
                ProductState.ACTIVE,
                pageable
        );

        return productMapper.toPageDto(productPage);
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto dto) {
        Product productEntity = productMapper.toEntity(dto);

        Product savedProduct = shoppingStoreRepository.save(productEntity);

        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto dto) throws ProductNotFoundException {
        UUID id = dto.getProductId();
        if (id == null) {
            throw new ProductNotFoundException("ID товара не может быть null.");
        }

        Product oldProduct = shoppingStoreRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID: " + id + " не найден."));
        log.info("Данные товара для обновления: {}.", oldProduct);

        oldProduct.setProductName(dto.getProductName());
        oldProduct.setDescription(dto.getDescription());
        oldProduct.setImageSrc(dto.getImageSrc());
        oldProduct.setQuantityState(dto.getQuantityState());
        oldProduct.setProductState(dto.getProductState());
        oldProduct.setProductCategory(dto.getProductCategory());
        oldProduct.setPrice(dto.getPrice());

        Product updatedProduct = shoppingStoreRepository.save(oldProduct);

        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public Boolean removeProduct(UUID productId) throws ProductNotFoundException {
        Product oldProduct = shoppingStoreRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID: " + productId + " не найден."));

        try {
            oldProduct.setProductState(ProductState.DEACTIVATE);
            Product updatedProduct = shoppingStoreRepository.save(oldProduct);
            log.info("Следующий товар был удален: {}.", updatedProduct);
        } catch (Exception e) {
            log.error("Не удалось удалить товар с ID: {}. Ошибка: {}.", productId, e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public Boolean setQuantityState(SetProductQuantityStateRequest quantityState) throws ProductNotFoundException {
        Product oldProduct = shoppingStoreRepository.findById(quantityState.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID: "
                        + quantityState.getProductId() + " не найден."));

        oldProduct.setQuantityState(quantityState.getQuantityState());

        try {
            shoppingStoreRepository.save(oldProduct);
        } catch (Exception e) {
            log.error("Не удалось изменить количество товара с ID: {}. Ошибка: {}.",
                    quantityState.getProductId(), e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public ProductDto getProductById(UUID productId) throws ProductNotFoundException {
        Product product = shoppingStoreRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID: " + productId + " не найден."));

        return productMapper.toDto(product);
    }

}