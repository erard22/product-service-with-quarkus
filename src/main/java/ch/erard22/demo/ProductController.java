package ch.erard22.demo;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
