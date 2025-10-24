package ch.erard22.demo;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class ProductRepository {

  @Inject
  EntityManager em;

  public List<Product> searchByName(String name) {
    TypedQuery<Product> query = em.createQuery(
        "SELECT p FROM Product p WHERE p.name = :name", Product.class);
    query.setParameter("name", name);
    return query.getResultList();
  }

  public Product getById(long id) {
    return em.find(Product.class, id);
  }

  public Product save(Product product) {
    em.persist(product);
    return product;
  }

  public List<Product> listAll() {
    return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
  }
}
