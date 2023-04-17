package mappers;


import model.Case;
import model.CaseDTO;
import model.Donation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaseToCaseDTOMapper {
    public List<CaseDTO> convert (List<Case> cases, List<Donation> donations) {
        List<CaseDTO> caseDTOList = new ArrayList<>();
        Map<Integer, Double> caseToSum = new HashMap<>();
        cases.forEach((theCase)->{
            caseToSum.put(theCase.getId(), (double) 0);
        });
        donations.forEach((donation -> {
            if (caseToSum.containsKey(donation.getDonationCase().getId())) {
                double raisedSum = caseToSum.get(donation.getDonationCase().getId());
                raisedSum += donation.getAmountDonated();
                caseToSum.replace(donation.getDonationCase().getId(), raisedSum);
            }
        }));
        cases.forEach((theCase) -> {
            Integer caseId = theCase.getId();
            String personInNeed = theCase.getPersonInNeed();
            String description = theCase.getDescription();
            double raisedSum = caseToSum.get(caseId);
            double necessarySum = theCase.getNecessarySum();
            caseDTOList.add(new CaseDTO(caseId, description, personInNeed, raisedSum, necessarySum));
        });
        return caseDTOList;
    }
}
