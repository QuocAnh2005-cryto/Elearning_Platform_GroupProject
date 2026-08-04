package vn.edu.fpt.service.quiz;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.dto.quizdto.QuizImportRowDto;
import vn.edu.fpt.entity.Quiz;
import vn.edu.fpt.entity.QuizAnswer;
import vn.edu.fpt.entity.QuizQuestion;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.repository.QuizRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizImportService {

    private static final int MAX_ANSWER = 10;

    private final QuizRepository quizRepository;
    private final QuizService quizService;

    public void importQuiz(MultipartFile file, Integer quizId, String mode, User instructor) {

        validateFile(file);

        List<QuizImportRowDto> rows = readExcel(file);

        validateRows(rows);

        Quiz quiz = quizService.getInstructorOwnedQuiz(quizId, instructor);

        if ("OVERWRITE".equals(mode)) {

            quiz.getQuestions().clear();

        }

        int position = quiz.getQuestions().size() + 1;

        for (QuizImportRowDto dto : rows) {

            QuizQuestion question = QuizQuestion.builder().quiz(quiz).questionText(dto.getQuestion()).questionType(dto.getType()).points(dto.getPoints()).position(position++).explanation(dto.getExplanation()).build();

            Set<Integer> correctAnswers = Arrays.stream(dto.getCorrect().split(",")).map(String::trim).map(Integer::parseInt).collect(Collectors.toSet());

            for (int i = 0; i < dto.getAnswers().size(); i++) {

                String answerText = dto.getAnswers().get(i);

                if (answerText == null || answerText.isBlank()) {
                    continue;
                }

                QuizAnswer answer = QuizAnswer.builder().answerText(answerText).position(i + 1).correct(correctAnswers.contains(i + 1)).build();

                question.addAnswer(answer);
            }

            quiz.getQuestions().add(question);

        }

        quizRepository.save(quiz);

    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String filename = file.getOriginalFilename();

        if (filename == null || !filename.endsWith(".xlsx")) {
            throw new RuntimeException("Only .xlsx is supported");
        }

    }

    private List<QuizImportRowDto> readExcel(MultipartFile file) {

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            validateHeader(sheet);

            List<QuizImportRowDto> result = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                List<String> answers = new ArrayList<>();

                for (int col = 3; col <= 12; col++) {

                    answers.add(

                            getString(row.getCell(col))

                    );

                }

                QuizImportRowDto dto = new QuizImportRowDto();

                dto.setRow(i + 1);

                dto.setQuestion(

                        getString(row.getCell(0))

                );

                dto.setType(

                        getString(row.getCell(1))

                );

                dto.setPoints(

                        getInteger(row.getCell(2))

                );

                dto.setAnswers(answers);

                dto.setCorrect(

                        getString(row.getCell(13))

                );

                dto.setExplanation(

                        getString(row.getCell(14))

                );

                result.add(dto);

            }

            return result;

        } catch (Exception ex) {

            throw new RuntimeException("Cannot read excel file", ex);

        }

    }

    private void validateHeader(Sheet sheet) {

        Row header = sheet.getRow(0);

        String[] expected = {

                "Question",

                "Type",

                "Points",

                "Answer1",

                "Answer2",

                "Answer3",

                "Answer4",

                "Answer5",

                "Answer6",

                "Answer7",

                "Answer8",

                "Answer9",

                "Answer10",

                "Correct",

                "Explanation"

        };

        for (int i = 0; i < expected.length; i++) {

            String value = getString(header.getCell(i));

            if (!expected[i].equals(value)) {

                throw new RuntimeException("Invalid excel template");

            }

        }

    }

    private void validateRows(List<QuizImportRowDto> rows) {


        for (QuizImportRowDto dto : rows) {

            if (dto.getQuestion().isBlank()) {

                throw new RuntimeException(

                        "Row "

                                + dto.getRow()

                                + " question is empty"

                );

            }

            if (

                    dto.getPoints() == null ||

                            dto.getPoints() <= 0

            ) {

                throw new RuntimeException(

                        "Row "

                                + dto.getRow()

                                + " invalid points"

                );

            }

            if (

                    !Set.of(

                                    "SINGLE",

                                    "MULTIPLE"

                            )

                            .contains(

                                    dto.getType()

                            )

            ) {

                throw new RuntimeException(

                        "Row "

                                + dto.getRow()

                                + " invalid type"

                );

            }

            long totalAnswer = dto.getAnswers().stream().filter(s -> s != null && !s.isBlank()).count();

            if (totalAnswer < 2) {
                throw new RuntimeException("Row " + dto.getRow() + " must contain at least two answers");
            }

            Set<Integer> correctAnswers =

                    Arrays.stream(

                                    dto.getCorrect()

                                            .split(",")

                            )

                            .map(String::trim)

                            .map(Integer::parseInt)

                            .collect(Collectors.toSet());

            if (

                    dto.getType()

                            .equals("SINGLE")

                            &&

                            correctAnswers.size() != 1

            ) {

                throw new RuntimeException(

                        "Row "

                                + dto.getRow()

                                + " SINGLE question must have one correct answer"

                );

            }

            for (

                    Integer index :

                    correctAnswers

            ) {

                if (

                        index < 1 ||

                                index > MAX_ANSWER

                ) {

                    throw new RuntimeException(

                            "Row "

                                    + dto.getRow()

                                    + " invalid correct answer index"

                    );

                }

                String answer =

                        dto.getAnswers()

                                .get(index - 1);

                if (

                        answer == null ||

                                answer.isBlank()

                ) {

                    throw new RuntimeException(

                            "Row "

                                    + dto.getRow()

                                    + " correct answer does not exist"

                    );

                }

            }

        }

    }

    private String getString(Cell cell) {

        if (cell == null) {
            return "";
        }

        return switch (

                cell.getCellType()

                ) {

            case STRING -> cell.getStringCellValue().trim();

            case NUMERIC -> String.valueOf(

                    (int)

                            cell.getNumericCellValue()

            );

            default -> "";

        };

    }

    private Integer getInteger(Cell cell) {

        if (cell == null) {
            return null;
        }
        return (int) cell.getNumericCellValue();

    }
}
