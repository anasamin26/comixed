/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2023, The ComiXed Project
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

package org.comixedproject.service.comicpages;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertSame;

import java.util.List;
import org.comixedproject.model.comicpages.DeletedPage;
import org.comixedproject.repositories.comicpages.PageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeletedPageServiceTest {
  @InjectMocks private DeletedPageService service;
  @Mock private PageRepository pageRepository;
  @Mock private List<DeletedPage> deletedPageList;

  @Test
  public void testLoadAll() {
    Mockito.when(pageRepository.loadAllDeletedPages()).thenReturn(deletedPageList);

    final List<DeletedPage> result = service.loadAll();

    assertNotNull(result);
    assertSame(deletedPageList, result);

    Mockito.verify(pageRepository, Mockito.times(1)).loadAllDeletedPages();
  }
}
