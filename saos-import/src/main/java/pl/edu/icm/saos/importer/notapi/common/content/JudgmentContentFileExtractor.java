package pl.edu.icm.saos.importer.notapi.common.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
                
            } catch (IOException | ImportException e) {
                if (inputStream != null) {
                    inputStream.close();
                }
                throw e;
            }

        }
        
        throw new ImportException("File " + archiveFile.getName() + " is of type that is not supported");
        
    }
}
