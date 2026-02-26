package uk.gov.hmcts.reform.sscs.ccd.util;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdValue;
import uk.gov.hmcts.reform.sscs.ccd.domain.DocumentSelectionDetails;
import uk.gov.hmcts.reform.sscs.ccd.domain.DynamicList;
import uk.gov.hmcts.reform.sscs.ccd.domain.DynamicListItem;
import uk.gov.hmcts.reform.sscs.ccd.domain.OtherPartySelectionDetails;

@UtilityClass
public final class SelectionValidator {

    public static Optional<String> validateOtherPartySelection(
        final List<CcdValue<OtherPartySelectionDetails>> otherPartySelection) {
        return validateNoDuplicateCodes(
            otherPartySelection,
            OtherPartySelectionDetails::getOtherPartiesList,
            "Other parties cannot be selected more than once");
    }

    public static Optional<String> validateDocumentSelection(
        final List<CcdValue<DocumentSelectionDetails>> documentSelection) {
        return validateNoDuplicateCodes(
            documentSelection,
            DocumentSelectionDetails::getDocumentsList,
            "The same document cannot be selected more than once");
    }

    private static <T> Optional<String> validateNoDuplicateCodes(
        final List<CcdValue<T>> items,
        final Function<T, DynamicList> dynamicListExtractor,
        final String errorMessage) {

        if (!isNotEmpty(items)) {
            return Optional.empty();
        }

        final List<String> codes = items.stream()
            .map(CcdValue::getValue)
            .map(dynamicListExtractor)
            .filter(Objects::nonNull)
            .map(DynamicList::getValue)
            .filter(Objects::nonNull)
            .map(DynamicListItem::getCode)
            .toList();

        return codes.size() != new HashSet<>(codes).size()
            ? Optional.of(errorMessage)
            : Optional.empty();
    }
}