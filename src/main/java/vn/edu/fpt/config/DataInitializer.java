
//
//package vn.edu.fpt.config;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.jspecify.annotations.NonNull;
//import vn.edu.fpt.entity.*;
//import vn.edu.fpt.repository.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.List;
//import java.util.Set;
//import java.util.HashSet;
//
//@Component
//@RequiredArgsConstructor // tự động sinh constructor với tất cả các field final nên không cần gán @Autowired cho từng repository
//@Transactional
//public class DataInitializer implements CommandLineRunner {
//
//    private final RoleRepository roleRepository;
//    private final UserRepository userRepository;
//    private final CategoryRepository categoryRepository;
//    private final CourseRepository courseRepository;
//    private final CourseSectionRepository courseSectionRepository;
//    private final LessonRepository lessonRepository;
//    private final QuizRepository quizRepository;
//    private final QuizQuestionRepository quizQuestionRepository;
//    private final QuizAnswerRepository quizAnswerRepository;
//    private final LessonMaterialRepository lessonMaterialRepository;
//    private final EnrollmentRepository enrollmentRepository;
//    private final CartRepository cartRepository;
//    private final CartItemRepository cartItemRepository;
//    private final CouponRepository couponRepository;
//    private final CouponUsageRepository couponUsageRepository;
//    private final OrderRepository orderRepository;
//    private final OrderItemRepository orderItemRepository;
//    private final PaymentRepository paymentRepository;
//    private final FeedbackRepository feedbackRepository;
//    private final LessonProgressRepository lessonProgressRepository;
//    private final QuizAttemptRepository quizAttemptRepository;
//
//    @Override
//    public void run(String @NonNull... args) {
//        // Ensure default roles exist
//        Role adminRole = roleRepository.findByName("admin").orElse(null);
//        if (adminRole == null) {
//            roleRepository.save(Role.builder()
//                    .name("admin")
//                    .description("Quản trị hệ thống")
//                    .build());
//        }
//
//        Role managerRole = roleRepository.findByName("manager").orElse(null);
//        if (managerRole == null) {
//            roleRepository.save(Role.builder()
//                    .name("manager")
//                    .description("Quản lý nội dung")
//                    .build());
//        }
//
//        Role instructorRole = roleRepository.findByName("instructor").orElse(null);
//        if (instructorRole == null) {
//            instructorRole = roleRepository.save(Role.builder()
//                    .name("instructor")
//                    .description("Giảng viên")
//                    .build());
//        }
//
//        Role learnerRole = roleRepository.findByName("learner").orElse(null);
//        if (learnerRole == null) {
//            learnerRole = roleRepository.save(Role.builder()
//                    .name("learner")
//                    .description("Học viên")
//                    .build());
//        }
//
//        User instructor = userRepository.findByEmail("28tech@gmail.com").orElse(null);
//        if (instructor == null) {
//            instructor = userRepository.save(User.builder()
//                    .roles(Set.of(instructorRole))
//                    .firstName("28")
//                    .lastName("Tech")
//                    .email("28tech@gmail.com")
//                    .phone("0909999999")
//                    .passwordHash("123456")
//                    .status("ACTIVE")
//                    .build());
//        }
//
//        Category frontEndCategory = categoryRepository.save(Category.builder()
//                .name("Lập trình Front-End")
//                .description("Khóa học về Lập trình Front-End")
//                .status("ACTIVE")
//                .build());
//
//        Category backEndCategory = categoryRepository.save(Category.builder()
//                .name("Lập trình Back-End")
//                .description("Khóa học về Lập trình Back-End")
//                .status("ACTIVE")
//                .build());
//
//        Category iosCategory = categoryRepository.save(Category.builder()
//                .name("Lập trình iOS")
//                .description("Khóa học lập trình ứng dụng iOS")
//                .status("ACTIVE")
//                .build());
//
//        Category htmlCat = categoryRepository.save(Category.builder().name("HTML").description("Khóa học thiết kế giao diện với HTML").parent(frontEndCategory).status("ACTIVE").build());
//        Category cssCat = categoryRepository.save(Category.builder().name("CSS").description("Khóa học định dạng giao diện với CSS").parent(frontEndCategory).status("ACTIVE").build());
//        Category reactCat = categoryRepository.save(Category.builder().name("React").description("Khóa học thư viện ReactJS").parent(frontEndCategory).status("ACTIVE").build());
//
//        Category nodeCat = categoryRepository.save(Category.builder().name("Node.js").description("Khóa học lập trình Back-End với Node.js").parent(backEndCategory).status("ACTIVE").build());
//        Category pythonCat = categoryRepository.save(Category.builder().name("Python").description("Khóa học ngôn ngữ lập trình Python").parent(backEndCategory).status("ACTIVE").build());
//        Category javaCat = categoryRepository.save(Category.builder().name("Java").description("Khóa học ngôn ngữ lập trình Java").parent(backEndCategory).status("ACTIVE").build());
//        Category phpCat = categoryRepository.save(Category.builder().name("PHP").description("Khóa học ngôn ngữ lập trình PHP").parent(backEndCategory).status("ACTIVE").build());
//        Category netCat = categoryRepository.save(Category.builder().name(".NET").description("Khóa học lập trình với .NET Framework / .NET Core").parent(backEndCategory).status("ACTIVE").build());
//
//        Category swiftCat = categoryRepository.save(Category.builder().name("Swift").description("Khóa học ngôn ngữ lập trình Swift").parent(iosCategory).status("ACTIVE").build());
//        Category swiftuiCat = categoryRepository.save(Category.builder().name("SwiftUI").description("Khóa học UI Framework SwiftUI cho iOS").parent(iosCategory).status("ACTIVE").build());
//
//        Course course = courseRepository.save(Course.builder()
//                .instructor(instructor)
//                .category(htmlCat)
//                .title("Lập Trình C Cơ Bản - 28Tech")
//                .description("Khóa học lập trình C cơ bản")
//                .thumbnailUrl("2aOboQZWp6Ov5iGTAZLOlCgmiOhOKsGgeQU1cI0O.jpg")
//                .price(BigDecimal.ZERO)
//                .level("BEGINNER")
//                .status("PUBLISHED")
//                .build());
//
//        // Create 3 more courses similar to the first one
//        Course course2 = courseRepository.save(Course.builder()
//                .instructor(instructor)
//                .category(cssCat)
//                .title("Lập Trình C Nâng Cao - 28Tech")
//                .description("Khóa học nâng cao lập trình C")
//                .thumbnailUrl("2aOboQZWp6Ov5iGTAZLOlCgmiOhOKsGgeQU1cI0O.jpg")
//                .price(new BigDecimal("350000.00"))
//                .level("INTERMEDIATE")
//                .status("PUBLISHED")
//                .build());
//
//        Course course3 = courseRepository.save(Course.builder()
//                .instructor(instructor)
//                .category(reactCat)
//                .title("Lập Trình C Thực Hành - 28Tech")
//                .description("Bài tập thực hành và project nhỏ với C")
//                .thumbnailUrl("2aOboQZWp6Ov5iGTAZLOlCgmiOhOKsGgeQU1cI0O.jpg")
//                .price(new BigDecimal("600000.00"))
//                .level("BEGINNER")
//                .status("PUBLISHED")
//                .build());
//
//        Course course4 = courseRepository.save(Course.builder()
//                .instructor(instructor)
//                .category(nodeCat)
//                .title("Thuật Toán C với 28Tech")
//                .description("Giải thuật và cấu trúc dữ liệu cơ bản bằng C")
//                .thumbnailUrl("2aOboQZWp6Ov5iGTAZLOlCgmiOhOKsGgeQU1cI0O.jpg")
//                .price(new BigDecimal("850000.00"))
//                .level("ADVANCED")
//                .status("PUBLISHED")
//                .build());
//
//        CourseSection section = courseSectionRepository.save(CourseSection.builder()
//                .course(course)
//                .title("Giới thiệu")
//                .position(1)
//                .build());
//
//        lessonRepository.save(Lesson.builder()
//                .courseSection(section)
//                .title("Bài 1 - Giới thiệu ngôn ngữ C")
//                .videoUrl("Recording 2026-05-28 212131.mp4")
//                .durationSeconds(600)
//                .position(1)
//                .published(true)
//                .moderationStatus("APPROVED")
//                .build());
//
//        Lesson lesson2 = lessonRepository.save(Lesson.builder()
//                .courseSection(section)
//                .title("Bài 2 - Kiểu dữ liệu và khai báo biến trong C")
//                .videoUrl("L%E1%BA%ADp%20tr%C3%ACnh%20C%20-%2002.%20Ki%E1%BB%83u%20d%E1%BB%AF%20li%E1%BB%87u%20v%C3%A0%20c%C3%A1ch%20khai%20b%C3%A1o%20bi%E1%BA%BFn%20trong%20l%E1%BA%ADp%20tr%C3%ACnh%20C%20-%20T%E1%BB%B1%20h%E1%BB%8Dc%20l%E1%BA%ADp%20tr%C3%ACnh%20C-C++.mp4")
//                .durationSeconds(900)
//                .position(2)
//                .published(true)
//                .moderationStatus("APPROVED")
//                .build());
//
//        Lesson lesson3 = lessonRepository.save(Lesson.builder()
//                .courseSection(section)
//                .title("Bài 3 - Xuất dữ liệu với printf")
//                .videoUrl("L%E1%BA%ADp%20tr%C3%ACnh%20C%20-%2003.%20C%C3%A1ch%20xu%E1%BA%A5t%20d%E1%BB%AF%20li%E1%BB%87u%20ra%20m%C3%A0n%20h%C3%ACnh%20l%E1%BA%ADp%20tr%C3%ACnh%20C%20-%20H%C3%A0m%20printf%20-%20T%E1%BB%B1%20h%E1%BB%8Dc%20l%E1%BA%ADp%20tr%C3%ACnh%20C.mp4")
//                .durationSeconds(850)
//                .position(3)
//                .published(true)
//                .moderationStatus("APPROVED")
//                .build());
//
//        Lesson lesson4 = lessonRepository.save(Lesson.builder()
//                .courseSection(section)
//                .title("Bài 4 - Nhập dữ liệu với scanf")
//                .videoUrl("L%E1%BA%ADp%20tr%C3%ACnh%20C%20-%2004.%20C%C3%A1ch%20nh%E1%BA%ADp%20d%E1%BB%AF%20li%E1%BB%87u%20t%E1%BB%AB%20b%C3%A0n%20ph%C3%ADm%20trong%20l%E1%BA%ADp%20tr%C3%ACnh%20C%20-%20T%E1%BB%B1%20h%E1%BB%8Dc%20l%E1%BA%ADp%20tr%C3%ACnh%20C.mp4")
//                .durationSeconds(920)
//                .position(4)
//                .published(true)
//                .moderationStatus("APPROVED")
//                .build());
//
//        seedQuizForLesson2(lesson2);
//        seedQuizForLesson3(lesson3);
//        seedQuizForLesson4(lesson4);
//
//        CourseSection section2 = createSection(course, "Cấu trúc điều khiển", 2);
//        Lesson lesson5 = createLesson(section2, "Bài 5 - Câu lệnh if else", "Recording 2026-05-28 212131.mp4", 780, 1);
//        Lesson lesson6 = createLesson(section2, "Bài 6 - Câu lệnh switch case", "Recording 2026-05-28 212131.mp4", 840, 2);
//        Lesson lesson7 = createLesson(section2, "Bài 7 - Vòng lặp for và while", "Recording 2026-05-28 212131.mp4", 900, 3);
//        seedQuizWithTwoQuestions(lesson5,
//                "Quiz - Câu lệnh if else",
//                "Câu lệnh nào dùng để rẽ nhánh trong C?",
//                "if else",
//                new String[]{"switch case", "for", "while"},
//                "Điều kiện trong if thường có kiểu dữ liệu gì?",
//                "Boolean",
//                new String[]{"String", "Char", "Float"});
//        seedQuizWithTwoQuestions(lesson6,
//                "Quiz - Câu lệnh switch case",
//                "Switch case thường dùng để làm gì?",
//                "Chọn một nhánh theo giá trị",
//                new String[]{"Lặp vô hạn", "Khai báo biến", "Tạo hàm"},
//                "Từ khóa nào dùng để thoát khỏi case?",
//                "break",
//                new String[]{"continue", "return", "exit"});
//        seedQuizWithTwoQuestions(lesson7,
//                "Quiz - Vòng lặp for và while",
//                "Vòng lặp nào thường biết trước số lần lặp?",
//                "for",
//                new String[]{"while", "do while", "switch"},
//                "Câu lệnh nào kiểm tra điều kiện trước khi lặp?",
//                "while",
//                new String[]{"if", "switch", "goto"});
//
//        CourseSection section3 = createSection(course, "Mảng và chuỗi", 3);
//        Lesson lesson8 = createLesson(section3, "Bài 8 - Khai báo và truy cập mảng", "Recording 2026-05-28 212131.mp4", 870, 1);
//        Lesson lesson9 = createLesson(section3, "Bài 9 - Duyệt mảng với vòng lặp", "Recording 2026-05-28 212131.mp4", 930, 2);
//        Lesson lesson10 = createLesson(section3, "Bài 10 - Xử lý chuỗi trong C", "Recording 2026-05-28 212131.mp4", 960, 3);
//        seedQuizWithTwoQuestions(lesson8,
//                "Quiz - Mảng trong C",
//                "Chỉ số phần tử đầu tiên của mảng trong C là gì?",
//                "0",
//                new String[]{"1", "-1", "Kích thước mảng"},
//                "Mảng trong C có đặc điểm nào?",
//                "Lưu các phần tử cùng kiểu dữ liệu",
//                new String[]{"Lưu mọi kiểu dữ liệu", "Không có kích thước", "Chỉ lưu chuỗi"});
//        seedQuizWithTwoQuestions(lesson9,
//                "Quiz - Duyệt mảng",
//                "Vòng lặp nào thường dùng để duyệt mảng?",
//                "for",
//                new String[]{"switch", "goto", "break"},
//                "Độ dài mảng tĩnh trong C được xác định khi nào?",
//                "Khi khai báo",
//                new String[]{"Khi chạy chương trình", "Khi in ra", "Khi kết thúc"});
//        seedQuizWithTwoQuestions(lesson10,
//                "Quiz - Xử lý chuỗi",
//                "Chuỗi trong C kết thúc bằng ký tự nào?",
//                "\\0",
//                new String[]{"\n", "space", "#"},
//                "Hàm nào thường dùng để đo độ dài chuỗi?",
//                "strlen",
//                new String[]{"strcpy", "printf", "scanf"});
//
//        CourseSection section4 = createSection(course, "Hàm và con trỏ", 4);
//        Lesson lesson11 = createLesson(section4, "Bài 11 - Hàm trong C", "Recording 2026-05-28 212131.mp4", 840, 1);
//        Lesson lesson12 = createLesson(section4, "Bài 12 - Tham số và giá trị trả về", "Recording 2026-05-28 212131.mp4", 900, 2);
//        Lesson lesson13 = createLesson(section4, "Bài 13 - Con trỏ cơ bản", "Recording 2026-05-28 212131.mp4", 980, 3);
//        seedQuizWithTwoQuestions(lesson11,
//                "Quiz - Hàm trong C",
//                "Mục đích của hàm trong C là gì?",
//                "Tái sử dụng và tổ chức code",
//                new String[]{"Tăng dung lượng RAM", "Xóa biến toàn cục", "Thay thế vòng lặp"},
//                "Từ khóa nào thường dùng để khai báo hàm trả về số nguyên?",
//                "int",
//                new String[]{"void", "char", "float"});
//        seedQuizWithTwoQuestions(lesson12,
//                "Quiz - Tham số và giá trị trả về",
//                "Tham số hàm được truyền vào khi nào?",
//                "Khi gọi hàm",
//                new String[]{"Khi biên dịch", "Khi import file", "Khi kết thúc hàm"},
//                "Giá trị trả về của hàm được khai báo bằng gì?",
//                "Kiểu dữ liệu trả về",
//                new String[]{"Tên hàm", "Số tham số", "Tên biến cục bộ"});
//        seedQuizWithTwoQuestions(lesson13,
//                "Quiz - Con trỏ cơ bản",
//                "Con trỏ lưu gì?",
//                "Địa chỉ bộ nhớ",
//                new String[]{"Giá trị chuỗi", "Tên biến", "Kết quả hàm"},
//                "Toán tử nào dùng để lấy địa chỉ biến?",
//                "&",
//                new String[]{"*", "%", "#"});
//
//        seedLessonMaterials(instructor, course, lesson4);
//
//        // Seed same structure for additional courses
//        seedCourseContent(course2, instructor);
//        seedCourseContent(course3, instructor);
//        seedCourseContent(course4, instructor);
//
//        // Seed 10 more courses (course 5 -> course 14)
//        String[][] extraCourseConfigs = {
//                {"Lập Trình C Chuyên Đề 5 - 28Tech", "Khóa học chuyên đề C số 5", "BEGINNER", "1500000.00"},
//                {"Lập Trình C Chuyên Đề 6 - 28Tech", "Khóa học chuyên đề C số 6", "INTERMEDIATE", "0.00"},
//                {"Lập Trình C Chuyên Đề 7 - 28Tech", "Khóa học chuyên đề C số 7", "ADVANCED", "250000.00"},
//                {"Lập Trình C Chuyên Đề 8 - 28Tech", "Khóa học chuyên đề C số 8", "BEGINNER", "550000.00"},
//                {"Lập Trình C Chuyên Đề 9 - 28Tech", "Khóa học chuyên đề C số 9", "INTERMEDIATE", "900000.00"},
//                {"Lập Trình C Chuyên Đề 10 - 28Tech", "Khóa học chuyên đề C số 10", "ADVANCED", "1100000.00"},
//                {"Lập Trình C Chuyên Đề 11 - 28Tech", "Khóa học chuyên đề C số 11", "BEGINNER", "0.00"},
//                {"Lập Trình C Chuyên Đề 12 - 28Tech", "Khóa học chuyên đề C số 12", "INTERMEDIATE", "450000.00"},
//                {"Lập Trình C Chuyên Đề 13 - 28Tech", "Khóa học chuyên đề C số 13", "ADVANCED", "650000.00"},
//                {"Lập Trình C Chuyên Đề 14 - 28Tech", "Khóa học chuyên đề C số 14", "INTERMEDIATE", "950000.00"}
//        };
//
//        List<Category> childCategories = List.of(
//            htmlCat, cssCat, reactCat, nodeCat, pythonCat, javaCat, phpCat, netCat, swiftCat, swiftuiCat
//        );
//
//        for (int i = 0; i < extraCourseConfigs.length; i++) {
//            String[] cfg = extraCourseConfigs[i];
//            Category targetCat = childCategories.get((i + 4) % childCategories.size());
//            createAndSeedCourse(instructor, targetCat, cfg[0], cfg[1], cfg[2], new BigDecimal(cfg[3]));
//        }
//
//        // Seed test learner user 'Do Thanh' and enroll to course id=1
//        User learner = userRepository.findByEmail("dothanh2572005@gmail.com").orElse(null);
//        if (learner == null) {
//            learner = userRepository.save(User.builder()
//                    .roles(Set.of(learnerRole))
//                    .firstName("Do")
//                    .lastName("Thanh")
//                    .email("dothanh2572005@gmail.com")
//                    .phone(null)
//                    .passwordHash("123")
//                    .status("ACTIVE")
//                    .build());
//        }
//
//        // Enroll into course id=1 if exists and not already enrolled
//        final User finalLearner = learner;
//        courseRepository.findById(1).ifPresent(c -> {
//            boolean already = enrollmentRepository.findAll().stream()
//                    .anyMatch(en -> en.getUser().getId().equals(finalLearner.getId()) && en.getCourse().getId().equals(c.getId()));
//            if (!already) {
//                enrollmentRepository.save(Enrollment.builder()
//                        .user(finalLearner)
//                        .course(c)
//                        .progressPercent(BigDecimal.ZERO)
//                        .build());
//            }
//        });
//
//        // Seed 10 diverse learner profiles with full mock data
//        seedLearnersData(learnerRole, instructor);
//    }
//
//    private void seedLearnersData(Role learnerRole, User instructor) {
//        // 1. Seed Coupons
//        Coupon welcome10 = couponRepository.findByCode("WELCOME10").orElse(null);
//        if (welcome10 == null) {
//            welcome10 = couponRepository.save(Coupon.builder()
//                    .instructor(instructor)
//                    .code("WELCOME10")
//                    .discountType("PERCENT")
//                    .discountValue(new BigDecimal("10.00"))
//                    .usageLimit(100)
//                    .usedCount(0)
//                    .status("ACTIVE")
//                    .expiredAt(LocalDateTime.now().plusMonths(6))
//                    .build());
//        }
//
//        Coupon devspecial = couponRepository.findByCode("DEVSPECIAL").orElse(null);
//        if (devspecial == null) {
//            devspecial = couponRepository.save(Coupon.builder()
//                    .instructor(instructor)
//                    .code("DEVSPECIAL")
//                    .discountType("FIXED")
//                    .discountValue(new BigDecimal("100000.00"))
//                    .usageLimit(50)
//                    .usedCount(0)
//                    .status("ACTIVE")
//                    .expiredAt(LocalDateTime.now().plusMonths(3))
//                    .build());
//        }
//
//        // 2. Fetch Courses
//        Course c1 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Lập Trình C Cơ Bản")).findFirst().orElse(null);
//        Course c2 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Lập Trình C Nâng Cao")).findFirst().orElse(null);
//        Course c3 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Lập Trình C Thực Hành")).findFirst().orElse(null);
//        Course c4 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Thuật Toán C")).findFirst().orElse(null);
//        Course c5 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Chuyên Đề 5")).findFirst().orElse(null);
//        Course c6 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Chuyên Đề 6")).findFirst().orElse(null);
//        Course c7 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Chuyên Đề 7")).findFirst().orElse(null);
//        Course c8 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Chuyên Đề 8")).findFirst().orElse(null);
//        Course c9 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Chuyên Đề 9")).findFirst().orElse(null);
//        Course c10 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Chuyên Đề 10")).findFirst().orElse(null);
//        Course c11 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Chuyên Đề 11")).findFirst().orElse(null);
//        Course c12 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Chuyên Đề 12")).findFirst().orElse(null);
//        Course c13 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Chuyên Đề 13")).findFirst().orElse(null);
//        Course c14 = courseRepository.findAll().stream().filter(c -> c.getTitle().contains("Chuyên Đề 14")).findFirst().orElse(null);
//
//        // 3. Define learners
//        String[][] learnersInfo = {
//                {"Nguyễn Văn", "An", "an.nguyen@elearning.com", "0981112222"},
//                {"Trần Thị", "Bình", "binh.tran@elearning.com", "0982223333"},
//                {"Phạm Văn", "Cường", "cuong.pham@elearning.com", "0983334444"},
//                {"Hoàng Thị", "Dung", "dung.hoang@elearning.com", "0984445555"},
//                {"Đỗ Văn", "Em", "em.do@elearning.com", "0985556666"},
//                {"Lê Thu", "Giang", "giang.le@elearning.com", "0986667777"},
//                {"Vũ Thanh", "Hải", "hai.vu@elearning.com", "0987778888"},
//                {"Đỗ Khánh", "Huy", "huy.do@elearning.com", "0988889999"},
//                {"Bùi Anh", "Khoa", "khoa.bui@elearning.com", "0989990000"},
//                {"Đặng Quang", "Long", "long.dang@elearning.com", "0980001111"}
//        };
//
//        User[] users = new User[10];
//        for (int i = 0; i < 10; i++) {
//            String[] info = learnersInfo[i];
//            User u = userRepository.findByEmail(info[2]).orElse(null);
//            if (u == null) {
//                u = userRepository.save(User.builder()
//                        .roles(Set.of(learnerRole))
//                        .firstName(info[0])
//                        .lastName(info[1])
//                        .email(info[2])
//                        .phone(info[3])
//                        .passwordHash("123456")
//                        .status("ACTIVE")
//                        .build());
//            }
//            users[i] = u;
//        }
//
//        // 4. Create carts & populate items
//        for (int i = 0; i < 10; i++) {
//            User u = users[i];
//            Cart cart = cartRepository.findByUser(u).orElse(null);
//            if (cart == null) {
//                cart = cartRepository.save(Cart.builder().user(u).build());
//            }
//            // Add items to cart based on the matrix
//            final Cart finalCart = cart;
//            if (i == 1 && cartItemRepository.findAll().stream().noneMatch(item -> item.getCart().getId().equals(finalCart.getId()))) { // Binh: C3
//                cartItemRepository.save(CartItem.builder().cart(cart).course(c3).build());
//            } else if (i == 3 && cartItemRepository.findAll().stream().noneMatch(item -> item.getCart().getId().equals(finalCart.getId()))) { // Dung: C2
//                cartItemRepository.save(CartItem.builder().cart(cart).course(c2).build());
//            } else if (i == 5 && cartItemRepository.findAll().stream().noneMatch(item -> item.getCart().getId().equals(finalCart.getId()))) { // Giang: C1
//                cartItemRepository.save(CartItem.builder().cart(cart).course(c1).build());
//            } else if (i == 7 && cartItemRepository.findAll().stream().noneMatch(item -> item.getCart().getId().equals(finalCart.getId()))) { // Huy: C1, C4
//                cartItemRepository.save(CartItem.builder().cart(cart).course(c1).build());
//                cartItemRepository.save(CartItem.builder().cart(cart).course(c4).build());
//            }
//        }
//
//        // 5. Create Orders, OrderItems, Payments, CouponUsages
//        // == Learner 0: An (Paid Order for C1 & C2)
//        createMockOrder(users[0], List.of(c1, c2), null, BigDecimal.ZERO, "PAID", "MOMO", "success");
//
//        // == Learner 1: Binh (Paid Order for C1)
//        createMockOrder(users[1], List.of(c1), null, BigDecimal.ZERO, "PAID", "VNPAY", "success");
//
//        // == Learner 2: Cuong (Paid Order for C2 with WELCOME10 coupon)
//        createMockOrder(users[2], List.of(c2), welcome10, new BigDecimal("50000.00"), "PAID", "CARD", "success");
//
//        // == Learner 3: Dung (Paid Order for C3)
//        createMockOrder(users[3], List.of(c3), null, BigDecimal.ZERO, "PAID", "MOMO", "success");
//
//        // == Learner 4: Em (Paid Order for C1 & C3)
//        createMockOrder(users[4], List.of(c1, c3), null, BigDecimal.ZERO, "PAID", "VNPAY", "success");
//
//        // == Learner 5: Giang (Pending Order for C2)
//        createMockOrder(users[5], List.of(c2), null, BigDecimal.ZERO, "PENDING", "BANK_TRANSFER", "PENDING");
//
//        // == Learner 6: Hai (Paid Order for C4 with DEVSPECIAL coupon)
//        createMockOrder(users[6], List.of(c4), devspecial, new BigDecimal("100000.00"), "PAID", "BANK_TRANSFER", "success");
//
//        // == Learner 8: Khoa (Paid Order for C5 with WELCOME10 coupon)
//        createMockOrder(users[8], List.of(c5), welcome10, new BigDecimal("0.00"), "PAID", "MOMO", "success"); // C5 is 0 VND
//
//        // == Learner 9: Long (Paid Order for C1 & C5)
//        createMockOrder(users[9], List.of(c1, c5), null, BigDecimal.ZERO, "PAID", "VNPAY", "success");
//
//
//        // 6. Create Enrollments, Lesson Progress, Quiz Attempts
//        // == An: C1 100% (4 lessons), C2 50% (2/4 sections or similar, say 1 lesson of section 1)
//        seedEnrollmentAndProgress(users[0], c1, 100, 4);
//        seedEnrollmentAndProgress(users[0], c2, 50, 1);
//        seedQuizAttempts(users[0], c1, 100, true);
//
//        // == Binh: C1 50% (2 lessons)
//        seedEnrollmentAndProgress(users[1], c1, 50, 2);
//        seedQuizAttempts(users[1], c1, 75, true);
//
//        // == Cuong: C2 0% (0 lessons)
//        seedEnrollmentAndProgress(users[2], c2, 0, 0);
//
//        // == Dung: C3 100% (all lessons in C3, which has 3 lessons in section 1)
//        seedEnrollmentAndProgress(users[3], c3, 100, 3);
//        seedQuizAttempts(users[3], c3, 100, true);
//
//        // == Em: C1 75% (3 lessons), C3 50% (1 lesson)
//        seedEnrollmentAndProgress(users[4], c1, 75, 3);
//        seedEnrollmentAndProgress(users[4], c3, 50, 1);
//        // Em failed once then passed C1 quizzes
//        seedQuizAttempts(users[4], c1, 50, false);
//        seedQuizAttempts(users[4], c1, 80, true);
//
//        // == Hai: C4 25% (1 lesson)
//        seedEnrollmentAndProgress(users[6], c4, 25, 1);
//        seedQuizAttempts(users[6], c4, 60, false); // Failed quiz
//
//        // == Khoa: C5 100% (all lessons in C5 section 1)
//        seedEnrollmentAndProgress(users[8], c5, 100, 3);
//        seedQuizAttempts(users[8], c5, 90, true);
//
//        // == Long: C1 100% (4 lessons), C5 100%
//        seedEnrollmentAndProgress(users[9], c1, 100, 4);
//        seedEnrollmentAndProgress(users[9], c5, 100, 3);
//        seedQuizAttempts(users[9], c1, 100, true);
//        seedQuizAttempts(users[9], c5, 100, true);
//
//
//        // 7. Feedbacks (distributing ratings to produce fractional averages like 4.7, 4.3, 3.7, 2.7, 3.3)
//        // An -> C1: 5 stars, Binh -> C1: 4 stars, Long -> C1: 1 star -> avg = 3.3 stars
//        seedFeedback(users[0], c1, 5, "Khóa học C cơ bản vô cùng chất lượng, giảng viên giải thích cực kỳ tỉ mỉ và dễ nhớ!");
//        seedFeedback(users[1], c1, 4, "Bài giảng chuẩn bị rất công phu, giao diện học tập trực quan. Tuy nhiên, một số bài tập tự luyện hơi khó.");
//        seedFeedback(users[9], c1, 1, "Chất lượng âm thanh video bài 2 và bài 3 mờ nhạt và rất khó nghe, mong admin sớm cải thiện.");
//
//        // Cuong -> C2: 5 stars, An -> C2: 5 stars, Binh -> C2: 4 stars -> avg = 4.7 stars
//        seedFeedback(users[2], c2, 5, "Tài liệu PDF đi kèm rất xịn, bài tập trắc nghiệm có giải thích chi tiết đáp án.");
//        seedFeedback(users[0], c2, 5, "Khóa học nâng cao siêu hay, kiến thức sâu sắc.");
//        seedFeedback(users[1], c2, 4, "Khá tốt nhưng cần thêm bài tập thực hành.");
//
//        // Dung -> C3: 5 stars, Binh -> C3: 4 stars, Cuong -> C3: 4 stars -> avg = 4.3 stars
//        seedFeedback(users[3], c3, 5, "Lập trình C thực hành rất thực tế, nhiều bài tập hay.");
//        seedFeedback(users[1], c3, 4, "Bài tập đa dạng, rất bám sát thực tế đi làm.");
//        seedFeedback(users[2], c3, 4, "Giao diện bài thực hành chạy mượt mà.");
//
//        // Hai -> C4: 4 stars, Cuong -> C4: 3 stars, An -> C4: 3 stars -> avg = 3.3 stars
//        seedFeedback(users[6], c4, 4, "Nội dung cấu trúc dữ liệu và giải thuật chi tiết.");
//        seedFeedback(users[2], c4, 3, "Thuật toán C hơi phức tạp so với trình độ của tôi, bài giảng đi nhanh quá.");
//        seedFeedback(users[0], c4, 3, "Nội dung tạm ổn.");
//
//        // Khoa -> C5: 5 stars, Dung -> C5: 4 stars, An -> C5: 2 stars -> avg = 3.7 stars
//        seedFeedback(users[8], c5, 5, "Khóa học chuyên đề rất hữu ích, giúp tôi hiểu sâu về mảng và con trỏ!");
//        seedFeedback(users[3], c5, 4, "Học rất ổn.");
//        seedFeedback(users[0], c5, 2, "Hơi khó so với người mới bắt đầu.");
//
//        // Additional feedbacks to cover all stars and courses
//        if (c6 != null) {
//            seedFeedback(users[0], c6, 4, "Khá tốt!");
//            seedFeedback(users[1], c6, 4, "Bài giảng rõ ràng.");
//            seedFeedback(users[2], c6, 3, "Được."); // avg = 3.7 stars
//        }
//        if (c7 != null) seedFeedback(users[1], c7, 1, "Quá tệ!");
//        if (c8 != null) {
//            seedFeedback(users[2], c8, 3, "Bình thường.");
//            seedFeedback(users[3], c8, 3, "Hơi khó hiểu.");
//            seedFeedback(users[4], c8, 2, "Chưa thực sự chi tiết."); // avg = 2.7 stars
//        }
//        if (c9 != null) seedFeedback(users[3], c9, 4, "Rất hay.");
//        if (c10 != null) {
//            seedFeedback(users[4], c10, 5, "Tuyệt vời.");
//            seedFeedback(users[5], c10, 5, "Khá hay.");
//            seedFeedback(users[6], c10, 4, "Nội dung phong phú."); // avg = 4.7 stars
//        }
//        if (c11 != null) seedFeedback(users[5], c11, 2, "Hơi sơ sài.");
//        if (c12 != null) {
//            seedFeedback(users[6], c12, 4, "Được.");
//            seedFeedback(users[7], c12, 4, "Bổ ích.");
//            seedFeedback(users[8], c12, 3, "Tạm được."); // avg = 3.7 stars
//        }
//        if (c13 != null) seedFeedback(users[7], c13, 4, "Tốt.");
//        if (c14 != null) {
//            seedFeedback(users[8], c14, 5, "Cực tốt.");
//            seedFeedback(users[9], c14, 4, "Chất lượng.");
//            seedFeedback(users[0], c14, 4, "Học xong làm được ngay."); // avg = 4.3 stars
//        }
//    }
//
//    private void createMockOrder(User user, List<Course> courses, Coupon coupon, BigDecimal discount, String status, String method, String paymentStatus) {
//        BigDecimal total = BigDecimal.ZERO;
//        for (Course c : courses) {
//            total = total.add(c.getPrice());
//        }
//        total = total.subtract(discount);
//        if (total.compareTo(BigDecimal.ZERO) < 0) {
//            total = BigDecimal.ZERO;
//        }
//
//        // Check if order already exists for this user to avoid duplicates
//        final String finalStatus = status;
//        boolean exists = orderRepository.findAll().stream()
//                .anyMatch(o -> o.getUser().getId().equals(user.getId()) && o.getStatus().equals(finalStatus));
//        if (exists) return;
//
//        Order order = orderRepository.save(Order.builder()
//                .user(user)
//                .totalAmount(total)
//                .discountAmount(discount)
//                .status(status)
//                .paymentMethod(method)
//                .build());
//
//        for (Course c : courses) {
//            orderItemRepository.save(OrderItem.builder()
//                    .order(order)
//                    .course(c)
//                    .coupon(coupon)
//                    .priceSnapshot(c.getPrice())
//                    .discountAmount(coupon != null ? discount : BigDecimal.ZERO)
//                    .finalPrice(c.getPrice().subtract(coupon != null ? discount : BigDecimal.ZERO))
//                    .courseTitleSnapshot(c.getTitle())
//                    .build());
//        }
//
//        if ("SUCCESS".equals(paymentStatus)) {
//            paymentRepository.save(Payment.builder()
//                    .order(order)
//                    .transactionCode("TX_" + user.getLastName().toUpperCase() + "_" + (System.currentTimeMillis() % 10000))
//                    .gateway(method)
//                    .gatewayTxId("GATEWAY_" + (System.currentTimeMillis() % 10000))
//                    .amount(total)
//                    .status("SUCCESS")
//                    .paidAt(LocalDateTime.now())
//                    .build());
//
//            if (coupon != null) {
//                couponUsageRepository.save(CouponUsage.builder()
//                        .coupon(coupon)
//                        .user(user)
//                        .order(order)
//                        .discountAmount(discount)
//                        .usedAt(LocalDateTime.now())
//                        .build());
//            }
//        }
//    }
//
//    private void seedEnrollmentAndProgress(User user, Course course, int progress, int completedLessonsCount) {
//        if (course == null) return;
//        Enrollment enrollment = enrollmentRepository.findAll().stream()
//                .filter(e -> e.getUser().getId().equals(user.getId()) && e.getCourse().getId().equals(course.getId()))
//                .findFirst().orElse(null);
//
//        if (enrollment == null) {
//            enrollment = enrollmentRepository.save(Enrollment.builder()
//                    .user(user)
//                    .course(course)
//                    .progressPercent(new BigDecimal(progress))
//                    .completedAt(progress == 100 ? LocalDateTime.now() : null)
//                    .build());
//        }
//
//        // Get lessons of this course
//        final Enrollment finalEnrollment = enrollment;
//        List<Lesson> lessons = lessonRepository.findAll().stream()
//                .filter(l -> l.getCourseSection().getCourse().getId().equals(course.getId()))
//                .toList();
//
//        for (int i = 0; i < Math.min(completedLessonsCount, lessons.size()); i++) {
//            Lesson lesson = lessons.get(i);
//            boolean exists = lessonProgressRepository.findAll().stream()
//                    .anyMatch(lp -> lp.getEnrollment().getId().equals(finalEnrollment.getId()) && lp.getLesson().getId().equals(lesson.getId()));
//            if (!exists) {
//                lessonProgressRepository.save(LessonProgress.builder()
//                        .enrollment(finalEnrollment)
//                        .lesson(lesson)
//                        .completed(true)
//                        .lastAccessed(LocalDateTime.now())
//                        .build());
//            }
//        }
//    }
//
//    private void seedQuizAttempts(User user, Course course, int score, boolean passed) {
//        if (course == null) return;
//        List<Lesson> lessons = lessonRepository.findAll().stream()
//                .filter(l -> l.getCourseSection().getCourse().getId().equals(course.getId()))
//                .toList();
//
//        for (Lesson lesson : lessons) {
//            Quiz quiz = quizRepository.findAll().stream()
//                    .filter(q -> q.getLesson().getId().equals(lesson.getId()))
//                    .findFirst().orElse(null);
//            if (quiz != null) {
//                quizAttemptRepository.save(QuizAttempt.builder()//        QuizQuestion q1 = QuizQuestion.builder()
//                .quiz(quiz)
//                .questionText("Kiểu dữ liệu nào dùng để lưu số nguyên trong C?")
//                .questionType("SINGLE")
//                .points(1)
//                .position(1)
//                .explanation("int được dùng để khai báo biến kiểu số nguyên trong ngôn ngữ C.")
//                .build();
//        q1 = quizQuestionRepository.save(q1);
//        persistAnswer(q1, "int", true);
//        persistAnswer(q1, "float", false);
//        persistAnswer(q1, "double", false);
//        persistAnswer(q1, "char", false);
//
//        QuizQuestion q2 = QuizQuestion.builder()
//                .quiz(quiz)
//                .questionText("Từ khóa nào dùng để khai báo biến số thực?")
//                .questionType("SINGLE")
//                .points(1)
//                .position(2)
//                .explanation("float được dùng để khai báo số thực đơn trong ngôn ngữ C.")
//                .build();
//        q2 = quizQuestionRepository.save(q2);
//        persistAnswer(q2, "float", true);
//        persistAnswer(q2, "int", false);
//        persistAnswer(q2, "char", false);
//        persistAnswer(q2, "void", false);
//    }
//
//    private void seedQuizForLesson3(Lesson lesson3) {
//        Quiz quiz = Quiz.builder()
//                .lesson(lesson3)
//                .title("Quiz - Hàm printf")
//                .passScore(70)
//                .build();
//        quiz = quizRepository.save(quiz);
//
//        QuizQuestion q1 = QuizQuestion.builder()
//                .quiz(quiz)
//                .questionText("Hàm nào dùng để xuất dữ liệu ra màn hình?")
//                .questionType("SINGLE")
//                .points(1)
//                .position(1)
//                .explanation("printf() là hàm chuẩn dùng để in chuỗi hoặc giá trị biến ra màn hình.")
//                .build();
//        q1 = quizQuestionRepository.save(q1);
//        persistAnswer(q1, "printf", true);
//        persistAnswer(q1, "scanf", false);
//        persistAnswer(q1, "gets", false);
//        persistAnswer(q1, "cin", false);
//
//        QuizQuestion q2 = QuizQuestion.builder()
//                .quiz(quiz)
//                .questionText("%d trong printf dùng để in kiểu dữ liệu nào?")
//                .questionType("SINGLE")
//                .points(1)
//                .position(2)
//                .explanation("%d là specifier đại diện cho số nguyên có dấu (signed integer).")
//                .build();
//        q2 = quizQuestionRepository.save(q2);
//        persistAnswer(q2, "Số nguyên", true);
//        persistAnswer(q2, "Số thực", false);
//        persistAnswer(q2, "Ký tự", false);
//        persistAnswer(q2, "Chuỗi", false);
//    }
//
//    private void seedQuizForLesson4(Lesson lesson4) {
//        seedQuizWithTwoQuestions(lesson4,
//                "Quiz - Hàm scanf",
//                "Hàm nào dùng để nhập dữ liệu từ bàn phím?",
//                "scanf",
//                new String[]{"printf", "puts", "cout"},
//                "Dấu & trong scanf có tác dụng gì?",
//                "Lấy địa chỉ biến",
//                new String[]{"Kết thúc lệnh", "Nối chuỗi", "Xuất dữ liệu"});
//    }
//
//    private CourseSection createSection(Course course, String title, int position) {
//        return courseSectionRepository.save(CourseSection.builder()
//                .course(course)
//                .title(title)
//                .position(position)
//                .build());
//    }
//
//    private Lesson createLesson(CourseSection section, String title, String videoUrl, int durationSeconds, int position) {
//        return lessonRepository.save(Lesson.builder()
//                .courseSection(section)
//                .title(title)
//                .videoUrl(videoUrl)
//                .durationSeconds(durationSeconds)
//                .position(position)
//                .published(true)
//                .moderationStatus("APPROVED")
//                .build());
//    }
//
//    private void seedQuizWithTwoQuestions(Lesson lesson,
//                                          String quizTitle,
//                                          String q1Text,
//                                          String q1Correct,
//                                          String[] q1WrongAnswers,
//                                          String q2Text,
//                                          String q2Correct,
//                                          String[] q2WrongAnswers) {
//        Quiz quiz = quizRepository.save(Quiz.builder()
//                .lesson(lesson)
//                .title(quizTitle)
//                .passScore(70)
//                .build());
//
//        QuizQuestion q1 = quizQuestionRepository.save(QuizQuestion.builder()
//                .quiz(quiz)
//                .questionText(q1Text)
//                .questionType("SINGLE")
//                .points(1)
//                .position(1)
//                .explanation("Giải thích chi tiết cho câu hỏi: " + q1Text)
//                .build());
//        persistAnswer(q1, q1Correct, true);
//        for (String wrongAnswer : q1WrongAnswers) {
//            persistAnswer(q1, wrongAnswer, false);
//        }
//
//        QuizQuestion q2 = quizQuestionRepository.save(QuizQuestion.builder()
//                .quiz(quiz)
//                .questionText(q2Text)
//                .questionType("SINGLE")
//                .points(1)
//                .position(2)
//                .explanation("Giải thích chi tiết cho câu hỏi: " + q2Text)
//                .build());         .build());
//    }
//
//    private void seedQuizWithTwoQuestions(Lesson lesson,
//                                          String quizTitle,
//                                          String q1Text,
//                                          String q1Correct,
//                                          String[] q1WrongAnswers,
//                                          String q2Text,
//                                          String q2Correct,
//                                          String[] q2WrongAnswers) {
//        Quiz quiz = quizRepository.save(Quiz.builder()
//                .lesson(lesson)
//                .title(quizTitle)
//                .passScore(70)
//                .build());
//
//        QuizQuestion q1 = quizQuestionRepository.save(QuizQuestion.builder()
//                .quiz(quiz)
//                .questionText(q1Text)
//                .questionType("SINGLE")
//                .points(1)
//                .position(1)
//                .build());
//        persistAnswer(q1, q1Correct, true);
//        for (String wrongAnswer : q1WrongAnswers) {
//            persistAnswer(q1, wrongAnswer, false);
//        }
//
//        QuizQuestion q2 = quizQuestionRepository.save(QuizQuestion.builder()
//                .quiz(quiz)
//                .questionText(q2Text)
//                .questionType("SINGLE")
//                .points(1)
//                .position(2)
//                .build());
//        persistAnswer(q2, q2Correct, true);
//        for (String wrongAnswer : q2WrongAnswers) {
//            persistAnswer(q2, wrongAnswer, false);
//        }
//    }
//
//    private void persistAnswer(QuizQuestion question, String text, boolean correct) {
//        QuizAnswer answer = QuizAnswer.builder()
//                .question(question)
//                .answerText(text)
//                .correct(correct)
//                .build();
//        quizAnswerRepository.save(answer);
//    }
//
//    private void seedLessonMaterials(User instructor, Course course, Lesson lesson) {
//        LessonMaterial material = LessonMaterial.builder()
//                .instructor(instructor)
//                .course(course)
//                .lesson(lesson)
//                .fileName("[28Tech] BUOI 1.pdf")
//                .fileUrl("%5B28Tech%5D.%20BUOI%201.pdf")
//                .fileType("pdf")
//                .build();
//        lessonMaterialRepository.save(material);
//    }
//
//    private void seedCourseContent(Course course, User instructor) {
//        // create a section and some lessons + quizzes similar to the first course
//        CourseSection s1 = createSection(course, "Giới thiệu", 1);
//        createLesson(s1, "Bài 1 - Giới thiệu ngôn ngữ C", "Recording 2026-05-28 212131.mp4", 600, 1);
//        Lesson l2 = createLesson(s1, "Bài 2 - Kiểu dữ liệu và khai báo biến trong C", "L%E1%BA%ADp%20tr%C3%ACnh%20C%20-%2002.%20Ki%E1%BB%83u%20d%E1%BB%AF%20li%E1%BB%87u%20v%C3%A0%20c%C3%A1ch%20khai%20b%C3%A1o%20bi%E1%BA%BFn%20trong%20l%E1%BA%ADp%20tr%C3%ACnh%20C%20-%20T%E1%BB%B1%20h%E1%BB%8Dc%20l%E1%BA%ADp%20tr%C3%ACnh%20C-C++.mp4", 900, 2);
//        Lesson l3 = createLesson(s1, "Bài 3 - Xuất dữ liệu với printf", "L%E1%BA%ADp%20tr%C3%ACnh%20C%20-%2003.%20C%C3%A1ch%20xu%E1%BA%A5t%20d%E1%BB%AF%20li%E1%BB%87u%20ra%20m%C3%A0n%20h%C3%ACnh%20l%E1%BA%ADp%20tr%C3%ACnh%20C%20-%20H%C3%A0m%20printf%20-%20T%E1%BB%B1%20h%E1%BB%8Dc%20l%E1%BA%ADp%20tr%C3%ACnh%20C.mp4", 850, 3);
//        seedQuizForLesson2(l2);
//        seedQuizForLesson3(l3);
//
//        CourseSection s2 = createSection(course, "Cấu trúc điều khiển", 2);
//        Lesson la = createLesson(s2, "Bài A - Ví dụ", "Recording 2026-05-28 212131.mp4", 700, 1);
//        createLesson(s2, "Bài B - Ví dụ", "Recording 2026-05-28 212131.mp4", 700, 2);
//        createLesson(s2, "Bài C - Ví dụ", "Recording 2026-05-28 212131.mp4", 700, 3);
//        seedQuizWithTwoQuestions(la, "Quiz - Ví dụ A", "Câu 1?", "Đáp án", new String[]{"a","b","c"}, "Câu 2?", "Đáp", new String[]{"x","y","z"});
//
//        CourseSection s3 = createSection(course, "Mảng và chuỗi", 3);
//        createLesson(s3, "Bài X - Mảng", "Recording 2026-05-28 212131.mp4", 800, 1);
//        createLesson(s3, "Bài Y - Chuỗi", "Recording 2026-05-28 212131.mp4", 800, 2);
//        createLesson(s3, "Bài Z - Ứng dụng", "Recording 2026-05-28 212131.mp4", 800, 3);
//
//        CourseSection s4 = createSection(course, "Hàm và con trỏ", 4);
//        createLesson(s4, "Bài P - Hàm cơ bản", "Recording 2026-05-28 212131.mp4", 840, 1);
//        createLesson(s4, "Bài Q - Tham số", "Recording 2026-05-28 212131.mp4", 900, 2);
//        Lesson lLast = createLesson(s4, "Bài R - Con trỏ cơ bản", "Recording 2026-05-28 212131.mp4", 980, 3);
//        seedQuizForLesson4(lLast);
//
//        // add a lesson material for one lesson in this course
//        seedLessonMaterials(instructor, course, lLast);
//    }
//
//    private void createAndSeedCourse(User instructor, Category category, String title, String description, String level, BigDecimal price) {
//        Course newCourse = courseRepository.save(Course.builder()
//                .instructor(instructor)
//                .category(category)
//                .title(title)
//                .description(description)
//                .thumbnailUrl("2aOboQZWp6Ov5iGTAZLOlCgmiOhOKsGgeQU1cI0O.jpg")
//                .price(price)
//                .level(level)
//                .status("PUBLISHED")
//                .build());
//        seedCourseContent(newCourse, instructor);
//    }
//}
//
////package vn.edu.fpt.config;
////
////import jakarta.transaction.Transactional;
////import lombok.RequiredArgsConstructor;
////import org.springframework.boot.CommandLineRunner;
////import org.springframework.stereotype.Component;
////import org.jspecify.annotations.NonNull;
////import vn.edu.fpt.entity.*;
////import vn.edu.fpt.repository.*;
////
////import java.math.BigDecimal;
////
////@Component
////@RequiredArgsConstructor // tự động sinh constructor với tất cả các field final nên không cần gán @Autowired cho từng repository
////@Transactional
////public class DataInitializer implements CommandLineRunner {
////
////    private final RoleRepository roleRepository;
////    private final UserRepository userRepository;
////    private final CategoryRepository categoryRepository;
////    private final CourseRepository courseRepository;
////    private final CourseSectionRepository courseSectionRepository;
////    private final LessonRepository lessonRepository;
////    private final QuizRepository quizRepository;
////    private final QuizQuestionRepository quizQuestionRepository;
////    private final QuizAnswerRepository quizAnswerRepository;
////
////    @Override
////    public void run(String @NonNull... args) {
////        Role instructorRole = roleRepository.findByName("instructor").orElse(null);
////        if (instructorRole == null) {
////            instructorRole = roleRepository.save(Role.builder()
////                    .name("instructor")
////                    .description("Giảng viên")
////                    .build());
////        }
////
////        User instructor = userRepository.findByEmail("28tech@gmail.com").orElse(null);
////        if (instructor == null) {
////            instructor = userRepository.save(User.builder()
////                    .role(instructorRole)
////                    .firstName("28")
////                    .lastName("Tech")
////                    .email("28tech@gmail.com")
////                    .phone("0909999999")
////                    .passwordHash("123456")
////                    .status("active")
////                    .build());
////        }
////
////        Category category = categoryRepository.save(Category.builder()
////                .name("Lập trình")
////                .description("Các khóa học lập trình")
////                .status("active")
////                .build());
////
////        Course course = courseRepository.save(Course.builder()
////                .instructor(instructor)
////                .category(category)
////                .title("Lập Trình C Cơ Bản - 28Tech")
////                .description("Khóa học lập trình C cơ bản")
////                .thumbnailUrl("course-thumbnails/2aOboQZWp6Ov5iGTAZLOlCgmiOhOKsGgeQU1cI0O.jpg")
////                .price(BigDecimal.ZERO)
////                .level("beginner")
////                .status("published")
////                .build());
////
////        CourseSection section = courseSectionRepository.save(CourseSection.builder()
////                .course(course)
////                .title("Giới thiệu")
////                .position(1)
////                .build());
////
////        lessonRepository.save(Lesson.builder()
////                .courseSection(section)
////                .title("Bài 1 - Giới thiệu ngôn ngữ C")
////                .videoUrl("videos/Recording 2026-05-28 212131.mp4")
////                .durationSeconds(600)
////                .position(1)
////                .published(true)
////                .moderationStatus("approved")
////                .build());
////
////        Lesson lesson2 = lessonRepository.save(Lesson.builder()
////                .courseSection(section)
////                .title("Bài 2 - Kiểu dữ liệu và khai báo biến trong C")
////                .videoUrl("videos/L%E1%BA%ADp%20tr%C3%ACnh%20C%20-%2002.%20Ki%E1%BB%83u%20d%E1%BB%AF%20li%E1%BB%87u%20v%C3%A0%20c%C3%A1ch%20khai%20b%C3%A1o%20bi%E1%BA%BFn%20trong%20l%E1%BA%ADp%20tr%C3%ACnh%20C%20-%20T%E1%BB%B1%20h%E1%BB%8Dc%20l%E1%BA%ADp%20tr%C3%ACnh%20C-C++.mp4")
////                .durationSeconds(900)
////                .position(2)
////                .published(true)
////                .moderationStatus("approved")
////                .build());
////
////        Lesson lesson3 = lessonRepository.save(Lesson.builder()
////                .courseSection(section)
////                .title("Bài 3 - Xuất dữ liệu với printf")
////                .videoUrl("videos/L%E1%BA%ADp%20tr%C3%ACnh%20C%20-%2003.%20C%C3%A1ch%20xu%E1%BA%A5t%20d%E1%BB%AF%20li%E1%BB%87u%20ra%20m%C3%A0n%20h%C3%ACnh%20l%E1%BA%ADp%20tr%C3%ACnh%20C%20-%20H%C3%A0m%20printf%20-%20T%E1%BB%B1%20h%E1%BB%8Dc%20l%E1%BA%ADp%20tr%C3%ACnh%20C.mp4")
////                .durationSeconds(850)
////                .position(3)
////                .published(true)
////                .moderationStatus("approved")
////                .build());
////
////        Lesson lesson4 = lessonRepository.save(Lesson.builder()
////                .courseSection(section)
////                .title("Bài 4 - Nhập dữ liệu với scanf")
////                .videoUrl("videos/L%E1%BA%ADp%20tr%C3%ACnh%20C%20-%2004.%20C%C3%A1ch%20nh%E1%BA%ADp%20d%E1%BB%AF%20li%E1%BB%87u%20t%E1%BB%AB%20b%C3%A0n%20ph%C3%ADm%20trong%20l%E1%BA%ADp%20tr%C3%ACnh%20C%20-%20T%E1%BB%B1%20h%E1%BB%8Dc%20l%E1%BA%ADp%20tr%C3%ACnh%20C.mp4")
////                .durationSeconds(920)
////                .position(4)
////                .published(true)
////                .moderationStatus("approved")
////                .build());
////
////        seedQuizForLesson2(lesson2);
////        seedQuizForLesson3(lesson3);
////        seedQuizForLesson4(lesson4);
////
////    }
////
////
////    private void seedQuizForLesson2(Lesson lesson2) {
////        Quiz quiz = Quiz.builder()
////                .lesson(lesson2)
////                .title("Quiz - Kiểu dữ liệu và biến")
////                .passScore(70)
////                .build();
////        quiz = quizRepository.save(quiz);
////
////        QuizQuestion q1 = QuizQuestion.builder()
////                .quiz(quiz)
////                .questionText("Kiểu dữ liệu nào dùng để lưu số nguyên trong C?")
////                .questionType("single")
////                .points(1)
////                .position(1)
////                .build();
////        q1 = quizQuestionRepository.save(q1);
////        persistAnswer(q1, "int", true);
////        persistAnswer(q1, "float", false);
////        persistAnswer(q1, "double", false);
////        persistAnswer(q1, "char", false);
////
////        QuizQuestion q2 = QuizQuestion.builder()
////                .quiz(quiz)
////                .questionText("Từ khóa nào dùng để khai báo biến số thực?")
////                .questionType("single")
////                .points(1)
////                .position(2)
////                .build();
////        q2 = quizQuestionRepository.save(q2);
////        persistAnswer(q2, "float", true);
////        persistAnswer(q2, "int", false);
////        persistAnswer(q2, "char", false);
////        persistAnswer(q2, "void", false);
////    }
////
////    private void seedQuizForLesson3(Lesson lesson3) {
////        Quiz quiz = Quiz.builder()
////                .lesson(lesson3)
////                .title("Quiz - Hàm printf")
////                .passScore(70)
////                .build();
////        quiz = quizRepository.save(quiz);
////
////        QuizQuestion q1 = QuizQuestion.builder()
////                .quiz(quiz)
////                .questionText("Hàm nào dùng để xuất dữ liệu ra màn hình?")
////                .questionType("single")
////                .points(1)
////                .position(1)
////                .build();
////        q1 = quizQuestionRepository.save(q1);
////        persistAnswer(q1, "printf", true);
////        persistAnswer(q1, "scanf", false);
////        persistAnswer(q1, "gets", false);
////        persistAnswer(q1, "cin", false);
////
////        QuizQuestion q2 = QuizQuestion.builder()
////                .quiz(quiz)
////                .questionText("%d trong printf dùng để in kiểu dữ liệu nào?")
////                .questionType("single")
////                .points(1)
////                .position(2)
////                .build();
////        q2 = quizQuestionRepository.save(q2);
////        persistAnswer(q2, "Số nguyên", true);
////        persistAnswer(q2, "Số thực", false);
////        persistAnswer(q2, "Ký tự", false);
////        persistAnswer(q2, "Chuỗi", false);
////    }
////
////    private void seedQuizForLesson4(Lesson lesson4) {
////        Quiz quiz = Quiz.builder()
////                .lesson(lesson4)
////                .title("Quiz - Hàm scanf")
////                .passScore(70)
////                .build();
////        quiz = quizRepository.save(quiz);
////
////        QuizQuestion q1 = QuizQuestion.builder()
////                .quiz(quiz)
////                .questionText("Hàm nào dùng để nhập dữ liệu từ bàn phím?")
////                .questionType("single")
////                .points(1)
////                .position(1)
////                .build();
////        q1 = quizQuestionRepository.save(q1);
////        persistAnswer(q1, "scanf", true);
////        persistAnswer(q1, "printf", false);
////        persistAnswer(q1, "puts", false);
////        persistAnswer(q1, "cout", false);
////
////        QuizQuestion q2 = QuizQuestion.builder()
////                .quiz(quiz)
////                .questionText("Dấu & trong scanf có tác dụng gì?")
////                .questionType("single")
////                .points(1)
////                .position(2)
////                .build();
////        q2 = quizQuestionRepository.save(q2);
////        persistAnswer(q2, "Lấy địa chỉ biến", true);
////        persistAnswer(q2, "Kết thúc lệnh", false);
////        persistAnswer(q2, "Nối chuỗi", false);
////        persistAnswer(q2, "Xuất dữ liệu", false);
////    }
////
////    private void persistAnswer(QuizQuestion question, String text, boolean correct) {
////        QuizAnswer answer = QuizAnswer.builder()
////                .question(question)
////                .answerText(text)
////                .correct(correct)
////                .build();
////        quizAnswerRepository.save(answer);
////    }
////}
//
