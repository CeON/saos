package pl.edu.icm.saos.api.response.representations;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Success's view representation.
 * @author pavtel
 */
public class SuccessRepresentation {

    private Map<String, Object> representation = new LinkedHashMap<String, Object>();

    private static class CS {
        public static final String LINKS = "links";
        public static final String DATA = "data";
        public static final String ITEMS = "items";
        public static final String QUERY_TEMPLATE = "queryTemplate";
        public static final String TEMPLATE = "template";
    }

    private SuccessRepresentation(Builder builder) {
        representation.put(CS.LINKS, builder.links);

        if(builder.data != null){
           representation.put(CS.DATA, builder.data);
        }

        if(builder.showItems || CollectionUtils.isNotEmpty(builder.items)){
           representation.put(CS.ITEMS, builder.items);
        }

        if(builder.queryTemplate != null){
           representation.put(CS.QUERY_TEMPLATE, builder.queryTemplate);
        }

        if(builder.template != null){
           representation.put(CS.TEMPLATE, builder.template);
        }
    }

    private Map<String, Object> getRepresentation() {
        return representation;
    }

    public static class Builder {
        private List<Link> links =  new ArrayList<Link>();
        private Object data;
        private List<Object> items = new ArrayList<Object>();
        private boolean showItems = false;
        private Object queryTemplate;
        private Object template;

        public Builder links(List<Link> links){
            this.links = links;
            return this;
        }

        public Builder addLink(Link link){
            this.links.add(link);
            return this;
        }

        public Builder data(Object data){
            this.data = data;
            return this;
        }

        public Builder items(List<Object> items){
            this.items = items;
            this.showItems = true;
            return this;
        }

        public Builder queryTemplate(Object queryTemplate){
            this.queryTemplate = queryTemplate;
            return this;
        }

        public Builder template(Object template){
            this.template = template;
            return this;
        }

        public  Map<String, Object> build(){
            SuccessRepresentation successRepresentation = new SuccessRepresentation(this);
            return  successRepresentation.getRepresentation();
        }

    }

}
