package org.glytching.sandbox.powermock.directorystream;

import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DirectoryStreamReader.class})
public class DirectoryStreamTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void canReadFilesUsingDirectoryStream() throws IOException {
        PowerMockito.mockStatic(Files.class);

        Path directory = mock(Path.class);
        DirectoryStream<Path> expected = mock(DirectoryStream.class);
        when(Files.newDirectoryStream(any(Path.class))).thenReturn(expected);

        File fileOne = folder.newFile();
        File fileTwo = folder.newFile();
        Iterator<Path> directoryIterator = Lists.newArrayList(Paths.get(fileOne.toURI()),
                Paths.get(fileTwo.toURI())).iterator();

        when(expected.iterator()).thenReturn(directoryIterator);

        when(Files.isRegularFile(any(Path.class))).thenReturn(true);
        when(Files.readAllBytes(any(Path.class))).thenReturn("fileOneContents".getBytes()).thenReturn("fileTwoContents".getBytes());

        Map<String, String> fileContentsByName = new DirectoryStreamReader().read(directory);

        assertEquals(2, fileContentsByName.size());
        assertTrue(fileContentsByName.containsKey(fileOne.getName()));
        assertEquals("fileOneContents", fileContentsByName.get(fileOne.getName()));
        assertTrue(fileContentsByName.containsKey(fileTwo.getName()));
        assertEquals("fileTwoContents", fileContentsByName.get(fileTwo.getName()));
    }
}
