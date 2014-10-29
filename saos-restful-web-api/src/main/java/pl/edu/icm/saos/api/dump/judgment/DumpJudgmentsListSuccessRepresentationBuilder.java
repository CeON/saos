package pl.edu.icm.saos.api.dump.judgment;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.icm.saos.api.dump.judgment.assemblers.DumpJudgmentAssembler;
import pl.edu.icm.saos.api.dump.judgment.parameters.RequestDumpJudgmentsParameters;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.services.dates.DateMapping;
import pl.edu.icm.saos.api.services.representations.SuccessRepresentationDep;
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
@Service
public class DumpJudgmentsListSuccessRepresentationBuilder {

    @Autowired
    private DumpJudgmentAssembler dumpJudgmentAssembler;

    @Autowired
    private DateMapping dateMapping;


    //------------------------ LOGIC --------------------------
    public Map<String, Object> build(SearchResult<Judgment> searchResult, Pagination pagination, RequestDumpJudgmentsParameters requestDumpJudgmentsParameters, UriComponentsBuilder uriComponentsBuilder){
        SuccessRepresentationDep.Builder builder = new SuccessRepresentationDep.Builder();
        String startDate = dateMapping.toISO8601Format(requestDumpJudgmentsParameters.getJudgmentStartDate());
        String endDate = dateMapping.toISO8601Format(requestDumpJudgmentsParameters.getJudgmentEndDate());
        String modificationDate = dateMapping.toStringWithZoneUTC(requestDumpJudgmentsParameters.getSinceModificationDate());

        builder.links(toLinks(pagination, startDate, endDate, modificationDate, uriComponentsBuilder, searchResult.isMoreRecordsExist()));
        builder.items(toItems(searchResult.getResultRecords()));
        builder.queryTemplate(toQueryTemplate(pagination, startDate, endDate, modificationDate));

        return builder.build();
    }



    //------------------------ PRIVATE --------------------------
    private List<Link> toLinks(Pagination pagination, String startDate, String endDate, String modificationDate, UriComponentsBuilder uriComponentsBuilder, boolean hasMore) {
        List<Link> links = new LinkedList<>();


        links.add(buildLink(pagination, startDate, endDate ,modificationDate, SELF, uriComponentsBuilder));

        if(pagination.hasPrevious()){
            links.add(buildLink(pagination.getPrevious(), startDate, endDate,  modificationDate, Link.REL_PREVIOUS, uriComponentsBuilder));
        }

        if(hasMore){
            links.add(buildLink(pagination.getNext(), startDate, endDate, modificationDate, Link.REL_NEXT, uriComponentsBuilder));
        }


        return links;
    }


    private Link buildLink(Pagination pagination, String startDate, String endDate, String modificationDate, String relName, UriComponentsBuilder uriComponentsBuilder) {

        uriComponentsBuilder
                .replaceQueryParam(PAGE_SIZE, pagination.getPageSize())
                .replaceQueryParam(PAGE_NUMBER, pagination.getPageNumber());

        if(StringUtils.isNotBlank(startDate)){
            uriComponentsBuilder.replaceQueryParam(JUDGMENT_START_DATE, startDate);
        }

        if(StringUtils.isNotBlank(endDate)){
            uriComponentsBuilder.replaceQueryParam(JUDGMENT_END_DATE, endDate);
        }

        if(StringUtils.isNotBlank(modificationDate)){
            uriComponentsBuilder.replaceQueryParam(SINCE_MODIFICATION_DATE, modificationDate);
        }

        String path = uriComponentsBuilder.build().encode().toUriString();
        return new Link(path, relName);
    }

    private List<Object> toItems(List<Judgment> resultRecords) {
        return dumpJudgmentAssembler.toItemsList(resultRecords);
    }

    private Object toQueryTemplate(Pagination pagination, String startDate, String endDate, String modificationDate) {
        Map<String, Object> queryTemplate = new LinkedHashMap<String, Object>();

        queryTemplate.put(PAGE_NUMBER, pagination.getPageNumber());
        queryTemplate.put(PAGE_SIZE, pagination.getPageSize());
        queryTemplate.put(JUDGMENT_START_DATE, StringUtils.trimToEmpty(startDate));
        queryTemplate.put(JUDGMENT_END_DATE, StringUtils.trimToEmpty(endDate));
        queryTemplate.put(SINCE_MODIFICATION_DATE, StringUtils.trimToEmpty(modificationDate));

        return queryTemplate;
    }

    //------------------------ SETTERS --------------------------
    public void setDumpJudgmentAssembler(DumpJudgmentAssembler dumpJudgmentAssembler) {
        this.dumpJudgmentAssembler = dumpJudgmentAssembler;
    }

    public void setDateMapping(DateMapping dateMapping) {
        this.dateMapping = dateMapping;
    }
}
