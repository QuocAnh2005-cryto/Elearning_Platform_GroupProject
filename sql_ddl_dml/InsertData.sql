-- USE [ElearningPlatform];
-- GO


-- =========================
-- ROLES SAMPLE DATA
-- =========================

-- Role đã chèn trước ở bảng Elearning nên sẽ không cần tạo lại

-- =========================
-- USERS SAMPLE DATA
-- Password cho tất cả user:
-- $2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy
-- =========================

INSERT INTO users (
    first_name,
    last_name,
    email,
    phone,
    bio,
    password_hash,
    avatar_url,
    google_id,
    status
)
VALUES
-- ADMIN
(N'Nguyễn', N'An', 'admin1@elearning.com', '0901000001',
 NULL,
 '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy',
 '01da5229-793f-49a5-a3a6-a181f9d4d19f_Screenshot%202026-07-01%20083102.png',
 NULL,
 'ACTIVE'),

(N'Trần', N'Bình', 'admin2@elearning.com', '0901000002',
 NULL,
 '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy',
 '06a98026-0028-498b-b6f4-af64ce94cbe0_i1.jpg',
 NULL,
 'ACTIVE'),

-- MANAGER
(N'Lê', N'Cường', 'manager1@elearning.com', '0902000001',
 NULL,
 '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy',
 '2605ba54-3b34-4dd8-936f-827d591e8cc9_kawai_ks2.jpg',
 NULL,
 'ACTIVE'),

(N'Phạm', N'Dung', 'manager2@elearning.com', '0902000002',
 NULL,
 '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy',
 'images.jpg',
 NULL,
 'ACTIVE'),

-- INSTRUCTOR
(N'Hoàng', N'Giang', 'instructor1@elearning.com', '0903000001',
 N'Java Backend Instructor với hơn 5 năm kinh nghiệm.',
 '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy',
 'images (1).jpg',
 NULL,
 'ACTIVE'),

(N'Vũ', N'Hải', 'instructor2@elearning.com', '0903000002',
 N'Spring Boot và SQL Server Instructor.',
 '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy',
 'images (2).jpg',
 NULL,
 'ACTIVE'),

-- LEARNER
(N'Đỗ', N'Minh', 'instructor3@elearning.com', '0903000003',
 N'Giảng viên ReactJS & Front-End.',
 '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy',
 'images (3).jpg',
 NULL,
 'ACTIVE'),

(N'Bùi', N'Ngọc', 'instructor4@elearning.com', '0903000004',
 N'Chuyên gia CSS và UI/UX.',
 '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy',
 'images (4).jpg',
 NULL,
 'ACTIVE'),

(N'Nguyễn', N'Tuấn', 'instructor5@elearning.com', '0903000005',
 N'Giảng viên Node.js & Backend.',
 '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy',
 'images (17).jpg',
 NULL,
 'ACTIVE'),

(N'Lê', N'Hương', 'instructor6@elearning.com', '0903000006',
 N'Giảng viên Python & Data Science.',
 '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy',
 'images (16).jpg',
 NULL,
 'ACTIVE'),

(N'Phạm', N'Vy', 'instructor7@elearning.com', '0903000007',
 N'Giảng viên Java & Spring Boot.',
 '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy',
 'images (15).jpg',
 NULL,
 'ACTIVE'),

