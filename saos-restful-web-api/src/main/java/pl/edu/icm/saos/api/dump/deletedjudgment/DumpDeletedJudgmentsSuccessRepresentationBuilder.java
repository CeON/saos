package pl.edu.icm.saos.api.dump.deletedjudgment;

import static pl.edu.icm.saos.api.ApiConstants.SELF;

import java.util.LinkedList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.api.dump.deletedjudgment.views.DumpDeletedJudgmentsView;

/**
 * Builder of a success object view for a dump list of deleted judgments.
 * @author Łukasz Dumiszewski
 */
@Service
public class DumpDeletedJudgmentsSuccessRepresentationBuilder {



    
    //------------------------ LOGIC --------------------------
    /**
     * Constructs {@link pl.edu.icm.saos.api.dump.court.views.DumpDeletedJudgmentsView DumpDeletedJudgmentsView}.
     * @param deletedJudgmentIds list of ids of deleted judgments
     * @param uriComponentsBuilder used to construct links.
     */
    public DumpDeletedJudgmentsView build(List<Long> deletedJudgmentIds, UriComponentsBuilder uriComponentsBuilder){
        
        Preconditions.checkNotNull(deletedJudgmentIds);
        Preconditions.checkNotNull(uriComponentsBuilder);
        
        DumpDeletedJudgmentsView dumpDeletedJudgmentsView = new DumpDeletedJudgmentsView();
        
        dumpDeletedJudgmentsView.setLinks(toLinks(uriComponentsBuilder));
        dumpDeletedJudgmentsView.setItems(deletedJudgmentIds);
        
        
        return dumpDeletedJudgmentsView;
    }

    
    //------------------------ PRIVATE --------------------------
    
    
    private List<Link> toLinks(UriComponentsBuilder uriComponentsBuilder) {
        List<Link> links = new LinkedList<>();
        links.add(buildLink(SELF, uriComponentsBuilder));
        return links;
    }


    private Link buildLink(String relName, UriComponentsBuilder uriComponentsBuilder) {
        String path = uriComponentsBuilder.build().encode().toUriString();
        return new Link(path, relName);
    }
 
}
