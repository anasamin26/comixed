/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2023, The ComiXed Project.
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

package org.comixedproject.service.comicbooks;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.assertTrue;

import java.util.*;
import org.apache.commons.lang.math.RandomUtils;
import org.comixedproject.model.archives.ArchiveType;
import org.comixedproject.model.comicbooks.ComicDetail;
import org.comixedproject.model.comicbooks.ComicState;
import org.comixedproject.model.comicbooks.ComicTagType;
import org.comixedproject.model.comicbooks.ComicType;
import org.comixedproject.repositories.comicbooks.ComicDetailRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class ComicDetailServiceTest {
  private static final long TEST_LAST_ID = 71765L;
  private static final int TEST_MAXIMUM = 1000;
  private static final String TEST_EMAIL = "reader@comixedproject.org";
  private static final String TEST_PUBLISHER = "THe Publisher Name";
  private static final String TEST_SERIES = "The Series Name";
  private static final String TEST_VOLUME = "2023";
  private static final ComicTagType TEST_TAG = ComicTagType.CHARACTER;
  private static final int TEST_YEAR = 2023;
  private static final int TEST_WEEK = 17;
  private static final String TEST_SEARCH_TERM = "Random text...";
  private static final String TEST_TAG_VALUE = "tag value";
  private static final int TEST_PAGE_SIZE = 10;
  private static final int TEST_PAGE_INDEX = RandomUtils.nextInt(100);
  private static final Integer TEST_COVER_YEAR = RandomUtils.nextInt(50) + 1970;
  private static final Integer TEST_COVER_MONTH = RandomUtils.nextInt(12);
  private static final ArchiveType TEST_ARCHIVE_TYPE = ArchiveType.CB7;
  private static final ComicType TEST_COMIC_TYPE = ComicType.ISSUE;
  private static final ComicState TEST_COMIC_STATE = ComicState.REMOVED;
  private static final Boolean TEST_READ_STATE = RandomUtils.nextBoolean();
  private static final Boolean TEST_UNSCRAPED_STATE = RandomUtils.nextBoolean();
  private static final String TEST_SEARCH_TEXT = "The search text";
  private static final long TEST_TOTAL_COMIC_COUNT = RandomUtils.nextLong() * 30000L;

  private final Set<Date> weeksList = new HashSet<>();
  private final List<String> sortFieldNames = new ArrayList<>();
  @InjectMocks private ComicDetailService service;
  @Mock private ComicDetailRepository comicDetailRepository;
  @Mock private List<ComicDetail> comicDetailList;
  @Mock private Set<String> publisherList;
  @Mock private Set<String> seriesList;
  @Mock private Set<String> volumeList;
  @Mock private Set<String> tagList;
  @Mock private Set<Integer> yearsList;
  @Mock private ObjectFactory<ComicDetailExampleBuilder> exampleBuilderObjectFactory;
  @Mock private ComicDetailExampleBuilder exampleBuilder;
  @Mock private Example<ComicDetail> comicDetailExample;
  @Mock private Page<ComicDetail> comicDetailListPage;
  @Mock private Set<Long> comicBookIdSet;

  @Captor private ArgumentCaptor<Pageable> pageableArgumentCaptor;
  @Captor private ArgumentCaptor<Date> startDateArgumentCaptor;
  @Captor private ArgumentCaptor<Date> endDateArgumentCaptor;
  @Captor private ArgumentCaptor<Example<ComicDetail>> exampleArgumentCaptor;
  @Captor private ArgumentCaptor<Pageable> sortArgumentCaptor;

  @Before
  public void setUp() {
    weeksList.add(new Date());
    Mockito.when(exampleBuilderObjectFactory.getObject()).thenReturn(exampleBuilder);
    Mockito.when(exampleBuilder.build()).thenReturn(comicDetailExample);
    Mockito.when(comicDetailListPage.toList()).thenReturn(comicDetailList);

    sortFieldNames.add("archive-type");
    sortFieldNames.add("comic-state");
    sortFieldNames.add("comic-type");
    sortFieldNames.add("publisher");
    sortFieldNames.add("series");
    sortFieldNames.add("volume");
    sortFieldNames.add("issue-number");
    sortFieldNames.add("added-date");
    sortFieldNames.add("cover-date");
    sortFieldNames.add("id");
  }

  @Test
  public void testLoadById() {
    Mockito.when(
            comicDetailRepository.getWithIdGreaterThan(
                Mockito.anyLong(), pageableArgumentCaptor.capture()))
        .thenReturn(comicDetailList);

    final List<ComicDetail> result = service.loadById(TEST_LAST_ID, TEST_MAXIMUM);

    assertNotNull(result);
    assertSame(comicDetailList, result);

    final Pageable pageable = pageableArgumentCaptor.getValue();
    assertNotNull(pageable);
    assertEquals(TEST_MAXIMUM, pageable.getPageSize());
    assertEquals(0, pageable.getPageNumber());

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getWithIdGreaterThan(TEST_LAST_ID, pageable);
  }

  @Test
  public void testGetAllPublishersWithUnread() {
    Mockito.when(comicDetailRepository.getAllUnreadPublishers(Mockito.anyString()))
        .thenReturn(publisherList);

    final Set<String> result = service.getAllPublishers(TEST_EMAIL, true);

    assertNotNull(result);
    assertSame(publisherList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1)).getAllUnreadPublishers(TEST_EMAIL);
  }

  @Test
  public void testGetAllPublishers() {
    Mockito.when(comicDetailRepository.getAllPublishers()).thenReturn(publisherList);

    final Set<String> result = service.getAllPublishers(TEST_EMAIL, false);

    assertNotNull(result);
    assertSame(publisherList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1)).getAllPublishers();
  }

  @Test
  public void testGetAllSeriesForPublisherWithUnread() {
    Mockito.when(
            comicDetailRepository.getAllUnreadSeriesForPublisher(
                Mockito.anyString(), Mockito.anyString()))
        .thenReturn(seriesList);

    final Set<String> result = service.getAllSeriesForPublisher(TEST_PUBLISHER, TEST_EMAIL, true);

    assertNotNull(result);
    assertSame(seriesList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllUnreadSeriesForPublisher(TEST_PUBLISHER, TEST_EMAIL);
  }

  @Test
  public void testGetAllSeriesForPublishers() {
    Mockito.when(comicDetailRepository.getAllSeriesForPublisher(Mockito.anyString()))
        .thenReturn(seriesList);

    final Set<String> result = service.getAllSeriesForPublisher(TEST_PUBLISHER, TEST_EMAIL, false);

    assertNotNull(result);
    assertSame(seriesList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllSeriesForPublisher(TEST_PUBLISHER);
  }

  @Test
  public void testGetAllVolumesForPublisherAndSeriesWithUnread() {
    Mockito.when(
            comicDetailRepository.getAllUnreadVolumesForPublisherAndSeries(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(volumeList);

    final Set<String> result =
        service.getAllVolumesForPublisherAndSeries(TEST_PUBLISHER, TEST_SERIES, TEST_EMAIL, true);

    assertNotNull(result);
    assertSame(volumeList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllUnreadVolumesForPublisherAndSeries(TEST_PUBLISHER, TEST_SERIES, TEST_EMAIL);
  }

  @Test
  public void testGetAllVolumesForPublisherAndSeries() {
    Mockito.when(
            comicDetailRepository.getAllVolumesForPublisherAndSeries(
                Mockito.anyString(), Mockito.anyString()))
        .thenReturn(volumeList);

    final Set<String> result =
        service.getAllVolumesForPublisherAndSeries(TEST_PUBLISHER, TEST_SERIES, TEST_EMAIL, false);

    assertNotNull(result);
    assertSame(volumeList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllVolumesForPublisherAndSeries(TEST_PUBLISHER, TEST_SERIES);
  }

  @Test
  public void testGetAllSeries() {
    Mockito.when(comicDetailRepository.getAllSeries()).thenReturn(seriesList);

    final Set<String> result = service.getAllSeries();

    assertNotNull(result);
    assertSame(seriesList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1)).getAllSeries();
  }

  @Test
  public void testGetAllSeriesAsTopLevelWithUnread() {
    Mockito.when(comicDetailRepository.getAllUnreadSeries(Mockito.anyString()))
        .thenReturn(volumeList);

    final Set<String> result = service.getAllSeries(TEST_EMAIL, true);

    assertNotNull(result);
    assertSame(volumeList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1)).getAllUnreadSeries(TEST_EMAIL);
  }

  @Test
  public void testGetAllSeriesAsTopLevel() {
    Mockito.when(comicDetailRepository.getAllSeries()).thenReturn(volumeList);

    final Set<String> result = service.getAllSeries(TEST_EMAIL, false);

    assertNotNull(result);
    assertSame(volumeList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1)).getAllSeries();
  }

  @Test
  public void testGetAllPublishersForSeriesWithUnread() {
    Mockito.when(
            comicDetailRepository.getAllUnreadPublishersForSeries(
                Mockito.anyString(), Mockito.anyString()))
        .thenReturn(publisherList);

    final Set<String> result = service.getAllPublishersForSeries(TEST_SERIES, TEST_EMAIL, true);

    assertNotNull(result);
    assertSame(publisherList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllUnreadPublishersForSeries(TEST_SERIES, TEST_EMAIL);
  }

  @Test
  public void testGetAllPublishersForSeries() {
    Mockito.when(comicDetailRepository.getAllPublishersForSeries(Mockito.anyString()))
        .thenReturn(publisherList);

    final Set<String> result = service.getAllPublishersForSeries(TEST_SERIES, TEST_EMAIL, false);

    assertNotNull(result);
    assertSame(publisherList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1)).getAllPublishersForSeries(TEST_SERIES);
  }

  @Test
  public void testGetAllComicBooksForPublisherAndSeriesAndVolumeWithUnread() {
    Mockito.when(
            comicDetailRepository.getAllUnreadForPublisherAndSeriesAndVolume(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(comicDetailList);

    final List<ComicDetail> result =
        service.getAllComicBooksForPublisherAndSeriesAndVolume(
            TEST_PUBLISHER, TEST_SERIES, TEST_VOLUME, TEST_EMAIL, true);

    assertNotNull(result);
    assertSame(comicDetailList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllUnreadForPublisherAndSeriesAndVolume(
            TEST_PUBLISHER, TEST_SERIES, TEST_VOLUME, TEST_EMAIL);
  }

  @Test
  public void testGetAllComicBooksForPublisherAndSeriesAndVolume() {
    Mockito.when(
            comicDetailRepository.getAllForPublisherAndSeriesAndVolume(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(comicDetailList);

    final List<ComicDetail> result =
        service.getAllComicBooksForPublisherAndSeriesAndVolume(
            TEST_PUBLISHER, TEST_SERIES, TEST_VOLUME, TEST_EMAIL, false);

    assertNotNull(result);
    assertSame(comicDetailList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllForPublisherAndSeriesAndVolume(TEST_PUBLISHER, TEST_SERIES, TEST_VOLUME);
  }

  @Test
  public void testGetAllValuesForTagsWithUnread() {
    Mockito.when(
            comicDetailRepository.getAllUnreadValuesForTagType(
                Mockito.any(ComicTagType.class), Mockito.anyString()))
        .thenReturn(tagList);

    final Set<String> result = service.getAllValuesForTag(TEST_TAG, TEST_EMAIL, true);

    assertNotNull(result);
    assertSame(tagList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllUnreadValuesForTagType(TEST_TAG, TEST_EMAIL);
  }

  @Test
  public void testGetAllValuesForTags() {
    Mockito.when(comicDetailRepository.getAllValuesForTagType(Mockito.any(ComicTagType.class)))
        .thenReturn(tagList);

    final Set<String> result = service.getAllValuesForTag(TEST_TAG, TEST_EMAIL, false);

    assertNotNull(result);
    assertSame(tagList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1)).getAllValuesForTagType(TEST_TAG);
  }

  @Test
  public void testGetAllYearsWithUnread() {
    Mockito.when(comicDetailRepository.getAllUnreadYears(Mockito.anyString()))
        .thenReturn(yearsList);

    final Set<Integer> result = service.getAllYears(TEST_EMAIL, true);

    assertNotNull(result);
    assertSame(yearsList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1)).getAllUnreadYears(TEST_EMAIL);
  }

  @Test
  public void testGetAllYears() {
    Mockito.when(comicDetailRepository.getAllYears()).thenReturn(yearsList);

    final Set<Integer> result = service.getAllYears(TEST_EMAIL, false);

    assertNotNull(result);
    assertSame(yearsList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1)).getAllYears();
  }

  @Test
  public void testGetAllWeeksForYearWithUnread() {
    Mockito.when(
            comicDetailRepository.getAllUnreadWeeksForYear(Mockito.anyInt(), Mockito.anyString()))
        .thenReturn(weeksList);

    final Set<Integer> result = service.getAllWeeksForYear(TEST_YEAR, TEST_EMAIL, true);

    assertNotNull(result);
    assertFalse(result.isEmpty());

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllUnreadWeeksForYear(TEST_YEAR, TEST_EMAIL);
  }

  @Test
  public void testGetAllWeeksForYear() {
    Mockito.when(comicDetailRepository.getAllWeeksForYear(Mockito.anyInt())).thenReturn(weeksList);

    final Set<Integer> result = service.getAllWeeksForYear(TEST_YEAR, TEST_EMAIL, false);

    assertNotNull(result);
    assertFalse(result.isEmpty());

    Mockito.verify(comicDetailRepository, Mockito.times(1)).getAllWeeksForYear(TEST_YEAR);
  }

  @Test
  public void testGetComicsForYearAndWeekWithUnread() {
    Mockito.when(
            comicDetailRepository.getAllUnreadForYearAndWeek(
                startDateArgumentCaptor.capture(),
                endDateArgumentCaptor.capture(),
                Mockito.anyString()))
        .thenReturn(comicDetailList);

    final List<ComicDetail> result =
        service.getComicsForYearAndWeek(TEST_YEAR, TEST_WEEK, TEST_EMAIL, true);

    assertNotNull(result);
    assertSame(comicDetailList, result);

    final Date startDate = startDateArgumentCaptor.getValue();
    final Date endDate = endDateArgumentCaptor.getValue();
    assertTrue(endDate.after(startDate));

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllUnreadForYearAndWeek(startDate, endDate, TEST_EMAIL);
  }

  @Test
  public void testGetComicsForYearAndWeek() {
    Mockito.when(
            comicDetailRepository.getAllForYearAndWeek(
                startDateArgumentCaptor.capture(), endDateArgumentCaptor.capture()))
        .thenReturn(comicDetailList);

    final List<ComicDetail> result =
        service.getComicsForYearAndWeek(TEST_YEAR, TEST_WEEK, TEST_EMAIL, false);

    assertNotNull(result);
    assertSame(comicDetailList, result);

    final Date startDate = startDateArgumentCaptor.getValue();
    final Date endDate = endDateArgumentCaptor.getValue();
    assertTrue(endDate.after(startDate));

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllForYearAndWeek(startDate, endDate);
  }

  @Test
  public void testGetComicsForSearchTerm() {
    Mockito.when(comicDetailRepository.getForSearchTerm(Mockito.anyString()))
        .thenReturn(comicDetailList);

    final List<ComicDetail> result = service.getComicForSearchTerm(TEST_SEARCH_TERM);

    assertNotNull(result);
    assertSame(comicDetailList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1)).getForSearchTerm(TEST_SEARCH_TERM);
  }

  @Test
  public void testGetComicsForTagWithUnread() {
    Mockito.when(
            comicDetailRepository.getAllUnreadComicsForTagType(
                Mockito.any(ComicTagType.class), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(comicDetailList);

    final List<ComicDetail> result =
        service.getAllComicsForTag(TEST_TAG, TEST_TAG_VALUE, TEST_EMAIL, true);

    assertNotNull(result);
    assertSame(comicDetailList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllUnreadComicsForTagType(TEST_TAG, TEST_TAG_VALUE, TEST_EMAIL);
  }

  @Test
  public void testGetComicsForTag() {
    Mockito.when(
            comicDetailRepository.getAllComicsForTagType(
                Mockito.any(ComicTagType.class), Mockito.anyString()))
        .thenReturn(comicDetailList);

    final List<ComicDetail> result =
        service.getAllComicsForTag(TEST_TAG, TEST_TAG_VALUE, TEST_EMAIL, false);

    assertNotNull(result);
    assertSame(comicDetailList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1))
        .getAllComicsForTagType(TEST_TAG, TEST_TAG_VALUE);
  }

  @Test
  public void testLoadComicDetailsSortDescending() {
    sortFieldNames.forEach(
        sortField -> {
          Mockito.when(
                  comicDetailRepository.findAll(
                      exampleArgumentCaptor.capture(), sortArgumentCaptor.capture()))
              .thenReturn(comicDetailListPage);

          final List<ComicDetail> result =
              service.loadComicDetailList(
                  TEST_PAGE_SIZE,
                  TEST_PAGE_INDEX,
                  TEST_COVER_YEAR,
                  TEST_COVER_MONTH,
                  TEST_ARCHIVE_TYPE,
                  TEST_COMIC_TYPE,
                  TEST_COMIC_STATE,
                  TEST_READ_STATE,
                  TEST_UNSCRAPED_STATE,
                  TEST_SEARCH_TEXT,
                  sortField,
                  "desc");

          assertNotNull(result);
          assertSame(comicDetailList, result);

          final Example<ComicDetail> example = exampleArgumentCaptor.getValue();
          final Pageable sort = sortArgumentCaptor.getValue();

          assertTrue(sort.getSort().stream().toList().get(0).isDescending());
          assertEquals(TEST_PAGE_SIZE, sort.getPageSize());
          assertEquals(TEST_PAGE_INDEX, sort.getPageNumber());

          Mockito.verify(comicDetailRepository, Mockito.times(1)).findAll(example, sort);
          Mockito.reset(comicDetailRepository);
        });

    Mockito.verify(exampleBuilder, Mockito.times(sortFieldNames.size())).build();
  }

  @Test
  public void testLoadComicDetailsAscendingSorts() {
    sortFieldNames.forEach(
        sortField -> {
          Mockito.when(
                  comicDetailRepository.findAll(
                      exampleArgumentCaptor.capture(), sortArgumentCaptor.capture()))
              .thenReturn(comicDetailListPage);

          final List<ComicDetail> result =
              service.loadComicDetailList(
                  TEST_PAGE_SIZE,
                  TEST_PAGE_INDEX,
                  TEST_COVER_YEAR,
                  TEST_COVER_MONTH,
                  TEST_ARCHIVE_TYPE,
                  TEST_COMIC_TYPE,
                  TEST_COMIC_STATE,
                  TEST_READ_STATE,
                  TEST_UNSCRAPED_STATE,
                  TEST_SEARCH_TEXT,
                  sortField,
                  "asc");

          assertNotNull(result);
          assertSame(comicDetailList, result);

          final Example<ComicDetail> example = exampleArgumentCaptor.getValue();
          final Pageable sort = sortArgumentCaptor.getValue();

          assertTrue(sort.getSort().stream().toList().get(0).isAscending());
          assertEquals(TEST_PAGE_SIZE, sort.getPageSize());
          assertEquals(TEST_PAGE_INDEX, sort.getPageNumber());

          Mockito.verify(comicDetailRepository, Mockito.times(1)).findAll(example, sort);
          Mockito.reset(comicDetailRepository);
        });

    Mockito.verify(exampleBuilder, Mockito.times(sortFieldNames.size())).build();
  }

  @Test
  public void testGetFilterCount() {
    Mockito.when(comicDetailRepository.count(exampleArgumentCaptor.capture()))
        .thenReturn(TEST_TOTAL_COMIC_COUNT);

    final long result =
        service.getFilterCount(
            TEST_COVER_YEAR,
            TEST_COVER_MONTH,
            TEST_ARCHIVE_TYPE,
            TEST_COMIC_TYPE,
            TEST_COMIC_STATE,
            TEST_READ_STATE,
            TEST_UNSCRAPED_STATE,
            TEST_SEARCH_TEXT);

    assertEquals(TEST_TOTAL_COMIC_COUNT, result);

    final Example<ComicDetail> example = exampleArgumentCaptor.getValue();

    Mockito.verify(exampleBuilder, Mockito.times(1)).build();
    Mockito.verify(comicDetailRepository, Mockito.times(1)).count(comicDetailExample);
  }

  @Test
  public void testLoadComicDetailsById() {
    Mockito.when(comicDetailRepository.findAllById(Mockito.anySet())).thenReturn(comicDetailList);

    final List<ComicDetail> result = service.loadComicDetailListById(comicBookIdSet);

    assertNotNull(result);
    assertSame(comicDetailList, result);

    Mockito.verify(comicDetailRepository, Mockito.times(1)).findAllById(comicBookIdSet);
  }
}
