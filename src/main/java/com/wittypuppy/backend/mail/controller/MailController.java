package com.wittypuppy.backend.mail.controller;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.config.scheduler.DynamicTaskScheduler;
import com.wittypuppy.backend.mail.dto.EmailDTO;
import com.wittypuppy.backend.mail.dto.EmployeeDTO;
import com.wittypuppy.backend.mail.service.EmailService;
import com.wittypuppy.backend.util.TokenUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.*;
//com.wittypuppy.backend.Employee.dto.EmployeeDTO
@Tag(name = "메일 스웨거 연동")
@RequestMapping("/mail")
@RestController
public class MailController {
    private final EmailService emailService;
    private final SimpMessagingTemplate simp;
    private final DynamicTaskScheduler dynamicTaskScheduler;
    private final RestTemplate restTemplate;

    public MailController(EmailService emailService, SimpMessagingTemplate simp, DynamicTaskScheduler dynamicTaskScheduler, RestTemplateBuilder restTemplateBuilder) {
        this.emailService = emailService;
        this.simp = simp;
        this.dynamicTaskScheduler = dynamicTaskScheduler;
        this.restTemplate = restTemplateBuilder.build();
    }
    @Tag(name = "이메일 검색", description = "똑같은 거 같은데")
    @GetMapping("/find-a-mail")
    public ResponseEntity<ResponseDTO> findAMail(@RequestParam Long emailCode){
        EmailDTO email = emailService.findById(emailCode);
        return res("메일을 검색했습니다.",email);
    }
    @Tag(name = "이메일 조회", description = "이메일 코드로 이메일 조회")
    @GetMapping("find-email-by-code")
    public ResponseEntity<ResponseDTO> findByEmailId(@RequestParam Long emailCode){
        System.out.println("여기 오는건 맞음?");
        EmailDTO email =  emailService.findById(emailCode);
        System.out.println("==============================================================="+email);
        return res("성공",email);
    }
    @Tag(name = "이메일 조회", description = "이메일 상태에 따라 조회")
    @GetMapping("/find-email")
    public ResponseEntity<ResponseDTO> findEmail(@RequestParam String word,@RequestParam String option,@RequestParam Integer page,@AuthenticationPrincipal User user){
        Pageable pageable = PageRequest.of(page, 12);

        EmployeeDTO me = new EmployeeDTO((long)user.getEmployeeCode());

        System.out.println("프론트에서 가져온 word는 : "+word);
        System.out.println("프론트에서 가져온 option은 : "+option);
        Long receiver = (long)user.getEmployeeCode();
        Page<EmailDTO> emailList = null;
                switch (option) {
            case "title" : emailList = emailService.findByEmailTitle(word,me,pageable); break;
            case "receiver" : emailList = emailService.findAllByEmailSender(word,receiver,pageable); break;
        }
        System.out.println(emailList);
        if(emailList == null){
            return resNull(1100,"검색된 메일이 없습니다.");
        }
        return res("메일을 검색했습니다.",emailList);
    }

    @Tag(name = "임시 저장 메일 조회", description = "임시 저장 메일 조회")
    @GetMapping("/temporary")
    public ResponseEntity<ResponseDTO> readTemporary(){
        //유저 코드 넣어주면서 임시저장한 거 찾아오기
        List<EmailDTO> emailDTO = emailService.findByEmailSender(new EmployeeDTO(1L));
        return res("성공",emailDTO);
    }



    @Tag(name = "안 읽은 메일 개수", description = "readStatus = N 인 메일 찾기")
    @GetMapping("unread-email-count")
    public Long countUnreadEmail(@AuthenticationPrincipal User user){
        return emailService.findByEmailReadStatusCount(user);
    }
    @Tag(name = "안읽은 메일 조회", description = "안읽은 이메일을 조회함")
    @GetMapping("/non-read-email")
    public ResponseEntity<ResponseDTO> findByEmailReadStatus(@AuthenticationPrincipal User user, @RequestParam Integer page){
        Pageable pageable = PageRequest.of(page, 12);
        EmployeeDTO employee = new EmployeeDTO((long)user.getEmployeeCode(),user.getEmployeeId());
        System.out.println(employee+"의 readStatus가 N인 것들을 찾아옵니다. "+pageable);
        Page<EmailDTO> emailDTO = emailService.findByEmailReadStatus(employee,pageable);
        for(EmailDTO email : emailDTO){
            System.out.println(email);
        }
        return res("성공",emailDTO);
    }
    @GetMapping("/to-me")
    public ResponseEntity<ResponseDTO> findByEmailSenderAndEmailReceiver(@AuthenticationPrincipal User user,@RequestParam Integer page){
        Pageable pageable = PageRequest.of(page,12);
        Page<EmailDTO> emailDTOs = emailService.findAllByEmailSenderAndEmailReceiver(user,pageable);
        return res("나에게 보낸 이메일 조회 성공",emailDTOs);
    }


    @GetMapping("getUser")
    public User getUser(@AuthenticationPrincipal User user){
        return user;
    }

    //예약 메일 등록 후 예약 처리

