-- -- RESET DATABASE: Xóa database cũ nếu đã tồn tại để tránh xung đột dữ liệu cũ và cập nhật DDL mới
-- USE master;
-- GO
-- IF EXISTS (SELECT * FROM sys.databases WHERE name = 'ElearningPlatform')
-- BEGIN
--     ALTER DATABASE ElearningPlatform SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
--     DROP DATABASE ElearningPlatform;
-- END
-- GO
--
-- CREATE DATABASE ElearningPlatform;
-- GO

-- USE ElearningPlatform;
-- GO
--
USE master;
GO
--
IF EXISTS (SELECT * FROM sys.databases WHERE name = 'ElearningPlatform')
BEGIN
    ALTER DATABASE ElearningPlatform SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE ElearningPlatform;
END
GO

CREATE DATABASE ElearningPlatform;
GO

USE ElearningPlatform;
GO

-- =========================
-- ROLES
-- =========================
-- Bảng lưu các vai trò hệ thống (tách riêng để dễ mở rộng)
CREATE TABLE roles (
                       id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh vai trò

                       name VARCHAR(50) UNIQUE NOT NULL,
    -- Tên vai trò: 'admin', 'manager', 'instructor', 'learner'

                       description NVARCHAR(255) NULL,
    -- Mô tả chi tiết vai trò

                       created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo vai trò
                       updated_at DATETIME NULL
    -- Thời gian cập nhật gần nhất
);

-- Seed dữ liệu vai trò mặc định
IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN')
    INSERT INTO roles (name, description) VALUES ('ADMIN', N'Quản trị hệ thống');

IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'MANAGER')
    INSERT INTO roles (name, description) VALUES ('MANAGER', N'Quản lý nội dung');

IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'INSTRUCTOR')
    INSERT INTO roles (name, description) VALUES ('INSTRUCTOR', N'Giảng viên');

IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'LEARNER')
    INSERT INTO roles (name, description) VALUES ('LEARNER', N'Học viên');

-- =========================
-- USERS
-- =========================
CREATE TABLE users (
                       id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh duy nhất, tự động tăng

                       first_name NVARCHAR(255) NOT NULL,
    -- Họ của người dùng

                       last_name NVARCHAR(255) NOT NULL,
    -- Tên của người dùng

                       email VARCHAR(255) UNIQUE NOT NULL,
    -- Email duy nhất dùng cho đăng nhập local, phải unique

                       phone VARCHAR(20) NULL,
    -- Số điện thoại (tuỳ chọn), duy nhất nếu có giá trị

                       bio NVARCHAR(MAX) NULL,
    ---Giới thiệu bản thân của user (instructor)

                       password_hash VARCHAR(255) NULL,
    -- Hash mật khẩu (BCrypt/Argon2). NULL nếu user chỉ login via Google

                       avatar_url VARCHAR(500) NULL,
    -- URL ảnh đại diện (lưu link từ Azure Blob Storage)

                       google_id VARCHAR(255) NULL,
    -- Google ID nếu user authenticate via OAuth Google

                       status VARCHAR(20) NOT NULL,
    -- Trạng thái: active (hoạt động), banned (cấm)

                       favorite_setup_completed BIT NOT NULL DEFAULT 0,
    -- Đã thiết lập danh mục sở thích hay chưa (0: chưa, 1: rồi)

                       created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo tài khoản (mặc định là thời chạy lệnh CREATE)

                       updated_at DATETIME NULL
    -- Thời gian cập nhật gần nhất
);

-- Index duy nhất cho số điện thoại (chỉ áp dụng đối với các số điện thoại không NULL trong SQL Server)
CREATE UNIQUE NONCLUSTERED INDEX UX_users_phone_notnull ON users(phone) WHERE phone IS NOT NULL;

