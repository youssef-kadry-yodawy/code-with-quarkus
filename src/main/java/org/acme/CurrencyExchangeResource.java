package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.acme.model.ExchangeRate;
import org.acme.proxy.CurrencyExchangeProxy;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;

@Path("/exchange")
public class CurrencyExchangeResource {

    @RestClient
    CurrencyExchangeProxy proxy;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ExchangeRate> get(@QueryParam("amount") Double amount, @QueryParam("from") String from, @QueryParam("to") String to) {
        Uni<ExchangeRate> firstExchange = proxy.get(amount,from, to);

        Uni<ExchangeRate> secondExchange =  firstExchange.onItem().transformToUni(
                exchange -> {
                    Map.Entry<String, Double> entry = exchange.getRates().entrySet().iterator().next();
                    return proxy.get(entry.getValue()*2,from, to);
                }
        );

        return secondExchange.onItem().transformToUni(
                exchange -> {
                    Map.Entry<String, Double> entry = exchange.getRates().entrySet().iterator().next();
                    return proxy.get(entry.getValue(), to, from);
                }
        );
    }
}
