package com.app.server.http;

import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.Business;
import com.app.server.models.Product;
import com.app.server.services.BusinessService;
import com.app.server.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("business")

public class BusinessHttpService {
    private BusinessService service;
    private ProductService productService;
    private ObjectWriter ow;


    public BusinessHttpService() {
        productService = ProductService.getInstance();
        service = BusinessService.getInstance();
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    @OPTIONS
    @PermitAll
    public Response optionsById() {
        return Response.ok().build();
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getAll() {

        return new APPResponse(service.getAll());
    }

    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getOne(@PathParam("id") String id) {
        try {
            Business d = service.getOne(id);
            if (d == null)
                throw new APPNotFoundException(56,"Business not found");
            return new APPResponse(d);
        }
        catch(IllegalArgumentException e){
            throw new APPNotFoundException(56,"Business not found");
        }
        catch (Exception e) {
            throw new APPInternalServerException(0,"Something happened. Come back later.");
        }

    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse create(Object request) {
            return new APPResponse(service.create(request));
    }

    @PATCH
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse update(@PathParam("id") String id, Object request){

        return new APPResponse(service.update(id,request));

    }

    @DELETE
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse delete(@PathParam("id") String id) {

        return new APPResponse(service.delete(id));
    }

    @DELETE
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse delete() {

        return new APPResponse(service.deleteAll());
    }


    @GET
    @Path("{id}/products/")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getAllProducts(@PathParam("id") String businessId) {

        return new APPResponse(productService.getAllProductsInBusiness(businessId));
    }

    @GET
    @Path("{id}/products/{pid}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getOneProduct(@PathParam("id") String id, @PathParam("pid") String productId) {
        try {
            Product d = productService.getOne(id, productId);
            if (d == null)
                throw new APPNotFoundException(56,"Product not found");
            return new APPResponse(d);
        }
        catch(IllegalArgumentException e){
            throw new APPNotFoundException(56,"Product not found");
        }
        catch (Exception e) {
            throw new APPInternalServerException(0,"Something happened. Come back later.");
        }

    }

    @POST
    @Path("{id}/products/")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse createProduct(@PathParam("id") String businessId, Object request) {
        return new APPResponse(productService.create(businessId, request));
    }

    @PATCH
    @Path("{bid}/products/{pid}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse updateProduct(@PathParam("bid") String bid, @PathParam("pid") String pid, Object request){

        return new APPResponse(productService.update(bid, pid, request));

    }

    @DELETE
    @Path("{bid}/products/{pid}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse deleteProduct(@PathParam("bid") String bid, @PathParam("pid") String pid) {

        return new APPResponse(productService.delete(bid,pid));
    }

    @DELETE
    @Path("{id}/products/")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse deleteAll() {

        return new APPResponse(productService.deleteAll());
    }

}
