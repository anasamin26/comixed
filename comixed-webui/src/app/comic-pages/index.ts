/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2021, The ComiXed Project
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

import { Params } from '@angular/router';
import { routerReducer, RouterReducerState } from '@ngrx/router-store';
import {
  BLOCKED_HASH_LIST_FEATURE_KEY,
  BlockedHashListState,
  reducer as blockedHashListReducer
} from './reducers/blocked-hash-list.reducer';
import { ActionReducerMap } from '@ngrx/store';
import {
  BLOCKED_PAGE_DETAIL_FEATURE_KEY,
  BlockedPageDetailState,
  reducer as blockedPageDetailReducer
} from '@app/comic-pages/reducers/blocked-page-detail.reducer';
import {
  BLOCK_PAGE_FEATURE_KEY,
  BlockPageState,
  reducer as blockPageReducer
} from '@app/comic-pages/reducers/block-page.reducer';
import {
  DOWNLOAD_BLOCKED_PAGES_FEATURE_KEY,
  DownloadBlockedPagesState,
  reducer as downloadBlockedPagesReducer
} from '@app/comic-pages/reducers/download-blocked-pages.reducer';
import {
  reducer as uploadBlockedPagesReducer,
  UPLOAD_BLOCKED_PAGES_FEATURE_KEY,
  UploadedBlockedPagesState
} from '@app/comic-pages/reducers/upload-blocked-pages.reducer';
import {
  DELETE_BLOCKED_PAGES_FEATURE_KEY,
  DeleteBlockedPagesState,
  reducer as deleteBlockedPagesReducer
} from '@app/comic-pages/reducers/delete-blocked-pages.reducer';
import {
  DELETED_PAGE_FEATURE_KEY,
  DeletedPagesState,
  reducer as deletedPageReducer
} from '@app/comic-pages/reducers/deleted-pages.reducer';

interface RouterStateUrl {
  url: string;
  params: Params;
  queryParams: Params;
}

export interface BlockedPagesModuleState {
  router: RouterReducerState<RouterStateUrl>;
  [BLOCKED_HASH_LIST_FEATURE_KEY]: BlockedHashListState;
  [BLOCKED_PAGE_DETAIL_FEATURE_KEY]: BlockedPageDetailState;
  [BLOCK_PAGE_FEATURE_KEY]: BlockPageState;
  [DOWNLOAD_BLOCKED_PAGES_FEATURE_KEY]: DownloadBlockedPagesState;
  [UPLOAD_BLOCKED_PAGES_FEATURE_KEY]: UploadedBlockedPagesState;
  [DELETE_BLOCKED_PAGES_FEATURE_KEY]: DeleteBlockedPagesState;
  [DELETED_PAGE_FEATURE_KEY]: DeletedPagesState;
}

export type ModuleState = BlockedPagesModuleState;

export const reducers: ActionReducerMap<BlockedPagesModuleState> = {
  router: routerReducer,
  [BLOCKED_HASH_LIST_FEATURE_KEY]: blockedHashListReducer,
  [BLOCKED_PAGE_DETAIL_FEATURE_KEY]: blockedPageDetailReducer,
  [BLOCK_PAGE_FEATURE_KEY]: blockPageReducer,
  [DOWNLOAD_BLOCKED_PAGES_FEATURE_KEY]: downloadBlockedPagesReducer,
  [UPLOAD_BLOCKED_PAGES_FEATURE_KEY]: uploadBlockedPagesReducer,
  [DELETE_BLOCKED_PAGES_FEATURE_KEY]: deleteBlockedPagesReducer,
  [DELETED_PAGE_FEATURE_KEY]: deletedPageReducer
};
