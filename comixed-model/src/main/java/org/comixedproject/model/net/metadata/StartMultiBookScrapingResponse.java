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

package org.comixedproject.model.net.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.comixedproject.model.comicbooks.ComicBook;
import org.comixedproject.views.View;

/**
 * <code>StartMultiBookScrapingResponse</code> represents the payload for the response when starting
 * multi-book comic scraping.
 *
 * @author Darryl L. Pierce
 */
@AllArgsConstructor
public class StartMultiBookScrapingResponse {
  @JsonProperty("pageSize")
  @JsonView(View.ComicListView.class)
  @Getter
  private final int pageSize;

  @JsonProperty("pageNumber")
  @JsonView(View.ComicListView.class)
  @Getter
  private final int pageNumber;

  @JsonProperty("totalComics")
  @JsonView(View.ComicListView.class)
  @Getter
  private final int totalComics;

  @JsonProperty("comicBooks")
  @JsonView(View.ComicListView.class)
  @Getter
  private List<ComicBook> comicBooks;
}
