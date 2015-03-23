package pl.edu.icm.saos.importer.notapi.common.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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
     * Extracts judgment content from archive file.
     * At the moment only zip files are supported.
     */
    public InputStreamWithFilename extractJudgmentContent(File archiveFile, String judgmentSourceId) {
        
        if (FilenameUtils.getExtension(archiveFile.getName()).equals("zip")) {
            ZipInputStream inputStream = null;
            try {
                inputStream = new ZipInputStream(new FileInputStream(archiveFile));

                ZipEntry entry = inputStream.getNextEntry();

                while(true) {
                    if (entry == null) {
                        break;
                    }

                    if (FilenameUtils.getBaseName(entry.getName()).equals(judgmentSourceId)) {
                        return new InputStreamWithFilename(inputStream, entry.getName());
                    }

                    entry = inputStream.getNextEntry();

                }
                throw new ImportException("Content for judgment with sourceId " + judgmentSourceId + " not found in file " + archiveFile.getName());
                
            } catch (IOException e) {
                IOUtils.closeQuietly(inputStream);
                throw new ImportException(e);
            }

        }
        
        throw new ImportException("File " + archiveFile.getName() + " is of type that is not supported");
        
    }
}