    @Tag(name = "받은 메일 조회", description = "받은 상태에 따라 조회하여 페이징 처리")
    @GetMapping("/find-receive-mail")
    public ResponseEntity<ResponseDTO> findReceiveMail(@RequestParam String condition,@RequestParam Integer page,@AuthenticationPrincipal User user){
        Pageable pageable = PageRequest.of(page, 12);

        System.out.println(condition);
        Page<EmailDTO> emailList = emailService.findReceiveMail(condition,user,pageable);

        if(condition.equals("temporary")){
            for(EmailDTO email : emailList){
                email.setEmailTitle("[임시저장] "+email.getEmailTitle());
            }
        }
        if(condition.equals("reserve")){
            for(EmailDTO email : emailList){
                email.setEmailTitle("[예약메일] "+email.getEmailTitle());
            }
        }
        if(condition.equals("trash")){
            for(EmailDTO email : emailList){
                email.setEmailTitle("[휴지통] "+email.getEmailTitle());
            }
        }
        return res("받은 이메일 조회 성공",emailList);
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        SimpleDateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
//        SimpleDateFormat outputFormat2 = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
//
//        if(emailList == null){
//            return resNull(1003,"받은 이메일이 없습니다.");
//        }
//        for(EmailDTO email : emailList) {
//            try {
//                Date date = inputFormat.parse(email.getEmailSendTime());
//                String formattedDate = outputFormat.format(date);
//                email.setEmailSendTime(formattedDate);
//            } catch (ParseException e) {
//                try {
//                    Date date = inputFormat2.parse(email.getEmailSendTime());
//                    String formattedDate2 = outputFormat2.format(date);
//                    email.setEmailSendTime(formattedDate2);
//                } catch (ParseException ex) {
//                    System.out.println(ex);
//                    return null;
//                }
//
//            }
//
//        }

    }

    @Tag(name = "보낸 메일 조회", description = "아직 사용 안함")
    @GetMapping("/find-send-mail")
    public ResponseEntity<ResponseDTO> findSendMail(@RequestParam String condition){
        List<EmailDTO> emailList = emailService.findSendMail(condition);
        if(emailList == null){
            return resNull(1004,"보낸 이메일이 없습니다.");
        }
        return res("보낸 이메일 조회 성공",emailList);
    }

    @Tag(name = "이메일 상태 변경", description = "가져온 status로 상태 변경")
    @PutMapping("/update-mail-status")
    public ResponseEntity<ResponseDTO> updateMailStatus(@RequestBody List<EmailDTO> emailDTOs, @RequestParam String status){
        //프론트에 키가 있으니 그걸 primary 키로 가져오자
        try {
            List<EmailDTO> emails = emailService.findByAllEmailCode(emailDTOs);

            System.out.println("DTO로 잘 가져왔니?");
            for (EmailDTO email : emails) {
                System.out.println(email);
                email.setEmailStatus(status);
            }
            try {
                emails = emailService.updateEmailStatus(emails);
            } catch (TransactionSystemException e) {
                System.out.println("트랜잭션 에러");
                return resNull(1007, "트랜젝션 중 에러가 발생했습니다.");
            }
            return res("이메일이 " + status + " 이(가) 되었습니다.", emails);

        } catch (EmptyResultDataAccessException e){
            System.out.println("체크한 이메일을 찾을 수 없습니다.");
            return resNull(1006,"이메일을 찾을 수 없습니다.");
        } catch (Exception e){
            System.out.println("알 수 없는 에러");
            return resNull(1999,"복합적인 에러가 발생했습니다.");
        }
    }
    @Tag(name = "중요 이메일 전환", description = "이메일의 상태를 important로 바꿈")
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/toggle-important")
    public ResponseEntity<ResponseDTO> updateStatus(@RequestParam Long emailCode, @RequestParam String emailStatus){
        EmailDTO emailDTO = emailService.findById(emailCode);
        System.out.println(emailDTO);
        EmailDTO result = emailService.updateStatus(emailDTO,emailStatus);
        return res("성공",result);
    }
    @Tag(name = "이메일 삭제", description = "이메일 상태를 trash로 변경")
    @PutMapping("/update-status")
    public ResponseEntity<ResponseDTO> updateMail(@RequestParam Long emailCode, @RequestParam String status){
        System.out.println("emailcode는 : "+emailCode);
        System.out.println("status는 : "+status);
        boolean result = emailService.updateEmailStatus(emailCode,status);
        return res("쓰레기통 버리기 성공",result);
    }
    @Tag(name = "이메일 완전 삭제", description = "아직 사용 안함")
    @DeleteMapping("/delete-mail")
    public ResponseEntity<ResponseDTO> deleteMail(@RequestBody List<EmailDTO> emails){
        try{
            emailService.deleteEmail(emails);
            return res("성공",null);
        } catch (EmptyResultDataAccessException e){
            return resNull(1005,"삭제하려는 이메일이 존재하지 않습니다.");
        } catch (Exception e){
            return resNull(1999,"복합적인 에러가 발생했습니다.");
        }
    }




