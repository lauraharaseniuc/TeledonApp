package model;

import java.io.Serializable;

public class CaseDTO implements Entity<Integer>, Serializable {
    private Integer id;
    private String description;
    private String personInNeed;
    private double raisedSum;
    private double necessaryAmount;
    private String formatted;

    private String formatCaseDTO() {
        return "Beneficiar: "+this.getPersonInNeed()+"                        "+this.getRaisedSum()+" / "
                +this.getNecessaryAmount()+"\n"+this.getDescription();
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
}
