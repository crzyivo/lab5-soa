package soa.web;

import org.apache.camel.ProducerTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
public class SearchController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SearchController.class);

    @RequestMapping(value="/search")
    @ResponseBody
    public Object search(@RequestParam("q") String q) {
        String[] parse = q.split("max:");
        String qr = parse[0];
        String n = "30";
        if(parse.length != 1) {
            String[] parse2 = parse[1].split(" ");
            try {
               Integer.parseInt(parse2[0]);
               n = parse2[0];
            }catch (NumberFormatException e){ }
            if(parse2.length != 1) {
                qr = qr + " " + parse2[1];
            }
        }
        logger.info("qr: "+ qr);
        logger.info("n: "+ n);
        Map headers = new HashMap<>();
        headers.put("CamelTwitterKeywords",qr);
        headers.put("CamelTwitterCount",n);
        return producerTemplate.requestBodyAndHeaders("direct:search", "", headers);
    }


}