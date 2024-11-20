package backend.academy.logsAnalyzer.services.util;

import backend.academy.logsAnalyzer.models.util.Log;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Set;
import backend.academy.logsAnalyzer.services.logFieldsAccessor.LogReflection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LogReflectionTest {

    @Test
    void get_field_names() {
        //Arrange
        Set<String> expected =
            Set.of("DATE", "BODYBYTESSENT", "USERAGENT", "STATUS", "HTTPMETHOD", "HTTPREFERER", "REQUEST", "REMOTEADDR",
                "REMOTEUSER", "HTTPVERSION");
        //Act
        LogReflection logReflection = new LogReflection();
        Set<String> answer = logReflection.getFieldNames();
        //Assert
        Assertions.assertEquals(expected, answer);
    }

    @Test
    void get_field_value_as_string() {
        //Arrange
        Log log = new Log("-from", "-to", OffsetDateTime.MIN, "GET"
            , "/codesOfNuclearWeapon.txt", "HTTP/1.1"
            , (short) 404, 123L, "-", "Mozilla");

        String expected = "404";

        //Act
        LogReflection logReflection = new LogReflection();

        String answer = logReflection.getFieldValueAsString(log, "STatUS");

        Assertions.assertEquals(expected, answer);
    }
}
