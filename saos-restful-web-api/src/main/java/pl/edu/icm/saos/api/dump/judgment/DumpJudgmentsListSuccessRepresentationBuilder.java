package pl.edu.icm.saos.api.dump.judgment;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.icm.saos.api.dump.judgment.item.representation.CommonCourtJudgmentItem;
import pl.edu.icm.saos.api.dump.judgment.item.representation.JudgmentItem;
import pl.edu.icm.saos.api.dump.judgment.item.representation.SupremeCourtJudgmentItem;
import pl.edu.icm.saos.api.dump.judgment.mapping.DumpCommonCourtJudgmentItemMapper;
import pl.edu.icm.saos.api.dump.judgment.mapping.DumpJudgmentItemMapper;
import pl.edu.icm.saos.api.dump.judgment.mapping.DumpSupremeCourtJudgmentItemMapper;
import pl.edu.icm.saos.api.dump.judgment.parameters.RequestDumpJudgmentsParameters;
import pl.edu.icm.saos.api.dump.judgment.views.DumpJudgmentsView;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.services.dates.DateMapping;
import pl.edu.icm.saos.api.services.representations.success.template.JudgmentDateFromTemplate;
import pl.edu.icm.saos.api.services.representations.success.template.JudgmentDateToTemplate;
import pl.edu.icm.saos.api.services.representations.success.template.PageNumberTemplate;
import pl.edu.icm.saos.api.services.representations.success.template.PageSizeTemplate;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.ApiConstants.*;
import static pl.edu.icm.saos.api.dump.judgment.views.DumpJudgmentsView.*;

/**
 * Provides functionality for building success object view for dump list of judgments.
 * @author pavtel
 */
@Service
public class DumpJudgmentsListSuccessRepresentationBuilder {

    @Autowired
    private DumpJudgmentItemMapper judgmentItemMapper;

    @Autowired
    private DumpCommonCourtJudgmentItemMapper commonCourtJudgmentItemMapper;

    @Autowired
    private DumpSupremeCourtJudgmentItemMapper supremeCourtJudgmentItemMapper;


    @Autowired
    private DateMapping dateMapping;



    //------------------------ LOGIC --------------------------

    /**
     * Constructs {@link pl.edu.icm.saos.api.dump.judgment.views.DumpJudgmentsView DumpJudgmentsView}.
     * @param searchResult to process.
     * @param pagination to process.
     * @param requestDumpJudgmentsParameters used to create queryTemplate.
     * @param uriComponentsBuilder used to create links.
     * @return representation.
     */
    public DumpJudgmentsView build(SearchResult<Judgment> searchResult, Pagination pagination, RequestDumpJudgmentsParameters requestDumpJudgmentsParameters, UriComponentsBuilder uriComponentsBuilder){
        DumpJudgmentsView dumpJudgmentsView = new DumpJudgmentsView();
        dumpJudgmentsView.setItems(toItems(searchResult.getResultRecords()));

        String startDate = dateMapping.toISO8601Format(requestDumpJudgmentsParameters.getJudgmentStartDate());
        String endDate = dateMapping.toISO8601Format(requestDumpJudgmentsParameters.getJudgmentEndDate());
        String modificationDate = dateMapping.toStringWithZoneUTC(requestDumpJudgmentsParameters.getSinceModificationDate());

        dumpJudgmentsView.setLinks(toLinks(pagination, startDate, endDate, modificationDate, uriComponentsBuilder, searchResult.isMoreRecordsExist()));
        dumpJudgmentsView.setQueryTemplate(toQueryTemplate(pagination, startDate, endDate, modificationDate));

        return dumpJudgmentsView;
    }

    //------------------------ PRIVATE --------------------------
    private List<JudgmentItem> toItems(List<Judgment> judgments) {
        List<JudgmentItem> items = judgments.stream()
                .map(judgment -> toItem(judgment))
                .collect(Collectors.toList());

        return items;
    }

    private JudgmentItem toItem(Judgment judgment){
        JudgmentItem item = initializeItemViewAndFillSpecificFields(judgment);
        judgmentItemMapper.fillJudgmentsFieldsToRepresentation(item, judgment);

        return item;
    }

    private JudgmentItem initializeItemViewAndFillSpecificFields(Judgment judgment){
        if(judgment.getCourtType() == CourtType.COMMON) {
            CommonCourtJudgment ccJudgment = (CommonCourtJudgment) judgment;
            CommonCourtJudgmentItem item = new CommonCourtJudgmentItem();
            commonCourtJudgmentItemMapper.fillJudgmentsFieldsToItemRepresentation(item, ccJudgment);
            return item;
        } else if (judgment.getCourtType() == CourtType.SUPREME){
            SupremeCourtJudgment scJudgment = (SupremeCourtJudgment) judgment;
            SupremeCourtJudgmentItem item = new SupremeCourtJudgmentItem();
            supremeCourtJudgmentItemMapper.fillJudgmentsFieldsToItemRepresentation(item, scJudgment);
            return item;
        } else {
            //default
            return new JudgmentItem();
        }
    }


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


    private QueryTemplate toQueryTemplate(Pagination pagination, String startDate, String endDate, String modificationDate) {

        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setPageNumber(new PageNumberTemplate(pagination.getPageNumber()));
        queryTemplate.setPageSize(new PageSizeTemplate(pagination.getPageSize()));
        queryTemplate.setJudgmentStartDate(new JudgmentDateFromTemplate(StringUtils.trimToEmpty(startDate)));
        queryTemplate.setJudgmentEndDate(new JudgmentDateToTemplate(StringUtils.trimToEmpty(endDate)));
        queryTemplate.setSinceModificationDate(new SinceModificationDateTemplate(StringUtils.trimToEmpty(modificationDate)));

        return queryTemplate;
    }

    //------------------------ SETTERS --------------------------

    public void setJudgmentItemMapper(DumpJudgmentItemMapper judgmentItemMapper) {
        this.judgmentItemMapper = judgmentItemMapper;
    }

    public void setCommonCourtJudgmentItemMapper(DumpCommonCourtJudgmentItemMapper commonCourtJudgmentItemMapper) {
        this.commonCourtJudgmentItemMapper = commonCourtJudgmentItemMapper;
    }

    public void setDateMapping(DateMapping dateMapping) {
        this.dateMapping = dateMapping;
    }
}
