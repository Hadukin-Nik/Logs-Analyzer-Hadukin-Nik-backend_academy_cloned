package backend.academy.logsAnalyzer.services.initializer;

import java.io.ByteArrayOutputStream;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ViewsInitImplTest {
    private ByteArrayOutputStream byteArrayOutputStream;

    @BeforeEach
    void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Test
    void happy_path() {
        //Arrange
        Set<String> expected = Set.of("markdown", "adoc");

        //Act
        ViewsInitImpl viewsInitImpl = new ViewsInitImpl(byteArrayOutputStream);

        //Assert
        Assertions.assertEquals(expected, viewsInitImpl.getViews().keySet());
    }

    @Test
    void views_not_null() {
        //Arrange
        Set<String> expected = Set.of("markdown", "adoc");

        //Act
        ViewsInitImpl viewsInitImpl = new ViewsInitImpl(byteArrayOutputStream);

        //Assert
        boolean isAnyNull = expected.stream().anyMatch(s -> viewsInitImpl.getViews().getOrDefault(s, null) == null);
        Assertions.assertFalse(isAnyNull);
    }
}
