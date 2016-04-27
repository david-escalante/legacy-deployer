package org.craftercms.cstudio.publishing.utils.xml;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 * {@link DocumentProcessor} that renames elements or fields based on mappings, where a mapping is of the form:
 * XPath of fields to rename -> new field name.
 *
 * @author avasquez
 */
public class FieldRenamingDocumentProcessor implements DocumentProcessor {

    private static final Log logger = LogFactory.getLog(FieldRenamingDocumentProcessor.class);

    private Map<String, String> fieldMappings;

    public void setFieldMappings(Map<String, String> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

    @Override
    public Document process(Document document, File file, String rootFolder) {
        if (MapUtils.isNotEmpty(fieldMappings)) {
            for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
                String xpath = entry.getKey();
                String newName = entry.getValue();

                if (logger.isDebugEnabled()) {
                    logger.debug("Renaming elements that match XPath " + xpath + " to '" + newName + "' for file " +
                                 file + "...");
                }

                List<Element> elements = document.selectNodes(xpath);
                if (CollectionUtils.isNotEmpty(elements)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Number of elements found to rename: " + elements.size());
                    }

                    for (Element element : elements) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Renaming element " + element.getUniquePath());
                        }

                        element.setName(newName);

                        if (logger.isDebugEnabled()) {
                            logger.debug("Element renamed to " + element.getUniquePath());
                        }
                    }
                }
            }
        }

        return document;
    }

}
