var LANG_SUPORT = ["ko", "my", "en"];

var LANG_ASIDE_MENU = {
    ko: {
        aside_menu_config: "Config Menu",
        aside_menu_0: "대시보드",
        aside_menu_1: "회원 및 관리자 관리",
        aside_menu_1_1: "관리자",
        aside_menu_1_2: "회원",
        aside_menu_1_3: "권한 관리",
        aside_menu_2: "게시판",
        aside_menu_2_1: "공지사항",
        aside_menu_2_2: "자주묻는질문",
        aside_menu_2_3: "문의게시판",
        aside_menu_3: "대사관 관리",
        aside_menu_3_1: "방문 예약 관리",
        aside_menu_3_2: "방문 이력 조회",
        aside_menu_3_3: "예약 오픈 기간 설정",
        aside_menu_4: "배너/팝업 관리",
        aside_menu_4_1: "배너 관리",
        aside_menu_4_2: "팝업 관리",
        aside_menu_5: "푸시 관리",
        aside_menu_5_1: "푸시알림",
    },
    my: {
        aside_menu_config: "Config Menu",
        aside_menu_0: "ဒက်ဘုတ်",
        aside_menu_1: "အဖွဲ့ဝင်နှင့် စီမံခန့်ခွဲသူ စီမံခန့်ခွဲမှု",
        aside_menu_1_1: "စီမံခန့်ခွဲသူ",
        aside_menu_1_2: "အဖွဲ့ဝင်",
        aside_menu_1_3: "ခွင့်ပြုချက်စီမံခန့်ခွဲမှု",
        aside_menu_2: "ကြော်ငြာသင်ပုန်း",
        aside_menu_2_1: "ကြေငြာချက်",
        aside_menu_2_2: "အမေးများသောမေးခွန်းများ",
        aside_menu_2_3: "စုံစမ်းရေးဘုတ်အဖွဲ့",
        aside_menu_3: "သံရုံးစီမံခန့်ခွဲမှု",
        aside_menu_3_1: "ကြိုတင်မှာကြားမှု စီမံခန့်ခွဲမှုသို့ သွားရောက်ပါ။",
        aside_menu_3_2: "လည်ပတ်မှုမှတ်တမ်းကို စစ်ဆေးပါ။",
        aside_menu_3_3: "အားလပ်ရက်ဆက်တင်များ",
        aside_menu_4: "ဆိုင်းဘုတ် စီမံခန့်ခွဲမှု",
        aside_menu_4_1: "ဆိုင်းဘုတ် စီမံခန့်ခွဲမှု",
        aside_menu_4_2: "သပိတ် စီမံခန့်ခွဲမှု",
        aside_menu_5: "တွန်းအားပေး စီမံခန့်ခွဲမှု",
        aside_menu_5_1: "တွန်းအားပေး အသိပေးခြင်း",
    },
    en: {
        aside_menu_config: "Config Menu",
        aside_menu_0: "Dashboard",
        aside_menu_1: "Admin and Member Management",
        aside_menu_1_1: "Admin",
        aside_menu_1_2: "Member",
        aside_menu_1_3: "Authorization management",
        aside_menu_2: "Board",
        aside_menu_2_1: "Notice",
        aside_menu_2_2: "Frequently Asked Questions (FAQ)",
        aside_menu_2_3: "Inquiry Board",
        aside_menu_3: "Visit Schedule Management",
        aside_menu_3_1: "Visit Reservation Management",
        aside_menu_3_2: "Visit History",
        aside_menu_3_3: "Opening Time Settings",
        aside_menu_4: "Banner / Popup Management",
        aside_menu_4_1: "Banner",
        aside_menu_4_2: "Popup ",
        aside_menu_5: "Push Management",
        aside_menu_5_1: "Push",
    },
};

var LANG_HEADER = {
    ko: {
        ROLE_ADMIN: "일반관리자",
        ROLE_SUPER_ADMIN: "최고관리자",
        ROLE_SYSTEM_ADMIN: "시스템관리자",
    },
    my: {
        ROLE_ADMIN: "သာမန် ခန့်ခွဲသူ",
        ROLE_SUPER_ADMIN: "အကောင်းဆုံး ခန့်ခွဲသူ",
        ROLE_SYSTEM_ADMIN: "စနစ်စီမံခန့်ခွဲသူ",
    },
    en: {
        ROLE_ADMIN: "Admin",
        ROLE_SUPER_ADMIN: "Super Admin",
        ROLE_SYSTEM_ADMIN: "System Admin",
    },
};

var LANG_DAYS_OF_WEEK = {
    ko: ["일", "월", "화", "수", "목", "금", "토"],
    my: [
        "တနင်္ဂနွေနေ့",
        "တနင်္လာနေ့",
        "အင်္ဂါနေ့",
        "ဗုဒ္ဓဟူးနေ့",
        "ကြာသပတေးနေ",
        "သောကြာနေ့",
        "စနေနေ့ ",
    ],
    en: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
};

var LANG_DAYS_OF_WEEK_KR_EN = {
    ko: ["일", "월", "화", "수", "목", "금", "토"],
    my: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
    en: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
};

var LANG_MONTH_NAMES = {
    ko: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
    my: [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
    ],
    en: [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
    ],
};

var LANG_CANCEL = {
    ko: "취소",
    my: "ဖျက်သိမ်းခြင်း",
    en: "Cancel",
};

var LANG_APPLY = {
    ko: "적용하다",
    my: "လျှောက်ထားပါ",
    en: "Apply",
};

var LANG_OK = {
    ko: "확인",
    my: "အတည်ပြုခြင်း",
    en: "OK",
};

var LANG_CLEAR = {
    ko: "삭제",
    my: "ရှင်းလင်းသော",
    en: "Clear",
};

