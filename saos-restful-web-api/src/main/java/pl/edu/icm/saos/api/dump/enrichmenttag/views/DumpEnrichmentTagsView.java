package pl.edu.icm.saos.api.dump.enrichmenttag.views;

import java.io.Serializable;

import pl.edu.icm.saos.api.dump.enrichmenttag.views.DumpEnrichmentTagsView.DumpEnrichmentTagItem;
import pl.edu.icm.saos.api.dump.enrichmenttag.views.DumpEnrichmentTagsView.Info;
import pl.edu.icm.saos.api.dump.enrichmenttag.views.DumpEnrichmentTagsView.QueryTemplate;
import pl.edu.icm.saos.api.services.representations.success.CollectionRepresentation;
import pl.edu.icm.saos.api.services.representations.success.template.PageNumberTemplate;
import pl.edu.icm.saos.api.services.representations.success.template.PageSizeTemplate;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.google.common.base.Objects;

public class DumpEnrichmentTagsView extends CollectionRepresentation<DumpEnrichmentTagItem, QueryTemplate, Info> {

    private static final long serialVersionUID = -2750343176196682357L;
    

    public static class DumpEnrichmentTagItem implements Serializable {
        
        private static final long serialVersionUID = 3734544451856110990L;
        
        private int id;
        private int judgmentId;
        private String tagType;
        private String value;
        
        
        //------------------------ GETTERS --------------------------
        
        public int getId() {
            return id;
        }
        
        public int getJudgmentId() {
            return judgmentId;
        }
        
        public String getTagType() {
            return tagType;
        }
        
        @JsonRawValue
        public String getValue() {
            return value;
        }
        
        
        //------------------------ SETTERS --------------------------
        
        public void setId(int id) {
            this.id = id;
        }
        
        public void setJudgmentId(int judgmentId) {
            this.judgmentId = judgmentId;
        }
        
        public void setTagType(String tagType) {
            this.tagType = tagType;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
        
        
        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(id, judgmentId, tagType, value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final DumpEnrichmentTagItem other = (DumpEnrichmentTagItem) obj;
            return Objects.equal(this.id, other.id) &&
                    Objects.equal(this.judgmentId, other.judgmentId) &&
                    Objects.equal(this.tagType, other.tagType) &&
                    Objects.equal(this.value, other.value);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("judgmentId", judgmentId)
                    .add("tagType", tagType)
                    .add("value", value)
                    .toString();
        }
    }
    
    
    public static class QueryTemplate implements Serializable {
        private static final long serialVersionUID = -9175231935730751891L;

        private PageSizeTemplate pageSize;
        private PageNumberTemplate pageNumber;

        //------------------------ GETTERS --------------------------

        public PageSizeTemplate getPageSize() {
            return pageSize;
        }

        public PageNumberTemplate getPageNumber() {
            return pageNumber;
        }


        //------------------------ SETTERS --------------------------

        public void setPageSize(PageSizeTemplate pageSize) {
            this.pageSize = pageSize;
        }

        public void setPageNumber(PageNumberTemplate pageNumber) {
            this.pageNumber = pageNumber;
        }


        //------------------------ HashCode & Equals --------------------------

        @Override
        public int hashCode() {
            return Objects.hashCode(pageSize, pageNumber);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final QueryTemplate other = (QueryTemplate) obj;
            return Objects.equal(this.pageSize, other.pageSize) &&
                    Objects.equal(this.pageNumber, other.pageNumber);
        }

        //------------------------ toString --------------------------

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("pageSize", pageSize)
                    .add("pageNumber", pageNumber)
                    .toString();
        }
    }
    
    public static class Info implements Serializable {
        private static final long serialVersionUID = 7389760412678772304L;
    }
}