(N'Nguyễn', N'Thảo', 'instructor8@elearning.com', '0903000008', N'Giảng viên iOS Swift & SwiftUI.', '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (14).jpg', NULL, 'ACTIVE'),
(N'Trần', N'Linh', 'learner7@elearning.com', '0904000007', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (13).jpg', NULL, 'ACTIVE'),
(N'Lê', N'Kha', 'learner8@elearning.com', '0904000008', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (12).jpg', NULL, 'ACTIVE'),
(N'Phạm', N'Phong', 'learner9@elearning.com', '0904000009', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (11).jpg', NULL, 'ACTIVE'),
(N'Hoàng', N'Sơn', 'learner10@elearning.com', '0904000010', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (10).jpg', NULL, 'ACTIVE'),
(N'Vũ', N'Lan', 'learner11@elearning.com', '0904000011', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', '01da5229-793f-49a5-a3a6-a181f9d4d19f_Screenshot%202026-07-01%20083102.png', NULL, 'ACTIVE'),
(N'Đặng', N'Hùng', 'learner12@elearning.com', '0904000012', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', '06a98026-0028-498b-b6f4-af64ce94cbe0_i1.jpg', NULL, 'ACTIVE'),
(N'Bùi', N'Trang', 'learner13@elearning.com', '0904000013', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', '2605ba54-3b34-4dd8-936f-827d591e8cc9_kawai_ks2.jpg', NULL, 'ACTIVE'),
(N'Đỗ', N'Phúc', 'learner14@elearning.com', '0904000014', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images.jpg', NULL, 'ACTIVE'),
(N'Hồ', N'Quân', 'learner15@elearning.com', '0904000015', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (1).jpg', NULL, 'ACTIVE'),
(N'Ngô', N'Mai', 'learner16@elearning.com', '0904000016', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (2).jpg', NULL, 'ACTIVE'),
(N'Dương', N'Nam', 'learner17@elearning.com', '0904000017', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (3).jpg', NULL, 'ACTIVE'),
(N'Lý', N'Hà', 'learner18@elearning.com', '0904000018', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (4).jpg', NULL, 'ACTIVE'),
(N'Vương', N'Tú', 'learner19@elearning.com', '0904000019', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (17).jpg', NULL, 'ACTIVE'),
(N'Trịnh', N'Hải', 'learner20@elearning.com', '0904000020', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (16).jpg', NULL, 'ACTIVE'),
(N'Đoàn', N'Hòa', 'learner21@elearning.com', '0904000021', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (15).jpg', NULL, 'ACTIVE'),
(N'Lâm', N'Yến', 'learner22@elearning.com', '0904000022', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (14).jpg', NULL, 'ACTIVE'),
(N'Phùng', N'Cường', 'learner23@elearning.com', '0904000023', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (13).jpg', NULL, 'ACTIVE'),
(N'Tống', N'Huy', 'learner24@elearning.com', '0904000024', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (12).jpg', NULL, 'ACTIVE'),
(N'Diệp', N'Trúc', 'learner25@elearning.com', '0904000025', NULL, '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy', 'images (11).jpg', NULL, 'ACTIVE');

 -- =========================
-- USER ROLES SAMPLE DATAở 
-- =========================

INSERT INTO user_roles (user_id, role_id)
VALUES
-- Admin
(1, 1),
(2, 1),

-- Manager
(3, 2),
(4, 2),

-- Instructor
(5, 3),
(6, 3),
(7, 3),
(8, 3),
(9, 3),
(10, 3),
(11, 3),
(12, 3),

-- Learner
(13, 4),
(14, 4),
(15, 4),
(16, 4),
(17, 4),
(18, 4),
(19, 4),
(20, 4),
(21, 4),
(22, 4),
(23, 4),
(24, 4),
(25, 4),
(26, 4),
(27, 4),
(28, 4),
(29, 4),
(30, 4),
(31, 4);

-- =========================
-- CATEGORY
-- =========================
-- Đăng ký các danh mục cha trước (parent_id = NULL)
INSERT INTO categories (name, description, parent_id, status)
VALUES (N'Lập trình Front-End', N'Các khóa học lập trình Front-End', NULL, 'ACTIVE'); -- ID = 1
INSERT INTO categories (name, description, parent_id, status)
VALUES (N'Lập trình Back-End', N'Các khóa học lập trình Back-End', NULL, 'ACTIVE'); -- ID = 2
INSERT INTO categories (name, description, parent_id, status)
VALUES (N'Lập trình iOS', N'Các khóa học lập trình iOS', NULL, 'ACTIVE');
-- ID = 3

-- Đăng ký các danh mục con (parent_id trỏ về ID cha tương ứng)
INSERT INTO categories (name, description, parent_id, status)
VALUES (N'HTML', N'HTML5 cơ bản và nâng cao', 1, 'ACTIVE'); -- ID = 4
INSERT INTO categories (name, description, parent_id, status)
VALUES (N'CSS', N'CSS3, Flexbox, Grid, Responsive', 1, 'ACTIVE'); -- ID = 5
INSERT INTO categories (name, description, parent_id, status)
VALUES (N'React', N'ReactJS Component, Hooks, Redux', 1, 'ACTIVE'); -- ID = 6
INSERT INTO categories (name, description, parent_id, status)
VALUES (N'Node.js', N'Backend với Express, Node.js', 2, 'ACTIVE'); -- ID = 7
INSERT INTO categories (name, description, parent_id, status)
VALUES (N'Python', N'Lập trình Python từ cơ bản đến nâng cao', 2, 'ACTIVE'); -- ID = 8
INSERT INTO categories (name, description, parent_id, status)
VALUES (N'Java', N'Lập trình Java core và nâng cao', 2, 'ACTIVE'); -- ID = 9
INSERT INTO categories (name, description, parent_id, status)
VALUES (N'Swift', N'Ngôn ngữ lập trình Swift', 3, 'ACTIVE'); -- ID = 10
INSERT INTO categories (name, description, parent_id, status)
VALUES (N'SwiftUI', N'Thiết kế giao diện SwiftUI', 3, 'ACTIVE');

-- =========================
-- COURSES SAMPLE DATA
-- =========================

INSERT INTO courses (
    instructor_id,
    category_id,
    title,
    description,
    thumbnail_url,
    price,
    level,
    status,
    approved_by,
    approved_at
)
VALUES

-- Instructor 1
(
    5,
    9,
    N'Java Core Từ Cơ Bản Đến Nâng Cao',
    N'Học Java từ cú pháp cơ bản, OOP, Collections đến xử lý ngoại lệ.',
    N'Java Core Từ Cơ Bản Đến Nâng Cao.jpg',
    1000,
    'BEGINNER',
    'PUBLISHED',
    3,
    GETDATE()
),

(
    5,
    6,
    N'ReactJS Thực Chiến',
    N'Xây dựng ứng dụng ReactJS sử dụng Hooks, Router và Redux Toolkit.',
    N'ReactJS Thực Chiến.png',
    1000,
    'INTERMEDIATE',
    'PUBLISHED',
    3,
    GETDATE()
),

(
    5,
    5,
    N'CSS Mastery',
    N'Flexbox, Grid, Animation và Responsive Design chuyên sâu.',
    N'CSS Mastery.jpg',
    5000,
    'BEGINNER',
    'PUBLISHED',
    3,
    GETDATE()
),

-- Instructor 2
(
    6,
    7,
    N'Node.js REST API Với Express',
    N'Thiết kế và xây dựng RESTful API bằng Node.js và Express.',
    N'Node.js REST API Với Express.jpg',
    10000,
    'INTERMEDIATE',
    'PUBLISHED',
    4,
    GETDATE()
),

(
    6,
    8,
    N'Python Cho Người Mới Bắt Đầu',
    N'Học Python từ cơ bản đến lập trình hướng đối tượng.',
    N'Python Cho Người Mới Bắt Đầu.jpg',
    2000,
    'BEGINNER',
    'PUBLISHED',
    4,
    GETDATE()
),

(
    6,
    10,
    N'Swift Programming Fundamentals',
    N'Lập trình ứng dụng iOS bằng ngôn ngữ Swift.',
    N'Swift Programming Fundamentals.jpg',
    1000,
    'BEGINNER',
    'PUBLISHED',
    4,
    GETDATE()
),

-- Instructor 3
(
    7,
    11,
    N'Xây Dựng Giao Diện Với SwiftUI',
    N'Thiết kế giao diện hiện đại trên iOS bằng SwiftUI.',
    N'Xây Dựng Giao Diện Với SwiftUI.jpg',
    1000,
    'INTERMEDIATE',
    'PUBLISHED',
    4,
    GETDATE()
),

(
    7,
    9,
    N'Spring Boot REST API',
    N'Xây dựng hệ thống Backend với Spring Boot, JPA và Security.',
    N'Spring Boot REST API.jpg',
    1000,
    'ADVANCED',
    'PUBLISHED',
    4,
    GETDATE()
),

-- HTML (ID = 4)
(
    7,
    4,
    N'HTML5 & CSS3 Cơ Bản Cho Người Mới',
    N'Khóa học nền tảng thiết kế giao diện web với HTML5 và CSS3.',
    N'HTML5 & CSS3 Cơ Bản Cho Người Mới.jpg',
    190000,
    'BEGINNER',
    'PUBLISHED',
    3,
    GETDATE()
),

-- Instructor 4
(
    8,
    4,
    N'Thiết Kế Web Landing Page Với HTML5',
    N'Lập trình và hoàn thiện giao diện landing page chuyên nghiệp.',
    N'Thiết Kế Web Landing Page Với HTML5.jpg',
    350000,
    'INTERMEDIATE',
    'PUBLISHED',
    4,
    GETDATE()
),

-- CSS (ID = 5)
(
    8,
    5,
    N'Tailwind CSS Từ Zero Đến Hero',
    N'Sử dụng Tailwind CSS để thiết kế nhanh các giao diện hiện đại.',
    N'Tailwind CSS Từ Zero Đến Hero.jpg',
    250000,
    'BEGINNER',
    'PUBLISHED',
    3,
    GETDATE()
),
(
    8,
    5,
    N'Responsive Web Design Với Flexbox & Grid',
    N'Làm chủ bố cục Responsive với CSS Flexbox và CSS Grid.',
    N'Responsive Web Design Với Flexbox & Grid.jpg',
    450000,
    'INTERMEDIATE',
    'PUBLISHED',
    4,
    GETDATE()
),

-- Instructor 5 (React ID = 6)
(
    9,
    6,
    N'React Native - Lập Trình Di Động Thực Chiến',
    N'Xây dựng ứng dụng di động đa nền tảng Android/iOS bằng React Native.',
    N'React Native - Lập Trình Di Động Thực Chiến.jpg',
    890000,
    'ADVANCED',
    'PUBLISHED',
    3,
    GETDATE()
),
(
    9,
    6,
    N'Next.js 14 - Tối Ưu Hóa Ứng Dụng React',
    N'Xây dựng ứng dụng Server-side Rendering với Next.js 14 mới nhất.',
    N'Next.js 14 - Tối Ưu Hóa Ứng Dụng React.png',
    1150000,
    'ADVANCED',
    'PUBLISHED',
    4,
    GETDATE()
),

-- Node.js (ID = 7)
(
    9,
    7,
    N'Node.js RESTful API & NestJS nâng cao',
    N'Thiết kế kiến trúc hệ thống chuyên nghiệp với NestJS và Node.js.',
    N'Node.js RESTful API & NestJS nâng cao.png',
    1290000,
    'ADVANCED',
    'PUBLISHED',
    3,
    GETDATE()
),

-- Instructor 6
(
    10,
    7,
    N'Lập Trình Backend Thực Chiến Với Node.js & MongoDB',
    N'Kết nối Express, Node.js với cơ sở dữ liệu MongoDB NoSQL.',
    N'Lập Trình Backend Thực Chiến Với Node.js & MongoDB.jpg',
    490000,
    'INTERMEDIATE',
    'PUBLISHED',
    4,
    GETDATE()
),

-- Python (ID = 8)
(
    10,
    8,
    N'Phân Tích Dữ Liệu Với Python, Pandas & NumPy',
    N'Sử dụng thư viện Pandas và NumPy để xử lý và phân tích số liệu.',
    N'Phân Tích Dữ Liệu Với Python, Pandas & NumPy.jpg',
    750000,
    'INTERMEDIATE',
    'PUBLISHED',
    3,
    GETDATE()
),
(
    10,
    8,
    N'Django & Python - Xây Dựng Website Tin Tức',
    N'Tạo dự án Website tin tức hoàn thiện bằng Python & Django Framework.',
    N'Django & Python - Xây Dựng Website Tin Tức.jpg',
    590000,
    'INTERMEDIATE',
    'PUBLISHED',
    4,
    GETDATE()
),

-- Instructor 7 (Java ID = 9)
(
    11,
    9,
    N'Khóa học lập trình java đến OOP',
    N'Học 4 tính chất OOP cơ bản trong Java: Kế thừa, Đa hình, Đóng gói, Trừu tượng.',
    N'Lập Trình Hướng Đối Tượng Java Core Cơ Bản.jpg',
    150000,
    'BEGINNER',
    'PUBLISHED',
    3,
    GETDATE()
),
(
    11,
    9,
    N'Microservices Với Spring Boot & Spring Cloud',
    N'Xây dựng hệ thống phân tán chịu tải cao bằng Microservices.',
    N'Microservices Với Spring Boot & Spring Cloud.png',
    1490000,
    'ADVANCED',
    'PUBLISHED',
    4,
    GETDATE()
),

-- Swift (ID = 10)
(
    11,
    10,
    N'iOS Development Swift & Xcode Thực Hành',
    N'Tự làm ứng dụng iOS đầu tiên sử dụng Storyboard, AutoLayout và Xcode.',
    N'iOS Development Swift & Xcode Thực Hành.jpg',
    650000,
    'BEGINNER',
    'PUBLISHED',
    3,
    GETDATE()
),

-- Instructor 8
(
    12,
    10,
    N'Cấu Trúc Dữ Liệu & Giải Thuật Bằng Swift',
    N'Cải thiện tư duy thuật toán khi thiết kế logic trên iOS.',
    N'Cấu Trúc Dữ Liệu & Giải Thuật Bằng Swift.jpg',
    990000,
    'ADVANCED',
    'PUBLISHED',
    4,
    GETDATE()
),

-- SwiftUI (ID = 11)
(
    12,
    11,
    N'SwiftUI Animation - Chuyển Động Đẹp Mắt',
    N'Tạo các chuyển cảnh, chuyển động mượt mà bằng SwiftUI Animation.',
    N'SwiftUI Animation - Chuyển Động Đẹp Mắt.jpg',
    550000,
    'ADVANCED',
    'PUBLISHED',
    3,
    GETDATE()
),
(
    12,
    11,
    N'Xây Dựng Clone App iOS Với SwiftUI & Firebase',
    N'Clone ứng dụng mạng xã hội nổi tiếng sử dụng SwiftUI và Realtime Database.',
    N'Xây Dựng Clone App iOS Với SwiftUI & Firebase.jpg',
    1090000,
    'INTERMEDIATE',
    'PUBLISHED',
    4,
    GETDATE()
),
(
    5,
    6,
    N'ReactJS Thực Chiến Nâng Cao 25',
    N'Học ReactJS chuyên sâu với các bài giảng thực tế từ TrungQuanDev.',
    N'ReactJS Thực Chiến.png',
    150000,
    'ADVANCED',
    'PUBLISHED',
    3,
    GETDATE()
);

-- =========================
-- COURSE SECTIONS SAMPLE DATA
-- =========================

-- Course 1: Java Core Từ Cơ Bản Đến Nâng Cao
INSERT INTO course_sections (course_id, title, position)
VALUES
(1, N'Chương 1: Giới thiệu Java', 1),
(1, N'Chương 2: Lập trình hướng đối tượng', 2),
(1, N'Chương 3: Collections và Exception', 3);

-- Course 2: ReactJS Thực Chiến
INSERT INTO course_sections (course_id, title, position)
VALUES
(2, N'Chương 1: React Fundamentals', 1),
(2, N'Chương 2: React Hooks', 2),
(2, N'Chương 3: Redux Toolkit', 3);

-- Course 3: CSS Mastery
INSERT INTO course_sections (course_id, title, position)
VALUES
(3, N'Chương 1: CSS Cơ Bản', 1),
(3, N'Chương 2: Flexbox và Grid', 2),
(3, N'Chương 3: Responsive Design', 3);

-- Course 4: Node.js REST API Với Express
INSERT INTO course_sections (course_id, title, position)
VALUES
(4, N'Chương 1: Node.js Cơ Bản', 1),
(4, N'Chương 2: Express Framework', 2),
(4, N'Chương 3: REST API Thực Chiến', 3);

-- Course 5: Python Cho Người Mới Bắt Đầu
INSERT INTO course_sections (course_id, title, position)
VALUES
(5, N'Chương 1: Python Fundamentals', 1),
(5, N'Chương 2: Hàm và Module', 2),
(5, N'Chương 3: OOP Với Python', 3);

-- Course 6: Swift Programming Fundamentals
INSERT INTO course_sections (course_id, title, position)
VALUES
(6, N'Chương 1: Swift Basics', 1),
(6, N'Chương 2: Control Flow', 2),
(6, N'Chương 3: Object-Oriented Programming', 3);

-- Course 7: Xây Dựng Giao Diện Với SwiftUI
INSERT INTO course_sections (course_id, title, position)
VALUES
(7, N'Chương 1: SwiftUI Introduction', 1),
(7, N'Chương 2: Layout và Navigation', 2),
(7, N'Chương 3: State Management', 3);

-- Course 8: Spring Boot REST API
INSERT INTO course_sections (course_id, title, position)
VALUES
(8, N'Chương 1: Spring Boot Fundamentals', 1),
(8, N'Chương 2: Spring Data JPA', 2),
(8, N'Chương 3: Spring Security', 3);

-- Course 9: HTML5 & CSS3 Cơ Bản Cho Người Mới
INSERT INTO course_sections (course_id, title, position) VALUES
(9, N'Chương 1: Giới thiệu HTML5', 1),
(9, N'Chương 2: Thẻ và Bố cục cơ bản', 2),
(9, N'Chương 3: Kết hợp CSS3 cơ bản', 3);

-- Course 10: Thiết Kế Web Landing Page Với HTML5
INSERT INTO course_sections (course_id, title, position) VALUES
(10, N'Chương 1: Lập kế hoạch Landing Page', 1),
(10, N'Chương 2: Cấu trúc HTML & Layout', 2),
(10, N'Chương 3: Hoàn thiện Landing Page', 3);

-- Course 11: Tailwind CSS Từ Zero Đến Hero
INSERT INTO course_sections (course_id, title, position) VALUES
(11, N'Chương 1: Setup Tailwind CSS', 1),
(11, N'Chương 2: Tailwind Utility Classes', 2),
(11, N'Chương 3: Responsive & Customization', 3);

-- Course 12: Responsive Web Design Với Flexbox & Grid
INSERT INTO course_sections (course_id, title, position) VALUES
(12, N'Chương 1: Tư duy Responsive', 1),
(12, N'Chương 2: Flexbox Layout', 2),
(12, N'Chương 3: Grid Layout chuyên sâu', 3);

-- Course 13: React Native - Lập Trình Di Động Thực Chiến
INSERT INTO course_sections (course_id, title, position) VALUES
(13, N'Chương 1: Khởi động dự án React Native', 1),
(13, N'Chương 2: Core Components & Navigation', 2),
(13, N'Chương 3: API & State Management', 3);

-- Course 14: Next.js 14 - Tối Ưu Hóa Ứng Dụng React
INSERT INTO course_sections (course_id, title, position) VALUES
(14, N'Chương 1: App Router & Routing', 1),
(14, N'Chương 2: Server & Client Components', 2),
(14, N'Chương 3: Data Fetching & Caching', 3);

-- Course 15: Node.js RESTful API & NestJS nâng cao
INSERT INTO course_sections (course_id, title, position) VALUES
(15, N'Chương 1: Kiến trúc NestJS', 1),
(15, N'Chương 2: Controllers & Providers', 2),
(15, N'Chương 3: Authentication & Security', 3);

-- Course 16: Lập Trình Backend Thực Chiến Với Node.js & MongoDB
INSERT INTO course_sections (course_id, title, position) VALUES
(16, N'Chương 1: Cơ sở dữ liệu NoSQL MongoDB', 1),
(16, N'Chương 2: Mongoose ODM trong Express', 2),
(16, N'Chương 3: CRUD & Middlewares', 3);

-- Course 17: Phân Tích Dữ Liệu Với Python, Pandas & NumPy
INSERT INTO course_sections (course_id, title, position) VALUES
(17, N'Chương 1: Giới thiệu NumPy', 1),
(17, N'Chương 2: Pandas DataFrames', 2),
(17, N'Chương 3: Trực quan hóa dữ liệu', 3);

-- Course 18: Django & Python - Xây Dựng Website Tin Tức
INSERT INTO course_sections (course_id, title, position) VALUES
(18, N'Chương 1: Cấu trúc Django Project', 1),
(18, N'Chương 2: Models & Django Admin', 2),
(18, N'Chương 3: Views, Templates & URL routing', 3);

-- Course 19: Lập Trình Hướng Đối Tượng Java Core Cơ Bản
INSERT INTO course_sections (course_id, title, position) VALUES
(19, N'Chương 1: Giới thiệu về Java và cách cài đặt môi trường', 1),
(19, N'Chương 2: Lớp và Đối tượng', 2),
(19, N'Chương 3: Tính chất OOP cơ bản', 3);

-- Course 20: Microservices Với Spring Boot & Spring Cloud
INSERT INTO course_sections (course_id, title, position) VALUES
(20, N'Chương 1: Kiến trúc Microservices', 1),
(20, N'Chương 2: Service Discovery & Gateway', 2),
(20, N'Chương 3: Distributed Tracing & Config', 3);

-- Course 21: iOS Development Swift & Xcode Thực Hành
INSERT INTO course_sections (course_id, title, position) VALUES
(21, N'Chương 1: Giao diện Xcode', 1),
(21, N'Chương 2: Storyboard & AutoLayout', 2),
(21, N'Chương 3: ViewControllers & Lifecycle', 3);

-- Course 22: Cấu Trúc Dữ Liệu & Giải Thuật Bằng Swift
INSERT INTO course_sections (course_id, title, position) VALUES
(22, N'Chương 1: Các cấu trúc dữ liệu cơ bản', 1),
(22, N'Chương 2: Thuật toán Sắp xếp & Tìm kiếm', 2),
(22, N'Chương 3: Cây & Đồ thị trong Swift', 3);

-- Course 23: SwiftUI Animation - Chuyển Động Đẹp Mắt
INSERT INTO course_sections (course_id, title, position) VALUES
(23, N'Chương 1: Animatable Properties', 1),
(23, N'Chương 2: Transitions & Spring Animations', 2),
(23, N'Chương 3: Custom Animations nâng cao', 3);

-- Course 24: Xây Dựng Clone App iOS Với SwiftUI & Firebase
INSERT INTO course_sections (course_id, title, position) VALUES
(24, N'Chương 1: Cấu hình Firebase SDK', 1),
(24, N'Chương 2: Đăng nhập & Đăng ký', 2),
(24, N'Chương 3: Realtime Database & Storage', 3);

-- Course 25: ReactJS Thực Chiến Nâng Cao 25
INSERT INTO course_sections (course_id, title, position) VALUES
(25, N'Chương 1: Khởi đầu dự án 25', 1),
(25, N'Chương 2: Thiết kế giao diện & Hooks 25', 2),
(25, N'Chương 3: Tối ưu hóa ứng dụng 25', 3);

-- =========================
-- LESSONS SAMPLE DATA
-- Mỗi section có 3 bài học
-- =========================

INSERT INTO lessons (
    section_id,
    title,
    video_url,
    duration_seconds,
    position,
    is_published,
    moderation_status
)
SELECT 
    cs.id,
    CASE 
        WHEN CHARINDEX(':', cs.title) > 0 
        THEN SUBSTRING(cs.title, CHARINDEX(':', cs.title) + 2, LEN(cs.title))
        ELSE cs.title
    END + 
    CASE n.num
        WHEN 1 THEN N' - Bài 1: Khái niệm & Cơ bản'
        WHEN 2 THEN N' - Bài 2: Thực hành & Chi tiết'
        WHEN 3 THEN N' - Bài 3: Tổng kết & Ứng dụng'
    END,
    CASE n.num
        WHEN 1 THEN N'Recording 2026-05-28 212131.mp4'
        WHEN 2 THEN N'Lập trình C - 03. Cách xuất dữ liệu ra màn hình lập trình C - Hàm printf - Tự học lập trình C.mp4'
        WHEN 3 THEN N'Lập trình C - 04. Cách nhập dữ liệu từ bàn phím trong lập trình C - Tự học lập trình C.mp4'
    END,
    CASE n.num
        WHEN 1 THEN 600
        WHEN 2 THEN 720
        WHEN 3 THEN 840
    END,
    n.num,
    1,
    'APPROVED'
FROM course_sections cs
CROSS JOIN (
    SELECT 1 AS num UNION ALL
    SELECT 2 AS num UNION ALL
    SELECT 3 AS num
) n
WHERE cs.course_id NOT IN (2, 3, 4, 25);


-- Chèn lesson tùy chỉnh cho course id = 2, chia đều vào 3 section
DECLARE @secId1 INT = (SELECT id FROM course_sections WHERE course_id = 2 AND position = 1);
DECLARE @secId2 INT = (SELECT id FROM course_sections WHERE course_id = 2 AND position = 2);
DECLARE @secId3 INT = (SELECT id FROM course_sections WHERE course_id = 2 AND position = 3);

-- Section 1: React Fundamentals
INSERT INTO lessons (section_id, title, video_url, duration_seconds, position, is_published, moderation_status)
VALUES
(@secId1, N'1. Cài đặt môi trường cho dự án (NVM, Node, Git, Yarn...vv) - ReactJS + Material UI - TrungQuanDev (1)', N'1. Cài đặt môi trường cho dự án (NVM, Node, Git, Yarn...vv) - ReactJS + Material UI - TrungQuanDev (1).mp4', 600, 1, 1, 'APPROVED'),
(@secId1, N'2. Vite, Create React App và NextJS - Lựa chọn cái nào- - ReactJS + Material UI - TrungQuanDev', N'2. Vite, Create React App và NextJS - Lựa chọn cái nào- - ReactJS + Material UI - TrungQuanDev.mp4', 720, 2, 1, 'APPROVED'),
(@secId1, N'3. Vite - Hiểu toàn bộ Code Base ban đầu - Push lên GitHub - ReactJS + Material UI - TrungQuanDev', N'3. Vite - Hiểu toàn bộ Code Base ban đầu - Push lên GitHub - ReactJS + Material UI - TrungQuanDev.mp4', 840, 3, 1, 'APPROVED');

-- Section 2: React Hooks
INSERT INTO lessons (section_id, title, video_url, duration_seconds, position, is_published, moderation_status)
VALUES
(@secId2, N'4. JSX là gì- Hiểu Tường Tận về JSX cho các bạn - ReactJS + Material UI - TrungQuanDev', N'4. JSX là gì- Hiểu Tường Tận về JSX cho các bạn - ReactJS + Material UI - TrungQuanDev.mp4', 900, 1, 1, 'APPROVED'),
(@secId2, N'1.1 Cách đọc đúng của Vite -vit- (video ngắn bổ sung) - ReactJS + Material UI - TrungQuanDev', N'1.1 Cách đọc đúng của Vite -vit- (video ngắn bổ sung) - ReactJS + Material UI - TrungQuanDev.mp4', 300, 2, 1, 'APPROVED'),
(@secId2, N'5. Semantic Versioning là gì- Lưu ý Quan Trọng về bộ Code Base của dự án - ReactJS + Material UI', N'5. Semantic Versioning là gì- Lưu ý Quan Trọng về bộ Code Base của dự án - ReactJS + Material UI.mp4', 960, 3, 1, 'APPROVED');

-- Section 3: Redux Toolkit
INSERT INTO lessons (section_id, title, video_url, duration_seconds, position, is_published, moderation_status)
VALUES
(@secId3, N'8. Cài đặt và sử dụng Material UI (Emotion, Fonts, Icons…vv) - ReactJS + Material UI - TrungQuanDev', N'8. Cài đặt và sử dụng Material UI (Emotion, Fonts, Icons…vv) - ReactJS + Material UI - TrungQuanDev.mp4', 1020, 1, 1, 'APPROVED'),
(@secId3, N'7. Material UI, TailwindCSS, Bootstrap - Chọn cái nào- - ReactJS + Material UI - TrungQuanDev', N'7. Material UI, TailwindCSS, Bootstrap - Chọn cái nào- - ReactJS + Material UI - TrungQuanDev.mp4', 1080, 2, 1, 'APPROVED'),
(@secId3, N'9. Tổng kết chương và xây dựng giao diện thực chiến - ReactJS + Material UI - TrungQuanDev', N'9. Tổng kết chương và xây dựng giao diện thực chiến - ReactJS + Material UI - TrungQuanDev.mp4', 1200, 3, 1, 'APPROVED');


-- Chèn lesson tùy chỉnh cho course id = 3, chia đều vào 3 section
DECLARE @course3SecId1 INT = (SELECT id FROM course_sections WHERE course_id = 3 AND position = 1);
DECLARE @course3SecId2 INT = (SELECT id FROM course_sections WHERE course_id = 3 AND position = 2);
DECLARE @course3SecId3 INT = (SELECT id FROM course_sections WHERE course_id = 3 AND position = 3);

-- Section 1: CSS Cơ Bản
INSERT INTO lessons (section_id, title, video_url, duration_seconds, position, is_published, moderation_status)
VALUES
(@course3SecId1, N'1. Cài đặt môi trường cho dự án (NVM, Node, Git, Yarn...vv) - ReactJS + Material UI - TrungQuanDev (1)', N'1. Cài đặt môi trường cho dự án (NVM, Node, Git, Yarn...vv) - ReactJS + Material UI - TrungQuanDev (1).mp4', 600, 1, 1, 'APPROVED'),
(@course3SecId1, N'3. Tạo Local Development Server đơn giản để Code dễ dàng - HTML CSS Master A-Z - TrungQuanDev', N'3. Tạo Local Development Server đơn giản để Code dễ dàng - HTML CSS Master A-Z - TrungQuanDev.mp4', 720, 2, 1, 'APPROVED'),
(@course3SecId1, N'9. HTML Links - Thẻ liên kết là gì và ứng dụng như thế nào- - HTML CSS Master A-Z - TrungQuanDev', N'9. HTML Links - Thẻ liên kết là gì và ứng dụng như thế nào- - HTML CSS Master A-Z - TrungQuanDev.mp4', 840, 3, 1, 'APPROVED');

-- Section 2: Flexbox và Grid
INSERT INTO lessons (section_id, title, video_url, duration_seconds, position, is_published, moderation_status)
VALUES
(@course3SecId2, N'6. HTML Styles - Làm đẹp giao diện trang web như thế nào- - HTML CSS Master A-Z - TrungQuanDev', N'6. HTML Styles - Làm đẹp giao diện trang web như thế nào- - HTML CSS Master A-Z - TrungQuanDev.mp4', 900, 1, 1, 'APPROVED'),
(@course3SecId2, N'5. HTML Paragraphs - Đoạn văn bản - HTML CSS Master A-Z - TrungQuanDev', N'5. HTML Paragraphs - Đoạn văn bản - HTML CSS Master A-Z - TrungQuanDev.mp4', 800, 2, 1, 'APPROVED'),
(@course3SecId2, N'1. HTML là gì- Cấu trúc cơ bản của một trang Web- - HTML CSS Master A-Z - TrungQuanDev', N'1. HTML là gì- Cấu trúc cơ bản của một trang Web- - HTML CSS Master A-Z - TrungQuanDev.mp4', 960, 3, 1, 'APPROVED');

-- Section 3: Responsive Design
INSERT INTO lessons (section_id, title, video_url, duration_seconds, position, is_published, moderation_status)
VALUES
(@course3SecId3, N'7. Formatting Elements - Những thẻ định dạng văn bản đặc biệt - HTML CSS Master A-Z - TrungQuanDev', N'7. Formatting Elements - Những thẻ định dạng văn bản đặc biệt - HTML CSS Master A-Z - TrungQuanDev.mp4', 1020, 1, 1, 'APPROVED'),
(@course3SecId3, N'4. JSX là gì- Hiểu Tường Tận về JSX cho các bạn - ReactJS + Material UI - TrungQuanDev', N'4. JSX là gì- Hiểu Tường Tận về JSX cho các bạn - ReactJS + Material UI - TrungQuanDev.mp4', 1080, 2, 1, 'APPROVED'),
(@course3SecId3, N'10. Tổng kết chương và thực hành tạo trang Landing Page cơ bản - HTML CSS Master A-Z - TrungQuanDev', N'10. Tổng kết chương và thực hành tạo trang Landing Page cơ bản - HTML CSS Master A-Z - TrungQuanDev.mp4', 1200, 3, 1, 'APPROVED');


-- Chèn lesson tùy chỉnh cho course id = 4, chia đều vào 3 section
DECLARE @course4SecId1 INT = (SELECT id FROM course_sections WHERE course_id = 4 AND position = 1);
DECLARE @course4SecId2 INT = (SELECT id FROM course_sections WHERE course_id = 4 AND position = 2);
DECLARE @course4SecId3 INT = (SELECT id FROM course_sections WHERE course_id = 4 AND position = 3);

-- Section 1: Node.js Cơ Bản
INSERT INTO lessons (section_id, title, video_url, duration_seconds, position, is_published, moderation_status)
VALUES
(@course4SecId1, N'Khám phá cách xây dựng REST API cực kỳ hiệu quả sử dụng NodeJS', N'Khám phá cách xây dựng REST API cực kỳ hiệu quả sử dụng NodeJS.mp4', 600, 1, 1, 'APPROVED'),
(@course4SecId1, N'Hướng Dẫn Cấu Hình Visual Studio Code Cho API NodeJS MongoDB TypeScript', N'Hướng Dẫn Cấu Hình Visual Studio Code Cho API NodeJS MongoDB TypeScript.mp4', 720, 2, 1, 'APPROVED'),
(@course4SecId1, N'Giới thiệu về TypeScipt - Xây dựng REST API với NodeJS, TypeScript và MongoDB - Khóa học MERN Stack', N'Giới thiệu về TypeScipt - Xây dựng REST API với NodeJS, TypeScript và MongoDB - Khóa học MERN Stack.mp4', 840, 3, 1, 'APPROVED');

-- Section 2: Express Framework
INSERT INTO lessons (section_id, title, video_url, duration_seconds, position, is_published, moderation_status)
VALUES
(@course4SecId2, N'5 Bước Cài Đặt REST API NodeJS, MongoDB & TypeScript CHO NGƯỜI MỚI', N'5 Bước Cài Đặt REST API NodeJS, MongoDB & TypeScript CHO NGƯỜI MỚI.mp4', 900, 1, 1, 'APPROVED'),
(@course4SecId2, N'Khóa học Xây Dựng REST API NodeJS, TypeScript & MongoDB - Khóa Học MERN Stack TEDU', N'Khóa học Xây Dựng REST API NodeJS, TypeScript & MongoDB - Khóa Học MERN Stack TEDU.mp4', 800, 2, 1, 'APPROVED'),
(@course4SecId2, N'Giới thiệu MongoDB - Học REST API với NodeJS, TypeScript và MongoDB từ A-Z - Khóa học TEDU', N'Giới thiệu MongoDB - Học REST API với NodeJS, TypeScript và MongoDB từ A-Z - Khóa học TEDU.mp4', 960, 3, 1, 'APPROVED');

-- Section 3: REST API Thực Chiến
INSERT INTO lessons (section_id, title, video_url, duration_seconds, position, is_published, moderation_status)
VALUES
(@course4SecId3, N'Giới thiệu về NodeJS - Xây dựng REST API cho Mobile & Web (TEDU)', N'Giới thiệu về NodeJS - Xây dựng REST API cho Mobile & Web (TEDU).mp4', 1020, 1, 1, 'APPROVED'),
(@course4SecId3, N'Khởi tạo REST API với NodeJS, MongoDB & TypeScript - Học MERN Stack TEDU', N'Khởi tạo REST API với NodeJS, MongoDB & TypeScript - Học MERN Stack TEDU.mp4', 1080, 2, 1, 'APPROVED'),
(@course4SecId3, N'Phân Tích ER Theo Tư Duy NoSQL - REST API NodeJS & MongoDB - TEDU', N'Phân Tích ER Theo Tư Duy NoSQL - REST API NodeJS & MongoDB - TEDU.mp4', 1200, 3, 1, 'APPROVED');


-- Chèn lesson tùy chỉnh cho course id = 25, chia đều vào 3 section
DECLARE @course25SecId1 INT = (SELECT id FROM course_sections WHERE course_id = 25 AND position = 1);
DECLARE @course25SecId2 INT = (SELECT id FROM course_sections WHERE course_id = 25 AND position = 2);
DECLARE @course25SecId3 INT = (SELECT id FROM course_sections WHERE course_id = 25 AND position = 3);

-- Section 1: Khởi đầu dự án 25
INSERT INTO lessons (section_id, title, video_url, duration_seconds, position, is_published, moderation_status)
VALUES
(@course25SecId1, N'1. Cài đặt môi trường cho dự án (NVM, Node, Git, Yarn...vv) - ReactJS + Material UI - TrungQuanDev (1)', N'1. Cài đặt môi trường cho dự án (NVM, Node, Git, Yarn...vv) - ReactJS + Material UI - TrungQuanDev (1).mp4', 600, 1, 1, 'APPROVED'),
(@course25SecId1, N'2. Vite, Create React App và NextJS - Lựa chọn cái nào- - ReactJS + Material UI - TrungQuanDev', N'2. Vite, Create React App và NextJS - Lựa chọn cái nào- - ReactJS + Material UI - TrungQuanDev.mp4', 720, 2, 1, 'APPROVED'),
(@course25SecId1, N'3. Vite - Hiểu toàn bộ Code Base ban đầu - Push lên GitHub - ReactJS + Material UI - TrungQuanDev', N'3. Vite - Hiểu toàn bộ Code Base ban đầu - Push lên GitHub - ReactJS + Material UI - TrungQuanDev.mp4', 840, 3, 1, 'APPROVED');

-- Section 2: Thiết kế giao diện & Hooks 25
INSERT INTO lessons (section_id, title, video_url, duration_seconds, position, is_published, moderation_status)
VALUES
(@course25SecId2, N'4. JSX là gì- Hiểu Tường Tận về JSX cho các bạn - ReactJS + Material UI - TrungQuanDev', N'4. JSX là gì- Hiểu Tường Tận về JSX cho các bạn - ReactJS + Material UI - TrungQuanDev.mp4', 900, 1, 1, 'APPROVED'),
(@course25SecId2, N'1.1 Cách đọc đúng của Vite -vit- (video ngắn bổ sung) - ReactJS + Material UI - TrungQuanDev', N'1.1 Cách đọc đúng của Vite -vit- (video ngắn bổ sung) - ReactJS + Material UI - TrungQuanDev.mp4', 300, 2, 1, 'APPROVED'),
(@course25SecId2, N'5. Semantic Versioning là gì- Lưu ý Quan Trọng về bộ Code Base của dự án - ReactJS + Material UI', N'5. Semantic Versioning là gì- Lưu ý Quan Trọng về bộ Code Base của dự án - ReactJS + Material UI.mp4', 960, 3, 1, 'APPROVED');

-- Section 3: Tối ưu hóa ứng dụng 25
INSERT INTO lessons (section_id, title, video_url, duration_seconds, position, is_published, moderation_status)
VALUES
(@course25SecId3, N'8. Cài đặt và sử dụng Material UI (Emotion, Fonts, Icons…vv) - ReactJS + Material UI - TrungQuanDev', N'8. Cài đặt và sử dụng Material UI (Emotion, Fonts, Icons…vv) - ReactJS + Material UI - TrungQuanDev.mp4', 1020, 1, 1, 'APPROVED'),
(@course25SecId3, N'7. Material UI, TailwindCSS, Bootstrap - Chọn cái nào- - ReactJS + Material UI - TrungQuanDev', N'7. Material UI, TailwindCSS, Bootstrap - Chọn cái nào- - ReactJS + Material UI - TrungQuanDev.mp4', 1080, 2, 1, 'APPROVED'),
(@course25SecId3, N'10. Tổng kết chương và thực hành tạo trang Landing Page cơ bản - HTML CSS Master A-Z - TrungQuanDev', N'10. Tổng kết chương và thực hành tạo trang Landing Page cơ bản - HTML CSS Master A-Z - TrungQuanDev.mp4', 1200, 3, 1, 'APPROVED');


-- =========================
-- QUIZZES SAMPLE DATA
-- 1 quiz cho mỗi lesson
-- =========================

DECLARE @maxLessonId INT = (SELECT MAX(id) FROM lessons);
DECLARE @lessonId INT = 1;

WHILE @lessonId <= @maxLessonId
BEGIN

    INSERT INTO quizzes (
        lesson_id,
        title,
        pass_score_percent
    )
    VALUES (
        @lessonId,
        N'Quiz bài học ' + CAST(@lessonId AS NVARCHAR(10)),
        70
    );

    SET @lessonId = @lessonId + 1;
END

-- ==========================================
-- BÀI QUIZ THỨ 2 CHO MỖI LESSON (ĐỂ ĐẠT ÍT NHẤT 2 QUIZ/LESSON)
-- ==========================================
DECLARE @lessonId2 INT = 1;

WHILE @lessonId2 <= @maxLessonId
BEGIN

    INSERT INTO quizzes (
        lesson_id,
        title,
        pass_score_percent
    )
    VALUES (
        @lessonId2,
        N'Quiz nâng cao bài học ' + CAST(@lessonId2 AS NVARCHAR(10)),
        75
    );

    SET @lessonId2 = @lessonId2 + 1;
END

-- =========================
-- QUIZ 1 (Java)
-- =========================

INSERT INTO quiz_questions
(quiz_id, question_text, question_type, points, position, explanation)
VALUES
(1, N'Java là ngôn ngữ lập trình thuộc loại nào?', 'SINGLE', 1, 1, N'Java là ngôn ngữ lập trình hướng đối tượng (OOP) phổ biến, chạy trên môi trường máy ảo JVM.'),
(1, N'Từ khóa nào được sử dụng để kế thừa trong Java?', 'SINGLE', 1, 2, N'Từ khóa extends được sử dụng để một lớp kế thừa các thuộc tính và phương thức từ lớp cha.'),
(1, N'Những kiểu dữ liệu nguyên thủy nào tồn tại trong Java?', 'MULTIPLE', 1, 3, N'Java có 8 kiểu dữ liệu nguyên thủy bao gồm byte, short, int, long, float, double, boolean, char. String là kiểu đối tượng.');

-- =========================
-- QUIZ 2 (Java)
-- =========================

INSERT INTO quiz_questions
(quiz_id, question_text, question_type, points, position, explanation)
VALUES
(2, N'OOP là viết tắt của cụm từ nào?', 'SINGLE', 1, 1, N'OOP là viết tắt của Object-Oriented Programming (Lập trình hướng đối tượng).'),
(2, N'Đặc tính nào KHÔNG thuộc OOP?', 'SINGLE', 1, 2, N'Biên dịch động không phải là tính chất cốt lõi của OOP. 4 tính chất cơ bản là Đóng gói, Kế thừa, Đa hình và Trừu tượng.'),
(2, N'Những tính chất nào thuộc OOP?', 'MULTIPLE', 1, 3, N'4 tính chất cốt lõi của lập trình hướng đối tượng gồm Đóng gói, Kế thừa, Đa hình và Trừu tượng.');

-- =========================
-- QUIZ 3 (React)
-- =========================

INSERT INTO quiz_questions
(quiz_id, question_text, question_type, points, position, explanation)
VALUES
(3, N'React được phát triển bởi công ty nào?', 'SINGLE', 1, 1, N'React là thư viện JavaScript mã nguồn mở được phát triển và duy trì bởi Meta (Facebook).'),
(3, N'Hook nào dùng để quản lý state?', 'SINGLE', 1, 2, N'useState là Hook cơ bản trong React dùng để khởi tạo và cập nhật state của Functional Component.'),
(3, N'Những Hook nào là Hook có sẵn của React?', 'MULTIPLE', 1, 3, N'useState, useEffect và useMemo là các React Hook chuẩn tích hợp sẵn.');

-- =========================
-- QUIZ 4 (React)
-- =========================

INSERT INTO quiz_questions
(quiz_id, question_text, question_type, points, position, explanation)
VALUES
(4, N'JSX là gì?', 'SINGLE', 1, 1, N'JSX (JavaScript XML) cho phép người dùng viết cú pháp HTML trực tiếp bên trong file JavaScript.'),
(4, N'Virtual DOM dùng để làm gì?', 'SINGLE', 1, 2, N'Virtual DOM giúp React so sánh sự thay đổi và chỉ cập nhật những node cần thiết lên Real DOM để tối ưu hiệu năng.'),
(4, N'Các lợi ích của React là gì?', 'MULTIPLE', 1, 3, N'React cho phép tái sử dụng Component, tối ưu hiệu năng nhờ Virtual DOM và có hệ sinh thái rộng lớn.');

-- =========================
-- QUIZ 5 (NodeJS)
-- =========================

INSERT INTO quiz_questions
(quiz_id, question_text, question_type, points, position, explanation)
VALUES
(5, N'Node.js chạy trên engine nào?', 'SINGLE', 1, 1, N'Node.js thực thi mã JavaScript bằng V8 Engine - bộ công cụ thực thi mã nhanh của Google Chrome.'),
(5, N'Framework phổ biến nhất của Node.js là gì?', 'SINGLE', 1, 2, N'Express.js là web framework tối giản, linh hoạt và được dùng nhiều nhất cho Node.js.'),
(5, N'Node.js thường được sử dụng để làm gì?', 'MULTIPLE', 1, 3, N'Node.js tối ưu cho việc xây dựng REST API, ứng dụng thời gian thực (Realtime) và Backend services.');

-- =========================
-- QUIZ 6 (Python)
-- =========================

INSERT INTO quiz_questions
(quiz_id, question_text, question_type, points, position, explanation)
VALUES
(6, N'Python là ngôn ngữ thông dịch hay biên dịch?', 'SINGLE', 1, 1, N'Python là ngôn ngữ thông dịch (Interpreted), code được thực thi từng dòng thông qua trình thông dịch Python.'),
(6, N'Hàm nào dùng để xuất dữ liệu ra màn hình?', 'SINGLE', 1, 2, N'Hàm print() được dùng mặc định trong Python để xuất thông tin ra màn hình.'),
(6, N'Những kiểu dữ liệu nào tồn tại trong Python?', 'MULTIPLE', 1, 3, N'Python cung cấp sẵn các kiểu dữ liệu phong phú như int, str, list, dict, tuple, set, bool.');

-- =========================
-- QUIZ 7 (Swift)
-- =========================

INSERT INTO quiz_questions
(quiz_id, question_text, question_type, points, position, explanation)
VALUES
(7, N'Ngôn ngữ Swift được phát triển bởi công ty nào?', 'SINGLE', 1, 1, N'Swift do Apple nghiên cứu và phát triển nhằm thay thế cho Objective-C.'),
(7, N'Swift chủ yếu dùng để phát triển nền tảng nào?', 'SINGLE', 1, 2, N'Swift được sử dụng làm ngôn ngữ chính để lập trình ứng dụng trên hệ sinh thái Apple (iOS, macOS, watchOS, tvOS).'),
(7, N'Những framework nào thuộc hệ sinh thái Apple?', 'MULTIPLE', 1, 3, N'SwiftUI, UIKit và Combine là các framework chuẩn do Apple cung cấp cho lập trình viên.');

-- =========================
-- QUIZ 8 (Spring Boot)
-- =========================

INSERT INTO quiz_questions
(quiz_id, question_text, question_type, points, position, explanation)
VALUES
(8, N'Spring Boot thuộc hệ sinh thái nào?', 'SINGLE', 1, 1, N'Spring Boot là framework thuộc hệ sinh thái Java Spring Framework rộng lớn.'),
(8, N'Annotation nào dùng để đánh dấu Controller REST?', 'SINGLE', 1, 2, N'@RestController là sự kết hợp giữa @Controller và @ResponseBody, dùng tạo RESTful Web Services trong Spring.'),
(8, N'Những module nào thuộc Spring Framework?', 'MULTIPLE', 1, 3, N'Spring MVC, Spring Security và Spring Data JPA là các thành phần phổ biến hàng đầu của Spring Framework.');

-- =========================
-- QUESTION 1
-- Java là ngôn ngữ lập trình thuộc loại nào?
-- =========================
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(1, N'Ngôn ngữ hướng đối tượng', 1),
(1, N'Ngôn ngữ đánh dấu', 0),
(1, N'Hệ quản trị cơ sở dữ liệu', 0),
(1, N'Hệ điều hành', 0);

-- QUESTION 2
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(2, N'extends', 1),
(2, N'implements', 0),
(2, N'inherit', 0),
(2, N'super', 0);

-- QUESTION 3
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(3, N'int', 1),
(3, N'long', 1),
(3, N'double', 1),
(3, N'String', 0);

-- QUESTION 4
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(4, N'Object Oriented Programming', 1),
(4, N'Open Object Programming', 0),
(4, N'Object Operating Process', 0),
(4, N'Online Oriented Program', 0);

-- QUESTION 5
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(5, N'Kế thừa', 0),
(5, N'Đóng gói', 0),
(5, N'Đa hình', 0),
(5, N'Biên dịch động', 1);

-- QUESTION 6
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(6, N'Đóng gói', 1),
(6, N'Kế thừa', 1),
(6, N'Đa hình', 1),
(6, N'Trừu tượng', 1);

-- QUESTION 7
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(7, N'Google', 0),
(7, N'Microsoft', 0),
(7, N'Meta', 1),
(7, N'Apple', 0);

-- QUESTION 8
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(8, N'useState', 1),
(8, N'useClass', 0),
(8, N'useComponent', 0),
(8, N'useVariable', 0);

-- QUESTION 9
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(9, N'useState', 1),
(9, N'useEffect', 1),
(9, N'useMemo', 1),
(9, N'useController', 0);

-- QUESTION 10
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(10, N'Mở rộng HTML trong JavaScript', 1),
(10, N'Một database', 0),
(10, N'Một CSS Framework', 0),
(10, N'Một API', 0);

-- QUESTION 11
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(11, N'Tăng hiệu năng render', 1),
(11, N'Tạo database', 0),
(11, N'Quản lý server', 0),
(11, N'Xử lý CSS', 0);

-- QUESTION 12
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(12, N'Tái sử dụng component', 1),
(12, N'Virtual DOM', 1),
(12, N'Hiệu năng tốt', 1),
(12, N'Không cần JavaScript', 0);

-- QUESTION 13
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(13, N'V8 Engine', 1),
(13, N'JVM', 0),
(13, N'.NET CLR', 0),
(13, N'Python VM', 0);

-- QUESTION 14
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(14, N'Express', 1),
(14, N'Spring Boot', 0),
(14, N'Django', 0),
(14, N'Laravel', 0);

-- QUESTION 15
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(15, N'Xây dựng API', 1),
(15, N'Realtime Application', 1),
(15, N'Backend Service', 1),
(15, N'Thiết kế Photoshop', 0);

-- QUESTION 16
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(16, N'Thông dịch', 1),
(16, N'Biên dịch hoàn toàn', 0),
(16, N'Hợp ngữ', 0),
(16, N'Đánh dấu', 0);

-- QUESTION 17
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(17, N'print()', 1),
(17, N'println()', 0),
(17, N'echo()', 0),
(17, N'console.log()', 0);

-- QUESTION 18
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(18, N'int', 1),
(18, N'str', 1),
(18, N'list', 1),
(18, N'boolean', 0);

-- QUESTION 19
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(19, N'Apple', 1),
(19, N'Google', 0),
(19, N'Microsoft', 0),
(19, N'Meta', 0);

-- QUESTION 20
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(20, N'iOS', 1),
(20, N'Android', 0),
(20, N'Windows', 0),
(20, N'Linux', 0);

-- QUESTION 21
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(21, N'SwiftUI', 1),
(21, N'UIKit', 1),
(21, N'Combine', 1),
(21, N'Spring MVC', 0);

-- QUESTION 22
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(22, N'Java', 1),
(22, N'Python', 0),
(22, N'PHP', 0),
(22, N'JavaScript', 0);

-- QUESTION 23
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(23, N'@RestController', 1),
(23, N'@ControllerAdvice', 0),
(23, N'@Repository', 0),
(23, N'@Service', 0);

-- QUESTION 24
INSERT INTO quiz_answers (question_id, answer_text, is_correct)
VALUES
(24, N'Spring MVC', 1),
(24, N'Spring Security', 1),
(24, N'Spring Data JPA', 1),
(24, N'Laravel', 0);


-- =========================================================================
-- TỰ ĐỘNG TẠO CÂU HỎI VÀ ĐÁP ÁN MẪU PHÙ HỢP VỚI TIÊU ĐỀ KHÓA HỌC VÀ BÀI HỌC
-- =========================================================================
DECLARE @currentQuizId INT;
DECLARE @insertedQuestionId INT;

DECLARE @courseTitle NVARCHAR(255);
DECLARE @sectionTitle NVARCHAR(255);
DECLARE @lessonTitle NVARCHAR(255);

-- Sử dụng Cursor để duyệt qua tất cả các bài Quiz thực tế có trong Database
DECLARE quiz_cursor CURSOR FOR 
SELECT id FROM quizzes;

OPEN quiz_cursor;
FETCH NEXT FROM quiz_cursor INTO @currentQuizId;

WHILE @@FETCH_STATUS = 0
BEGIN
    -- Chỉ tạo câu hỏi mẫu nếu bài Quiz đó chưa có câu hỏi nào
    IF NOT EXISTS (SELECT 1 FROM quiz_questions WHERE quiz_id = @currentQuizId)
    BEGIN
        -- Lấy thông tin khóa học, chương học và bài học tương ứng với Quiz ID
        SELECT 
            @courseTitle = c.title,
            @sectionTitle = s.title,
            @lessonTitle = l.title
        FROM quizzes q
        JOIN lessons l ON q.lesson_id = l.id
        JOIN course_sections s ON l.section_id = s.id
        JOIN courses c ON s.course_id = c.id
        WHERE q.id = @currentQuizId;

        -- Câu hỏi 1 (SINGLE choice)
        INSERT INTO quiz_questions (quiz_id, question_text, question_type, points, position, explanation)
        VALUES (
            @currentQuizId, 
            N'Câu hỏi trắc nghiệm số 1 của bài học "' + @lessonTitle + N'" thuộc "' + @sectionTitle + N'" của khóa học "' + @courseTitle + N'". Đâu là khẳng định đúng?', 
            'SINGLE', 
            1, 
            1,
            N'Giải thích chi tiết cho câu hỏi 1 của bài học "' + @lessonTitle + N'": Đáp án đúng phản ánh chính xác nội dung cốt lõi và lý thuyết được trình bày trong bài học.'
        );
        SET @insertedQuestionId = (SELECT MAX(id) FROM quiz_questions);

        INSERT INTO quiz_answers (question_id, answer_text, is_correct)
        VALUES 
        (@insertedQuestionId, N'Khái niệm chính xác về ' + @courseTitle + N' (Đáp án đúng)', 1),
        (@insertedQuestionId, N'Định nghĩa sai lệch liên quan đến ' + @courseTitle, 0),
        (@insertedQuestionId, N'Nội dung không thuộc phạm vi của bài học ' + @lessonTitle, 0),
        (@insertedQuestionId, N'Tất cả các phương án trên đều sai', 0);

        -- Câu hỏi 2 (MULTIPLE choice)
        INSERT INTO quiz_questions (quiz_id, question_text, question_type, points, position, explanation)
        VALUES (
            @currentQuizId, 
            N'Câu hỏi trắc nghiệm số 2 của bài học "' + @lessonTitle + N'" thuộc "' + @sectionTitle + N'" của khóa học "' + @courseTitle + N'". Chọn các đáp án đúng:', 
            'MULTIPLE', 
            1, 
            2,
            N'Giải thích chi tiết cho câu hỏi 2 của bài học "' + @lessonTitle + N'": Các đáp án đúng mô tả chính xác các đặc tính cơ bản và phương pháp thực hành được khuyến nghị.'
        );
        SET @insertedQuestionId = (SELECT MAX(id) FROM quiz_questions);

        INSERT INTO quiz_answers (question_id, answer_text, is_correct)
        VALUES 
        (@insertedQuestionId, N'Đặc tính cơ bản của ' + @courseTitle + N' trong thực tế', 1),
        (@insertedQuestionId, N'Phương pháp áp dụng tốt nhất cho bài học ' + @lessonTitle, 1),
        (@insertedQuestionId, N'Lý thuyết lỗi thời không nên dùng', 0),
        (@insertedQuestionId, N'Lỗi cú pháp thường gặp khi thực hành', 0);
    END

    FETCH NEXT FROM quiz_cursor INTO @currentQuizId;
END

CLOSE quiz_cursor;
DEALLOCATE quiz_cursor;


-- =========================
-- FEEDBACKS SAMPLE DATA
-- =========================
INSERT INTO feedbacks (user_id, course_id, rating, comment, status, created_at)
VALUES
-- Course 1 (Java Core): 5, 4, 3, 5, 4
(8, 1, 4, N'Nội dung rất ổn, tuy nhiên một số bài tập cuối chương hơi khó.', 'VISIBLE', GETDATE()),
(9, 1, 3, N'Khóa học bình thường, phần giải thích OOP hơi nhanh quá.', 'VISIBLE', GETDATE()),
(10, 1, 5, N'Giảng viên nhiệt tình, hỗ trợ Q&A rất nhanh.', 'VISIBLE', GETDATE()),
(11, 1, 4, N'Tài liệu đầy đủ, video chất lượng cao.', 'VISIBLE', GETDATE()),

-- Course 2 (ReactJS): 5, 5, 4, 4, 5
(7, 2, 5, N'ReactJS thực chiến đỉnh cao, học xong làm được dự án ngay.', 'VISIBLE', GETDATE()),
(9, 2, 4, N'Nội dung cập nhật mới, thực hành nhiều.', 'VISIBLE', GETDATE()),
(10, 2, 4, N'Tốt, nhưng nên nói sâu hơn về Performance Optimization.', 'VISIBLE', GETDATE()),
(11, 2, 5, N'Giảng viên có kiến thức thực tế sâu rộng, giải thích rất dễ thấm.', 'VISIBLE', GETDATE()),

-- Course 3 (CSS): 3, 2, 4, 3, 2
(8, 3, 2, N'Khóa học hơi ngắn, thiếu bài tập thực tế về Grid Layout.', 'VISIBLE', GETDATE()),
(9, 3, 4, N'Responsive Design được giải thích khá ổn.', 'VISIBLE', GETDATE()),
(10, 3, 3, N'Tạm được, chưa xứng đáng lắm với giá tiền.', 'VISIBLE', GETDATE()),
(11, 3, 2, N'Nói hơi lan man và slide chuẩn bị hơi sơ sài.', 'VISIBLE', GETDATE()),

-- Course 4 (Node.js): 4, 4, 3, 4, 5
(7, 4, 4, N'Học làm API với Express rất thực chiến, cấu trúc thư mục chuẩn.', 'VISIBLE', GETDATE()),
(8, 4, 4, N'Rất hay, code chạy mượt mà không lỗi.', 'VISIBLE', GETDATE()),
(9, 4, 3, N'Nội dung cơ bản tốt nhưng phần Middleware giảng hơi khó hiểu.', 'VISIBLE', GETDATE()),
(10, 4, 4, N'Tài liệu tham khảo đầy đủ, có github repo kèm theo.', 'VISIBLE', GETDATE()),
(11, 4, 5, N'Khóa học xuất sắc, giảng viên có giọng nói rất cuốn hút.', 'VISIBLE', GETDATE()),

-- Course 5 (Python): 5, 4, 5, 4, 3
(7, 5, 5, N'Python cơ bản rất dễ học qua khóa này, ví dụ thực tế phong phú.', 'VISIBLE', GETDATE()),
(8, 5, 4, N'Thích hợp cho người bắt đầu từ con số 0.', 'VISIBLE', GETDATE()),
(9, 5, 5, N'Rất thích phần giải thích về OOP trong Python.', 'VISIBLE', GETDATE()),
(10, 5, 4, N'Bài tập thực hành phong phú giúp củng cố kiến thức.', 'VISIBLE', GETDATE()),
(11, 5, 3, N'Video hơi dài và tốc độ giảng hơi chậm.', 'VISIBLE', GETDATE()),

-- Course 6 (Swift): 2, 1, 3, 2, 1
(7, 6, 2, N'Nội dung Swift này cũ quá rồi, nhiều cú pháp không còn đúng ở Xcode mới.', 'VISIBLE', GETDATE()),
(8, 6, 1, N'Không nên mua, giảng viên không cập nhật nội dung mới.', 'VISIBLE', GETDATE()),
(9, 6, 3, N'Tạm được nếu tự tìm tòi sửa lỗi cú pháp thêm.', 'VISIBLE', GETDATE()),
(10, 6, 2, N'Giọng giảng viên đều đều nghe buồn ngủ quá.', 'VISIBLE', GETDATE()),
(11, 6, 1, N'Quá tệ, support cực kỳ chậm hoặc không thèm trả lời.', 'VISIBLE', GETDATE()),

-- Course 7 (SwiftUI): 5, 5, 5, 4, 5
(7, 7, 5, N'SwiftUI quá đẹp, giảng viên thiết kế giao diện đỉnh cao.', 'VISIBLE', GETDATE()),
(8, 7, 5, N'Best course về SwiftUI trên nền tảng này!', 'VISIBLE', GETDATE()),
(9, 7, 5, N'Học xong tự tin code UI cho iOS luôn.', 'VISIBLE', GETDATE()),
(10, 7, 4, N'Cần bổ dung thêm phần kết nối API.', 'VISIBLE', GETDATE()),
(11, 7, 5, N'Tài nguyên hình ảnh và source code kèm theo rất xịn.', 'VISIBLE', GETDATE()),

-- Course 8 (Spring Boot REST API): 4, 3, 4, 5, 4
(7, 8, 4, N'Kiến trúc Spring Boot chuẩn chỉnh, học được nhiều best practice.', 'VISIBLE', GETDATE()),
(8, 8, 3, N'Phần Spring Security cấu hình hơi phức tạp và giảng hơi nhanh.', 'VISIBLE', GETDATE()),
(9, 8, 4, N'Học backend Java thì nên học khóa này.', 'VISIBLE', GETDATE()),
(10, 8, 5, N'Tuyệt vời, deploy lên Azure chạy mượt mà.', 'VISIBLE', GETDATE()),
(11, 8, 4, N'Rất đáng tiền, giảng viên giải đáp thắc mắc chi tiết.', 'VISIBLE', GETDATE()),

-- Course 9 (HTML5 & CSS3): 5, 4, 4, 5, 3
(7, 9, 5, N'Nhập môn Web không thể bỏ qua khóa này, rất dễ hiểu.', 'VISIBLE', GETDATE()),
(8, 9, 4, N'Nền tảng vững chắc để học tiếp Front-End.', 'VISIBLE', GETDATE()),
(9, 9, 4, N'Bài tập làm web đơn giản khá thú vị.', 'VISIBLE', GETDATE()),
(10, 9, 5, N'Thích cách giảng viên truyền đạt kiến thức.', 'VISIBLE', GETDATE()),
(11, 9, 3, N'Hơi cơ bản, mong có thêm phần nâng cao.', 'VISIBLE', GETDATE()),

-- Course 10 (Web Landing Page): 3, 2, 3, 4, 2
(7, 10, 3, N'Khóa học trung bình, giao diện mẫu chưa được đẹp lắm.', 'VISIBLE', GETDATE()),
(8, 10, 2, N'Thiếu phần tối ưu SEO cho Landing Page.', 'VISIBLE', GETDATE()),
(9, 10, 3, N'Tạm ổn để làm quen với layout.', 'VISIBLE', GETDATE()),
(10, 10, 4, N'Hướng dẫn từng bước khá dễ đi theo.', 'VISIBLE', GETDATE()),
(11, 10, 2, N'Không học được nhiều kỹ thuật mới, toàn copy code.', 'VISIBLE', GETDATE()),

-- Course 11 (Tailwind CSS): 5, 5, 4, 5, 5
(7, 11, 5, N'Học xong bỏ luôn CSS thuần, viết Tailwind siêu nhanh.', 'VISIBLE', GETDATE()),
(8, 11, 5, N'Cấu trúc và cách sử dụng class rất trực quan.', 'VISIBLE', GETDATE()),
(9, 11, 4, N'Giải thích cặn kẽ cách custom config Tailwind.', 'VISIBLE', GETDATE()),
(10, 11, 5, N'Tuyệt vời, tiết kiệm bao nhiêu thời gian làm UI.', 'VISIBLE', GETDATE()),
(11, 11, 5, N'Giảng viên giải thích rất hay, ví dụ sinh động.', 'VISIBLE', GETDATE()),

-- Course 12 (Responsive Web Design): 4, 3, 5, 4, 4
(7, 12, 4, N'Hiểu sâu sắc về Media Query và cách chia cột.', 'VISIBLE', GETDATE()),
(8, 12, 3, N'Phần Grid hơi phức tạp, cần xem lại nhiều lần.', 'VISIBLE', GETDATE()),
(9, 12, 5, N'Responsive chạy hoàn hảo trên cả mobile và tablet.', 'VISIBLE', GETDATE()),
(10, 12, 4, N'Khóa học bổ ích cho các Web Designer.', 'VISIBLE', GETDATE()),
(11, 12, 4, N'Code mẫu sạch và dễ áp dụng vào dự án thực tế.', 'VISIBLE', GETDATE()),

-- Course 13 (React Native): 2, 2, 3, 1, 2
(7, 13, 2, N'Config môi trường Android/iOS quá phức tạp và nhiều lỗi.', 'VISIBLE', GETDATE()),
(8, 13, 2, N'Giảng viên code lỗi liên tục trong video làm mất thời gian.', 'VISIBLE', GETDATE()),
(9, 13, 3, N'Nội dung tạm chấp nhận được nếu chịu khó search StackOverflow.', 'VISIBLE', GETDATE()),
(10, 13, 1, N'Không chạy được code mẫu trên phiên bản React Native mới.', 'VISIBLE', GETDATE()),
(11, 13, 2, N'Nói lắp bắp và giải thích kiến thức không rõ ràng.', 'VISIBLE', GETDATE()),

-- Course 14 (Next.js 14): 5, 4, 5, 5, 4
(7, 14, 5, N'NextJS 14 App Router giảng cực kỳ dễ hiểu, quá hay.', 'VISIBLE', GETDATE()),
(8, 14, 4, N'Server Component và Client Component phân biệt rất rõ ràng.', 'VISIBLE', GETDATE()),
(9, 14, 5, N'SEO và Performance cải thiện rõ rệt sau khi áp dụng.', 'VISIBLE', GETDATE()),
(10, 14, 5, N'Khóa học chất lượng cao nhất về Next.js hiện tại.', 'VISIBLE', GETDATE()),
(11, 14, 4, N'Tài liệu hướng dẫn siêu chi tiết, code mẫu chuẩn.', 'VISIBLE', GETDATE()),

-- Course 15 (Node.js RESTful & NestJS): 4, 5, 4, 3, 5
(7, 15, 4, N'Kiến trúc NestJS sạch sẽ, chuẩn doanh nghiệp.', 'VISIBLE', GETDATE()),
(8, 15, 5, N'Khóa học nâng cao chất lượng, áp dụng được ngay vào công việc.', 'VISIBLE', GETDATE()),
(9, 15, 4, N'Giải thích rõ về Dependency Injection.', 'VISIBLE', GETDATE()),
(10, 15, 3, N'Phần microservices giảng hơi sơ sài.', 'VISIBLE', GETDATE()),
(11, 15, 5, N'Thích cách giảng viên thiết kế DB và bảo mật JWT.', 'VISIBLE', GETDATE()),

-- Course 16 (Node.js & MongoDB): 3, 3, 4, 3, 2
(7, 16, 3, N'Học MongoDB cơ bản ổn, nhưng phần Aggregation giảng chưa sâu.', 'VISIBLE', GETDATE()),
(8, 16, 3, N'Slide bài giảng hơi sơ sài, chủ yếu là live code.', 'VISIBLE', GETDATE()),
(9, 16, 4, N'Kết nối Express với MongoDB dễ dàng theo hướng dẫn.', 'VISIBLE', GETDATE()),
(10, 16, 3, N'Nội dung ở mức trung bình, có thể tìm thấy free trên mạng.', 'VISIBLE', GETDATE()),
(11, 16, 2, N'Nhiều đoạn code bị lỗi thời do thư viện cập nhật.', 'VISIBLE', GETDATE()),

-- Course 17 (Pandas & NumPy): 5, 5, 4, 5, 4
(7, 17, 5, N'Tuyệt vời cho AI và Data Science path, NumPy/Pandas rất trực quan.', 'VISIBLE', GETDATE()),
(8, 17, 5, N'Thao tác dữ liệu cực kỳ nhanh gọn sau khóa này.', 'VISIBLE', GETDATE()),
(9, 17, 4, N'Nhiều bài tập thực hành phân tích file CSV thực tế.', 'VISIBLE', GETDATE()),
(10, 17, 5, N'Giảng viên giải thích toán học đằng sau rất dễ hiểu.', 'VISIBLE', GETDATE()),
(11, 17, 4, N'Tài liệu Jupyter Notebook đi kèm rất tiện tra cứu.', 'VISIBLE', GETDATE()),

-- Course 18 (Django & Python): 4, 4, 3, 4, 3
(7, 18, 4, N'Xây dựng web với Django nhanh chóng, admin panel quá xịn.', 'VISIBLE', GETDATE()),
(8, 18, 4, N'Học được cách làm hệ thống phân quyền và CMT.', 'VISIBLE', GETDATE()),
(9, 18, 3, N'Tốc độ nói của giảng viên hơi nhanh ở phần Template.', 'VISIBLE', GETDATE()),
(10, 18, 4, N'Hướng dẫn Deploy lên Heroku rất chi tiết.', 'VISIBLE', GETDATE()),
(11, 18, 3, N'Nội dung tạm ổn, có thể bổ sung thêm REST Framework.', 'VISIBLE', GETDATE()),

-- Course 19 (OOP Java Core): 1, 2, 1, 3, 2
(7, 19, 1, N'Khóa học chán quá, giảng viên chỉ đọc slide.', 'VISIBLE', GETDATE()),
(8, 19, 2, N'Bài tập OOP quá đơn điệu, không thực tế.', 'VISIBLE', GETDATE()),
(9, 19, 1, N'Microphone của giảng viên bị rè, nghe rất khó chịu.', 'VISIBLE', GETDATE()),
(10, 19, 3, N'Nội dung cơ bản ở mức tạm chấp nhận được.', 'VISIBLE', GETDATE()),
(11, 19, 2, N'Giải thích phần Đa hình rất mơ hồ và khó hiểu.', 'VISIBLE', GETDATE()),

-- Course 20 (Microservices): 5, 5, 5, 4, 5
(7, 20, 5, N'Kiến trúc đỉnh cao, Eureka, Gateway, Config Server đầy đủ.', 'VISIBLE', GETDATE()),
(8, 20, 5, N'Khóa học chất lượng nhất về Microservices bằng Java.', 'VISIBLE', GETDATE()),
(9, 20, 5, N'Rất thực chiến, có phần Docker hóa các service.', 'VISIBLE', GETDATE()),
(10, 20, 4, N'Cần thêm phần Kubernetes nữa thì hoàn hảo.', 'VISIBLE', GETDATE()),
(11, 20, 5, N'Thầy dạy cực kỳ tâm huyết, hỗ trợ group chat nhiệt tình.', 'VISIBLE', GETDATE()),

-- Course 21 (iOS Swift & Xcode): 4, 4, 5, 4, 3
(7, 21, 4, N'Làm quen Xcode nhanh chóng, tạo được app đầu tiên sau vài giờ.', 'VISIBLE', GETDATE()),
(8, 21, 4, N'Rất thích hợp cho người mới chuyển sang lập trình iOS.', 'VISIBLE', GETDATE()),
(9, 21, 5, N'Tư duy chia Layout bằng AutoLayout rất dễ thấm.', 'VISIBLE', GETDATE()),
(10, 21, 4, N'Bài giảng trực quan, hình ảnh đẹp.', 'VISIBLE', GETDATE()),
(11, 21, 3, N'Nên bổ sung thêm phần UIKit nâng cao.', 'VISIBLE', GETDATE()),

-- Course 22 (Data Structures Swift): 3, 3, 4, 3, 4
(7, 22, 3, N'Cấu trúc dữ liệu bằng Swift giảng ở mức vừa phải.', 'VISIBLE', GETDATE()),
(8, 22, 3, N'Nhiều phần giải thuật viết code hơi phức tạp hóa vấn đề.', 'VISIBLE', GETDATE()),
(9, 22, 4, N'Giúp cải thiện tư duy thuật toán khi làm iOS.', 'VISIBLE', GETDATE()),
(10, 22, 3, N'Tạm được, chưa có nhiều ví dụ nâng cao.', 'VISIBLE', GETDATE()),
(11, 22, 4, N'Slide đẹp, hình ảnh minh họa thuật toán dễ hiểu.', 'VISIBLE', GETDATE()),

-- Course 23 (SwiftUI Animation): 5, 5, 4, 4, 5
(7, 23, 5, N'Animation trong SwiftUI quá ảo diệu, dạy rất chi tiết.', 'VISIBLE', GETDATE()),
(8, 23, 5, N'Học xong làm được hiệu ứng chuyển động mượt mà.', 'VISIBLE', GETDATE()),
(9, 23, 4, N'Cung cấp nhiều ý tưởng thiết kế UI độc đáo.', 'VISIBLE', GETDATE()),
(10, 23, 4, N'Rất hay, mong giảng viên làm thêm phần Transition nâng cao.', 'VISIBLE', GETDATE()),
(11, 23, 5, N'Không thể chê được điểm nào, video HD siêu nét.', 'VISIBLE', GETDATE()),

-- Course 24 (Clone App & Firebase): 2, 3, 2, 3, 1
(7, 24, 2, N'Cấu hình Firebase iOS quá khó khăn và lỗi liên tục.', 'VISIBLE', GETDATE()),
(8, 24, 3, N'Khóa học hơi vội vàng ở phần xử lý Realtime Database.', 'VISIBLE', GETDATE()),
(9, 24, 2, N'Nhiều đoạn code bị lỗi do Firebase update SDK mới.', 'VISIBLE', GETDATE()),
(10, 24, 3, N'Ở mức trung bình, xem để biết luồng đăng nhập.', 'VISIBLE', GETDATE()),
(11, 24, 1, N'Không được hỗ trợ khi hỏi bài, code lỗi không chạy được.', 'VISIBLE', GETDATE());


-- =========================
-- ENROLLMENTS SAMPLE DATA
-- =========================
INSERT INTO enrollments (user_id, course_id, progress_percent, created_at)
VALUES
-- Course 1
(7, 1, 50.00, GETDATE()),
(8, 1, 40.00, GETDATE()),
(9, 1, 35.00, GETDATE()),
(10, 1, 80.00, GETDATE()),
(11, 1, 100.00, GETDATE()),

-- Course 2
(7, 2, 55.00, GETDATE()),
(8, 2, 60.00, GETDATE()),
(9, 2, 30.00, GETDATE()),
(10, 2, 75.00, GETDATE()),
(11, 2, 100.00, GETDATE()),

-- Course 3
(7, 3, 30.00, GETDATE()),
(8, 3, 35.00, GETDATE()),
(9, 3, 45.00, GETDATE()),
(10, 3, 40.00, GETDATE()),
(11, 3, 30.00, GETDATE()),

-- Course 4
(7, 4, 90.00, GETDATE()),
(8, 4, 85.00, GETDATE()),
(9, 4, 70.00, GETDATE()),
(10, 4, 65.00, GETDATE()),
(11, 4, 100.00, GETDATE()),

-- Course 5
(7, 5, 45.00, GETDATE()),
(8, 5, 50.00, GETDATE()),
(9, 5, 60.00, GETDATE()),
(10, 5, 55.00, GETDATE()),
(11, 5, 30.00, GETDATE()),

-- Course 6
(7, 6, 32.00, GETDATE()),
(8, 6, 30.00, GETDATE()),
(9, 6, 35.00, GETDATE()),
(10, 6, 40.00, GETDATE()),
(11, 6, 30.00, GETDATE()),

-- Course 7
(7, 7, 95.00, GETDATE()),
(8, 7, 100.00, GETDATE()),
(9, 7, 88.00, GETDATE()),
(10, 7, 70.00, GETDATE()),
(11, 7, 100.00, GETDATE()),

-- Course 8
(7, 8, 40.00, GETDATE()),
(8, 8, 30.00, GETDATE()),
(9, 8, 45.00, GETDATE()),
(10, 8, 50.00, GETDATE()),
(11, 8, 60.00, GETDATE()),

-- Course 9
(7, 9, 80.00, GETDATE()),
(8, 9, 70.00, GETDATE()),
(9, 9, 60.00, GETDATE()),
(10, 9, 90.00, GETDATE()),
(11, 9, 50.00, GETDATE()),

-- Course 10
(7, 10, 35.00, GETDATE()),
(8, 10, 30.00, GETDATE()),
(9, 10, 30.00, GETDATE()),
(10, 10, 40.00, GETDATE()),
(11, 10, 30.00, GETDATE()),

-- Course 11
(7, 11, 100.00, GETDATE()),
(8, 11, 95.00, GETDATE()),
(9, 11, 85.00, GETDATE()),
(10, 11, 90.00, GETDATE()),
(11, 11, 100.00, GETDATE()),

-- Course 12
(7, 12, 60.00, GETDATE()),
(8, 12, 50.00, GETDATE()),
(9, 12, 70.00, GETDATE()),
(10, 12, 65.00, GETDATE()),
(11, 12, 80.00, GETDATE()),

-- Course 13
(7, 13, 30.00, GETDATE()),
(8, 13, 35.00, GETDATE()),
(9, 13, 40.00, GETDATE()),
(10, 13, 30.00, GETDATE()),
(11, 13, 32.00, GETDATE()),

-- Course 14
(7, 14, 90.00, GETDATE()),
(8, 14, 85.00, GETDATE()),
(9, 14, 95.00, GETDATE()),
(10, 14, 100.00, GETDATE()),
(11, 14, 80.00, GETDATE()),

-- Course 15
(7, 15, 75.00, GETDATE()),
(8, 15, 80.00, GETDATE()),
(9, 15, 70.00, GETDATE()),
(10, 15, 60.00, GETDATE()),
(11, 15, 90.00, GETDATE()),

-- Course 16
(7, 16, 30.00, GETDATE()),
(8, 16, 35.00, GETDATE()),
(9, 16, 40.00, GETDATE()),
(10, 16, 35.00, GETDATE()),
(11, 16, 30.00, GETDATE()),

-- Course 17
(7, 17, 100.00, GETDATE()),
(8, 17, 90.00, GETDATE()),
(9, 17, 80.00, GETDATE()),
(10, 17, 95.00, GETDATE()),
(11, 17, 85.00, GETDATE()),

-- Course 18
(7, 18, 50.00, GETDATE()),
(8, 18, 60.00, GETDATE()),
(9, 18, 40.00, GETDATE()),
(10, 18, 55.00, GETDATE()),
(11, 18, 45.00, GETDATE()),

-- Course 19
(7, 19, 35.00, GETDATE()),
(8, 19, 30.00, GETDATE()),
(9, 19, 30.00, GETDATE()),
(10, 19, 35.00, GETDATE()),
(11, 19, 30.00, GETDATE()),

-- Course 20
(7, 20, 100.00, GETDATE()),
(8, 20, 95.00, GETDATE()),
(9, 20, 100.00, GETDATE()),
(10, 20, 85.00, GETDATE()),
(11, 20, 90.00, GETDATE()),

-- Course 21
(7, 21, 70.00, GETDATE()),
(8, 21, 65.00, GETDATE()),
(9, 21, 80.00, GETDATE()),
(10, 21, 75.00, GETDATE()),
(11, 21, 60.00, GETDATE()),

-- Course 22
(7, 22, 40.00, GETDATE()),
(8, 22, 35.00, GETDATE()),
(9, 22, 45.00, GETDATE()),
(10, 22, 30.00, GETDATE()),
(11, 22, 50.00, GETDATE()),

-- Course 23
(7, 23, 90.00, GETDATE()),
(8, 23, 95.00, GETDATE()),
(9, 23, 80.00, GETDATE()),
(10, 23, 85.00, GETDATE()),
(11, 23, 100.00, GETDATE()),

-- Course 24
(7, 24, 30.00, GETDATE()),
(8, 24, 35.00, GETDATE()),
(9, 24, 30.00, GETDATE()),
(10, 24, 30.00, GETDATE()),
(11, 24, 30.00, GETDATE());


-- =========================================================================
-- INSTRUCTOR SAMPLE TEST DATA
-- =========================================================================

-- 1. KHAI BÁO CÁC BIẾN ĐỂ LƯU ID CỦA USER
DECLARE @UserId1 INT, @UserId2 INT, @UserId3 INT, @UserId4 INT, @UserId5 INT, @UserId6 INT, @UserId7 INT, @UserId8 INT, @UserId9 INT, @UserId10 INT;
DECLARE @PasswordHash VARCHAR(255) = '$2a$12$BBHjuDWH7w0RXg9ejOmJ1uds8/7ZLaDM0zpX/9INmkUqawEwaaXUy';

-- USER 1
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor_test1@elearning.com')
BEGIN
    INSERT INTO users (first_name, last_name, email, phone, bio, password_hash, avatar_url, google_id, status, created_at)
    VALUES (N'Nguyễn Văn', N'Tiến', 'instructor_test1@elearning.com', '0905000001', N'Đam mê dạy học lập trình C++.', @PasswordHash, 'https://fptcontainer.blob.core.windows.net/avatars/avatar_test1.jpg', NULL, 'ACTIVE', DATEADD(hour, -12, GETDATE()));
    SET @UserId1 = SCOPE_IDENTITY();
END
ELSE BEGIN SELECT @UserId1 = id FROM users WHERE email = 'instructor_test1@elearning.com'; END

-- USER 2
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor_test2@elearning.com')
BEGIN
    INSERT INTO users (first_name, last_name, email, phone, bio, password_hash, avatar_url, google_id, status, created_at)
    VALUES (N'Trần Thị', N'Quỳnh', 'instructor_test2@elearning.com', '0905000002', N'Giảng viên tiếng Anh có 2 năm kinh nghiệm.', @PasswordHash, 'https://fptcontainer.blob.core.windows.net/avatars/avatar_test2.jpg', NULL, 'ACTIVE', DATEADD(day, -2, GETDATE()));
    SET @UserId2 = SCOPE_IDENTITY();
END
ELSE BEGIN SELECT @UserId2 = id FROM users WHERE email = 'instructor_test2@elearning.com'; END

-- USER 3
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor_test3@elearning.com')
BEGIN
    INSERT INTO users (first_name, last_name, email, phone, bio, password_hash, avatar_url, google_id, status, created_at)
    VALUES (N'Phạm Minh', N'Hoàng', 'instructor_test3@elearning.com', '0905000003', N'Kỹ sư phần mềm mong muốn chia sẻ kiến thức React Native.', @PasswordHash, 'https://fptcontainer.blob.core.windows.net/avatars/avatar_test3.jpg', NULL, 'BANNED', DATEADD(day, -3, GETDATE()));
    SET @UserId3 = SCOPE_IDENTITY();
END
ELSE BEGIN SELECT @UserId3 = id FROM users WHERE email = 'instructor_test3@elearning.com'; END

-- USER 4
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor_test4@elearning.com')
BEGIN
    INSERT INTO users (first_name, last_name, email, phone, bio, password_hash, avatar_url, google_id, status, created_at)
    VALUES (N'Hoàng Gia', N'Bảo', 'instructor_test4@elearning.com', '0905000004', N'Chuyên gia UI/UX Designer.', @PasswordHash, 'https://fptcontainer.blob.core.windows.net/avatars/avatar_test4.jpg', NULL, 'ACTIVE', DATEADD(hour, -2, GETDATE()));
    SET @UserId4 = SCOPE_IDENTITY();
END
ELSE BEGIN SELECT @UserId4 = id FROM users WHERE email = 'instructor_test4@elearning.com'; END

-- USER 5
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor_test5@elearning.com')
BEGIN
    INSERT INTO users (first_name, last_name, email, phone, bio, password_hash, avatar_url, google_id, status, created_at)
    VALUES (N'Lê Minh', N'Khánh', 'instructor_test5@elearning.com', '0905000005', N'Fullstack Developer.', @PasswordHash, 'https://fptcontainer.blob.core.windows.net/avatars/avatar_test1.jpg', NULL, 'ACTIVE', DATEADD(hour, -14, GETDATE()));
    SET @UserId5 = SCOPE_IDENTITY();
END
ELSE BEGIN SELECT @UserId5 = id FROM users WHERE email = 'instructor_test5@elearning.com'; END

-- USER 6
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor_test6@elearning.com')
BEGIN
    INSERT INTO users (first_name, last_name, email, phone, bio, password_hash, avatar_url, google_id, status, created_at)
    VALUES (N'Phan Thanh', N'Hà', 'instructor_test6@elearning.com', '0905000006', N'Data Analyst.', @PasswordHash, 'https://fptcontainer.blob.core.windows.net/avatars/avatar_test2.jpg', NULL, 'BANNED', DATEADD(hour, -16, GETDATE()));
    SET @UserId6 = SCOPE_IDENTITY();
END
ELSE BEGIN SELECT @UserId6 = id FROM users WHERE email = 'instructor_test6@elearning.com'; END

-- USER 7
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor_test7@elearning.com')
BEGIN
    INSERT INTO users (first_name, last_name, email, phone, bio, password_hash, avatar_url, google_id, status, created_at)
    VALUES (N'Đặng Quốc', N'Bảo', 'instructor_test7@elearning.com', '0905000007', N'DevOps Engineer.', @PasswordHash, 'https://fptcontainer.blob.core.windows.net/avatars/avatar_test3.jpg', NULL, 'ACTIVE', DATEADD(hour, -18, GETDATE()));
    SET @UserId7 = SCOPE_IDENTITY();
END
ELSE BEGIN SELECT @UserId7 = id FROM users WHERE email = 'instructor_test7@elearning.com'; END

-- USER 8
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor_test8@elearning.com')
BEGIN
    INSERT INTO users (first_name, last_name, email, phone, bio, password_hash, avatar_url, google_id, status, created_at)
    VALUES (N'Bùi Minh', N'Tuấn', 'instructor_test8@elearning.com', '0905000008', N'Backend developer.', @PasswordHash, 'https://fptcontainer.blob.core.windows.net/avatars/avatar_test4.jpg', NULL, 'ACTIVE', DATEADD(hour, -20, GETDATE()));
    SET @UserId8 = SCOPE_IDENTITY();
END
ELSE BEGIN SELECT @UserId8 = id FROM users WHERE email = 'instructor_test8@elearning.com'; END

-- USER 9
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor_test9@elearning.com')
BEGIN
    INSERT INTO users (first_name, last_name, email, phone, bio, password_hash, avatar_url, google_id, status, created_at)
    VALUES (N'Vũ Thị', N'Lan', 'instructor_test9@elearning.com', '0905000009', N'Tester.', @PasswordHash, 'https://fptcontainer.blob.core.windows.net/avatars/avatar_test1.jpg', NULL, 'BANNED', DATEADD(hour, -22, GETDATE()));
    SET @UserId9 = SCOPE_IDENTITY();
END
ELSE BEGIN SELECT @UserId9 = id FROM users WHERE email = 'instructor_test9@elearning.com'; END

-- USER 10
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'instructor_test10@elearning.com')
BEGIN
    INSERT INTO users (first_name, last_name, email, phone, bio, password_hash, avatar_url, google_id, status, created_at)
    VALUES (N'Đỗ Hoàng', N'Anh', 'instructor_test10@elearning.com', '0905000010', N'AI Engineer.', @PasswordHash, 'https://fptcontainer.blob.core.windows.net/avatars/avatar_test2.jpg', NULL, 'ACTIVE', DATEADD(hour, -24, GETDATE()));
    SET @UserId10 = SCOPE_IDENTITY();
END
ELSE BEGIN SELECT @UserId10 = id FROM users WHERE email = 'instructor_test10@elearning.com'; END


-- -------------------------------------------------------------
-- 2. GÁN VAI TRÒ GIẢNG VIÊN (ROLE_ID = 3) NẾU CHƯA CÓ
-- -------------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = @UserId1 AND role_id = 3) INSERT INTO user_roles (user_id, role_id) VALUES (@UserId1, 3);
IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = @UserId2 AND role_id = 3) INSERT INTO user_roles (user_id, role_id) VALUES (@UserId2, 3);
IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = @UserId3 AND role_id = 3) INSERT INTO user_roles (user_id, role_id) VALUES (@UserId3, 3);
IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = @UserId4 AND role_id = 3) INSERT INTO user_roles (user_id, role_id) VALUES (@UserId4, 3);
IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = @UserId5 AND role_id = 3) INSERT INTO user_roles (user_id, role_id) VALUES (@UserId5, 3);
IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = @UserId6 AND role_id = 3) INSERT INTO user_roles (user_id, role_id) VALUES (@UserId6, 3);
IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = @UserId7 AND role_id = 3) INSERT INTO user_roles (user_id, role_id) VALUES (@UserId7, 3);
IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = @UserId8 AND role_id = 3) INSERT INTO user_roles (user_id, role_id) VALUES (@UserId8, 3);
IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = @UserId9 AND role_id = 3) INSERT INTO user_roles (user_id, role_id) VALUES (@UserId9, 3);
IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = @UserId10 AND role_id = 3) INSERT INTO user_roles (user_id, role_id) VALUES (@UserId10, 3);


-- =========================================================================
-- 5. DỮ LIỆU TEST DASHBOARD MẪU (KHÓA HỌC CHỜ DUYỆT, BÁO CÁO VI PHẠM, BIỂU ĐỒ)
-- =========================================================================

DELETE FROM feedbacks WHERE comment IN (N'Khoá học rất hay, nhưng bài 3 video hơi mờ.', N'Quá tệ, giảng viên nói tục tĩu.', N'Giảng viên lười trả lời câu hỏi, khoá học cũ kỹ.');
DELETE FROM courses WHERE title IN (N'Lập trình Java Web với Spring Boot', N'Thiết kế giao diện nâng cao với Figma');
DELETE FROM payments WHERE gateway = 'TEST_GATEWAY';
DELETE FROM orders WHERE payment_method = 'TEST_METHOD';

-- Tạo các khóa học chờ phê duyệt (PENDING COURSES)
INSERT INTO courses (instructor_id, category_id, title, description, thumbnail_url, price, level, status, approved_by, approved_at, created_at)
VALUES 
(5, 9, N'Lập trình Java Web với Spring Boot', N'Học Spring MVC, JPA, Security và xây dựng Restful API hoàn chỉnh.', N'Lập trình Java Web với Spring Boot.jpg', 1000, 'ADVANCED', 'PENDING', NULL, NULL, DATEADD(hour, -5, GETDATE())),
(6, 5, N'Thiết kế giao diện nâng cao với Figma', N'Làm chủ Figma, AutoLayout, Component, Variable và Design System.', N'Thiết kế giao diện nâng cao với Figma.jpg', 1000, 'INTERMEDIATE', 'PENDING', NULL, NULL, DATEADD(hour, -1, GETDATE()));

-- Tạo feedback
DECLARE @FeedbackId1 INT, @FeedbackId2 INT, @FeedbackId3 INT;

INSERT INTO feedbacks (user_id, course_id, rating, comment, status, created_at)
VALUES (7, 1, 4, N'Khoá học rất hay, nhưng bài 3 video hơi mờ.', 'VISIBLE', GETDATE());
SET @FeedbackId1 = SCOPE_IDENTITY();

INSERT INTO feedbacks (user_id, course_id, rating, comment, status, created_at)
VALUES (8, 2, 1, N'Quá tệ, giảng viên nói tục tĩu.', 'VISIBLE', GETDATE());
SET @FeedbackId2 = SCOPE_IDENTITY();

INSERT INTO feedbacks (user_id, course_id, rating, comment, status, created_at)
VALUES (7, 3, 2, N'Giảng viên lười trả lời câu hỏi, khoá học cũ kỹ.', 'VISIBLE', GETDATE());
SET @FeedbackId3 = SCOPE_IDENTITY();


-- =========================
-- LESSON MATERIALS
-- Lessons 1 -> 9
-- =========================

INSERT INTO lesson_materials
(
    instructor_id,
    lesson_id,
    file_name,
    file_url,
    file_type,
    file_size
)
VALUES
    (5, 1, N'Junit_Jacoco_Assignment_Sample.docx', '45d7c134-5be5-44f2-8009-d84c3fdc383d_Junit_Jacoco_Assignment_Sample.docx', 'docx', 1048576),
    (5, 1, N'Lab4_Requirements.pdf', 'f9846ac0-7788-414e-b102-e85190b373a5_Lab4_Requirements.pdf', 'pdf', 1048576),
    (5, 2, N'Team 5_SE2034_Defect Log FE.xlsx', '45243280-d293-476a-9fda-439b37acab95_Team 5_SE2034_Defect Log FE.xlsx', 'xlsx', 1048576),
    (5, 2, N'blog.haposoft.com-Bản dịch ISTQB.docx', '40a9db01-e535-46fb-8954-ab7694f0e964_blog.haposoft.com-Bản dịch ISTQB.docx', 'docx', 1048576),
    (5, 3, N'Team 5_SE2034_Defect Log FE.xlsx', '36f3ac2b-9e84-4b6c-8bff-52b93cfe2af1_Team 5_SE2034_Defect Log FE.xlsx', 'xlsx', 1048576),
    (5, 3, N'[giao.lang] Thymeleaf in 5 minutes-25.0619.AI generated.docx', 'dd1f1f23-3e2e-415d-9873-81979b464aaf_[giao.lang] Thymeleaf in 5 minutes-25.0619.AI generated.docx', 'docx', 1048576),
    (5, 3, N'Lab4_Requirements.pdf', 'f9846ac0-7788-414e-b102-e85190b373a5_Lab4_Requirements.pdf', 'pdf', 1048576),
    (5, 4, N'Team 5_SE2034_Defect Log FE.xlsx', '36f3ac2b-9e84-4b6c-8bff-52b93cfe2af1_Team 5_SE2034_Defect Log FE.xlsx', 'xlsx', 1048576),
    (5, 5, N'Junit_Jacoco_Assignment_Sample.docx', '4df90b30-ff0f-4d11-b50f-8463cdae22b7_Junit_Jacoco_Assignment_Sample.docx', 'docx', 1048576),
    (5, 6, N'[giao.lang] Thymeleaf in 5 minutes-25.0619.AI generated.docx', 'dd1f1f23-3e2e-415d-9873-81979b464aaf_[giao.lang] Thymeleaf in 5 minutes-25.0619.AI generated.docx', 'docx', 1048576),
    (5, 7, N'Lab4_Requirements.pdf', 'f9846ac0-7788-414e-b102-e85190b373a5_Lab4_Requirements.pdf', 'pdf', 1048576),
    (5, 8, N'Junit_Jacoco_Assignment_Sample.docx', '45d7c134-5be5-44f2-8009-d84c3fdc383d_Junit_Jacoco_Assignment_Sample.docx', 'docx', 1048576),
    (5, 9, N'Team 5_SE2034_Defect Log FE.xlsx', '45243280-d293-476a-9fda-439b37acab95_Team 5_SE2034_Defect Log FE.xlsx', 'xlsx', 1048576);

INSERT INTO orders (
    user_id,
    total_amount,
    status,
    payment_method,
    created_at,
    updated_at
)
VALUES
    -- THÁNG 1 (6 tháng trước)
    (13, 890000, 'PAID', 'PAYOS', DATEADD(MONTH, -6, DATEADD(DAY, 5, GETDATE())), DATEADD(MONTH, -6, DATEADD(DAY, 5, GETDATE()))),
    (14, 450000, 'PAID', 'PAYOS', DATEADD(MONTH, -6, DATEADD(DAY, 15, GETDATE())), DATEADD(MONTH, -6, DATEADD(DAY, 15, GETDATE()))),
    (15, 350000, 'PAID', 'PAYOS', DATEADD(MONTH, -6, DATEADD(DAY, 22, GETDATE())), DATEADD(MONTH, -6, DATEADD(DAY, 22, GETDATE()))),

    -- THÁNG 2 (5 tháng trước)
    (16, 1900000, 'PAID', 'PAYOS', DATEADD(MONTH, -5, DATEADD(DAY, 3, GETDATE())), DATEADD(MONTH, -5, DATEADD(DAY, 3, GETDATE()))),
    (17, 450000, 'PAID', 'PAYOS', DATEADD(MONTH, -5, DATEADD(DAY, 12, GETDATE())), DATEADD(MONTH, -5, DATEADD(DAY, 12, GETDATE()))),
    (18, 840000, 'PAID', 'PAYOS', DATEADD(MONTH, -5, DATEADD(DAY, 20, GETDATE())), DATEADD(MONTH, -5, DATEADD(DAY, 20, GETDATE()))),

    -- THÁNG 3 (4 tháng trước)
    (19, 2500000, 'PAID', 'PAYOS', DATEADD(MONTH, -4, DATEADD(DAY, 7, GETDATE())), DATEADD(MONTH, -4, DATEADD(DAY, 7, GETDATE()))),
    (20, 1190000, 'PAID', 'PAYOS', DATEADD(MONTH, -4, DATEADD(DAY, 18, GETDATE())), DATEADD(MONTH, -4, DATEADD(DAY, 18, GETDATE()))),
    (21, 890000, 'PAID', 'PAYOS', DATEADD(MONTH, -4, DATEADD(DAY, 25, GETDATE())), DATEADD(MONTH, -4, DATEADD(DAY, 25, GETDATE()))),

    -- THÁNG 4 (3 tháng trước)
    (22, 1450000, 'PAID', 'PAYOS', DATEADD(MONTH, -3, DATEADD(DAY, 4, GETDATE())), DATEADD(MONTH, -3, DATEADD(DAY, 4, GETDATE()))),
    (23, 1500000, 'PAID', 'PAYOS', DATEADD(MONTH, -3, DATEADD(DAY, 14, GETDATE())), DATEADD(MONTH, -3, DATEADD(DAY, 14, GETDATE()))),
    (24, 890000, 'PAID', 'PAYOS', DATEADD(MONTH, -3, DATEADD(DAY, 21, GETDATE())), DATEADD(MONTH, -3, DATEADD(DAY, 21, GETDATE()))),

    -- THÁNG 5 (2 tháng trước)
    (25, 1890000, 'PAID', 'PAYOS', DATEADD(MONTH, -2, DATEADD(DAY, 2, GETDATE())), DATEADD(MONTH, -2, DATEADD(DAY, 2, GETDATE()))),
    (26, 1250000, 'PAID', 'PAYOS', DATEADD(MONTH, -2, DATEADD(DAY, 10, GETDATE())), DATEADD(MONTH, -2, DATEADD(DAY, 10, GETDATE()))),
    (27, 1110000, 'PAID', 'PAYOS', DATEADD(MONTH, -2, DATEADD(DAY, 19, GETDATE())), DATEADD(MONTH, -2, DATEADD(DAY, 19, GETDATE()))),
    (28, 1000000, 'PAID', 'PAYOS', DATEADD(MONTH, -2, DATEADD(DAY, 26, GETDATE())), DATEADD(MONTH, -2, DATEADD(DAY, 26, GETDATE()))),

    -- THÁNG 6 (1 tháng trước)
    (29, 1450000, 'PAID', 'PAYOS', DATEADD(MONTH, -1, DATEADD(DAY, 5, GETDATE())), DATEADD(MONTH, -1, DATEADD(DAY, 5, GETDATE()))),
    (30, 1350000, 'PAID', 'PAYOS', DATEADD(MONTH, -1, DATEADD(DAY, 12, GETDATE())), DATEADD(MONTH, -1, DATEADD(DAY, 12, GETDATE()))),
    (31, 890000, 'PAID', 'PAYOS', DATEADD(MONTH, -1, DATEADD(DAY, 18, GETDATE())), DATEADD(MONTH, -1, DATEADD(DAY, 18, GETDATE()))),
    (13, 1000000, 'PAID', 'PAYOS', DATEADD(MONTH, -1, DATEADD(DAY, 24, GETDATE())), DATEADD(MONTH, -1, DATEADD(DAY, 24, GETDATE()))),

    -- THÁNG 7 (Tháng hiện tại - rải đều từ 20 ngày trước đến hôm nay)
    (14, 190000, 'PAID', 'PAYOS', DATEADD(DAY, -20, GETDATE()), DATEADD(DAY, -20, GETDATE())),
    (15, 350000, 'PAID', 'PAYOS', DATEADD(DAY, -18, GETDATE()), DATEADD(DAY, -18, GETDATE())),
    (16, 250000, 'PAID', 'PAYOS', DATEADD(DAY, -15, GETDATE()), DATEADD(DAY, -15, GETDATE())),
    (17, 890000, 'PAID', 'PAYOS', DATEADD(DAY, -12, GETDATE()), DATEADD(DAY, -12, GETDATE())),
    (18, 450000, 'PAID', 'PAYOS', DATEADD(DAY, -10, GETDATE()), DATEADD(DAY, -10, GETDATE())),
    (19, 190000, 'CANCELLED', 'PAYOS', DATEADD(DAY, -8, GETDATE()), DATEADD(DAY, -8, GETDATE())),
    (20, 350000, 'PAID', 'PAYOS', DATEADD(DAY, -6, GETDATE()), DATEADD(DAY, -6, GETDATE())),
    (21, 890000, 'PAID', 'PAYOS', DATEADD(DAY, -4, GETDATE()), DATEADD(DAY, -4, GETDATE())),
    (22, 250000, 'PAID', 'PAYOS', DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
    (23, 450000, 'PAID', 'PAYOS', DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
    (24, 250000, 'PAID', 'PAYOS', GETDATE(), GETDATE()),
    (25, 450000, 'PENDING', 'PAYOS', DATEADD(HOUR, -2, GETDATE()), DATEADD(HOUR, -2, GETDATE()));

INSERT INTO payments (
    order_id,
    gateway,
    gateway_order_code,
    amount,
    payment_url,
    qr_code_url,
    status,
    paid_at,
    expired_at,
    created_at,
    updated_at
)
VALUES
-- THÁNG 1
(1, 'PAYOS', '1784283301', 890000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100001', 'PAID', DATEADD(MONTH, -6, DATEADD(DAY, 5, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -6, DATEADD(DAY, 5, GETDATE()))), DATEADD(MONTH, -6, DATEADD(DAY, 5, GETDATE())), DATEADD(MONTH, -6, DATEADD(DAY, 5, GETDATE()))),
(2, 'PAYOS', '1784283302', 450000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100002', 'PAID', DATEADD(MONTH, -6, DATEADD(DAY, 15, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -6, DATEADD(DAY, 15, GETDATE()))), DATEADD(MONTH, -6, DATEADD(DAY, 15, GETDATE())), DATEADD(MONTH, -6, DATEADD(DAY, 15, GETDATE()))),
(3, 'PAYOS', '1784283303', 350000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100003', 'PAID', DATEADD(MONTH, -6, DATEADD(DAY, 22, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -6, DATEADD(DAY, 22, GETDATE()))), DATEADD(MONTH, -6, DATEADD(DAY, 22, GETDATE())), DATEADD(MONTH, -6, DATEADD(DAY, 22, GETDATE()))),

-- THÁNG 2
(4, 'PAYOS', '1784283304', 1900000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100004', 'PAID', DATEADD(MONTH, -5, DATEADD(DAY, 3, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -5, DATEADD(DAY, 3, GETDATE()))), DATEADD(MONTH, -5, DATEADD(DAY, 3, GETDATE())), DATEADD(MONTH, -5, DATEADD(DAY, 3, GETDATE()))),
(5, 'PAYOS', '1784283305', 450000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100005', 'PAID', DATEADD(MONTH, -5, DATEADD(DAY, 12, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -5, DATEADD(DAY, 12, GETDATE()))), DATEADD(MONTH, -5, DATEADD(DAY, 12, GETDATE())), DATEADD(MONTH, -5, DATEADD(DAY, 12, GETDATE()))),
(6, 'PAYOS', '1784283306', 840000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100006', 'PAID', DATEADD(MONTH, -5, DATEADD(DAY, 20, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -5, DATEADD(DAY, 20, GETDATE()))), DATEADD(MONTH, -5, DATEADD(DAY, 20, GETDATE())), DATEADD(MONTH, -5, DATEADD(DAY, 20, GETDATE()))),

-- THÁNG 3
(7, 'PAYOS', '1784283307', 2500000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100007', 'PAID', DATEADD(MONTH, -4, DATEADD(DAY, 7, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -4, DATEADD(DAY, 7, GETDATE()))), DATEADD(MONTH, -4, DATEADD(DAY, 7, GETDATE())), DATEADD(MONTH, -4, DATEADD(DAY, 7, GETDATE()))),
(8, 'PAYOS', '1784283308', 1190000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100008', 'PAID', DATEADD(MONTH, -4, DATEADD(DAY, 18, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -4, DATEADD(DAY, 18, GETDATE()))), DATEADD(MONTH, -4, DATEADD(DAY, 18, GETDATE())), DATEADD(MONTH, -4, DATEADD(DAY, 18, GETDATE()))),
(9, 'PAYOS', '1784283309', 890000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100009', 'PAID', DATEADD(MONTH, -4, DATEADD(DAY, 25, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -4, DATEADD(DAY, 25, GETDATE()))), DATEADD(MONTH, -4, DATEADD(DAY, 25, GETDATE())), DATEADD(MONTH, -4, DATEADD(DAY, 25, GETDATE()))),

-- THÁNG 4
(10, 'PAYOS', '1784283310', 1450000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100010', 'PAID', DATEADD(MONTH, -3, DATEADD(DAY, 4, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -3, DATEADD(DAY, 4, GETDATE()))), DATEADD(MONTH, -3, DATEADD(DAY, 4, GETDATE())), DATEADD(MONTH, -3, DATEADD(DAY, 4, GETDATE()))),
(11, 'PAYOS', '1784283311', 1500000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100011', 'PAID', DATEADD(MONTH, -3, DATEADD(DAY, 14, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -3, DATEADD(DAY, 14, GETDATE()))), DATEADD(MONTH, -3, DATEADD(DAY, 14, GETDATE())), DATEADD(MONTH, -3, DATEADD(DAY, 14, GETDATE()))),
(12, 'PAYOS', '1784283312', 890000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100012', 'PAID', DATEADD(MONTH, -3, DATEADD(DAY, 21, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -3, DATEADD(DAY, 21, GETDATE()))), DATEADD(MONTH, -3, DATEADD(DAY, 21, GETDATE())), DATEADD(MONTH, -3, DATEADD(DAY, 21, GETDATE()))),

-- THÁNG 5
(13, 'PAYOS', '1784283313', 1890000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100013', 'PAID', DATEADD(MONTH, -2, DATEADD(DAY, 2, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -2, DATEADD(DAY, 2, GETDATE()))), DATEADD(MONTH, -2, DATEADD(DAY, 2, GETDATE())), DATEADD(MONTH, -2, DATEADD(DAY, 2, GETDATE()))),
(14, 'PAYOS', '1784283314', 1250000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100014', 'PAID', DATEADD(MONTH, -2, DATEADD(DAY, 10, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -2, DATEADD(DAY, 10, GETDATE()))), DATEADD(MONTH, -2, DATEADD(DAY, 10, GETDATE())), DATEADD(MONTH, -2, DATEADD(DAY, 10, GETDATE()))),
(15, 'PAYOS', '1784283315', 1110000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100015', 'PAID', DATEADD(MONTH, -2, DATEADD(DAY, 19, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -2, DATEADD(DAY, 19, GETDATE()))), DATEADD(MONTH, -2, DATEADD(DAY, 19, GETDATE())), DATEADD(MONTH, -2, DATEADD(DAY, 19, GETDATE()))),
(16, 'PAYOS', '1784283316', 1000000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100016', 'PAID', DATEADD(MONTH, -2, DATEADD(DAY, 26, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -2, DATEADD(DAY, 26, GETDATE()))), DATEADD(MONTH, -2, DATEADD(DAY, 26, GETDATE())), DATEADD(MONTH, -2, DATEADD(DAY, 26, GETDATE()))),

-- THÁNG 6
(17, 'PAYOS', '1784283317', 1450000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100017', 'PAID', DATEADD(MONTH, -1, DATEADD(DAY, 5, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -1, DATEADD(DAY, 5, GETDATE()))), DATEADD(MONTH, -1, DATEADD(DAY, 5, GETDATE())), DATEADD(MONTH, -1, DATEADD(DAY, 5, GETDATE()))),
(18, 'PAYOS', '1784283318', 1350000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100018', 'PAID', DATEADD(MONTH, -1, DATEADD(DAY, 12, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -1, DATEADD(DAY, 12, GETDATE()))), DATEADD(MONTH, -1, DATEADD(DAY, 12, GETDATE())), DATEADD(MONTH, -1, DATEADD(DAY, 12, GETDATE()))),
(19, 'PAYOS', '1784283319', 890000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100019', 'PAID', DATEADD(MONTH, -1, DATEADD(DAY, 18, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -1, DATEADD(DAY, 18, GETDATE()))), DATEADD(MONTH, -1, DATEADD(DAY, 18, GETDATE())), DATEADD(MONTH, -1, DATEADD(DAY, 18, GETDATE()))),
(20, 'PAYOS', '1784283320', 1000000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100020', 'PAID', DATEADD(MONTH, -1, DATEADD(DAY, 24, GETDATE())), DATEADD(HOUR, 2, DATEADD(MONTH, -1, DATEADD(DAY, 24, GETDATE()))), DATEADD(MONTH, -1, DATEADD(DAY, 24, GETDATE())), DATEADD(MONTH, -1, DATEADD(DAY, 24, GETDATE()))),

-- THÁNG 7 (Có 3 mã PayOS thật)
(21, 'PAYOS', '1784811700', 190000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100021', 'PAID', DATEADD(DAY, -20, GETDATE()), DATEADD(HOUR, 2, DATEADD(DAY, -20, GETDATE())), DATEADD(DAY, -20, GETDATE()), DATEADD(DAY, -20, GETDATE())),
(22, 'PAYOS', '1784811719', 350000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100022', 'PAID', DATEADD(DAY, -18, GETDATE()), DATEADD(HOUR, 2, DATEADD(DAY, -18, GETDATE())), DATEADD(DAY, -18, GETDATE()), DATEADD(DAY, -18, GETDATE())),
(23, 'PAYOS', '1784811730', 250000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100023', 'PAID', DATEADD(DAY, -15, GETDATE()), DATEADD(HOUR, 2, DATEADD(DAY, -15, GETDATE())), DATEADD(DAY, -15, GETDATE()), DATEADD(DAY, -15, GETDATE())),
(24, 'PAYOS', '1784283324', 890000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100024', 'PAID', DATEADD(DAY, -12, GETDATE()), DATEADD(HOUR, 2, DATEADD(DAY, -12, GETDATE())), DATEADD(DAY, -12, GETDATE()), DATEADD(DAY, -12, GETDATE())),
(25, 'PAYOS', '1784283325', 450000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100025', 'PAID', DATEADD(DAY, -10, GETDATE()), DATEADD(HOUR, 2, DATEADD(DAY, -10, GETDATE())), DATEADD(DAY, -10, GETDATE()), DATEADD(DAY, -10, GETDATE())),
(26, 'PAYOS', '1784283326', 190000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100026', 'CANCELLED', NULL, DATEADD(HOUR, 2, DATEADD(DAY, -8, GETDATE())), DATEADD(DAY, -8, GETDATE()), DATEADD(DAY, -8, GETDATE())),
(27, 'PAYOS', '1784283327', 350000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100027', 'PAID', DATEADD(DAY, -6, GETDATE()), DATEADD(HOUR, 2, DATEADD(DAY, -6, GETDATE())), DATEADD(DAY, -6, GETDATE()), DATEADD(DAY, -6, GETDATE())),
(28, 'PAYOS', '1784283328', 890000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100028', 'PAID', DATEADD(DAY, -4, GETDATE()), DATEADD(HOUR, 2, DATEADD(DAY, -4, GETDATE())), DATEADD(DAY, -4, GETDATE()), DATEADD(DAY, -4, GETDATE())),
(29, 'PAYOS', '1784283329', 250000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100029', 'PAID', DATEADD(DAY, -2, GETDATE()), DATEADD(HOUR, 2, DATEADD(DAY, -2, GETDATE())), DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
(30, 'PAYOS', '1784283330', 450000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100030', 'PAID', DATEADD(DAY, -1, GETDATE()), DATEADD(HOUR, 2, DATEADD(DAY, -1, GETDATE())), DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
(31, 'PAYOS', '1784283331', 250000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100031', 'PAID', GETDATE(), DATEADD(HOUR, 2, GETDATE()), GETDATE(), GETDATE()),
(32, 'PAYOS', '1784283332', 450000, 'https://pay.payos.vn/web/56b19a169a0845c880a8de4f0baa581c', 'https://qr.payos.vn/100032', 'PENDING', NULL, DATEADD(HOUR, 2, GETDATE()), DATEADD(HOUR, -1, GETDATE()), DATEADD(HOUR, -1, GETDATE()));

-- =========================
-- ORDER ITEMS SAMPLE DATA
-- =========================
INSERT INTO order_items (order_id, course_id, price_snapshot, course_title_snapshot, created_at, updated_at) 
VALUES
(1, 13, 890000.00, N'React Native - Lập Trình Di Động Thực Chiến', DATEADD(MONTH, -6, DATEADD(DAY, 5, GETDATE())), DATEADD(MONTH, -6, DATEADD(DAY, 5, GETDATE()))),
(2, 12, 450000.00, N'Responsive Web Design Với Flexbox & Grid', DATEADD(MONTH, -6, DATEADD(DAY, 15, GETDATE())), DATEADD(MONTH, -6, DATEADD(DAY, 15, GETDATE()))),
(3, 10, 350000.00, N'Thiết Kế Web Landing Page Với HTML5', DATEADD(MONTH, -6, DATEADD(DAY, 22, GETDATE())), DATEADD(MONTH, -6, DATEADD(DAY, 22, GETDATE()))),

(4, 13, 890000.00, N'React Native - Lập Trình Di Động Thực Chiến', DATEADD(MONTH, -5, DATEADD(DAY, 3, GETDATE())), DATEADD(MONTH, -5, DATEADD(DAY, 3, GETDATE()))),
(5, 12, 450000.00, N'Responsive Web Design Với Flexbox & Grid', DATEADD(MONTH, -5, DATEADD(DAY, 12, GETDATE())), DATEADD(MONTH, -5, DATEADD(DAY, 12, GETDATE()))),
(6, 11, 250000.00, N'Tailwind CSS Từ Zero Đến Hero', DATEADD(MONTH, -5, DATEADD(DAY, 20, GETDATE())), DATEADD(MONTH, -5, DATEADD(DAY, 20, GETDATE()))),

(7, 13, 890000.00, N'React Native - Lập Trình Di Động Thực Chiến', DATEADD(MONTH, -4, DATEADD(DAY, 7, GETDATE())), DATEADD(MONTH, -4, DATEADD(DAY, 7, GETDATE()))),
(8, 10, 350000.00, N'Thiết Kế Web Landing Page Với HTML5', DATEADD(MONTH, -4, DATEADD(DAY, 18, GETDATE())), DATEADD(MONTH, -4, DATEADD(DAY, 18, GETDATE()))),
(9, 9, 190000.00, N'HTML5 & CSS3 Cơ Bản Cho Người Mới', DATEADD(MONTH, -4, DATEADD(DAY, 25, GETDATE())), DATEADD(MONTH, -4, DATEADD(DAY, 25, GETDATE()))),

(10, 12, 450000.00, N'Responsive Web Design Với Flexbox & Grid', DATEADD(MONTH, -3, DATEADD(DAY, 4, GETDATE())), DATEADD(MONTH, -3, DATEADD(DAY, 4, GETDATE()))),
(11, 13, 890000.00, N'React Native - Lập Trình Di Động Thực Chiến', DATEADD(MONTH, -3, DATEADD(DAY, 14, GETDATE())), DATEADD(MONTH, -3, DATEADD(DAY, 14, GETDATE()))),
(12, 11, 250000.00, N'Tailwind CSS Từ Zero Đến Hero', DATEADD(MONTH, -3, DATEADD(DAY, 21, GETDATE())), DATEADD(MONTH, -3, DATEADD(DAY, 21, GETDATE()))),

(13, 13, 890000.00, N'React Native - Lập Trình Di Động Thực Chiến', DATEADD(MONTH, -2, DATEADD(DAY, 2, GETDATE())), DATEADD(MONTH, -2, DATEADD(DAY, 2, GETDATE()))),
(14, 10, 350000.00, N'Thiết Kế Web Landing Page Với HTML5', DATEADD(MONTH, -2, DATEADD(DAY, 10, GETDATE())), DATEADD(MONTH, -2, DATEADD(DAY, 10, GETDATE()))),
(15, 12, 450000.00, N'Responsive Web Design Với Flexbox & Grid', DATEADD(MONTH, -2, DATEADD(DAY, 19, GETDATE())), DATEADD(MONTH, -2, DATEADD(DAY, 19, GETDATE()))),
(16, 9, 190000.00, N'HTML5 & CSS3 Cơ Bản Cho Người Mới', DATEADD(MONTH, -2, DATEADD(DAY, 26, GETDATE())), DATEADD(MONTH, -2, DATEADD(DAY, 26, GETDATE()))),

(17, 12, 450000.00, N'Responsive Web Design Với Flexbox & Grid', DATEADD(MONTH, -1, DATEADD(DAY, 5, GETDATE())), DATEADD(MONTH, -1, DATEADD(DAY, 5, GETDATE()))),
(18, 10, 350000.00, N'Thiết Kế Web Landing Page Với HTML5', DATEADD(MONTH, -1, DATEADD(DAY, 12, GETDATE())), DATEADD(MONTH, -1, DATEADD(DAY, 12, GETDATE()))),
(19, 13, 890000.00, N'React Native - Lập Trình Di Động Thực Chiến', DATEADD(MONTH, -1, DATEADD(DAY, 18, GETDATE())), DATEADD(MONTH, -1, DATEADD(DAY, 18, GETDATE()))),
(20, 11, 250000.00, N'Tailwind CSS Từ Zero Đến Hero', DATEADD(MONTH, -1, DATEADD(DAY, 24, GETDATE())), DATEADD(MONTH, -1, DATEADD(DAY, 24, GETDATE()))),

(21, 9, 190000.00, N'HTML5 & CSS3 Cơ Bản Cho Người Mới', DATEADD(DAY, -20, GETDATE()), DATEADD(DAY, -20, GETDATE())),
(22, 10, 350000.00, N'Thiết Kế Web Landing Page Với HTML5', DATEADD(DAY, -18, GETDATE()), DATEADD(DAY, -18, GETDATE())),
(23, 11, 250000.00, N'Tailwind CSS Từ Zero Đến Hero', DATEADD(DAY, -15, GETDATE()), DATEADD(DAY, -15, GETDATE())),
(24, 13, 890000.00, N'React Native - Lập Trình Di Động Thực Chiến', DATEADD(DAY, -12, GETDATE()), DATEADD(DAY, -12, GETDATE())),
(25, 12, 450000.00, N'Responsive Web Design Với Flexbox & Grid', DATEADD(DAY, -10, GETDATE()), DATEADD(DAY, -10, GETDATE())),
(26, 9, 190000.00, N'HTML5 & CSS3 Cơ Bản Cho Người Mới', DATEADD(DAY, -8, GETDATE()), DATEADD(DAY, -8, GETDATE())),
(27, 10, 350000.00, N'Thiết Kế Web Landing Page Với HTML5', DATEADD(DAY, -6, GETDATE()), DATEADD(DAY, -6, GETDATE())),
(28, 13, 890000.00, N'React Native - Lập Trình Di Động Thực Chiến', DATEADD(DAY, -4, GETDATE()), DATEADD(DAY, -4, GETDATE())),
(29, 11, 250000.00, N'Tailwind CSS Từ Zero Đến Hero', DATEADD(DAY, -2, GETDATE()), DATEADD(DAY, -2, GETDATE())),
(30, 12, 450000.00, N'Responsive Web Design Với Flexbox & Grid', DATEADD(DAY, -1, GETDATE()), DATEADD(DAY, -1, GETDATE())),
(31, 11, 250000.00, N'Tailwind CSS Từ Zero Đến Hero', GETDATE(), GETDATE()),
(32, 12, 450000.00, N'Responsive Web Design Với Flexbox & Grid', DATEADD(HOUR, -1, GETDATE()), DATEADD(HOUR, -1, GETDATE()));

-- =========================
-- TỰ ĐỘNG ĐỒNG BỘ ENROLLMENTS & ORDERS CHO FEEDBACKS
-- (Tự động thêm khóa học vào trạng thái 'Đã Mua' nếu user đó có để lại Feedback)
-- =========================

-- 1. Insert missing orders and order_items
DECLARE @user_id INT, @course_id INT, @price DECIMAL(18,2), @title NVARCHAR(255), @order_id INT, @days_ago INT;
DECLARE cur CURSOR FOR 
SELECT f.user_id, f.course_id, c.price, c.title 
FROM feedbacks f 
JOIN courses c ON f.course_id = c.id 
LEFT JOIN (
    SELECT o.user_id, oi.course_id 
    FROM orders o 
    JOIN order_items oi ON o.id = oi.order_id 
    WHERE o.status = 'PAID'
) o_existing ON f.user_id = o_existing.user_id AND f.course_id = o_existing.course_id 
WHERE o_existing.course_id IS NULL;

OPEN cur; 
FETCH NEXT FROM cur INTO @user_id, @course_id, @price, @title; 
SET @days_ago = 20;
WHILE @@FETCH_STATUS = 0 
BEGIN 
    INSERT INTO orders (user_id, total_amount, status, payment_method, created_at, updated_at) 
    VALUES (@user_id, @price, 'PAID', 'SYSTEM', DATEADD(DAY, -@days_ago, GETDATE()), DATEADD(DAY, -@days_ago, GETDATE())); 
    
    SET @order_id = SCOPE_IDENTITY(); 
    
    INSERT INTO order_items (order_id, course_id, price_snapshot, course_title_snapshot, created_at, updated_at) 
    VALUES (@order_id, @course_id, @price, @title, DATEADD(DAY, -@days_ago, GETDATE()), DATEADD(DAY, -@days_ago, GETDATE())); 
    
    SET @days_ago = CASE WHEN @days_ago > 2 THEN @days_ago - 2 ELSE 15 END;
    FETCH NEXT FROM cur INTO @user_id, @course_id, @price, @title; 
END; 
CLOSE cur; 
DEALLOCATE cur;

-- 2. Insert missing enrollments and ensure progress > 30%
INSERT INTO enrollments (user_id, course_id, progress_percent, created_at, updated_at) 
SELECT DISTINCT f.user_id, f.course_id, 35.00, GETDATE(), GETDATE() 
FROM feedbacks f 
LEFT JOIN enrollments e ON f.user_id = e.user_id AND f.course_id = e.course_id 
WHERE e.id IS NULL;

UPDATE e 
SET progress_percent = CASE WHEN e.progress_percent < 35 THEN 35 ELSE e.progress_percent END 
FROM enrollments e 
JOIN feedbacks f ON e.user_id = f.user_id AND e.course_id = f.course_id;

-- 3. Insert missing enrollments for any PAID orders
INSERT INTO enrollments (user_id, course_id, progress_percent, created_at, updated_at) 
SELECT DISTINCT o.user_id, oi.course_id, 0.00, GETDATE(), GETDATE() 
FROM orders o 
JOIN order_items oi ON o.id = oi.order_id 
WHERE o.status IN ('PAID', 'COMPLETED') 
AND NOT EXISTS (
    SELECT 1 FROM enrollments e 
    WHERE e.user_id = o.user_id AND e.course_id = oi.course_id
);


-- =========================================================================
-- GENERATE LESSON PROGRESS DATA BASED ON ENROLLMENT PROGRESS PERCENTAGE
-- =========================================================================
-- Tự động sinh dữ liệu trong bảng lesson_progress để khớp với progress_percent ở bảng enrollments.
-- Ví dụ: Nếu một khóa học có 9 bài học và tiến độ học viên là 50%, học viên đó sẽ hoàn thành 4 hoặc 5 bài học.

DECLARE @ProgEnrollmentId INT, @ProgCourseId INT, @ProgProgressPercent DECIMAL(5,2);
DECLARE @ProgTotalLessons INT, @ProgCompletedLessons INT;
DECLARE @ProgLessonId INT, @ProgCounter INT;

DECLARE enrollment_cursor CURSOR FOR
SELECT id, course_id, progress_percent
FROM enrollments;

OPEN enrollment_cursor;
FETCH NEXT FROM enrollment_cursor INTO @ProgEnrollmentId, @ProgCourseId, @ProgProgressPercent;

WHILE @@FETCH_STATUS = 0
BEGIN
    -- Lấy tổng số bài học của khóa học này
    SELECT @ProgTotalLessons = COUNT(l.id)
    FROM lessons l
    JOIN course_sections cs ON l.section_id = cs.id
    WHERE cs.course_id = @ProgCourseId;

    IF @ProgTotalLessons > 0
    BEGIN
        -- Tính số bài học cần hoàn thành tương ứng với progress_percent
        SET @ProgCompletedLessons = ROUND((@ProgProgressPercent / 100.0) * @ProgTotalLessons, 0);

        -- Dùng cursor phụ để duyệt qua từng bài học của khóa học theo thứ tự position/id
        DECLARE lesson_cursor CURSOR FOR
        SELECT l.id
        FROM lessons l
        JOIN course_sections cs ON l.section_id = cs.id
        WHERE cs.course_id = @ProgCourseId
        ORDER BY cs.position, l.position, l.id;

        OPEN lesson_cursor;
        SET @ProgCounter = 1;
        FETCH NEXT FROM lesson_cursor INTO @ProgLessonId;

        WHILE @@FETCH_STATUS = 0
        BEGIN
            DECLARE @ProgIsCompleted BIT = 0;
            IF @ProgCounter <= @ProgCompletedLessons
            BEGIN
                SET @ProgIsCompleted = 1;
            END

            -- Chèn dữ liệu vào bảng lesson_progress nếu chưa tồn tại
            IF NOT EXISTS (SELECT 1 FROM lesson_progress WHERE enrollment_id = @ProgEnrollmentId AND lesson_id = @ProgLessonId)
            BEGIN
                INSERT INTO lesson_progress (enrollment_id, lesson_id, is_completed, last_accessed, updated_at)
                VALUES (@ProgEnrollmentId, @ProgLessonId, @ProgIsCompleted, GETDATE(), GETDATE());
            END
            ELSE
            BEGIN
                UPDATE lesson_progress
                SET is_completed = @ProgIsCompleted, updated_at = GETDATE()
                WHERE enrollment_id = @ProgEnrollmentId AND lesson_id = @ProgLessonId;
            END

            SET @ProgCounter = @ProgCounter + 1;
            FETCH NEXT FROM lesson_cursor INTO @ProgLessonId;
        END

        CLOSE lesson_cursor;
        DEALLOCATE lesson_cursor;
    END

    FETCH NEXT FROM enrollment_cursor INTO @ProgEnrollmentId, @ProgCourseId, @ProgProgressPercent;
END

CLOSE enrollment_cursor;
DEALLOCATE enrollment_cursor;
