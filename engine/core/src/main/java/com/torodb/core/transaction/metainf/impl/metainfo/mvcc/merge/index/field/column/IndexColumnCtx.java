/*
 * ToroDB
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.torodb.core.transaction.metainf.impl.metainfo.mvcc.merge.index.field.column;

import com.torodb.core.transaction.metainf.ImmutableMetaIdentifiedDocPartIndex;
import com.torodb.core.transaction.metainf.MetaDocPartIndexColumn;
import com.torodb.core.transaction.metainf.MetaElementState;
import com.torodb.core.transaction.metainf.MetaIdentifiedDocPartIndex;
import com.torodb.core.transaction.metainf.impl.metainfo.mvcc.merge.ExtendedMergeContext;

/**
 *
 */
public class IndexColumnCtx extends ExtendedMergeContext<ImmutableMetaIdentifiedDocPartIndex,
    MetaDocPartIndexColumn, MetaIdentifiedDocPartIndex> {

  public IndexColumnCtx(ImmutableMetaIdentifiedDocPartIndex commitedParent,
      MetaDocPartIndexColumn changed, MetaIdentifiedDocPartIndex uncommitedParent) {
    super(commitedParent, changed, MetaElementState.ADDED, uncommitedParent);
  }

}
