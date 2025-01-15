package com.tranphucvinh.constant;

public enum GrpRefTableEnum {

    tb_advisory(
            new RefTypeEnum[]{
                    RefTypeEnum.advisory_type
            }
    ),
    tb_advisory_answer(
            new RefTypeEnum[]{
                    RefTypeEnum.advisory_answer_type
            }
    ),
    tb_example1(new RefTypeEnum[]{RefTypeEnum.example_type1}),
    tb_notification(
            new RefTypeEnum[]{RefTypeEnum.notification_type_attachment, RefTypeEnum.notification_type_image}),
    tb_popup(new RefTypeEnum[]{RefTypeEnum.popup_img}),
    tb_banner(new RefTypeEnum[]{RefTypeEnum.banner_img});

    public final RefTypeEnum[] grpRefTypes;

    GrpRefTableEnum(RefTypeEnum[] grpRefTypes) {
        this.grpRefTypes = grpRefTypes;
    }

    public enum RefTypeEnum {
        advisory_type // tb_advisory
        , advisory_answer_type // tb_advisory_answer
        , example_type // tb_example
        , example_type1 // tb_example
        , notification_type_attachment, notification_type_image // tb_notification
        , popup_img // tb_popup
        , banner_img // tb_banner
    }
}



