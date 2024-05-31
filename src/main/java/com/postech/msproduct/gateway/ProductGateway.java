package com.postech.msproduct.gateway;

import com.postech.msproduct.dto.ProductDTO;
import com.postech.msproduct.entity.Product;
import com.postech.msproduct.exceptions.NotFoundException;
import com.postech.msproduct.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductGateway {

    @Autowired
    private ProductRepository productRepository;

    public ProductGateway(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product newProd = new Product(productDTO);
        return productRepository.save(newProd).toDTO();
    }

    public ProductDTO updateProduct(ProductDTO productDTO) {
        Product newProd = new Product(productDTO);
        return productRepository.save(newProd).toDTO();
    }

    public boolean deleteProduct(Integer id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public ProductDTO findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado")).toDTO();
    }

    public List<ProductDTO> listAll() {
        List<Product> customerList = productRepository.findAll();
        return customerList
                .stream()
                .map(this::toProductDTO)
                .collect(Collectors.toList());
    }
    private ProductDTO toProductDTO(Product product) {
        return product.toDTO();
    }

    public ProductDTO updateStockIncrease(Integer id, int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        if (product != null) {
            product.setQuantity(product.getQuantity() + quantity);
            return productRepository.save(product).toDTO();
        }
        throw new NotFoundException("Produto não encontrado");
    }

    public ProductDTO updateStockDecrease(Integer id, int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        if (product != null) {
            product.setQuantity(product.getQuantity() - quantity);
            return productRepository.save(product).toDTO();
        }
        throw new NotFoundException("Produto não encontrado");
    }

    public Boolean isProductAvailableById(Integer id, int qttyNewOrder) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        if (product != null) {
            return product.getQuantity() >= qttyNewOrder;
        }
        throw new NotFoundException("Produto não encontrado");
    }
}
