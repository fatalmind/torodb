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

package com.torodb.core.transaction.metainf.impl.metainfo.mvcc.merge.result;

import java.util.function.Function;

/**
 * A marker interface used to identify error factories.
 *
 * <p>Objects of this class are wrapped by {@link PrettyErrorMessageFactory} to create the real
 * error message factory that can be used to generate the error message with some context 
 * information (like the rule id).
 */
@FunctionalInterface
public interface InnerErrorMessageFactory<P>
    extends Function<ParentDescriptionFun<P>, String> {

  @Override
  public String apply(ParentDescriptionFun<P> parentDescriptionFun);

}
