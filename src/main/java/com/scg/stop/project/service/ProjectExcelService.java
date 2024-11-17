package com.scg.stop.project.service;

import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.InternalServerErrorException;
import com.scg.stop.project.domain.AwardStatus;
import com.scg.stop.project.domain.ProjectCategory;
import com.scg.stop.project.domain.ProjectType;
import com.scg.stop.project.domain.Role;
import com.scg.stop.project.dto.request.MemberRequest;
import com.scg.stop.project.dto.request.ProjectRequest;
import com.scg.stop.project.dto.response.FileResponse;
import com.scg.stop.project.dto.response.ProjectExcelResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.scg.stop.global.exception.ExceptionCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectExcelService {

    private final ProjectService projectService;

    private static final int COLUMN_SIZE = 13;

    public ProjectExcelResponse createProjectExcel(MultipartFile excelFile, List<FileResponse> thumbnails, List<FileResponse> posters) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            validateRequestSize(sheet, thumbnails.size(), posters.size());

            // ProjectRequest DTO로 변환
            Map<String, Integer> headerMap = getHeaderMap(sheet);
            List<ProjectRequest> projectRequests = convertExcelToDtoList(sheet, headerMap, thumbnails, posters);
            validateNoDuplicateFile(projectRequests);

            // 프로젝트 생성
            projectRequests.forEach(projectRequest -> projectService.createProject(projectRequest, null));
            return new ProjectExcelResponse(projectRequests.size());
        } catch (IOException e) {
            throw new InternalServerErrorException(INVALID_EXCEL);
        } catch (BadRequestException e) {
            throw new BadRequestException(INVALID_EXCEL_FORMAT, e.getMessage());
        }
    }

    private List<ProjectRequest> convertExcelToDtoList(XSSFSheet sheet, Map<String, Integer> headerMap, List<FileResponse> thumbnails, List<FileResponse> posters) {
        List<ProjectRequest> projectRequests = new ArrayList<>();
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            try {
                validateNoEmptyShell(row);
                projectRequests.add(convertRowToDto(row, headerMap, thumbnails, posters));
            } catch (BadRequestException e) {
                throw new BadRequestException(INVALID_EXCEL_FORMAT, String.format("%d행의 형식이 올바르지 않습니다: %s", rowIndex + 1, e.getMessage()));
            }
        }
        return projectRequests;
    }

    private ProjectRequest convertRowToDto(Row row, Map<String, Integer> headerMap, List<FileResponse> thumbnails, List<FileResponse> posters) {
        String projectName = row.getCell(headerMap.get("프로젝트명")).getStringCellValue().strip();
        String projectTypeStr = row.getCell(headerMap.get("프로젝트 종류")).getStringCellValue().strip();
        String projectCategoryStr = row.getCell(headerMap.get("프로젝트 분야")).getStringCellValue().strip();
        String teamName = row.getCell(headerMap.get("참가팀명")).getStringCellValue().strip();
        String professors = row.getCell(headerMap.get("담당 교수")).getStringCellValue().strip();
        String students = row.getCell(headerMap.get("학생")).getStringCellValue().strip();
        String youtubeId = row.getCell(headerMap.get("유튜브 아이디")).getStringCellValue().strip();
        String url = row.getCell(headerMap.get("웹사이트")).getStringCellValue().strip();
        String description = row.getCell(headerMap.get("간략 설명")).getStringCellValue().strip();
        Integer year = validateAndParseNumericCellValue(row.getCell(headerMap.get("프로젝트 년도")));
        String awardStatusStr = row.getCell(headerMap.get("수상 내역")).getStringCellValue().strip();
        String thumbnailImageName = row.getCell(headerMap.get("썸네일 이미지")).getStringCellValue().strip();
        String posterImageName = row.getCell(headerMap.get("포스터 이미지")).getStringCellValue().strip();

        // 이미지 이름으로 아이디 검색

        Long thumbnailId = thumbnails.stream()
                .filter(thumbnail -> thumbnail.getName().equals(thumbnailImageName))
                .map(FileResponse::getId)
                .findFirst()
                .orElseThrow(() -> new BadRequestException(INVALID_THUMBNAIL_NAME));
        Long posterId = posters.stream()
                .filter(poster -> poster.getName().equals(posterImageName))
                .map(FileResponse::getId)
                .findFirst()
                .orElseThrow(() -> new BadRequestException(INVALID_POSTER_NAME));

        // enum 변환
        ProjectType projectType = ProjectType.fromKoreanName(projectTypeStr);
        ProjectCategory projectCategory = ProjectCategory.fromKoreanName(projectCategoryStr);
        AwardStatus awardStatus = AwardStatus.fromKoreanName(awardStatusStr);

        // 교수 이름, 학생 이름 -> List<Member>
        List<MemberRequest> members = new ArrayList<>();
        Arrays.stream(professors.split(",")).forEach(name -> members.add(new MemberRequest(name.strip(), Role.PROFESSOR)));
        Arrays.stream(students.split(",")).forEach(name -> members.add(new MemberRequest(name.strip(), Role.STUDENT)));

        return new ProjectRequest(
                thumbnailId,
                posterId,
                projectName,
                projectType,
                projectCategory,
                teamName,
                youtubeId,
                year,
                awardStatus,
                members,
                url,
                description);
    }

    private Integer validateAndParseNumericCellValue(Cell cell) {
        try {
            return (int) cell.getNumericCellValue();
        } catch (Exception e) {
            throw new BadRequestException(INVALID_YEAR_FORMAT);
        }
    }

    private void validateNoEmptyShell(Row row) {
        short lastCellNum = row.getLastCellNum();
        if (lastCellNum != COLUMN_SIZE) {
            throw new BadRequestException(EMPTY_CELL);
        }
        for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
            Cell cell = row.getCell(cellIndex);
            if (cell == null || cell.getCellType() == CellType.BLANK) {
                throw new BadRequestException(EMPTY_CELL);
            }
        }
    }

    private void validateRequestSize(Sheet sheet, int thumbnails, int posters) {
        int dataRowCount = 0;
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            if (!isRowEmpty(sheet.getRow(rowIndex))) {
                dataRowCount++;
            }
        }
        if (dataRowCount != thumbnails || dataRowCount != posters) {
            throw new BadRequestException(INVALID_FILE_SIZE);
        }
    }

    private void validateNoDuplicateFile(List<ProjectRequest> projectRequests) {
        List<Long> thumbnailIdList = projectRequests.stream().map(ProjectRequest::getThumbnailId).toList();
        Set<Long> thumbnailIdSet = Set.copyOf(thumbnailIdList);
        if (thumbnailIdList.size() != thumbnailIdSet.size()) {
            throw new BadRequestException(DUPLICATE_THUMBNAIL_ID);
        }

        List<Long> posterIdList = projectRequests.stream().map(ProjectRequest::getPosterId).toList();
        Set<Long> posterIdSet = Set.copyOf(posterIdList);
        if (posterIdList.size() != posterIdSet.size()) {
            throw new BadRequestException(DUPLICATE_POSTER_ID);
        }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (Cell cell : row) {
            if (!isCellEmpty(cell)) {
                return false;
            }
        }
        return true;
    }

    private boolean isCellEmpty(Cell cell) {
        return cell == null || cell.getCellType() == CellType.BLANK;
    }

    private Map<String, Integer> getHeaderMap(XSSFSheet sheet) {
        Map<String, Integer> headerMap = new HashMap<>();
        Row headerRow = sheet.getRow(0);
        for (Cell cell : headerRow) {
            headerMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
        return headerMap;
    }
}
