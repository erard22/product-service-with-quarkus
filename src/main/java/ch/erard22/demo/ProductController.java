package ch.erard22.demo;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("products")
public class ProductController {

    @Inject
    ProductService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> listAll() {
        return service.listAll();
    }

    @POST
    public Product create(Product product) {
        return service.create(product);
    }

    @GET
    @Path("/{id}")
    public Product getById(@PathParam("id") Long id) {
        return service.getById(id);
    }

    @GET
    @Path("/search/{name}")
    public List<Product> searchByName(@PathParam("name") String name) {
        return service.searchByName(name);
    }
}
