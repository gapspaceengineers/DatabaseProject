package com.project.test.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A FooTable.
 */
@Entity
@Table(name = "foo_table")
public class FooTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long code;

    @Column(name = "source")
    private String source;

    @Column(name = "code_list_code")
    private String codeListCode;

    @Column(name = "display_value")
    private String displayValue;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "from_date")
    private String fromDate;

    @Column(name = "to_date")
    private String toDate;

    @Column(name = "sorting_priority")
    private Long sortingPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public FooTable code(Long code) {
        this.code = code;
        return this;
    }

    public String getSource() {
        return this.source;
    }

    public FooTable source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCodeListCode() {
        return this.codeListCode;
    }

    public FooTable codeListCode(String codeListCode) {
        this.codeListCode = codeListCode;
        return this;
    }

    public void setCodeListCode(String codeListCode) {
        this.codeListCode = codeListCode;
    }

    public String getDisplayValue() {
        return this.displayValue;
    }

    public FooTable displayValue(String displayValue) {
        this.displayValue = displayValue;
        return this;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getLongDescription() {
        return this.longDescription;
    }

    public FooTable longDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getFromDate() {
        return this.fromDate;
    }

    public FooTable fromDate(String fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return this.toDate;
    }

    public FooTable toDate(String toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Long getSortingPriority() {
        return this.sortingPriority;
    }

    public FooTable sortingPriority(Long sortingPriority) {
        this.sortingPriority = sortingPriority;
        return this;
    }

    public void setSortingPriority(Long sortingPriority) {
        this.sortingPriority = sortingPriority;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FooTable)) {
            return false;
        }
        return code != null && code.equals(((FooTable) o).code);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FooTable{" +
            "code=" + getCode() +
            ", source='" + getSource() + "'" +
            ", codeListCode='" + getCodeListCode() + "'" +
            ", displayValue='" + getDisplayValue() + "'" +
            ", longDescription='" + getLongDescription() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", sortingPriority=" + getSortingPriority() +
            "}";
    }
}
