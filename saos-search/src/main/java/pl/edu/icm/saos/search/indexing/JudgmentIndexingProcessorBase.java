package pl.edu.icm.saos.search.indexing;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.saos.search.config.model.IndexFieldsConstants;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;

public abstract class JudgmentIndexingProcessorBase {

    private static Logger log = LoggerFactory.getLogger(JudgmentIndexingProcessorBase.class);
    
    protected void addField(SolrInputDocument doc, JudgmentIndexField field, String value) {
        if (StringUtils.isNotBlank(value)) {
            doc.addField(field.getFieldName(), value);
        }
    }
    
    protected void addField(SolrInputDocument doc, JudgmentIndexField field, String fieldPostfix, String value) {
        if (!field.isPostfixAllowed()) {
            log.warn("Trying to index field [{}] with postfix [{}], but field is not marked as dynamic. Ommiting.",
                    field.getFieldName(), fieldPostfix);
        }
        if (StringUtils.isNotBlank(value)) {
            doc.addField(field.getFieldName() + IndexFieldsConstants.FIELD_SEPARATOR + fieldPostfix, value);
        }
    }
    
    protected void addDateField(SolrInputDocument doc, JudgmentIndexField field, LocalDate date) {
        if (date != null) {
            String dateString = SearchDateTimeUtils.convertDateToISOString(date);
            doc.addField(field.getFieldName(), dateString);
        }
    }
}
