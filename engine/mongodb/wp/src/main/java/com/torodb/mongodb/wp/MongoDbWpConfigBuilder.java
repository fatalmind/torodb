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

package com.torodb.mongodb.wp;

import com.torodb.core.modules.BundleConfig;
import com.torodb.mongodb.core.MongoDbCoreBundle;


public class MongoDbWpConfigBuilder {

  private MongoDbCoreBundle coreBundle;
  private int port;
  private final BundleConfig genericBundle;

  public MongoDbWpConfigBuilder(BundleConfig genericBundle) {
    this.genericBundle = genericBundle;
  }

  public MongoDbWpConfigBuilder setCoreBundle(MongoDbCoreBundle coreBundle) {
    this.coreBundle = coreBundle;
    return this;
  }

  public MongoDbWpConfigBuilder setPort(int port) {
    this.port = port;
    return this;
  }

  public MongoDbWpConfig build() {
    return new MongoDbWpConfig(coreBundle, port, genericBundle);
  }

}
