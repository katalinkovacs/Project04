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
                .to("wmq:" + errorQ)
                .endDoTry()
                .doCatch(Exception.class)
                .log(LoggingLevel.ERROR, logger, "exception when putting msg on error queue").end()
                .end();


        from("amq:AS.IN?concurrentConsumers=1&maxConcurrentConsumers=50")
                .routeId("kati-as")
                .log(LoggingLevel.INFO, logger, " AS Received  request to process ${headers}") //and body : ${body}")
                .inOut("amq:PLATFORM.IN?replyTo=PLATFORM.OUT&requestTimeout=22000")
                .log(LoggingLevel.INFO, logger, " AS Got the response back ${headers}")
                .to("amq:AS.OUT");
    }

}