package ch.erard22.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Iterable<Product> listAll() {
        return repository.findAll();
    }

    public Product create(Product product) {
        return repository.save(product);
    }

    public Product getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Product> searchByName(String name) {
        return repository.findByName(name);
    }
}