CREATE TABLE user_roles (
                            id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh tự tăng cho mỗi bản ghi user_role

                            user_id INT NOT NULL,
                            role_id INT NOT NULL,

                            created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian token được tạo
                            updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất


                            CONSTRAINT UQ_user_roles_user_role UNIQUE (user_id, role_id),

                            CONSTRAINT FK_user_roles_user
                                FOREIGN KEY (user_id) REFERENCES users(id),

                            CONSTRAINT FK_user_roles_role
                                FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- ===========================
-- EMAIL VERIFICATION TOKENS
-- ===========================
CREATE TABLE email_verification_tokens (
                                           id INT IDENTITY(1,1) PRIMARY KEY,

                                           created_at DATETIME2 NULL,
                                           updated_at DATETIME2 NULL,

                                           user_id INT NOT NULL UNIQUE,

                                           otp_code VARCHAR(6) NOT NULL,

                                           expired_at DATETIME2 NOT NULL,

                                           used BIT NOT NULL
                                               CONSTRAINT DF_email_verification_tokens_used DEFAULT(0),

                                           resend_count INT NOT NULL
                                               CONSTRAINT DF_email_verification_tokens_resend_count DEFAULT(0),

                                           CONSTRAINT FK_email_verification_tokens_user
                                               FOREIGN KEY (user_id)
                                                   REFERENCES users(id)
                                                   ON DELETE CASCADE
);
-- =========================
-- PASSWORD RESET TOKENS
-- =========================
CREATE TABLE password_reset_tokens (
                                       id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh token reset

                                       user_id INT NOT NULL,
    -- Tham chiếu đến bảng users, người dùng yêu cầu reset password

                                       token VARCHAR(255) NOT NULL,
    -- Token ngẫu nhiên được gửi qua email (dùng cho link reset)

                                       expired_at DATETIME NOT NULL,
    -- Thời gian token hết hiệu lực (thường 1 giờ sau khi tạo)

                                       is_used BIT DEFAULT 0,
    -- Cờ đánh dấu token đã được sử dụng (reset password thành công) hay chưa

                                       created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian token được tạo
                                       updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                                       CONSTRAINT FK_reset_tokens_user
                                           FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =========================
-- SYSTEM LOGS
-- =========================
CREATE TABLE system_logs (
                             id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh log

                             user_id INT NOT NULL,
    -- Tham chiếu đến bảng users, người dùng thực hiện hành động

                             action NVARCHAR(255),
    -- Hành động thực hiện (ví dụ: 'create_course', 'upload_video', 'edit_course', 'delete_lesson')

                             target_type VARCHAR(100),
    -- Loại đối tượng bị tác động (ví dụ: 'course', 'lesson', 'quiz', 'user')

                             target_id VARCHAR(100),
    -- ID của đối tượng bị tác động (để trace được)

                             meta NVARCHAR(MAX) NULL,
    -- Dữ liệu bổ sung dạng JSON (ví dụ chi tiết thay đổi, thông tin cũ/mới)

                             created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian ghi log
                             updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                             CONSTRAINT FK_system_logs_user
                                 FOREIGN KEY (user_id) REFERENCES users(id)
);


-- =========================
-- CATEGORIES
-- =========================
CREATE TABLE categories (
                            id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh danh mục

                            name NVARCHAR(255) NOT NULL,
    -- Tên danh mục (ví dụ: 'Lập trình', 'Thiết kế', 'Kinh doanh')

                            description NVARCHAR(MAX) NULL,
    -- Mô tả chi tiết về danh mục

                            parent_id INT NULL,
    -- ID danh mục cha (cho phép phân cấp danh mục: Lập trình -> Web Development -> Frontend)

                            status VARCHAR(20),
    -- Trạng thái: active (hiển thị), inactive (ẩn)

                            created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo danh mục

                            updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                            CONSTRAINT FK_categories_parent
                                FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- Bảng trung gian lưu danh mục yêu thích của người dùng (Many-to-Many)
CREATE TABLE user_favorite_categories (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    CONSTRAINT FK_user_favorite_categories_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT FK_user_favorite_categories_category
        FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);



-- =========================
-- COURSES
-- =========================
CREATE TABLE courses (
                         id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh khóa học

                         instructor_id INT NOT NULL,
    -- Tham chiếu đến bảng users (role='instructor'), giáo viên tạo khóa học

                         category_id INT NOT NULL,
    -- Tham chiếu đến bảng categories, danh mục khóa học

                         title NVARCHAR(255) NOT NULL,
    -- Tên khóa học (ví dụ: 'Lập trình C Cơ bản')

                         description NVARCHAR(MAX) NULL,
    -- Mô tả chi tiết nội dung, mục tiêu khóa học

                         thumbnail_url NVARCHAR(500) NULL,
    -- URL ảnh bìa khóa học (lưu link từ Azure Blob Storage)

                         price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    -- Giá khóa học (0 = miễn phí)

                         level VARCHAR(20),
    -- Mức độ: beginner (cơ bản), intermediate (trung cấp), advanced (nâng cao)

                         status VARCHAR(20),
    -- Trạng thái: draft (nháp), pending (chờ duyệt), published (đã xuất bản), rejected (từ chối), hidden (ẩn)

                         intro_video_url NVARCHAR(500),
    --Video giới thiệu khoá học
                         approved_by INT NULL,
    -- Tham chiếu đến manager/admin đã phê duyệt khóa học

                         approved_at DATETIME NULL,
    -- Thời gian phê duyệt

                         rejection_reason NVARCHAR(1000) NULL,
    -- Lý do từ chối khóa học (nếu status = rejected)

                         version INT NOT NULL DEFAULT 0,
    -- Số phiên bản dùng cho Optimistic Locking (JPA @Version), tự tăng mỗi lần cập nhật

                         created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo khóa học

                         updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                         CONSTRAINT FK_courses_instructor
                             FOREIGN KEY (instructor_id) REFERENCES users(id),
                         CONSTRAINT FK_courses_category
                             FOREIGN KEY (category_id) REFERENCES categories(id),
                         CONSTRAINT FK_courses_approved_by
                             FOREIGN KEY (approved_by) REFERENCES users(id)
);

-- =========================
-- COURSE SECTIONS
-- =========================
CREATE TABLE course_sections (
                                 id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh section (chương)

                                 course_id INT NOT NULL,
    -- Tham chiếu đến bảng courses, khóa học mà section này thuộc về

                                 title NVARCHAR(255) NOT NULL,
    -- Tên section (ví dụ: 'Chương 1: Nhập môn', 'Chương 2: Cú pháp cơ bản')

                                 position INT NOT NULL,
    -- Thứ tự hiển thị section trong khóa học (1, 2, 3, ...)

                                 created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo section

                                 updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                                 CONSTRAINT FK_sections_course
                                     FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- =========================
-- LESSONS
-- =========================
CREATE TABLE lessons (
                         id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh bài học

                         section_id INT NOT NULL,
    -- Tham chiếu đến bảng course_sections, bài học này thuộc section nào

                         title NVARCHAR(255) NOT NULL,
    -- Tên bài học (ví dụ: 'Bài 1: Giới thiệu ngôn ngữ C')

                         video_url NVARCHAR(500) NULL,
    -- URL video bài học (lưu link từ Azure Blob Storage)

                         duration_seconds INT NULL CHECK (duration_seconds > 0),
    -- Thời lượng video (giây), kiểm tra phải > 0 nếu có giá trị

                         position INT NULL,
    -- Thứ tự hiển thị bài trong section (1, 2, 3, ...)

                         is_published BIT DEFAULT 0,
    -- Cờ đánh dấu bài học đã xuất bản cho học viên hay chưa

                         moderation_status VARCHAR(20) DEFAULT 'PENDING',
    -- Trạng thái kiểm duyệt video bởi Azure AI:
    -- pending (chờ), auto_flagged (AI phát hiện vấn đề), clean (không vấn đề),
    -- approved (manager phê duyệt), rejected (manager từ chối)

                         created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo bài học

                         updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                         CONSTRAINT FK_lessons_section
                             FOREIGN KEY (section_id) REFERENCES course_sections(id)
);


CREATE TABLE lesson_notes (
                              id INT PRIMARY KEY IDENTITY(1,1),

                              user_id INT NOT NULL,
    -- Người ghi chú

                              lesson_id INT NOT NULL,
    -- Ghi chú thuộc bài học nào

                              video_timestamp_seconds INT NOT NULL
                                  CHECK (video_timestamp_seconds >= 0),
    -- Thời điểm trong video (giây)

                              note_content NVARCHAR(MAX) NOT NULL,
    -- Nội dung ghi chú

                              created_at DATETIME DEFAULT GETDATE(),
                              updated_at DATETIME NULL,

                              CONSTRAINT FK_lesson_notes_user
                                  FOREIGN KEY (user_id) REFERENCES users(id),

                              CONSTRAINT FK_lesson_notes_lesson
                                  FOREIGN KEY (lesson_id) REFERENCES lessons(id)
);


-- =========================
-- LESSON MATERIALS
-- =========================
    CREATE TABLE lesson_materials (
                                      id INT PRIMARY KEY IDENTITY(1,1),
        -- Mã định danh tài liệu

                                      instructor_id INT NOT NULL,
        -- Tham chiếu đến bảng users (role='instructor'), giáo viên upload tài liệu

                                      course_id INT NULL,
        -- Tham chiếu đến bảng courses (tài liệu cho cả khóa học nếu có)

                                      lesson_id INT NULL,
        -- Tham chiếu đến bảng lessons (tài liệu cho bài học cụ thể)

                                      file_name NVARCHAR(255) NULL,
        -- Tên file gốc

                                      file_url VARCHAR(500) NULL,
        -- URL file (lưu link từ Azure Blob Storage)

                                      file_type VARCHAR(50) NULL,
        -- Loại file: 'pdf', 'docx', 'pptx', 'zip', 'txt', ...

                                      file_size BIGINT NULL,
        -- Kích thước file (bytes)

                                      created_at DATETIME DEFAULT GETDATE(),
        -- Thời gian upload tài liệu

                                      updated_at DATETIME NULL,
        -- Thời gian cập nhật gần nhất

                                      CONSTRAINT FK_materials_instructor
                                          FOREIGN KEY (instructor_id) REFERENCES users(id),
                                      CONSTRAINT FK_materials_course
                                          FOREIGN KEY (course_id) REFERENCES courses(id),
                                      CONSTRAINT FK_materials_lesson
                                          FOREIGN KEY (lesson_id) REFERENCES lessons(id)
    );

-- =========================
-- QUIZZES
-- =========================
CREATE TABLE quizzes (
    id INT PRIMARY KEY IDENTITY(1,1),

    lesson_id INT NOT NULL,

    title NVARCHAR(255) NOT NULL,

    description NVARCHAR(MAX) NULL,

    pass_score_percent INT NOT NULL
        CHECK(pass_score_percent BETWEEN 0 AND 100),

    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',

    time_limit_minutes INT NULL,

    is_random_question BIT NOT NULL DEFAULT 0,

    is_random_answer BIT NOT NULL DEFAULT 0,

    created_at DATETIME DEFAULT GETDATE(),

    updated_at DATETIME NULL,

    published_at DATETIME NULL,

    CONSTRAINT FK_quizzes_lesson
        FOREIGN KEY (lesson_id)
        REFERENCES lessons(id)
);

-- =========================
-- QUIZ QUESTIONS
-- =========================
CREATE TABLE quiz_questions (
                                id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh câu hỏi

                                quiz_id INT NOT NULL,
    -- Tham chiếu đến bảng quizzes, câu hỏi này thuộc quiz nào

                                question_text NVARCHAR(MAX) NOT NULL,
    -- Nội dung câu hỏi

                                question_type VARCHAR(20),
    -- Loại câu hỏi: single (1 đáp án đúng), multiple (nhiều đáp án đúng)

                                points INT DEFAULT 1,
    -- Điểm thưởng nếu trả lời đúng (mặc định 1)

                                position INT NULL,
    -- Thứ tự câu hỏi trong quiz
	 explanation NVARCHAR(MAX) NULL,



                                created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo câu hỏi

                                updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                                CONSTRAINT FK_questions_quiz
                                    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- =========================
-- QUIZ ANSWERS
-- =========================
CREATE TABLE quiz_answers (
                              id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh đáp án

                              question_id INT NOT NULL,
    -- Tham chiếu đến bảng quiz_questions, đáp án thuộc câu hỏi nào

                              answer_text NVARCHAR(MAX) NOT NULL,
    -- Nội dung đáp án

                              is_correct BIT DEFAULT 0,
    -- Cờ đánh dấu đáp án đúng hay sai (1 = đúng, 0 = sai)

                              position INT NULL,
    -- Thứ tự hiển thị của đáp án

                              created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo đáp án
                              updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                              CONSTRAINT FK_answers_question
                                  FOREIGN KEY (question_id) REFERENCES quiz_questions(id)
);

-- =========================
-- QUIZ ATTEMPTS
-- =========================
CREATE TABLE quiz_attempts (
                               id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh lần làm quiz

                               user_id INT NOT NULL,
    -- Tham chiếu đến bảng users, học viên làm quiz

                               quiz_id INT NOT NULL,
    -- Tham chiếu đến bảng quizzes, quiz nào được làm

                               score DECIMAL(5,2) NULL,
    -- Điểm đạt được (NULL nếu còn đang làm)

                               is_passed BIT NULL,
    -- Cờ đánh dấu pass hay fail (NULL nếu chưa submit, 1 = pass, 0 = fail)

                               started_at DATETIME DEFAULT GETDATE(),
    -- Thời gian bắt đầu làm quiz
                               submitted_at DATETIME NULL,
    -- Thời gian submit quiz (NULL nếu chưa submit)
                               created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo bản ghi
                               updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                               CONSTRAINT FK_attempts_user
                                   FOREIGN KEY (user_id) REFERENCES users(id),
                               CONSTRAINT FK_attempts_quiz
                                   FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- =========================
-- QUIZ ATTEMPT ANSWERS
-- =========================
CREATE TABLE quiz_attempt_answers (
                                      id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh câu trả lời trong lần làm quiz

                                      attempt_id INT NOT NULL,
    -- Tham chiếu đến bảng quiz_attempts, thuộc lần làm bài nào

                                      question_id INT NOT NULL,
    -- Tham chiếu đến bảng quiz_questions, câu hỏi nào được trả lời

                                      selected_answer_id INT NULL,
    -- Tham chiếu đến bảng quiz_answers, đáp án được chọn (nếu có)

                                      is_correct BIT NULL,
    -- Đánh dấu câu trả lời này đúng hay sai (1 = đúng, 0 = sai)

                                      created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian ghi nhận câu trả lời
                                      updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                                      CONSTRAINT FK_attempt_answers_attempt
                                          FOREIGN KEY (attempt_id) REFERENCES quiz_attempts(id) ON DELETE CASCADE,
                                      CONSTRAINT FK_attempt_answers_question
                                          FOREIGN KEY (question_id) REFERENCES quiz_questions(id),
                                      CONSTRAINT FK_attempt_answers_selected_answer
                                          FOREIGN KEY (selected_answer_id) REFERENCES quiz_answers(id)
);

-- =========================
-- CARTS
-- =========================
CREATE TABLE carts (
                       id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh giỏ hàng

                       user_id INT UNIQUE NOT NULL,
    -- Tham chiếu đến bảng users, mỗi user có 1 giỏ hàng duy nhất

                       created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo giỏ hàng

                       updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                       CONSTRAINT FK_carts_user
                           FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =========================
-- CART ITEMS
-- =========================
CREATE TABLE cart_items (
                            id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh item trong giỏ

                            cart_id INT NOT NULL,
    -- Tham chiếu đến bảng carts, item này thuộc giỏ nào

                            course_id INT NOT NULL,
    -- Tham chiếu đến bảng courses, khóa học nào được thêm vào giỏ

                            selected BIT NOT NULL DEFAULT 1,
    -- Trạng thái chọn thanh toán của khóa học (1 = true, 0 = false)

                            created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo bản ghi
                            updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                            CONSTRAINT FK_cart_items_cart
                                FOREIGN KEY (cart_id) REFERENCES carts(id),

                            CONSTRAINT FK_cart_items_course
                                FOREIGN KEY (course_id) REFERENCES courses(id),

    -- Constraint unique để tránh thêm cùng khóa học 2 lần vào giỏ
                            CONSTRAINT UQ_cart_course UNIQUE(cart_id, course_id)
);


-- =========================
-- ORDERS
-- =========================
CREATE TABLE orders (
                        id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh đơn hàng

                        user_id INT NOT NULL,
    -- Tham chiếu đến bảng users, học viên tạo đơn hàng

                        total_amount DECIMAL(10,2) NOT NULL CHECK (total_amount >= 0),
    -- Tổng tiền của đơn hàng

                        status VARCHAR(20),
    -- Trạng thái đơn hàng:
    -- pending (chờ thanh toán), paid (đã thanh toán), completed (hoàn tất),
    -- cancelled (hủy), expired (hết hạn thanh toán)

                        payment_method VARCHAR(50) NULL,
    -- Phương thức thanh toán (ví dụ: 'MOMO', 'VNPAY', 'CARD')

                        created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo đơn hàng

                        updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                        CONSTRAINT FK_orders_user
                            FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =========================
-- ORDER ITEMS
-- =========================
CREATE TABLE order_items (
                             id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh item trong đơn hàng

                             order_id INT NOT NULL,
    -- Tham chiếu đến bảng orders, item này thuộc đơn hàng nào

                             course_id INT NOT NULL,
    -- Tham chiếu đến bảng courses, khóa học nào được thanh toán

                             price_snapshot DECIMAL(10,2) NOT NULL CHECK (price_snapshot >= 0),
    -- Giá gốc khóa học tại thời điểm tạo đơn (snapshot)

                             course_title_snapshot NVARCHAR(255) NULL,
    -- Tên khóa học tại thời điểm tạo đơn (snapshot, dùng cho lịch sử)

                             created_at DATETIME DEFAULT GETDATE(),


                             updated_at DATETIME NULL,

                             CONSTRAINT FK_order_items_order
                                 FOREIGN KEY (order_id) REFERENCES orders(id),
                             CONSTRAINT FK_order_items_course
                                 FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- =========================
-- PAYMENTS
-- =========================
CREATE TABLE payments (
                          id INT PRIMARY KEY IDENTITY(1,1),
    -- Mã định danh duy nhất của giao dịch thanh toán

                          order_id INT NOT NULL UNIQUE,
    -- Mỗi đơn hàng chỉ có một giao dịch thanh toán (1-1 relationship)

                          gateway VARCHAR(50) NOT NULL,
    -- PAYOS, MOMO, VNPAY,...

                          gateway_order_code VARCHAR(255) NOT NULL UNIQUE,
    -- Mã đơn hàng gửi sang cổng thanh toán

                          amount DECIMAL(10,2) NOT NULL
                              CHECK (amount >= 0),
    -- Số tiền thanh toán

                          payment_url VARCHAR(1000) NULL,
    -- Link thanh toán PayOS

                          qr_code_url VARCHAR(1000) NULL,
    -- Link QR động

                          status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    -- Trạng thái giao dịch

                          error_code VARCHAR(50) NULL,
    -- Mã lỗi từ gateway

                          error_message NVARCHAR(500) NULL,
    -- Nội dung lỗi

                          gateway_response NVARCHAR(MAX) NULL,
    -- JSON response từ PayOS
                          expired_at DATETIME NULL,
    -- Thời điểm QR/link hết hạn

                          paid_at DATETIME NULL,
    -- Thời điểm thanh toán thành công

                          last_synced_at DATETIME NULL,
    -- Thời điểm sync gần nhất với PayOS
    -- Dùng để tránh query PayOS quá tần suất (skip nếu < 5 phút)
                           account_number VARCHAR(100) NULL,
                           description NVARCHAR(500) NULL,
                           bank_name NVARCHAR(100) NULL,
                           account_holder NVARCHAR(255) NULL,

                          created_at DATETIME NOT NULL DEFAULT GETDATE(),
    -- Thời điểm tạo giao dịch

                          updated_at DATETIME NULL,
    -- Thời điểm cập nhật gần nhất

                          CONSTRAINT FK_payments_order
                              FOREIGN KEY (order_id)
                                  REFERENCES orders(id)
);

-- =========================
-- ENROLLMENTS
-- =========================
CREATE TABLE enrollments (
                             id INT PRIMARY KEY IDENTITY(1,1),
                             user_id INT NOT NULL,
                             course_id INT NOT NULL,
                             progress_percent DECIMAL(5,2) DEFAULT 0,
                             completed_at DATETIME NULL,
                             created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo bản ghi
                             updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                             CONSTRAINT UQ_enrollment UNIQUE (user_id, course_id),
                             CONSTRAINT FK_enrollments_user
                                 FOREIGN KEY (user_id) REFERENCES users(id),
                             CONSTRAINT FK_enrollments_course
                                 FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- =========================
-- LESSON PROGRESS
-- =========================
CREATE TABLE lesson_progress (
                                 id INT PRIMARY KEY IDENTITY(1,1),
                                 enrollment_id INT NOT NULL,
                                 lesson_id INT NOT NULL,
                                 is_completed BIT DEFAULT 0,
                                 last_accessed DATETIME NULL,
                                 created_at DATETIME DEFAULT GETDATE(),
    -- Thời gian tạo bản ghi
                                 updated_at DATETIME NULL,
    -- Thời gian cập nhật gần nhất

                                 CONSTRAINT UQ_lesson_progress UNIQUE (enrollment_id, lesson_id),
                                 CONSTRAINT FK_lesson_progress_enrollment
                                     FOREIGN KEY (enrollment_id) REFERENCES enrollments(id),
                                 CONSTRAINT FK_lesson_progress_lesson
                                     FOREIGN KEY (lesson_id) REFERENCES lessons(id)
);

-- =========================
-- FEEDBACKS
-- =========================
CREATE TABLE feedbacks (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    course_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment NVARCHAR(MAX) NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'VISIBLE',
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME NULL,
    CONSTRAINT FK_feedbacks_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT FK_feedbacks_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT UQ_feedback_user_course UNIQUE(user_id, course_id)
);



-- ==========================================
-- PERFORMANCE INDEXES (MỚI - TỐI ƯU HÓA TRUY VẤN)
-- ==========================================
CREATE INDEX IX_courses_instructor ON courses(instructor_id);
CREATE INDEX IX_courses_category ON courses(category_id);
CREATE INDEX IX_course_sections_course ON course_sections(course_id);
CREATE INDEX IX_lessons_section ON lessons(section_id);
CREATE INDEX IX_quiz_attempts_user_quiz ON quiz_attempts(user_id, quiz_id);
CREATE INDEX IX_quiz_attempt_answers_attempt ON quiz_attempt_answers(attempt_id);
CREATE INDEX IX_lesson_progress_lookup ON lesson_progress(enrollment_id, lesson_id);
CREATE INDEX IX_orders_user ON orders(user_id);
CREATE INDEX IX_order_items_order ON order_items(order_id);
CREATE INDEX IX_payments_order ON payments(order_id);
CREATE INDEX IX_enrollments_lookup ON enrollments(user_id, course_id);

-- ==========================================
-- PAYMENT SYNCHRONIZATION INDEXES (MỚI)
-- ==========================================
-- Index 1: Tối ưu query tìm các payment hết hạn (expirePaymentsByTimeout)
CREATE INDEX IX_payment_status_expired_at ON payments(status, expired_at, updated_at);

-- Index 2: Tối ưu query tìm PENDING cần sync từ PayOS (syncPendingPaymentsFromPayOs)
CREATE INDEX IX_payment_status_created_at ON payments(status, created_at, last_synced_at);




CREATE INDEX IX_feedback_course ON feedbacks(course_id);
GO
