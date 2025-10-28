package ch.erard22.demo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByName(String name);

    Product findById(long id);

}
