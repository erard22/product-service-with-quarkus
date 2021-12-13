package ch.erard22.demo;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ProductService {

    public List<Product> listAll() {
        return Product.listAll();
    }

    @Transactional
    public Product create(Product product) {
        product.persist();
        return product;
    }

    public Product getById(Long id) {
        return Product.findById(id);
    }

    public List<Product> searchByName(String name) {
        return Product.find("name", name).list();
    }

}
