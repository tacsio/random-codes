package io.tacsio.quarkus.jwt;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/jwt")
@RequestScoped
public class TokenSecuredResource {

    @Inject
    private JsonWebToken jwt;

    private final Random rnd = new Random();

    // Not secured
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello";
    }

    // Secured and allowed to all roles
    @GET
    @Path("permit-all")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@Context final SecurityContext ctx) {
        final Principal caller = ctx.getUserPrincipal();
        final String name = caller == null ? "anonymous" : caller.getName();
        final boolean hasJWT = jwt.getClaimNames() != null;

        return String.format("hello %s, isSecure: %s, authScheme: %s, hasJWT: %s", name, ctx.isSecure(),
                ctx.getAuthenticationScheme(), hasJWT);
    }

    // Secured and allowed to specific roles
    @GET
    @Path("roles-allowed")
    @RolesAllowed({ "Echoer", "Subscriber" })
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowed(@Context final SecurityContext ctx) {
        final Principal caller = ctx.getUserPrincipal();
        final String name = caller == null ? "anonymous" : caller.getName();
        final boolean hasJWT = jwt.getClaimNames() != null;

        return String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s", name, ctx.isSecure(),
                ctx.getAuthenticationScheme(), hasJWT);
    }

    @GET
    @Path("claims")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Subscriber")
    public String claims() {
        Map<String, Object> claims = new HashMap<>();
        jwt.getClaimNames().forEach(name -> claims.put(name, jwt.getClaim(name)));

        return claims.toString();
    }
}
