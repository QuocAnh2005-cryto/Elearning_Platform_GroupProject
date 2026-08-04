package vn.edu.fpt.util;

import java.util.Map;

public final class AppConstants {

    private AppConstants() {
    }

    public static final long SAS_EXPIRATION_HOURS = 1L;
    public static final String AZURE_STORAGE_BASE_URL = "https://elearningstorageswp391.blob.core.windows.net";

    public static final String AZURE_STORAGE_CONTAINER_LOGS = "logs";
    public static final String AZURE_STORAGE_CONTAINER_COURSE_THUMBNAILS = "course-thumbnails";
    public static final String AZURE_STORAGE_CONTAINER_INSTRUCTOR_CVS = "instructor-cvs";
    public static final String AZURE_STORAGE_CONTAINER_MATERIALS = "materials";
    public static final String AZURE_STORAGE_CONTAINER_USER_AVATARS = "user-avatars";
    public static final String AZURE_STORAGE_CONTAINER_VIDEOS = "videos";

    public static final String OFFICE_VIEWER_BASE_URL = "https://view.officeapps.live.com/op/embed.aspx?src=";

    public static final Float DEFAULT_DISCOUNT = 0.3F;

    public static final int PAYMENT_EXPIRATION_MINUTES = 15;

    public static final Double PLATFORM_FEE = 0.3D;

    public static final String PAYMENT_GATEWAY = "PAYOS";

    public static final String QR_CODE_BASE_URL = "https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=";

    public static final Map<String, String> BANK_NAMES = Map.of(
            "970422", "MB Bank",
            "970436", "Vietcombank",
            "970418", "BIDV",
            "970415", "VietinBank",
            "970407", "Techcombank");

    public static final int NUMBER_PAYMENT_RECORD_PER_PAGE = 6;
    public static final int NUMBER_LEARNER_RECORD_PER_PAGE = 10;

    public static final int NUMBER_PAGE_PER_BLOCK = 5;

    public static final long EXPIRED_PAYMENT_CHECK_INTERVAL_SECONDS = 120;

    public static final long PENDING_SYNC_CHECK_INTERVAL_MINUTES = 5;

    public static final long PENDING_PAYMENT_MAX_AGE_MINUTES = 30;

    public static final long PAYMENT_LAST_SYNC_SKIP_INTERVAL_MINUTES = 5;

    public static final long PAYMENT_SYNC_TIMESTAMP_INTERVAL_MINUTES = 5;

    public static final double PERCENT_COMPLETED_LESSON_TO_COMMENT = 30.0;

    public static final long MAX_MATERIAL_FILE_SIZE_BYTES = 50 * 1024 * 1024L; // 50MB limit per BR-35 & PERF-04

    public static final String RETURN_URL = "https://learninghubswp391.eastasia.cloudapp.azure.com/payment/success";
    public static final String CANCEL_URL = "https://learninghubswp391.eastasia.cloudapp.azure.com/payment/cancel";

}