    @Tag(name = "이메일 읽음 상태 업데이트", description = "이메일을 조회하면 Y로 바꿈")
    @PutMapping("/read-stats-y")
    public ResponseEntity<ResponseDTO> updateReadStatus(@RequestParam Long emailCode){
        EmailDTO emailDTO = emailService.findById(emailCode);
        emailDTO.setEmailReadStatus("Y");
        emailDTO = emailService.updateEmailReadStatus(emailDTO);
        return res("성공적으로 읽음",emailDTO);
    }
    @Tag(name = "이메일 읽음 상태 업데이트", description = "이메일을 다시 안읽은 상태로 변경")
    @PutMapping("/read-stats-n")
    public ResponseEntity<ResponseDTO> changeReadStatus(@RequestParam Long emailCode){
        EmailDTO emailDTO = emailService.findById(emailCode);
        emailDTO.setEmailReadStatus("N");
        emailDTO = emailService.updateEmailReadStatus(emailDTO);
        return res("성공적으로 안읽은 것으로 만들었음",emailDTO);
    }










    /**
     * 에러를 갖고 응답하는 메소드
     * @param errorCode 에러코드
     * @param message 메세지
     * @return 에러코드,메세지,null 로 응답
     */
    private ResponseEntity<ResponseDTO> resNull(int errorCode, String message){
        return ResponseEntity.ok().body(new ResponseDTO(errorCode,message,null));
    }

    /**
     * 정상적으로 응답하는 메소드
     * @param msg 메세지
     * @param data 보낼 데이터
     * @return 200, 메세지, 보낼데이터 로 응답
     */
    private ResponseEntity<ResponseDTO> res(String msg,Object data){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,msg,data));
    }
    private String getId(String email){
        int index = email.indexOf("@"); //처음으로 @가 나오는 인덱스 (없으면 -1 반환)
        if(index != 1){ //있으면
            return email.substring(0,index);    //잘라서 갖다 줌
        } else {
            return "에러";
        }
    }
    /**
     * 일반 메일 전송
     * @param email 보내는 이메일DTO 객체
     */
    @MessageMapping("/mail/alert/send") //방법 3 시도 중
    public void mailAlert(@Payload EmailDTO email, SimpMessageHeaderAccessor accessor){
        //wERjtIdxQ8lNjF0w/AAiN6HqTASaCAUzSq6nbKefMwf5CbPE8GvwLsClz94uVt9Q1esxYwwXVU+BYn7/mR01Qg== 비밀키임

        String token = accessor.getFirstNativeHeader("Authorization");
        TokenUtils tokenUtils = new TokenUtils();
        if (token != null) {
            System.out.println("유저의 아이디는 : "+tokenUtils.getUserId(token));
            System.out.println("유저가 누구에게 보내냐 : "+email.getEmailReceiver().getEmployeeId());
            EmailDTO emailDTO = setDefault(email,tokenUtils.getUserId(token));
            System.out.println("가져온 이메일의 내용은 : "+emailDTO);

            emailDTO = emailService.sendMail(emailDTO,"send");
            System.out.println("누구에게 알람을 보내냐 하면 : "+emailDTO.getEmailReceiver().getEmployeeCode());
            System.out.println("알람의 총 주소는 : "+"/topic/mail/alert/"+emailDTO.getEmailReceiver().getEmployeeCode());
            simp.convertAndSend("/topic/mail/alert/"+emailDTO.getEmailReceiver().getEmployeeCode(),emailDTO);

        } else {
            System.out.println("토큰이 없다.");
        }



//        simp.convertAndSend("/topic/mail/alert/"+1,    //누구에게 보낼건지
//                emailService.sendMail(setDefault(email),"send"));   //뭐를 보낼건지
    }
    @PostMapping("send-reserve-mail")
    public ResponseEntity<ResponseDTO> test(@RequestBody EmailDTO emailDTO, @AuthenticationPrincipal User user){
        System.out.println("오는지만 보자");
        EmailDTO result = emailService.sendReserveMail(setDefault(emailDTO, user.getEmployeeId()));
        Long emailCode = result.getEmailCode();

        System.out.println("예약한 시간 : "+emailDTO.getEmailReservationTime());
        dynamicTaskScheduler.scheduleTask(emailDTO.getEmailReservationTime(),emailCode);
        return res("예약 메일이 정상적으로 등록되었습니다.",null);
    }

    private EmailDTO setDefault(EmailDTO email, String user){
        String receiverId = getId(email.getEmailReceiver().getEmployeeId());    //받는 사람
        System.out.println("받는 사람 : "+receiverId);  //얘가 없을 때 처리를 해줘야겠네
        email.setEmailReceiver(emailService.findByEmployeeCode(receiverId));    //가져갈 객체에 받는 사람 저장
        email.setEmailSender(emailService.findByEmployeeId(user));              //보내는 사람 찾아 와서 설정함.(보내는 사람이 없을 순 없다)

        email.setEmailSendTime(new Date().toString());                            //보낼 시간 현재로 저장
        email.setEmailReadStatus("N");                                          //읽지 않음으로 저장
        email.setEmailStatus("send");                                            //이메일 상태 기본으로
        return email;
    }
}

