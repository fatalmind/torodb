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

package com.torodb.core.transaction.metainf.impl.metainfo.mvcc.merge.collection;

import com.torodb.core.transaction.metainf.ImmutableMetaCollection;
import com.torodb.core.transaction.metainf.ImmutableMetaDatabase;
import com.torodb.core.transaction.metainf.MutableMetaCollection;
import com.torodb.core.transaction.metainf.impl.metainfo.mvcc.merge.result.ExecutionResult;

/**
 * A strategy that shortcuts the merge process if the commited collection is the same as the
 * {@link MutableMetaCollection#getOrigin() origin} of the one that is being merged, which means
 * that no other transaction modified the commited collection and therefore no more checks need
 * to be done.
 */
public class ShortcutCollectionStrategy implements CollectionPartialStrategy {

  @Override
  public boolean appliesTo(ColContext context) {
    ImmutableMetaCollection origin = context.getChanged().getOrigin();
    ImmutableMetaCollection commited = context.getCommitedParent().getMetaCollectionByName(
        origin.getName()
    );

    return commited != null && commited == origin;
  }

  @Override
  public ExecutionResult<ImmutableMetaDatabase> execute(ColContext context,
      ImmutableMetaDatabase.Builder parentBuilder) throws IllegalArgumentException {
    switch (context.getChange()) {
      case ADDED:
      case MODIFIED:
        parentBuilder.put(context.getChanged().immutableCopy());
        break;
      case REMOVED:
        parentBuilder.remove(context.getChanged());
        break;
      default:
        throw new AssertionError("Unexpected change " + context.getChange());
    }

    return ExecutionResult.success();
  }

}
