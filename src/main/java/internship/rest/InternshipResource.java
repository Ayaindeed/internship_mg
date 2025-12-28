package internship.rest;


import internship.entity.Internship;
import internship.service.InternshipService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/internships")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InternshipResource {

    @Inject
    private InternshipService internshipService;

    @POST
    public Response createInternship(@Valid Internship internship) {
        try {
            // Extraire les IDs depuis les objets student et company dans le JSON
            Long studentId = internship.getStudent() != null ? internship.getStudent().getId() : null;
            Long companyId = internship.getCompany() != null ? internship.getCompany().getId() : null;
            
            if (studentId == null || companyId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Student ID et Company ID sont requis")).build();
            }
            
            Internship created = internshipService.createInternship(internship, studentId, companyId);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @GET
    public Response getAllInternships() {
        List<Internship> internships = internshipService.getAllInternships();
        return Response.ok(internships).build();
    }

    @GET
    @Path("/{id}")
    public Response getInternshipById(@PathParam("id") Long id) {
        return internshipService.getInternshipById(id)
                .map(internship -> Response.ok(internship).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Stage introuvable")).build());
    }

    @GET
    @Path("/student/{studentId}")
    public Response getInternshipsByStudent(@PathParam("studentId") Long studentId) {
        List<Internship> internships = internshipService.getInternshipsByStudent(studentId);
        return Response.ok(internships).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateInternship(@PathParam("id") Long id, @Valid Internship internship) {
        try {
            Internship updated = internshipService.updateInternship(id, internship);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteInternship(@PathParam("id") Long id) {
        internshipService.deleteInternship(id);
        return Response.noContent().build();
    }

    public static class ErrorResponse {
        public String message;
        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
