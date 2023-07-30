package org.acme.proxy;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.acme.model.ExchangeRate;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface CurrencyExchangeProxy {

    @GET
    @Path("/latest")
    Uni<ExchangeRate> get(@QueryParam("amount") Double amount, @QueryParam("from") String from, @QueryParam("to") String to);

}
