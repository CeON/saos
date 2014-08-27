package pl.edu.icm.saos.search.indexing;

import org.apache.solr.common.SolrInputDocument;

import pl.edu.icm.saos.persistence.model.Judgment;

public interface SpecificJudgmentIndexingProcessor<J extends Judgment> {

    void process(SolrInputDocument doc, J judgment);
    
}
