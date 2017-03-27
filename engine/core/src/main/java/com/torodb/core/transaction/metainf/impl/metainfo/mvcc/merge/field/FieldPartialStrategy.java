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

package com.torodb.core.transaction.metainf.impl.metainfo.mvcc.merge.field;

import com.torodb.core.transaction.metainf.ImmutableMetaDocPart;
import com.torodb.core.transaction.metainf.MetaField;
import com.torodb.core.transaction.metainf.impl.metainfo.mvcc.merge.PartialMergeStrategy;

import javax.annotation.Nullable;


/**
 *
 */
interface FieldPartialStrategy extends PartialMergeStrategy<ImmutableMetaDocPart,
    MetaField, ImmutableMetaDocPart.Builder, FieldContext> {

  @Nullable
  default MetaField getCommitedById(FieldContext context) {
    return context.getCommitedParent()
        .getMetaFieldByIdentifier(context.getChanged().getIdentifier());
  }

  @Nullable
  default MetaField getCommitedByNameAndType(FieldContext context) {
    MetaField changed = context.getChanged();
    return context.getCommitedParent().getMetaFieldByNameAndType(
        changed.getName(),
        changed.getType()
    );
  }

}
