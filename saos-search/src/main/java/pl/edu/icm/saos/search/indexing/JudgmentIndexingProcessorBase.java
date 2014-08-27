package pl.edu.icm.saos.search.indexing;

import org.apache.solr.common.SolrInputDocument;
import org.joda.time.DateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.saos.search.config.model.IndexFieldsConstants;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

public abstract class JudgmentIndexingProcessorBase {

    private static Logger log = LoggerFactory.getLogger(JudgmentIndexingProcessorBase.class);
    
    protected void addField(SolrInputDocument doc, JudgmentIndexField field, String value) {
        doc.addField(field.getFieldName(), value);
    }
    
    protected void addField(SolrInputDocument doc, JudgmentIndexField field, String fieldPostfix, String value) {
        if (!field.isPostfixAllowed()) {
            log.warn("Trying to index field [{}] with postfix [{}], but field is not marked as dynamic. Ommiting.",
                    field.getFieldName(), fieldPostfix);
        }
        doc.addField(field.getFieldName() + IndexFieldsConstants.FIELD_SEPARATOR + fieldPostfix, value);
    }
    
    protected void addDateField(SolrInputDocument doc, JudgmentIndexField field, ReadablePartial date) {
        
        String dateString = ISODateTimeFormat
                .dateTimeNoMillis()
                .withZoneUTC()
                .print(date.toDateTime(new DateTime(1900, 1, 1, 0, 0)));
        doc.addField(field.getFieldName(), dateString);
    }
}
