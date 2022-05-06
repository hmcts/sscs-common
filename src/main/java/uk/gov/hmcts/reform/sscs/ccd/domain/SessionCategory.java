package uk.gov.hmcts.reform.sscs.ccd.domain;

import static uk.gov.hmcts.reform.sscs.ccd.domain.PanelMember.*;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SessionCategory {
    CATEGORY_00(0, List.of(SALARIED_JUDGE), "Interlocutory"),
    CATEGORY_01(1,List.of(JUDGE),"Judge Alone"),
    CATEGORY_02(2,List.of(JUDGE, FQPM),"Judge & Financial Member"),
    CATEGORY_03(3,List.of(JUDGE, DQPM),"Judge, Doctor & Disability Member"),
    CATEGORY_04(4,List.of(JUDGE, DOCTOR),"Judge & Doctor"),
    CATEGORY_05(5,List.of(JUDGE, MQPM1),"Judge & Specialist Doctor"),
    CATEGORY_06(6,List.of(JUDGE, MQPM1, MQPM2),"Judge & Two Specialist Doctors"),
    CATEGORY_07(7,List.of(JUDGE, SPECIALIST_MEMBER),"Judge and Specialist Member");


    private final int sessionCategoryCode;
    private final List<PanelMember> panelMembers;
    private final String name;

    public static SessionCategory getSessionCategory(String sessionCategoryCode) {
        int sessionCategoryCodeInt = Integer.parseInt(sessionCategoryCode.replaceFirst("^0+(?!$)", ""));
        return getSessionCategory(sessionCategoryCodeInt);
    }

    public static SessionCategory getSessionCategory(int sessionCategoryCode) {
        return Arrays.stream(values())
                .filter(sessionCategory -> sessionCategory.sessionCategoryCode == sessionCategoryCode)
                .findFirst()
                .orElse(null);
    }
}
