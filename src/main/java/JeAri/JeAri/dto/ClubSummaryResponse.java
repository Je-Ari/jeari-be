package JeAri.JeAri.dto;

import JeAri.JeAri.model.Club;
import JeAri.JeAri.model.ClubCategory;
import JeAri.JeAri.model.RecruitmentStatus;
import lombok.Getter;

@Getter
public class ClubSummaryResponse {
    private final Integer id;
    private final String name;
    private final String description;
    private final ClubCategory category;
    private final String subcategory;
    private final String recruitmentStatus;

    public ClubSummaryResponse(Club club, RecruitmentStatus status) {
        this.id = club.getClubId();
        this.name = club.getName();
        this.description = club.getDescription();
        this.category = club.getCategory();
        this.subcategory = club.getSubcategory();
        this.recruitmentStatus = status.getLabel();
    }
}