var LANG_SEARCH_REGISTAION = {
    ko: {
        search_registration_date_now: "오늘",
        search_registration_date_7_days: "7일",
        search_registration_date_next_7_days: "7일",
        search_registration_date_1_month: "1개월",
        search_registration_date_next_1_month: "1개월",
        search_registration_date_3_months: "3개월",
        search_registration_date_next_3_months: "3개월",
        search_registration_date_enter_directly: "직접 입력",
    },
    my: {
        search_registration_date_now: "ဒီနေ့",
        search_registration_date_7_days: "၇ ရက်",
        search_registration_date_next_7_days: "၇ ရက်",
        search_registration_date_1_month: "တတစ်လ",
        search_registration_date_next_1_month: "တတစ်လ",
        search_registration_date_3_months: "သုံးလ",
        search_registration_date_next_3_months: "သုံးလ",
        search_registration_date_enter_directly: "တိုက်ရိုက် ညွှန်ကြားချက်",
    },
    en: {
        search_registration_date_now: "Today",
        search_registration_date_7_days: "Last 7 Days",
        search_registration_date_next_7_days: "Next 7 Days",
        search_registration_date_1_month: "Last Month",
        search_registration_date_next_1_month: "Next Month",
        search_registration_date_3_months: "Last 3 Months",
        search_registration_date_next_3_months: "Next 3 Months",
        search_registration_date_enter_directly: "Custom",
    },
};

var UPDATE_INFORMATION_SECTION = {
    ko: {
        update_name: "수정자명",
        update_time: "수정일시",
        update_name_QA: "답변자",
        update_time_QA: "답변일시",
    },
    my: {
        update_name: "နာမည် ပြုပြင်ပြောင်းလဲခြင်း",
        update_time: "ပြန်လည်ပြင်ဆင်ရေးရက်စွဲ",
        update_name_QA: "အဖြေပေးသူ",
        update_time_QA: "တုံ့ပြန်မှု",
    },
    en: {
        update_name: "Modified By",
        update_time: "Last Modified Date",
        update_name_QA: "Responder",
        update_time_QA: "Response Date",
    },
};

var LANG_PUSHED = {
    true: {
        ko: "발송",
        my: "ပေးပို့သည်",
        en:"Sent"
    },
    false: {
        ko: "미발송",
        my: "မပေးပို့ပါ",
        en: "Not Sent"
    }
};

var LANG_SHOWED = {
    true: {
        ko: "노출",
        my: "ဖော်ထုတ်ခြင်း",
        en: "Visible"
    },
    false: {
        ko: "비노출",
        my: "မဖော်ထုတ်ခြင်း",
        en: "Invisible"
    }
};

var LANG_SHOWED_QA = {
    true: {
        ko: "공개",
        my: "ဖော်ပြမည်",
        en: "Public"
    },
    false: {
        ko: "비공개",
        my: "မဖော်ပြထားပါ",
        en: "Private"
    }
}
var LANG_PUSHED_NOTIFICATION = {
    true: {
        ko: "발송",
        my: "ဖော်ထုတ်ခြင်း",
        en: "Sent"

    },
    false: {
        ko: "미발송",
        my: "မဖော်ထုတ်ခြင်း",
        en: "Scheduled"
    }
}

var FULL_CALENDAR_LANG = {
    ko: {
        buttonText: {
            today: "오늘",
        },
    },
    my: {
        buttonText: {
            today: "ဒီနေ့",
        },
    },
    en: {
        buttonText: {
            today: "Today",
        },
    },
};

var LANG_GENDER = {
    ko: {
        MALE: "남",
        FEMALE: "여",
    },
    my: {
        MALE: "ကျား",
        FEMALE: "မ",
    },
    en: {
        MALE: "Male",
        FEMALE: "Female",
    },
};

var LANG_MEMBER_STATUS_EXIT = {
    ko: {
        true: "탈퇴",
        false: "사용"
    },
    my: {
        true: "N",
        false: "Y",
    },
    en: {
        true: "Withdraw",
        false: "Active",
    },
};

var UPDATE_HISTORY_TBL = {
    ko: {
        no: "no",
        header_visit_dt: "방문일시",
        header_update_item: "수정사항",
        header_before_val: "수정 전 내용",
        header_after_val: "수정 후 내용",
        header_update_acc_name: "수정자",
        header_update_dt: "수정일시",
    },
    my: {
        no: "NO",
        header_visit_dt: "ရက်စွဲနှင့် လည်ပတ်ချိန်",
        header_update_item: "သတ်မှတ်ချက်များ",
        header_before_val: "ပြန်လည်ပြင်ဆင်မှုမတိုင်မီ အချက်အလက်များ",
        header_after_val: "ပြန်လည်ပြင်ဆင်ပြီးနောက် အချက်အလက်များ",
        header_update_acc_name: "ပြုပြင်သူ",
        header_update_dt: "ပြန်လည်ပြင်ဆင်ရေးရက်စွဲ",
    },
    en: {
        no: "No.",
        header_visit_dt: "Visit Date",
        header_update_item: "Changes Made",
        header_before_val: "Before Changes",
        header_after_val: "After Changes",
        header_update_acc_name: "Modified By",
        header_update_dt: "Modified Date",
    },
};

var RESIDENCY_ISSUED_BY = {
    ko: {
        SEOUL: "서울",
        INCHEON: "인천",
        BUSAN: "부산",
    },
    my: {
        SEOUL: "seoul",
        INCHEON: "Incheon",
        BUSAN: "Busan",
    },
    en: {
        SEOUL: "seoul",
        INCHEON: "Incheon",
        BUSAN: "Busan",
    },
};
