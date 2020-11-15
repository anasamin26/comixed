/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2020, The ComiXed Project
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

import { createReducer, on } from '@ngrx/store';
import {
  clearComicFileSelections,
  comicFilesLoaded,
  comicFilesSent,
  loadComicFiles,
  loadComicFilesFailed,
  sendComicFiles,
  sendComicFilesFailed,
  setComicFilesSelectedState,
  setImportingComicsState
} from '@app/comic-import/actions/comic-import.actions';
import { ComicFile } from '@app/comic-import/models/comic-file';

export const COMIC_IMPORT_FEATURE_KEY = 'comic_import_state';

export interface ComicImportState {
  loading: boolean;
  files: ComicFile[];
  selections: ComicFile[];
  sending: boolean;
  importing: boolean;
}

export const initialState: ComicImportState = {
  loading: false,
  files: [],
  selections: [],
  sending: false,
  importing: false
};

export const reducer = createReducer(
  initialState,

  on(loadComicFiles, state => ({ ...state, loading: true })),
  on(comicFilesLoaded, (state, action) => ({
    ...state,
    loading: false,
    files: action.files,
    selections: []
  })),
  on(loadComicFilesFailed, state => ({
    ...state,
    loading: false,
    files: [],
    selections: []
  })),
  on(setComicFilesSelectedState, (state, action) => {
    let selections = state.selections.filter(file => {
      return action.files.every(entry => entry.id !== file.id);
    });
    if (action.selected) {
      selections = selections.concat(action.files);
    }
    return { ...state, selections };
  }),
  on(clearComicFileSelections, state => ({ ...state, selections: [] })),
  on(sendComicFiles, state => ({ ...state, sending: true })),
  on(comicFilesSent, state => ({
    ...state,
    sending: false,
    files: [],
    selections: []
  })),
  on(sendComicFilesFailed, state => ({
    ...state,
    sending: false
  })),
  on(setImportingComicsState, (state, action) => ({
    ...state,
    importing: action.importing
  }))
);
