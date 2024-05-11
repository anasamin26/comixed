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

package org.comixedproject.batch.comicpages.listeners;

import static org.comixedproject.batch.comicpages.LoadPageHashesConfiguration.JOB_LOAD_PAGE_HASHES_STARTED;
import static org.comixedproject.model.messaging.batch.ProcessComicBooksStatus.*;

import lombok.extern.log4j.Log4j2;
import org.comixedproject.batch.listeners.AbstractBatchProcessListener;
import org.comixedproject.model.batch.BatchProcessDetail;
import org.comixedproject.service.comicpages.PageService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@JobScope
@Log4j2
public class LoadPageHashesJobListener extends AbstractBatchProcessListener
    implements JobExecutionListener {
  @Autowired private PageService pageService;

  @Override
  public void beforeJob(final JobExecution jobExecution) {
    this.doPublishBatchProcessDetail(BatchProcessDetail.from(jobExecution));
    final ExecutionContext context = jobExecution.getExecutionContext();
    context.putLong(JOB_LOAD_PAGE_HASHES_STARTED, System.currentTimeMillis());
  }

  @Override
  public void afterJob(final JobExecution jobExecution) {
    this.doPublishBatchProcessDetail(BatchProcessDetail.from(jobExecution));
    final ExecutionContext context = jobExecution.getExecutionContext();
    context.putLong(
        PROCESS_COMIC_BOOKS_STATUS_JOB_STARTED,
        jobExecution.getJobParameters().getLong(JOB_LOAD_PAGE_HASHES_STARTED));
    context.putString(PROCESS_COMIC_BOOKS_STATUS_BATCH_NAME, "");
    context.putString(
        PROCESS_COMIC_BOOKS_STATUS_STEP_NAME, PROCESS_COMIC_BOOKS_STEP_NAME_LOAD_PAGE_HASH);
    final long total = this.pageService.getCount();
    context.putLong(PROCESS_COMIC_BOOKS_STATUS_TOTAL_COMICS, total);
    context.putLong(PROCESS_COMIC_BOOKS_STATUS_PROCESSED_COMICS, total);
    context.putLong(PROCESS_COMIC_BOOKS_STATUS_JOB_FINISHED, System.currentTimeMillis());
    this.doPublishProcessComicBookStatus(context, false);
  }
}
