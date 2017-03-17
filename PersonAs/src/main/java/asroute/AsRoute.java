package asroute;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsRoute extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(AsRoute.class);
    private static final String errorQ = "AS.ERROR";



    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .handled(false)
                .useOriginalMessage()
                .log(LoggingLevel.INFO, logger, "onexception block")
                .doTry()
                .to("amq:" + errorQ)
                .endDoTry()
                .doCatch(Exception.class)
                .log(LoggingLevel.ERROR, logger, "exception when putting msg on error queue").end()
                .end();


        from("amq:AS.IN")
                .routeId("person-as")
                .log(LoggingLevel.INFO, logger, " AS Received  request to process ${headers}")
                // here we invoke the platform we send the message to PLATFORM.IN on which the platform route is listening
                // platform module will send the response back to PLATFORM.OUT queue -- this inout step is waiting on that
                // once the message arrives to PLATFORM.OUT then we carry on with this route to the next step
                .inOut("amq:PLATFORM.IN?replyTo=PLATFORM.OUT&requestTimeout=22000")
                .log(LoggingLevel.INFO, logger, " AS Got the response back ${headers}")
                .to("amq:AS.OUT");
    }

}