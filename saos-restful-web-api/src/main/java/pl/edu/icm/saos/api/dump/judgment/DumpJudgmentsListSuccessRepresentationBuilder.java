package pl.edu.icm.saos.api.dump.judgment;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.icm.saos.api.dump.judgment.assemblers.DumpJudgmentAssembler;
import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.api.response.representations.SuccessRepresentation;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * @author pavtel
 */
@Component
public class DumpJudgmentsListSuccessRepresentationBuilder {

    @Autowired
    private DumpJudgmentAssembler dumpJudgmentAssembler;


    public Map<String, Object> build(SearchResult<Judgment> searchResult, Pagination pagination, String startDate, String endDate, UriComponentsBuilder uriComponentsBuilder){
        SuccessRepresentation.Builder builder = new SuccessRepresentation.Builder();
        builder.links(toLinks(pagination, startDate, endDate, uriComponentsBuilder, searchResult.isMoreRecordsExist()));
        builder.items(toItems(searchResult.getResultRecords()));
        builder.queryTemplate(toQueryTemplate(pagination, startDate, endDate));

        return builder.build();
    }



    private List<Link> toLinks(Pagination pagination, String startDate, String endDate, UriComponentsBuilder uriComponentsBuilder, boolean hasMore) {
        List<Link> links = new LinkedList<>();


        links.add(buildLink(pagination, startDate, endDate , SELF, uriComponentsBuilder));

        if(pagination.hasPrevious()){
            links.add(buildLink(pagination.getPrevious(), startDate, endDate, Link.REL_PREVIOUS, uriComponentsBuilder));
        }

        if(hasMore){
            links.add(buildLink(pagination.getNext(), startDate, endDate, Link.REL_NEXT, uriComponentsBuilder));
        }


        return links;
    }


    private Link buildLink(Pagination pagination, String startDate, String endDate, String relName, UriComponentsBuilder uriComponentsBuilder) {

        uriComponentsBuilder
                .replaceQueryParam(PAGE_SIZE, pagination.getPageSize())
                .replaceQueryParam(PAGE_NUMBER, pagination.getPageNumber());

        if(StringUtils.isNotBlank(startDate)){
            uriComponentsBuilder.replaceQueryParam(JUDGMENT_START_DATE, startDate);
        }

        if(StringUtils.isNotBlank(endDate)){
            uriComponentsBuilder.replaceQueryParam(JUDGMENT_END_DATE, endDate);
        }

        String path = uriComponentsBuilder.build().encode().toUriString();
        return new Link(path, relName);
    }

    private List<Object> toItems(List<Judgment> resultRecords) {
        return dumpJudgmentAssembler.toItemsList(resultRecords);
    }

    private Object toQueryTemplate(Pagination pagination, String startDate, String endDate) {
        Map<String, Object> queryTemplate = new LinkedHashMap<String, Object>();

        queryTemplate.put(PAGE_NUMBER, pagination.getPageNumber());
        queryTemplate.put(PAGE_SIZE, pagination.getPageSize());
        queryTemplate.put(JUDGMENT_START_DATE, StringUtils.trimToEmpty(startDate));
        queryTemplate.put(JUDGMENT_END_DATE, StringUtils.trimToEmpty(endDate));

        return queryTemplate;
    }

    //*** setters ***
    public void setDumpJudgmentAssembler(DumpJudgmentAssembler dumpJudgmentAssembler) {
        this.dumpJudgmentAssembler = dumpJudgmentAssembler;
    }
}
