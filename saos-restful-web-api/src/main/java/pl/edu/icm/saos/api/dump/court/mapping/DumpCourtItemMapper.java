package pl.edu.icm.saos.api.dump.court.mapping;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.dump.court.views.DumpCourtsView;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.dump.court.views.DumpCourtsView.Item;

/**
 * Provides functionality for mapping {@link pl.edu.icm.saos.persistence.model.CommonCourt CommonCourt}
 * into {@link pl.edu.icm.saos.api.dump.court.views.DumpCourtsView.Item Item}.
 * @author pavtel
 */
@Service
public class DumpCourtItemMapper {


    //------------------------ LOGIC --------------------------

    /**
     * Fills item's fields using {@link pl.edu.icm.saos.persistence.model.CommonCourt CommonCourt} fields values.
     * @param item representation.
     * @param court to process.
     */
    public void fillCommonCourtFieldsToItemRepresentation(Item item, CommonCourt court){
        item.setId(court.getId());
        item.setName(court.getName());
        item.setType(court.getType());
        item.setCode(court.getCode());

        if(court.getParentCourt()!=null){
            DumpCourtsView.ParentCourt parentCourt = new DumpCourtsView.ParentCourt();
            parentCourt.setId(court.getParentCourt().getId());
            item.setParentCourt(parentCourt);
        }

        item.setDivisions(toDivisions(court.getDivisions()));
    }

    //------------------------ PRIVATE --------------------------

    private List<DumpCourtsView.Division> toDivisions(List<CommonCourtDivision> divisions) {
        if(divisions == null){
            divisions = Collections.emptyList();
        }

        List<DumpCourtsView.Division> divisionsRepresentation = divisions.stream()
                .map(division -> toDivisionView(division))
                .collect(Collectors.toList());

        return divisionsRepresentation;
    }


    private DumpCourtsView.Division toDivisionView(CommonCourtDivision division){
        DumpCourtsView.Division view = new DumpCourtsView.Division();
        view.setId(division.getId());
        view.setCode(division.getCode());
        view.setName(division.getName());
        view.setType(division.getType().getName());

        return view;
    }
}
