package pl.edu.icm.saos.importer.notapi.common.content;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.ImportException;

/**
 * Extractor of judgment content files
 * 
 * @author madryk
 */
@Service
public class JudgmentContentFileExtractor {
    

    /**
     * Extracts judgment content file from archive file.
     * 
     * Looks in the given archiveFile for a file with a name (without extension)
     * equal to judgmentSourceId.
     * At the moment only zip archive files are supported.
     * 
     * @param archiveFile - archive file from which judgment content file can be extracted
     * @param judgmentSourceId - source id of judgment.
     * @return 
     * @throws IOException - when I/O error has occurred
     * @throws ImportException - when archive is of not supported type or it doesn't contain
     *     proper judgment content file
     */
    public InputStreamWithFilename extractJudgmentContent(File archiveFile, String judgmentSourceId) throws IOException {
        
        if (FilenameUtils.getExtension(archiveFile.getName()).equals("zip")) {
            ZipFile file = null;
            file = new ZipFile(archiveFile);
            
            final Enumeration<? extends ZipEntry> entries = file.entries();
            
            while(entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                
                if (FilenameUtils.getBaseName(entry.getName()).equals(judgmentSourceId)) {
                    return new ZipEntryBasedInputStreamWithFilename(file, entry);
                }
            }
            
            file.close();
            throw new ImportException("Content for judgment with sourceId " + judgmentSourceId + " not found in file " + archiveFile.getName());
        }
        
        throw new ImportException("File " + archiveFile.getName() + " is of type that is not supported");
        
    }
}
