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

package com.torodb.backend.postgresql.converters.jooq.binding;

import com.torodb.backend.converters.array.ArrayConverter;
import com.torodb.backend.converters.jooq.ArrayToJooqConverter;
import com.torodb.backend.converters.jooq.DataTypeForKv;
import com.torodb.backend.converters.jooq.KvValueConverter;
import com.torodb.kvdocument.values.KvValue;
import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.Objects;

import javax.json.JsonValue;

public class JsonbBinding<T> implements Binding<String, T> {

  private static final long serialVersionUID = 1L;

  public static <J, U extends KvValue<?>> DataTypeForKv<U> fromKvValue(Class<U> type,
      KvValueConverter<String, J, U> converter) {
    return DataTypeForKv.from(new DefaultDataType<>(null, String.class, "jsonb"), converter,
        new JsonbBinding<>(converter));
  }

  public static <U extends KvValue<?>, V extends JsonValue> DataType<U> fromKvValue(
      final Class<U> type, final ArrayConverter<V, U> arrayConverter) {
    Converter<String, U> converter = new ArrayToJooqConverter<>(type, arrayConverter);
    return new DefaultDataType<>(null, String.class, "jsonb")
        .asConvertedDataType(new JsonbBinding<U>(converter));
  }

  public static <U> DataType<U> fromType(Class<U> type, Converter<String, U> converter) {
    return new DefaultDataType<>(null, String.class, "jsonb")
        .asConvertedDataType(new JsonbBinding<U>(converter));
  }

  private final Converter<String, T> converter;

  public JsonbBinding(Converter<String, T> converter) {
    super();
    this.converter = converter;
  }

  @Override
  public Converter<String, T> converter() {
    return converter;
  }

  @Override
  public void sql(BindingSQLContext<T> ctx) throws SQLException {
    ctx.render().visit(DSL.val(ctx.convert(converter()).value())).sql("::jsonb");
  }

  @Override
  public void register(BindingRegisterContext<T> ctx) throws SQLException {
    ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
  }

  @Override
  public void set(BindingSetStatementContext<T> ctx) throws SQLException {
    ctx.statement()
        .setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
  }

  @Override
  public void set(BindingSetSQLOutputContext<T> ctx) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  @Override
  public void get(BindingGetSQLInputContext<T> ctx) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  @Override
  public void get(BindingGetResultSetContext<T> ctx) throws SQLException {
    ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
  }

  @Override
  public void get(BindingGetStatementContext<T> ctx) throws SQLException {
    ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
  }

}
