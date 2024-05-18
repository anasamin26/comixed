/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2024, The ComiXed Project
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

package org.comixedproject.batch.comicbooks.listeners;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.comixedproject.model.messaging.batch.ProcessComicBooksStatus.MOVE_COMIC_FILES_STEP;

import org.comixedproject.messaging.PublishingException;
import org.comixedproject.messaging.batch.PublishBatchProcessDetailUpdateAction;
import org.comixedproject.messaging.comicbooks.PublishProcessComicBooksStatusAction;
import org.comixedproject.model.batch.BatchProcessDetail;
import org.comixedproject.model.messaging.batch.ProcessComicBooksStatus;
import org.comixedproject.service.comicbooks.ComicBookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;

@RunWith(MockitoJUnitRunner.class)
public class MoveComicFilesChunkListenerTest {
  private static final long TEST_TOTAL_COMICS = 717L;
  private static final long TEST_REMAINING_COMICS = 129L;
  private static final String TEST_JOB_NAME = "The job name";

  @InjectMocks private MoveComicFilesChunkListener listener;
  @Mock private ComicBookService comicBookService;
  @Mock private ChunkContext chunkContext;
  @Mock private StepContext stepContext;
  @Mock private StepExecution stepExecution;
  @Mock private JobInstance jobInstance;
  @Mock private JobExecution jobExecution;
  @Mock private PublishProcessComicBooksStatusAction publishProcessComicBooksStatusAction;
  @Mock private PublishBatchProcessDetailUpdateAction publishBatchProcessDetailUpdateAction;
  @Mock private JobParameters jobParameters;

  @Captor ArgumentCaptor<ProcessComicBooksStatus> processComicStatusArgumentCaptor;
  @Captor ArgumentCaptor<BatchProcessDetail> batchProcessDetailArgumentCaptor;

  @Before
  public void setUp() throws PublishingException {
    Mockito.when(jobExecution.getJobParameters()).thenReturn(jobParameters);
    Mockito.when(jobInstance.getJobName()).thenReturn(TEST_JOB_NAME);
    Mockito.when(jobExecution.getJobInstance()).thenReturn(jobInstance);
    Mockito.when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
    Mockito.when(jobExecution.getExitStatus()).thenReturn(ExitStatus.COMPLETED);
    Mockito.when(comicBookService.getComicBookCount()).thenReturn(TEST_TOTAL_COMICS);
    Mockito.when(comicBookService.findComicsToBeMovedCount()).thenReturn(TEST_REMAINING_COMICS);

    Mockito.when(chunkContext.getStepContext()).thenReturn(stepContext);
    Mockito.when(stepExecution.getJobExecution()).thenReturn(jobExecution);
    Mockito.when(stepContext.getStepExecution()).thenReturn(stepExecution);
    Mockito.doNothing()
        .when(publishProcessComicBooksStatusAction)
        .publish(processComicStatusArgumentCaptor.capture());
    Mockito.doNothing()
        .when(publishBatchProcessDetailUpdateAction)
        .publish(batchProcessDetailArgumentCaptor.capture());
  }

  @Test
  public void testBeforeChunk() throws PublishingException {
    listener.beforeChunk(chunkContext);

    final ProcessComicBooksStatus status = processComicStatusArgumentCaptor.getValue();

    assertNotNull(status);
    assertTrue(status.isActive());
    assertEquals(MOVE_COMIC_FILES_STEP, status.getStepName());
    assertEquals(TEST_TOTAL_COMICS, status.getTotal());
    assertEquals(TEST_TOTAL_COMICS - TEST_REMAINING_COMICS, status.getProcessed());

    Mockito.verify(publishProcessComicBooksStatusAction, Mockito.times(1)).publish(status);
  }

  @Test
  public void testAfterChunk() throws PublishingException {
    listener.afterChunk(chunkContext);

    final ProcessComicBooksStatus status = processComicStatusArgumentCaptor.getValue();

    assertNotNull(status);
    assertTrue(status.isActive());
    assertEquals(MOVE_COMIC_FILES_STEP, status.getStepName());
    assertEquals(TEST_TOTAL_COMICS, status.getTotal());
    assertEquals(TEST_TOTAL_COMICS - TEST_REMAINING_COMICS, status.getProcessed());

    Mockito.verify(publishProcessComicBooksStatusAction, Mockito.times(1)).publish(status);
  }

  @Test
  public void testAfterChunkError() throws PublishingException {
    listener.afterChunkError(chunkContext);

    final ProcessComicBooksStatus status = processComicStatusArgumentCaptor.getValue();

    assertNotNull(status);
    assertTrue(status.isActive());
    assertEquals(MOVE_COMIC_FILES_STEP, status.getStepName());
    assertEquals(TEST_TOTAL_COMICS, status.getTotal());
    assertEquals(TEST_TOTAL_COMICS - TEST_REMAINING_COMICS, status.getProcessed());

    Mockito.verify(publishProcessComicBooksStatusAction, Mockito.times(1)).publish(status);
  }

  @Test
  public void testAfterChunkPublishingException() throws PublishingException {
    listener.beforeChunk(chunkContext);

    final ProcessComicBooksStatus status = processComicStatusArgumentCaptor.getValue();

    assertNotNull(status);
    assertTrue(status.isActive());
    assertEquals(MOVE_COMIC_FILES_STEP, status.getStepName());
    assertEquals(TEST_TOTAL_COMICS, status.getTotal());
    assertEquals(TEST_TOTAL_COMICS - TEST_REMAINING_COMICS, status.getProcessed());

    Mockito.verify(publishProcessComicBooksStatusAction, Mockito.times(1)).publish(status);
  }
}
