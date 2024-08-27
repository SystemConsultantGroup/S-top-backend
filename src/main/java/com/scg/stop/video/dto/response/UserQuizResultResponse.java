package com.scg.stop.video.dto.response;

import com.scg.stop.global.excel.annotation.ExcelColumn;
import com.scg.stop.global.excel.annotation.ExcelDownload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.CellType;

@Getter
@AllArgsConstructor
@ExcelDownload(
        fileName = "UserQuiz"
)
public class UserQuizResultResponse {
    @ExcelColumn(headerName = "아이디")
    private Long user_id;
    @ExcelColumn(headerName = "이름")
    private String name;
    @ExcelColumn(headerName = "휴대전화")
    private String phone;
    @ExcelColumn(headerName = "이메일")
    private String email;
    @ExcelColumn(headerName = "성공횟수")
    private Long successCount;
}
