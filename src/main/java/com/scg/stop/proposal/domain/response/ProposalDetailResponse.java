package com.scg.stop.proposal.domain.response;

import com.scg.stop.file.domain.File;
import com.scg.stop.file.dto.response.FileResponse;
import com.scg.stop.project.domain.ProjectType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ProposalDetailResponse {
    private Long id;
    private String authorName;
    private String email;
    private String webSite;
    private String title;
    private List<ProjectType> projectTypes;
    private String content;
    private Boolean replied;
    private List<FileResponse> files;

    public static ProposalDetailResponse of(Long id, String authorName, String email, String webSite, String title,
                                            List<ProjectType> projectTypes, String content, Boolean replied,
                                            List<FileResponse> files) {
        return new ProposalDetailResponse(
                id,
                authorName,
                email,
                webSite,
                title,
                projectTypes,
                content,
                replied,
                files
        );
    }
}
