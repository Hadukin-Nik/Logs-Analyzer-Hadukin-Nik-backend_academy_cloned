package backend.academy.logsAnalyzer.services.converter;

import backend.academy.logsAnalyzer.models.util.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import java.util.TimeZone;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LogConverterTest {

    @Test
    void convert() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
        //Arrange
        String expected = "Log[remoteAddr=31.22.86.126, remoteUser=-, date=2015-05-17T10:05:38+00:10, httpMethod=GET, request=/downloads/product_1, httpVersion=HTTP/1.1, status=404, bodyBytesSent=335, httpReferer=-, userAgent=DEBIANAPT-HTTP/1.3(0.8.16~EXP12UBUNTU10.16)]";

        //Act
        LogConverter converter = new LogConverter();
        Log answer = converter.convert(
            "31.22.86.126 - - [17/May/2015:10:05:38 +0010] \"GET /downloads/product_1 HTTP/1.1\" 404 335 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.16)\"");

        //Assert
        String check = answer.toString().replaceAll("\r", "").replaceAll("\n", "");
        Assertions.assertEquals(expected, check);
    }
}
