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

import static org.comixedproject.model.messaging.batch.ProcessComicBooksStatus.DELETE_DESCRIPTORS_STEP;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

/**
 * <code>DeleteImportedDescriptorsListener</code> relays status while comic file descriptors that
 * have been imported are deleted.
 *
 * @author Darryl L. Pierce
 */
@Component
@StepScope
@Log4j2
public class DeleteImportedDescriptorsListener extends AbstractBatchProcessChunkListener {
  @Override
  protected String getStepName() {
    return DELETE_DESCRIPTORS_STEP;
  }

  @Override
  protected boolean isActive() {
    return this.comicFileService.getUnimportedComicFileDescriptorCount() > 0;
  }

  @Override
  protected long getProcessedElements() {
    return this.comicBookService.getComicBookCount();
  }

  @Override
  protected long getTotalElements() {
    return this.comicFileService.getUnimportedComicFileDescriptorCount()
        + this.comicBookService.getComicBookCount();
  }
}
