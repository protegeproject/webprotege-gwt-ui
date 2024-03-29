package edu.stanford.bmir.protege.web.shared.perspective;

import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;
import org.assertj.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ChangeRequestId_TestCase {

    @Test
    void isWellFormedProjectId_withValidUUID_returnsTrue() {
        // Given
        String validUUID = "123e4567-e89b-12d3-a456-426614174000"; 

        // When
        boolean result = ChangeRequestId.isWelFormedProjectId(validUUID);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void isWellFormedProjectId_withInvalidUUID_returnsFalse() {
        // Given
        String invalidUUID = "invalid-uuid";

        // When
        boolean result = ChangeRequestId.isWelFormedProjectId(invalidUUID);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void get_withValidUUID_returnsChangeRequestId() {
        // Given
        String validUUID = "123e4567-e89b-12d3-a456-426614174000"; 

        // When
        ChangeRequestId result = ChangeRequestId.get(validUUID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validUUID);
    }

    @Test
    void get_withInvalidUUID_throwsIllegalArgumentException() {
        // Given
        String invalidUUID = "invalid-uuid";

        // Then
        assertThatThrownBy(() -> ChangeRequestId.get(invalidUUID)).isInstanceOf(IllegalArgumentException.class)
                                                                  .hasMessageContaining("Invalid change id format");
    }

    @Test
    void getNil_returnsNilUuidChangeRequestId() {
        // When
        ChangeRequestId nil = ChangeRequestId.getNil();

        // Then
        assertThat(nil).isNotNull();
        assertThat(nil.getId()).isEqualTo(UUIDUtil.getNilUuid());
    }

    @Test
    void valueOf_withValidUUID_returnsEquivalentToGet() {
        // Given
        String validUUID = "123e4567-e89b-12d3-a456-426614174000"; 

        // When
        ChangeRequestId fromValueOf = ChangeRequestId.valueOf(validUUID);
        ChangeRequestId fromGet = ChangeRequestId.get(validUUID);

        // Then
        assertThat(fromValueOf).isEqualToComparingFieldByField(fromGet);
    }

    @Test
    void getFromNullable_withNull_returnsEmptyOptional() {
        // When
        Optional<ChangeRequestId> result = ChangeRequestId.getFromNullable(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getFromNullable_withEmptyString_returnsEmptyOptional() {
        // When
        Optional<ChangeRequestId> result = ChangeRequestId.getFromNullable("");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getFromNullable_withValidUUID_returnsNonEmptyOptional() {
        // Given
        String validUUID = "123e4567-e89b-12d3-a456-426614174000"; 

        // When
        Optional<ChangeRequestId> result = ChangeRequestId.getFromNullable(validUUID);

        // Then
        assertThat(result).isNotEmpty()
                          .hasValueSatisfying(changeRequestId -> assertThat(changeRequestId.getId()).isEqualTo(validUUID));
    }
}