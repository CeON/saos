package pl.edu.icm.saos.importer.common;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;

import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.SourceCode;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentSourceInfoAssertUtils {

    public static void assertSourceInfo(JudgmentSourceInfo oldSourceInfo, JudgmentSourceInfo newSourceInfo,
            DateTime newPublicationDate, String newPublisher,
            String newReviser, String newSourceJudgmentId, String newSourceUrl,
            SourceCode newSourceType) {
        assertEquals(newPublicationDate, oldSourceInfo.getPublicationDate());
        assertEquals(newPublisher, oldSourceInfo.getPublisher());
        assertEquals(newReviser, oldSourceInfo.getReviser());
        assertEquals(newSourceJudgmentId, oldSourceInfo.getSourceJudgmentId());
        assertEquals(newSourceUrl, oldSourceInfo.getSourceJudgmentUrl());
        assertEquals(newSourceType, oldSourceInfo.getSourceCode());
        
        assertEquals(newPublicationDate, newSourceInfo.getPublicationDate());
        assertEquals(newPublisher, newSourceInfo.getPublisher());
        assertEquals(newReviser, newSourceInfo.getReviser());
        assertEquals(newSourceJudgmentId, newSourceInfo.getSourceJudgmentId());
        assertEquals(newSourceUrl, newSourceInfo.getSourceJudgmentUrl());
        assertEquals(newSourceType, newSourceInfo.getSourceCode());
    }
}
