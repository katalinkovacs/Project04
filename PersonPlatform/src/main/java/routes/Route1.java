package routes;

import org.apache.camel.builder.RouteBuilder;


public class Route1 extends RouteBuilder{

    @Override
    public void configure() throws Exception {


        // this is a route that will pass back the response to the queue defined
        // in the incoming JMSReplyTo -- this is coming from your AS
        from("amq:PLATFORM.IN")
                .routeId("person-platform")
                .log(LoggingLevel.INFO, logger, " platform received a request process ${headers}");

    }

    
}
