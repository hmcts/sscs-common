package uk.gov.hmcts.reform.sscs.ccd.util;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdValue;
import uk.gov.hmcts.reform.sscs.ccd.domain.DocumentSelectionDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.DynamicList;
import uk.gov.hmcts.reform.sscs.ccd.domain.DynamicListItem;
import uk.gov.hmcts.reform.sscs.ccd.domain.OtherPartySelectionDetails;

@UtilityClass
public final class SelectionValidator {

    public static boolean otherPartySelectionContainsDuplicates(
        final List<CcdValue<OtherPartySelectionDetails>> otherPartySelection) {
        return containsDuplicateCodes(otherPartySelection, OtherPartySelectionDetails::getOtherPartiesList);
    }

    public static boolean documentSelectionContainsDuplicates(
        final List<CcdValue<DocumentSelectionDetails>> documentSelection) {
        return containsDuplicateCodes(documentSelection, DocumentSelectionDetails::getDocumentsList);
    }

    private static <T> boolean containsDuplicateCodes(
        final List<CcdValue<T>> items,
        final Function<T, DynamicList> dynamicListExtractor) {

        if (isEmpty(items)) {
            return false;
        }

        final List<String> codes = items.stream()
            .map(CcdValue::getValue)
            .map(dynamicListExtractor)
            .filter(Objects::nonNull)
            .map(DynamicList::getValue)
            .filter(Objects::nonNull)
            .map(DynamicListItem::getCode)
            .toList();

        return codes.size() != new HashSet<>(codes).size();
    }
}