package pl.edu.icm.saos.api.single.judgment.views;

import pl.edu.icm.saos.api.services.representations.success.SingleElementRepresentation;
import pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData;

/**
 * Base class for judgment view that extends {@link pl.edu.icm.saos.persistence.model.Judgment Judgment}
 * @author pavtel
 */
public abstract class JudgmentView<SPECIAL_DATA extends JudgmentData> extends SingleElementRepresentation<SPECIAL_DATA>{

    private static final long serialVersionUID = 1549820512541021764L;
}
