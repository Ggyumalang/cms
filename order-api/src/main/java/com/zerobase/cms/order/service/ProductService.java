package com.zerobase.cms.order.service;

import static com.zerobase.cms.order.exception.ErrorCode.NOT_FOUND_PRODUCT;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.UpdateProductForm;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product addProduct(Long sellerId, AddProductForm form) {
        return productRepository.save(Product.of(sellerId, form));
    }

    @Transactional
    public Product updateProduct(Long sellerId, UpdateProductForm form) {
        Product product = productRepository.findBySellerIdAndId(sellerId,
                form.getProductId())
            .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

        product.updateProductInfo(form);
        return product;
    }

    @Transactional
    public void deleteProduct(Long sellerId, Long productId) {
        Product product = productRepository.findBySellerIdAndId(sellerId,
                productId)
            .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

        productRepository.delete(product);
    }
}
