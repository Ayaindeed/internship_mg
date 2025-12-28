package internship.rest;


import internship.entity.Company;
import internship.service.CompanyService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/companies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyResource {

    @Inject
    private CompanyService companyService;

    @POST
    public Response createCompany(@Valid Company company) {
        Company created = companyService.createCompany(company);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    public Response getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return Response.ok(companies).build();
    }

    @GET
    @Path("/{id}")
    public Response getCompanyById(@PathParam("id") Long id) {
        return companyService.getCompanyById(id)
                .map(company -> Response.ok(company).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Entreprise introuvable")).build());
    }

    @PUT
    @Path("/{id}")
    public Response updateCompany(@PathParam("id") Long id, @Valid Company company) {
        try {
            Company updated = companyService.updateCompany(id, company);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCompany(@PathParam("id") Long id) {
        companyService.deleteCompany(id);
        return Response.noContent().build();
    }

    public static class ErrorResponse {
        public String message;
        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
