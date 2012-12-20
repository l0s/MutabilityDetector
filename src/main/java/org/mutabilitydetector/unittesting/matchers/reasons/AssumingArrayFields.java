package org.mutabilitydetector.unittesting.matchers.reasons;

import static java.util.Arrays.asList;
import static org.mutabilitydetector.MutabilityReason.ARRAY_TYPE_INHERENTLY_MUTABLE;
import static org.mutabilitydetector.MutabilityReason.MUTABLE_TYPE_TO_FIELD;

import org.hamcrest.Matcher;
import org.mutabilitydetector.MutableReasonDetail;
import org.mutabilitydetector.locations.FieldLocation;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

public class AssumingArrayFields {
    private final ImmutableSet<String> fieldNames;

    public AssumingArrayFields(ImmutableSet<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public static AssumingArrayFields named(String first, String... rest) {
        return new AssumingArrayFields(ImmutableSet.copyOf(Iterables.concat(asList(first), asList(rest))));
    }
    
    public Matcher<MutableReasonDetail> areNotModifiedAndDoNotEscape() {
        return new BaseMutableReasonDetailMatcher() {
            @Override
            protected boolean matchesSafely(MutableReasonDetail reasonDetail) {
                if (reasonDetail.codeLocation() instanceof FieldLocation) {
                    return reasonDetail.reason().isOneOf(MUTABLE_TYPE_TO_FIELD, ARRAY_TYPE_INHERENTLY_MUTABLE)
                            && fieldNames.contains(((FieldLocation)reasonDetail.codeLocation()).fieldName());
                } else {
                    return false;
                }
            }
            
        };
    }
}