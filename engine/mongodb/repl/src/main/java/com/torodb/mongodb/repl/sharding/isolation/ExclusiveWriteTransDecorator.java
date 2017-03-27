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

package com.torodb.mongodb.repl.sharding.isolation;

import com.torodb.core.exceptions.user.UserException;
import com.torodb.core.transaction.RollbackException;
import com.torodb.torod.ExclusiveWriteTorodTransaction;
import com.torodb.torod.TorodConnection;

public class ExclusiveWriteTransDecorator<C extends TorodConnection>
    extends WriteTransDecorator<ExclusiveWriteTorodTransaction, C>
    implements ExclusiveWriteTorodTransaction {

  public ExclusiveWriteTransDecorator(C connection, ExclusiveWriteTorodTransaction decorated) {
    super(connection, decorated);
  }

  @Override
  public void renameCollection(String fromDb, String fromCollection, String toDb,
      String toCollection) throws RollbackException, UserException {
    getDecorated().renameCollection(fromDb, fromCollection, toDb, toCollection);
  }

}
