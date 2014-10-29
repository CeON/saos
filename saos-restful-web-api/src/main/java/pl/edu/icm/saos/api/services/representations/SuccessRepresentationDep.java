package pl.edu.icm.saos.api.services.representations;

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
public class SuccessRepresentationDep {

    private Map<String, Object> representation = new LinkedHashMap<String, Object>();

    private static class CS {
        public static final String LINKS = "links";
        public static final String DATA = "data";
        public static final String ITEMS = "items";
        public static final String QUERY_TEMPLATE = "queryTemplate";
        public static final String TEMPLATE = "template";
        public static final String INFO = "info";
    }

    private SuccessRepresentationDep(Builder builder) {
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

        if(builder.info != null){
            representation.put(CS.INFO, builder.info);
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
        private Object info;

        public Builder links(List<Link> links){
            this.links = links;
            return this;
        }

        public Builder info(Object info){
            this.info = info;
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
            SuccessRepresentationDep successRepresentation = new SuccessRepresentationDep(this);
            return  successRepresentation.getRepresentation();
        }

    }

}
