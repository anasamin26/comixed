/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2017, The ComiXed Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses>
 */

package org.comixedproject.task;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.comixedproject.model.comic.Comic;
import org.comixedproject.model.lists.ReadingList;
import org.comixedproject.service.lists.ReadingListService;
import org.comixedproject.state.comic.ComicEvent;
import org.comixedproject.state.comic.ComicStateHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;

@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUtils.class)
@SpringBootTest
public class MarkComicForRemovalTaskTest {
  private static final int TEST_READING_LIST_COUNT = 25;

  @InjectMocks private MarkComicForRemovalTask task;
  @Mock private ReadingListService readingListService;
  @Mock private ComicStateHandler comicStateHandler;
  @Mock private Comic comic;
  @Mock private ReadingList readingList;

  private List<Long> comicIds = new ArrayList<>();

  @Before
  public void setUp() {
    task.setComic(comic);
  }

  @Test
  public void testCreateDescription() {
    assertNotNull(task.createDescription());
  }

  @Test
  public void testStartTask() throws TaskException {
    Set<ReadingList> readingLists = new HashSet<>();
    for (int index = 0; index < TEST_READING_LIST_COUNT; index++) {
      ReadingList list = new ReadingList();
      list.setName("List" + index);
      list.setSummary("List" + index);
      readingLists.add(list);
      readingList.getComics().add(comic);
    }
    Mockito.when(comic.getReadingLists()).thenReturn(readingLists);

    task.setComic(comic);

    task.startTask();

    assertTrue(readingLists.isEmpty());

    Mockito.verify(readingListService, Mockito.times(TEST_READING_LIST_COUNT))
        .saveReadingList(Mockito.any(ReadingList.class));
    Mockito.verify(comicStateHandler, Mockito.times(1))
        .fireEvent(comic, ComicEvent.markedForRemoval);
  }
}
