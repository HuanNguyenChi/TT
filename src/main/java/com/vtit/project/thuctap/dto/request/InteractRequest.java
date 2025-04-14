package com.vtit.project.thuctap.dto.request;

import com.vtit.project.thuctap.constant.enums.EStatusInteract;
import lombok.Data;

@Data
public class InteractRequest {
    private Long idUser;
    private Long idPost;
    private EStatusInteract status;

}
