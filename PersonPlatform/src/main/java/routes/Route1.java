package routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Route1 extends RouteBuilder{

    private static final Logger logger = LoggerFactory.getLogger(Route1.class);

    @Override
    public void configure() throws Exception {


        // this is a route that will pass back the response to the queue defined
        // in the incoming JMSReplyTo -- this is coming from your AS
        from("amq:PLATFORM.IN")
                .routeId("person-platform")
                .log(LoggingLevel.INFO, logger, " platform received a request process ${headers}");

    }

    
}
