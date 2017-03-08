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

package com.torodb.backend.postgresql.converters;

import com.torodb.backend.postgresql.converters.util.CopyEscaper;
import com.torodb.common.util.HexUtils;
import com.torodb.common.util.TextEscaper;
import com.torodb.kvdocument.values.*;

/**
 *
 */
public class PostgreSqlValueToCopyConverter implements KvValueVisitor<Void, StringBuilder> {

  public static final PostgreSqlValueToCopyConverter INSTANCE =
      new PostgreSqlValueToCopyConverter();

  private static final TextEscaper ESCAPER = CopyEscaper.INSTANCE;

  PostgreSqlValueToCopyConverter() {
  }

  @Override
  public Void visit(KvBoolean value, StringBuilder arg) {
    if (value.getValue()) {
      arg.append("true");
    } else {
      arg.append("false");
    }
    return null;
  }

  @Override
  public Void visit(KvNull value, StringBuilder arg) {
    arg.append("true");
    return null;
  }

  @Override
  public Void visit(KvArray value, StringBuilder arg) {
    throw new UnsupportedOperationException("Ouch this should not occur");
  }

  @Override
  public Void visit(KvInteger value, StringBuilder arg) {
    arg.append(value.getValue().toString());
    return null;
  }

  @Override
  public Void visit(KvLong value, StringBuilder arg) {
    arg.append(value.getValue().toString());
    return null;
  }

  @Override
  public Void visit(KvDouble value, StringBuilder arg) {
    arg.append(value.getValue().toString());
    return null;
  }

  @Override
  public Void visit(KvString value, StringBuilder arg) {
    ESCAPER.appendEscaped(arg, value.getValue());
    return null;
  }

  @Override
  public Void visit(KvMongoObjectId value, StringBuilder arg) {
    arg.append("\\\\x");

    HexUtils.bytes2Hex(value.getArrayValue(), arg);

    return null;
  }

  @Override
  public Void visit(KvBinary value, StringBuilder arg) {
    arg.append("\\\\x");

    HexUtils.bytes2Hex(value.getByteSource().read(), arg);

    return null;
  }

  @Override
  public Void visit(KvInstant value, StringBuilder arg) {
    arg.append('\'')
        //this prints the value on ISO-8601, which is the recommended format on PostgreSQL
        .append(value.getValue().toString())
        .append('\'');
    return null;
  }

  @Override
  public Void visit(KvDate value, StringBuilder arg) {
    arg.append('\'')
        //this prints the value on ISO-8601, which is the recommended format on PostgreSQL
        .append(value.getValue().toString())
        .append('\'');
    return null;
  }

  @Override
  public Void visit(KvTime value, StringBuilder arg) {
    arg.append('\'')
        //this prints the value on ISO-8601, which is the recommended format on PostgreSQL
        .append(value.getValue().toString())
        .append('\'');
    return null;
  }

  @Override
  public Void visit(KvMongoTimestamp value, StringBuilder arg) {
    arg.append('(').append(value.getSecondsSinceEpoch()).append(',').append(value.getOrdinal())
        .append(')');
    return null;
  }

  @Override
  public Void visit(KvDocument value, StringBuilder arg) {
    throw new UnsupportedOperationException("Ouch this should not occur");
  }

  @Override
  public Void visit(KvDecimal128 value, StringBuilder arg) {
    arg.append(value.getValue().toPlainString());
    return null;
  }

  @Override
  public Void visit(KvJavascript value, StringBuilder arg) {
    ESCAPER.appendEscaped(arg, value.getValue());
    return null;
  }

  @Override
  public Void visit(KvJavascriptWithScope value, StringBuilder arg) {
    ESCAPER.appendEscaped(arg, value.getJs());
    arg.append(value.getScopeAsString());
    return null;
  }

  @Override
  public Void visit(KvMinKey value, StringBuilder arg) {
    arg.append("false");
    return null;
  }

  @Override
  public Void visit(KvMaxKey value, StringBuilder arg) {
    arg.append("true");
    return null;
  }

  @Override
  public Void visit(KvUndefined value, StringBuilder arg) {
    arg.append("true");
    return null;
  }

  @Override
  public Void visit(KvMongoRegex value, StringBuilder arg) {
    ESCAPER.appendEscaped(arg, value.getPattern());
    ESCAPER.appendEscaped(arg, ",");
    ESCAPER.appendEscaped(arg, value.getOptionsAsText());
    return null;
  }

  @Override
  public Void visit(KvMongoDbPointer value, StringBuilder arg) {
    arg.append('(');
    ESCAPER.appendEscaped(arg, value.getNamespace());
    arg.append(',');
    visit(value.getId(),arg);
    arg.append(')');
    return null;
  }

  @Override
  public Void visit(KvDeprecated value, StringBuilder arg) {
    arg.append(value.toString());
    return null;
  }
}
