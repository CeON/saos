package pl.edu.icm.saos.webapp.analysis.request.converter;

import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentGlobalFilter;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;

/**
 * A contract for classes that can generate {@link ChartCode} specific {@link XSettings}.
 * @author Łukasz Dumiszewski
 */

public interface XSettingsGenerator {

    /**
     * Generates {@link XSettings} for the passed judgmentGlobalFilter
     * @throws IllegalArgumentException if the generator can not generate x-settings
     * for the given judgmentGlobalFilter, see: {@link #canGenerateXSettings(JudgmentGlobalFilter)}
     */
    XSettings generateXSettings(JudgmentGlobalFilter judgmentGlobalFilter);
    
    /**
     * Checks whether the generator is able to generate {@link XSettings} for the given
     * judgmentGlobalFilter. It may happen that the passed filter does not contain values
     * needed to create {@link XSettings} for the {@link ChartCode} handled by this generator.
     */
    boolean canGenerateXSettings(JudgmentGlobalFilter judgmentGlobalFilter);
    
    /**
     * Returns true if the generator handles creating x-settings for the passed chartCode
     */
    boolean handles(ChartCode chartCode);
    
}
