package backend.academy.logsAnalyzer.services.converter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ToStringConvertorTest {

    @Test
    void string_from_date() {


        //Arrange
        String expected = "-999999999-01-01T00:00:00";
        OffsetDateTime date = OffsetDateTime.MIN;

        //Act
        ToStringConvertor convertor = new ToStringConvertor();

        //Assert
        Assertions.assertEquals(expected, convertor.convert(date));
    }

    @Test
    void string_from_long_first() {
        //Arrange
        String expected = "1_123_999";

        //Act
        ToStringConvertor convertor = new ToStringConvertor();

        //Assert
        Assertions.assertEquals(expected, convertor.convert(1123999L));
    }

    @Test
    void string_from_long_second() {
        //Arrange
        String expected = "999";

        //Act
        ToStringConvertor convertor = new ToStringConvertor();

        //Assert
        Assertions.assertEquals(expected, convertor.convert(999L));
    }

    @Test
    void string_from_strings() {
        //Arrange
        String expected = "'1', '2', '3'";
        List<String> mas = List.of("1", "2", "3");
        //Act
        ToStringConvertor convertor = new ToStringConvertor();
        //Assert
        Assertions.assertEquals(expected, convertor.convert(mas));
    }
}
