package ch.erard22.demo;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repository;

    public List<Product> listAll() {
        return repository.listAll();
    }

    @Transactional
    public Product create(Product product) {
        return repository.save(product);
    }

    public Product getById(Long id) {
        return repository.getById(id);
    }

    public List<Product> searchByName(String name) {
        return repository.searchByName(name);
    }
}
