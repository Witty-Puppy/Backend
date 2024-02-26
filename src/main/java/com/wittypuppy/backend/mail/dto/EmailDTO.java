package com.wittypuppy.backend.mail.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class EmailDTO {
    private Long emailCode;
    private String emailReadStatus;

    private String emailTitle;
    private String emailContent;

    private String  emailSendTime;
    private LocalDateTime emailReservationTime;
    private String emailStatus;
    private EmployeeDTO emailSender;    //이 사람이 메서드를 호출하는 거니까 security에서 얻어올 수 있다.
    private EmployeeDTO emailReceiver;
    private List<EmailAttachmentDTO> attachments;
}
