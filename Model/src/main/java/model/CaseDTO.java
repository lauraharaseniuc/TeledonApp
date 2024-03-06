package model;

import java.io.Serializable;
import java.util.Objects;

public class CaseDTO implements Entity<Integer>, Serializable {
    private Integer id;
    private String description;
    private String personInNeed;
    private double raisedSum;
    private double necessaryAmount;
    private String formatted;

    public String formatCaseDTO() {
        return "Beneficiar: "+this.getPersonInNeed()+"                        "+this.getRaisedSum()+" / "
                +this.getNecessaryAmount()+"\n"+this.getDescription();
    }

    public void addToRaisedSum (double sumToAdd) {
        this.raisedSum += sumToAdd;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    public CaseDTO(Integer id, String description, String personInNeed, double raisedSum, double necessaryAmount) {
        this.id = id;
        this.description = description;
        this.personInNeed = personInNeed;
        this.raisedSum = raisedSum;
        this.necessaryAmount = necessaryAmount;
        this.formatted=this.formatCaseDTO();
    }

    public String getDescription() {
        return description;
    }

    public String getFormatted() {
        return formatted;
    }

    public double getNecessaryAmount() {
        return necessaryAmount;
    }

    public String getPersonInNeed() {
        return personInNeed;
    }

    public double getRaisedSum() {
        return raisedSum;
    }

    @Override
    public Integer getId() {
        return this.id;
    }


    @Override
    public void setId(Integer integer) {
        this.id=integer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaseDTO caseDTO)) return false;
        return getId().equals(caseDTO.getId()) && Objects.equals(getDescription(), caseDTO.getDescription()) && Objects.equals(getPersonInNeed(), caseDTO.getPersonInNeed()) && Objects.equals(getFormatted(), caseDTO.getFormatted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getPersonInNeed(), getRaisedSum(), getNecessaryAmount(), getFormatted());
    }
}
